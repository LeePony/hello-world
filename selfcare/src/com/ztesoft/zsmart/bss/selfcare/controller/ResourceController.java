package com.ztesoft.zsmart.bss.selfcare.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ztesoft.zsmart.bss.selfcare.common.util.MvcBoUtil;
import com.ztesoft.zsmart.bss.selfcare.service.ResourceServices;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.AssertUtil;

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
public class ResourceController {
    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("QrySimCardDetail")
    @ResponseBody
    public JSONObject qrySimCardDetial(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        AssertUtil.isNotNull(dict.getLong("SIM_CARD_ID"), "SIM_CARD_ID is null");
        dict = ResourceServices.qrySimCardDetail(dict.getLong("SIM_CARD_ID"));
        if (dict != null) {
            return com.ztesoft.zsmart.web.mvc.helper.BoUtil.boToJson(dict);
        }
        else {
            return null;
        }
    }
}
