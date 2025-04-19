package com.xunmeng.codura.toolwin.sharedchat.browser;

import com.xunmeng.codura.toolwin.sharedchat.events.ClientMessage;

@FunctionalInterface
public interface FromChatHandler {
    void accept(ClientMessage clientMessage);
}
