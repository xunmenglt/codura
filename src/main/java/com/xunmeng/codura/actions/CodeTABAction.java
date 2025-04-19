package com.xunmeng.codura.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.InlayModel;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;

public class CodeTABAction extends AnAction {
    
    public static String CODE= "";
    
    /*代码补全action，action执行将会执行代码补全操作*/
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("动作触发");
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        InlayModel inlayModel = editor.getInlayModel();
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();// 偏移量
        WriteCommandAction.runWriteCommandAction(e.getProject(),()->{
            Document document = editor.getDocument();
            String content=CODE;
            document.insertString(offset,content);
            caretModel.moveToOffset(caretModel.getOffset()+content.length());
            inlayModel.getInlineElementsInRange(0,editor.getDocument().getTextLength()).forEach((it-> Disposer.dispose(it)));
            inlayModel.getBlockElementsInRange(0,editor.getDocument().getTextLength()).forEach((it-> Disposer.dispose(it)));
        });
        
    }
}
