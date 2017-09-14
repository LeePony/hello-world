package com.ztesoft.zsmart.bss.selfcare.bll;

import java.util.List;

import org.mortbay.util.DateCache;

import com.ztesoft.zsmart.bss.bizcommon.coreapi.helper.BoHelper;
import com.ztesoft.zsmart.bss.bizcommon.coreapi.helper.DateHelper;
import com.ztesoft.zsmart.bss.cc.coreapi.helper.CcSeqHelper;
import com.ztesoft.zsmart.bss.selfcare.common.core.api.JdbcUtil4CC;
import com.ztesoft.zsmart.bss.selfcare.dao.ITransferLogDAO;
import com.ztesoft.zsmart.bss.selfcare.model.AthSelfOperLogDto;
import com.ztesoft.zsmart.bss.selfcare.model.TransferLogDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.DAOFactory;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.DateUtil;
import com.ztesoft.zsmart.core.utils.SeqUtil;
import com.ztesoft.zsmart.jdbc.qdb.util.StringUtil;

/**
 * <Description> <br>
 * 
 * @author zhang.liang<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月27日 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.selfcare.bll <br>
 */
public class BillingBizManager {
    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param subsId <br>
     * @param charge <br>
     * @param receveNbr <br>
     * @param spId <br>
     * @param transDict 
     * @param comments 
     * @return <br>
     * @throws BaseAppException <br>
     */
    public TransferLogDto addTransferHis(Long subsId, Long charge, String receveNbr, Long spId, String comments, DynamicDict transDict) throws BaseAppException {
        TransferLogDto dto = new TransferLogDto();
        dto.setOriSubsId(subsId);
        dto.setObjNbr(receveNbr);
        dto.setTransCharge(charge);
        dto.setSpId(spId);
        dto.setCreatedDate(DateUtil.GetDBDateTime());
        dto.setAthTransferLogId(SeqUtil.getMaxValue(JdbcUtil4CC.getDbIdentifier(), null, "ATH_TRANSFER_LOG",
            "ATH_TRANSFER_LOG_ID"));
        if (dto.getAthTransferLogId() == null) {
            dto.setAthTransferLogId(1l);
        }
        dto.setComments(comments);
        if (StringUtil.isEmpty(transDict.getString("ERROR_CODE")) && StringUtil.isEmpty(transDict.getString("ERROR_MESSAGE"))) {
        	dto.setState("S");
        }
        else {
        	dto.setState("F");
        }

        getDAO().insertTransferLog(dto);

        return dto;
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author Yu.Lei<br>
     * @taskId <br>
     * @param dict 
     * @throws BaseAppException <br>
     */
    public void addSelfOperLog(DynamicDict dict) throws BaseAppException {
    	AthSelfOperLogDto dto = (AthSelfOperLogDto) BoHelper.boToDto(dict, AthSelfOperLogDto.class);
    	dto.setSelfOperLogId(CcSeqHelper.getSeq("ATH_SELF_OPER_LOG_ID_SEQ"));
    	dto.setLogDate(DateUtil.GetDBDateTime());
    	getDAO().insertSelfOperLog(dto);
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
    public List<TransferLogDto> qryNearlyTransferHis(Long subsId, Long count) throws BaseAppException {
        return getDAO().qryNearly4Records(subsId, count);
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
        return getDAO().qryNearlyNbr(subsId, count);
    }

    private ITransferLogDAO dao = null;

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    private ITransferLogDAO getDAO() throws BaseAppException {
        if (dao == null) {
            dao = (ITransferLogDAO) DAOFactory.createModuleDAO("TransferLog", "selfcare",
                JdbcUtil4CC.getDbBackService());
        }
        return dao;
    }
}
