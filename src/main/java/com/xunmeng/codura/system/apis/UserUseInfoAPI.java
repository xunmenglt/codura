package com.xunmeng.codura.system.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xunmeng.codura.net.HttpClient;
import com.xunmeng.codura.net.constants.Method;
import com.xunmeng.codura.setting.state.SystemInfoStateService;
import com.xunmeng.codura.system.URLConstants;
import com.xunmeng.codura.system.pojo.UserUseInfo;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class UserUseInfoAPI {
    public static Future<Response> info(){
//        /*获取基础url*/
        String url= SystemInfoStateService.settings().getSystemInfoProvider().getRequestBaseUrl();
        url=url+ URLConstants.AIUSE_INFO;
        String token = SystemInfoStateService.settings().getUserInfoProvider().getUser().getToken();
        /*判断是否有token*/
        // todo WIN:UserNotLoginInError
        if (token==null || token.equals("")) return null;
        Map<String,String> headers=new HashMap<>();
        headers.put("Authorization","Bearer "+token);
        
        Future<Response> response = null;
        try {
            response = HttpClient.request(Method.GET, url, headers, null, null);
        } catch (IOException e) {
            // todo WIN:RequestError
        }
        return response;
    }

    public static Future<Response> todayUseInfo(){
        String url= SystemInfoStateService.settings().getSystemInfoProvider().getRequestBaseUrl();
        url=url+ URLConstants.AIUSE_TODAY_INFO;
        String token = SystemInfoStateService.settings().getUserInfoProvider().getUser().getToken();
        /*判断是否有token*/
        // todo WIN:UserNotLoginInError
        if (token==null || token.equals("")) return null;
        Map<String,String> headers=new HashMap<>();
        headers.put("Authorization","Bearer "+token);

        Future<Response> response = null;
        try {
            response = HttpClient.request(Method.GET, url, headers, null, null);
        } catch (IOException e) {
            // todo WIN:RequestError
        }
        return response;

    }

    public static Future<Response> weekUseInfo(){
        String url= SystemInfoStateService.settings().getSystemInfoProvider().getRequestBaseUrl();
        url=url+ URLConstants.AIUSE_WEEK_INFO;
        String token = SystemInfoStateService.settings().getUserInfoProvider().getUser().getToken();
        /*判断是否有token*/
        // todo WIN:UserNotLoginInError
        if (token==null || token.equals("")) return null;
        Map<String,String> headers=new HashMap<>();
        headers.put("Authorization","Bearer "+token);

        Future<Response> response = null;
        try {
            response = HttpClient.request(Method.GET, url, headers, null, null);
        } catch (IOException e) {
            // todo WIN:RequestError
        }
        return response;
    }



    public static Future<Response> updateUserEditorUseInfo(UserUseInfo useInfo){
        String url= SystemInfoStateService.settings().getSystemInfoProvider().getRequestBaseUrl();
        url=url+ URLConstants.UPDATE_USER_EDITOR_USE_INFO;
        String token = SystemInfoStateService.settings().getUserInfoProvider().getUser().getToken();
        /*判断是否有token*/
        // todo WIN:UserNotLoginInError
        if (token==null || token.equals("")) return null;
        Map<String,String> headers=new HashMap<>();
        headers.put("Authorization","Bearer "+token);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> data=new HashMap<>();
        try {
            data= objectMapper.readValue(objectMapper.writeValueAsString(useInfo), Map.class);
        } catch (JsonProcessingException e) {
            // todo WIN:SystemRunTimeError
            return null;
        }

        Future<Response> response = null;
        try {
            response = HttpClient.request(Method.POST, url, headers, null, data);
        } catch (IOException e) {
            // todo WIN:RequestError
        }
        return response;
    }
}
