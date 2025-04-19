package com.xunmeng.codura.system.apis;

import com.xunmeng.codura.net.HttpClient;
import com.xunmeng.codura.net.constants.Method;
import com.xunmeng.codura.setting.state.SystemInfoStateService;
import com.xunmeng.codura.system.URLConstants;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class UserAPI {
    // 登录，获取token
    public static Future<Response> login(String username,String password,String code,String uuid) throws IOException {
        String url= SystemInfoStateService.settings().getSystemInfoProvider().getRequestBaseUrl();
        url=url+ URLConstants.LOGIN;
        Map<String,Object> data=new HashMap<>();
        data.put("username",username);
        data.put("password",password);
        data.put("code",code);
        data.put("uuid",uuid);
        Future<Response> response = HttpClient.request(Method.POST, url, null, null, data);
        return response;
    }
    
    // 获取用户信息
    public static Future<Response> getUserInfo() throws IOException {
        String url= SystemInfoStateService.settings().getSystemInfoProvider().getRequestBaseUrl();
        url=url+ URLConstants.USER_INFO;
        /*获取token*/
        String token = SystemInfoStateService.settings().getUserInfoProvider().getUser().getToken();
        Map<String,String> headers=new HashMap<>();
        headers.put("Authorization","Bearer "+token);
        Future<Response> response = HttpClient.request(Method.GET, url, headers, null, null);
        return response;
    }

    public static Future<Response> logout() throws IOException {
        String url= SystemInfoStateService.settings().getSystemInfoProvider().getRequestBaseUrl();
        url=url+ URLConstants.LOGOUT;
        /*获取token*/
        String token = SystemInfoStateService.settings().getUserInfoProvider().getUser().getToken();
        Map<String,String> headers=new HashMap<>();
        headers.put("Authorization","Bearer "+token);
        Future<Response> response = HttpClient.request(Method.POST, url, headers, null, new HashMap<>());
        return response;
    }
}
