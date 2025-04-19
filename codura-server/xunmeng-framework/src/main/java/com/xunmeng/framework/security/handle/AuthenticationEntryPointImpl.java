package com.xunmeng.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.xunmeng.common.constant.HttpStatus;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.common.utils.ServletUtils;
import com.xunmeng.common.utils.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        int code = HttpStatus.UNAUTHORIZED;
        String msg= StringUtils.format("请求访问：{},认证失败，无法访问系统资源",httpServletRequest.getRequestURI());
        ServletUtils.renderString(httpServletResponse, JSON.toJSONString(AjaxResult.error(code,msg)));
    }
    
}
