/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.test.service;

import com.tcci.ecdemo.model.sales.Sales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class SalesTest {

    private final static Logger logger = LoggerFactory.getLogger(SalesTest.class);

    public static void main(String[] args) {
        logger.debug("Test: sales/customer/1");
        Sales[] sales = ClientUtil.get(Sales[].class, "sales/customer/1", "jimmy.lee:HMAC-SIGNED-TOKEN");
        showResult(sales);
    }

    private static void showResult(Sales[] sales) {
        if (null == sales || 0 == sales.length) {
            logger.debug("empty");
        } else {
            for (Sales s : sales) {
                logger.debug("{} {}", s.getCode(), s.getName());
            }
        }
        logger.debug("--------------------");
    }
}
