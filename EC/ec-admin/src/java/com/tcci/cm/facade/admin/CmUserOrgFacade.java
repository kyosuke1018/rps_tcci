/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmUserOrg;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.fc.entity.org.TcUser;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter
 */
@Stateless
public class CmUserOrgFacade extends AbstractFacade<CmUserOrg> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmUserOrgFacade() {
        super(CmUserOrg.class);
    }
    
    /**
     * 單筆儲存
     * @param entity 
     * @param operator 
     */
    public void save(CmUserOrg entity, TcUser operator){
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
                this.edit(entity);
            }else{
                entity.setCreator(operator.getId());
                entity.setCreatetimestamp(new Date());
                this.create(entity);
            }
        }
    }
}
