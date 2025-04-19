package com.xunmeng.codura.net;


import com.intellij.util.concurrency.AppExecutorUtil;
import com.xunmeng.codura.utils.JsonUtils;
import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

public class HttpClient {
    public static final Logger logger = Logger.getLogger(HttpClient.class.getName());
    private static final OkHttpClient client = new OkHttpClient();
    public static final ScheduledExecutorService scheduler = AppExecutorUtil.createBoundedScheduledExecutorService("HttpClient", 12);

    public static Future<Response> request(String method,final String url, Map<String,String> headers, Map<String,Object> params, Map<String,Object> data) throws IOException {
        Future<Response> future = scheduler.submit(new Callable<Response>() {
            private String _url=url;
            @Override
            public Response call() throws Exception {
                Request.Builder builder = new Request.Builder();
                if (params != null && params.keySet().size() > 0) {
                    _url = url + "?" + transParams(params);
                }
                builder.url(_url);
                if (data == null) {
                    builder.method(method, null);
                } else {
                    // 创建MediaType对象，指定发送数据的格式
                    MediaType JSON = MediaType.get("application/json; charset=utf-8");
                    // 创建RequestBody对象，并将JSON数据放入
                    RequestBody requestBody = RequestBody.create(JsonUtils.convert2Json(data), JSON);
                    builder.method(method, requestBody);
                }
                if (headers != null) {
                    for (String key : headers.keySet()) {
                        String value = headers.get(key);
                        builder.addHeader(key, value);
                    }
                }
                Request request = builder.build();
                //准备发送请求
                Call call = client.newCall(request);
                //执行请求
                Response response = call.execute();
                return response;
            }
        });
        return future;
    }

    public static String transParams(Map<String, Object> params) {
        StringJoiner result = new StringJoiner("&");
        try {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String propName = entry.getKey();
                Object value = entry.getValue();
                String part = URLEncoder.encode(propName, "UTF-8") + "=";
                if (value != null && !value.equals("") && !value.equals("undefined")) {
                    if (value instanceof Map) {
                        Map<String, Object> valueMap = (Map<String, Object>) value;
                        for (Map.Entry<String, Object> subEntry : valueMap.entrySet()) {
                            String subPropName = propName + '[' + subEntry.getKey() + ']';
                            String subPart = URLEncoder.encode(subPropName, "UTF-8") + "=";
                            result.add(subPart + URLEncoder.encode(String.valueOf(subEntry.getValue()), "UTF-8"));
                        }
                    } else {
                        result.add(part + URLEncoder.encode(String.valueOf(value), "UTF-8"));
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
