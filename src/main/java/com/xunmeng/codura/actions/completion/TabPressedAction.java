package com.xunmeng.codura.actions.completion;

import com.intellij.codeInsight.hint.HintManagerImpl.ActionToIgnore;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.xunmeng.codura.actions.completion.handler.TabPressedActionHandler;


/**
 * 接收补全action，快捷键Tab
 */
public class TabPressedAction extends EditorAction implements ActionToIgnore {
    public static final String ACTION_ID = "TabPressedAction";
    public TabPressedAction() {
        super(new TabPressedActionHandler());
    }
}
