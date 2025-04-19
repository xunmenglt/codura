package com.xunmeng.codura.io;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.xunmeng.codura.net.SSEHttpClient;
import com.xunmeng.codura.net.constants.Method;
import com.xunmeng.codura.net.options.RequestOptions;
import com.xunmeng.codura.net.response.ResponseCallable;
import com.xunmeng.codura.net.resquest.StreamResquest;
import com.xunmeng.codura.llm.openai.message.Message;
import com.xunmeng.codura.llm.openai.request.StreamRequestBodyChatOpenAI;
import com.xunmeng.codura.llm.openai.request.StreamRequestBodyCompletionOpenAI;
import com.xunmeng.codura.llm.openai.response.StreamResponseChatDataItem;
import com.xunmeng.codura.llm.openai.response.StreamResponseCompletionDataItem;
import com.xunmeng.codura.utils.JsonUtils;
import okhttp3.Call;
import org.junit.Test;

public class SSETest {

    @Test
    public void chatTest() throws InterruptedException {
        StreamRequestBodyChatOpenAI body = new StreamRequestBodyChatOpenAI();
        body.setModel("qwen-turbo");
        body.setMax_tokens(1024);
        body.addMessage(new Message("system","你是一个代码小助手"))
                .addMessage(new Message("assistant","帮我写一个冒泡排序"));
        RequestOptions options = new RequestOptions();
        options.setHostname("dashscope.aliyuncs.com")
                .setProtocol("https")
                .setPort(443)
                .setPath("/compatible-mode/v1/chat/completions")
                .setMethod(Method.POST)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization","Bearer xxx");
        StreamResquest streamResquest = new StreamResquest(body,options,new ResponseCallable() {
            @Override
            public <Call> void onStart(Call item) {
                System.out.println("===>数据开始接收：" + item);
            }

            @Override
            public <D> void onData(D result) {
                StreamResponseChatDataItem dataItem=null;
                try {
                    dataItem = JsonUtils.tree2Object((JsonNode) result, StreamResponseChatDataItem.class);
                } catch (JsonProcessingException e) {
                    System.out.println(e);
                    System.out.println("\n===>解析失败"+result);
                }
                String content = dataItem.contentValue();
                System.out.printf(content);
            }

            @Override
            public <E> void onEnd(E item) {
                System.out.println("\n===>生成结束");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("\n===>异常：" + throwable.toString());
            }
        });
        Call call=null;
        SSEHttpClient sseHttpClient = new SSEHttpClient(streamResquest);
        call=sseHttpClient.getCall();
        while (!call.isCanceled()){
            Thread.sleep(500);
        }
    }

    @Test
    public void completionsTest() throws InterruptedException {
        StreamRequestBodyCompletionOpenAI body = new StreamRequestBodyCompletionOpenAI();
        body.setModel("CodeLlama-7b-hf");
        body.setMax_tokens(256);
        String prompt= """
                <fim_prefix>def quicksort(arr):
                    if len(arr) <= 1:
                        return arr
                    pivot = arr[len(arr) // 2]
                    <fim_suffix>
                    middle = [x for x in arr if x == pivot]
                    right = [x for x in arr if x > pivot]
                    return quicksort(left) + middle + quicksort(right)<fim_middle>
                """;
        body.setPrompt(prompt.trim());
        RequestOptions options = new RequestOptions();
        options.setHostname("localhost")
                .setProtocol("http")
                .setPort(8000)
                .setPath("/v1/completions")
                .setMethod(Method.POST)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization","Bearer xxx");
        StreamResquest streamResquest = new StreamResquest(body,options,new ResponseCallable() {
            @Override
            public <Call> void onStart(Call item) {
                System.out.println("===>数据开始接收：" + item);
            }

            @Override
            public <D> void onData(D result) {
                StreamResponseCompletionDataItem dataItem=null;
                try {
                    dataItem = JsonUtils.tree2Object((JsonNode) result, StreamResponseCompletionDataItem.class);
                } catch (JsonProcessingException e) {
                    System.out.println(e);
                    System.out.println("\n===>解析失败"+result);
                }
                String content = dataItem.contentValue();
                System.out.printf(content);
            }

            @Override
            public <E> void onEnd(E item) {
                System.out.println("\n===>生成结束");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("\n===>异常：" + throwable.toString());
            }
        });
        Call call=null;
        SSEHttpClient sseHttpClient = new SSEHttpClient(streamResquest);
        call=sseHttpClient.getCall();
        while (!call.isCanceled()){
            Thread.sleep(500);
        }
    }

    @Test
    public void requestTest() throws JsonProcessingException {
        StreamRequestBodyChatOpenAI streamRequestBodyChatOpenAI = new StreamRequestBodyChatOpenAI();
        streamRequestBodyChatOpenAI
                .addMessage(new Message("user","中国四大发明"))
                .addMessage(new Message("assistant","指南针，造纸术"))
                .addMessage(new Message("user","你少了两个，哼，请重新回答我！"));
        String json = JsonUtils.convert2Json(streamRequestBodyChatOpenAI);
        System.out.println(json);

    }
}
