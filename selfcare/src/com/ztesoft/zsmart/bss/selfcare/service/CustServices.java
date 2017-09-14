package com.ztesoft.zsmart.bss.selfcare.service;

import java.util.List;


import com.ztesoft.zsmart.bss.selfcare.common.helper.BoHelper;
import com.ztesoft.zsmart.bss.selfcare.model.CertTypeDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;


/**
 * 
 * <Description> <br> 
 *  
 * @author yao.yueqing<br>
 * @version 1.0<br>
 * @taskId  <br>
 * @CreateDate 2016年6月7日 <br>
 * @since CRM8.1 <br>
 * @see com.ztesoft.zsmart.bss.selfcare.service <br>
 */
public final class CustServices {

    /**
     * logger
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(CustServices.class);
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
    public static DynamicDict qryCustById(Long custId) throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.serviceName = "AdapterQryCustInfo";
        dict.set("CUST_ID", custId);
        ServiceFlow.callService(dict);
        return dict.getBO("CUST");
    }

    /**
     * Description: <br> 
     *  
     * @author  <br>
     * @taskId <br>
     * @param custId 
     * @param certId 
     * @return DynamicDict 
     * @throws BaseAppException <br>
     */ 
    public static DynamicDict qryCertById(Long custId, Long certId) throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.serviceName = "AdapterQryCert";
        dict.set("CUST_ID", custId);
        dict.set("CERT_ID", certId);
        ServiceFlow.callService(dict);
        return dict.getBO("CERT");
        
    }
    
    public static DynamicDict qryCustType(String custType) throws BaseAppException{
        DynamicDict dict = new DynamicDict();
        dict.setServiceName("AdapterQryCustType");
        dict.set("CUST_TYPE", custType);
        ServiceFlow.callService(dict);
        List<DynamicDict> custTypeDtoList = dict.getList("DATA_VALUE_LIST");
        DynamicDict returnDict =null;
        if(custTypeDtoList!=null&&!custTypeDtoList.isEmpty()){
            returnDict = custTypeDtoList.get(0);
        }
        return returnDict;
    }
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param accNbr 
     * @return DynamicDict 
     * @throws BaseAppException  <br>
     */
    public static DynamicDict qryCustByAccNbr(String accNbr) throws BaseAppException {
        DynamicDict subsDict = SubsServices.qrySubsByAccNbr(null, accNbr);
        if (subsDict != null && subsDict.getLong("CUST_ID") != null) {
            Long custId = subsDict.getLong("CUST_ID");
            DynamicDict custDict = qryCustById(custId);
            return custDict;
        }
        return null;
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
    public static void modCust(DynamicDict dict) throws BaseAppException {
        DynamicDict modCustDict = new  DynamicDict();
//        if (StringUtil.isNotEmpty(dict.getString("CUST_CODE"))) {
//            modCustDict.set("CUST_CODE", dict.getString("CUST_CODE"));
//        }
//        if (StringUtil.isNotEmpty(dict.getString("PWD"))) {
//            modCustDict.set("PWD", dict.getString("PWD"));
//        }
//        if(StringUtil.isNotEmpty(dict.getString("EMAIL"))){
//            modCustDict.set("EMAIL", dict.getString("EMAIL"));
//        }
//        if(StringUtil.isNotEmpty(dict.getString("ADDRESS"))){
//            modCustDict.set("ADDRESS", dict.getString("ADDRESS"));
//        }
//        if(StringUtil.isNotEmpty(dict.getString("CONTACT_NUMBER"))){
//            modCustDict.set("PHONE_NUMBER", dict.getString("CONTACT_NUMBER"));
//        }
//        if(StringUtil.isNotEmpty(dict.getString("OCCUPATION"))){
//            modCustDict.set("OCCUPATION_ID", dict.getString("OCCUPATION"));
//        }
        modCustDict.valueMap = dict.valueMap;
        modCustDict.serviceName = "ModCust{PN}Athena";

        ServiceFlow.callService(modCustDict);
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yu.anjin<br>
     * @taskId <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    public static List<CertTypeDto> qryAllCertType() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.setServiceName("AdapterQryCertType");
        ServiceFlow.callService(dict);
        logger.info("After call AdapterQryCertType:[{}]", dict.asXML("UTF-8"));
        List<CertTypeDto> certTypeDtoList = BoHelper.bo2ListDto(dict, "DATA_VALUE_LIST", CertTypeDto.class);
        logger.info("certTypeDtoList:[{}]", certTypeDtoList.toString());
        return certTypeDtoList;
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yu.anjin<br>
     * @taskId <br>
     * @param occupationDto <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @SuppressWarnings("unchecked")
    public static List<DynamicDict> qryOccupation() throws BaseAppException {

        DynamicDict dict = new DynamicDict();
        dict.set("ORDER_BY", "OCCUPATION_NAME");
        dict.setServiceName("AdapterQryOccupation");
        ServiceFlow.callService(dict);
        return dict.getList("DATA_VALUE_LIST");
    }
    
    
    public static List<DynamicDict> qryContactHistory(DynamicDict dict) throws BaseAppException{
        dict.serviceName = "QryCustContact{PN}Athena";
        ServiceFlow.callService(dict);
        return dict.getList("z_d_r");
    }
    
}
