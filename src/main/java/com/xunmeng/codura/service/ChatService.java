package com.xunmeng.codura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.util.concurrency.AppExecutorUtil;
import com.xunmeng.codura.constants.Constants;
import com.xunmeng.codura.constants.Languages;
import com.xunmeng.codura.constants.enums.ModeType;
import com.xunmeng.codura.net.SSEHttpClient;
import com.xunmeng.codura.net.constants.Method;
import com.xunmeng.codura.net.options.RequestOptions;
import com.xunmeng.codura.net.response.ResponseCallable;
import com.xunmeng.codura.net.resquest.StreamResquest;
import com.xunmeng.codura.llm.openai.message.Message;
import com.xunmeng.codura.llm.openai.request.StreamRequestBodyChatOpenAI;
import com.xunmeng.codura.llm.openai.response.StreamResponseChatDataItem;
import com.xunmeng.codura.pojo.ConversationMessage;
import com.xunmeng.codura.pojo.LanguageType;
import com.xunmeng.codura.setting.provider.ChatConfigProvider;
import com.xunmeng.codura.setting.provider.ChatModelProvider;
import com.xunmeng.codura.setting.state.CodeState;
import com.xunmeng.codura.setting.state.CodeStateService;
import com.xunmeng.codura.system.logs.LogExecutor;
import com.xunmeng.codura.system.logs.constans.AIUsageType;
import com.xunmeng.codura.toolwin.CodeToolWindowFactory;
import com.xunmeng.codura.toolwin.sharedchat.SharedChatPane;
import com.xunmeng.codura.toolwin.sharedchat.events.eidtor.Snippet;
import com.xunmeng.codura.utils.CompletionUtils;
import com.xunmeng.codura.utils.JsonUtils;
import okhttp3.Call;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Service
public final class ChatService {
    public static final Logger logger=Logger.getInstance(ChatService.class);
    // 补全
    private String completion="";
    // 异步回调
    private Call call;
    
    // 项目全局配置
    private CodeState codeState;
    
    private Editor editor;
    
    private TemplateService templateService;
    
    private String promptTemplate;
    
    private ScheduledExecutorService scheduler = AppExecutorUtil.createBoundedScheduledExecutorService("SMCChatScheduler", 1);

    private Future debouncer;

    // 防抖时间
    private long debounceWait=200;
    
    private boolean isGeneratingText=false;
    
    // 输入prompt
    private List<ConversationMessage> inputMessages=null;
    
    // 当前事件雷系
    private AIUsageType usageType;
    
    
    public ChatService(){
        codeState = CodeStateService.settings();
        templateService=DefaultService.getServiceByClass(TemplateService.class);
    }
    
    
    public void streamChatCompletion(List<ConversationMessage> messages){
        synchronized (ChatService.class){
            if (isGeneratingText){
                onGenerateLimit();
                return;
            }
        }
        this.completion="";
        this.promptTemplate="";
        // 标志当前对话内容为补全
        usageType=AIUsageType.CODE_QA;
        // 获取系统消息
        String systemMessage = templateService.readSystemMessageTemplate(this.promptTemplate);
        if (!StringUtils.isEmpty(systemMessage)){
            messages.add(0,new ConversationMessage(Constants.SYSTEM,systemMessage));
        }
        // 获取当前选择文本
        ApplicationManager.getApplication().invokeLater(()->{
            Snippet snippet = CompletionUtils.getSnippet();
            String selectionContext="";
            if (snippet!=null){
                selectionContext=snippet.getCode();
            }
            if(!StringUtils.isEmpty(selectionContext)){
                messages.add(new ConversationMessage(Constants.USER,selectionContext));
            }
            // 构建请求体
            inputMessages=messages;
            StreamResquest request = buildStreamRequest(messages, null);
            if (request!=null){
                debouncer=scheduler.schedule(()->{
                    SSEHttpClient sseHttpClient = new SSEHttpClient(request);
                    call=sseHttpClient.getCall();
                },debounceWait, TimeUnit.MILLISECONDS);
            }
        });
        
    }

    public void streamTemplateCompletion(String promptTemplate, AIUsageType usageType, String desc, Consumer<Call> onEnd, boolean skipMessage){
        
        synchronized (ChatService.class){
            if (isGeneratingText){
                onGenerateLimit();
                return;
            }
        }
        // todo 初始化状态栏
        // 获取当前语言
        this.completion="";
        this.promptTemplate=promptTemplate;
        this.usageType=usageType;
        
        String systemMessageTemplate = DefaultService.getServiceByClass(TemplateService.class).readSystemMessageTemplate(promptTemplate);

        ApplicationManager.getApplication().invokeLater(()->{
            SharedChatPane chatPane = CodeToolWindowFactory.getCurrentFocusChatPane();
            if (chatPane==null) return;
            Snippet snippet = CompletionUtils.getSnippet();
            if (snippet==null || snippet.getCode()==null){
                return;
            };
            String prompt=buildTemplatePrompt(promptTemplate,snippet.getLanguage(),snippet.getCode());
            if (!skipMessage){// 是否要跳过向视图端添加message，默认为fase,不跳过
                // 向视图端添加message
                ConversationMessage userMessage = new ConversationMessage(Constants.USER,prompt);
                ConversationMessage message=userMessage;
                if (!StringUtils.isEmpty(desc)){
                    message=new ConversationMessage(Constants.USER,desc+"\n\n"+"```"+snippet.getLanguage()+"\n"+snippet.getCode());
                }
                chatPane.senAddMessage(message);
                // 获取系统信息
                ConversationMessage systemMessage=new ConversationMessage(Constants.SYSTEM,systemMessageTemplate);
                inputMessages=Arrays.asList(systemMessage, userMessage);
                StreamResquest request = this.buildStreamRequest(inputMessages, onEnd);
                if (request!=null){
                    debouncer=scheduler.schedule(()->{
                        SSEHttpClient sseHttpClient = new SSEHttpClient(request);
                        call=sseHttpClient.getCall();
                    },debounceWait, TimeUnit.MILLISECONDS);
                }
            }
        });
    }

    private String buildTemplatePrompt(String promptTemplate, String languageId, String selectionText) {
        Map<String,String> data=new HashMap<>();
        data.put("code",selectionText);
        data.put("language",languageId!=null?languageId:"unknown");
        try {
            String prompt = templateService.renderTemplate(promptTemplate, data);
            return prompt;
        } catch (IOException e) {
            return "";
        }
    }

    private StreamResquest buildStreamRequest(List<ConversationMessage> conversationMessages, Consumer<Call> onEnd) {
        ChatConfigProvider chatConfigProvider = codeState.getChatConfigProvider();
        List<Message> messageList=new ArrayList<>();
        int maxMessageLength=chatConfigProvider.getMessageLength()==null?3:chatConfigProvider.getMessageLength();
        for(int i=conversationMessages.size()-1;i>=0&&i>=(conversationMessages.size()-maxMessageLength+1);i--){
            ConversationMessage message = conversationMessages.get(i);
            messageList.add(0,new Message(message.getRole(),message.getContent()));
        }
        StreamRequestBodyChatOpenAI requestBody = new StreamRequestBodyChatOpenAI(messageList);
        ChatModelProvider provider= codeState.getChatModelProvider();
        ModeType modelName = provider.getModelName();
        requestBody.setModel(modelName.V().toString());
        requestBody.setMax_tokens(chatConfigProvider.getNumPredict());
        requestBody.setTemperature(chatConfigProvider.getTemperature());
        
        RequestOptions options = new RequestOptions();
        options.setHostname(provider.getHostName())
                .setProtocol(provider.getProtocol().V().toString())
                .setPort(provider.getPort())
                .setPath(provider.getPath())
                .setMethod(Method.POST)
                .addHeader("Content-Type","application/json");
        if (!StringUtils.isEmpty(provider.getApiKey())){
            options.addHeader("Authorization","Bearer %s".formatted(provider.getApiKey()));
        }
        ResponseCallable responseCallable=createCallable(onEnd);
        return new StreamResquest(requestBody,options,responseCallable);
    }

    private ResponseCallable createCallable(Consumer<Call> onEnd) {
        return new ResponseCallable() {
            private List<ConversationMessage> inputMessages=ChatService.this.inputMessages;
            private AIUsageType usageType=ChatService.this.usageType;
            @Override
            public <T> void onStart(T item) {
                call=(Call) item;
                isGeneratingText=true;
            }

            @Override
            public <D> void onData(D result) {
                StreamResponseChatDataItem dataItem=null;
                try {
                    dataItem = JsonUtils.tree2Object((JsonNode) result, StreamResponseChatDataItem.class);
                    String data = dataItem.contentValue();
                    if (!StringUtils.isEmpty(data)){
                        completion=completion.concat(data);
                    }
                    if (onEnd!=null){ // 为git commit提交做准备
                        return;
                    }

                    SharedChatPane chatPane = CodeToolWindowFactory.getCurrentFocusChatPane();
                    ConversationMessage conversationMessage = new ConversationMessage();
                    conversationMessage.setContent(completion);
                    conversationMessage.setRole(Constants.ASSISTANT);
                    if (editor!=null){
                        Languages.CodeLanguageDetails codeLanguageDetails = CompletionUtils.getLanguage(editor);
                        LanguageType languageType = new LanguageType(codeLanguageDetails.getLangName(),codeLanguageDetails);
                        conversationMessage.setLanguage(languageType);
                    }
                    chatPane.sendOnCompletion(conversationMessage);
                } catch (JsonProcessingException e) {
                    logger.error(e);
                }
            }

            @Override
            public <E> void onEnd(E item) {
                // todo 设置文本生成标志符为false
                isGeneratingText=false;
                if (onEnd!=null){
                    onEnd.accept((Call) item);
                }
                SharedChatPane chatPane = CodeToolWindowFactory.getCurrentFocusChatPane();
                ConversationMessage conversationMessage = new ConversationMessage();
                conversationMessage.setContent(completion.strip());
                conversationMessage.setRole(Constants.ASSISTANT);
                if (editor!=null){
                    Languages.CodeLanguageDetails codeLanguageDetails = CompletionUtils.getLanguage(editor);
                    LanguageType languageType = new LanguageType(codeLanguageDetails.getLangName(),codeLanguageDetails);
                    conversationMessage.setLanguage(languageType);
                }
                // 记录日志
                try {
                    if(conversationMessage.getContent()!=null && !conversationMessage.getContent().equals("")){
                        LogExecutor.me().aiuse(usageType,inputMessages,conversationMessage.getContent());
                    }
                }finally {
                    chatPane.sendOnEnd(conversationMessage);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                onStopGeneration();
                isGeneratingText=false;
                // todo 发送结束消息给前端组件，如果为错误，将错误信息为ai回复信息
            }
        };
    }
    
    private void onStopGeneration(){
        if (call!=null && !call.isCanceled()){
            call.cancel();
        }
    }

    private void onGenerateLimit() {
        SharedChatPane chatPane = CodeToolWindowFactory.getCurrentFocusChatPane();
        chatPane.sendGenerateLimit();
    }
    


    public void setEditor(Editor editor){
        this.editor=editor;
    }
    
    
    public void destroyStream(){
        onStopGeneration();
    }


    public boolean isGeneratingText() {
        return isGeneratingText;
    }
}
