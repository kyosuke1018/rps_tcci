/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.product;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.ec.entity.EcFile;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPrdType;
import com.tcci.ec.entity.EcProduct;
import com.tcci.ec.enums.CompanyTypeEnum;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.ImageSizeEnum;
import com.tcci.ec.enums.OptionEnum;
import com.tcci.ec.enums.ProductStatusEnum;
import com.tcci.ec.enums.ProductVariantEnum;
import com.tcci.ec.facade.AbstractFacade;
import com.tcci.ec.facade.store.EcStoreAreaFacade;
import com.tcci.ec.facade.util.ProductFilter;
import com.tcci.ec.model.PrdAttrVO;
import com.tcci.ec.model.PrdAttrValVO;
import com.tcci.ec.model.PrdDetailVO;
import com.tcci.ec.model.PrdIntroVO;
import com.tcci.ec.model.PrdPaymentVO;
import com.tcci.ec.model.PrdShippingVO;
import com.tcci.ec.model.PrdVarOptionVO;
import com.tcci.ec.model.PrdVariantVO;
import com.tcci.ec.model.ProductVO;
import com.tcci.ec.model.StoreAreaVO;
import com.tcci.ec.model.criteria.ProductCriteriaVO;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EcProductFacade extends AbstractFacade {
    private final static Logger logger = LoggerFactory.getLogger(EcProductFacade.class);
    
    @EJB EcPrdTypeFacade prdTypeFacade;
    @EJB EcPrdAttrFacade prdAttrFacade;
    @EJB EcPrdAttrValFacade prdAttrValFacade;
    @EJB EcPrdIntroFacade prdIntroFacade;    
    @EJB EcPrdVarOptionFacade prdVarOptionFacade;
    @EJB EcPrdVariantFacade prdVariantFacade;
    @EJB EcPrdShippingFacade prdShippingFacade;
    @EJB EcPrdPaymentFacade prdPaymentFacade;
    @EJB EcPrdDetailFacade prdDetailFacade;
    @EJB EcStoreAreaFacade storeAreaFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public EcProductFacade() {
        super(EcProduct.class);
    }

    public void save(EcProduct entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    public void save(EcProduct entity, EcMember operator){
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytime(new Date());
                this.edit(entity);
                logger.info("save update "+entity);
            }else{
                entity.setCreator(operator);
                entity.setCreatetime(new Date());
                this.create(entity);
                logger.info("save new "+entity);
            }
        }
    }
    
    public void saveVO(ProductVO vo, EcMember operator){
        if( vo!=null ){
            EcProduct entity = (vo.getId()!=null && vo.getId()>0)? this.find(vo.getId()):new EcProduct();
            // 需保存的系統產生欄位
            //vo.setCreator(entity.getCreator()!=null? entity.getCreator().getId():null);
            vo.setCreatetime(entity.getCreatetime());
            // 複製 UI 輸入欄位
            ExtBeanUtils.copyProperties(entity, vo);
            // DB 儲存
            save(entity, operator);
            // 回傳 VO 欄位
            vo.setId(entity.getId());
            vo.setCreatorId(entity.getCreator()!=null? entity.getCreator().getId():null);
            vo.setCreatetime(entity.getCreatetime());
            vo.setModifierId(entity.getModifier()!=null? entity.getModifier().getId():null);
            vo.setModifytime(entity.getModifytime());
        }
    }
    
    public EcProduct find(Long id) {
        return em.find(EcProduct.class, id);
    }
    
    @Override
    public List<EcProduct> findAll() {
        return em.createNamedQuery("EcProduct.findAll").getResultList();
    }
    
    public EcProduct findByCode(String code) {
        Query q = em.createNamedQuery("EcProduct.findByCode");
        q.setParameter("code", code);
        List<EcProduct> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<EcProduct> findByStore(EcMember store) {
        Query q = em.createNamedQuery("EcProduct.findByStore");
        q.setParameter("store", store);
        return q.getResultList();
    }
    
    public List<EcProduct> findByCriteria(ProductFilter filter) {
        List<EcProduct> result = null;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<EcProduct> cq = cb.createQuery(EcProduct.class);
        Root<EcProduct> root = cq.from(EcProduct.class);
        cq.select(root);

        List<Predicate> predicateList = new ArrayList();
        if (filter.getId() != null) {
            predicateList.add(cb.equal(root.get("id").as(Long.class), filter.getId()));
        }
        logger.debug("StoreId:"+filter.getStoreId());
        if (filter.getStoreId() != null) {
            predicateList.add(cb.equal(root.get("store").get("id").as(Long.class), filter.getStoreId()));
        }
        if( CollectionUtils.isNotEmpty(filter.getStoreIdList()) ){
            predicateList.add(root.get("store").get("id").as(Long.class).in(filter.getStoreIdList()));
        }
        if (StringUtils.isNotEmpty(filter.getCode())) {
            predicateList.add(cb.equal(root.get("code").as(String.class), filter.getCode()));
        }
        logger.debug("Keyword:"+filter.getKeyword());
        if (StringUtils.isNotEmpty(filter.getKeyword())) {//關鍵字
            Predicate[] ps = new Predicate[4];
            ps[0] = cb.like(root.get("cname").as(String.class), "%".concat(filter.getKeyword()).concat("%"));
            ps[1] = cb.like(root.get("ename").as(String.class), "%".concat(filter.getKeyword()).concat("%"));
            ps[2] = cb.like(root.get("store").get("cname").as(String.class), "%".concat(filter.getKeyword()).concat("%"));
            ps[3] = cb.like(root.get("store").get("ename").as(String.class), "%".concat(filter.getKeyword()).concat("%"));
            predicateList.add(cb.or(ps));
        }
        
        if (filter.getPriceLow() != null) {
            Predicate predicate = cb.greaterThanOrEqualTo(root.get("price").as(BigDecimal.class), filter.getPriceLow());
            predicateList.add(predicate);
        }
        if (filter.getPriceUpper() != null) {
            Predicate predicate = cb.lessThan(root.get("price").as(BigDecimal.class), filter.getPriceUpper());
            predicateList.add(predicate);
        }
//        if (filter.getTypeId() != null) {
//            predicateList.add(cb.equal(root.get("type").get("id").as(Long.class), filter.getTypeId()));
//        }
        if (filter.getStatusList() != null && !filter.getStatusList().isEmpty()) {
            predicateList.add(root.get("status").as(String.class).in(filter.getStatusList()));
        }

        predicateList.add(cb.notEqual(root.get("disabled").as(Boolean.class), Boolean.TRUE));//not disable
        //store not disable and open
        predicateList.add(cb.notEqual(root.get("store").get("disabled").as(Boolean.class), Boolean.TRUE));//not disable
        predicateList.add(cb.equal(root.get("store").get("opened").as(Boolean.class), Boolean.TRUE));//open
        
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        //order by
        //cq.orderBy(cb.desc(root.get("createtime")));
        if (StringUtils.isNotEmpty(filter.getOrder())) {
            if("new".equals(filter.getOrder())){//新上架
                cq.orderBy(cb.desc(root.get("modifytime")));
            }else if("priceAsc".equals(filter.getOrder())){//價格低
                cq.orderBy(cb.asc(root.get("price")));
            }else if("priceDesc".equals(filter.getOrder())){//價格高
                cq.orderBy(cb.desc(root.get("price")));
            }
        }else{
            cq.orderBy(cb.desc(root.get("publishTime")));
        }
        result = em.createQuery(cq).getResultList();
//        if (filter.getStartResult()>0 && filter.getMaxResult()>0) {
//            result = em.createQuery(cq).setFirstResult(filter.getStartResult()).setMaxResults(filter.getMaxResult()).getResultList();
//        }else{
//            result = em.createQuery(cq).setMaxResults(20).getResultList();
//        }
        
//        logger.debug("result:"+result.size());
        return result;
    }
    
    public List<EcFile> findImageByProduct(EcProduct product) {
        Query q = em.createNamedQuery("EcFile.findByPrimary");
        String primaryType = EcProduct.class.getName();
//        logger.debug("EcProduct:"+primaryType);
        q.setParameter("primaryType", primaryType);
        q.setParameter("primaryId", product.getId());
        return q.getResultList();
    }
    
    public EcFile findImageById(Long id) {
        Query q = em.createNamedQuery("EcFile.findById");
        q.setParameter("id", id);
        List<EcFile> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public ProductVO findById(Long storeId, Long prdId, boolean fullInfo, Locale locale){
        if( storeId==null || prdId==null ){
            logger.error("findById error storeId="+storeId+", prdId="+prdId);
            return null;
        }
        
        ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setId(prdId);
        List<ProductVO> list = findByCriteria(criteriaVO, locale);
        
        ProductVO prdVO = (list!=null && !list.isEmpty())? list.get(0):null;
        
        if( prdVO==null || !fullInfo ){
            return prdVO;
        }
        
        // 發佈狀態 for 判斷發佈時間
        prdVO.setStatusOri(prdVO.getStatus());
        
        // 多型別選項
        // 顏色
        List<PrdVarOptionVO> colors = prdVarOptionFacade.findByPrd(storeId, prdId, ProductVariantEnum.COLOR.getCode());
        prdVO.setColorsByVoList(colors);
        // 大小
        List<PrdVarOptionVO> sizes = prdVarOptionFacade.findByPrd(storeId, prdId, ProductVariantEnum.SIZE.getCode());
        prdVO.setSizesByVoList(sizes);
        // 多型別設定
        List<PrdVariantVO> variants = prdVariantFacade.findByPrd(storeId, prdId);
        prdVO.setVariants(variants);
        // 簡介
        List<PrdIntroVO> introductions = prdIntroFacade.findByPrd(storeId, prdId);
        prdVO.setIntroductionsByVO(introductions);

        // 運送方式
        List<PrdShippingVO> shippings = prdShippingFacade.findByPrd(storeId, prdId);
        prdVO.setShippingsByVoList(shippings);
        // 付款方式
        List<PrdPaymentVO> payments = prdPaymentFacade.findByPrd(storeId, prdId);
        prdVO.setPaymentsByVoList(payments);
        
        // 商品詳細介紹
        List<PrdDetailVO> details = prdDetailFacade.findByPrd(storeId, prdId);
        prdVO.setDetails(details);
  
        // 商品類別屬性選項
        List<PrdAttrVO> typeAttrs = prdAttrFacade.findByPrdType(storeId, prdVO.getTypeId());
        prdVO.setTypeAttrs(typeAttrs);
        // 商品屬性設定
        List<PrdAttrValVO> attrVals = prdAttrValFacade.findByPrd(storeId, prdVO.getId());
        prdVO.setAttrs(attrVals);
        //商店銷售地區
        List<StoreAreaVO> areas = storeAreaFacade.findByStore(storeId);
        prdVO.setAreas(areas);
               
        return prdVO;
    }
    
    /**
     * 依條件查詢商品
     * @param criteriaVO
     * @return 
     */
    public List<ProductVO> findByCriteria(ProductCriteriaVO criteriaVO, Locale locale){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT S.* \n");
        sql.append(", ST.CNAME STORE_NAME \n"); 
        sql.append(", CUR.NAME CUR_NAME, CUR.CODE CURRENCY \n");// 幣別
        sql.append(", T.TYPENAME, T.TYPEL2, T.TYPEL2NAME, T.TYPEL1, T.TYPEL1NAME \n");// 類別
        sql.append(", Y.CNAME VENDORNAME \n");// 供應商
        sql.append(", NVL(FP.CC, 0) FAV_COUNT \n");// 喜愛商品
        sql.append(", AP.LOGIN_ACCOUNT APPLICANT_ACCOUNT \n");// 申請人
        // 封面圖
        sql.append(", FI.NAME picName, FI.DESCRIPTION picDescription, FI.SAVENAME, FI.CONTENT_TYPE, FI.FILESIZE \n");
        // 庫存
        sql.append(", NVL(S.STOCK_SETTLE, 0)+ST.STOCK STOCK \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.CNAME");
        }
        
        List<ProductVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(ProductVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(ProductVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(ProductVO.class, sql.toString(), params);
        }
        
        if( list!=null ){
            for(ProductVO vo : list){
                vo.genStatusName(locale);
                vo.setUrl(vo.genUrl("", GlobalConstant.URL_GET_IMAGE, ImageSizeEnum.SMALL.getCode()));
            }
        }
        return list;
    }
    
    public String findByCriteriaSQL(ProductCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        
        sql.append("FROM EC_PRODUCT S \n");
        sql.append("LEFT OUTER JOIN EC_STORE ST ON ST.ID=S.STORE_ID \n");
        // 幣別
        sql.append("LEFT OUTER JOIN EC_CURRENCY CUR ON CUR.ID=S.CURRENCY_ID \n");
        // 供應商
        sql.append("LEFT OUTER JOIN EC_VENDOR V ON V.ID=S.VENDOR_ID \n"); 
        sql.append("LEFT OUTER JOIN EC_COMPANY Y ON Y.MAIN_ID=V.ID AND Y.TYPE='").append(CompanyTypeEnum.VENDOR.getCode()).append("' \n");
        // 品牌
        sql.append("LEFT OUTER JOIN EC_OPTION B ON B.ID=S.BRAND_ID AND B.TYPE='").append(OptionEnum.PRD_BRAND.getCode()).append("' \n");
        // 計量單位
        sql.append("LEFT OUTER JOIN EC_OPTION U ON U.ID=S.PRICE_UNIT AND U.TYPE='").append(OptionEnum.PRD_UNIT.getCode()).append("' \n");
        // 重量單位
        sql.append("LEFT OUTER JOIN EC_OPTION W ON W.ID=S.WEIGHT_UNIT AND W.TYPE='").append(OptionEnum.WEIGHT_UNIT.getCode()).append("' \n");
        // 申請人
        sql.append("LEFT OUTER JOIN EC_MEMBER AP ON AP.ID=S.APPLICANT \n");
        // 類別
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("     SELECT L3.ID, L3.CNAME TYPENAME \n");
        sql.append("     , L2.ID TYPEL2, L2.CNAME TYPEL2NAME \n");
        sql.append("     , L1.ID TYPEL1, L1.CNAME TYPEL1NAME \n");
        sql.append("     FROM EC_PRD_TYPE L3 \n");
        sql.append("     JOIN EC_PRD_TYPE L2 ON L2.ID=L3.PARENT \n");
        sql.append("     JOIN EC_PRD_TYPE L1 ON L1.ID=L2.PARENT \n");
        sql.append(") T ON T.ID=S.TYPE_ID \n");
        // 喜愛商品
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("     SELECT F.PRODUCT_ID, COUNT(DISTINCT F.MEMBER_ID) CC \n");
        sql.append("     FROM EC_FAVORITE_PRD F \n");
        sql.append("     WHERE 1=1 \n");
        sql.append("     GROUP BY F.PRODUCT_ID \n");
        sql.append(") FP ON FP.PRODUCT_ID=S.ID \n");
        // 封面圖
        sql.append("LEFT OUTER JOIN EC_FILE FI ON FI.ID=S.COVER_PIC_ID \n");
        // 庫存
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("     SELECT L.STORE_ID, L.PRD_ID, SUM(L.QUANTITY) STOCK \n");
        sql.append("     FROM EC_STOCK_LOG L \n");
        sql.append("     WHERE L.CLOSED=0 \n");// 未結帳項目
        sql.append("     AND L.DISABLED=0 \n");// 有效項目
        sql.append("     GROUP BY L.STORE_ID, L.PRD_ID \n");
        sql.append(") ST ON ST.STORE_ID=S.STORE_ID AND ST.PRD_ID=S.ID \n");
        
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");

        if( criteriaVO.getStoreId()!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        if( criteriaVO.getTypeId()!=null ){
            sql.append("AND S.TYPE_ID=#TYPE_ID \n");
            params.put("TYPE_ID", criteriaVO.getTypeId());
        }else if( criteriaVO.getTypeL2()!=null ){
            sql.append("AND T.TYPEL2=#TYPEL2 \n");
            params.put("TYPEL2", criteriaVO.getTypeL2());
        }else if( criteriaVO.getTypeL1()!=null ){
            sql.append("AND T.TYPEL1=#TYPEL1 \n");
            params.put("TYPEL1", criteriaVO.getTypeL1());
        }
        if( !StringUtils.isBlank(criteriaVO.getStatus()) ){
            sql.append("AND S.STATUS=#STATUS \n");
            params.put("STATUS", criteriaVO.getStatus());
        }
        if( !CollectionUtils.isEmpty(criteriaVO.getStatusList()) ){
            sql.append(NativeSQLUtils.getInSQL("S.STATUS", criteriaVO.getStatusList(), params)).append("\n");
        }
        if( !StringUtils.isBlank(criteriaVO.getKeyword()) ){
            String kw = "%" + criteriaVO.getKeyword().trim() + "%";
            sql.append("AND (S.CNAME LIKE #KW OR S.CODE LIKE #KW) \n");
            params.put("KW", kw);
        }
        
        return sql.toString();
    }
    
    public boolean checkInput(ProductVO vo, EcMember member, Locale locale, List<String> errors) {
        vo.setDisabled(vo.getDisabled()==null?false:vo.getDisabled());
        vo.setTypeId((vo.getTypeId()!=null && vo.getTypeId()==0)? null:vo.getTypeId());
        vo.setVendorId((vo.getVendorId()!=null && vo.getVendorId()==0)? null:vo.getVendorId());
        vo.setBrandId((vo.getBrandId()!=null && vo.getBrandId()==0)? null:vo.getBrandId());
        vo.setWeightUnit((vo.getWeightUnit()!=null && vo.getWeightUnit()==0)? null:vo.getWeightUnit());
        
        boolean pass = true;
        pass = inputCheckFacade.checkInput(vo, locale, errors);

        return pass;
    }
    
    /**
     * 自動設定商品封面圖
     * 
     * @param storeId
     * @param prdId
     * @return 
     */
    public int autoSetCoverPicture(Long storeId, Long prdId){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");

        sql.append("MERGE INTO EC_PRODUCT S \n");
        sql.append("USING ( \n");
        sql.append("  SELECT P.ID, MAX(F.ID) FID \n");
        sql.append("  FROM EC_PRODUCT P \n");
        sql.append("  JOIN EC_FILE F ON F.PRIMARY_TYPE=#PRIMARY_TYPE AND F.PRIMARY_ID=P.ID \n");
        sql.append("  WHERE P.COVER_PIC_ID IS NULL \n");
        
        params.put("PRIMARY_TYPE", FileEnum.PRD_PIC.getPrimaryType());
        
        if( storeId!=null ){
            sql.append("  AND P.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", storeId);
        }
        if( prdId!=null ){
            sql.append("  AND P.ID=#PRD_ID \n");
            params.put("PRD_ID", prdId);
        }
        
        sql.append("  GROUP BY P.ID \n");
        sql.append(") W ON (W.ID=S.ID) \n");
        sql.append("WHEN MATCHED THEN \n");
        sql.append("  UPDATE SET S.COVER_PIC_ID=W.FID; \n");

        sql.append("END; \n");
        
        logger.debug("autoSetCoverPicture sql =\n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        setParamsToQuery("autoSetCoverPicture", params, q);
        
        return q.executeUpdate();
    }    
}
