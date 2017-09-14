package test.com.ztesoft.zsmart.bss.selfcare.service;

import org.junit.Test;

import com.ztesoft.zsmart.bss.selfcare.service.BalanceService;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;


/** 
 * <Description> <br> 
 *  
 * @author wang.yongs<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2017年5月9日 <br>
 * @since V8.1<br>
 * @see test.com.ztesoft.zsmart.bss.selfcare.service <br>
 */
public class BalanceServiceTest {

    /** logger */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(BalanceServiceTest.class);
    /**
     * Description: 加载余额信息<br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testLoanBal() {
        logger.debug("testLoanBal start.");
        try {
            DynamicDict dict = new DynamicDict();
            dict.set("MSISDN", "951521111222");
            DynamicDict resultDict = BalanceService.loanBal(dict);
            logger.debug("testLoanBal dict = [{}]", resultDict);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testLoanBal end.");
    }

    /**
     * Description: 查询余额历史纪录<br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQryLoanBalHis() {
        logger.debug("testQryLoanBalHis start.");
        DynamicDict dict = new DynamicDict();
        try {
            dict.set("MSISDN", "951521111222");
            dict.set("ACCOUNT_CODE", "132221548");
            dict.set("START_DATE", "2016/10/27");
            dict.set("SP_ID", 0);
            dict.set("PARTY_CODE", "2610");
            dict.set("PARTY_TYPE", "E");
            DynamicDict resultDict = BalanceService.qryLoanBalHis(dict);
            logger.debug("testQryLoanBalHis dict = [{}]", resultDict);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQryLoanBalHis end.");
    }
}
