/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.test.service;

import com.tcci.ecdemo.model.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class OrderTest {
    

    private final static Logger logger = LoggerFactory.getLogger(OrderTest.class);

    public static void main(String[] args) {
        logger.debug("Test: order/list/CLOSE/0000019706");
        Order[] closeOrders = ClientUtil.get(Order[].class, "order/list/CLOSE/0000019706","jimmy.lee:HMAC-SIGNED-TOKEN");
        showResult(closeOrders);
        logger.debug("Test: order/list/OPEN/0000020236");
        Order[] openOrders = ClientUtil.get(Order[].class, "order/list/OPEN/0000020236","jimmy.lee:HMAC-SIGNED-TOKEN");
        showResult(openOrders);
    }

    public static void showResult(Order[] orders) {
        if (null == orders || 0 == orders.length) {
            logger.debug("empty");
        } else {
            for (Order order : orders) {
                logger.debug("{}", order);
            }
        }
        logger.debug("--------------------");
    }

}
