/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.order;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOrder;
import com.tcci.ec.entity.EcOrderLog;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.PayStatusEnum;
import com.tcci.ec.enums.ShipStatusEnum;
import com.tcci.ec.facade.util.OrderFilter;
import com.tcci.fc.entity.org.TcUser;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kyle.cheng
 */
@Stateless
public class EcOrderFacade {
    private final static Logger logger = LoggerFactory.getLogger(EcOrderFacade.class);

    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    // @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcOrderFacade() {
        //super(EcOrder.class);
    }
    
    public EcOrder save(EcOrder entity) {
        entity.setModifytime(new Date());
        if (entity.getId() == null) {
            entity.setCreatetime(new Date());
            em.persist(entity);
        } else {
            em.merge(entity);
        }
        return entity;
    }
    
    // 前台新增訂單, 可以是OPEN(待審核), APPROVE(自動核准)
    public EcOrder createOrder(EcOrder ecOrder, String noticeFormat) {
//        if (OrderStatusEnum.APPROVE == ecOrder.getStatus()) {
//            ecOrder.setMessage("等候SAP建立订单");
//        }
        String msg = "";
        if(ecOrder.getStatus()!=null){
            if(ecOrder.getStatus().equals(OrderStatusEnum.Pending)){
                msg = "訂單已送出!";
            }else if(ecOrder.getStatus().equals(OrderStatusEnum.Inquiry)){
                msg = "詢價已送出!";
            }
        }
        
//        em.persist(ecOrder);
        ecOrder = this.save(ecOrder);
        
        addLog(ecOrder, ecOrder.getStatus().name(), ecOrder.getCreator(), msg);
        String notice = MessageFormat.format(noticeFormat,
                            new Object[]{String.valueOf(ecOrder.getId()), ecOrder.getOrderNumber()});
        logger.debug("createOrder notice:"+notice);
//        addNotice(ecOrder, NotifyTypeEnum.ORDER_CREATE, notice);
        return ecOrder;
    }
    
    // 前台取消訂單
    public EcOrder cancelByMember(EcOrder ecOrder, String orderNotice) {
//        ecOrder.setStatus(OrderStatusEnum.CANCEL);
        String msg = "使用者取消";
        ecOrder.setStatus(OrderStatusEnum.Cancelled);
        ecOrder.setModifier(ecOrder.getMember());
        ecOrder.setModifytime(new Date());
        em.merge(ecOrder);
        addLog(ecOrder, ecOrder.getStatus().name(), ecOrder.getMember(), msg);
//        addNotice(ecOrder, NotifyTypeEnum.ORDER_CANCEL, orderNotice);
        return ecOrder;
    }
    
    // 後台取消訂單
    public void cancelByUser(EcOrder ecOrder, String orderNotice, TcUser byUser, String comment) {
//        ecOrder.setStatus(OrderStatusEnum.CANCEL);
        ecOrder.setStatus(OrderStatusEnum.Cancelled);
//        ecOrder.setModifier(byUser);
        ecOrder.setModifytime(new Date());
        em.merge(ecOrder);
        addLog(ecOrder, ecOrder.getStatus().name(), byUser, comment);
//        addNotice(ecOrder, NotifyTypeEnum.ORDER_CANCEL, orderNotice);
    }
    
    // 後台核准訂單
    public void approveByUser(EcOrder ecOrder, TcUser byUser) {
//        ecOrder.setStatus(OrderStatusEnum.APPROVE);
//        ecOrder.setApprover(byUser);
//        ecOrder.setApprovalTime(new Date());
        String msg = "賣家確認訂單";
        ecOrder.setStatus(OrderStatusEnum.Approve);
        ecOrder.setModifier(ecOrder.getStore().getSeller().getMember());
        ecOrder.setModifytime(new Date());
        em.merge(ecOrder);
//        addLog(ecOrder, ecOrder.getStatus().name(), byUser, ecOrder.getMessage());
        addLog(ecOrder, ecOrder.getStatus().name(), ecOrder.getStore().getSeller().getMember(), msg);
        // 核准時不發通知, SAP開單成功or失敗時才發通知
    }
    
    public void changeStatus(EcOrder ecOrder, OrderStatusEnum status, String msg, EcMember operator) {
        ecOrder.setStatus(status);
        ecOrder.setModifier(operator);
        ecOrder.setModifytime(new Date());
        em.merge(ecOrder);
//        addLog(ecOrder, ecOrder.getStatus().name(), byUser, ecOrder.getMessage());
        addLog(ecOrder, status.name(), operator, msg);
        // 核准時不發通知, SAP開單成功or失敗時才發通知
    }
    
    public void changeStatus(EcOrder ecOrder, PayStatusEnum status, String msg, EcMember operator) {
        ecOrder.setPayStatus(status);
        ecOrder.setModifier(operator);
        ecOrder.setModifytime(new Date());
        em.merge(ecOrder);
//        addLog(ecOrder, ecOrder.getStatus().name(), byUser, ecOrder.getMessage());
        addLog(ecOrder, status.name(), operator, msg);
        // 核准時不發通知, SAP開單成功or失敗時才發通知
    }
    
    public void changeStatus(EcOrder ecOrder, ShipStatusEnum status, String msg, EcMember operator) {
        ecOrder.setShipStatus(status);
        ecOrder.setModifier(operator);
        ecOrder.setModifytime(new Date());
        em.merge(ecOrder);
//        addLog(ecOrder, ecOrder.getStatus().name(), byUser, ecOrder.getMessage());
        addLog(ecOrder, status.name(), operator, msg);
        // 核准時不發通知, SAP開單成功or失敗時才發通知
    }
    
    public void addLog(EcOrder ecOrder, String eventType, EcMember operator, String message) {
        EcOrderLog orderLog = new EcOrderLog(ecOrder, eventType, operator, message);
        //
        orderLog.setStore(ecOrder.getStore());
        orderLog.setBuyer(ecOrder.getMember().equals(operator));
        orderLog.setTotal(ecOrder.getTotal());
        orderLog.setShippingTotal(ecOrder.getShippingTotal());
        
        em.persist(orderLog);
    }
    
    public void addLog(EcOrder ecOrder, String eventType, TcUser operator, String message) {
        EcOrderLog orderLog = new EcOrderLog(ecOrder, eventType, operator, message);
        em.persist(orderLog);
    }
    
//    private void addNotice(EcOrder ecOrder, NotifyTypeEnum type, String message) {
//        Date now = new Date();
//        long diff = now.getTime() - ecOrder.getCreatetime().getTime();
//        // 超過7天的訂單不再發通知
//        if (diff < 1000*60*60*24*7L) {
//            notifyFacade.createNotify(type, message, ecOrder, ecOrder.getMemberId());
//        }
//    }
    
    public EcOrder find(Long id) {
        return em.find(EcOrder.class, id);
    }
    
    public List<EcOrder> findOrderByCustomer(EcMember member) {
        OrderFilter filter = new OrderFilter();
        filter.setMemberId(member.getId());
//        List<OrderStatusEnum> statusList = new ArrayList();
//        statusList.add(OrderStatusEnum.OPEN);
//        statusList.add(OrderStatusEnum.APPROVE);
//        statusList.add(OrderStatusEnum.FAIL);
//        filter.setStatusList(statusList);
        return findByCriteria(filter);
    }
    
    public List<EcOrder> findOrderByStore(EcStore store) {
        OrderFilter filter = new OrderFilter();
        filter.setStoreId(store.getId());
        return findByCriteria(filter);
    }
    
    public List<EcOrder> findByCriteria(OrderFilter filter) {
        List<EcOrder> result = null;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<EcOrder> cq = cb.createQuery(EcOrder.class);
        Root<EcOrder> root = cq.from(EcOrder.class);
        cq.select(root);

        List<Predicate> predicateList = new ArrayList();
        if (filter.getStatus() != null) {
            predicateList.add(cb.equal(root.get("status").as(OrderStatusEnum.class), filter.getStatus()));
        }
        if (filter.getPayStatus() != null) {
            predicateList.add(cb.equal(root.get("payStatus").as(PayStatusEnum.class), filter.getPayStatus()));
        }
        if (filter.getShipStatus() != null) {
            predicateList.add(cb.equal(root.get("shipStatus").as(ShipStatusEnum.class), filter.getShipStatus()));
        }

//        if (StringUtils.isNotEmpty(filter.getYearMonth())) {
//            predicateList.add(cb.equal(cb.function("TO_CHAR", String.class, root.get("createtime")), filter.getYearMonth()));
//        }

        if (filter.getCreatetimeBegin() != null) {
//            predicateList.add(cb.greaterThanOrEqualTo(root.get("createtime").as(Date.class), filter.getCreatetimeBegin()));
            //jdk8 CriteriaBuilder, no suitable method found 
            //trick
            Predicate predicate = cb.greaterThanOrEqualTo(root.get("createtime").as(Date.class), filter.getCreatetimeBegin());
            predicateList.add(predicate);
        }

        if (filter.getId() != null) {
            predicateList.add(cb.equal(root.get("id").as(Long.class), filter.getId()));
        }
        
        if (filter.getMemberId() != null) {
            predicateList.add(cb.equal(root.get("member").get("id"), filter.getMemberId()));
        }
        
        if (filter.getStoreId() != null) {
            predicateList.add(cb.equal(root.get("store").get("id"), filter.getStoreId()));
        }

        if (filter.getCreatetimeEnd() != null) {
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(filter.getCreatetimeEnd());
            endDate.add(Calendar.DATE, 1);
            Predicate predicate = cb.lessThan(root.get("createtime").as(Date.class), endDate.getTime());
            predicateList.add(predicate);
        }

        if (filter.getStatusList() != null && !filter.getStatusList().isEmpty()) {
            predicateList.add(root.get("status").as(OrderStatusEnum.class).in(filter.getStatusList()));
        }
        if (filter.getPayStatusList() != null && !filter.getPayStatusList().isEmpty()) {
            predicateList.add(root.get("payStatus").as(PayStatusEnum.class).in(filter.getPayStatusList()));
        }
        if (filter.getShipStatusList() != null && !filter.getShipStatusList().isEmpty()) {
            predicateList.add(root.get("shipStatus").as(ShipStatusEnum.class).in(filter.getShipStatusList()));
        }

        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(cb.desc(root.get("createtime")));
        result = em.createQuery(cq).getResultList();
        return result;
    }
    
}
