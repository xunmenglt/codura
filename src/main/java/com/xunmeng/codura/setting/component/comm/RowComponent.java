package com.xunmeng.codura.setting.component.comm;

import javax.swing.*;

public class RowComponent extends JPanel {
    public RowComponent(JComponent ...components){
        BoxLayout boxLayout = new BoxLayout(this,BoxLayout.X_AXIS);
        this.setLayout(boxLayout);
        for (JComponent component : components) {
            component.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
            this.add(component);
        }
    }
}
