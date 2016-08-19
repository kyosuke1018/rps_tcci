/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.service.banner;

import com.tcci.ecdemo.entity.EcBanner;
import com.tcci.ecdemo.facade.banner.EcBannerFacade;
import com.tcci.ecdemo.model.banner.Banner;
import com.tcci.ecdemo.service.AuthType;
import com.tcci.ecdemo.service.HMACAuth;
import com.tcci.ecdemo.service.HMACServiceBase;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jimmy.Lee
 */
@Path("banner")
public class BannerService extends HMACServiceBase {

    @EJB
    private EcBannerFacade ecBannerFacade;
    
    @HMACAuth(authType = AuthType.OPTION)
    @GET
    @Path("home")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Banner> home() {
        List<Banner> banners = new ArrayList<>();
//        banners.add(new Banner(Banner.CAT_HOME, "http://www.taiwancement.com/Images/LeftImages/1/1.png", "http://www.taiwancement.com/"));
//        banners.add(new Banner(Banner.CAT_HOME, "http://www.taiwancement.com/Images/LeftImages/1/2.png", "http://www.taiwancement.com/"));
//        banners.add(new Banner(Banner.CAT_HOME, "http://www.taiwancement.com/Images/LeftImages/1/4.png", "http://www.taiwancement.com/"));
        List<EcBanner> ecBanners = ecBannerFacade.home();
        for (EcBanner e : ecBanners) {
            banners.add(new Banner(e.getCategory(), e.getContentUrl(), e.getLink()));
        }
        return banners;
    }

    @GET
    @Path("product/{product_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Banner> product(@PathParam("product_id") Long product_id) {
        List<Banner> banners = new ArrayList<>();
//        banners.add(new Banner(Banner.CAT_ADVERTISING, "http://www.taiwancement.com/Images/LeftImages/1/1.png", "http://www.taiwancement.com/"));
//        banners.add(new Banner(Banner.CAT_PROMOTION, "http://www.taiwancement.com/Images/LeftImages/1/2.png", "http://www.taiwancement.com/"));
        List<EcBanner> ecBanners = ecBannerFacade.product(product_id);
        for (EcBanner e : ecBanners) {
            banners.add(new Banner(e.getCategory(), e.getContentUrl(), e.getLink()));
        }
        return banners;
    }

    @GET
    @Path("bonus")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Banner> bonus() {
        List<Banner> banners = new ArrayList<>();
//        banners.add(new Banner(Banner.CAT_BONUS, "http://www.taiwancement.com/Images/LeftImages/1/1.png", "http://www.taiwancement.com/"));
        List<EcBanner> ecBanners = ecBannerFacade.bonus();
        for (EcBanner e : ecBanners) {
            banners.add(new Banner(e.getCategory(), e.getContentUrl(), e.getLink()));
        }
        return banners;
    }

    @GET
    @Path("reward")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Banner> reward() {
        List<Banner> banners = new ArrayList<>();
//        banners.add(new Banner(Banner.CAT_REWARD, "http://www.taiwancement.com/Images/LeftImages/1/1.png", "http://www.taiwancement.com/"));
        List<EcBanner> ecBanners = ecBannerFacade.reward();
        for (EcBanner e : ecBanners) {
            banners.add(new Banner(e.getCategory(), e.getContentUrl(), e.getLink()));
        }
        return banners;
    }

}
