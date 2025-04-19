package com.xunmeng.framework.web.service;

import com.xunmeng.common.core.pojo.entity.SysUser;
import com.xunmeng.common.core.pojo.model.LoginUser;
import com.xunmeng.common.enums.UserStatus;
import com.xunmeng.common.exception.ServiceException;
import com.xunmeng.common.utils.MessageUtils;
import com.xunmeng.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    
    @Autowired
    private ISysUserService userService;
    
    
    @Autowired
    private SysPasswordService passwordService;
    
    @Autowired
    private SysPermissionService permissionService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.getById(username);
        if (null==user){
            log.info("登录用户：{} 不存在.",username);
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        }else if (UserStatus.DISABLE.getCode()==user.getEnabled()){
            // 用户被禁用
            log.info("登录用户：{} 已经被停用.",username);
            throw new ServiceException(MessageUtils.message("user.blocked"));
        }
        
        passwordService.validate(user);
        return createLoginUser(user);
    }
    public LoginUser createLoginUser(SysUser user){
        return new LoginUser(user.getUserName(),user,permissionService.getMenuPermission(user));
    }
}
