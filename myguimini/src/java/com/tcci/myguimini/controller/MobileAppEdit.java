/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.myguimini.controller;

import com.tcci.fc.controller.attachment.AttachmentController;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.myguimini.entity.MyMobileApp;
import com.tcci.myguimini.facade.MyMobileAppFacade;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "mobileAppEdit")
@ViewScoped
public class MobileAppEdit {
    
    private MyMobileApp myApp;
    private boolean editable = false;
    
    @ManagedProperty(value = "#{attachmentController}")
    private AttachmentController attachmentController;
    
    @EJB
    private MyMobileAppFacade appFacade;
    
    @PostConstruct
    private void init() {
        String id = JsfUtil.getRequestParameter("id");
        String platform = JsfUtil.getRequestParameter("platform");
        if (null==id && null==platform) {
            myApp = new MyMobileApp(null, "android", "1");
        } else if (platform != null) {
            myApp = appFacade.findByPlatform(platform);
            if (null == myApp) {
                myApp = new MyMobileApp(null, platform, "1");
            }
        } else {
            try {
                myApp = appFacade.find(Long.valueOf(id));
            } catch (Exception ex) {
                JsfUtil.addErrorMessage("無此資料!");
                return;
            }
        }
        if (myApp.getId() != null) {
            attachmentController.init(myApp, false);
        } else {
            attachmentController.init(null, false);
        }
        editable = true;
    }
    
    // action
    public void save() {
        try {
            appFacade.save(myApp, attachmentController.getAttachmentVOList());
            JsfUtil.addSuccessMessage("save success!");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }

    // getter, setter
    public MyMobileApp getMyApp() {
        return myApp;
    }

    public void setMyApp(MyMobileApp myApp) {
        this.myApp = myApp;
    }

    public AttachmentController getAttachmentController() {
        return attachmentController;
    }

    public void setAttachmentController(AttachmentController attachmentController) {
        this.attachmentController = attachmentController;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
