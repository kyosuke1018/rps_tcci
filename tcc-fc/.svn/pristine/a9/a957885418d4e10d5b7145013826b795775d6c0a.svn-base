/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.content;

import com.tcci.fc.facade.essential.TcDomainFacade;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.entity.content.ContentRole;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.entity.content.TcFvvault;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.fc.util.TcFcUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
@Named
public class FileUploadFacade extends AbstractFacade<TcFvitem> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @EJB
    TcUserFacade tcUserFacade;
    @EJB
    TcDomainFacade tcDomainFacade;
    @EJB
    TcFvvaultFacade tcFvvaultFacade;
    @EJB
    ContentFacade tcApplicationdataFacade;
    @EJB
    TcFvitemFacade tcFvitemFacade;
    @Resource
    SessionContext sessionContext;
    final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FileUploadFacade() {
        super(TcFvitem.class);
    }
    //above is base   
    
    /**
     * 
     * @param domain
     * @param container
     * @param inputstream
     * @param uploadedFileName
     * @param filesize
     * @param contenttype
     * @param contentRole
     * @return
     * @throws Exception 
     */
    public TcApplicationdata uploadContent(TcDomain domain, ContentHolder container, InputStream inputstream, String uploadedFileName, long filesize, String contenttype, ContentRole contentRole) throws Exception {
        return uploadContent(domain,container,inputstream,uploadedFileName,filesize,contenttype,contentRole,"");
    }    

    /**
     *
     * @param domain
     * @param uploadedFileName
     * @param filesize
     * @param contenttype
     * @return
     * @throws Exception
     */
    public TcApplicationdata uploadContent(TcDomain domain, ContentHolder container, InputStream inputstream, String uploadedFileName, long filesize, String contenttype, ContentRole contentRole, String description) throws Exception {
//        System.out.println("uploadContent");
        if (container == null) {
            throw new NullPointerException("container cannot be null.");
        }
        TcApplicationdata applicationData = new TcApplicationdata();
        try {
            //
            TcFvitem fvItem = buildFvItem(domain, inputstream, uploadedFileName, filesize, contenttype);
            Principal principal = sessionContext.getCallerPrincipal();
            TcUser tcUser = null;
            String loginAccount = null;
            if (principal != null) {
                loginAccount = principal.getName();
            }
            if (loginAccount != null) {
                tcUser = tcUserFacade.findUserByLoginAccount(loginAccount);
            }
            fvItem.setCreator(tcUser);

            fvItem.setCreatetimestamp(new java.util.Date());
            em.persist(fvItem);

            // prepare ApplicationData
            //applicationData.setId(fvItem.getId());
            //logger.info("gilbert:" + applicationData.getId().toString());
            applicationData.setContainerclassname(container.getClass().getCanonicalName());
            applicationData.setContainerid(container.getId());
            applicationData.setContentrole(contentRole.toString().charAt(0));
            applicationData.setCreator(tcUser);
            applicationData.setCreatetimestamp(new Date());
            em.merge(fvItem);
            applicationData.setFvitem(fvItem);
            applicationData.setDescription(description);
            em.persist(applicationData);
        } catch (Exception e) {
//            e.printStackTrace();
            logger.log(Level.SEVERE, "uploadContent", e);
//            System.out.println(e);
            throw new Exception(e);
        }
        return applicationData;
    }

    public TcApplicationdata uploadContent(TcDomain domain, ContentHolder container, InputStream inputstream, String uploadedFileName, long filesize, String contenttype) throws Exception {
        return uploadContent(domain, container, inputstream, uploadedFileName, filesize, contenttype, ContentRole.SECONDARY);
    }

    private TcFvitem buildFvItem(TcDomain domain, InputStream inputstream, String uploadedFileName, long filesize, String contenttype) throws Exception {
        //get Full file name
        int index = uploadedFileName.lastIndexOf('/');
        String client_fileName;

        if (index >= 0) {
            client_fileName = uploadedFileName.substring(index + 1);
        } else {
            // Try backslash
            index = uploadedFileName.lastIndexOf('\\');
            if (index >= 0) {
                client_fileName = uploadedFileName.substring(index + 1);
            } else {
                // No forward or back slashes
                client_fileName = uploadedFileName;
            }
        }
        //get extension name
        String extension = client_fileName.substring(client_fileName.lastIndexOf(".") + 1);
        // rename the file name that store in server
        TcFvitem fvItem = new TcFvitem();
        //fvItem.setId(getFvItemId());
        String server_fileName = TcFcUtils.getNewId() + "." + extension;

        //TcFvvault tcFvvault = tcFvvaultFacade.getTcFvvaultByLocalhost(domain);
//        InetAddress inetAddress = null;
//        String host = "";
//        try {
//            inetAddress = InetAddress.getLocalHost();
//            host = inetAddress.getHostName();
//        } catch (UnknownHostException uhe) {
        String host = "localhost";
//        }
        TcFvvault tcFvvault = tcFvvaultFacade.getTcFvvaultByHost(domain, host);
        String server_folder = tcFvvault.getLocation();

        uploadAttachment(inputstream, server_folder, server_fileName);
        // save the file information
        fvItem.setName(client_fileName);
        fvItem.setFilename(server_fileName);
        fvItem.setFilesize(filesize);
        fvItem.setDomain(domain);
        fvItem.setContenttype(contenttype);
        return fvItem;
    }

    private void uploadAttachment(InputStream inputstream, String folder, String server_fileName) throws Exception {
        //execute upload
        try {
            File file = new File(folder, server_fileName);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            final int bufferSize = 2048;
            byte[] buffer = new byte[bufferSize];
            for (int s = 0; (s = inputstream.read(buffer)) != -1;) {
                out.write(buffer, 0, s);
            }
            inputstream.close();
            out.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new Exception(ioe);
        }
    }

    /**
     * @return 
     * @deprecated
     */
    public Long getFvItemId() {
        try {
            Query query = em.createNativeQuery("select SEQ_FVITEM.nextval from dual");
            BigDecimal id = (BigDecimal) query.getSingleResult();
            Long ObjectId = id.longValue();
            return ObjectId;
        } catch (RuntimeException e) {
            System.out.println("RuntimeException: " + e.toString());
            throw e;
        }
    }

    public Long getObjectId() {
        try {
            Query query = em.createNativeQuery("select SEQ_NUM.nextval from dual");
            BigDecimal id = (BigDecimal) query.getSingleResult();
            Long ObjectId = id.longValue();
//            System.out.println("id="+ObjectId);
            return ObjectId;
        } catch (RuntimeException e) {
            System.out.println("RuntimeException: " + e.toString());
            throw e;
        }
    }

    public TcDomain getDomainById(Long id) {
        return tcDomainFacade.find(id);
    }

    public void removeContent(ContentHolder container, ContentRole contentRole) throws Exception {
        List<TcApplicationdata> apppList = tcApplicationdataFacade.getApplicationdata(container, contentRole);
        for (TcApplicationdata tcApplicationdata : apppList) {
            tcApplicationdata.getContainerclassname();
            tcApplicationdata.getContainerid();
            TcFvitem fvitem = tcApplicationdata.getFvitem();
            tcApplicationdataFacade.remove(tcApplicationdata);
            tcFvitemFacade.remove(fvitem);
        }
    }
}
