/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.bulletin;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.tccstore.entity.EcBulletin;
import com.tcci.tccstore.facade.bulletin.EcBulletinFacade;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "bulletinController")
@ViewScoped
public class BulletinController {

    private List<EcBulletin> data;
    private EcBulletin selected;
    
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    
    @EJB
    private EcBulletinFacade ecBulletinFacade;
    
    @PostConstruct
    private void init() {
        query();
    }
    
    public void query() {
        data = ecBulletinFacade.findAll();
    }
    
    public void edit(EcBulletin row) {
        selected = (null==row ? new EcBulletin() : row );
    }
    
    public void save() {
        try {
            if (selected.getId() == null) {
                selected.setCreator(userSession.getTcUser());
                selected.setCreatetime(new Date());
            }
            ecBulletinFacade.edit(selected);
            data = ecBulletinFacade.findAll();
            JsfUtil.addSuccessMessage("已儲存!");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "save exception!");
        }
    }
    
    public void remove(EcBulletin row) {
        if (row != null) {
            ecBulletinFacade.remove(row);
            data = ecBulletinFacade.findAll();
            JsfUtil.addSuccessMessage("已刪除!");
        }
    }

    // getter, setter
    public List<EcBulletin> getData() {
        return data;
    }

    public void setData(List<EcBulletin> data) {
        this.data = data;
    }

    public EcBulletin getSelected() {
        return selected;
    }

    public void setSelected(EcBulletin selected) {
        this.selected = selected;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
}
