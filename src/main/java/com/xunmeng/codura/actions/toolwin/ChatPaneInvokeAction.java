package com.xunmeng.codura.actions.toolwin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.xunmeng.codura.toolwin.CodeToolWindowFactory;
import org.jetbrains.annotations.NotNull;

import static com.xunmeng.codura.utils.LastUsedProject.getLastUsedProject;

public class ChatPaneInvokeAction extends AnAction {
    public ChatPaneInvokeAction(){
//        super(CodeAlienIcons.TOOL_LOGO);
        super();
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ToolWindow chat = ToolWindowManager.getInstance(getLastUsedProject()).getToolWindow("CodeAlien");
        chat.activate(()->{
            CodeToolWindowFactory.focusChat();
        });
    }
}
