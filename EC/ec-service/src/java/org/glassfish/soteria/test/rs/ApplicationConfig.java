/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.soteria.test.rs;

import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author Peter.pan
 */
@ApplicationPath("resources")
public class ApplicationConfig extends Application {
    public ApplicationConfig() {
    }

    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }
    
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.tcci.ec.service.DeliveryService.class);
        resources.add(com.tcci.ec.service.JacksonConfigurator.class);
        resources.add(com.tcci.ec.service.SystemService.class);
        resources.add(com.tcci.ec.service.customer.CustomerService.class);
        resources.add(com.tcci.ec.service.image.ImageService.class);
        resources.add(com.tcci.ec.service.member.MemberService.class);
        resources.add(com.tcci.ec.service.order.OrderService.class);
        resources.add(com.tcci.ec.service.payment.PaymentService.class);
        resources.add(com.tcci.ec.service.product.ProductService.class);
        resources.add(com.tcci.ec.service.push.PushService.class);
        resources.add(com.tcci.ec.service.seller.SellerService.class);
        resources.add(com.tcci.ec.service.shipping.ShipService.class);
        resources.add(com.tcci.ec.service.sms.SmsService.class);
        resources.add(com.tcci.ec.service.store.StoreService.class);
        resources.add(org.glassfish.soteria.test.rs.SampleREST.class);
    }
    
}
