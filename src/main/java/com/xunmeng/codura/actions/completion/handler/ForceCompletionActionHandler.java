package com.xunmeng.codura.actions.completion.handler;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.xunmeng.codura.service.CodeCompletionService;
import com.xunmeng.codura.service.DefaultService;
import com.xunmeng.codura.status.CodeStatus;
import com.xunmeng.codura.status.CodeStatusService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 用于手动触发代码补全操作
 */
public class ForceCompletionActionHandler extends EditorWriteActionHandler {
    private static final Logger logger=Logger.getInstance(ForceCompletionActionHandler.class);
    private CodeCompletionService codeCompletionService= DefaultService.getServiceByClass(CodeCompletionService.class);
    

    @Override
    public void executeWriteAction(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        CodeStatus codeStatus = CodeStatusService.getCurrentStatus().first;
        if (codeStatus != CodeStatus.InProgress){
            try {
                codeCompletionService.provideInlineCompletionItems(editor,dataContext);
            }catch (Exception e){
                logger.error(e);
                CodeStatusService.notifyApplication(CodeStatus.Error,null);
            }
        }
    }

    @Override
    protected boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
        return true;
    }
}
