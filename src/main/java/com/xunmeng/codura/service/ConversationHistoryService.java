package com.xunmeng.codura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.components.Service;
import com.xunmeng.codura.call.CallBack;
import com.xunmeng.codura.constants.Constants;
import com.xunmeng.codura.constants.enums.ModeType;
import com.xunmeng.codura.net.SSEHttpClient;
import com.xunmeng.codura.net.constants.Method;
import com.xunmeng.codura.net.options.RequestOptions;
import com.xunmeng.codura.net.response.ResponseCallable;
import com.xunmeng.codura.net.resquest.StreamResquest;
import com.xunmeng.codura.llm.openai.message.Message;
import com.xunmeng.codura.llm.openai.request.StreamRequestBodyChatOpenAI;
import com.xunmeng.codura.llm.openai.response.StreamResponseChatDataItem;
import com.xunmeng.codura.pojo.Conversation;
import com.xunmeng.codura.pojo.ConversationMessage;
import com.xunmeng.codura.setting.provider.ChatConfigProvider;
import com.xunmeng.codura.setting.provider.ChatModelProvider;
import com.xunmeng.codura.setting.state.CodeState;
import com.xunmeng.codura.setting.state.CodeStateService;
import com.xunmeng.codura.utils.CodeBundle;
import com.xunmeng.codura.utils.JsonUtils;
import okhttp3.Call;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public final class ConversationHistoryService {
    public static String TITLE_GENERATION_PROMPT_MESAGE= CodeBundle.message("code.chat.TITLE_GENERATION_PROMPT_MESAGE");
    private CodeState codeState = CodeStateService.settings();
    private Call call;
    

    private StreamResquest buildStreamRequest(List<ConversationMessage> conversationMessages,CallBack callBack) {
        List<Message> messageList=new ArrayList<>();
        for (ConversationMessage message : conversationMessages) {
            messageList.add(new Message(message.getRole(),message.getContent()));
        }
        messageList.add(new Message(Constants.USER,TITLE_GENERATION_PROMPT_MESAGE));
        StreamRequestBodyChatOpenAI requestBody = new StreamRequestBodyChatOpenAI(messageList);
        ChatModelProvider provider= codeState.getChatModelProvider();
        ChatConfigProvider chatConfigProvider = codeState.getChatConfigProvider();
        ModeType modelName = provider.getModelName();
        requestBody.setModel(modelName.V().toString());
        requestBody.setMax_tokens(100);
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
        ResponseCallable responseCallable=createCallable(callBack);
        return new StreamResquest(requestBody,options,responseCallable);
    }

    private ResponseCallable createCallable(CallBack callBack) {
        return new ResponseCallable() {
            String title="";
            @Override
            public <T> void onStart(T item) {
                
            }

            @Override
            public <D> void onData(D result) {
                StreamResponseChatDataItem dataItem=null;
                try {
                    dataItem = JsonUtils.tree2Object((JsonNode) result, StreamResponseChatDataItem.class);
                    String data = dataItem.contentValue();
                    if (!StringUtils.isEmpty(data)){
                        title=title.concat(data);
                    }
                } catch (JsonProcessingException e) {
                    call.cancel();
                }
            }

            @Override
            public <E> void onEnd(E item) {
                callBack.call(title);
            }

            @Override
            public void onError(Throwable throwable) {
                // pass
            }
        };
    }
    
    public void streamConversationTitle(Conversation conversation, CallBack callBack){
        StreamResquest request = this.buildStreamRequest(conversation.getMessages(), callBack);
        SSEHttpClient sseHttpClient = new SSEHttpClient(request);
        call=sseHttpClient.getCall();
    }
}
