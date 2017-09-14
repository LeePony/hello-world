package com.ztesoft.zsmart.bss.selfcare.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import com.ztesoft.zsmart.bss.common.config.bll.ConfigItemCache;
import com.ztesoft.zsmart.bss.selfcare.bll.SystemManager;
import com.ztesoft.zsmart.bss.selfcare.common.cache.SelfCareCache;
import com.ztesoft.zsmart.bss.selfcare.common.core.api.ActionDomain;
import com.ztesoft.zsmart.bss.selfcare.common.core.api.CoreDef;
import com.ztesoft.zsmart.bss.selfcare.common.helper.CommonHelper;
import com.ztesoft.zsmart.bss.selfcare.common.util.MvcBoUtil;
import com.ztesoft.zsmart.bss.selfcare.common.util.RandomUtil;
import com.ztesoft.zsmart.bss.selfcare.model.MenuDto;
import com.ztesoft.zsmart.bss.selfcare.model.ScLoginHis;
import com.ztesoft.zsmart.bss.selfcare.model.SessionDto;
import com.ztesoft.zsmart.bss.selfcare.service.CommonServices;
import com.ztesoft.zsmart.bss.selfcare.service.CustServices;
import com.ztesoft.zsmart.bss.selfcare.service.SubsServices;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.i18n.MessageSource;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.Constants;
import com.ztesoft.zsmart.core.utils.DateUtil;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.mvc.core.helper.BoUtil;
import com.ztesoft.zsmart.web.resource.Common;

/**
 * <Description> <br>
 * 
 * @author moon<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年5月26日 <br>
 * @since CRM R8.1<br>
 * @see com.ztesoft.zsmart.bss.selfcare.controller <br>
 */
@Controller
public class SystemController {

    /**
     * systemManager
     */
    @Autowired
    public SystemManager systemManager;

    /**
     * logger
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(SystemController.class);

    /**
     * expireTime
     */
    private static Long expireTime = Long.valueOf(ConfigItemCache.instance().getString("CUSTOMER_CARE/ATHENA_CC_PUBLIC/ATHENA_SC_SMS_CODE_EFF_TIME", "60"));

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @return JSONObject<br>
     * @throws BaseAppException 
     */
    @RequestMapping("QryScMenu")
    @ResponseBody
    public Map<String, String> qryScMenu(HttpServletRequest request) throws BaseAppException {
        Map<String, String> obj = new HashMap<String, String>();
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        
        String paidFlag = session.getPaidFlag();
        // 转为json格式
        JSONObject menuJson = new JSONObject();
        menuJson.put("menu", JSONArray.fromObject(SelfCareCache.qryMenuList(paidFlag)));

        obj.put("CODE", "0");
        obj.put("RESULT", menuJson.toString());
        JSONObject.fromObject(obj);
        
        logger.debug("sc menu Json: [{}]", JSONUtils.valueToString(obj));
        return obj;
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param request
     * @return
     * @throws BaseAppException  <br>
     */
    @RequestMapping("qrySysDate")
    @ResponseBody
    public Map qrySysDate(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        Calendar c= Calendar.getInstance();
        Date beginDate = DateUtil.getBeginDayOfMonth(new java.sql.Date(c.getTime().getTime()));
        List<DynamicDict> monthList = new ArrayList<DynamicDict>();
        for(int i =1;i<7;i++){
            DynamicDict dateDict = new DynamicDict();
            int offSet = i*-1;
            dateDict.set("OFFSET", offSet);
            String date = DateUtil.date2String(DateUtil.offsetMonth(new java.sql.Date(beginDate.getTime()), offSet));
            dateDict.set("MONTH", date.substring(0, 7));
            
            monthList.add(dateDict);
        }
       dict.set("MONTH_LIST", monthList);
        return BoUtil.boToJson(dict);
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @return  JSONObject <br>
     * @throws BaseAppException 
     */
    @RequestMapping("QryAd")
    @ResponseBody
    public JSONObject queryAd(HttpServletRequest request) throws BaseAppException{
        DynamicDict result = new DynamicDict();
        
        String path = request.getRealPath("/");
        List<DynamicDict> adList =  SelfCareCache.qryScAthenaImage(path);
        result.set("AD_LIST", adList);
        return BoUtil.boToJson(result);
    }
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @return JSONObject
     * @throws BaseAppException  <br>
     */
    @RequestMapping("queryLanguageList")
    @ResponseBody
    public JSONObject queryLanguageList() throws BaseAppException{
        DynamicDict tmp = new DynamicDict();
        List<DynamicDict> al = new ArrayList<DynamicDict>();
        for(int i=0;i<Common.SUPPORT_LANGUAGE_LIST.size();i++)
        {
            String key = Common.SUPPORT_LANGUAGE_LIST.get(i).toString();
            DynamicDict dt = new DynamicDict();
            dt.set("KEY", key);
            String value  = "";
            int index = key.indexOf("_");
            if(index>0){
                Locale l = new Locale(key.substring(0,index),key.substring(index+1));
                value  =  l.getDisplayName(l);
            }else{
                value  = new Locale(key).getDisplayName( new Locale(key));
            }
            dt.set("SHOW_VALUE",value);
            al.add(dt);
        }
        tmp.set("LANGUAGE_LIST", al);
    
        return BoUtil.boToJson(tmp);
    }
    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @return Object<br>
     * @RequestMapping("QryScMenuNew")
     * @ResponseBody public Object qryScMenuNew() { HashMap pmap = new HashMap(); try { List<DynamicDict> listMenu =
     *               getSystemManager().qryAllScMenu(); pmap.put("total", 1); pmap.put("page", 1); pmap.put("rows",
     *               rePackage4Flexi(listMenu)); } catch (BaseAppException e) { logger.error(e); } return pmap; }
     */

    public SystemManager getSystemManager() {
        return systemManager;
    }

    public void setSystemManager(SystemManager systemManager) {
        this.systemManager = systemManager;
    }

    /**
     * Description: <br>
     * 
     * @author XXX<br>
     * @taskId <br>
     * @param request
     * @param response
     * @throws BaseAppException <br>
     * @return JSONObject
     */
    @RequestMapping("sendMsgCode")
    @ResponseBody
    public JSONObject sendMsgCode(HttpServletRequest request, HttpServletResponse response) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        logger.debug("sendMsgCode dict = {}", dict);
        String accNbr = dict.getString("ACC_NBR");
        String msgType = dict.getString("MSG_TYPE");
        if (StringUtil.isNotEmpty(accNbr) && accNbr.startsWith("0")) {
            accNbr = accNbr.substring(1, accNbr.length());
        }
        logger.debug("sendMsgCode accNbr = {}", accNbr);
        validateForSendMsg(accNbr, msgType);

        String msg = getSystemManager().sendMsgCode(dict);
        HttpSession ses = request.getSession();
        if (ses != null) {
            ses.setAttribute(msgType + "_CODE", msg);
            ses.setAttribute(msgType + "_TIME", System.currentTimeMillis());
           // logger.debug("MSG_CODE:" + msg);
        }
        JSONObject result = new JSONObject();
        result.put("flag", "success");
        result.put("effSecond", expireTime);
        return result;
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param request
     * @return
     * @throws BaseAppException  <br>
     */
    @RequestMapping("validate4CallHistory")
    @ResponseBody
    public JSONObject validate4CallHistory(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        
        String input = dict.getString("MSG");
        HttpSession ses = request.getSession();
        
        SessionDto session = (SessionDto) ses.getAttribute(Common.USER_INFO_BEAN);
        input =  session.getAccNbr()+input;
        
        if(ses!=null){
            String msg = (String) ses.getAttribute("CALL_HISTORY_CODE");
            
            Long sendTime = (Long) ses.getAttribute("CALL_HISTORY_TIME");
            long now = System.currentTimeMillis();
            // String registerCode = dict.getString("REGISTER_CODE");
            if (isExpire(now, sendTime)) {
                ExceptionHandler.publish("SC_MSG_EXPIRED", ExceptionHandler.BUSS_ERROR);
            }
            if (!input.equals(msg)) {
                ExceptionHandler.publish("SC_MSG_CODE_ERROR", ExceptionHandler.BUSS_ERROR);
            }
            ses.setAttribute("CAN_QRY_CDR", "Y");
            
            
            
        }else{
            ExceptionHandler.publish("SC_SESSION_EXPIRE", ExceptionHandler.BUSS_ERROR);
        }
        
        JSONObject result = new JSONObject();
        result.put("RESULT", "0");
        return result;
        
    }
    
    /** 
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param request
     * @return
     * @throws BaseAppException  <br>
     */
    @RequestMapping("sendMsg4ValidateCallHistory")
    @ResponseBody
    public JSONObject sendMsg4ValidateCallHistory(HttpServletRequest request) throws BaseAppException {
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        DynamicDict dict = new DynamicDict();
        dict.set("ACC_NBR", session.getAccNbr());
        String msg = getSystemManager().sendMsgCode(dict);
        logger.debug("msg : {}",msg);
        request.getSession().setAttribute("CALL_HISTORY_CODE", msg);
        request.getSession().setAttribute("CALL_HISTORY_TIME", System.currentTimeMillis());
        JSONObject result = new JSONObject();
        result.put("flag", "success");
        result.put("effSecond", expireTime);
        return result;
       
    }
    /**
     * Description: <br>
     * 
     * @author XXX<br>
     * @taskId <br>
     * @param request
     * @param response
     * @return JSONObject
     * @throws BaseAppException <br>
     */
    @RequestMapping("resetPwd")
    @ResponseBody
    public JSONObject resetPwd(HttpServletRequest request, HttpServletResponse response) throws BaseAppException {
        JSONObject result = new JSONObject();
//
        DynamicDict dict = MvcBoUtil.requestToBO(request);

        validateInputMsg(request, dict);
        String accNbr = dict.getString("ACC_NBR");
        if (StringUtil.isNotEmpty(accNbr) && accNbr.startsWith("0")) {
            accNbr = accNbr.substring(1);
        }
        DynamicDict custDict = CustServices.qryCustByAccNbr(accNbr);
        // 调用修改客户密码接口
        DynamicDict modifyCustDict = new DynamicDict();
        modifyCustDict.set("CUST_CODE", custDict.getString("CUST_CODE"));
        String pwd = RandomUtil.getRandomString(6);
        modifyCustDict.set("PWD", pwd);
        modifyCustDict.set("SP_ID", custDict.getString("SP_ID"));
        CustServices.modCust(modifyCustDict);

        DynamicDict adviceDict = new DynamicDict();
        adviceDict.set("ACC_NBR", accNbr);
        adviceDict.set("STD_CODE", "SC_RESET_PWD");
        adviceDict.set("PWD", pwd);
        CommonServices.adviceService(adviceDict);
        result.put("MSG", MessageSource.getText("SC_RESETPWD_SUCCESS"));
        result.put("URL", "login.html");
        return result;
    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param request
     * @param response
     * @return JSONObject
     * @throws BaseAppException <br>
     */
//    @RequestMapping("register")
//    @ResponseBody
//    public JSONObject register(HttpServletRequest request, HttpServletResponse response) throws BaseAppException {
//
//        JSONObject result = new JSONObject();
//        DynamicDict dict = MvcBoUtil.requestToBO(request);
//        logger.debug("register dict = {}", dict);
//        String accNbr = dict.getString("ACC_NBR");
//        if (StringUtil.isNotEmpty(accNbr) && accNbr.startsWith("0")) {
//            accNbr = accNbr.substring(1, accNbr.length());
//        }
//        logger.debug("register accNbr = {}", accNbr);
//        validateInputMsg(request, dict);
//        DynamicDict custDict = CustServices.qryCustByAccNbr(accNbr);
//        if(!"A".equals(custDict.getString("CUST_TYPE"))){//企业客户不能登录
//            ExceptionHandler.publish("SC_B_CUSTCANNOTREGISTER", ExceptionHandler.BUSS_ERROR);
//        }
//        // 调用修改客户密码接口
//        DynamicDict modifyCustDict = new DynamicDict();
//        modifyCustDict.set("CUST_CODE", custDict.getString("CUST_CODE"));
//        modifyCustDict.set("PWD", dict.getString("PWD"));
//        modifyCustDict.set("SP_ID", custDict.getString("SP_ID"));
//        CustServices.modCust(modifyCustDict);
//        // 写入注册用户表
//
//        getSystemManager().saveScUserData(custDict);
//        result.put("URL", "login.html");
//        result.put("MSG_TYPE", "1");
//        result.put("MSG", MessageSource.getText("SC_REGISTER_SUCCESS"));
//        return result;
//    }

    /**
     * Description: 登陆成功跳转 <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param request <br>
     * @param response <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
//    @RequestMapping("Login")
//    @ResponseBody
//    public JSONObject loginSuccess(HttpServletRequest request, HttpServletResponse response) throws BaseAppException {
//        return getSystemManager().login(request, response, "PWD");
//    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param request <br>
     * @param response <br>
     * @throws BaseAppException <br>
     * @throws IOException <br>
     */
    @RequestMapping("Logout")
    @ResponseBody
    public JSONObject logout(HttpServletRequest request, HttpServletResponse response) throws BaseAppException {
        JSONObject json = new JSONObject();
        getSystemManager().logout(request);
        json.put("CODE", "1");
        return json;
    }
    /**
     * Description: 登陆成功跳转 <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param request <br>
     * @param response <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("loginWithSmsCode")
    @ResponseBody
    public JSONObject loginWithSmsCode(HttpServletRequest request, HttpServletResponse response)
        throws BaseAppException {

        DynamicDict requestDict = MvcBoUtil.requestToBO(request);

        validateInputMsg(request, requestDict);

        return getSystemManager().login(request, response, "SMS");
    }

    @RequestMapping("qryLoginHistory")
    @ResponseBody
    public JSONObject qryLoginHistory(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        
        dict.set("SUBS_ID", session.getSubsId());
        dict.set("ACC_NBR", session.getAccNbr());
        
        List<DynamicDict> loginList = getSystemManager().qryLoginHistory(dict);
        
        DynamicDict result = new DynamicDict();
        result.set("LOGIN_LIST", loginList);
        return BoUtil.boToJson(result);
    }
    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param request
     * @param dict
     * @throws BaseAppException <br>
     */
    private void validateInputMsg(HttpServletRequest request, DynamicDict dict) throws BaseAppException {
        // 先校验验证码
        HttpSession ses = request.getSession();
        long now = System.currentTimeMillis();
        String accNbr = dict.getString("ACC_NBR");
        String msgCodeStr = dict.getString("MSG_TYPE") + "_CODE";
        String msgTimeStr = dict.getString("MSG_TYPE") + "_TIME";
        String verCode = dict.getString("VER_CODE");

        if (ses != null) {
            if (ses.getAttribute(msgCodeStr) == null || ses.getAttribute(msgTimeStr) == null) {
                ExceptionHandler.publish("SC_MSG_NULL", ExceptionHandler.BUSS_ERROR);
            }
            String registCode = (String) ses.getAttribute(msgCodeStr);
            Long sendTime = (Long) ses.getAttribute(msgTimeStr);

            // String registerCode = dict.getString("REGISTER_CODE");
            if (!registCode.equals(accNbr + verCode)) {
                ExceptionHandler.publish("SC_MSG_CODE_ERROR", ExceptionHandler.BUSS_ERROR);
            }

            if (isExpire(now, sendTime)) {
                ExceptionHandler.publish("SC_MSG_EXPIRED", ExceptionHandler.BUSS_ERROR);
            }
        }
        else {
            ExceptionHandler.publish("SC_SESSION_EXPIRE", ExceptionHandler.BUSS_ERROR);
        }
    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param accNbr
     * @param msgType
     * @throws BaseAppException <br>
     */
    private void validateForSendMsg(String accNbr, String msgType) throws BaseAppException {
        DynamicDict subsDict = SubsServices.qrySubsByAccNbr(null, accNbr);
        if (subsDict == null) {
            ExceptionHandler.publish("SC_REG_SUBS_NOT_EXITS", ExceptionHandler.BUSS_ERROR);
        }
        

        DynamicDict userDict = getSystemManager().qryScUser(subsDict.getLong("CUST_ID"));

        if ("REGISTER".equals(msgType) && userDict != null) {
            ExceptionHandler.publish("SC_USER_HAS_REGIST", ExceptionHandler.BUSS_ERROR);
        }

        if (!"REGISTER".equals(msgType) && !"LOGIN_WITH_SMS".equals(msgType) && userDict == null) {
            ExceptionHandler.publish("SC_USER_NOT_REGIST", ExceptionHandler.BUSS_ERROR);
        }
    }
    
    /**
     * Description: 密码加密 <br>
     * 
     * @author qi.mingjie<br>
     * @taskId <br>
     * @param request <br>
     * @param response <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("passwordEncryption")
    @ResponseBody
    public JSONObject passwordEncryption(HttpServletRequest request, HttpServletResponse response) throws BaseAppException {
        return getSystemManager().loginPasswordEncryption(request, response, "");
    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param now
     * @param send
     * @return <br>
     */
    private boolean isExpire(long now, long send) {
        return now - send > (expireTime * 1000) ? true : false;
    }
}
