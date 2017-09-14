package test.com.ztesoft.zsmart.bss.selfcare.service;

import java.sql.Date;

import org.junit.Test;

import com.ztesoft.zsmart.bss.selfcare.service.BillingService;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.DateUtil;
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
public class BillingServiceTest {

    /** logger */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(BillingServiceTest.class);
    /**
     * Description: 查询默认余额<br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQryDefaultBal() {
        logger.debug("testQryDefaultBal start.");
        try {
            DynamicDict resultDict = BillingService.qryDefaultBal(132221343L);
            logger.debug("testQryDefaultBal dict = [{}]", resultDict);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQryDefaultBal end.");
    }

    /**
     * Description: 查询支付历史记录<br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQryPaymentHis() {
        logger.debug("testQryLoanBalHis start.");
        try {
            Date begin = new Date(1404955893000L);
            Date end = DateUtil.getNowDate();
            DynamicDict resultDict = BillingService.qryPaymentHis(132221343L, begin, end, null);
            logger.debug("testQryLoanBalHis dict = [{}]", resultDict);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQryLoanBalHis end.");
    }
    
    /**
     * Description: 查询账户信息<br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQryAcctByAcctId() {
        logger.debug("testQryAcctByAcctId start.");
        try {
            DynamicDict resultDict = BillingService.qryAcctByAcctId(132221343L);
            logger.debug("testQryAcctByAcctId dict = [{}]", resultDict);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQryAcctByAcctId end.");
    }
    /**
     * Description: 查询余额列表过滤失效<br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQryBalListFilterExpire() {
        logger.debug("testQryBalListFilterExpire start.");
        try {
            DynamicDict resultDict = BillingService.qryBalListFilterExpire(132221343L);
            logger.debug("testQryBalListFilterExpire dict = [{}]", resultDict); 
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQryBalListFilterExpire end.");
    }
    /**
     * Description: <br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQueryCurCharge() {
        logger.debug("testQueryCurCharge start.");
        try {
            DynamicDict resultDict = BillingService.queryCurCharge("95", "1521111222");
            logger.debug("testQueryCurCharge dict = [{}]", resultDict);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQueryCurCharge end.");
    }
    /**
     * Description: <br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQueryAccount() {
        logger.debug("testQueryAccount start.");
        try {
            DynamicDict resultDict = BillingService.queryAccount("95", "1521111222", "132221548");
            logger.debug("testQueryAccount dict = [{}]", resultDict);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQueryAccount end.");
    }
    /**
     * Description:查询历史账单 <br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQryHisBill() {
        logger.debug("testQryHisBill start.");
        try {
            Date begin = new Date(1404955893000L);
            Date end = DateUtil.getNowDate();
            DynamicDict resultDict = BillingService.qryHisBill("132221548", "951521111222", begin, end);
            logger.debug("testQryHisBill dict = [{}]", resultDict); 
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQryHisBill end.");
    }
    
}
