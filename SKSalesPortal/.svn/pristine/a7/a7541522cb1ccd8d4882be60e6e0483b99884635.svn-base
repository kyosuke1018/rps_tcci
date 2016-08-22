/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.admin;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.SelectEmployeeController;
import com.tcci.sksp.entity.org.SkSalesChannelMember;
import com.tcci.sksp.entity.org.SkSalesChannels;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkSalesChannelMemberFacade;
import com.tcci.sksp.facade.SkSalesChannelsFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class SalesOrgListController {
//<editor-fold defaultstate="collapsed" desc="Properties">

    private TreeNode salesChannelRoot;
    private TreeNode selectedNode;
    private SkSalesChannels selectedSalesChannel;
    private SkSalesMember selectedSalesMember;    
    private List<SkSalesMember> salesMemberList;
    private boolean hasChildOrMember;
    private boolean saveMemberOK;
    //</editor-fold>
//<editor-fold defaultstate="collapsed" desc="EJB">
    @EJB
    SkSalesChannelsFacade salesChannelsFacade;
    @EJB
    SkSalesMemberFacade salesMemberFacade;    
    @EJB
    SkSalesChannelMemberFacade salesChannelMemberFacade;
    @EJB
    TcUserFacade tcUserFacade;
//</editor-fold>    

    @ManagedProperty(value = "#{selectEmployeeController}")
    SelectEmployeeController selectEmployeeController;

    public void setSelectEmployeeController(SelectEmployeeController selectEmployeeController) {
        this.selectEmployeeController = selectEmployeeController;
    }    
    
    public TreeNode getSalesChannelRoot() {       
        return salesChannelRoot;
    }

    public void setSalesChannelRoot(TreeNode salesChannelRoot) {
        this.salesChannelRoot = salesChannelRoot;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public SkSalesChannels getSelectedSalesChannel() {
        return selectedSalesChannel;
    }

    public void setSelectedSalesChannel(SkSalesChannels selectedSalesChannel) {
        this.selectedSalesChannel = selectedSalesChannel;
    }

    public SkSalesMember getSelectedSalesMember() {
        return selectedSalesMember;
    }

    public void setSelectedSalesMember(SkSalesMember selectedSalesMember) {
        this.selectedSalesMember = selectedSalesMember;
    }

    public List<SkSalesMember> getSalesMemberList() {
        return salesMemberList;
    }

    public void setSalesMemberList(List<SkSalesMember> salesMemberList) {
        this.salesMemberList = salesMemberList;
    }

    public boolean isHasChildOrMember() {
        return hasChildOrMember;
    }

    public void setHasChildOrMember(boolean hasChildOrMember) {
        this.hasChildOrMember = hasChildOrMember;
    }

    public boolean isSaveMemberOK() {
        return saveMemberOK;
    }

    public void setSaveMemberOK(boolean saveMemberOK) {
        this.saveMemberOK = saveMemberOK;
    }

    @PostConstruct
    public void init() {
        initTreeNode();
        saveMemberOK = false;        
    }    

    private void initTreeNode() {
        salesChannelRoot = new DefaultTreeNode("root", null);
        salesChannelRoot.setExpanded(true);
        SkSalesChannels channel = null;
        buildChildTree(channel,salesChannelRoot);
    }

    private void buildChildTree(SkSalesChannels channel, TreeNode parentNode) {
        List<SkSalesChannels> childList = null;
        if( channel == null )
            childList = salesChannelsFacade.findRootChannel();
        else
            childList = salesChannelsFacade.findChildChannel(channel);
        if (childList != null && !childList.isEmpty()) {
            for (SkSalesChannels childChannel : childList) {
                TreeNode childNode = new DefaultTreeNode(childChannel, parentNode);
                childNode.setExpanded(true);
                buildChildTree(childChannel, childNode);
            }
        }
    }    

    public void onNodeSelect(NodeSelectEvent event) {
        selectedSalesChannel = (SkSalesChannels)event.getTreeNode().getData();
        refreshSelectedNodeMemberList();           
    }    
    
    public void refreshTreeAndMemberList(SkSalesChannels selectedSalesChannel) {
        init();
        this.selectedSalesChannel = selectedSalesChannel;
        refreshSelectedNodeMemberList();
    }
    
    public void refreshSelectedNodeMemberList() {
        setSalesMemberList( salesMemberFacade.findByChannels(selectedSalesChannel) ); 
    }

    public void deleteChannel() {
       salesChannelsFacade.deleteChannel(selectedSalesChannel);
       init();       
    }    
    
    public void refreshSelectedSalesMember(SkSalesMember selectedSalesMember) {
        this.selectedSalesMember = selectedSalesMember;
        TcUser user = selectedSalesMember.getMember();
        selectEmployeeController.setSelectedUser(user != null ? user.getEmpId() : null);
        selectEmployeeController.setCname(user != null ? user.getCname() : null);     
    }
    
    public void addNewSelectedSalesMember() {
        this.selectedSalesMember = new SkSalesMember();
        selectEmployeeController.setSelectedUser(null);
        selectEmployeeController.setCname(null);        
    }   
    
    private void validate(boolean isCreate) throws Exception {
        if (StringUtils.isEmpty(selectedSalesMember.getCode())) {
            throw new Exception(FacesUtil.getMessage("sales.channel.code.required"));           
        }
        if (isCreate && !StringUtils.isEmpty(selectedSalesMember.getCode())) {
            SkSalesMember member = salesMemberFacade.findByCode(selectedSalesMember.getCode());
            if (member != null) {
                throw new Exception(FacesUtil.getMessage("sales.member.exist.error"));            
            }
        }        
    }

    public void saveMember() {
        boolean isCreate = (selectedSalesMember.getId() == null);
        try {
            validate(isCreate);
            if (!StringUtils.isEmpty(selectEmployeeController.getSelectedUser())) {
                TcUser user = tcUserFacade.findUserByEmpId(selectEmployeeController.getSelectedUser());
                selectedSalesMember.setMember(user);
            } else {
                selectedSalesMember.setMember(null);
            }
            if (selectedSalesMember.getId() != null) {
                salesMemberFacade.edit(selectedSalesMember);
            } else {
                salesMemberFacade.create(selectedSalesMember);
                SkSalesChannelMember channelMember = new SkSalesChannelMember();
                channelMember.setSalesMember(selectedSalesMember);
                channelMember.setSalesChannel(selectedSalesChannel);
                salesChannelMemberFacade.create(channelMember);
            }
            saveMemberOK = true;            
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, FacesUtil.getMessage("remitmaintenance.msg.save.success"));
        } catch (Exception e) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("errormsg", e.getLocalizedMessage());            
        }
    }
    
    public void checkChildAndMember() {       
        boolean hasChild = (selectedNode.getChildCount() > 0);
         boolean hasMember = false;
        SkSalesChannelMember channelMember = salesChannelMemberFacade.findByChannel(selectedSalesChannel);
        if (channelMember != null) {
            hasMember = true;
        }
        hasChildOrMember = (hasChild || hasMember);
    }
}
