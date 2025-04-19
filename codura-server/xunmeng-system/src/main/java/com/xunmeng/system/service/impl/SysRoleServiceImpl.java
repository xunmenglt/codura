package com.xunmeng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xunmeng.common.core.pojo.entity.SysRole;
import com.xunmeng.common.exception.ServiceException;
import com.xunmeng.common.utils.StringUtils;
import com.xunmeng.system.mapper.SysRoleMapper;
import com.xunmeng.system.mapper.SysRoleMenuMapper;
import com.xunmeng.system.mapper.SysUserRoleMapper;
import com.xunmeng.system.pojo.SysRoleMenu;
import com.xunmeng.system.pojo.SysUserRole;
import com.xunmeng.system.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-05
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysRoleMenuMapper roleMenuMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    
    @Override
    public Set<String> selectRolePermissionByUserName(String userName) {
        List<SysRole> perms=roleMapper.selectRolePermissionByUserName(userName);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)){
                permsSet.add(perm.getRoleName().trim());
            }
        }
        
        return permsSet;
    }

    @Override
    public List<SysRole> selectRoleList(SysRole role) {
        return roleMapper.selectRoleList(role);
    }

    @Override
    public SysRole selectRoleByName(String roleName) {
        
        return roleMapper.selectRoleByName(roleName);
        
    }

    @Override
    public boolean checkRoleNameUnique(SysRole role) {
        SysRole sysRole = roleMapper.selectById(role.getRoleName());
        if (sysRole!=null){
            return false;
        }
        return true;
    }

    @Override
    public int insertRole(SysRole role) {
        // 新增角色信息
        roleMapper.insert(role);
        return insertRoleMenu(role);
    }

    @Override
    public void checkRoleAllowed(SysRole role) {
        
        if (StringUtils.isNotNull(role) && !"admin".equalsIgnoreCase(role.getRoleName())){
            return;
        }
        throw new ServiceException("不允许操作超级管理员角色");
        
    }

    @Override
    public int updateRole(SysRole role) {
        
        roleMapper.updateById(role);
        // 删除角色与菜单关联
        roleMenuMapper.delete(new QueryWrapper<SysRoleMenu>().eq("role_name",role.getRoleName()));
        return insertRoleMenu(role);
    }

    @Override
    public int deleteRoleByNames(String[] roleNames) {
        for (String roleName : roleNames) {
            checkRoleAllowed(new SysRole(roleName));
            // 判断角色是否分配给用户
            List<SysUserRole> roleList = userRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("role_name", roleName));
            if (roleList!=null && roleList.size()>0){
                throw new ServiceException(String.format("%1$s已分配,不能删除", roleName));
            }
        }
        // 删除角色与菜单关联
        roleMenuMapper.deleteBatchByRoleNames(roleNames);
        // 删除角色对应的菜单
        return roleMapper.deleteBatchIds(Arrays.asList(roleNames));

    }

    /**
     * 插入角色菜单
     * @param role
     * @return
     */
    private int insertRoleMenu(SysRole role) {
        int rows=1;
        // 新增角色与菜单管理
        List<SysRoleMenu> list=new ArrayList<>();
        for (Integer menuId : role.getMenuIds())
        {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleName(role.getRoleName());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0)
        {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }
}
