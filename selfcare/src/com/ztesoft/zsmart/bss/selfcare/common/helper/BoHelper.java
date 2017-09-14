/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.ztesoft.zsmart.bss.selfcare.common.helper;

import java.util.ArrayList;
import java.util.List;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;

/**
 * <Description> <br>
 * 
 * @author ma.jianan(0027001157)<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2014年5月26日 <br>
 * @since V8<br>
 * @see com.ztesoft.zsmart.bss.bizcommon.coreapi.helper <br>
 */
public final class BoHelper extends com.ztesoft.zsmart.core.utils.BoHelper {

    /**
     * Description: 针对BoHelper.boToListDto的封装<br>
     * 使用示例:List<Goods> goodsList = boToListDto(dict, "GOODS_LIST", Goods.class); <br>
     * 
     * @author ma.jianan<br>
     * @taskId <br>
     * @param <T> <br>
     * @param dict DynamicDict
     * @param dictListName <br>
     * @param dtoClass <br>
     * @return List<T>
     * @throws BaseAppException <br>
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> bo2ListDto(DynamicDict dict, String dictListName, Class<T> dtoClass)
        throws BaseAppException {
        if (dict == null) {
            return null;
        }

        List<Object> dictArray = dict.getList(dictListName);
        List<T> dtoArray = new ArrayList<T>(dictArray.size());

        for (Object obj : dictArray) {
            Class<?> objClass = obj.getClass();

            if (objClass.equals(DynamicDict.class)) {
                dtoArray.add((T) boToDto((DynamicDict) obj, dtoClass));
            }
            else if (objClass.equals(dtoClass)) {
                dtoArray.add((T) obj);
            }
        }

        return dtoArray;
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author xu.hong<br>
     * @taskId <br>
     * @param xmlAll String
     * @param index int index inde之前为修改完的，inde之后是需要修改的
     * @return <br>
     */
    public static String parseKeyUpper(String xmlAll, int index) {

        String xmlTemp = xmlAll.substring(index);

        // 计算左括号位置
        int indexLeft = xmlTemp.indexOf("<");
        // 计算右括号位置
        int indexRight = xmlTemp.indexOf(">");

        // 说明全部修改完了
        if (indexLeft == -1 || indexRight == -1) {
            return xmlAll;
        }

        // 左边是否有斜杠
        boolean leftHasLine = "/".equals(xmlTemp.substring(indexLeft + 1, indexLeft + 2));
        // 右边是否有斜杠
        boolean rightHasLine = "/".equals(xmlTemp.substring(indexRight - 1, indexRight));

        // 修改标签中的内容
        StringBuffer elementXml = new StringBuffer();
        if (leftHasLine) {
            elementXml.append("</");
            elementXml.append(firstLetterUpperToSlash(xmlTemp.substring(indexLeft + 2, indexRight)));
            elementXml.append(">");

        }
        else if (rightHasLine) {
            elementXml.append("<");
            elementXml.append(firstLetterUpperToSlash(xmlTemp.substring(indexLeft + 1, indexRight - 1)));
            elementXml.append("/>");
        }
        else {
            elementXml.append("<");
            elementXml.append(firstLetterUpperToSlash(xmlTemp.substring(indexLeft + 1, indexRight)));
            elementXml.append(">");
        }

        // 替换这个修改后的标签到xml中标签
        StringBuffer returnXml = new StringBuffer();
        returnXml.append(xmlAll.substring(0, index));
        if (indexLeft > 0) {

            returnXml.append(xmlAll.substring(index, index + indexLeft));
        }
        returnXml.append(elementXml.toString());
        returnXml.append(xmlAll.substring(index + indexRight + 1));

        // 继续替换下一个标签
        return parseKeyUpper(returnXml.toString(), index + indexLeft + elementXml.length());
    }

    /**
     * BoHelper <br>
     */
    private BoHelper() {
    }
}
