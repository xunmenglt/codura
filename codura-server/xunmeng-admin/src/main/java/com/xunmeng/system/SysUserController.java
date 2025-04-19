package com.xunmeng.system;


import com.xunmeng.common.annotation.Log;
import com.xunmeng.common.core.controller.BaseController;
import com.xunmeng.common.core.page.TableDataInfo;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.common.core.pojo.entity.SysRole;
import com.xunmeng.common.core.pojo.entity.SysUser;
import com.xunmeng.common.enums.BusinessType;
import com.xunmeng.common.utils.SecurityUtils;
import com.xunmeng.common.utils.StringUtils;
import com.xunmeng.system.service.ISysRoleService;
import com.xunmeng.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "后台用户管理接口")
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private ISysRoleService roleService;
    
    @ApiOperation(value = "获取后台用户列表")
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user){
        startPage();
        List<SysUser> userList=userService.selectUserList(user);
        TableDataInfo dataTable = getDataTable(userList);
        return dataTable;
    }
    
    
    @ApiOperation(value = "根据用户名获取用户信息")
    @GetMapping(value = { "/","/{userName}" })
    public AjaxResult getInfo(@PathVariable(value = "userName", required = false) String userName)
    {
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.list();
        ajax.put("roles", SysUser.isAdmin(userName) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        if (StringUtils.isNotNull(userName))
        {
            SysUser sysUser = userService.selectUserByName(userName);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleName).collect(Collectors.toList()));
        }
        return ajax;
    }
    
    @ApiOperation(value = "修改用户")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PutMapping("/update")
    public AjaxResult update(@Validated @RequestBody SysUser user){
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        return toAjax(userService.updateUser(user));
    }

    @ApiOperation(value = "删除用户")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:user:delete')")
    @DeleteMapping("/delete/{userNames}")
    public AjaxResult delete(@PathVariable String[] userNames){
        String currentUserName=getUserId();
        for (String userName : userNames) {
            if (userName!=null && userName.equalsIgnoreCase(currentUserName)){
                return error("当前用户不能删除");
            }
        }
        return toAjax(userService.deleteUserByNames(userNames));
    }

    /**
     * 新增用户
     */
    @ApiOperation(value = "新增用户")
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysUser user)
    {
        if (!userService.checkUserNameUnique(user))
        {
            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            return error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    @ApiOperation(value = "重置密码")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.resetPwd(user));
    }


    @ApiOperation(value = "状态修改")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PostMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        return toAjax(userService.updateUserStatus(user));
    }
    
    
    @ApiOperation(value = "根据用户编号获取授权角色")
    @GetMapping("/authRole/{userName}")
    public AjaxResult authRole(@PathVariable("userName") String userName)
    {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserByName(userName);
        List<SysRole> roles = user.getRoles();
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userName) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    
    @ApiOperation(value = "用户授权角色")
    @Log(title = "用户授权角色", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PostMapping("/authRole")
    public AjaxResult insertAuthRole(String userName, String[] roleNames)
    {
        userService.insertUserAuth(userName, roleNames);
        return success();
    }
}
