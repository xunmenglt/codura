package com.xunmeng.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xunmeng.common.constant.HttpStatus;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.common.utils.ServletUtils;
import com.xunmeng.common.utils.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        int code = HttpStatus.FORBIDDEN;
        String msg= StringUtils.format("请求访问：{},授权失败，禁止访问系统资源",httpServletRequest.getRequestURI());
        ServletUtils.renderString(httpServletResponse, JSON.toJSONString(AjaxResult.error(code,msg)));
    }
}
