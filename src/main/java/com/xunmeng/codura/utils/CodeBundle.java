package com.xunmeng.codura.utils;


import com.intellij.DynamicBundle;
import org.jetbrains.annotations.PropertyKey;

public final class CodeBundle extends DynamicBundle {
    private static final String BUNDLE="codura.messages.CodeBundle";
    private static DynamicBundle bundle=new CodeBundle();
    
    public CodeBundle(){
        super(BUNDLE);
    }
    public static String message(@PropertyKey(resourceBundle = BUNDLE)String key, Object params){
        return bundle.getMessage(key,params);
    }
    public static String message(@PropertyKey(resourceBundle = BUNDLE)String key){
        return message(key,null);
    }
}
