package com.xunmeng.system.mapper;

import com.xunmeng.common.core.pojo.entity.SysRole;
import com.xunmeng.system.pojo.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-05
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    void batchUserRole(List<SysUserRole> roleList);

    void deleteUserRole(String[] userNames);
    
    
}
