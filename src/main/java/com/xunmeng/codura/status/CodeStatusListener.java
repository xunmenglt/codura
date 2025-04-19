package com.xunmeng.codura.status;

import com.intellij.util.messages.Topic;
import com.xunmeng.codura.utils.CodeBundle;

public interface CodeStatusListener {

    public static final String TOPIC_ID= CodeBundle.message("code.status.topic");
    public static Topic<CodeStatusListener> TOPIC= Topic.create(TOPIC_ID, CodeStatusListener.class);
    void onCodeAlienStatus(CodeStatus status, String icon);

}
