package com.ztesoft.zsmart.bss.selfcare.common.core.api;

import com.ztesoft.zsmart.core.jdbc.JdbcDS;
import com.ztesoft.zsmart.core.jdbc.config.ConfigDbIdentifier;
import com.ztesoft.zsmart.core.jdbc.ds.DbIdentifier;

/**
 * <p>
 * Title: ZSMART
 * </p>
 * <p>
 * Description: JdbcUtil4CC.java
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: ztesoft
 * </p>
 * <p>
 * Created Date: 2012-7-18
 * </p>
 * 
 * @author: ZGL
 * @version: R13
 * @Task：
 * @Reason：
 */
public final class JdbcUtil4CC {
    /**
     * CC
     */
    public static final String NAME = "cc";

    /**
     * DS
     */
    private static final JdbcDS JDBCDS;

    static {
        JDBCDS = JdbcDS.forJdbcDS(NAME);
    }

    /**
     * Description: <br>
     * 
     * @author ma.jianan<br>
     * @taskId <br>
     * @return <br>
     */
    public static DbIdentifier getDbIdentifier() {
        return JDBCDS.getDbIdentifier();
    }

    /**
     * Description: <br>
     * 
     * @author ma.jianan<br>
     * @taskId <br>
     * @return <br>
     */
    public static DbIdentifier getDbBackService() {
        return JDBCDS.getDbBackService();
    }

    /**
     * Description: <br>
     * 
     * @author ma.jianan<br>
     * @taskId <br>
     * @return <br>
     */
    public static DbIdentifier getDbBilling() {
        return JDBCDS.getDbBilling();
    }

    /**
     * Description: <br>
     * 
     * @author ma.jianan<br>
     * @taskId <br>
     * @return <br>
     */
    public static DbIdentifier getDbCache() {
        return JDBCDS.getDbCache();
    }

    /**
     * Description: <br>
     * 
     * @author ma.jianan<br>
     * @taskId <br>
     * @param dbName 名称
     * @return <br>
     */
    public static ConfigDbIdentifier getDbIDConfig(String dbName) {
        return JDBCDS.getDbIDConfig(dbName);
    }

    /**
     * 私有
     */
    private JdbcUtil4CC() {
    }
}
