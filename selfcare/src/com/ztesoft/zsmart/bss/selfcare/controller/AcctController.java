package com.ztesoft.zsmart.bss.selfcare.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ztesoft.zsmart.bss.selfcare.common.util.MvcBoUtil;
import com.ztesoft.zsmart.bss.selfcare.model.SessionDto;
import com.ztesoft.zsmart.bss.selfcare.service.AcctService;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.mvc.core.helper.BoUtil;
import com.ztesoft.zsmart.web.resource.Common;

/**
 * 
 * <Description> <br> 
 *  
 * @author yao.yueqing<br>
 * @version 1.0<br>
 * @taskId  <br>
 * @CreateDate 2016年7月2日 <br>
 * @since CRM8.1 <br>
 * @see com.ztesoft.zsmart.bss.selfcare.controller <br>
 */
@Controller
public class AcctController {

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @return  <br>
     */
    @RequestMapping("queryAllAcct.do")
    @ResponseBody
    public JSONObject queryAcctByCustId(HttpServletRequest request) throws BaseAppException{
        DynamicDict result = new DynamicDict();
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        boolean isQryAll = true;
        if(StringUtil.isNotEmpty(dict.getString("POSTPAID"))){
            isQryAll = false;
        }
        List<DynamicDict>  list = new ArrayList<DynamicDict>();
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        DynamicDict acctDict = AcctService.queryAcctByCustId(Long.valueOf(session.getCustId()));
        if(acctDict!=null&&acctDict.getList("ACCT_EX_LIST")!=null){
            for(Object tmp : acctDict.getList("ACCT_EX_LIST")){
                DynamicDict d = (DynamicDict)tmp;
                if(isQryAll||dict.getString("POSTPAID").equals(d.getString("POSTPAID"))){
                    list.add(d);
                }
            }
        }
        result.set("ACCT_LIST", list);
        return BoUtil.boToJson(result);
    }
    
}
