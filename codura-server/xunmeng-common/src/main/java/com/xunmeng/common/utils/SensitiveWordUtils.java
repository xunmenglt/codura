package com.xunmeng.common.utils;

import com.github.houbb.sensitive.word.api.IWordContext;
import com.github.houbb.sensitive.word.api.IWordReplace;
import com.github.houbb.sensitive.word.api.IWordResult;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.github.houbb.sensitive.word.utils.InnerWordCharUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SensitiveWordUtils {
    
    /*将文本敏感内容高亮*/
    public static String highlightSensitiveWord(String text){
        return SensitiveWordHelper.replace(text, new IWordReplace() {
            @Override
            public void replace(StringBuilder stringBuilder, char[] rawChars, IWordResult wordResult, IWordContext iWordContext) {
                // 获取敏感词
                String sensitiveWord = InnerWordCharUtils.getString(rawChars, wordResult);
                stringBuilder.append("<font color='red'>"+sensitiveWord+"</font>");
            }
        });
    }
    
    /*返回敏感词列表*/
    public static Set<String> findSensitiveWordInText(String text){
        List<String> res = SensitiveWordHelper.findAll(text);
        HashSet<String> resSet = new HashSet<>();
        resSet.addAll(res);
        return resSet;
    }
}
