/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.customer;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcCreditsLog;
import com.tcci.ec.entity.EcCustomer;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOrder;
import com.tcci.ec.enums.CreditLogEnum;
import com.tcci.ec.enums.CustomerEnum;
import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.facade.AbstractFacade;
import com.tcci.fc.util.ResourceBundleUtils;
import java.math.BigDecimal;
import java.text.MessageFormat;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EcCustomerFacade extends AbstractFacade {
    private final static Logger logger = LoggerFactory.getLogger(EcCustomerFacade.class);
    
    @EJB EcCreditsLogFacade creditsLogFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcCustomerFacade() {
        super(EcCustomer.class);
    }

    public void save(EcCustomer entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    public void save(EcCustomer entity, EcMember operator){
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
    
    public EcCustomer find(Long id) {
        return em.find(EcCustomer.class, id);
    }
    
    @Override
    public List<EcCustomer> findAll() {
        return em.createNamedQuery("EcCustomer.findAll").getResultList();
    }
    
    public List<EcCustomer> findByMemberApplied(EcMember member) {
        Query q = em.createNamedQuery("EcCustomer.findByMemberApplied");
        q.setParameter("member", member);
        List<EcCustomer> list = q.getResultList();
        return list;
    }
    
    public List<EcCustomer> findAllByMember(EcMember member) {
        Query q = em.createNamedQuery("EcCustomer.findByMember");
        q.setParameter("member", member);
        List<EcCustomer> list = q.getResultList();
        return list;
    }
    
    public EcCustomer findByMember(EcMember member) {
        Query q = em.createNamedQuery("EcCustomer.findByMember");
        q.setParameter("member", member);
        List<EcCustomer> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public EcCustomer findByMemberAndStore(EcMember member, Long storeId) {
        Query q = em.createNamedQuery("EcCustomer.findByMemberAndStore");
        q.setParameter("member", member);
        q.setParameter("storeId", storeId);
        List<EcCustomer> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public EcCustomer findByApplied(EcMember member, Long storeId) {
        Query q = em.createNamedQuery("EcCustomer.findByApplied");
        q.setParameter("member", member);
        q.setParameter("storeId", storeId);
        List<EcCustomer> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
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
    /**
    public boolean changeCredits(Long storeId, Long customerId, boolean subtract, BigDecimal changeAmt, String reason,
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
        entity.setCredits(credits);
        entity.setCreditsCur(creditsCur);
        entity.setCreditsDiff(creditsDiff);
        entity.setCreditsOri(creditsOri);
//        entity.setMemberId(cusEntity.getMemberId());
        entity.setMemberId(cusEntity.getMember().getId());
        entity.setStoreId(storeId);
        entity.setReason(reason);
        
        if( creditsLogFacade.checkInput(entity, operator, locale, errors) ){
            // 先寫 LOG
            creditsLogFacade.save(entity, operator, false);
            logger.info("setNewCredits save EcCreditsLog");
            // 寫 EcCustomer
            cusEntity.setCredits(credits);
            cusEntity.setCreditsCur(creditsCur);
            cusEntity.setCreditsTime(new Date());
            cusEntity.setCreditsUser(operator.getId());
//            this.save(cusEntity, operator, false);
            this.save(cusEntity);
            return true;
        }
        return false;
    }*/
    
    /**
     * 變更信用額度 -- 編輯異動信用額度的數字
     * 
     * @param order
     * @param locale
     * @param errors
     * @return 
     */
    public boolean changeCreditsByOrder(EcOrder order, Locale locale, List<String> errors, boolean subtract, BigDecimal changeAmt, CreditLogEnum typeEnum){
        EcMember operator = order.getMember();
        Long storeId = order.getStore().getId();
        String reason = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "notify.msg.014"), order.getOrderNumber());
        EcCustomer cusEntity = this.findByApplied(operator, storeId);
        if( cusEntity==null || cusEntity.getApplyTime()==null 
                || cusEntity.getCredits()==null ){
            logger.error("setNewCredits cusEntity==null || cusEntity.getApplyTime()==null || cusEntity.getCredits()==null ");
            errors.add(ResourceBundleUtils.getMessage(locale, "cus.noapply.credits"));// 客戶未申請信用額度!
            return false;
        }
        if( subtract && BigDecimal.ZERO.equals(cusEntity.getCredits()) 
                && CreditLogEnum.ORDER_PRE_PAY.equals(typeEnum)){//預付時才檢查額度是否足夠
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
//        entity.setMemberId(cusEntity.getMemberId());
        entity.setMemberId(operator.getId());
        entity.setStoreId(storeId);
        entity.setReason(reason);
        entity.setOrderId(order.getId());
        
        if( creditsLogFacade.checkInput(entity, operator, locale, errors) ){
            // 先寫 LOG
            creditsLogFacade.save(entity, operator, false);
            logger.info("setNewCredits save EcCreditsLog");
            // 寫 EcCustomer
            cusEntity.setCredits(credits);
//            cusEntity.setCreditsCur(creditsCur);
//            cusEntity.setCreditsTime(new Date());
//            cusEntity.setCreditsUser(operator.getId());
//            this.save(cusEntity, operator, false);
            this.save(cusEntity);
            return true;
        }
        return false;
    }
    
    /**
     * 有已核准交易的會員，加入商店客戶關聯主檔
     * @param storeId
     * @return 
     */
    public int addCustomerByOrder(Long storeId){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");

        // 建立者為會員本身
        // 建立時間第一次PO核准時間
        sql.append("INSERT INTO EC_CUSTOMER (ID, MEMBER_ID, CREATOR, CREATETIME, STORE_ID, CUS_TYPE) \n");
        sql.append("SELECT SEQ_CUSTOMER.NEXTVAL, M.ID MEMBER_ID, M.ID CREATOR, firstBuyDate, O.STORE_ID, '")
           .append(CustomerEnum.ORDER.getCode()).append("' \n");// 交易產生的客戶
        sql.append("FROM EC_MEMBER M \n");

        // 有已核准交易
        sql.append("JOIN ( \n");
        sql.append("    SELECT STORE_ID, MEMBER_ID, COUNT(ID) COUNTS, SUM(TOTAL) TOTAL_AMT \n");
        sql.append("    , MIN(APPROVETIME) firstBuyDate, MAX(APPROVETIME) lastBuyDate \n"); 
        sql.append("    FROM EC_ORDER \n"); 
        sql.append("    WHERE 1=1 \n");
        sql.append("    AND STATUS IN ('")
           .append(OrderStatusEnum.Pending.getCode()).append("', '")// 待賣方確認             
           .append(OrderStatusEnum.Approve.getCode()).append("', '")// 賣家已核准
           .append(OrderStatusEnum.Close.getCode()).append("') \n");// 賣家結案
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
    public void addCustomerByOrder(EcMember member, Long storeId){
        EcCustomer entity = this.findByMemberAndStore(member, storeId);
        if(entity!=null){
            return;
        }
        entity = new EcCustomer();
        entity.setMember(member);
        entity.setStoreId(storeId);
        entity.setCusType(CustomerEnum.ORDER.getCode());
        entity.setCreator(member);
        entity.setCreditsTime(new Date());
        this.save(entity);
    }
    
}
