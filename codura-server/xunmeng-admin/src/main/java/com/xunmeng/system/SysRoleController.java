package com.xunmeng.system;


import com.xunmeng.common.annotation.Log;
import com.xunmeng.common.core.controller.BaseController;
import com.xunmeng.common.core.page.TableDataInfo;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.common.core.pojo.entity.SysRole;
import com.xunmeng.common.core.pojo.model.LoginUser;
import com.xunmeng.common.enums.BusinessType;
import com.xunmeng.common.utils.StringUtils;
import com.xunmeng.framework.web.service.SysPermissionService;
import com.xunmeng.framework.web.service.TokenService;
import com.xunmeng.system.service.ISysRoleService;
import com.xunmeng.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
@Api(tags = "角色管理接口")
public class SysRoleController extends BaseController {
    
    @Autowired
    private ISysRoleService roleService;
    
    @Autowired
    private SysPermissionService permissionService;
    
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private TokenService tokenService;
    
    
    @ApiOperation(value = "获取角色列表")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysRole role){
        startPage();
        List<SysRole> roleList=roleService.selectRoleList(role);
        return getDataTable(roleList);
    }
    
    
    @ApiOperation(value = "根据角色编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/{roleName}")
    public AjaxResult getInfo(@PathVariable String roleName){
        return success(roleService.selectRoleByName(roleName));
    }
    
    @ApiOperation(value = "新增角色")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SysRole role){
        if(!roleService.checkRoleNameUnique(role)){
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        return toAjax(roleService.insertRole(role));
    }

    @ApiOperation(value = "修改保存角色")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping("/update")
    public AjaxResult update(@RequestBody SysRole role){
        roleService.checkRoleAllowed(role);
        if (roleService.updateRole(role) > 0)
        {
            // 更新缓存用户权限
            LoginUser loginUser = getLoginUser();
            if (StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isAdmin())
            {
                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
                loginUser.setUser(userService.selectUserByName(loginUser.getUser().getUserName()));
                tokenService.setLoginUser(loginUser);
            }
            return success();
        }
        return error("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }


    @ApiOperation(value = "删除角色")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:role:delete')")
    @DeleteMapping("/delete/{roleNames}")
    public AjaxResult remove(@PathVariable String[] roleNames)
    {
        return toAjax(roleService.deleteRoleByNames(roleNames));
    }

    @ApiOperation(value = "获取角色选择框列表")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        return success(roleService.list());
    }


}
