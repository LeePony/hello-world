/**************************************************************************************** 
 Copyright © 2003-2013 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.ztesoft.zsmart.bss.selfcare.common.core.api;


/**
 * <Description> 常量定义<br>
 * 
 * @author Xu.fuqin<br>
 * @version 1.0<br>
 * @taskId 212688<br>
 * @CreateDate 2013-1-10 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.esc.common.bll <br>
 */
public class ActionDomain {

    /**
     * PARTY_TYPE_F 标记为运营商员工<br>
     */
    public static final String PARTY_TYPE_F = "F";

    /**
     * PARTY_TYPE_G 标记为企业客户<br>
     */
    public static final String PARTY_TYPE_G = "G";

    /**
     * USER_CUST_ID:当前登陆的用户所属的企业用户ID，如果是operator内部员工登陆，该信息为空 <br>
     */
    public static final String USER_CUST_ID = "CUST_ID";

    /**
     * USER_ROLE_ID:当前登陆的用户所用的角色ID <br>
     */
    public static final String USER_ROLE_ID = "ROLE_ID";

    /**
     * USER_ROLE_CODE:当前登陆的用户所用的角色编码 <br>
     */
    public static final String USER_ROLE_CODE = "ROLE_CODE";

    /**
     * USER_ID:当前登陆的USER_ID，如果是operator内部员工，保存为STAFF_ID <br>
     */
    public static final String USER_ID = "USER_ID";

    /**
     * USER_HQ_FLAG:当前登陆的用户的总公司标志 <br>
     */
    public static final String USER_HQ_FLAG = "HQ_FLAG";

    /**
     * USER_CUST_GROUP_ID:当前登陆的用户所属的cust group id <br>
     */
    public static final String USER_CUST_GROUP_ID = "CUST_GROUP_ID";

    /**
     * SRC_IP:当前登陆的客户端ip <br>
     */
    public static final String SRC_IP = "SRC_IP";

    /**
     * PASSPORT_ID:登陆报表服务器的passport <br>
     */
    public static final String PASSPORT_ID = "PASSPORT_ID";

    /**
     * LOGIN_PAGE:登陆途径，从sso登陆的，还是从自服务登陆的 <br>
     */
    public static final String LOGIN_PAGE = "LOGIN_PAGE";

    /**
     * 超级管理员 <br>
     */
    public static final String SUPER_CUST_ADMIN = "SUPER_CUST_ADMIN";

    /**
     * 获取当前MENU_ID
     */
    public static final String CURRENT_MENU_ID = "CURRENT_MENU_ID";

    /**
     * 获取授权后的MENU
     */
    public static final String ESC_AUTH_MENUS = "ESC_AUTH_MENUS";

    /**
     * SSO中的USER_ID
     */
    public static final String SSO_USER_ID = "SSO_USER_ID";

    /**
     * 由SSO返回的 SessionId
     */
    public static final String SESSION_ID = "SESSION_ID";

    /**
     * 下载文件路径
     */
    public static final String DOWNLOAD_FILEPATH = "DOWNLOAD_FILEPATH";

    /**
     * 下载文件名
     */
    public static final String DOWNLOAD_FILENAME = "DOWNLOAD_FILENAME";

    /**
     * 下载文件全路径
     */
    public static final String DOWNLOAD_FULLFILENAME = "DOWNLOAD_FULLFILENAME";

    /**
     * 下载文件编码
     */
    public static final String DOWNLOAD_FILE_CHARSET = "DOWNLOAD_FILE_CHARSET";

    /**
     * cvbsAppId cvbs系统的app_id，定义在web.xml里面 <br>
     * ?此处有疑问，应该是默认为1，跟web.xml中的不是一个意思
     */
    public static Long cvbsAppId = 1L;

    /**
     * g_WebRoot <br>
     */
    public static String gWebRoot = "";

    /**
     * COOKIE_USER_ROLE_CODE_NAME <br>
     */
    public static final String COOKIE_USER_ROLE_CODE_NAME = "COOKIE_USER_ROLE_CODE";
}
