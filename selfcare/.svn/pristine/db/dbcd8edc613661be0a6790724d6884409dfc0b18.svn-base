package com.ztesoft.zsmart.bss.selfcare.common.util;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.HttpCall;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.web.resource.Common;

/**
 * <Description> <br>
 * 
 * @author yao.yueqing<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月7日 <br>
 * @since CRM8.1 <br>
 * @see com.ztesoft.zsmart.bss.selfcare.common.util <br>
 */
public class PerformtUtil {
    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param bo 
     * @throws BaseAppException <br>
     */
    public static void callService(DynamicDict bo) throws BaseAppException {
        bo.set("APP_ID", Common.APP_IP);
        if (bo.getLong("SP_ID") == null) {
            bo.set("SP_ID", 0);
        }
        HttpCall hc = new HttpCall();
        hc.callService(bo);
    }

   /**
    * 
    * Description: <br> 
    *  
    * @author yao.yueqing<br>
    * @taskId  <br>
    * @param bo 
    * @param isNewTran 
    * @throws BaseAppException  <br>
    */
    public static void callService(DynamicDict bo, boolean isNewTran) throws BaseAppException {
        bo.set("APP_ID", Common.APP_IP);
        if (bo.getLong("SP_ID") == null) {
            bo.set("SP_ID", 0);
        }
        if (isNewTran) {
            HttpCall hc = new HttpCall();
            hc.callService(bo);
        }
        else {
            ServiceFlow.callService(bo);
        }
    }
}
