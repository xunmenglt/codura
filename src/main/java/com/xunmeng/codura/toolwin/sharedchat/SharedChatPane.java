package com.xunmeng.codura.toolwin.sharedchat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.SelectionEvent;
import com.intellij.openapi.editor.event.SelectionListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.jcef.JBCefBrowser;
import com.intellij.util.ui.UIUtil;
import com.xunmeng.codura.call.CallBack;
import com.xunmeng.codura.net.HttpClient;
import com.xunmeng.codura.pojo.Conversation;
import com.xunmeng.codura.pojo.ConversationMessage;
import com.xunmeng.codura.service.ChatService;
import com.xunmeng.codura.service.ConversationHistoryService;
import com.xunmeng.codura.service.DefaultService;
import com.xunmeng.codura.setting.state.SystemInfoStateService;
import com.xunmeng.codura.system.apis.UserAPI;
import com.xunmeng.codura.system.pojo.User;
import com.xunmeng.codura.toolwin.sharedchat.browser.ChatWebView;
import com.xunmeng.codura.toolwin.sharedchat.events.ClientMessage;
import com.xunmeng.codura.toolwin.sharedchat.events.ServerMessage;
import com.xunmeng.codura.toolwin.sharedchat.events.eidtor.Snippet;
import com.xunmeng.codura.toolwin.sharedchat.events.eventnames.FromChatEvent;
import com.xunmeng.codura.toolwin.sharedchat.events.eventnames.ToChatEvent;
import com.xunmeng.codura.utils.CompletionUtils;
import com.xunmeng.codura.utils.JsonUtils;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 聊天面板控件
 */
public class SharedChatPane extends JPanel implements Disposable {
    private static final  Logger logger=Logger.getInstance(SharedChatPane.class);
    private Project project;
    private ChatWebView browser;
    private JBCefBrowser webView;
    private PropertyChangeListener uiChangeListener;
    private Thread chatThreadToRestore;
    
    private Conversation conversation;


    public SharedChatPane(Project project, Conversation conversation) {
        this.project=project;
        this.conversation=conversation;
        initUiChangeListener();
    }

    private void initUiChangeListener(){
        uiChangeListener=(event)->{
            if (event.getPropertyName() .equals("lookAndFeel") ) {
                this.setLookAndFeel();
            }
        };
    }

    private void setLookAndFeel() {
        this.browser.setStyle();
    }



    /**
     * 向视图发送剪贴内容
     * @param id
     */
    private void sendSelectedSnippet(String id) {
        getSelectedSnippet(snippet -> {
            ServerMessage<Snippet> message = new ServerMessage<>();
            message.setType(ToChatEvent.SET_SELECTED_SNIPPET.getValue());
            message.getValue().setData(snippet);
            this.postMessage(message);
        });
    }

    /**
     * 向视图发送conversation信息
     */
    public void sendConversation(){
        ApplicationManager.getApplication().invokeLater(()->{
            ServerMessage<Conversation> message = new ServerMessage<>();
            message.setType(ToChatEvent.InitConversation.getValue());
            message.getValue().setData(conversation);
            this.postMessage(message);
        });
    }
    
    public void sendOnCompletion(ConversationMessage message){
        ApplicationManager.getApplication().invokeLater(()->{
            ServerMessage<ConversationMessage> serverMessage=new ServerMessage<>();
            serverMessage.setType(ToChatEvent.ON_COMPLETION.getValue());
            serverMessage.getValue().setData(message);
            this.postMessage(serverMessage);
        });
    }

    public void sendOnEnd(ConversationMessage message) {
        ApplicationManager.getApplication().invokeLater(()->{
            ServerMessage<ConversationMessage> serverMessage=new ServerMessage<>();
            serverMessage.setType(ToChatEvent.ON_END.getValue());
            serverMessage.getValue().setData(message);
            this.postMessage(serverMessage);
        });
    }

    /*处理生成限制事件*/
    public void sendGenerateLimit() {
        ApplicationManager.getApplication().invokeLater(()->{
            ServerMessage serverMessage=new ServerMessage();
            serverMessage.setType(ToChatEvent.ON_GENERATE_LIMIT.getValue());
            this.postMessage(serverMessage);
        });
    }

    public void senAddMessage(ConversationMessage userMessage) {
        ServerMessage<ConversationMessage> serverMessage=new ServerMessage<ConversationMessage>();
        serverMessage.getValue().setData(userMessage);
        serverMessage.setType(ToChatEvent.ADD_MESSAGE.getValue());
        this.postMessage(serverMessage);
    }
    
    
    // 发送用户配置
    private void sendUserConfig(){
        boolean isDarkMode = UIUtil.isUnderDarcula();
        String mode = isDarkMode?"dark":"light";
//        this.postMessage();
    }
    
    // 获取选定代码片段给
    private void getSelectedSnippet(CallBack<Snippet> callBack){
        ApplicationManager.getApplication().invokeLater(()->{
            if (!project.isDisposed()){
                Snippet snippet = CompletionUtils.getSnippet();
                callBack.call(snippet);
            }
        });
    }

    // 处理视图端发送过来的信息
    private void handleEvent(ClientMessage clientMessage) {
        String eventTypeValue = clientMessage.getType();
        if (StringUtils.isEmpty(eventTypeValue)) return;
        FromChatEvent eventType = FromChatEvent.valueToObj(eventTypeValue);
        switch (eventType) {
            case READY:
                handleReadyMessage(clientMessage);
                break;

            case CHAT_MESSAGE:
                handleChatMessage(clientMessage);
                break;

            case STOP_GENERATION:
                handleStopGeneration();
                break;

            case SAVE_CONVERSATION:
                handleSaveConversation(clientMessage);
                break;
            case LOGIN:
                handleLogin(clientMessage);
                break;
            case GET_USER_INFO:
                handleGetUserInfo(clientMessage);
                break;
            case LOGOUT:
                handleLogout(clientMessage);
            default:
                // 如果需要的话，处理其他情况
                break;
        }

    }


    private void handleReadyMessage(ClientMessage clientMessage) {
        // 发送对话信息
        sendConversation();
        // 添加事件监听信息
        addEventListeners();
        // todo 设置用户配置
    }

    public void handleStopGeneration() {
        DefaultService.getServiceByClass(ChatService.class).destroyStream();
    }

    private void handleChatMessage(ClientMessage<List<ConversationMessage>> clientMessage) {
        String json_str=null;
        try {
            json_str = JsonUtils.convert2Json(clientMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (json_str==null) return;
        TypeReference<ClientMessage<List<ConversationMessage>>> typeReference = new TypeReference<>() {};
        ClientMessage<List<ConversationMessage>> message = JsonUtils.convert2Object(json_str, typeReference);
        DefaultService.getServiceByClass(ChatService.class).streamChatCompletion(message.getData());
    }
    private void handleSaveConversation(ClientMessage<Conversation> clientMessage) {
        String json_str=null;
        try {
            json_str = JsonUtils.convert2Json(clientMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (json_str==null) return;
        TypeReference<ClientMessage<Conversation>> typeReference = new TypeReference<>() {};
        ClientMessage<Conversation> message = JsonUtils.convert2Object(json_str, typeReference);
        if (message!=null && message.getData()!=null){
            if (message.getData().getMessages()==null){
                conversation.setMessages(new ArrayList<>());
            }else {
                conversation.setMessages(message.getData().getMessages());
            }
        }
        if (StringUtils.isEmpty(conversation.getTitle())){
            // 获取标题
            setConversationTitle(conversation);
        }
    }

    private void handleLogin(ClientMessage clientMessage){
        HttpClient.scheduler.submit(()->{
            try {
                if (clientMessage!=null && clientMessage.getData()!=null) {
                    String json_str = null;
                    json_str = JsonUtils.convert2Json(clientMessage);

                    if (json_str == null) sendLoginFail("参数异常");
                    JsonNode jsonNode = JsonUtils.convert2Object(json_str, JsonNode.class);
                    if (jsonNode!=null){
                        /*获取请求数据*/
                        JsonNode data=jsonNode.get("data");
                        String username=data.get("username").asText();
                        String password=data.get("password").asText();
                        String uuid=data.get("uuid").asText();
                        String code=data.get("code").asText();
                        Future<Response> future = UserAPI.login(username, password, code, uuid);
                        while (! future.isDone());
                        Response response = future.get();
                        String body = response.body().string();
                        JsonNode bodyNode = JsonUtils.convert2Object(body, JsonNode.class);
                        int _code = bodyNode.get("code").asInt();
                        if (_code >= 200 && _code <= 300) {
                            String token = bodyNode.get("token").asText();
                            SystemInfoStateService.settings().getUserInfoProvider().getUser().setToken(token);
                            sendFlashUserInfo();
                        } else {
                            // 登录失败
                            String msg = bodyNode.get("msg").toString();
                            sendLoginFail(msg);
                        }
                    }
                }else {
                    sendLoginFail("请求异常");
                }
            }catch(Exception e){
                sendLoginFail("请求异常");
            }
                
        });
    }
    
    private void handleGetUserInfo(ClientMessage clientMessage){
        sendFlashUserInfo();
    }

    private void handleLogout(ClientMessage clientMessage){
        HttpClient.scheduler.submit(()->{
            try {
                String json_str = null;
                json_str = JsonUtils.convert2Json(clientMessage);
                if (json_str == null) sendLoginFail("参数异常");
                JsonNode jsonNode = JsonUtils.convert2Object(json_str, JsonNode.class);
                if (jsonNode!=null){
                    /*获取请求数据*/
                    Future<Response> future = UserAPI.logout();
                    while (! future.isDone());
                    Response response = future.get();
                    String body = response.body().string();
                    JsonNode bodyNode = JsonUtils.convert2Object(body, JsonNode.class);
                    int _code = bodyNode.get("code").asInt();
                    if (_code >= 200 && _code <= 300) {
                        SystemInfoStateService.settings().getUserInfoProvider().setUser(User.getDefaultUser());
                        sendLogoutSuccess();
                    } else {
                        // 退出失败
                        sendLogoutFail(bodyNode.get("msg").asText());
                    }
                }
            } catch (Exception e) {
                sendLogoutFail("退出异常");
            }
        });
    }
    private void setConversationTitle(Conversation conversation) {
        ApplicationManager.getApplication().executeOnPooledThread(()->{
            DefaultService.getServiceByClass(ConversationHistoryService.class).streamConversationTitle(conversation, new CallBack<String>() {
                @Override
                public void call(String title) {
                    conversation.setTitle(title);
                    sendConversation();
                }
            });
        });
    }
    private void sendFlashUserInfo(){
        HttpClient.scheduler.submit(()->{
            ServerMessage<User> serverMessage=new ServerMessage<>();
            serverMessage.setType(ToChatEvent.FLASH_USER_INFO.getValue());
            serverMessage.getValue().setError(true);
            try {
                User user = SystemInfoStateService.settings().getUserInfoProvider().getUser();
                if (user==null || user.getToken()==null || user.getToken().equals("")){
                    this.postMessage(serverMessage);
                    return;
                }
                Future<Response> userInfo = UserAPI.getUserInfo();
                Response response = userInfo.get();
                String bodyStr = response.body().string();
                JsonNode jsonNode = JsonUtils.convert2Object(bodyStr, JsonNode.class);
                int code = jsonNode.get("code").asInt();
                if (code>=200&&code<=300){
                    String username=jsonNode.get("user").get("userName").asText();
                    String avatar=jsonNode.get("user").get("avatar").asText();
                    String id=username;
                    JsonNode rolesNode = jsonNode.withArray("roles");
                    List<String> roles=new ArrayList<>();
                    for (int i = 0; i < rolesNode.size(); i++) {
                        roles.add(rolesNode.get(i).asText());
                    }
                    JsonNode permissionsNode = jsonNode.withArray("permissions");
                    List<String> permissions=new ArrayList<>();
                    for (int i = 0; i < permissionsNode.size(); i++) {
                        permissions.add(permissionsNode.get(i).asText());
                    }
                    user.setId(id);
                    user.setName(username);
                    user.setAvatar(avatar);
                    user.setRoles(roles);
                    user.setPermissions(permissions);
                    serverMessage.getValue().setError(false);
                    serverMessage.getValue().setData(user);
                }else {
                    serverMessage.getValue().setError(true);
                    String msg=jsonNode.get("msg").asText();
                    serverMessage.getValue().setErrorMessage(msg);
                }
            } catch (Exception e) {
                serverMessage.getValue().setError(true);
                serverMessage.getValue().setErrorMessage(e.getMessage());
            }
            this.postMessage(serverMessage);
        });
    }

    private void sendLoginFail(String errorMessage){
        ServerMessage serverMessage=new ServerMessage<>();
        serverMessage.getValue().setErrorMessage(errorMessage);
        serverMessage.setType(ToChatEvent.LOGIN_FAIL.getValue());
        postMessage(serverMessage);
    }

    private void sendLogoutSuccess(){
        ServerMessage serverMessage=new ServerMessage<>();
        serverMessage.setType(ToChatEvent.LOGOUT_SUCCESS.getValue());
        serverMessage.getValue().setError(false);
        postMessage(serverMessage);
    }
    private void sendLogoutFail(String msg){
        ServerMessage serverMessage=new ServerMessage<>();
        serverMessage.setType(ToChatEvent.LOGOUT_FAIL.getValue());
        serverMessage.getValue().setErrorMessage(msg);
        serverMessage.getValue().setError(true);
        postMessage(serverMessage);
    }
    private void addEventListeners() {
        logger.info("adding event listeners...");
        // 文件管理监听
        FileEditorManagerListener fileEditorManagerListener=new FileEditorManagerListener() {
            @Override
            public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
//                FileEditorManagerListener.super.fileOpened(source, file);
            }

            @Override
            public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
//                FileEditorManagerListener.super.fileClosed(source, file);
            }

            @Override
            public void selectionChanged(@NotNull FileEditorManagerEvent event) {
                sendSelectedSnippet(conversation.getId());
            }
        };

        SelectionListener selectionListener=new SelectionListener() {
            @Override
            public void selectionChanged(@NotNull SelectionEvent e) {
                sendSelectedSnippet(conversation.getId());
            }
        };
        // 添加订阅
        project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER,fileEditorManagerListener);
        EditorFactory editorFactory = EditorFactory.getInstance();
        editorFactory.getEventMulticaster().addSelectionListener(selectionListener,this);
        // todo 添加ui改变事件
        
    }

    public synchronized ChatWebView getBrowser() {
        if (this.browser==null){
            this.browser=new ChatWebView((event)->{
                this.handleEvent(event);
            });
        }
        return this.browser;
    }
    
    public JBCefBrowser getWebView(){
        ChatWebView bw = getBrowser();
        if (bw!=null){
            this.webView = bw.getLazyWebView();
        }
        return this.webView;
    }

    private void postMessage(ServerMessage message) {
        this.browser.postMessage(message);
    }

    public String getId() {
        
        if (conversation!=null){
            return conversation.getId();
        }
        return null;
    }

    @Override
    public void dispose() {
        UIManager.removePropertyChangeListener(uiChangeListener);
        getWebView().dispose();
        Disposer.dispose(this);
    }



    public void restoreWhenReady(String id, List<ConversationMessage> messages) {
        Thread chatThread = new Thread(id, messages, null,false);
        this.chatThreadToRestore = chatThread;
    }
}
