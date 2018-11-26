package com.yunnex.ops.erp.modules.sys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 跨域拦截器，设置响应头允许跨域访问
 */
public class CrossDomainInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*"); // 解决跨域访问报错
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600"); // 设置过期时间
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, client_id, uuid, Authorization");
        // 支持HTTP 1.1.
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        // 支持HTTP 1.0.
        response.setHeader("Pragma", "no-cache");

        return true;  
    }
    
}
