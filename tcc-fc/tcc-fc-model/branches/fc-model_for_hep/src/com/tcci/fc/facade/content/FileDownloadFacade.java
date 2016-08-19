/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.content;

import com.tcci.fc.facade.essential.TcDomainFacade;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.TcFvitem;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.tcci.fc.entity.content.ContentRole;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.entity.content.TcFvvault;
import com.tcci.fc.facade.AbstractFacade;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
public class FileDownloadFacade extends AbstractFacade<TcFvitem> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FileDownloadFacade() {
        super(TcFvitem.class);
    }
    @EJB
    TcApplicationdataFacade tcApplicationdataFacade;
    @EJB
    TcFvvaultFacade tcFvvaultFacade;
    @EJB
    TcDomainFacade tcDomainFacade;    
    public TcApplicationdata getPrimaryApplicationData(ContentHolder container) throws Exception {
        List<TcApplicationdata> list = tcApplicationdataFacade.getByContentHolder(container, ContentRole.PRIMARY);
        TcApplicationdata tcApplicationdata = null;
        if(list.size()>0){
            tcApplicationdata = list.get(0);
        }
        return tcApplicationdata;
    }

    public InputStream findContentStream(TcFvitem tcFvitem) throws Exception {
        TcDomain domain = tcFvitem.getDomain();
        TcFvvault tcFvvault = tcFvvaultFacade.getTcFvvaultByLocalhost(domain);
        String path = tcFvvault.getLocation();
        String fileName_server = tcFvitem.getFilename();
        String fileName_client = tcFvitem.getName();
        String contentType = tcFvitem.getContenttype();
        InputStream stream = new FileInputStream(path+fileName_server);
        return stream;
    }
    public InputStream findContentStream(ContentHolder container) throws Exception {
        TcApplicationdata tcApplicationdata = getPrimaryApplicationData(container);
        TcFvitem tcFvitem = tcApplicationdata.getFvitem();
        InputStream stream = findContentStream(tcFvitem);
        return stream;
    }
    public InputStream findContentStream(TcDomain domain, ContentHolder container) throws Exception {
        TcApplicationdata tcApplicationdata = getPrimaryApplicationData(container);
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
    public TcFvvault getTcFvvaultByLocalhost(TcDomain tcDomain){
        return tcFvvaultFacade.getTcFvvaultByLocalhost(tcDomain);
    }   
}
