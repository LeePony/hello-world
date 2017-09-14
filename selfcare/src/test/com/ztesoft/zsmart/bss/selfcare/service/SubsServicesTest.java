package test.com.ztesoft.zsmart.bss.selfcare.service;

import org.junit.Test;

import com.ztesoft.zsmart.bss.selfcare.service.SubsServices;
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
public class SubsServicesTest {

    /** logger */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(SubsServicesTest.class);


    /**
     * Description: 根据前缀，号码查询订户信息<br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQrySubsByAccNbr() {
        logger.debug("testQrySubsByAccNbr start.");
        try {
            DynamicDict dict = SubsServices.qrySubsByAccNbr("95", "1521111222");
            logger.debug("testQrySubsByAccNbr dict = [{}]", dict);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQrySubsByAccNbr end.");
    }
    /**
     * Description: sim_card挂失<br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testSimCardLost() {
        logger.debug("testSimCardLost start.");
        try {
            SubsServices.simCardLost("95", "1521111222");
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testSimCardLost end.");
    }
    /**
     * Description: sim_card补办<br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testSimCardRestore() {
        logger.debug("testSimCardRestore start.");
        try {
            SubsServices.simCardRestore("95", "1521111222");
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testSimCardRestore end.");
    }
    
}
