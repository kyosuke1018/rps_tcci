/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.myguimini.facade;

import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.myguimini.entity.MyMobileApp;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class MyMobileAppFacade {
    
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    @EJB
    private ContentFacade contentFacade;
    
    public MyMobileApp find(Long id) {
        return em.find(MyMobileApp.class, id);
    }
    
    public MyMobileApp findByPlatform(String platform) {
        Query q = em.createNamedQuery("MyMobileApp.findByPlatform");
        q.setParameter("platform", platform);
        List<MyMobileApp> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public void save(MyMobileApp app, List<AttachmentVO> attachments) throws Exception {
        if (app.getId() == null) {
            em.persist(app);
        } else {
            em.merge(app);
        }
        contentFacade.saveContent(app, attachments);
    }
    
    public AttachmentVO getAttachmentVO(MyMobileApp app) {
        List<TcApplicationdata> list = contentFacade.getApplicationdata(app);
        if (null == list || list.isEmpty()) {
            return null;
        }
        TcFvitem fvItem = list.get(0).getFvitem();
        if (null == fvItem) {
            return null;
        }
        AttachmentVO vo = new AttachmentVO();
        vo.setApplicationdata(list.get(0));
        vo.setContentType(fvItem.getContenttype());
        vo.setFileName(fvItem.getName());
        vo.setSize(fvItem.getFilesize());
        vo.setIndex(0);
        return vo;
    }
}
