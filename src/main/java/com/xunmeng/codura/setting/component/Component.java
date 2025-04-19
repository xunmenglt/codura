package com.xunmeng.codura.setting.component;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.ui.components.JBTextField;
import com.xunmeng.codura.constants.enums.ComponentType;
import com.xunmeng.codura.constants.enums.Type;
import com.xunmeng.codura.setting.component.annotation.UIComponent;
import com.xunmeng.codura.setting.provider.Provider;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public interface Component {
    void initComponent() throws Throwable;
    default JComponent createJcomponent(Field field, Provider provider) throws IllegalAccessException {
        UIComponent uiComponent=field.getAnnotation(UIComponent.class);
        ComponentType componentType = uiComponent.componentType();
        Class<?> tabs = uiComponent.tabs();
        JComponent component;
        switch (componentType){
            case JTEXTFILED:
                component=createJTextField(field,provider);
                break;
            case JCOMBOBOX:
                component=createJCombobox(field,tabs,provider);
                break;
            case JRADIOBUTTON:
                component=creatJRadioButton(field,tabs,provider);
                break;
            case JCHECKBOX:
                component=createJCheckBox(field,provider);
                break;
            default:
                throw new IllegalArgumentException("Unsupported component type: " + uiComponent.componentType());
        }
        if (uiComponent.disabled()){
            disableAllComponents(component);
        }
        return component;
    }
    


    default JComponent creatJRadioButton(Field field, Class<?> tabs, Provider provider)throws IllegalAccessException {
        Object _value = field.get(provider);
        if (_value==null){
            return null;
        }
        String value=null;
        if (_value instanceof Type){
            value = (((Type) _value).V()).toString();
        }else {
            value=_value.toString();
        }
        // 创建ButtonGroup
        ButtonGroup group = new ButtonGroup();
        JPanel panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel,BoxLayout.X_AXIS);
        panel.setLayout(boxLayout);
        JBRadioButton defaultSelect=null;
        if (tabs.isEnum()){
            Object[] constants = tabs.getEnumConstants();
            Map<String,Object> value2EnumObjectMap=new HashMap<>();
            for (Object constant : constants) {
                if (constant instanceof Type){
                    value2EnumObjectMap.put((((Type) constant).V()).toString(),constant);
                }
            }
            for (Object constant : constants) {
                if (constant instanceof Type){
                    String tabText=(((Type) constant).V()).toString();
                    JBRadioButton radioButton = new JBRadioButton(tabText);
                    group.add(radioButton);
                    panel.add(radioButton);
                    if (value.equals(tabText)){
                        radioButton.setSelected(true);
                    }
                    if (defaultSelect==null) defaultSelect=radioButton;
                    radioButton.addChangeListener((e)->{
                        JRadioButton jb = (JRadioButton) e.getSource();
                        String text = jb.getText();
                        if (jb.isSelected()){
                            try {
                                field.set(provider,value2EnumObjectMap.get(text));
                            } catch (IllegalAccessException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                }
            }
        }
        return panel;
    }

    default JComponent createJCombobox(Field field, Class<?> tabs, Provider provider) throws IllegalAccessException {
        Object _value = field.get(provider);
        String value=null;
        if (_value instanceof Type){
            value = (((Type) _value).V()).toString();
        }else if (value!=null){
            value=_value.toString();
        }
        JComboBox comboBox=new JComboBox<String>();
        if (tabs.isEnum()){
            Object[] constants = tabs.getEnumConstants();
            Map<String,Object> value2EnumObjectMap=new HashMap<>();
            for (Object constant : constants) {
                if (constant instanceof Type){
                    String key = (((Type) constant).V()).toString();
                    value2EnumObjectMap.put(key,constant);
                    comboBox.addItem(key);
                }
            }
            if (value!=null)
                comboBox.setSelectedItem(value);
            comboBox.addActionListener((e)->{
                JComboBox jcb=(JComboBox)e.getSource();//获取事件源
                Object item = jcb.getSelectedItem();
                Object o = value2EnumObjectMap.get(item.toString());
                try {
                    field.set(provider,o);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
        return comboBox;
    }

    default JComponent createJTextField(Field field, Provider provider) throws IllegalAccessException {
        String value = String.valueOf(field.get(provider)==null?"":field.get(provider));
        JTextField textField = new JBTextField();
        textField.setText(value);
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                textChanged();
            }
            private void textChanged() {
                try {
                    String text=textField.getText();
                    Object v=null;
                    Class<?> fieldType = field.getType();
                    if (StringUtils.isEmpty(text)){
                        v=null;
                    }else if (fieldType==int.class || fieldType==Integer.class){
                        v=Integer.valueOf(text);
                    }else if (fieldType== Double.class || fieldType==double.class){
                        v=Double.valueOf(text);
                    }else {
                        v=text;
                    }
                    field.set(provider,v);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return textField;
    }

    default JComponent createJCheckBox(Field field, Provider provider) throws IllegalAccessException {
        boolean value=field.get(provider)==null?false:(boolean) field.get(provider);
        JBCheckBox checkBox = new JBCheckBox("",value);
        checkBox.addChangeListener((e)->{
            JBCheckBox jc = (JBCheckBox) e.getSource();
            boolean selected = jc.isSelected();
            try {
                field.set(provider,selected);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });
        return checkBox;
    }

    default void disableAllComponents(Container container) {
        for (java.awt.Component c : container.getComponents()) {
            c.setEnabled(false);
            if (c instanceof Container) {
                disableAllComponents((Container) c);
            }
        }
    }

}
