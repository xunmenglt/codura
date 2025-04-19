package com.xunmeng.codura.actions.completion.handler;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.xunmeng.codura.service.CodeCompletionService;
import com.xunmeng.codura.service.DefaultService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CancelPressedActionHandler extends EditorWriteActionHandler {
    private CodeCompletionService codeCompletionService= DefaultService.getServiceByClass(CodeCompletionService.class);
    @Override
    public void executeWriteAction(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        codeCompletionService.abortCompletion();
    }

    @Override
    protected boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
        return super.isEnabledForCaret(editor, caret, dataContext);
    }
}
