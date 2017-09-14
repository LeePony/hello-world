package com.ztesoft.zsmart.bss.selfcare.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;






import com.ztesoft.zsmart.bss.selfcare.common.core.api.CoreDef;
import com.ztesoft.zsmart.bss.selfcare.model.SessionDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.ActionDomain;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.DateUtil;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.web.resource.Common;

/**
 * <Description> <br>
 * 
 * @author Jia.Ziran<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2013-1-6 <br>
 * @since V80<br>
 * @see com.ztesoft.zsmart.bss.esc.setup.bll <br>
 */
public final class MvcBoUtil {

    /**
     * JSON_OBJ_STR_REQUEST_PARAM <br>
     */
    public static final String JSON_OBJ_STR_REQUEST_PARAM = "__JSON_OBJ_STR_REQUEST_PARAM__";

    /**
     * 构造方法
     */
    private MvcBoUtil() {

    }

    /**
     * 将前台传过来的值转成BO
     * 
     * @param request <br>
     * @return DynamicDict <br>
     * @throws BaseAppException <br>
     */
    @SuppressWarnings({
        "rawtypes"
    })
    public static DynamicDict requestToBO(HttpServletRequest request) throws BaseAppException {
        DynamicDict dict = new DynamicDict();

        Map rmap = request.getParameterMap();
        if (rmap.containsKey(JSON_OBJ_STR_REQUEST_PARAM)) {
            Object[] valueObj = (Object[]) rmap.get(JSON_OBJ_STR_REQUEST_PARAM);
            dict.putAll(jsonStringToBo(valueObj[0].toString()));
        }
        else {
            Iterator it = rmap.entrySet().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                String key = entry.getKey().toString();
                Object[] values = (Object[]) entry.getValue();

                if (key.endsWith("[]")) {
                    key = key.substring(0, key.length() - 2);
                }
                for (Object value : values) {
                    dict.add(key, value);
                }
            }

        }

        DynamicDict sessionBo = new DynamicDict();
        HttpSession session = request.getSession();
        Enumeration en = session.getAttributeNames();
        while (en.hasMoreElements()) {
            String key = en.nextElement().toString();
            if (Common.USER_INFO_BEAN.equals(key)) {
                SessionDto dto = (SessionDto) session.getAttribute(key);
                DynamicDict bo = BoDtoTool.toBo(dto);
                if (bo != null) {
                    sessionBo.putAll(bo);
                }
            }
            else {
                sessionBo.set(key, session.getAttribute(key));
            }
        }
        // SP_ID的特殊处理
        Long spId = sessionBo.getLong("SP_ID");
        if (dict.get("SP_ID") == null) {
            dict.set("SP_ID", spId == null ? ActionDomain.DEFALUT_SP_ID : spId);
        }  
        sessionBo.set("sp-id", dict.getLong("SP_ID"));       
        dict.set(CoreDef.BO_SESSION, sessionBo);
        return dict;

    }

    /**
     * 将JSON Object String转成BO
     * 
     * @param jsonString <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    public static DynamicDict jsonStringToBo(String jsonString) throws BaseAppException {
        JSONObject jo = JSONObject.fromObject(jsonString);
        return jsonObjectToBo(jo);
    }

    /**
     * 将JSON Object转成BO
     * 
     * @param jo <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @SuppressWarnings({
        "rawtypes"
    })
    public static DynamicDict jsonObjectToBo(JSONObject jo) throws BaseAppException {
        DynamicDict bo = new DynamicDict();
        for (Iterator it = jo.entrySet().iterator(); it.hasNext();) {
            Entry entry = (Entry) it.next();
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            if (value instanceof JSONObject) {
                bo.set(key, jsonObjectToBo((JSONObject) value));
            }
            else if (value instanceof JSONArray) {
                bo.set(key, jsonArrayToList((JSONArray) value));
            }
            else if (value instanceof JSONNull) {
                continue;
            }
            else {
                bo.set(key, value);
            }
        }

        return bo;
    }

    /**
     * 将JSON Array转成List
     * 
     * @param ja <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    @SuppressWarnings({
        "rawtypes", "unchecked"
    })
    public static List jsonArrayToList(JSONArray ja) throws BaseAppException {
        List list = new ArrayList();

        for (Iterator it = ja.iterator(); it.hasNext();) {
            Object object = it.next();
            if (object instanceof JSONObject) {
                list.add(jsonObjectToBo((JSONObject) object));
            }
            else if (object instanceof JSONArray) {
                list.add(jsonArrayToList((JSONArray) object));
            }
            else {
                list.add(object);
            }
        }

        return list;
    }

    /**
     * 将BO返回为JSON格式
     * 
     * @param bo <br>
     * @return <br>
     */
    public static JSONObject boToJson(DynamicDict bo) {
        JSONObject jo = new JSONObject();
        if (bo != null) {
            HashMap<String, Object> valueMap = bo.valueMap;
            Iterator<String> iter = valueMap.keySet().iterator();
            String key;
            Object value;
            while (iter.hasNext()) {
                key = iter.next();
                value = valueMap.get(key);
                fillJsonObject(jo, key, value);
            }
            if (!StringUtil.isEmpty(bo.getServiceName())) {
                jo.put("TFM_SERVICE_NAME", bo.getServiceName());
            }
        }
        return jo;
    }

    /**
     * 将BO数组返回为JSON数组格式
     * 
     * @param boArr <br>
     * @return <br>
     */
    public static JSONArray boArrToJson(Object[] boArr) {
        JSONArray arr = new JSONArray();
        for (Object obj : boArr) {
            if (obj instanceof DynamicDict) {
                arr.add(boToJson((DynamicDict) obj));
            }
        }
        return arr;
    }

    /**
     * 将BO集合返回为JSON数组格式
     * 
     * @param boList <br>
     * @return <br>
     */
    @SuppressWarnings("rawtypes")
    public static JSONArray boListToJson(List boList) {
        JSONArray arr = new JSONArray();
        for (Object obj : boList) {
            if (obj instanceof DynamicDict) {
                arr.add(boToJson((DynamicDict) obj));
            }
        }
        return arr;
    }

    /**
     * 将调用SQL服务产生的结果集返回为JSON数组格式
     * 
     * @param dict <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    public static JSONArray boSqlResultToJson(DynamicDict dict) throws BaseAppException {
        return boResultToJson(dict, ActionDomain.BO_FIELD_QUERY_RESULT);
    }

    /**
     * 将调用服务产生的结果集返回为JSON数组格式
     * 
     * @param dict <br>
     * @param key <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    public static JSONArray boResultToJson(DynamicDict dict, String key) throws BaseAppException {
        return MvcBoUtil.boListToJson(dict.getList(key));
    }

    /**
     * 将每个属性填充到JSON对象
     * 
     * @param jo <br>
     * @param key <br>
     * @param value <br>
     */
    @SuppressWarnings("rawtypes")
    private static void fillJsonObject(JSONObject jo, String key, Object value) {
        // logger.debug("key:[{}],type:[{}]", key, value.getClass().getName());
        if (value instanceof String || value instanceof Boolean) {
            if ("null".equals(value)) {
                jo.put(key, "");
            }
            else {
                jo.put(key, value.toString());
            }
        }
        else if (value instanceof Long) {
            jo.put(key, value);
        }
        else if (value instanceof Date) {
            jo.put(key, DateUtil.date2String((Date) value, DateUtil.DEFAULT_TIME_FORMAT));
        }
        else if (value instanceof List) {
            JSONArray ja = new JSONArray();
            List l = (List) value;
            for (int i = 0; i < l.size(); i++) {
                fillJsonArray(ja, l.get(i));
            }
            jo.put(key, ja);
        }
        else if (value instanceof DynamicDict) {
            jo.put(key, boToJson((DynamicDict) value));
        }
        else if (value instanceof Integer) {
            jo.put(key, value);
        }
    }

    /**
     * 将每个对象填充到JSON数组
     * 
     * @param ja <br>
     * @param obj <br>
     */
    @SuppressWarnings("rawtypes")
    private static void fillJsonArray(JSONArray ja, Object obj) {
        // logger.debug("fill array type:[{}]", obj.getClass().getName());
        if (obj instanceof String) {
            ja.add(obj);
        }
        else if (obj instanceof Long || obj instanceof Integer || obj instanceof Short) {
            ja.add(obj.toString());
        }
        else if (obj instanceof Date) {
            ja.add(DateUtil.date2String((Date) obj, DateUtil.DEFAULT_TIME_FORMAT));
        }
        else if (obj instanceof List) {
            List l = (List) obj;
            JSONArray ja2 = new JSONArray();
            for (int i = 0; i < l.size(); i++) {
                fillJsonArray(ja2, l.get(i));
            }
            if (ja2.size() > 0) {
                ja.add(ja2);
            }
        }
        else if (obj instanceof DynamicDict) {
            ja.add(boToJson((DynamicDict) obj));
        }
    }
}
