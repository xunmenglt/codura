package com.xunmeng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xunmeng.common.constant.Constants;
import com.xunmeng.common.constant.UserConstants;
import com.xunmeng.common.core.pojo.TreeSelect;
import com.xunmeng.common.core.pojo.entity.SysMenu;
import com.xunmeng.common.core.pojo.entity.SysUser;
import com.xunmeng.common.utils.SecurityUtils;
import com.xunmeng.common.utils.StringUtils;
import com.xunmeng.system.mapper.SysMenuMapper;
import com.xunmeng.system.mapper.SysRoleMenuMapper;
import com.xunmeng.system.pojo.SysRoleMenu;
import com.xunmeng.system.pojo.vo.MetaVo;
import com.xunmeng.system.pojo.vo.RouterVo;
import com.xunmeng.system.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-05
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;
    @Autowired
    private SysRoleMenuMapper roleMenuMapper;


    @Override
    public Set<String> selectMenuPermsByUserName(String userName) {
        List<String> perms=menuMapper.selectMenuPermsByUserName(userName);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.add(perm.trim());
            }
        }
        return permsSet;
    }

    @Override
    public Set<String> selectMenuPermsByRoleName(String roleName) {
        List<String> perms=menuMapper.selectMenuPermsByRoleName(roleName);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.add(perm.trim());
            }
        }
        return permsSet;
    }

    @Override
    public List<SysMenu> selectMenuTreeByUserName(String userName) {
        
        List<SysMenu> menus=null;
        if (SecurityUtils.isAdmin(userName)){// 返回所以路由
            menus=menuMapper.selectMenuTreeAll();
        }else {
            menus = menuMapper.selectMenuTreeByUserName(userName);
        }
        return getChildPerms(menus, 0);
    }



    /**
     * 根据父节点获子菜单
     * @param menus
     * @param parentId
     * @return
     */
    private List<SysMenu> getChildPerms(List<SysMenu> menus, int parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (SysMenu menu : menus) {
            if (menu.getParentId().intValue()==parentId){
                recursionFn(menus, menu);// 向该菜单中添加子菜单
                returnList.add(menu);
            }
        }
        return returnList;
    }

    /**
     * 递归创建子菜单
     * @param menus
     * @param menu
     */
    private void recursionFn(List<SysMenu> menus, SysMenu menu) {
        List<SysMenu> childList = getChildList(menus,menu);
        menu.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(menus,tChild)){
                recursionFn(menus,tChild);
            }
        }
    }

    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t)
    {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext())
        {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().intValue() == t.getMenuId().intValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    private boolean hasChild(List<SysMenu> menus, SysMenu tChild) {
        return getChildList(menus,tChild).size()>0;
    }


    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus)
    {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus)
        {
            RouterVo router = new RouterVo();
            router.setHidden(0==menu.getVisible().intValue());
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(),0==menu.getIsCache().intValue(), menu.getPath()));
            List<SysMenu> cMenus = menu.getChildren();
            if (StringUtils.isNotEmpty(cMenus) && UserConstants.TYPE_DIR.equals(menu.getMenuType()))
            {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            routers.add(router);
        }
        return routers;
    }

    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, String userName) {

        List<SysMenu> menuList = null;
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userName))
        {
            menuList = menuMapper.selectMenuList(menu);
        }
        else
        {
            menu.getParams().put("userName", userName);
            menuList = menuMapper.selectMenuListByUserName(menu);
        }
        return menuList;
    }

    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    @Override
    public List<SysMenu> selectMenuList(String userId)
    {
        return selectMenuList(new SysMenu(), userId);
    }

    @Override
    public List<Integer> selectMenuListByRoleName(String roleName) {
        return menuMapper.selectMenuListByRoleId(roleName, true);
    }

    @Override
    public boolean hasChildByMenuId(Long menuId) {
        List<SysMenu> menus = menuMapper.selectList(new QueryWrapper<SysMenu>().eq("parent_id", menuId));
        if (menus!=null && menus.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean checkMenuExistRole(Long menuId) {
        List<SysRoleMenu> roleMenuList = roleMenuMapper.selectList(new QueryWrapper<SysRoleMenu>().eq("menu_id", menuId));
        if(roleMenuList!=null && roleMenuList.size()>0){
            return true;
        }
        return false;
    }

    private List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        List<Integer> tempList = menus.stream().map(SysMenu::getMenuId).collect(Collectors.toList());
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext();)
        {
            SysMenu menu = (SysMenu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId()))
            {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu)
    {
        String routerName = StringUtils.capitalize(menu.getPath());
        return routerName;
    }

    public String getComponent(SysMenu menu)
    {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()))
        {
            component = menu.getComponent();
        }
        return component;
    }


    public String getRouterPath(SysMenu menu)
    {
        String routerPath = menu.getPath();

        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME==menu.getIsFrame().intValue())
        {
            routerPath = "/" + menu.getPath();
        }
        return routerPath;
    }
    


}
