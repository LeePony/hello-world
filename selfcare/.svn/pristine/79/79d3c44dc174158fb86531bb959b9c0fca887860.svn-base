package com.ztesoft.zsmart.bss.selfcare.dao;

import java.util.List;

import com.ztesoft.zsmart.bss.selfcare.model.AthSelfOperLogDto;
import com.ztesoft.zsmart.bss.selfcare.model.TransferLogDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;

/**
 * <Description> <br>
 * 
 * @author zhang.liang<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月27日 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.selfcare.dao <br>
 */
public interface ITransferLogDAO {
    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param dto <br>
     * @throws BaseAppException <br>
     */
    public void insertTransferLog(TransferLogDto dto) throws BaseAppException;

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param subsId <br>
     * @param count <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    public List<TransferLogDto> qryNearly4Records(Long subsId, Long count) throws BaseAppException;

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param subsId <br>
     * @param count <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    public List<String> qryNearlyNbr(Long subsId, Long count) throws BaseAppException;
    
    /**
     * Description: ATH_SELF_OPER_LOG<br>
     * 
     * @author yu.lei<br>
     * @taskId <br>
     * @param dto <br>
     * @throws BaseAppException <br>
     */
    public void insertSelfOperLog(AthSelfOperLogDto dto) throws BaseAppException;
}
