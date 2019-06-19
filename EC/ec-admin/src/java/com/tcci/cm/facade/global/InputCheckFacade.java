/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.global;

import com.tcci.cm.annotation.InputCheckMeta;
import com.tcci.cm.annotation.enums.DataTypeEnum;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.enums.RsErrEnum;
import com.tcci.ec.enums.ValidateKeyEnum;
import com.tcci.ec.enums.ValidateNumEnum;
import com.tcci.ec.enums.ValidateStrEnum;
import com.tcci.fc.util.StringUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class InputCheckFacade extends AbstractFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public boolean checkInput(Object vo, Locale locale, List<String> errors) {
        return checkInput(vo, "id", locale, errors);
    }
    public boolean checkInput(Object vo, String idField, Locale locale, List<String> errors) {
        boolean pass = true;
        Field[] fs = vo.getClass().getDeclaredFields();
        for (Field f : fs) {
            try{
                String fieldName = f.getName();
                InputCheckMeta metaData = f.getAnnotation(InputCheckMeta.class);
                if( metaData!=null && StringUtils.isNotBlank(metaData.key()) ){
                    // 字串長度驗證
                    pass = pass && validateString(vo, metaData, fieldName, locale, errors);
                    // 不重複驗證
                    pass = pass && validateKey(vo, metaData, idField, fieldName, locale, errors);
                    // 數字驗證
                    if( metaData.type()==DataTypeEnum.BIG_DECIMAL 
                            || metaData.type()==DataTypeEnum.INT 
                            || metaData.type()==DataTypeEnum.LONG ){
                        pass = pass && validateNum(vo, metaData, fieldName, locale, errors);
                    }
                }
            }catch(Exception e){
                logger.error("checkInput Exception:\n", e);
            }
        }
        return pass;
    }

    /**
     * 數字相關驗證
     * @param vo
     * @param metaData
     * @param fieldName
     * @param locale
     * @param errors
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException 
     */
    public boolean validateNum(Object vo, InputCheckMeta metaData, String fieldName, Locale locale, List<String> errors) 
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        boolean pass = true;
        ValidateNumEnum numEnum = ValidateNumEnum.getFromCode(metaData.key());
        if( numEnum!=null ){
            logger.debug("checkInput numEnum = "+numEnum);
            String value = ExtBeanUtils.getProperty(vo, fieldName);
            logger.debug("checkInput value = "+value);
            if( value==null || StringUtils.isEmpty(value.trim()) ){
                if( !numEnum.isNullable() ){// 非空檢查
                    StringBuilder sb = new StringBuilder();
                    sb.append("[").append(numEnum.getDisplayName()).append("]")
                            .append(RsErrEnum.NO_EMPTY.getDisplayName(locale));
                    errors.add(sb.toString());
                    pass = false;
                }
            }else{
                Long longVal = null;
                try{
                    if( metaData.type() == DataTypeEnum.BIG_DECIMAL ){
                        BigDecimal num = BigDecimal.valueOf(Double.parseDouble(value));
                        longVal = num.longValue();
                    }else if( metaData.type() == DataTypeEnum.LONG ){
                        Long num = Long.parseLong(value);
                        longVal = num;
                    }else if( metaData.type() == DataTypeEnum.INT ){
                        Integer num = Integer.parseInt(value);
                        longVal = num.longValue();
                    }

                    if( !numEnum.isCanZero() && longVal==0L ){// 不可為零
                        StringBuilder sb = new StringBuilder();
                        sb.append("[").append(numEnum.getDisplayName()).append("]")
                                .append(RsErrEnum.NO_ZERO.getDisplayName(locale));
                        errors.add(sb.toString());
                        pass = false;
                    }else if( numEnum.isPositiveOnly() && longVal<0 ){// 正整數
                        StringBuilder sb = new StringBuilder();
                        sb.append("[").append(numEnum.getDisplayName()).append("]")
                                .append(RsErrEnum.POSITIVE_ONLY.getDisplayName(locale));
                        errors.add(sb.toString());
                        pass = false;
                    }else if( numEnum.getMin()!=null && numEnum.getMin()>longVal ){// 最小限制
                        StringBuilder sb = new StringBuilder();
                        sb.append("[").append(numEnum.getDisplayName()).append("]")
                                .append(MessageFormat.format(RsErrEnum.MIN_LIMIT.getDisplayName(locale), numEnum.getMin()));
                        errors.add(sb.toString());
                        pass = false;
                    }else if( numEnum.getMax()!=null && numEnum.getMax()<longVal ){// 最大限制
                        StringBuilder sb = new StringBuilder();
                        sb.append("[").append(numEnum.getDisplayName()).append("]")
                                .append(MessageFormat.format(RsErrEnum.MAX_LIMIT.getDisplayName(locale), numEnum.getMax()));
                        errors.add(sb.toString());
                        pass = false;
                    }
                }catch(NumberFormatException e){
                    logger.error("validateNum NumberFormatException value = "+value);
                    StringBuilder sb = new StringBuilder();
                    sb.append("[").append(numEnum.getDisplayName()).append("]")
                            .append(RsErrEnum.INPUT.getDisplayName(locale));
                    errors.add(sb.toString());
                    pass = false;
                }
            }
        }
        
        return pass;
    }
    
    /**
     * 字串相關驗驗證
     * @param vo
     * @param metaData
     * @param fieldName
     * @param locale
     * @param errors
     * @return 
     * @throws java.lang.IllegalAccessException 
     * @throws java.lang.reflect.InvocationTargetException 
     * @throws java.lang.NoSuchMethodException 
     */
    public boolean validateString(Object vo, InputCheckMeta metaData, String fieldName, Locale locale, List<String> errors) 
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        boolean pass = true;
        ValidateStrEnum lenEnum = ValidateStrEnum.getFromCode(metaData.key());
        if( lenEnum!=null ){
            logger.debug("checkInput lenEnum = "+lenEnum+", fieldName = "+fieldName);
            String value = ExtBeanUtils.getProperty(vo, fieldName);
            logger.debug("checkInput lenEnum value = "+value);
            if( value==null || StringUtils.isEmpty(value.trim()) ){
                if( !lenEnum.isCanEmpty() ){
                        StringBuilder sb = new StringBuilder();
                        sb.append("[").append(lenEnum.getDisplayName()).append("]")
                                .append(RsErrEnum.NO_EMPTY.getDisplayName(locale));
                        errors.add(sb.toString());
                        pass = false;
                }
            }else if( StringUtils.includeNoASCII(value) ){// 有包含中文或全形
                if( value.length()>lenEnum.getcLen() ){
                    StringBuilder sb = new StringBuilder();
                    sb.append("[").append(lenEnum.getDisplayName()).append("]")
                            .append(RsErrEnum.STR_LENGTH_CH.getDisplayName(locale))
                            .append(lenEnum.getcLen())
                            .append("。");
                    errors.add(sb.toString());
                    pass = false;
                }
            }else{// ASCII
                if( value.length()>lenEnum.geteLen() ){
                    StringBuilder sb = new StringBuilder();
                    sb.append("[").append(lenEnum.getDisplayName()).append("]")
                            .append(RsErrEnum.STR_LENGTH.getDisplayName(locale))
                            .append(lenEnum.getcLen())
                            .append("。");
                    errors.add(sb.toString());
                    pass = false;
                }
            }
        }
        return pass;
    }
    
    /**
     * 關鍵值欄位檢查
     * @param vo
     * @param metaData
     * @param idField
     * @param fieldName
     * @param locale
     * @param errors
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException 
     */
    public boolean validateKey(Object vo, InputCheckMeta metaData, String idField, String fieldName, Locale locale, List<String> errors)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        boolean pass = true;
        ValidateKeyEnum keyEnum = ValidateKeyEnum.getFromCode(metaData.key());
        if( keyEnum!=null ){
            logger.debug("checkInput keyEnum = "+keyEnum);
            String idStr = ExtBeanUtils.getProperty(vo, idField);
            Long id = idStr!=null?Long.parseLong(idStr):null;
            String value = ExtBeanUtils.getProperty(vo, fieldName);
            logger.debug("checkInput keyEnum id = "+id+", value = "+value);

            String[] ary = keyEnum.getCode().split("\\.");
            String table  = ary[0];
            String field  = ary[1];

            Map<String, Object> params = new HashMap<String, Object>();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT COUNT(*) CC FROM ").append(table).append("\n");
            sql.append("WHERE ").append(field).append(" = #VALUE \n");
            params.put("VALUE", value);

            if( !keyEnum.isGlobal() ){
                if( keyEnum.isStoreOnly()){
                    Long stroeId = getIdValue(vo, "storeId");
                    if( stroeId!=null ){
                        sql.append("AND STORE_ID = #STORE_ID \n");
                        params.put("STORE_ID", stroeId);
                    }
                }
                if( keyEnum.isProductOnly()){
                    Long prdId = getIdValue(vo, "prdId");
                    if( prdId!=null ){
                        sql.append("AND PRD_ID = #PRD_ID \n");
                        params.put("PRD_ID", prdId);
                    }
                }
                if( keyEnum.isTypeOnly()){
                    String type = ExtBeanUtils.getProperty(vo, "type");
                    if( type!=null ){
                        sql.append("AND TYPE = #TYPE \n");
                        params.put("TYPE", type);
                    }
                }
                if( keyEnum.isParentOnly()){
                    Long parent = getIdValue(vo, "parent");
                    if( parent!=null ){
                        sql.append("AND PARENT = #PARENT \n");
                        params.put("PARENT", parent);
                    }
                }
            }
            if( id!=null ){
                sql.append("AND ID <> #ID ");
                params.put("ID", id);
            }

            int rownum = this.count(sql.toString(), params);
            logger.debug("checkInput keyEnum rownum = "+rownum);
            if( rownum>0 ){
                StringBuilder sb = new StringBuilder();
                sb.append("[").append(keyEnum.getDisplayName()).append("]")
                        .append(RsErrEnum.INPUT_EXISTED.getDisplayName(locale));// 輸入值已存在，不可重複!
                errors.add(sb.toString());
                pass = false;
            }
        }
        return pass;
    }
    
    public Long getIdValue(Object vo, String fieldname) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String idStr = ExtBeanUtils.getProperty(vo, fieldname);
        Long id = idStr!=null?Long.parseLong(idStr):null;
        
        return id;
    }
}
