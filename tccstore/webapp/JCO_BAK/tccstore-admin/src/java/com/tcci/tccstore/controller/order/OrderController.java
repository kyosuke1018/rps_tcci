/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.order;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcOrder;
import com.tcci.tccstore.entity.EcOrderLog;
import com.tcci.tccstore.enums.NotifyTypeEnum;
import com.tcci.tccstore.enums.OrderStatusEnum;
import com.tcci.tccstore.facade.customer.EcCustomerFacade;
import com.tcci.tccstore.facade.notify.EcNotifyFacade;
import com.tcci.tccstore.facade.order.EcOrderFacade;
import com.tcci.tccstore.facade.util.OrderFilter;
import com.tcci.tccstore.sapproxy.SdProxy;
import com.tcci.tccstore.sapproxy.SdProxyFactory;
import com.tcci.tccstore.sapproxy.dto.SapProxyResponseDto;
import com.tcci.tccstore.sapproxy.dto.SapTableDto;
import com.tcci.tccstore.sapproxy.jco.JcoUtils;
import com.tcci.tccstore.util.SapUtil;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@ManagedBean
@ViewScoped
public class OrderController {

    @Resource(mappedName = "jndi/sapclient.config")
    transient private Properties jndiConfig;
    private ResourceBundle rb = ResourceBundle.getBundle("/msgOrder", FacesContext.getCurrentInstance().getViewRoot().getLocale());

    private Logger logger = LoggerFactory.getLogger(OrderController.class);
    private List<EcOrder> items;
    private EcOrder selected;
    private List<EcOrderLog> orderLogs;
    private String siteLoc; // 袋裝噴碼編輯
    private OrderFilter filter;
    private List<SelectItem> plantList;
    private List<SelectItem> statusList;
    private Date deliveryDateBegin;
    private Date deliveryDateEnd;
    private String msgPrefix;
    private String msgInput;
    private StreamedContent exportFile; // 匯出檔案

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); // for deliveryDate filter
    private String MSG_DEFAULT = "详情请洽负责业务员.";
    
    @Inject
    private EcOrderFacade ejbFacade;
    @Inject
    private EcNotifyFacade notifyFacade;
    @Inject
    private EcCustomerFacade customerFacade;
//    @Inject
//    private OrderUtil orderUtil;

    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    @PostConstruct
    private void init() {
        plantList = new ArrayList<>();
        List<String> plantCodeList = new ArrayList<>();
        for (TcGroup tcGroup : userSession.getPlantGroups()) {
            String prefix = tcGroup.getCode().substring(0, 2);
            plantCodeList.add(prefix);
            plantList.add(new SelectItem(prefix, tcGroup.getCode() + " " + tcGroup.getName()));
        }
        this.statusList = new ArrayList();
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            statusList.add(new SelectItem(status, status.getDisplayName()));
        }
        this.filter = new OrderFilter();
        // this.filter.setStatus(OrderStatusEnum.OPEN);
        this.filter.setPlantList(plantCodeList);
        
        // 預設提貨日查詢
        Calendar today = Calendar.getInstance();
        deliveryDateBegin = today.getTime();

        this.filter.setExcludeC1(true); // 不顯示c1開的訂單，僅adminstrator可以修改
        if (userSession.getPlantGroups().isEmpty()) {
            JsfUtil.addErrorMessage("未设定厂权限!");
        } else {
            query();
        }
    }

    public List<EcCustomer> completeCustomer(String input) {
        return customerFacade.findByKeyword(input);
    }

    public void query() {
        this.filter.setDeliveryDateBegin(this.deliveryDateBegin==null ? null : sdf.format(this.deliveryDateBegin));
        this.filter.setDeliveryDateEnd(this.deliveryDateEnd==null ? null : sdf.format(this.deliveryDateEnd));
        this.items = ejbFacade.findByCriteria(this.filter);
        // logger.debug("this.items.size()={}", this.items.size());
    }

    public void initReviewOrder(EcOrder order) {
        this.selected = order;
        this.siteLoc = order.getSiteLoc();
        this.orderLogs = ejbFacade.findLog(order);
        this.msgPrefix = MessageFormat.format(rb.getString("order.msg.orderCancelNotifyPrefix"),
                        new Object[]{String.valueOf(order.getId()), order.getQuantity(), order.getVehicle()});
        if (order.getMessage() == null) {
            this.msgInput = MSG_DEFAULT;
        } else {
            this.msgInput = order.getMessage();
        }
    }

    public void cancel() {
        cancel(this.selected);
    }
    
    public void cancel(EcOrder ecOrder) {
        if (!ejbFacade.canCancel(ecOrder)) {
            JsfUtil.addWarningMessage("该订单狀態已變更，请按[查询]重新整理!");
            return;
        }
        boolean cancelOrder = true;
        if (StringUtils.isNotEmpty(ecOrder.getSapOrdernum())) {
            try {
                Properties jcoProp = JcoUtils.getJCoProp(jndiConfig, "tcc_cn"); //取得相關Jco連結參數
                SdProxy sdProxy = SdProxyFactory.createProxy(jcoProp);//建立連線
                SapProxyResponseDto resultDto = SapUtil.cancelOrder(sdProxy, ecOrder);
                SapTableDto sapTableDto = (SapTableDto) resultDto.getResult();
                if (sapTableDto.getDataMapList().size() > 0) {
                    List<Map<String, Object>> dataMapList = sapTableDto.getDataMapList();
                    logger.debug("dataMapList.size()={}", dataMapList.size());
                    for (Map<String, Object> dataMap : dataMapList) {
                        logger.debug("MES_TYPE={}", dataMap.get("MES_TYPE"));
                        logger.debug("MESSAGE={}", dataMap.get("MESSAGE"));
                        logger.debug("VBELN={}", dataMap.get("VBELN"));
                    }
                    if (dataMapList.size() > 0) {
                        Map<String, Object> dataMap = dataMapList.get(0);
                        String messageType = (String) dataMap.get("MES_TYPE");
                        logger.debug("messageType={}", messageType);
                        String vbeln = (String) dataMap.get("VBELN");
                        logger.debug("vbeln={}", vbeln);
                        String message = (String) dataMap.get("MESSAGE");
                        logger.debug("message={}", message);
                        if (messageType.equals("S")) {
                            JsfUtil.addSuccessMessage(MessageFormat.format(rb.getString("order.msg.sapOrderCancelSuccess"), message));
                        } else {
                            cancelOrder = false;
                            JsfUtil.addErrorMessage(MessageFormat.format(rb.getString("order.msg.sapOrderCancelFail"), message));
                            ejbFacade.addLog(ecOrder, "CANCEL_FAIL", userSession.getTcUser(), message);
                        }
                    }
                }
                sdProxy.dispose();
            } catch (Exception e) {
                logger.error("e={}", e);
                cancelOrder = false;
                JsfUtil.addErrorMessage(e, "exception");
                ejbFacade.addLog(ecOrder, "CANCEL_FAIL", userSession.getTcUser(), e.getMessage());
            }
        }
        if (cancelOrder) {
            ecOrder.setApprover(userSession.getTcUser());
            ecOrder.setApprovalTime(new Date());
            ecOrder.setStatus(OrderStatusEnum.CANCEL);
            if (!MSG_DEFAULT.equals(this.msgInput)) {
                ecOrder.setMessage(this.msgInput);
            }
            createOrderCancelNotify(ecOrder);
            ejbFacade.editThenReturn(ecOrder);
            String logMsg = MSG_DEFAULT.equals(this.msgInput) ? null : this.msgInput;
            ejbFacade.addLog(ecOrder, OrderStatusEnum.CANCEL.name(), userSession.getTcUser(), logMsg);
            logger.debug("order {} cancel by {}!", ecOrder.getId(), userSession.getTcUser().getLoginAccount());
            JsfUtil.addSuccessMessage(rb.getString("order.msg.cancelSuccess"));
        }
    }
    
    public void approve(EcOrder ecOrder) {
        approveOrder(ecOrder);
    }

    public void approve() {
        approveOrder(this.selected);
    }
    
    // helper
    public boolean canInputSiteLoc() {
        // 非貴港且為袋泥(倒數第4碼為2)時一定要輸入袋裝噴碼
        if (this.selected == null) {
            return false;
        }
        String plantCode = this.selected.getPlantCode();
        String prodCode = this.selected.getProductCode();
        return !plantCode.matches("^25..$") && prodCode.matches("^.{8}2...$");
    }

    private void createOrderCancelNotify(EcOrder order) {
        notifyFacade.createNotify(NotifyTypeEnum.ORDER_CANCEL,
                this.msgPrefix + " " + this.msgInput, order, order.getMemberId());
    }
    
    private void approveOrder(EcOrder ecOrder) {
        if (!ejbFacade.canApprove(ecOrder)) {
            JsfUtil.addWarningMessage("该订单狀態已變更，请按[查询]重新整理!");
            return;
        }
        try {
            Properties jcoProp = JcoUtils.getJCoProp(jndiConfig, "tcc_cn"); //取得相關Jco連結參數
            SdProxy sdProxy = SdProxyFactory.createProxy(jcoProp);//建立連線
            List<EcOrder> ecOrders = new ArrayList<>();
            ecOrders.add(ecOrder);
            List<String> resultMessages = SapUtil.approveOrder(sdProxy, ecOrders, userSession.getTcUser(), ejbFacade, notifyFacade);
            if (resultMessages.isEmpty()) {
                JsfUtil.addSuccessMessage(MessageFormat.format(rb.getString("order.msg.sapOrderCreateSuccess"), ecOrder.getSapOrdernum()));
            } else {
                StringBuilder sb = new StringBuilder();
                for (String msg : resultMessages) {
                    if (sb.length() > 0) {
                        sb.append('\n');
                    }
                    sb.append(msg);
                }
                JsfUtil.addErrorMessage(sb.toString());
            }
            sdProxy.dispose();
        } catch (Exception e) {
            logger.error("approve(), e={}", e);
            JsfUtil.addErrorMessage(e.getMessage());
            ejbFacade.addLog(ecOrder, "APPROVE_FAIL", userSession.getTcUser(), e.getMessage());
        }
    }

    // 讓PostConstruct的message可以被顯示
    public String getTitle() {
        return rb.getString("order.label.listOrder");
    }
    
    //getter, setter
    public List<EcOrder> getItems() {
        return items;
    }

    public void setItems(List<EcOrder> items) {
        this.items = items;
    }

    public EcOrder getSelected() {
        return selected;
    }

    public void setSelected(EcOrder selected) {
        this.selected = selected;
    }

    public List<EcOrderLog> getOrderLogs() {
        return orderLogs;
    }

    public void setOrderLogs(List<EcOrderLog> orderLogs) {
        this.orderLogs = orderLogs;
    }

    public String getSiteLoc() {
        return siteLoc;
    }

    public void setSiteLoc(String siteLoc) {
        this.siteLoc = siteLoc;
    }

    public OrderFilter getFilter() {
        return filter;
    }

    public void setFilter(OrderFilter filter) {
        this.filter = filter;
    }

    public List<SelectItem> getPlantList() {
        return plantList;
    }

    public void setPlantList(List<SelectItem> plantList) {
        this.plantList = plantList;
    }

    public List<SelectItem> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<SelectItem> statusList) {
        this.statusList = statusList;
    }

    public Date getDeliveryDateBegin() {
        return deliveryDateBegin;
    }

    public void setDeliveryDateBegin(Date deliveryDateBegin) {
        this.deliveryDateBegin = deliveryDateBegin;
    }

    public Date getDeliveryDateEnd() {
        return deliveryDateEnd;
    }

    public void setDeliveryDateEnd(Date deliveryDateEnd) {
        this.deliveryDateEnd = deliveryDateEnd;
    }

    public String getMsgPrefix() {
        return msgPrefix;
    }

    public void setMsgPrefix(String msgPrefix) {
        this.msgPrefix = msgPrefix;
    }

    public String getMsgInput() {
        return msgInput;
    }

    public void setMsgInput(String msgInput) {
        this.msgInput = msgInput;
    }

    public StreamedContent getExportFile() {
        return exportFile;
    }

    public void setExportFile(StreamedContent exportFile) {
        this.exportFile = exportFile;
    }
    
}
