package com.xunmeng.common.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xunmeng.common.core.pojo.BaseEntity;
import lombok.Data;

import javax.validation.constraints.Email;
import java.util.List;

/**
 * 系统用户 sys_user
 *
 * @author xunmeng
 */
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {
    /*用户名*/
    @TableId(value = "user_name",type = IdType.INPUT)
    private String userName;
    /*昵称*/
    private String nickName;
    /*密码*/
    private String password;
    /*头像*/
    private String avatar;
    /*邮箱*/
    @Email
    private String email;
    /*是否启用*/
    private Integer enabled;
    /** 角色对象 */
    @TableField(exist = false)
    private List<SysRole> roles;
    
    @TableField(exist = false)
    private String[] roleIds;

    public SysUser(){
        
    }
    
    public SysUser(String userName){
        this.userName=userName;
    }
    
    public boolean isAdmin()
    {
        return isAdmin(this.userName);
    }

    public static boolean isAdmin(String userName)
    {
        return userName != null && "admin".equals(userName);
    }
}
