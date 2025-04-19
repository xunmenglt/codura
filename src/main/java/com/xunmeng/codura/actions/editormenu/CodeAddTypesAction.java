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
 * 变量类型声明
 */
public class CodeAddTypesAction extends AnAction {
    public static final String ID="CodeAddTypesAction";
    public static String text= CodeBundle.message("action.CodeAddTypesAction.text");
    public static String description= CodeBundle.message("action.CodeAddTypesAction.description");
    public CodeAddTypesAction(){
//        super(text,description, CodeAlienIcons.TOOL_LOGO);
        super(text,description, CodeIcons.TOOL_LOGO);
        
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DefaultService.getServiceByClass(ChatService.class).streamTemplateCompletion("add-types",AIUsageType.VARIABLE_TYPE_DECLARATION, CodeBundle.message("action.CodeAddTypesAction.text"),null,false);
    }
    public boolean displayTextInToolbar() {
        return !DefaultService.getServiceByClass(ChatService.class).isGeneratingText();
    }
}
