/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Jason.Yu
 */
public class FileUtil {
    public String getQueryCommand(String fullPathFileName) throws IOException{
        StringBuffer buffer = new StringBuffer();
        //String fullPathFileName = "/report/sql/sales_achievement_detail.txt";
        java.io.InputStream is = this.getClass().getClassLoader().getResourceAsStream(fullPathFileName);
        if( is != null ){
            InputStreamReader isr =
                new InputStreamReader(is);
            BufferedReader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                    buffer.append((char)ch);
            }
            in.close();
            //System.out.println(buffer.toString());
        }
        return buffer.toString();        
    }
}
