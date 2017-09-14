package com.ztesoft.zsmart.bss.selfcare.dao.abstractimpl;

import java.util.List;

import com.ztesoft.zsmart.bss.common.utils.BusiBaseDAO;
import com.ztesoft.zsmart.bss.selfcare.dao.ITransferLogDAO;
import com.ztesoft.zsmart.bss.selfcare.model.AthSelfOperLogDto;
import com.ztesoft.zsmart.bss.selfcare.model.TransferLogDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamArray;

/**
 * <Description> <br>
 * 
 * @author zhang.liang<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月27日 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.selfcare.dao.abstractimpl <br>
 */
public class TransferLogDAO extends BusiBaseDAO implements ITransferLogDAO {

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param dto <br>
     * @throws BaseAppException <br>
     */
    public void insertTransferLog(TransferLogDto dto) throws BaseAppException {
        String sql = "INSERT INTO ATH_TRANSFER_LOG(ATH_TRANSFER_LOG_ID,ORI_SUBS_ID,OBJ_NBR,TRANS_CHARGE,CREATED_DATE,SP_ID,COMMENTS,STATE) VALUES(?,?,?,?,?,?,?,?)";

        ParamArray pa = new ParamArray();
        pa.set("", dto.getAthTransferLogId());
        pa.set("", dto.getOriSubsId());
        pa.set("", dto.getObjNbr());
        pa.set("", dto.getTransCharge());
        pa.set("", dto.getCreatedDate());
        pa.set("", dto.getSpId());
        pa.set("", dto.getComments());
        pa.set("", dto.getState());

        executeUpdate(sql, pa);
    }
    
    /**
     * Description: ATH_SELF_OPER_LOG<br>
     * 
     * @author yu.lei<br>
     * @taskId <br>
     * @param dto <br>
     * @throws BaseAppException <br>
     */
    public void insertSelfOperLog(AthSelfOperLogDto dto) throws BaseAppException {
        String sql = "INSERT INTO ATH_SELF_OPER_LOG(SELF_OPER_LOG_ID,OPER_TYPE,SUBS_ID,TRANSACTION_ID,AMOUNT,LOG_DATE,STATE,COMMENTS,SP_ID) VALUES(?,?,?,?,?,?,?,?,?)";

        ParamArray pa = new ParamArray();
        pa.set("", dto.getSelfOperLogId());
        pa.set("", dto.getOperType());
        pa.set("", dto.getSubsId());
        pa.set("", dto.getTransactionId());
        pa.set("", dto.getAmount());
        pa.set("", dto.getLogDate());
        pa.set("", dto.getState());
        pa.set("", dto.getComments());
        pa.set("", dto.getSpId());

        executeUpdate(sql, pa);
    }

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
    public List<TransferLogDto> qryNearly4Records(Long subsId, Long count) throws BaseAppException {
        String sql = "SELECT ATH_TRANSFER_LOG_ID,ORI_SUBS_ID,OBJ_NBR,TRANS_CHARGE,CREATED_DATE,SP_ID"
            + " FROM ATH_TRANSFER_LOG A WHERE A.ORI_SUBS_ID = ? AND ROWNUM <=? ORDER BY A.CREATED_DATE DESC";
        return this.selectList(sql, TransferLogDto.class, subsId, count);
    }

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
    public List<String> qryNearlyNbr(Long subsId, Long count) throws BaseAppException {
        String sql = "SELECT OBJ_NBR FROM (SELECT OBJ_NBR FROM ATH_TRANSFER_LOG WHERE ORI_SUBS_ID=?\r\n"
            + "GROUP BY OBJ_NBR ORDER BY MAX(CREATED_DATE) DESC) WHERE ROWNUM<=?";
        return this.selectList(sql, String.class, subsId, count);
    }
}
