package com.ztesoft.zsmart.bss.selfcare.service;


import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
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
public class CommonServices {

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param dict 
     * @throws BaseAppException  <br>
     */
    public static void adviceService(DynamicDict dict) throws BaseAppException {
        
        DynamicDict sendDict = new DynamicDict();
        sendDict.valueMap = dict.valueMap;
        sendDict.serviceName = "AdviceService";
        ServiceFlow.callService(sendDict);
    }
}
