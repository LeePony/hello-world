package com.ztesoft.zsmart.bss.selfcare.service;

import java.sql.Date;
import java.util.List;

import com.ztesoft.zsmart.bss.common.config.bll.ConfigItemCache;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;

/**
 * <Description> <br>
 * 
 * @author yao.yueqing<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月14日 <br>
 * @since CRM8.1 <br>
 * @see com.ztesoft.zsmart.bss.selfcare.service <br>
 */
public final class SubsServices {

    /**
     * logger <br>
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(SubsServices.class);
    
    /**
     * contactChannelId <br>
     */
    public static Long contactChannelId = ConfigItemCache.instance().getLong(
        "CUSTOMER_CARE/ATHENA_CC_PUBLIC/SELFCARE_CONTACT_CHANNEL_ID");
    
    /**
     * defFellowNbrTypeId <br>
     */
    public static Long defFellowNbrTypeId = ConfigItemCache.instance().getLong(
        "CUSTOMER_CARE/ATHENA_CC_PUBLIC/SELFCARE_FELLOW_NBR_TYPE_ID");

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param prefix
     * @param accNbr
     * @return DynamicDict
     * @throws BaseAppException <br>
     */
    public static DynamicDict qrySubsByAccNbr(String prefix, String accNbr) throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        DynamicDict result = null;
        if (StringUtil.isNotEmpty(prefix)) {
            dict.set("PREFIX", prefix);
        }
        dict.set("ACC_NBR", accNbr);
        dict.serviceName = "AdapterQrySubsList";
        ServiceFlow.callService(dict);

        List<DynamicDict> subsList = dict.getList("SUBS_LIST");
        if (subsList != null && !subsList.isEmpty()) {
            result = subsList.get(0);
        }
        return result;
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param subsId <br>
     * @param subsQueryFlag <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    public static DynamicDict qrySubsDetail(Long subsId, DynamicDict subsQueryFlag) throws BaseAppException {

        logger.debug("Begin to qrySubsInfo subsId = {[]}", subsId);

        DynamicDict dict = new DynamicDict();
        dict.set("SUBS_ID", subsId);
        dict.serviceName = "QrySubsDetail";

        // 如果没有传入查询条件则会查询订户所有信息，效率较低，慎用
        if (subsQueryFlag == null) {
            subsQueryFlag = new DynamicDict();
            subsQueryFlag.set("QRY_CUST", true);
            subsQueryFlag.set("QRY_USER", true);
            subsQueryFlag.set("QRY_ACCT", true);
            subsQueryFlag.set("QRY_OFFER", true);
            subsQueryFlag.set("QRY_ORG", true);
            subsQueryFlag.set("QRY_INDEP_PROD", true);
            subsQueryFlag.set("QRY_SUBS_PLAN", true);
            subsQueryFlag.set("QRY_CATG", true);
            subsQueryFlag.set("QRY_SUBS_SPECIAL_GROUP", true);
            subsQueryFlag.set("QRY_SUBS_UPP_INST", true);
            subsQueryFlag.set("QRY_DEPEND_PROD", true);
            subsQueryFlag.set("QRY_PROD_NEXT_STATE", true);
            subsQueryFlag.set("QRY_ORDER_ITEM", true);
            subsQueryFlag.set("INCLUDE_TERMINATION", true);
            subsQueryFlag.set("QRY_SUBS_PLAN_ATTR_XML", true);
        }
        dict.set("SUBS_QUERY_FLAG", subsQueryFlag);

        ServiceFlow.callService(dict);

        return dict;
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param prefix <br>
     * @param accNbr <br>
     * @throws BaseAppException <br>
     */
    public static void simCardLost(String prefix, String accNbr) throws BaseAppException {
        logger.debug("Begin to simCardLost prefix = {[]}, accNbr= {[]}", prefix, accNbr);

        DynamicDict dict = new DynamicDict();
        dict.set("PREFIX", prefix);
        dict.set("ACC_NBR", accNbr);
        dict.set("CONTACT_CHANNEL_ID", contactChannelId);
        dict.serviceName = "WSLost";
        ServiceFlow.callService(dict);

        logger.debug("End to simCardLost prefix = {[]}, accNbr= {[]}", prefix, accNbr);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param prefix <br>
     * @param accNbr <br>
     * @throws BaseAppException <br>
     */
    public static void simCardRestore(String prefix, String accNbr) throws BaseAppException {
        logger.debug("Begin to simCardRestore prefix = {[]}, accNbr= {[]}", prefix, accNbr);

        DynamicDict dict = new DynamicDict();
        dict.set("PREFIX", prefix);
        dict.set("ACC_NBR", accNbr);
        dict.set("CONTACT_CHANNEL_ID", contactChannelId);
        dict.serviceName = "WSRestore";
        ServiceFlow.callService(dict);

        logger.debug("End to simCardRestore prefix = {[]}, accNbr= {[]}", prefix, accNbr);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param subsId <br>
     * @param fellowNbr <br>
     * @param fellowNbrTypeId <br>
     * @param effDate <br>
     * @param expDate <br>
     * @throws BaseAppException <br>
     */
    public static void addFellowNbr(Long subsId, String fellowNbr, Long fellowNbrTypeId, Date effDate, Date expDate)
        throws BaseAppException {
        logger.debug("Begin to addFellowNbr subsId = {[]}, fellowNbr= {[]}", subsId, fellowNbr);

        if (fellowNbrTypeId == null) {
            // TODO 获取默认值
            fellowNbrTypeId = defFellowNbrTypeId;
        }

        DynamicDict dict = new DynamicDict();
        dict.set("SUBS_ID", subsId);
        dict.set("FELLOW_NBR", fellowNbr);
        dict.set("FELLOW_NBR_TYPE_ID", fellowNbrTypeId);
        dict.set("EFF_DATE", effDate);
        dict.set("EXP_DATE", expDate);
        dict.set("OPERATION_TYPE", "A");
        dict.set("CONTACT_CHANNEL_ID", contactChannelId);
        dict.serviceName = "FellowNbrManager{PN}Athena";
        ServiceFlow.callService(dict);

        logger.debug("End to addFellowNbr subsId = {[]}, fellowNbr= {[]}", subsId, fellowNbr);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param subsId <br>
     * @param fellowNbr <br>
     * @param fellowNbrTypeId <br>
     * @throws BaseAppException <br>
     */
    public static void delFellowNbr(Long subsId, String fellowNbr, Long fellowNbrTypeId) throws BaseAppException {
        logger.debug("Begin to delFellowNbr subsId = {[]}, fellowNbr= {[]}", subsId, fellowNbr);

        DynamicDict dict = new DynamicDict();
        dict.set("SUBS_ID", subsId);
        dict.set("FELLOW_NBR", fellowNbr);
        dict.set("FELLOW_NBR_TYPE_ID", fellowNbrTypeId);
        dict.set("OPERATION_TYPE", "D");
        dict.set("CONTACT_CHANNEL_ID", contactChannelId);
        dict.serviceName = "FellowNbrManager{PN}Athena";
        ServiceFlow.callService(dict);

        logger.debug("End to delFellowNbr subsId = {[]}, fellowNbr= {[]}", subsId, fellowNbr);
    }
}
