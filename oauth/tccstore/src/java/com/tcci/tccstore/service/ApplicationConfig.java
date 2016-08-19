/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Jimmy.Lee
 */
// 為了設定 multipart-config, 移到 web.xml
// @javax.ws.rs.ApplicationPath("service")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        // Add Jackson feature.
        resources.add(org.glassfish.jersey.jackson.JacksonFeature.class);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.tcci.fc.facade.util.CacheFlushFacade.class);
        resources.add(com.tcci.tccstore.service.JacksonConfigurator.class);
        resources.add(com.tcci.tccstore.service.TestService.class);
        resources.add(com.tcci.tccstore.service.article.ArticleService.class);
        resources.add(com.tcci.tccstore.service.banner.BannerService.class);
        resources.add(com.tcci.tccstore.service.campaign.CampaignService.class);
        resources.add(com.tcci.tccstore.service.contract.ContractService.class);
        resources.add(com.tcci.tccstore.service.ecsso.EcssoService.class);
        resources.add(com.tcci.tccstore.service.form.FormService.class);
        resources.add(com.tcci.tccstore.service.home.HomeService.class);
        resources.add(com.tcci.tccstore.service.image.ImageService.class);
        resources.add(com.tcci.tccstore.service.member.MemberService.class);
        resources.add(com.tcci.tccstore.service.notify.NotifyService.class);
        resources.add(com.tcci.tccstore.service.order.OrderService.class);
        resources.add(com.tcci.tccstore.service.partner.PartnerService.class);
        resources.add(com.tcci.tccstore.service.plant.PlantService.class);
        resources.add(com.tcci.tccstore.service.product.ProductService.class);
        resources.add(com.tcci.tccstore.service.sales.SalesService.class);
        resources.add(com.tcci.tccstore.service.vehicle.VehicleService.class);
   }
    
}
