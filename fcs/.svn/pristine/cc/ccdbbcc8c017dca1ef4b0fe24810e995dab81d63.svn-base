/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.irs.controller;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fcs.entity.FcCompGroup;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcUserCompGroupR;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.facade.FcCompGroupFacade;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.model.global.GlobalConstant;
import com.tcci.irs.entity.IrsCompanyType;
import com.tcci.irs.enums.IrsCompanyTypeEnum;
import com.tcci.irs.facade.IrsCompanyTypeFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "companyTypeController")
@ViewScoped
public class IrsCompanyTypeController {
    private final static Logger logger = LoggerFactory.getLogger(IrsCompanyTypeController.class);
    
    //<editor-fold defaultstate="collapsed" desc="Injects">
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    public UserSession getUserSession() {
        return userSession;
    }
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
    
    @EJB
    private FcCompanyFacade companyFacade;
    @EJB
    private IrsCompanyTypeFacade companyTypeFacade;
    @EJB
    private FcCompGroupFacade fcCompGroupFacade;
    //</editor-fold>
    
    private List<SelectItem> groups;//企業團
    private CompanyGroupEnum group;
    private List<FcUserCompGroupR> cgList;
    private boolean isAdmin;
    private boolean noPermission = false;
    private List<SelectItem> allCompanyOptions;//公司選單
    private FcCompany editCompany;
    private String editType;
    private List<IrsCompanyType> companyTypeList;
    private IrsCompanyType editCompanyType;
    private List<SelectItem> typeEnums;//
    private IrsCompanyTypeEnum typeEnum;
    
    @PostConstruct
    protected void init() {
        cgList =  userSession.getTcUser().getCompGroupList();
        isAdmin = userSession.isUserInRole("ADMINISTRATORS");
        //公司群組權限
        groups = this.buildGroupOptions();
        if (groups == null || groups.isEmpty()) {
            noPermission = true;
            return;
        }
        if(this.isAdmin){
            group = CompanyGroupEnum.TCC;//預設台泥
        }else{
            if (this.cgList != null && !this.cgList.isEmpty()) {
                group = this.cgList.get(0).getGroup();
            }
        }
        this.buildCompanyOptions();
        this.typeEnums = this.bulidTypeEnum();
        this.fetchIrsCompanyType();
    }
    
    //<editor-fold defaultstate="collapsed" desc="private method">
    private List<SelectItem> buildGroupOptions() {
        List<SelectItem> options = new ArrayList();
        for (CompanyGroupEnum item : CompanyGroupEnum.values()) {
            //admin有所有公司群組權限
            if(this.isAdmin){
                options.add(new SelectItem(item, item.getName()));
            }else{
                if (this.cgList != null && !this.cgList.isEmpty()) {
                    for(FcUserCompGroupR cg : this.cgList){
                        if (item.equals(cg.getGroup())) {
                            options.add(new SelectItem(item, item.getName()));
                        }
                    }
                }
            }
        }
        return options;
    }
    
    private void buildCompanyOptions() {
        List<FcCompany> companyList = companyFacade.findAllActive();
        allCompanyOptions = new ArrayList();
        //非公告主體公司
        for(FcCompany company : companyList){
            if(CompanyGroupEnum.TCC.equals(group) && GlobalConstant.PARENT_COMP_1000.equals(company.getCode())){
                continue;
            }
            if(CompanyGroupEnum.CSRC.equals(group) && GlobalConstant.PARENT_COMP_8000.equals(company.getCode())){
                continue;
            }
            
            allCompanyOptions.add(new SelectItem(company, company.toString()));
        }
    }
    
    private List<SelectItem> bulidTypeEnum(){
        List<SelectItem> options = new ArrayList();
        for (IrsCompanyTypeEnum item : IrsCompanyTypeEnum.values()) {
            options.add(new SelectItem(item, item.name()));
        }
        return options;
    }
    
    private void fetchIrsCompanyType(){
        companyTypeList = companyTypeFacade.findAll(group);
    }
    
    private void reload(){
        
        this.fetchIrsCompanyType();
    }
    //</editor-fold>
    
    public void changeGroup() {
//        logger.debug("changeGroup:" + this.group);
        this.buildCompanyOptions();
        this.reload();
    }
    
    public void replaceType(){
//        logger.debug("replaceType! :"+this.typeEnum.name());
        this.editType = this.typeEnum.name();
    }
    
    public void add() {
        logger.debug("add!");
        if(this.group == null
                || this.editCompany == null){
            JsfUtil.addErrorMessage("請選擇公司!");
            return;
        }else{
            IrsCompanyType irsCompanyType = companyTypeFacade.findByCompany(editCompany, group);
            if(irsCompanyType != null){
                JsfUtil.addErrorMessage("公司重複!");
                return;
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            IrsCompanyType irsCompanyType = new IrsCompanyType();
            FcCompGroup fcCompGroup = fcCompGroupFacade.findByCode(group.getCode());
            irsCompanyType.setGroup(fcCompGroup);
            irsCompanyType.setCompany(editCompany);
            irsCompanyType.setType(editType);
            companyTypeFacade.save(irsCompanyType);
            
            JsfUtil.addSuccessMessage("新增成功!");
            this.reload();
        }catch (Exception ex) {
            logger.error("save exception", ex);
            JsfUtil.addErrorMessage("新增失敗!");
            context.addCallbackParam("saved", false);
        }
    }
    
    public void initIrsCompanyTypeEdit(IrsCompanyType irsCompanyType) {
//        logger.debug("initIrsCompanyTypeEdit!");
        editCompanyType = irsCompanyType;
    }
    
    //只修改type
    public void save() {
        logger.debug("save!");
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            companyTypeFacade.save(editCompanyType);
            
            JsfUtil.addSuccessMessage("儲存成功!");
            this.reload();
        }catch (Exception ex) {
            logger.error("save exception", ex);
            JsfUtil.addErrorMessage("儲存失敗!");
            context.addCallbackParam("saved", false);
        }
    }
    
    public void remove(IrsCompanyType irsCompanyType) {
        logger.debug("remove!");
        companyTypeFacade.remove(irsCompanyType.getId());
        JsfUtil.addSuccessMessage("刪除成功!");
        this.reload();
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="getter setter">
    public List<SelectItem> getAllCompanyOptions() {
        return allCompanyOptions;
    }

    public FcCompany getEditCompany() {
        return editCompany;
    }

    public void setEditCompany(FcCompany editCompany) {
        this.editCompany = editCompany;
    }

    public CompanyGroupEnum getGroup() {
        return group;
    }

    public void setGroup(CompanyGroupEnum group) {
        this.group = group;
    }

    public List<SelectItem> getGroups() {
        return groups;
    }

    public boolean isNoPermission() {
        return noPermission;
    }

    public String getEditType() {
        return editType;
    }

    public void setEditType(String editType) {
        this.editType = editType;
    }

    public IrsCompanyType getEditCompanyType() {
        return editCompanyType;
    }

    public void setEditCompanyType(IrsCompanyType editCompanyType) {
        this.editCompanyType = editCompanyType;
    }

    public List<IrsCompanyType> getCompanyTypeList() {
        return companyTypeList;
    }

    public List<SelectItem> getTypeEnums() {
        return typeEnums;
    }

    public IrsCompanyTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(IrsCompanyTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }
    //</editor-fold>
}
