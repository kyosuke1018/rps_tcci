/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.article;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.tccstore.entity.EcArticle;
import com.tcci.tccstore.facade.article.EcArticleFacade;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "articleEdit")
@ViewScoped
public class ArticleEdit {

    private String id;
    private EcArticle selected;
    private boolean useContent = false;

    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;

    @EJB
    private EcArticleFacade ecArticleFacade;

    @PostConstruct
    private void init() {
        id = JsfUtil.getRequestParameter("id");
        if (null == id) {
            selected = new EcArticle();
            selected.setContent("<p>請輸入內容</p>");
        } else {
            try {
                selected = ecArticleFacade.find(Long.valueOf(id));
            } catch (NumberFormatException ex) {
                JsfUtil.addErrorMessage("id必須是數字!");
                return;
            }
            if (null == selected) {
                JsfUtil.addErrorMessage("沒有這筆資料!");
                return;
            }
        }
        useContent = selected.getContent() != null;
    }

    public void save() {
        try {
            preSave();
            ecArticleFacade.edit(selected);
            JsfUtil.addSuccessMessage("資料已儲存!");
            redirectToList();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "save exception");
        }
    }
    
    public void saveAndPublish() {
        try {
            preSave();
            ecArticleFacade.saveAndPublish(selected);
            JsfUtil.addSuccessMessage("資料已發佈!");
            redirectToList();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "save exception");
        }
    }

    // helper
    public String getPageTitle() {
        return null == id ? "新增文章" : "編輯文章 (" + id + ")";
    }
    
    private void preSave() {
        if (selected.getId() == null) {
            selected.setCreatetime(new Date());
            selected.setCreator(userSession.getTcUser());
        }
        if (useContent) {
            selected.setLink(null);
        } else {
            selected.setContent(null);
        }
    }
    
    private void redirectToList() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext exContext = context.getExternalContext();
            exContext.getFlash().setKeepMessages(true);
            exContext.redirect("list.xhtml");
            context.responseComplete();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "redirectToList exception");
        }
    }

    // getter, setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EcArticle getSelected() {
        return selected;
    }

    public void setSelected(EcArticle selected) {
        this.selected = selected;
    }

    public boolean isUseContent() {
        return useContent;
    }

    public void setUseContent(boolean useContent) {
        this.useContent = useContent;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

}
