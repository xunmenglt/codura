package com.xunmeng.codura.actions.completion.promoter;

import com.intellij.openapi.actionSystem.ActionPromoter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.xunmeng.codura.actions.completion.CancelPressedAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class CancelActionsPromoter implements ActionPromoter {
    @Override
    public @Nullable List<AnAction> promote(@NotNull List<? extends AnAction> actions, @NotNull DataContext context) {
        if (getEditor(context) == null)
            return actions.stream().collect(Collectors.toList());
        return actions.stream().filter((a)->{
            if (a instanceof CancelPressedAction){
                return true;
            }
            return false;
        }).collect(Collectors.toList());
    }
    
    private Editor getEditor(DataContext dataContext) {
        return CommonDataKeys.EDITOR.getData(dataContext);
    }
}
