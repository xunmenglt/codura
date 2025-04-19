package com.xunmeng.common.core.pojo.model;

import lombok.Data;

@Data
public class LoginBody {

    /*用户名*/
    private String username;

    /*用户密码*/
    private String password;

    /*验证码*/
    private String code;

    /*唯一标识*/
    private String uuid;

}
