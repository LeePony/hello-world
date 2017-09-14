package test.com.ztesoft.zsmart.bss.selfcare.common.cache;

import java.util.List;

import org.junit.Test;

import com.ztesoft.zsmart.bss.selfcare.common.cache.SelfCareCache;
import com.ztesoft.zsmart.bss.selfcare.model.CertTypeDto;
import com.ztesoft.zsmart.bss.selfcare.model.MenuDto;
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
 * @see test.com.ztesoft.zsmart.bss.selfcare.common.cache <br>
 */
public class SelfCareCacheTest {
    /** logger */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(SelfCareCacheTest.class);

    
    /**
     * Description: <br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testGetCertTypeById() {
        logger.debug("testGetCertTypeById start.");
        CertTypeDto re = null;
        try {
            re = SelfCareCache.getCertTypeById(1L);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testGetCertTypeById end.[{}]", re);
    }

    
    /**
     * Description: <br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testGetSystemParamByMask() {
        logger.debug("testGetSystemParamByMask start.");
        //金额小数位数
        String systemParam = null;
        try {
            systemParam = SelfCareCache.getSystemParamByMask("CURRENCY_PRECISION");
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testGetSystemParamByMask systemParam = [{}]", systemParam);

        logger.debug("testGetSystemParamByMask end.");
    }

    
    /**
     * Description: <br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQryMenuList() {
        logger.debug("testQryMenuList start.");
        try {
            List<MenuDto> menu = SelfCareCache.qryMenuList("1");
            logger.debug("testQryMenuList menu = [{}]", menu);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQryMenuList end.");
    }   

    
    /**
     * Description: <br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     */ 
    @Test
    public void testQryAthenaAcctRes() {
        logger.debug("testQryAthenaAcctRes start.");
        try {
            DynamicDict athAcctRes = SelfCareCache.qryAthenaAcctRes("TIMES");
            logger.debug("testQryAthenaAcctRes athAcctRes = [{}]", athAcctRes);
        }
        catch (Exception e) {
            logger.debug(e);
        }
        logger.debug("testQryAthenaAcctRes end.");
    }
    

}
