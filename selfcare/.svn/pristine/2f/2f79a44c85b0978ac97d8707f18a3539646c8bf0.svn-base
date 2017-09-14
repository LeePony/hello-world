package com.ztesoft.zsmart.bss.selfcare.common.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.AbstractDto;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;

/**
 * <Description> <br>
 * 
 * @author <br>
 * @version 1.0<br>
 * @param <T>
 * @taskId <br>
 * @CreateDate 2011-8-5 <br>
 * @since V7.0<br>
 * @see com.ztesoft.zsmart.bss.common <br>
 */
public final class BoDtoTool<T> {
    /**
     * 私有构造函数
     */
    private BoDtoTool() {
    }

    /**
     * logger
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(BoDtoTool.class);

    /**
     * 使用默认名称风格转换BO为Dto <br>
     * 
     * @author qiaozhu <br>
     * @taskId <br>
     * @param dtoType <br>
     * @param dict <br>
     * @param <T> <br>
     * @return <br>
     */
    public static <T extends AbstractDto> T fromBo(Class<T> dtoType, DynamicDict dict) {
        return fromBo(dtoType, dict, ALL_UPCASE);
    }

    /**
     * Description: <br>
     * 
     * @author cai.jiansheng<br>
     * @taskId <br>
     * @param dtoType ;
     * @param dictList ;
     * @param <T> ;
     * @return <br>
     */
    public static <T extends AbstractDto> List<T> fromListBo2ListDto(Class<T> dtoType, List<DynamicDict> dictList) {
        List<T> dtoList = Collections.emptyList();
        if (dictList != null && !dictList.isEmpty()) {
            dtoList = new ArrayList<T>(dictList.size());
            for (DynamicDict bo : dictList) {
                dtoList.add(fromBo(dtoType, bo, ALL_UPCASE));
            }
        }
        return dtoList;
    }

    /**
     * 将BO转换为Dto <br>
     * 
     * @author qiaozhu <br>
     * @taskId <br>
     * @param dtoType <br>
     * @param dict <br>
     * @param nameFormat bo中的名称风格（转换类）<br>
     * @param <T> <br>
     * @return <br>
     */
  
    public static <T extends AbstractDto> T fromBo(Class<T> dtoType, DynamicDict dict, INameFormat nameFormat) {
        if (dict == null) {
            return null;
        }
        T dto = null;
        try {
            dto = dtoType.newInstance();
        }
        catch (Exception e) {
            logger.warn("Failed to get instance of [{}]. error :[{}]", dtoType.getName(), e);
        }
        if (null == dto) {
            return null;
        }
       
        Field[] fieldList = dto.getClass().getFields();
        if (fieldList != null) {
            for (Field field : fieldList) {
                try {

                    // 对SP_ID的特殊处理 待定
                    if ("spId".equals(field.getName()) && dict.getLong("SP_ID") == null) {
                        field.set(dto,
                            (dict.getLong("zsmart_session.sp-id") != null) ? dict.getLong("zsmart_session.sp-id")
                                : Long.valueOf(0));
                        continue;
                    }

                    dealField4FromBo(dto, field, dict, nameFormat);

                }
                catch (Exception e) {
                    logger.warn("Failed to get [{}]. [{}], bo is [{}]. error = [{}].", 
                        dtoType.getName(), field.getName(), dict, e);
                }
            }
        }
       
        return dto;
    }

    /**
     * 
     * Description: <br> 
     *  
     * @taskId <br>
     * @param dto 
     * @param field  
     * @param dict 
     * @param nameFormat 
     * @throws BaseAppException  <br>
      * @throws IllegalAccessException  <br>
     */
    
    private static void dealField4FromBo(AbstractDto dto, Field field , DynamicDict dict, INameFormat nameFormat) throws BaseAppException,
    IllegalAccessException {
       
        Object objValue = null;
        
        Class<?> type = field.getType();
        String boName = nameFormat.asBoName(field.getName());
        if (type.isArray()) {
            // 取元素的类型
            Class<?> itemType = type.getComponentType();
            @SuppressWarnings("rawtypes")
            List list = dict.getList(boName);
            if (null != list && !list.isEmpty()) {
                objValue = Array.newInstance(type.getComponentType(), list.size());
                if (AbstractDto.class.isAssignableFrom(itemType)) {
                    for (int j = 0; j < list.size(); j++) {
                        Array
                            .set(
                                objValue,
                                j,
                                fromBo((Class<AbstractDto>) itemType, (DynamicDict) list.get(j),
                                    nameFormat));
                    }
                }
                else {
                    for (int j = 0; j < list.size(); j++) {
                        Array.set(objValue, j,
                            getValueFromBo(simpleValueAsBo(list.get(j), boName), itemType, boName));
                    }
                }
            }
        }
        else if (AbstractDto.class.isAssignableFrom(type)) {
            objValue = fromBo((Class<AbstractDto>) type, dict.getBO(boName), nameFormat);
        }
        else {
            objValue = getValueFromBo(dict, type, boName);
        }
        
        field.set(dto, objValue);
        
    }
    /**
     * 从Bo中获取指定字段的值<br>
     * 
     * @author qiaozhu <br>
     * @taskId <br>
     * @param dict <br>
     * @param type 要获取的字段的类型<br>
     * @param boName 要获取的字段名（bo中的名称）<br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    private static Object getValueFromBo(DynamicDict dict, final Class<?> type, String boName) throws BaseAppException {
        BoField boField = boFieldMap.get(type);
        if (boField == null) {
            return null;
        }
        return boField.getFromBo(dict, boName);
    }

    /**
     * 将简单类型的数据包装为Bo用于本类中几个用Bo的接口 <br>
     * 
     * @author qiaozhu <br>
     * @taskId <br>
     * @param simpleValue <br>
     * @param name <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    private static DynamicDict simpleValueAsBo(Object simpleValue, String name) throws BaseAppException {
        DynamicDict tmpBo = new DynamicDict();
        tmpBo.set(name, simpleValue);
        return tmpBo;
    }

    /**
     * 将dto数组放到Bo中 <br>
     * 
     * @author <br>
     * @taskId <br>
     * @return DynamicDict 存放数组的Bo <br>
     * @param arrayName 数组存放名称<br>
     * @param dtoArr <br>
     * @param nameFormat 名称风格（名称转换器）
     * @throws BaseAppException <br>
     */
    public static DynamicDict dtoArr2Bo(String arrayName, AbstractDto[] dtoArr, INameFormat nameFormat)
        throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        if (null != dtoArr && dtoArr.length > 0) {
            for (AbstractDto dto : dtoArr) {
                dict.add(arrayName, toBo(dto, nameFormat));
            }
        }
        return dict;
    }

    /**
     * 将dtoList放到Bo中 <br>
     * 
     * @author cai.jiansheng<br>
     * @taskId <br>
     * @return DynamicDict 存放数组的Bo <br>
     * @param arrayName 数组存放名称<br>
     * @param dtoList <br>
     * @param nameFormat 名称风格（名称转换器）
     * @throws BaseAppException <br>
     */
    public static DynamicDict dtoList2Bo(String arrayName, List<? extends AbstractDto> dtoList, INameFormat nameFormat)
        throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        if (null != dtoList && !dtoList.isEmpty()) {
            for (Object dto : dtoList) {
                dict.add(arrayName, toBo((AbstractDto) dto, nameFormat));
            }
        }
        return dict;
    }

    /**
     * 将dtoList放到Bo中 <br>
     * 
     * @author cai.jiansheng<br>
     * @taskId <br>
     * @return DynamicDict 存放数组的Bo <br>
     * @param arrayName 数组存放名称<br>
     * @param dtoList <br>
     * @throws BaseAppException <br>
     */
    public static DynamicDict dtoList2Bo(String arrayName, List<? extends AbstractDto> dtoList) throws BaseAppException {
        return dtoList2Bo(arrayName, dtoList, ALL_UPCASE);
    }

    /**
     * dto转bo，使用默认的名称转换器{@link BoDtoTool#ALL_UPCASE}
     * 
     * @author qiao.zhu<br>
     * @taskId <br>
     * @param dto <br>
     * @return DynamicDict <br>
     */
    public static DynamicDict toBo(AbstractDto dto) {
        return toBo(dto, ALL_UPCASE);
    }

    /**
     * Dto转为Bo <br>
     * 
     * @author <br>
     * @taskId <br>
     * @param dto <br>
     * @param nameFormat {@link INameFormat}<br>
     * @return DynamicDict <br>
     */
    public static DynamicDict toBo(AbstractDto dto, INameFormat nameFormat) {
        DynamicDict dict = new DynamicDict();
        if (dto == null) {
            return dict;
        }
        try {
            Class<?> c = dto.getClass();
            Field[] fieldList = c.getFields();
            if (fieldList == null || fieldList.length == 0) {
                return dict;
            }

            for (int i = 0; i < fieldList.length; i++) {
                Field field = fieldList[i];
                if (field == null || field.get(dto) == null) {
                    continue;
                }
                dealField4ToBo(dto, field, dict, nameFormat);
                
            }
        }
        catch (Exception ex) {
            logger.error("toBo error:[{}]", ex);
        }
        return dict;
    }
    
    /**
     * 
     * Description: <br> 
     *  
     * @taskId <br>
     * @param dto 
     * @param field  
     * @param dict 
     * @param nameFormat 
     * @throws BaseAppException  <br>
      * @throws IllegalAccessException  <br>
     */
    
    private static void dealField4ToBo(AbstractDto dto, Field field , DynamicDict dict, INameFormat nameFormat) throws BaseAppException,
    IllegalAccessException {
       
        Class<?> type = field.getType();
        String name = field.getName();
        String boName = nameFormat.asBoName(name);

        if (type.isArray()) {
            Object[] array = (Object[]) field.get(dto);
            if (array != null) {
                if (AbstractDto.class.isAssignableFrom(type.getComponentType())) {
                    for (int j = 0; j < array.length; j++) {
                        dict.add(boName, toBo((AbstractDto) array[j], nameFormat));
                    }
                }
                else {
                    for (int j = 0; j < array.length; j++) {
                        dict.add(boName, array[j]);
                    }
                }
            }
        }
        else {
            try {
                if (AbstractDto.class.isAssignableFrom(type)) {
                    dict.set(boName, toBo((AbstractDto) field.get(dto), nameFormat));
                }
                else {
                    dict.set(boName, field.get(dto));
                }
            }
            catch (Exception ex) {
                logger.error(ex);
            }
        }
    }

    /**
     * <br>
     * 
     * @author <br>
     * @taskId <br>
     * @param type <br>
     * @param nameFormat {@link INameFormat}<br>
     * @return DynamicDict <br>
     */
    public static List<String> getFieldNames(Class<?> type, INameFormat nameFormat) {
        List<String> fileldNames = new ArrayList<String>();
        if (type == null) {
            return fileldNames;
        }
        try {
            Field[] fieldList = type.getFields();
            if (fieldList == null || fieldList.length == 0) {
                return fileldNames;
            }

            for (int i = 0; i < fieldList.length; i++) {
                Field field = fieldList[i];
                if (field == null) {
                    continue;
                }
                String name = field.getName();
                String boName = nameFormat.asBoName(name);

                fileldNames.add(boName);
            }
        }
        catch (Exception ex) {
            logger.error("Get dto field error:[{}]", ex);
        }
        return fileldNames;
    }

    /**
     * 字段名转换器接口dto中的字段名转换为Bo中的字段名<br>
     * 
     * @author qiaozhu <br>
     * @version 1.0<br>
     * @taskId <br>
     * @CreateDate 2013-3-6 <br>
     * @since V7.0<br>
     * @see com.ztesoft.zsmart.bss.common <br>
     */
    public interface INameFormat {
        /**
         * 转为Bo中的名称 <br>
         * 
         * @param dtoName dto中的某字段名称<br>
         * @return <br>
         */
        String asBoName(String dtoName);
    }

    /**
     * ALL_UPCASE 下划线分割的全部大写的风格： YOUR_NAME MY_HOME<br>
     * <li>ID认为是惯用写法，不会分为I_D，例如recordID对应RECORD_ID，而IDoIt会被分割为I_DO_IT <li>Dto认为是Dto后缀（如果在最后的话）
     */
    public static final INameFormat ALL_UPCASE = new INameFormat() {
        /** buffer <br> */
        private Map<String, String> nameCache = new HashMap<String, String>();

        /** 缓冲区最大值 */
        private static final long BUFFER_MAX_SIZE = 10000;

        public String asBoName(String dtoName) {
            if (dtoName == null) {
                return null;
            }
            // 先在缓存中找<br>
            if (nameCache.containsKey(dtoName)) {
                return nameCache.get(dtoName);
            }

            // 转换<br>
            String ret = dtoName.replaceAll("ID([^a-z])", "Id$1").replaceAll("Dto$", "").replaceAll("([A-Z])", "_$1")
                .toUpperCase();
            // 加入缓冲
            if (nameCache.size() < BUFFER_MAX_SIZE) {
                // 防止缓冲爆掉
                synchronized (nameCache) {
                    if (!nameCache.containsKey(dtoName)) {
                        nameCache.put(dtoName, ret);
                    }
                }
            }
            return ret;
        }
    };

    /**
     * FIRST_LETTER_UPCASE 大驼峰风格的，某些地方移植代码，前台使用的是 AbcDef的名称格式<br>
     */
    public static final INameFormat FIRST_LETTER_UPCASE = new INameFormat() {
        public String asBoName(String dtoName) {
            if (StringUtil.isEmpty(dtoName)) {
                return "";
            }
            return dtoName.substring(0, 1).toUpperCase() + dtoName.substring(1);
        }
    };

    /**
     * 字段转换器接口<br>
     * 
     * @author qiaozhu <br>
     * @version 1.0<br>
     * @taskId <br>
     * @CreateDate 2013-3-6 <br>
     * @since V7.0<br>
     * @see com.ztesoft.zsmart.bss.common <br>
     */
    interface BoField {
        /**
         * 从Bo中获取字段<br>
         * 
         * @param bo <br>
         * @param name <br>
         * @return <br>
         * @throws BaseAppException <br>
         */
        Object getFromBo(DynamicDict bo, String name) throws BaseAppException;
    }

    /** map <br> */
    static Map<Class<?>, BoField> boFieldMap = new HashMap<Class<?>, BoDtoTool.BoField>();
    static {
        boFieldMap.put(Long.class, new BoField() {
            public Object getFromBo(DynamicDict bo, String name) throws BaseAppException {
                return bo.getLong(name);
            }
        });
        boFieldMap.put(String.class, new BoField() {
            public Object getFromBo(DynamicDict bo, String name) throws BaseAppException {
                return bo.getString(name);
            }
        });
        boFieldMap.put(java.sql.Date.class, new BoField() {
            public Object getFromBo(DynamicDict bo, String name) throws BaseAppException {
                return bo.getDate(name);
            }
        });
        boFieldMap.put(Boolean.class, new BoField() {
            public Object getFromBo(DynamicDict bo, String name) throws BaseAppException {
                String val = bo.getString(name);
                if (val != null) {
                    if ("true".equalsIgnoreCase(val)) {
                        return Boolean.TRUE;
                    }
                    else if ("false".equalsIgnoreCase(val)) {
                        return Boolean.FALSE;
                    }
                }
                return null;
            }
        });
    }

    // public static void main(String[] args) throws BaseAppException {
    // DynamicDict head = new DynamicDict();
    // for (int i = 0; i < 5; i++) {
    // DynamicDict leaf = new DynamicDict();
    // leaf.set("ATTR_ID", i);
    // head.add("LIST", leaf);
    // }
    //
    // List<AttrExDto> list = fromListBo2ListDto(AttrExDto.class, head.getList("LIST"));
    // System.out.print(list);
    // }
}
