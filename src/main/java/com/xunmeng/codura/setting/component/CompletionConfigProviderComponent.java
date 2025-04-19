package com.xunmeng.codura.setting.component;

import com.xunmeng.codura.setting.component.impl.ProviderComponent;
import com.xunmeng.codura.setting.state.CodeState;
import com.xunmeng.codura.utils.CodeBundle;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CompletionConfigProviderComponent extends JPanel {
    private CodeState codeState;
    public CompletionConfigProviderComponent(CodeState codeState){
        super();
        this.codeState = codeState;
        init();
    }

    private void init(){
        GridBagLayout bg = new GridBagLayout();
        this.setLayout(bg);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;    // 组件在网格中的列位置
        constraints.gridy = 0;    // 组件在网格中的行位置
        constraints.gridwidth = 1; // 组件占据的列数
        constraints.gridheight = 1;// 组件占据的行数
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST; // 组件对齐方式
        constraints.weightx = 1.0; // 水平方向的权重
        constraints.weighty = 2.0; // 垂直方向的权重，这里设置为0表示不扩展
        JComponent completionConfigProvider = createCompletionConfigProvider();
        completionConfigProvider.setBorder(null);
        this.add(completionConfigProvider,constraints);
        constraints.gridy++;
        // 添加底部组件
        constraints.gridheight = GridBagConstraints.REMAINDER; // 占据剩余的所有行
        constraints.anchor = GridBagConstraints.SOUTH; // 锚定底部
        this.add(createOptionBtns(),constraints);
    }

    private JComponent createCompletionConfigProvider(){
        JPanel panel = new ProviderComponent(codeState.getCompletionConfigProvider()).getPanel();
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
        this.codeState.resetCompletionProvider();
        this.flash();
    }
}
