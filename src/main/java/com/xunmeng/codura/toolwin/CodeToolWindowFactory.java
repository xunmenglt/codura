package com.xunmeng.codura.toolwin;

import com.intellij.execution.ui.layout.impl.JBRunnerTabs;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.tabs.TabInfo;
import com.xunmeng.codura.constants.Constants;
import com.xunmeng.codura.toolwin.sharedchat.ChatPaneContainer;
import com.xunmeng.codura.toolwin.sharedchat.SharedChatPane;
import org.jetbrains.annotations.NotNull;

import static com.xunmeng.codura.utils.LastUsedProject.getLastUsedProject;

public class CodeToolWindowFactory implements ToolWindowFactory, DumbAware {
    private static Key<ChatPaneContainer> panesKey = new Key("code.panes");

    @Override
    public boolean isApplicable(@NotNull Project project) {
        try {
            return JBCefApp.isSupported();
        }catch (Exception e){
            return false;
        }
    }
    

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.getInstance();
        ChatPaneContainer chatPaneContainer = new ChatPaneContainer(project,toolWindow.getDisposable());
        Content content = contentFactory.createContent(chatPaneContainer.getComponent(), "Chat", false);
        content.setCloseable(false);
        content.putUserData(panesKey,chatPaneContainer);
        toolWindow.getContentManager().addContent(content);
    }

    public static ChatPaneContainer getChatPaneContainer(){
        ToolWindow toolWindow = ToolWindowManager.getInstance(getLastUsedProject()).getToolWindow(Constants.TOOL_WIN_NAME);
        ChatPaneContainer chatPaneContainer = toolWindow.getContentManager().findContent("Chat").getUserData(panesKey);
        return chatPaneContainer;
    }

    // 获取当前聚焦的panel
    public static SharedChatPane getCurrentFocusChatPane(){
        ToolWindow toolWindow = ToolWindowManager.getInstance(getLastUsedProject()).getToolWindow(Constants.TOOL_WIN_NAME);
        ChatPaneContainer chatPaneContainer = toolWindow.getContentManager().findContent("Chat").getUserData(panesKey);
        JBRunnerTabs panes = chatPaneContainer.getPanes();
        if (panes!=null){
            TabInfo selectedInfo = panes.getSelectedInfo();
            SharedChatPane chatPane = (SharedChatPane) selectedInfo.getObject();
            return chatPane;
        }
        return null;
    }
    
    public static void focusChat() {
        // 获取 ID为CodeAlien的ToolWindow
        ToolWindow tw = ToolWindowManager.getInstance(getLastUsedProject()).getToolWindow(Constants.TOOL_WIN_NAME);
        if (tw!=null){
            ContentManager contentManager = tw.getContentManager();
            if (contentManager!=null){
                // 获取Content
                Content content = contentManager.findContent("Chat");
                if (content==null) return;
                // 选择该Content
                tw.getContentManager().setSelectedContent(content,true);
                ChatPaneContainer panes = content.getUserData(panesKey);
                panes.requestFocus();
            }
        }
    }
}
