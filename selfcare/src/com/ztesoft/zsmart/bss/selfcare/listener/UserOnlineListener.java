/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.ztesoft.zsmart.bss.selfcare.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.ztesoft.zsmart.bss.selfcare.model.SessionDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.web.resource.Common;

/**
 * <Description> <br>
 * 
 * @author Xu.Fuqin<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2013-3-14 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.esc.common.listener <br>
 */
public class UserOnlineListener implements HttpSessionListener, HttpSessionAttributeListener {

    /**
     * logger
     */
    private static final ZSmartLogger logger = ZSmartLogger.getLogger(UserOnlineListener.class);

    /**
     * private constructor
     */
    public UserOnlineListener() {
    }

    /**
     * Description:属性移除 <br>
     * 
     * @author Xu.Fuqin<br>
     * @taskId <br>
     * @param arg0 <br>
     */
    public void attributeRemoved(HttpSessionBindingEvent arg0) {
        String sessionId = arg0.getSession().getId();
        logger.debug("attributeRemoved");
        logger.debug("session id=[{}]", sessionId);
    }

    /**
     * Description:属性替代 <br>
     * 
     * @author Xu.Fuqin<br>
     * @taskId <br>
     * @param arg0 <br>
     */
    public void attributeReplaced(HttpSessionBindingEvent arg0) {
        logger.debug("attributeReplaced");
        logger.debug("session id=[{}]", arg0.getSession().getId());
    }

    /**
     * Description:新增属性 <br>
     * 
     * @author Xu.Fuqin<br>
     * @taskId <br>
     * @param arg0 <br>
     */
    public void attributeAdded(HttpSessionBindingEvent arg0) {
        logger.debug("attributeAdded");
        logger.debug("session id=[{}]", arg0.getSession().getId());

    }

    /**
     * Description:session销毁 <br>
     * 
     * @author Xu.Fuqin<br>
     * @taskId <br>
     * @param arg0 <br>
     */
    public void sessionDestroyed(HttpSessionEvent arg0) {
        logger.info("sessionDestroyed info session id = [{}]", arg0.getSession().getId());
        HttpSession session = arg0.getSession();
        if (session.getAttribute(Common.USER_INFO_BEAN) != null) { // session 销毁的时候
            try {
                SessionDto sessionDto = (SessionDto) session.getAttribute(Common.USER_INFO_BEAN);

                DynamicDict logoutDict = new DynamicDict();
                logoutDict.set("USER_ID", sessionDto.getSsoUserId());
                logoutDict.set("SESSION_ID", sessionDto.getSessionId());
                logoutDict.setServiceName("Logout");
//                PerformtUtil.callService(logoutDict);

                // 注销后，更新登陆日志
                DynamicDict logDict = new DynamicDict();
                logDict.setServiceName("UpdateEscLoginLogForLogout");
                logDict.set("LOGIN_LOG_ID", sessionDto.getLoginLogId());
//                PerformtUtil.callService(logDict);

                // 从在线监控表中去除
                DynamicDict monitorDict = new DynamicDict();
                monitorDict.set("SESSION_ID", sessionDto.getSelfSessionId());
                monitorDict.serviceName = "UserLogoutMonitor";
//                PerformtUtil.callService(monitorDict);
            }
            catch (BaseAppException e) {
                logger.error("session destroy error.", e);
            }
        }
    }

    /**
     * Description: <br>
     * 
     * @author Xu.Fuqin<br>
     * @taskId <br>
     * @param arg0 <br>
     */
    public void sessionCreated(HttpSessionEvent arg0) {
        logger.debug("sessionCreated");
        logger.debug("session id=[{}]", arg0.getSession().getId());
    }

}
