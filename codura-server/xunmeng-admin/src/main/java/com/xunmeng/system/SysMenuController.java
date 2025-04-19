package com.xunmeng.system;


import com.xunmeng.common.annotation.Log;
import com.xunmeng.common.core.controller.BaseController;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.common.core.pojo.entity.SysMenu;
import com.xunmeng.common.enums.BusinessType;
import com.xunmeng.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/system/menu")
@Api(tags = "菜单管理接口")
public class SysMenuController extends BaseController {
    
    @Autowired
    private ISysMenuService menuService;
    
    @ApiOperation(value = "获取菜单列表")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu)
    {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return success(menus);
    }
    
    @ApiOperation(value = "根据菜单编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId)
    {
        return success(menuService.getById(menuId));
    }

    @ApiOperation(value = "获取下拉树表")
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenu menu)
    {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @ApiOperation(value = "加载对应角色菜单列表树")
    @GetMapping(value = "/roleMenuTreeselect/{roleName}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleName") String roleName)
    {
        List<SysMenu> menus = menuService.selectMenuList(getUserId());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleName(roleName));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }


    @ApiOperation(value = "添加菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @PostMapping("/add")
    public AjaxResult edit(@Validated @RequestBody SysMenu menu)
    {
        return toAjax(menuService.save(menu));
    }

    
    @ApiOperation(value = "修改菜单")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @PostMapping("/update")
    public AjaxResult update(@Validated @RequestBody SysMenu menu)
    {
        return toAjax(menuService.updateById(menu));
    }


    /**
     * 删除菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:delete')")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId)
    {
        if (menuService.hasChildByMenuId(menuId))
        {
            return warn("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return warn("菜单已分配,不允许删除");
        }
        return toAjax(menuService.removeById(menuId));
    }
}
