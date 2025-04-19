package com.xunmeng.framework.web.service;

import com.xunmeng.common.constant.Constants;
import com.xunmeng.common.core.pojo.entity.SysRole;
import com.xunmeng.common.core.pojo.model.LoginUser;
import com.xunmeng.common.utils.SecurityUtils;
import com.xunmeng.common.utils.StringUtils;
import com.xunmeng.framework.security.context.PermissionContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Service("ss")
public class PermissionService {

    /**
     * 判断是否包含该权限
     * 
     * @param permission
     * @return
     */
    public boolean hasPermi(String permission){
        if (StringUtils.isEmpty(permission)){
            return false;
        }
        LoginUser loginUser= SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())){
            return false;
        }
        PermissionContextHolder.setContext(permission);
        return hasPermissions(loginUser.getPermissions(),permission);
    }
    /**
     * 判断用户是否拥有某个角色
     * 
     */
    public boolean hasRole(String role){
        if (StringUtils.isEmpty(role)){
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles()))
        {
            return false;
        }
        for (SysRole sysRole : loginUser.getUser().getRoles()){
            String roleKey=sysRole.getRoleName();
            if (Constants.SUPER_ADMIN.equals(roleKey) || roleKey.equals(StringUtils.trim(role))){
                return true;
            }
        }
        return false;
    }
    


    /**
     * 判断是否包含权限
     */
    private boolean hasPermissions(Set<String> permissions,String permission){
        return permissions.contains(Constants.ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }

}
