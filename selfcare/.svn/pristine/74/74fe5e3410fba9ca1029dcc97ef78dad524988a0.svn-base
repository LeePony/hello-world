package com.ztesoft.zsmart.bss.selfcare.common.util;

import java.util.Random;

/**
 * <Description> <br>
 * 
 * @author yao.yueqing<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月7日 <br>
 * @since CRM8.1 <br>
 * @see com.ztesoft.zsmart.bss.selfcare.common.util <br>
 */
public class RandomUtil {
    /**
     * Description: <br>
     * 
     * @author zhu.quan<br>
     * @taskId <br>
     * @return char[]<br>
     */
    public static char[] getChar() {
        char[] passwordLit = new char[62];
        char fword = 'A';
        char mword = 'a';
        char bword = '0';
        for (int i = 0; i < 62; i++) {
            if (i < 26) {
                passwordLit[i] = fword;
                fword++;
            }
            else if (i < 52) {
                passwordLit[i] = mword;
                mword++;
            }
            else {
                passwordLit[i] = bword;
                bword++;
            }
        }
        return passwordLit;
    }

    /**
     * 获取特定长度的随机码 <br>
     * 
     * @author zhu.quan<br>
     * @taskId <br>
     * @param length int
     * @return String<br>
     */
    public static String getRandomString(int length) {
        //char[] r = getChar();
        Random rr = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = rr.nextInt(10);
            sb.append(num);
        }
        return sb.toString();
    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param args <br>
     */
    public static void main(String[] args) {
        
    }
}
