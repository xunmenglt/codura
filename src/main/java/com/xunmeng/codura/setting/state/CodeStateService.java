package com.xunmeng.codura.setting.state;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "codura",storages = {@Storage("codeAlienState.xml")})
public class CodeStateService implements PersistentStateComponent<CodeState> {
    public CodeState codeState;


    public static CodeState settings() {
        return ApplicationManager.getApplication().getService(CodeStateService.class).getState();
    }



    @Override
    public @Nullable CodeState getState() {
        return this.codeState;
    }

    @Override
    public void loadState(@NotNull CodeState state) {
        if (this.codeState ==null){
            this.codeState =new CodeState();
        }
        XmlSerializerUtil.copyBean(state, this.codeState);
    }


    // 当配置没变更过时，不会有序列化文件，loadState不会被调用，为了获取配置的默认值，需要重写该方法
    @Override
    public void noStateLoaded() {
        this.codeState = new CodeState();
    }
}
