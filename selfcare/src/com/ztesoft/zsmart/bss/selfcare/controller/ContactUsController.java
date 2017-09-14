package com.ztesoft.zsmart.bss.selfcare.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ztesoft.zsmart.bss.common.config.bll.ConfigItemCache;
import com.ztesoft.zsmart.bss.selfcare.common.util.MvcBoUtil;
import com.ztesoft.zsmart.bss.selfcare.model.SessionDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
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
public class ContactUsController {
    /**
     * logger <br>
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(ContactUsController.class);

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("SendMail")
    @ResponseBody
    public JSONObject sendMail(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        logger.debug("Begin sendMail bo = [{}]", dict.asXML());
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        String accNbr = session.getAccNbr();

        String stdCode = ConfigItemCache.instance().getString(
            "CUSTOMER_CARE/ATHENA_CC_PUBLIC/SELFCARE_CONTACT_MAIL_CODE");
        String addr = ConfigItemCache.instance().getString("CUSTOMER_CARE/ATHENA_CC_PUBLIC/SELFCARE_MPT_CONTACT_ADDR");

        if (StringUtil.isEmpty(addr) || StringUtil.isEmpty(stdCode)) {
            logger.error("The email address or template does not config, please config it.");
            return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
        }

        DynamicDict adviceDict = new DynamicDict();
        adviceDict.serviceName = "AdviceService";
        adviceDict.set("STD_CODE", stdCode);
        adviceDict.set("TYPE", dict.getString("TYPE"));
        adviceDict.set("COMMENTS", dict.getString("COMMENTS"));
        adviceDict.set("CONTACT_NUMBER", dict.getString("CONTACT_NUMBER"));
        if (StringUtil.isEmpty(adviceDict.getString("CONTACT_NUMBER"))) {
            adviceDict.set("CONTACT_NUMBER", accNbr);
        }
        DynamicDict sendParam = new DynamicDict();
        adviceDict.set("SENDER_PARAM", sendParam);
        sendParam.set("MAIL_TO", addr);
        sendParam.set("MAIL_SUBJECT", dict.getString("TITLE"));

        ServiceFlow.callService(adviceDict);

        logger.debug("End SendMail bo = [{}]", dict.asXML());

        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }
}
