package com.xunmeng.codura.actions.completion.handler;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.Disposer;
import com.xunmeng.codura.service.CodeCompletionService;
import com.xunmeng.codura.service.DefaultService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TabPressedActionHandler extends EditorWriteActionHandler {
    private CodeCompletionService codeCompletionService= DefaultService.getServiceByClass(CodeCompletionService.class);
    @Override
    public void executeWriteAction(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        InlayModel inlayModel = editor.getInlayModel();
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();// 偏移量
        String completion = codeCompletionService.getLastCompletionText();
        codeCompletionService.setLastCompletionText("");
        codeCompletionService.setCanTab(false);
        if (!StringUtils.isEmpty(completion) && !codeCompletionService.isAcceptedLastCompletion()){
            codeCompletionService.setAcceptedLastCompletion(true);
            WriteCommandAction.runWriteCommandAction(editor.getProject(),()->{
                Document document = editor.getDocument();
                String content=completion;
                document.insertString(offset,content);
                caretModel.moveToOffset(caretModel.getOffset()+content.length());
                inlayModel.getInlineElementsInRange(0,editor.getDocument().getTextLength()).forEach((it-> Disposer.dispose(it)));
                inlayModel.getBlockElementsInRange(0,editor.getDocument().getTextLength()).forEach((it-> Disposer.dispose(it)));
            });
        }
    }

    @Override
    protected boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
        return codeCompletionService.isCanTab() && !StringUtils.isEmpty(codeCompletionService.getLastCompletionText());
    }
}
