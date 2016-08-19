/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.controller;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fcs.entity.FcUserCompGroupR;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.rpt.entity.RptCompanyOrg;
import com.tcci.rpt.facade.RptCompanyOrgFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "companyOrgController")
@ViewScoped
public class CompanyOrgController {
    private final static Logger logger = LoggerFactory.getLogger(CompanyOrgController.class);
    
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
    private RptCompanyOrgFacade rptCompanyOrgFacade;
    //</editor-fold>
    
    
    private List<SelectItem> groups;//企業團
    private CompanyGroupEnum group;
    private List<FcUserCompGroupR> cgList;
    private boolean isAdmin;
    private boolean noPermission = false;
    private List<RptCompanyOrg> companyOrgList = new ArrayList<>();
    private RptCompanyOrg editOrg;
    // tree
    private TreeNode root;
    private static final String ORG_DISABLE = "(停用)";
    private static final String ORG_DISABLE_REGEX = "\\(停用\\)";// regxd: \\(.*?\\)
//    private TreeNode selectedNode;
    
    
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
        
        this.buildCompanyOrg();
    }
    
    public void changeGroup() {
//        logger.debug("changeGroup:" + this.group);
        this.buildCompanyOrg();
    }
    
    public void initAddChild(RptCompanyOrg vo){
        logger.debug("initAddChild parent code:"+vo.getCode());
        editOrg = new RptCompanyOrg();
        editOrg.setGroup(vo.getGroup());
        editOrg.setParent(vo);
        editOrg.setHlevel(vo.getHlevel()+1);//新增的子階層 == parent階層+1
    }
    
    public void addChild(){
        logger.debug("addChild code:"+editOrg.getCode());
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            RptCompanyOrg result = rptCompanyOrgFacade.findByCompany(group, editOrg.getCode());
            if(result!=null){
                JsfUtil.addErrorMessage("儲存失敗! 已存在相同公司");
                context.addCallbackParam("saved", false);
                return;
            }
            rptCompanyOrgFacade.save(editOrg);
            
            this.buildCompanyOrg();//reload
            JsfUtil.addSuccessMessage("儲存完畢!");
            context.addCallbackParam("saved", true);
        } catch (Exception ex) {
//            ex.printStackTrace();
            logger.error("save exception", ex);
            JsfUtil.addErrorMessage("儲存失敗!");
            context.addCallbackParam("saved", false);
        }
    }
    
    public void deleteNode(RptCompanyOrg vo){
        logger.debug("deleteNode code:"+vo.getCode());
        try {
//            List<RptCompanyOrg> result = rptCompanyOrgFacade.findByParent(group, vo);
//            if (CollectionUtils.isNotEmpty(result)) {
//                JsfUtil.addErrorMessage("刪除失敗! 該公司已存在子公司");
//                return;
//            }
            if(this.hasChild(vo)){
                JsfUtil.addErrorMessage("刪除失敗! 該公司已存在子公司");
                return;
            }
            rptCompanyOrgFacade.remove(vo.getId());
            
            this.buildCompanyOrg();//reload
            JsfUtil.addSuccessMessage("刪除完畢!");
        } catch (Exception ex) {
//            ex.printStackTrace();
            logger.error("save exception", ex);
            JsfUtil.addErrorMessage("刪除失敗!");
        }
    }
    
    public boolean isOrgDisabled(RptCompanyOrg vo){
        return vo.getCode().endsWith(ORG_DISABLE);
    }
    
    public void disableChange(RptCompanyOrg vo){
        try {
            if(this.isOrgDisabled(vo)){
//                vo.setCode(vo.getCode().replaceAll(ORG_DISABLE, ""));//括號無法replace
                vo.setCode(vo.getCode().replaceAll(ORG_DISABLE_REGEX, ""));//改用正規
                rptCompanyOrgFacade.save(vo);
            }else{
                vo.setCode(vo.getCode()+ORG_DISABLE);
                rptCompanyOrgFacade.save(vo);
            }
            
            this.buildCompanyOrg();//reload
            JsfUtil.addSuccessMessage("修改完畢!");
        } catch (Exception ex) {
//            ex.printStackTrace();
            logger.error("save exception", ex);
            JsfUtil.addErrorMessage("修改失敗!");
        }
    }
    
    //是否有child
    public boolean hasChild(RptCompanyOrg vo){
        List<RptCompanyOrg> result = rptCompanyOrgFacade.findByParent(group, vo);
        return CollectionUtils.isNotEmpty(result);
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
    
    private void buildCompanyOrg(){
        companyOrgList = rptCompanyOrgFacade.findAll(group);
        
        this.buildOrgsTree();
//        selectedNode = root;
    }
    
    /**
     * 依 DB 資訊建置組織圖
     */
    private void buildOrgsTree() {
        root = null;
        for(RptCompanyOrg orgVO : companyOrgList){
            if(orgVO.getParent() == null){
                root = new DefaultTreeNode(new OrganizationVO(orgVO.getId(), false, orgVO), null);
                break;
            }
        }
        
        if(root != null){
            this.buildSubOrgTree(root); // 遞迴建立組織圖
            JsfUtil.collapsingORExpandingTree(root, true);// 預設全部展開  
        }
    }
    
    /**
     * 遞迴建立組織圖子樹
     * @param node
     */
    private void buildSubOrgTree(TreeNode node) {
        OrganizationVO parent = (OrganizationVO)node.getData();
        long parentId = parent.getKey();
        for(RptCompanyOrg orgVO : companyOrgList){
            if( orgVO.getParent()!=null && orgVO.getParent().getId()==parentId ){// 子項目
                parent.setHasChild(true);//有child
                
                TreeNode newNode = new DefaultTreeNode(new OrganizationVO(orgVO.getId(), false, orgVO), node);
                buildSubOrgTree(newNode);
                JsfUtil.collapsingORExpandingTree(newNode, true);// 預設全部展開  
            }
        }
    }
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getter setter">
    public List<SelectItem> getGroups() {
        return groups;
    }

    public boolean isNoPermission() {
        return noPermission;
    }

    public CompanyGroupEnum getGroup() {
        return group;
    }

    public void setGroup(CompanyGroupEnum group) {
        this.group = group;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public List<RptCompanyOrg> getCompanyOrgList() {
        return companyOrgList;
    }

    public TreeNode getRoot() {
        return root;
    }

    public RptCompanyOrg getEditOrg() {
        return editOrg;
    }

    public void setEditOrg(RptCompanyOrg editOrg) {
        this.editOrg = editOrg;
    }
    //</editor-fold>
}
