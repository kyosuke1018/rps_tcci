/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util.ssoclient;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Jimmy.Lee
 */
public class MultiDateDeserializer extends StdDeserializer<Date> {

    private static final String[] DATE_FORMATS = new String[] {
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
        "yyyy-MM-dd'T'HH:mm:ssXXX",
        "MMM dd, yyyy h:mm:ss a",
        "MMM dd, yyyy HH:mm:ss",
        "MMM dd, yyyy"
    };
    
    public MultiDateDeserializer() {
        this(null);
    }
    
    public MultiDateDeserializer(Class<?> vc) {
        super(vc);
    }
    
    @Override
    public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        final String date = node.textValue();

        for (String DATE_FORMAT : DATE_FORMATS) {
            try {
                return new SimpleDateFormat(DATE_FORMAT, Locale.US).parse(date);
            } catch (ParseException e) {
            }
        }
        throw new IOException("unknown date format:" + date);
    }
    
}
