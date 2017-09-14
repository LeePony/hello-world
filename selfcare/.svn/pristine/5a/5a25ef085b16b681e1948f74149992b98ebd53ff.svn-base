package com.ztesoft.zsmart.bss.selfcare.filter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ztesoft.zsmart.bss.selfcare.common.core.api.JdbcUtil4CC;
import com.ztesoft.zsmart.bss.selfcare.dao.ISystemDAO;
import com.ztesoft.zsmart.core.configuation.ConfigurationMgr;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.DAOFactory;
import com.ztesoft.zsmart.core.jdbc.Session;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.HttpCall;
import com.ztesoft.zsmart.core.utils.Constants;
import com.ztesoft.zsmart.core.utils.DateUtil;
import com.ztesoft.zsmart.core.utils.SecurityUtil;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.web.handler.ExecutionSerializer;
import com.ztesoft.zsmart.web.resource.Common;
import com.ztesoft.zsmart.web.util.IPUtil;
import com.ztesoft.zsmart.web.util.SystemParam;

/**
 * 
 * <Description> <br> 
 *  
 * @author yao.yueqing<br>
 * @version 1.0<br>
 * @taskId  <br>
 * @CreateDate 2016年6月7日 <br>
 * @since CRM8.1 <br>
 * @see com.ztesoft.zsmart.bss.selfcare.filter <br>
 */
public class SessionFilter extends HttpServlet implements Filter {
    /**
     * logger
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(SessionFilter.class);
    /**
     * logger
     */
    public static byte[] applicationLock = new byte[0]; // 定义锁，用来锁application中存储sessionId和对应ip键值对或者根据sessionId取application时候的数据
    /**
     * logger
     */
    private static final long serialVersionUID = -8214762754470838265L;
    /**
     * logger
     */
    private static final String CONTENT_TYPE = "text/xml; charset=UTF-8";
    /**
     * logger
     */
    private FilterConfig filterConfig;
    /**
     * logger
     */
    private ServletContext application;
    /**
     * logger
     */
    private static final String PASS = "PASS"; // 用于处理不需要session校验的页面的callremote操作
    /**
     * logger
     */
    private static final String CURRENT_PAGE_URL = "CURRENT_PAGE_URL";
    /**
     * logger
     */
    private String[] excludeFile = null;
    
    /**
     * dao
     */
    private ISystemDAO dao;
    
    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @return ISystemDAO
     * @throws BaseAppException <br>
     */
    private ISystemDAO getSystemDao() throws BaseAppException {
        if (dao == null) {
            dao = (ISystemDAO) DAOFactory.createModuleDAO("System", "selfcare", JdbcUtil4CC.getDbIdentifier());
        }
        return dao;
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author XXX<br>
     * @taskId <br>
     * @param filterConfig 
     * @throws ServletException <br>
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.application = filterConfig.getServletContext();
        String strExcludeFile = this.filterConfig.getInitParameter("ExcludeFile");
        if (strExcludeFile != null && strExcludeFile.trim().length() > 0) {
            excludeFile = strExcludeFile.split(",");
        }
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author XXX<br>
     * @taskId <br>
     * @param srequest 
     * @param sresponse 
     * @param filterChain 
     * @throws IOException <br>
     */
    public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain filterChain)
        throws IOException {
        String sessionId = null;

        sessionId = ((HttpServletRequest) srequest).getSession().getId();
        logger.debug("SessionId:{}, enter sessionFilter doFilter", sessionId);

        try {
            HttpServletRequest request = (HttpServletRequest) srequest;
            HttpServletResponse response = (HttpServletResponse) sresponse;

            HttpSession session = request.getSession();
            String isDevelopMode = ConfigurationMgr.instance().getString("productMode.isDevelopMode", "false");

            logger.debug("SessionId:{}, isDevelopMode-->{}", sessionId, isDevelopMode);

            String requestStr = request.getRequestURL().toString();
            String strWebRoot = Common.getWebRoot(request);
            String sourceIp = Common.getIpAddr(request);
          //  isDevelopMode = "TRUE";
            if ("TRUE".equals(isDevelopMode.toUpperCase())) { // 如果是开发模式，则直接通过验证
                logger.debug("SessionId:{}, enter isDevelopMode doFilter", sessionId);
                filterChain.doFilter(request, response);
                return;
            }

            logger.debug("SessionId:{}, requestStr-->{}", sessionId, requestStr);

            if (requestStr.endsWith("/")) {
                requestStr = requestStr + "Login.jsp";
            }
            else if(requestStr.endsWith("/sign-up")){
                response.sendRedirect(strWebRoot + "login.html?sign-up=1");
                return;
            }

            String refererUrlStr = "";
            if (request.getHeader("referer") != null) {
                refererUrlStr = request.getHeader("referer").toString();
            }

            // add by chi.yuxing [756052]
            String lastPageUrl = "";
            if (session.getAttribute(CURRENT_PAGE_URL) != null) {
                lastPageUrl = (String) session.getAttribute(CURRENT_PAGE_URL);
            }

            if (refererUrlStr.length() == 0 || refererUrlStr.endsWith("/")) {
                refererUrlStr = lastPageUrl;
            }

            logger.debug("SessionId:{}, HTTP Header :referer-->{}", sessionId, refererUrlStr);

            if (!sessionPrepare(request, response)) { // 构造Session（表示是SSO模式的情况下）
                return;
            }

            // 这里处理：不需要判断session的页面进行的前后台调用
            int length = excludeFile.length;
            if ((requestStr.indexOf("/callservice.do") > 0 || requestStr.indexOf("/messagebroker/") > 0)
                && refererUrlStr.length() > 0) { // 如果调用的是callservice.do或者是blazeds通信
                if (excludeFile != null && excludeFile.length > 0) { // 有排除的项
                    for (int j = 0; j < length; j++) {
                        if (refererUrlStr.indexOf(excludeFile[j]) > 0) {
                            filterChain.doFilter(request, response);
                            return;
                        }
                    }
                    // 判断是不是有PASS
                    String pass = "" + session.getAttribute(PASS);
                    if ("TRUE".equalsIgnoreCase(pass)) {
                        filterChain.doFilter(request, response);
                        return;
                    }
                }
            }
            // 处理其他所有的调用

            // 记录访问页面的URL by chi.yuxing [756052]
            session.setAttribute(CURRENT_PAGE_URL, requestStr);

            if (excludeFile != null && excludeFile.length > 0) {
                int i = 0;
                for (i = 0; i < length; i++) {
                    if (requestStr.indexOf(excludeFile[i]) < 0) {
                        continue;
                    }
                    else {
                        break;
                    }
                        
                }
                if (i == length) {
                    // session.setAttribute(PASS, "FALSE");
                    if (!validateSession(request, response, requestStr, strWebRoot, sourceIp)) {
                        return;
                    }
                }
                // modified by chi.yuxing 不能有这样的标志位，不然注销后服务调用也能通过校验 [751592]
                // else {
                // session.setAttribute(PASS, "TRUE");
                // }
            }
            else {
                // session.setAttribute(PASS, "FALSE");
                if (!validateSession(request, response, requestStr, strWebRoot, sourceIp)) {
                    return;
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (ServletException sx) {
            logger.error("SessionId:{}, sessionFilter error {}", sessionId, sx);
            filterConfig.getServletContext().log(sx.getMessage());
        }
        catch (IOException iox) {
            logger.error("SessionId:{}, sessionFilter error {}", sessionId, iox);
            filterConfig.getServletContext().log(iox.getMessage());
        }
        catch (Exception bx) {
            logger.error("SessionId:{}, sessionFilter error {}", sessionId, bx);
            filterConfig.getServletContext().log(bx.getMessage());
        }
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author XXX<br>
     * @taskId <br> <br>
     */
    public void destroy() {
    }

   /**
    * 
    * Description: <br> 
    *  
    * @author yao.yueqing<br>
    * @taskId  <br>
    * @param request 
    * @param response 
    * @param requestStr 
    * @param strWebRoot 
    * @param sourceIp 
    * @return boolean 
    * @throws IOException  <br>
    */
    private boolean validateSession(HttpServletRequest request, HttpServletResponse response, String requestStr,
        String strWebRoot, String sourceIp) throws IOException {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        
        List<DynamicDict> dictSessionList = null;
        Session sessionMgr = null;
        try {
            // sessionFilter 不受框架控制，容易导致：warning : connection leaking
            sessionMgr = SessionContext.newSession();
            sessionMgr.beginTrans();
            dictSessionList = getSystemDao().qryLoginSession(null, sessionId);

            String forward = ""; 
            if (dictSessionList.isEmpty() ) {
                response.setHeader("sessionstatus", "sessionTimeOut");
                
                forward = strWebRoot + "login.html"; 
                response.sendRedirect(forward);

                return false;
            } 

            // 先将此次任务提交，不要一直拿着session
            sessionMgr.commitTrans();
        }
        catch (BaseAppException e) {
            logger.error("Session check error, sessionFilter error {}", sessionId, e);
            filterConfig.getServletContext().log(e.getMessage());
        } 
        finally {
            if (sessionMgr != null) {
                try {
                    sessionMgr.releaseTrans();
                }
                catch (BaseAppException e) {
                    logger.error("Session release error, sessionFilter error {}", sessionId, e);
                }
            }
        }
        
        
        if (session != null && session.getAttribute(Common.USER_INFO_BEAN) != null) {
            Object ip;
            synchronized (applicationLock) {
                ip = application.getAttribute(session.getId());
            }

            if (ip != null && !StringUtil.isEmpty(ip.toString())) {
                if (!sourceIp.equals(ip.toString())) {
                    logger
                        .warn(
                            "SessionId:{}, Login Session Monitor. Session Filter. Source Ip is {}, Host ip is {}. "
                            + "The request ip {} is not same with the ip in application {},the application may be attacked by session cheat. ",
                            sessionId, sourceIp, Common.HOST_IP, sourceIp, ip);
                    boolean deal = false;
                    try {
                        String sessionCheatDeal = SystemParam.queryParamByMask("DEAL_SESSION_HIJACKING").getString(
                            "CURRENT_VALUE");
                        deal = "true".equalsIgnoreCase(sessionCheatDeal);
                    }
                    catch (BaseAppException e) {
                        deal = false;
                    }
                    if (deal) {
                        logger.warn("SessionId:{}, Deal Session Attack", sessionId);
                        if (requestStr.indexOf("/callservice.do") > 0) {
                            BaseAppException bp = new BaseAppException("S-SYS-00027");
                            responseException(request, response, bp);
                        }
                        else if (requestStr.indexOf("/Index.jsp") > 0) {
                            response.sendRedirect(strWebRoot + "login.html");
                        }
                        else if (requestStr.indexOf("/messagebroker/") > 0) { // 表示是Flex通信,Flex通信不能像ajax那样把流直接返回到客户端，因为它有自己的通信规则
                            request.setAttribute("_FLEX_SESSION_INVALID_", "true");
                            return true;
                        }
                        else {
                            response.sendRedirect(strWebRoot + "login.html");
                        }
                        return false; // 只有在处理的时候才返回true
                    }
                }
            }
            return true;
        }

        if (requestStr.indexOf("/callservice.do") > 0) {
            logger.warn("SessionId:{}, ajax communication, invalid session.", sessionId);
            BaseAppException bp = new BaseAppException("S-SYS-00027");
            responseException(request, response, bp);
        }
        else if (requestStr.indexOf("/Index.jsp") > 0) {
            logger.warn("SessionId:{}, goto login page.", sessionId);
            response.sendRedirect(strWebRoot + "login.html");
        }
        else if (requestStr.indexOf("/messagebroker/") > 0) { // 表示是Flex通信,Flex通信不能像ajax那样把流直接返回到客户端，因为它有自己的通信规则
            logger.warn("SessionId:{}, Blazeds Communication, invalid session.", sessionId);
            request.setAttribute("_FLEX_SESSION_INVALID_", "true");
            return true;
        }
        else {
            logger.warn("SessionId:{}, Other resources, invalid session, geto Error.jsp?timeout=1.", sessionId);
            // response.sendRedirect(strWebRoot + "Error.jsp?Timeout=1");
            String url = strWebRoot + "login.html";
            if (request.getHeader("x-requested-with") != null
                && "XMLHttpRequest".equals(request.getHeader("x-requested-with"))) { // ajax请求
                response.setHeader("sessionstatus", "sessionTimeOut");
                response.setHeader("timeouturl", url);
            }
            else {
                response.sendRedirect(url);
            }
        }
        return false;
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param request 
     * @param response 
     * @param bae 
     * @throws IOException  <br>
     */
    private void responseException(HttpServletRequest request, HttpServletResponse response, BaseAppException bae)
        throws IOException {
        response.setContentType(CONTENT_TYPE);
        String sessionId = request.getSession().getId();
        ServletOutputStream out = response.getOutputStream();
        byte[] returnValue = null;
        try {
            returnValue = ExecutionSerializer.packageException(bae);
            out.write(returnValue);
            out.flush();
            logger.error("SessionId:{}, Error occur in SessionFilter.doFilter {}.", sessionId, bae);
            logger.error("return Exception package: {}", returnValue);
        }
        catch (Exception ex) {
            returnValue = ex.toString().getBytes();
            out.write(returnValue);
            out.flush();
        }
        finally {
            returnValue = null;
            if (out != null) {
                out.close();
            }

            if (response != null) {
                response = null;
            }
        }

    }

    // public String getWebRoot(HttpServletRequest request) {
    // String strWebRoot = "";
    // if (strWebRoot == null || strWebRoot.length() == 0) {
    // strWebRoot = request.getScheme();
    // strWebRoot += "://";
    // strWebRoot += request.getServerName();
    //
    // int port = request.getServerPort();
    // if (port != 80) {
    // strWebRoot += ":" + port;
    // }
    //
    // strWebRoot += request.getContextPath() + "/";
    // }
    // return strWebRoot;
    // }

    // /**
    // * @description 获取区域与语言<br>
    // * @param request
    // * @param response
    // * @return <br>
    // */
    // public Locale getRequestLocale(HttpServletRequest request,
    // HttpServletResponse response) {
    // Locale loc = request.getLocale();
    // if (loc == null) {
    // loc = Locale.US;
    // }
    // return loc;
    // }

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param response 
     * @param cookieName 
     * @param content 
     * @param maxAge  <br>
     */
    public void setCookie(HttpServletResponse response, String cookieName, String content, int maxAge) {
        Cookie cookie = new Cookie(cookieName, content);
        cookie.setMaxAge(maxAge != 0 ? maxAge : 365 * 24 * 3600); // 有效期一年
        response.addCookie(cookie);
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param request 
     * @param response 
     * @return boolean 
     * @throws BaseAppException  <br>
     */
    private boolean sessionPrepare(HttpServletRequest request, HttpServletResponse response) throws BaseAppException {
        String sessionId = request.getSession().getId();
        boolean isSsoMode = ConfigurationMgr.instance().getBoolean("ssoMode.value", false);
        if (isSsoMode) {
            HttpSession session = request.getSession();
            if (session != null && session.getAttribute(Common.USER_INFO_BEAN) != null) { // session已经存在的情况
                return true; // 说明session已经构建过了
            }
            else if (session.getAttribute(Constants.SSO_APP_USER_NAME) != null) { // 还没有session的情况
                logger.debug("SessionId:{}, In SSO Mode, session generate, sso user name is {} ", sessionId,
                    session.getAttribute(Constants.SSO_APP_USER_NAME));
                try {
                    // Long spId = 0l; // 这里假设SP_ID为0
                    String srcIp = IPUtil.getClientIpAddr(request);
                    DynamicDict loginDict = new DynamicDict();
                    String userCode = session.getAttribute(Constants.SSO_APP_USER_NAME).toString();
                    loginDict.setServiceName("UserLoginInWhenSsoByUserCode");
                    loginDict.setValueByName("USER_CODE", userCode);
                    loginDict.setValueByName("SRC_IP", srcIp);
                    loginDict.setValueByName("DEST_IP", Common.HOST_IP);
                    // loginDict.setValueByName("SP_ID", spId);
                    loginDict.setValueByName("APP_ID", Common.APP_IP);
                    loginDict.setValueByName("SESSION_ID", sessionId); // 记录服务器端sessionID
                    HttpCall httpCall = new HttpCall();
                    httpCall.callService(loginDict); // 调用登陆接口

                    DynamicDict sysLoginDto = loginDict.getBO("USER");
                    Long userId = sysLoginDto.getLong("USER_ID");
                    DynamicDict staffInfo = new DynamicDict();
                    staffInfo.set("USER_ID", userId);
                    staffInfo.serviceName = "QryStaffInfoByUserId";
                    try {
                        httpCall.callService(staffInfo);
                    }
                    catch (Exception ex) {
                        logger.error(ex);
                    }
                    sysLoginDto.set("STAFF_INFO", staffInfo); // 获取员工信息
                    request.getSession().setAttribute("LAST_OPER_TIME", System.currentTimeMillis());
                    request.getSession().setAttribute("SYS_LOGIN_INFO", sysLoginDto);
                    request.getSession().setAttribute("LOGIN_DATE", DateUtil.getStandardNowTime());
                    request.getSession().setAttribute("LOGIN_IP", srcIp);
                    request.getSession().setAttribute("SP_ID",
                        loginDict.getLong("SP_ID") == null ? 0L : loginDict.getLong("SP_ID"));
                    request.getSession().setAttribute("APP_ID", Common.APP_IP);
                    Locale cLocale = Common.getLocale(request); // getRequestLocale(request,
                                                               // response);
                    request.getSession().setAttribute(Common.COOKIE_LOCALE_NAME, cLocale.toString());
                    request.getSession().setAttribute("USER_PARAM_LIST", loginDict.getList("USER_PARAM_LIST")); // 将USER_PARAM_LIST设置到session中//
                                                                                                               // task:132390
                    request.getSession().setAttribute("USER_ROLE_LIST", loginDict.getList("USER_ROLE_LIST"));
                    synchronized (applicationLock) {
                        application.setAttribute(request.getSession().getId(), srcIp);
                    }
                    String userToken = SecurityUtil.encrypt(userCode + "\r\n" + srcIp);
                    int maxAge = 1 * 30 * 24 * 60 * 60; // 默认cookie失效时间为一个月
                    setCookie(response, Common.COOKIE_USER_TOKEN_NAME, userToken, maxAge);
                }
                catch (Exception ex) {
                    logger.error("SessionId:{}, In SSO Mode, Error in session generate {} ", sessionId, ex);
                    Common.gotoAnotherLink(request, response, "", "Error.jsp?Timeout=3");
                    return false;
                }
                logger.debug("SessionId:{}, In SSO Mode, End session generate...", sessionId);
            }
            return true;
        }
        else {
            return true;
        }
    }
}
