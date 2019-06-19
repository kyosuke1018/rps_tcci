/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.controller.admin;

import static com.tcci.cm.controller.admin.PermissionController.FUNC_OPTION;
import com.tcci.cm.entity.admin.CmBulletin;
import com.tcci.cm.facade.admin.CmBulletinFacade;
import com.tcci.cm.util.JsfUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "bulletinController")
@ViewScoped
public class BulletinController implements Serializable {
    //<editor-fold defaultstate="collapsed" desc="Inject">
    @EJB CmBulletinFacade cmBulletinFacade;
    //</editor-fold>
    private List<CmBulletin> resultList;
    private CmBulletin bulletin;
    
    @PostConstruct
    public void init() {
        resultList = new ArrayList<CmBulletin>();
        this.query();
    }
    
    public void view(CmBulletin cmBulletin){
        this.bulletin = cmBulletin;
        JsfUtils.buildSuccessCallback();
    }
    
    public void query() {
        resultList = cmBulletinFacade.findAll();
    }
    
    /**
     * 功能標題
     * @return 
     */
    public String getFuncTitle(){
        return "首頁";
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public List<CmBulletin> getResultList() {
        return resultList;
    }

    public void setResultList(List<CmBulletin> resultList) {
        this.resultList = resultList;
    }

    public CmBulletin getBulletin() {
        return bulletin;
    }

    public void setBulletin(CmBulletin bulletin) {
        this.bulletin = bulletin;
    }
    //</editor-fold>
}
