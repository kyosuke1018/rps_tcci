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
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import com.tcci.fc.util.TcException;
import com.tcci.fc.vo.AttachmentVO;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @EJB
    TcFvvaultFacade tcFvvaultFacade;
    @EJB
    TcDomainFacade tcDomainFacade;
    @EJB
    FileUploadFacade fileUploadFacade;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ContentFacade() {
        super(TcApplicationdata.class);
    }

    public void savePrimaryContent(TcDomain domain, ContentHolder contentHolder, AttachmentVO attachment) throws Exception {
        List<TcApplicationdata> applicationdataList = getApplicationdata(contentHolder, ContentRole.PRIMARY);
        for (TcApplicationdata applicationdata : applicationdataList) {
            remove(applicationdata);
        }
        InputStream is = new ByteArrayInputStream(attachment.getContent());
        fileUploadFacade.uploadContent(domain, contentHolder, is, attachment.getFileName(), attachment.getSize(), attachment.getContentType(),  ContentRole.PRIMARY);
    }
    
    public void saveSecondaryContent(TcDomain domain, ContentHolder contentHolder, List<AttachmentVO> attachmentVOList) throws Exception {

        List<TcApplicationdata> applicationdataList = null;
        applicationdataList = getApplicationdata(contentHolder, ContentRole.SECONDARY);
        //create new attachments
        Set existedSet = new HashSet();
        if (attachmentVOList != null) {
            for (AttachmentVO attachment : attachmentVOList) {
                if (attachment.getApplicationdata() == null) {
                    InputStream is = new ByteArrayInputStream(attachment.getContent());
                    fileUploadFacade.uploadContent(domain, contentHolder, is, attachment.getFileName(), attachment.getSize(), attachment.getContentType(),  ContentRole.SECONDARY);
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
                }
            }
        }
    }
    
    
    public void saveContent(TcDomain domain, ContentHolder contentHolder, List<AttachmentVO> attachmentVOList) throws Exception {

        List<TcApplicationdata> applicationdataList = null;
        applicationdataList = getApplicationdata(contentHolder);
        //create new attachments
        Set existedSet = new HashSet();
        int i = 0;
        if (attachmentVOList != null) {
            for (AttachmentVO attachment : attachmentVOList) {
                if (attachment.getApplicationdata() == null) {
                    InputStream is = new ByteArrayInputStream(attachment.getContent());
                    ContentRole fileRole = null;
                    if (i == 0) {
                        fileRole = ContentRole.PRIMARY;
                    } else {
                        fileRole = ContentRole.SECONDARY;
                    }
                    fileUploadFacade.uploadContent(domain, contentHolder, is, attachment.getFileName(), attachment.getSize(), attachment.getContentType(), fileRole);
                } else {
                    existedSet.add(attachment.getApplicationdata());
                }
                i++;
            }
        }

        //remove attachment
        if (applicationdataList != null) {
            for (TcApplicationdata a : applicationdataList) {
                if (!existedSet.contains(a)) {
                    remove(a);
                }
            }
        }
    }

   public TcApplicationdata getPrimaryApplicationdata(ContentHolder container) throws Exception {
        List<TcApplicationdata> list = getApplicationdata(container, ContentRole.PRIMARY);
        TcApplicationdata tcApplicationdata = null;
        if (list.size() > 0) {
            tcApplicationdata = list.get(0);
        }
        return tcApplicationdata;
    }
    
   public List<TcApplicationdata> getSecondaryApplicationdata(ContentHolder container) throws Exception {
        List<TcApplicationdata> list = getApplicationdata(container, ContentRole.SECONDARY);
        return list;
    }        
    
    public List<TcApplicationdata> getApplicationdata(ContentHolder container) throws Exception {
        return getApplicationdata(container, null);
    }

    public List<TcApplicationdata> getApplicationdata(ContentHolder container, ContentRole contentRole) throws Exception {
        if (container == null) {
            throw new Exception("container is null!");
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
        em.remove(em.find(contentItem.getClass(), contentItem.getId()));
    }

 

    public InputStream findContentStream(TcFvitem tcFvitem) throws Exception {
        TcDomain domain = tcFvitem.getDomain();
//        InetAddress inetAddress = null;
//        String host = "";
//        try {
//            inetAddress = InetAddress.getLocalHost();
//            host = inetAddress.getHostName();
//        } catch (UnknownHostException uhe) {
        String host = "localhost";
//        }
        TcFvvault tcFvvault = tcFvvaultFacade.getTcFvvaultByHost(domain, host);
        String path = tcFvvault.getLocation();
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        String fileName_server = tcFvitem.getFilename();
        String fileName_client = tcFvitem.getName();
        String contentType = tcFvitem.getContenttype();
        InputStream stream = new FileInputStream(path + fileName_server);
        return stream;
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
