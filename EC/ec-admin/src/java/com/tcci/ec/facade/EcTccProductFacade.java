/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcTccProduct;
import com.tcci.ec.enums.BrandEnum;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.ImageSizeEnum;
import com.tcci.ec.enums.OptionEnum;
import com.tcci.ec.enums.ProductStatusEnum;
import com.tcci.ec.enums.ProductUnitEnum;
import com.tcci.ec.enums.VendorEnum;
import com.tcci.ec.model.OptionVO;
import com.tcci.ec.model.PrdTypeVO;
import com.tcci.ec.model.TccProductVO;
import com.tcci.ec.model.VendorVO;
import com.tcci.ec.model.criteria.ProductCriteriaVO;
import com.tcci.ec.model.e10.ProductE10VO;
import com.tcci.fc.util.StringUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcTccProductFacade extends AbstractFacade<EcTccProduct> {
    @EJB EcProductFacade productFacade;
    @EJB EcPrdTypeFacade prdTypeFacade;
    @EJB EcOptionFacade optionFacade;
    @EJB EcVendorFacade vendorFacade;

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcTccProductFacade() {
        super(EcTccProduct.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcTccProduct entity, EcMember operator, boolean simulated){
        if( entity!=null ){
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

    public List<EcTccProduct> findByCodeOnly(String code){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        List<EcTccProduct> list = this.findByNamedQuery("EcTccProduct.findByCodeOnly", params);
        return list;
    }
        
    public EcTccProduct findByCode(String code, Boolean active){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        params.put("active", active);
        List<EcTccProduct> list = this.findByNamedQuery("EcTccProduct.findByCode", params);
        
        return (list!=null && !list.isEmpty())?list.get(0):null;
    }
    
    public EcTccProduct findByName(String name, Boolean active){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("active", active);
        List<EcTccProduct> list = this.findByNamedQuery("EcTccProduct.findByName", params);
        
        return (list!=null && !list.isEmpty())?list.get(0):null;
    }
    
    public List<TccProductVO> findTccByCriteria(ProductCriteriaVO criteriaVO, Locale locale) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT S.*, S.NAME AS CNAME \n");
        // 封面圖
        sql.append(", FI.ID coverPicId, FI.NAME picName, FI.DESCRIPTION picDescription, FI.SAVENAME, FI.CONTENT_TYPE, FI.FILESIZE \n");

        if( criteriaVO.getStoreId()!=null ){
            sql.append(", P.STORE_ID, P.ID AS PRODUCT_ID, P.PRICE \n");
        }
        sql.append(findTccByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.NAME");
        }
        
        List<TccProductVO> list = this.selectBySql(TccProductVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());

        if( list!=null ){
            for(TccProductVO vo : list){
                vo.setUrl(vo.genUrl("", GlobalConstant.URL_GET_IMAGE, ImageSizeEnum.SMALL.getCode()));
            }
        }
        return list;
    }

    public int countTccByCriteria(ProductCriteriaVO criteriaVO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findTccByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }

    public String findTccByCriteriaSQL(ProductCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        
        sql.append("FROM EC_TCC_PRODUCT S \n");
        // 封面圖
        sql.append("LEFT OUTER JOIN EC_FILE FI ON FI.PRIMARY_TYPE=#PRIMARY_TYPE AND FI.PRIMARY_ID=S.ID \n");
        params.put("PRIMARY_TYPE", FileEnum.TCC_PRD_PIC.getPrimaryType());

        if( criteriaVO.getStoreId()!=null ){
            sql.append("LEFT OUTER JOIN EC_PRODUCT P ON P.DISABLED=0 AND P.TCC_PRD=1 ");
            sql.append("AND P.STORE_ID=#STORE_ID AND P.CODE=S.CODE \n");
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.ACTIVE=1 \n");
        
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        if( !StringUtils.isBlank(criteriaVO.getKeyword()) ){
            String kw = "%" + criteriaVO.getKeyword().trim() + "%";
            sql.append("AND (S.NAME LIKE #KW OR S.CODE LIKE #KW) \n");
            params.put("KW", kw);
        }
        if( criteriaVO.getNoPicOnly()!=null && criteriaVO.getNoPicOnly() ){
            sql.append("AND FI.ID IS NULL \n");
        }
        
        return sql.toString();
    }

    /**
     * 匯入所有台泥有效商品
     * @param storeId
     * @param locale
     * @param operator
     * @return 
     */
    public int addAllTccProducts(Long storeId, EcMember operator, Locale locale){
        ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
        criteriaVO.setStoreId(storeId);
        List<TccProductVO> list = findTccByCriteria(criteriaVO, locale);
        
        int count = 0;

        if( list!=null ){
            // 匯入台泥經銷商時，為各經銷商的商店預設建立的台泥專用屬性
            PrdTypeVO typeVO = prdTypeFacade.findTccPrdType();
            OptionVO unit = optionFacade.findByCode(storeId, OptionEnum.PRD_UNIT, ProductUnitEnum.METRIC_TON.getCode());
            OptionVO brand = optionFacade.findByCode(storeId, OptionEnum.PRD_BRAND, BrandEnum.TCC.getCode());
            VendorVO vendor = vendorFacade.findByCode(storeId, VendorEnum.TCC.getCode(), false);
            
            for(TccProductVO vo : list){
                // 商品代碼不存在則加入
                if( vo!=null && vo.getCode()!=null && vo.getProductId()==null ){
                    vo.setTypeId(typeVO.getId());
                    vo.setPriceUnit(unit!=null?unit.getId():null);
                    vo.setBrandId(brand!=null?brand.getId():null);
                    vo.setVendorId(vendor!=null?vendor.getVendorId():null);
                    productFacade.createTccProduct(storeId, vo, operator);
                    count++;
                }
            }
        }
        return count;
    }
    
    // 商品 for 匯入 EC1.5
    public List<ProductE10VO> findProductForImport(){
        logger.debug("findProductForImport ...");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT DISTINCT S.CODE, S.NAME, CASE WHEN P15.CODE IS NULL THEN 0 ELSE 1 END EXISTED \n");
        sql.append("FROM TCCSTORE_USER.EC_PRODUCT S \n");
        sql.append("JOIN TCCSTORE_USER.EC_PLANT_PRODUCT PP ON PP.PRODUCT_ID=S.ID AND PP.ACTIVE=1 \n");
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("     SELECT DISTINCT CODE FROM EC_TCC_PRODUCT WHERE ACTIVE=1 \n");
        sql.append(") P15 ON P15.CODE=S.CODE \n");
        sql.append("WHERE S.ACTIVE=1 \n");
        sql.append("ORDER BY S.NAME \n");
        
        List<ProductE10VO> list = this.selectBySql(ProductE10VO.class, sql.toString(), params);
        return list;
    }
    
    /**
     * disable TCCSTORE_USER.EC_PRODUCT 時， 同時 disable EC_PRODUCT
     * @param code
     * @return 
     */
    public int disabledByTccProduct(String code){
        logger.info("disabledByTccProduct ...");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");
        sql.append("   UPDATE EC_PRODUCT SET STATUS=#STATUS  \n");
        sql.append("   WHERE DISABLED=0 \n");
        if( !sys.isBlank(code) ){
            sql.append("   AND CODE=#CODE; \n");
            params.put("CODE", code);
        }else{
            sql.append("   AND EXISTS (SELECT * FROM EC_TCC_PRODUCT WHERE ACTIVE=0 AND EC_TCC_PRODUCT.CODE=EC_PRODUCT.CODE); \n");
        }
        sql.append("END; \n");
        
        params.put("STATUS", ProductStatusEnum.REMOVE.getCode());
        
        logger.debug("disabledByTccProduct sql =\n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        setParamsToQuery("disabledByTccProduct", params, q);
        
        return q.executeUpdate();
    }
    
}

