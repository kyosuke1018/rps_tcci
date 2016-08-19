/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.order;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcOrder;
import com.tcci.tccstore.entity.EcOrderLog;
import com.tcci.tccstore.enums.OrderStatusEnum;
import com.tcci.tccstore.facade.util.OrderFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@Named
@Stateless
// public class EcOrderFacade extends AbstractFacade {
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

    public EcOrder find(Long id) {
        return em.find(EcOrder.class, id);
    }

    public List<EcOrder> findOrderByCustomer(String customerCode) {
        OrderFilter filter = new OrderFilter();
        filter.setCustomerCode(customerCode);
        List<OrderStatusEnum> statusList = new ArrayList();
        statusList.add(OrderStatusEnum.OPEN);
        statusList.add(OrderStatusEnum.APPROVE);
        statusList.add(OrderStatusEnum.FAIL);
        filter.setStatusList(statusList);
        return findByCriteria(filter);
    }
    
    public List<EcOrder> findOrderByStatus(OrderStatusEnum status) {
        Query q = em.createNamedQuery("EcOrder.findByStatus");
        q.setParameter("status", status);
        return q.getResultList();
    }
    
    public void cancelExpiredOrder() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date today = new Date();
        String deliveryDate = sdf.format(today);
        String sql = "SELECT e FROM EcOrder e WHERE e.status=:status AND e.deliveryDate<:deliveryDate";
        Query q= em.createQuery(sql);
        q.setParameter("status", OrderStatusEnum.OPEN);
        q.setParameter("deliveryDate", deliveryDate);
        List<EcOrder> orders = q.getResultList();
        for (EcOrder order : orders) {
            order.setStatus(OrderStatusEnum.CANCEL);
            em.merge(order);
            addLog(order, OrderStatusEnum.CANCEL.name(), (TcUser) null, null);
            logger.debug("order expired, id={}", order.getId());
            // 有需要通知嗎?
        }
    }
    
    // 最近7天未closed的訂單
    public List<EcOrder> findNotClosedOrder(EcCustomer ecCustomer) {
        String sql = "SELECT e FROM EcOrder e"
                + " WHERE e.customerId=:ecCustomer"
                + " AND e.status<>:status"
                + " AND e.createtime>=:createtime"
                + " ORDER BY e.createtime DESC, e.createtime DESC";
        Query q = em.createQuery(sql);
        Calendar createtime = Calendar.getInstance();
        createtime.add(Calendar.DATE, -6); // 含今天共7天
        q.setParameter("ecCustomer", ecCustomer);
        q.setParameter("createtime", createtime, TemporalType.DATE);
        q.setParameter("status", OrderStatusEnum.CLOSE);
        return q.getResultList();
    }
    
    public void edit(EcOrder ecOrder) {
        em.merge(ecOrder);
    }

    public EcOrder editThenReturn(EcOrder ecOrder) {
        return em.merge(ecOrder);
    }

    public EcOrder createThenReturn(EcOrder ecOrder, OrderStatusEnum status) {
        if (ecOrder.getContractId() != null) {
            ecOrder.setContractCode(ecOrder.getContractId().getCode());
            ecOrder.setContractName(ecOrder.getContractId().getName());
        }
        if (ecOrder.getProductId() != null) {
            ecOrder.setProductCode(ecOrder.getProductId().getCode());
            ecOrder.setProductName(ecOrder.getProductId().getName());
        }
        if (ecOrder.getPlantId() != null) {
            ecOrder.setPlantCode(ecOrder.getPlantId().getCode());
            ecOrder.setPlantName(ecOrder.getPlantId().getName());
        }
        if (ecOrder.getSalesareaId() != null) {
            ecOrder.setSalesareaCode(ecOrder.getSalesareaId().getCode());
            ecOrder.setSalesareaName(ecOrder.getSalesareaId().getName());
        }
        if (ecOrder.getDeliveryId() != null) {
            ecOrder.setDeliveryCode(ecOrder.getDeliveryId().getCode());
            ecOrder.setDeliveryName(ecOrder.getDeliveryId().getName());
        }
        if (ecOrder.getSalesId() != null) {
            ecOrder.setSalesCode(ecOrder.getSalesId().getCode());
            ecOrder.setSalesName(ecOrder.getSalesId().getName());
        }
        ecOrder.setStatus(status);

        em.persist(ecOrder);
        return em.merge(ecOrder);
    }

    public List<EcOrder> findByCriteria(OrderFilter filter) {
        List<EcOrder> result = null;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<EcOrder> cq = cb.createQuery(EcOrder.class);
        Root<EcOrder> root = cq.from(EcOrder.class);
        cq.select(root);

        List<Predicate> predicateList = new ArrayList();
        if (StringUtils.isNotEmpty(filter.getCustomerCode())) {
            predicateList.add(cb.equal(root.get("customerId").get("code"), filter.getCustomerCode()));
        }

        if (filter.getStatus() != null) {
            predicateList.add(cb.equal(root.get("status").as(OrderStatusEnum.class), filter.getStatus()));
        }

        if (StringUtils.isNotEmpty(filter.getYearMonth())) {
            predicateList.add(cb.equal(cb.function("TO_CHAR", String.class, root.get("createtime")), filter.getYearMonth()));
        }

        if (filter.getCreatetimeBegin() != null) {
            predicateList.add(cb.greaterThanOrEqualTo(root.get("createtime").as(Date.class), filter.getCreatetimeBegin()));
        }

        if (filter.getId() != null) {
            predicateList.add(cb.equal(root.get("id").as(Long.class), filter.getId()));
        }

        if (StringUtils.isNotEmpty(filter.getSapOrdernum())) {
            predicateList.add(cb.equal(root.get("sapOrdernum"), filter.getSapOrdernum()));
        }

        if (filter.getCreatetimeEnd() != null) {
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(filter.getCreatetimeEnd());
            endDate.add(Calendar.DATE, 1);
            predicateList.add(cb.lessThan(root.get("createtime").as(Date.class), endDate.getTime()));
        }

        if (filter.getStatusList() != null && !filter.getStatusList().isEmpty()) {
            predicateList.add(root.get("status").as(OrderStatusEnum.class).in(filter.getStatusList()));
        }

        if (StringUtils.isNotEmpty(filter.getPlant())) {
            predicateList.add(cb.like(root.get("plantId").get("code").as(String.class), filter.getPlant().concat("%")));
        }
        
        if (StringUtils.isNotEmpty(filter.getVehicle())) {
            predicateList.add(cb.like(root.get("vehicle").as(String.class), "%".concat(filter.getVehicle()).concat("%")));
        }
        
        if (filter.getPlantList() != null) {
            Predicate[] ps = new Predicate[filter.getPlantList().size()];
            int i = 0;
            for (String plantCode : filter.getPlantList()) {
                ps[i] = cb.like(root.get("plantId").get("code").as(String.class), plantCode.concat("%"));
                i++;
            }
            predicateList.add(cb.or(ps));
        }
        
        // 不顯示c1開的訂單
        if (filter.isExcludeC1()) {
            predicateList.add(cb.notEqual(root.get("memberId").get("loginAccount"), "c1"));
        }

        if (filter.getDeliveryDateBegin() != null) {
            predicateList.add(cb.greaterThanOrEqualTo(root.get("deliveryDate").as(String.class), filter.getDeliveryDateBegin()));
        }
        if (filter.getDeliveryDateEnd() != null) {
            predicateList.add(cb.lessThanOrEqualTo(root.get("deliveryDate").as(String.class), filter.getDeliveryDateEnd()));
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

    public boolean canApprove(EcOrder ecOrder) {
        // 狀態變了就不允許審核
        OrderStatusEnum origStatus = ecOrder.getStatus();
        EcOrder order = em.find(EcOrder.class, ecOrder.getId());
        return (origStatus==order.getStatus() && (OrderStatusEnum.OPEN==order.getStatus() || OrderStatusEnum.OPEN_AUTO==order.getStatus()));
    }
    
    public boolean canCancel(EcOrder ecOrder) {
        // 狀態變了就不允許審核
        OrderStatusEnum origStatus = ecOrder.getStatus();
        EcOrder order = em.find(EcOrder.class, ecOrder.getId());
        return (origStatus==order.getStatus() && !(OrderStatusEnum.CANCEL==order.getStatus() || 
                OrderStatusEnum.CLOSE==order.getStatus()));
    }
    
    public List<TcUser> findApprover(EcOrder ecOrder) {
        String groupCode = ecOrder.getPlantCode().substring(0, 2) + "00";
        return findApproverByGroupCode(groupCode);
    }
    
    public List<TcUser> findApproverByGroupCode(String groupCode) {
        // 符合 ORDER_APPROVER 及 廠 才是訂單審核人
        String exists1 = "SELECT 1 FROM TC_USERGROUP UG, TC_GROUP G WHERE UG.USER_ID=U.ID AND UG.GROUP_ID=G.ID AND G.CODE='ORDER_APPROVER'";
        String exists2 = "SELECT 1 FROM TC_USERGROUP UG, TC_GROUP G WHERE UG.USER_ID=U.ID AND UG.GROUP_ID=G.ID AND G.CODE=#groupCode";
        String sql = "SELECT * FROM TC_USER U WHERE U.DISABLED=0 AND EXISTS (" + exists1 + ") AND EXISTS (" + exists2 +")";
        Query q = em.createNativeQuery(sql, TcUser.class);
        q.setParameter("groupCode", groupCode);
        return q.getResultList();
    }
    
    public void addLog(EcOrder ecOrder, String eventType, EcMember operator, String message) {
        EcOrderLog orderLog = new EcOrderLog(ecOrder, eventType, operator, message);
        em.persist(orderLog);
    }
    
    public void addLog(EcOrder ecOrder, String eventType, TcUser operator, String message) {
        EcOrderLog orderLog = new EcOrderLog(ecOrder, eventType, operator, message);
        em.persist(orderLog);
    }

    public List<EcOrderLog> findLog(EcOrder ecOrder) {
        Query q = em.createNamedQuery("EcOrderLog.findByOrder");
        q.setParameter("ecOrder", ecOrder);
        return q.getResultList();
    }

}
