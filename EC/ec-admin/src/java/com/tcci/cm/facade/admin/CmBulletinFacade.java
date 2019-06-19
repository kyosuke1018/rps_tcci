/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmBulletin;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.fc.entity.org.TcUser;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class CmBulletinFacade extends AbstractFacade<CmBulletin>  {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    public CmBulletinFacade(){
        super(CmBulletin.class);
    }

    /**
     * 單筆儲存
     * @param entity 
     * @param operator 
     */
    public void save(CmBulletin entity, TcUser operator){
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytimestamp(new Date());
                this.edit(entity);
            }else{
                entity.setCreator(operator);
                entity.setCreatetimestamp(new Date());
                this.create(entity);
            }
        }
    }
    
    @Override
    public List<CmBulletin> findAll(){
        Query q = em.createNamedQuery("CmBulletin.findOrderByDataDate");
        
        return q.getResultList();
    }
}
