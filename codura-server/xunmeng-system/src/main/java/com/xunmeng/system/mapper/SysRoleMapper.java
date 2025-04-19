package com.xunmeng.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunmeng.common.core.pojo.entity.SysRole;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-05
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> selectRolePermissionByUserName(String userName);

    List<SysRole> selectRoleList(SysRole role);

    SysRole selectRoleByName(String roleName);

    List<SysRole> selectRolesByUserName(String userName);
}
