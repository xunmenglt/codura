package com.xunmeng.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunmeng.common.core.pojo.entity.SysRole;

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
public interface ISysRoleService extends IService<SysRole> {

    Set<String> selectRolePermissionByUserName(String userName);

    List<SysRole> selectRoleList(SysRole role);

    SysRole selectRoleByName(String roleName);

    boolean checkRoleNameUnique(SysRole role);

    int insertRole(SysRole role);

    void checkRoleAllowed(SysRole role);

    int updateRole(SysRole role);

    int deleteRoleByNames(String[] roleNames);
}
