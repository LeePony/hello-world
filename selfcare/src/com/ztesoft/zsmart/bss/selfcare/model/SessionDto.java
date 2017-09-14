/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.ztesoft.zsmart.bss.selfcare.model;

import java.io.Serializable;
import java.util.Date;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.AbstractDto;
import com.ztesoft.zsmart.core.jdbc.Validater;

/**
 * <Description> <br>
 * 
 * @author Xu.Fuqin<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2013-2-18 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.esc.common.model <br>
 */
public class SessionDto extends AbstractDto implements Serializable, Validater {

    /**
     * serialVersionUID <br>
     */
    private static final long serialVersionUID = 1L;

    /**
     * partyType <br>
     */
    public String partyType;

    /**
     * partyCode <br>
     */
    public String partyCode;

    /**
     * loginLogId <br>
     */
    public String loginLogId;

    /**
     * custId <br>
     */
    public String custId;

    /**
     * roleId <br>
     */
    public String roleId;

    /**
     * roleCode <br>
     */
    public String roleCode;

    /**
     * userId <br>
     */
    public String userId;

    /**
     * hqFlag <br>
     */
    public String hqFlag;

    /**
     * custGroupId <br>
     */
    public String custGroupId;

    /**
     * srcIp <br>
     */
    public String srcIp;

    /**
     * ssoUserId <br>
     */
    public String ssoUserId;

    /**
     * sessionId <br>
     */
    public String sessionId;

    /**
     * escAuthMenus <br>
     */
    public Object escAuthMenus;

    /**
     * currentMenuId <br>
     */
    public String currentMenuId;

    /**
     * currentMenuPath <br>
     */
    public String currentMenuPath;

    /**
     * escAuthMenusCacheDate <br>
     */
    public Date escAuthMenusCacheDate;

    /**
     * manager <br>
     */
    public boolean managerFlag;

    /**
     * selfSessionId:自服务侧的sessionId，上面的sessionId是sso侧的sessionId <br>
     */
    public String selfSessionId;

    /**
     * lastOperTime:上次操作时间 <br>
     */
    public Long lastOperTime;

    /**
     * passportId:访问报表服务器的passport <br>
     */
    public String passportId;

    /**
     * custName:所属公司的名称 <br>
     */
    public String custName;

    /**
     * loginPage：登陆途径，从sso登陆的，还是从自服务登陆的 <br>
     */
    public String loginPage;

    /**
     * sp_id
     */
    public Long spId;

    /**
     * userCode:登陆的user，企业客户是email，内部员工是userCode <br>
     */
    public String userCode;

    /**
     * loginAccNbr <br>
     */
    public String loginAccNbr;

    /**
     * titleName <br>
     */
    public String titleName;

    /**
     * nickName <br>
     */
    public String nickName;

    /**
     * acctId <br>
     */
    public Long acctId;
    
    public String acctCode;

    /**
     * loginSubsId
     */
    public Long loginSubsId;

    /**
     * accNbr
     */
    public String accNbr;
    
    /**
     * prefix <br>
     */
    public String prefix;

    /**
     * subsId
     */
    public Long subsId;

    public String custCode;

    public String offerCharge;
    
    /**
     * paidFlag
     */
    public String paidFlag;

    public String getLoginAccNbr() {
        return loginAccNbr;
    }

    public void setLoginAccNbr(String loginAccNbr) {
        this.loginAccNbr = loginAccNbr;
    }

    public Long getLoginSubsId() {
        return loginSubsId;
    }

    public void setLoginSubsId(Long loginSubsId) {
        this.loginSubsId = loginSubsId;
    }

    public String getAccNbr() {
        return accNbr;
    }

    public void setAccNbr(String accNbr) {
        this.accNbr = accNbr;
    }

    public Long getSubsId() {
        return subsId;
    }

    public void setSubsId(Long subsId) {
        this.subsId = subsId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public Long getLastOperTime() {
        return lastOperTime;
    }

    public void setLastOperTime(Long lastOperTime) {
        this.lastOperTime = lastOperTime;
    }

    public String getSelfSessionId() {
        return selfSessionId;
    }

    public void setSelfSessionId(String selfSessionId) {
        this.selfSessionId = selfSessionId;
    }

    public boolean isManagerFlag() {
        return managerFlag;
    }

    public void setManagerFlag(boolean managerFlag) {
        this.managerFlag = managerFlag;
    }

    /**
     * pwdRemindExp <br>
     */
    public String pwdRemindExp;

    /**
     * pwdRemindExpDesc <br>
     */
    public String pwdRemindExpDesc;

    public String getPwdRemindExp() {
        return pwdRemindExp;
    }

    public void setPwdRemindExp(String pwdRemindExp) {
        this.pwdRemindExp = pwdRemindExp;
    }

    public String getPwdRemindExpDesc() {
        return pwdRemindExpDesc;
    }

    public void setPwdRemindExpDesc(String pwdRemindExpDesc) {
        this.pwdRemindExpDesc = pwdRemindExpDesc;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getLoginLogId() {
        return loginLogId;
    }

    public void setLoginLogId(String loginLogId) {
        this.loginLogId = loginLogId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHqFlag() {
        return hqFlag;
    }

    public void setHqFlag(String hqFlag) {
        this.hqFlag = hqFlag;
    }

    public String getCustGroupId() {
        return custGroupId;
    }

    public void setCustGroupId(String custGroupId) {
        this.custGroupId = custGroupId;
    }

    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public String getSsoUserId() {
        return ssoUserId;
    }

    public void setSsoUserId(String ssoUserId) {
        this.ssoUserId = ssoUserId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Object getEscAuthMenus() {
        return escAuthMenus;
    }

    public void setEscAuthMenus(Object escAuthMenus) {
        this.escAuthMenus = escAuthMenus;
    }

    public String getCurrentMenuId() {
        return currentMenuId;
    }

    public void setCurrentMenuId(String currentMenuId) {
        this.currentMenuId = currentMenuId;
    }

    public String getCurrentMenuPath() {
        return currentMenuPath;
    }

    public void setCurrentMenuPath(String currentMenuPath) {
        this.currentMenuPath = currentMenuPath;
    }

    public Date getEscAuthMenusCacheDate() {
        return escAuthMenusCacheDate;
    }

    public void setEscAuthMenusCacheDate(Date escAuthMenusCacheDate) {
        this.escAuthMenusCacheDate = escAuthMenusCacheDate;
    }

    public Long getSpId() {
        return spId;
    }

    public void setSpId(Long spId) {
        this.spId = spId;
    }

    /**
     * Description: <br>
     * 
     * @author Jia.Ziran <br>
     * @taskId <br>
     * @return <br>
     */
    public String getTitleName() {
        return titleName;
    }

    /**
     * Description: <br>
     * 
     * @author Jia.Ziran <br>
     * @taskId <br>
     * @param titleName <br>
     */
    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    /**
     * Description: <br>
     * 
     * @author Jia.Ziran <br>
     * @taskId <br>
     * @return <br>
     */
    public Long getAcctId() {
        return acctId;
    }

    /**
     * Description: <br>
     * 
     * @author Jia.Ziran <br>
     * @taskId <br>
     * @param acctId <br>
     */
    public void setAcctId(Long acctId) {
        this.acctId = acctId;
    }

    /**
     * Description: <br>
     * 
     * @author Jia.Ziran <br>
     * @taskId <br>
     * @return nickName <br>
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Description: <br>
     * 
     * @author Jia.Ziran <br>
     * @taskId <br>
     * @param nickName <br>
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAcctCode() {
        return acctCode;
    }

    public void setAcctCode(String acctCode) {
        this.acctCode = acctCode;
    }

    /**
     * 校验函数
     * 
     * @throws BaseAppException 应用异常
     */
    public void validate() throws BaseAppException {
        throw new java.lang.UnsupportedOperationException();
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getOfferCharge() {
        return offerCharge;
    }

    public void setOfferCharge(String offerCharge) {
        this.offerCharge = offerCharge;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPaidFlag() {
        return paidFlag;
    }

    public void setPaidFlag(String paidFlag) {
        this.paidFlag = paidFlag;
    }

}
