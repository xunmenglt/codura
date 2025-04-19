package com.xunmeng.framework.web.service;


import com.xunmeng.common.constant.CacheConstants;
import com.xunmeng.common.core.pojo.entity.SysUser;
import com.xunmeng.common.core.redis.RedisCache;
import com.xunmeng.common.exception.user.UserPasswordNotMatchException;
import com.xunmeng.common.exception.user.UserPasswordRetryLimitExceedException;
import com.xunmeng.common.utils.SecurityUtils;
import com.xunmeng.framework.security.context.AuthenticationContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 登录密码方法
 */
@Service
public class SysPasswordService {
    
    @Autowired
    private RedisCache redisCache;
    
    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;
    
    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username){
        return CacheConstants.PWD_ERR_CNT_KEY+username;
    }

    /**
     * 验证用户登录次数
     */
    public void validate(SysUser user){
        Authentication usernamePasswordAuthenticationToken= AuthenticationContextHolder.getContext();
        String username=usernamePasswordAuthenticationToken.getName();
        String password=usernamePasswordAuthenticationToken.getCredentials().toString();
        
        Integer retryCount=redisCache.getCacheObject(getCacheKey(username));
        
        if (retryCount==null){
            retryCount=0;
        }
        
        if (retryCount>=Integer.valueOf(maxRetryCount).intValue()){
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }
        
        if (!matches(user,password)){
            retryCount = retryCount + 1;
            redisCache.setCacheObject(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES);
            throw new UserPasswordNotMatchException();
        }
        
        else {
            clearLoginRecordCache(username);
        }
        
    }

    public boolean matches(SysUser user, String rawPassword){
        return SecurityUtils.matchesPassword(rawPassword,user.getPassword());
    }
    
    public void clearLoginRecordCache(String username){
        redisCache.deleteObject(getCacheKey(username));
    }
    
}
