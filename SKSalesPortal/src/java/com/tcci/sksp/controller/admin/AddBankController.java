/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.admin;

import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.entity.SkBank;
import com.tcci.sksp.facade.SkBankFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class AddBankController {
    @EJB SkBankFacade skBankFacade;
    
    private String code;
    private String name;
    
    private List<SkBank> bankList;

    public List<SkBank> getBankList() {
        return bankList;
    }

    public void setBankList(List<SkBank> bankList) {
        this.bankList = bankList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String createBankAction(){
        boolean flag = false;
        if( StringUtils.isEmpty(code) ){
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, "銀行編號不可為空值!");
            flag= true;
        }
        if( StringUtils.isEmpty(name) ){
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, "銀行名稱不可為空值!");
            flag= true;
        }
        if( !flag ){
            SkBank bank = new SkBank();
            bank.setCode(code);
            bank.setName(name);
            if( skBankFacade.findByCode(bank.getCode()) != null ){
                FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, "銀行編號已存在!");
                flag= true;
            }
            List<SkBank> list = skBankFacade.findByCriteria(null,bank.getName());
            if( list != null && !list.isEmpty() ){
                FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, "銀行名稱已存在!");
                flag= true;
            }
            if( !flag ){
                try{
                    bank.setCreatetimestamp(new Date() );
                    skBankFacade.create(bank);
                    FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, "儲存成功!");
                }catch(Exception e){
                    FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Error:"+ e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    public String doSearchAction(){
        bankList = skBankFacade.findByCriteria(code, name);
        return null;
    }
}
