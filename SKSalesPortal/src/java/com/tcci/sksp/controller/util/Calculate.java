/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.util;

import java.util.Calendar;

/**
 *
 * @author Jason.Yu
 */
public class Calculate {
    private Calendar startTime = null;
    private Calendar endTime = null;
    public Calculate(){
        //startTime = Calendar.getInstance();
    }
    
    public void start(){
        startTime = Calendar.getInstance();
    }
    public void end(){
        endTime = Calendar.getInstance();
    }
    public long spent(){
        return endTime.getTimeInMillis() - startTime.getTimeInMillis();
    }
}
