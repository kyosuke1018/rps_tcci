/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.content;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.ContentItem;
import com.tcci.fc.entity.content.ContentRole;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.entity.content.TcFvvault;
import com.tcci.fc.entity.content.TcURLData;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.event.content.TcApplicationdataEvent;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import com.tcci.fc.util.TcException;
import com.tcci.fc.vo.AttachmentVO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
@Named
public class ContentFacade extends AbstractFacade<TcApplicationdata> {

    public static final String FILE = "F";
    public static final String URL = "U";
    private Logger logger = LoggerFactory.getLogger(ContentFacade.class);
    private TcDomain defaultDomain;
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @EJB
    TcFvvaultFacade tcFvvaultFacade;
    @EJB
    TcFvitemFacade tcFvitemFacade;
    @EJB
    TcDomainFacade tcDomainFacade;
    @EJB
    FileUploadFacade fileUploadFacade;
    @Inject
    Event<TcApplicationdataEvent> event;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ContentFacade() {
        super(TcApplicationdata.class);
    }

    @PostConstruct
    private void init() {
        defaultDomain = tcDomainFacade.getDefaultDomain();
    }

    public void savePrimaryURLData(ContentHolder contentHolder, TcURLData urldata) throws Exception {
        savePrimaryURLData(contentHolder, urldata, null);
    }

    public void savePrimaryURLData(ContentHolder contentHolder, TcURLData urldata, TcUser creator) throws Exception {
        List<TcURLData> primaryURLDataList = getURLData(contentHolder, ContentRole.PRIMARY);
        urldata = em.merge(urldata);
        Set<TcURLData> existsSet = new HashSet();
        existsSet.add(urldata);
        if (primaryURLDataList != null) {
            for (TcURLData existsPrimaryURLData : primaryURLDataList) {
                if (!existsSet.contains(existsPrimaryURLData)) {
                    em.remove(existsPrimaryURLData);
                }
            }
        }
    }

    public void savePrimaryContent(ContentHolder contentHolder, AttachmentVO attachment) throws Exception {
        savePrimaryContent(contentHolder, attachment, null);
    }

    public void savePrimaryContent(ContentHolder contentHolder, AttachmentVO attachment, TcUser creator) throws Exception {
        savePrimaryContent(defaultDomain, contentHolder, attachment, creator);
    }

    public void savePrimaryContent(TcDomain domain, ContentHolder contentHolder, AttachmentVO attachment, TcUser creator) throws Exception {
        if (null == domain) {
            throw new TcException("domain cannot be null.");
        }
        List<TcApplicationdata> applicationdataList = getApplicationdata(contentHolder, ContentRole.PRIMARY);
        HashSet<TcApplicationdata> existedSet = new HashSet<TcApplicationdata>();
        if (attachment != null) {
            TcApplicationdata appData = attachment.getApplicationdata();
            if (null == appData) {
                InputStream is = new ByteArrayInputStream(attachment.getContent());
                if (null == creator) {
                    fileUploadFacade.uploadContent(domain, contentHolder, is, attachment.getFileName(), attachment.getSize(), attachment.getContentType(), ContentRole.PRIMARY, attachment.getDescription());
                } else {
                    fileUploadFacade.uploadContent(domain, contentHolder, is, attachment.getFileName(), attachment.getSize(), attachment.getContentType(), ContentRole.PRIMARY, attachment.getDescription(), creator);
                }
            } else {
                appData.setDescription(attachment.getDescription());
                em.merge(appData);
                existedSet.add(attachment.getApplicationdata());
            }
        }
        if (applicationdataList != null) {
            for (TcApplicationdata applicationdata : applicationdataList) {
                if (!existedSet.contains(applicationdata)) {
                    TcApplicationdataEvent destroyEvent = new TcApplicationdataEvent();
                    destroyEvent.setAction(TcApplicationdataEvent.DESTROY_EVENT);
                    destroyEvent.setApplicationdata(applicationdata);
                    event.fire(destroyEvent);
                    remove(applicationdata);
                }
            }
        }
    }

    public void saveSecondaryURLData(ContentHolder contentHolder, List<TcURLData> URLDataList) throws Exception {
        saveSecondaryURLData(contentHolder, URLDataList, null);
    }

    public void saveSecondaryURLData(ContentHolder contentHolder, List<TcURLData> URLDataList, TcUser creator) throws Exception {
        List<TcURLData> secondaryURLDataList = getURLData(contentHolder, ContentRole.SECONDARY);
        Set<TcURLData> existsSet = new HashSet();
        if (URLDataList != null && !URLDataList.isEmpty()) {
            for (TcURLData urldata : URLDataList) {
                urldata = em.merge(urldata);
                existsSet.add(urldata);
            }
        }
        if (secondaryURLDataList != null && !secondaryURLDataList.isEmpty()) {
            for (TcURLData existsSecondaryURLData : secondaryURLDataList) {
                if (!existsSet.contains(existsSecondaryURLData)) {
                    em.remove(existsSecondaryURLData);
                }
            }
        }
    }

    public void saveSecondaryContent(ContentHolder contentHolder, List<AttachmentVO> attachmentVOList) throws Exception {
        saveSecondaryContent(contentHolder, attachmentVOList, null);
    }

    public void saveSecondaryContent(ContentHolder contentHolder, List<AttachmentVO> attachmentVOList, TcUser creator) throws Exception {
        saveSecondaryContent(defaultDomain, contentHolder, attachmentVOList, creator);
    }

    public void saveSecondaryContent(TcDomain domain, ContentHolder contentHolder, List<AttachmentVO> attachmentVOList, TcUser creator) throws Exception {
        if (null == domain) {
            throw new TcException("domain cannot be null.");
        }
        List<TcApplicationdata> applicationdataList = getApplicationdata(contentHolder, ContentRole.SECONDARY);
        HashSet<TcApplicationdata> existedSet = new HashSet<TcApplicationdata>();
        if (attachmentVOList != null && !attachmentVOList.isEmpty()) {
            for (AttachmentVO attachment : attachmentVOList) {
                TcApplicationdata appData = attachment.getApplicationdata();
                if (null == appData) {
                    InputStream is = new ByteArrayInputStream(attachment.getContent());
                    if (creator == null) {
                        fileUploadFacade.uploadContent(domain, contentHolder, is, attachment.getFileName(), attachment.getSize(), attachment.getContentType(), ContentRole.SECONDARY, attachment.getDescription());
                    } else {
                        fileUploadFacade.uploadContent(domain, contentHolder, is, attachment.getFileName(), attachment.getSize(), attachment.getContentType(), ContentRole.SECONDARY, attachment.getDescription(), creator);
                    }
                } else {
                    appData.setDescription(attachment.getDescription());
                    em.merge(appData);
                    existedSet.add(attachment.getApplicationdata());
                }
            }
        }

        //remove attachment
        if (applicationdataList != null) {
            for (TcApplicationdata a : applicationdataList) {
                if (!existedSet.contains(a)) {
                    TcApplicationdataEvent destroyEvent = new TcApplicationdataEvent();
                    destroyEvent.setAction(TcApplicationdataEvent.DESTROY_EVENT);
                    destroyEvent.setApplicationdata(a);
                    event.fire(destroyEvent);
                    remove(a);
                }
            }
        }
    }

    public void saveUrl(ContentHolder contentHolder, List<TcURLData> urlVOList) throws Exception {
        saveUrl(contentHolder, urlVOList, null);
    }

    public void saveUrl(ContentHolder contentHolder, List<TcURLData> urldataList, TcUser creator) throws Exception {
        List<TcURLData> existedList = getURLData(contentHolder);
        Set<TcURLData> existedSet = new HashSet();
        if (urldataList != null && !urldataList.isEmpty()) {
            int i = 0;
            for (TcURLData urldata : urldataList) {
                urldata.setContainerclassname(contentHolder.getClass().getCanonicalName());
                urldata.setContainerid(contentHolder.getId());
                urldata.setCreator(creator);
                urldata.setCreatetimestamp(new Date());
                if (i == 0) {
                    urldata.setContentrole(ContentRole.PRIMARY.toCharacter());
                } else {
                    urldata.setContentrole(ContentRole.SECONDARY.toCharacter());
                }
                em.merge(urldata);
                existedSet.add(urldata);
                i++;
            }
        }
        //remove not exists urldata
        if (existedList != null && !existedList.isEmpty()) {
            for (TcURLData urldata : existedList) {
                if (!existedSet.contains(urldata)) {
                    em.remove(urldata);
                }
            }
        }
    }

    public void saveContent(ContentHolder contentHolder, List<AttachmentVO> attachmentVOList) throws Exception {
        saveContent(contentHolder, attachmentVOList, null);
    }

    public void saveContent(ContentHolder contentHolder, List<AttachmentVO> attachmentVOList, TcUser creator) throws Exception {
        saveContent(defaultDomain, contentHolder, attachmentVOList, creator);
    }

    public void saveContent(TcDomain domain, ContentHolder contentHolder, List<AttachmentVO> attachmentVOList, TcUser creator) throws Exception {
        if (null == domain) {
            throw new TcException("domain cannot be null.");
        }
        List<TcApplicationdata> applicationdataList = getApplicationdata(contentHolder);
        HashSet<TcApplicationdata> existedSet = new HashSet<TcApplicationdata>();
        if (attachmentVOList != null && !attachmentVOList.isEmpty()) {
            int i = 0;
            for (AttachmentVO attachment : attachmentVOList) {
                TcApplicationdata appData = attachment.getApplicationdata();
                if (null == appData) {
                    if (null == attachment.getContentRole()) {
                        if (i == 0) {
                            attachment.setContentRole(ContentRole.PRIMARY);
                        } else {
                            attachment.setContentRole(ContentRole.SECONDARY);
                        }
                    }
                    InputStream is = new ByteArrayInputStream(attachment.getContent());
                    if (creator == null) {
                        fileUploadFacade.uploadContent(domain, contentHolder, is, attachment.getFileName(), attachment.getSize(), attachment.getContentType(), attachment.getContentRole(), attachment.getDescription());
                    } else {
                        fileUploadFacade.uploadContent(domain, contentHolder, is, attachment.getFileName(), attachment.getSize(), attachment.getContentType(), attachment.getContentRole(), attachment.getDescription(), creator);
                    }
                    i++;
                } else {
                    appData.setDescription(attachment.getDescription());
                    em.merge(appData);
                    existedSet.add(attachment.getApplicationdata());
                }
            }
        }

        //remove attachment
        if (applicationdataList != null) {
            for (TcApplicationdata a : applicationdataList) {
                if (!existedSet.contains(a)) {
                    TcApplicationdataEvent destroyEvent = new TcApplicationdataEvent();
                    destroyEvent.setAction(TcApplicationdataEvent.DESTROY_EVENT);
                    destroyEvent.setApplicationdata(a);
                    event.fire(destroyEvent);
                    a.getFvitem().getTcApplicationdataCollection().remove(a);
                    em.merge(a.getFvitem());
                    remove(a);
                }
            }
        }
    }

    public void removeContent(ContentHolder container) throws Exception {
        List<TcApplicationdata> applicationDataList = getApplicationdata(container);
        if (applicationDataList != null && !applicationDataList.isEmpty()) {
            for (TcApplicationdata a : applicationDataList) {
                TcApplicationdataEvent destroyEvent = new TcApplicationdataEvent();
                destroyEvent.setAction(TcApplicationdataEvent.DESTROY_EVENT);
                destroyEvent.setApplicationdata(a);
                event.fire(destroyEvent);
                remove(a);
            }
        }
    }

    public TcApplicationdata getPrimaryApplicationdata(ContentHolder container) {
        List<TcApplicationdata> list = getApplicationdata(container, ContentRole.PRIMARY);
        TcApplicationdata tcApplicationdata = null;
        if (list.size() > 0) {
            tcApplicationdata = list.get(0);
        }
        return tcApplicationdata;
    }

    public List<TcApplicationdata> getSecondaryApplicationdata(ContentHolder container) {
        List<TcApplicationdata> list = getApplicationdata(container, ContentRole.SECONDARY);
        return list;
    }

    public List<TcApplicationdata> getApplicationdata(ContentHolder container) {
        return getApplicationdata(container, null);
    }

    public List<TcApplicationdata> getApplicationdata(ContentHolder container, ContentRole contentRole) {
        if (container == null) {
            return null;
        }
        String containerClassname = container.getClass().getCanonicalName();
        Long containerId = container.getId();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TcApplicationdata> cq = cb.createQuery(TcApplicationdata.class);
        Root<TcApplicationdata> from = cq.from(TcApplicationdata.class);

        List<Predicate> predicateList = new ArrayList<Predicate>();

        Predicate p1, p2, p3;
        p1 = cb.equal(from.<String>get("containerclassname"), containerClassname);
        predicateList.add(p1);
        p2 = cb.equal(from.<String>get("containerid"), containerId);
        predicateList.add(p2);
        if (contentRole != null) {
            p3 = cb.equal(from.<Character>get("contentrole"), contentRole.toCharacter());
            predicateList.add(p3);
        }

        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        cq.where(predicates);
        return em.createQuery(cq).getResultList();
    }

    public List<AttachmentVO> findAttachment(ContentHolder container) {
        List<TcApplicationdata> applicationDataList = getApplicationdata(container);
        List<AttachmentVO> result = new ArrayList<AttachmentVO>();
        if (applicationDataList != null) {
            int index = 0;
            for (TcApplicationdata a : applicationDataList) {
                AttachmentVO vo = new AttachmentVO();
                vo.setApplicationdata(a);
                TcFvitem fvItem = a.getFvitem();
                vo.setContentType(fvItem.getContenttype());
                vo.setFileName(fvItem.getName());
                vo.setSize(fvItem.getFilesize());
                vo.setIndex(index++);
                vo.setDescription(a.getDescription());
                result.add(vo);
            }
        }
        return result;
    }

    public void removeContentItem(ContentItem contentItem) throws TcException {
        if (contentItem instanceof TcApplicationdata) {
            TcApplicationdataEvent applicationdataEvent = new TcApplicationdataEvent();
            applicationdataEvent.setAction(TcApplicationdataEvent.DESTROY_EVENT);
            applicationdataEvent.setApplicationdata((TcApplicationdata) contentItem);
            event.fire(applicationdataEvent);
        }
        em.remove(em.find(contentItem.getClass(), contentItem.getId()));
    }

    public InputStream findContentStream(AttachmentVO vo) throws FileNotFoundException {
        if (vo.getContent() != null) {
            return new ByteArrayInputStream(vo.getContent());
        } else {
            return findContentStream(vo.getApplicationdata());
        }
    }

    public InputStream findContentStream(TcApplicationdata applicationdata) throws FileNotFoundException {
        InputStream inputStream = null;
        try {
            inputStream = findContentStream(applicationdata.getFvitem());
        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        }
        TcApplicationdataEvent applicationdataEvent = new TcApplicationdataEvent();
        applicationdataEvent.setAction(TcApplicationdataEvent.DOWNLOAD_EVENT);
        applicationdataEvent.setApplicationdata(applicationdata);
        event.fire(applicationdataEvent);
        return inputStream;
    }

    private InputStream findContentStream(TcFvitem tcFvitem) throws FileNotFoundException {
        return new FileInputStream(getPhysicalPathname(tcFvitem));
    }

    public InputStream findContentStream(ContentHolder container) throws Exception {
        TcApplicationdata tcApplicationdata = getPrimaryApplicationdata(container);
        TcFvitem tcFvitem = tcApplicationdata.getFvitem();
        InputStream stream = findContentStream(tcFvitem);
        return stream;
    }

    public InputStream findContentStream(TcDomain domain, ContentHolder container) throws Exception {
        TcApplicationdata tcApplicationdata = getPrimaryApplicationdata(container);
        TcFvitem tcFvitem = tcApplicationdata.getFvitem();
        /*
         TcFvvault tcFvvault = tcFvvaultFacade.getTcFvvaultByLocalhost(domain);
         String path = tcFvvault.getLocation();
         String fileName_server = tcFvitem.getFilename();
         String fileName_client = tcFvitem.getName();
         String contentType = tcFvitem.getContenttype();
         InputStream stream = new FileInputStream(path+fileName_server);
         * 
         */
        InputStream stream = findContentStream(tcFvitem);
        return stream;
    }

    public TcDomain getDomainById(Long id) {
        return tcDomainFacade.find(id);
    }

    public TcFvvault getTcFvvaultByLocalhost(TcDomain tcDomain) {
        return tcFvvaultFacade.getTcFvvaultByLocalhost(tcDomain);
    }

    public String getPhysicalPathname(AttachmentVO vo) {
        return getPhysicalPathname(vo.getApplicationdata().getFvitem());
    }

    public String getPhysicalPathname(TcFvitem tcFvitem) {
        TcDomain domain = tcFvitem.getDomain();
        TcFvvault tcFvvault = tcFvvaultFacade.getTcFvvaultByHost(domain, "localhost");
        String path = tcFvvault.getLocation();
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            path = path + File.separator;
        }
        String fileName_server = tcFvitem.getFilename();
        return path + fileName_server;
    }

    /**
     * 刪除所以有關連 Fvitems 及 實體檔案
     *
     * @param TcApplicationdata
     * @param tcFvvaultList 要處理的 TcFvvault (null 值表 TcDomain 關聯的全部 TcFvvault)
     * @param backupFileflag 是否要備份檔案
     */
    public void destroyFvitems(TcApplicationdata tcApplicationdata, List<TcFvvault> tcFvvaultList, boolean backupFileFlag) {
        if (tcApplicationdata != null) {
            tcFvitemFacade.destroyTcFvitem(tcApplicationdata.getFvitem(), tcFvvaultList, backupFileFlag);
        }
    }

    public ContentItem getPrimaryContent(ContentHolder container) {
        List<ContentItem> list = getContentList(container, ContentRole.PRIMARY);
        ContentItem primary = null;
        if (list != null && list.size() > 0) {
            primary = list.get(0);
            if (list.size() > 1) {
                System.out.println("[WARNING] There are " + list.size() + " Primary contents for " + container);
            }
        }
        return primary;
    }

    public List<ContentItem> getSecondaryContent(ContentHolder container) {
        return getContentList(container, ContentRole.SECONDARY);
    }

    public List<ContentItem> getContentList(ContentHolder container) {
        return getContentList(container, null);
    }

    public List<ContentItem> getContentList(ContentHolder container, ContentRole contentRole) {
        List<ContentItem> contentList = new ArrayList<ContentItem>();
        List<TcApplicationdata> appList = getApplicationdata(container, contentRole);
        List<TcURLData> urlList = getURLData(container, contentRole);
        if (appList != null) {
            contentList.addAll(appList);
        }
        if (urlList != null) {
            contentList.addAll(urlList);
        }
        return contentList;
    }

    public List<TcURLData> getURLData(ContentHolder container) {
        return getURLData(container, null);
    }

    public TcURLData getPrimaryURLData(ContentHolder container) {
        TcURLData primary = null;

        List<TcURLData> list = getURLData(container, ContentRole.PRIMARY);
        if (list != null && list.size() > 0) {
            primary = list.get(0);
            if (list.size() > 1) {
                System.out.println("[WARNING] There are " + list.size() + " Primary contents for " + container);
            }
        }
        return primary;
    }

    public List<TcURLData> getSecondaryURLData(ContentHolder container) {
        return getURLData(container, ContentRole.SECONDARY);
    }

    public List<TcURLData> getURLData(ContentHolder container, ContentRole contentRole) {
        if (container == null) {
            return null;
        }
        String containerClassname = container.getClass().getCanonicalName();
        Long containerId = container.getId();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TcURLData> cq = cb.createQuery(TcURLData.class);
        Root<TcURLData> from = cq.from(TcURLData.class);

        List<Predicate> predicateList = new ArrayList<Predicate>();

        Predicate p1, p2, p3;
        p1 = cb.equal(from.<String>get("containerclassname"), containerClassname);
        predicateList.add(p1);
        p2 = cb.equal(from.<String>get("containerid"), containerId);
        predicateList.add(p2);
        if (contentRole != null) {
            p3 = cb.equal(from.<Character>get("contentrole"), contentRole.toCharacter());
            predicateList.add(p3);
        }

        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        cq.where(predicates);
        return em.createQuery(cq).getResultList();
    }

    public void removeUrl(TcURLData urldata) {
        em.remove(urldata);
    }

    /**
     * archive files by ContentHolder.
     * @param contentHolder
     * @param fvvaultList 要處理的 TcFvvault (null 值表 TcDomain 關聯的全部 TcFvvault)
     * @param archiveDomain 目的地 domain
     * @throws TcFvvaultNotFoundException
     */
    public void archiveContent(ContentHolder contentHolder, List<TcFvvault> fvvaultList, TcDomain archiveDomain) throws TcFvvaultNotFoundException {
        for (TcApplicationdata applicationdata : getApplicationdata(contentHolder)) {
            archiveContent(applicationdata, fvvaultList, archiveDomain);
        }
    }

    /**
     * archive file by TcApplicationdata.
     * @param applicationdata 
     * @param fvvaultList 要處理的 TcFvvault (null 值表 TcDomain 關聯的全部 TcFvvault)
     * @param archiveDomain
     * @throws TcFvvaultNotFoundException
     */
    public void archiveContent(TcApplicationdata applicationdata, List<TcFvvault> fvvaultList, TcDomain archiveDomain) throws TcFvvaultNotFoundException {
        tcFvitemFacade.moveTcFvitem(applicationdata.getFvitem(), fvvaultList, archiveDomain);
    }

}
