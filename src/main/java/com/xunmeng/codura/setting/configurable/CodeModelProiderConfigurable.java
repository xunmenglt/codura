package com.xunmeng.codura.setting.configurable;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.xunmeng.codura.setting.component.ModelProviderComponent;
import com.xunmeng.codura.setting.state.CodeState;
import com.xunmeng.codura.setting.state.CodeStateService;
import com.xunmeng.codura.utils.CodeBundle;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CodeModelProiderConfigurable implements Configurable {
    private ModelProviderComponent modelProviderComponent;
    @Override
    public String getDisplayName() {
        return CodeBundle.message("code.configurable.provider.name");
    }

    @Override
    public @Nullable JComponent createComponent() {
        CodeState codeState = CodeStateService.settings();
        ModelProviderComponent component = new ModelProviderComponent(codeState);
        this.modelProviderComponent=component;
        return component;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    /*初始化的时候*/
    @Override
    public void apply() throws ConfigurationException {
        modelProviderComponent.flash();
    }

    /*重置*/
    @Override
    public void reset() {
        modelProviderComponent.flash();
    }

    @Override
    public void disposeUIResources() {
        modelProviderComponent=null;
    }
}
