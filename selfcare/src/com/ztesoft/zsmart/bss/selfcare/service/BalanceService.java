package com.ztesoft.zsmart.bss.selfcare.service;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;

/** 
 * <Description> <br> 
 *  
 * @author yuan.zhilang<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月30日 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.selfcare.service <br>
 */
public class BalanceService {

    /**
     * Description: <br> 
     *  
     * @author yuan.zhilang<br>
     * @taskId <br>
     * @param dict <br>
     * @return <br>
     * @throws BaseAppException <br>
     */ 
    public static DynamicDict loanBal(DynamicDict dict) throws BaseAppException {
        dict.setServiceName("LoanBalanceService{PN}Athena");
        dict.set("ACTION_ID", "loanBalance");
        ServiceFlow.callService(dict);
        return dict;
    }
    
    /**
     * Description: <br> 
     *  
     * @author yuan.zhilang<br>
     * @taskId <br>
     * @param dict <br>
     * @return <br>
     * @throws BaseAppException <br>
     */ 
    public static DynamicDict qryLoanBalHis(DynamicDict dict) throws BaseAppException {
        dict.setServiceName("LoanBalanceService{PN}Athena");
        dict.set("ACTION_ID", "qryLoanBalHis");
        ServiceFlow.callService(dict);
        return dict;
    }
}
