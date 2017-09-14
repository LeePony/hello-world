package com.ztesoft.zsmart.bss.selfcare.controller;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ztesoft.zsmart.bss.selfcare.common.util.MvcBoUtil;
import com.ztesoft.zsmart.bss.selfcare.model.SessionDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
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
 * @CreateDate 2016年6月19日 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.selfcare.controller <br>
 */
@Controller
public class OrderController {

    /**
     * logger <br>
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(OrderController.class);

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("QryOrderItem")
    @ResponseBody
    public JSONObject qryOrderItem(HttpServletRequest request) throws BaseAppException {

        DynamicDict dict = MvcBoUtil.requestToBO(request);
        logger.debug("Begin qryOrderItem bo = [{}]", dict.asXML());

        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        Long subsId = session.getSubsId();

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

        DynamicDict qryDict = new DynamicDict();
        qryDict.serviceName = "QryOrderItemBySubsId";
        qryDict.set("SUBS_ID", subsId);
        qryDict.set("START_DATE", DateUtil.date2String(beginDate, DateUtil.DATETIME_FORMAT_1));
        qryDict.set("END_DATE", DateUtil.date2String(endDate, DateUtil.DATETIME_FORMAT_1));
        ServiceFlow.callService(qryDict);

        List<DynamicDict> orderList = qryDict.getList("z_d_r");
        dict.set("ORDER_LIST", orderList);

        return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
    }
}
