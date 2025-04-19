package com.xunmeng.system.mapper;

import com.xunmeng.system.pojo.SysRoleMenu;
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
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    int batchRoleMenu(List<SysRoleMenu> list);

    int deleteBatchByRoleNames(String[] roleNames);
    
    int deleteRoleMenuByRoleName(String roleName);
}
