package test.com.ztesoft.zsmart.bss.selfcare.service;

import org.junit.Test;

import com.ztesoft.zsmart.bss.selfcare.service.ResourceServices;
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
public class ResourceServicesTest {

    /** logger */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(ResourceServicesTest.class);


    /**
     * Description: 根据sim_card详情<br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQrySimCardDetail() {
        logger.debug("testQrySimCardDetail start.");
        try {
            DynamicDict dict = ResourceServices.qrySimCardDetail(31411404L);
            logger.debug("testQrySimCardDetail dict = [{}]", dict);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQrySimCardDetail end.");
    }
}
