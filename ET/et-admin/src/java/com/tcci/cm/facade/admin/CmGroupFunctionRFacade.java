/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmFunction;
import com.tcci.cm.entity.admin.CmGroupFunctionR;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.cm.model.global.GlobalConstant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Peter
 */
@Stateless
public class CmGroupFunctionRFacade extends AbstractFacade<CmGroupFunctionR> {
    @EJB
    private CmFunctionFacade cmFunctionFacade;
    
    @PersistenceContext(unitName = "Model")
    
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmGroupFunctionRFacade() {
        super(CmGroupFunctionR.class);
    }
    
    /**
     * 設定群組功能權限
     * @param tcGroup
     * @param funcList
     * @param authMap
     * @param loginUser 
     * @param simulated 
     */
    public void saveGroupFunctionR(TcGroup tcGroup, List<Long> funcList, Map<Long, String> authMap, TcUser loginUser, boolean simulated){
        if( GlobalConstant.SIMULATE_DENIED_UPDATE && simulated ){ // 模擬使用者
            throw new RuntimeException("*** Simulate user can not update data! ***");
        }
        
        StringBuilder sql = new StringBuilder();
        //sql.append("BEGIN \n");
        sql.append("delete from CM_GROUP_FUNCTION_R where GROUP_ID = #group_id");// \n");
        // sql.append("  commit; \n"); // 不自行Commit避免後續 Exception，而原資料已刪除
        //sql.append("END;");
        
        Query q = em.createNativeQuery(sql.toString());
        q.setParameter("group_id", tcGroup.getId());
        q.executeUpdate();
        
        if( funcList!=null ){
            for(Long fid : funcList){
                //PpFunction ppFunction = new PpFunction();
                //ppFunction.setId(fid);
                CmFunction ppFunction = cmFunctionFacade.find(fid);
                
                if( ppFunction!=null ){
                    CmGroupFunctionR cmGroupFunctionR = new CmGroupFunctionR();
                    cmGroupFunctionR.setGroupId(tcGroup);
                    cmGroupFunctionR.setFuncId(ppFunction);
                    cmGroupFunctionR.setCreator(loginUser.getId());
                    cmGroupFunctionR.setCreatetimestamp(new Date());
                    String auth = authMap.get(fid);
                    cmGroupFunctionR.setAuth(auth);
                    
                    em.persist(cmGroupFunctionR);
                }
            }
        }
    }
}
