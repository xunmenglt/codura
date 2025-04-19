package com.xunmeng.codura.listeners;

import com.intellij.codeInsight.editorActions.CopyPastePreProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RawText;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.xunmeng.codura.setting.state.TimerMasterStateService;
import com.xunmeng.codura.system.pojo.UserUseInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 记录Ctrl+C和Ctrl+V的操作
 */
public class CopyPasteListener implements CopyPastePreProcessor {
    private TimerMasterStateService state = TimerMasterStateService.settings();
    @Override
    public @Nullable String preprocessOnCopy(PsiFile file, int[] startOffsets, int[] endOffsets, String text) {
        UserUseInfo userUseInfo = state.initAndGetUserUseInfo();
        userUseInfo.setCtrlCCount(userUseInfo.getCtrlCCount()+1);
        return null;
    }

    @Override
    public @NotNull String preprocessOnPaste(Project project, PsiFile file, Editor editor, String text, RawText rawText) {
        UserUseInfo userUseInfo = state.initAndGetUserUseInfo();
        userUseInfo.setCtrlVCount(userUseInfo.getCtrlVCount()+1);
        if (text==null){
            return "";
        }
        return text;
    }
}
