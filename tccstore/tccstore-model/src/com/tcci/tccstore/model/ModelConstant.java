/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.model;

/**
 *
 * @author Jimmy.Lee
 */
public class ModelConstant {

    public static String ISO8601Format = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"; // json date format
    public static int INCO_FLAG_EXW = 0b0001; // bit0, 廠交自提
    public static int INCO_FLAG_FCA = 0b0010; // bit1, 工地交自提

}
