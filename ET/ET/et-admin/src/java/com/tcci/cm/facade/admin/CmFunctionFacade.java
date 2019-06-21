/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmFunction;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.fc.entity.org.TcUser;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter
 */
@Stateless
public class CmFunctionFacade extends AbstractFacade<CmFunction> {
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmFunctionFacade() {
        super(CmFunction.class);
    }
    
    /**
     * 單筆儲存
     * @param entity 
     * @param operator 
     * @param simulated 
     */
    public void save(CmFunction entity, TcUser operator, boolean simulated){
        if( entity!=null ){
            entity.setCreator(operator.getId());
            entity.setCreatetimestamp(new Date());
            
            if( entity.getId()!=null && entity.getId()>0 ){
                this.edit(entity, simulated);
            }else{
                this.create(entity, simulated);
            }
        }
    }
    
    /**
     * 依 RuleCode 取資料
     * @param ruleCode
     * @return 
     */
    public CmFunction findByRuleCode(String ruleCode){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", ruleCode);
        List<CmFunction> list = findByNamedQuery("CmFunction.findByRuleCode", params);
        if( list!=null && !list.isEmpty() ){
            return list.get(0);
        }else{
            return null;
        }
    }
}
