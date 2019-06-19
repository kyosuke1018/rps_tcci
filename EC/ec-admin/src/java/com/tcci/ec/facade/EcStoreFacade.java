/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.LongOptionVO;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.ec.entity.EcCompany;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOption;
import com.tcci.ec.entity.EcPayment;
import com.tcci.ec.entity.EcShipping;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.BrandEnum;
import com.tcci.ec.enums.CompanyTypeEnum;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.OptionEnum;
import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.PayMethodEnum;
import com.tcci.ec.enums.PayStatusEnum;
import com.tcci.ec.enums.ProductStatusEnum;
import com.tcci.ec.enums.ProductUnitEnum;
import com.tcci.ec.enums.RfqStatusEnum;
import com.tcci.ec.enums.ShipMethodEnum;
import com.tcci.ec.enums.ShipStatusEnum;
import com.tcci.ec.enums.TodoEnum;
import com.tcci.ec.enums.VendorEnum;
import com.tcci.ec.model.criteria.StoreCriteriaVO;
import com.tcci.ec.model.FileVO;
import com.tcci.ec.model.MemberVO;
import com.tcci.ec.model.OptionVO;
import com.tcci.ec.model.PaymentVO;
import com.tcci.ec.model.ShippingVO;
import com.tcci.ec.model.StoreAreaVO;
import com.tcci.ec.model.StoreVO;
import com.tcci.ec.model.VendorVO;
import com.tcci.fc.util.StringUtils;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class EcStoreFacade extends AbstractFacade<EcStore> {
    @EJB EcCompanyFacade companyFacade;
    @EJB EcShippingFacade shippingFacade;
    @EJB EcPaymentFacade paymentFacade;
    @EJB EcStoreAreaFacade storeAreaFacade;
    @EJB EcOptionFacade optionFacade;
    @EJB EcVendorFacade vendorFacade;
    @EJB EcFileFacade fileFacade;
    @EJB EcTccProductFacade tccProductFacade;

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcStoreFacade() {
        super(EcStore.class);
    }
    
    /**
     * 新增商店
     * @param vo
     * @param storeVO
     * @param operator
     * @param locale
     * @param defStore
     * @param forTccDealer
     * @param simulated
     * @return 
     */
    public EcStore createNewStore(MemberVO vo, StoreVO storeVO,
            EcMember operator, Locale locale, 
            boolean defStore, boolean forTccDealer, boolean simulated){
        // 首次建立，部分欄位自會員註冊資料 copy
        String cname = (storeVO==null || storeVO.getCname()==null)?vo.getCname():storeVO.getCname();
        String ename = (storeVO==null || storeVO.getEname()==null)?vo.getEname():storeVO.getEname();
        String brief = (storeVO==null || storeVO.getEname()==null)?vo.getBrief():storeVO.getBrief();
        String email = (storeVO==null || storeVO.getEmail1()==null)?vo.getEmail():storeVO.getEmail1(); 
        String tel = (storeVO==null || storeVO.getTel1()==null)?vo.getPhone():storeVO.getTel1();
        String idCode = (storeVO==null || storeVO.getIdCode()==null)?vo.getIdCode():storeVO.getIdCode();
        String addr1 = (storeVO==null || storeVO.getIdCode()==null)?vo.getAddr1():storeVO.getAddr1();
        
        // 詳細資料未輸入，抓住註冊資料
        cname = StringUtils.isBlank(cname)? vo.getName():cname;
        email = StringUtils.isBlank(email)? vo.getEmail():email;
        tel = StringUtils.isBlank(tel)? vo.getPhone():tel;

        // 商店主檔 EC_STORE
        EcStore store = new EcStore();
        store.setDisabled(false);
        store.setOpened(false);
        store.setDefStore(defStore);// 預設商店
        store.setSellerId(vo.getSellerId());
        store.setCname(cname);
        store.setEname(ename);
        store.setBrief(brief);
        store.setOpened(forTccDealer);// TCC Dealer 預設開店
        this.save(store, operator, simulated);
        logger.info("createNewStore store.getId() = "+store.getId());
        
        // 商店明細 EC_COMPANY
        EcCompany company = new EcCompany();
        company.setCname(cname);
        company.setEname(ename);
        company.setNickname(vo.getNickname());
        company.setBrief(brief);
        company.setEmail1(email);
        company.setTel1(tel);
        company.setIdCode(idCode);
        company.setAddr1(addr1);
        company.setType(CompanyTypeEnum.STORE.getCode());
        company.setMainId(store.getId());
        companyFacade.save(company, operator, simulated);
        logger.info("createNewStore company save = "+company.getId());
        
        // 預設加入系統提供設定
        autoGenStoreConfig(store.getId(), true, operator, locale, forTccDealer, simulated);

        return store;
    }
    
    /**
     * 預設加入系統提供設定
     * @param storeId
     * @param forNew
     * @param operator
     * @param locale
     * @param simulated 
     * @param forTccDealer 
     */
    public void autoGenStoreConfig(Long storeId, boolean forNew, EcMember operator, Locale locale, 
            boolean forTccDealer, boolean simulated){
        int sortnum = 0;
        // EC_SHIPPING
        for(ShipMethodEnum method : ShipMethodEnum.values()){
            ShippingVO vo = null;
            if( !forNew ){
                vo = shippingFacade.findByKey(storeId, method.getCode());
            }
            if( forNew || vo==null ){
                sortnum++;
                EcShipping entity = shippingFacade.genByEnum(storeId, method, sortnum, locale);
                shippingFacade.save(entity, operator, simulated);
            }
        }
        logger.info("autoGenStoreConfig after EC_SHIPPING save.");
        
        // EC_PAYMENT
        sortnum = 0;
        for(PayMethodEnum method : PayMethodEnum.values()){
            PaymentVO vo = null;
            if( !forNew ){
                vo = paymentFacade.findByKey(storeId, method.getCode());
            }
            if( forNew || vo==null ){
                sortnum++;
                EcPayment entity = paymentFacade.genByEnum(storeId, method, sortnum, locale);
                paymentFacade.save(entity, operator, simulated);
            }
        }
        logger.info("autoGenStoreConfig after EC_PAYMENT save.");
        
        // EC_OPTION - for prdUnit
        sortnum = 0;
        for(ProductUnitEnum item : ProductUnitEnum.values()){
            OptionVO vo = null;
            if( !forNew ){
                vo = optionFacade.findByCode(storeId, OptionEnum.PRD_UNIT, item.getCode());
            }
            if( forNew || vo==null ){
                sortnum++;
                EcOption entity = optionFacade.genByPrdUnitEnum(storeId, item, sortnum, locale);
                optionFacade.save(entity, operator, simulated);
            }
        }
        logger.info("autoGenStoreConfig after EC_OPTION - for prdUnit save.");
        
        if( forTccDealer ){// 台泥經銷商
            // EC_OPTION - for prdBrand
            sortnum = 0;
            for(BrandEnum item : BrandEnum.values()){
                OptionVO vo = null;
                if( !forNew ){
                    vo = optionFacade.findByCode(storeId, OptionEnum.PRD_BRAND, item.getCode());
                }
                if( forNew || vo==null ){
                    sortnum++;
                    EcOption entity = optionFacade.genByPrdBrandEnum(storeId, item, sortnum, locale);
                    optionFacade.save(entity, operator, simulated);
                }
            }
            logger.info("autoGenStoreConfig after EC_OPTION - for prdBrand save.");

            // EC_VENDOR (& EC_COMPANY)
            sortnum = 0;
            for(VendorEnum item : VendorEnum.values()){
                VendorVO vo = null;
                if( !forNew ){
                    vo = vendorFacade.findByCode(storeId, item.getCode(), false);
                }
                if( forNew || vo==null ){
                    sortnum++;
                    vo = vendorFacade.genByVendorEnum(storeId, item, sortnum, locale);
                    vendorFacade.saveVO(vo, operator, simulated);
                }
            }
            logger.info("autoGenStoreConfig after EC_VENDOR (& EC_COMPANY) save.");
            // 匯入所有台泥有效商品
            tccProductFacade.addAllTccProducts(storeId, operator, locale);
            logger.info("autoGenStoreConfig addAllTccProducts after EC_PRODUCT save.");
        }
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcStore entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getDisabled()==null ){ entity.setDisabled(false); }
            if( entity.getDefStore()==null ){ entity.setDefStore(true); }

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
    public void saveVO(StoreVO vo, EcMember operator, boolean simulated){
        if( vo==null ){
            logger.error("saveVO vo==null");
            return;
        }
        logger.debug("saveVO storeId = {}, sellerId = {}, companyId = {}", new Object[] {vo.getStoreId(), vo.getSellerId(), vo.getId()});
        EcCompany company = (vo.getId()!=null)?companyFacade.find(vo.getId()):new EcCompany();
        EcStore store = (vo.getStoreId()!=null)?this.find(vo.getStoreId()):new EcStore();
        
        store.setDisabled(vo.getDisabled());
        store.setOpened(vo.getOpened());
        store.setCname(vo.getCname());
        store.setEname(vo.getEname());
        store.setBrief(vo.getBrief());
        store.setRemitAccount(vo.getRemitAccount());
        this.save(store, operator, simulated);
        
        ExtBeanUtils.copyProperties(company, vo);
        company.setType(CompanyTypeEnum.STORE.getCode());
        company.setMainId(store.getId());
        companyFacade.save(company, operator, simulated);
    }
    
    /**
     * find By SellerId
     * @param sellerId
     * @return 
     */
    public EcStore findBySellerId(Long sellerId){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sellerId", sellerId);
        params.put("defStore", true);
        List<EcStore> list = this.findByNamedQuery("EcStore.findBySellerId", params);
        
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     * 指定會員的商店 // 可管理商店 (1.包含自己註冊商店 及 2.被加入管理者的商店)
     * @param memberVO
     * @param includeMgn 是否包含 2.
     * @return 
     */
    public List<LongOptionVO> findByMemberStoreOptions(MemberVO memberVO, boolean includeMgn){
        StoreCriteriaVO criteriaVO = new StoreCriteriaVO();
        criteriaVO.setMemberId(memberVO.getMemberId());
        criteriaVO.setActive(Boolean.TRUE);
        criteriaVO.setDisabled(Boolean.FALSE);
        
        List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
        List<Long> existedList = new ArrayList<Long>();
        List<StoreVO> list = this.findByCriteria(criteriaVO);
        if( list!=null ){
            logger.debug("findByMemberStoreOptions list = "+list.size());
            for(StoreVO vo : list){
                existedList.add(vo.getStoreId());
                ops.add(new LongOptionVO(vo.getStoreId(), vo.getCname()));// EC_COMPANY.CNAME
            }
        }
        
        if( includeMgn ){// 包含 被加入管理者的商店
            criteriaVO.setMemberId(null);
            criteriaVO.setManagerId(memberVO.getMemberId());
            criteriaVO.setStoreManager(includeMgn);
            criteriaVO.setOrderBy("NVL(SU.MODIFYTIME, SU.CREATETIME) DESC");// 最近設定在前面
            
            list = this.findByCriteria(criteriaVO);
            if( list!=null ){
                logger.debug("findByMemberStoreOptions includeMgn list = "+list.size());
                if( ops.isEmpty() ){
                    memberVO.setManager(true);// 目前為商店管理員身分(對應 storeId)
                    logger.debug("findByMemberStoreOptions Manager = "+memberVO.isManager());
                }
                for(StoreVO vo : list){
                    if( !existedList.contains(vo.getStoreId()) ){
                        existedList.add(vo.getStoreId());
                        ops.add(new LongOptionVO(vo.getStoreId(), vo.getCname()));// EC_COMPANY.CNAME
                    }
                }
            }
        }
        
        memberVO.setStores(ops.isEmpty()?null:ops);
        return ops;
    }
    public List<LongOptionVO> findByOwnerStoreOptions(Long memberId){
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(memberId);
        return findByMemberStoreOptions(memberVO, false);
    }
    
    /**
     * 依統編查詢
     * @param idCode
     * @return 
     */
    public StoreVO findByIdCode(String idCode){
        StoreCriteriaVO criteriaVO = new StoreCriteriaVO();
        criteriaVO.setDisabled(Boolean.FALSE);
        criteriaVO.setIdCode(idCode);
        
        List<StoreVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())?list.get(0):null;
    }
    
    /**
     * 依商品查詢
     * @param criteriaVO
     * @return 
     */
    public int countByCriteria(StoreCriteriaVO criteriaVO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    public List<StoreVO> findByCriteria(StoreCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.ID STORE_ID, S.TYPE STORE_TYPE, S.STYLE_ID, S.FLOW_ID, S.SELLER_ID, S.REMIT_ACCOUNT, S.OPENED, S.DEF_STORE \n");
        sql.append(", C.* \n"); // EC_COMPANY
        sql.append(", M.ID MEMBER_ID, M.LOGIN_ACCOUNT, M.NAME, M.EMAIL, M.PHONE, M.ACTIVE \n");
        sql.append(", M.TCC_DEALER, M.TCC_DS \n");
        sql.append(", NVL(MGR.CC, 0) MANAGER_COUNT \n");
        
        if( criteriaVO.isFullData() ){
            sql.append(", RT.PRATE, RT.NRATE \n");
            sql.append(", NVL(FS.CC, 0) FAV_COUNT \n");
            sql.append(", NVL(FP.CC, 0) FAV_PRD_COUNT \n");
        }
        
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
    /**
     * 依輸入條件查詢
     * @param criteriaVO
     * @param params
     * @return 
     */
    public String findByCriteriaSQL(StoreCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("FROM EC_STORE S \n");
        sql.append("JOIN EC_SELLER R ON R.ID=S.SELLER_ID AND R.DISABLED=0 \n");
        // 關聯有效會員、核准賣家
        sql.append("JOIN EC_MEMBER M ON M.ID=R.MEMBER_ID AND M.DISABLED=0 AND M.ACTIVE=1 AND M.SELLER_APPROVE=1 \n");
        
        if( Boolean.TRUE.equals(criteriaVO.getStoreManager()) ){// 可管理商店(非原賣家)
            sql.append("JOIN EC_STORE_USER SU ON SU.MEMBER_ID=#MANAGER_ID AND SU.STORE_ID=S.ID AND SU.DISABLED=0 \n");
            params.put("MANAGER_ID", criteriaVO.getManagerId());
        }
        
        sql.append("LEFT OUTER JOIN EC_COMPANY C ON C.TYPE=#COM_TYPE AND C.MAIN_ID=S.ID \n");
        params.put("COM_TYPE", CompanyTypeEnum.STORE.getCode());
        
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("    SELECT STORE_ID, COUNT(DISTINCT MEMBER_ID) CC \n");
        sql.append("    FROM EC_STORE_USER \n");
        sql.append("    WHERE 1=1 \n");
        sql.append("    AND DISABLED=0 \n");
        sql.append("    GROUP BY STORE_ID \n");
        sql.append(") MGR ON MGR.STORE_ID=S.ID \n");
        
        if( criteriaVO.isFullData() ){
            // 評價
            sql.append("LEFT OUTER JOIN ( \n");
            sql.append("    SELECT S.STORE_ID \n");
            sql.append("    , SUM(CASE WHEN S.CUSTOMER_RATE>0 THEN 1 ELSE 0 END) PRATE \n");// 正評
            sql.append("    , SUM(CASE WHEN S.CUSTOMER_RATE<0 THEN 1 ELSE 0 END) NRATE \n");// 負評
            sql.append("    FROM EC_ORDER_RATE S \n");
            sql.append("    WHERE 1=1 \n");
            sql.append("    GROUP BY S.STORE_ID \n");
            sql.append(") RT ON RT.STORE_ID=S.ID \n");
            // 喜愛商店
            sql.append("LEFT OUTER JOIN ( \n");
            sql.append("    SELECT STORE_ID, COUNT(DISTINCT MEMBER_ID) CC \n");
            sql.append("    FROM EC_FAVORITE_STORE \n");
            sql.append("    WHERE 1=1 \n");
            sql.append("    GROUP BY STORE_ID \n");
            sql.append(") FS ON FS.STORE_ID=S.ID \n");
            // 喜愛商品
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
        
        // 銷售區域
        if( criteriaVO.getAreaId()!=null ){
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT A.STORE_ID FROM EC_STORE_AREA A WHERE A.STORE_ID=S.ID AND A.AREA_ID=#AREA_ID \n");
            sql.append(") \n");
            params.put("AREA_ID", criteriaVO.getAreaId());
        }else if( criteriaVO.getAreaList()!=null && !criteriaVO.getAreaList().isEmpty() ){
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT A.STORE_ID FROM EC_STORE_AREA A WHERE A.STORE_ID=S.ID \n")
               .append(NativeSQLUtils.getInSQL("A.AREA_ID", criteriaVO.getAreaList(), params));
            sql.append(") \n");
        }
        // 此商店是否為台泥經銷商建立
        if( criteriaVO.getTccDealer()!=null ){
            sql.append("AND M.TCC_DEALER=#TCC_DEALER \n");
            params.put("TCC_DEALER", criteriaVO.getTccDealer());
        }
        if( criteriaVO.getIdCode()!=null ){
            sql.append("AND C.ID_CODE=#ID_CODE \n");
            params.put("ID_CODE", criteriaVO.getIdCode());
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
        if( vo!=null && fullData ){
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
    
    /**
     * 擁有商店數
     * @param memberId
     * @return 
     */
    public int countMemberStore(Long memberId) {
        StoreCriteriaVO criteriaVO = new StoreCriteriaVO();
        criteriaVO.setMemberId(memberId);
        criteriaVO.setDisabled(Boolean.FALSE);
        
        return this.countByCriteria(criteriaVO);
    }
    
    /**
     * @param vo
     * @param member
     * @param locale
     * @param errors
     * @return 
     */
    public boolean checkInput(StoreVO vo, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(vo, "storeId", locale, errors);

        return pass;
    }
    public boolean checkInputEntity(EcStore entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, "storeId", locale, errors);

        return pass;
    }

    /**
     * 線上商店
     * @param memberId
     * @param opLang
     * @return 
     */
    public List<LongOptionVO> findOnlineStoreOps(Long memberId, String opLang) {
        StoreCriteriaVO criteriaVO = new StoreCriteriaVO();
        criteriaVO.setMemberId(memberId);
        criteriaVO.setDisabled(Boolean.FALSE);
        criteriaVO.setFullData(false);
        if( "E".equals(opLang) ){
            criteriaVO.setOrderBy("NVL(S.ENAME, S.CNAME)");
        }else{
            criteriaVO.setOrderBy("S.CNAME");
        }
        
        List<StoreVO> list = findByCriteria(criteriaVO);
        
        List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
        for(StoreVO vo : list){
            StringBuilder label = new StringBuilder();
            // use cname or ename by opLang (C/E)
            String name = "E".equals(opLang)?(StringUtils.isBlank(vo.getEname())?vo.getCname():vo.getEname()):vo.getCname();
            label.append("[").append(vo.getStoreId()).append("]")
                 .append(name!=null?name:"");
            ops.add(new LongOptionVO(vo.getStoreId(), label.toString()));
        }
        return ops;
    }

    /**
     * 設定賣家預設顯示商店
     * @param sellerId
     * @param storeId
     * @param operator
     * @param simulated 
     */
    public void setDefaultStore(Long sellerId, Long storeId, EcMember operator, boolean simulated) {
        if( simulated ){
            logger.info("setDefaultStore simulated = "+simulated);
            return;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");
        
        sql.append("UPDATE EC_STORE \n");
        sql.append("SET DEF_STORE=0 \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND SELLER_ID=#SELLER_ID; \n");
        
        sql.append("UPDATE EC_STORE \n");
        sql.append("SET DEF_STORE=1 \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND SELLER_ID=#SELLER_ID \n");
        sql.append("AND STORE_ID=#STORE_ID; \n");
        
        params.put("SELLER_ID", sellerId);
        params.put("STORE_ID", storeId);
        
        sql.append("END;");
        
        logger.debug("setDefaultStore sql =\n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        setParamsToQuery("setDefaultStore", params, q);
        
        q.executeUpdate();
    }

    /**
     * 待辦事項統計
     * 
     *   PRD_ONSALES("1", "待上架商品", false),
     *   PRD_CORRECT("2", "待修正商品", false),
     *   QUOTATION("3", "待報價詢價單", false),
     *   PO_CONFIRM("4", "待確認訂單", false),
     *   PO_PAY_RECEIVED("4", "待確認收款訂單", false),
     *   PO_SHIP("6", "待出貨訂單", false),
     *   CUS_CREDITS("7", "信用額度申請", false),
     *   PO_MSG_REPLY("8", "待回覆訂單訊息", false),
     *   MEM_MSG_REPLY("9", "待回覆訪客留言", false),
     * 
     * @param todoEnum
     * @param storeId
     * @param memberId
     * @return 
     */
    public BigDecimal countTodo(TodoEnum todoEnum, Long storeId, Long memberId) {
        BigDecimal res = null;
        if( todoEnum==null || storeId==null ){
            logger.error("countTodo todoEnum==null || storeId==null");
            return res;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        if( todoEnum==TodoEnum.PRD_ONSALES ){// 待上架商品
            sql.append("SELECT COUNT(*) CC FROM EC_PRODUCT WHERE STATUS='")// 審核通過
               .append(ProductStatusEnum.PASS.getCode()).append("' AND DISABLED=0 AND STORE_ID=#STORE_ID");
        }else if( todoEnum==TodoEnum.PRD_CORRECT ){// 待修正商品
            sql.append("SELECT COUNT(*) CC FROM EC_PRODUCT WHERE STATUS='")// 審核不通過
               .append(ProductStatusEnum.REJECT.getCode()).append("' AND DISABLED=0 AND STORE_ID=#STORE_ID");
        }else if( todoEnum==TodoEnum.QUOTATION ){// 待報價詢價單
            sql.append("SELECT COUNT(*) CC FROM EC_ORDER WHERE STATUS='")
               .append(RfqStatusEnum.Inquiry.getCode()).append("' AND STORE_ID=#STORE_ID");
        }else if( todoEnum==TodoEnum.PO_CONFIRM ){// 待確認訂單
            sql.append("SELECT COUNT(*) CC FROM EC_ORDER WHERE STATUS='")
               .append(OrderStatusEnum.Pending.getCode()).append("' AND STORE_ID=#STORE_ID");
        }else if( todoEnum==TodoEnum.PO_PAY_RECEIVED ){// 待確認收款訂單
            sql.append("SELECT COUNT(*) CC FROM EC_ORDER WHERE STATUS='").append(OrderStatusEnum.Approve.getCode())
               .append("' AND PAY_STATUS='").append(PayStatusEnum.NOTIFY_PAID.getCode())// 付款通知
               .append("' AND STORE_ID=#STORE_ID");
        }else if( todoEnum==TodoEnum.PO_SHIP ){// 待出貨訂單
            sql.append("SELECT COUNT(*) CC FROM EC_ORDER WHERE STATUS='").append(OrderStatusEnum.Approve.getCode())
               .append("' AND PAY_STATUS='").append(PayStatusEnum.PAID.getCode())// 已付款
               .append("' AND SHIP_STATUS='").append(ShipStatusEnum.NOT_SHIPPED.getCode())// 未出貨
               .append("' AND STORE_ID=#STORE_ID");
        }else if( todoEnum==TodoEnum.CUS_CREDITS ){// 信用額度申請
            sql.append("SELECT COUNT(*) CC FROM EC_CUSTOMER \n");
            sql.append("WHERE STORE_ID=#STORE_ID AND APPLY_TIME IS NOT NULL AND CREDITS_TIME IS NULL");
        }else if( todoEnum==TodoEnum.PO_MSG_REPLY ){// 待回覆訂單訊息
            // 最後一筆為買家留言
            sql.append("SELECT COUNT(S.ID) CC \n");
            sql.append("FROM EC_ORDER_MESSAGE S \n");
            sql.append("JOIN ( \n");
            sql.append("     SELECT MAX(ID) MID, ORDER_ID FROM EC_ORDER_MESSAGE WHERE STORE_ID=#STORE_ID GROUP BY ORDER_ID \n");
            sql.append(") L ON L.MID=S.ID \n");
            sql.append("JOIN EC_ORDER O ON O.ID=S.ORDER_ID \n");
            sql.append(NativeSQLUtils.getInSQL("O.STATUS", OrderStatusEnum.getCodes(), params)).append(" \n");
            sql.append("WHERE 1=1 \n");
            sql.append("AND S.BUYER=1 \n");// 買家
            sql.append("AND S.DISABLED=0 \n");
        }else if( todoEnum==TodoEnum.MEM_MSG_REPLY ){// 待回覆訪客留言   
            // 最後一筆非買家留言 // TODO: 單一商店多人管理問題
            sql.append("SELECT COUNT(S.ID) CC \n");
            sql.append("FROM EC_MEMBER_MSG S \n");
            sql.append("LEFT OUTER JOIN ( \n");
            sql.append("     SELECT A.* \n");
            sql.append("     FROM EC_MEMBER_MSG A \n");
            sql.append("     JOIN ( \n");
            sql.append("       SELECT MAX(ID) MID, PARENT FROM EC_MEMBER_MSG \n");
            sql.append("       WHERE STORE_ID=#STORE_ID AND PARENT IS NOT NULL \n");
            sql.append("       GROUP BY PARENT \n");
            sql.append("     ) L ON L.MID=A.ID \n");
            sql.append(") R ON R.PARENT=S.ID \n");
            sql.append("WHERE S.STORE_ID=#STORE_ID \n");
            sql.append("AND S.PARENT IS NULL \n");
            sql.append("AND (R.ID IS NULL OR R.MEMBER_ID<>#MEMBER_ID) \n");
            
            params.put("MEMBER_ID", memberId);
        }else{
            logger.error("countTodo not support todoEnum=="+todoEnum);
            return null;
        }
        
        params.put("STORE_ID", storeId);
        
        int count = this.count(sql.toString(), params);
        res = BigDecimal.valueOf(Integer.valueOf(count).longValue());

        return res;
    }

    /**
     * 此商店是否為台泥經銷商建立
     * @param storeId
     * @return 
     */
    public boolean isTccDealer(Long storeId) {
        StoreCriteriaVO criteriaVO = new StoreCriteriaVO();
        criteriaVO.setDisabled(Boolean.FALSE);
        criteriaVO.setFullData(false);
        criteriaVO.setTccDealer(true);
        criteriaVO.setId(storeId);
        
        List<StoreVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty());
    }

    /**
     * 依賣家關店
     * @param sellerId
     * @param operator
     * @param simulated 
     */
    void closeAllBySeller(Long sellerId, EcMember operator, boolean simulated) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sellerId", sellerId);
        List<EcStore> list = this.findByNamedQuery("EcStore.findAllBySeller", params);
        
        if( list!=null ){
            for(EcStore entity : list){
                if( entity.getOpened()!=null && entity.getOpened() ){
                    entity.setOpened(false);
                    this.save(entity, operator, simulated);
                    logger.info("closeAllBySeller close store = "+entity.getId());
                }
            }
        }
    }
}
