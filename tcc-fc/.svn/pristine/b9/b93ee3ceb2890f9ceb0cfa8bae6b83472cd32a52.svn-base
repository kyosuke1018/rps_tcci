package com.tcci.fc.controller.doc;

import com.tcci.fc.controller.util.MessageUtils;
import com.tcci.fc.entity.repository.TcDocument;
import com.tcci.fc.entity.repository.TcFolder;
import com.tcci.fc.facade.repository.RepositoryFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lynn.Huang
 */
@ManagedBean
@RequestScoped
public class FolderController {

    Logger logger = LoggerFactory.getLogger(FolderController.class);
    private String folderName;

    @EJB
    private RepositoryFacade repositoryFacade;

    @ManagedProperty(value = "#{folderTreeController}")
    private FolderTreeController folderTreeController;

    public void setFolderTreeController(FolderTreeController folderTreeController) {
        this.folderTreeController = folderTreeController;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void prepareCreateFolder() {
        this.folderName = "";
    }

    public void prepareCreateRootFolder() {
        folderTreeController.setSelectedFolder(folderTreeController.getRootFolder());
        prepareCreateFolder();
    }

    public void createFolder() {
        try {
            TcFolder parentFolder = folderTreeController.getSelectedFolder();
            if (parentFolder == null) {
                parentFolder = folderTreeController.getRootFolder();
            }
            logger.debug("parentFolder={}", parentFolder);
            TcFolder newFolder = repositoryFacade.createFolder(parentFolder, folderName);
            folderTreeController.setSelectedFolder(newFolder);
            folderTreeController.init();
        } catch (Exception e) {
            logger.error("e={}", e);
            MessageUtils.addErrorMessage("Create Folder Failed!", e.getMessage());
        }
    }

    public void deleteFolder() {
        TcFolder folder = folderTreeController.getSelectedFolder();
        List<TcFolder> subFolders = repositoryFacade.findSubFolders(folder);
        if (subFolders.size() > 0) {
            MessageUtils.addErrorMessage("Deleted Failed!", "The folder '" + folder.getName() + "' can't has subfolders while being deleted!");
            return;
        }
        List<TcDocument> documents = repositoryFacade.findDocuments(folder, false);
        if (documents.size() > 0) {
            MessageUtils.addErrorMessage("Deleted Failed!", "Folder '" + folder.getName() + "' can't has documents while being deleted!");
            return;
        }
        TcFolder parentFolder = folder.getFolder();
        try {
            repositoryFacade.removeFolder(folder, false);
            MessageUtils.addSuccessMessage("Deleted Successfully!", "Folder '" + folder.getName() + "' was deleted successfully!");
        } catch (Exception e) {
            MessageUtils.addErrorMessage("Deleted Failed!", e.getMessage());
            return;
        }
        if (parentFolder != null) {
            folderTreeController.setSelectedFolder(parentFolder);
            folderTreeController.init();
        }
    }

}
