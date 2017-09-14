package com.ztesoft.zsmart.bss.selfcare.bll;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import com.ztesoft.zsmart.bss.selfcare.common.core.api.CoreDef;
import com.ztesoft.zsmart.bss.selfcare.common.helper.CommonHelper;
import com.ztesoft.zsmart.bss.selfcare.common.util.MvcBoUtil;
import com.ztesoft.zsmart.bss.selfcare.model.SessionDto;
import com.ztesoft.zsmart.bss.selfcare.service.CustServices;
import com.ztesoft.zsmart.bss.selfcare.service.SubsServices;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.ActionDomain;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.web.resource.Common;

/**
 * <Description> .....<br>
 * 
 * @author QIN.BIN<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2014-3-18<br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.esc.oauth.service <br>
 */
@Controller
public class LoginMgr {

    /**
     * logger <br>
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(LoginMgr.class);

    /**
     * ... <br>
     */
    private String userCode;

    /**
     * ... <br>
     */
    private String userType;
    
    /**
     * loginAccNbr <br>
     */
    private String loginAccNbr;

    /**
     * Description: <br>
     * 
     * @author <br>
     * @taskId <br>
     * @return <br>
     */
    public String getUserCode() {
        return userCode;
    }

    /**
     * Description: <br>
     * 
     * @author <br>
     * @taskId <br>
     * @param userCode <br>
     */
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    /**
     * Description: <br>
     * 
     * @author <br>
     * @taskId <br>
     * @return <br>
     */
    public String getUserType() {
        return userType;
    }

    /**
     * Description: <br>
     * 
     * @author <br>
     * @taskId <br>
     * @param userType <br>
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
  /**
   * 
   * Description: <br> 
   *  
   * @author yao.yueqing<br>
   * @taskId  <br>
   * @return  String<br>
   */
    public String getLoginAccNbr() {
        return loginAccNbr;
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param loginAccNbr  <br>
     */
    public void setLoginAccNbr(String loginAccNbr) {
        this.loginAccNbr = loginAccNbr;
    }
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param request 
     * @param response 
     * @return DynamicDict 
     * @throws BaseAppException  <br>
     */
    public DynamicDict loginSuccess(HttpServletRequest request, HttpServletResponse response) throws BaseAppException {

        DynamicDict dict = MvcBoUtil.requestToBO(request);
        this.loginAccNbr = dict.getString("LOGIN_ACC_NBR");
        String loginPwd = dict.getString("LOGIN_PASSWD");
        String g_Webroot = CommonHelper.getWebRoot(request);
        logger.debug("g_Webroot:{}", g_Webroot);
        if (StringUtil.isEmpty(this.loginAccNbr) || StringUtil.isEmpty(loginPwd)) {
            String url = g_Webroot + "Login.jsp";
            dict.set("URL", url);
            dict.set("IS_SUCCESS", "false");
            dict.set("DESC", "user and password can't be null.");
            return dict;
        }
        
//        if (!StringUtil.isNumeric(this.loginAccNbr.substring(0, 1))) {
//            DynamicDict qryScUserDict = new DynamicDict();
//            qryScUserDict.set("USER_NAME", this.loginAccNbr);
//            qryScUserDict.set("STATE", "A");
//            qryScUserDict.serviceName = "QrySubsAndAfrimaxScUser";
//            ServiceFlow.callService(qryScUserDict);
//            DynamicDict scUserDict = qryScUserDict.getBO(ActionDomain.BO_FIELD_QUERY_RESULT);
//            if (null == scUserDict) {
//                String url = g_Webroot + "Login.jsp";
//                dict.set("URL", url);
//                dict.set("IS_SUCCESS", "false");
//                dict.set("DESC", "This User Name was not found.Please check and try again.");
//                return dict;
//            }
//            else {
//                this.loginAccNbr = scUserDict.getString("ACC_NBR");
//            }
//        }
        
//        //检验订户的合法性
//        DynamicDict qrySubsBo = new DynamicDict();
//        /*qrySubsBo.serviceName = "QryAndCheckSubs";
//        ServiceFlow.callService(qrySubsBo);*/
//        qrySubsBo.set("ACC_NBR", this.loginAccNbr);
//        qrySubsBo.serviceName = "QrySubsByAccNbr";
//        ServiceFlow.callService(qrySubsBo);
       DynamicDict subs = SubsServices.qrySubsByAccNbr(null, this.loginAccNbr);
       if(subs == null){
           String url = g_Webroot + "Login.jsp";
         dict.set("URL", url);
         dict.set("IS_SUCCESS", "false");
         dict.set("DESC", "This Mobile Data Number was not found.Please check and try again.");
         return dict;
       }
           //SubsHelper.qrySubsByAccNbr(null, this.loginAccNbr);
        
//        DynamicDict subsBo = qrySubsBo.getBO("z_d_r");
//        if (null == subsBo) {
//        	String url = g_Webroot + "Login.jsp";
//            dict.set("URL", url);
//            dict.set("IS_SUCCESS", "false");
//            dict.set("DESC", "This Mobile Data Number was not found.Please check and try again.");
//            return dict;
//        }
        
        DynamicDict qryCustInfo = new DynamicDict();
      //  qryCustInfo.set("CUST_ID", subs.getCustId());
        DynamicDict custBo = CustServices.qryCustById(subs.getLong("CUST_ID"));
        if (custBo == null) {
        	String url = g_Webroot + "Login.jsp";
            dict.set("URL", url);
            dict.set("IS_SUCCESS", "false");
            dict.set("DESC", "This Mobile Data Number/User Name was not found.Please check and try again.");
            return dict;
        }
        //用订户的对应的客户密码登陆
        //先在JAVA里加密
//        String md5Pwd = SecurityHelper.base64AndMD5(loginPwd);
//        if (!md5Pwd.equals(custBo.getString("PWD"))) {
//        	String url = g_Webroot + "Login.jsp";
//            dict.set("URL", url);
//            dict.set("IS_SUCCESS", "false");
//            dict.set("DESC", "Please enter correct password.");
//            return dict;
//        }
        
        // 校验是否是首次登录,需要放在密码校验后面,因为还有密码为空的情况,以及密码输入错误的情况
//        DynamicDict qryCustDict = new DynamicDict();
//        qryCustDict.set("CUST_ID", subs.getCustId());
//        qryCustDict.set("ATTR_CODE", "AFRIMAX_CUST_LOGIN_SELFCARE");
//        qryCustDict.serviceName = "QryCustDetailAttrValue";
//        ServiceFlow.callService(qryCustDict);
//        DynamicDict retDict = qryCustDict.getBO(ActionDomain.BO_FIELD_QUERY_RESULT);
//        if (null == retDict || !"Y".equals(retDict.getString("ATTR_VALUE"))) {
//            dict.set("IS_SUCCESS", "FIRST_LOGIN");
//            dict.set("CUST_ID", qryCustDict.getString("CUST_ID"));
//            return dict;
//        }
        
        //登陆成功后设置session
        HttpSession session = request.getSession();
        SessionDto sessionDto = new SessionDto();
        sessionDto.setLoginAccNbr(subs.getString("ACC_NBR"));
        sessionDto.setLoginSubsId(subs.getLong("SUBS_ID"));
        sessionDto.setAccNbr(subs.getString("ACC_NBR"));
        sessionDto.setPrefix(subs.getString("PREFIX"));
        sessionDto.setSubsId(subs.getLong("SUBS_ID"));
        sessionDto.setAcctId(subs.getLong("ACCT_I"));
        sessionDto.setCustId(custBo.getString("CUST_ID"));
        sessionDto.setCustName(custBo.getString("CUST_NAME"));
        sessionDto.setSpId(subs.getLong("SP_ID"));
       
        /*DynamicDict sessDict = null;

        String userId = null;
        Long cvbsUserId = null;
        if ("1".equals(userType)) {
            userId = sessDict.getString("STAFF_ID");
            cvbsUserId = sessDict.getLong("USER_ID");
            userCode = sessDict.getString("USER_CODE");
        }
        else {
            userId = sessDict.getString("USER_ID");
            // sessDict中的是cust.EMAIL
            userCode = sessDict.getString("EMAIL");
        }*/

        /*String roleCode = "";
        Long roleId = null;
        EscRoleDto roleDto = getLoginRole(dict, cvbsUserId, userId, g_Webroot);
        if (roleDto == null) {
        }
        else {
            roleCode = roleDto.getRoleCode();
            roleId = roleDto.getRoleId();
        }*/

        //String partyType = switchPartyType(roleCode);
        //sessionDto.setRoleId(roleId.toString());
        //sessionDto.setRoleCode(roleCode);
        //sessionDto.setPartyType(partyType);

        // 调用成功登陆后记录登陆日志的服务
        String srcIp = Common.getIpAddr(request);
        
        // 保存自服务侧的sessionId
        sessionDto.setSelfSessionId(session.getId());
        sessionDto.setLastOperTime(System.currentTimeMillis());
        sessionDto.setSrcIp(srcIp);
        //sessionDto.setLoginLogId(loginLogId);
        sessionDto.setLoginPage(CoreDef.LOGIN_PAGE_SELFSERVICE);
        //sessionDto.setUserCode(userCode);
       // sessionDto.setPartyCode(userCode);
        //sessionDto.setUserId(userId);

        sessionDto.setTitleName(custBo.getString("TITLE_NAME"));
        session.setAttribute(Common.USER_INFO_BEAN, sessionDto);
        logger.info("Login successful...");

        Cookie cookie = new Cookie("", sessionDto.getLoginPage());
        int maxAge = 365 * 24 * 3600;
        cookie.setMaxAge(maxAge); // 有效期一年
        cookie.setPath("/");
        response.addCookie(cookie);
        
        //跳到登陆成功界面
        //dict.set("URL", g_Webroot + "LoginSuccess.jsp");
        dict.set("URL", g_Webroot + "Home.jsp");
        dict.set("IS_SUCCESS", "true");
        return dict;
        
        /*if (StringUtils.isNotEmpty(dict.getString("USER_TYPE"))) {
            this.userType = dict.getString("USER_TYPE");
        }
        else {
            dict.set("USER_TYPE", this.userType);
        }
        if (StringUtils.isNotEmpty(dict.getString("EMAIL"))) {
            this.userCode = dict.getString("EMAIL");
        }
        else {
            dict.set("EMAIL", userCode);
        }
        Long spId = 0l;
        List<DynamicDict> sessList = null;
        if ("1".equals(userType)) {
            // operator员工
            dict.setServiceName("QryStaffByUserCode4Esc");
            dict.setValueByName("STAFF_STATE", "A");
            dict.setValueByName("USER_STATE", "A");
            dict.setValueByName("USER_CODE", userCode);
            ServiceFlow.callService(dict);
            sessList = MvcBoUtil.jsonArrayToList(MvcBoUtil.boSqlResultToJson(dict));
        }
        else {
            // Long userId = dict.getLong("USER_ID");
            // dict.setServiceName("QryLoginUserCustInfoByEmail");
        	DynamicDict qryEscUser = new DynamicDict();
        	qryEscUser.set("EMAIL", userCode);
        	qryEscUser.setServiceName("QryEscUser");
        	ServiceFlow.callService(qryEscUser);
        	dict.set("USER_ID", qryEscUser.getLong("z_d_r.USER_ID"));
            dict.setServiceName("QryUserCust");
            ServiceFlow.callService(dict);
            sessList = MvcBoUtil.jsonArrayToList(MvcBoUtil.boResultToJson(dict, "ESC_USER_CUST"));
        }

        

        logger.info("sessList length: " + sessList.size());
        logger.info("sessList length: " + sessList.size());
        if (sessList.isEmpty()) {

            logger.error("The user does not exist or not activate.");
            HttpSession session = request.getSession();
            EscSessionDto sessionDto = (EscSessionDto) session.getAttribute(Common.USER_INFO_BEAN);
            boolean isManager = false;
            isManager = isManager(sessionDto.getUserId());
            sessionDto.setManagerFlag(isManager);
            // 调用成功登陆后记录登陆日志的服务
            String srcIp = Common.getIpAddr(request);
            DynamicDict logDict = new DynamicDict();
            logDict.set("USER_ID", sessionDto.getUserId());
            logDict.set("SRC_IP", srcIp);
            logDict.set("SP_ID", spId);
            logDict.setServiceName("AddEscLoginLogForLogin");
            ServiceFlow.callService(logDict);
            logger.info("Success add login log for user log.");
            String loginLogId = logDict.getString("LOGIN_LOG_ID");

            // 保存自服务侧的sessionId
            sessionDto.setSelfSessionId(session.getId());
            sessionDto.setLastOperTime(System.currentTimeMillis());
            sessionDto.setSrcIp(srcIp);
            sessionDto.setLoginLogId(loginLogId);
            sessionDto.setLoginPage(EscCoreDef.LOGIN_PAGE_SELFSERVICE);
            sessionDto.setUserCode(userCode);
            sessionDto.setPartyCode("G");
            sessionDto.setSpId(spId);
            logger.info("SELF_SESSION_ID   ------------> " + session.getId());
            logger.info("USER_ID        ------------> " + sessionDto.getUserId());
            logger.info("SRC_IP         ------------> " + srcIp);
            logger.info("LOGIN_LOG_ID   ------------> " + loginLogId);
            logger.info("IS_MANAGER   ------------> " + isManager);
            logger.info("USER_CODE   ------------> " + "G");

            session.setAttribute(Common.USER_INFO_BEAN, sessionDto);
            logger.info("Login successful...");

            if ("true".equals(sessionDto.getPwdRemindExp())) {
                session.setAttribute("PWD_REMIND_EXPERIED_DESC", sessionDto.getPwdRemindExpDesc());
            }

            // USER加入监控
            DynamicDict monitorDict = new DynamicDict();
            monitorDict.set("SESSION_ID", session.getId());
            monitorDict.set("PARTY_TYPE", "G");
            monitorDict.set("USER_ID", sessionDto.getUserId());
            monitorDict.set("SRC_IP", srcIp);
            // monitorDict.serviceName = "AddUserToOnlineMonitor";
            UserOnlineConsole console = new UserOnlineConsole();
            EscUserOnlineDto dto = BoDtoTool.fromBo(EscUserOnlineDto.class, monitorDict);
            console.userAddToConsole(dto);
            // ServiceFlow.callService(monitorDict);

            Cookie cookie = new Cookie(EscActionDomain.COOKIE_USER_ROLE_CODE_NAME, sessionDto.getLoginPage());
            int maxAge = 365 * 24 * 3600;
            cookie.setMaxAge(maxAge); // 有效期一年
            cookie.setPath("/");
            response.addCookie(cookie);
            String url = g_Webroot + "setup/CustomerInformation.jsp";
            
            dict.set("URL", url);
        }
        else {
            // 获取 session 并且对其赋值
            HttpSession session = request.getSession();
            DynamicDict sessDict = sessList.get(0);

            String userId = null;
            Long cvbsUserId = null;
            if ("1".equals(userType)) {
                userId = sessDict.getString("STAFF_ID");
                cvbsUserId = sessDict.getLong("USER_ID");
                userCode = sessDict.getString("USER_CODE");
            }
            else {
                userId = sessDict.getString("USER_ID");
                // sessDict中的是cust.EMAIL
                userCode = sessDict.getString("EMAIL");
            }
            if (StringUtil.isNotEmpty(sessDict.getString("SP_ID"))) {
                spId = new Long(sessDict.getString("SP_ID"));
            }
            EscSessionDto sessionDto = (EscSessionDto) session.getAttribute(Common.USER_INFO_BEAN);
            if (sessionDto == null || !userId.equals(sessionDto.getUserId())) {
                logger.error("This guy trying to access system useing other user code. userId:{}", userId);
                String url = g_Webroot + "Login.jsp";
                dict.set("URL", url);
                return dict;
            	sessionDto = new EscSessionDto();
            	session.setAttribute(Common.USER_INFO_BEAN, sessionDto);
            }

            String roleCode = "";
            Long roleId = null;
            EscRoleDto roleDto = getLoginRole(dict, cvbsUserId, userId, g_Webroot);
            if (roleDto == null) {
            }
            else {
                roleCode = roleDto.getRoleCode();
                roleId = roleDto.getRoleId();
            }

            String partyType = switchPartyType(roleCode);
            sessionDto.setRoleId(roleId.toString());
            sessionDto.setRoleCode(roleCode);
            sessionDto.setPartyType(partyType);

            boolean isManager = false;
            if ("2".equals(userType)) {
                // 查询user是否是管理员或者管理员的DOA
                isManager = isManager(userId);
                String custId = sessDict.getString("CUST_ID");
                String hqFlag = sessDict.getString("HQ_FLAG");
                String custGroupId = sessDict.getString("CUST_GROUP_ID");
                String custName = sessDict.getString("CUST_NAME");
                sessionDto.setCustId(custId);
                sessionDto.setHqFlag(hqFlag);
                sessionDto.setCustGroupId(custGroupId);
                sessionDto.setCustName(custName);
                logger.info("CUST_ID        ------------> " + custId);
                logger.info("HQ_FLAG        ------------> " + hqFlag);
                logger.info("CUST_GROUP_ID  ------------> " + custGroupId);
            }
            sessionDto.setManagerFlag(isManager);

            // 调用成功登陆后记录登陆日志的服务
            String srcIp = Common.getIpAddr(request);
            DynamicDict logDict = new DynamicDict();
            logDict.set("USER_ID", userId);
            logDict.set("SRC_IP", srcIp);
            logDict.set("PARTY_TYPE", partyType);
            logDict.set("SP_ID", spId);
            logDict.setServiceName("AddEscLoginLogForLogin");
            ServiceFlow.callService(logDict);
            logger.info("Success add login log for user log.");
            String loginLogId = logDict.getString("LOGIN_LOG_ID");

            // 保存自服务侧的sessionId
            sessionDto.setSelfSessionId(session.getId());
            sessionDto.setLastOperTime(System.currentTimeMillis());
            sessionDto.setSrcIp(srcIp);
            sessionDto.setLoginLogId(loginLogId);
            sessionDto.setLoginPage(EscCoreDef.LOGIN_PAGE_SELFSERVICE);
            sessionDto.setUserCode(userCode);
            sessionDto.setPartyCode(userCode);
            sessionDto.setSpId(spId);
            sessionDto.setUserId(userId);
            
            logger.info("ROLE_ID  ------------> " + roleId);
            logger.info("ROLE_CODE   ------------> " + roleCode);
            logger.info("PARTY_TYPE   ------------> " + partyType);
            logger.info("SELF_SESSION_ID   ------------> " + session.getId());
            logger.info("USER_ID        ------------> " + userId);
            logger.info("SRC_IP         ------------> " + srcIp);
            logger.info("LOGIN_LOG_ID   ------------> " + loginLogId);
            logger.info("IS_MANAGER   ------------> " + isManager);
            logger.info("USER_CODE   ------------> " + userCode);

            session.setAttribute(Common.USER_INFO_BEAN, sessionDto);
            logger.info("Login successful...");

            if ("true".equals(sessionDto.getPwdRemindExp())) {
                session.setAttribute("PWD_REMIND_EXPERIED_DESC", sessionDto.getPwdRemindExpDesc());
            }

            // USER加入监控
            DynamicDict monitorDict = new DynamicDict();
            monitorDict.set("SESSION_ID", session.getId());
            monitorDict.set("PARTY_TYPE", partyType);
            monitorDict.set("USER_ID", userId);
            monitorDict.set("SRC_IP", srcIp);
            // monitorDict.serviceName = "AddUserToOnlineMonitor";
            UserOnlineConsole console = new UserOnlineConsole();
            EscUserOnlineDto dto = BoDtoTool.fromBo(EscUserOnlineDto.class, monitorDict);
            console.userAddToConsole(dto);
            // ServiceFlow.callService(monitorDict);

            Cookie cookie = new Cookie(EscActionDomain.COOKIE_USER_ROLE_CODE_NAME, sessionDto.getLoginPage());
            int maxAge = 365 * 24 * 3600;
            cookie.setMaxAge(maxAge); // 有效期一年
            cookie.setPath("/");
            response.addCookie(cookie);

            String url = g_Webroot + "LoginSuccess.jsp";
            if ("1".equals(userType)) {
                url = g_Webroot + "setup/ChoseCust.jsp";
            }
            dict.set("URL", url);

        }
        return dict;*/
    }

    /**
     * 获取登录人角色
     * 
     * @author xu.meiting<br>
     * @taskId <br>
     * @param dict <br>
     * @param cvbsUserId <br>
     * @param userId <br>
     * @param g_Webroot <br>
     * @return EscRoleDto
     * @throws BaseAppException <br>
    
    private EscRoleDto getLoginRole(DynamicDict dict, Long cvbsUserId, String userId, String g_Webroot)
        throws BaseAppException {
        String userType = dict.getString("USER_TYPE", true);
        EscRoleDto roleDto = null;
        String roleCode = "";
        Long roleId = null;
        if ("1".equals(userType)) {
            if (cvbsUserId == null) {
                logger.info("CvbsuserId is null.");
                String url = g_Webroot + "Login.jsp";
                dict.set("URL", url);
                return null;
            }

            DynamicDict staffInfo = new DynamicDict();
            staffInfo.set("USER_ID", cvbsUserId);
            staffInfo.set("APP_ID", EscActionDomain.cvbsAppId); // 使用cvbs的角色
            staffInfo.serviceName = "QryRoleListByUserId";
            HttpCall httpCall = new HttpCall();
            httpCall.callService(staffInfo);

            logger.debug("call QryRoleListByUserId end.");

            List<DynamicDict> list = staffInfo.getList("ROLE_LIST");
            if (list == null || list.isEmpty()) {
                logger.error("The user has no role. userId:{}", userId);
                String url = g_Webroot + "Error.jsp?Timeout=5";
                dict.set("URL", url);
                return null;
            }

            roleDto = checkRole(list);
        }
        else {
            DynamicDict roleDict = new DynamicDict();
            roleDict.set("USER_ID", userId);
            roleDict.setServiceName("QryUserRoleByUserId");
            ServiceFlow.callService(roleDict);
            List<DynamicDict> roleList = MvcBoUtil.jsonArrayToList(MvcBoUtil.boSqlResultToJson(roleDict));
            if (roleList != null && roleList.size() == 1) {
                roleDto = new EscRoleDto();
                roleDto.setRoleId(roleList.get(0).getLong("ROLE_ID"));
                roleDto.setRoleCode(roleList.get(0).getString("ROLE_CODE"));
            }
        }
        if (roleDto != null) {
            roleCode = roleDto.getRoleCode();
            roleId = roleDto.getRoleId();
        }
        if (StringUtil.isEmpty(roleCode) || roleId == null) {
            // 既不是SH admin，也不是AM。或者没有角色
            logger.error("The user has no role. userId:{}", userId);
            String url = g_Webroot + "Error.jsp?Timeout=5";
            dict.set("URL", url);
            return null;
        }
        return roleDto;
    } */

    /**
     * <br>
     * 
     * @author <br>
     * @taskId <br>
     * @param userId <br>
     * @return boolean
     * @throws BaseAppException <br>
     */
    private boolean isManager(String userId) throws BaseAppException {
        DynamicDict mDict = new DynamicDict();
        mDict.set("USER_ID", userId);
        mDict.setServiceName("QryIfManagerUser");
        ServiceFlow.callService(mDict);
        List<DynamicDict> userCntList = mDict.getList(ActionDomain.BO_FIELD_QUERY_RESULT);
        DynamicDict userCnt = userCntList.get(0);
        Long userCntL = userCnt.getLong("CNT");
        return userCntL.longValue() > 0;
    }

    /**
     * Description: 根据角色code往session中添加partyType<br>
     * 
     * @author XXX<br>
     * @taskId <br>
     * @param roleCode roleCode
     * @return <br>
    
    private String switchPartyType(String roleCode) {
        String partyType = "G";
        if (EscRole.OPERATOR_ADMIN.equals(roleCode) || EscRole.AM.equals(roleCode)) {
            partyType = "F";
        }
        return partyType;
    } */

    /**
     * 返回营业员角色
     * 
     * @author xu.meiting<br>
     * @taskId <br>
     * @param list List
     * @return Long
     * @throws BaseAppException <br>
     
    private EscRoleDto checkRole(List<DynamicDict> list) throws BaseAppException {
        EscRoleDto retDto = null;
        String roleCode = "";
        Long roleId = null;
        // 判断是否是运营商 admin
        EscRoleDto dto = CacheHelper.getEscRole(EscRole.OPERATOR_ADMIN);
        if (dto != null) {
            String shRoleCode = dto.getShRoleCode();
            if (StringUtil.isNotEmpty(shRoleCode)) {
                String roleStr = "," + shRoleCode + ",";
                Iterator<DynamicDict> iterator = list.iterator();
                while (iterator.hasNext()) {
                    DynamicDict roleDict = (DynamicDict) iterator.next();
                    String role = "," + roleDict.getString("ROLE_CODE") + ",";
                    if (roleStr.indexOf(role) > -1) {
                        roleCode = EscRole.OPERATOR_ADMIN;
                        roleId = dto.getRoleId();
                        retDto = dto;
                        break;
                    }
                }
            }
        }

        if (StringUtil.isEmpty(roleCode) || roleId == null) {
            // 判断是否是AM
            dto = CacheHelper.getEscRole(EscRole.AM);
            if (dto != null) {
                String shRoleCode = dto.getShRoleCode();
                if (StringUtil.isNotEmpty(shRoleCode)) {
                    String roleStr = "," + shRoleCode + ",";
                    Iterator<DynamicDict> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        DynamicDict roleDict = (DynamicDict) iterator.next();
                        String role = "," + roleDict.getString("ROLE_CODE") + ",";
                        if (roleStr.indexOf(role) > -1) {
                            roleCode = EscRole.AM;
                            retDto = dto;
                            break;
                        }
                    }
                }
            }
        }
        logger.debug("roleCode is {}, roleId is {}", roleCode, roleId);
        return retDto;
    }
*/
    /**
     * Description: 需要选择角色后跳转到主界面<br>
     * 
     * @author qin.bin<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    public String selectRole(HttpServletRequest request) throws BaseAppException {
        LoginMgr loginMgr = new LoginMgr();
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        dict.setServiceName("QryRoleInfoByRoleId");
        ServiceFlow.callService(dict);
        List<DynamicDict> roleList = MvcBoUtil.jsonArrayToList(MvcBoUtil.boSqlResultToJson(dict));

        DynamicDict roleDict = new DynamicDict();
        roleDict = roleList.get(0);
        String roleId = roleDict.getString("ROLE_ID");
        String roleCode = roleDict.getString("ROLE_CODE");

       // String partyType = loginMgr.switchPartyType(roleCode);
        HttpSession session = request.getSession();

        SessionDto sessionDto = null;
        if (session.getAttribute(Common.USER_INFO_BEAN) != null) {
            sessionDto = (SessionDto) session.getAttribute(Common.USER_INFO_BEAN);
        }
        else {
            sessionDto = new SessionDto();
            // 似乎没有地方用到，暂时不加SP_ID
        }
        sessionDto.setRoleId(roleId);
        sessionDto.setRoleCode(roleCode);
        //sessionDto.setPartyType(partyType);

        logger.info("ROLE_ID  ------------> [{}]", roleId);
        logger.info("ROLE_CODE   ------------> [{}]", roleCode);
        //logger.info("PARTY_TYPE   ------------> " + partyType);

        return "redirect:LoginSuccess.jsp";
    }
    
}
