package com.xunmeng.codura.actions.completion;

import com.intellij.codeInsight.hint.HintManagerImpl.ActionToIgnore;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.xunmeng.codura.actions.completion.handler.CancelPressedActionHandler;

public class CancelPressedAction extends EditorAction implements ActionToIgnore {
    public static final String ACTION_ID= "CancelPressedAction";
    public CancelPressedAction(){
        super(new CancelPressedActionHandler());
    }
}
