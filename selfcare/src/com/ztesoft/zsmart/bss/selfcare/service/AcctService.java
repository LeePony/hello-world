package com.ztesoft.zsmart.bss.selfcare.service;



import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;

public class AcctService {

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param custId
     * @return
     * @throws BaseAppException  <br>
     */
    public static DynamicDict queryAcctByCustId(Long custId) throws BaseAppException{
        DynamicDict dict = new DynamicDict();
        dict.set("CUST_ID", custId);
        dict.serviceName = "AdapterQryAcctList";
        ServiceFlow.callService(dict);
       
        return dict;
    }
}
