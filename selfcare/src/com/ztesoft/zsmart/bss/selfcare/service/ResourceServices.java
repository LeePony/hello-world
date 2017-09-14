package com.ztesoft.zsmart.bss.selfcare.service;

import java.util.List;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;

/**
 * <Description> <br>
 * 
 * @author zhang.liang<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月7日 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.selfcare.service <br>
 */
public final class ResourceServices {
    /**
     * logger <br>
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(ResourceServices.class);

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param simCardId <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    public static DynamicDict qrySimCardDetail(Long simCardId) throws BaseAppException {
        logger.debug("Begin to qrySimCardDetail simCardId = {[]} ", simCardId);
        DynamicDict dict = new DynamicDict();
        dict.serviceName = "QrySimCardDetails";
        dict.set("SIM_CARD_ID", simCardId);
        ServiceFlow.callService(dict);
        @SuppressWarnings("unchecked")
        List<DynamicDict> list = dict.getList("z_d_r");
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        else {
            return null;
        }
    }
}
