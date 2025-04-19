package com.xunmeng.system;


import com.xunmeng.common.annotation.Log;
import com.xunmeng.common.constant.Constants;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.common.core.pojo.entity.SysMenu;
import com.xunmeng.common.core.pojo.entity.SysUser;
import com.xunmeng.common.core.pojo.model.LoginBody;
import com.xunmeng.common.enums.BusinessType;
import com.xunmeng.common.utils.SecurityUtils;
import com.xunmeng.framework.web.service.SysLoginService;
import com.xunmeng.framework.web.service.SysPasswordService;
import com.xunmeng.framework.web.service.SysPermissionService;
import com.xunmeng.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@Api(tags = "登录相关接口")
public class SysLoginController {
    
    @Autowired
    private SysLoginService loginService;
    
    @Autowired
    private ISysMenuService menuService;
    
    @Autowired
    private SysPasswordService passwordService;
    
    @Autowired
    private SysPermissionService permissionService;
    
    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody){
        AjaxResult ajax=AjaxResult.success();
        // 生成令牌
        String token=loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN,token);
        return ajax;
    }
    
    @ApiOperation(value = "获取用户信息")
    @GetMapping("/getInfo")
    public AjaxResult getInfo()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }
    
    @ApiOperation(value = "获取路由信息")
    @GetMapping("/getRouters")
    public AjaxResult getRouters(){
        String userName = SecurityUtils.getUserId();
        List<SysMenu> menus=menuService.selectMenuTreeByUserName(userName);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
