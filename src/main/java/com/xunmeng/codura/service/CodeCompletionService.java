package com.xunmeng.codura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.concurrency.AppExecutorUtil;
import com.xunmeng.codura.constants.*;
import com.xunmeng.codura.constants.enums.FimTemplateType;
import com.xunmeng.codura.constants.enums.ModeType;
import com.xunmeng.codura.editor.CodeDefaultInlayRenderer;
import com.xunmeng.codura.editor.InlayUtils;
import com.xunmeng.codura.llm.openai.message.Message;
import com.xunmeng.codura.llm.openai.request.StreamRequestBodyChatOpenAI;
import com.xunmeng.codura.llm.openai.response.StreamResponseChatDataItem;
import com.xunmeng.codura.net.SSEHttpClient;
import com.xunmeng.codura.net.constants.Method;
import com.xunmeng.codura.net.options.RequestOptions;
import com.xunmeng.codura.net.response.ResponseCallable;
import com.xunmeng.codura.net.response.ResponseDataBaseItem;
import com.xunmeng.codura.net.resquest.StreamResquest;
import com.xunmeng.codura.llm.openai.request.StreamRequestBodyCompletionOpenAI;
import com.xunmeng.codura.llm.openai.response.StreamResponseCompletionDataItem;
import com.xunmeng.codura.pojo.ConversationMessage;
import com.xunmeng.codura.pojo.PrefixSuffix;
import com.xunmeng.codura.setting.provider.CompletionConfigProvider;
import com.xunmeng.codura.setting.provider.FimModelProvider;
import com.xunmeng.codura.setting.state.CodeStateService;
import com.xunmeng.codura.status.CodeStatus;
import com.xunmeng.codura.status.CodeStatusService;
import com.xunmeng.codura.system.logs.LogExecutor;
import com.xunmeng.codura.system.logs.constans.AIUsageType;
import com.xunmeng.codura.system.logs.pojo.AIUsageLog;
import com.xunmeng.codura.utils.*;
import okhttp3.Call;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.xunmeng.codura.constants.Constants.*;

/**
 * CompletionProvider，代码补全提供者
 * 用于对代码补全进行控制
 */
@Service
public final class CodeCompletionService {
    // 配置
    private Call call;

    // 上次补全接受状态
    private boolean acceptedLastCompletion = false;

    // 补全缓存是否开启
    private boolean completionCacheEnabled = false;

    // 总共接受的字符数
    private int chunkCount = 0;

    // 鼠标指针位置
    private ASTNode nodeAtPosition;

    // 防抖器
    private Future debouncer;

    // 防抖时间
    private long debounceWait = 200;

    // 自动建议
    private boolean autoSuggestEnabled = false;

    // 文档
    private Document document;
    // 是否开启补全
    private boolean enabled = true;
    // 是否开启连续补全
    private boolean enableSubsequentCompletions = false; // todo config
    // 扩展内容
    private Object extensionContext;
    // 文件信息缓存
    private Object fileInteractionCache;
    // 是否多行补全
    private boolean isMultilineCompletion = false;
    // keepAlive
    private int keepAlive = 0;
    // 上次是否是多行补全
    private boolean lastCompletionMultiline = false;
    // 最后的补全文本
    public String lastCompletionText = "";
    // 日志
    private Logger logger = Logger.getInstance(CodeCompletionService.class);
    private int maxLines = 20;
    // 是否可多行补全
    private boolean multilineCompletionsEnabled = true;
    private int nonce = 0;

    // 生成内容长度
    private int numLineContext = 300;

    // 预测长度
    private int numPredictFim = 256;
    // 解析器
    private PsiParserFacade parser;

    // 位置
    private CaretModel position;
    // 前缀
    private PrefixSuffix prefixSuffix;
    // 状态栏
    private Object statusBar;
    // 温度
    private double temperature = 0.2;
    // TemplateProvider 
    private TemplateService templateProvider;
    private boolean fileContextEnabled = false;
    private boolean usingFimTemplate = false;
    private ScheduledExecutorService scheduler = AppExecutorUtil.createBoundedScheduledExecutorService("SMCCompletionScheduler", 1);
    // 异步锁
    private ReentrantLock lock = new ReentrantLock();
    private boolean canTab = false;

    private String completion = "";

    // 创建缓存
    private CompletionCache cache = new CompletionCache(50);

    private Editor editor;


    private CompletionConfigProvider completionConfigProvider = CodeStateService.settings().getCompletionConfigProvider();

    /*日志对象*/
    private AIUsageLog commpletionLog = null;

    private String inputPrompt = "";

    public void provideInlineCompletionItems(Editor editor, DataContext dataContext) {
        this.document = editor.getDocument();
        this.editor = editor;
        // 判断上一次补全状态是否被接受,并判断是否开启了连续补全
        // 如果开启了连续补全 则isLastCompletionAccepted为fasle，当补全后还是会继续生成
        boolean isLastCompletionAccepted = this.acceptedLastCompletion && !completionConfigProvider.isEnableSubsequentCompletions();

        // 获取前后缀信息
        CaretModel caretModel = editor.getCaretModel();

        prefixSuffix = CompletionUtils.getPrefixSuffix(completionConfigProvider.getNumLineContext(), document, caretModel);

        // 获取代码补全缓存，判断当前前后缀信息是否在之前以及出现过了
        String cachedCompletion = cache.getCache(prefixSuffix);
        // 如果出现过，则返回缓存提示信息，前提条件是开启了缓存补全功能（_completionCacheEnabled=true）
        if (cachedCompletion != null && completionConfigProvider.isCompletionCacheEnabled()) {
            this.completion = cachedCompletion;
            this.provideInlineCompletion();
            return;
        }

        // 判断是否开启
        if (
                !completionConfigProvider.isEnabled() ||
                        editor == null ||
                        // 上一次补全被接收
                        isLastCompletionAccepted ||
                        // 上一次补全是多行，则直接返回空
                        this.lastCompletionMultiline ||
                        // 是否要跳过
                        CompletionUtils.getShouldSkipCompletion(document, editor, this.autoSuggestEnabled) ||
                        CompletionUtils.getIsMiddleOfString(editor)
        ) {
            codeAlienOnReady();
            return;
        }

        // 初始化参数
        this.chunkCount = 0;
        this.position = editor.getCaretModel();
        this.nonce += 1;
        codeAlienInProgress();
        // 获取文件解析器
        parser = PsiParserFacade.getInstance(editor.getProject());
        // 获取当前鼠标指针所在的位置
        nodeAtPosition = CompletionUtils.safeRead(()->CompletionUtils.getNodeAtPosition(document, editor));
        // 根据鼠标在代码中的位置判断接下来是否可以多行补全
        this.isMultilineCompletion = CompletionUtils.getIsMultilineCompletion(nodeAtPosition, prefixSuffix, editor);
        // 清空定时器
        cancelTask();
        this.debouncer = null;
        // 获取Prompt提示词模板
        String prompt = getPrompt(prefixSuffix);
        /*记录输入prompt*/
        inputPrompt = prompt;

        if (StringUtils.isEmpty(prompt)) return;
        debouncer = scheduler.schedule(() -> {
            doInlineCompletion(prompt);
        }, debounceWait, TimeUnit.MILLISECONDS);
    }

    private boolean isChatCompletion() {
        FimModelProvider provider = getProvider();
        if (provider.getFimTemplateType() != null && provider.getFimTemplateType() == FimTemplateType.USECHAT) {
            return true;
        } else {
            return false;
        }
    }

    private void doInlineCompletion(String prompt) {
        lock.lock();// 获取锁
        try {
            FimModelProvider provider = getProvider();
            if (provider == null) return;
            StreamResquest request = buildStreamRequest(prompt, provider);
            SSEHttpClient sseHttpClient = new SSEHttpClient(request);
            call = sseHttpClient.getCall();
        } finally {
            lock.unlock();// 释放锁
        }
    }


    private StreamResquest buildCompletionStreamRequest(String prompt, FimModelProvider provider) {
        StreamRequestBodyCompletionOpenAI requestBody = new StreamRequestBodyCompletionOpenAI();
        ModeType modelName = provider.getModelName();
        requestBody.setModel(modelName.V().toString());
        requestBody.setMax_tokens(completionConfigProvider.getNumPredictFim());
        requestBody.setTemperature(completionConfigProvider.getTemperature());
        requestBody.setPrompt(prompt);
        RequestOptions options = new RequestOptions();
        options.setHostname(provider.getHostName())
                .setProtocol(provider.getProtocol().V().toString())
                .setPort(provider.getPort())
                .setPath(provider.getPath())
                .setMethod(Method.POST)
                .addHeader("Content-Type", "application/json");
        if (!StringUtils.isEmpty(provider.getApiKey())) {
            options.addHeader("Authorization", "Bearer ".formatted(provider.getApiKey()));
        }
        ResponseCallable responseCallable = createCallable();
        return new StreamResquest(requestBody, options, responseCallable);
    }

    private StreamResquest buildChatCompletionStreamRequest(String prompt, FimModelProvider provider) {
        // 构建消息数组
        List<Message> messageList = new ArrayList<>();
        messageList.add(
                new Message(
                        MessageRole.USER,
                        prompt
                )
        );
        StreamRequestBodyChatOpenAI requestBody = new StreamRequestBodyChatOpenAI(messageList);
        ModeType modelName = provider.getModelName();
        requestBody.setModel(modelName.V().toString());
        requestBody.setMax_tokens(completionConfigProvider.getNumPredictFim());
        requestBody.setTemperature(completionConfigProvider.getTemperature());
        RequestOptions options = new RequestOptions();
        options.setHostname(provider.getHostName())
                .setProtocol(provider.getProtocol().V().toString())
                .setPort(provider.getPort())
                .setPath(provider.getPath())
                .setMethod(Method.POST)
                .addHeader("Content-Type", "application/json");
        if (!StringUtils.isEmpty(provider.getApiKey())) {
            options.addHeader("Authorization", "Bearer %s".formatted(provider.getApiKey()));
        }
        ResponseCallable responseCallable = createCallable();
        return new StreamResquest(requestBody, options, responseCallable);
    }

    private StreamResquest buildStreamRequest(String prompt, FimModelProvider provider) {
        if (isChatCompletion()) {
            return buildChatCompletionStreamRequest(prompt, provider);
        } else {
            return buildCompletionStreamRequest(prompt, provider);
        }
    }


    private ResponseCallable createCallable() {
        return new ResponseCallable() {
            @Override
            public <T> void onStart(T item) {
                call = (Call) item;
            }

            @Override
            public <D> void onData(D result) {
                ResponseDataBaseItem dataItem = null;

                try {
                    if (isChatCompletion()) {
                        dataItem = JsonUtils.tree2Object((JsonNode) result, StreamResponseChatDataItem.class);
                    } else {
                        dataItem = JsonUtils.tree2Object((JsonNode) result, StreamResponseCompletionDataItem.class);
                    }
                } catch (JsonProcessingException e) {
                    logger.error(e);
                }
                String data = dataItem.contentValue();
                String _completion = onCompletionData(data);
                if (!StringUtils.isEmpty(_completion)) {
                    call.cancel();
                }else {
                    // 渲染内容
                    ApplicationManager.getApplication().invokeLater(() -> {
                        clearRender();
                        doRender(editor, completion, false);
                    });
                }
            }

            @Override
            public <E> void onEnd(E item) {
                ApplicationManager.getApplication().invokeLater(()->{
                    provideInlineCompletion();
                    canTab = true;
                    codeAlienOnDone();
                });
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error(throwable);
                if (call != null) {
                    call.cancel();
                }
                codeAlienOnError(throwable.getMessage());
                NotifyUtils.error(throwable.getMessage());
                ApplicationManager.getApplication().invokeLater(() -> {
                    clearRender();
                });
            }
        };
    }

    private String onCompletionData(String data) {
        FimModelProvider provider = getProvider();
        if (provider == null) return "";
        try {
            // 处理接受到的数据
            String providerFimData = data;
            if (StringUtils.isEmpty(providerFimData)) return "";
            // 更新补全内容
            this.completion = this.completion + providerFimData;
            // 消除markdown
            this.completion=clearMarkdownHeader(completion);
            this.chunkCount = this.chunkCount + 1;
            // 处理空补全，超过一定空格，停止生成
            if (this.completion.length() > MAX_EMPTY_COMPLETION_CHARS && this.completion.trim().length() == 0) {
                abortCompletion();
                logger.info("Streaming response end as llm in empty completion loop:  %s".formatted(nonce));
            }
            // 单行补全逻辑
            // 如果配置中关闭了多行补全，并且当前补全内容已经达到最小数据块数量，且内容以换行符开始，则结束补全并返回当前补全内容。
            // 作用：当用户关闭了（multilineCompletionsEnabled）则返回单行提示内容
            if (!completionConfigProvider.isMultilineCompletionsEnabled() &&
                    this.chunkCount >= MIN_COMPLETION_CHUNKS &&
                    LINE_BREAK_REGEX.matcher(this.completion.replaceFirst("^\\s+", "")).find()) {
                return this.completion;
            }
            // 多行补全逻辑
            // 如果当前补全不是多行补全，但配置中启用了多行补全，并且数据块数量达到阈值，且补全内容以换行符开始，则结束补全并返回当前补全内容。
            // 获取多行请求标志
            boolean isNotMultilineCompletionRequired = !this.isMultilineCompletion &&
                    completionConfigProvider.isMultilineCompletionsEnabled() &&
                    this.chunkCount >= MIN_COMPLETION_CHUNKS &&
                    LINE_BREAK_REGEX.matcher(this.completion.replaceFirst("^\\s+", "")).find();

            if (isNotMultilineCompletionRequired) {
                return this.completion;
            }
            try {
                if (this.nodeAtPosition != null) {
                    // takeFirst用于记录鼠标指针是否在如类体、接口体之内，或者在对象函数体内且子元素要大于2
                    String elementType = this.nodeAtPosition.getElementType().getDebugName().toLowerCase();
                    boolean takeFirst = MULTILINE_OUTSIDE.contains(elementType)
                            || (MULTILINE_INSIDE.contains(elementType) &&
                            this.nodeAtPosition.getChildren(null).length > 2);
                    // 获取当前选中行文本
                    String lineText = CompletionUtils.safeRead(()->CompletionUtils.getCurrentLineText(this.position, document));
                    if (parser == null) return "";
                    if (providerFimData.indexOf("\n") != -1 && this.nodeAtPosition != null) {
                        Language language = this.nodeAtPosition.getPsi().getLanguage();
                        boolean hasError = false;
                        CompletionUtils.safeRead(()->{
                            try {
                                parser.createBlockCommentFromText(language, lineText + completion);
                                return false;
                            } catch (Exception e) {
                                return true;
                            }
                        });
                        if (this.isMultilineCompletion && this.chunkCount >= 2 && takeFirst && !hasError) {
                            if (MULTI_LINE_DELIMITERS.stream().anyMatch((e) -> this.completion.endsWith(e))) {
                                return this.completion;
                            }
                        }

                    }
                }
            } catch (Exception e) {
                logger.error(e);
                abortCompletion();
            }
            if (getLineBreakCount(this.completion) >= this.maxLines) {
                return this.completion;
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }


    private String getPrompt(PrefixSuffix prefixSuffix) {
        FimModelProvider provider = getProvider();
        if (provider == null || document == null || position == null) {
            return "";
        }
        String documentLanguage = this.nodeAtPosition.getPsi().getLanguage().getID().toLowerCase();

        // todo 获取文件上下文信息
        String fileInteractionContext = "";

        String filePath = PsiDocumentManager.getInstance(editor.getProject()).getPsiFile(document).getVirtualFile().getPath();
        String header = getPromptHeader(documentLanguage, filePath);
        // 获取fim模板
        String prompt = FimTemplateUtils.getFimPrompt(provider, fileInteractionContext, prefixSuffix, header, fileContextEnabled, documentLanguage);
        return prompt;
    }

    private String getPromptHeader(String languageId, String filePath) {
        Languages.CodeLanguageDetails lang = Languages.getLang(languageId);
        if (lang == null) {
            return "";
        }
        String language = "%s Language: %s (%s) %s".formatted(lang.getSyntaxComments().getStart(), lang.getLangName(), languageId, lang.getSyntaxComments().getEnd());
        String path = "%s File uri: %s (%s) %s".formatted(lang.getSyntaxComments().getStart(), filePath, languageId, lang.getSyntaxComments().getEnd());
        return "\n%s\n%s\n".formatted(language, path);
    }

    private FimModelProvider getProvider() {
        FimModelProvider fimModelProvider = CodeStateService.settings().getFimModelProvider();
        return fimModelProvider;
    }


    private String clearMarkdownHeader(String str){
        String[] split = str.split("\n");
        if (split.length<=2) return str;
        StringBuilder sb = new StringBuilder("");
        for(int i=0;i<split.length;i++){
            String line=split[i];
            if (i==0 && line.strip().startsWith("```")) continue;
            if (i==split.length-1){
                line.replace("```","");
            }
            sb.append(line);
            if (i!=split.length-1) sb.append("\n");
        }
        return sb.toString();
    }

    private String formatInsertedCode(Project project, String prefix, String fillCode, String suffix) {
        String fullText = prefix + fillCode + suffix;

        // 获取语言和文件名
        PsiFile psiFile = CompletionUtils.safeRead(()->PsiDocumentManager.getInstance(project).getPsiFile(document));
        if (psiFile == null) return fillCode;

        Language language = psiFile.getLanguage();
        String name = psiFile.getName();

        // 构建新的临时 PsiFile
        PsiFile newPsiFile = CompletionUtils.safeRead(()->PsiFileFactory.getInstance(project).createFileFromText(
                name, language, fullText
        ));

        FileType fileType = psiFile.getFileType();
        if (!(fileType instanceof LanguageFileType)) return fillCode;


        int fillCodeStartOffset = prefix.length();
        int fillCodeEndOffset = prefix.length() + fillCode.length();

        PsiElement startElement = newPsiFile.findElementAt(fillCodeStartOffset);
        PsiElement endElement = newPsiFile.findElementAt(fillCodeEndOffset - 1);
        if (startElement == null || endElement == null) return fillCode;

        PsiElement commonParent = CompletionUtils.safeRead(()->PsiTreeUtil.findCommonParent(startElement, endElement));
        if (commonParent == null) return fillCode;

        // 必须放在 WriteCommandAction 中格式化 PSI
        WriteCommandAction.runWriteCommandAction(project, () -> {
            CodeStyleManager.getInstance(project).reformat(commonParent);
        });

        // 提取格式化后文本
        String formattedAll = newPsiFile.getText();
        try {
            String res = formattedAll.substring(fillCodeStartOffset, fillCodeEndOffset).trim();
            return res;
        }catch (Exception e){
            return fillCode;
        }
    }


    private void provideInlineCompletion() {
        clearRender();

        if (editor == null || nodeAtPosition == null) {
            return;
        }
        // todo 清除响应结果停止词
        completion=formatInsertedCode(editor.getProject(),prefixSuffix.getPrefix(),completion,prefixSuffix.getSuffix());
        String formattedCompletion = completion;
        if (this.completionCacheEnabled) {
            cache.setCache(this.prefixSuffix, formattedCompletion);
        }
        completion = "";
        codeAlienOnDone();
        this.lastCompletionText = formattedCompletion;
        this.lastCompletionMultiline = getLineBreakCount(formattedCompletion) > 1;

        // 记录日志
        ConversationMessage conversation = new ConversationMessage(USER, inputPrompt);
        List<ConversationMessage> conversationMessageList = Arrays.asList(conversation);

        try {
            if (formattedCompletion != null && !formattedCompletion.equals("")) {
                LogExecutor.me().aiuse(AIUsageType.CODE_COMPLETION, conversationMessageList, formattedCompletion);
            }
        } finally {
            // 渲染内容
            ApplicationManager.getApplication().invokeLater(() -> {
                doRender(editor, formattedCompletion, false);
            });
        }


    }


    public void clearRender() {
        InlayModel inlayModel = editor.getInlayModel();
        int offset = CompletionUtils.safeRead(()->editor.getCaretModel().getOffset());
        int clearLength = Math.max(THINKING_TEXT.length(), completion.length());
        inlayModel.getInlineElementsInRange(offset, offset + clearLength).forEach((it -> Disposer.dispose(it)));
        inlayModel.getBlockElementsInRange(offset, offset + clearLength).forEach((it -> Disposer.dispose(it)));
    }

    private void doRender(Editor editor, String completion, boolean isThinking) {
        InlayModel inlayModel = editor.getInlayModel();
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();// 偏移量
        if (isThinking) {
            String thinkingText = THINKING_TEXT;
            CodeDefaultInlayRenderer thinkingRenderer = new CodeDefaultInlayRenderer(
                    CodeCompletionType.Inline,
                    List.of(thinkingText)
            );
            inlayModel.addInlineElement(offset, InlayUtils.createBlockProperties(0), thinkingRenderer);
        } else {
            clearRender();
            //        inlayModel.getInlineElementsInRange(0,editor.getDocument().getTextLength()).forEach((it-> Disposer.dispose(it)));
            //        inlayModel.getBlockElementsInRange(0,editor.getDocument().getTextLength()).forEach((it-> Disposer.dispose(it)));
            List<String> lines = Arrays.asList(completion.split("\n"));
            List<String> lineLiens = lines.subList(0, 1);
            CodeDefaultInlayRenderer lineRenderer = new CodeDefaultInlayRenderer(CodeCompletionType.Block,
                    lineLiens);
            inlayModel.addInlineElement(offset, InlayUtils.createBlockProperties(0), lineRenderer);

            List<String> blockLines = new ArrayList<>();
            if (lines.size() > 1) {
                blockLines = lines.subList(1, lines.size());
            }
            CodeDefaultInlayRenderer blockRenderer = new CodeDefaultInlayRenderer(CodeCompletionType.Block,
                    blockLines);
            inlayModel.addBlockElement(offset, InlayUtils.createBlockProperties(0), blockRenderer);
        }

        // 移动光标位置到初始位置
        caretModel.moveToOffset(offset);
    }


    public void abortCompletion() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
        codeAlienOnDone();
    }

    public void cancelTask() {
        if (debouncer != null && !debouncer.isCancelled()) {
            debouncer.cancel(true);
        }
    }

    // 判断是否可以补全
    public boolean isCanTab() {
        boolean tmp = canTab;
        return tmp;
    }

    public void setCanTab(boolean canTab) {
        this.canTab = canTab;
    }


    public String getLastCompletionText() {
        return lastCompletionText;
    }

    public void setLastCompletionText(String lastCompletionText) {
        this.lastCompletionText = lastCompletionText;
    }

    // 设置上次补全状态
    public void setAcceptedLastCompletion(boolean value) {
        this.acceptedLastCompletion = value;
        this.lastCompletionMultiline = CompletionUtils.getLineBreakCount(this.completion) > 1;
    }

    public Editor getEditor() {
        return editor;
    }

    public boolean isAcceptedLastCompletion() {
        return acceptedLastCompletion;
    }

    private void codeAlienOnReady() {
        CodeStatusService.notifyApplication(CodeStatus.Ready, null);
    }

    private void codeAlienInProgress() {
        ApplicationManager.getApplication().invokeLater(() -> {
            doRender(editor, null, true);
        });
        CodeStatusService.notifyApplication(CodeStatus.InProgress, null);
    }

    private void codeAlienOnError(String message) {
        CodeStatusService.notifyApplication(CodeStatus.Error, message);
    }

    private void codeAlienOnDone() {
        CodeStatusService.notifyApplication(CodeStatus.Done, null);
    }
}
