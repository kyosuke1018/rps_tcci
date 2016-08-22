/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.partner;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.tccstore.entity.EcPartner;
import com.tcci.tccstore.entity.EcPartnerComment;
import com.tcci.tccstore.enums.CommentStatusEnum;
import com.tcci.tccstore.facade.partner.EcPartnerCommentFacade;
import com.tcci.tccstore.facade.partner.EcPartnerFacade;
import com.tcci.tccstore.facade.util.PartnerCommentFilter;
import com.tcci.tccstore.facade.util.PartnerFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@ManagedBean
@ViewScoped
public class PartnerCommentController {

    private ResourceBundle rb = ResourceBundle.getBundle("/msgPartner", FacesContext.getCurrentInstance().getViewRoot().getLocale());

    private Logger logger = LoggerFactory.getLogger(PartnerCommentController.class);
    private List<EcPartnerComment> items;
    private EcPartnerComment selected = new EcPartnerComment();
    private PartnerCommentFilter filter;
    private List<SelectItem> statusList;

    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;

    @Inject
    private EcPartnerCommentFacade ejbFacade;
    @Inject
    private EcPartnerFacade partnerFacade;

    @PostConstruct
    private void init() {
        this.filter = new PartnerCommentFilter();
        this.filter.setStatus(CommentStatusEnum.APPLY);
        statusList = new ArrayList<>();
        for (CommentStatusEnum cs : CommentStatusEnum.values()) {
            statusList.add(new SelectItem(cs, cs.getDisplayName()));
        }
        query();
    }

    public List<EcPartner> completePartner(String input) {
        PartnerFilter partnerFilter = new PartnerFilter();
        partnerFilter.setName(input);
        return partnerFacade.findByCriteria(partnerFilter);
    }

    public void query() {
        this.items = ejbFacade.findByCriteria(this.filter);
        logger.debug("this.items.size()={}", this.items.size());
    }

    public List<EcPartnerComment> getItems() {
        return items;
    }

    public void setItems(List<EcPartnerComment> items) {
        this.items = items;
    }

    public EcPartnerComment getSelected() {
        return selected;
    }

    public void setSelected(EcPartnerComment selected) {
        this.selected = selected;
    }

    public void approve(EcPartnerComment comment) {
        if (!ejbFacade.isStatusApply(comment)) {
            JsfUtil.addWarningMessage("状态已变更，请重新查询!");
            return;
        }
        comment.setApprover(userSession.getTcUser());
        comment.setApprovalTime(new Date());
        ejbFacade.approve(comment);
        JsfUtil.addSuccessMessage(rb.getString("partner.msg.activeCommentSuccess"));
    }

    public void reject(EcPartnerComment comment) {
        if (!ejbFacade.isStatusApply(comment)) {
            JsfUtil.addWarningMessage("状态已变更，请重新查询!");
            return;
        }
        comment.setApprover(userSession.getTcUser());
        comment.setApprovalTime(new Date());
        ejbFacade.reject(comment);
        JsfUtil.addSuccessMessage(rb.getString("partner.msg.inactiveCommentSuccess"));
    }

    public PartnerCommentFilter getFilter() {
        return filter;
    }

    public void setFilter(PartnerCommentFilter filter) {
        this.filter = filter;
    }

    public List<SelectItem> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<SelectItem> statusList) {
        this.statusList = statusList;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

}
