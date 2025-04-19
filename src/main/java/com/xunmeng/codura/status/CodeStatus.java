package com.xunmeng.codura.status;

import com.xunmeng.codura.constants.CodeIcons;

import javax.swing.*;

public enum CodeStatus {
    WAITING(CodeIcons.DARK),
    Ready(CodeIcons.AI_COPILOT),
    InProgress(CodeIcons.IntProgress),
    Error(CodeIcons.ERROR),
    Done(CodeIcons.AI_COPILOT);

    private Icon icon;

    CodeStatus(Icon icon) {
        this.icon=icon;
    }

    public Icon getIcon() {
        return icon;
    }
}
