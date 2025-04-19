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

public class SSEHttpClient {
    public static final Logger logger = Logger.getLogger(SSEHttpClient.class.getName());
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
                // 判断是否成功建立连接
                if (response.code() < 200 || response.code() > 300) {
                    if (streamResquest != null) {
                        streamResquest.getResponseCallable().onError(new RuntimeException("Server responded with status code: %d".formatted(response.code())));
                    }
                }
                // 判断是否有可读流
                if (response.body() == null) {
                    if (streamResquest != null) {
                        streamResquest.getResponseCallable().onError(new RuntimeException("Failed to get a ReadableStream from the response"));
                    }
                }
                // 说明可以进行处理了
                streamResquest.getResponseCallable().onStart(call);
                StringBuilder buffer = new StringBuilder();
                BufferedReader reader = new BufferedReader(response.body().charStream());
                char[] charBuf = new char[256];
                int offset = 0;
                try {
                    while ((offset = reader.read(charBuf)) != -1 && !call.isCanceled()) {
                        buffer.append(charBuf, 0, offset);
                        int position = -1;
                        String splitTig = "\n\n";
                        while ((position = buffer.indexOf(splitTig)) != -1) {
                            String line = buffer.substring(0, position);
                            buffer.delete(0, position + splitTig.length());
                            try {
                                JsonNode jsonNode = safeParseJsonResponse(line);
                                if (jsonNode != null) streamResquest.getResponseCallable().onData(jsonNode);
                            } catch (Exception e) {
                                if (streamResquest != null) {
                                    streamResquest.getResponseCallable().onError(e);
                                }
                            }
                        }
                    }
                    if (!buffer.isEmpty()) {
                        try {
                            JsonNode jsonNode = safeParseJsonResponse(buffer.toString());
                            if (jsonNode != null) streamResquest.getResponseCallable().onData(jsonNode);
                        } catch (Exception e) {
                            if (streamResquest != null) {
                                streamResquest.getResponseCallable().onError(e);
                            }
                        }
                    }
                } catch (Exception e) {
//                    System.out.println(e);
//                    e.printStackTrace();
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
            throw new RuntimeException("Error parsing JSON data from event");
        }
    }

    private boolean isStreamWithDataPrefix(String str) {
        return str.startsWith(DATA_PREFIX);
    }

    public void dispose() {
        if (call != null) {
            // 取消执行
            call.cancel();
        }
    }
}
