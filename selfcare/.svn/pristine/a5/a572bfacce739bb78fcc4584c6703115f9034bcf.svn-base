package test.com.ztesoft.zsmart.bss.selfcare.common.helper;

import org.junit.Test;

import com.ztesoft.zsmart.bss.selfcare.common.helper.CommonHelper;
import com.ztesoft.zsmart.core.exception.BaseAppException;
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
public class CommonHelperTest {
    /** logger */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(CommonHelperTest.class);
    
    /**
     * Description: 校验email格式<br>
     * 
     * @author wang.yongs<br>
     * @taskId <br>
     */
    @Test
    public void testCheckEmailFormat() {
        logger.debug("testCheckEmailFormat start.");
        //正确Email格式：1234@qq.com  错误格式：www.baidu.com
        boolean emailFlag = CommonHelper.checkEmailFormat("www.bai.com");
        logger.debug("testCheckEmailFormat emailFlag = [{}]", emailFlag);

        logger.debug("testCheckEmailFormat end.");
    }

    /**
     * Description: <br>
     * 
     * @author wang.yongs<br>
     * @taskId <br>
     */
    @Test
    public void testHexToByte() {
        logger.debug("testHexToByte start.");
        byte[] target = CommonHelper.hexToByte("aa");
        logger.debug("testHexToByte target = [{}]", target);

        logger.debug("testHexToByte end.");
    }

    /**
     * Description: char转换成字节 <br>
     * 
     * @author wang.yongs<br>
     * @taskId <br>
     */
    @Test
    public void testHexCharToByte() {
        logger.debug("testHexCharToByte start.");
        byte b = CommonHelper.hexCharToByte('a');
        logger.debug("testHexCharToByte b = [{}]", b);

        logger.debug("testHexCharToByte end.");
    }

    /**
     * Description: 判断数组中是否有重复值 <br>
     * 
     * @author wang.yongs<br>
     * @taskId <br>
     */
    @Test
    public void testCheckArrayRepeat() {
        logger.debug("testCheckArrayRepeat start.");
        String[] str = {"a", "b", "c", "b", "c"};
        boolean flag = CommonHelper.checkArrayRepeat(str);
        logger.debug("testCheckArrayRepeat flag = [{}]", flag);

        logger.debug("testCheckArrayRepeat end.");
    }

    /**
     * Description: 停机原因 <br>
     * 
     * @author wang.yongs<br>
     * @taskId <br>
     * @throws BaseAppException <br>
     */
    @Test(expected = BaseAppException.class)
    public void testGetBlockReasonForDisplay() throws BaseAppException {
        logger.debug("testGetBlockReasonForDisplay start.");
        
        String blockReasonStr = CommonHelper.getBlockReasonForDisplay("02000000000000");
        logger.debug("testGetBlockReasonForDisplay blockReasonStr = [{}]", blockReasonStr);

        logger.debug("testGetBlockReasonForDisplay end.");
    }
}
