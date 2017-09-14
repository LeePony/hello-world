package com.ztesoft.zsmart.bss.selfcare.service;

import java.sql.Date;

import com.ztesoft.zsmart.bss.common.config.bll.ConfigItemCache;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.core.utils.DateUtil;
import com.ztesoft.zsmart.core.utils.StringUtil;

/**
 * <Description> <br>
 * 
 * @author zhang.liang<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月17日 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.selfcare.service <br>
 */
public class BillingService {

    /**
     * contactChannelId <br>
     */
    private static Long contactChannelId = ConfigItemCache.instance().getLong(
        "CUSTOMER_CARE/ATHENA_CC_PUBLIC/SELFCARE_CONTACT_CHANNEL_ID");

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param acctId <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    public static DynamicDict qryDefaultBal(Long acctId) throws BaseAppException {

        DynamicDict dict = new DynamicDict();
        dict.serviceName = "QryDefaultBal{PN}Athena";
        dict.set("ACCT_ID", acctId);
        ServiceFlow.callService(dict);

        return dict;
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param msisdn <br>
     * @param vcCard <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    public static DynamicDict vcCharge(String msisdn, String vcCard) throws BaseAppException {
    	DynamicDict dict = new DynamicDict();
        dict.serviceName = "CallOcsService{PN}Athena";
        dict.set("MSISDN", msisdn);
        dict.set("VOUCHER_CARD", vcCard);
        dict.set("PAYMENT_METHOD_ID", 4);
        dict.set("CONTACT_CHANNEL_ID", "18");
        // dict.set("OCS_SERVICES_NAME", "WebVcRecharge");
        dict.set("OCS_SERVICES_NAME", "SelfVcRecharge{PN}Athena");
        String comments = "MSISDN=" + msisdn + "|VOUCHER_CARD=" + vcCard + "|PAYMENT_METHOD_ID=4|CONTACT_CHANNEL_ID=" + contactChannelId;
        dict.set("COMMENTS", comments);
        ServiceFlow.callService(dict);
        return dict;
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param acctIds <br>
     * @param beginDate <br>
     * @param endDate <br>
     * @param page <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    public static DynamicDict qryPaymentHis(Long acctId, Date beginDate, Date endDate, DynamicDict page)
        throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.serviceName = "CallOcsService{PN}Athena";
        dict.set("TRADE_BEGIN_TIME", DateUtil.date2String(beginDate, DateUtil.DATETIME_FORMAT_1));
        dict.set("TRADE_END_TIME", DateUtil.date2String(endDate, DateUtil.DATETIME_FORMAT_1));
        dict.set("ACCT_ID", acctId);
        dict.set("TRADE_TYPE_NORMAL", 1);
        if (page != null) {
            dict.set("zsmart_query_page", page);
        }
        dict.set("OCS_SERVICES_NAME", "QryAllPaymentOrderByDate{PN}Athena");
        ServiceFlow.callService(dict);

        return dict;
    }
    
    /**
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param acctId <br>
     * @return Acct <br>
     * @throws BaseAppException  <br>
     */ 
    
    
    public static DynamicDict qryAcctByAcctId(Long acctId) throws BaseAppException {
        DynamicDict acctDict = new DynamicDict();
        acctDict.set("ACCT_ID", acctId);
        acctDict.set("ACCT_FLAG", "Y");
        acctDict.setServiceName("AdapterQryAcctDetail");
        ServiceFlow.callService(acctDict);
        return acctDict.getBO("ACCT");
    }
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param acctId
     * @return
     * @throws BaseAppException  <br>
     */
    public static DynamicDict qryBalListFilterExpire(Long acctId) throws BaseAppException{
        DynamicDict dict = new DynamicDict();
        dict.serviceName = "CallOcsService{PN}Athena";
        dict.set("OCS_SERVICES_NAME", "QryBalListFilterExpire{PRJ}Athena");
        dict.set("ACCT_ID", acctId);       
        ServiceFlow.callService(dict);
        return dict;
    }
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param accNbr
     * @return
     * @throws BaseAppException  <br>
     */
    public static DynamicDict queryCurCharge(String prefix,String accNbr) throws BaseAppException{
        DynamicDict dict = new DynamicDict();
        dict.serviceName = "QueryCurCharge{PN}Athena";
        dict.set("MSISDN", prefix+accNbr);       
        ServiceFlow.callService(dict);
//        dict.set("EventCharge", 1000);
//        dict.set("RecurringCharge", 1000);
        return dict;
    }
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param accNbr
     * @return
     * @throws BaseAppException  <br>
     */
    public static DynamicDict queryAccount(String prefix,String accNbr,String acctCode) throws BaseAppException{
        DynamicDict dict = new DynamicDict();
        
        dict.serviceName = "QueryAccount{PN}Athena";
        if(StringUtil.isNotEmpty(acctCode)){
            dict.set("ACCOUNT_CODE", acctCode);
        }else{
            dict.set("MSISDN", prefix+accNbr);
        }
        
        
        ServiceFlow.callService(dict);

        /*List<DynamicDict> arr = new ArrayList<DynamicDict> ();
        DynamicDict tmp = new DynamicDict();
        tmp.set("RealBal", 1000);
        tmp.set("ConsumeBal", 2000);
        tmp.set("BalType", 2);
        tmp.set("UNIT", "KB");
        tmp.set("AcctResCode", "DATA1");
        arr.add(tmp);
        
        DynamicDict tmp1 = new DynamicDict();
        tmp1.set("RealBal", 1024);
        tmp1.set("ConsumeBal", 2048);
        tmp1.set("BalType", 2);
        tmp1.set("UNIT", "KB");
        tmp1.set("AcctResCode", "DATA2");
        arr.add(tmp1);
        
        
        DynamicDict tmp5 = new DynamicDict();
        tmp5.set("RealBal", 1024);
        tmp5.set("ConsumeBal", 2048);
        tmp5.set("BalType", 2);
        tmp5.set("UNIT", "KB");
        tmp5.set("AcctResCode", "DATA2");
        
        arr.add(tmp5);


        
        DynamicDict tmp2 = new DynamicDict();
        tmp2.set("RealBal", 10);
        tmp2.set("ConsumeBal", 200);
        tmp2.set("BalType", 2);
        tmp2.set("UNIT", "OTHER");
        tmp2.set("AcctResCode", "OTHER");
        arr.add(tmp2);
        
        DynamicDict tmp3 = new DynamicDict();
        tmp3.set("RealBal", 60);
        tmp3.set("ConsumeBal",360);
        tmp3.set("BalType", 2);
        tmp3.set("UNIT", "S");
        tmp3.set("AcctResCode", "VOICE");
        arr.add(tmp3);
        
        DynamicDict tmp4 = new DynamicDict();
        tmp4.set("RealBal", 60);
        tmp4.set("ConsumeBal",360);
        tmp4.set("BalType", 1);
        tmp4.set("UNIT", "OTHER");
        tmp4.set("AcctResCode", "1");
        arr.add(tmp4);
        
        dict.set("BalDtoList", arr);
        dict.set("AccountCode", "1111");
        dict.set("PostPaidFlag", "N");
        dict.set("Advance", "200");
        dict.set("Consumed", "100");*/
        
        
        return dict;
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param msisdn
     * @param type
     * @param startDate
     * @param endDate
     * @return
     * @throws BaseAppException  <br>
     */
    public static DynamicDict qyrCdr(String msisdn,String type,Date startDate,Date endDate) throws BaseAppException{
        
        DynamicDict dict = new DynamicDict();
        dict.set("msisdn", msisdn);
        dict.set("type", type);
        dict.set("START_TIME", DateUtil.date2String(startDate,DateUtil.DATE_FORMAT_1));
        dict.set("END_TIME", DateUtil.date2String(endDate,DateUtil.DATE_FORMAT_1));
        dict.serviceName = "QryCdr{PN}Athena";
        ServiceFlow.callService(dict);
        
        /*ArrayList list = new ArrayList();
        DynamicDict data = new DynamicDict();
        data.set("EVENT", 1);
        data.set("RATING_NUMBER", 2);
        data.set("DURATION", 3);
        data.set("STARTTIME", "2016-10-01");
        data.set("PACKAGENAME1", 1);
        data.set("CHARGE1", 1);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        
        list.add(data);
        
        dict.set("CNT", 30);
        dict.set("CDR_LIST",list);*/
        return dict;
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param accountCode
     * @param msisdn
     * @param startDate
     * @param endDate
     * @return
     * @throws BaseAppException  <br>
     */
    public static DynamicDict qryHisBill(String accountCode,String msisdn,Date startDate,Date endDate) throws BaseAppException{
        DynamicDict dict = new DynamicDict();
        dict.serviceName = "QryHisBill{PN}Athena";
        if(StringUtil.isNotEmpty(accountCode)){
            dict.set("ACCOUNT_CODE", accountCode);
        }else{
            dict.set("MSISDN", msisdn);
        }
        
        
        dict.set("START_TIME", DateUtil.date2String(startDate, DateUtil.DATE_FORMAT_1));
        dict.set("END_TIME", DateUtil.date2String(endDate, DateUtil.DATE_FORMAT_1));
        ServiceFlow.callService(dict);
        return dict;
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param accountCode
     * @param msisdn
     * @param billId
     * @return
     * @throws BaseAppException  <br>
     */
    public static DynamicDict qryBillAcctItem(String accountCode,String msisdn,Long billId) throws BaseAppException{
        DynamicDict dict = new DynamicDict();
        dict.serviceName = "QryBillAcctItem{PN}Athena";
        if(StringUtil.isNotEmpty(accountCode)){
            dict.set("ACCOUNT_CODE", accountCode);
        }else{
            dict.set("MSISDN", msisdn);
        }
        dict.set("BILL_ID", billId);
        ServiceFlow.callService(dict);
        return dict;
    }
}
