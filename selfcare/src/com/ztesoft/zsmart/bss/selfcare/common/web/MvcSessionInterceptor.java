/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.ztesoft.zsmart.bss.selfcare.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.Session;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;

/**
 * <br>
 * 
 * @author <br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2013-7-11 <br>
 * @since V7.3<br>
 * @see com.ztesoft.zsmart.bss.esc.common.core.web <br>
 */

public class MvcSessionInterceptor extends HandlerInterceptorAdapter {

    /**
     * logger <br>
     */
    ZSmartLogger logger = ZSmartLogger.getLogger(MvcSessionInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse,
        Object obj, Exception exception) throws BaseAppException {
        if (null != exception) {
            logger.warn(exception);
        }
        closeSession();
    }

    /**
     * <br>
     * 
     * @author <br>
     * @taskId <br>
     * @throws BaseAppException <br>
     */
    private void closeSession() throws BaseAppException {
        if (SessionContext.existCurrentSession()) {
            Session session = SessionContext.currentSession();
            if (session != null) {
                session.commitTrans();
                try {
                    session.releaseTrans();
                }
                catch (BaseAppException e) {
                    logger.error(e);
                }
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Object obj,
        ModelAndView modelandview) throws BaseAppException {
        closeSession();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws BaseAppException {
        Session session = SessionContext.newSession();
        session.beginTrans();
        return true;
    }
}
