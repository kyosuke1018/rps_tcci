/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.resolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Jimmy.Lee
 */
//@Provider
public class JacksonConfigurator {//implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper = new ObjectMapper();

    public JacksonConfigurator() {
        System.out.println("JacksonConfigurator init ...");// 2
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        mapper.setDateFormat(dateFormat);
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // javax.json.bind.JsonbException: Error deserialize JSON value into type: class java.math.BigDecimal.
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);*/
        
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));// UTC
        //反串行化時，屬性不存在的兼容處理 
        mapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  
        //串行化時，日期的統一格式  
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mapper.setDateFormat(df);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  
    }

    //@Override
    public ObjectMapper getContext(Class<?> clazz) {
        return mapper;
    }

}
