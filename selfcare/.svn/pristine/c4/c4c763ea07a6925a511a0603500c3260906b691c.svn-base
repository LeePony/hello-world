package com.ztesoft.zsmart.bss.selfcare.common.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.ztesoft.zsmart.bss.selfcare.bll.SystemManager;
import com.ztesoft.zsmart.bss.selfcare.model.CertTypeDto;
import com.ztesoft.zsmart.bss.selfcare.model.MenuDto;
import com.ztesoft.zsmart.bss.selfcare.service.CustServices;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.jdbc.qdb.util.StringUtil;

/**
 * <Description> <br>
 * 
 * @author yao.yueqing<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年6月7日 <br>
 * @since CRM8.1 <br>
 * @see com.ztesoft.zsmart.bss.selfcare.common.cache <br>
 */
public class SelfCareCache {

    private static Map<Long, CertTypeDto> certTypeMap = null;

    private static List<DynamicDict> OccupationList = null;
    
    private static Map<Long,DynamicDict> occupationMap = null;
    
    private static Map<String,List<MenuDto>> menuMap = null;
    
    private static Map<String,DynamicDict> athenaAcctResMap = null;
     
    private static Map<String,String> athenaAcctItemMap =null;
    
    private static Map<String,String> systemParamMap = null;

    
    private static List<MenuDto> menuList = null;
    
    private static List<DynamicDict> adList = null;
    
    public static Boolean flag = false;

    public static CertTypeDto getCertTypeById(Long certTypeId) throws BaseAppException {
        if (certTypeMap ==null) {
            certTypeMap = new HashMap<Long, CertTypeDto>();
            List<CertTypeDto> list = CustServices.qryAllCertType();
            if (list != null && !list.isEmpty()) {
                for (CertTypeDto certTypeDto : list) {
                    certTypeMap.put(certTypeDto.getCertTypeId(), certTypeDto);
                }
            }
        }
        return certTypeMap.get(certTypeId);

    }

    
    
    public static List<DynamicDict> qryOccupation() throws BaseAppException {
        if (OccupationList == null) {
            OccupationList = CustServices.qryOccupation();
        }
        return OccupationList;
    }

    public static List<MenuDto> qryMenuList(String paidFlag) throws BaseAppException {
        if(menuMap== null||!menuMap.containsKey(paidFlag)){
            menuMap = new HashMap<String,List<MenuDto>>();
            menuList = new SystemManager().qryAllScMenu(paidFlag);
            List<MenuDto> nodeList = new ArrayList<MenuDto>();
            for (MenuDto node1 : menuList) {
                boolean mark = false;
                for (MenuDto node2 : menuList) {
                    
                    if (node1.getParentMenuId() != null && node1.getParentMenuId().equals(node2.getMenuId())) {                  
                        mark = true;
                        if (node2.getMenu() == null) {
                            node2.setMenu(new ArrayList<MenuDto>());
                        }                   
                        node2.getMenu().add(node1);
                        node2.setAttr("iconfont icon-jianhao has-child");
                        break;
                    }
                }
                if (!mark) {                
                    nodeList.add(node1);
                }
            }
            menuMap.put(paidFlag, nodeList);
        }
        

        return menuMap.get(paidFlag);
    }
    
    public static DynamicDict getOccupationById(Long id) throws BaseAppException{
        if(occupationMap==null){
            occupationMap = new HashMap<Long , DynamicDict>();
            List<DynamicDict> list= qryOccupation();
            if(list!=null&&!list.isEmpty()){
                for(DynamicDict tmp :list){
                    occupationMap.put(tmp.getLong("OCCUPATION_ID"), tmp);
                }
            }
        }
        
        return occupationMap.get(id);
    }
    
    public static List<DynamicDict> qryScAthenaImage(String path) throws BaseAppException{
        if(adList == null){
            synchronized (flag){
                if(!flag){
                   adList =  new SystemManager().qryScAthenaImage(path);
                   flag = true;
                }
            }
        }
        
        return adList;
    }
    
    public static DynamicDict qryAthenaAcctRes(String unitType) throws BaseAppException{
        if(athenaAcctResMap==null||athenaAcctResMap.isEmpty()){
            athenaAcctResMap = new HashMap<String,DynamicDict>();
            List<DynamicDict> acctResList = new SystemManager().qryAthenaAcctRes();
            if(acctResList!=null && !acctResList.isEmpty()){
                for(DynamicDict dict : acctResList){
                    athenaAcctResMap.put(dict.getString("UNIT_TYPE").toUpperCase(), dict);
                }
            }
        }
        return athenaAcctResMap.get(unitType);
    }
    
    public static Map<String,String> qryAthenaAcctItemBiz() throws BaseAppException{
        if(athenaAcctItemMap ==null||athenaAcctItemMap.isEmpty()){
            athenaAcctItemMap = new HashMap<String,String>();
            List<DynamicDict> acctItemList = new SystemManager().qryAthenaAcctItemBiz();
            if(acctItemList!=null&&!acctItemList.isEmpty()){
                for(DynamicDict dict : acctItemList){
                    String acctItemIdStr = athenaAcctItemMap.get(dict.getString("BIZ_TYPE"));
                    if(StringUtil.isEmpty(acctItemIdStr)){
                        acctItemIdStr = ",";
                    }
                    
                    acctItemIdStr +=dict.getString("ACCT_ITEM_TYPE_ID") +",";
                                    
                    athenaAcctItemMap.put(dict.getString("BIZ_TYPE"), acctItemIdStr);
                }
            }
        }
        return athenaAcctItemMap;
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param mask
     * @return
     * @throws BaseAppException  <br>
     */
    public static String getSystemParamByMask(String mask) throws BaseAppException{
        if(systemParamMap==null||systemParamMap.isEmpty()){
            systemParamMap = new HashMap<String,String>();
            List<DynamicDict> list = new SystemManager().qrySystemParamByMask();
            if(list!=null&&!list.isEmpty()){
                for(DynamicDict dict:list){
                    systemParamMap.put(dict.getString("MASK"), dict.getString("CURRENT_VALUE"));
                }
            }
        }
        return systemParamMap.get(mask);
    }
    
    public static void clearAll(){
        certTypeMap = null;
        OccupationList = null;
        menuList = null;
    }
}
