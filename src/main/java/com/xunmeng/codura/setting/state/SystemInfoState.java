package com.xunmeng.codura.setting.state;

import com.xunmeng.codura.setting.provider.SystemInfoProvider;
import com.xunmeng.codura.setting.provider.UserInfoProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SystemInfoState {
    private UserInfoProvider userInfoProvider;
    
    private SystemInfoProvider systemInfoProvider;
    
    public SystemInfoState(){
        systemInfoProvider=SystemInfoProvider.getDefaultProvider();
        userInfoProvider=UserInfoProvider.getDefaultProvider();
    }

    public void resetModerProviders(){
        userInfoProvider= UserInfoProvider.getDefaultProvider();
        systemInfoProvider= SystemInfoProvider.getDefaultProvider();
    }
}
