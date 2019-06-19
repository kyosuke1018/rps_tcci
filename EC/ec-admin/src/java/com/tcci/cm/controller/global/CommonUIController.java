/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.controller.global;

import com.tcci.cm.entity.admin.CmOrg;
import com.tcci.cm.enums.BooleanTypeEnum;
import com.tcci.cm.enums.OrgTypeEnum;
import com.tcci.cm.facade.admin.CmOrgFacade;
import com.tcci.cm.enums.ActivityLogEnum;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcGroupComparator;
import com.tcci.fc.facade.org.TcGroupFacade;
import com.tcci.fc.util.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
@ManagedBean(name = "ui")
@ApplicationScoped
public class CommonUIController {
    private static final Logger logger = LoggerFactory.getLogger(CommonUIController.class);

    @EJB private TcGroupFacade tcGroupFacade;
    @EJB private CmOrgFacade cmOrgFacade;
    //@EJB private CmImpRuleFacade cmImpRuleFacade;
    
    private List<SelectItem> tcGroups;// 系統群組
    private List<SelectItem> tcGroupsMultiSelect;// 系統群組多選
    private List<SelectItem> cmOrgMultiSelect;// 系統組織多選
    private List<SelectItem> userOrgOptions;//user所屬組織選單 
    private List<SelectItem> companyOrgOptions;//系統組織 公司(無parent)
    private List<SelectItem> orgTypeOtions;
    
    private List<SelectItem> reportModuleOptions;// 報表模組
    private List<SelectItem> reportPeriodOptions;// 報表區間
    private List<SelectItem> reportTypeOptions;// 報表類型
    
    private List<SelectItem> boolOptions; // 有/無選單
    
    private List<SelectItem> activityLogOptions; // 操作類別
    private List<SelectItem> monthOptions; // 月份選單
    
    @PostConstruct
    public void init(){
        logger.debug("CommonUIController init ...");
        
        monthOptions = buildMonthOptions();
        
        tcGroups = buildGroupOptions(true);
        tcGroupsMultiSelect = buildGroupOptions(false);
        userOrgOptions = buildOrgOptions(true, false, true);
        companyOrgOptions = buildOrgOptions(true, true, false);

        orgTypeOtions = buildOrgTypeOptions();
   
        // 有/無選單
        boolOptions = buildBoolOptions();
        // 操作類別
        activityLogOptions = buildActivityLogOptions();
    }
    
    public void selectUser(){
        logger.debug("selectUser ...");
    }

    /**
     * 月份選單
     * @return 
     */
    private  List<SelectItem> buildMonthOptions(){
        List<SelectItem> options = new ArrayList<SelectItem>();
        for(int i=1; i<=12; i++){
            options.add((new SelectItem(Integer.valueOf(i).toString(), Integer.valueOf(i).toString())));
        }
        return options;
    }
    
    /**
     * 有/無選單
     * @return 
     */
    private List<SelectItem> buildBoolOptions(){
        List<SelectItem> options = new ArrayList<SelectItem>();
        options.add(new SelectItem(Boolean.FALSE, "無"));
        options.add(new SelectItem(Boolean.TRUE, "有"));
        
        return options;
    }
    
    /**
     * 操作類別
     * @return 
     */
    private List<SelectItem> buildActivityLogOptions() {
        List<SelectItem> options = new ArrayList<SelectItem>();
        for(ActivityLogEnum item : ActivityLogEnum.values()){
            options.add(new SelectItem(item.getCode(), item.getCode() + "-" + item.getDisplayName()));
        }
        return options;
    }    
        
    /**
     * 系統角色群組選單
     * @param includeNoSelect
     * @return 
     */
    private List<SelectItem> buildGroupOptions(boolean includeNoSelect){
        List<SelectItem> options = new ArrayList<SelectItem>();
        if( includeNoSelect ){
            options.add(new SelectItem(Long.valueOf(0), "---"));
        }
        
        List<TcGroup> groupList = tcGroupFacade.findAll();
        // 排序 by code
        TcGroupComparator tcGroupComparator = new TcGroupComparator();
        Collections.sort(groupList, tcGroupComparator);
        
        if( groupList!=null ){
            for (TcGroup group : groupList) {
                if (includeNoSelect) {
                    options.add(new SelectItem(group.getId(), group.getName() + "-" + group.getCode()));
                } else {
                    options.add(new SelectItem(group.getId(), group.getName() + "-" + group.getCode()));
                }
            }
        }
        return options;
    }
    
    /**
     * 組織類別選單
     */
    private List<SelectItem> buildOrgTypeOptions(){
        List<SelectItem> options = new ArrayList<SelectItem>();
        for(OrgTypeEnum item : OrgTypeEnum.values()){
            options.add(new SelectItem(item.getCode(), item.getDisplayName()));
        }
        return options;
    }
    
    /**
     * 系統組織選單
     * @param includeNoSelect
     * @return 
     */
    private List<SelectItem> buildOrgOptions(boolean includeNoSelect, boolean onlyParent, boolean onlyChild){
        List<SelectItem> options = new ArrayList<SelectItem>();
        if( includeNoSelect ){
            options.add(new SelectItem(Long.valueOf(0), "---"));
        }
        
        List<CmOrg> orgList = cmOrgFacade.findAllSortByName();
        if( orgList!=null ){
            for(CmOrg org : orgList){
                if(onlyParent){
                    //只取父節點的組織 公司
                    if(org.getParent()==null){
                        options.add(new SelectItem(org.getId().toString(), org.getName()));
                    }
                }else if(onlyChild){
                    //只取子節點的組織 部門 廠端
                    if(org.getParent()!=null){
                        CmOrg parent = getFromListById(orgList, org.getParent());
                        String label = (parent!=null)?"("+parent.getName()+")"+org.getName():org.getName();
                        
                        options.add(new SelectItem(org.getId().toString(), label));
                    }
                }else{
                    options.add(new SelectItem(org.getId().toString(), org.getName()));
                }
            }
        }
        return options;
    }
    
    private CmOrg getFromListById(List<CmOrg> orgList, Long id){
        if( orgList!=null && id!=null ){
            for(CmOrg org : orgList){
                if( id.equals(org.getId()) ){
                    return org;
                }
            }
        }
        return null;
    }

    /**
     * 最多顯示字串長度
     * @param ori
     * @return 
     */
    public String showMaxStr(String ori){
        return StringUtils.showMaxTxt(ori, GlobalConstant.STR_MAX_SHOW_LEN);
    }
    public String showMaxTxt(String ori){
        return StringUtils.showMaxTxt(ori, GlobalConstant.TXT_MAX_SHOW_LEN);
    }
    public String showMaxMsg(String ori){
        return StringUtils.showMaxTxt(ori, GlobalConstant.MSG_MAX_SHOW_LEN);
    }
    public String showMaxTxt(String ori, int len){
        return StringUtils.showMaxTxt(ori, len);
    }
    
    public String getReOpsLabel(List<SelectItem> ops,String code){
        String label ="";
        if(null!=ops){
        for(SelectItem si :ops){
            //logger.debug("si.getValue()="+si.getValue());
            //logger.debug("code="+code);
            if(si.getValue().equals(code)){
                label = si.getLabel();
            }
        }
        }
        return label;
    }
    
     public String getReCompanyOpsLabel(List<SelectItem> ops,String code){
        String label ="";
        if(null==code || code.length()==0){
           label = "";
        }else{
            for(SelectItem si :ops){
                if(si.getValue()== Long.valueOf(code)){
                    label = si.getLabel();
                }
            }
        }
        logger.debug("label="+label);
        return label;
    }
     
    public String getRePlantOpsLabel(List<SelectItem> ops,Long code){
        String label ="";
        if(null==code){
           label = "";
        }else{
            for(SelectItem si :ops){
                if(((Long)si.getValue()).longValue()==code.longValue()){
                    label = si.getLabel();
                }
            }
        }
        logger.debug("ops.size()="+ops.size());
        logger.debug("label="+label);
        return label;
    }
        
    // Y/N
    public String getYes(){
        return BooleanTypeEnum.YES.getYn();
    }
    public String getNo(){
        return BooleanTypeEnum.NO.getYn();
    }
       
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">    
    public TcGroupFacade getTcGroupFacade() {
        return tcGroupFacade;
    }

    public void setTcGroupFacade(TcGroupFacade tcGroupFacade) {
        this.tcGroupFacade = tcGroupFacade;
    }

    public List<SelectItem> getMonthOptions() {
        return monthOptions;
    }

    public void setMonthOptions(List<SelectItem> monthOptions) {
        this.monthOptions = monthOptions;
    }

    public List<SelectItem> getActivityLogOptions() {
        return activityLogOptions;
    }

    public void setActivityLogOptions(List<SelectItem> activityLogOptions) {
        this.activityLogOptions = activityLogOptions;
    }

    public List<SelectItem> getBoolOptions() {
        return boolOptions;
    }

    public void setBoolOptions(List<SelectItem> boolOptions) {
        this.boolOptions = boolOptions;
    }

    public List<SelectItem> getReportModuleOptions() {
        return reportModuleOptions;
    }

    public void setReportModuleOptions(List<SelectItem> reportModuleOptions) {
        this.reportModuleOptions = reportModuleOptions;
    }

    public List<SelectItem> getReportTypeOptions() {
        return reportTypeOptions;
    }

    public void setReportTypeOptions(List<SelectItem> reportTypeOptions) {
        this.reportTypeOptions = reportTypeOptions;
    }

    public List<SelectItem> getReportPeriodOptions() {
        return reportPeriodOptions;
    }

    public void setReportPeriodOptions(List<SelectItem> reportPeriodOptions) {
        this.reportPeriodOptions = reportPeriodOptions;
    }

    public List<SelectItem> getOrgTypeOtions() {
        return orgTypeOtions;
    }

    public void setOrgTypeOtions(List<SelectItem> orgTypeOtions) {
        this.orgTypeOtions = orgTypeOtions;
    }

    public List<SelectItem> getTcGroups() {
        return tcGroups;
    }

    public void setTcGroups(List<SelectItem> tcGroups) {
        this.tcGroups = tcGroups;
    }

    public List<SelectItem> getTcGroupsMultiSelect() {
        return tcGroupsMultiSelect;
    }

    public void setTcGroupsMultiSelect(List<SelectItem> tcGroupsMultiSelect) {
        this.tcGroupsMultiSelect = tcGroupsMultiSelect;
    }
    
    public List<SelectItem> getCmOrgMultiSelect() {
        return cmOrgMultiSelect;
    }

    public void setCmOrgMultiSelect(List<SelectItem> cmOrgMultiSelect) {
        this.cmOrgMultiSelect = cmOrgMultiSelect;
    }
    
    public List<SelectItem> getUserOrgOptions() {
        return userOrgOptions;
    }

    public void setUserOrgOptions(List<SelectItem> userOrgOptions) {
        this.userOrgOptions = userOrgOptions;
    }
    
    public List<SelectItem> getCompanyOrgOptions() {
        return companyOrgOptions;
    }

    public void setCompanyOrgOptions(List<SelectItem> companyOrgOptions) {
        this.companyOrgOptions = companyOrgOptions;
    }
    //</editor-fold>

}
