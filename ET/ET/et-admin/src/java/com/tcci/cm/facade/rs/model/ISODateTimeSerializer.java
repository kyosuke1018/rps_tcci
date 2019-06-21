/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.rs.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tcci.fc.util.DateUtils;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class ISODateTimeSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date dateTime, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        jg.writeString(DateUtils.getISODateTimeStr(dateTime));
    } 
}