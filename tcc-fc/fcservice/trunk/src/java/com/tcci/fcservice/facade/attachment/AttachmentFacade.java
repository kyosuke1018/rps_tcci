/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.facade.attachment;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.ContentRole;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.entity.content.TcFvvault;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.facade.content.FileUploadFacade;
import com.tcci.fc.facade.content.TcFvvaultFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.fcservice.facade.AbstractFacade;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jimmy.lee
 */
@Stateless
public class AttachmentFacade extends AbstractFacade<TcApplicationdata> {
    @PersistenceContext(unitName="Model")
    private EntityManager em;

    private TcDomain defaultDomain;
    
    @EJB
    TcDomainFacade domainFacade;
    
    @EJB
    TcFvvaultFacade tcFvvaultFacade;
    
    @EJB
    FileUploadFacade fileUploadFacade;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AttachmentFacade() {
        super(TcApplicationdata.class);
    }
    
    @PostConstruct
    private void init() {
        defaultDomain = domainFacade.getDefaultDomain();
    }
    
    /*
     * public API:
     *   List<AttachmentVO> loadContent(ContentHolder)
     *   InputStream getContentStream(AttachmentVO)
     *   void saveContent(ContentHolder contentHolder, List<AttachmentVO> attachmentVOList)
     */
    
    public List<AttachmentVO> loadContent(ContentHolder container) {
        List<TcApplicationdata> applicationDataList = getApplicationdata(container);
        List<AttachmentVO> result = new ArrayList<AttachmentVO>();
        if (applicationDataList != null) {
            int index = 0;
            for (TcApplicationdata a : applicationDataList) {
                AttachmentVO vo = new AttachmentVO();
                vo.setApplicationdata(a);
                TcFvitem fvItem = a.getFvitem();
                if (fvItem == null) {
                    continue;
                }
                // vo.setContent(content); // 下載時再讀取檔案
                vo.setContentType(fvItem.getContenttype());
                vo.setFileName(fvItem.getName());
                vo.setSize(fvItem.getFilesize());
                vo.setIndex(index++);
                result.add(vo);
            }
        }
        return result;
    }
    
    public InputStream getContentStream(AttachmentVO vo) throws FileNotFoundException {
        byte[] bytArray = vo.getContent();
        if (bytArray != null) {
            return new ByteArrayInputStream(bytArray);
        }
        
        TcApplicationdata appData = vo.getApplicationdata();
        if (appData != null) {
            TcFvitem tcFvitem = appData.getFvitem();
            if (tcFvitem == null) {
                return null;
            }            
            TcDomain domain = tcFvitem.getDomain();
            if (domain == null) {
                return null;
            }
            
            // TODO
            // 1. 在 EJB 中呼叫 FacesContext 取得 request 方式似乎不好(雖然可行)
            // 2. TcFvvault 是否能用 JNDI 的方式設定較有效率 ?
            // String host = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteHost();
            String host = "localhost";
            TcFvvault tcFvvault = tcFvvaultFacade.getTcFvvaultByHost(domain, host);
            String path = tcFvvault.getLocation();
            if (!path.endsWith("/") && !path.endsWith("\\")) {
                path = path + File.separator;
            }
            String fileName_server = tcFvitem.getFilename();
            return new FileInputStream(path + fileName_server);
        }
        return null;
    }
    
    public void saveContent(TcDomain domain, ContentHolder container, List<AttachmentVO> attachmentVOList) throws Exception {
        if (domain == null) {
            domain = defaultDomain;
        }
        List<TcApplicationdata> applicationdataList = getApplicationdata(container);
        HashSet<TcApplicationdata> existedSet = new HashSet<TcApplicationdata>();
        if (attachmentVOList != null && !attachmentVOList.isEmpty()) {
            for (AttachmentVO attachment : attachmentVOList) {
                if (attachment.getApplicationdata() == null) {
                    // TODO: ContentRole 目前並未用到
                    // ContentFacade saveContent ContentRole logic 有問題
                    InputStream is = new ByteArrayInputStream(attachment.getContent());
                    fileUploadFacade.uploadContent(domain, container, is, attachment.getFileName(), attachment.getSize(), attachment.getContentType(), ContentRole.SECONDARY);
                } else {
                    existedSet.add(attachment.getApplicationdata());
                }
            }
        }
        //remove attachment
        if (applicationdataList != null) {
            for (TcApplicationdata a : applicationdataList) {
                if (!existedSet.contains(a)) {
                    // TODO: 附件檔案並未跟著刪除
                    remove(a);
                    TcFvitem fvItem = a.getFvitem();
                    if (fvItem != null) {
                        fvItem.setTcApplicationdataCollection(null);
                        em.remove(em.merge(fvItem));
                    }
                }
            }
        }        
    }
    
    public void removeContent(ContentHolder container) {
        List<TcApplicationdata> applicationDataList = getApplicationdata(container);
        if (applicationDataList != null && !applicationDataList.isEmpty()) {
            for (TcApplicationdata a : applicationDataList) {
                // TODO: 附件檔案並未跟著刪除
                remove(a);
                TcFvitem fvItem = a.getFvitem();
                if (fvItem != null) {
                    fvItem.setTcApplicationdataCollection(null);
                    em.remove(em.merge(fvItem));
                }
            }
        }
    }
    
    // private helper
    public List<TcApplicationdata> getApplicationdata(ContentHolder container) {
        String containerclassname = container.getClass().getCanonicalName();
        Long containerid = container.getId();
        String sql = "SELECT t FROM TcApplicationdata t"
                + " WHERE (t.containerclassname=:containerclassname)"
                + " AND (t.containerid=:containerid)"
                ;
        Query q = em.createQuery(sql);
        q.setParameter("containerclassname", containerclassname);
        q.setParameter("containerid", containerid);
        return q.getResultList();        
    }
}
