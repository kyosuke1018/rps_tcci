package com.tcci.sksp.facade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.facade.schedule.TcScheduleFacade;
import com.tcci.sapproxy.PpProxy;
import com.tcci.sapproxy.dto.SapProxyResponseDto;
import com.tcci.sapproxy.jco.JcoUtils;
import com.tcci.sksp.controller.util.QuotationCondition;
import com.tcci.sksp.controller.util.SessionController;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkProductMaster;
import com.tcci.sksp.entity.quotation.SkQuotationDetail;
import com.tcci.sksp.entity.quotation.SkQuotationMaster;
import com.tcci.sksp.entity.enums.QuotationStatusEnum;
import com.tcci.sksp.entity.quotation.SkQuotationGift;
import com.tcci.sksp.entity.quotation.SkQuotationReviewHistory;
import com.tcci.sksp.event.quotation.SkQuotationMasterEvent;
import com.tcci.sksp.vo.SkQuotationDetailVO;
import com.tcci.sksp.vo.SkQuotationGiftVO;
import com.tcci.sksp.vo.SkQuotationMasterVO;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@Path("quotation")
@Stateless
public class SkQuotationFacade extends AbstractFacade<SkQuotationDetail> {

    Logger logger = LoggerFactory.getLogger(SkQuotationFacade.class);
    @Resource(mappedName = "jndi/sk.config")
    transient private Properties jndiConfig;
    ResourceBundle rb = ResourceBundle.getBundle("/messages");
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @Inject
    TcUserFacade userFacade;
    @Inject
    SkProductMasterFacade productFacade;
    @Inject
    SkCustomerFacade customerFacade;
    @EJB
    TcScheduleFacade scheduleFacade;
    @Inject
    Event<SkQuotationMasterEvent> event;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkQuotationFacade() {
        super(SkQuotationDetail.class);
    }

    @GET
    @Path("remove")
    @Produces("text/plain; charset=UTF-8;")
    public String remove(@Context HttpServletRequest request, @QueryParam("quotation_master") String jsonMaster) {
        Gson gson = new Gson();
        try {
            logger.debug("jsonMaster={}", jsonMaster);
            if (StringUtils.isNotEmpty(jsonMaster)) {
                SkQuotationMasterVO masterVO = gson.fromJson(jsonMaster, SkQuotationMasterVO.class);
                logger.debug("masterVO={}", masterVO.toStringVerbose());
                SkQuotationMaster master = convertVOToEntity(masterVO);
                master = restoreRelation(master);
                logger.debug("master={}", master.toStringVerbose());
                remove(master);
                return "0";

            } else {
                return "ERROR:" + rb.getString("quotation.emptyMasterForRemove");
            }

        } catch (Exception e) {
            return "ERROR:" + e.getLocalizedMessage();
        }

    }

    public void remove(SkQuotationMaster master) {
        em.remove(em.merge(master));
    }

    public void remove(SkQuotationGift gift) {
        em.remove(em.merge(gift));
    }

    @GET
    @Path("edit")
    @Produces("text/plain; charset=UTF-8;")
    public String edit(@Context HttpServletRequest request,
            @QueryParam("quotation_master") String jsonMaster) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            if (StringUtils.isNotEmpty(jsonMaster)) {
                logger.debug("jsonMaster={}", jsonMaster);
                SkQuotationMasterVO masterVO = gson.fromJson(jsonMaster, SkQuotationMasterVO.class);
                SkQuotationMaster master = convertVOToEntity(masterVO);
                master = restoreRelation(master);
                master = recalculate(master);
                master.setModifier(userFacade.findUserByLoginAccount(request.getUserPrincipal().getName()));
                Date now = new Date();
                master.setModifytimestamp(now);
                masterVO = convertEntityToVO(validateThenEditAndReturn(master));
                return gson.toJson(masterVO);
            } else {
                return "ERROR: " + rb.getString("quotation.emptyMasterForEdit");
            }
        } catch (Exception e) {
            logger.error("e={}", e);
            return "ERROR:" + e.getLocalizedMessage();
        }
    }

    private SkQuotationMaster recalculate(SkQuotationMaster master) {
        BigDecimal masterAmount = BigDecimal.ZERO;
        BigDecimal masterPremiumDiscount = BigDecimal.ZERO;

        if (null != master.getDetailCollection()) {
            for (SkQuotationDetail detail : master.getDetailCollection()) {
                masterPremiumDiscount = masterPremiumDiscount.add(detail.getPremiumDiscount());
                masterAmount = masterAmount.add(detail.getPrice().multiply(detail.getQuantity()));

            }
            master.setAmount(masterAmount);
            master.setPremiumDiscount(masterPremiumDiscount);
            master.setTotalAmount(masterAmount.add(masterPremiumDiscount.negate()));
        }
        return master;
    }

    public SkQuotationMaster edit(SkQuotationMaster master) {
        SkQuotationMasterEvent quotationEvent = new SkQuotationMasterEvent();
        quotationEvent.setQuotationMaster(master);
        quotationEvent.setAction(SkQuotationMasterEvent.EDIT_EVENT);
        event.fire(quotationEvent);
        return em.merge(master);
    }

    @GET
    @Path("create")
    @Produces("text/plain; charset=UTF-8;")
    public String createThenReturn(@Context HttpServletRequest request, @QueryParam("quotation_master") String jsonMaster) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            logger.debug("jsonMaster={}", jsonMaster);
            if (StringUtils.isNotEmpty(jsonMaster)) {
                SkQuotationMasterVO masterVO = gson.fromJson(jsonMaster, SkQuotationMasterVO.class);
                logger.debug("master={}", masterVO);
                SkQuotationMaster master = convertVOToEntity(masterVO);
                master = restoreRelation(master);
                master = recalculate(master);
                Date now = new Date();
                master.setQuotationDate(now);
                master.setCreator(userFacade.findUserByLoginAccount(request.getUserPrincipal().getName()));
                master.setCreatetimestamp(now);
                master.setModifier(userFacade.findUserByLoginAccount(request.getUserPrincipal().getName()));
                master.setModifytimestamp(now);
                logger.debug("master={}", master.toStringVerbose());
                masterVO = convertEntityToVO(validateThenCreateAndReturn(master));
                return gson.toJson(masterVO);

            } else {
                return "ERROR:" + rb.getString("quotation.emptyMasterForCreate");
            }

        } catch (Exception e) {
            logger.error("e={}", e);
            return "ERROR:" + e.getLocalizedMessage();
        }

    }

    private SkQuotationMaster restoreRelation(SkQuotationMaster master) {
        if (master.getDetailCollection() != null) {
            for (SkQuotationDetail detail : master.getDetailCollection()) {
                detail.setQuotationMaster(master);
                if (detail.getGiftList() != null) {
                    for (SkQuotationGift gift : detail.getGiftList()) {
                        gift.setQuotationDetail(detail);
                    }
                }
            }
        }
        if (master.getReviewHistoryCollection() != null) {
            for (SkQuotationReviewHistory reviewHistory : master.getReviewHistoryCollection()) {
                reviewHistory.setQuotationMaster(master);
            }
        }
        return master;
    }

    public SkQuotationMaster findMaster(Long id) {
        return em.find(SkQuotationMaster.class, id);
    }

    public SkQuotationMaster createThenReturn(SkQuotationMaster master) {
        em.persist(master);
        master = em.merge(master);
        SkQuotationMasterEvent createEvent = new SkQuotationMasterEvent();
        createEvent.setAction(SkQuotationMasterEvent.CREATE_EVENT);
        createEvent.setQuotationMaster(master);
        event.fire(createEvent);
        return master;
    }

    public SkQuotationMaster editThenReturn(SkQuotationMaster master) {

        SkQuotationMaster original = em.find(SkQuotationMaster.class, master.getId());
        List<SkQuotationDetail> removedDetailList = new ArrayList();
        for (SkQuotationDetail originalDetail : original.getDetailCollection()) {
            List<SkQuotationGift> newGiftList = null;
            for (SkQuotationDetail newDetail : master.getDetailCollection()) {
                if (originalDetail.equals(newDetail)) {
                    newGiftList = newDetail.getGiftList();
                }
            }
            if (newGiftList != null) {
                for (SkQuotationGift newGift : newGiftList) {
                    logger.debug("newGift.getId()={}", newGift.getId());
                }
            }
            List<SkQuotationGift> removedGiftList = new ArrayList();
            for (SkQuotationGift originalGift : originalDetail.getGiftList()) {
                logger.debug("originalGift.getId()={}", originalGift.getId());
                boolean removeGift = true;
                if (newGiftList != null) {
                    removeGift = !newGiftList.contains(originalGift);
                }
                logger.debug("removeGift={}", removeGift);
                if (removeGift) {
                    removedGiftList.add(originalGift);
                    em.remove(em.merge(originalGift));
                }
            }
            originalDetail.getGiftList().removeAll(removedGiftList);
            if (!master.getDetailCollection().contains(originalDetail)) {
                removedDetailList.add(originalDetail);
                em.remove(em.merge(originalDetail));
            }
        }
        master.getDetailCollection().removeAll(removedDetailList);
        List<SkQuotationReviewHistory> removedReviewHistoryList = new ArrayList();
        if (master.getReviewHistoryCollection() != null) {
            for (SkQuotationReviewHistory reviewHistory : original.getReviewHistoryCollection()) {
                if (!master.getReviewHistoryCollection().contains(reviewHistory)) {
                    removedReviewHistoryList.add(reviewHistory);
                    em.remove(em.merge(reviewHistory));
                }
            }
            master.getReviewHistoryCollection().removeAll(removedReviewHistoryList);
        }
        return edit(master);
    }

    public List<SkQuotationMaster> findAllMaster() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(SkQuotationMaster.class));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @GET
    @Path("findbycriteria")
    @Produces("text/plain; charset=UTF-8;")
    public String findByCriteria(@Context HttpServletRequest request,
            @QueryParam("condition") String jsonCondition,
            @QueryParam("index") int index,
            @QueryParam("limit") int limit) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        QuotationCondition condition = new QuotationCondition();
        logger.debug("jsonCondition={}", jsonCondition);
        if (StringUtils.isNotEmpty(jsonCondition)) {
            condition = gson.fromJson(jsonCondition, QuotationCondition.class);
        }
        logger.debug("condition={}", condition);
        logger.debug("condition.getMaster()={}", condition.getMaster());
        if (limit == 0) {
            return "ERROR:limit is required!";
        } else if (limit > 100) {
            return "ERROR:limit only allow 1 to 100!";
        }
        List<SkQuotationMaster> masterList = findByCriteria(condition, index, limit);
        List<SkQuotationMasterVO> masterVOList = new ArrayList();
        for (SkQuotationMaster master : masterList) {
            masterVOList.add(convertEntityToVO(master));
        }
        return gson.toJson(masterVOList);
    }

    private SkQuotationMasterVO convertEntityToVO(SkQuotationMaster master) {
        SkQuotationMasterVO masterVO = new SkQuotationMasterVO(master);
        if (null != master.getDetailCollection()) {
            for (SkQuotationDetail detail : master.getDetailCollection()) {
                SkQuotationDetailVO detailVO = new SkQuotationDetailVO(detail);
                if (StringUtils.isNotEmpty(detailVO.getProductNumber())) {
                    SkProductMaster product = productFacade.findByCode(detailVO.getProductNumber());
                    if (null != product) {
                        detailVO.setProductName(product.getName());
                    }
                }
                masterVO.getDetailCollection().add(detailVO);
                if (null != detail.getGiftList()) {
                    for (SkQuotationGift gift : detail.getGiftList()) {
                        SkQuotationGiftVO giftVO = new SkQuotationGiftVO(gift);
                        if (StringUtils.isNotEmpty(giftVO.getProductNumber())) {
                            SkProductMaster product = productFacade.findByCode(giftVO.getProductNumber());
                            if (null != product) {
                                giftVO.setProductName(product.getName());
                            }
                        }
                        detailVO.getGiftList().add(giftVO);
                    }
                }
            }
        }

        masterVO.setReviewHistoryCollection(new ArrayList());
        if (master.getReviewHistoryCollection() != null) {
            masterVO.getReviewHistoryCollection().addAll(master.getReviewHistoryCollection());
        }

        return masterVO;
    }

    private SkQuotationMaster convertVOToEntity(SkQuotationMasterVO masterVO) {
        SkQuotationMaster master = null;
        //if edit, need restore by id first.
        if (masterVO.getId() != null) {
            SkQuotationMaster existsMaster = findMaster(masterVO.getId());
            removeDetailGiftReviewHistory(existsMaster);
            SkQuotationMaster.copyProperty(masterVO, existsMaster);
            existsMaster.setDetailCollection(new ArrayList());
            master = existsMaster;
        } else {
            master = new SkQuotationMaster(masterVO);
        }
        for (SkQuotationDetailVO detailVO : masterVO.getDetailCollection()) {
            SkQuotationDetail detail = new SkQuotationDetail(detailVO);
            for (SkQuotationGiftVO giftVO : detailVO.getGiftList()) {
                detail.getGiftList().add(new SkQuotationGift(giftVO));
            }
            master.getDetailCollection().add(detail);
        }
        master.setReviewHistoryCollection(new ArrayList());
        if (masterVO.getReviewHistoryCollection() != null) {
            logger.debug("masterVO.getReviewHistoryCollection().size()={}", masterVO.getReviewHistoryCollection().size());
            for (SkQuotationReviewHistory reviewHistory : masterVO.getReviewHistoryCollection()) {
                SkQuotationReviewHistory newReviewHistory = new SkQuotationReviewHistory();
                SkQuotationReviewHistory.copyProperty(reviewHistory, newReviewHistory);
                master.getReviewHistoryCollection().add(newReviewHistory);
            }
        }
        if (master.getReviewHistoryCollection() != null) {
            logger.debug("master.getReviewHistoryCollection().size()={}", master.getReviewHistoryCollection().size());
        }
        return master;
    }

    private void removeDetailGiftReviewHistory(SkQuotationMaster master) {
        for (SkQuotationDetail detail : master.getDetailCollection()) {
            for (SkQuotationGift gift : detail.getGiftList()) {
                em.remove(gift);
            }
            em.remove(detail);
        }
        for (SkQuotationReviewHistory reviewHistory : master.getReviewHistoryCollection()) {
            em.remove(reviewHistory);
        }
    }

    public SkQuotationGift findGift(Long id) {
        return em.find(SkQuotationGift.class, id);
    }

    public List<SkQuotationMaster> findByCriteria(QuotationCondition condition) {
        return findByCriteria(condition, 0, 0);
    }

    @GET
    @Path("findlatestquotation")
    @Produces("text/plain; charset=UTF-8;")
    public String findLatestQuotation(@QueryParam("customer") String jsonCustomer) {
        String jsonQuotation = "";
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        SkCustomer customer = gson.fromJson(jsonCustomer, SkCustomer.class);
        logger.debug(gson.toJson(customerFacade.findBySimpleCode("211611")));
        logger.debug("customer={}", customer);
        if (customer != null) {
            SkQuotationMaster quotation = findLatestQuotation(customer);
            jsonQuotation = gson.toJson(quotation);
        }
        return jsonQuotation;
    }

    public SkQuotationMaster findLatestQuotation(SkCustomer customer) {
        logger.debug("customer={}", customer);
        SkQuotationMaster latest = null;
        QuotationCondition condition = new QuotationCondition();
        condition.setCustomer(customer);
        List<SkQuotationMaster> quotationList = findByCriteria(condition);
        if (!quotationList.isEmpty()) {
            latest = quotationList.get(0);
        }
        return latest;
    }

    public List<SkQuotationMaster> findByCriteria(QuotationCondition condition, int index, int limit) {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SkQuotationDetail> cq = cb.createQuery(SkQuotationDetail.class);
        Root<SkQuotationDetail> root = cq.from(SkQuotationDetail.class);
        cq.select(root);

        List<Predicate> predicateList = new ArrayList<Predicate>();

        //區域 (銷售群組前兩碼)
        logger.debug("area={}", condition.getArea());
        if (StringUtils.isNotEmpty(condition.getArea())) {
            Predicate p = cb.like(root.get("quotationMaster").get("customer").get("sapid").as(String.class), condition.getArea() + "%");
            predicateList.add(p);
        }

        //銷售群組
        logger.debug("sapid={}", condition.getSapid());
        if (StringUtils.isNotEmpty(condition.getSapid())) {
            Predicate p = cb.equal(root.get("quotationMaster").get("customer").get("sapid"), condition.getSapid());
            predicateList.add(p);
        }

        //客戶
        logger.debug("customer={}", condition.getCustomer());
        if (condition.getCustomer() != null) {
            Predicate p = cb.equal(root.get("quotationMaster").get("customer").as(SkCustomer.class), condition.getCustomer());
            predicateList.add(p);
        }

        //報價單編號
        logger.debug("quotationNo={}", condition.getMaster().getQuotationNo());
        if (StringUtils.isNotEmpty(condition.getMaster().getQuotationNo())) {
            Predicate p = cb.equal(root.get("quotationMaster").get("quotationNo"), condition.getMaster().getQuotationNo());
            predicateList.add(p);
        }

        //表單編號
        logger.debug("id={}", condition.getMaster().getId());
        if (condition.getMaster().getId() != null) {
            Predicate p = cb.equal(root.get("quotationMaster").get("id").as(Long.class), condition.getMaster().getId());
            predicateList.add(p);
        }

        //報價單日期
        logger.debug("beginDate={},endDate={}", new Object[]{condition.getBeginDate(), condition.getEndDate()});
        if (condition.getBeginDate() != null && condition.getEndDate() != null) {
            Predicate p1 = cb.greaterThanOrEqualTo(root.get("quotationMaster").get("quotationDate").as(Date.class), condition.getBeginDate());
            Predicate p2 = cb.lessThanOrEqualTo(root.get("quotationMaster").get("quotationDate").as(Date.class), condition.getEndDate());
            Predicate p = cb.and(p1, p2);
            predicateList.add(p);
        } else if (condition.getBeginDate() != null && condition.getEndDate() == null) {
            Predicate p = cb.greaterThanOrEqualTo(root.get("quotationMaster").get("quotationDate").as(Date.class), condition.getBeginDate());
            predicateList.add(p);
        } else if (condition.getEndDate() != null && condition.getBeginDate() == null) {
            Predicate p = cb.lessThanOrEqualTo(root.get("quotationMaster").get("quotationDate").as(Date.class), condition.getEndDate());
            predicateList.add(p);
        }

        //零件編號
        logger.debug("productNumber={}", condition.getDetail().getProductNumber());
        if (StringUtils.isNotEmpty(condition.getDetail().getProductNumber())) {
            Predicate p = cb.equal(root.get("productNumber"), condition.getDetail().getProductNumber());
            predicateList.add(p);
        }

        //狀態
        logger.debug("status={}", condition.getMaster().getStatus());
        if (condition.getMaster().getStatus() != null) {
            Predicate p = cb.equal(root.get("quotationMaster").get("status").as(QuotationStatusEnum.class), condition.getMaster().getStatus());
            predicateList.add(p);
        }

        //可查詢的銷售群組
        if (condition.getSapidList() != null && !condition.getSapidList().isEmpty()) {
            Predicate p = root.get("quotationMaster").get("customer").get("sapid").as(String.class).in(condition.getSapidList());
            predicateList.add(p);
        }

        //狀態
        if (condition.getStatusList() != null && !condition.getStatusList().isEmpty()) {
            Predicate p = root.get("quotationMaster").get("status").in(condition.getStatusList());
            predicateList.add(p);
        }

        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }

        List<SkQuotationDetail> details = getEntityManager().createQuery(cq).getResultList();
        logger.debug("details.size()={}", details.size());
        Set<SkQuotationMaster> orderMasterSet = new HashSet();
        for (SkQuotationDetail detail : details) {
            orderMasterSet.add(detail.getQuotationMaster());
        }
        logger.debug("orderMasterSet.size()={}", orderMasterSet.size());
        List<SkQuotationMaster> masterList = new ArrayList();
        masterList.addAll(orderMasterSet);
        //修改排序時需注意findLatestQuotation()需要修改.
        Collections.sort(masterList, new Comparator<SkQuotationMaster>() {
            @Override
            public int compare(SkQuotationMaster m1, SkQuotationMaster m2) {
                return m2.getQuotationDate().compareTo(m1.getQuotationDate());
            }
        });
        List<SkQuotationMaster> sortedMasterList = new ArrayList();
        if (limit > 0) {
            int i = 0;
            for (SkQuotationMaster master : masterList) {
                if (i >= index && i <= index + limit) {
                    sortedMasterList.add(master);
                }
                i++;
            }
        } else {
            sortedMasterList.addAll(masterList);
        }
        return sortedMasterList;
    }

    public SkQuotationMaster validateThenCreateAndReturn(SkQuotationMaster master) throws Exception {
        List<String> errorMessageList = validate(master);
        if (errorMessageList.isEmpty()) {
            return createThenReturn(master);
        } else {
            StringBuilder sb = new StringBuilder();
            for (String errorMessage : errorMessageList) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(errorMessage);
            }
            throw new Exception(sb.toString());
        }
    }

    public SkQuotationMaster validateThenEditAndReturn(SkQuotationMaster master) throws Exception {
        List<String> errorMessageList = validate(master);
        if (errorMessageList.isEmpty()) {
            return editThenReturn(master);
        } else {
            StringBuilder sb = new StringBuilder();
            for (String errorMessage : errorMessageList) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(errorMessage);
            }
            throw new Exception(sb.toString());
        }
    }

    private List<String> validate(SkQuotationMaster master) {
        List<String> errorMessageList = new ArrayList();
        //check master
        //customer
        if (master.getCustomer() == null) {
            errorMessageList.add(rb.getString("quotation.customerIsRequired"));
        } else {
            //檢查客戶是否存在
            boolean customerExists = false;
            if (master.getCustomer().getId() != null) {
                if (customerFacade.find(master.getCustomer().getId()) != null) {
                    customerExists = true;
                }
            } else if (StringUtils.isNotEmpty(master.getCustomer().getCode())) {
                if (customerFacade.findByCode(master.getCustomer().getCode()) != null) {
                    customerExists = true;
                }
            }
            if (!customerExists) {
                errorMessageList.add(rb.getString("customer.msg.notexists"));
            }
        }

        //consignee
        if (master.getConsignee() == null) {
            errorMessageList.add(rb.getString("quotation.consigneeIsRequired"));
        } else {
            //檢查收貨人是否存在
            boolean consigneeExists = false;
            if (master.getConsignee().getId() != null) {
                if (customerFacade.find(master.getConsignee().getId()) != null) {
                    consigneeExists = true;
                }
            } else if (StringUtils.isNotEmpty(master.getConsignee().getCode())) {
                if (customerFacade.findByCode(master.getConsignee().getCode()) != null) {
                    consigneeExists = true;
                }
            }
            if (!consigneeExists) {
                errorMessageList.add(rb.getString("quotation.consigneenotexists"));
            }
        }

        //check detail
        if (master.getDetailCollection().isEmpty()) {
            errorMessageList.add(rb.getString("quotation.detailEmpty"));
        }
        //總筆數不能超過6筆.
        int count = master.getDetailCollection().size();
        for (SkQuotationDetail detail : master.getDetailCollection()) {
            count += detail.getGiftList().size();
        }
        if (count > 6) {
            errorMessageList.add(rb.getString("quotation.moreThen6Item") + "!");
        }

        int i = 1;
        int j = 1;
        boolean hasIce = false; //冰品
        boolean hasNotIce = false; //非冰品
        boolean hasPrescription = false; //處方
        boolean hasInstruction = false; //指示
        String iceProductList = "";
        for (SkQuotationDetail detail : master.getDetailCollection()) {
            //product number
            if (StringUtils.isEmpty(detail.getProductNumber())) {
                errorMessageList.add(MessageFormat.format(rb.getString("quotation.productNumberIsRequired"), new Object[]{i}));
            }

            //quantity
            if (detail.getQuantity() == null || BigDecimal.ZERO.compareTo(detail.getQuantity()) == 0) {
                errorMessageList.add(MessageFormat.format(rb.getString("quotation.quantityIsRequired"), new Object[]{i}));
            } else if (detail.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
                errorMessageList.add(MessageFormat.format(rb.getString("quotation.quantityLessThanZero"), new Object[]{i}));
            }

            //price
            //未輸入價格或價格小於0.
            if (detail.getPrice() == null || BigDecimal.ZERO.compareTo(detail.getPrice()) == 0) {
                errorMessageList.add(MessageFormat.format(rb.getString("quotation.priceIsRequired"), new Object[]{i}));
            } else if (detail.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                errorMessageList.add(MessageFormat.format(rb.getString("quotation.priceLessThanZero"), new Object[]{i}));
            }

            //premiumDiscount
            if (detail.getPremiumDiscount() != null) {
                if (detail.getPremiumDiscount().compareTo(BigDecimal.ZERO) < 0) {
                    errorMessageList.add(MessageFormat.format(rb.getString("quotation.premiumDiscountLessThanZero"), new Object[]{i}));
                }
            }

            //賣價小於溢價折讓
            if (detail.getPrice() != null && detail.getQuantity() != null && detail.getPremiumDiscount() != null) {
                if (detail.getPrice().multiply(detail.getQuantity()).compareTo(detail.getPremiumDiscount()) == -1) {
                    errorMessageList.add(MessageFormat.format(rb.getString("quotation.amountLessThanPremiumDiscount"), new Object[]{i}));
                }
            }
            SkProductMaster product = productFacade.findByCode(detail.getProductNumber());
            if (!hasIce) {
                hasIce = productFacade.isIce(product);
                if (hasIce) {
                    if (iceProductList.length() > 0) {
                        iceProductList += ", ";
                    }
                    iceProductList += product.getCode();
                }
            }
            if (!hasNotIce) {
                hasNotIce = !productFacade.isIce(product);
            }

            if (!hasPrescription) {
                hasPrescription = productFacade.isPrescription(product);
            }

            if (!hasInstruction) {
                hasInstruction = productFacade.isInstruction(product);
            }

            //檢查贈品
            for (SkQuotationGift gift : detail.getGiftList()) {
                logger.debug("gift.getProductNumber()={}", gift.getProductNumber());
                if (StringUtils.isEmpty(gift.getProductNumber())) {
                    errorMessageList.add(MessageFormat.format(rb.getString("quotation.giftProductNumberIsRequired"), new Object[]{j}));
                }

                //quantity
                logger.debug("gift.getQuantity()={}", gift.getQuantity());
                if (gift.getQuantity() == null || BigDecimal.ZERO.compareTo(gift.getQuantity()) == 0) {
                    logger.debug("zero");
                    errorMessageList.add(MessageFormat.format(rb.getString("quotation.giftQuantityIsRequired"), new Object[]{j}));
                } else if (gift.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
                    logger.debug("negative");
                    errorMessageList.add(MessageFormat.format(rb.getString("quotation.giftQuantityLessThanZero"), new Object[]{j}));
                }

                SkProductMaster giftProduct = productFacade.findByCode(gift.getProductNumber());
                if (!hasIce) {
                    hasIce = productFacade.isIce(giftProduct);
                    if (hasIce) {
                        if (iceProductList.length() > 0) {
                            iceProductList += ", ";
                        }
                        iceProductList += giftProduct.getCode();
                    }
                }
                if (!hasNotIce) {
                    hasNotIce = !productFacade.isIce(giftProduct);
                }

                if (!hasPrescription) {
                    hasPrescription = productFacade.isPrescription(giftProduct);
                }

                if (!hasInstruction) {
                    hasInstruction = productFacade.isInstruction(giftProduct);
                }
                j++;
            }
            i++;
        }

        //冰品不可與非冰品同一張報價單
        if (hasIce && hasNotIce) {
            errorMessageList.add(MessageFormat.format(rb.getString("quotation.iceMixNotIce"), iceProductList));
        }
        if (StringUtils.isNotEmpty(master.getCustomer().getVat()) && "04".equals(master.getCustomer().getKukla())) { //營登客戶: 有統一編號, kukla=='04'
            if (hasPrescription && hasInstruction) {
                errorMessageList.add(rb.getString("quotation.prescriptionMixInstruction"));
            }
//        } else { //非營登客戶: 沒有統一編號
//            if (hasInstruction) {
//                errorMessageList.add(rb.getString("quotation.notVatWithInstruction"));
//            }
        }
        return errorMessageList;
    }

    @Schedule(hour = "7", persistent = false)
    //@Schedule(hour = "17", minute = "41", persistent = false)
    public void notifyBatchCreateQuotationSchedule() {
        try {
            // 30分內不要再執行
            if (scheduleFacade.canExecute("BatchCreateQuotation", 30)) {
                logger.debug("notifyBatchCreateQuotationSchedule()");
                batchCreateSapQuotation();
            } else {
                logger.warn("notifyBatchCreateQuotationSchedule not execute");
            }
        } catch (Exception ex) {
            logger.error("notifyBatchCreateQuotationSchedule exception", ex);
        }
    }

    private void batchCreateSapQuotation() throws Exception {
        QuotationCondition condition = new QuotationCondition();
        List<QuotationStatusEnum> statusList = new ArrayList();
        statusList.add(QuotationStatusEnum.READED);
        statusList.add(QuotationStatusEnum.APPROVED);
        condition.setStatusList(statusList);
        for (SkQuotationMaster master : findByCriteria(condition)) {
            logger.debug("master={}", master.getId());
            master = createSapQuotation(master, userFacade.findUserByLoginAccount("administrator"));
            edit(master);
        };
    }

    public SkQuotationMaster createSapQuotation(SkQuotationMaster master, TcUser user) throws Exception {
//        Properties jcoProp = JcoUtils.getJCoProp(jndiConfig); //取得相關Jco連結參數
//        PpProxy ppProxy = PpProxyFactory.createProxy(jcoProp);//建立連線
        PpProxy ppProxy = JcoUtils.getSapProxy("sking", user.getLoginAccount());
        SapProxyResponseDto returnResult = ppProxy.createQuotation(master, (String) jndiConfig.get("sking.jco.client.client"));
        if (returnResult != null) {
            List<Map<String, Object>> results = returnResult.getResultAsMapList();
            if (results != null) {
                logger.debug("results.size()={}", results.size());
                Map<String, Object> result = results.get(0);
                //update vbeln (銷售文件號碼) when success, otherwise, update error message to errorMessage (錯誤訊息)欄位
                //<editor-fold defaultstate="collapsed" desc="debug message">
                String type = (String) result.get("TYPE");
                if (type.equals("S")) {
                    master.setStatus(QuotationStatusEnum.CLOSED);
                    master.setQuotationNo((String) result.get("VBELN"));
                    master.setErrorMessage("");
                } else {
                    master.setStatus(QuotationStatusEnum.FAILED);
                    master.setErrorMessage((String) result.get("MESSAGE"));
                }
            }
        }
        return master;
    }
}
