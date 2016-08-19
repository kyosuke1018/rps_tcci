package com.tcci.fc.facade.repository;

import com.tcci.fc.entity.access.FolderedAccessControlled;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.repository.Foldered;
import com.tcci.fc.entity.repository.TcDocument;
import com.tcci.fc.entity.repository.TcDocumentMaster;
import com.tcci.fc.facade.content.ContentFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.tcci.fc.entity.repository.TcFolder;
import com.tcci.fc.event.repository.FolderedEvent;
import com.tcci.fc.exception.repository.FolderExistsException;
import com.tcci.fc.exception.repository.FolderNameRequiredException;
import com.tcci.fc.exception.repository.FolderNullException;
import com.tcci.fc.facade.access.PermissionUtils;
import com.tcci.fc.facade.access.TcFolderedAclEntryFacade;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.util.TcException;
import java.math.BigDecimal;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Named
public class RepositoryFacade {

    int ITEM_STATE_REMOVED = 0;
    int RECOVERY_MODE_MERGE = 1;
    int RECOVERY_MODE_REPLACE = 2;
    Logger logger = LoggerFactory.getLogger(RepositoryFacade.class);
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @Inject
    Event<FolderedEvent> event;
    @Resource
    SessionContext ctx;
    @Inject
    private ContentFacade contentFacade;
    @EJB
    private TcFolderFacade folderFacade;
    @EJB
    private TcDocumentFacade documentFacade;
    @EJB
    private TcDocumentMasterFacade documentMasterFacade;
    @EJB
    private TcUserFacade userFacade;
    @EJB
    private TcFolderedAclEntryFacade folderedAclEntryFacade;

    public List<TcFolder> findFirstLevelFolders() {
        String sql = "SELECT f FROM TcFolder f WHERE f.folder is null ORDER BY f.name";
        Query query = em.createQuery(sql);
        return query.getResultList();

    }

    public List<TcFolder> findFirstLevelFolders(boolean withRemoved) {
        String sql = "SELECT f FROM TcFolder f WHERE f.folder is null ";
        if (!withRemoved) {
            sql += "AND f.isremoved = :isremoved";
        }
        Query query = em.createQuery(sql);
        if (!withRemoved) {
            query.setParameter("isremoved", Boolean.FALSE);
        }
        return query.getResultList();
    }

    public TcFolder findFolder(String folderPath) throws TcException {
        TcFolder folder = null;
        try {
            if (folderPath != null) {
                String[] paths = folderPath.split("/");
                String sql = "SELECT f FROM TcFolder f WHERE f.folder is null AND f.name=:folderName ORDER BY f.name";
                Query query = em.createQuery(sql);
                query.setParameter("folderName", paths[0]);
                folder = (TcFolder) query.getSingleResult();
                for (int k = 1; k < paths.length; k++) {
                    List<TcFolder> subFolders = findSubFolders(folder);
                    boolean notFound = true;
                    for (TcFolder subFolder : subFolders) {
                        if (subFolder.getName().equals(paths[k])) {
                            folder = subFolder;
                            notFound = false;
                            break;
                        }
                    }
                    if (notFound) {
                        throw new TcException("The folder " + folderPath + " is not found!");
                    }
                }
            }
        } catch (NoResultException nre) {
            folder = null;
        }

        return folder;
    }

    public TcFolder findFolder(Long id) {
        return folderFacade.find(id);
    }

    public List<TcFolder> findSubFolders(TcFolder folder) {
        return findSubFolders(folder, true);
    }

    public List<TcFolder> findSubFolders(TcFolder folder, boolean withRemoved) {
        String sql = "SELECT o FROM TcFolder as o WHERE o.folder = :folder ";
        if (!withRemoved) {
            sql += "AND o.isremoved = :isremoved";
        }
        Query query = em.createQuery(sql);
        query.setParameter("folder", folder);
        if (!withRemoved) {
            query.setParameter("isremoved", Boolean.FALSE);
        }
        return query.getResultList();
    }

    /**
     * A function to get all sub folders by hierarchy.
     *
     * @param folder parent folder
     * @param includeSelf include first parameter
     * @return all sub folders
     */
    public List<TcFolder> findAllSubFolders(TcFolder folder, boolean includeSelf) {
        List<TcFolder> list = new ArrayList();
        if (includeSelf) {
            list.add(folder);
        }

        list.addAll(findAllSubFolder(folder));
        logger.debug("list.size()={}", list.size());
        return list;
    }

    private List<TcFolder> findAllSubFolder(TcFolder folder) {
        List<TcFolder> list = new ArrayList();
        if (folder != null) {
            for (TcFolder subFolder : findSubFolder(folder)) {
                list.add(subFolder);
                list.addAll(findAllSubFolder(subFolder));
            }
        }
        return list;
    }

    public List<TcFolder> findSubFolder(TcFolder folder) {
        List<TcFolder> folderList = null;
        if (folder != null) {
            Query q = em.createQuery("SELECT f FROM TcFolder f where f.folder = :folder");
            q.setParameter("folder", folder);
            folderList = (List<TcFolder>) q.getResultList();
        }
        return folderList;
    }

    private List<TcDocument> findDocumentList(TcFolder folder) {
        return findDocumentList(folder, true);
    }

    private List<TcDocument> findDocumentList(TcFolder folder, boolean withRemoved) {
        String sql = "SELECT o FROM TcDocument as o WHERE o.folder = :folder ";
        if (!withRemoved) {
            sql += "AND o.isremoved = :isremoved";
        }
        Query query = em.createQuery(sql);
        query.setParameter("folder", folder);
        if (!withRemoved) {
            query.setParameter("isremoved", Boolean.FALSE);
        }
        return query.getResultList();
    }

    public List<TcDocument> findDocuments(TcFolder folder, boolean isRecursive) {
        return findDocuments(folder, isRecursive, true);
    }

    public List<TcDocument> findDocuments(TcFolder folder, boolean isRecursive, boolean withRemoved) {
        List<TcDocument> docs = null;
        if (folder != null) {
            docs = new LinkedList<TcDocument>();
            List<TcDocument> documentList = findDocumentList(folder, withRemoved ? true : false);
            for (TcDocument doc : documentList) {
                docs.add(doc);
            }

            List<TcFolder> folders = findSubFolders(folder);
            if (folders != null && folders.size() > 0 && isRecursive) {
                for (TcFolder f : folders) {
                    List<TcDocument> tmp = findDocuments(f, true);
                    if (tmp != null) {
                        docs.addAll(tmp);
                    }
                }
            }
        }
        return docs;
    }

    public Collection<TcFolder> findRecycledFolders() {
        String sql = "SELECT f FROM Folder f WHERE f.isremoved = :isremoved";
        Query query = em.createQuery(sql);
        query.setParameter("isremoved", true);

        return query.getResultList();
    }

    public Collection<TcDocument> findRecycledDocuments() {
        String sql = "SELECT d FROM TcDocument d WHERE d.isremoved = :isremoved";
        Query query = em.createQuery(sql);
        query.setParameter("isremoved", true);
        return query.getResultList();
    }

    public Collection<Foldered> findRecycledFoldereds() {
        Collection<Foldered> c = new ArrayList<Foldered>();

        c.addAll(findRecycledFolders());
        c.addAll(findRecycledDocuments());

        return c;
    }

    /**
     * Returns null if not exist. Note: In some circumstances, this method finds
     * the source Folder itself and returns. This may cause logical problem of
     * your program. Assign "true" to the parameter 'compareId' for filtering
     * out the source Folder itself.
     *
     * @param parentFolder
     * @param folder
     * @param compareId
     * @return Folder
     */
    public TcFolder checkFolderExist(TcFolder folder, boolean compareId) {
        TcFolder returnFolder = null;

        String sql = "SELECT f FROM TcFolder f WHERE f.name= :name";
        if (folder.getFolder() == null) {
            sql += " AND f.folder is null";
        } else {
            sql += " AND f.folder = :folder";
        }

        if (compareId) {
            if (folder.getId() == null) {
                sql += " AND NOT f.id is null";
            } else {
                sql += " AND NOT f.id = :id";
            }
        }

        Query query = em.createQuery(sql);
        query.setParameter("name", folder.getName());

        if (folder.getFolder() != null) {
            query.setParameter("folder", folder.getFolder());
        }
        if (compareId) {
            if (folder.getId() != null) {
                query.setParameter("id", folder.getId());
            }
        }

        List<TcFolder> list = query.getResultList();
        if (list.size() > 0) {
            returnFolder = list.get(0);
        }

        return returnFolder;
    }

    public TcFolder createFolder(String folderName) throws TcException {

        TcFolder folder = null;
        if (folderName == null) {
            throw new FolderNameRequiredException("Folder name is null");
        }
        try {

            folder = new TcFolder();
            folder.setName(folderName);
            folder.setCreatetimestamp(Calendar.getInstance().getTime());
            TcUser user = userFacade.findUserByLoginAccount(ctx.getCallerPrincipal().getName());
            folder.setCreator(user);
            folder.setModifier(user);
            folder.setModifytimestamp(new Date());
            folder.setIsremoved(Boolean.FALSE);
            //folder.setFullpath("\\" + folderName);

            if (checkFolderExist(folder, false) != null) {
                throw new FolderExistsException("Folder name (" + folder.getName() + ") has already exsit.");
            }

            em.persist(folder);

        } catch (Exception e) {
            e.printStackTrace();
            throw new TcException(e.getMessage());
        }

        return folder;
    }

    public TcFolder createFolder(TcFolder parentFolder, String folderName) throws TcException {
        TcFolder folder = null;
        if (folderName == null) {
            throw new FolderNameRequiredException("folder name is null");
        }

        try {

            folder = new TcFolder();
            folder.setName(folderName);
            folder.setCreatetimestamp(Calendar.getInstance().getTime());
            folder.setFolder(parentFolder);
            TcUser user = userFacade.findUserByLoginAccount(ctx.getCallerPrincipal().getName());
            folder.setCreator(user);
            folder.setModifier(user);
            folder.setModifytimestamp(new Date());
            folder.setIsremoved(Boolean.FALSE);

            if (checkFolderExist(folder, false) != null) {
                throw new FolderExistsException("Folder name (" + folder.getName() + ") has already exsit.");
            }

            em.persist(folder);
            FolderedEvent folderedEvent = new FolderedEvent();
            folderedEvent.setAction(FolderedEvent.CREATE_EVENT);
            folderedEvent.setFoldered(folder);
            event.fire(folderedEvent);
        } catch (TcException e) {
            e.printStackTrace();
            throw e;
        }

        return folder;
    }

    public TcFolder renameFolder(TcFolder folder, String newFolderName) throws TcException {
        if (folder == null || newFolderName == null) {
            throw new TcException("Either folder or folder name is null");
        }

        try {
            folder.setName(newFolderName);
            TcUser user = userFacade.findUserByLoginAccount(ctx.getCallerPrincipal().getName());
            folder.setModifier(user);
            folder.setModifytimestamp(new Date());

            if (checkFolderExist(folder, true) != null) {
                throw new TcException("Folder name has already exsit.");
            }

            folder = (TcFolder) em.merge(folder);

        } catch (Exception e) {
            e.printStackTrace();
            throw new TcException(e.getMessage());
        }

        return folder;
    }

    public TcFolder renameToFolder(TcFolder folder, TcFolder targetFolder, String folderName) throws TcException {
        if (folder == null || folderName == null) {
            throw new TcException("Either folder or modifier name is null");
        }

        try {

            folder.setName(folderName);
            folder.setModifytimestamp(Calendar.getInstance().getTime());
            TcUser user = userFacade.findUserByLoginAccount(ctx.getCallerPrincipal().getName());
            folder.setModifier(user);
            if (targetFolder != null) {
                folder.setFolder(targetFolder);
            }

            folder = (TcFolder) em.merge(folder);

        } catch (Exception e) {
            e.printStackTrace();
            throw new TcException(e);
        }

        return folder;
    }

    private void removeSubFolders(TcFolder folder) throws TcException {
        if (folder == null) {
            throw new TcException("Either folder or modifier name is null");
        }

        Collection documents = findDocumentList(folder);
        java.util.Collections.sort((List<TcDocument>) documents, new java.util.Comparator() {
            public int compare(Object o1, Object o2) {
                TcDocument p1 = (TcDocument) o1;
                TcDocument p2 = (TcDocument) o2;
                if (Integer.parseInt(p1.getVersionnumber()) < Integer.parseInt(p2.getVersionnumber())) {
                    return 1;
                } else {
                    return -1;
                }

            }
        });

        if (documents != null && documents.size() > 0) {
            for (Iterator itd = documents.iterator(); itd.hasNext();) {
                TcDocument document = (TcDocument) itd.next();
                removeDocument(document, false);
            }
        }

        Collection<TcFolder> folders = findSubFolders(folder);
        if (folders != null && folders.size() > 0) {
            for (TcFolder f : folders) {
                removeSubFolders(f);
            }
        }

        folderFacade.remove(folder);
        FolderedEvent folderedEvent = new FolderedEvent();
        folderedEvent.setAction(FolderedEvent.DESTROY_EVENT);
        folderedEvent.setFoldered(folder);
        event.fire(folderedEvent);
    }

    public TcFolder removeFolder(TcFolder folder, boolean disableOnly) throws TcException {
        if (folder == null) {
            throw new FolderNullException("folder is null");
        }

        try {
            if (disableOnly) {
                folder.setIsremoved(true);
                TcUser user = userFacade.findUserByLoginAccount(ctx.getCallerPrincipal().getName());
                folder.setModifier(user);
                folder.setModifytimestamp(new Date());
                folder = (TcFolder) em.merge(folder);
            } else {
                removeSubFolders(folder);
            }
        } catch (TcException e) {
            e.printStackTrace();
            throw e;
        }

        return folder;
    }

    private TcFolder recoverFolderParent(TcFolder folder) throws TcException {
        TcFolder f = null;

        if (folder.getFolder() != null) {
            TcFolder tmp = recoverFolderParent(folder.getFolder());
            if (!folder.getFolder().equals(tmp)) {
                f = new TcFolder();
                f.setFolder(tmp);
                f.setName(tmp.getName());
            } else {
                f = folder;
            }
        } else {
            f = folder;
        }

        TcFolder compareFolder = checkFolderExist(f, false);
        if (compareFolder != null) {
            return compareFolder;
        } else {
            if (folder.getFolder() == null) {
                return createFolder(folder.getName());
            } else {
                return createFolder(f.getFolder(), folder.getName());
            }
        }

    }

    private TcFolder recoverFolderChildren(TcFolder folder) throws TcException {

        TcFolder compareFolder = checkFolderExist(folder, true);
        if (compareFolder != null) {
            for (TcFolder f : findSubFolders(folder)) {
                f.setFolder(compareFolder);
                f = (TcFolder) em.merge(f);
                recoverFolderChildren(f);
            }
            for (TcDocument d : findDocumentList(folder)) {
                d.setFolder(compareFolder);
                d = (TcDocument) em.merge(d);
            }
            em.refresh(folder);
            folderFacade.remove(folder);

            return compareFolder;
        } else {
            return folder;
        }

    }

    public TcFolder recoverFolder(TcFolder folder) throws TcException {
        if (folder == null) {
            throw new TcException("Folder is null");
        }
        try {

            if (folder.getFolder() != null) {
                folder.setFolder(recoverFolderParent(folder.getFolder()));
                em.merge(folder.getFolder());
            }

            folder = recoverFolderChildren(folder);

            folder.setIsremoved(false);
            TcUser user = userFacade.findUserByLoginAccount(ctx.getCallerPrincipal().getName());
            folder.setModifier(user);
            folder.setModifytimestamp(new Date());

            folder = (TcFolder) em.merge(folder);

            refreshFolderParent(folder);
            refreshFolderChildren(folder);

        } catch (Exception e) {
            e.printStackTrace();
            throw new TcException(e);
        }

        return folder;
    }

    private void refreshFolderParent(Foldered foldered) throws TcException {
        if (foldered.getFolder() != null) {
            refreshFolderParent(foldered.getFolder());
        }
        em.refresh(em.find(foldered.getClass(), foldered.getId()));
    }

    private void refreshFolderChildren(TcFolder folder) throws TcException {
        for (TcFolder f : findSubFolders(folder)) {
            refreshFolderChildren(f);
        }

        em.refresh(folderFacade.find(folder.getId()));
    }

    public void recoverDocument(TcDocument document) throws TcException {
        if (document == null) {
            throw new TcException("Document is null");
        }

        try {

            if (document.getFolder() != null) {
                document.setFolder(recoverFolderParent(document.getFolder()));
                folderFacade.edit(document.getFolder());
            }

            document.setIsremoved(false);
            TcUser user = userFacade.findUserByLoginAccount(ctx.getCallerPrincipal().getName());
            document.setModifier(user);
            document.setModifytimestamp(new Date());

            documentFacade.edit(document);

            refreshFolderParent(document);

        } catch (Exception e) {
            e.printStackTrace();
            throw new TcException(e);
        }

    }

    public void recoverFoldered(Foldered foldered) throws TcException {
        if (foldered instanceof TcFolder) {
            recoverFolder((TcFolder) foldered);
        } else if (foldered instanceof TcDocument) {
            recoverDocument((TcDocument) foldered);
        }
    }

    public void recycleFoldered(Foldered foldered) throws TcException {
        if (foldered == null) {
            throw new TcException("Folder is null");
        }
        TcUser user = userFacade.findUserByLoginAccount(ctx.getCallerPrincipal().getName());
        if (!folderedAclEntryFacade.hasPermission((FolderedAccessControlled) foldered, user, PermissionUtils.DELETE_PERMISSION)) {
            throw new TcException("Denied! You don't have permission to delete this object");
        }

        try {
            foldered.setIsremoved(true);
            foldered.setModifytimestamp(new Date());
            foldered.setModifier(user);
            em.merge(foldered);

        } catch (Exception e) {
            e.printStackTrace();
            throw new TcException(e);
        }

    }

    public String getFolderedPath(Foldered foldered) {
        String path = "";

        if (foldered.getFolder() != null) {
            path += getFolderedPath(foldered.getFolder());
            path += "/" + foldered.getFolder().getDisplayIdentifier();
        }

        return path;
    }

    /**
     * Create a new document. Content is reserved for document content data.
     *
     */
    public TcDocument reviseDocument(TcDocument document) throws TcException {
        if (document == null) {
            throw new TcException("Document is null");
        } else {
            if (document.getIsremoved() || document.getIslatestversion() == false) {
                throw new TcException("This document is not able to be revised.");
            }
        }

        TcDocument doc = null;

        try {

            // duplicate doucment
            doc = document.getClass().newInstance();
            doc.setFolder(document.getFolder());
            //doc.setParent(document);
            doc.setMaster((TcDocumentMaster) document.getMaster());
            doc.setDoctype(document.getDoctype());
            doc.setName(document.getName());
            doc.setDescription(document.getDescription());
            TcUser user = userFacade.findUserByLoginAccount(ctx.getCallerPrincipal().getName());
            doc.setCreator(user);
            doc.setCreatetimestamp(Calendar.getInstance().getTime());
            doc.setIsremoved(false);
            doc.setVersionnumber(String.valueOf(Integer.parseInt(document.getVersionnumber()) + 1));
            doc.setIslatestversion(true);
            documentFacade.create(doc);

            // duplicate applicationData
            List<TcApplicationdata> list = contentFacade.getApplicationdata(document);
            for (TcApplicationdata applicationData : list) {
                TcApplicationdata a = new TcApplicationdata();
                a.setContainerclassname(doc.getClass().getName());
                a.setContainerid(doc.getId());
                a.setCreatetimestamp(new Date());
                a.setCreator(user);
                a.setFvitem(applicationData.getFvitem());
                a.setContentrole(applicationData.getContentrole());
                em.persist(a);
            }
            // refresh document
            document = em.find(document.getClass(), document.getId());

            // increase origrin document version
            document.setIslatestversion(false);
            documentFacade.edit(document);

            em.refresh(folderFacade.find(doc.getFolder().getId()));
            em.refresh(documentMasterFacade.find(doc.getMaster().getId()));

        } catch (Exception e) {
            e.printStackTrace();
            throw new TcException(e);
        }

        return doc;
    }

    private Long getDocumentId() {
        try {
            Query query = em.createNativeQuery("select SEQ_DOCUMENT.nextval from dual");
            BigDecimal id = (BigDecimal) query.getSingleResult();
            Long ObjectId = id.longValue();
            return ObjectId;
        } catch (RuntimeException e) {
            System.out.println("RuntimeException: " + e.toString());
            throw e;
        }
    }

    /**
     * Persist given Document.
     *
     * @param document Document object.
     * @return Document
     * @throws fc.util.HTException
     */
    public TcDocument createDocument(TcDocument document) throws TcException {

        if (document.getFolder() == null || document.getCreator() == null) {
            throw new TcException("Operation fail, incomplete document object.");
        }

        try {

            // =========================================================
            // To persist document
            // =========================================================
            TcUser user = userFacade.findUserByLoginAccount(ctx.getCallerPrincipal().getName());
            document.setVersionnumber("1");
            document.setIslatestversion(true);
            document.setIsremoved(Boolean.FALSE);
            Long id = getDocumentId();
            //document.setDocnumber("DOC-"+id);
            Date now = new Date();
            document.setCreatetimestamp(now);
            document.setCreator(user);
            document.setModifier(user);
            document.setModifytimestamp(now);
            em.persist(document);

        } catch (Exception e) {
            e.printStackTrace();
            throw new TcException(e);
        }

        return document;
    }

    public TcDocument updateDocument(TcDocument doc) throws TcException {
        if (doc == null) {
            throw new TcException("Document is null");
        }

        try {
            TcUser user = userFacade.findUserByLoginAccount(ctx.getCallerPrincipal().getName());
            doc.setModifier(user);
            doc.setModifytimestamp(Calendar.getInstance().getTime());

            documentFacade.edit(doc);

        } catch (Exception e) {
            e.printStackTrace();
            throw new TcException(e);
        }

        return doc;
    }

    public Foldered moveFoldered(Foldered foldered, TcFolder targetFolder) throws TcException {
        if (foldered == null) {
            throw new TcException("Source foldered object is null");
        }
        if ((foldered.getFolder() == null && targetFolder == null) || (foldered.getFolder() != null && foldered.getFolder().equals(targetFolder))) {
            throw new TcException("Source and target folder are the same, operation is failed.");
        }
        TcUser user = userFacade.findUserByLoginAccount(ctx.getCallerPrincipal().getName());
        if (!folderedAclEntryFacade.hasPermission((FolderedAccessControlled) foldered, user, PermissionUtils.DELETE_PERMISSION)) {
            throw new TcException("Denied! You don't have permission. [d]");
        }
        if (targetFolder != null) {
            if (!folderedAclEntryFacade.hasPermission((FolderedAccessControlled) targetFolder, user, PermissionUtils.CREATE_PERMISSION)) {
                throw new TcException("Denied! You don't have permission. [c]");
            }
        }

        Foldered returnFoldered = null;
        if (foldered instanceof TcFolder) {
            returnFoldered = moveFolder((TcFolder) foldered, targetFolder);
        } else if (foldered instanceof TcDocument) {
            returnFoldered = moveDocument((TcDocument) foldered, targetFolder);
        }
        return returnFoldered;
    }

    public TcFolder moveFolder(TcFolder folder, TcFolder targetFolder) throws TcException {

        try {
            TcFolder oriFolder = folder.getFolder();

            folder.setFolder(targetFolder);
            folder = moveFolderChildren(folder);
            folderFacade.edit(folder);

            refreshFolderChildren(folder);

        } catch (Exception e) {
            e.printStackTrace();
            throw new TcException(e);
        }

        return folder;
    }

    private TcFolder moveFolderChildren(TcFolder folder) throws TcException {

        TcFolder compareFolder = checkFolderExist(folder, true);
        if (compareFolder != null) {
            for (TcFolder f : findSubFolders(folder)) {
                f.setFolder(compareFolder);
                folderFacade.edit(f);
                moveFolderChildren(f);
            }
            for (TcDocument d : findDocumentList(folder)) {
                d.setFolder(compareFolder);
                documentFacade.edit(d);
            }
            folderFacade.remove(folderFacade.find(folder.getId()));

            return compareFolder;
        } else {
            return folder;
        }

    }

    public TcDocument moveDocument(TcDocument document, TcFolder targetFolder) throws TcException {

        try {

            TcFolder oriFolder = document.getFolder();

            document.setFolder(targetFolder);
            documentFacade.edit(document);

        } catch (Exception e) {
            e.printStackTrace();
            throw new TcException(e);
        }

        return document;
    }

    public void removeDocument(TcDocument document, boolean disableOnly) throws TcException {
        if (document == null) {
            throw new TcException("Document is null");
        }

        try {
            TcUser user = userFacade.findUserByLoginAccount(ctx.getCallerPrincipal().getName());
            document = (TcDocument) documentFacade.find(document.getId());
            if (disableOnly) {
                document.setIsremoved(true);
                document.setModifier(user);
                document.setModifytimestamp(new Date());
                documentFacade.edit(document);
            } else {
                // remove application data
                List<TcApplicationdata> list = contentFacade.getApplicationdata(document);
                for (TcApplicationdata a : list) {
                    contentFacade.removeContentItem(a);
                }

                TcDocumentMaster master = (TcDocumentMaster) document.getMaster();
                documentFacade.remove(document);

                resetLatestVersion(master);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new TcException(e);
        }

        return;
    }

    public List<TcDocument> getVersionsOf(TcDocumentMaster master) {
        return em.createQuery("select object(o) from TcDocument as o where o.master = :master order by createtimestamp desc").
                setParameter("master", master).
                getResultList();

    }

    private void resetLatestVersion(TcDocumentMaster master) throws TcException {
        master = (TcDocumentMaster) documentMasterFacade.find(master.getId());

        List<TcDocument> docs = em.createQuery("select object(o) from TcDocument as o where o.master = :master order by o.createtimestamp desc").
                setParameter("master", master).
                getResultList();

        if (docs != null && docs.size() > 0) {
            TcDocument d = docs.get(0);
            d.setIslatestversion(Boolean.TRUE);
            documentFacade.edit(d);
        } else {
            documentMasterFacade.remove(master);
        }

    }

    /*
     public List<TcDocumentUsageLink> getDocumentUsageLinks(TcDocument document) throws TcException {

     List<TcDocumentUsageLink> links = new LinkedList<TcDocumentUsageLink>();
     try {
     links = em.createQuery("select object(o) from DocumentUsageLink o where o.document = :document").
     setParameter("document", document).
     getResultList();
     } catch (Exception ex) {
     ex.printStackTrace();
     throw new TcException(ex);
     }


     return links;
     }

     public List<TcDocumentUsageLink> getDocumentUsageLinks(TcDocumentMaster documentMaster) throws TcException {

     List<TcDocumentUsageLink> links = new LinkedList<TcDocumentUsageLink>();
     try {
     links = em.createQuery("select object(o) from TcDocumentUsageLink o where o.master = :documentmaster").
     setParameter("documentmaster", documentMaster).
     getResultList();
     } catch (Exception ex) {
     ex.printStackTrace();
     throw new TcException(ex);
     }

     return links;
     }

     public List<TcDocumentUsageLink> getDocumentUsageLinks(TcDocument document, TcDocumentMaster documentMaster) throws TcException {

     List<TcDocumentUsageLink> links = new ArrayList<TcDocumentUsageLink>();
     try {
     links = em.createQuery(
     " select object(o)" +
     " from TcDocumentUsageLink o " +
     " where o.documentmaster = :documentmaster" +
     "    and o.document = :document").
     setParameter("documentmaster", documentMaster).
     setParameter("document", document).
     getResultList();
     } catch (Exception ex) {
     throw new TcException(ex);
     }

     return links;
     }

     public TcDocumentUsageLink saveDocumentUsageLink(TcDocument parent, TcDocumentMaster child) throws TcException {
     List<TcDocumentUsageLink> links = getDocumentUsageLinks(parent, child);
     if (links.size() > 0) {
     throw new TcException("Duplicated DocumnetUsageLink.");
     }

     TcDocumentUsageLink link = new TcDocumentUsageLink();
     link.setDocument(parent);
     link.setDocumentmaster(child);

     return (TcDocumentUsageLink) em.persist(link);
     }

     public void deleteDocumentUsageLink(TcDocument parent, TcDocumentMaster child) throws TcException {
     List<TcDocumentUsageLink> links = getDocumentUsageLinks(parent, child);
     for (TcDocumentUsageLink link : links) {
     persistenceHelper.delete(link);
     }
     }
     
     */
}
