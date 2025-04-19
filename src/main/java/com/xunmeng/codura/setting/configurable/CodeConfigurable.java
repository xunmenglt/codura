package com.xunmeng.codura.setting.configurable;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.xunmeng.codura.setting.component.SystemInfoProviderComponent;
import com.xunmeng.codura.setting.state.SystemInfoState;
import com.xunmeng.codura.setting.state.SystemInfoStateService;
import com.xunmeng.codura.utils.CodeBundle;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CodeConfigurable implements Configurable {

    private SystemInfoProviderComponent systemInfoProviderComponent;
    
    @Override
    public String getDisplayName() {
        return CodeBundle.message("code.configurable.name");
    }

    @Override
    public @Nullable JComponent createComponent() {
        SystemInfoState systemInfoState = SystemInfoStateService.settings();
        SystemInfoProviderComponent component = new SystemInfoProviderComponent(systemInfoState);
        this.systemInfoProviderComponent=component;
        return component;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    /*初始化的时候*/
    @Override
    public void apply() throws ConfigurationException {
        systemInfoProviderComponent.flash();
    }

    /*重置*/
    @Override
    public void reset() {
        systemInfoProviderComponent.flash();
    }

    @Override
    public void disposeUIResources() {
        systemInfoProviderComponent=null;
    }
}
