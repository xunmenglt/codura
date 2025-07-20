package com.xunmeng.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunmeng.common.core.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Update;

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


    @Update("UPDATE sys_user SET enabled=#{enabled} WHERE user_name=#{userName}")
    int updateUserStatus(SysUser user);

    int deleteUserByIds(String[] userNames);
}
