package com.ztesoft.zsmart.bss.selfcare.dao;

import java.util.Date;
import java.util.List;

import com.ztesoft.zsmart.bss.selfcare.model.LoginSessionDto;
import com.ztesoft.zsmart.bss.selfcare.model.MenuDto;
import com.ztesoft.zsmart.bss.selfcare.model.ScLoginHis;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;

/**
 * 
 * <Description> <br> 
 *  
 * @author yao.yueqing<br>
 * @version 1.0<br>
 * @taskId  <br>
 * @CreateDate 2016年6月7日 <br>
 * @since CRM8.1 <br>
 * @see com.ztesoft.zsmart.bss.selfcare.dao <br>
 */
public interface ISystemDAO {

    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @return List<DynamicDict> 
     * @throws BaseAppException  <br>
     */
    public List<MenuDto> qryScMenu(String paidFlag) throws BaseAppException;
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param custId 
     * @return DynamicDict 
     * @throws BaseAppException  <br>
     */
    public DynamicDict qryScUserByCustId(Long custId) throws BaseAppException;
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @return List<DynamicDict>
     * @throws BaseAppException  <br>
     */
    public List<DynamicDict> qryScImage() throws BaseAppException;
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param dict 
     * @return int 
     * @throws BaseAppException  <br>
     */
    public int insertScUser(DynamicDict dict) throws BaseAppException;
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param dto 
     * @return int 
     * @throws BaseAppException  <br>
     */
    public int insertScLoginHis(ScLoginHis dto) throws BaseAppException;
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param dto 
     * @return int 
     * @throws BaseAppException  <br>
     */
    public int updateScLoginHis(ScLoginHis dto) throws BaseAppException;
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param subsId
     * @param startDate
     * @param EndDate
     * @return List<ScLoginHis> 
     * @throws BaseAppException  <br>
     */
    public List<DynamicDict> qryLoginHistory(String accNbr,Long subsId,Date startDate,Date endDate) throws BaseAppException;
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @return List<DynamicDict>
     * @throws BaseAppException  <br>
     */
    public List<DynamicDict> qyrScAdImage() throws BaseAppException;
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @return
     * @throws BaseAppException  <br>
     */
    public List<DynamicDict> qryAthenaAcctRes() throws BaseAppException;
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @return
     * @throws BaseAppException  <br>
     */
    public List<DynamicDict> qryAthenaAcctItemBiz() throws BaseAppException;
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @return
     * @throws BaseAppException  <br>
     */
    public List<DynamicDict> qrySystemParamByMask() throws BaseAppException;
    
    /**
     * 
     * Description: <br> 
     *  
     * @author qi.mingjie<br>
     * @taskId  <br>
     * @param dto <br>
     * @return int <br>
     * @throws BaseAppException  <br>
     */
    public int insertLoginSession(LoginSessionDto dto) throws BaseAppException;
    
    /**
     * 
     * Description: <br> 
     *  
     * @author qi.mingjie<br>
     * @taskId  <br>
     * @param subsId <br> 
     * @param id <br> 
     * @return List <br> 
     * @throws BaseAppException  <br>
     */
    public List<DynamicDict> qryLoginSession(Long subsId, String sessionId) throws BaseAppException;
    
    /**
     * 
     * Description: <br> 
     *  
     * @author qi.mingjie<br>
     * @taskId  <br>
     * @param dto <br>
     * @return int <br>
     * @throws BaseAppException  <br>
     */
    public int updateLoginSession(LoginSessionDto dto) throws BaseAppException;
    
    /**
     * 
     * Description: <br> 
     *  
     * @author qi.mingjie<br>
     * @taskId  <br>
     * @param sessionId <br>
     * @return int <br>
     * @throws BaseAppException  <br>
     */
    public int deleteLoginSession(String sessionId) throws BaseAppException;
    
    /**
     * 
     * Description: 更新session <br> 
     *  
     * @author qi.mingjie<br>
     * @taskId  <br>
     * @param subsId, sessionId <br>
     * @return  list<br>
     */
    public int updateLoginSessionLockState(LoginSessionDto dto) throws BaseAppException;
}
