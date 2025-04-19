package com.xunmeng.common.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xunmeng.common.core.pojo.BaseEntity;
import lombok.Data;

import java.util.Set;

/**
 * 系统用户
 * 
 * @autor xunmeng
 */
@Data
@TableName("sys_role")
public class SysRole extends BaseEntity {
    @TableId(value = "role_name")
    private String roleName;
    private String roleZhName;
    /** 角色菜单权限 */
    @TableField(exist = false)
    private Set<String> permissions;
    /*菜单编号*/
    @TableField(exist = false)
    private Integer[] menuIds;
    
    public SysRole(){
        
    }
    public SysRole(String roleName){
        this.roleName=roleName;
    }
    
    public boolean isAdmin(){
        return this.roleName!=null && "admin".equalsIgnoreCase(this.roleName);
    }
}
