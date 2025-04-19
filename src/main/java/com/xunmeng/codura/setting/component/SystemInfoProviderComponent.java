package com.xunmeng.codura.setting.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.xunmeng.codura.setting.component.impl.ProviderComponent;
import com.xunmeng.codura.setting.provider.UserInfoProvider;
import com.xunmeng.codura.setting.state.SystemInfoState;
import com.xunmeng.codura.system.apis.UserUseInfoAPI;
import com.xunmeng.codura.system.pojo.User;
import com.xunmeng.codura.utils.CodeBundle;
import com.xunmeng.codura.utils.JsonUtils;
import okhttp3.Response;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SystemInfoProviderComponent extends JPanel {
    private SystemInfoState systemInfoState;
    public SystemInfoProviderComponent(SystemInfoState systemInfoState){
        super();
        this.systemInfoState=systemInfoState;
        init();
    }

    private void init(){
        BoxLayout boxLayout = new BoxLayout(this,BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);
        if (systemInfoState!=null){
            this.add(createSystemInfoProvider());
            this.add(createUserInfoFimProvider());
            this.add(createOptionBtns());
        }
        ApplicationManager.getApplication().invokeLater(() -> {
            initTotalUserInfo();
        });
    }

    private void initTotalUserInfo() {
        Future<Response> future = UserUseInfoAPI.info();
        if (future==null) return;
        try {
            Response response = future.get(10, TimeUnit.SECONDS);
            if (response.code()==200){
                String body = response.body().string();
                if (body == null) return;
                JsonNode jsonNode = JsonUtils.convert2Object(body, JsonNode.class);
                int code = jsonNode.get("code").asInt();
                if (code!=200) return;
                JsonNode data = jsonNode.get("data");
                UserInfoProvider userInfoProvider = JsonUtils.tree2Object(data, UserInfoProvider.class);
                User user = systemInfoState.getUserInfoProvider().getUser();
                userInfoProvider.setUser(user);
                systemInfoState.setUserInfoProvider(userInfoProvider);
            }
        } catch (Exception e) {
            return;
        }
    }
    
    

    private JComponent createSystemInfoProvider(){
        TitledBorder title = BorderFactory.createTitledBorder(CodeBundle.message("code.configurable.system.provider.name"));
        JPanel panel = new ProviderComponent(systemInfoState.getSystemInfoProvider()).getPanel();
        panel.setBorder(title);
        return panel;
    }

    private JComponent createUserInfoFimProvider(){
        TitledBorder title = BorderFactory.createTitledBorder(CodeBundle.message("code.configurable.user.provider.name"));
        
        JPanel infoTable=createInfoTabel(systemInfoState.getUserInfoProvider());
        infoTable.setBorder(title);
        return infoTable;
    }

    private JPanel createInfoTabel(UserInfoProvider userInfoProvider) {
        // 设置编辑器使用情况表头和数据
        String[] editorColumnNames = {"指标", "值"};
        Object[][] editorData = {
                {"编辑器使用时间", String.format("%.2f小时",userInfoProvider.getEditorUsageTime()*1.0/60/60)},
                {"编辑器活跃时间", String.format("%.2f小时",userInfoProvider.getEditorActiveTime()*1.0/60/60)},
                {"添加的代码行数", String.format("%s行",userInfoProvider.getAddedCodeLines())},
                {"删除的代码行数", String.format("%s行",userInfoProvider.getDeletedCodeLines())},
                {"总键盘输入次数", userInfoProvider.getTotalKeyboardInputs()},
                {"CTRL+C次数", userInfoProvider.getCtrlCCount()},
                {"CTRL+V次数", userInfoProvider.getCtrlVCount()},
                {"AI使用次数", userInfoProvider.getCodeCompletionQaTimes()}
        };

        // 创建编辑器使用情况表格模型和JBTable
        DefaultTableModel editorModel = new DefaultTableModel(editorData, editorColumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 设置单元格不可编辑
            }
        };
        JBTable editorTable = new JBTable(editorModel);
        
        // 配置编辑器表格
        editorTable.setRowHeight(25);
        editorTable.getTableHeader().setReorderingAllowed(false); // 禁止拖动表头
        editorTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // 设置 AI 使用情况表头和数据
        String[] aiColumnNames = {"事件名称", "字符数", "token数"};
        Object[][] aiData = {
                {"代码补全", userInfoProvider.getCodeCompletionChars(), userInfoProvider.getCodeCompletionTokens()},
                {"代码问答", userInfoProvider.getCodeCompletionQaChars(), userInfoProvider.getCodeCompletionQaTokens()},
                {"测试编写", userInfoProvider.getTestCaseWritingChars(), userInfoProvider.getTestCaseWritingTokens()},
                {"变量声明", userInfoProvider.getVariableTypeDeclarationChars(), userInfoProvider.getVariableTypeDeclarationTokens()},
                {"代码解释", userInfoProvider.getCodeExplanationChars(), userInfoProvider.getCodeExplanationTokens()},
                {"文档编写", userInfoProvider.getDcoumentionWritingChars(), userInfoProvider.getDcoumentionWritingTokens()},
                {"代码重构", userInfoProvider.getCodeRefactoringChars(), userInfoProvider.getCodeRefactoringTokens()}
        };

        // 创建 AI 使用情况表格模型和JBTable
        DefaultTableModel aiModel = new DefaultTableModel(aiData, aiColumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 设置单元格不可编辑
            }
        };
        JBTable aiTable = new JBTable(aiModel);

        // 配置 AI 使用情况表格
        aiTable.setRowHeight(25);
        aiTable.getTableHeader().setReorderingAllowed(false); // 禁止拖动表头
        aiTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // 创建主面板用于布局两个表格
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // 垂直布局

        // 添加编辑器使用情况部分
        JLabel editorLabel = new JLabel("编辑器使用情况", JLabel.CENTER);
        editorLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 16)); // 设置标题字体
        editorLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT); // 居中对齐
        panel.add(editorLabel);
        panel.add(Box.createVerticalStrut(5)); // 增加一点间距
        panel.add(new JBScrollPane(editorTable));

        // 添加 AI 使用情况部分
        JLabel aiLabel = new JLabel("AI 使用情况", JLabel.CENTER);
        aiLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 16)); // 设置标题字体
        aiLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT); // 居中对齐
        panel.add(Box.createVerticalStrut(20)); // 添加间距来区分两个表格
        panel.add(aiLabel);
        panel.add(Box.createVerticalStrut(5)); // 增加一点间距
        panel.add(new JBScrollPane(aiTable));
        return panel;
    }

    private JComponent createOptionBtns(){
        TitledBorder title = BorderFactory.createTitledBorder(CodeBundle.message("code.configurable.provider.options.name"));
        JPanel panel = new JPanel();
        JButton jButton = new JButton("Reset");
        jButton.addActionListener((e)->{
            this.reset();
        });
        panel.setBorder(title);
        //设置流布局
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.add(jButton);
        return panel;
    }

    public void flash(){
        this.removeAll();
        init();
    }

    public void reset() {
        this.systemInfoState.resetModerProviders();
        this.flash();
    }
}
