package com.xunmeng.codura.listeners;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.event.BulkAwareDocumentListener;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.xunmeng.codura.service.CodeCompletionService;
import com.xunmeng.codura.service.DefaultService;
import com.xunmeng.codura.status.CodeStatus;
import com.xunmeng.codura.status.CodeStatusService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class DocumentListener implements BulkAwareDocumentListener, Disposable {
    private CodeCompletionService codeCompletionService = DefaultService.getServiceByClass(CodeCompletionService.class);

    @Override
    public void beforeDocumentChangeNonBulk(@NotNull DocumentEvent event) {
//        System.out.println("文档之前改变的内容："+event.getDocument().getText());
    }

    @Override
    public void documentChangedNonBulk(@NotNull DocumentEvent event) {
        Editor editor = codeCompletionService.getEditor();
        if (editor == null) return;
        // 清空内容
        CodeStatus codeStatus = CodeStatusService.getCurrentStatus().first;
        String lastCompletionText = codeCompletionService.getLastCompletionText();
        if (codeCompletionService.isCanTab() || ((codeStatus == CodeStatus.Done || codeStatus == CodeStatus.InProgress) && !StringUtils.isEmpty(lastCompletionText))) {
            codeCompletionService.clearRender();
        }
        codeCompletionService.abortCompletion();
        codeCompletionService.cancelTask();
        codeCompletionService.setCanTab(false);
        codeCompletionService.setLastCompletionText("");
        codeCompletionService.setAcceptedLastCompletion(false);
    }

    /*获取当前活动的Editor对象*/
    private Editor getActiveEditor(Document document) {
        if (!ApplicationManager.getApplication().isDispatchThread()) {
            return null;
        }
        Editor[] editors = EditorFactory.getInstance().getEditors(document);
        if (editors != null && editors.length > 0) {
            return editors[0];
        }
        return null;
    }

    @Override
    public void dispose() {
        // pass
    }
}
