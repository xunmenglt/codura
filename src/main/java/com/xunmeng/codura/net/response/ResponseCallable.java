package com.xunmeng.codura.net.response;

public interface ResponseCallable {
    <T> void onStart(T item);
    <D> void onData(D result);
    <E> void onEnd(E item);
    void onError(Throwable throwable);
}
