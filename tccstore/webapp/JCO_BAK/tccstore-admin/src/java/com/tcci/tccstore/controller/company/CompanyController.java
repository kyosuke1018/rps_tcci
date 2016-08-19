/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.company;

import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.tccstore.entity.EcCompany;
import com.tcci.tccstore.facade.company.EcCompanyFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "companyController")
@ViewScoped
public class CompanyController {

    private List<EcCompany> companys;

    @EJB
    private EcCompanyFacade companyFacade;

    @PostConstruct
    private void init() {
        companys = companyFacade.findAll();
    }
    
    // action
    public void toggle(EcCompany company) {
        if (company != null) {
            boolean result = !company.isHideCredit();
            company.setHideCredit(result);
            companyFacade.edit(company);
            JsfUtil.addSuccessMessage("資料已異動!");
        }
    }

    // getter, setter
    public List<EcCompany> getCompanys() {
        return companys;
    }

    public void setCompanys(List<EcCompany> companys) {
        this.companys = companys;
    }

}
