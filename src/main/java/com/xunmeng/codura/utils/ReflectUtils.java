package com.xunmeng.codura.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class ReflectUtils {
    public static List<Field> getFieldListFromClazz(Class clazz){
        List<Field> fieldList=new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            fieldList.add(field);
        }
        Class superclass = clazz.getSuperclass();
        if (superclass!=null){
            for (Field field : superclass.getDeclaredFields()) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }
}
