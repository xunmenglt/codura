package com.xunmeng.codura.listeners;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.xunmeng.codura.service.ChatService;
import com.xunmeng.codura.service.DefaultService;
import org.jetbrains.annotations.NotNull;

public class GlobalCaretListener implements CaretListener {
    @Override
    public void caretPositionChanged(@NotNull CaretEvent event) {
        // 获取对应的editor对象
        Editor editor = event.getEditor();
        DefaultService.getServiceByClass(ChatService.class).setEditor(editor);
    }
}
