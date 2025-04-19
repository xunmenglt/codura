package com.xunmeng.codura.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xunmeng.codura.toolwin.sharedchat.events.ServerMessage;

public class EventUtils {
    public static String stringify(ServerMessage message) {
        try {
            return JsonUtils.convert2Json(message);
        } catch (JsonProcessingException e) {
            return "\"{}\"";
        }
    }
}
