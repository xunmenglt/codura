package com.xunmeng.codura.setting.component;
import com.xunmeng.codura.setting.component.impl.ProviderComponent;
import com.xunmeng.codura.setting.state.CodeState;
import com.xunmeng.codura.utils.CodeBundle;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ModelProviderComponent extends JPanel {
    private CodeState codeState;
    public ModelProviderComponent(CodeState codeState){
        super();
        this.codeState = codeState;
        init();
    }
    
    private void init(){
        BoxLayout boxLayout = new BoxLayout(this,BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);
        if (codeState !=null){
            this.add(createChatProvider());
            this.add(createFimProvider());
            this.add(createOptionBtns());
        }
    }
    
    private JComponent createChatProvider(){
        TitledBorder title = BorderFactory.createTitledBorder(CodeBundle.message("code.configurable.chat.provider.name"));
        JPanel panel = new ProviderComponent(codeState.getChatModelProvider()).getPanel();
        panel.setBorder(title);
        return panel;
    }

    private JComponent createFimProvider(){
        TitledBorder title = BorderFactory.createTitledBorder(CodeBundle.message("code.configurable.fim.provider.name"));
        JPanel panel = new ProviderComponent(codeState.getFimModelProvider()).getPanel();
        panel.setBorder(title);
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
        this.codeState.resetModerProviders();
        this.flash();
    }
    
}
