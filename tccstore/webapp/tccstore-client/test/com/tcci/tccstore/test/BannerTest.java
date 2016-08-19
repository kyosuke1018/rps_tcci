/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.test;

import com.tcci.tccstore.client.SSOClientException;
import com.tcci.tccstore.model.banner.Banner;

/**
 *
 * @author Jimmy.Lee
 */
public class BannerTest extends TestBase {

    public static void main(String[] args) throws SSOClientException {
        BannerTest test = new BannerTest();
        String service = "/banner/home";
        System.out.println(service);
        Banner[] banners = test.get(service, Banner[].class);
        test.printBanners(banners);

        service = "/banner/bonus";
        System.out.println(service);
        banners = test.get(service, Banner[].class);
        test.printBanners(banners);

        service = "/banner/gold";
        System.out.println(service);
        banners = test.get(service, Banner[].class);
        test.printBanners(banners);

        service = "/banner/productusage";
        System.out.println(service);
        banners = test.get(service, Banner[].class);
        test.printBanners(banners);

        service = "/banner/campaign";
        System.out.println(service);
        banners = test.get(service, Banner[].class);
        test.printBanners(banners);

    }

    private void printBanners(Banner[] banners) throws SSOClientException {
        System.out.println(toJson(banners));
    }

}
