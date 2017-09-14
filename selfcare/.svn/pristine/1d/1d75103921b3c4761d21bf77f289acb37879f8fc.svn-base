package com.ztesoft.zsmart.bss.selfcare.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.Session;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.web.mvc.exception.ExceptionResolver;

/**
 * <br>
 * 
 * @author qiao.zhu<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2013-7-17 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.esc.common.core.web <br>
 */
@Service
public class MvcExceptionResolver extends ExceptionResolver {
    /**
     * logger <br>
     */
    ZSmartLogger logger = ZSmartLogger.getLogger(this.getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
        Exception exception) {
        try {
            if (SessionContext.existCurrentSession()) {
                Session session = SessionContext.currentSession();
                if (session != null) {
                    try {
                        session.releaseTrans();
                    }
                    catch (BaseAppException e) {
                        logger.error(e);
                    }
                }
            }
        }
        catch (BaseAppException e) {
            logger.error(e);
        }
        return super.resolveException(request, response, handler, exception);
    }

}
