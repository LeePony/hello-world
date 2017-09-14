package com.ztesoft.zsmart.bss.selfcare.servlet;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ztesoft.zsmart.core.auto.AutoRunningThreadManager;
import com.ztesoft.zsmart.core.configuation.ConfigurationMgr;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.i18n.FlexResourceManager;
import com.ztesoft.zsmart.core.i18n.utils.LocaleUtil;
import com.ztesoft.zsmart.core.jdbc.Session;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.core.utils.ShowVersion;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.core.utils.model.ModuleDto;

import com.ztesoft.zsmart.web.resource.Common;
import com.ztesoft.zsmart.web.util.IPUtil;

/**
 * <p>
 * Title: MainServlet Init some DataObject Resource
 * </p>
 * <p>
 * Description: OCSWeb
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: ZTEsoft
 * </p>
 * 
 * @author lu.zhen
 * @version 1.0
 */
public class MainServlet extends HttpServlet {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5641357322100877157L;

    /**
     * date
     */
    public static String  date = (new Date()).toString();

    /**
     * logger
     */
    public static final ZSmartLogger logger = ZSmartLogger.getLogger(MainServlet.class);

    /**
     * 
     * Description: <br> 
     *  
     * @author XXX<br>
     * @taskId <br> <br>
     */
    public void init() {
        Session ses = null;
        try {
            ses = SessionContext.currentSession();
            // 启动事务
            ses.beginTrans();
            Common.DEFAULT_LANGUAGE = this.getInitParameter("DEFAULT_LANGUAGE");
            Common.LOCAL_CHARSET = this.getInitParameter("DEFAULT_CHARSET");
            Common.REQUEST_ENCODE = this.getInitParameter("REQUEST_ENCODE");
            Common.APP_IP = Long.valueOf(this.getInitParameter("SYSTEM_APP"));
            Common.HOST_IP = IPUtil.getLocalIP();
            ServletContext context = this.getServletConfig().getServletContext();
            // for 36334 by luxiang start
            String webAppPath = context.getRealPath("");
            webAppPath.replace('\\', '/');
            // end
            String realRootPath = context.getRealPath("/");
            Common.REAL_ROOT_PATH = realRootPath;
            initOCSConfig();
            getFlexCommincationConfig();

            getFlexLocal();
            updateLocaleCss();
            printVersion();

            logger.debug("Login Session Monitor. Main servlet initialize,clear all online user.Dest ip is {}",
                Common.HOST_IP);
            handlerUserOnline();
            String isDevelopMode = ConfigurationMgr.instance().getString("productMode.isDevelopMode", "false");
            // 提交事务
            ses.commitTrans();
            // task:690532 mod by gao.peng3
            AutoRunningThreadManager.getInstance().init();
        }
        catch (java.lang.Throwable ex) {
            ExceptionHandler.logErrorInfo("MainServlet init error.", ex);
        }
        finally {
            try {
                // 释放事务
                ses.releaseTrans();
            }
            catch (BaseAppException e) {
                logger.error(e);
            }
        }
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @throws Exception  <br>
     */
    public void initOCSConfig() throws Exception {
        ServletContext application = getServletContext();
        // modified by chi.yuxing [760795]
        // String version = ConfigurationMgr.instance().getString("version_no", "");
        // String updateDate = ConfigurationMgr.instance().getString("update_date", "");
        String version = "";
        String updateDate = "";
        ShowVersion showVersion = new ShowVersion();
        showVersion.loadAllVersionInfo();
        showVersion.loadModuleInfo();
        ModuleDto module = showVersion.getCVBSModuleDto();
        if (null != module) {
            version = module.getVersion();
            updateDate = module.getDate();
        }
        application.setAttribute("VERSION", getVersion(version));
        application.setAttribute("PreciseVersion", version);
        application.setAttribute("UPGRATE_DATE", updateDate);

        Common.VERSION = (String) application.getAttribute("VERSION");
        Common.ResLoggerLevel = Common.getConfig("ResourceLoggerLevel");
        Common.IS_DEVELOPE_MODE = ConfigurationMgr.instance().getBoolean("productMode.isDevelopMode");

        String auditLog = ConfigurationMgr.instance().getString("AuditLog.value", "false");
        application.setAttribute("AuditLog", auditLog.toUpperCase());

        String dataFormat = ConfigurationMgr.instance().getString("commons.dateFormat.java.dateTime",
            "yyyy-MM-dd HH:mm:ss");
        application.setAttribute("DateFormat", dataFormat);
        application.setAttribute("Direction", "LTR");
        application.setAttribute("Email", "someone@zte.com.cn");
        application.setAttribute("ShowDetailLogComments", "false");

        application.setAttribute("AlignRightString", "RIGHT");
        application.setAttribute("ImgFilter", "");
        application.setAttribute("PosLeftString", "LEFT");
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param oriVersion 
     * @return  <br>
     */
    public String getVersion(String oriVersion) {
        String version = oriVersion;
        int point1 = oriVersion.indexOf(".");
        if (point1 > -1) {
            int point2 = oriVersion.indexOf(".", point1 + 1);
            if (point2 > -1) {
                version = oriVersion.substring(0, point2);
            }
        }
        return version;
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @return  <br>
     */
    public boolean checkGlobalConfig() {
        return true;
    }

   /**
    * 
    * Description: <br> 
    *  
    * @author yao.yueqing<br>
    * @taskId  <br>  <br>
    */
    public void printVersion() {
        logger.error("\t\t****************************************");
        logger.error("\t\t*     ZTEsoft R13 WebStructure         *");
        logger.error("\t\t*     [{}] [{}]", MainServlet.date, "     *");
        logger.error("\t\t*     VERSION : [{}] [{}]", Common.VERSION, "                  *");
        logger.error("\t\t****************************************");
        logger.error("");
    }

    /**
     * 获取Flex的通信配置
     */
    public void getFlexCommincationConfig() {
        String flexConfigPath = getServletContext().getRealPath("WEB-INF/flex/remoting-config.xml");
        File configFile = new File(flexConfigPath);
        String channel = "";
        if (configFile.exists() && configFile.isFile()) {
            SAXReader saxReader = new SAXReader();
            Document doc = null;
            try {
                doc = saxReader.read(configFile);
                Element root = doc.getRootElement();
                List<Element> elementList = root.elements("destination");
                for (Element e : elementList) {
                    String attValue = e.attributeValue("channels");
                    if (!StringUtil.isEmpty(attValue)) {
                        channel = attValue;
                        break;
                    }
                }
                if ("".equals(channel)) {
                    elementList = root.element("default-channels").elements("channel");
                    for (Element e : elementList) {
                        String attValue = e.attributeValue("ref");
                        if (!StringUtil.isEmpty(attValue)) {
                            channel = attValue;
                            break;
                        }
                    }
                }
                if (StringUtil.isEmpty(channel)) {
                    channel = "my-amf";
                }
            }
            catch (DocumentException ex) {
                channel = "my-amf";
            }
        }
        else {
            channel = "my-amf";
        }
        if ("my-secure-amf".equals(channel)) {
            channel = "messagebroker/amfsecure";
        }
        else {
            channel = "messagebroker/amf";
        }
        getServletContext().setAttribute("flex-communication-channel", channel);
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>  <br>
     */
    public void getFlexLocal() {
        ArrayList<String> laguages = Common.SUPPORT_LANGUAGE_LIST;
        for (String s : laguages) {
            FlexResourceManager.getInstance().getFlexResources(LocaleUtil.localeFromString(s));
        }
    }

    /**
     * 更新CSS <br>
     * 
     * @throws BaseAppException 异常<br>
     */
    private void updateLocaleCss() throws BaseAppException {
        for (int i = 0; i < Common.SUPPORT_LANGUAGE_LIST.size(); i++) {
            String key = Common.SUPPORT_LANGUAGE_LIST.get(i).toString();
            if (java.util.Locale.ENGLISH.toString().equalsIgnoreCase(key)
                || java.util.Locale.CHINESE.toString().equalsIgnoreCase(key)
                || java.util.Locale.US.toString().equalsIgnoreCase(key)
                || java.util.Locale.PRC.toString().equalsIgnoreCase(key)) {
                continue;
            }
            DynamicDict dt = new DynamicDict();
            dt.set("LANG", key);
            dt.setServiceName("UpdateWebCss");
            ServiceFlow.callService(dt);
            // System.out.println("\t\t*********Updated CSS file for locale:" + key + "  ********");
        }
    }

    /**
     * Description: <br>
     * <br>
     * 
     * @throws BaseAppException 异常
     */
    private void handlerUserOnline() throws BaseAppException {
        // UserOnLineManager userOnLineManager = new UserOnLineManager();
        // userOnLineManager.clearUserOnLine(Common.HOST_IP);
        // 启动时清理当前服务器下的在线用户数据
        DynamicDict dt = new DynamicDict();
        dt.set("DEST_IP", Common.HOST_IP);
        // new UserOnLineManager().clearOfflineUser(dt);
    }

}
