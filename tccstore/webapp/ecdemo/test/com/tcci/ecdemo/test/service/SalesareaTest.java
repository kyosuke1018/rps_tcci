/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.test.service;

import com.tcci.ecdemo.model.salesarea.Salesarea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class SalesareaTest {

    private final static Logger logger = LoggerFactory.getLogger(SalesareaTest.class);

    public static void main(String[] args) {
        logger.debug("Test: salesarea/plant/1");
        Salesarea[] salesareas = ClientUtil.get(Salesarea[].class, "salesarea/plant/1", "jimmy.lee:HMAC-SIGNED-TOKEN");
        showResult(salesareas);
    }

    public static void showResult(Salesarea[] salesareas) {
        if (null == salesareas || 0 == salesareas.length) {
            logger.debug("empty");
        } else {
            for (Salesarea salesarea : salesareas) {
                logger.debug("{} {}", salesarea.getCode(), salesarea.getName());
            }
        }
        logger.debug("--------------------");
    }
}
