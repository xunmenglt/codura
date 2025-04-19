package com.xunmeng.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunmeng.common.core.pojo.entity.SysRole;
import com.xunmeng.common.core.pojo.entity.SysUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-05
 */
public interface ISysUserService extends IService<SysUser> {

    List<SysUser> selectUserList(SysUser user);


    SysUser selectUserByName(String userName);

    boolean checkUserNameUnique(SysUser user);

    boolean checkEmailUnique(SysUser user);

    int updateUser(SysUser user);

    int deleteUserByNames(String[] userNames);
    
    boolean checkUserAllowed(SysUser user);

    int resetPwd(SysUser user);

    int updateUserStatus(SysUser user);

    void insertUserAuth(String userName, String[] roleNames);

    int insertUser(SysUser user);

    List<SysRole> selectUserRoleGroup(String userName);

    int resetUserPwd(String userName, String newPassword);

    boolean updateUserAvatar(String username, String avatar);
}
