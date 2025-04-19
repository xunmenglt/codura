package com.xunmeng.codura.system;

import com.xunmeng.codura.system.utils.SystemInfoUtil;

public abstract class SecurityUtil {
    public static String userId(){
        return String.format("CODE_ALIEN_USER[%s]", SystemInfoUtil.macAddress());
    }

    
}
