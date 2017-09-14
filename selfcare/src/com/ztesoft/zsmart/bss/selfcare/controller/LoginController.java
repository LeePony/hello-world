package com.ztesoft.zsmart.bss.selfcare.controller;

/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/


/**
 * 
 */
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;








import com.ztesoft.zsmart.bss.selfcare.common.util.MvcBoUtil;
import com.ztesoft.zsmart.bss.selfcare.model.SessionDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.ActionDomain;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.web.resource.Common;

/**
 * <Description> <br>
 * 
 * @author hu.jianfei<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2013-1-15 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.self.mvc <br>
 */
@Controller
public class LoginController {
    /**
     * logger <br>
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(LoginController.class);
    

    /**
     * 校验登陆用户来源
     * 
     * @author xu.meiting<br>
     * @taskId <br>
     * @param request HttpServletRequest
     * @return JSONObject
     * @throws BaseAppException <br>
     */
    @RequestMapping("CheckUserSrc")
    @ResponseBody
    public JSONObject checkUserSrc(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        DynamicDict loginUserDict = new DynamicDict();
        loginUserDict.serviceName = "CheckUserSrc";
        loginUserDict.set("USER_CODE", dict.getString("USER_CODE"));
        ServiceFlow.callService(loginUserDict);
        String str = loginUserDict.getString("RETURN_VALUE");
        if (StringUtil.isEmpty(str)) {
            logger.error("sso value is empty...");
            dict.set("IS_SUCCESS", "false");
            dict.set("DESC", "Check user source fail, please contact the administrator.");
        }
        else {
            DynamicDict ssoDict = MvcBoUtil.jsonStringToBo(str);
            if ("true".equals(ssoDict.getString("is_success"))) {
                dict.set("DESC", ssoDict.getString("description"));
                dict.set("IS_SUCCESS", "true");
                // SSO返回登陆成功 获取 ONCE
                String once = ssoDict.getString("once");
                dict.set("ONCE", once);
            }
            else {
                dict.set("IS_SUCCESS", "false");
                dict.set("DESC", ssoDict.getString("description"));
            }
        }
//        String m2mUrl =  BusiConfigurationFactory.instance().getString("CUSTOMER_CARE.MTNYL_PUBLIC.MTNYL_EC_URL") ;
//        String accessToken = getAccessToken(dict.getString("USER_CODE"), dict.getString("LOGIN_PASSWD"));
//        if(StringUtil.isNotEmpty(accessToken)) {
//            dict.set("M2M_URL", m2mUrl + "?ACCESS_TOKEN=" + accessToken);
//        }
//        else {
//            dict.set("M2M_URL", m2mUrl );
//        }
        
        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }
    
    /**
     * 
     * Description: 获取令牌<br> 
     *  
     * @author XXX<br>
     * @taskId <br>
     * @param userName <br> 
     * @param passwd <br> 
     * @return <br> 
     * @throws BaseAppException <br>
    
    private  String getAccessToken(String userName, String passwd) throws BaseAppException {      
        StringBuffer reqUrl = new StringBuffer();
        String accessToken = null;

        String oauth2Url = CacheHelper.getConfigItemParam("ESC_PUBLIC", "ESC_OAUTH2_SERVICE_URL");

        reqUrl.append(oauth2Url).append("/oauth/2.0/token");   
        
        OAuthClientRequest request = new OAuthClientRequest(reqUrl.toString());
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(OAuthDef.HeaderType.CONTENT_TYPE, OAuthDef.ContentType.URL_ENCODED);
        String requestMethod = "POST";
        request.setBody("grant_type=" + OAuthDef.OAUTH_PASSWORD + "&username=" + userName+ "&password="
            + passwd + "&client_id=" + OAuthDef.REQUEST_OAUTH_CLIENT_ID + "&client_secret="
            + OAuthDef.REQUEST_OAUTH_CLIENT_SECRET);

        URLConnection c = null;
        int responseCode = 0;
        try {
            URL url = new URL(request.getLocationUri());

            c = url.openConnection();
            responseCode = -1;
            if (c instanceof HttpURLConnection) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) c;
                httpURLConnection.setConnectTimeout(30000);
                httpURLConnection.setReadTimeout(30000);

                if (headers != null && !headers.isEmpty()) {
                    for (Map.Entry<String, String> header : headers.entrySet()) {
                        httpURLConnection.addRequestProperty(header.getKey(), header.getValue());
                    }
                }

                httpURLConnection.setRequestMethod(requestMethod);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);

                httpURLConnection.connect();
                DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
                out.writeBytes(request.getBody());
                out.flush();
                out.close();



                InputStream inputStream;
                responseCode = httpURLConnection.getResponseCode();
                if (responseCode == 400) {
                    inputStream = httpURLConnection.getErrorStream();
                }
                else {
                    inputStream = httpURLConnection.getInputStream();
                }
                String responseBody = OAuthUtils.saveStreamAsString(inputStream);
                logger.debug("responseBody ==" + responseBody);

                OAuthClientResponse oAuthResponse = OAuthClientResponseFactory.createUserInfoResponse(responseBody,
                    c.getContentType());
                inputStream.close();
                httpURLConnection.disconnect();

                accessToken =  oAuthResponse.getParam("access_token");
            }
        }
        catch (IOException e) {
            logger.error("visit oauth2 service fail: " + reqUrl.toString());
            logger.error("", e);
            return null;
        }
        logger.debug("the service is over");
        return accessToken;
    }
 */
    /**
     * Description: <br>
     * 
     * @author hu.jianfei<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("LoginUser")
    @ResponseBody
    public JSONObject loginUser(HttpServletRequest request) throws BaseAppException {

        String checkCode = "";
        Object checkCodeObj = request.getSession().getAttribute("randCheckCode");

        if (checkCodeObj != null) {
            checkCode = checkCodeObj.toString();
        }

        DynamicDict dict = MvcBoUtil.requestToBO(request);
        HttpSession session = request.getSession();
        // 360977 配置项开关打开，始终不需要验证码
        String noNeedVerifyCode = "N";
        if (StringUtil.isEmpty(noNeedVerifyCode)) {
            // 无此配置项时, 默认为N, 此时需要校验验证码
            noNeedVerifyCode = "N";
        }
        dict.set("NO_NEED_VERIFY_CODE", noNeedVerifyCode);
        logger.info("ESC_NO_NEED_VERIFY_CODE: [{}]", noNeedVerifyCode);

        if ("Y".equalsIgnoreCase(noNeedVerifyCode)) {

            dict.set("LOGIN_FAIL_COUNT", "");
        }

        if (!StringUtil.isEmpty(dict.getString("LOGIN_FAIL_COUNT"))
            && !checkCode.equalsIgnoreCase(dict.getString("INPUT_CODE"))) {
            // 需要校验图形验证码，并且验证码不匹配
            logger.debug("check code does not matches.");
            dict.set("IS_SUCCESS", "false");
            dict.set("DESC", "Your entry does not match the displayed characters. Please try again.");
        }
        else { // 不需要校验图形验证码 或者 验证码匹配的情形
               // 校验用户
            String userType = dict.getString("USER_TYPE");
            String userId = null;
            String userCode = dict.getString("EMAIL");
            Long spId = 0l;
            if ("1".equals(userType)) {
                // operator用户
                DynamicDict staffDict = new DynamicDict();
                staffDict.setServiceName("QryStaffByUserCode4Esc");
                staffDict.setValueByName("STAFF_STATE", "A");
                staffDict.setValueByName("USER_STATE", "A");
                staffDict.setValueByName("USER_CODE", userCode);
                ServiceFlow.callService(staffDict); // 查询staff

                logger.debug("call QryStaffByUserCode4Esc end.");

                List<DynamicDict> staff = staffDict.getList(ActionDomain.BO_FIELD_QUERY_RESULT);
                if (!checkUserValid(staff, dict)) {
                    return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
                }
                DynamicDict staffBo = staff.get(0);
                userId = staffBo.getString("STAFF_ID");
                if (StringUtil.isNotEmpty(staffBo.getString("SP_ID"))) {
                    spId = new Long(staffBo.getString("SP_ID"));
                }
            }
            else {
                DynamicDict loginUserDict = new DynamicDict();
                loginUserDict.serviceName = "QryActivateUserForLoginByEmail";
                loginUserDict.set("EMAIL", userCode);
                loginUserDict.set("STATE", "A");
                loginUserDict.set("LOGIN_STATE", "X");
                ServiceFlow.callService(loginUserDict);

                List<DynamicDict> userList = loginUserDict.getList(ActionDomain.BO_FIELD_QUERY_RESULT);
                if (!checkUserValid(userList, dict)) {
                    return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
                }
                DynamicDict userBo = userList.get(0);
                userId = userBo.getString("USER_ID");
                if (StringUtil.isNotEmpty(userBo.getString("SP_ID"))) {
                    spId = new Long(userBo.getString("SP_ID"));
                }
            }

            dict.set("SRC_IP", Common.getIpAddr(request));
            //loginViaSso(dict, session, userId, spId);
        }

        return MvcBoUtil.boToJson(dict);
    }

    /**
     * 是否有可登陆用户
     * 
     * @author xu.meiting<br>
     * @taskId <br>
     * @param userList <br>
     * @param dict <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    private boolean checkUserValid(List<DynamicDict> userList, DynamicDict dict) throws BaseAppException {
        if (userList == null || userList.isEmpty()) {
            dict.set("IS_SUCCESS", "false");
            dict.set("DESC", "The username or password is not valid. Please try again.");
            dict.set("LOGIN_FAIL_COUNT", "1");
            return false;
        }
        return true;
    }

    /**
     * 调用登陆接口
     * 
     * @author xu.meiting<br>
     * @taskId <br>
     * @param dict <br>
     * @param session <br>
     * @param userId <br>
     * @param spId <br>
     * @throws BaseAppException <br>
     
    private void loginViaSso(DynamicDict dict, HttpSession session, String userId, Long spId) throws BaseAppException {
        DynamicDict loginDict = new DynamicDict();
        // 读取配置项,获取APP_ID的值
        String appId = CacheHelper.getConfigItemParam("ESC_PUBLIC", "ESC_SSO_APPID");

        logger.debug("APP_ID: " + appId);
        loginDict.set("SSO_APP_ID", appId);
        loginDict.set("USER_CODE", dict.getString("EMAIL"));
        loginDict.set("PWD", dict.getString("PASSWORD"));
        loginDict.set("SRC_IP", dict.getString("SRC_IP"));

        loginDict.setServiceName("LoginNeedVerifyCode");
        logger.info("Begin login user via sso.");
        ServiceFlow.callService(loginDict);
        dict.set("SSO_VALUE", loginDict.getString("RETURN_VALUE"));

        String str = dict.getString("SSO_VALUE");
        if (StringUtil.isEmpty(str)) {
            logger.error("sso value is empty...");
            dict.set("IS_SUCCESS", "false");
            dict.set("DESC", "Login fail, please contact the administrator.");
        }
        else {
            DynamicDict ssoDict = MvcBoUtil.jsonStringToBo(str);
            if ("true".equals(ssoDict.getString("is_success"))) {
                // SSO返回登陆成功 获取 ONE_TIME_PWD 和 IS_TODAY_FIRST_TIME
                String isTodayFirstTime = ssoDict.getString("IS_TODAY_FIRST_TIME");
                if (StringUtil.isEmpty(isTodayFirstTime)) {
                    isTodayFirstTime = "false";
                }

                dict.set("ONE_TIME_PWD", ssoDict.getString("ONE_TIME_PWD"));
                dict.set("IS_SUCCESS", "true");
                dict.set("IS_TODAY_FIRST_TIME", isTodayFirstTime);
                if ("false".equals(isTodayFirstTime)) {
                    String ssoUserId = ssoDict.getString("userId");
                    String sessionId = ssoDict.getString("sessionId");
                    SessionDto sessionDto = new SessionDto();
                    sessionDto.setSsoUserId(ssoUserId);
                    sessionDto.setSessionId(sessionId);
                    String userType = dict.getString("USER_TYPE");
                    if ("2".equals(userType)) {
                        // esc员工
                        String pwdRemindExp = ssoDict.getString("PWD_REMIND_EXPERIED");
                        String pwdRemindExpDesc = ssoDict.getString("PWD_REMIND_EXPERIED_DESC");
                        sessionDto.setPwdRemindExp(pwdRemindExp);
                        sessionDto.setPwdRemindExpDesc(pwdRemindExpDesc);
                    }
                    // 登陆成功，并且不是当天第一次登陆，记录SSO返回的UserId和SessionId，或者配置项已配置不需要校验验证码 360977
                    sessionDto.setUserId(userId);
                    sessionDto.setSpId(spId);
                    session.setAttribute(Common.USER_INFO_BEAN, sessionDto);

                    logger.info("SSO_USER_ID    ------------> " + ssoUserId);
                    logger.info("SESSION_ID     ------------> " + sessionId);
                }
                else {

                    // 登陆成功，并且是当天第一次登陆，发送每天第一次登陆校验码的邮件
                    DynamicDict sendParam = new DynamicDict();
                    sendParam.set("MAIL_TO", dict.getString("EMAIL"));
                    sendParam.set("MAIL_SUBJECT", "This is your one time password.");

                    DynamicDict sendDict = new DynamicDict();
                    logger.info("Begin send one time password email.");

                    sendDict.set("ONE_TIME_PWD", ssoDict.getString("ONE_TIME_PWD"));
                    sendDict.set("SUBS_ID", -1); // 暂时定为 -1
                    sendDict.set("STD_CODE", "ESC_ONE_TIME_PWD_EMAIL");
                    sendDict.set("SENDER_PARAM", sendParam);
                    // 发邮件需要传SP_ID
                    sendDict.set("SP_ID", spId);
                    sendDict.setServiceName("CcAddAdvice");
                    // 发送审核邮件内容
                    logger.info("Begin send one time password email.");
                    ServiceFlow.callService(sendDict);
                }
            }
            else { // SSO返回失败 记录 LOGIN_FAIL_COUNT
                String loginFailCount = ssoDict.getString("LOGIN_FAIL_COUNT");
                logger.info("LOGIN FAIL COUNT: " + loginFailCount);
                if (StringUtil.isEmpty(loginFailCount)) {
                    // operator员工登陆失败的话，不返回错误次数，但前台需要根据这个标志判断是否需要图形验证码，所以赋个值
                    loginFailCount = "1";
                }
                dict.set("DESC", ssoDict.getString("description"));
                dict.set("LOGIN_FAIL_COUNT", loginFailCount);
                dict.set("IS_SUCCESS", "false");
                dict.set("PWD_EXPERIRED", ssoDict.getString("PWD_EXPERIRED"));
                dict.set("SSO_USER_ID", ssoDict.getString("userId"));
                dict.set("FORCE_CHANGE_PWD", ssoDict.getString("FORCE_CHANGE_PWD"));
            }
        }
    }*/

    /**
     * Description: cognos登出 <br>
     * 
     * @author hu.jianfei<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     
    @RequestMapping("logoutCogs")
    @ResponseBody
    public JSONObject logoutCogs(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        String logoutUrlString = CacheHelper.getConfigItemParam("SC_PUBLIC_NODE", "COGNOS_LOGOUT");
        if (request.isSecure()) {
            String sllUrlReplaceString = CacheHelper.getConfigItemParam("SC_PUBLIC_NODE", "REPORT_URL_REPLACE_SSL");
            logoutUrlString = logoutUrlString.replace(sllUrlReplaceString.split("\\|")[0],
                sllUrlReplaceString.split("\\|")[1]);
        }
        dict.set("LOGOUTCOGS", logoutUrlString);
        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }*/

    /**
     * Description: 校验每日首次登陆的验证码 <br>
     * 
     * @author hu.jianfei<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("CheckVerifyCode")
    @ResponseBody
    public JSONObject checkVerifyCode(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        HttpSession session = request.getSession();
        Long spId = 0l;
        DynamicDict loginUserDict = new DynamicDict();
        loginUserDict.serviceName = "QryActivateUserForLoginByEmail";
        loginUserDict.set("EMAIL", dict.getString("EMAIL"));
        loginUserDict.set("STATE", "A");
        loginUserDict.set("LOGIN_STATE", "X");
        ServiceFlow.callService(loginUserDict);

        List<DynamicDict> userList = loginUserDict.getList(ActionDomain.BO_FIELD_QUERY_RESULT);
        if (userList == null || userList.isEmpty()) {
            dict.set("IS_SUCCESS", "false");
            dict.set("DESC", "The username  is not valid. Please try again.");
            return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
        }
        else {
            DynamicDict userDict = userList.get(0);
            if (userDict != null) {
                spId = userDict.getLong("SP_ID");
            }
        }

        String appId = "1";

        DynamicDict loginDict = new DynamicDict();

        loginDict.set("SSO_APP_ID", appId);
        loginDict.set("USER_CODE", dict.getString("EMAIL"));
        loginDict.set("PWD", dict.getString("PASSWORD"));
        loginDict.set("SRC_IP", Common.getIpAddr(request));

        loginDict.setServiceName("CheckVerifyCode");
        logger.info("Begin check verify code via sso.");
        ServiceFlow.callService(loginDict);
        dict.set("SSO_VALUE", loginDict.getString("RETURN_VALUE"));

        String str = dict.getString("SSO_VALUE");
        if (StringUtil.isEmpty(str)) {
            logger.error("sso value is empty...");
            dict.set("IS_SUCCESS", "false");
            dict.set("DESC", "Check one time password fail.");
        }
        else {
            DynamicDict ssoDict = MvcBoUtil.jsonStringToBo(str);
            if ("true".equals(ssoDict.getString("is_success"))) {
                dict.set("DESC", ssoDict.getString("description"));
                dict.set("IS_SUCCESS", "true");

                String ssoUserId = ssoDict.getString("userId");
                String sessionId = ssoDict.getString("sessionId");
                String pwdRemindExp = ssoDict.getString("PWD_REMIND_EXPERIED");
                String pwdRemindExpDesc = ssoDict.getString("PWD_REMIND_EXPERIED_DESC");
                SessionDto sessionDto = null;
                if (session.getAttribute(Common.USER_INFO_BEAN) != null) {
                    sessionDto = (SessionDto) session.getAttribute(Common.USER_INFO_BEAN);
                }
                else {
                    sessionDto = new SessionDto();
                }
                sessionDto.setSsoUserId(ssoUserId);
                sessionDto.setSessionId(sessionId);
                sessionDto.setPwdRemindExp(pwdRemindExp);
                sessionDto.setPwdRemindExpDesc(pwdRemindExpDesc);
                sessionDto.setUserId(userList.get(0).getString("USER_ID"));
                sessionDto.setSpId(spId);
                
                session.setAttribute(Common.USER_INFO_BEAN, sessionDto);

                logger.info("SSO_USER_ID    ------------> [{}]", ssoUserId);
                logger.info("SESSION_ID     ------------> [{}]", sessionId);
            }
            else {
                dict.set("DESC", ssoDict.getString("description"));
                dict.set("IS_SUCCESS", "false");
            }
        }

        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }


    
}
