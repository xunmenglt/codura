package com.xunmeng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xunmeng.common.core.pojo.entity.SysRole;
import com.xunmeng.common.core.pojo.entity.SysUser;
import com.xunmeng.common.exception.ServiceException;
import com.xunmeng.common.utils.StringUtils;
import com.xunmeng.system.mapper.SysRoleMapper;
import com.xunmeng.system.mapper.SysUserMapper;
import com.xunmeng.system.mapper.SysUserRoleMapper;
import com.xunmeng.system.pojo.SysUserRole;
import com.xunmeng.system.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-05
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private SysRoleMapper roleMapper;
    
    @Override
    public List<SysUser> selectUserList(SysUser user) {
        
        
        return userMapper.selectUserList(user);
        
    }

    @Override
    public SysUser selectUserByName(String userName) {
        
        return userMapper.selectUserByName(userName);
    }

    @Override
    public boolean checkUserNameUnique(SysUser user) {

        SysUser su =userMapper.selectById(user.getUserName());
        if (su==null){
            return true;
        }
        return false;

    }

    @Override
    public boolean checkEmailUnique(SysUser user) {
        SysUser su = userMapper.selectOne(new QueryWrapper<SysUser>().eq("email", user.getEmail()));
        if (su==null || user.getUserName().equals(su.getUserName())){
            return true;
        }
        return false;
    }

    @Override
    public int updateUser(SysUser user) {
        checkUserAllowed(user);
        String userName = user.getUserName();
        // 删除用户与角色关联
        userRoleMapper.delete(new QueryWrapper<SysUserRole>().eq("user_name",userName));
        // 新增用户与角色管理
        insertUserRole(user);
        user.setUpdateTime(new Date());
        return userMapper.updateById(user);
        
    }

    @Override
    public int deleteUserByNames(String[] userNames) {
        for (String userName : userNames)
        {
            checkUserAllowed(new SysUser(userName));
        }
        // 删除用户与角色关联
        userRoleMapper.deleteUserRole(userNames);
        return userMapper.deleteBatchIds(Arrays.asList(userNames));
    }

    @Override
    public boolean checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserName()) && user.isAdmin())
        {
            throw new ServiceException("不允许操作超级管理员用户");
        }
        return true;
    }

    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateById(user);
    }

    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateById(user);
    }

    @Override
    public void insertUserAuth(String userName, String[] roleNames) {
        userRoleMapper.deleteUserRole(new String[]{userName});
        insertUserRole(userName,roleNames);
    }

    @Override
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = userMapper.insert(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    @Override
    public List<SysRole> selectUserRoleGroup(String userName) {

        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        return list;
    }

    @Override
    public int resetUserPwd(String userName, String newPassword) {
        SysUser sysUser = new SysUser(userName);
        sysUser.setPassword(newPassword);
        return userMapper.updateById(sysUser);
    }

    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        SysUser sysUser = new SysUser(userName);
        sysUser.setAvatar(avatar);
        return userMapper.updateById(sysUser)>0;
    }

    private void insertUserRole(SysUser user) {
        this.insertUserRole(user.getUserName(), user.getRoleIds());
    }

    private void insertUserRole(String userName, String[] roleIds) {
        if (StringUtils.isNotEmpty(roleIds))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
            for (String roleName : roleIds)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserName(userName);
                ur.setRoleName(roleName);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
    }
}
