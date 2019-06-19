/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcProduct;
import com.tcci.ec.enums.ProductStatusEnum;
import com.tcci.ec.model.TccProductVO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcProductFacade extends AbstractFacade<EcProduct> {
    @EJB EcPrdTypeFacade prdTypeFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcProductFacade() {
        super(EcProduct.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcProduct entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getDisabled()==null ){ entity.setDisabled(false); }
            if( entity.getStatus()==null ){ entity.setStatus(ProductStatusEnum.DRAF.getCode()); }
            if( entity.getCurrencyId()==null ){ entity.setCurrencyId(GlobalConstant.DEF_CURRENCY_ID); }
            if( entity.getPriceAmt()==null ){ entity.setPriceAmt(GlobalConstant.DEF_PRD_PRICE_AMT); }
            // 0 to NULL (UI use select input)
            entity.setTypeId((entity.getTypeId()!=null && entity.getTypeId()==0)? null:entity.getTypeId());
            entity.setVendorId((entity.getVendorId()!=null && entity.getVendorId()==0)? null:entity.getVendorId());
            entity.setBrandId((entity.getBrandId()!=null && entity.getBrandId()==0)? null:entity.getBrandId());
            entity.setWeightUnit((entity.getWeightUnit()!=null && entity.getWeightUnit()==0)? null:entity.getWeightUnit());

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
    
    //<editor-fold defaultstate="collapsed" desc="for TCC Product">
    /**
     * 已匯入過的 TCC 商品
     * @param storeId
     * @param vo
     * @return 
     */
    public boolean existsTccProduct(Long storeId, TccProductVO vo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(S.ID) \n");
        sql.append("FROM EC_PRODUCT S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");
        sql.append("AND S.TCC_PRD=1 \n");
        sql.append("AND S.STORE_ID=#STORE_ID \n");
        sql.append("AND S.CODE=#CODE \n");
        
        params.put("STORE_ID", storeId);
        params.put("CODE", vo.getCode());
            
        return this.count(sql.toString(), params)>0;
    }

    /**
     * 建立 TCC 商品
     * @param storeId
     * @param vo
     * @param operator 
     */
    public void createTccProduct(Long storeId, TccProductVO vo, EcMember operator) {
        // EcProduct
        EcProduct entity = new EcProduct();
        entity.setTccPrd(Boolean.TRUE);
        entity.setDisabled(Boolean.FALSE);
        entity.setCname(vo.getName());
        entity.setCode(vo.getCode());
        entity.setCompareAtPrice(vo.getPrice());
        entity.setCurrencyId(GlobalConstant.DEF_CURRENCY_ID);
        entity.setPrice(vo.getPrice());
        entity.setPriceAmt(BigDecimal.ONE);
        entity.setPublishTime(new Date());
        entity.setStatus(ProductStatusEnum.PUBLISH.getCode());
        entity.setStoreId(storeId);
        
        entity.setCoverPicId(vo.getCoverPicId());
        entity.setTypeId(vo.getTypeId());
        entity.setBrandId(vo.getBrandId());
        entity.setPriceUnit(vo.getPriceUnit());
        entity.setVendorId(vo.getVendorId());

        this.save(entity, operator, false);
    }
    //</editor-fold>
}
