package com.xunmeng.codura.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.util.Disposer;
import com.xunmeng.codura.constants.CodeCompletionType;
import com.xunmeng.codura.editor.CodeDefaultInlayRenderer;
import com.xunmeng.codura.editor.InlayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class CodeCompletionAction extends AnAction {
    
    public static String CODE= "";
    
    /*代码补全action，action执行将会执行代码补全操作*/
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        InlayModel inlayModel = editor.getInlayModel();
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();// 偏移量
        inlayModel.getInlineElementsInRange(0,editor.getDocument().getTextLength()).forEach((it-> Disposer.dispose(it)));
        inlayModel.getBlockElementsInRange(0,editor.getDocument().getTextLength()).forEach((it-> Disposer.dispose(it)));
        List<String> lines = Arrays.asList(CODE.split("\n"));
        List<String> lineLiens=lines.subList(0,1);
        CodeDefaultInlayRenderer lineRenderer = new CodeDefaultInlayRenderer(CodeCompletionType.Block,
                lineLiens);
        inlayModel.addInlineElement(offset, InlayUtils.createInlineProperties(0),lineRenderer);
        List<String> blockLines=lines.subList(1,lines.size());
        CodeDefaultInlayRenderer blockRenderer = new CodeDefaultInlayRenderer(CodeCompletionType.Block,
                blockLines);
        inlayModel.addBlockElement(offset, InlayUtils.createBlockProperties(0),blockRenderer);
        // 移动光标位置到初始位置
        caretModel.moveToOffset(offset);
    }
}
