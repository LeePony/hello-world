package com.ztesoft.zsmart.bss.selfcare.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thoughtworks.xstream.core.util.Base64Encoder;
import net.sf.json.JSONObject;

import com.ztesoft.zsmart.bss.bizcommon.adapter.helper.CustHelper;
import com.ztesoft.zsmart.bss.bizcommon.adapter.helper.SubsHelper;
import com.ztesoft.zsmart.bss.bizcommon.coreapi.util.ListUtil;
import com.ztesoft.zsmart.bss.cc.abe.profile.Cert;
import com.ztesoft.zsmart.bss.cc.abe.profile.Cust;
import com.ztesoft.zsmart.bss.cc.abe.profile.Subs;
import com.ztesoft.zsmart.bss.common.config.bll.ConfigItemCache;
import com.ztesoft.zsmart.bss.common.helper.SecurityHelper;
import com.ztesoft.zsmart.bss.selfcare.common.cache.SelfCareCache;
import com.ztesoft.zsmart.bss.selfcare.common.util.MvcBoUtil;
import com.ztesoft.zsmart.bss.selfcare.model.CertTypeDto;
import com.ztesoft.zsmart.bss.selfcare.model.SessionDto;
import com.ztesoft.zsmart.bss.selfcare.service.CustServices;
import com.ztesoft.zsmart.core.configuation.ConfigurationMgr;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.core.utils.DateUtil;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.mvc.core.helper.BoUtil;
import com.ztesoft.zsmart.web.resource.Common;


/**
 * <Description> <br>
 * 
 * @author yao.yueqing<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月18日 <br>
 * @since CRM8.1 <br>
 * @see com.ztesoft.zsmart.bss.selfcare.controller <br>
 */
@Controller
public class CustController {

    /**
     * channelMap <br>
     */
    public static Map<String, String> channelMap = new HashMap<String, String>();

    static {
        channelMap.put("CSR", "1");
        channelMap.put("USSD", "5");
        channelMap.put("SMS", "4");
        channelMap
            .put(
                "CALL_CENTER",
                ConfigItemCache.instance().getString("CUSTOMER_CARE/ATHENA_CC_PUBLIC/CALL_CENTER_CONTACT_CHANNEL_ID",
                    "15"));
        channelMap.put("SELFCARE",
            ConfigItemCache.instance().getString("CUSTOMER_CARE/ATHENA_CC_PUBLIC/SELFCARE_CONTACT_CHANNEL_ID", "3"));
        
    }

    /**
     * logger <br>
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(CustController.class);

    /**
     * Description: <br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     * @param request <br>
     * @return JSONObject
     * @throws BaseAppException <br>
     */ 
    @RequestMapping("changePassword")
    @ResponseBody
    public JSONObject changePassword(HttpServletRequest request) throws BaseAppException {
        JSONObject result = new JSONObject();
        DynamicDict dict = MvcBoUtil.requestToBO(request);

        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);

        DynamicDict cust = CustServices.qryCustById(Long.valueOf(session.getCustId()));

        dict.set("CUST_CODE", cust.getString("CUST_CODE"));

        result.put("CODE", "0");
        if (cust != null) {
            String oldCustPwd = cust.getString("PWD");
            String newPwd = SecurityHelper.dESEncrypt(dict.getString("PWD"));
            String oldPwd = SecurityHelper.dESEncrypt(dict.getString("OLD_PWD"));
            if (!oldPwd.equals(oldCustPwd)) {
                result.put("CODE", "1");
                result.put("MSG", "The old password you have entered is not correct.");
            }
            if (oldPwd.equals(newPwd)) {
                result.put("CODE", "1");
                result.put("MSG", "New password can not equals with old password.");
            }
        }
        CustServices.modCust(dict);
        return result;
    }

    /**
     * Description: <br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     * @param request <br>
     * @return JSONObject
     * @throws BaseAppException 
     */ 
    @RequestMapping("qryCustDetail")
    @ResponseBody
    public JSONObject qryCustDetail(HttpServletRequest request) throws BaseAppException {
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        DynamicDict cust = CustServices.qryCustById(Long.valueOf(session.getCustId()));
        DynamicDict custTypeDict = CustServices.qryCustType(cust.getString("CUST_TYPE"));
        if (custTypeDict != null) {
            cust.set("CUST_TYPE_NAME", custTypeDict.getString("CUST_TYPE_NAME"));
        }
        if (cust != null) {
            Date birthday = cust.getDate("BIRTHDAY_DAY");
            if (birthday != null) {
                String birthdayStr = DateUtil.date2String(birthday, DateUtil.DATE_FORMAT_1);
                cust.set("BIRTHDAY_DAY", birthdayStr);
            }
            
            DynamicDict occ = SelfCareCache.getOccupationById(cust.getLong("OCCUPATION_ID"));
            if (occ != null) {
                cust.set("OCCUPATION_NAME", occ.getString("OCCUPATION_NAME"));
            }

        }
        if (cust.getLong("CERT_ID") != null) {
            Long custId = Long.valueOf(session.getCustId());
            DynamicDict cert = CustServices.qryCertById(custId, cust.getLong("CERT_ID"));
            if (cert != null) {
                CertTypeDto dto = SelfCareCache.getCertTypeById(cert.getLong("CERT_TYPE_ID"));
                cert.set("CERT_TYPE_NAME", dto.getCertTypeName());
            }
            cust.set("CERT", cert);
        }
        cust.remove("PWD");
        return BoUtil.boToJson(cust);
    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param request <br>
     * @return JSONObject
     * @throws BaseAppException
     */
    @RequestMapping("modCust")
    @ResponseBody
    public JSONObject modCust(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        DynamicDict dict;
        result.put("CODE", "0");
        try {
            dict = MvcBoUtil.requestToBO(request);
            dict.set("CUST_CODE", session.getCustCode());
            dict.set("CUST_ID", session.getCustId());
            dict.set("SP_ID", session.getSpId());
            //添加自服务渠道
            dict.set("CHANNEL", "20");
            String custName = dict.getString("CUST_NAME");
            if (StringUtil.isNotEmpty(custName)) {
                session.setCustName(custName);
            }
            CustServices.modCust(dict);
        }
        catch (BaseAppException e) {
			String errCode = e.getCode();
            logger.error(e);
            result.put("CODE", "1");
            result.put("MSG", e.getMessage());
			if ("S-ATHENA-SALES-00014".equals(errCode)) {
				result.put("CODE", "2");
			}
        }

        return result;
    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param request <br>
     * @return JSONObject <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("qryOccupation")
    @ResponseBody
    public JSONObject qryOccupation(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        List<DynamicDict> list = SelfCareCache.qryOccupation();
        dict.set("OCCUPATION_LIST", list);
        return BoUtil.boToJson(dict);
    }

    /**
     * Description: <br>
     * 
     * @author yao.yueqing<br>
     * @taskId <br>
     * @param request <br>
     * @return JSONObject <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("qryContactHistory")
    @ResponseBody
    public JSONObject qryCustContactHistory(HttpServletRequest request) throws BaseAppException {
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        String custId = session.getCustId();
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        DynamicDict qryDict = new DynamicDict();
        qryDict.set("CUST_ID", custId);

        if (dict.getString("CHANNEL") != null) {
            qryDict.set("CONTACT_CHANNEL_ID", channelMap.get(dict.getString("CHANNEL")));
        }
        if (dict.getString("MONTHS") != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(System.currentTimeMillis()));
            calendar.add(Calendar.MONTH, dict.getLong("MONTHS").intValue());
            qryDict.set("START_DATE", calendar.getTime());
        }
        else {
            //1027556，在created上面进行加减操作，可以避免走到created_date索引，强制走cust_id索引
            //对sql服务里的CREATED_DATE+1，同时传入的START_DATE和END_DATE也进行+1操作
            if (dict.getDate("START_DATE") != null) {
                java.sql.Date startDatebak = dict.getDate("START_DATE");
                startDatebak = DateUtil.offsetDay(startDatebak, 1);
                qryDict.set("START_DATE", startDatebak);
            }
            if (dict.getDate("END_DATE") != null) {
                java.sql.Date endDatebak = dict.getDate("END_DATE");
                //1027556，原本就有+1操作，所以在此基础上改为+2
                endDatebak = DateUtil.offsetDay(endDatebak, 2);
                qryDict.set("END_DATE", endDatebak);
            }
        }
        DynamicDict returnDict = new DynamicDict();
        returnDict.set("CUST_CONTACT_LIST", CustServices.qryContactHistory(qryDict));
        return BoUtil.boToJson(returnDict);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     */
    @RequestMapping("CheckCustNRC")
    @ResponseBody
    public JSONObject checkCustNRC(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        try {
            DynamicDict dict = new DynamicDict();
            dict.set("CUST_ID", session.getCustId());
            dict.serviceName = "QryCustNRCByCustId{PN}Athena";
            ServiceFlow.callService(dict);

            @SuppressWarnings("unchecked")
            List<DynamicDict> imgList = dict.getList("IMAGE_LIST");
            DynamicDict retDict = new DynamicDict();
            if (imgList != null && !imgList.isEmpty()) {
                retDict.set("UPLOAD_NRC", "Y");
            }
            else {
                retDict.set("UPLOAD_NRC", "N");
            }
            result = BoUtil.boToJson(retDict);
        }
        catch (BaseAppException e) {
            logger.error(e);
        }

        return result;
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     */
    @RequestMapping("qryCustImage")
    @ResponseBody
    public JSONObject qryCustImage(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        try {
            DynamicDict dict = new DynamicDict();
            dict.set("CUST_ID", session.getCustId());
            dict.serviceName = "QryCustNRCByCustId{PN}Athena";
            ServiceFlow.callService(dict);

            @SuppressWarnings("unchecked")
            List<DynamicDict> imgList = dict.getList("IMAGE_LIST");
            DynamicDict retDict = new DynamicDict();
            if (imgList != null && !imgList.isEmpty()) {
                retDict.set("IMAGE_LIST", imgList);
            }
            result = BoUtil.boToJson(retDict);
        }
        catch (BaseAppException e) {
            logger.error(e);
        }

        return result;
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @return JSONObject <br>
     * @throws BaseAppException <br>
     */
    @RequestMapping("submitCustNRC")
    @ResponseBody
    public JSONObject submitCustNRC(HttpServletRequest request) throws BaseAppException {
        JSONObject result = new JSONObject();
        SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
        result.put("CODE", "0");
      //1033240 上传照片前校验是否更新了客户名和证件号
        checkCustInfo(session);
        try {
            DynamicDict dict = MvcBoUtil.requestToBO(request);
            dict.set("CUST_CODE", session.getCustCode());
            dict.set("CUST_ID", session.getCustId());
            dict.set("SP_ID", session.getSpId());
            
            byte[] frontResult = null;
            if (StringUtil.isNotEmpty(dict.getString("FRONT_FILE_NAME"))) {
                // 读取文件
                frontResult = getImage(dict.getString("FRONT_FILE_NAME"), dict.getString("FRONT_FILE_URL"), dict
                    .getLong("FRONT_FILE_SIZE").intValue());
            }
           
            byte[] backResult = null;
            if (StringUtil.isNotEmpty(dict.getString("BACK_FILE_NAME"))) {
                backResult = getImage(dict.getString("BACK_FILE_NAME"), dict.getString("BACK_FILE_URL"),
                    dict.getLong("BACK_FILE_SIZE").intValue());
            }

            // 新增证件,我自己新增一种类型，用来标识证件照的正反面
            String imageType = "Z";
            String isPhoto = "T";
            List<DynamicDict> imageList = new ArrayList<DynamicDict>();
            
            if (frontResult != null && frontResult.length > 0) {
                DynamicDict front = new DynamicDict();
                front.set("CUST_ID", session.getCustId());
                front.set("IMAGE_NAME", dict.getString("FRONT_FILE_NAME"));
                front.set("IMAGE_TYPE", imageType);
                front.set("IS_PHOTO", isPhoto);
                front.set("CHANNEL", 2L);
                String frontCont = new Base64Encoder().encode(frontResult);
                front.set("IMG_CONT", frontCont);
                imageList.add(front);
            }

            if (backResult != null && backResult.length > 0) {
                DynamicDict back = new DynamicDict();
                back.set("CUST_ID", session.getCustId());
                back.set("IMAGE_NAME", dict.getString("BACK_FILE_NAME"));
                back.set("IMAGE_TYPE", imageType);
                back.set("IS_PHOTO", isPhoto);
                back.set("CHANNEL", 2L);
                String backCont = new Base64Encoder().encode(backResult);
                back.set("IMG_CONT", backCont);
                imageList.add(back);
            }

            if (imageList.isEmpty()) {
                logger.error("Not find image, imageList is null");
                ExceptionHandler.publish("SC_NOT_FOUND_IMAGE", ExceptionHandler.BUSS_ERROR);
            }
            dict.set("IMAGE_LIST", imageList);
            dict.serviceName = "AddCustNRC{PN}Athena";
            ServiceFlow.callService(dict);

            // 提交成功以后将本地文件删除
            deleteFile(dict.getString("FRONT_FILE_NAME"), dict.getString("FRONT_FILE_URL"));
            deleteFile(dict.getString("BACK_FILE_NAME"), dict.getString("BACK_FILE_URL"));
//            
//            List<DynamicDict> oldFileList = new ArrayList<DynamicDict>();
//            DynamicDict oldFrontFile = dict.getBO("OLD_FRONT_NRC");
//            DynamicDict oldBackFile = dict.getBO("OLD_BACK_NRC");
//            if (oldFrontFile != null && StringUtil.isNotEmpty(oldFrontFile.getString("CUST_IMAGE_ID"))) {
//                oldFileList.add(oldFrontFile);
//            }
//            if (oldBackFile != null && StringUtil.isNotEmpty(oldBackFile.getString("CUST_IMAGE_ID"))) {
//                oldFileList.add(oldBackFile);
//            }
//            
//            for (DynamicDict oldFileBo : oldFileList) {
//                oldFileBo.set("CUST_ID", session.getCustId());
//                oldFileBo.setServiceName("DelCustImageById{PN}Athena");
//                ServiceFlow.callService(oldFileBo);
//            }
        }
        catch (BaseAppException e) {
            logger.error(e);
            result.put("CODE", "1");
            result.put("MSG", e.getMessage());
        }

        return result;
    }
    /**
     * Description: <br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     * @param session <br>
     * @throws BaseAppException <br>
     */ 
    private void checkCustInfo(SessionDto session) throws BaseAppException {
        Cust cust = null;
        if (null != session.getCustId()) {
            cust = CustHelper.qryCustById(Long.valueOf(session.getCustId()));
            Cert cert = CustHelper.qryCert(Long.valueOf(session.getCustId()));
            // 判断客户证件号是否为空，客户姓名是否和号码相同
            String custName = cust.getCustName();
            List<Subs> subsList = SubsHelper.qrySubsByCustId(cust.getCustId());
            if (ListUtil.isNotEmpty(subsList)) {
                for (Subs tmp : subsList) {
                    if (cert == null || StringUtil.isEmpty(cert.getCertNbr()) 
                        || custName.equals(tmp.getPrefix() + tmp.getAccNbr()) || custName.equals(tmp.getAccNbr())) {
                        logger.warn("custName or certNbr does not update");
                        ExceptionHandler.publish("SC_CUST_INFO_NOT_UPDATE", ExceptionHandler.BUSS_ERROR);
                    }
                }
            }
        }
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param fileName <br>
     * @param filePath <br>
     * @param fileSize <br>
     * @return imgByte<br>
     * @throws BaseAppException <br>
     */
    private byte[] getImage(String fileName, String filePath, int fileSize) throws BaseAppException {

        FileInputStream in;
        byte[] imgByte;
        try {
            File file = new File(filePath + fileName);
            if (!file.exists()) {
                return null;
            }
            in = new FileInputStream(filePath + fileName);
            imgByte = new byte[(int) file.length()];
            in.read(imgByte, 0, imgByte.length);
            in.close();
        }
        catch (Exception e) {
            logger.error("Fail to add cust nrc", e);
            throw new BaseAppException("Fail to add customer NRC", e);
        }

        return imgByte;
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param fileName <br>
     * @param filePath <br>
     * @throws BaseAppException <br>
     */
    private void deleteFile(String fileName, String filePath) throws BaseAppException {
        try {
            File file = new File(filePath + fileName);
            if (file.exists()) {
                file.delete();
            }
        }
        catch (Exception e) {
            logger.error("Fail to add cust nrc", e);
            throw new BaseAppException("Fail to add customer NRC", e);
        }

    }
    
    /**
     * Description: <br> 
     *  
     * @author wang.yongs<br>
     * @taskId <br>
     * @param request <br>
     * @return <br>
     * @throws BaseAppException <br>
     */ 
    @RequestMapping("deltempCustNRC")
    @ResponseBody
    public JSONObject deltempCustNRC(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = MvcBoUtil.requestToBO(request);
        logger.debug("deltempCustNRC dict = {}", dict);
        String tempFileName = dict.getString("DEL_TEMP_CUST_NRC");
        if (StringUtil.isNotEmpty(tempFileName)) {
            String fileDirectory = ConfigurationMgr.instance().getString("upload.uploadFileDirectory");
            while (!fileDirectory.endsWith("\\\\") && !fileDirectory.endsWith("/")) { // 可能需要添加两次,所以用while
                fileDirectory += File.separator;
            }
            File file = new File(fileDirectory + tempFileName);
            boolean delRtn = file.delete();
            logger.debug("delete result = {}" , delRtn);
        }
        return MvcBoUtil.boToJson(dict);
    }
}
