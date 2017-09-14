package test.com.ztesoft.zsmart.bss.selfcare.service;

import org.junit.Test;

import com.ztesoft.zsmart.bss.selfcare.service.CustServices;
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
public class CustServicesTest {

    /** logger */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(CustServicesTest.class);


    /**
     * Description: 根据客户id查询客户信息<br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQryCustById() {
        logger.debug("testQryCustById start.");
        try {
            DynamicDict dict = CustServices.qryCustById(131906353L);
            logger.debug("testQryCustById dict = [{}]", dict); 
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQryCustById end.");
    }
    /**
     * Description: 根据客户id查询客户信息<br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQryCertById() {
        logger.debug("testQryCertById start.");
        try {
            DynamicDict dict = CustServices.qryCertById(131906353L, 11283168L);
            logger.debug("testQryCertById dict = [{}]", dict);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQryCertById end.");
    }
    /**
     * Description: 查询客户类型<br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQryCustType() {
        logger.debug("testQryCustType start.");
        try {
            DynamicDict dict = CustServices.qryCustType("A");
            logger.debug("testQryCustType dict = [{}]", dict);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQryCustType end.");
    }
    /**
     * Description: 根据号码查询客户信息<br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQryCustByAccNbr() {
        logger.debug("testQryCustByAccNbr start.");
        try {
            DynamicDict dict = CustServices.qryCustByAccNbr("1521111222");
            logger.debug("testQryCustByAccNbr dict = [{}]", dict); 
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQryCustByAccNbr end.");
    }
}
