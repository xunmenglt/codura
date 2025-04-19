package com.xunmeng.codura.editor;

import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.intellij.openapi.editor.Inlay;
import com.xunmeng.codura.constants.CodeCompletionType;

import java.util.List;

public interface CodeInlayRenderer extends EditorCustomElementRenderer {
    /*获取文本行列表*/
    public List<String> getContentLines();

    /*获取Inlay对象*/
    public Inlay<CodeInlayRenderer> getInlay();

    /*获取补全类型*/
    public CodeCompletionType getType();
}
