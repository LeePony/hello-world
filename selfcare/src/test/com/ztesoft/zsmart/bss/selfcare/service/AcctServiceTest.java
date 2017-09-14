package test.com.ztesoft.zsmart.bss.selfcare.service;

import org.junit.Test;

import com.ztesoft.zsmart.bss.selfcare.service.AcctService;
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
public class AcctServiceTest {

    /** logger */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(AcctServiceTest.class);


    /**
     * Description: 根据客户id查询账户信息<br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQueryAcctByCustId() {
        logger.debug("testQueryAcctByCustId start.");
        DynamicDict dict = new DynamicDict();
        try {
            dict = AcctService.queryAcctByCustId(131906353L);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQueryAcctByCustId dict = [{}]", dict);
        logger.debug("testQueryAcctByCustId end.");
    }
}
