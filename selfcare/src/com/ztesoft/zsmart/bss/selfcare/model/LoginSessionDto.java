/**
 * <p>Title:ZSMART </p>
 *
 * <p>Description: LoginSessionDto </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company:ztesoft </p>
 *
 * <p>Created Date:2016年9月18日16:40:56 </p>
 *
 * @version R8.0
 */
package com.ztesoft.zsmart.bss.selfcare.model;

import java.sql.Date;

/**
 * session 管理类 <br>
 * 
 * @author qi.mingjie<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年9月18日16:40:26 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.selfcare.model <br>
 */
public class LoginSessionDto {
    /**
     * subsId
     */
    private Long subsId;
    /**
     * sessionId
     */
    private String sessionId;
    
    /**
     * isLock
     */
    private String isLock;
    
    /**
     * loginFailedCount
     */
    private Long loginFailedCount;
    /**
     * createDate
     */
    private Date createDate;
    /**
     * updateDate
     */
    private Date updateDate;
    
    public Long getSubsId() {
        return subsId;
    }
    public void setSubsId(Long subsId) {
        this.subsId = subsId;
    }
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getIsLock() {
        return isLock;
    }
    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }
    public Long getLoginFailedCount() {
        return loginFailedCount;
    }
    public void setLoginFailedCount(Long loginFailedCount) {
        this.loginFailedCount = loginFailedCount;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
