/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmUserFactorygroupR;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.cm.util.NativeSQLUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
@Stateless
public class CmUserFactorygroupRFacade extends AbstractFacade<CmUserFactorygroupR> {
    private Logger logger = LoggerFactory.getLogger(CmUserFactorygroupRFacade.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmUserFactorygroupRFacade() {
        super(CmUserFactorygroupR.class);
    }

    /**
     * 移除 TcGroups 時，移除關聯廠設定
     * @param removedUG 
     */
    public void removeByTcGroups(ArrayList<TcGroup> removedUG) {
        if( removedUG==null || removedUG.isEmpty() ){
            return;
        }
     
        List<Long> ids = new ArrayList<Long>();
        for(TcGroup tcGroup : removedUG){
            if( tcGroup.getId()!=null && tcGroup.getId().longValue()>0 ){
                ids.add(tcGroup.getId());
            }
        }
        
        if( ids.isEmpty() ){
            return;
        }
        
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");
        sql.append("  DELETE FROM CM_USER_FACTORYGROUP_R \n");
        sql.append("  WHERE 1=1 \n");
        sql.append(NativeSQLUtils.getInSQL("USERGROUP_ID", ids, params)).append("; \n");
        sql.append("END;");
        
        Query q = em.createNativeQuery(sql.toString());
        
        for(String key : params.keySet()){
            q.setParameter(key, params.get(key));
        }
        
        q.executeUpdate();
    }
}
