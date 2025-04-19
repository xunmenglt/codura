package com.xunmeng.codura.net.resquest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.xunmeng.codura.utils.JsonUtils;

public class RequestBodyBase {
    
    public String toJsonStr() {
        String json = null;
        try {
            json = JsonUtils.convert2Json(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
        return json;
    }

}
