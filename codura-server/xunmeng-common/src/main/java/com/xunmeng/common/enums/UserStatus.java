package com.xunmeng.common.enums;

/**
 * 用户状态
 * 
 * @author ruoyi
 */
public enum UserStatus
{
    DISABLE(0, "停用"), OK(1, "正常");

    private final int code;
    private final String info;

    UserStatus(int code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public int getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
