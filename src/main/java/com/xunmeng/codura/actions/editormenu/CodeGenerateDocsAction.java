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
 * 编写开发文档
 */
public class CodeGenerateDocsAction extends AnAction {
    public static final String ID="CodeGenerateDocsAction";
    public static String text= CodeBundle.message("action.CodeGenerateDocsAction.text");
    public static String description= CodeBundle.message("action.CodeGenerateDocsAction.description");
    public CodeGenerateDocsAction(){
//        super(text,description, CodeAlienIcons.TOOL_LOGO);
        
        super(text,description, CodeIcons.TOOL_LOGO);
        
    }
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DefaultService.getServiceByClass(ChatService.class).streamTemplateCompletion("generate-docs",AIUsageType.DOCUMENTATION_WRITING, CodeBundle.message("action.CodeGenerateDocsAction.text"),null,false);
    }
    public boolean displayTextInToolbar() {
        return !DefaultService.getServiceByClass(ChatService.class).isGeneratingText();
    }
}
