/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.ztesoft.zsmart.bss.selfcare.common.helper;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.ztesoft.zsmart.bss.common.config.bll.ConfigItemCache;
import com.ztesoft.zsmart.bss.selfcare.common.cache.SelfCareCache;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.web.util.SystemParam;

/**
 * <Description> <br>
 * 
 * @author ji.zhiwei<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2015年11月26日 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.bizcommon.coreapi.helper <br>
 */

public final class CurrencyHelper {

    /**
     * Description: <br>
     * 根据货币精度格式化货币
     * 
     * @author ji.zhiwei<br>
     * @taskId <br>
     * <br>
     */
    private CurrencyHelper() {

    }

    /**
     * Description: <br>
     * 根据货币精度格式化货币
     * 
     * @author zang.yunqiang<br>
     * @taskId <br>
     * @param dbValue 数据库中的货币额度
     * @return 格式化后的货币
     * @throws BaseAppException <br>
     */
    public static String toFloatSum(String dbValue) throws BaseAppException {
        if (StringUtil.isEmpty(dbValue)) {
            return "";
        }
        String currency = SelfCareCache.getSystemParamByMask("CURRENCY_PRECISION");

        if (StringUtil.isEmpty(currency)) {
            return dbValue;
        }
        Long currencyPrecision = Long.valueOf(currency);
        if (currencyPrecision == null || currencyPrecision.longValue() == 0) {
            return dbValue;
        }
        Double doubleNum = new Double(dbValue);
        doubleNum = new Double(doubleNum.doubleValue() / Math.pow(10, currencyPrecision.doubleValue()));
        String strNum = doubleNum.toString();
        StringBuffer s = new StringBuffer();
        s.append("#########0");
        int i = currencyPrecision.intValue();
        if (i > 0) {
            if (doubleNum.toString().indexOf(".") >= 0) {
                strNum = strNum + "00000000000000000000";
            }
            else {
                strNum = strNum + ".00000000000000000000";
            }
            // 科学计数法
            if (strNum.indexOf("E") < 0) {
                strNum = strNum.substring(0, strNum.indexOf(".") + 1 + i);
                doubleNum = Double.valueOf(strNum);
            } 
            s.append(".");
            while (i > 0) {
                s.append("0");
                i--;
            }
        }
        DecimalFormat df1 = new DecimalFormat(s.toString());
        return df1.format(doubleNum);
    }
    
    /**
     * Description: <br> 
     *  按照指定小数长度，进行小数补齐或者截取
     * @author zhang.xiaofei<br>
     * @taskId <br>
     * @param d 输入数据
     * @param n 小数长度
     * @param w 取整方式
     * @return <br>
     */
    private static String processDecimal(double d, int n, int w) {
        // double值超出一定范围，转成String会是科学计数法，改成用BigDecimal
        BigDecimal bigDecimal = new BigDecimal(d);
        if (n < 0) {
            n = 0;
        }
        BigDecimal setScale = new BigDecimal(d);
        if (w == 2) {
            setScale = bigDecimal.setScale(n, BigDecimal.ROUND_UP);
        }
        else if (w == 3) {
            setScale = bigDecimal.setScale(n, BigDecimal.ROUND_HALF_UP);
        }
        else {
            setScale = bigDecimal.setScale(n, BigDecimal.ROUND_DOWN);
        }
         
        return setScale.toPlainString();
      
    }
    
    public static String processCurreny(Long input , Long currency) throws BaseAppException{
        double d =0d;
        double rate = 1.0d;
        String currencyPrecision = SelfCareCache.getSystemParamByMask("CURRENCY_PRECISION");;
        if(currency>0){
            rate = rate / currency;
        }else{
            rate = rate * currency;
        }
        double out = input * rate;
        return processDecimal(out,Integer.valueOf(currencyPrecision),3);
        
    }
    
    public static String processCurreny(Long input , Long currency, int w) throws BaseAppException{
        double d =0d;
        double rate = 1.0d;
        String currencyPrecision = SelfCareCache.getSystemParamByMask("CURRENCY_PRECISION");;
        if(currency>0){
            rate = rate / currency;
        }else{
            rate = rate * currency;
        }
        double out = input * rate;
        return processDecimal(out,Integer.valueOf(currencyPrecision),w);
        
    }
}
