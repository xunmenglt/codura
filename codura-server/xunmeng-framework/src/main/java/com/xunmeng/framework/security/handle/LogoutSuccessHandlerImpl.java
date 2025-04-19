package com.xunmeng.framework.security.handle;


import com.alibaba.fastjson2.JSON;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.common.core.pojo.model.LoginUser;
import com.xunmeng.common.utils.MessageUtils;
import com.xunmeng.common.utils.ServletUtils;
import com.xunmeng.common.utils.StringUtils;
import com.xunmeng.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {


    @Autowired
    private TokenService tokenService;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser=tokenService.getLoginUser(httpServletRequest);
        if (StringUtils.isNotNull(loginUser)){
            String username=loginUser.getUsername();
            tokenService.delLoginUser(loginUser.getToken());
        }
        ServletUtils.renderString(httpServletResponse, JSON.toJSONString(AjaxResult.success(MessageUtils.message("user.logout.success"))));
    }
}
