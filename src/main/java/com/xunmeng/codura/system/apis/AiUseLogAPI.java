package com.xunmeng.codura.system.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xunmeng.codura.net.HttpClient;
import com.xunmeng.codura.net.constants.Method;
import com.xunmeng.codura.setting.state.SystemInfoStateService;
import com.xunmeng.codura.system.URLConstants;
import com.xunmeng.codura.system.logs.pojo.AIUsageLog;
import com.xunmeng.codura.system.logs.pojo.BaseLog;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class AiUseLogAPI {
    public static Future<Response> add(BaseLog log) {
//        /*获取基础url*/
        String url = SystemInfoStateService.settings().getSystemInfoProvider().getRequestBaseUrl();
        url = url + URLConstants.AIUSELOG_ADD;
        String token = SystemInfoStateService.settings().getUserInfoProvider().getUser().getToken();
        /*判断是否有token*/
        // todo WIN:UserNotLoginInError
        if (token == null || token.equals("")) return null;
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        //构建请求参数
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        try {
            data = objectMapper.readValue(objectMapper.writeValueAsString(log), Map.class);
            if (log instanceof AIUsageLog) {
                String inputContent = objectMapper.writeValueAsString(((AIUsageLog) log).getInputContent());
                data.put("inputContent", inputContent);
            }
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
