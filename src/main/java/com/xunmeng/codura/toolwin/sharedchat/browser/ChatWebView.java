package com.xunmeng.codura.toolwin.sharedchat.browser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.jcef.JBCefBrowser;
import com.intellij.ui.jcef.JBCefBrowserBase;
import com.intellij.ui.jcef.JBCefClient;
import com.intellij.ui.jcef.JBCefJSQuery;
import com.intellij.util.ui.UIUtil;
import com.xunmeng.codura.setting.state.SystemInfoStateService;
import com.xunmeng.codura.toolwin.sharedchat.events.ClientMessage;
import com.xunmeng.codura.toolwin.sharedchat.events.ServerMessage;
import com.xunmeng.codura.utils.EventUtils;
import com.xunmeng.codura.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.cef.CefApp;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefLoadHandlerAdapter;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * JCEF 聊天视图对象
 */
public class ChatWebView implements Disposable {
    public static final Logger logger=Logger.getInstance(ChatWebView.class);

    private String jsPoolSize = "200";
    
    private JBCefBrowser webView;
    
    private FromChatHandler fromChatHandler;
    
    private static String tmpPath="";
    
    static {
        File file = new File(System.getProperty("user.home"), ".codealien/web");
        tmpPath=file.getPath();
    }
    
    
    public ChatWebView(FromChatHandler fromChatHandler){
        this.fromChatHandler=fromChatHandler;
        System.setProperty("ide.browser.jcef.jsQueryPoolSize", jsPoolSize);
    }
    
    public synchronized JBCefBrowser getLazyWebView(){
        if (webView==null){
            webView=createWebView();
        }
        return webView;
    }

    private JBCefBrowser createWebView() {
        String osName = System.getProperty("os.name").toLowerCase();
        boolean useOsr=false;
        
        if (osName.contains("mac") || osName.contains("darwin")) {
            // 对于 macOS，不使用离屏渲染
        } else if (osName.contains("win")) {
            // 对于 Windows，不使用离屏渲染
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            // 对于类 Unix 系统，使用离屏渲染
            useOsr = true;
        } else {
            // 对于其他操作系统，使用离屏渲染
            useOsr = true;
        }
        // 创建浏览器对象
        // 获取html页面地址
//        String url= "http://127.0.0.1:8082/code-chat";
        String url= SystemInfoStateService.settings().getSystemInfoProvider().getRequestBaseUrl()+"/code-chat";
//        String url=new File(tmpPath,"index.html").getPath();
        logger.info("current ChatWebView url: %s".formatted(url));
        JBCefBrowser browser = JBCefBrowser.createBuilder()
                .setUrl(url)
                .setOffScreenRendering(useOsr)
                .build();
        // 设置js QUERY 池大小
        browser.getJBCefClient().setProperty(
                JBCefClient.Properties.JS_QUERY_POOL_SIZE,
                jsPoolSize
        );
        
        // 在应用程序初始化时注册SchemeHandlerFactory 以便处理对应的协议程序
        CefApp.getInstance().registerSchemeHandlerFactory("http", "codura", new RequestHandlerFactory());
        JBCefJSQuery myJSQueryOpenInBrowser = JBCefJSQuery.create((JBCefBrowserBase)browser);
        myJSQueryOpenInBrowser.addHandler((msg)->{
            // 用于监听视图发送过来的信息
            logger.info("===>client message:"+msg);
            // 解析msg为client message
            ClientMessage clientMessage;
            try {
                clientMessage= JsonUtils.convert2Object(msg,ClientMessage.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            if (clientMessage!=null){
                fromChatHandler.accept(clientMessage);
            }
            return null;
        });

        JBCefJSQuery myJSQueryOpenInBrowserRedirectHyperlink = JBCefJSQuery.create((JBCefBrowserBase)browser);
        myJSQueryOpenInBrowserRedirectHyperlink.addHandler((href)->{
            if(!StringUtils.isEmpty(href)) {
                BrowserUtil.browse(href);
            }
            return null;
        });
        
        browser.getJBCefClient().addLoadHandler(new CefLoadHandlerAdapter() {
            // 变量 installedScript 用来标记 JavaScript 脚本是否已经被安装到 CEF 浏览器中
            private boolean installedScript=false;
            @Override
            public void onLoadingStateChange(CefBrowser browser, boolean isLoading, boolean canGoBack, boolean canGoForward) {
                // 检查脚本是否已安装，如果没有则调用 setUpJavaScriptMessageBus 方法
                if(!installedScript) {
                    installedScript = setUpJavaScriptMessageBus(browser, myJSQueryOpenInBrowser);
                }
                // 如果浏览器不再加载，则设置 JavaScript 消息总线重定向超链接并设置样式
                if(!isLoading) {// 浏览器加载完成
                    setUpJavaScriptMessageBusRedirectHyperlink(browser, myJSQueryOpenInBrowserRedirectHyperlink);
                    setStyle();
                }
            }
        }, browser.getCefBrowser());
        
        
        browser.createImmediately();
        return browser;
    }

    
    // 注册 JavaScript 脚本到消息总线
    private boolean setUpJavaScriptMessageBus(CefBrowser browser, JBCefJSQuery myJSQueryOpenInBrowser) {
        String script = String.format(
                "window.postIntellijMessage = function(event) { \n" +
                        "    const msg = JSON.stringify(event); \n" +
                        "    %s \n" +
                        "}",
                myJSQueryOpenInBrowser.inject("msg")
        );

        if(browser != null) {
            browser.executeJavaScript(script, browser.getURL(), 0);
            return true;
        }
        return false;
    }

    private void setUpJavaScriptMessageBusRedirectHyperlink(CefBrowser browser, JBCefJSQuery myJSQueryOpenInBrowser) {
        String script = String.format(
                "window.openLink = function(href) { \n" +
                        "    %s \n" +
                        "} \n" +
                        "document.addEventListener('click', function(event) { \n" +
                        "    if (event.target.tagName.toLowerCase() === 'a') { \n" +
                        "        event.preventDefault(); \n" +
                        "        window.openLink(event.target.href); \n" +
                        "    } \n" +
                        "});",
                myJSQueryOpenInBrowser.inject("href")
        );

        if (browser!=null){
            browser.executeJavaScript(script, browser.getURL(), 0);
        }
    }
    
    // 设置页面样式
    public void setStyle() {
        boolean isDarkMode = UIUtil.isUnderDarcula();
        String mode = isDarkMode? "dark":"light";
        String bodyClass = isDarkMode?"vscode-dark":"vscode-light";
        Color backgroundColour = UIUtil.getPanelBackground(); // 获取面板背景颜色
        int red = backgroundColour.getRed();
        int green = backgroundColour.getGreen();
        int blue = backgroundColour.getBlue();
        CefBrowser cefBrowser = getLazyWebView().getCefBrowser();
        cefBrowser.executeJavaScript("document.body.style.setProperty(\"background-color\", \"rgb(%s, %s, %s\");".formatted(red,green,blue),"",0);
        cefBrowser.executeJavaScript("document.body.style.setProperty(\"--bgcolor\", \"rgb(%s, %s, %s\");".formatted(red,green,blue),"",0);
        cefBrowser.executeJavaScript("document.body.class = \"%s\";".formatted(bodyClass),"",0);
        cefBrowser.executeJavaScript("document.documentElement.className = \"%s\";".formatted(mode),"",0);
    }

    // 发送消息到视图页面
    public void postMessage(ServerMessage message) {
        if(message != null) {
            String json = EventUtils.stringify(message);
            this.postMessage(json);
        }
    }

    public void postMessage(String message) {
        String script = "window.postMessage(%s, \"*\");".formatted(message);
        webView.getCefBrowser().executeJavaScript(script, webView.getCefBrowser().getURL(), 0);
    }
    
    
    public JComponent getComponent() {
        return webView.getComponent();
    }


    @Override
    public void dispose() {
        if (webView!=null){
            webView.dispose();
        }
    }
}

