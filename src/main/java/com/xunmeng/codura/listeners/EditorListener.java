package com.xunmeng.codura.listeners;

import com.intellij.openapi.editor.event.*;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.xunmeng.codura.setting.state.TimerMasterStateService;
import com.xunmeng.codura.system.pojo.UserUseInfo;
import org.jetbrains.annotations.NotNull;

import java.util.*;

// 全局监听编辑器的变化
public class EditorListener implements EditorFactoryListener, BulkAwareDocumentListener, CaretListener {
    private TimerMasterStateService state = TimerMasterStateService.settings();
    private Set<String> fileSet=new HashSet<>();
    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {
        VirtualFile file = FileDocumentManager.getInstance().getFile(event.getEditor().getDocument());
        if (file==null) return;
        
        if (fileSet.contains(file.getPath())){
            return;
        }
        fileSet.add(file.getPath());
        // 监听编辑操作
        event.getEditor().getDocument().addDocumentListener(this);
        // 监听光标移动事件
        event.getEditor().getCaretModel().addCaretListener(this);
    }

    @Override
    public void caretPositionChanged(@NotNull CaretEvent event) {
        state.initAndGetUserUseInfo();
    }

    @Override
    public void documentChangedNonBulk(@NotNull DocumentEvent event) {
        UserUseInfo userUseInfo = state.initAndGetUserUseInfo();
        if ((event.getOldFragment().length() > 0 || event.getNewFragment().length() > 0) || !event.isWholeTextReplaced()) {
            // 只对字符长度为 1 和非空空白符的情况进行统计
            if (event.getNewFragment().length() > 0 &&(event.getNewFragment().length() == 1 || event.getNewFragment().toString().trim().isEmpty())) {
                userUseInfo.setTotalKeyboardInputs(userUseInfo.getTotalKeyboardInputs()+1);
            }

            // 根据文档代码段变更信息判断是新增还是删除行
            if (event.getOldFragment().toString().contains("\n")) {
                userUseInfo.setDeletedCodeLines(userUseInfo.getDeletedCodeLines()+(int) event.getOldFragment().chars().filter(ch -> ch == '\n').count());
            }
            
            if (event.getNewFragment().toString().contains("\n")) {
                userUseInfo.setAddedCodeLines(userUseInfo.getAddedCodeLines()+(int) event.getNewFragment().chars().filter(ch -> ch == '\n').count());
            }
        }

    }
}
