package com.xunmeng.framework.web.service;


import com.xunmeng.common.constant.CacheConstants;
import com.xunmeng.common.constant.UserConstants;
import com.xunmeng.common.core.pojo.model.LoginUser;
import com.xunmeng.common.core.redis.RedisCache;
import com.xunmeng.common.exception.ServiceException;
import com.xunmeng.common.exception.user.CaptchaException;
import com.xunmeng.common.exception.user.CaptchaExpireException;
import com.xunmeng.common.exception.user.UserNotExistsException;
import com.xunmeng.common.exception.user.UserPasswordNotMatchException;
import com.xunmeng.common.utils.StringUtils;
import com.xunmeng.framework.security.context.AuthenticationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysLoginService {

    private static final Logger log = LoggerFactory.getLogger(SysLoginService.class);


    @Resource
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private RedisCache redisCache;
    public String login(String username, String password, String code, String uuid) {
        
        // 验证码校验
        validateCaptcha(username,code,uuid);
        // 登录前置校验
        loginPreCheck(username, password);
        
        //用户密码校验
        Authentication authentication=null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            authentication = authenticationManager.authenticate(authenticationToken);
        }catch (Exception e){
            if (e instanceof BadCredentialsException)
            {
                // todo 记录用户登录信息
                throw new UserPasswordNotMatchException();
            }else {
                // todo 记录用户登录信息
                throw new ServiceException(e.getMessage());
            }
        }finally {
            AuthenticationContextHolder.clearContext();
        }
        // todo 记录用户登录成功信息
        LoginUser loginUser=(LoginUser) authentication.getPrincipal();
        
        // 生成token
        return tokenService.createToken(loginUser);
    }

    private void loginPreCheck(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            // todo 记录用户登录失败信息，用户名或密码为空
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            // todo 记录用户登录失败信息，密码长度受到限制
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            // todo 记录用户登录失败信息，用户名长度受到限制
            throw new UserPasswordNotMatchException();
        }
        
    }

    private void validateCaptcha(String username, String code, String uuid) {
        String verfykey= CacheConstants.CAPTCHA_CODE_KEY+ StringUtils.nvl(uuid,"");
        String captcha=redisCache.getCacheObject(verfykey);
        if (captcha==null){
            log.info("用户 {} 输入的验证码已过期",username);
            // todo 记录用户登录失败信息，验证码过期
            throw new CaptchaExpireException();
        }
        if (!captcha.equalsIgnoreCase(code)){
            log.info("用户 {} 输入的验证码 {} 不正确,正确验证码为 {}",username,code,captcha);
            // todo 记录用户登录失败信息，验证码不正确
            throw new CaptchaException();
        }
    }
    
    
}
