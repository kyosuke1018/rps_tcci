/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.test.service;

import com.tcci.ecdemo.model.banner.Banner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class BannerTest {

    private final static Logger logger = LoggerFactory.getLogger(BannerTest.class);

    public static void main(String[] args) {
        logger.debug("Test: banner/home");
        Banner[] banners = ClientUtil.get(Banner[].class, "banner/home", null);
        showResult(banners);

        logger.debug("Test: banner/product/1");
        banners = ClientUtil.get(Banner[].class, "banner/product/1", "jimmy.lee:HMAC-SIGNED-TOKEN");
        showResult(banners);

        logger.debug("Test: banner/product/1 unauthorized");
        banners = ClientUtil.get(Banner[].class, "banner/product/1", null);
        showResult(banners);

        logger.debug("Test: banner/bonus");
        banners = ClientUtil.get(Banner[].class, "banner/bonus", "jimmy.lee:HMAC-SIGNED-TOKEN");
        showResult(banners);

        logger.debug("Test: banner/bonus unauthorized");
        banners = ClientUtil.get(Banner[].class, "banner/bonus", null);
        showResult(banners);

        logger.debug("Test: banner/reward");
        banners = ClientUtil.get(Banner[].class, "banner/reward", "jimmy.lee:HMAC-SIGNED-TOKEN");
        showResult(banners);

        logger.debug("Test: banner/reward unauthorized");
        banners = ClientUtil.get(Banner[].class, "banner/reward", null);
        showResult(banners);
    }

    public static void showResult(Banner[] banners) {
        if (null == banners || 0 == banners.length) {
            logger.debug("empty");
        } else {
            for (Banner banner : banners) {
                logger.debug("{} {}", banner.getCategory(), banner.getContentUrl());
            }
        }
        logger.debug("--------------------");
    }

}
