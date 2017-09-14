package com.ztesoft.zsmart.bss.selfcare.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ztesoft.zsmart.bss.selfcare.common.helper.CommonHelper;
import com.ztesoft.zsmart.bss.selfcare.common.helper.CurrencyHelper;
import com.ztesoft.zsmart.bss.selfcare.common.util.MvcBoUtil;
import com.ztesoft.zsmart.bss.selfcare.model.SessionDto;
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
import com.ztesoft.zsmart.web.resource.Common;

/**
 * <Description> <br>
 * 
 * @author zhang.liang<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月7日 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.selfcare.controller <br>
 */
@Controller
public class SubsController {
    /**
     * logger <br>
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(SubsController.class);

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("QrySubsInfo")
    @ResponseBody
    public JSONObject qrySubsInfo(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long subsId = session.getSubsId();

        logger.debug("Begin qrySubsInfo bo = [{}]", dict.asXML());
        dict = SubsServices.qrySubsDetail(subsId, dict.getBO("SUBS_QUERY_FLAG"));
        logger.debug("End qrySubsInfo bo = [{}]", dict.asXML());

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
    @RequestMapping("qryMySubscriber.do")
    @ResponseBody
    public JSONObject qryMySubscriber(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long subsId = session.getSubsId();

        logger.debug("Begin qryMySubscriber bo = [{}]", dict.asXML());
        dict = SubsServices.qrySubsDetail(subsId, dict.getBO("SUBS_QUERY_FLAG"));
        DynamicDict subsDetail = dict.getBO("SUBS.SUBS_BASE_DETAIL");
        if (subsDetail != null) {
            String blockReasonId = CommonHelper.getBlockReasonForDisplay(subsDetail.getString("BLOCK_REASON"));

            subsDetail.set("BLOCK_REASON_ID", blockReasonId);
        }
        logger.debug("End qryMySubscriber bo = [{}]", dict.asXML());

        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("SimCardLost")
    @ResponseBody
    public JSONObject simCardLost(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long subsId = session.getSubsId();

        // 后台再次校验
        DynamicDict qryFlag = new DynamicDict();
        qryFlag.set("INCLUDE_TERMINATION", false);
        dict = SubsServices.qrySubsDetail(subsId, qryFlag);

        // 订户状态为A才允许卡挂失
        if (dict == null || dict.getBO("SUBS") == null) {
            // 订户不存在
            ExceptionHandler.publish("CC-S-SALES-00001", ExceptionHandler.BUSS_ERROR);
        }
        DynamicDict subs = dict.getBO("SUBS");
        String prodState = subs.getBO("SUBS_BASE_DETAIL").getString("PROD_STATE");
        if (!"A".equals(prodState)) {
            // 订户状态不正确
            ExceptionHandler.publish("SC_USER_PROD_STATE_INACTIVE", ExceptionHandler.BUSS_ERROR);
        }

        String accNbr = subs.getBO("SUBS_BASE_DETAIL").getString("ACC_NBR");
        String prefix = subs.getBO("SUBS_BASE_DETAIL").getString("PREFIX");

        SubsServices.simCardLost(prefix, accNbr);

        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("SimCardRestore")
    @ResponseBody
    public JSONObject simCardRestore(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long subsId = session.getSubsId();

        // 后台校验状态和停机位
        DynamicDict qryFlag = new DynamicDict();
        qryFlag.set("INCLUDE_TERMINATION", false);
        dict = SubsServices.qrySubsDetail(subsId, qryFlag);

        // 订户状态为停机才允许解挂失
        if (dict == null || dict.getBO("SUBS") == null) {
            // 订户不存在
            ExceptionHandler.publish("CC-S-SALES-00001", ExceptionHandler.BUSS_ERROR);
        }
        DynamicDict subs = dict.getBO("SUBS");
        String prodState = subs.getBO("SUBS_BASE_DETAIL").getString("PROD_STATE");
        String blockReason = subs.getBO("SUBS_BASE_DETAIL").getString("BLOCK_REASON");

        if (!"D".equals(prodState) && !"E".equals(prodState)) {
            // 订户状态不正确
            ExceptionHandler.publish("SC_USER_PROD_STATE_NOT_WAY_BLOCK", ExceptionHandler.BUSS_ERROR);
        }
        if (!"2".equals(blockReason.substring(7, 8)) && !"1".equals(blockReason.substring(7, 8))) {
            // 订户状态不正确
            ExceptionHandler.publish("SC_USER_BLOCK_REASON_NOT_OUT_SERVICE", ExceptionHandler.BUSS_ERROR);
        }

        String accNbr = subs.getBO("SUBS_BASE_DETAIL").getString("ACC_NBR");
        String prefix = subs.getBO("SUBS_BASE_DETAIL").getString("PREFIX");

        SubsServices.simCardRestore(prefix, accNbr);

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
    @RequestMapping("AddFellowNbr")
    @ResponseBody
    public JSONObject addFellowNbr(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        AssertUtil.isNotNull(dict.getString("FELLOW_NBR"), "FELLOW_NBR is null");
        AssertUtil.isNotNull(dict.getString("EFF_DATE"), "EFF_DATE is null");

        String fellowNum = dict.getString("FELLOW_NBR");
        if (!StringUtil.isNumeric(fellowNum)) {
            ExceptionHandler.publish("SC_FELLOW_NUMBER_NOT_DIGITS", ExceptionHandler.BUSS_ERROR);
        }
        else if (fellowNum.length() < 8 || fellowNum.length() > 12) {
            ExceptionHandler.publish("SC_FELLOW_NUMBER_LENGTH_INVALID", ExceptionHandler.BUSS_ERROR); 
        }
        
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long subsId = session.getSubsId();

        // 校验
        DynamicDict qryFlag = new DynamicDict();
        qryFlag.set("INCLUDE_TERMINATION", false);
        qryFlag.set("QRY_FELLOW_NBR", true);
        qryFlag.set("QRY_DEPEND_PROD", true);
        DynamicDict subsDict = SubsServices.qrySubsDetail(subsId, qryFlag);
        if (subsDict == null || subsDict.getBO("SUBS") == null) {
            // 订户不存在
            ExceptionHandler.publish("CC-S-SALES-00001", ExceptionHandler.BUSS_ERROR);
        }
        DynamicDict subs = subsDict.getBO("SUBS");
        String accNbr = subs.getBO("SUBS_BASE_DETAIL").getString("ACC_NBR");
        if (dict.getString("FELLOW_NBR").equals(accNbr)) {
            // 新增号码不能与当前订户号码相同
            ExceptionHandler.publish("CC-S-SALES-01007", ExceptionHandler.BUSS_ERROR);
        }

        // 校验是否已经订购了亲情号码依赖产品，如果没有则需要提示将自动订购，并且存在月租
        if (!"Y".equals(dict.getString("CONFIRM_ORDER_DP"))) {
            DynamicDict checkDict = new DynamicDict();
            checkDict.serviceName = "CheckDpOfferOfFellowNbr{PN}Athena";
            checkDict.set("SUBS_ID", subsId);
            if (dict.getLong("FELLOW_NBR_TYPE_ID") == null) {
                checkDict.set("FELLOW_NBR_TYPE_ID", SubsServices.defFellowNbrTypeId);
            }
            ServiceFlow.callService(checkDict);
            if ("Y".equals(checkDict.getString("DP_DOES_NOT_EXIST"))) {
                DynamicDict retDict = new DynamicDict();
                retDict.set("DP_DOES_NOT_EXIST", checkDict.getString("DP_DOES_NOT_EXIST"));
                retDict.set("RENT_LIST_PRICE", checkDict.getString("RENT_LIST_PRICE"));
                return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(retDict);
            }
        }

        List<DynamicDict> fellowNbrList = subs.getList("FELLOW_NBR_EX_LIST");
        for (DynamicDict fellowNbr : fellowNbrList) {
            if (dict.getString("FELLOW_NBR").equals(fellowNbr.getString("FELLOW_NBR"))) {
                // 相同的亲情号码已经存在
                ExceptionHandler.publish("SC_FELLOW_NBR_DUPLICATE", ExceptionHandler.BUSS_ERROR);
                break;
            }
        }

        SubsServices.addFellowNbr(subsId, dict.getString("FELLOW_NBR"), dict.getLong("FELLOW_NBR_TYPE_ID"),
            dict.getDate("EFF_DATE"), dict.getDate("EXP_DATE"));

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
    @RequestMapping("DelFellowNbr")
    @ResponseBody
    public JSONObject delFellowNbr(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        AssertUtil.isNotNull(dict.getString("FELLOW_NBR"), "FELLOW_NBR is null");

        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long subsId = session.getSubsId();

        // 校验
        DynamicDict qryFlag = new DynamicDict();
        qryFlag.set("QRY_FELLOW_NBR", true);
        DynamicDict subsDict = SubsServices.qrySubsDetail(subsId, qryFlag);
        if (subsDict == null || subsDict.getBO("SUBS") == null) {
            // 订户不存在
            ExceptionHandler.publish("CC-S-SALES-00001", ExceptionHandler.BUSS_ERROR);
        }
        DynamicDict subs = subsDict.getBO("SUBS");
        List<DynamicDict> fellowNbrList = subs.getList("FELLOW_NBR_EX_LIST");
        if (fellowNbrList == null || fellowNbrList.isEmpty()) {
            // 删除的亲情号码不存在
            ExceptionHandler.publish("CC-S-SALES-02048", ExceptionHandler.BUSS_ERROR);
        }
        Long fellowNbrTypeId = null;
        for (DynamicDict fellowNbr : fellowNbrList) {
            if (dict.getString("FELLOW_NBR").equals(fellowNbr.getString("FELLOW_NBR"))) {
                fellowNbrTypeId = fellowNbr.getLong("FELLOW_NBR_TYPE_ID");
                break;
            }
        }
        if (fellowNbrTypeId == null) {
            // 删除的亲情号码不存在
            ExceptionHandler.publish("CC-S-SALES-02048", ExceptionHandler.BUSS_ERROR);
        }

        SubsServices.delFellowNbr(subsId, dict.getString("FELLOW_NBR"), fellowNbrTypeId);

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
    @RequestMapping("QrySubsOffer")
    @ResponseBody
    public JSONObject qrySubsOffers(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long subsId = session.getSubsId();
        logger.debug("Begin qrySubsOffers subsId = [{}]", subsId);

        // 现场询实例数据
        DynamicDict qryFlag = new DynamicDict();
        qryFlag.set("QRY_SUBS_UPP_INST", true);
        qryFlag.set("QRY_DEPEND_PROD", true);
        DynamicDict subsDict = SubsServices.qrySubsDetail(subsId, qryFlag);
        List<DynamicDict> dependProdList = new ArrayList<DynamicDict>();
        List<DynamicDict> subsUppInstList = new ArrayList<DynamicDict>();
        Long subsPlanId = null;
        if (subsDict != null && subsDict.getBO("SUBS") != null) {
            dependProdList = subsDict.getBO("SUBS").getList("DEPEND_PROD_EX_LIST");
            subsUppInstList = subsDict.getBO("SUBS").getList("SUBS_UPP_INST_EX_LIST");
            subsPlanId = subsDict.getBO("SUBS").getBO("SUBS_BASE_DETAIL").getLong("SUBS_PLAN_ID");

            // 过滤掉默认资费计划
            if (subsUppInstList != null) {
                List<DynamicDict> tempList = new ArrayList<DynamicDict>();
                for (DynamicDict subsUpp : subsUppInstList) {
                    if (!"2".equals(subsUpp.getString("PRICE_PLAN_TYPE"))
                        && !"Y".equals(subsUpp.getString("HIDE_FLAG"))) {
                        tempList.add(subsUpp);
                    }
                }
                subsUppInstList = tempList;
            }

            // 过滤隐藏的依赖产品
            if (dependProdList != null) {
                List<DynamicDict> tempList = new ArrayList<DynamicDict>();
                for (DynamicDict depProd : dependProdList) {
                    if (!"Y".equals(depProd.getString("HIDE_FLAG"))) {
                        tempList.add(depProd);
                    }
                }
                dependProdList = tempList;
            }
        }

        // 查询规格数据
        if (subsPlanId != null) {
            DynamicDict qryOfferDict = new DynamicDict();
            qryOfferDict.serviceName = "QryVAS";
            qryOfferDict.set("SUBS_PLAN_ID", subsPlanId);
            qryOfferDict.set("CONTACT_CHANNEL_ID", SubsServices.contactChannelId);
            qryOfferDict.set("DEF_PRICE_PLAN", false);
            ServiceFlow.callService(qryOfferDict);
            List<DynamicDict> groupList = qryOfferDict.getList("OFFER_VAS_LIST");
            List<DynamicDict> dependGroupList = new ArrayList<DynamicDict>();
            List<DynamicDict> priceGroupList = new ArrayList<DynamicDict>();
            if (groupList != null && !groupList.isEmpty()) {
                for (DynamicDict group : groupList) {
                    List<DynamicDict> childList = group.getList("CHILDREN");
                    List<DynamicDict> offerMemList = new ArrayList<DynamicDict>();
                    if (childList == null || childList.isEmpty()) {
                        continue;
                    }
                    // 如果不允许重复订购，则过滤掉已经存在实例的商品
                    for (DynamicDict offer : childList) {
                        if (!isExist(offer, dependProdList, subsUppInstList)) {
                            offerMemList.add(offer);
                        }
                    }
                    if (offerMemList.isEmpty()) {
                        continue;
                    }
                    group.set("CHILDREN", offerMemList);
                    if ("3".equals(group.getString("OFFER_GROUP_TYPE"))) {
                        // 依赖产品
                        dependGroupList.add(group);
                    }
                    else {
                        // 资费计划
                        priceGroupList.add(group);
                    }
                }
            }
            dict.set("DEPEND_GROUP_LIST", dependGroupList);
            dict.set("PRICE_GROUP_LIST", priceGroupList);
        }

        dict.set("DEPEND_PROD_LIST", dependProdList);
        dict.set("SUBS_UPP_LIST", subsUppInstList);

        logger.debug("End qrySubsOffers dict = [{}]", dict.asXML());

        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param offer <br>
     * @param depProdList <br>
     * @param subsUppList <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    private boolean isExist(DynamicDict offer, List<DynamicDict> depProdList, List<DynamicDict> subsUppList)
        throws BaseAppException {
        boolean ret = false;
        if ("3".equals(offer.getString("OFFER_TYPE"))) {
            if (depProdList != null) {
                for (DynamicDict depProd : depProdList) {
                    if (depProd.getString("OFFER_ID").equals(offer.getString("OFFER_ID"))) {
                        if (offer.getBO("OFFER") != null) {
                            depProd.set("RENT_LIST_PRICE", offer.getBO("OFFER").getString("RENT_LIST_PRICE"));
                            depProd.set("SALE_LIST_PRICE", offer.getBO("OFFER").getString("SALE_LIST_PRICE"));
                        }
                        if (StringUtil.isNotEmpty(offer.getString("DUPLICATE_FLAG"))) {
                            // 允许重复订购，则返回false
                            continue;
                        }
                        return true;
                    }
                }
            }
        }
        else {
            if (subsUppList != null) {
                for (DynamicDict subsUpp : subsUppList) {
                    if (subsUpp.getString("PRICE_PLAN_ID").equals(offer.getString("OFFER_ID"))) {
                        if (offer.getBO("OFFER") != null) {
                            subsUpp.set("RENT_LIST_PRICE", offer.getBO("OFFER").getString("RENT_LIST_PRICE"));
                            subsUpp.set("SALE_LIST_PRICE", offer.getBO("OFFER").getString("SALE_LIST_PRICE"));
                        }
                        if (StringUtil.isNotEmpty(offer.getString("DUPLICATE_FLAG"))) {
                            // 允许重复订购，则返回false
                            continue;
                        }
                        return true;
                    }
                }
            }
        }
        return ret;
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
    @RequestMapping("QryDpOrderFee")
    @ResponseBody
    public JSONObject qryDpOrderFee(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        AssertUtil.isNotNull(dict.getString("OFFER_ID"), "OFFER_ID is null");
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Boolean prePaidFlag = "1".equals(session.getPaidFlag())?true:false;
        Long subsId = session.getSubsId();
        Long offerId = dict.getLong("OFFER_ID");

        logger.debug("Begin qryDpOrderFee subsId = [{}], offerId = [{}]", subsId, offerId);
        DynamicDict serviceDict = new DynamicDict();
        if(prePaidFlag){
            try {

                serviceDict.serviceName = "QryDpOrderFee{PN}Athena";
                serviceDict.set("SUBS_ID", subsId);
                serviceDict.set("OFFER_ID", offerId);
                serviceDict.set("OPERATION_TYPE", "A");
                serviceDict.set("CONTACT_CHANNEL_ID", SubsServices.contactChannelId);
                ServiceFlow.callService(serviceDict);
            }
            catch (BaseAppException ex) {
                throw ex;
            }

            Long charge = 0l;
            if (serviceDict.getBO("CUST_ORDER") != null && serviceDict.getBO("CUST_ORDER").getBO("CASH_DESK_FEE") != null) {
                charge = serviceDict.getBO("CUST_ORDER").getBO("CASH_DESK_FEE").getLong("RECEIVABLE_CHARGE");
            }
            if (charge == null) {
                charge = 0l;
            }
            dict.set("RECEIVABLE_CHARGE", CurrencyHelper.toFloatSum(charge.toString()));
            dict.set("CHARGE_INT", charge.toString());
            session.setOfferCharge(charge.toString());
            DynamicDict offerDict = serviceDict.getBO("OFFER");
            if (offerDict != null) {
                dict.set("OFFER_NAME", offerDict.getString("OFFER_NAME"));
                dict.set("OFFER_CODE", offerDict.getString("OFFER_CODE"));
            }
            dict.set("POST_PAID", "N");
        }
        else{
            serviceDict.serviceName = "QryOfferById{PN}Athena";
            serviceDict.set("OFFER_ID", offerId);
            ServiceFlow.callService(serviceDict);
            dict.set("OFFER_NAME", serviceDict.getString("OFFER_NAME"));
            dict.set("POST_PAID", "Y");
            dict.set("OFFER_CODE", serviceDict.getString("OFFER_CODE"));
        }

        // 查询订户余额
        DynamicDict balDict = BillingService.qryDefaultBal(session.getAcctId());
        Long grossBal = balDict.getLong("GROSS_BAL");
        Long realBal = balDict.getLong("REAL_BAL");
        if (grossBal == null) {
            grossBal = 0l;
        }
        grossBal = grossBal * -1;
        
        if (realBal == null) {
            realBal = 0l;
        }
        realBal = realBal * -1;
        // 精度处理
        dict.set("GROSS_BAL", CurrencyHelper.toFloatSum(grossBal.toString()));
        dict.set("REAL_BAL", CurrencyHelper.toFloatSum(realBal.toString()));
        dict.set("BAL_INT", grossBal);

       

        dict.set("ACC_NBR", session.getAccNbr());

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
    @RequestMapping("AddOffer")
    @ResponseBody
    public JSONObject addOffer(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        AssertUtil.isNotNull(dict.getString("OFFER_ID"), "OFFER_ID is null");
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long subsId = session.getSubsId();
        Long offerId = dict.getLong("OFFER_ID");

        logger.debug("Begin addOffer subsId = [{}], offerId = [{}]", subsId, offerId);

        // 校验余额是否足够
        if (StringUtil.isNotEmpty(session.getOfferCharge())) {
            // 查询订户余额
            DynamicDict balDict = BillingService.qryDefaultBal(session.getAcctId());
            Long grossBal = balDict.getLong("GROSS_BAL");
            if (grossBal == null) {
                grossBal = 0l;
            }
            grossBal = grossBal * -1;
            if (Long.valueOf(session.getOfferCharge()) > grossBal) {
                ExceptionHandler.publish("SC_BAL_NOT_ENOUGH", ExceptionHandler.BUSS_ERROR);
            }
        }

        DynamicDict serviceDict = new DynamicDict();
        serviceDict.serviceName = "OrderOffer{PN}Athena";
        serviceDict.set("SUBS_ID", subsId);
        serviceDict.set("OFFER_ID", offerId);
        serviceDict.set("OFFER_CODE", dict.getString("OFFER_CODE"));
        serviceDict.set("OPERATION_TYPE", "A");
        serviceDict.set("CONTACT_CHANNEL_ID", SubsServices.contactChannelId);
        ServiceFlow.callService(serviceDict);

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
    @RequestMapping("DelOffer")
    @ResponseBody
    public JSONObject delOffer(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        AssertUtil.isNotNull(dict.getString("OFFER_ID"), "OFFER_ID is null");
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long subsId = session.getSubsId();
        Long offerId = dict.getLong("OFFER_ID");
        Long subsUppInstId = dict.getLong("SUBS_UPP_INST_ID");
        Long prodId = dict.getLong("PROD_ID");

        logger.debug("Begin delOffer subsId = [{}], offerId = [{}]", subsId, offerId);

        if (subsUppInstId == null && prodId == null) {
            // TODO
        }

        DynamicDict serviceDict = new DynamicDict();
        serviceDict.serviceName = "OrderOffer{PN}Athena";
        serviceDict.set("SUBS_ID", subsId);
        serviceDict.set("OFFER_ID", offerId);
        serviceDict.set("OFFER_INST_ID", subsUppInstId != null ? subsUppInstId : prodId);
        serviceDict.set("OPERATION_TYPE", "D");
        serviceDict.set("CONTACT_CHANNEL_ID", SubsServices.contactChannelId);
        ServiceFlow.callService(serviceDict);

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
    @RequestMapping("QryAddOnOfferHis")
    @ResponseBody
    public JSONObject qryAddOnOfferHis(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        logger.debug("Begin qryAddOnOfferHis bo = [{}]", dict.asXML());
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long subsId = session.getSubsId();

        Date now = DateUtil.GetDBDateTime();
        Date endDate = now;
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

        // 查询订户的依赖产品和资费计划实例
        DynamicDict qrySubsUppDict = new DynamicDict();
        qrySubsUppDict.serviceName = "AdapterQrySubsUppInst";
        qrySubsUppDict.set("SUBS_ID", subsId);
        qrySubsUppDict.set("EXPRIED_FLAG", "Y");
        ServiceFlow.callService(qrySubsUppDict);
        List<DynamicDict> subsUppInstList = qrySubsUppDict.getList("SUBS_UPP_INST_LIST");

        DynamicDict qryProdDict = new DynamicDict();
        qryProdDict.serviceName = "AdapterQryDependProd";
        qryProdDict.set("SUBS_ID", subsId);
        ServiceFlow.callService(qryProdDict);
        List<DynamicDict> dependProdList = qryProdDict.getList("DEPEND_PROD_LIST");

        // 过滤掉生效的数据，只返回历史
        DynamicDict hisDict = null;
        if (dependProdList != null) {
            for (DynamicDict depProd : dependProdList) {
//                if (!"B".equals(depProd.getString("PROD_STATE")) && !"E".equals(depProd.getString("PROD_STATE"))) {
//                    continue;
//                }
//                if (depProd.getDate("PROD_STATE_DATE") == null || now.before(depProd.getDate("PROD_STATE_DATE"))) {
//                    continue;
//                }

                if (depProd.getDate("COMPLETED_DATE").before(beginDate)
                    || depProd.getDate("COMPLETED_DATE").after(endDate)) {
                    if (!depProd.getDate("COMPLETED_DATE").toString().equals(beginDate.toString())
                        && !depProd.getDate("COMPLETED_DATE").toString().equals(endDate.toString())) {
                        continue;
                    }
                }
                hisDict = new DynamicDict();
                DynamicDict offerDict = qryOfferById(depProd.getLong("OFFER_ID"));
                hisDict.set("OFFER_NAME", offerDict.getString("OFFER_NAME"));
                hisDict.set("ACTIVE_DATE", depProd.getDate("COMPLETED_DATE"));
                hisDict.set("STATE", depProd.getString("PROD_STATE"));
                hisDict.set("COMMENTS", offerDict.getString("COMMENTS"));
                dict.add("ADD_ON_OFFER_HIS", hisDict);
            }
        }
        if (subsUppInstList != null) {
            for (DynamicDict subsUpp : subsUppInstList) {
//                if (!"X".equals(subsUpp.getString("STATE")) && !"E".equals(subsUpp.getString("STATE"))) {
//                    continue;
//                }
//                if (subsUpp.getDate("EXP_DATE") == null || now.before(subsUpp.getDate("EXP_DATE"))) {
//                    continue;
//                }
//                if (subsUpp.getDate("EFF_DATE").before(beginDate) || subsUpp.getDate("EFF_DATE").after(endDate)) {
//                    if (!subsUpp.getDate("EFF_DATE").toString().equals(beginDate.toString())
//                        && !subsUpp.getDate("EFF_DATE").toString().equals(endDate.toString())) {
//                        continue;
//                    }
//                }
                if (subsUpp.getDate("EFF_DATE").before(beginDate)
                    || subsUpp.getDate("EFF_DATE").after(endDate)) {
                    if (!subsUpp.getDate("EFF_DATE").toString().equals(beginDate.toString())
                        && !subsUpp.getDate("EFF_DATE").toString().equals(endDate.toString())) {
                        continue;
                    }
                }
                hisDict = new DynamicDict();
                hisDict.set("OFFER_NAME", qryOfferById(subsUpp.getLong("PRICE_PLAN_ID")).getString("OFFER_NAME"));
                hisDict.set("ACTIVE_DATE", subsUpp.getDate("EFF_DATE"));
                hisDict.set("STATE", subsUpp.getString("STATE"));
                dict.add("ADD_ON_OFFER_HIS", hisDict);
            }
        }

        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param offerId <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    private DynamicDict qryOfferById(Long offerId) throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.serviceName = "QryOfferById{PN}Athena";
        dict.set("OFFER_ID", offerId);
        ServiceFlow.callService(dict);
        return dict;
    }
}
