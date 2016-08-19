/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.controller;

import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.facade.service.CacheFlushFacade;
import com.tcci.fcs.entity.FcCompGroup;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcCurrency;
import com.tcci.fcs.entity.FcUploaderR;
import com.tcci.fcs.facade.FcCompGroupFacade;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.facade.FcCurrencyFacade;
import com.tcci.fcs.facade.FcUploaderRFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name="companyController")
@ViewScoped
public class CompanyController {
    private final static Logger logger = LoggerFactory.getLogger(CompanyController.class);
    
    private List<FcCompany> companyList = new ArrayList<FcCompany>(); // filterd companys
    private String filter; // company lookup filter
    private FcCompany editComp = new FcCompany(); // 編輯中的 company
    private AutoComplete autoComplete; // for Uploader lookup

    @EJB
    private FcCompanyFacade companyFacade;
    @EJB
    private TcUserFacade userFacade;
    @EJB
    private FcUploaderRFacade fcUploaderRFacade;
    @EJB
    private CacheFlushFacade cacheFlushFacade;
    @EJB
    private FcCurrencyFacade fcCurrencyFacade;
    @EJB
    private FcCompGroupFacade fcCompGroupFacade;

    private List<FcCompany> allCompany;
    private List<TcUser> allUser;
    private List<TcUser> filtedUsers = new ArrayList<>();
    private FcUploaderR editUploader;
    private List<SelectItem> allCurrency;//幣別選單
    private Long editCurrencyId;
    private List<SelectItem> groups;//公司群組選單
    private Long groupId;
    
    @PostConstruct
    private void init() {
        allUser = userFacade.findAll();
        allCurrency = this.buildCurrencyOptions();
        groups = this.buildGroupOptions();
        reload();
    }
    
    // action
    public void activeChange(FcCompany company) {
        boolean active = company.getActive();
        company.setActive(!active);
        companyFacade.save(company);
        JsfUtil.addSuccessMessage("已變更!");
    }
    
    public void filterChange() {
        companyList.clear();
        String str = StringUtils.trimToEmpty(filter);
        if (str.isEmpty()) {
            companyList.addAll(allCompany);
        } else {
            for (FcCompany comp : allCompany) {
                if (isCompanyMatch(comp, str)) {
                    companyList.add(comp);
                }
            }
        }
    }
    
    public void editCompany(FcCompany comp) {
        editComp = (null == comp) ? new FcCompany() : comp;
        this.editCurrencyId = (null == editComp.getCurrency())?null:editComp.getCurrency().getId();
        this.groupId = (null == editComp.getGroup())?null:editComp.getGroup().getId();
//        TcUser uploader = editComp.getUploader();
//        autoComplete.setValue(uploader);
//        filtedUsers.clear();
    }
    
    public void save() {
        RequestContext context = RequestContext.getCurrentInstance();
//        autoCompleteToUploader();
        try {
            if(null != this.editCurrencyId){
                editComp.setCurrency(fcCurrencyFacade.find(this.editCurrencyId));
            }
            if(null != this.groupId){
                editComp.setGroup(fcCompGroupFacade.find(groupId));
            }
            companyFacade.save(editComp);
            JsfUtil.addSuccessMessage("儲存成功!");
            context.addCallbackParam("saved", true);
            reload();
        } catch (Exception ex) {
            logger.error("save exception", ex);
            JsfUtil.addErrorMessage("儲存失敗，請確認公司代碼或名稱沒有重覆!");
            context.addCallbackParam("saved", false);
        }
    }
 
    //<editor-fold defaultstate="collapsed" desc="for uploader">
    public void editUploader(FcCompany comp) {
        editComp = (null == comp) ? new FcCompany() : comp;
        editUploader = new FcUploaderR();
        editUploader.setFcCompany(comp);
        autoComplete.setValue(null);
        filtedUsers.clear();
    }
    
    /**
     * 刪除上傳人關聯
     */
    public void deleteUploader() {
        Map map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String idStr = (String) map.get("id");
        long id = Long.parseLong(idStr);
        
        fcUploaderRFacade.remove(id);
        reload();
    }
    
    public void saveUploader() {
        RequestContext context = RequestContext.getCurrentInstance();
        autoCompleteToUploader();
        try {
            if(editUploader.getTcUser()!=null){
                fcUploaderRFacade.save(editUploader);
                JsfUtil.addSuccessMessage("儲存成功!");
                context.addCallbackParam("saved", true);
                reload();
                cacheFlushFacade.cacheFlush();
            }
        } catch (Exception ex) {
            logger.error("save exception", ex);
            JsfUtil.addErrorMessage("儲存失敗，請確認上傳人員是否正確!");
            context.addCallbackParam("saved", false);
        }
    }
    
    public List<TcUser> completeUser(String query) {
        filtedUsers.clear();
        String[] patterns = StringUtils.split(query, " ;,()\t");
        
        if (patterns != null && patterns.length > 0) {
            for (String pattern : patterns) {
                for (TcUser user : allUser) {
                    if (1==user.getId() || user.getDisabled()) {
                        continue;
                    }
                    if (!filtedUsers.contains(user) && isUserMatchPattern(user, pattern)) {
                        boolean duplicate = false;
                        if (editComp.getUploaderList() != null) {
                            for(FcUploaderR fcUploaderR:editComp.getUploaderList()){
                                if(fcUploaderR.getTcUser().getId().equals(user.getId())){
//                                if(fcUploaderR.getTcUser().getId() == user.getId()){
                                    duplicate = true;
                                }
                            }
                        }
                        if(!duplicate){//排除現有的上傳人
                            filtedUsers.add(user);
                        }
                    }
                }
            }
        }
        return filtedUsers;
    }
    
    private void autoCompleteToUploader() {
        Object value = autoComplete.getValue();
        // 如果未輸入任何資料時, 維持原值
        if (filtedUsers.isEmpty()) {
            if (null == value) {
//                editComp.setUploader(null);
                editUploader.setTcUser(null);
            }
            return;
        }
        try {
            // 做selection時value應該是user id
            // 但不做selection直接貼上文字時,有可能出錯
            Long userId = Long.valueOf((String) value);
            for (TcUser user : filtedUsers) {
                if (user.getId().equals(userId)) {
//                    editComp.setUploader(user);
                    editUploader.setTcUser(user);
                    return;
                }
            }
        } catch (Exception ex) {
        }
//        editComp.setUploader(null);
        editUploader.setTcUser(null);
    }
    //</editor-fold>
    
    // helper
    private boolean isCompanyMatch(FcCompany comp, String str) {
        if (StringUtils.containsIgnoreCase(comp.getCode(), str) ||
                StringUtils.containsIgnoreCase(comp.getName(), str)) {
            return true;
        }
        //filter 上傳人
//        TcUser uploader = comp.getUploader();
        if (comp.getUploaderList() != null) {
            for(FcUploaderR fcUploaderR:comp.getUploaderList()){
                TcUser uploader = fcUploaderR.getTcUser();
                if (uploader != null) {
                    if (StringUtils.containsIgnoreCase(uploader.getCname(), str) ||
                            StringUtils.containsIgnoreCase(uploader.getLoginAccount(), str)) {
                        return true;
                    }
                }
            }
        }
        //filter COMP_GROUP
        if (comp.getGroup() != null) {
            if (StringUtils.containsIgnoreCase(comp.getGroup().getCode(), str) ||
                    StringUtils.containsIgnoreCase(comp.getGroup().getName(), str)) {
                return true;
            }
        }
        
        return false;
    }
    
    private void reload() {
        logger.debug("reload");
        //清除cache
        cacheFlushFacade.cacheFlush();
        allCompany = companyFacade.findAll();
        filterChange();
    }
    
    // 比對 TcUser, 符合條件: loginAccount, cname, email 部份符合, cname簡繁部份符合
    private boolean isUserMatchPattern(TcUser user, String pattern) {
        // 包含於
        if (StringUtils.containsIgnoreCase(user.getLoginAccount(), pattern)
                || StringUtils.containsIgnoreCase(user.getCname(), pattern)) {
            return true;
        }
        return false;
    }
    
    private List<SelectItem> buildCurrencyOptions() {
        List<SelectItem> options = new ArrayList();
        options.add(new SelectItem(null, "--選擇幣別--"));
        
        List<FcCurrency> allList = fcCurrencyFacade.findAll();
        for (FcCurrency item : allList) {
            options.add(new SelectItem(item.getId(), item.getName()+"("+item.getCode()+")"));
        }
        
        return options;
    }
    
    private List<SelectItem> buildGroupOptions() {
        List<SelectItem> options = new ArrayList();
        options.add(new SelectItem(null, "--"));
        List<FcCompGroup> allList = fcCompGroupFacade.findAll();
        for (FcCompGroup item : allList) {
            options.add(new SelectItem(item.getId(), item.getName()));
        }
        return options;
    }

    //<editor-fold defaultstate="collapsed" desc="for getter, setter">
    public List<FcCompany> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<FcCompany> companyList) {
        this.companyList = companyList;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public FcCompany getEditComp() {
        return editComp;
    }

    public void setEditComp(FcCompany editComp) {
        this.editComp = editComp;
    }

    public AutoComplete getAutoComplete() {
        return autoComplete;
    }

    public void setAutoComplete(AutoComplete autoComplete) {
        this.autoComplete = autoComplete;
    }

    public List<SelectItem> getAllCurrency() {
        return allCurrency;
    }
//    public List<FcCurrency> getAllCurrency() {
//        return allCurrency;
//    }
    
    public Long getEditCurrencyId() {
        return editCurrencyId;
    }

    public void setEditCurrencyId(Long editCurrencyId) {
        this.editCurrencyId = editCurrencyId;
    }

    public List<SelectItem> getGroups() {
        return groups;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    //</editor-fold>
}
