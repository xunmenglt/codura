package com.xunmeng.codura.net.resquest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.xunmeng.codura.utils.JsonUtils;
import lombok.Data;

import java.util.UUID;
@Data
public class RequestBodyBase {

    @JsonProperty("requestId" )
    private String requestId ;
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
