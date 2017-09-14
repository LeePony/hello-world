package com.ztesoft.zsmart.bss.selfcare.dao.abstractimpl;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ztesoft.zsmart.bss.common.utils.BusiBaseDAO;
import com.ztesoft.zsmart.bss.selfcare.common.helper.SeqHelper;
import com.ztesoft.zsmart.bss.selfcare.dao.ISystemDAO;
import com.ztesoft.zsmart.bss.selfcare.model.LoginSessionDto;
import com.ztesoft.zsmart.bss.selfcare.model.MenuDto;
import com.ztesoft.zsmart.bss.selfcare.model.ScLoginHis;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.BaseDAO;
import com.ztesoft.zsmart.core.jdbc.LobHelper;
import com.ztesoft.zsmart.core.jdbc.ParamArray;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.DateUtil;
import com.ztesoft.zsmart.core.utils.StringUtil;

/**
 * <Description> <br>
 * 
 * @author yao.yueqing<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月7日 <br>
 * @since CRM8.1 <br>
 * @see com.ztesoft.zsmart.bss.selfcare.dao.abstractimpl <br>
 */
@Repository
public class SystemDAO extends BusiBaseDAO implements ISystemDAO {

    @Override
    public List<MenuDto> qryScMenu(String paidFlag) throws BaseAppException {
        String sql = " SELECT A.MENU_ID,A.PARENT_MENU_ID,A.MENU_NAME NAME ,A.MENU_ICON,A.URL,A.ACTION_CODE"
            + " FROM ATHENAN_SC_MENU A " + " WHERE A.STATE = 'A' " 
            +"  AND (PAID_FLAG = ? OR PAID_FLAG IS NULL) "
            + " START WITH A.PARENT_MENU_ID  IS NULL "
            + " CONNECT BY PRIOR A.MENU_ID = A.PARENT_MENU_ID "
            + " ORDER BY A.DISPLAY_SEQ ";
        ParamArray pa = new ParamArray();
        if(StringUtil.isNotEmpty(paidFlag)){
            pa.set("", paidFlag);
        }
        
        List<MenuDto> menuList = null;

        menuList = queryListToSimpObj3(sql, pa, MenuDto.class);

        return menuList;
    }

    @Override
    public DynamicDict qryScUserByCustId(Long custId) throws BaseAppException {
        String sql = "SELECT * FROM ATHENAN_SC_USER WHERE CUST_ID = ?";
        ParamArray pa = new ParamArray();
        pa.set("", custId);
        DynamicDict user = null;
        try {
            user = query(sql, pa);
        }
        catch (SQLException e) {
            throw new BaseAppException("S-DAT-00020", e);
        }
        return user;
    }

    @Override
    public int insertScUser(DynamicDict dict) throws BaseAppException {
        String sql = " INSERT INTO ATHENAN_SC_USER(USER_ID,CUST_ID,USER_NAME,STATE,STATE_DATE,"
            + " CREATED_DATE,UPDATE_DATE,PARTY_TYPE,PARTY_CODE,NICK_NAME,SP_ID)"
            + " VALUES(?,?,?,'A',SYSDATE,SYSDATE,SYSDATE,?,?,?,?)";
        ParamArray pa = new ParamArray();
        pa.set("", SeqHelper.getSeq("ATH_SC_USER_ID_SEQ"));
        pa.set("", dict.getLong("CUST_ID"));
        pa.set("", dict.getString("CUST_NAME"));
        pa.set("", dict.getString("PARTY_TYPE"));
        pa.set("", dict.getString("PARTY_CODE"));
        pa.set("", dict.getString("NICK_NAME"));
        pa.set("", dict.getLong("SP_ID"));

        return this.executeUpdate(sql, pa);
    }

    @Override
    public int insertScLoginHis(ScLoginHis dto) throws BaseAppException {
        String sql = " INSERT INTO ATHENAN_SC_LOGIN_LOG (LOG_ID,CUST_ID,SUBS_ID,LOGIN_DATE,LOGOUT_DATE,LOGIN_IP,SP_ID)"
            + " VALUES(?,?,?,?,?,?,?)";
        ParamArray pa = new ParamArray();
        Long id = SeqHelper.getSeq("ATH_SC_LOGIN_LOG_ID_SEQ");
        dto.setLogId(id);
        pa.set("", id);
        pa.set("", dto.getCustId());
        pa.set("", dto.getSubsId());
        pa.set("", dto.getLoginDate());
        pa.set("", dto.getLogoutDate());
        pa.set("", dto.getLoginIp());
        pa.set("", dto.getSpId());

        return this.executeUpdate(sql, pa);
    }

    @Override
    public List<DynamicDict> qryScImage() throws BaseAppException {
        return null;
        // String sql = " SELECT  * " +
        // " FROM ATHENAN_SC_IMAGE A " +
        // " WHERE A.STATE = 'A' " +
        // " ORDER BY SEQ " +
        // " CONNECT BY PRIOR A.MENU_ID = A.PARENT_MENU_ID ";
        // ParamArray pa = new ParamArray();
        // List<MenuDto> menuList = null;
        //
        // menuList = queryListToSimpObj3(sql, pa, MenuDto.class);
        //
        //
        // return menuList;
    }

    @Override
    public List<DynamicDict> qryLoginHistory(String accNbr,Long subsId, Date startDate, Date endDate) throws BaseAppException {
        String sql = " SELECT * FROM ATHENAN_SC_LOGIN_LOG WHERE SUBS_ID = :SUBS_ID  "
            + "[ AND LOGIN_DATE >= :START_DATE ] "
            + "[ AND LOGIN_DATE <= :END_DATE ]"
            + "  ORDER BY LOGIN_DATE DESC ";
        ParamMap pa = new ParamMap();
        final String acc = accNbr;
        List<ScLoginHis> loginList = null;
        pa.set("SUBS_ID", subsId);
        if(startDate != null){
            pa.set("START_DATE", startDate);
        }
        if(endDate != null){
            pa.set("END_DATE", endDate);
        }
        
        return (List<DynamicDict>) this.query(sql.toString(), pa, null, null, new RowSetMapper() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                List<DynamicDict> loginList = new ArrayList<DynamicDict>();
                while (rs.next()) {
                    int flag = 1;
                    DynamicDict his = new DynamicDict();
                    his.set("ACC_NBR", acc);
                    his.set("LOGIN_DATE", DateUtil.date2String(rs.getTimestamp("LOGIN_DATE"), DateUtil.DATETIME_FORMAT_1));
                    his.set("LOGIN_IP", rs.getString("LOGIN_IP"));
                  
                    loginList.add(his);
                   
                }
                return loginList;
            }
        });
    }

    @Override
    public int updateScLoginHis(ScLoginHis dto) throws BaseAppException {
        String sql = " UPDATE ATHENAN_SC_LOGIN_LOG SET LOG_ID=:LOG_ID "
            + " [, CUST_ID = :CUST_ID]"
            + " [, SUBS_ID = :SUBS_ID]"
            + " [, LOGIN_DATE = :LOGIN_DATE]"
            + " [, LOGOUT_DATE = :LOGOUT_DATE]"
            + " [, LOGIN_IP = :LOGIN_IP]"
            + " [, SP_ID = :SP_ID]"
            + " WHERE LOG_ID = :LOG_ID ";
        ParamMap pa = new ParamMap();
        pa.set("LOG_ID", dto.getLogId());
        if(dto.getCustId()!=null){
            pa.set("CUST_ID", dto.getCustId());
        }
        if(dto.getSubsId()!=null){
            pa.set("SUBS_ID",dto.getSubsId());
        }
        if(dto.getLoginDate()!=null){
            pa.set("LOGIN_DATE", dto.getLoginDate());
        }
        if(dto.getLogoutDate()!=null){
            pa.set("LOGOUT_DATE", dto.getLogoutDate());
        }
        if(dto.getLoginIp()!=null){
            pa.set("LOGIN_IP", dto.getLoginIp());
        }
        if(dto.getSpId()!=null){
            pa.set("SP_ID", dto.getSpId());
        }
        
        updateObject(sql, pa);
        return 0;
    }

    @Override
    public List<DynamicDict> qyrScAdImage() throws BaseAppException {
        String sql = "SELECT IMAGE,IMAGE_NAME,SEQ FROM ATHENAN_SC_IMAGE WHERE STATE='A' ORDER BY SEQ ASC";
        ParamArray pa = new ParamArray();
        return query(sql, pa, null, new RowSetMapper<List<DynamicDict>>() {
            public List<DynamicDict> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                List<DynamicDict> l = new ArrayList<DynamicDict>();
                
                while(rs.next()){
                    DynamicDict tmp = new DynamicDict();
                    byte[] bytes = LobHelper.getBlobBytes(rs, 1);
                    tmp.set("IMAGE", new String(bytes));
                    tmp.set("IMAGE_NAME", rs.getString(2));
                    tmp.set("SEQ", rs.getString(3));
                    l.add(tmp);
                }
                return l;
            }

        });
    }
    @Override
    public List<DynamicDict> qryAthenaAcctRes() throws BaseAppException {
        String sql = "SELECT * FROM ATH_ACCT_RES_BIZ  ORDER BY UNIT_TYPE";
        ParamArray pa = new ParamArray();
        return query(sql, pa, null, new RowSetMapper<List<DynamicDict>>() {
            public List<DynamicDict> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                List<DynamicDict> l = new ArrayList<DynamicDict>();
                
                while(rs.next()){
                    DynamicDict tmp = new DynamicDict();
                    tmp.set("UNIT_TYPE", rs.getString("UNIT_TYPE"));
                    tmp.set("BIZ_TYPE", rs.getString("BIZ_TYPE"));
                    tmp.set("CURRENCY_PRECISION", rs.getLong("CURRENCY_PRECISION"));
                    tmp.set("SHOW_UNIT", rs.getString("SHOW_UNIT"));
                    tmp.set("SP_ID", rs.getLong("SP_ID"));
                    l.add(tmp);
                }
                return l;
            }

        });
    }

    @Override
    public List<DynamicDict> qryAthenaAcctItemBiz() throws BaseAppException {
        String sql = "SELECT * FROM ATH_ACCT_ITEM_TYPE_BIZ    ORDER BY BIZ_TYPE";
        ParamArray pa = new ParamArray();
        return query(sql, pa, null, new RowSetMapper<List<DynamicDict>>() {
            public List<DynamicDict> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                List<DynamicDict> l = new ArrayList<DynamicDict>();
                
                while(rs.next()){
                    DynamicDict tmp = new DynamicDict();
                    tmp.set("ACCT_ITEM_TYPE_ID", rs.getLong("ACCT_ITEM_TYPE_ID"));
                    tmp.set("BIZ_TYPE", rs.getString("BIZ_TYPE"));
                    tmp.set("SP_ID", rs.getLong("SP_ID"));
                    l.add(tmp);
                }
                return l;
            }

        });
    }

    @Override
    public List<DynamicDict> qrySystemParamByMask() throws BaseAppException {
        String sql = "SELECT * FROM SYSTEM_PARAM  ";
        ParamArray pa = new ParamArray();
        return query(sql, pa, null, new RowSetMapper<List<DynamicDict>>() {
            public List<DynamicDict> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                List<DynamicDict> l = new ArrayList<DynamicDict>();
                
                while(rs.next()){
                    DynamicDict tmp = new DynamicDict();
                    tmp.set("CURRENT_VALUE", rs.getString("CURRENT_VALUE"));
                    tmp.set("MASK", rs.getString("MASK"));
                    l.add(tmp);
                }
                return l;
            }

        });
    }
    
    @Override
    public int insertLoginSession(LoginSessionDto dto) throws BaseAppException {
        String sql = " INSERT INTO ATHENAN_SC_LOGIN_SESSION(SUBS_ID,SESSION_ID,IS_LOCK,LOGINFAILED_COUNT,CREATED_DATE,UPDATE_DATE)"
            + " VALUES(?,?,?,?,SYSDATE,SYSDATE)";
        ParamArray pa = new ParamArray();
        pa.set("", dto.getSubsId());
        pa.set("", dto.getSessionId());
        pa.set("", dto.getIsLock());
        pa.set("", dto.getLoginFailedCount());

        return this.executeUpdate(sql, pa);
    }
    
    /**
     * 
     * Description: 查询session <br> 
     *  
     * @author qi.mingjie<br>
     * @taskId  <br>
     * @param subsId, sessionId <br>
     * @return  list<br>
     */
    @Override
    public List<DynamicDict> qryLoginSession(Long subsId, String sessionId) throws BaseAppException {
        String sql = "SELECT SUBS_ID, SESSION_ID, IS_LOCK, LOGINFAILED_COUNT, CREATED_DATE, UPDATE_DATE FROM ATHENAN_SC_LOGIN_SESSION WHERE 1=1 "
            + "[ AND SUBS_ID = :SUBS_ID ] "
            + "[ AND SESSION_ID = :SESSION_ID ] ";
        ParamMap pa = new ParamMap();
        
        if (subsId != null) {
            pa.set("SUBS_ID", subsId);
        }
        if (sessionId != null) {
            pa.set("SESSION_ID", sessionId);
        }
        return (List<DynamicDict>) this.query(sql.toString(), pa, null, null, new RowSetMapper() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                List<DynamicDict> l = new ArrayList<DynamicDict>();
                while(rs.next()){
                    DynamicDict tmp = new DynamicDict();
                    tmp.set("SUBS_ID", rs.getLong("SUBS_ID"));
                    tmp.set("SESSION_ID", rs.getString("SESSION_ID"));
                    tmp.set("IS_LOCK",  rs.getString("IS_LOCK"));
                    tmp.set("LOGINFAILED_COUNT",  rs.getLong("LOGINFAILED_COUNT"));
                    tmp.set("CREATED_DATE",  DateUtil.date2String(rs.getTimestamp("CREATED_DATE"), DateUtil.DATETIME_FORMAT_1));
                    tmp.set("UPDATE_DATE", DateUtil.date2String(rs.getTimestamp("UPDATE_DATE"), DateUtil.DATETIME_FORMAT_1));
                    l.add(tmp);
                }
                return l;
            }
        });
    }
    
    /**
     * 
     * Description: 更新session <br> 
     *  
     * @author qi.mingjie<br>
     * @taskId  <br>
     * @param subsId, sessionId <br>
     * @return  list<br>
     */
    @Override
    public int updateLoginSession(LoginSessionDto dto) throws BaseAppException {
        String sql = " UPDATE ATHENAN_SC_LOGIN_SESSION SET SESSION_ID = :SESSION_ID "
            + " [, IS_LOCK = :IS_LOCK] "
            + " [, LOGINFAILED_COUNT = :LOGINFAILED_COUNT] "
            + " [, UPDATE_DATE = :UPDATE_DATE] "
            + " WHERE SUBS_ID = :SUBS_ID ";
        ParamMap pa = new ParamMap();
        if (dto.getSessionId() != null) {
            pa.set("SESSION_ID",dto.getSessionId());
        }
        if (dto.getIsLock() != null) {
            pa.set("IS_LOCK", dto.getIsLock());
        }
        if (dto.getLoginFailedCount() != null) {
            pa.set("LOGINFAILED_COUNT", dto.getLoginFailedCount());
        }
        if (dto.getSubsId() != null) {
            pa.set("SUBS_ID",dto.getSubsId());
        }
        if (dto.getUpdateDate() != null) {
            pa.set("UPDATE_DATE", dto.getUpdateDate());
        }
        updateObject(sql, pa);
        return 0;
    }
    
    /**
     * 
     * Description: 更新session <br> 
     *  
     * @author qi.mingjie<br>
     * @taskId  <br>
     * @param subsId, sessionId <br>
     * @return  list<br>
     */
    @Override
    public int updateLoginSessionLockState(LoginSessionDto dto) throws BaseAppException {
        String sql = " UPDATE ATHENAN_SC_LOGIN_SESSION SET UPDATE_DATE = :UPDATE_DATE"
            + " [, SESSION_ID = :SESSION_ID] "
            + " [, IS_LOCK = :IS_LOCK] "
            + " [, LOGINFAILED_COUNT = :LOGINFAILED_COUNT] "
            + " WHERE SUBS_ID = :SUBS_ID ";
        ParamMap pa = new ParamMap();
        
        if (dto.getUpdateDate() != null) {
            pa.set("UPDATE_DATE", dto.getUpdateDate());
        }
        if (dto.getSessionId() != null) {
            pa.set("SESSION_ID",dto.getSessionId());
        }
        if (dto.getIsLock() != null) {
            pa.set("IS_LOCK", dto.getIsLock());
        }
        if (dto.getLoginFailedCount() != null) {
            pa.set("LOGINFAILED_COUNT", dto.getLoginFailedCount());
        }
        if (dto.getSubsId() != null) {
            pa.set("SUBS_ID",dto.getSubsId());
        }
        updateObject(sql, pa);
        return 0;
    }
    
    /**
     * 
     * Description: 删除session <br> 
     *  
     * @author qi.mingjie<br>
     * @taskId  <br>
     * @param sessionId <br>
     * @return  int<br>
     */
    @Override
    public int deleteLoginSession(String sessionId) throws BaseAppException {
        String sql = " DELETE FROM ATHENAN_SC_LOGIN_SESSION WHERE SESSION_ID = ? ";
        ParamArray pa = new ParamArray();
        pa.set("", sessionId);
        return this.executeUpdate(sql, pa);
    }

}
