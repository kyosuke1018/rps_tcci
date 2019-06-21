/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.facade.doc;

import com.tcci.cm.entity.doc.CmDocument;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.entity.content.TcFvvault;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.content.TcFvvaultFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter
 */
@Stateless
public class CmDocumentFacade extends AbstractFacade<CmDocument> {
    @EJB private TcDomainFacade domainFacade;
    @EJB private TcFvvaultFacade tcFvvaultFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmDocumentFacade() {
        super(CmDocument.class);
    }

    /**
     * 單筆儲存
     * @param entity 
     * @param operator 
     */
    public void save(CmDocument entity, TcUser operator, boolean simulated){
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytimestamp(new Date());
                this.edit(entity, simulated);
            }else{
                entity.setCreator(operator);
                entity.setCreatetimestamp(new Date());
                this.create(entity, simulated);
            }
        }
    }    

    /**
     * find by Type and DataId
     * @param ctype
     * @param dataId
     * @return 
     */
    public List<CmDocument> findByTypeAndDataId(String ctype, long dataId) {
        logger.debug("findByTypeAndDataId ... ctype="+ctype+", dataId="+dataId);
        Map<String, Object> params = new HashMap<>();
        params.put("ctype", ctype);
        params.put("dataId", dataId);
        
        return findByNamedQuery("CmDocument.findByTypeAndDataId", params);
    }
        
    /**
     * 系統檔案路徑
     * @return 
     */
    public String getDefFilePath(){
        TcDomain tcDomain = domainFacade.getDefaultDomain();
        String fvVaultHost = GlobalConstant.getFvVaultHost(); // GlobalConstant.FVVAULT_HOST_WIN;
        TcFvvault tcFvvault = tcFvvaultFacade.getTcFvvaultByHost(tcDomain, fvVaultHost);
        
        return (tcFvvault.getLocation()!=null)?tcFvvault.getLocation():null;
    }
    
}
