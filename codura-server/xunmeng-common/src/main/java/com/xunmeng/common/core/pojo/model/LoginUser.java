package com.xunmeng.common.core.pojo.model;


import com.alibaba.fastjson2.annotation.JSONField;
import com.xunmeng.common.core.pojo.entity.SysUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * 登录用户对象
 *
 * @author xunmeng
 */

@Data
public class LoginUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    /*用户ID*/
    private String userId;
    /*用户登录token*/
    private String token;
    /*登录时间*/
    private Long loginTime;
    /*过期时间*/
    private Long expireTime;
    /*登录ip地址*/
    private String ipaddr;
    /*登录地点*/
    private String loginLocation;
    /*浏览器类型*/
    private String browser;
    /*操作系统*/
    private String os;
    /*权限列表*/
    private Set<String> permissions;

    /*用户信息*/
    private SysUser user;


    public LoginUser()
    {
    }

    public LoginUser(SysUser user,Set<String> permissions){
        this.user=user;
        this.permissions=permissions;
    }

    public LoginUser(String userId, SysUser user, Set<String> permissions){
        this.userId = userId;
        this.user = user;
        this.permissions = permissions;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @JSONField(serialize = false)
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
