package com.xunmeng.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunmeng.common.core.pojo.entity.SysUser;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-05
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<SysUser> selectUserList(SysUser user);

    SysUser selectUserByName(String userName);

    int deleteUserByIds(String[] userNames);
}
