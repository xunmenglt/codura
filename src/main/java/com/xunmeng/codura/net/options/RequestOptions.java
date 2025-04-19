package com.xunmeng.codura.net.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xunmeng.codura.net.constants.Method;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RequestOptions {
    @JsonProperty(value = "hostname")
    private String hostname="0.0.0.0";
    @JsonProperty(value = "path",required = true)
    private String path;
    @JsonProperty(value = "port")
    private int port=80;
    @JsonProperty(value = "protocol")
    private String protocol="http";
    
    @JsonProperty(value = "method")
    private String method= Method.GET;
    
    private Map<String,String> headers;
    
    public RequestOptions addHeader(String key,String value){
        if (headers==null){
            headers=new HashMap<>();
        }
        headers.put(key,value);
        return this;
    }

    public Map<String, String> getHeaders() {
        if (headers==null){
            headers=new HashMap<>();
            addHeader("Content-Type","application/json");
        }
        return headers;
    }
}
