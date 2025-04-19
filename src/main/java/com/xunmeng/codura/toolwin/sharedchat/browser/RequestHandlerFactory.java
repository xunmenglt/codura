package com.xunmeng.codura.toolwin.sharedchat.browser;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAware;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefCallback;
import org.cef.callback.CefSchemeHandlerFactory;
import org.cef.handler.CefLoadHandler;
import org.cef.handler.CefResourceHandler;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * CefSchemeHandlerFactory 允许开发者为特定的 URL 模式（称为 "scheme"）创建自定义的请求处理程序。
 */
public class RequestHandlerFactory implements CefSchemeHandlerFactory {
    public static final Logger logger=Logger.getInstance(RequestHandlerFactory.class); 
    @Override
    public CefResourceHandler create(CefBrowser browser, CefFrame frame, String schemeName, CefRequest request) {
        return new ResourceHandler();
    }
    
    public class ResourceHandler implements CefResourceHandler, DumbAware{
        private ResourceHandlerState state=new ClosedConnection();
        private String currentUrl;
        
        @Override
        public boolean processRequest(CefRequest request, CefCallback callback) {
            // 获取url
            String url=request.getURL();
            if (url!=null){
                // 将网络资源映射到类路径（classpath）下的资源。
                String pathToResource=url.replace("http://codealien/", "codura/web/");
                URL newUrl = this.getClass().getClassLoader().getResource(pathToResource);
                if (newUrl!=null){
                    try {
                        state=new OpenedConnection(newUrl.openConnection());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                currentUrl=url;
                callback.Continue();
                return true;
            }else {
                return false;
            }
        }

        @Override
        public void getResponseHeaders(CefResponse cefResponse, IntRef responseLength, StringRef redirectUrl) {
            if (currentUrl!=null){
                if (currentUrl.contains("css")){
                    cefResponse.setMimeType("text/css");
                }else if (currentUrl.contains("js")){
                    cefResponse.setMimeType("text/javascript");
                }else if (currentUrl.contains("html")){
                    cefResponse.setMimeType("text/html");
                }
            }
            state.getResponseHeaders(cefResponse, responseLength, redirectUrl);
        }

        @Override
        public boolean readResponse(byte[] dataOut, int bytesToRead, IntRef bytesRead, CefCallback callback) {
            return state.readResponse(dataOut, bytesToRead, bytesRead, callback);
        }

        @Override
        public void cancel() {
            state.close();
            state = new ClosedConnection();
        }
    }
    
    public abstract class ResourceHandlerState{
        public void getResponseHeaders(CefResponse cefResponse, IntRef responseLength,StringRef redirectUrl){
             
        }
        public boolean readResponse(byte[] dataOut,int bytesToRead, IntRef bytesRead,CefCallback callback){
            return false;
        }
        public void close(){
            
        }
    }
    
    public class ClosedConnection extends ResourceHandlerState{
        @Override
        public void getResponseHeaders(CefResponse cefResponse, IntRef responseLength, StringRef redirectUrl) {
            cefResponse.setStatus(404);
        }
    }
    
    public class OpenedConnection extends ResourceHandlerState{
        private URLConnection connection;
        private InputStream inputStream;
        
        public OpenedConnection(URLConnection connection){
            this.connection=connection;
        }

        @Override
        public void getResponseHeaders(CefResponse cefResponse, IntRef responseLength, StringRef redirectUrl) {
            try {
                if (connection!=null){
                    String url = connection.getURL().toString();
                    if (url.contains("css")){
                        cefResponse.setMimeType("text/css");
                    }else if (url.contains("js")){
                        cefResponse.setMimeType("text/javascript");
                    }else if (url.contains("html")){
                        cefResponse.setMimeType("text/html");
                    }else {
                        cefResponse.setMimeType(connection.getContentType());
                    }
                    if (getInputStream()!=null){
                        responseLength.set(getInputStream().available());
                    }else {
                        responseLength.set(0);
                    }
                    cefResponse.setStatus(200);
                }else {
                    cefResponse.setError(CefLoadHandler.ErrorCode.ERR_FAILED);
                    cefResponse.setStatusText("Connection is null");
                    cefResponse.setStatus(500);
                }
            }catch (Exception e){
                logger.error("Error"+e);
                cefResponse.setError(CefLoadHandler.ErrorCode.ERR_FILE_NOT_FOUND);
                cefResponse.setStatusText(e.getLocalizedMessage());
                cefResponse.setStatus(404);
            }
        }

        @Override
        public boolean readResponse(byte[] dataOut, int bytesToRead, IntRef bytesRead, CefCallback callback) {
            InputStream stream = getInputStream();
            if (stream!=null){
                try {
                    int availableSize=stream.available();
                    if (availableSize>0){
                        // 获取最小的窗口大小
                        int maxBytesToRead = Math.min(availableSize, bytesToRead);
                        // 读取流内容
                        int realBytesRead = inputStream.read(dataOut, 0, maxBytesToRead);
                        bytesRead.set(realBytesRead);
                        return true;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return false;
        }

        public synchronized InputStream getInputStream() {
            if (connection!=null){
                try {
                    this.inputStream=connection.getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return inputStream;
        }

        @Override
        public void close() {
            InputStream stream = getInputStream();
            if (stream!=null){
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    
}
