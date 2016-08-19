/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.repository;

import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.entity.repository.TcDocument;
import com.tcci.fc.entity.repository.TcDocumentMaster;
import com.tcci.fc.entity.repository.TcFolder;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.facade.content.FileUploadFacade;
import com.tcci.fc.vo.AttachmentVO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless
@Named
public class TcDocumentFacade extends AbstractFacade<TcDocument> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @EJB
    TcDocumentMasterFacade documentMasterFacade;
    @EJB
    FileUploadFacade fileUploadFacade;
    @EJB
    ContentFacade contentFacade;

    protected EntityManager getEntityManager() {
        return em;
    }

    public TcDocumentFacade() {
        super(TcDocument.class);
    }

    private TcDocument editDocument(TcDocumentMaster documentMaster, TcDocument document) {
        if (documentMaster.getId() == null) {
            documentMasterFacade.create(documentMaster);
        } else {
            documentMasterFacade.edit(documentMaster);
        }
        document.setMaster(documentMaster);
        if (document.getId() == null) {
            create(document);
        } else {
            edit(document);
        }
        return document;
    }
    
    public void createDocument(TcDocumentMaster documentMaster,TcDocument document, List<AttachmentVO> attachmentVOList) throws Exception {
            createDocument(null,documentMaster,document,attachmentVOList);
    }

    public void createDocument(TcDomain domain, TcDocumentMaster documentMaster, TcDocument document, List<AttachmentVO> attachmentVOList) throws Exception {
        document = editDocument(documentMaster, document);
        if (null != domain) {
            contentFacade.saveContent(domain, document, attachmentVOList);
        } else {
            contentFacade.saveContent(document, attachmentVOList);
        }
    }

    public void editDocument(TcDocumentMaster documentMaster, TcDocument document, List<AttachmentVO> attachmentVOList) throws Exception {
        document = editDocument(documentMaster, document);
        contentFacade.saveContent(document, attachmentVOList);
    }

    public List<TcDocument> findByFolder(TcFolder folder) {
        Query query = getEntityManager().createNamedQuery("TcDocument.findByFolder");
        query.setParameter("folder", folder);
        return query.getResultList();
    }

    public boolean isDocumentExist(TcFolder folder, String number, String name, List<Long> skipIds) {
        boolean isDocumentExists = isNameDuplicate(folder, name, skipIds);
        if (!isDocumentExists) {
            isDocumentExists = isNumberDuplicate(number, skipIds);
        }
        return isDocumentExists;
    }

    private boolean isNameDuplicate(TcFolder folder, String name, List<Long> skipIds) {
        StringBuilder sb = new StringBuilder("SELECT d FROM TcDocument d"
                + " WHERE d.folder=:folder"
                + " AND d.name=:name"
                + " AND d.isremoved=0");
        if (skipIds != null && !skipIds.isEmpty()) {
            sb.append(" AND d.id NOT IN :skipIds");
        }
        Query q = em.createQuery(sb.toString());
        q.setParameter("folder", folder);
        q.setParameter("name", name);
        if (skipIds != null && !skipIds.isEmpty()) {
            q.setParameter("skipIds", skipIds);
        }
        List<TcDocument> list = q.getResultList();
        return !list.isEmpty();
    }

    private boolean isNumberDuplicate(String number, List<Long> skipIds) {
        StringBuilder sb = new StringBuilder("SELECT m FROM TcDocumentMaster m"
                + " WHERE m.number=:number");
        if (skipIds != null && !skipIds.isEmpty()) {
            sb.append(" AND m.id NOT IN :skipIds");
        }
        Query q = em.createQuery(sb.toString());
        q.setParameter("number", number);
        if (skipIds != null && !skipIds.isEmpty()) {
            q.setParameter("skipIds", skipIds);
        }
        List<TcDocumentMaster> list = q.getResultList();
        return !list.isEmpty();
    }

    public List<TcDocument> findLatestDocuments(List<TcFolder> folders, int maxCount) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(TcDocument.class);
        Root root = cq.from(TcDocument.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (folders != null && !folders.isEmpty()) {
            List<Long> ids = new ArrayList<Long>();
            for (TcFolder folder : folders) {
                ids.add(folder.getId());
            }
            Predicate p1 = root.get("folder").get("id").in(ids);
            predicateList.add(p1);
        }

        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(maxCount);
        return q.getResultList();
    }

    public List<TcDocument> findDocuments(TcFolder folder, Boolean includeRemoved) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(TcDocument.class);
        Root root = cq.from(TcDocument.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (folder != null) {
            Predicate p1 = cb.equal(root.get("folder").as(TcFolder.class), folder);
            predicateList.add(p1);
        }
        if (!includeRemoved) {
            Predicate p2 = cb.equal(root.get("isremoved").as(Boolean.class), false);
            predicateList.add(p2);
        }

        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        return getEntityManager().createQuery(cq).getResultList();
    }
}
