package com.ztesoft.zsmart.bss.selfcare.bll;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;

import com.itextpdf.text.pdf.codec.Base64;

import net.sf.json.JSONObject;

import com.ztesoft.zsmart.bss.common.helper.SecurityHelper;
import com.ztesoft.zsmart.bss.selfcare.common.core.api.CoreDef;
import com.ztesoft.zsmart.bss.selfcare.common.core.api.JdbcUtil4CC;
import com.ztesoft.zsmart.bss.selfcare.common.helper.CommonHelper;
import com.ztesoft.zsmart.bss.selfcare.common.util.MvcBoUtil;
import com.ztesoft.zsmart.bss.selfcare.common.util.RandomUtil;
import com.ztesoft.zsmart.bss.selfcare.dao.ISystemDAO;
import com.ztesoft.zsmart.bss.selfcare.model.LoginSessionDto;
import com.ztesoft.zsmart.bss.selfcare.model.MenuDto;
import com.ztesoft.zsmart.bss.selfcare.model.ScLoginHis;
import com.ztesoft.zsmart.bss.selfcare.model.SessionDto;
import com.ztesoft.zsmart.bss.selfcare.service.CommonServices;
import com.ztesoft.zsmart.bss.selfcare.service.CustServices;
import com.ztesoft.zsmart.bss.selfcare.service.SubsServices;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.jdbc.DAOFactory;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.DateUtil;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.mvc.core.helper.BoUtil;
import com.ztesoft.zsmart.web.resource.Common;



/**
 * <Description> <br>
 * 
 * @author yao.yueqing<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月7日 <br>
 * @since CRM8.1 <br>
 * @see com.ztesoft.zsmart.bss.selfcare.bll <br>
 */
@Repository
public class SystemManager {

    /**
     * dao
     */
    private ISystemDAO dao;

    /**
     * logger
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(SystemManager.class);

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
            dao = (ISystemDAO) DAOFactory.createModuleDAO("System", "selfcare", JdbcUtil4CC.getDbBackService());
        }
        return dao;
    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param paidFlag <br>
     * @return List<DynamicDict>
     * @throws BaseAppException <br>
     */
    public List<MenuDto> qryAllScMenu(String paidFlag) throws BaseAppException {
        return getSystemDao().qryScMenu(paidFlag);
    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param dict 
     * @return String 
     * @throws BaseAppException <br>
     */
    public String sendMsgCode(DynamicDict dict) throws BaseAppException {
        String accNbr = dict.getString("ACC_NBR");
        String msgCode = "";
        // 先校验号码
        if (StringUtil.isNotEmpty(accNbr) && accNbr.startsWith("0")) {
            accNbr = accNbr.substring(1, accNbr.length());
        }
        String valCode = RandomUtil.getRandomString(6);
        msgCode = dict.getString("ACC_NBR") + valCode;
        DynamicDict adviceDict = new DynamicDict();
        logger.debug("msgCode :{}", valCode);
        adviceDict.set("ACC_NBR", accNbr);
        adviceDict.set("STD_CODE", "SC_SEND_VER_MSG");
        adviceDict.set("MSG_TYPE", dict.getString("MSG_TYPE"));
        adviceDict.set("VER_MSG", valCode);


        CommonServices.adviceService(adviceDict);

        return msgCode;

    }

   /**
    * 
    * Description: <br> 
    *  
    * @author yao.yueqing<br>
    * @taskId  <br>
    * @param request 
    * @param response 
    * @param method 
    * @return DynamicDict
    * @throws BaseAppException  <br>
    */
    public JSONObject login(HttpServletRequest request, HttpServletResponse response, String method)
        throws BaseAppException {

        DynamicDict dict = MvcBoUtil.requestToBO(request);
        JSONObject result = new JSONObject();
        String accNbr = dict.getString("ACC_NBR");
        if (StringUtil.isNotEmpty(accNbr) && accNbr.startsWith("0")) {
            accNbr = accNbr.substring(1, accNbr.length());
        }
        String loginAccNbr = accNbr;
       
        String loginPwd = dict.getString("LOGIN_PASSWD");
        String g_Webroot = CommonHelper.getWebRoot(request);
        logger.debug("g_Webroot:{}", g_Webroot);
        if (StringUtil.isEmpty(loginAccNbr)) {
            String url = g_Webroot + "login.html";
            result.put("URL", url);
            result.put("IS_SUCCESS", false);
            result.put("DESC", "The mobile number can't be null.");
            return result;
        }
        DynamicDict subs = SubsServices.qrySubsByAccNbr(null, loginAccNbr);
        if (subs == null) {
            ExceptionHandler.publish("SC_REG_SUBS_NOT_EXITS", ExceptionHandler.BUSS_ERROR);
        }

        DynamicDict cust = CustServices.qryCustById(subs.getLong("CUST_ID"));
        
        if (!"A".equals(cust.getString("CUST_TYPE"))) { //企业客户不能登录
            ExceptionHandler.publish("SC_B_CUSTCANNOTLOGIN", ExceptionHandler.BUSS_ERROR);
        }

        DynamicDict custBo = qryScUser(subs.getLong("CUST_ID"));
        if (custBo == null && "PWD".equals(method)) {
            ExceptionHandler.publish("SC_USER_NOT_REGIST", ExceptionHandler.BUSS_ERROR);
        }

        // 用订户的对应的客户密码登陆
        // 先在JAVA里加密
        String md5Pwd = "";
        //用于判断密码是否为记住密码的密码
        String isCookiePwd = dict.getString("IS_COOKIE_PWD");
        if (StringUtil.isNotEmpty(loginPwd)) {
            if (StringUtil.isEmpty(isCookiePwd) || !"TRUE".equals(isCookiePwd)) {
                md5Pwd = SecurityHelper.dESEncrypt(loginPwd); 
            }
            else {
                md5Pwd =  loginPwd;
            }
        }
        //1008893 session校验逻辑优化
        //loginSessionLockAndFailed, loginSessionMgr中各自查询seesion信息修改为统一查询
        List<DynamicDict> listLogin = new ArrayList<DynamicDict>();
        listLogin = qryLoginSession(subs.getLong("SUBS_ID"));
        if ("PWD".equals(method)) {
            LoginSessionDto loginSessionDto = loginSessionLockAndFailed(listLogin, subs.getLong("SUBS_ID"), null);
            if ("Y".equals(loginSessionDto.getIsLock())) {
                // ExceptionHandler.publish("SC_ACCOUNT_LOCKED_HALFHOUR", "The Account is locked, please try again after 30 minutes.");
                result.put("RESTULT_CODE", 2);
                result.put("IS_SUCCESS", true);
                return result;
            }
        }
        if ("PWD".equals(method) && !md5Pwd.equals(cust.getString("PWD"))) {
            // ExceptionHandler.publish("SC_PASSWORD_NOT_CORRECT", "The password you have entered is not correct.");
            result.put("RESTULT_CODE", 1);
            result.put("IS_SUCCESS", true);
            return result;
        }

        // 登陆成功后设置session
        HttpSession session = request.getSession();
        session.invalidate();
        session = request.getSession(true);
        SessionDto sessionDto = new SessionDto();
        sessionDto.setLoginAccNbr(subs.getString("ACC_NBR"));
        sessionDto.setLoginSubsId(subs.getLong("SUBS_ID"));
        sessionDto.setAccNbr(subs.getString("ACC_NBR"));
        sessionDto.setPrefix(subs.getString("PREFIX"));
        sessionDto.setSubsId(subs.getLong("SUBS_ID"));
        sessionDto.setAcctId(subs.getLong("ACCT_ID"));
        sessionDto.setCustId(cust.getString("CUST_ID"));
        sessionDto.setCustName(cust.getString("CUST_NAME"));
        sessionDto.setCustCode(cust.getString("CUST_CODE"));
        
        // 调用成功登陆后记录登陆日志的服务
        String srcIp = Common.getIpAddr(request);

        // 保存自服务侧的sessionId
        sessionDto.setSelfSessionId(session.getId());
        sessionDto.setLastOperTime(System.currentTimeMillis());
        sessionDto.setSrcIp(srcIp);
        // sessionDto.setLoginLogId(loginLogId);
        sessionDto.setLoginPage(CoreDef.LOGIN_PAGE_SELFSERVICE);
        sessionDto.setUserCode(cust.getString("USER_CODE"));
        sessionDto.setPartyCode(cust.getString("PARTY_CODE"));
        sessionDto.setUserId(cust.getString("USER_ID"));

        sessionDto.setTitleName(cust.getString("TITLE_NAME"));
        sessionDto.setSpId(cust.getLong("SP_ID"));
        ScLoginHis dto = new ScLoginHis();
        dto.setLoginIp(srcIp);
        dto.setCustId(cust.getLong("CUST_ID"));
        dto.setLoginDate(DateUtil.GetDBDateTime());
        dto.setSubsId(subs.getLong("SUBS_ID"));
        dto.setSpId(subs.getLong("SP_ID"));
        getSystemDao().insertScLoginHis(dto);
        
        sessionDto.setSessionId(session.getId());
        
        if ("SMS".equals(method)) { // PWD密码验证时，前面已经插入了数据，不能重复插入数据
            loginSessionMgr(listLogin, subs.getLong("SUBS_ID"), session.getId());
        }

        subs.set("LOG_ID", dto.getLogId());
        subs.set("CUST_NAME", cust.getString("CUST_NAME"));
        subs.set("CUST_CODE", cust.getString("CUST_CODE"));
        sessionDto.setLoginLogId(String.valueOf(dto.getLogId()));
        session.setAttribute(Common.USER_INFO_BEAN, sessionDto);
        logger.info("Login successful...");
        DynamicDict qryDict = new DynamicDict();
        qryDict.set("QRY_ACCT", true);
        DynamicDict subsCustBo = SubsServices.qrySubsDetail(subs.getLong("SUBS_ID"), qryDict);
        if (subsCustBo != null && subsCustBo.getBO("SUBS.ACCT_EX") != null) {
            DynamicDict acct = subsCustBo.getBO("SUBS.ACCT_EX");
            sessionDto.setPaidFlag("N".equals(acct.getString("POSTPAID")) ? "1" : "2");
            sessionDto.setAcctCode(acct.getString("ACCT_NBR"));
            subs.set("ACCT_CODE", acct.getString("ACCT_NBR"));
        }
        
       /* Cookie cookie = new Cookie("", sessionDto.getLoginPage());
        int maxAge = 365 * 24 * 3600;
        cookie.setMaxAge(maxAge); // 有效期一年
        cookie.setPath("/");
        response.addCookie(cookie);*/

        // 跳到登陆成功界面
        // dict.set("URL", g_Webroot + "LoginSuccess.jsp");
        
        result.put("URL", g_Webroot +  "index.html");
        result.put("IS_SUCCESS", true);
        result.put("SESSION", BoUtil.boToJson(subs));
        return result;
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param request <br>
     * @throws BaseAppException  <br>
     */
    public void logout(HttpServletRequest request) throws BaseAppException {
       // 记录退出时间
        Object o = request.getSession().getAttribute(Common.USER_INFO_BEAN);
        if (o != null) {
            SessionDto sessionDto = (SessionDto) o;
            ScLoginHis dto = new ScLoginHis();
            dto.setLogId(Long.valueOf(sessionDto.getLoginLogId()));
            dto.setLogoutDate(new java.sql.Date(System.currentTimeMillis()));
            getSystemDao().updateScLoginHis(dto);
            // logout 删除session
            if (sessionDto.getSessionId() != null) {
                getSystemDao().deleteLoginSession(sessionDto.getSessionId());
            }
            request.getSession().removeAttribute(Common.USER_INFO_BEAN);
            request.getSession().removeAttribute("CAN_QRY_CDR");
        }
        
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param dict 
     * @throws BaseAppException  <br>
     */
    public void saveScUserData(DynamicDict dict) throws BaseAppException {
        getSystemDao().insertScUser(dict);
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param custId 
     * @return DynamicDict
     * @throws BaseAppException  <br>
     */
    public DynamicDict qryScUser(Long custId) throws BaseAppException {

        return getSystemDao().qryScUserByCustId(custId);
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param dict <br>
     * @return List<ScLoginHis>
     * @throws BaseAppException  <br>
     */
    public List<DynamicDict> qryLoginHistory(DynamicDict dict) throws BaseAppException {
        Date startDate = null;
        Date endDate = null;
        if (dict.getLong("MONTHS") != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(System.currentTimeMillis()));
            calendar.add(Calendar.MONTH, dict.getLong("MONTHS").intValue());
            startDate = calendar.getTime();
            
        }
        String accNbr = dict.getString("ACC_NBR");
        if (dict.getDate("START_DATE") != null) {
            startDate = dict.getDate("START_DATE");
        }
        if (dict.getDate("END_DATE") != null) {
            endDate = dict.getDate("END_DATE");
            java.sql.Date endDatebak = DateUtil.dateToSqlDate(endDate);
            endDate = new java.util.Date(DateUtil.offsetDay(endDatebak, 1).getTime());
        }
        
        return getSystemDao().qryLoginHistory(dict.getString("ACC_NBR"), dict.getLong("SUBS_ID"), startDate, endDate);
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param path <br>
     * @return List<DynamicDict> 
     * @throws BaseAppException  <br>
     */
    public List<DynamicDict> qryScAthenaImage(String path) throws BaseAppException {
        List<DynamicDict> l = getSystemDao().qyrScAdImage();
        logger.debug("path : {}", path);
        if (l != null && !l.isEmpty()) {
            for (DynamicDict dict : l) {
                StringBuffer sb = new StringBuffer();
                sb.append(path).append("selfcare_ad_image/");
                writeImg(sb.toString(), dict.getString("IMAGE_NAME"), dict.getString("IMAGE"));
                dict.set("URL", "selfcare_ad_image/" + dict.getString("IMAGE_NAME"));
                dict.remove("IMAGE");
            }
        }
        return l;
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @return List <br>
     * @throws BaseAppException  <br>
     */
    public List<DynamicDict> qryAthenaAcctRes() throws BaseAppException {
        return getSystemDao().qryAthenaAcctRes();
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @return List  <br>
     * @throws BaseAppException  <br>
     */
    public List<DynamicDict> qryAthenaAcctItemBiz()throws BaseAppException {
        return getSystemDao().qryAthenaAcctItemBiz();
    }
    

    /**
     * Description: <br> 
     *  
     * @author wang.yongs<br>
     * @taskId 1008893<br>
     * @param subsId Long
     * @return List<DynamicDict>
     * @throws BaseAppException <br>
     */ 
    public List<DynamicDict> qryLoginSession(Long subsId)throws BaseAppException {
        return getSystemDao().qryLoginSession(subsId, null);
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param fileName <br>
     * @param path <br>
     * @param image <br>
     * @throws BaseAppException  <br>
     */
    private void writeImg(String path, String fileName, String image) throws BaseAppException {

        File file = new File(path);
        // 如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }
        if (image == null) {
            return;
        }
        byte[] b = Base64.decode(image);

        File f = new File(path + fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(b);
            fos.flush();

        }
        catch (FileNotFoundException e) {
            logger.error(e);
        }
        catch (IOException e) {
            logger.error(e);
        }
        finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            }
            catch (IOException e) {
                logger.error(e);
            }
        }

    }
    /**
     * 
     * Description: <br> 
     *  
     * @author qi.mingjie<br>
     * @taskId  <br>
     * @return List <br>
     * @throws BaseAppException  <br>
     */
    public List<DynamicDict> qrySystemParamByMask() throws BaseAppException {
        return getSystemDao().qrySystemParamByMask();
    }
    /**
     * 
     * Description: <br> 
     *  
     * @author qi.mingjie<br>
     * @taskId  <br>
     * @param listLogin <br>
     * @param subsId <br>
     * @param sessionId <br>
     * @return DynamicDict <br>
     * @throws BaseAppException  <br>
     */
    public Boolean loginSessionMgr(List<DynamicDict> listLogin, Long subsId, String sessionId) throws BaseAppException {

        LoginSessionDto dto = new LoginSessionDto();
        dto.setSubsId(subsId);
        dto.setSessionId(sessionId);
        dto.setIsLock("N");
        dto.setLoginFailedCount(0L);
        dto.setCreateDate(DateUtil.GetDBDateTime());
        dto.setUpdateDate(DateUtil.GetDBDateTime());
        if (!listLogin.isEmpty()) {
            getSystemDao().updateLoginSession(dto);
            return true;
        }
        else {
            getSystemDao().insertLoginSession(dto);
        }
        return true;
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author qi.mingjie<br>
     * @taskId  <br>
     * @param request <br>
     * @param response <br>
     * @param method <br>
     * @return DynamicDict <br>
     * @throws BaseAppException  <br>
     */
    public JSONObject loginPasswordEncryption(HttpServletRequest request, HttpServletResponse response, String method) throws BaseAppException {

        DynamicDict dict = MvcBoUtil.requestToBO(request);
        JSONObject result = new JSONObject();
        String loginPwd = dict.getString("LOGIN_PASSWD");
        String md5Pwd = "";
        if (StringUtil.isNotEmpty(loginPwd)) {
            md5Pwd = SecurityHelper.dESEncrypt(loginPwd);
        }
        result.put("PASSWORD", md5Pwd);
        result.put("IS_SUCCESS", true);
        return result;
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author qi.mingjie<br>
     * @taskId  <br>
     * @param listLogin <br>
     * @param subsId <br>
     * @param sessionId <br>
     * @return DynamicDict <br>
     * @throws BaseAppException  <br>
     */
    public LoginSessionDto loginSessionLockAndFailed(List<DynamicDict> listLogin, Long subsId, String sessionId) throws BaseAppException {

        // List<DynamicDict> listLogin = getSystemDao().qryLoginSession(subsId, null);
        LoginSessionDto dto = new LoginSessionDto();
        dto.setSubsId(subsId);
        dto.setSessionId(sessionId);
        
        if (!listLogin.isEmpty()) {
            String isLock = listLogin.get(0).getString("IS_LOCK");
            Long failedCount = listLogin.get(0).getLong("LOGINFAILED_COUNT");
            if ("Y".equals(isLock)) {
                java.sql.Date updateDate = listLogin.get(0).getDate("UPDATE_DATE");
                
                long nOffsetMinutes = DateUtil.differDateInDays(DateUtil.GetDBDateTime(), updateDate, 1);
                if (nOffsetMinutes > 30) { //小于30分钟，不允许登录
                    dto.setIsLock("N");
                    dto.setLoginFailedCount(0L);
                    dto.setUpdateDate(DateUtil.GetDBDateTime());
                    getSystemDao().updateLoginSessionLockState(dto);
                }
                else {
                    dto.setIsLock(isLock);
                }
            } 
            else {
                if (failedCount != null) {
                    failedCount = failedCount.longValue() + 1;
                }
                else {
                    failedCount = 1L;
                }
                if (failedCount > 4) {
                    dto.setIsLock("Y");
                }
                else {
                    dto.setIsLock("N");
                }
                dto.setLoginFailedCount(failedCount);
                dto.setUpdateDate(DateUtil.GetDBDateTime());
                getSystemDao().updateLoginSessionLockState(dto);
            }
        }
        else {
            dto.setIsLock("N");
            dto.setLoginFailedCount(0L);
            dto.setCreateDate(DateUtil.GetDBDateTime());
            dto.setUpdateDate(DateUtil.GetDBDateTime());
            getSystemDao().insertLoginSession(dto);

        }
        return dto;
    }

}