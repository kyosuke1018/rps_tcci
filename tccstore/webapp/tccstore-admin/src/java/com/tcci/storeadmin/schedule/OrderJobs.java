/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.schedule;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.VelocityMail;
import com.tcci.tccstore.entity.EcOrder;
import com.tcci.tccstore.entity.datawarehouse.ZstdSoinputVO;
import com.tcci.tccstore.enums.OrderStatusEnum;
import com.tcci.tccstore.facade.datawarehouse.ZstdFacade;
import com.tcci.tccstore.facade.order.EcOrderFacade;
import com.tcci.tccstore.rfc.RFCExec;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class OrderJobs {

    private final static Logger logger = LoggerFactory.getLogger(OrderJobs.class);
    
    private ResourceBundle rb = ResourceBundle.getBundle("/msgOrder");
//    @Resource(mappedName = "jndi/sapclient.config")
//    transient private Properties jndiConfig;
    @Resource(mappedName = "jndi/tccstore.config")
    transient private Properties tccstoreConfig;
    @Resource(mappedName = "jndi/global.config")
    transient private Properties globalConfig;

    @Inject
    private EcOrderFacade ejbFacade;
//    @Inject
//    private EcNotifyFacade notifyFacade;
//    @Inject
//    private EcRewardFacade rewardFacade;
    @Inject
    private ZstdFacade zstdFacade;

    // 已核准的訂單與SAP同步
    // APPROVE -> CLOSE，如果SAP的訂單狀態是已出貨，同時贈送紅利1點
    // APPROVE -> CANCEL，如果在SAP上已查不到該筆訂單
    /*
    public void syncOrderStatus() {
        OrderFilter filter = new OrderFilter();
        filter.setStatus(OrderStatusEnum.APPROVE);
        Map<String, EcOrder> approveOrderMap = new HashMap();
        for (EcOrder ecOrder : ejbFacade.findByCriteria(filter)) {
            if (StringUtils.isNotEmpty(ecOrder.getSapOrdernum())) {
                approveOrderMap.put(ecOrder.getSapOrdernum(), ecOrder);
            }
        }
        // 無資料就不需要call SAP
        if (approveOrderMap.isEmpty()) {
            return;
        }
        try {
            Properties jcoProp = JcoUtils.getJCoProp(jndiConfig, "tcc_cn"); //取得相關Jco連結參數
            SdProxy sdProxy = SdProxyFactory.createProxy(jcoProp);//建立連線
            List<String> vbelnList = new ArrayList();
            vbelnList.addAll(approveOrderMap.keySet());
            //先更新是否已出貨 (status = CLOSE)
            List<String> shippedVbelnList = new ArrayList();
            SapProxyResponseDto result = sdProxy.findShippedSalesDocument(vbelnList);
            Map<String, SapTableDto> sapTableDtoMap = (Map<String, SapTableDto>) result.getResult();
            if (sapTableDtoMap.size() > 0) {
                SapTableDto sapTableDto = sapTableDtoMap.get("ZTAB_EXP_VBAK");
                logger.debug("sapTableDto.getDataMapList().size()={}", sapTableDto.getDataMapList().size());
                if (sapTableDto.getDataMapList().size() > 0) {
                    List<Map<String, Object>> dataMapList = sapTableDto.getDataMapList();
                    for (Map<String, Object> dataMap : dataMapList) {
                        String sapOrderNum = ((String) dataMap.get("VBELN")).replaceAll("^0+", "");
                        shippedVbelnList.add(sapOrderNum);
                    }
                }
            }
            for (String shippedVbeln : shippedVbelnList) {
                EcOrder closeOrder = approveOrderMap.get(shippedVbeln);
                closeOrder.setStatus(OrderStatusEnum.CLOSE);
                createOrderCloseNotify(closeOrder);
                ejbFacade.edit(closeOrder);
                ejbFacade.addLog(closeOrder, OrderStatusEnum.CLOSE.name(), (TcUser) null, null);
                approveOrderMap.remove(shippedVbeln);
                logger.debug("order close by schedule, id={}", closeOrder.getId());
                // 11/2起 ~12/31, 每筆訂單1點
                String eventDetail = "訂單(" + closeOrder.getSapOrdernum() + ")紅利";
                rewardFacade.awardPoints(closeOrder.getMemberId(), RewardTypeEnum.BONUS, 1, RewardEventEnum.ORDER_CLOSE, eventDetail, null, closeOrder);
                logger.warn("add 1 bonus point, member id:{}", closeOrder.getMemberId().getId());
            }

            //再更新SAP訂單是否已刪除 (status = CANCEL).
            vbelnList = new ArrayList();
            vbelnList.addAll(approveOrderMap.keySet());
            List<String> existsVbelnList = new ArrayList();
            SapProxyResponseDto result2 = sdProxy.findSalesDocument(vbelnList, Boolean.FALSE);
            Map<String, SapTableDto> sapTableDtoMap2 = (Map<String, SapTableDto>) result2.getResult();
            if (sapTableDtoMap2.size() > 0) {
                SapTableDto sapTableDto = sapTableDtoMap2.get("ZTAB_EXP_VBAK");
                logger.debug("sapTableDto.getDataMapList().size()={}", sapTableDto.getDataMapList().size());
                if (sapTableDto.getDataMapList().size() > 0) {
                    List<Map<String, Object>> dataMapList = sapTableDto.getDataMapList();
                    for (Map<String, Object> dataMap : dataMapList) {
                        String sapOrderNum = ((String) dataMap.get("VBELN")).replaceAll("^0+", "");
                        existsVbelnList.add(sapOrderNum);
                    }
                }
            }
            for (String existsVbeln : existsVbelnList) {
                logger.debug("remove {}", existsVbeln);
                approveOrderMap.remove(existsVbeln);
            }
            for (EcOrder cancelOrder : approveOrderMap.values()) {
                cancelOrder.setStatus(OrderStatusEnum.CANCEL);
                createOrderCancelNotify(cancelOrder);
                ejbFacade.edit(cancelOrder);
                ejbFacade.addLog(cancelOrder, OrderStatusEnum.CANCEL.name(), (TcUser) null, null);
                logger.debug("order cancel by schedule, id={}", cancelOrder.getId());
            }
        } catch (Exception e) {
            logger.error("e={}", e);
        }
    }
    */
    // transaction 由ejbFacade控制
    @TransactionAttribute(NOT_SUPPORTED)
    public void syncOrderStatus() {
        List<EcOrder> ecOrders = ejbFacade.findSapCreated(); // 找出已核准且Sap已開單
        logger.debug("findSapCreated return {} record(s).", ecOrders.size());
        if (ecOrders.isEmpty()) {
            return;
        }
        Map<String, EcOrder> mapOrder = new HashMap<>();
        for (EcOrder ecOrder : ecOrders) {
            mapOrder.put(ecOrder.getSapOrdernum(), ecOrder);
        }
        List<String> sapOrders = new ArrayList<>(mapOrder.keySet());
        // 如果在今日出貨資料裡, 狀態改成已出貨
        List<String> closeOrders = zstdFacade.findInShipdetail(sapOrders);
        for (String orderNum : closeOrders) {
            EcOrder ecOrder = mapOrder.get(orderNum);
            logger.debug("order closed (findInShipdetail), order id:{}", ecOrder.getId());
            ejbFacade.closeOrder(mapOrder.get(orderNum));
        }
        sapOrders.removeAll(closeOrders);
        closeOrders = zstdFacade.findInZorderCn(sapOrders);
        for (String orderNum : closeOrders) {
            EcOrder ecOrder = mapOrder.get(orderNum);
            logger.debug("order closed (findInZorderCn), order id:{}", ecOrder.getId());
            ejbFacade.closeOrder(ecOrder);
        }
    }
    
    // 取消提貨日過期 AND OPEN 的訂單
    // 已核准但尚未出貨目前先不取消
    // TODO: 在SAP刪除的訂單無法回饋到訂單系統
    public void cancelExpiredOrder() {
        OrderStatusEnum[] statusList = { OrderStatusEnum.OPEN };
        List<EcOrder> orders = ejbFacade.findExpiredOrder(statusList);
        for (EcOrder ecOrder : orders) {
            ejbFacade.cancelExpiredOrder(ecOrder);
            /*
            if (ecOrder.getSapOrdernum() != null) {
                zstdFacade.soinputCancel(ecOrder);
            }
            */
        }
    }

    // 訂單建立(OPEN)批次通知
    public void openOrdersNotify() {
        Map<String, List<EcOrder>> mapOrders = new HashMap<>(); // by公司別通知
        List<EcOrder> allOpenOrders = ejbFacade.findOrderByStatus(OrderStatusEnum.OPEN);
        for (EcOrder ecOrder : allOpenOrders) {
            // c1開的單不通知
            if ("c1".equals(ecOrder.getMemberId().getLoginAccount())) {
                continue;
            }
            String groupCode = ecOrder.getPlantCode().substring(0, 2) + "00";
            List<EcOrder> groupOrders = mapOrders.get(groupCode);
            if (null == groupOrders) {
                groupOrders = new ArrayList<>();
                mapOrders.put(groupCode, groupOrders);
            }
            groupOrders.add(ecOrder);
        }
        String orderLink = tccstoreConfig.getProperty("adminURL") + "/faces/sales/order.xhtml";
        for (Map.Entry<String, List<EcOrder>> entry : mapOrders.entrySet()) {
            String groupCode = entry.getKey();
            List<EcOrder> openOrders = entry.getValue();
            List<TcUser> approvers = ejbFacade.findApproverByGroupCode(groupCode);
//            logger.debug("groupCode:{}, openOrders:{}, approvers:{}", new Object[] {
//                groupCode, openOrders, approvers});
            openOrdersNotify(groupCode, openOrders, approvers, orderLink);
        }
    }
    
    // transaction 由ejbFacade控制
    @TransactionAttribute(NOT_SUPPORTED)
    public void autoOrderApprove() {
        /*
        try {
            List<EcOrder> autoOrders = ejbFacade.findOrderByStatus(OrderStatusEnum.OPEN_AUTO);
            // 無資料就回去了，不需建SAP連線
            if (autoOrders.isEmpty()) {
                return;
            }
            // order by id, 舊的先核准 
            Collections.sort(autoOrders, new Comparator<EcOrder>() {
                @Override
                public int compare(EcOrder o1, EcOrder o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            });
            Properties jcoProp = JcoUtils.getJCoProp(jndiConfig, "tcc_cn"); //取得相關Jco連結參數
            SdProxy sdProxy = SdProxyFactory.createProxy(jcoProp);//建立連線
            long timeouts = 4 * 60 * 1000L; // 超過時間的就下次再核(避免還沒結束, 另一台的排程又開始了)
            SapUtil.approveOrder(sdProxy, autoOrders, null, ejbFacade, notifyFacade, timeouts);
            // 有錯時寄通知信
            Map<String, List<EcOrder>> mapOrders = new HashMap<>(); // by公司別通知
            for (EcOrder ecOrder : autoOrders) {
                if (OrderStatusEnum.FAIL == ecOrder.getStatus()) {
                    String groupCode = ecOrder.getPlantCode().substring(0, 2) + "00";
                    List<EcOrder> groupOrders = mapOrders.get(groupCode);
                    if (null == groupOrders) {
                        groupOrders = new ArrayList<>();
                        mapOrders.put(groupCode, groupOrders);
                    }
                    groupOrders.add(ecOrder);
                }
            }
            String orderLink = tccstoreConfig.getProperty("adminURL") + "/faces/sales/order.xhtml";
            for (Map.Entry<String, List<EcOrder>> entry : mapOrders.entrySet()) {
                String groupCode = entry.getKey();
                List<EcOrder> failOrders = entry.getValue();
                List<TcUser> approvers = ejbFacade.findApproverByGroupCode(groupCode);
                approveFailNotify(groupCode, failOrders, approvers, orderLink);
            }
        } catch (Exception ex) {
            logger.error("autoOrderApprove exception", ex);
        }
        */
    }

    // 
    // transaction 由ejbFacade控制
    @TransactionAttribute(NOT_SUPPORTED)
    public void zstdSoinputSync() {
        Map<String, List<EcOrder>> mapOrders = new HashMap<>(); // 失敗時by公司別通知
        List<ZstdSoinputVO> list = zstdFacade.soinputFindSapFinished();
        for (ZstdSoinputVO vo : list) {
            EcOrder ecOrder = ejbFacade.find(vo.getSigni());
            zstdFacade.soinputEcComplete(vo.getSigni(), vo.getOrderFlag2());
            if (null == ecOrder) {
                logger.error("order id {} not found!", vo.getSigni());
                continue;
            }
            OrderStatusEnum status = ecOrder.getStatus();
            String flags = vo.getProcFlag() + vo.getOrderFlag2();
            if ("C1".equals(flags)) { // SAP開單成功
                String sapOrdernum = vo.getVbelv();
                if (OrderStatusEnum.APPROVE == status) {
                    ejbFacade.sapCreateSuccess(ecOrder, sapOrdernum);
                } else if (OrderStatusEnum.CANCEL == status) {
                    // SAP補刪單
                    logger.warn("sap cancel postpone, order id {}, sap ordernum {}", vo.getSigni(), vo.getVbelv());
                    ecOrder.setSapOrdernum(sapOrdernum);
                    ejbFacade.edit(ecOrder);
                    ejbFacade.addLog(ecOrder, "SAP_CANCEL_POSTPONE", (TcUser) null, "SAP單號:" + sapOrdernum);
                    zstdFacade.soinputCancel(ecOrder);
                } else {
                    logger.error("C1 unknown status:{}, signi:{}", status, vo.getSigni());
                }
            } else if ("C2".equals(flags)) {
                // 刪除成功
                ejbFacade.sapCancelSuccess(ecOrder, vo.getVbelv());
                logger.warn("sap cancel success, order id {}, sap ordernum {}", vo.getSigni(), vo.getVbelv());
            } else if ("F1".equals(flags)) {
                // 新增失敗
                if (OrderStatusEnum.APPROVE == status) {
                    String message = zstdFacade.soinputFindError(vo.getSigni());
                    logger.error("sap create failed, order id:{}, message:{}", vo.getSigni(), message);
                    ejbFacade.sapCreateFail(ecOrder, message);
                } else if (OrderStatusEnum.CANCEL == status) {
                    logger.warn("sap create failed, but order {} had canceled!", vo.getSigni());
                } else {
                    logger.error("F1 unknown status:{}, signi:{}", status, vo.getSigni());
                }
            } else if ("F2".equals(flags)) {
                // 刪除失敗
                String message = zstdFacade.soinputFindError(vo.getSigni());
                if (!StringUtils.contains(message, "找不到訂單")) {
                    logger.error("sap cancel failed, order id:{}, message:{}", vo.getSigni(), message);
                    ejbFacade.sapCancelFail(ecOrder, message);
                } else {
                    logger.warn("找不到訂單, order id:{}", vo.getSigni());
                }
            } else {
                logger.error("unknown flags:{}, signi:{}", vo.getSigni());
            }
            // 失敗時by公司別通知
            if (OrderStatusEnum.FAIL == ecOrder.getStatus()) {
                String groupCode = ecOrder.getPlantId().getVkorg();
                List<EcOrder> groupOrders = mapOrders.get(groupCode);
                if (null == groupOrders) {
                    groupOrders = new ArrayList<>();
                    mapOrders.put(groupCode, groupOrders);
                }
                groupOrders.add(ecOrder);
            }
        }
        String orderLink = tccstoreConfig.getProperty("adminURL") + "/faces/sales/order.xhtml";
        for (Map.Entry<String, List<EcOrder>> entry : mapOrders.entrySet()) {
            String groupCode = entry.getKey();
            List<EcOrder> failOrders = entry.getValue();
            List<TcUser> approvers = ejbFacade.findApproverByGroupCode(groupCode);
            sapFailNotify(groupCode, failOrders, approvers, orderLink);
        }
    }

    @TransactionAttribute(NOT_SUPPORTED)
    public void tmpOrderStatusSync(int count) {
        logger.debug("tmpOrderStatusSync start...");
        // 更新 TMP_ORDERSTATUS_SYNC
        ejbFacade.tmpOrderUpdate();
        
        // 找出狀態是已核准且SAP已開單
        List<EcOrder> ecOrders = ejbFacade.tmpOrderFindAppove(count); 
        if (ecOrders.isEmpty()) {
            logger.debug("tmpOrderFindAppove is empty!");
            return;
        }
        
        // 取得SAP訂單狀態
        List<String> vbelns = new ArrayList<>();
        for (EcOrder ecOrder : ecOrders) {
            vbelns.add(ecOrder.getSapOrdernum());
        }
        String jcoServiceUrl = globalConfig.getProperty("SAP_REST_ROOT");
        Map<String, List<Map<String, Object>>> result = RFCExec.expSdGetOrder(jcoServiceUrl, vbelns);
        if (null == result) {
            logger.error("RFCExec.expSdGetOrder return null!");
            return;
        }
        List<Map<String, Object>> list = result.get("ZTAB_EXP_VBUK");
        if (null == list) {
            logger.error("ZTAB_EXP_VBUK not found!");
            return;
        }
        Map<String, String> mapOrderStatus = new HashMap<>();
        for (Map<String, Object> map : list) {
            String vbeln = (String) map.get("VBELN");
            String gbstk = (String) map.get("GBSTK");
            if (null == vbeln) {
                continue;
            }
            vbeln = vbeln.replaceFirst("^0+(?!$)", ""); // remove leading zeros
            mapOrderStatus.put(vbeln, gbstk);
        }
        for (EcOrder ecOrder : ecOrders) {
            ecOrder = ejbFacade.find(ecOrder.getId()); // 取得最新狀態
            if (OrderStatusEnum.APPROVE != ecOrder.getStatus()) {
                logger.warn("order id:{}, status:{} changed!", ecOrder.getId(), ecOrder.getStatus());
                continue;
            }
            if (!mapOrderStatus.containsKey(ecOrder.getSapOrdernum())) {
                logger.debug("order id:{}, 订单号码不存在!", ecOrder.getId());
                String notice = MessageFormat.format("订单(序号: {0}, 数量: {1} 吨, 车号: {2})已经取消!",
                        new Object[]{String.valueOf(ecOrder.getId()), ecOrder.getQuantity(), ecOrder.getVehicle()});
                String comment = "订单号码不存在!";
                ejbFacade.cancelByUser(ecOrder, notice, null, comment);
                continue;
            }
            String gbstk = mapOrderStatus.get(ecOrder.getSapOrdernum());
            if ("C".equals(gbstk)) {
                logger.debug("order id:{} 已出貨!", ecOrder.getId());
                ejbFacade.closeOrder(ecOrder);
            } else {
                // logger.debug("order id:{}, gbstk:{}!", ecOrder.getId(), gbstk);
                ejbFacade.tmpOrderIncCount(ecOrder, gbstk);
            }
        }
        logger.debug("tmpOrderStatusSync end.");
    }
    
    /*
    private void createOrderCloseNotify(EcOrder order) {
        notifyFacade.createNotify(NotifyTypeEnum.ORDER_CLOSE,
                MessageFormat.format(rb.getString("order.msg.orderCloseNotify"),
                        new Object[]{order.getSapOrdernum(), String.valueOf(order.getId()), order.getQuantity(), order.getVehicle()}),
                order,
                order.getMemberId());
    }

    private void createOrderCancelNotify(EcOrder order) {
        notifyFacade.createNotify(NotifyTypeEnum.ORDER_CANCEL,
                MessageFormat.format(rb.getString("order.msg.orderCancelNotify"),
                        new Object[]{String.valueOf(order.getId()), order.getQuantity(), order.getVehicle()}),
                order,
                order.getMemberId());
    }
    */

    private void openOrdersNotify(String groupCode, List<EcOrder> openOrders, List<TcUser> approvers, String orderLink) {
        HashMap<String, Object> mailBean = new HashMap<>();
        String subject = "[台泥电商] 订单待审核，销售组织代码:" + groupCode;
        mailBean.put(VelocityMail.SUBJECT, subject);
        StringBuilder sb = new StringBuilder();
        for (TcUser u : approvers) {
            if (u.getEmail() == null || u.getDisabled()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(u.getEmail());
        }
        if (sb.length() == 0) {
            sb.append("jimmy.lee@tcci.com.tw");
        }
        mailBean.put(VelocityMail.TO, sb.toString());
        mailBean.put("openOrders", openOrders);
        mailBean.put("orderLink", orderLink);
        VelocityMail.sendMail(mailBean, "/mail_openOrdersNotify.vm");
    }
    
    private void approveFailNotify(String groupCode, List<EcOrder> failOrders, List<TcUser> approvers, String orderLink) {
        HashMap<String, Object> mailBean = new HashMap<>();
        String subject = "[台泥电商] 订单自动审核失败，销售组织代码:" + groupCode;
        mailBean.put(VelocityMail.SUBJECT, subject);
        StringBuilder sb = new StringBuilder("jimmy.lee@tcci.com.tw"); // 觀察一陣子看看
        for (TcUser u : approvers) {
            if (u.getEmail() == null || u.getDisabled()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(u.getEmail());
        }
        if (sb.length() == 0) {
            sb.append("jimmy.lee@tcci.com.tw");
        }
        mailBean.put(VelocityMail.TO, sb.toString());
        mailBean.put("failOrders", failOrders);
        mailBean.put("orderLink", orderLink);
        VelocityMail.sendMail(mailBean, "/mail_approveFailNotify.vm");
    }
    
    private void sapFailNotify(String groupCode, List<EcOrder> failOrders, List<TcUser> approvers, String orderLink) {
        HashMap<String, Object> mailBean = new HashMap<>();
        String subject = "[台泥电商] SAP订单新增/删除失败，销售组织代码:" + groupCode;
        mailBean.put(VelocityMail.SUBJECT, subject);
        // TODO: 之後應該是用角色區分
        //StringBuilder sb = new StringBuilder("jimmy.lee@tcci.com.tw"); // 觀察一陣子看看
        StringBuilder sb = new StringBuilder();
        for (TcUser u : approvers) {
            if (u.getEmail() == null || u.getDisabled()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(u.getEmail());
        }
        if (sb.length() == 0) {
            sb.append("jimmy.lee@tcci.com.tw");
        }
        mailBean.put(VelocityMail.TO, sb.toString());
        mailBean.put("failOrders", failOrders);
        mailBean.put("orderLink", orderLink);
        VelocityMail.sendMail(mailBean, "/mail_sapFailNotify.vm");
    }

}
