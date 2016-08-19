/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.test.service;

import com.tcci.ecdemo.model.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class ProductTest {

    private final static Logger logger = LoggerFactory.getLogger(ProductTest.class);

    public static void main(String[] args) {
        logger.debug("Test: product/contract/1");
        Product[] products = ClientUtil.get(Product[].class, "product/contract/1", "jimmy.lee:HMAC-SIGNED-TOKEN");
        showResult(products);

        logger.debug("Test: product/plant/1");
        products = ClientUtil.get(Product[].class, "product/plant/1", "jimmy.lee:HMAC-SIGNED-TOKEN");
        showResult(products);
    }

    private static void showResult(Product[] products) {
        if (null == products || 0 == products.length) {
            logger.debug("empty");
        } else {
            for (Product product : products) {
                logger.debug("{} {}", product.getCode(), product.getName());
            }
        }
        logger.debug("--------------------");
    }
}
