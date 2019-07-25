/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security;

/**
 * Override Config
 * @author Peter.pan
 */
public class SecurityConfig {
    /**
     * 必要，若需至DB取得 Groups 資訊
     * ex. "jdbc/testDB";
     * @return 
     */
    public static String getJdbcNmae(){
        return "jdbc/et";
    }

    /**
     * Optional
     * ex. "jndi/JavaEESecSSO.config";
     * @return 
     */
    public static String getJndiName(){
//        return null;
        return "jndi/et.config";
    }
}
