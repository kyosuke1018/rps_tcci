/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.partner;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.tccstore.entity.EcPartner;
import com.tcci.tccstore.enums.NotifyTypeEnum;
import com.tcci.tccstore.enums.PartnerStatusEnum;
import com.tcci.tccstore.facade.notify.EcNotifyFacade;
import com.tcci.tccstore.facade.partner.EcPartnerFacade;
import com.tcci.tccstore.facade.util.PartnerFilter;
import java.text.MessageFormat;
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
public class PartnerController {

    private ResourceBundle rb = ResourceBundle.getBundle("/msgPartner", FacesContext.getCurrentInstance().getViewRoot().getLocale());

    private Logger logger = LoggerFactory.getLogger(PartnerController.class);
    private List<EcPartner> items;
    private EcPartner selected = new EcPartner();
    private PartnerFilter filter;
    private List<SelectItem> statusList;
    private List<String> owners;
    private List<String> provinces;

    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;

    @Inject
    private EcPartnerFacade ejbFacade;
    @Inject
    private EcNotifyFacade notifyFacade;

    @PostConstruct
    private void init() {
        this.filter = new PartnerFilter();
        this.filter.setStatus(PartnerStatusEnum.APPLY);
        this.statusList = new ArrayList();
        for (PartnerStatusEnum partnerStatus : PartnerStatusEnum.values()) {
            statusList.add(new SelectItem(partnerStatus, partnerStatus.getDisplayName()));
        }
        owners = ejbFacade.findOwners();
        provinces = ejbFacade.findProvinces();
        query();
    }

    public void query() {
        this.items = ejbFacade.findByCriteria(this.filter);
    }

    public void prepareReject(EcPartner partner) {
        this.selected = partner;
    }

    public void prepareEdit(EcPartner partner) {
        this.selected = partner;
    }

    public void save() {
        //ejbFacade.editThenReturn(this.selected);
        ejbFacade.save(this.selected);
        JsfUtil.addSuccessMessage(rb.getString("partner.msg.editSuccess"));
    }

    public void approve(EcPartner partner) {
        if (!ejbFacade.isStatusApply(partner)) {
            JsfUtil.addWarningMessage("状态已变更，请重新查询!");
            return;
        }
        partner.setActive(true);
        partner.setStatus(PartnerStatusEnum.APPROVE);
        partner.setApprover(userSession.getTcUser());
        partner.setApprovalTime(new Date());
        //partner = ejbFacade.editThenReturn(partner);
        ejbFacade.save(partner);
        createPartnerApproveNotify(partner);
        JsfUtil.addSuccessMessage(rb.getString("partner.msg.approveSuccess"));
    }

    private void createPartnerApproveNotify(EcPartner partner) {
        notifyFacade.createNotify(NotifyTypeEnum.PARTNER_APPROVE,
                rb.getString("partner.msg.partnerApproveNotify"),
                partner, partner.getOwner());
    }

    public void reject(EcPartner partner) {
        if (!ejbFacade.isStatusApply(partner)) {
            JsfUtil.addWarningMessage("状态已变更，请重新查询!");
            return;
        }
        partner.setStatus(PartnerStatusEnum.REJECT);
        partner.setApprover(userSession.getTcUser());
        partner.setApprovalTime(new Date());
        // this.selected = ejbFacade.editThenReturn(this.selected);
        ejbFacade.save(partner);
        createPartnerRejectNotify(partner);
        JsfUtil.addSuccessMessage(rb.getString("partner.msg.rejectSuccess"));
    }
    
    public List<String> completeOwner(String input) {
        List<String> result = new ArrayList<>();
        for (String str : owners) {
            if (str.contains(input)) {
                result.add(str);
            }
        }
        return result;
    }

    private void createPartnerRejectNotify(EcPartner partner) {
        notifyFacade.createNotify(NotifyTypeEnum.PARTNER_REJECT,
                MessageFormat.format(rb.getString("partner.msg.partnerRejectNotify"), new Object[]{partner.getMessage()}),
                partner, partner.getOwner());
    }

    public void remove(EcPartner partner) {
        logger.debug("remove(),partner={}", partner);
        ejbFacade.removePartner(partner);
        this.items.remove(partner);
        JsfUtil.addSuccessMessage(rb.getString("partner.msg.deleteSuccess"));
    }

    public List<EcPartner> getItems() {
        return items;
    }

    public void setItems(List<EcPartner> items) {
        this.items = items;
    }

    public EcPartner getSelected() {
        return selected;
    }

    public void setSelected(EcPartner selected) {
        this.selected = selected;
    }

    public PartnerFilter getFilter() {
        return filter;
    }

    public void setFilter(PartnerFilter filter) {
        this.filter = filter;
    }

    public List<SelectItem> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<SelectItem> statusList) {
        this.statusList = statusList;
    }

    public List<String> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<String> provinces) {
        this.provinces = provinces;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

}
