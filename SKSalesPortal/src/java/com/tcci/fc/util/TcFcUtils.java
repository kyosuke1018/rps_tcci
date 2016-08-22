/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import java.util.UUID;

/**
 *
 * @author Neo.Fu
 */
public class TcFcUtils {
    
    /**
     * get Unique identifier from UUID.
     * @return unique identifier.
     */
    public static String getNewId() {
        String id = UUID.randomUUID().toString();
        return id.substring(0,8)+id.substring(9,13)+id.substring(14,18)+id.substring(19,23)+id.substring(24); 
    }
}
