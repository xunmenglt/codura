package com.xunmeng.codura.setting.component.impl;

import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import com.xunmeng.codura.setting.component.Component;
import com.xunmeng.codura.setting.component.annotation.UIComponent;
import com.xunmeng.codura.setting.provider.Provider;
import com.xunmeng.codura.utils.ReflectUtils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;


public class ProviderComponent implements Component {
    private JPanel panel;
    private Provider provider;
    
    public ProviderComponent(Provider provider){
        super();
        this.provider=provider;
        try {
            initComponent();
        } catch (Exception e) {
            throw new RuntimeException("not can create JComponent object"+e);
        }
        this.panel.setBorder(BorderFactory.createLineBorder(Color.cyan));
    }

    public void initComponent() throws IllegalAccessException {
        FormBuilder formBuilder = FormBuilder.createFormBuilder();
        Class<? extends Provider> providerClass = provider.getClass();
        List<Field> fieldList= ReflectUtils.getFieldListFromClazz(providerClass);
        fieldList.sort((f1,f2)->{
            int flag1=0;
            int flag2=0;
            if (f1.isAnnotationPresent(UIComponent.class)){
                flag1=f1.getAnnotation(UIComponent.class).order();
            }
            if (f2.isAnnotationPresent(UIComponent.class)){
                flag2=f2.getAnnotation(UIComponent.class).order();
            }
            return Integer.compare(flag1,flag2);
        });
        for (Field field : fieldList) {
            field.setAccessible(true);
            // 检查字段是否存在UIComponent注解
            if (!field.isAnnotationPresent(UIComponent.class)) continue;
            JComponent component=createJcomponent(field,provider);
            String labelText=field.getAnnotation(UIComponent.class).label();
            formBuilder.addLabeledComponent(new JBLabel(labelText),component,1,false);        
        }
        this.panel=formBuilder.getPanel();
    }

    public JPanel getPanel() {
        return panel;
    }
}
