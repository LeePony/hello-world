/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.ztesoft.zsmart.bss.selfcare.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ztesoft.zsmart.bss.selfcare.common.core.api.ActionDomain;
import com.ztesoft.zsmart.bss.selfcare.common.core.api.CoreDef;
import com.ztesoft.zsmart.bss.selfcare.common.helper.CommonHelper;
import com.ztesoft.zsmart.bss.selfcare.common.util.MvcBoUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.Constants;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.web.resource.Common;

/**
 * <Description> <br>
 * 
 * @author hu.jianfei<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2013-1-24 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.esc.mvc.setup <br>
 */
public class LogoutController {

    /**
     * logger <br>
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(LogoutController.class);

    /**
     * Description: <br>
     * 
     * @author hu.jianfei<br>
     * @taskId <br>
     * @param request <br>
     * @param response <br>
     * @throws BaseAppException <br>
     * @throws IOException <br>
     */
    @RequestMapping("Logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws BaseAppException, IOException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        DynamicDict sessionBo = dict.getBO(CoreDef.BO_SESSION);
        String loginPage = null;
        if (sessionBo != null) {
            loginPage = sessionBo.getString(ActionDomain.LOGIN_PAGE);
        }
        if (StringUtil.isEmpty(loginPage)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c != null) {
                        String cookieName = c.getName();
                        if (ActionDomain.COOKIE_USER_ROLE_CODE_NAME.equals(cookieName)) {
                            loginPage = c.getValue();
                        }
                    }
                }
            }
        }

        HttpSession session = request.getSession();
        Object userCode = session.getAttribute(Constants.SSO_APP_USER_NAME);

        // 在session监听器里调注销接口，修改登陆日志

        session.invalidate();
        logger.debug("Success log out");
        invalidateCookie(request, response); // del cookie

        if (CoreDef.LOGIN_PAGE_SSO.equals(loginPage)) {

            // 返回sso登陆页面
            if (userCode == null) {
                userCode = "";
            }
            String ssoUrl = getSsoLogoutURL(userCode.toString(), Common.getIpAddr(request));
            logger.info("Logout url: {}, User code: {}", ssoUrl, userCode);
            response.sendRedirect(ssoUrl);
        }
        else {
            String g_Webroot = CommonHelper.getWebRoot(request);
            logger.debug("g_Webroot:{}", g_Webroot);
            response.sendRedirect(g_Webroot + "Login.jsp");
        }
    }

    /**
     * Description: <br>
     * 
     * @author Xu.Fuqin<br>
     * @taskId <br>
     * @param userCode <br>
     * @param ip <br>
     * @return <br>
     */
    private String getSsoLogoutURL(String userCode, String ip) {
        String url = null;
        try {
            Class<?> c = Class.forName("com.ztesoft.zsmart.sso.web.filter.SSOHttpFilter");
            Method method = c.getMethod("getSSOLogoutURL", String.class, String.class);
            method.setAccessible(true);
            url = String.valueOf(method.invoke(c.newInstance(), userCode, ip));
        }
        catch (ClassNotFoundException e) {
            logger.error(e);
        }
        catch (SecurityException e) {
            logger.error(e);
        }
        catch (NoSuchMethodException e) {
            logger.error(e);
        }
        catch (IllegalArgumentException e) {
            logger.error(e);
        }
        catch (IllegalAccessException e) {
            logger.error(e);
        }
        catch (InvocationTargetException e) {
            logger.error(e);
        }
        catch (InstantiationException e) {
            logger.error(e);
        }
        return url;
    }

    /**
     * del all cookies <br>
     * 
     * @param request <br>
     * @param response <br>
     */
    private void invalidateCookie(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("------invalidateCookie-----");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                // user_name不删
                if (!"user_name".equals(c.getName())) {
                    c.setMaxAge(0);
                    response.addCookie(c);
                }
            }
        }
    }

    /**
     * Description:用于operator内部员工在被别的客户端踢下线后，error.jsp页面返回到登陆页面时调用 <br>
     * 
     * @author Xu.Fuqin<br>
     * @taskId <br>
     * @param request <br>
     * @param response <br>
     * @throws BaseAppException <br>
     * @throws IOException <br>
     */
    @RequestMapping("InnerStaffTLogout")
    public void innerStaffTlogout(HttpServletRequest request, HttpServletResponse response) throws BaseAppException,
        IOException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        String staffId = dict.getString("PARTY_CODE");
        if (staffId == null) {
            staffId = "";
        }
        String loginPage = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c != null) {
                    String cookieName = c.getName();
                    if (ActionDomain.COOKIE_USER_ROLE_CODE_NAME.equals(cookieName)) {
                        loginPage = c.getValue();
                    }
                }
            }
        }
        invalidateCookie(request, response); // del cookie

        if (CoreDef.LOGIN_PAGE_SSO.equals(loginPage)) {

            String ssoUrl = getSsoLogoutURL(staffId, Common.getIpAddr(request));
            logger.info("Logout url: {}, User code: {}", ssoUrl, staffId);
            response.sendRedirect(ssoUrl);
        }
        else {
            String g_Webroot = CommonHelper.getWebRoot(request);
            logger.debug("g_Webroot:{}", g_Webroot);
            response.sendRedirect(g_Webroot + "Login.jsp");
        }
    }
}
