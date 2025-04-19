package com.xunmeng.codura.toolwin.sharedchat.events.eventnames;

public enum ToChatEvent {
    InitConversation("init_conversation"),
    ON_COMPLETION("on_completion"),
    ON_END("on_end"),
    ON_GENERATE_LIMIT("on_generate_limit"),
    ADD_MESSAGE("add_message"),
    /*刷新用户信息*/
    FLASH_USER_INFO("flash_user_info"),
    LOGIN_FAIL("login_fail"),
    LOGOUT_FAIL("logout_fail"),
    LOGOUT_SUCCESS("logout_success"),
    AUTH_FAIL("auth_fail"),
    CLEAR_ERROR("chat_clear_error"),
    RESTORE_CHAT("restore_chat_from_history"),
    CHAT_RESPONSE("chat_response"),
    BACKUP_MESSAGES("back_up_messages"),
    DONE_STREAMING("chat_done_streaming"),
    ERROR_STREAMING("chat_error_streaming"),
    NEW_CHAT("create_new_chat"),
    RECEIVE_CAPS("receive_caps"),
    RECEIVE_CAPS_ERROR("receive_caps_error"),
    SET_CHAT_MODEL("chat_set_chat_model"),
    SET_DISABLE_CHAT("set_disable_chat"),
    ACTIVE_FILE_INFO("chat_active_file_info"),
    TOGGLE_ACTIVE_FILE("chat_toggle_active_file"),
    RECEIVE_AT_COMMAND_COMPLETION("chat_receive_at_command_completion"),
    RECEIVE_AT_COMMAND_PREVIEW("chat_receive_at_command_preview"),
    SET_SELECTED_AT_COMMAND("chat_set_selected_command"),
    SET_LAST_MODEL_USED("chat_set_last_model_used"),

    SET_SELECTED_SNIPPET("chat_set_selected_snippet"),
    REMOVE_PREVIEW_FILE_BY_NAME("chat_remove_file_from_preview"),
    SET_PREVIOUS_MESSAGES_LENGTH("chat_set_previous_messages_length"),
    RECEIVE_TOKEN_COUNT("chat_set_tokens"),
    RECEIVE_PROMPTS("chat_receive_prompts"),
    RECEIVE_PROMPTS_ERROR("chat_receive_prompts_error"),
    SET_SELECTED_SYSTEM_PROMPT("chat_set_selected_system_prompt"),
    RECEIVE_CONFIG_UPDATE("receive_config_update"),
    RECEIVE_TOOLS("chat_receive_tools_chat");

    private String value;

    ToChatEvent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}