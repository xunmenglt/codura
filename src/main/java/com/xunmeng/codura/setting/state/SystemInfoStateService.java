package com.xunmeng.codura.setting.state;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper=false)
@State(name = "coduraSystem",storages = {@Storage("coduraSystemState.xml")})
public class SystemInfoStateService implements PersistentStateComponent<SystemInfoState> {
    private SystemInfoState systemInfoState=new SystemInfoState();

    public static SystemInfoState settings() {
        return ApplicationManager.getApplication().getService(SystemInfoStateService.class).getState();
    }



    @Override
    public SystemInfoState getState() {
        return this.systemInfoState;
    }

    @Override
    public void loadState(@NotNull SystemInfoState state) {
        if (this.systemInfoState==null){
            this.systemInfoState=new SystemInfoState();
        }
        XmlSerializerUtil.copyBean(state, this.systemInfoState);
    }


    // 当配置没变更过时，不会有序列化文件，loadState不会被调用，为了获取配置的默认值，需要重写该方法
    @Override
    public void noStateLoaded() {
        this.systemInfoState = new SystemInfoState();
    }
    
}
