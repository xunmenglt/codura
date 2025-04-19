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
 * 编写测试用例
 */
public class CodeAddTestsAction extends AnAction {
    
    public static final String ID="CodeAddTestsAction";
    public static String text= CodeBundle.message("action.CodeAddTestsAction.text");
    public static String description= CodeBundle.message("action.CodeAddTestsAction.description");
    public CodeAddTestsAction(){
//        super(text,description, CodeAlienIcons.TOOL_LOGO);
        super(text,description, CodeIcons.TOOL_LOGO);
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DefaultService.getServiceByClass(ChatService.class).streamTemplateCompletion("add-tests",AIUsageType.TEST_CASE_WRITING, CodeBundle.message("action.CodeAddTestsAction.text"),null,false);
    }

    @Override
    public boolean displayTextInToolbar() {
        return !DefaultService.getServiceByClass(ChatService.class).isGeneratingText();
    }
}
