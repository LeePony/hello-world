package com.ztesoft.zsmart.bss.selfcare.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.web.handler.ExecutionSerializer;
import com.ztesoft.zsmart.web.handler.WebHandler;
import com.ztesoft.zsmart.web.resource.Common;

/**
 * <Description> <br>
 * 
 * @author yao.yueqing<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月7日 <br>
 * @since CRM8.1 <br>
 * @see com.ztesoft.zsmart.bss.selfcare.servlet <br>
 */
public class BusiFacadeServlet extends HttpServlet {

    /**
     * serialVersionUID <br>
     */
    private static final long serialVersionUID = 1L;

    /**
     * logger <br>
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(BusiFacadeServlet.class);

    /**
     * CONTENT_TYPE
     */
    private static final String CONTENT_TYPE = "text/xml; charset=UTF-8";

    /**
     * Description: <br>
     * 
     * @author XXX<br>
     * @taskId <br>
     * @param request 
     * @param response 
     * @throws ServletException 
     * @throws IOException <br>
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(Common.LOCAL_CHARSET);
        ServletOutputStream out = response.getOutputStream();

        byte[] returnValue = null;
        try {
            try {
                WebHandler handler = new WebHandler();
                returnValue = handler.execute(request, response);
            }
            catch (Throwable e) {
                logger.error("This is BusiFacadeServlet : [{}]", e);
                try {
                    if (e instanceof BaseAppException) {
                        returnValue = ExecutionSerializer.packageException(e);
                    }
                    else {
                        BaseAppException bae = new BaseAppException("", ExceptionHandler.INNER_ERROR, e);
                        returnValue = ExecutionSerializer.packageException(bae);
                        // out.write(returnValue);
                        // out.flush();
                    }
                    // logger.error("return Exception package: " + returnValue);
                }
                catch (Exception ex) {
                    returnValue = e.toString().getBytes();

                }
            }
            out.write(returnValue);
            out.flush();
        }
        finally {
            returnValue = null;
            if (out != null) {
                out.close();
            }
            if (request != null) {
                request = null;
            }
            if (response != null) {
                response = null;
            }
        }
    }

}
