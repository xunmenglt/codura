package com.xunmeng.framework.web.service;

import com.xunmeng.common.core.pojo.entity.SysRole;
import com.xunmeng.common.core.pojo.entity.SysUser;
import com.xunmeng.system.service.ISysMenuService;
import com.xunmeng.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 权限处理服务
 */
@Service
public class SysPermissionService {
    
    @Autowired
    private ISysRoleService roleService;
    
    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取角色数据权限
     */
    public Set<String> getRolePermission(SysUser user){
        Set<String> roles=new HashSet<>();
        if (user.isAdmin()){
            roles.add("admin");
        }else {
            roles.addAll(roleService.selectRolePermissionByUserName(user.getUserName()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user){
        Set<String> perms=new HashSet<>();
        if (user.isAdmin()){
            perms.add("*:*:*");
        }
        else {
            List<SysRole> roles=user.getRoles();
            if (!CollectionUtils.isEmpty(roles)){
                for (SysRole role : roles) {
                    Set<String> rolePerms = menuService.selectMenuPermsByRoleName(role.getRoleName());
                    role.setPermissions(rolePerms);
                    perms.addAll(rolePerms);
                }
            } else {
                perms.addAll(menuService.selectMenuPermsByUserName(user.getUserName()));
            }
        }
        return perms;
    }
}
