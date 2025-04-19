package com.xunmeng.framework.interceptor;

import com.alibaba.fastjson2.JSON;
import com.xunmeng.common.annotation.RepeatSubmit;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.common.utils.ServletUtils;
import com.xunmeng.common.utils.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 防止重复提交设置
 */

@Component
public abstract class RepeatSubmitInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果该对象是方法
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod=(HandlerMethod) handler;
            Method method=handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (StringUtils.isNotNull(annotation)){
                if (this.isRepeatSubmit(request,annotation)){
                    AjaxResult ajaxResult = AjaxResult.error(annotation.message());
                    ServletUtils.renderString(response, JSON.toJSONString(ajaxResult));
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request,RepeatSubmit annotation);
}
