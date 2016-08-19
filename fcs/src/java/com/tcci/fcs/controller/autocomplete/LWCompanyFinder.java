/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.controller.autocomplete;

import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.facade.FcCompanyFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author David.Jen
 */
@ManagedBean(name = "LWCompanyFinder")
@ViewScoped
public class LWCompanyFinder {
    @EJB
    private FcCompanyFacade facade; 
    //
    public List<FcCompany> find(String keyword){
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<FcCompany>();
        }
        
        //
        List<FcCompany> matched = facade.findByKeyword(keyword); //facade.//customerFacade.findCustomersByKeyword(input);
        return matched;
    }
}
