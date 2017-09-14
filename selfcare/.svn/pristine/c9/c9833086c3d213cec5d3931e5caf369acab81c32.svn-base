/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.ztesoft.zsmart.bss.selfcare.common.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import com.ztesoft.zsmart.bss.selfcare.common.util.MvcBoUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;

/**
 * <Description> <br>
 * 
 * @author hu.jianfei<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2013-1-7 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.esc.common.helper <br>
 */

public final class CommonHelper {

    /**
     * 构造方法
     */
    private CommonHelper() {

    }

    /**
     * logger <br>
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(CommonHelper.class);

    /**
     * Description: <br>
     * 
     * @author wu.manjiang<br>
     * @taskId <br>
     * @param username <br>
     * @param password <br>
     * @param authNamespace <br>
     * @param searchPath <br>
     * @return string
     * @throws BaseAppException <br>
     */
    public static String getCmsReportHtml(String username, String password, String authNamespace, String searchPath)
        throws BaseAppException {
        return null;
    }

    /**
     * Description: <br>
     * 
     * @author hu.jianfei<br>
     * @taskId <br>
     * @param emailStr <br>
     * @return <br>
     */
    public static boolean checkEmailFormat(String emailStr) {

        boolean flag = false;
        String regex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(emailStr);
        flag = m.matches();
        return flag;

    }

    /**
     * Description: <br>
     * 
     * @author hu.jianfei<br>
     * @taskId <br>
     * @param srcStr <br>
     * @param keyStr <br>
     * @return <br>
     */
    public static String encryptAES(String srcStr, String keyStr) {

        byte[] raw = keyStr.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        byte[] encrypted = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            encrypted = cipher.doFinal(srcStr.getBytes());
        }
        catch (Exception ex) {
            logger.error(ex);
        }

        return CommonHelper.asHex(encrypted);
    }

    /**
     * Description: <br>
     * 
     * @author hu.jianfei<br>
     * @taskId <br>
     * @param srcStr <br>
     * @param keyStr <br>
     * @return <br>
     */
    public static String decryptAES(String srcStr, String keyStr) {

        byte[] raw = keyStr.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        String originalString = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] original = cipher.doFinal(CommonHelper.hexToByte(srcStr));
            originalString = new String(original);
        }
        catch (Exception ex) {
            logger.error(ex);
        }
        return originalString;
    }

    /**
     * Description: <br>
     * 
     * @author hu.jianfei<br>
     * @taskId <br>
     * @param buf <br>
     * @return <br>
     */
    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if ((buf[i] & 0xff) < 0x10) {
                strbuf.append('0');
            }
            strbuf.append(Long.toString(buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    /**
     * hex string to byte[]
     * 
     * @param hex <br>
     * @return <br>
     */
    public static byte[] hexToByte(String hex) {
        if (hex.length() % 2 != 0) {
            return null;
        }

        byte[] target = new byte[hex.length() / 2];
        int i = 0;
        int j = 0;
        for (; i < hex.length(); i += 2, j++) {
            byte b = hexCharToByte(hex.charAt(i));
            b = (byte) (b << 4);
            b += hexCharToByte(hex.charAt(i + 1));
            target[j] = b;
        }
        return target;
    }

    /**
     * hex char to byte
     * 
     * @param c <br>
     * @return <br>
     */
    public static byte hexCharToByte(char c) {
        byte b = -1;
        char[] chars = new char[] {
            'f', 'F', 'e', 'E', 'd', 'D', 'c', 'C', 'b', 'B', 'a', 'A', '9', '8', '7', '6', '5', '4', '3', '2', '1',
            '0'
        };
        for (char ch : chars) {
            if (c == ch) {
                b = (byte) Character.digit(ch, 16);
                break;
            }
        }
        return b;

        /*
         * switch (c) { case 'f': case 'F': b = 15; break; case 'e': case 'E': b = 14; break; case 'd': case 'D': b =
         * 13; break; case 'c': case 'C': b = 12; break; case 'b': case 'B': b = 11; break; case 'a': case 'A': b = 10;
         * break; case '9': b = 9; break; case '8': b = 8; break; case '7': b = 7; break; case '6': b = 6; break; case
         * '5': b = 5; break; case '4': b = 4; break; case '3': b = 3; break; case '2': b = 2; break; case '1': b = 1;
         * break; case '0': b = 0; break; default: logger.warn("The char({}) is not a hex char.", c); } return b;
         */
    }




  
    /**
     * Description: 判断数组中是否有重复值 <br>
     * 有重复值，返回 false <br>
     * 无重复值，返回 true <br>
     * 
     * @author hu.jianfei<br>
     * @taskId <br>
     * @param array <br>
     * @return <br>
     */
    public static boolean checkArrayRepeat(String[] array) {

        Set<String> set = new HashSet<String>();
        for (String str : array) {
            set.add(str);
        }
        return !(set.size() != array.length);
    }

    /**
     * 返回report的url<br>
     * 
     * @author wu.hao<br>
     * @taskId <br>
     * @param dict <br>
     * @param reportName <br>
     * @param passportId <br>
     * @return <br>
     * @throws BaseAppException <br>
     
    @SuppressWarnings("unchecked")
    public static String getReprotUrl(DynamicDict dict, String reportName, String passportId) throws BaseAppException {
        // 默认包的空间 和 登陆串一起
        String defaultNameSpace = CacheHelper.getConfigItemParam("SC_PUBLIC_NODE", "REPORT_NAME_SPACE");
        // 返回报表的URL
        String reportUrl = CacheHelper.getConfigItemParam("SC_PUBLIC_NODE", "REPORT_CONFIG_VIEW");

        String packageStr = "";
        List<DynamicDict> reprotPathlist = MvcBoUtil.jsonArrayToList(MvcBoUtil.boSqlResultToJson(dict));
        if (reprotPathlist != null && !reprotPathlist.isEmpty()) {
            // report完整路径名
            String reportPath = null;
            // report名
            for (int i = 0; i < reprotPathlist.size(); i++) {
                DynamicDict reprotPathDict = reprotPathlist.get(i);
                if (reprotPathDict.getString("REPORT_NAME").equals(reportName)) {
                    // TestPackage\liuwei\test\test1\InvoiceDetail01
                    reportPath = reprotPathDict.getString("REPORT_PATH").substring(1,
                        reprotPathDict.getString("REPORT_PATH").length());
                    packageStr = reportPath.substring(0, reportPath.indexOf("\\"));
                    // TestPackage\liuwei\test
                    String allFoldName = reportPath.substring(reportPath.indexOf("\\") + 1, reportPath.length()); // 去掉package
                    allFoldName = allFoldName.substring(0, allFoldName.lastIndexOf("\\") + 1); // 去掉reportname +1需要\
                                                                                               // liuwei\
                    // |liuwei|test
                    if (StringUtil.isNotEmpty(allFoldName)) {
                        String[] folds = StringUtil.split(allFoldName, "\\");
                        String fold = "";
                        if (folds != null) {
                            // liuwei\test\
                            for (int j = 0; j < folds.length; j++) {
                                if (StringUtil.isNotEmpty(folds[j])) {
                                    fold += "folder[@name='" + folds[j] + "']" + "/";
                                }
                            }
                            fold = fold.substring(0, fold.length() - 1);
                        }
                        return reportUrl.replace("REPORT_DEFAULT_PACKAGE", "package[@name='" + packageStr + "']")
                            .replace("REPORT_FOLDER_NAME", fold)
                            .replace("REPORT_NAME", "report[@name='" + reportName + "']")
                            .replace("DEF_NAME_SPACE", defaultNameSpace).replace("M_PASSPORTID", passportId);
                    }
                    // REPORT_DEFAULT_PACKAGE = package[@name='TestPackage']
                    // REPORT_FOLDER_NAME = folder[@name='Dashboard']
                    // REPORT_NAME = report[@name='xx']
                    return reportUrl.replace("REPORT_DEFAULT_PACKAGE", "package[@name='" + packageStr + "']")
                        .replace("/REPORT_FOLDER_NAME", "")
                        .replace("REPORT_NAME", "report[@name='" + reportName + "']")
                        .replace("DEF_NAME_SPACE", defaultNameSpace).replace("M_PASSPORTID", passportId);
                }
            }
        }
        return "";
    }*/

    /**
     * 返回report的url<br>
     * 
     * @author wu.manjiang<br>
     * @taskId <br>
     * @param dict <br>
     * @param reportName <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @SuppressWarnings("unchecked")
    public static String getReportSearchPath(DynamicDict dict, String reportName) throws BaseAppException {

        // 返回报表的URL
        String reportUrl = "/content/REPORT_DEFAULT_PACKAGE/REPORT_FOLDER_NAME/REPORT_NAME";

        String packageStr = "";
        List<DynamicDict> reprotPathlist = MvcBoUtil.jsonArrayToList(MvcBoUtil.boSqlResultToJson(dict));
        if (reprotPathlist != null && !reprotPathlist.isEmpty()) {
            // report完整路径名
            String reportPath = null;
            // report名
            for (int i = 0; i < reprotPathlist.size(); i++) {
                DynamicDict reprotPathDict = reprotPathlist.get(i);
                if (reprotPathDict.getString("REPORT_NAME").equals(reportName)) {
                    // TestPackage\liuwei\test\test1\InvoiceDetail01
                    reportPath = reprotPathDict.getString("REPORT_PATH").substring(1,
                        reprotPathDict.getString("REPORT_PATH").length());
                    packageStr = reportPath.substring(0, reportPath.indexOf("\\"));
                    // TestPackage\liuwei\test
                    String allFoldName = reportPath.substring(reportPath.indexOf("\\") + 1, reportPath.length()); // 去掉package
                    allFoldName = allFoldName.substring(0, allFoldName.lastIndexOf("\\") + 1); // 去掉reportname +1需要\
                                                                                               // liuwei\
                    // |liuwei|test
                    if (StringUtil.isNotEmpty(allFoldName)) {
                        String[] folds = StringUtil.split(allFoldName, "\\");
                        String fold = "";
                        if (folds != null) {
                            // liuwei\test\
                            for (int j = 0; j < folds.length; j++) {
                                if (StringUtil.isNotEmpty(folds[j])) {
                                    fold += "folder[@name='" + folds[j] + "']" + "/";
                                }
                            }
                            fold = fold.substring(0, fold.length() - 1);
                        }
                        return reportUrl.replace("REPORT_DEFAULT_PACKAGE", "package[@name='" + packageStr + "']")
                            .replace("REPORT_FOLDER_NAME", fold)
                            .replace("REPORT_NAME", "report[@name='" + reportName + "']");
                    }
                    // REPORT_DEFAULT_PACKAGE = package[@name='TestPackage']
                    // REPORT_FOLDER_NAME = folder[@name='Dashboard']
                    // REPORT_NAME = report[@name='xx']
                    return reportUrl.replace("REPORT_DEFAULT_PACKAGE", "package[@name='" + packageStr + "']")
                        .replace("/REPORT_FOLDER_NAME", "")
                        .replace("REPORT_NAME", "report[@name='" + reportName + "']");
                }
            }
        }
        return "";
    }

    /**
     * Description: <br>
     * 
     * @author Xu.Fuqin<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    public static String getWebRoot(HttpServletRequest request) throws BaseAppException {
        String strWebRoot = "";
        if (strWebRoot == null || strWebRoot.length() == 0) {
            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int port = request.getServerPort();
            String contextPath = request.getContextPath();
            logger.debug("scheme:{}", scheme);
            logger.debug("serverName:{}", serverName);
            logger.debug("port:{}", port);
            logger.debug("contextPath:{}", contextPath);

            String domain = "";
            if (StringUtil.isNotEmpty(domain)) {
                if (serverName.indexOf(domain) > -1) {
                    strWebRoot = "https";
                }
                else {
                    strWebRoot = scheme;
                }
            }
            else {
                strWebRoot = scheme;
            }

            strWebRoot += "://";
            strWebRoot += serverName;

            if (port != 80) {
                strWebRoot += ":" + port;
            }

            strWebRoot += contextPath + "/";
        }
        return strWebRoot;
    }

    /**
     * Description:URL中的特殊字符转义 <br>
     * 
     * @author Xu.Fuqin<br>
     * @taskId <br>
     * @param reportName <br>
     * @return <br>
     */
    public static String transUrlChar(String reportName) {

        return reportName.replaceAll("\\%", "%25").replaceAll("\\/", "%2F").replaceAll("\\&", "%26")
            .replaceAll("\\+", "%2B").replaceAll("\\ ", "%20").replaceAll("\\?", "%3F").replaceAll("\\#", "%23")
            .replaceAll("\\=", "%3D");
    }


    /**
     * Description:把日期转换为MMM-yyyy的格式 <br>
     * 
     * @author Xu.Fuqin<br>
     * @taskId <br>
     * @param runDate <br>
     * @return <br>
     */
    public static String formatBillMonth(Date runDate) {
        if (runDate == null) {
            return "";
        }

        SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy");
        return df.format(runDate);
    }

    /**
     * Description: <br>
     * 
     * @taskId <br>
     * @param src  <br>
     * @return Map  <br>
     * @throws IOException  <br>
     * @throws ClassNotFoundException <br>
     */
    public static Map deepCopy(Map src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteout);
        out.writeObject(src);
        ByteArrayInputStream bytein = new ByteArrayInputStream(byteout.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bytein);
        Map dest = (Map) in.readObject();
        return dest;
    }

    /**
     * 根据停机原因BlockReason取得界面上显示的停机原因字符串
     * 
     * @param blockReason 停机原因
     * @return 界面上显示的停机原因字符串
     * @throws BaseAppException ;
     */
    public static String getBlockReasonForDisplay(String blockReason) throws BaseAppException {
        if (StringUtil.isEmpty(blockReason)) {
            return "";
        }

        StringBuffer blockReasonStr = new StringBuffer("");

        // 分隔每一位
        String[] reasonArray = blockReason.split("");
        // 第一个元素为空
        for (int i = 1; i < reasonArray.length; i++) {
            if ("1".equals(reasonArray[i]) || "2".equals(reasonArray[i])) {
                blockReasonStr.append("SC_BLOCK_REASON_").append(i);
              break;
            }
        }
       
        return blockReasonStr.toString();
    }
    // public static void main(String[] args) {
    // //a&b+ c/d?%e#f=
    // String a = CommonHelper.transUrlChar("a%/b&c+d e?f#g=");
    // System.out.println(a);
    // }
}
