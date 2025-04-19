package com.xunmeng.codura.setting.configurable;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.xunmeng.codura.setting.component.CompletionConfigProviderComponent;
import com.xunmeng.codura.setting.state.CodeState;
import com.xunmeng.codura.setting.state.CodeStateService;
import com.xunmeng.codura.utils.CodeBundle;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CodeCompletionConfigConfigurable implements Configurable {
    private CompletionConfigProviderComponent completionConfigProviderComponent;
    @Override
    public String getDisplayName() {
        return CodeBundle.message("code.configurable.completion.config.provider.name");
    }

    @Override
    public @Nullable JComponent createComponent() {
        CodeState codeState = CodeStateService.settings();
        CompletionConfigProviderComponent component = new CompletionConfigProviderComponent(codeState);
        this.completionConfigProviderComponent=component;
        return component;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    /*初始化的时候*/
    @Override
    public void apply() throws ConfigurationException {
        completionConfigProviderComponent.flash();
    }

    /*重置*/
    @Override
    public void reset() {
        completionConfigProviderComponent.flash();
    }

    @Override
    public void disposeUIResources() {
        completionConfigProviderComponent=null;
    }
}
