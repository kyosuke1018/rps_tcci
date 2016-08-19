/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.myguimini.controller;

import com.tcci.myguimini.entity.MyService;
import com.tcci.myguimini.facade.MyServiceFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "myService")
@ViewScoped
public class MyServiceController {
    
    private List<MyService> myServices;
    
    @EJB
    private MyServiceFacade myServiceFacade;
    
    @PostConstruct
    private void init() {
        myServices = myServiceFacade.findAll();
    }

    // getter, setter
    public List<MyService> getMyServices() {
        return myServices;
    }

    public void setMyServices(List<MyService> myServices) {
        this.myServices = myServices;
    }
    
}
