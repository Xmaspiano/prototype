package com.helper.route.shiro;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/21 - 上午11:19
 * Created by IntelliJ IDEA.
 */
public class MyAuthzFilter extends AccessControlFilter {
    Logger log = LoggerFactory.getLogger(MyAuthzFilter.class);
    /**
     * Returns <code>true</code> if the request is allowed to proceed through the filter normally, or <code>false</code>
     * if the request should be handled by the
     * {@link #onAccessDenied(ServletRequest, ServletResponse, Object) onAccessDenied(request,response,mappedValue)}
     * method instead.
     *
     * @param request     the incoming <code>ServletRequest</code>
     * @param response    the outgoing <code>ServletResponse</code>
     * @param mappedValue the filter-specific config value mapped to this filter in the URL rules mappings.
     * @return <code>true</code> if the request should proceed through the filter normally, <code>false</code> if the
     * request should be processed by this filter's
     * {@link #onAccessDenied(ServletRequest, ServletResponse, Object)} method instead.
     * @throws Exception if an error occurs during processing.
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.info("[MyAuthzFilter.isAccessAllowed()]getRequestURI is :"+httpRequest.getRequestURI());
        log.info("[MyAuthzFilter.isAccessAllowed()]getServletPath is :"+httpRequest.getServletPath());


        /* 以下将编制权限控制体系，判断用户是否具有URL访问权限 */
        return Boolean.TRUE;
    }

    /**
     * Processes requests where the subject was denied access as determined by the
     * {@link #isAccessAllowed(ServletRequest, ServletResponse, Object) isAccessAllowed}
     * method.
     *
     * @param request  the incoming <code>ServletRequest</code>
     * @param response the outgoing <code>ServletResponse</code>
     * @return <code>true</code> if the request should continue to be processed; false if the subclass will
     * handle/render the response directly.
     * @throws Exception if there is an error processing the request.
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.info("[MyAuthzFilter.onAccessDenied()]"+httpRequest.getUserPrincipal());
        log.info("[MyAuthzFilter.onAccessDenied()]"+httpRequest.getServletPath());
        log.info("[MyAuthzFilter.onAccessDenied()] message end !!!");
        return Boolean.FALSE;
    }
}
