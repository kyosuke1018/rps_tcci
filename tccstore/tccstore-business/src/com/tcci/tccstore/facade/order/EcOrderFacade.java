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
import com.tcci.tccstore.enums.NotifyTypeEnum;
import com.tcci.tccstore.enums.OrderStatusEnum;
import com.tcci.tccstore.enums.RewardEventEnum;
import com.tcci.tccstore.enums.RewardTypeEnum;
import com.tcci.tccstore.facade.notify.EcNotifyFacade;
import com.tcci.tccstore.facade.reward.EcRewardFacade;
import com.tcci.tccstore.facade.util.OrderFilter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
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

    @Inject
    private EcNotifyFacade notifyFacade;
    @Inject
    private EcRewardFacade rewardFacade;
    
    // @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcOrderFacade() {
        //super(EcOrder.class);
    }

    // 前台新增訂單, 可以是OPEN(待審核), APPROVE(自動核准)
    public void createOrder(EcOrder ecOrder, String noticeFormat) {
        if (OrderStatusEnum.APPROVE == ecOrder.getStatus()) {
            ecOrder.setMessage("等候SAP建立订单");
        }
        em.persist(ecOrder);
        addLog(ecOrder, ecOrder.getStatus().name(), ecOrder.getMemberId(), ecOrder.getMessage());
        String notice = MessageFormat.format(noticeFormat,
                            new Object[]{String.valueOf(ecOrder.getId()), ecOrder.getQuantity(), ecOrder.getVehicle()});
        addNotice(ecOrder, NotifyTypeEnum.ORDER_CREATE, notice);
    }
    
    // 前台取消訂單
    public void cancelByMember(EcOrder ecOrder, String orderNotice) {
        ecOrder.setStatus(OrderStatusEnum.CANCEL);
        ecOrder.setMessage("使用者取消");
        em.merge(ecOrder);
        addLog(ecOrder, ecOrder.getStatus().name(), ecOrder.getMemberId(), null);
        addNotice(ecOrder, NotifyTypeEnum.ORDER_CANCEL, orderNotice);
    }
    
    // 後台取消訂單
    public void cancelByUser(EcOrder ecOrder, String orderNotice, TcUser byUser, String comment) {
        ecOrder.setStatus(OrderStatusEnum.CANCEL);
        ecOrder.setApprover(byUser);
        ecOrder.setApprovalTime(new Date());
        ecOrder.setMessage(comment);
        em.merge(ecOrder);
        addLog(ecOrder, ecOrder.getStatus().name(), byUser, comment);
        addNotice(ecOrder, NotifyTypeEnum.ORDER_CANCEL, orderNotice);
    }
    
    // 後台核准訂單
    public void approveByUser(EcOrder ecOrder, TcUser byUser) {
        ecOrder.setStatus(OrderStatusEnum.APPROVE);
        ecOrder.setApprover(byUser);
        ecOrder.setApprovalTime(new Date());
        ecOrder.setMessage("等候SAP建立订单");
        em.merge(ecOrder);
        addLog(ecOrder, ecOrder.getStatus().name(), byUser, ecOrder.getMessage());
        // 核准時不發通知, SAP開單成功or失敗時才發通知
    }
    
    // SAP開單成功
    public void sapCreateSuccess(EcOrder ecOrder, String sapOrdernum) {
        ecOrder.setSapOrdernum(sapOrdernum);
        ecOrder.setMessage("SAP建立订单完成");
        em.merge(ecOrder);
        addLog(ecOrder, "SAP_CREATE_SUCCESS", (TcUser) null, "SAP單號:" + sapOrdernum);
        String notice = MessageFormat.format("订单(序号:{0}, 数量:{1}吨, 车号:{2})已成立, 正式订单号码为{3}!",
                new Object[]{ecOrder.getId().toString(),
                    ecOrder.getQuantity(),
                    ecOrder.getVehicle(),
                    ecOrder.getSapOrdernum()});
        addNotice(ecOrder, NotifyTypeEnum.ORDER_CREATE, notice);
    }
    
    // SAP開單失敗
    public void sapCreateFail(EcOrder ecOrder, String message) {
        ecOrder.setStatus(OrderStatusEnum.FAIL);
        ecOrder.setMessage(message);
        em.merge(ecOrder);
        addLog(ecOrder, "SAP_CREATE_FAIL", (TcUser) null, message);
        String notice = MessageFormat.format("订单(序号:{0}, 数量:{1}吨, 车号:{2}), SAP订单产生失败(错误讯息:{3})!",
                new Object[]{ecOrder.getId().toString(),
                    ecOrder.getQuantity(),
                    ecOrder.getVehicle(),
                    message});
        addNotice(ecOrder, NotifyTypeEnum.ORDER_APPROVE_FAIL, notice);
    }
    
    // SAP取消成功
    public void sapCancelSuccess(EcOrder ecOrder, String sapOrdernum) {
        addLog(ecOrder, "SAP_CANCEL_SUCCESS", (TcUser) null, "SAP單號:" + sapOrdernum);
    }
    
    // SAP取消失敗
    public void sapCancelFail(EcOrder ecOrder, String message) {
        ecOrder.setStatus(OrderStatusEnum.FAIL);
        ecOrder.setMessage(message);
        em.merge(ecOrder);
        addLog(ecOrder, "SAP_CANCEL_FAIL", (TcUser) null, message);
        String notice = MessageFormat.format("订单(序号:{0}, 数量:{1}吨, 车号:{2}), SAP订单取消失败(错误讯息:{3})!",
                new Object[]{ecOrder.getId().toString(),
                    ecOrder.getQuantity(),
                    ecOrder.getVehicle(),
                    message});
        addNotice(ecOrder, NotifyTypeEnum.ORDER_CANCEL_FAIL, notice);
    }

    // 找出已核准且SAP訂單已完成
    public List<EcOrder> findSapCreated() {
        Query q = em.createNamedQuery("EcOrder.findSapCreated");
        q.setParameter("status", OrderStatusEnum.APPROVE);
        return q.getResultList();
    }
    
    // 訂單已出貨
    public void closeOrder(EcOrder ecOrder) {
        ecOrder.setStatus(OrderStatusEnum.CLOSE);
        ecOrder.setMessage("订单已出货");
        em.merge(ecOrder);
        addLog(ecOrder, ecOrder.getStatus().name(), (TcUser) null, ecOrder.getMessage());
        String notice = MessageFormat.format("订单{0}(序号: {1}, 数量: {2} 吨, 车号: {3}) 已出货!",
                        new Object[]{ ecOrder.getSapOrdernum(), 
                            String.valueOf(ecOrder.getId()), 
                            ecOrder.getQuantity(), 
                            ecOrder.getVehicle()});
        addNotice(ecOrder, NotifyTypeEnum.ORDER_CLOSE, notice);
        // 訂單紅利
        String eventDetail = "訂單(" + ecOrder.getSapOrdernum() + ")紅利";
        rewardFacade.awardPoints(ecOrder.getMemberId(), RewardTypeEnum.BONUS, 1, RewardEventEnum.ORDER_CLOSE, eventDetail, null, ecOrder);
        // logger.warn("add 1 bonus point, member id:{}", ecOrder.getMemberId().getId());
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
    
    public List<EcOrder> findExpiredOrder(OrderStatusEnum[] aryStatus) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date today = new Date();
        String deliveryDate = sdf.format(today);
        String sql = "SELECT e FROM EcOrder e WHERE e.status in :statusList AND e.deliveryDate<:deliveryDate";
        Query q= em.createQuery(sql);
        List<OrderStatusEnum> statusList = Arrays.asList(aryStatus);
        q.setParameter("statusList", statusList);
        q.setParameter("deliveryDate", deliveryDate);
        return q.getResultList();
    }
    
    public void cancelExpiredOrder(EcOrder ecOrder) {
        logger.debug("cancelExpiredOrder: order id:{}, original status:{}", ecOrder.getId(), ecOrder.getStatus());
        ecOrder.setStatus(OrderStatusEnum.CANCEL);
        ecOrder.setMessage("逾期取消");
        em.merge(ecOrder);
        addLog(ecOrder, OrderStatusEnum.CANCEL.name(), (TcUser) null, ecOrder.getMessage());
        String notice = MessageFormat.format("订单(序号:{0}, 数量:{1}吨, 车号:{2})逾期取消!",
                new Object[]{ecOrder.getId().toString(),
                    ecOrder.getQuantity(),
                    ecOrder.getVehicle()});
        addNotice(ecOrder, NotifyTypeEnum.ORDER_CANCEL, notice);
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

    private void addNotice(EcOrder ecOrder, NotifyTypeEnum type, String message) {
        Date now = new Date();
        long diff = now.getTime() - ecOrder.getCreatetime().getTime();
        // 超過7天的訂單不再發通知
        if (diff < 1000*60*60*24*7L) {
            notifyFacade.createNotify(type, message, ecOrder, ecOrder.getMemberId());
        }
    }

    // TMP_ORDERSTATUS_SYNC
    public void tmpOrderUpdate() {
        // 刪除不需同步的TMP_ORDERSTATUS_SYNC
        String sql = "delete tmp_orderstatus_sync t"
                + " where not exists (select 1 from ec_order o where o.id=t.order_id and o.status='APPROVE' and o.sap_ordernum is not null)"
                ;
        Query q = em.createNativeQuery(sql);
        q.executeUpdate();
        
        // 新增
        sql = "insert into tmp_orderstatus_sync(order_id, sap_ordernum, status)"
                + " select id, sap_ordernum, status from ec_order o"
                + " where status='APPROVE' and sap_ordernum is not null"
                + " and not exists (select 1 from tmp_orderstatus_sync t where o.id=t.order_id)"
                ;
        q = em.createNativeQuery(sql);
        q.executeUpdate();
    }

    public List<EcOrder> tmpOrderFindAppove(int count) {
        String sql = "select o.*"
                + " from ec_order o, tmp_orderstatus_sync t"
                + " where o.id=t.order_id and o.status='APPROVE'"
                + " order by t.modifytime, t.order_id"
                ;
        Query q = em.createNativeQuery(sql, EcOrder.class);
        q.setMaxResults(count);
        return q.getResultList();
    }
    
    public void tmpOrderIncCount(EcOrder ecOrder, String gbstk) {
        String sql = "update tmp_orderstatus_sync"
                + " set gbstk=#gbstk, modifycount=modifycount+1, modifytime=sysdate"
                + " where order_id=#order_id";
        Query q = em.createNativeQuery(sql);
        q.setParameter("gbstk", gbstk);
        q.setParameter("order_id", ecOrder.getId());
        q.executeUpdate();
    }

}
