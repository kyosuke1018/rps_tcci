/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.rs.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import com.tcci.fc.util.DateUtils;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class ISODateTimeDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        ObjectCodec codec = jp.getCodec();
        TextNode node = (TextNode)codec.readTree(jp);
        String dateString = node.textValue();
        // ISO DateTime String ex. 2005-10-10T12:34:56
        return DateUtils.getISODateTime(dateString);
    } 
}
