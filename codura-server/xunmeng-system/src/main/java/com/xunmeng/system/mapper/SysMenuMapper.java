package com.xunmeng.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunmeng.common.core.pojo.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-05
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<String> selectMenuPermsByRoleName(String roleName);

    List<String> selectMenuPermsByUserName(String userName);

    List<SysMenu> selectMenuTreeAll();

    List<SysMenu> selectMenuTreeByUserName(String userName);

    List<SysMenu> selectMenuList(SysMenu menu);
    
    List<SysMenu> selectMenuListByUserName(SysMenu menu);

    List<Integer> selectMenuListByRoleId(@Param("roleName") String roleName, @Param("menuCheckStrictly") boolean menuCheckStrictly);
}
