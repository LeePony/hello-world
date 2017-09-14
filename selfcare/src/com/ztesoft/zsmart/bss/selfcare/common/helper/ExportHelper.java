package com.ztesoft.zsmart.bss.selfcare.common.helper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ztesoft.zsmart.bss.selfcare.common.util.XLSExporter;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.web.export.ExportColumn;
import com.ztesoft.zsmart.web.export.ExportConfig;

/**
 * 
 * <Description> <br> 
 *  
 * @author yao.yueqing<br>
 * @version 1.0<br>
 * @taskId  <br>
 * @CreateDate 2016年6月29日 <br>
 * @since CRM8.1 <br>
 * @see com.ztesoft.zsmart.bss.selfcare.common.helper <br>
 */
public class ExportHelper {
    /**
     * 
     * Description: <br> 
     *  
     * @author jiao.yang<br>
     * @taskId <br>
     * @param dict dict
     * @param response dict
     * @throws IOException <br>
     * @throws BaseAppException <br>
     */
    public static void writeResult(String fileName ,JSONArray arr, List<DynamicDict> data, HttpServletResponse response) throws IOException, BaseAppException {
        
        // 根据文件后缀设置contentType
        String contentType ="txt";
        ExportConfig config = buildConfig(arr);
        response.setContentType(contentType + ";charset=UTF-8");
        response.addHeader("content-disposition", "attachment; filename=" + toUtf8(fileName));
        // response.setCharacterEncoding("UTF-8");

        OutputStream os = response.getOutputStream();
        
        XLSExporter p = new XLSExporter();
        p.init(config,os);
        p.beginExport(data);
   }
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param colArray
     * @return  <br>
     */
     private static ExportConfig buildConfig(JSONArray colArray){
         ExportConfig config = new ExportConfig();
         ExportColumn[] colum= new ExportColumn[colArray.size()];
         for(int i=0;i<colArray.size();i++){
             ExportColumn colum0= new ExportColumn();
             JSONObject o =(JSONObject)colArray.get(i);
             if(!o.containsKey("label")||!o.containsKey("name")){
                 continue;
             }
             colum0.setColumnName(o.getString("label"));
             colum0.setDataKey(o.getString("name"));
             colum0.setColumnWidth(10);
             colum[i] = colum0;
             
         }
         config.setColumns(colum);
         return config;
     }
    /**
     * 
     * Description: <br> 
     *  
     * @author yao.yueqing<br>
     * @taskId  <br>
     * @param src
     * @return  <br>
     */
    public static String toUtf8(String src) {
        // try {
        // 转换文件名不支持的字符，ie6、ie7在转换文件名不支持的字符时存在问题，所以程序自己转
        src = StringUtil.replace(src, "\\", "_");
        src = StringUtil.replace(src, "/", "_");
        src = StringUtil.replace(src, ":", "_");
        src = StringUtil.replace(src, "*", "_");
        src = StringUtil.replace(src, "?", "_");
        src = StringUtil.replace(src, "\"", "_");
        src = StringUtil.replace(src, "<", "_");
        src = StringUtil.replace(src, ">", "_");
        src = StringUtil.replace(src, "|", "_");
        return src;
    }
}
