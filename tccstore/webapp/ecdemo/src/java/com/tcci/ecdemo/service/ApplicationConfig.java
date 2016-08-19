/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Jimmy.Lee
 */
@javax.ws.rs.ApplicationPath("service")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.tcci.ecdemo.service.HMACRequestFilter.class);
        resources.add(com.tcci.ecdemo.service.TestService.class);
        resources.add(com.tcci.ecdemo.service.banner.BannerService.class);
        resources.add(com.tcci.ecdemo.service.contract.ContractService.class);
        resources.add(com.tcci.ecdemo.service.form.FormService.class);
        resources.add(com.tcci.ecdemo.service.member.MemberService.class);
        resources.add(com.tcci.ecdemo.service.order.OrderService.class);
        resources.add(com.tcci.ecdemo.service.partner.PartnerService.class);
        resources.add(com.tcci.ecdemo.service.product.ProductService.class);
        resources.add(com.tcci.ecdemo.service.sales.SalesService.class);
        resources.add(com.tcci.ecdemo.service.salesarea.SalesareaService.class);
    }
    
}
