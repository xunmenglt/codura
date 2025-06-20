package com.xunmeng.codura.net;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xunmeng.codura.net.resquest.StreamResquest;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSEHttpClient {

    public static final Logger logger = Logger.getLogger(SSEHttpClient.class.getName());

    private static final Pattern EVENT_PATTERN = Pattern.compile("(?s)(.*?)\\r?\\n\\r?\\n");

    public static final String DATA_PREFIX = "data: ";
    private Call call;

    private StreamResquest streamResquest;

    public SSEHttpClient(StreamResquest streamResquest) {
        this.streamResquest = streamResquest;
        this.call = createCall();
    }

    public Call getCall() {
        return call;
    }

    private Call createCall() {

        String url = "%s://%s:%s%s".formatted(
                streamResquest.getOptions().getProtocol(),
                streamResquest.getOptions().getHostname(),
                streamResquest.getOptions().getPort(),
                streamResquest.getOptions().getPath()
        );
        logger.info(
                "\nRequest Body ==> "
                        + this.streamResquest.getBody().toJsonStr()
                        + "\n"
                        + "Request Header ==> "
                        + this.streamResquest.getOptions().getHeaders()
        );
        RequestBody requestBody = RequestBody.create(this.streamResquest.getBody().toJsonStr(),
                MediaType.parse("application/json"));
        // 设置请求体
        Request.Builder requestBuilder = new Request.Builder().url(url).method(this.streamResquest.getOptions().getMethod(), requestBody);
        // 添加请求头
        for (String key : streamResquest.getOptions().getHeaders().keySet()) {
            requestBuilder.addHeader(key, streamResquest.getOptions().getHeaders().get(key));
        }
        Request request = requestBuilder.build();
        // 创建 OkHttpClient 并设置超时
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)   // 设置连接超时
                .writeTimeout(300, TimeUnit.SECONDS)     // 设置写入超时
                .readTimeout(300, TimeUnit.SECONDS)      // 设置读取超时
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                dispose();
                if (streamResquest != null) {
                    streamResquest.getResponseCallable().onError(e);
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() < 200 || response.code() > 300) {
                    if (streamResquest != null) {
                        streamResquest.getResponseCallable().onError(
                                new RuntimeException("模型API服务接口请求异常,状态码: %d".formatted(response.code())));
                    }
                    return;
                }

                if (response.body() == null) {
                    if (streamResquest != null) {
                        streamResquest.getResponseCallable().onError(
                                new RuntimeException("模型API服务不符合流式请求规范"));
                    }
                    return;
                }

                streamResquest.getResponseCallable().onStart(call);
                StringBuilder buffer = new StringBuilder();
                BufferedReader reader = new BufferedReader(response.body().charStream());
                char[] charBuf = new char[256];
                int offset;

                try {
                    while ((offset = reader.read(charBuf)) != -1 && !call.isCanceled()) {
                        buffer.append(charBuf, 0, offset);

                        String fullBuffer = buffer.toString();
                        Matcher matcher = EVENT_PATTERN.matcher(fullBuffer);
                        int lastEnd = 0;

                        while (matcher.find()) {
                            String event = matcher.group(1);
                            try {
                                JsonNode jsonNode = safeParseJsonResponse(event);
                                if (jsonNode != null) {
                                    streamResquest.getResponseCallable().onData(jsonNode);
                                }
                            } catch (Exception e) {
                                streamResquest.getResponseCallable().onError(e);
                            }
                            lastEnd = matcher.end();
                        }

                        // 移除已消费部分，保留不完整的
                        buffer.delete(0, lastEnd);
                    }

                    if (!buffer.isEmpty()) {
                        try {
                            JsonNode jsonNode = safeParseJsonResponse(buffer.toString());
                            if (jsonNode != null) {
                                streamResquest.getResponseCallable().onData(jsonNode);
                            }
                        } catch (Exception e) {
                            streamResquest.getResponseCallable().onError(e);
                        }
                    }
                } catch (Exception e) {
                    streamResquest.getResponseCallable().onError(e);
                } finally {
                    dispose();
                    if (streamResquest.getResponseCallable() != null) {
                        streamResquest.getResponseCallable().onEnd(call);
                    }
                }
            }

        });
        return call;
    }

    private JsonNode safeParseJsonResponse(String stringBuffer) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (isStreamWithDataPrefix(stringBuffer)) {
                return objectMapper.readTree(stringBuffer.split(DATA_PREFIX)[1]);
            }
            JsonNode jsonNode = objectMapper.readTree(stringBuffer);
            return jsonNode;
        } catch (Exception e) {
            return null;
//            throw new RuntimeException("Error parsing JSON data from event");
        }
    }

    private boolean isStreamWithDataPrefix(String str) {
        return str.startsWith(DATA_PREFIX);
    }

    public void dispose() {
        synchronized (call){
            if (call != null && !call.isCanceled()) {
                // 取消执行
                call.cancel();
            }
        }
    }
}
