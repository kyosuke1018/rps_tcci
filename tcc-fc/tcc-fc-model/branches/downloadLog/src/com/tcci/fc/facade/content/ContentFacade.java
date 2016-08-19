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
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
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

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
@Named
public class ContentFacade extends AbstractFacade<TcApplicationdata> {

    Logger logger = Logger.getLogger(this.getClass().getName());
    private TcDomain defaultDomain;
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @EJB
    TcFvvaultFacade tcFvvaultFacade;
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

    public void savePrimaryContent(ContentHolder contentHolder, AttachmentVO attachment) throws Exception {
        savePrimaryContent(defaultDomain, contentHolder, attachment);
    }

    public void savePrimaryContent(TcDomain domain, ContentHolder contentHolder, AttachmentVO attachment) throws Exception {
        if (null == domain) {
            throw new TcException("domain cannot be null.");
        }
        List<TcApplicationdata> applicationdataList = getApplicationdata(contentHolder, ContentRole.PRIMARY);
        for (TcApplicationdata applicationdata : applicationdataList) {
            remove(applicationdata);
        }
        InputStream is = new ByteArrayInputStream(attachment.getContent());
        fileUploadFacade.uploadContent(domain, contentHolder, is, attachment.getFileName(), attachment.getSize(), attachment.getContentType(), ContentRole.PRIMARY);
    }

    public void saveSecondaryContent(ContentHolder contentHolder, List<AttachmentVO> attachmentVOList) throws Exception {
        saveSecondaryContent(defaultDomain, contentHolder, attachmentVOList);
    }

    public void saveSecondaryContent(TcDomain domain, ContentHolder contentHolder, List<AttachmentVO> attachmentVOList) throws Exception {
        if (null == domain) {
            throw new TcException("domain cannot be null.");
        }
        List<TcApplicationdata> applicationdataList = getApplicationdata(contentHolder);
        HashSet<TcApplicationdata> existedSet = new HashSet<TcApplicationdata>();
        if (attachmentVOList != null && !attachmentVOList.isEmpty()) {
            for (AttachmentVO attachment : attachmentVOList) {
                if (attachment.getApplicationdata() == null) {
                    InputStream is = new ByteArrayInputStream(attachment.getContent());
                    fileUploadFacade.uploadContent(domain, contentHolder, is, attachment.getFileName(), attachment.getSize(), attachment.getContentType(), ContentRole.SECONDARY);
                } else {
                    existedSet.add(attachment.getApplicationdata());
                }
            }
        }

        //remove attachment
        if (applicationdataList != null) {
            for (TcApplicationdata a : applicationdataList) {
                if (!existedSet.contains(a)) {
                    remove(a);
//FvItem 不刪除是為了要做 housekeeping.
//                    TcFvitem fvItem = a.getFvitem();
//                    if (fvItem != null) {
//                        fvItem.setTcApplicationdataCollection(null);
//                        em.remove(em.merge(fvItem));
//                    }
                }
            }
        }
    }

    public void saveContent(ContentHolder contentHolder, List<AttachmentVO> attachmentVOList) throws Exception {
        saveContent(defaultDomain, contentHolder, attachmentVOList);
    }

    public void saveContent(TcDomain domain, ContentHolder contentHolder, List<AttachmentVO> attachmentVOList) throws Exception {
        if (null == domain) {
            throw new TcException("domain cannot be null.");
        }
        List<TcApplicationdata> applicationdataList = getApplicationdata(contentHolder);
        HashSet<TcApplicationdata> existedSet = new HashSet<TcApplicationdata>();
        if (attachmentVOList != null && !attachmentVOList.isEmpty()) {
            int i = 0;
            for (AttachmentVO attachment : attachmentVOList) {
                if (attachment.getApplicationdata() == null) {
                    if (null == attachment.getContentRole()) {
                        if (i == 0) {
                            attachment.setContentRole(ContentRole.PRIMARY);
                        } else {
                            attachment.setContentRole(ContentRole.SECONDARY);
                        }
                    }
                    InputStream is = new ByteArrayInputStream(attachment.getContent());
                    fileUploadFacade.uploadContent(domain, contentHolder, is, attachment.getFileName(), attachment.getSize(), attachment.getContentType(), attachment.getContentRole(), attachment.getFileName());
                    i++;
                } else {
                    existedSet.add(attachment.getApplicationdata());
                }
            }
        }

        //remove attachment
        if (applicationdataList != null) {
            for (TcApplicationdata a : applicationdataList) {
                if (!existedSet.contains(a)) {
                    remove(a);
//FvItem 不刪除是為了要做 housekeeping.
//                    TcFvitem fvItem = a.getFvitem();
//                    if (fvItem != null) {
//                        fvItem.setTcApplicationdataCollection(null);
//                        em.remove(em.merge(fvItem));
//                    }
                }
            }
        }
    }

    public void removeContent(ContentHolder container) throws Exception {
        List<TcApplicationdata> applicationDataList = getApplicationdata(container);
        if (applicationDataList != null && !applicationDataList.isEmpty()) {
            for (TcApplicationdata a : applicationDataList) {
                remove(a);
//FvItem 不刪除是為了要做 housekeeping.                
//                TcFvitem fvItem = a.getFvitem();
//                if (fvItem != null) {
//                    fvItem.setTcApplicationdataCollection(null);
//                    em.remove(em.merge(fvItem));
//                }
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

    public void removeContentItem(ContentItem contentItem) throws TcException {
        if(contentItem instanceof TcApplicationdata) {
            TcApplicationdataEvent applicationdataEvent = new TcApplicationdataEvent();
            applicationdataEvent.setAction(TcApplicationdataEvent.DESTROY_EVENT);
            applicationdataEvent.setApplicationdata((TcApplicationdata)contentItem);
            event.fire(applicationdataEvent);
        }
        em.remove(em.find(contentItem.getClass(), contentItem.getId()));
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
        TcDomain domain = tcFvitem.getDomain();
//        InetAddress inetAddress = null;
//        String host = "";
//        try {
//            inetAddress = InetAddress.getLocalHost();
//            host = inetAddress.getHostName();
//        } catch (UnknownHostException uhe) {
        // TODO
        // 1. 在 EJB 中呼叫 FacesContext 取得 request 方式似乎不好(雖然可行)
        // 2. TcFvvault 是否能用 JNDI 的方式設定較有效率 ?
        // String host = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteHost();
        String host = "localhost";
//            }
        TcFvvault tcFvvault = tcFvvaultFacade.getTcFvvaultByHost(domain, host);
        String path = tcFvvault.getLocation();
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            path = path + File.separator;
        }
        String fileName_server = tcFvitem.getFilename();
        String fileName_client = tcFvitem.getName();
        String contentType = tcFvitem.getContenttype();
        //fire event.
        return new FileInputStream(path + fileName_server);

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
}