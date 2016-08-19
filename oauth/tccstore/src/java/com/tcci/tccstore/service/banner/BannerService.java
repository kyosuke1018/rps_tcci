/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.banner;

import com.tcci.tccstore.entity.EcBanner;
import com.tcci.tccstore.service.EntityToModel;
import com.tcci.tccstore.enums.BannerCategoryEnum;
import com.tcci.tccstore.facade.banner.EcBannerFacade;
import com.tcci.tccstore.model.banner.Banner;
import com.tcci.tccstore.service.ServiceBase;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jimmy.Lee
 */
@Path("banner")
public class BannerService extends ServiceBase {

    @EJB
    private EcBannerFacade ecBannerFacade;
    
    @GET
    @Path("home")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Banner> home() {
        return convertToBanners(ecBannerFacade.home());
    }

    @GET
    @Path("bonus")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Banner> bonus() {
        return convertToBanners(ecBannerFacade.bonus());
    }

    @GET
    @Path("gold")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Banner> gold() {
        return convertToBanners(ecBannerFacade.gold());
    }
    
    @GET
    @Path("productusage")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Banner> productusage() {
        return convertToBanners(ecBannerFacade.productUsage());
    }
    
    @GET
    @Path("campaign")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Banner> campaign() {
        List<Banner> banners = convertToBanners(ecBannerFacade.findByCategory(BannerCategoryEnum.CAMPAIGN_TCC));
        banners.addAll(convertToBanners(ecBannerFacade.findByCategory(BannerCategoryEnum.CAMPAIGN_PARTNER)));
        return banners;
    }
    
    private List<Banner> convertToBanners(List<EcBanner> ecBanners) {
        List<Banner> banners = new ArrayList<>();
        for (EcBanner entity : ecBanners) {
            banners.add(EntityToModel.buildBanner(entity));
        }
        return banners;
    }

}
