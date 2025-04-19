package com.xunmeng.codura.actions.completion.promoter;

import com.intellij.openapi.actionSystem.ActionPromoter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.xunmeng.codura.actions.completion.TabPressedAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于对TAB快捷键所对应的action进行控制，
 * 当Editor不为空时执行TabPressedAction对应action对象列表，并只执行其中列表中的第一个
 */
public class AcceptActionsPromoter implements ActionPromoter {
    @Override
    public @Nullable List<AnAction> promote(@NotNull List<? extends AnAction> actions, @NotNull DataContext context) {
        if (getEditor(context) == null)
            return actions.stream().collect(Collectors.toList());
        return actions.stream().filter((a)->{
            if (a instanceof TabPressedAction){
                return true;
            }
            return false;
        }).collect(Collectors.toList());
    }
    
    private Editor getEditor(DataContext dataContext) {
        return CommonDataKeys.EDITOR.getData(dataContext);
    }
}
