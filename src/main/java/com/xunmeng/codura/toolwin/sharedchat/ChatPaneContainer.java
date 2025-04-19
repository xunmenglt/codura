package com.xunmeng.codura.toolwin.sharedchat;

import com.intellij.execution.ui.layout.impl.JBRunnerTabs;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.util.containers.ContainerUtil;
import com.xunmeng.codura.pojo.Conversation;
import com.xunmeng.codura.setting.provider.ConversationHistoryProvider;
import com.xunmeng.codura.setting.state.CodeStateService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 聊天面板容器
 */
public class ChatPaneContainer {
    private Project project;
    private Disposable parent;
    private String paneBaseName="Chat";
    
    private JBRunnerTabs panes;
    private JPanel holder=new JPanel(new BorderLayout());
    
    public ChatPaneContainer(Project project,Disposable parent){
        this.project=project;
        this.parent=parent;
        panes=new JBRunnerTabs(project,parent);
        setupPanes();
    }

    private void setupPanes() {
        ApplicationManager.getApplication().invokeLater(()->{
            // 先清空所有元素
            holder.removeAll();
            // 添加 panes
            holder.add(panes);
            restoreOrAddNew();
        });
    }

    private void restoreOrAddNew() {
        // 获取历史信息
        ConversationHistoryProvider conversationHistoryProvider = CodeStateService.settings().getConversationHistoryProvider();
        if (conversationHistoryProvider!=null){
            List<Conversation> conversationList = conversationHistoryProvider.getConversationList();
            if (conversationList!=null && conversationList.size()>0){
                for (Conversation conversation : conversationList) {
                    // 恢复历史会话
                    restoreTab(conversation);
                }
                return;
            }
        }
        addTab(null);
    }

    private void restoreTab(Conversation conversation) {
        ConversationHistoryProvider conversationHistoryProvider = CodeStateService.settings().getConversationHistoryProvider();
        SharedChatPane newPane = new SharedChatPane(project,conversation);
        JComponent component = newPane.getWebView().getComponent();
        TabInfo tabInfo = new TabInfo(component);
        tabInfo.setObject(newPane);
        String title = conversation.getTitle();
        if (StringUtils.isEmpty(title)){
            title="Chat";
        }else {
            if(title.length()>10){
                title=title.substring(0,3)+"...";
            }
        }
        tabInfo.setText(StringUtils.isEmpty(title) ?"Chat":title);
        newPane.restoreWhenReady(conversation.getId(),conversation.getMessages());
        Disposer.register(parent, newPane);
        // 添加按钮
        tabInfo.setActions(new DefaultActionGroup(new AnAction(AllIcons.General.Add) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                addTab(null);
            }

            @Override
            public @NotNull ActionUpdateThread getActionUpdateThread() {
                return ActionUpdateThread.EDT;
            }
        }), ActionPlaces.EDITOR_TAB);
        tabInfo.setTabLabelActions(new DefaultActionGroup(new AnAction(AllIcons.Actions.Close) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                newPane.handleStopGeneration();
                panes.removeTab(tabInfo);
                conversationHistoryProvider.deleteConversation(newPane.getId());
                if (getVisibleTabs().isEmpty()) {
                    addTab(null);
                }
            }

            @Override
            public @NotNull ActionUpdateThread getActionUpdateThread() {
                return ActionUpdateThread.EDT;
            }
        }),ActionPlaces.EDITOR_TAB);
        
        panes.addTab(tabInfo);
    }
    
    private void addTab(String title){
        ConversationHistoryProvider conversationHistoryProvider = CodeStateService.settings().getConversationHistoryProvider();
        Conversation conversation = new Conversation(title);
        conversationHistoryProvider.addConversation(conversation);
        SharedChatPane newPane = new SharedChatPane(project,conversation);
        JComponent component = newPane.getWebView().getComponent();
        TabInfo tabInfo = new TabInfo(component);
        tabInfo.setObject(newPane);
        tabInfo.setText(StringUtils.isEmpty(conversation.getTitle()) ?"Chat":conversation.getTitle());
        Disposer.register(parent, newPane);
        // 添加按钮
        tabInfo.setActions(new DefaultActionGroup(new AnAction(AllIcons.General.Add) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                addTab(null);
            }

            @Override
            public @NotNull ActionUpdateThread getActionUpdateThread() {
                return ActionUpdateThread.EDT;
            }
        }), ActionPlaces.EDITOR_TAB);
        tabInfo.setTabLabelActions(new DefaultActionGroup(new AnAction(AllIcons.Actions.Close) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                newPane.handleStopGeneration();
                panes.removeTab(tabInfo);
                conversationHistoryProvider.deleteConversation(newPane.getId());
                Disposer.dispose(newPane);
                if (getVisibleTabs().isEmpty()) {
                    addTab(null);
                }
            }
            @Override
            public @NotNull ActionUpdateThread getActionUpdateThread() {
                return ActionUpdateThread.EDT;
            }
        }),ActionPlaces.EDITOR_TAB);
        panes.addTab(tabInfo);
        panes.select(tabInfo, true);
    }


    public void requestFocus() {
        panes.requestFocus();
    }
    
    public List<TabInfo> getVisibleTabs(){
        return ContainerUtil.filter(panes.getTabs(),(tabInfo)->!tabInfo.isHidden());
    }

    public JComponent getComponent(){
        return holder;
    }

    public JBRunnerTabs getPanes() {
        return panes;
    }
}
