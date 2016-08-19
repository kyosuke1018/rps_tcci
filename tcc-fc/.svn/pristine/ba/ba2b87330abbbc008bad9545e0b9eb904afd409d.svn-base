package com.tcci.fc.controller.doc;

import com.tcci.fc.entity.repository.TcFolder;
import com.tcci.fc.facade.repository.RepositoryFacade;
import com.tcci.fc.facade.repository.TcFolderFacade;
import com.tcci.fc.util.ConstantUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Lynn.Huang
 */
@ManagedBean
@SessionScoped
public class FolderTreeController implements Serializable {
    private TreeNode root; 
    private TreeNode selectedTreeNode;
    private TcFolder selectedFolder;   
    private TcFolder firstFolder;
    
    @EJB
    private RepositoryFacade repositoryFacade;
    @EJB
    private TcFolderFacade folderFacade;
    
    public TreeNode getRoot() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();     
	String foid = params.get(ConstantUtil.FOID);        
        TcFolder folder = null;     
        if (!StringUtils.isEmpty(foid)) {
            try {
                folder = (TcFolder)folderFacade.getObject(foid);
                setSelectedFolder(folder);
                init();
            } catch (Exception e) {                
            }    
        }            
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TcFolder getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(TcFolder selectedFolder) {
        this.selectedFolder = selectedFolder;
    } 
    
    public TcFolder getFirstFolder() {
        return firstFolder;
    }

    public TreeNode getSelectedTreeNode() {
        return selectedTreeNode;
    }

    public void setSelectedTreeNode(TreeNode selectedTreeNode) {
        this.selectedTreeNode = selectedTreeNode;
    }        

    @PostConstruct
    public void init() {     
        initTree();
    }    
    
    private void initTree(){
        Long rootId = Long.valueOf(1);
        TcFolder rootFolder = repositoryFacade.findFolder(rootId);
        System.out.println("rootFolder=" + rootFolder );
        root = new DefaultTreeNode(rootFolder.getName(), null);  
        initSubTree(rootFolder, root);       
    }
    private void initSubTree(TcFolder folder,TreeNode node){
        List<TcFolder> subFolderList = repositoryFacade.findSubFolders(folder, false);
        if( subFolderList != null && !subFolderList.isEmpty()){
            firstFolder = subFolderList.get(0);
            for( TcFolder subFolder : subFolderList ){               
                System.out.println("subFolder=" + subFolder.getName() );                           
                FolderVO vo = new FolderVO();
                vo.setFolder(subFolder);
                TreeNode subNode = new DefaultTreeNode(vo, node);
                if (selectedFolder != null 
                        && subFolder.getId() == selectedFolder.getId()) {         
                    setSelectedTreeNode(subNode);
                    subNode.setExpanded(true);
                    subNode.setSelected(true);
                }
                initSubTree(subFolder,  subNode );
            }
        }
    }
    
    public void onNodeSelect(NodeSelectEvent event) { 
        TreeNode currentTreeNode = event.getTreeNode();
        Object data = currentTreeNode.getData();
        if (data instanceof FolderVO) {
            TreeNode previousTreeNode = getSelectedTreeNode();
            if (previousTreeNode != null) {
                previousTreeNode.setExpanded(false);
                previousTreeNode.setSelected(false);                
            }
            currentTreeNode.setExpanded(true);
            currentTreeNode.setSelected(true);
            setSelectedTreeNode(currentTreeNode);
            setSelectedFolder( ((FolderVO)data).getFolder() );     
            System.out.println("onNodeSelect SelectedFolder=" + getSelectedFolder() );
        }
    }
}
