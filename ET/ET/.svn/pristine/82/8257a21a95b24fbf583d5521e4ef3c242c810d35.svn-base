/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.rs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.tcci.tccstore.model.ModelConstant;
import java.text.SimpleDateFormat;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Jimmy.Lee
 */
@Provider
@Produces("application/json")
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper = new ObjectMapper();
    public static String ISO8601Format = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
//    private final static String ISO8601Format = "yyyy-MM-dd HH:mm:ss";

    public JacksonConfigurator() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat(ModelConstant.ISO8601Format);
        SimpleDateFormat dateFormat = new SimpleDateFormat(ISO8601Format);
        mapper.setDateFormat(dateFormat);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public ObjectMapper getContext(Class<?> clazz) {
        return mapper;
    }

}
