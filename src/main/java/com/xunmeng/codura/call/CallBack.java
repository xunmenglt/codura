package com.xunmeng.codura.call;

@FunctionalInterface
public interface CallBack<T> {
    void call(T t);
}
