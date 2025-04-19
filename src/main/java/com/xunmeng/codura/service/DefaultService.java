package com.xunmeng.codura.service;

import com.intellij.openapi.application.ApplicationManager;

public class DefaultService {
    public static <T> T getServiceByClass(Class<T> clazz){
        return ApplicationManager.getApplication().getService(clazz);
    }
}
