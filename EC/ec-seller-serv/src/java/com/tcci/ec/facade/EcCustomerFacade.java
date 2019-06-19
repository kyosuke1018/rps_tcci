package com.tcci.ec.facade;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcCreditsLog;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcCustomer;
import com.tcci.ec.entity.EcTccDealerDs;
import com.tcci.ec.enums.CreditLogEnum;
import com.tcci.ec.enums.CusCreditEnum;
import com.tcci.ec.enums.CustomerEnum;
import com.tcci.ec.enums.OptionEnum;
import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.PayStatusEnum;
import com.tcci.ec.enums.PersonTypeEnum;
import com.tcci.ec.model.criteria.CustomerCriteriaVO;
import com.tcci.ec.model.CustomerVO;
import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.ec.model.statistic.StatisticCusLevelVO;
import com.tcci.fc.util.CollectionUtils;
import com.tcci.fc.util.ResourceBundleUtils;
import com.tcci.fc.util.StringUtils;
import java.math.BigDecimal;
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
public class EcCustomerFacade extends AbstractFacade<EcCustomer> {
    @EJB EcMemberFacade memberFacade;
    @EJB EcOrderFacade orderFacade;
    @EJB EcCreditsLogFacade creditsLogFacade;
    @EJB EcStoreFacade storeFacade;
    @EJB EcTccDealerDsFacade tccDealerDsFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcCustomerFacade() {
        super(EcCustomer.class);
    }

    /**
     * 設定信用額度 -- 編輯當下信用額度
     * @param cusEntity
     * @param credits
     * @param reason
     * @param operator
     * @param locale
     * @param errors
     * @return 
     */
    public boolean setCredits(EcCustomer cusEntity, BigDecimal credits, CreditLogEnum typeEnum, String reason, Long orderId,
            EcMember operator, Locale locale, List<String> errors){
        if( cusEntity==null || credits==null || operator==null ){
            logger.error("setCredits cusEntity==null || credits==null || operator==null");
            return false;
        }
        
        if( cusEntity.getApplyTime()==null ){
            logger.error("setCredits cusEntity==null || cusEntity.getApplyTime()==null");
            errors.add(ResourceBundleUtils.getMessage(locale, "cus.noapply.credits"));// 客戶未申請信用額度!
            return false;
        }
               
        Long creditsCur = (cusEntity.getCreditsCur()==null)?GlobalConstant.DEF_CURRENCY_ID:cusEntity.getCreditsCur();
        BigDecimal creditsOri = (cusEntity.getCredits()==null)?BigDecimal.ZERO:cusEntity.getCredits();
        BigDecimal creditsDiff = credits.subtract(creditsOri);// 異動
                
        EcCreditsLog entity = new EcCreditsLog();
        entity.setCredits(credits);
        entity.setType(typeEnum.getCode());
        entity.setCreditsCur(creditsCur);
        entity.setCreditsDiff(creditsDiff);
        entity.setCreditsOri(creditsOri);
        entity.setMemberId(cusEntity.getMemberId());
        entity.setStoreId(cusEntity.getStoreId());
        entity.setReason(reason);
        entity.setOrderId(orderId);
        
        if( checkInput(cusEntity, operator, locale, errors)
         && creditsLogFacade.checkInput(entity, operator, locale, errors) ){
            // 先寫 LOG
            creditsLogFacade.save(entity, operator, false);
            logger.info("setCredits save EcCreditsLog");
            
            // 寫 EcCustomer
            cusEntity.setCredits(credits);
            cusEntity.setCreditsCur(creditsCur);
            cusEntity.setCreditsTime(new Date());
            cusEntity.setCreditsUser(operator.getId());            
            this.save(cusEntity, operator, false);
            
            return true;
        }
        return false;
    }

    /**
     * 變更信用額度 -- 編輯異動信用額度的數字
     * 
     * @param storeId
     * @param customerId
     * @param subtract
     * @param changeAmt
     * @param reason
     * @param operator
     * @param locale
     * @param errors
     * @return 
     */
    public boolean changeCredits(Long storeId, Long customerId, boolean subtract, BigDecimal changeAmt, CreditLogEnum typeEnum, String reason, Long orderId,
            EcMember operator, Locale locale, List<String> errors){
        if( storeId==null || customerId==null || changeAmt==null || operator==null ){
            logger.error("setNewCredits storeId==null || customerId==null || useAmt==null || operator==null");
            return false;
        }
        
        EcCustomer cusEntity = this.find(customerId);
        if( cusEntity==null || cusEntity.getApplyTime()==null 
                || cusEntity.getCredits()==null ){
            logger.error("setNewCredits cusEntity==null || cusEntity.getApplyTime()==null || cusEntity.getCredits()==null ");
            errors.add(ResourceBundleUtils.getMessage(locale, "cus.noapply.credits"));// 客戶未申請信用額度!
            return false;
        }
        if( subtract && BigDecimal.ZERO.equals(cusEntity.getCredits()) ){
            errors.add(ResourceBundleUtils.getMessage(locale, "cus.no.credits"));// 客戶無信用額度!
            return false;
        }
               
        Long creditsCur = (cusEntity.getCreditsCur()==null)?GlobalConstant.DEF_CURRENCY_ID:cusEntity.getCreditsCur();
        BigDecimal creditsOri = (cusEntity.getCredits()==null)?BigDecimal.ZERO:cusEntity.getCredits();
        BigDecimal creditsDiff = changeAmt;// 異動
        BigDecimal credits = subtract?creditsOri.subtract(changeAmt):creditsOri.add(changeAmt);// 剩餘信用額度

        EcCreditsLog entity = new EcCreditsLog();
        entity.setType(typeEnum.getCode());
        entity.setCredits(credits);
        entity.setCreditsCur(creditsCur);
        entity.setCreditsDiff(creditsDiff);
        entity.setCreditsOri(creditsOri);
        entity.setMemberId(cusEntity.getMemberId());
        entity.setStoreId(storeId);
        entity.setReason(reason);
        entity.setOrderId(orderId);
        
        if( creditsLogFacade.checkInput(entity, operator, locale, errors) ){
            // 先寫 LOG
            creditsLogFacade.save(entity, operator, false);
            logger.info("setNewCredits save EcCreditsLog");
            // 寫 EcCustomer
            cusEntity.setCredits(credits);
            cusEntity.setCreditsCur(creditsCur);
            cusEntity.setCreditsTime(new Date());
            cusEntity.setCreditsUser(operator.getId());
            this.save(cusEntity, operator, false);
            return true;
        }
        return false;
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcCustomer entity, EcMember operator, boolean simulated){
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
    public void saveVO(CustomerVO vo, EcMember operator, boolean simulated){
        if( vo!=null ){
            EcCustomer entity = (vo.getId()!=null && vo.getId()>0)? this.find(vo.getId()):new EcCustomer();
            // 需保存的系統產生欄位
            //vo.setCreator(entity.getCreator()!=null? entity.getCreator().getId():null);
            vo.setCreatetime(entity.getCreatetime());
            // 複製 UI 輸入欄位
            ExtBeanUtils.copyProperties(entity, vo);
            // DB 儲存
            save(entity, operator, simulated);
            // 回傳 VO 欄位
            vo.setId(entity.getId());
            vo.setCreatorId(entity.getCreator()!=null? entity.getCreator().getId():null);
            vo.setCreatetime(entity.getCreatetime());
            vo.setModifierId(entity.getModifier()!=null? entity.getModifier().getId():null);
            vo.setModifytime(entity.getModifytime());
        }
    }

    /**
     *  經銷商下游客戶，加入商店客戶
     * @param dealerId
     * @param dsId
     * @param operator
     * @param simulated 
     */
    public void saveByTccDealer(Long dealerId, Long dsId, EcMember operator, boolean simulated) {
        List<LongOptionVO> list = storeFacade.findByOwnerStoreOptions(dealerId);// 擁有商店皆要設定
        
        if( list!=null ){
            for(LongOptionVO op : list){
                saveByKey(dsId, op.getValue(), CustomerEnum.DOWNSTREAM, operator, simulated);
            }
        }
    }
    
    /**
     *  save by unique key
     * @param memberId
     * @param storeId
     * @param customerEnum
     * @param operator
     * @param simulated 
     */
    public void saveByKey(Long memberId, Long storeId, CustomerEnum customerEnum, EcMember operator, boolean simulated) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("memberId", memberId);
        params.put("storeId", storeId);
        
        EcCustomer entity = findByKey(memberId, storeId);
        if( entity==null ){
            entity = new EcCustomer(memberId, storeId);
            entity.setCusType(customerEnum!=null?customerEnum.getCode():null);
            save(entity, operator, simulated);
            logger.info("saveByKey save memberId="+memberId+", storeId="+storeId);
        }else{
            logger.warn("saveByKey exists memberId="+memberId+", storeId="+storeId);
        }
    }
    
    public EcCustomer findByKey(Long memberId, Long storeId){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("memberId", memberId);
        params.put("storeId", storeId);
        
        List<EcCustomer> list = this.findByNamedQuery("EcCustomer.findByKey", params);
        return (list==null || list.isEmpty())? null:list.get(0);
    }
    
    /**
     * 客戶等級統計
     * @param criteriaVO
     * @return 
     */
    public List<StatisticCusLevelVO> findGroupByLevel(CustomerCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT C.LEVEL_ID, L.CNAME LABEL, COUNT(DISTINCT C.ID) VALUE \n"); 
        sql.append(findByCriteriaSQL(criteriaVO, params));
        sql.append("GROUP BY C.LEVEL_ID, L.CNAME \n"); 
        
        List<StatisticCusLevelVO> list = this.selectBySql(StatisticCusLevelVO.class, sql.toString(), params);
        
        if( !CollectionUtils.isEmpty(list) ){
            for(StatisticCusLevelVO vo : list){
                vo.genLabel();
            }
        }
        return list;
    }

    /**
     * 有交易的會員，加入商店客戶關聯主檔
     * @param storeId
     * @return 
     */
    public int addCustomerByOrder(Long storeId){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");

        // 建立者為會員本身
        // 建立時間第一次PO建立時間
        sql.append("INSERT INTO EC_CUSTOMER (ID, MEMBER_ID, CREATOR, CREATETIME, STORE_ID, CUS_TYPE) \n");
        sql.append("SELECT SEQ_CUSTOMER.NEXTVAL, M.ID MEMBER_ID, M.ID CREATOR, firstBuyDate, O.STORE_ID, '")
           .append(CustomerEnum.ORDER.getCode()).append("' \n");// 交易產生的客戶
        sql.append("FROM EC_MEMBER M \n");

        // 有過交易
        sql.append("JOIN ( \n");
        sql.append("    SELECT STORE_ID, MEMBER_ID, MIN(CREATETIME) firstBuyDate \n");
        sql.append("    FROM EC_ORDER \n"); 
        sql.append("    WHERE 1=1 \n");
        sql.append("    AND STATUS IN (").append(OrderStatusEnum.getStatusListStr()).append(") \n");
        sql.append("    GROUP BY STORE_ID, MEMBER_ID \n");
        sql.append(") O ON O.MEMBER_ID=M.ID \n");

        sql.append("WHERE 1=1 \n"); 
        
        if( storeId!=null ){
            sql.append("AND O.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", storeId);
        }
        
        sql.append("AND NOT EXISTS ( \n");
        sql.append("    SELECT * FROM EC_CUSTOMER C WHERE C.MEMBER_ID=M.ID AND C.STORE_ID=O.STORE_ID \n");
        sql.append("); \n");

        sql.append("END; \n");
        
        logger.debug("addCustomerByOrder sql =\n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        setParamsToQuery("addCustomerByOrder", params, q);
        
        return q.executeUpdate();
    }

    /**
     * 依條件查詢商品
     * @param criteriaVO
     * @return 
     */
    public List<CustomerVO> findByCriteria(CustomerCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
                
        sql.append("SELECT C.ID, C.LEVEL_ID, C.CREDITS, C.CUS_TYPE \n");
        sql.append(", C.EXPECTED_CREDITS, C.APPLY_TIME creditsApplyTime \n");
        sql.append(", C.CREDITS_TIME, C.CREDITS_USER, C.MEMO, C.CREDITS_CUR \n");
        sql.append(", S.ID MEMBER_ID, S.LOGIN_ACCOUNT, S.NAME, S.EMAIL, S.PHONE, S.ACTIVE \n");
        sql.append(", D.* \n");// EC_COMPANY or EC_PERSON
        sql.append(", L.CNAME LEVEL_NAME \n"); 
        sql.append(", O.STORE_ID, O.COUNTS ORDER_COUNT, NVL(O.TOTAL_AMT, 0) TOTAL_AMT, O.NO_PAY_AMT, O.firstBuyDate, O.lastBuyDate \n"); 

        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.LOGIN_ACCOUNT");
        }
        
        List<CustomerVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(CustomerVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(CustomerVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(CustomerVO.class, sql.toString(), params);
        }
        return list;
    }
    public int countByCriteria(CustomerCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    public String findByCriteriaSQL(CustomerCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("FROM EC_MEMBER S \n");
        // 客戶關聯檔
        sql.append("JOIN EC_CUSTOMER C ON C.MEMBER_ID=S.ID \n");
        // 已成交訂單
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("    SELECT STORE_ID, MEMBER_ID, COUNT(ID) COUNTS, SUM(TOTAL) TOTAL_AMT \n");
        sql.append("    , MIN(APPROVETIME) firstBuyDate, MAX(APPROVETIME) lastBuyDate \n");
        sql.append("    , SUM(CASE WHEN PAY_STATUS='").append(PayStatusEnum.NOT_PAID.getCode()).append("' THEN TOTAL ELSE 0 END) NO_PAY_AMT \n");
        sql.append("    FROM EC_ORDER \n");
        sql.append("    WHERE 1=1 \n");
        sql.append("    AND STATUS IN (").append(OrderStatusEnum.getCanCountListStr()).append(") \n");
        sql.append("    GROUP BY STORE_ID, MEMBER_ID \n");
        sql.append(") O ON O.MEMBER_ID=C.MEMBER_ID AND O.STORE_ID=C.STORE_ID \n");
        // 客戶等級
        sql.append("LEFT OUTER JOIN EC_OPTION L ON L.TYPE='").append(OptionEnum.CUS_LEVEL.getCode()).append("' AND L.ID=C.LEVEL_ID \n");
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append(memberFacade.getMemberDetailInfoSQL());// 會員詳細資訊
        sql.append(") D ON D.TYPE='").append(PersonTypeEnum.MEMBER.getCode()).append("' AND D.MAIN_ID=S.ID AND D.MEM_TYPE=S.TYPE \n");
        sql.append("WHERE 1=1 \n");
        
        sql.append("AND C.STORE_ID=#STORE_ID \n");
        params.put("STORE_ID", criteriaVO.getStoreId());

        if( criteriaVO.getId()!=null ){// EC_CUSTOMER
            sql.append("AND C.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        if( criteriaVO.getMemberId()!=null ){// EC_MEMBER
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getMemberId());
        }
        if( criteriaVO.getLoginAccount()!=null ){// EC_MEMBER
            sql.append("AND UPPER(S.LOGIN_ACCOUNT)=UPPER(#LOGIN_ACCOUNT) \n");
            params.put("LOGIN_ACCOUNT", criteriaVO.getLoginAccount().trim());
        }
        if( !StringUtils.isBlank(criteriaVO.getKeyword()) ){
            String kw = "%" + criteriaVO.getKeyword().trim() + "%";
            sql.append("AND ( \n");
            sql.append("S.LOGIN_ACCOUNT LIKE #CUSKW OR S.NAME LIKE #CUSKW OR S.EMAIL LIKE #CUSKW \n");
            //sql.append("OR D.CNAME LIKE #CUSKW OR D.ENAME LIKE #CUSKW OR D.ID_CODE LIKE #CUSKW OR D.NICKNAME LIKE #CUSKW \n");
            sql.append(") \n");
            params.put("CUSKW", kw);
        }
        // 客戶等級
        if( criteriaVO.getCusLevel()!=null && criteriaVO.getCusLevel()>0 ){
            sql.append("AND C.LEVEL_ID=#LEVEL_ID \n");
            params.put("LEVEL_ID", criteriaVO.getCusLevel());
        }
        // 詢價單/訂單相關
        if( !StringUtils.isBlank(criteriaVO.getRfqStatus()) 
         || !StringUtils.isBlank(criteriaVO.getOrderStatus()) 
         || !StringUtils.isBlank(criteriaVO.getPayStatus())    
         || !StringUtils.isBlank(criteriaVO.getShipStatus()) ){
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT STORE_ID, MEMBER_ID \n"); 
            sql.append("    FROM EC_ORDER \n"); 
            sql.append("    WHERE 1=1 \n");
            // 詢價單狀態
            if( !StringUtils.isBlank(criteriaVO.getRfqStatus()) ){
                sql.append("    AND STATUS = #RFQ_STATUS \n");
                params.put("RFQ_STATUS", criteriaVO.getRfqStatus());
            }
            // 訂單狀態
            if( !StringUtils.isBlank(criteriaVO.getOrderStatus()) ){
                sql.append("    AND STATUS = #ORDER_STATUS \n");
                params.put("ORDER_STATUS", criteriaVO.getOrderStatus());
            }
            // 付款狀態
            if( !StringUtils.isBlank(criteriaVO.getPayStatus()) ){
                sql.append("    AND PAY_STATUS = #PAY_STATUS \n");
                params.put("PAY_STATUS", criteriaVO.getPayStatus());
            }
            // 出貨狀態
            if( !StringUtils.isBlank(criteriaVO.getShipStatus()) ){
                sql.append("    AND SHIP_STATUS = #SHIP_STATUS \n");
                params.put("SHIP_STATUS", criteriaVO.getShipStatus());
            }
            sql.append("    AND STORE_ID=C.STORE_ID AND MEMBER_ID=S.ID \n");
            sql.append(") \n");
        }
  
        // 累績消費
        if( criteriaVO.getCumulativeS()!=null ){
            sql.append("AND O.TOTAL_AMT>=#TOTAL_S \n");
            params.put("TOTAL_S", criteriaVO.getCumulativeS());
        }
        if( criteriaVO.getCumulativeE()!=null ){
            sql.append("AND O.TOTAL_AMT<=#TOTAL_E \n");
            params.put("TOTAL_E", criteriaVO.getCumulativeE());
        }
        // 信用額度
        
        if( criteriaVO.getCreditStatus()!=null ){
            CusCreditEnum creditEnum = CusCreditEnum.getFromCode(criteriaVO.getCreditStatus());
            if( creditEnum==CusCreditEnum.NONE ){
                sql.append("AND C.APPLY_TIME IS NULL \n");
            }else if( creditEnum==CusCreditEnum.APPLY ){
                sql.append("AND C.APPLY_TIME IS NOT NULL AND C.CREDITS_TIME IS NULL \n");
            }else if( creditEnum==CusCreditEnum.PASS ){
                sql.append("AND C.CREDITS_TIME IS NOT NULL \n");
            }else if( creditEnum==CusCreditEnum.ZERO ){
                sql.append("AND C.CREDITS_TIME IS NOT NULL AND NVL(C.CREDITS, 0)=0 \n");
            }
        }
        return sql.toString();
    }
    
    public CustomerVO findById(Long storeId, Long id, boolean fullInfo){
        if( storeId==null || id==null ){
            logger.error("findById error storeId="+storeId+", id="+id);
            return null;
        }
        
        CustomerCriteriaVO criteriaVO = new CustomerCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setId(id);
        List<CustomerVO> list = findByCriteria(criteriaVO);
        
        CustomerVO prdVO = (list!=null && !list.isEmpty())? list.get(0):null;
        
        if( prdVO==null || !fullInfo ){
            return prdVO;
        }
        // 其他關聯資訊
        // 訂單資訊 (考量資料可能多，用 Lazy loading)
        // 客戶反映 (考量資料可能多，用 Lazy loading)  
        return prdVO;
    }
    
    public CustomerVO findByMember(Long storeId, String loginAccount, boolean fullInfo){
        if( storeId==null || loginAccount==null ){
            logger.error("findByMember error storeId="+storeId+", loginAccount="+loginAccount);
            return null;
        }
        
        CustomerCriteriaVO criteriaVO = new CustomerCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setLoginAccount(loginAccount);
        List<CustomerVO> list = findByCriteria(criteriaVO);
        
        CustomerVO prdVO = (list!=null && !list.isEmpty())? list.get(0):null;
        
        if( prdVO==null || !fullInfo ){
            return prdVO;
        }
        // 其他關聯資訊
        // 訂單資訊 (考量資料可能多，用 Lazy loading)
        // 客戶反映 (考量資料可能多，用 Lazy loading)
        return prdVO;
    }
    public CustomerVO findByMemberId(Long storeId, Long memberId, boolean fullInfo){
        if( storeId==null || memberId==null ){
            logger.error("findByMember error storeId="+storeId+", memberId="+memberId);
            return null;
        }
        
        CustomerCriteriaVO criteriaVO = new CustomerCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setMemberId(memberId);
        List<CustomerVO> list = findByCriteria(criteriaVO);
        
        CustomerVO vo = (list!=null && !list.isEmpty())? list.get(0):null;
        
        if( vo==null || !fullInfo ){
            return vo;
        }
        // 其他關聯資訊
        // 訂單資訊 (考量資料可能多，用 Lazy loading)
        // 客戶反映 (考量資料可能多，用 Lazy loading)
        return vo;
    }

    /**
     * 輸入檢查
     * @param entity
     * @param member
     * @param locale
     * @param errors
     * @return 
     */
    public boolean checkInput(EcCustomer entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }

    public void addCustomerByDealer(Long dealerId, EcMember operator) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealerId", dealerId);
        
        List<EcTccDealerDs> list = tccDealerDsFacade.findByNamedQuery("EcTccDealerDs.findByDealer", params);
        
        if( list!=null ){
            for(EcTccDealerDs dealerDs : list){
                // 設為客戶
                saveByTccDealer(dealerDs.getDealerId(), dealerDs.getDsId(), operator, false);
            }
        }
    }
    
}
