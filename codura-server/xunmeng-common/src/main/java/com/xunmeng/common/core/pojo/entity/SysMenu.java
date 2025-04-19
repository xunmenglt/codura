package com.xunmeng.common.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xunmeng.common.core.pojo.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限表 md_sys_menu
 */
@Data
@TableName("sys_menu")
public class SysMenu extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    /*菜单ID*/
    @TableId(value = "menu_id",type = IdType.AUTO)
    private Integer menuId;
    
    /*菜单名称*/
    private String menuName;
    
    /*父菜单ID*/
    private Integer parentId;
    
    /*显示顺序*/
    private Integer orderNum;
    
    /*路由地址*/
    private String path;
    
    /*组件路径*/
    private String component;
    
    /*是否为外链（1是 0否）*/
    private Integer isFrame;
    
    /*是否缓存（1缓存 0不缓存）*/
    private Integer isCache;
    
    /*菜单类型（M目录 C菜单 F按钮）*/
    private String menuType;
    
    /*菜单状态（1显示 0隐藏）*/
    private Integer visible;
    
    /*菜单状态（1正常 0停用）*/
    private Integer status;

    /*权限标识*/
    private String perms;
    
    /*菜单图标*/
    private String icon;

    /** 子菜单 */
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<SysMenu>();

    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 0, max = 50, message = "菜单名称长度不能超过50个字符")
    public String getMenuName()
    {
        return menuName;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum()
    {
        return orderNum;
    }

    @Size(min = 0, max = 200, message = "路由地址不能超过200个字符")
    public String getPath()
    {
        return path;
    }

    @Size(min = 0, max = 200, message = "组件路径不能超过255个字符")
    public String getComponent()
    {
        return component;
    }

    @NotBlank(message = "菜单类型不能为空")
    public String getMenuType()
    {
        return menuType;
    }

    @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
    public String getPerms()
    {
        return perms;
    }
    
}
