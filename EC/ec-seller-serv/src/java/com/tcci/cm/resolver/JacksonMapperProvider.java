/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.resolver;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.tcci.cm.facade.rs.CombinedAnnotationBean;
import com.tcci.cm.facade.rs.model.ISODateTimeDeserializer;
import com.tcci.cm.facade.rs.model.ISODateTimeSerializer;
import com.tcci.cm.model.global.GlobalConstant;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Peter.pan
 */
@Provider
public class JacksonMapperProvider implements ContextResolver<ObjectMapper> {

    final ObjectMapper defaultObjectMapper;
    final ObjectMapper combinedObjectMapper;// 自訂Class特殊處理範例，如使用 JsonProperty、JsonInclude

    public JacksonMapperProvider() {
        defaultObjectMapper = createDefaultMapper();
        combinedObjectMapper = createCombinedObjectMapper();
    }

    //@Override
    public ObjectMapper getContext(final Class<?> type) {
        if (type == CombinedAnnotationBean.class) {
            return combinedObjectMapper;
        } else {
            return defaultObjectMapper;
        }
    }

    private static ObjectMapper createCombinedObjectMapper() {
         ObjectMapper result = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, true)
                .configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true)
                .setAnnotationIntrospector(createJaxbJacksonAnnotationIntrospector());
         result.registerModule(genCustomModule());
         return result;
    }

    private static ObjectMapper createDefaultMapper() {
        System.out.println("createDefaultMapper init ...");// 1
        final ObjectMapper result = new ObjectMapper();
        result.enable(SerializationFeature.INDENT_OUTPUT);
        result.setSerializationInclusion(Include.NON_NULL);
        result.setSerializationInclusion(Include.NON_EMPTY);
        //反串行化時，屬性不存在的兼容處理 
        result.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  
        result.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  
        //串行化時，日期的統一格式  
        result.setTimeZone(TimeZone.getTimeZone("GMT+8"));// UTC
        result.setDateFormat(new SimpleDateFormat(GlobalConstant.FORMAT_DATETIME));
        
        result.registerModule(genCustomModule());
        return result;
    }

    private static AnnotationIntrospector createJaxbJacksonAnnotationIntrospector() {
        final AnnotationIntrospector jaxbIntrospector = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
        final AnnotationIntrospector jacksonIntrospector = new JacksonAnnotationIntrospector();

        return AnnotationIntrospector.pair(jacksonIntrospector, jaxbIntrospector);
    }
    
    /**
     * 自訂格式轉換
     * 可使用 ObjectMapper.registerModule 
     * 或 
     * @JsonSerialize(using = ISODateTimeSerializer.class)
     * @JsonDeserialize(using = ISODateTimeDeserializer.class)
     * @return 
     */
    private static Module genCustomModule(){ 
        SimpleModule module = new SimpleModule();
        //module.addSerializer(Date.class, new ISODateTimeSerializer());
        module.addDeserializer(Date.class, new ISODateTimeDeserializer());
        
        return module;
    }
}