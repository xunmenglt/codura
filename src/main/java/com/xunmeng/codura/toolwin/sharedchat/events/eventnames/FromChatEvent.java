package com.xunmeng.codura.toolwin.sharedchat.events.eventnames;

public enum FromChatEvent {
    READY("chat_ready"),
    CHAT_MESSAGE("chat_message"),
    STOP_GENERATION("stop_generation"),
    SAVE_CONVERSATION("save_conversation"),
    /*登录事件*/
    LOGIN("login"),
    LOGOUT("logout"),
    GET_USER_INFO("get_user_info"),
    /*用户退出事件*/
    SAVE_CHAT("save_chat_to_history"),
    ASK_QUESTION("chat_question"),
    REQUEST_CAPS("chat_request_caps"),
    STOP_STREAMING("chat_stop_streaming"),
    BACK_FROM_CHAT("chat_back_from_chat"),
    OPEN_IN_CHAT_IN_TAB("open_chat_in_new_tab"),
    SEND_TO_SIDE_BAR("chat_send_to_sidebar"),
    
    NEW_FILE("chat_create_new_file"),
    PASTE_DIFF("chat_paste_diff"),
    REQUEST_AT_COMMAND_COMPLETION("chat_request_at_command_completion"),
    REQUEST_PREVIEW_FILES("chat_request_preview_files"),
    REQUEST_PROMPTS("chat_request_prompts"),
    REQUEST_TOOLS("chat_request_has_tool_check"),
    OPEN_SETTINGS("chat_open_settings");

    private String value;

    FromChatEvent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public static FromChatEvent valueToObj(String value){
        FromChatEvent[] values = FromChatEvent.values();
        for (FromChatEvent fromChatEvent : values) {
            if (fromChatEvent.getValue().equals(value)){
                return fromChatEvent;
            }
        }
        return null;
    }
}