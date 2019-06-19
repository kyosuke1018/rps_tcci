/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPrdAttr;
import com.tcci.ec.model.PrdAttrVO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcPrdAttrFacade extends AbstractFacade<EcPrdAttr> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcPrdAttrFacade() {
        super(EcPrdAttr.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcPrdAttr entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getDisabled()==null ){ entity.setDisabled(false); }
            if( entity.getSortnum()==null ){ entity.setSortnum(0); } 

            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytime(new Date());
                this.edit(entity, simulated);
                logger.info("save update "+entity);
            }else{
                entity.setCreator(operator);
                entity.setCreatetime(new Date());
                this.create(entity, simulated);
                logger.info("save new "+entity);
            }
        }
    }
    
    /**
     * 依商品類別查詢
     * @param storeId
     * @param typeId
     * @return 
     */
    public List<PrdAttrVO> findByPrdType(Long storeId, Long typeId){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM EC_PRD_ATTR S \n");
        sql.append("WHERE S.DISABLED=0 \n");
        sql.append("AND S.STORE_ID=#STORE_ID \n");
        sql.append("AND S.TYPE_ID=#TYPE_ID \n");
        sql.append("ORDER BY S.SORTNUM, S.CNAME \n");
        
        params.put("STORE_ID", storeId);
        params.put("TYPE_ID", typeId);
        
        List<PrdAttrVO> list = this.selectBySql(PrdAttrVO.class, sql.toString(), params);
        return list;
    }

    public boolean checkInput(EcPrdAttr entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }

}
