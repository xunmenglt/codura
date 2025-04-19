package com.xunmeng.codura.actions.editormenu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.xunmeng.codura.constants.CodeIcons;
import com.xunmeng.codura.service.ChatService;
import com.xunmeng.codura.service.DefaultService;
import com.xunmeng.codura.system.logs.constans.AIUsageType;
import com.xunmeng.codura.utils.CodeBundle;
import org.jetbrains.annotations.NotNull;

/**
 * 重构代码内容
 */
public class CodeRefactorAction extends AnAction {
    public static final String ID="CodeRefactorAction";
    public static String text= CodeBundle.message("action.CodeRefactorAction.text");
    public static String description= CodeBundle.message("action.CodeRefactorAction.description");
    public CodeRefactorAction(){
//        super(text,description,CodeAlienIcons.TOOL_LOGO);
        super(text,description, CodeIcons.TOOL_LOGO);
        
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DefaultService.getServiceByClass(ChatService.class).streamTemplateCompletion("refactor",AIUsageType.CODE_REFACTORING, CodeBundle.message("action.CodeRefactorAction.text"),null,false);
    }
    public boolean displayTextInToolbar() {
        return !DefaultService.getServiceByClass(ChatService.class).isGeneratingText();
    }
}
