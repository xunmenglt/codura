package com.xunmeng.common.utils;

import com.xunmeng.common.constant.Constants;
import com.xunmeng.common.constant.HttpStatus;
import com.xunmeng.common.core.pojo.entity.SysRole;
import com.xunmeng.common.core.pojo.model.LoginUser;
import com.xunmeng.common.exception.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务安全工具类
 *
 * @author xunmeng
 */
public class SecurityUtils {

    /**
     * 用户ID
     */
    public static String getUserId(){
        try {
            return getLoginUser().getUserId();
        }catch (Exception e){
            throw new ServiceException("获取用户ID异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户账户
     **/
    public static String getUsername()
    {
        try
        {
            return getLoginUser().getUsername();
        }
        catch (Exception e)
        {
            throw new ServiceException("获取用户账户异常", HttpStatus.UNAUTHORIZED);
        }
    }

    
    /**
     * 登录用户
     */
    public static LoginUser getLoginUser(){
        try{
            return (LoginUser) getAuthentication().getPrincipal();
        }catch (Exception e){
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(password);
        return encode;
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword,encodedPassword);
    }

    /**
     * 判断是否是总管理用户
     * @param userId
     * @return
     */
    public static boolean isAdmin(String userId){
        return StringUtils.isNotEmpty(userId) && userId.equalsIgnoreCase("admin");
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public static boolean hasPermi(Collection<String> authorities,String permission){
        return authorities.stream()
                .filter(StringUtils::hasText)
                .anyMatch(x-> Constants.ALL_PERMISSION.equals(x) || PatternMatchUtils.simpleMatch(x,permission));
    }

    /**
     * 验证用户是否拥有某个角色
     *
     * @param role 角色标识
     * @return 用户是否具备某角色
     */
    public static boolean hasRole(String role){
        List<SysRole> roleList=getLoginUser().getUser().getRoles();
        Collection<String> roles = roleList.stream().map(SysRole::getRoleName).collect(Collectors.toSet());
        return hasRole(roles, role);
    }


    /**
     * 判断是否包含角色
     *
     * @param roles 角色列表
     * @param role 角色
     * @return 用户是否具备某角色权限
     */
    public static boolean hasRole(Collection<String> roles, String role)
    {
        return roles.stream().filter(StringUtils::hasText)
                .anyMatch(x -> Constants.SUPER_ADMIN.equals(x) || PatternMatchUtils.simpleMatch(x, role));
    }


}
