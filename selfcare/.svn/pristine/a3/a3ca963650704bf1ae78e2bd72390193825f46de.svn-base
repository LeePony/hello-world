package com.ztesoft.zsmart.bss.selfcare.controller;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ztesoft.zsmart.bss.bizcommon.adapter.helper.SubsHelper;
import com.ztesoft.zsmart.bss.cc.abe.profile.Subs;
import com.ztesoft.zsmart.bss.common.config.bll.ConfigItemCache;
import com.ztesoft.zsmart.bss.selfcare.bll.BillingBizManager;
import com.ztesoft.zsmart.bss.selfcare.common.helper.CurrencyHelper;
import com.ztesoft.zsmart.bss.selfcare.common.util.MvcBoUtil;
import com.ztesoft.zsmart.bss.selfcare.model.SessionDto;
import com.ztesoft.zsmart.bss.selfcare.service.BalanceService;
import com.ztesoft.zsmart.bss.selfcare.service.SubsServices;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.core.utils.AssertUtil;
import com.ztesoft.zsmart.core.utils.DateUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.jdbc.qdb.util.StringUtil;
import com.ztesoft.zsmart.web.resource.Common;


/** 
 * <Description> <br> 
 *  
 * @author yuan.zhilang<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月28日 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.selfcare.controller <br>
 */
@Controller
public class BalanceController {
    
    /**
     * logger <br>
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(BalanceController.class);
    
    /**
     * precisionNo <br>
     */
    private Long precisionNo = null;
    
    /**
     * Description: <br> 
     *  
     * @author yuan.zhilang<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */ 
    @RequestMapping("QryLoanBal")
    @ResponseBody
    public JSONObject qryLoanBalconfig(HttpServletRequest request) throws BaseAppException {
        String loanAmount = ConfigItemCache.instance().getString(
            "CUSTOMER_CARE/ATHENA_CC_PUBLIC/SELFCARE_LOAN_CHARGE_AMOUNT");
        logger.debug("CUSTOMER_CARE/ATHENA_CC_PUBLIC/SELFCARE_LOAN_CHARGE_AMOUNT value={}", loanAmount);
        
        if (loanAmount == null || StringUtil.isEmpty(loanAmount)) {
            loanAmount = "0";
        }

       
//        getPrecisionNo();
//        if (precisionNo.intValue() > 0) {
//            String precisionStr = ".";
//            for (int i = 0; i < precisionNo.intValue(); i++) {
//                precisionStr += "0";
//            }
//           
//            String[] loanAmountArr = loanAmount.split(",");
//            loanAmount = "";
//            for (String loanAmountStr : loanAmountArr) {
//                if (loanAmountStr.indexOf(".") == -1) {
//                    loanAmountStr += precisionStr;
//                }
//                loanAmount += loanAmountStr + ",";
//            }
//            if (loanAmount.endsWith(",")) {
//                loanAmount = loanAmount.substring(0, loanAmount.length() - 1);
//            }
//            
//        }
        
        DynamicDict dict = new DynamicDict();
        dict.set("LOAN_AMOUNT", loanAmount);
        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }

    /**
     * Description: <br> 
     *  
     * @author zhang.liang<br>
     * @taskId <br>
     * @throws BaseAppException <br>
     */ 
    private Long getPrecisionNo() throws BaseAppException {
        if (precisionNo == null) {
            DynamicDict precisionDict = new DynamicDict();
            precisionDict.setServiceName("GetSystemParamService{PN}Athena");
            precisionDict.set("MASK", "CURRENCY_PRECISION");
            ServiceFlow.callService(precisionDict);
            precisionNo = precisionDict.getLong("CURRENT_VALUE");
        }
        logger.debug("Current precision is precisionNo={}", precisionNo);
        return precisionNo;
    }
    /**
     * Description: <br> 
     *  
     * @author yuan.zhilang <br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */ 
    @RequestMapping("LoanBalance")
    @ResponseBody
    public JSONObject loanBalance(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        String loanAmount = dict.getString("LOAN_AMOUNT");
        AssertUtil.isNotNull(loanAmount, "LOAN_AMOUNT is null");
        
        long amount = Long.parseLong(loanAmount);
        precisionNo = getPrecisionNo();
        for (int i = 0; i < precisionNo.intValue(); i++) {
            amount *= 10;
        }
        
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long subsId = session.getSubsId();

        logger.debug("Begin LoanBalance dict = [{}]", dict.asXML());

        Subs subs = SubsHelper.qrySubsBySubsId(subsId);

        if (subs == null) {
            // 订户不存在
            ExceptionHandler.publish("CC-S-SALES-00001", ExceptionHandler.BUSS_ERROR);
        }

        String accNbr = subs.getAccNbr();
        String prefix = subs.getPrefix();
        
        DynamicDict loanBalDict = new DynamicDict();
        loanBalDict.set("MSISDN", prefix + accNbr);
        loanBalDict.set("AMOUNT", amount);
        BalanceService.loanBal(loanBalDict);
        loanBalDict.set("COMMENTS", "MSISDN=" + prefix + accNbr + "|AMOUNT=" + amount);
        loanBalDict.set("SUBS_ID", subs.getSpId());
        loanBalDict.set("TRANSACTION_ID", session.getSelfSessionId());
        loanBalDict.set("SUBS_ID", subs.getSubsId());
        loanBalDict.set("SP_ID", subs.getSpId());
        loanBalDict.set("OPER_TYPE", "C");
        if (StringUtil.isEmpty(loanBalDict.getString("ERROR_CODE")) && StringUtil.isEmpty(loanBalDict.getString("ERROR_MESSAGE"))) {
        	loanBalDict.set("STATE", "S");
        }
        else {
        	loanBalDict.set("STATE", "F");
        }
        new BillingBizManager().addSelfOperLog(loanBalDict);

        logger.debug("End LoanBalance dict = [{}]", dict.asXML());

        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }
    
    /**
     * Description: 查询贷款历史<br> 
     *  
     * @author yuan.zhilang<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */ 
    @RequestMapping("QryLoanBalHis")
    @ResponseBody
    public JSONObject qryLoanBalHis(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long subsId = session.getSubsId();

        logger.debug("Begin LoanBalance dict = [{}]", dict.asXML());

        DynamicDict qryFlag = new DynamicDict();
        qryFlag.set("INCLUDE_TERMINATION", false);
        DynamicDict subsDict = SubsServices.qrySubsDetail(subsId, qryFlag);
        if (subsDict == null || subsDict.getBO("SUBS") == null) {
            // 订户不存在
            ExceptionHandler.publish("CC-S-SALES-00001", ExceptionHandler.BUSS_ERROR);
        }
        
        String accNbr = subsDict.getBO("SUBS").getBO("SUBS_BASE_DETAIL").getString("ACC_NBR");
        String prefix = subsDict.getBO("SUBS").getBO("SUBS_BASE_DETAIL").getString("PREFIX");
        Long acctId = subsDict.getBO("SUBS").getBO("SUBS_BASE_DETAIL").getLong("ACCT_ID");
        
        DynamicDict loanBalDict = new DynamicDict();
        loanBalDict.set("MSISDN", prefix + accNbr);
        loanBalDict.set("ACCOUNT_CODE", acctId);
        
        Date beginDate = null;
//        String endDate = "";
//        Calendar calendar = Calendar.getInstance();
        Date endDate = DateUtil.GetDBDateTime();
        Long type = dict.getLong("SEARCH_TYPE");
        if (3 == type.intValue()) {
            beginDate = DateUtil.offsetMonth(endDate, -3);
//            calendar.add(Calendar.MONTH, -3);
//            calendar.set(Calendar.DAY_OF_MONTH, 1);
//            beginDate = DateUtil.date2String(calendar.getTime(), DateUtil.DATE_FORMAT_1);
//            endDate = DateUtil.offsetDay(DateUtil.getNowDate(), 1).toString();
        }
        else if (6 == type.intValue()) {
            beginDate = DateUtil.offsetMonth(endDate, -6);
//            calendar.add(Calendar.MONTH, -6);
//            calendar.set(Calendar.DAY_OF_MONTH, 1);
//            beginDate = DateUtil.date2String(calendar.getTime(), DateUtil.DATE_FORMAT_1);
//            endDate = DateUtil.offsetDay(DateUtil.getNowDate(), 1).toString();
        }
        else if (0 == type.intValue()) {
            Date longestDate = DateUtil.offsetMonth(endDate, -6);
            beginDate = dict.getDate("START_DATE");
            if (beginDate.getTime() - longestDate.getTime() < 0) {
                beginDate = longestDate;
            }
//            endDate = DateUtil.date2String(dict.getDate("END_DATE"), DateUtil.DATE_FORMAT_1);
            endDate = (Date) DateUtil.offsetDay(DateUtil.dateToSqlDate(dict.getDate("END_DATE")), 1);
        }
        loanBalDict.set("START_DATE", beginDate);
        loanBalDict.set("END_DATE", endDate);
        logger.debug("The search  into beginDate = {} and endDate= {}.", beginDate, endDate);
        //暂时不查历史
        BalanceService.qryLoanBalHis(loanBalDict);
        precisionNo = getPrecisionNo();
        List<DynamicDict> loanBalHisList = loanBalDict.getList("LOAN_HIS_LIST");
        if (loanBalHisList != null && !loanBalHisList.isEmpty()) {
            for (DynamicDict loanBalHis : loanBalHisList) {
                long loanAmount = loanBalHis.getLong("LOAN_AMOUNT")==null?0:loanBalHis.getLong("LOAN_AMOUNT").longValue()*-1;
                long charge = loanBalHis.getLong("COMMISSION")==null?0:loanBalHis.getLong("COMMISSION").longValue()*-1;
                if (loanAmount > 0 && charge == 0) {
                    Integer per = ConfigItemCache.instance().getInt("CUSTOMER_CARE/ATHENA_CC_PUBLIC/SELFCARE_LOANBALHIS_CHARGE", 53);
                    charge = per;
                }
                loanBalHis.set("LOAN_AMOUNT",CurrencyHelper.toFloatSum(String.valueOf(loanAmount)));
                loanBalHis.set("COMMISSION", CurrencyHelper.toFloatSum(String.valueOf(charge)));
            }
        }
        dict.set("LOAN_BAL_LIST", loanBalHisList);
        logger.debug("search loan bal his, LOAN_BAL_LIST={}", loanBalHisList);
        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param request
     * @return JSONObject
     * @throws BaseAppException  <br>
     */
    @RequestMapping("qryTransferCharge")
    @ResponseBody
    public JSONObject qryTransferCharge(HttpServletRequest request) throws BaseAppException {
        DynamicDict reqDict = MvcBoUtil.requestToBO(request);
        Long charge = reqDict.getLong("CHARGE");
        Double dc = Double.valueOf(charge.toString());
        Integer per = ConfigItemCache.instance().getInt("CUSTOMER_CARE/ATHENA_CC_PUBLIC/SELFCARE_TRANSFER_RATE");
        JSONObject result = new JSONObject();
        result.put("CHARGE", Math.floor(dc*per/100000));
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
    @RequestMapping("QryLoanCharge")
    @ResponseBody
    public JSONObject QryLoanCharge(HttpServletRequest request) throws BaseAppException {
        DynamicDict reqDict = MvcBoUtil.requestToBO(request);
        Long charge = reqDict.getLong("LOAN_AMOUNT");
        Double dc = Double.valueOf(charge.toString());
        Integer per = ConfigItemCache.instance().getInt("CUSTOMER_CARE/ATHENA_CC_PUBLIC/SELFCARE_LOAN_RATE");
        JSONObject result = new JSONObject();
        result.put("CHARGE", Math.round(dc*per/100000));
        return result;
    }
}
