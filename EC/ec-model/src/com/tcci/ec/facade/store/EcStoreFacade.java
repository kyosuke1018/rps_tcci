/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.store;

import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcSeller;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.CompanyTypeEnum;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.OptionEnum;
import com.tcci.ec.facade.AbstractFacade;
import com.tcci.ec.facade.EcCompanyFacade;
import com.tcci.ec.facade.EcFileFacade;
import com.tcci.ec.facade.EcOptionFacade;
import com.tcci.ec.facade.payment.EcPaymentFacade;
import com.tcci.ec.facade.shipping.EcShippingFacade;
import com.tcci.ec.facade.util.StoreFilter;
import com.tcci.ec.model.FileVO;
import com.tcci.ec.model.LongOptionVO;
import com.tcci.ec.model.OptionVO;
import com.tcci.ec.model.PaymentVO;
import com.tcci.ec.model.ShippingVO;
import com.tcci.ec.model.StoreAreaVO;
import com.tcci.ec.model.StoreVO;
import com.tcci.ec.model.criteria.StoreCriteriaVO;
import com.tcci.fc.util.StringUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EcStoreFacade extends AbstractFacade<EcStore> {
    private final static Logger logger = LoggerFactory.getLogger(EcStoreFacade.class);
    
    @EJB EcCompanyFacade companyFacade;
    @EJB EcShippingFacade shippingFacade;
    @EJB EcPaymentFacade paymentFacade;
    @EJB EcStoreAreaFacade storeAreaFacade;
    @EJB EcOptionFacade optionFacade;
    @EJB EcFileFacade fileFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcStoreFacade() {
        super(EcStore.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void save(EcStore entity) {
        entity.setModifytime(new Date());
        if (entity.getId() == null) {
            entity.setCreatetime(new Date());
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public EcStore findBySellerId(Long sellerId){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sellerId", sellerId);
        List<EcStore> list = this.findByNamedQuery("EcStore.findBySellerId", params);
        
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     * 指定會員的商店
     * @param memberId
     * @return 
     */
    public List<LongOptionVO> findByMemberStoreOptions(Long memberId){
        StoreCriteriaVO criteriaVO = new StoreCriteriaVO();
        criteriaVO.setMemberId(memberId);
        criteriaVO.setActive(Boolean.TRUE);
        criteriaVO.setDisabled(Boolean.FALSE);
        
        List<StoreVO> list = this.findByCriteria(criteriaVO);
        List<LongOptionVO> ops = null;
        if( list!=null ){
            ops = new ArrayList<LongOptionVO>();
            for(StoreVO vo : list){
                ops.add(new LongOptionVO(vo.getStoreId(), vo.getCname()));// EC_COMPANY.CNAME
            }
        }
        return ops;
    }
    
    public List<EcStore> findBySeller(EcSeller seller) {
        Query q = em.createNamedQuery("EcStore.findBySeller");
        q.setParameter("seller", seller);
        List<EcStore> list = q.getResultList();
        return list;
    }
    
    public List<EcStore> findByMember(EcMember member) {
        Query q = em.createNamedQuery("EcStore.findByMember");
        q.setParameter("member", member);
        List<EcStore> list = q.getResultList();
        return list;
    }
    
    /**
     * 依條件查詢
     * @param criteriaVO
     * @return 
     */
    public List<StoreVO> findByCriteria(StoreCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.ID STORE_ID, S.TYPE STORE_TYPE, S.STYLE_ID, S.FLOW_ID, S.SELLER_ID \n");
        sql.append(", C.* \n"); // EC_COMPANY
        sql.append(", R.MEMBER_ID \n");
        sql.append(", M.LOGIN_ACCOUNT, M.NAME, M.EMAIL, M.PHONE, M.ACTIVE \n");
          
        sql.append(findByCriteriaSQL(criteriaVO, params));

        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.CNAME");
        }
        
        List<StoreVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(StoreVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(StoreVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(StoreVO.class, sql.toString(), params);
        }
        return list;
    }
    
    public String findByCriteriaSQL(StoreCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("FROM EC_STORE S \n");
        sql.append("JOIN EC_SELLER R ON R.ID=S.SELLER_ID \n");
        sql.append("JOIN EC_MEMBER M ON M.ID=R.MEMBER_ID AND M.DISABLED=0 AND M.ACTIVE=1 \n");
        sql.append("LEFT OUTER JOIN EC_COMPANY C ON C.TYPE=#COM_TYPE AND C.MAIN_ID=S.ID \n");
        params.put("COM_TYPE", CompanyTypeEnum.STORE.getCode());
        
        if( criteriaVO.isFullData() ){
            sql.append("LEFT OUTER JOIN ( \n");
            sql.append("    SELECT S.STORE_ID \n");
            sql.append("    , SUM(CASE WHEN S.CUSTOMER_RATE>0 THEN 1 ELSE 0 END) PRATE \n");// 正評
            sql.append("    , SUM(CASE WHEN S.CUSTOMER_RATE<0 THEN 1 ELSE 0 END) NRATE \n");// 負評
            sql.append("    FROM EC_ORDER_RATE S \n");
            sql.append("    WHERE 1=1 \n");
            sql.append("    GROUP BY S.STORE_ID \n");
            sql.append(") RT ON RT.STORE_ID=S.ID \n");

            sql.append("LEFT OUTER JOIN ( \n");
            sql.append("    SELECT STORE_ID, COUNT(DISTINCT MEMBER_ID) CC \n");
            sql.append("    FROM EC_FAVORITE_STORE \n");
            sql.append("    WHERE 1=1 \n");
            sql.append("    GROUP BY STORE_ID \n");
            sql.append(") FS ON FS.STORE_ID=S.ID \n");

            sql.append("LEFT OUTER JOIN ( \n");
            sql.append("    SELECT P.STORE_ID, COUNT(DISTINCT F.MEMBER_ID) CC \n");
            sql.append("    FROM EC_FAVORITE_PRD F \n");
            sql.append("    JOIN EC_PRODUCT P ON P.ID=F.PRODUCT_ID AND P.DISABLED=0 \n");
            sql.append("    WHERE 1=1 \n");
            sql.append("    GROUP BY P.STORE_ID \n");
            sql.append(") FP ON FP.STORE_ID=S.ID \n");
        }
        sql.append("WHERE 1=1 \n");

        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        if( criteriaVO.getSellerId()!=null ){
            sql.append("AND S.SELLER_ID=#SELLER_ID \n");
            params.put("SELLER_ID", criteriaVO.getSellerId());
        }
        if( criteriaVO.getMemberId()!=null ){
            sql.append("AND M.ID=#MEMBER_ID \n");
            params.put("MEMBER_ID", criteriaVO.getMemberId());
        }
        if( criteriaVO.getDisabled()!=null ){
            sql.append("AND S.DISABLED=#DISABLED \n");
            params.put("DISABLED", criteriaVO.getDisabled());
        }
        if( criteriaVO.getOpened()!=null ){
            sql.append("AND S.OPENED=#OPENED \n");
            params.put("OPENED", criteriaVO.getOpened());
        }
        
        if( !StringUtils.isBlank(criteriaVO.getKeyword()) ){
            String kw = "%" + criteriaVO.getKeyword().trim() + "%";
            sql.append("AND ( \n");
            sql.append("C.CNAME LIKE #KW OR C.ENAME LIKE #KW \n");
            sql.append("OR C.ID_CODE LIKE #KW OR C.NICKNAME LIKE #KW \n");
            sql.append(") \n");
            params.put("KW", kw);
        }
        if( !StringUtils.isBlank(criteriaVO.getTelKeyword()) ){
            String kw = "%" + criteriaVO.getTelKeyword().trim() + "%";
            sql.append("AND (C.TEL1 LIKE #TELKW OR C.TEL2 LIKE #TELKW OR C.TEL3 LIKE #TELKW) \n");
            params.put("TELKW", kw);
        }
        if( !StringUtils.isBlank(criteriaVO.getEmailKeyword()) ){
            String kw = "%" + criteriaVO.getEmailKeyword().trim() + "%";
            sql.append("AND (C.EMAIL1 LIKE #EMAILKW OR C.EMAIL2 LIKE #EMAILKW OR C.EMAIL3 LIKE #EMAILKW) \n");
            params.put("EMAILKW", kw);
        }
        if( !StringUtils.isBlank(criteriaVO.getAddrKeyword()) ){
            String kw = "%" + criteriaVO.getAddrKeyword().trim() + "%";
            sql.append("AND (C.ADDR1 LIKE #ADDRKW OR C.ADDR2 LIKE #ADDRKW) \n");
            params.put("ADDRKW", kw);
        }

        return sql.toString();
    }
    
    public StoreVO findById(Long id, boolean fullData) throws IllegalAccessException, InvocationTargetException {
        if( id==null ){
            logger.error("findByStore error storeId="+id);
            return null;
        }
        StoreCriteriaVO criteriaVO = new StoreCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<StoreVO> list = findByCriteria(criteriaVO);
        
        StoreVO vo = (list!=null && !list.isEmpty())? list.get(0):null;
        if( vo!=null ){
            // shippings, payments, storeAreas
            List<ShippingVO> shippings = shippingFacade.findByStore(id);
            vo.setShippings(shippings);
            List<PaymentVO> payments = paymentFacade.findByStore(id);
            vo.setPayments(payments);
            List<StoreAreaVO> areas = storeAreaFacade.findByStore(id);
            vo.setAreas(areas);

            // prdBrand, prdUnit, cusLevel, cusFeedback
            List<OptionVO> prdBrandList = optionFacade.findByType(id, OptionEnum.PRD_BRAND.getCode());
            vo.setPrdBrand(prdBrandList);
            List<OptionVO> prdUnitList = optionFacade.findByType(id, OptionEnum.PRD_UNIT.getCode());
            vo.setPrdUnit(prdUnitList);
            List<OptionVO> cusLevelList = optionFacade.findByType(id, OptionEnum.CUS_LEVEL.getCode());
            vo.setCusLevel(cusLevelList);
            List<OptionVO> cusFeedbackList = optionFacade.findByType(id, OptionEnum.CUS_FEEDBACK.getCode());
            vo.setCusFeedback(cusFeedbackList);

            // picture
            FileVO logo = fileFacade.findSingleByPrimary(vo.getStoreId(), FileEnum.STORE_LOGO.getCode(), vo.getStoreId());
            FileVO banner = fileFacade.findSingleByPrimary(vo.getStoreId(), FileEnum.STORE_BANNER.getCode(), vo.getStoreId());

            vo.setLogo(logo);
            vo.setBanner(banner);
        }
        
        return vo;
    }
    
    public List<Long> findIdListByArea(List<Long> arealist){
        logger.debug("findByCriteriaSQL:"+System.getProperty("java.classpath"));
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT distinct S.ID as id \n");
        sql.append("from EC_STORE S join EC_STORE_AREA SA on SA.STORE_ID = S.ID \n");
        sql.append("WHERE 1=1 \n");
//        if( arealist!=null && !arealist.isEmpty() ){
        if( CollectionUtils.isNotEmpty(arealist) ){
            sql.append(NativeSQLUtils.getInSQL("SA.AREA_ID", arealist, params)).append("\n");
        }
        
        return this.findIdList(sql.toString(), params);
    }
    
    public List<EcStore> findByCriteria(StoreFilter filter) {
        List<EcStore> result = null;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<EcStore> cq = cb.createQuery(EcStore.class);
        Root<EcStore> root = cq.from(EcStore.class);
        cq.select(root);

        List<Predicate> predicateList = new ArrayList();
        if (filter.getId() != null) {
            predicateList.add(cb.equal(root.get("id").as(Long.class), filter.getId()));
        }
        if( CollectionUtils.isNotEmpty(filter.getStoreIdList()) ){
            predicateList.add(root.get("id").as(Long.class).in(filter.getStoreIdList()));
        }
        logger.debug("Keyword:"+filter.getKeyword());
        if (StringUtils.isNotEmpty(filter.getKeyword())) {//關鍵字
            Predicate[] ps = new Predicate[3];
            ps[0] = cb.like(root.get("cname").as(String.class), "%".concat(filter.getKeyword()).concat("%"));
            ps[1] = cb.like(root.get("ename").as(String.class), "%".concat(filter.getKeyword()).concat("%"));
            ps[2] = cb.like(root.get("brief").as(String.class), "%".concat(filter.getKeyword()).concat("%"));
            predicateList.add(cb.or(ps));
        }

        //store not disable and open
        predicateList.add(cb.notEqual(root.get("disabled").as(Boolean.class), Boolean.TRUE));//not disable
        predicateList.add(cb.equal(root.get("opened").as(Boolean.class), Boolean.TRUE));//open
        
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
            }
        }
//        result = em.createQuery(cq).getResultList();
        if (filter.getStartResult()>0 && filter.getMaxResult()>0) {
            result = em.createQuery(cq).setFirstResult(filter.getStartResult()).setMaxResults(filter.getMaxResult()).getResultList();
        }else{
            result = em.createQuery(cq).setMaxResults(20).getResultList();
        }
        
//        logger.debug("result:"+result.size());
        return result;
    }
    
    public List<Long> findIdListByMember(EcMember member){
//        logger.debug("findIdListByMember:"+System.getProperty("java.classpath"));
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT distinct S.ID as id \n");
        sql.append("from EC_STORE S join EC_STORE_USER SU on SU.STORE_ID = S.ID \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND SU.DISABLED != 1 \n");
        sql.append("AND S.OPENED = 1 \n");
        sql.append("AND S.DISABLED != 1 \n");
        if( member != null ){
            sql.append(NativeSQLUtils.genEqulSQL("SU.MEMBER_ID",  member.getId(), params)).append("\n");
        }
        
        return this.findIdList(sql.toString(), params);
    }
    
}
