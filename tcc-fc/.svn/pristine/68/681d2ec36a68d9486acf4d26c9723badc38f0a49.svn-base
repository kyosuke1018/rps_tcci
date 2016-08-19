package com.tcci.fc.controller.doc;

import com.tcci.fc.entity.repository.Foldered;
import com.tcci.fc.entity.repository.TcDocument;
import com.tcci.fc.entity.repository.TcFolder;
import com.tcci.fc.facade.repository.RepositoryFacade;
import com.tcci.fc.facade.repository.TcFolderFacade;
import com.tcci.fc.util.FacesUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author Lynn.Huang
 */
@ManagedBean
@RequestScoped
public class ListDocumentController implements Serializable {

    private List<Foldered> folderedList;
    private String oid;
    @EJB
    private RepositoryFacade repositoryFacade;
    @EJB
    private TcFolderFacade folderFacade;
    @ManagedProperty(value = "#{folderTreeController}")
    private FolderTreeController folderTreeController;

    public void setFolderTreeController(FolderTreeController folderTreeController) {
        this.folderTreeController = folderTreeController;
    }

    public List<Foldered> getFolderedList() {
        return folderedList;
    }

    @PostConstruct
    public void init() {
//        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();     
//	oid = params.get(ConstantUtil.OID); 
//        
//        TcFolder folder = null;        
//        if (!StringUtils.isEmpty(oid)) {
//            try {
//                folder = (TcFolder)folderFacade.getObject(oid);
//                folderTreeController.setSelectedFolder(folder);
//                folderTreeController.init();
//            } catch (Exception e) {                
//            }    
//        } 
        TcFolder folder = folderTreeController.getSelectedFolder();
        if (folder == null) {
            folder = folderTreeController.getFirstFolder();
        }
        if (folder != null) {
            folderedList = new ArrayList<Foldered>();
            folderedList.addAll(repositoryFacade.findSubFolders(folder, false));
            List<TcDocument> allDocuments = repositoryFacade.findDocuments(folder, false, false);
            for (TcDocument document : allDocuments) {
                if (document.getIslatestiteration()) {
                    folderedList.add(document);
                }
            }
        }
    }

    public boolean isDocument(Foldered foldered) {
        boolean isDoc = false;
        if (foldered instanceof TcDocument) {
            isDoc = true;
        }
        return isDoc;
    }
}
