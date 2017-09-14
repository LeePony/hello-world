package com.ztesoft.zsmart.bss.selfcare.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ztesoft.zsmart.bss.bizcommon.adapter.helper.SubsHelper;
import com.ztesoft.zsmart.bss.cc.abe.profile.Subs;
import com.ztesoft.zsmart.bss.common.config.bll.ConfigItemCache;
import com.ztesoft.zsmart.bss.selfcare.bll.BillingBizManager;
import com.ztesoft.zsmart.bss.selfcare.common.cache.SelfCareCache;
import com.ztesoft.zsmart.bss.selfcare.common.helper.CurrencyHelper;
import com.ztesoft.zsmart.bss.selfcare.common.helper.ExportHelper;
import com.ztesoft.zsmart.bss.selfcare.common.util.MvcBoUtil;
import com.ztesoft.zsmart.bss.selfcare.model.SessionDto;
import com.ztesoft.zsmart.bss.selfcare.service.AcctService;
import com.ztesoft.zsmart.bss.selfcare.service.BillingService;
import com.ztesoft.zsmart.bss.selfcare.service.SubsServices;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.core.utils.AssertUtil;
import com.ztesoft.zsmart.core.utils.DateUtil;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.mvc.core.helper.BoUtil;
import com.ztesoft.zsmart.web.resource.Common;

/**
 * <Description> <br>
 * 
 * @author zhang.liang<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月17日 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.selfcare.controller <br>
 */
@Controller
public class BillingController {
    /**
     * logger <br>
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(BillingController.class);

    private static Map<String, String> cdrTypeMap = new HashMap<String, String>();

    static {
        cdrTypeMap.put("0", "2");
        cdrTypeMap.put("1", "1");
        cdrTypeMap.put("2", "3");
        cdrTypeMap.put("3", "0");
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("QryDefaultBal")
    @ResponseBody
    public JSONObject qryDefaultBal(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        logger.debug("Begin qryBalance bo = [{}]", dict.asXML());
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long acctId = session.getAcctId();

        dict = BillingService.qryDefaultBal(acctId);
        Long grossBal = dict.getLong("REAL_BAL");
        if (grossBal == null) {
            grossBal = 0l;
        }
        grossBal = grossBal * -1;
        // 精度处理
        dict.set("GROSS_BAL", CurrencyHelper.toFloatSum(grossBal.toString()));
        dict.set("PAID_FLAG", session.getPaidFlag());

        logger.debug("End qryBalance bo = [{}]", dict.asXML());

        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("VCRecharge")
    @ResponseBody
    public JSONObject vcRecharge(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        AssertUtil.isNotNull(dict.getString("VOUCHER_CARD"), "VOUCHER_CARD is null");
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long subsId = session.getSubsId();

        logger.debug("Begin VCRecharge bo = [{}]", dict.asXML());

        Subs subs = SubsHelper.qrySubsBySubsId(subsId);

        if (subs == null) {
            // 订户不存在
            ExceptionHandler.publish("CC-S-SALES-00001", ExceptionHandler.BUSS_ERROR);
        }

        String accNbr = subs.getAccNbr();
        String prefix = subs.getPrefix();

        try {
            BillingService.vcCharge(prefix + accNbr, dict.getString("VOUCHER_CARD"));
            dict.set("STATE", "S");
        }
        catch (BaseAppException e) {
            logger.error("VCRecharge error", e);
            dict.set("STATE", "F");
            throw e;
        }
        finally {
            dict.set("TRANSACTION_ID", session.getSelfSessionId());
            dict.set("SUBS_ID", subs.getSubsId());
            dict.set("SP_ID", subs.getSpId());
            dict.set("OPER_TYPE", "P");
            new BillingBizManager().addSelfOperLog(dict);
        }

        logger.debug("End VCRecharge bo = [{}]", dict.asXML());

        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("QryPaymentHis")
    @ResponseBody
    public JSONObject qryPaymentHis(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        logger.debug("Begin VCRecharge bo = [{}]", dict.asXML());

        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long acctId = session.getAcctId();
        Date endDate = DateUtil.GetDBDateTime();
        Date beginDate = null;
        if (StringUtil.isNotEmpty(dict.getString("THREE_MONTH")) && dict.getBoolean("THREE_MONTH")) {
            beginDate = DateUtil.offsetMonth(endDate, -3);
        }
        else if (StringUtil.isNotEmpty(dict.getString("SIX_MONTH")) && dict.getBoolean("SIX_MONTH")) {
            beginDate = DateUtil.offsetMonth(endDate, -6);
        }
        else if (StringUtil.isNotEmpty(dict.getString("CUSTOM_TIME")) && dict.getBoolean("CUSTOM_TIME")) {
            beginDate = dict.getDate("BEGIN_DATE");
            endDate = dict.getDate("END_DATE");
            endDate = DateUtil.offsetDay(endDate, 1);
        }
        DynamicDict retDict = new DynamicDict();
        if (beginDate != null && endDate != null) {
            dict = BillingService.qryPaymentHis(acctId, beginDate, endDate, dict.getBO("zsmart_query_page"));
            // 格式化数据
            List<DynamicDict> list = dict.getList("z_d_r");
            Long amount = 0l;
            if (list != null && !list.isEmpty()) {
                if (list.size() == 1 && list.get(0).getLong("CNT") != null) {
                    retDict.set("CNT", list.get(0).getLong("CNT"));
                }
                else {
                    for (DynamicDict item : list) {
                        // 精度处理
                        Long charge = item.getLong("CHARGE");
                        if (charge == null) {
                            charge = 0l;
                        }
                        charge = charge * -1;
                        amount += charge;
                        // 精度处理
                        item.set("CHARGE", CurrencyHelper.toFloatSum(charge.toString()));
                    }
                    retDict.set("PAYMENT_LIST", list);

                }
            }

            retDict.set("AMOUNT", CurrencyHelper.toFloatSum(amount.toString()));
        }

        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(retDict);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("QryTransferCharges")
    @ResponseBody
    public JSONObject qryTransferCharges(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        logger.debug("Begin qryTransferCharges bo = [{}]", dict.asXML());

        // 取配置项
        String charges = ConfigItemCache.instance().getString(
            "CUSTOMER_CARE/ATHENA_CC_PUBLIC/SELFCARE_TRANSFER_CHARGES");

        dict.set("TRANSFER_CHARGES", charges);

        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("QryHisReceverNbr")
    @ResponseBody
    public JSONObject qryHisReceverNbr(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        logger.debug("Begin qryHisReceverNbr bo = [{}]", dict.asXML());
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long subsId = session.getSubsId();
        // 查询转账号码
        // dict.set("TRANSFER_HIS_NBR", "2016042007,2016042010,7777017600");

        List<String> nbrList = new BillingBizManager().qryNearlyNbr(subsId, 4l);
        if (nbrList != null && !nbrList.isEmpty()) {
            String nbrs = "";
            for (String nbr : nbrList) {
                nbrs = nbrs + "," + nbr;
            }
            dict.set("TRANSFER_HIS_NBR", nbrs.substring(1));
        }

        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("TransferCharge")
    @ResponseBody
    public JSONObject transferCharge(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        AssertUtil.isNotNull(dict.getLong("CHARGE"), "CHARGE is null");
        AssertUtil.isNotNull(dict.getString("RECEVE_NBR"), "RECEVE_NBR is null");
        logger.debug("Begin transferCharge bo = [{}]", dict.asXML());

        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long acctId = session.getAcctId();
        Long subsId = session.getSubsId();
        Long charge = dict.getLong("CHARGE");
        String receveNbr = dict.getString("RECEVE_NBR");
        if (StringUtil.isNotEmpty(receveNbr) && receveNbr.startsWith("0")) {
            receveNbr = receveNbr.substring(1);
        }
        if (!StringUtil.isNumeric(receveNbr)) {
            ExceptionHandler.publish("SC_RECEIVER_NUMBER_NOT_DIGITS", ExceptionHandler.BUSS_ERROR);
        }
        else if (receveNbr.length() < 8 || receveNbr.length() > 12) {
            ExceptionHandler.publish("SC_RECEIVER_NUMBER_LENGTH_INVALID", ExceptionHandler.BUSS_ERROR);
        }

        // 校验转正金额不能超过余额
        DynamicDict balDict = BillingService.qryDefaultBal(acctId);
        Long grossBal = balDict.getLong("REAL_BAL");
        if (grossBal == null) {
            grossBal = 0l;
        }
        grossBal = grossBal * -1;
        if (grossBal <= charge) {
            ExceptionHandler.publish("SC_YOUR_BALANCE_NOT_ENOUGH", ExceptionHandler.BUSS_ERROR);
        }

        // 校验金额必须在配置项的范围内
        String charges = ConfigItemCache.instance().getString(
            "CUSTOMER_CARE/ATHENA_CC_PUBLIC/SELFCARE_TRANSFER_CHARGES");

        String chargeArr[] = charges.split(",");
        String max = chargeArr[chargeArr.length - 1];
        String min = chargeArr[0];
        if ("OTHER".equals(max) && chargeArr.length > 1) {
            max = chargeArr[chargeArr.length - 2];
        }
        if (Long.valueOf(min) > charge || Long.valueOf(max) < charge) {
            ExceptionHandler.publish("SC_TRANS_AMOUNT_BETWEEN", ExceptionHandler.BUSS_ERROR, min, max);
        }

        // 校验目标号码是否存在
        DynamicDict recDict = new DynamicDict();
        try {
            recDict.serviceName = "GetPrefixAccNbrService";
            recDict.set("MSISDN", receveNbr);
            ServiceFlow.callService(recDict);
        }
        catch (BaseAppException e) {
            logger.error("Fail to get prefix");
        }
        if (StringUtil.isEmpty(recDict.getString("ACC_NBR"))) {
            recDict.set("ACC_NBR", receveNbr);
        }
        // 查询目标订户
        DynamicDict recSubs = SubsServices.qrySubsByAccNbr(recDict.getString("PREFIX"), recDict.getString("ACC_NBR"));
        if (recSubs == null) {
            // TODO
            ExceptionHandler.publish("SC_RECEIVER_NOT_EXIST", ExceptionHandler.BUSS_ERROR);
        }
        DynamicDict recAcctDict = BillingService.qryAcctByAcctId(recSubs.getLong("ACCT_ID"));
        if (recSubs != null && "Y".equals(recAcctDict.getString("POSTPAID"))) {
            ExceptionHandler.publish("SC_RECEIVER_CAN_NOT_BE_POST", ExceptionHandler.BUSS_ERROR);
        }

        // 调用账务转账服务
        Long contactChannelId = SubsServices.contactChannelId;

        DynamicDict transDict = new DynamicDict();

        transDict.set("SMSISDN", session.getPrefix() + session.getAccNbr());
        transDict.set("DMSISDN", recSubs.getString("PREFIX") + recSubs.getString("ACC_NBR"));
        transDict.set("AMOUNT", charge);
        transDict.set("SP_ID", session.getSpId());
        transDict.set("CHANNEL_ID", contactChannelId);
        // fromRequest.set("CHARGE", UnitHelper.processInputDataUnit(contactChannelId, charge, null));

        // 参数转换在服务配置时已经完成
        transDict.serviceName = "SelfBalTransfer{PN}Athena";

        ServiceFlow.callService(transDict);
        if (transDict != null && StringUtil.isNotEmpty(transDict.getString("ERROR_CODE"))) {
            dict.set("ERROR_CODE", transDict.getString("ERROR_CODE"));
            if (StringUtil.isNotEmpty(transDict.getString("ERROR_MESSAGE"))) {
                dict.set("ERROR_MESSAGE", transDict.getString("ERROR_MESSAGE"));
            }
            else {
                dict.set("ERROR_MESSAGE", "Failed to Transfer Charge.");
            }

        }

        String comments = "SMSISDN=" + session.getPrefix() + session.getAccNbr() + "|DMSISDN="
            + recSubs.getString("PREFIX") + recSubs.getString("ACC_NBR") + "|AMOUNT=" + charge + "|SP_ID="
            + session.getSpId();
        // 记录充值历史
        new BillingBizManager().addTransferHis(subsId, charge, receveNbr, session.getSpId(), comments, transDict);

        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("QryTransferHis")
    @ResponseBody
    public JSONObject qryTransferHis(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        logger.debug("Begin qryTransferHis bo = [{}]", dict.asXML());
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long acctId = session.getAcctId();

        Date endDate = DateUtil.GetDBDateTime();
        Date beginDate = null;
        if (StringUtil.isNotEmpty(dict.getString("THREE_MONTH")) && dict.getBoolean("THREE_MONTH")) {
            beginDate = DateUtil.offsetMonth(endDate, -3);
        }
        else if (StringUtil.isNotEmpty(dict.getString("SIX_MONTH")) && dict.getBoolean("SIX_MONTH")) {
            beginDate = DateUtil.offsetMonth(endDate, -6);
        }
        else if (StringUtil.isNotEmpty(dict.getString("CUSTOM_TIME")) && dict.getBoolean("CUSTOM_TIME")) {
            beginDate = dict.getDate("BEGIN_DATE");
            endDate = dict.getDate("END_DATE");
        }
        DynamicDict retDict = new DynamicDict();
        if (beginDate != null && endDate != null) {
            // 暂时先不处理分页
            retDict.serviceName = "CallOcsService{PN}Athena";
            retDict.set("OCS_SERVICES_NAME", "QryTransferOutAcctBook{PN}Athena");
            retDict.set("ACCT_ID", acctId);
            retDict.set("CREATED_DATE_BEGIN", beginDate.toString());
            retDict.set("CREATED_DATE_END", endDate.toString());
            retDict.set("SP_ID", session.getSpId());
            retDict.set("TRANSFER_OUT", "1");
            retDict.set("TRANSFER_IN", "0");
            ServiceFlow.callService(retDict);
            // 格式化数据
            List<DynamicDict> list = retDict.getList("z_d_r");

            if (list != null && !list.isEmpty()) {

                retDict.set("TRANSFER_LIST", list);
            }
        }

        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(retDict);
    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("qryAcctInfo")
    @ResponseBody
    public JSONObject qryAcctInfo(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);

        // 查询当前用户的所有账户,过滤：如果不是当前用户的账户
        if (!checkAcctIdSelf(session, dict.getLong("ACCT_CODE"))) {
            JSONObject result = new JSONObject();
            result.put("RESULT", -1);
            return result;
        }

        DynamicDict acctDict = BillingService.queryAccount(session.getPrefix(), session.getAccNbr(),
            dict.getString("ACCT_CODE"));

        List<DynamicDict> balList = acctDict.getList("BAL_LIST");
        ArrayList<DynamicDict> newBalList = new ArrayList<DynamicDict>();
        if (balList != null && !balList.isEmpty()) {
            for (DynamicDict bal : balList) {
                DynamicDict acctResD = SelfCareCache.qryAthenaAcctRes(bal.getString("UNIT"));
                if ("5".equals(bal.getString("BAL_TYPE"))) {
                    continue;
                }
                if (acctResD == null) {
                    continue;
                }
                if ("1".equals(bal.getString("BAL_TYPE"))) {
                    // acctDict.set("defaultBal",
                    // CurrencyHelper.processCurreny(bal.getLong("RealBal"),acctResD.getLong("CURRENCY_PRECISION")));
                    continue;
                }
                String amountStr = CurrencyHelper.processCurreny(bal.getLong("GROSS_BAL") * -1,
                    acctResD.getLong("CURRENCY_PRECISION"));
                double amount = Double.parseDouble(amountStr);
                String realBalStr = CurrencyHelper.processCurreny(bal.getLong("REAL_BAL") * -1,
                    acctResD.getLong("CURRENCY_PRECISION"), 4);
                double realBal = Double.parseDouble(realBalStr);
                bal.set("ConsumeBal", (amount - realBal) + "");
                bal.set("RealBal", realBal + "");
                bal.set("SHOW_UNIT", acctResD.getString("SHOW_UNIT"));
                newBalList.add(bal);
            }
        }
        Long advance = acctDict.getLong("ADVANCE") == null ? 0L : acctDict.getLong("ADVANCE") * -1;
        Long consumed = acctDict.getLong("CONSUMED") == null ? 0L : acctDict.getLong("CONSUMED");
        acctDict.set("ADVANCE", CurrencyHelper.toFloatSum(advance.toString()));
        acctDict.set("CONSUMED", CurrencyHelper.toFloatSum(consumed.toString()));

        acctDict.set("BalDtoList", newBalList);
        return BoUtil.boToJson(acctDict);
    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("qryCdr")
    @ResponseBody
    public JSONObject qryCdr(HttpServletRequest request) throws BaseAppException {
        JSONObject result = new JSONObject();
        String canQryCdr = (String) request.getSession().getAttribute("CAN_QRY_CDR");

        if (!"Y".equals(canQryCdr)) {
            result.put("RESULT", "1");
            return result;
        }

        DynamicDict reqDict = MvcBoUtil.requestToBO(request);

        result.put("RESULT", "0");
        result.put("DATA", BoUtil.boToJson(qryCdr(reqDict, request)));

        return result;

    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param reqDict <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    private DynamicDict qryCdr(DynamicDict reqDict, HttpServletRequest request) throws BaseAppException {

        Long offset = reqDict.getLong("OFFSET");
        String type = reqDict.getString("TYPE");
        Date startDate = null;
        Date endDate = null;
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        if (offset != null) {
            Date queryDate = DateUtil.offsetMonth(new java.sql.Date(System.currentTimeMillis()), offset.intValue());
            startDate = DateUtil.getBeginDayOfMonth(queryDate);
            endDate = DateUtil.getLastDayOfMonth(queryDate);
        }
        else {
            startDate = reqDict.getDate("START_DATE");
            endDate = reqDict.getDate("END_DATE");

            if (startDate == null) {
                DynamicDict dictbak = new DynamicDict();
                dictbak.set("msisdn", session.getPrefix() + session.getAccNbr());
                dictbak.set("type", type);
                return dictbak;
            }
        }
        String msisdn = session.getPrefix() + session.getAccNbr();
        return BillingService.qyrCdr(msisdn, cdrTypeMap.get(type), startDate, endDate);
    }

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
    @RequestMapping("exportBill")
    public void exportBill(HttpServletRequest request, HttpServletResponse response) throws BaseAppException,
        IOException {

        DynamicDict reqDict = MvcBoUtil.requestToBO(request);
        JSONArray colArray = JSONArray.fromObject(reqDict.getString("colModel"));
        JSONObject param = JSONObject.fromObject(reqDict.getString("param"));
        DynamicDict qryCdrDict = new DynamicDict();
        qryCdrDict.set("TYPE", param.get("TYPE"));
        qryCdrDict.set("OFFSET", param.get("OFFSET"));
        qryCdrDict.set("START_DATE", param.get("START_DATE"));
        qryCdrDict.set("END_DATE", param.get("END_DATE"));
        String fileName = reqDict.getString("fileName");
        DynamicDict resultDict = qryCdr(qryCdrDict, request);
        if (resultDict != null && resultDict.getList("CDR_LIST") != null) {
            ExportHelper.writeResult(fileName + ".xls", colArray, resultDict.getList("CDR_LIST"), response);
        }
    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("qryCurCharge")
    @ResponseBody
    public JSONObject queryCurCharge(HttpServletRequest request) throws BaseAppException {
        JSONObject obj = new JSONObject();
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        DynamicDict curChargeDict = BillingService.queryCurCharge(session.getPrefix(), session.getAccNbr());
        Long recurringCharge = curChargeDict.getLong("RECURRING_CHARGE") == null ? 0L : curChargeDict
            .getLong("RECURRING_CHARGE");
        Long eventCharge = curChargeDict.getLong("EVENT_CHARGE") == null ? 0L : curChargeDict.getLong("EVENT_CHARGE");
        obj.put("RecurringCharge", recurringCharge);
        obj.put("EventCharge", eventCharge);
        obj.put("UsageTotal", recurringCharge + eventCharge);
        return obj;
    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("qryBillInfo")
    @ResponseBody
    public JSONObject qryBillInfo(HttpServletRequest request) throws BaseAppException {
        JSONObject obj = new JSONObject();
        DynamicDict reqDict = MvcBoUtil.requestToBO(request);
        String acctCode = reqDict.getString("acctCode");
        Long offset = reqDict.getLong("offset");

        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        if (!checkAcctIdSelf(session, reqDict.getLong("acctCode"))) {
            obj.put("RESULT", -1);
            return obj;
        }

        String attrValue = "";
        Long subsId = session.getSubsId();
        DynamicDict qryFlag = new DynamicDict();
        qryFlag.set("QRY_INDEP_PROD", true);
        DynamicDict subsDict = SubsServices.qrySubsDetail(subsId, qryFlag);
        logger.debug("qryBillInfo subsDict = {}", subsDict);
        DynamicDict subsBo = subsDict.getBO("SUBS");
        DynamicDict subsBaseDetail = subsBo.getBO("SUBS_BASE_DETAIL");
        List<DynamicDict> prodAttrList = subsBaseDetail.getList("PROD_ATTR_VALUE_EX_LIST");
        if (prodAttrList != null && !prodAttrList.isEmpty()) {
            for (DynamicDict attrBo : prodAttrList) {
                String attrCode = attrBo.getString("ATTR_CODE");
                if ("OLD_BILLCYCLE".equals(attrCode)) {
                    attrValue = attrBo.getString("VALUE");
                }
            }
        }

        Date queryDate = DateUtil.offsetMonth(new java.sql.Date(System.currentTimeMillis()), offset.intValue());

        Date startDate = DateUtil.getBeginDayOfMonth(queryDate);
        Date endDate = DateUtil.getLastDayOfMonth(queryDate);

        DynamicDict billList = BillingService.qryHisBill(acctCode, null, startDate, endDate);
        DynamicDict bill = null;
        if (billList != null && billList.getList("BILL_LIST") != null && !billList.getList("BILL_LIST").isEmpty()) {
            bill = (DynamicDict) billList.getList("BILL_LIST").get(0);
        }
        DynamicDict detailDict = new DynamicDict();
        if (bill != null) {
            DynamicDict billAcctItem = BillingService.qryBillAcctItem(acctCode, null, bill.getLong("BILL_ID"));
            if (billAcctItem != null) {
                List<DynamicDict> feeList = billAcctItem.getBO("BILL_ACCT_ITEM").getList("ACCT_ITEM_FEE_LIST");
                if (feeList != null && !feeList.isEmpty()) {
                    DynamicDict acctItemDict = getAcctItemByBizType(feeList, attrValue);
                    detailDict.set("Detail", acctItemDict);
                }
            }

            Long preBalance = billAcctItem.getLong("BILL_ACCT_ITEM.PRE_BALANCE") == null ? 0L : billAcctItem
                .getLong("BILL_ACCT_ITEM.PRE_BALANCE");
            Long adjustCharge = billAcctItem.getLong("BILL_ACCT_ITEM.ADJUST_CHARGE") == null ? 0L : billAcctItem
                .getLong("BILL_ACCT_ITEM.ADJUST_CHARGE");
            Long due = billAcctItem.getLong("BILL_ACCT_ITEM.DUE") == null ? 0L : billAcctItem
                .getLong("BILL_ACCT_ITEM.DUE");
            Long total = due;
            if (preBalance >= 0) {
                detailDict.set("Opening",
                    CurrencyHelper.toFloatSum(billAcctItem.getString("BILL_ACCT_ITEM.PRE_BALANCE")));
                detailDict.set("Advance", CurrencyHelper.toFloatSum("0"));
            }
            else {
                detailDict.set("Advance",
                    CurrencyHelper.toFloatSum(billAcctItem.getString("BILL_ACCT_ITEM.PRE_BALANCE")));
                detailDict.set("Opening", CurrencyHelper.toFloatSum("0"));
                total += preBalance;
            }
            detailDict.set("Adjustment",
                CurrencyHelper.toFloatSum(billAcctItem.getString("BILL_ACCT_ITEM.ADJUST_CHARGE")));

            Long closing = due + adjustCharge + preBalance;
            detailDict.set("Closing", CurrencyHelper.toFloatSum(String.valueOf(closing)));

            detailDict.set("Total", CurrencyHelper.toFloatSum(String.valueOf(total)));
            detailDict.set("AccountCode", bill.getString("ACCOUNT_CODE"));
            detailDict.set("BillNo", billAcctItem.getLong("BILL_ACCT_ITEM.BILL_NUMBER"));
            detailDict.set("BillMonth", DateUtil.date2String(startDate, "yyyy/MM"));
            obj = BoUtil.boToJson(detailDict);
        }
        else {
            obj.put("RESULT", -1);
        }

        return obj;
    }

    /**
     * Description: <br> 
     *  
     * @author zhang.liang<br>
     * @taskId <br>
     * @param list <br>
     * @param attrValue <br>
     * @return <br>
     * @throws BaseAppException <br>
     */ 
    private DynamicDict getAcctItemByBizType(List<DynamicDict> list, String attrValue) throws BaseAppException {
        Map<String, String> acctItemBizMap = SelfCareCache.qryAthenaAcctItemBiz();
        long taxAmount = 0;
        DynamicDict result = new DynamicDict();
        for (String key : acctItemBizMap.keySet()) {
            Long charge = 0L;
            String acctItemTypeId = "";
            for (DynamicDict acctItem : list) {
                acctItemTypeId = acctItem.getString("ACCT_ITEM_TYPE_ID");
                if (StringUtil.isEmpty(acctItemTypeId)) {
                    continue;
                }
                if (acctItemBizMap.get(key).indexOf("," + acctItemTypeId + ",") > -1) {
                    charge += acctItem.getLong("CHARGE");
                }
            }
            long preBalance = charge.longValue();
            if ((StringUtil.isEmpty(attrValue) || "10,30".indexOf(attrValue) == -1) && !"43".equals(acctItemTypeId)) {
                Double preBal = charge.longValue() / 1.05;
                preBalance = new BigDecimal(preBal).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
                long tax = charge.longValue() - preBalance;
                taxAmount += tax;
            }
            result.set(key, CurrencyHelper.toFloatSum(String.valueOf(preBalance)));
        }
        result.set("Commercial_Tax", taxAmount);
        return result;
    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("qryUsageDetail.do")
    @ResponseBody
    public JSONObject qryUsageDetail(HttpServletRequest request) throws BaseAppException {
        JSONObject obj = new JSONObject();
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        DynamicDict acctDict = BillingService.queryAccount(session.getPrefix(), session.getAccNbr(), null);

        List<DynamicDict> balList = acctDict.getList("BAL_LIST");
        DynamicDict resultDict = new DynamicDict();
        resultDict.set("V", new DynamicDict());
        resultDict.set("S", new DynamicDict());
        resultDict.set("D", new DynamicDict());
        resultDict.set("O", new DynamicDict());

        processUsageDetail(balList, resultDict);
        // 处理总计的精度
        processTotal(resultDict);

        Long consumed = 0L;
        Long advance = 0L;
        if (acctDict.getLong("CONSUMED") != null) {
            consumed = acctDict.getLong("CONSUMED");
        }
        if (acctDict.getLong("ADVANCE") != null) {
            advance = acctDict.getLong("ADVANCE") * -1;
        }

        resultDict.set("Consumed", CurrencyHelper.toFloatSum(consumed.toString()));
        resultDict.set("Advance", CurrencyHelper.toFloatSum(advance.toString()));
        resultDict.set("POSTPAID", session.getPaidFlag());

        return BoUtil.boToJson(resultDict);
    }

    /**
     * Description: <br> 
     *  
     * @author zhang.liang<br>
     * @taskId <br>
     * @param balList <br>
     * @param resultDict <br>
     * @throws BaseAppException <br>
     */ 
    private void processUsageDetail(List<DynamicDict> balList, DynamicDict resultDict) throws BaseAppException {
        Long point = 0L;
        String bonusAcctResIds = ConfigItemCache.instance().getString(
            "CUSTOMER_CARE/ATHENA_CC_PUBLIC/ATH_BONUS_ACCT_RES_IDS");
        logger.info("ATH_BONUS_ACCT_RES_IDS = [{}]", bonusAcctResIds);
        if (balList != null && !balList.isEmpty()) {
            for (DynamicDict bal : balList) {
                if (bal == null || bal.getString("UNIT") == null) {
                    continue;
                }

                DynamicDict athenaAcctRes = SelfCareCache.qryAthenaAcctRes(bal.getString("UNIT").toUpperCase());

                // 计算积分
                if ("5".equals(bal.getString("BAL_TYPE"))) {
                    point += bal.getLong("REAL_BAL") * -1;
                    continue;
                }
                if ("1".equals(bal.getString("BAL_TYPE"))) {
                    resultDict.set(
                        "defaultBal",
                        CurrencyHelper.processCurreny(bal.getLong("REAL_BAL"),
                            athenaAcctRes.getLong("CURRENCY_PRECISION")));
                    continue;
                }
                if (athenaAcctRes == null) {
                    continue;
                }
                DynamicDict singleDict = resultDict.getBO(athenaAcctRes.getString("BIZ_TYPE"));
                Long usage = singleDict.getLong("USAGE_TOTAL") == null ? 0L : singleDict.getLong("USAGE_TOTAL");
                Long remain = singleDict.getLong("REMAIN_TOTAL") == null ? 0L : singleDict.getLong("REMAIN_TOTAL");
                List<DynamicDict> list = singleDict.getList("BAL_LIST");
                if (list == null) {
                    list = new ArrayList<DynamicDict>();

                }
                // modify by task:1065933
                boolean isBonusBal = isBonusBal(bal, bonusAcctResIds);
                Long total = bal.getLong("GROSS_BAL") * -1;
                Long consumeBal = bal.getLong("CONSUME_BAL");
                if (isBonusBal) {
                    total = Math.abs(bal.getLong("INIT_BAL")) + Math.abs(bal.getLong("GROSS_BAL"));
                    if (bal.getLong("CONSUME_BAL") < 0) {
                        consumeBal = Math.abs(bal.getLong("INIT_BAL")) - Math.abs(bal.getLong("CONSUME_BAL"));
                    }
                    else {
                        consumeBal = Math.abs(bal.getLong("INIT_BAL")) + Math.abs(bal.getLong("CONSUME_BAL"));
                    }
                }
                // Long total = (bal.getLong("REAL_BAL")*-1) + bal.getLong("CONSUME_BAL");

                String amount = CurrencyHelper.processCurreny(total, athenaAcctRes.getLong("CURRENCY_PRECISION"));
                String realBalAmount = CurrencyHelper.processCurreny(bal.getLong("REAL_BAL") * -1,
                    athenaAcctRes.getLong("CURRENCY_PRECISION"));
                String consumeAmount = (Double.parseDouble(amount) - Double.parseDouble(realBalAmount)) + "";
                if (isBonusBal) {
                    // reaBal等于total-consumeBal
                    Long realBal = total - consumeBal;
                    realBalAmount = CurrencyHelper.processCurreny(realBal, athenaAcctRes.getLong("CURRENCY_PRECISION"));
                    consumeAmount = CurrencyHelper.processCurreny(consumeBal,
                        athenaAcctRes.getLong("CURRENCY_PRECISION"));
                }
                

                bal.set("Total", amount);
                bal.set("ConsumeBal", consumeAmount);
                // bal.set("RealBal",
                // CurrencyHelper.processCurreny(bal.getLong("REAL_BAL")*-1,athenaAcctRes.getLong("CURRENCY_PRECISION")));
                bal.set("RealBal", realBalAmount);
                if (total.intValue() == 0) {
                    bal.set("Rate", new Long(0));
                }
                else {
                    bal.set("Rate", (consumeBal) * 100 / total);
                }
                list.add(bal);
                singleDict.set("BAL_LIST", list);
                singleDict.set("SHOW_UNIT", athenaAcctRes.getString("SHOW_UNIT"));

                // 处理总额
                usage += (long) Double.parseDouble(consumeAmount);
                remain += (long) Double.parseDouble(realBalAmount);
                singleDict.set("USAGE_TOTAL", usage);
                singleDict.set("CURRENCY_PRECISION", athenaAcctRes.getLong("CURRENCY_PRECISION"));
                singleDict.set("REMAIN_TOTAL", remain);
                singleDict.set("MAIN_TOTAL", usage + remain);

            }
        }
        resultDict.set("POINTS", CurrencyHelper.toFloatSum(String.valueOf(point)));
    }
    
    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param bal <br>
     * @param bonusAcctResIds <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    private boolean isBonusBal(DynamicDict bal, String bonusAcctResIds) throws BaseAppException {
        boolean ret = false;
        if (StringUtil.isNotEmpty(bal.getString("ACCT_RES_ID")) && StringUtil.isNotEmpty(bonusAcctResIds)) {
            String tempIds = "," + bonusAcctResIds + ",";
            String tempId = "," + bal.getString("ACCT_RES_ID") + ",";
            if (tempIds.indexOf(tempId) > -1 && bal.getLong("INIT_BAL") != null) {
                // 如果配置了账本类型，则取init_bal字段作为total
                ret = true;
            }
        }
        return ret;
    }

    /**
     * Description: <br> 
     *  
     * @author zhang.liang<br>
     * @taskId <br>
     * @param resultDict <br>
     * @throws BaseAppException <br>
     */ 
    private void processTotal(DynamicDict resultDict) throws BaseAppException {

        if (resultDict == null || resultDict.valueMap == null) {
            return;
        }
        String[] keys = {
            "V", "O", "S", "D"
        };
        for (String key : keys) {
            DynamicDict singleDict = (DynamicDict) resultDict.valueMap.get(key);
            if (singleDict == null || singleDict.valueMap == null || singleDict.valueMap.isEmpty()) {
                continue;
            }

            // Long currency = singleDict.getLong("CURRENCY_PRECISION");
            // String remainTotal = CurrencyHelper.processCurreny(singleDict.getLong("REMAIN_TOTAL"),currency);
            // String mainTotal = CurrencyHelper.processCurreny(singleDict.getLong("MAIN_TOTAL"),currency);
            singleDict.set("USAGE_TOTAL", singleDict.getString("USAGE_TOTAL"));
            singleDict.set("REMAIN_TOTAL", singleDict.getString("REMAIN_TOTAL"));
            singleDict.set("MAIN_TOTAL", singleDict.getString("MAIN_TOTAL"));
        }
    }

    /**
     * Description: 查询当前用户的所有账户,过滤：如果不是当前用户的账户<br>
     * 
     * @author qi.mingjie<br>
     * @taskId <br>
     * @param session <br>
     * @param acctCode <br>
     * @return boolean <br>
     * @throws BaseAppException <br>
     */
    private boolean checkAcctIdSelf(SessionDto session, Long acctCode) throws BaseAppException {
        try {
            DynamicDict acctDict = AcctService.queryAcctByCustId(Long.valueOf(session.getCustId()));
            if (acctDict != null && acctDict.getList("ACCT_EX_LIST") != null) {
                for (Object tmp : acctDict.getList("ACCT_EX_LIST")) {
                    DynamicDict d = (DynamicDict) tmp;
                    if (d.getLong("ACCT_ID") != null && acctCode.equals(d.getLong("ACCT_ID"))) {
                        return true;
                    }
                }
            }
        }
        catch (BaseAppException e) {
            logger.error("Fail to check acctid");
        }
        return false;
    }
}
