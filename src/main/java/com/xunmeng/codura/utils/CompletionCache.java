package com.xunmeng.codura.utils;

import com.xunmeng.codura.pojo.PrefixSuffix;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.runtime.memoize.LRUCache;

public class CompletionCache {
    private int capacity;
    private LRUCache<String,String> cache;
    
    
    public CompletionCache(int capacity){
        this.capacity=capacity;
        this.cache=new LRUCache<String,String>(capacity);
    }

    private String normalize(String src) {
        if (!StringUtils.isEmpty(src)){
            return src.replaceAll("[\\s\\n]+","");
        }
        return "NULL";
    }

    public String getKey(PrefixSuffix prefixSuffix){
        if (!StringUtils.isEmpty(prefixSuffix.getSuffix())) {
            return this.normalize(prefixSuffix.getPrefix() + " #### " + prefixSuffix.getSuffix());
        }
        return this.normalize(prefixSuffix.getPrefix());
    }

    public String  getCache(PrefixSuffix prefixSuffix){
        String key = this.getKey(prefixSuffix);
        return this.cache.get(key);
    }

    public void setCache(PrefixSuffix prefixSuffix,String completion){
        String key = this.getKey(prefixSuffix);
        this.cache.put(key, completion);
    }
}
