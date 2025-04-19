package com.xunmeng.codura.setting.configurable;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.xunmeng.codura.setting.component.ChatConfigProviderComponent;
import com.xunmeng.codura.setting.state.CodeState;
import com.xunmeng.codura.setting.state.CodeStateService;
import com.xunmeng.codura.utils.CodeBundle;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CodeChatConfigConfigurable implements Configurable {
    private ChatConfigProviderComponent chatConfigProviderComponent;
    @Override
    public String getDisplayName() {
        return CodeBundle.message("code.configurable.chat.config.provider.name");
    }

    @Override
    public @Nullable JComponent createComponent() {
        CodeState codeState = CodeStateService.settings();
        ChatConfigProviderComponent component = new ChatConfigProviderComponent(codeState);
        this.chatConfigProviderComponent=component;
        return component;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    /*初始化的时候*/
    @Override
    public void apply() throws ConfigurationException {
        chatConfigProviderComponent.flash();
    }

    /*重置*/
    @Override
    public void reset() {
        chatConfigProviderComponent.flash();
    }

    @Override
    public void disposeUIResources() {
        chatConfigProviderComponent=null;
    }
}
