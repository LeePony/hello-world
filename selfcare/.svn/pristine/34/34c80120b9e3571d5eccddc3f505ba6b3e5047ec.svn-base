package com.ztesoft.zsmart.bss.selfcare.common.helper;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.JdbcUtil;
import com.ztesoft.zsmart.core.utils.AssertUtil;
import com.ztesoft.zsmart.core.utils.SeqUtil;
import com.ztesoft.zsmart.core.utils.ValidateUtil;

/**
 * 
 * <Description> <br> 
 *  
 * @author ma.jianan(0027001157)<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2014年5月20日 <br>
 * @since V8<br>
 * @see com.ztesoft.zsmart.bss.feature.helper <br>
 */
public final class SeqHelper {

    /**
     * Description: 获取单个的Sequence<br>
     * 
     * @author ma.jianan<br>
     * @taskId <br>
     * @param seqName 序列名称
     * @return Long
     * @throws BaseAppException <br>
     */
    public static Long getSeq(String seqName) throws BaseAppException {
        AssertUtil.isNotNull(seqName, "seqName is null");
        return SeqUtil.longValue(JdbcUtil.getDbIdentifier(), null, seqName);
    }

    /**
     * 批量获取Sequence
     * 
     * @param sequenceName 序列名
     * @param count 数量
     * @return Long[]
     * @throws BaseAppException <br>
     */
    public static Long[] getBatchSeq(String sequenceName, int count) throws BaseAppException {
        ValidateUtil.notNull(sequenceName, "sequenceName is null");
        ValidateUtil.isTrue(count > 0, "sequenceCount < 0");

        Long[] seqList = new Long[count];
        for (int i = 0; i < count; i++) {
            seqList[i] = SeqUtil.longValue(JdbcUtil.getDbIdentifier(), null, sequenceName);
        }
        return seqList;
    }

    /**
     * Description: <br>
     * 
     * @author ma.jianan<br>
     * @taskId <br>
     * @param tableName 表名
     * @param id id
     * @param idName idName
     * @param colName colName
     * @return Long
     * @throws BaseAppException <br>
     */
    public static Long maxSeq(String tableName, Long id, String idName, String colName) throws BaseAppException {
        return SeqUtil.maxSeq(JdbcUtil.getDbIdentifier(), null, tableName, id, idName, colName);
    }

    /**
     * Description: <br>
     * 
     * @author ma.jianan<br>
     * @taskId <br>
     * @param tableName 表名
     * @param primaryKeyColName 字段名
     * @param primaryKeyValue 字段值
     * @param secondKeyColName 字段名
     * @param secondKeyValue 字段值
     * @return Long
     * @throws BaseAppException <br>
     */
    public static Long maxSeq(String tableName, String primaryKeyColName, Long primaryKeyValue,
        String secondKeyColName, Long secondKeyValue) throws BaseAppException {
        return SeqUtil.maxSeq(JdbcUtil.getDbIdentifier(), null, tableName, primaryKeyColName, primaryKeyValue,
            secondKeyColName, secondKeyValue);
    }

    /**
     * Description: <br>
     * 
     * @author ma.jianan<br>
     * @taskId <br>
     * @param tableName 表名
     * @param columnName 字段名
     * @return Long
     * @throws BaseAppException <br>
     */
    public static Long getMaxValue(String tableName, String columnName) throws BaseAppException {
        return SeqUtil.getMaxValue(JdbcUtil.getDbIdentifier(), null, tableName, columnName);
    }

    /**
     * CcSeqHelper
     */
    private SeqHelper() {
    }
}
