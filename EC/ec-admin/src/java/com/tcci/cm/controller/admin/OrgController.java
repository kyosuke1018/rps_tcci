/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.controller.admin;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.entity.admin.CmOrg;
import com.tcci.cm.enums.OrgTypeEnum;
import com.tcci.cm.facade.admin.CmOrgFacade;
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.model.admin.OrganizationVO;
import com.tcci.cm.model.global.GlobalConstant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * organization management controller
 * @author Peter.pan
 */
@ManagedBean(name = "orgController")
@ViewScoped
public class OrgController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 5;
    public static final String DATATABLE_RESULT = "fmMain:dtResult";

    @EJB private CmOrgFacade cmOrgFacade;
    
    // tree
    private TreeNode root;
    private TreeNode selectedNode;

    private List<OrgTypeEnum> orgTypes;    
    private OrganizationVO editItem;
    
    private List<CmOrg> orglist;
    
    private boolean doAdd;
    
    @PostConstruct
    private void init(){
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        // Get view Id
        viewId = JsfUtils.getViewId();

        // 組織列表
        orglist = cmOrgFacade.findAll();
        
        buildOrgsTree();
        JsfUtils.collapsingORExpandingTree(root, true);
        selectedNode = root;
        
        orgTypes = new ArrayList<OrgTypeEnum>();
        orgTypes.addAll(Arrays.asList(OrgTypeEnum.values()));
    }    

    /**
     * 功能標題
     * @return
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }

    /**
     * 依 DB 資訊建置組織圖
     */
    public void buildOrgsTree() {
        CmOrg cmOrg = new CmOrg();
        cmOrg.setId(0L);
        cmOrg.setCode("");
        cmOrg.setCtype(OrgTypeEnum.GROUP.getCode());
        cmOrg.setName(GlobalConstant.SYS_ORG_ROOT);
        
        root = new DefaultTreeNode(new OrganizationVO(0, false, cmOrg), null);
        buildSubOrgTree(root); // 遞迴建立組織圖

        JsfUtils.collapsingORExpandingTree(root, true); // 預設全部展開    
    }
    
    /**
     * 遞迴建立組織圖子樹
     * @param node
     */
    public void buildSubOrgTree(TreeNode node) {
        if( orglist==null ){
            return;
        }
        OrganizationVO organizationVO = (OrganizationVO)node.getData();
        long parentId = organizationVO.getCmOrg().getId();
        
        for(CmOrg cmOrg: orglist){
            if( (cmOrg.getParent()==null && parentId==0) 
                    // || (cmOrg.getParent()!=null && cmOrg.getParent().getId()==parentId) ){// 子項目
                    || (cmOrg.getParent()!=null && cmOrg.getParent()==parentId) ){// 子項目
                OrganizationVO newNodeVO = new OrganizationVO();
                newNodeVO.setCmOrg(cmOrg);
                newNodeVO.setKey(cmOrg.getId());// Id為唯一，可用來做 key   
                
                organizationVO.setHasChild(true);
                
                TreeNode newNode = new DefaultTreeNode(newNodeVO, node);
                
                buildSubOrgTree(newNode);
            }
        }
    }
    
    /**
     * 刪除節點
     */
    public void deleteNode(long key) {
        logger.debug("deleteNode ... key ="+key);
        
        selectedNode = findOrgNodeByKey(root, key);
        
        if( selectedNode.getChildCount()>0 ){
            JsfUtils.addErrorMessage("有子節點不可刪除!");
            return;
        }
        
        OrganizationVO nodeVO = (OrganizationVO)selectedNode.getData();
        
        if( cmOrgFacade.hasRelationData(nodeVO.getCmOrg().getId()) ){
            JsfUtils.addErrorMessage("已有關聯資料，不可刪除!");
            return;
        }
        
        // DB
        if( nodeVO.getCmOrg()!=null && nodeVO.getCmOrg().getId()>0 ){
            cmOrgFacade.remove(nodeVO.getCmOrg());
        }
        
        // UI
        TreeNode parentNote = selectedNode.getParent();
        if( parentNote.getChildCount()<=1 ){
            ((OrganizationVO)parentNote.getData()).setHasChild(false);
        }
        
        selectedNode.getChildren().clear();
        selectedNode.getParent().getChildren().remove(selectedNode);
        selectedNode.setParent(null);
        selectedNode = null;
        
        root.setSelected(false);
    }
    
    /**
     * 新增子節點 STEP1 (因根節點 actionListoner 不會觸發，故用 remoteCommand 兩步驟方式)
     * @param parent 
     */
    public void addChild(OrganizationVO parent){
        logger.debug("addChild ... parent = "+parent.toString());
        selectedNode = this.findOrgNodeByKey(root, parent.getKey());
    }   
    
    /**
     * 新增子節點 STEP2 (因根節點 actionListoner 不會觸發，故用 remoteCommand 兩步驟方式)
     */
    public void addNode(){
        logger.debug("addNode ... ");

        if( selectedNode==null ){// 根節點
            selectedNode = root;
        }
        
        // 準備新節點資料
        // selectedNode = findOrgNodeByKey(root, selectedNode);
        
        long nextKey = getOrgNodeNextKey(root, 0);
        
        CmOrg cmOrg = new CmOrg();
        cmOrg.setId(0L);
        cmOrg.setCtype(OrgTypeEnum.DEPARTMENT.getCode());
        //cmOrg.setParent(((OrganizationVO)selectedNode.getData()).getCmOrg());
        cmOrg.setParent(((OrganizationVO)selectedNode.getData()).getCmOrg().getId());
        
        this.editItem = new OrganizationVO(nextKey, false, cmOrg);
        
        doAdd = true;
    }
    
    /**
     * 編輯節點
     * @param key 
     */
    public void editNode(long key){
        logger.debug("editNode ... key ="+key);
        selectedNode = findOrgNodeByKey(root, key);
        if( selectedNode!=null ){
            this.editItem = (OrganizationVO)selectedNode.getData();
        }

        doAdd = false;
    }
    
    /**
     * 儲存
     */
    public void save(){
       logger.debug("save ... doAdd ="+doAdd);
       if( doAdd ){
           TreeNode newNode = new DefaultTreeNode(editItem, selectedNode);// 加入子節點
           selectedNode.setExpanded(true);
           selectedNode.setSelected(false);
           newNode.setSelected(true);
           root.setSelected(false);
           ((OrganizationVO)selectedNode.getData()).setHasChild(true);
       }
       
       cmOrgFacade.save(editItem.getCmOrg(), this.getLoginUser());
       selectedNode = null;
    }
    
    /**
     * 依 key 找特定組織 Tree Node
     * @param n
     * @param key
     * @return 
     */
    public TreeNode findOrgNodeByKey(TreeNode n, long key) {
        if( n==null ){
            return null;
        }
        
        if( ((OrganizationVO)n.getData()).getKey()==key ){
            return n;
        }else{
            if(  n.getChildren()==null || n.getChildren().isEmpty() ){
                return null;
            }else{
                for(TreeNode s: n.getChildren()) {
                    TreeNode node = findOrgNodeByKey(s, key);
                    if( node!=null ){
                        return node;
                    }
                }
                return null;
            }
        }
    }
    
    /**
     * 找出下一可用 key 編號
     * @param n
     * @param max
     * @return 
     */
    public long getOrgNodeNextKey(TreeNode n, long max) {
        if( n==null ){
            return max+1;
        }
        
        if( ((OrganizationVO)n.getData()).getKey()>max ){
            max = ((OrganizationVO)n.getData()).getKey();
        }
        
        if(  n.getChildren()==null || n.getChildren().isEmpty() ){
                return max+1;
        }else{
            for(TreeNode s: n.getChildren()) {
                long smax = getOrgNodeNextKey(s, max);
                if( smax > max+1 ){
                    max = smax;
                }
            }
            return max+1;
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter"> 
    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getRoot() {
        return root;
    }
    
    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }
    
    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public List<OrgTypeEnum> getOrgTypes() {
        return orgTypes;
    }

    public void setOrgTypes(List<OrgTypeEnum> orgTypes) {
        this.orgTypes = orgTypes;
    }
    
    public OrganizationVO getEditItem() {
        return editItem;
    }

    public void setEditItem(OrganizationVO editItem) {
        this.editItem = editItem;
    }
    //</editor-fold>

}
