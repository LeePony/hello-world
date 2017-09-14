/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.ztesoft.zsmart.bss.selfcare.common.core.api;

/**
 * <br>
 * 
 * @author <br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2013-7-10 <br>
 * @since V7.3<br>
 * @see com.ztesoft.zsmart.bss.esc.domain.core <br>
 */

public interface CoreDef {

    /**
     * zsmart_session:session信息bo的名字 <br>
     */
    String BO_SESSION = "zsmart_session";

    /**
     * LOGIN_LOG_ID:登陆日志ID <br>
     */
    String LOGIN_LOG_ID = "LOGIN_LOG_ID";

    /**
     * PARTY_TYPE <br>
     */
    String PARTY_TYPE = "PARTY_TYPE";

    /**
     * PARTY_CODE <br>
     */
    String PARTY_CODE = "PARTY_CODE";

    /**
     * OPER_LOG_BO <br>
     */
    String OPER_LOG_BO = "OPER_LOG_BO";

    /**
     * OPER_LOG_BO <br>
     */
    String OPER_LOG_DETAIL_BO_LIST = "OPER_LOG_DETAIL_BO_LIST";

    /**
     * LOGIN_PAGE_SSO:SSO登陆标识 <br>
     */
    public static final String LOGIN_PAGE_SSO = "SSO";

    /**
     * LOGIN_PAGE_SELFSERVICE:自服务登陆标识 <br>
     */
    public static final String LOGIN_PAGE_SELFSERVICE = "SELFSERVICE";

    /**
     * SELF_CONFIG_ITEM_NODE:自服务配置项根节点 <br>
     */
    public static final String SELF_CONFIG_ITEM_NODE = "ESC_PUBLIC";
    
    /**
     * SESSION_USER_UPLOAD_UID <br>
     */
    public static final String SESSION_USER_UPLOAD_UID = "SESSION_USER_UPLOAD_UID";
    
    /**
     * SESSION_USER_UPLOAD_BULK_LOG_ID <br>
     */
    public static final String SESSION_USER_UPLOAD_BULK_LOG_ID = "SESSION_USER_UPLOAD_BULK_LOG_ID";
    
    
    
    

}
