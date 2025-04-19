package com.xunmeng.codura.constants.enums;

import com.intellij.ui.components.JBCheckBox;

import javax.swing.*;

public enum ComponentType implements Type {
    /*单选*/
    JCOMBOBOX(JComboBox.class),
    JRADIOBUTTON(JRadioButton.class),
    JTEXTFILED(JTextField.class),
    JCHECKBOX(JBCheckBox.class);


    private Class<? extends JComponent> clazz;

    ComponentType(Class<? extends JComponent> clazz){
        this.clazz=clazz;
    }

    public Class<? extends JComponent> getClazz() {
        return clazz;
    }

    @Override
    public Object V() {
        return clazz;
    }
}
