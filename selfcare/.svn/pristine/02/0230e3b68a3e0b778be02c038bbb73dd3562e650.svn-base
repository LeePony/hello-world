package test.com.ztesoft.zsmart.bss.selfcare.common.helper;

import org.junit.Test;

import com.ztesoft.zsmart.bss.selfcare.common.helper.CurrencyHelper;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;


/** 
 * <Description> <br> 
 *  
 * @author wang.yongs<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2017年5月9日 <br>
 * @since V8.1<br>
 * @see test.com.ztesoft.zsmart.bss.selfcare.common.helper <br>
 */
public class CurrencyHelperTest {
    /** logger */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(CurrencyHelperTest.class);

    /**
     * Description: 根据货币精度格式化货币<br>
     * 
     * @author wang.yongs<br>
     * @taskId <br>
     */
    @Test
    public void testToFloatSum() {
        logger.debug("testToFloatSum start.");
        try {
            String currency = CurrencyHelper.toFloatSum("5000");
            logger.debug("testToFloatSum currency = [{}]", currency);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testToFloatSum end.");
    }

    /**
     * Description: <br>
     * 
     * @author wang.yongs<br>
     * @taskId <br>
     */
    @Test
    public void testProcessCurreny() {
        logger.debug("testProcessCurreny start.");
        try {
            String processCurreny = CurrencyHelper.processCurreny(5000L, 2L);
            logger.debug("testProcessCurreny processCurreny = [{}]", processCurreny);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testProcessCurreny end.");
    }
}
