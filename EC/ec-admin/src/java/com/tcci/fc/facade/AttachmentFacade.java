/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.global.GlobalConstant;
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
    
    @EJB TcDomainFacade domainFacade;
    @EJB TcFvvaultFacade tcFvvaultFacade;
    @EJB FileUploadFacade fileUploadFacade;

    private TcDomain defaultDomain;
    
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
        if (applicationDataList == null || applicationDataList.isEmpty()) {
            return null;
        }
        List<AttachmentVO> result = new ArrayList<AttachmentVO>();
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
        return result;
    }
    
    /**
     * 取得實體檔名
     * @param vo
     * @return 
     */
    public String getFullFileName(AttachmentVO vo){
        TcApplicationdata appData = vo.getApplicationdata();
        if (appData == null) {
            return null;
        }
        
        TcFvitem tcFvitem = appData.getFvitem();
        if (tcFvitem == null) {
            return null;
        }            
        TcDomain domain = tcFvitem.getDomain();
        if (domain == null) {
            return null;
        }

        String host = GlobalConstant.getFvVaultHost(); //GlobalConstant.FVVAULT_HOST;
        TcFvvault tcFvvault = tcFvvaultFacade.getTcFvvaultByHost(domain, host);
        String path = tcFvvault.getLocation();
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            path = path + File.separator;
        }
        String fileName_server = tcFvitem.getFilename();

        return path + fileName_server;
    }
    
    /**
     * 取得FVVAULT位置
     * @param vo
     * @return 
     */
    public String getPathByAttachment(AttachmentVO vo){
        TcApplicationdata appData = vo.getApplicationdata();
        if (appData == null) {
            return null;
        }
        TcFvitem tcFvitem = appData.getFvitem();
        if (tcFvitem == null) {
            return null;
        }            
        TcDomain domain = tcFvitem.getDomain();
        if (domain == null) {
            return null;
        }

        String host = GlobalConstant.getFvVaultHost(); //GlobalConstant.FVVAULT_HOST;
        TcFvvault tcFvvault = tcFvvaultFacade.getTcFvvaultByHost(domain, host);
        String path = tcFvvault.getLocation();

        return path;       
    }
    
    /**
     * Get AttachmentVO's  Content InputStream
     * @param vo
     * @return
     * @throws FileNotFoundException 
     */
    public InputStream getContentStream(AttachmentVO vo) throws FileNotFoundException {
        byte[] bytArray = vo.getContent();
        logger.debug("bytArray = "+(bytArray!=null?bytArray.length:null));
        
        if (bytArray != null) {
            return new ByteArrayInputStream(bytArray);
        }
        
        String fullname = getFullFileName(vo);

        logger.debug("fullname = "+fullname);
        return new FileInputStream(fullname);
    }
    
    /**
     * Save content holder and it's attachments
     * @param domain
     * @param container
     * @param attachmentVOList
     * @throws Exception 
     */
    public void saveContent(TcDomain domain, ContentHolder container, List<AttachmentVO> attachmentVOList) throws Exception {
        if (domain == null) {
            domain = defaultDomain;
        }
        List<TcApplicationdata> applicationdataList = getApplicationdata(container);
        HashSet<TcApplicationdata> existedSet = new HashSet<TcApplicationdata>();
        if (attachmentVOList != null && !attachmentVOList.isEmpty()) {
            for (AttachmentVO attachment : attachmentVOList) {
                if (attachment.getApplicationdata() == null) {
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
                    // 附件檔案並未跟著刪除
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
    
    /**
     * remove content holder (附件檔案並未跟著刪除)
     * @param container 
     */
    public void removeContent(ContentHolder container) {
        List<TcApplicationdata> applicationDataList = getApplicationdata(container);
        if (applicationDataList != null && !applicationDataList.isEmpty()) {
            for (TcApplicationdata a : applicationDataList) {
                // 附件檔案並未跟著刪除
                remove(a);
                TcFvitem fvItem = a.getFvitem();
                if (fvItem != null) {
                    fvItem.setTcApplicationdataCollection(null);
                    em.remove(em.merge(fvItem));
                }
            }
        }
    }
    
    /**
     * get TcApplicationdata by ContentHolder
     * @param container
     * @return 
     */
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
