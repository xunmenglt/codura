package com.xunmeng.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunmeng.common.core.pojo.TreeSelect;
import com.xunmeng.common.core.pojo.entity.SysMenu;
import com.xunmeng.system.pojo.vo.RouterVo;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-05
 */
public interface ISysMenuService extends IService<SysMenu> {

    Set<String> selectMenuPermsByUserName(String userName);

    Set<String> selectMenuPermsByRoleName(String roleName);

    List<SysMenu> selectMenuTreeByUserName(String userName);

    List<RouterVo> buildMenus(List<SysMenu> menus);

    List<SysMenu> selectMenuList(SysMenu menu, String userId);

    List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    List<SysMenu> selectMenuList(String userId);

    List<Integer> selectMenuListByRoleName(String roleName);

    boolean hasChildByMenuId(Long menuId);

    boolean checkMenuExistRole(Long menuId);
    
}
