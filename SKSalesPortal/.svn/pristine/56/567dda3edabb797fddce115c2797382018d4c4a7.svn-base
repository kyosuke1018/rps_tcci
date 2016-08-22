/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.admin;

import com.tcci.sksp.rs.EntityCacheREST;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@RequestScoped
public class ClearCacheController {

    @EJB 
    EntityCacheREST entityCacheREST;
    
    public String entityName;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String doClear() {
        try {
            String msg = null;
            if (entityName == null || entityName.trim().equals("")) {
                entityCacheREST.evictAllCache();
                msg = "Done to clear all cache";
                FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
                FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            } else {

                Class cls = Class.forName(entityName);
                entityCacheREST.evictCache(cls);
                msg = "Done to clear the cache for " + entityName;
                FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
                FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            }
        } catch (Exception ex) {
            Logger.getLogger(ClearCacheController.class.getName()).log(Level.SEVERE, null, ex);
            String msg = ex.getMessage();
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        }
        return null;
    }
    
}
