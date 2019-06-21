/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.et.entity.KbSysProps;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author peter.pan
 */
@Stateless
public class KbSysPropsFacade extends AbstractFacade<KbSysProps> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KbSysPropsFacade() {
        super(KbSysProps.class);
    }

    /**
     * 單筆儲存
     * @param entity 
     * @param operator 
     * @param simulated 
     */
    public void save(KbSysProps entity, TcUser operator, boolean simulated){
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
    
    public KbSysProps findByCode(String code){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        List<KbSysProps> list = this.findByNamedQuery("KbSysProps.findByCode", params);
      
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
}
