/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.remit;

import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.SessionController;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkProductMaster;
import com.tcci.sksp.entity.ar.SkSalesAllowances;
import com.tcci.sksp.entity.enums.ReasonEnum;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkCustomerFacade;
import com.tcci.sksp.facade.SkProductMasterFacade;
import com.tcci.sksp.facade.SkSalesAllowancesFacade;
import com.tcci.sksp.facade.SkSalesDetailsFacade;
import com.tcci.sksp.vo.SalesAllowancesVO;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@ManagedBean
@ViewScoped
public class ApplySalesAllowancesController {

    //<editor-fold defaultstate="collapsed" desc="variables">
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ResourceBundle rb = ResourceBundle.getBundle("messages");
    private List<SalesAllowancesVO> salesAllowancesVOList;
    private SkSalesAllowances salesAllowances = new SkSalesAllowances();
    private SkSalesMember salesMember;
    private String orderNumber;
    private boolean selectAll;
    private ReasonEnum reason;
    private List<ReasonEnum> reasonList = new ArrayList<ReasonEnum>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    SkSalesDetailsFacade salesDetailsFacade;
    @EJB
    SkCustomerFacade customerFacade;
    @EJB
    SkProductMasterFacade productMasterFacade;
    @EJB
    SkSalesAllowancesFacade salesAllowancesFacade;
    @ManagedProperty(value = "#{salesAllowancesListController}")
    SalesAllowancesListController salesAllowancesListController;

    public void setSalesAllowancesListController(SalesAllowancesListController salesAllowancesListController) {
        this.salesAllowancesListController = salesAllowancesListController;
    }

    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getter,setter">
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public SkSalesMember getSalesMember() {
        return salesMember;
    }

    public void setSalesMember(SkSalesMember salesMember) {
        this.salesMember = salesMember;
    }

    public List<SalesAllowancesVO> getSalesAllowancesVOList() {
        return salesAllowancesVOList;
    }

    public void setSalesAllowancesVOList(List<SalesAllowancesVO> salesAllowancesVOList) {
        this.salesAllowancesVOList = salesAllowancesVOList;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public ReasonEnum getReason() {
        return reason;
    }

    public void setReason(ReasonEnum reason) {
        this.reason = reason;
    }

    public List<ReasonEnum> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<ReasonEnum> reasonList) {
        this.reasonList = reasonList;
    }

    public SkSalesAllowances getSalesAllowances() {
        return salesAllowances;
    }

    public void setSalesAllowances(SkSalesAllowances salesAllowances) {
        this.salesAllowances = salesAllowances;
    }

    //</editor-fold>
    @PostConstruct
    private void init() {
        logger.debug("init(), SalesAllowancesReasonEnum.values().length={}", ReasonEnum.values().length);
        reasonList.clear();
        reasonList.addAll(Arrays.asList(ReasonEnum.values()));
    }

    public String query() {
        try {
            if (StringUtils.isEmpty(orderNumber)) {
                FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("salesAllowances.error.orderNumberIsRequired"));
                return "";
            }
            salesAllowancesVOList = salesDetailsFacade.findBySalesMemberOrderNumberAndOrderDate(salesMember, orderNumber);
            logger.debug("salesAllowancesVOList size= {}", salesAllowancesVOList.size());
            /* already sorted in salesDetailsFacade.findBySalesMemberOrderNumberAndOrderDate
             Collections.sort(salesAllowancesVOList, new Comparator<SalesAllowancesVO>() {

             public int compare(SalesAllowancesVO s1, SalesAllowancesVO s2) {
             if (s1.getOrderNumber().equals(s2.getOrderNumber())) {
             return s1.getItem().compareTo(s2.getItem());
             } else {
             return s1.getOrderNumber().compareTo(s2.getOrderNumber());
             }
             }
             });
             *
             */
        } catch (Exception e) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
            logger.error("queyr(), e={}", e);
        }
        return null;
    }

    public void add() {
        try {
            if (salesAllowancesVOList == null || salesAllowancesVOList.isEmpty()) {
                FacesUtil.addFacesMessage(FacesMessage.SEVERITY_WARN, rb.getString("salesAllowances.error.empty"));
                return;
            }
            int selectedCount = 0;
            int notSelectedCount = 0;
            for (SalesAllowancesVO vo : salesAllowancesVOList) {
                if (vo.isSelected()) {
                    selectedCount++;
                } else {
                    notSelectedCount++;
                }
            }
            if (0 == selectedCount) {
                FacesUtil.addFacesMessage(FacesMessage.SEVERITY_WARN, rb.getString("salesAllowances.error.empty"));
                return;
            }
            // 原因為5%,5%特殊,3%時, 明細必需全選
            boolean reason101_105 = "101".equals(reason.getCode()) || "105".equals(reason.getCode());
            if (reason101_105 && notSelectedCount > 0) {
                FacesUtil.addFacesMessage(FacesMessage.SEVERITY_WARN, rb.getString("salesAllowances.error.allitemsrequired"));
                return;
            }
            List<Exception> allExceptions = new ArrayList<Exception>();
            for (SalesAllowancesVO vo : salesAllowancesVOList) {
                // 原因為5%,5%特殊,3%時, 不能重複新增
                if (reason101_105 && vo.isSelected()) {
                    allExceptions.addAll(validateVO(vo));
                }
            }
            if (!allExceptions.isEmpty()) {
                for (Exception exception : allExceptions) {
                    FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, exception.getLocalizedMessage());
                }
                return;
            }
            String originalOrderNumber = "";
            SkSalesAllowances previousSalesAllowances = null;
            String startEnd = "";
            for (SalesAllowancesVO vo : salesAllowancesVOList) {
                if (vo.isSelected()) {
                    vo.setSelected(false);
                    SkCustomer customer = customerFacade.findByCode(vo.getCustomer());
                    SkSalesAllowances salesAllowances = new SkSalesAllowances();
                    PropertyUtils.copyProperties(salesAllowances, vo);
                    // 原因為3%時, 重新計算銷貨折讓
                    if (reason.equals(ReasonEnum.PRECENT_3)) {
                        BigDecimal amount = vo.getAmount();
                        BigDecimal premiumDiscount = vo.getPremiumDiscount();
                        BigDecimal p3Allowances = amount.subtract(premiumDiscount).multiply(BigDecimal.valueOf(3)).divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_HALF_UP);
                        salesAllowances.setSalesAllowances(p3Allowances);
                    }
                    // Jimmy, issue#20120604, 之前 channel 是取客戶資料的SAPID前兩碼, 修正為 SK_SALES_DETAILS.SALES_CHANNEL
                    // salesAllowances.setChannel(customer.getSapid().substring(0, 2));
                    salesAllowances.setType(reason.getCategory());
                    salesAllowances.setDept("00");
                    String refOrder = "Y";
                    if ("20".equals(vo.getOrderNumber().substring(0, 2))) {
                        refOrder = "N";
                    }
                    salesAllowances.setRefOrder(refOrder);
                    salesAllowances.setBuyer(customer.getSimpleCode());
                    Date now = new Date();
                    String dateString = new SimpleDateFormat("yyyy/MM/dd").format(now);
                    salesAllowances.setApplyDate(dateString);
                    salesAllowances.setReason(reason.getCode());
                    if (originalOrderNumber.equals("") || !originalOrderNumber.equals(vo.getOrderNumber())) {
                        if (previousSalesAllowances != null) {
                            startEnd = "";
                            previousSalesAllowances.setBeginEnd((previousSalesAllowances.getBeginEnd() != null ? previousSalesAllowances.getBeginEnd() : "") + "E");
                            salesAllowancesListController.getSalesAllowancesList().remove(previousSalesAllowances);
                            previousSalesAllowances = salesAllowancesFacade.editThenReturn(previousSalesAllowances);
                            salesAllowancesListController.getSalesAllowancesList().add(previousSalesAllowances);
                        }
                        if (!"S".equals(startEnd)) {
                            startEnd = "S";
                            salesAllowances.setBeginEnd(startEnd);
                        }
                    } else {
                        salesAllowancesListController.getSalesAllowancesList().remove(previousSalesAllowances);
                        previousSalesAllowances = salesAllowancesFacade.editThenReturn(previousSalesAllowances);
                        salesAllowancesListController.getSalesAllowancesList().add(previousSalesAllowances);
                    }
                    // issue#2012062701, 折讓原因為其他, 且SK_SALES_DETAILS.SHIPPING_CONDITIONS=02, 設折讓condition為ZPR0
                    // 20120712, 改成 折讓原因不為5%, 且SK_SALES_DETAILS.SHIPPING_CONDITIONS=02, 設折讓condition為ZPR0
                    if (!reason.equals(ReasonEnum.PRECENT_5) && "02".equals(vo.getConditions())) {
                        salesAllowances.setCondition("ZPR0");
                    } else {
                        salesAllowances.setCondition("ZPR1");
                    }
                    salesAllowances.setPriceUnit(vo.getUnit());
                    salesAllowances.setPriceCondition(0);
                    salesAllowances.setSapid(vo.getSapid());
                    salesAllowances.setCreator(sessionController.getUser());
                    salesAllowances.setCreatetimestamp(now);
                    salesAllowances.setModifier(sessionController.getUser());
                    salesAllowances.setModifytimestamp(now);
                    salesAllowances = salesAllowancesFacade.createThenReturn(salesAllowances);
                    salesAllowancesListController.getSalesAllowancesList().add(salesAllowances);
                    originalOrderNumber = vo.getOrderNumber();
                    previousSalesAllowances = salesAllowances;
                }
            }
            previousSalesAllowances.setBeginEnd((previousSalesAllowances.getBeginEnd() != null ? previousSalesAllowances.getBeginEnd() : "") + "E");
            salesAllowancesListController.getSalesAllowancesList().remove(previousSalesAllowances);
            previousSalesAllowances = salesAllowancesFacade.editThenReturn(previousSalesAllowances);
            salesAllowancesListController.getSalesAllowancesList().add(previousSalesAllowances);
            Collections.sort(salesAllowancesListController.getSalesAllowancesList(), new Comparator<SkSalesAllowances>() {
                @Override
                public int compare(SkSalesAllowances s1, SkSalesAllowances s2) {
                    return s2.getId().compareTo(s1.getId());
                }
            });
            logger.debug("salesAllowancesList size= {}", salesAllowancesListController.getSalesAllowancesList().size());
        } catch (Exception e) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
            logger.error("add(), e={}", e);
        }
    }

    public String getCustomerName(String customerCode) {
        SkCustomer customer = customerFacade.findByCode(customerCode);
        if (customer != null) {
            return customer.getName();
        } else {
            return "";
        }
    }

    public String getProductName(String productNumber) {
        SkProductMaster productMaster = productMasterFacade.findByCode(productNumber);
        if (productMaster != null) {
            return productMaster.getName();
        } else {
            return "";
        }
    }

    public void selectAllChange(AjaxBehaviorEvent event) {
        for (SalesAllowancesVO salesAllowancesVO : salesAllowancesVOList) {
            salesAllowancesVO.setSelected(selectAll);
        }
    }

    /**
     * 若銷貨折讓已產生796, 不可以修改. 若原因為5%, 不可以修改.
     *
     * @param salesAllowances
     * @return
     */
    public boolean editable(SkSalesAllowances salesAllowances) {
        /*
         boolean flag = false;
         if (!ReasonEnum.PRECENT_5.getCode().equals(salesAllowances.getReason())) {
         flag = true;
         }
         return flag;
         * 
         */
        logger.debug("salesAllowances={}", salesAllowances);
        return !ReasonEnum.PRECENT_5.getCode().equals(salesAllowances.getReason())
                && !salesAllowancesListController.isProcessedOrderNumber(salesAllowances);
    }

    /**
     * 若銷貨折讓已產生796, 則不可以刪除.
     *
     * @param salesAllowances
     * @return
     */
    public boolean deletable(SkSalesAllowances salesAllowances) {
        /*
         boolean flag = true;
         if (salesAllowances.getReturnNumber() != null && salesAllowances.getReturnNumber().length() > 0) {
         flag = false;
         }
         return flag;
         * 
         */
        boolean flag = true;
        logger.debug("salesAllowances={}", salesAllowances);
        //不明白為何新增時 salesAllowances 為 null, 但可刪除...Orz.
        if (salesAllowances != null) {
            flag = !salesAllowancesListController.isProcessedOrderNumber(salesAllowances);
        } else {
            flag = true;
        }
        return flag;
    }

    public void edit(SkSalesAllowances salesAllowances) {
        this.salesAllowances = new SkSalesAllowances();
        try {
            PropertyUtils.copyProperties(this.salesAllowances, salesAllowances);
        } catch (Exception e) {
            logger.error("edit(), e={}", e);
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage());
        }
    }

    public void save() {
        logger.debug("save(),salesAllowances={}", salesAllowances);
        String message = "";
        try {
            List<Exception> exceptions = validate();
            if (!exceptions.isEmpty()) {
                String messages = "";
                for (Exception exception : exceptions) {
                    messages += exception.getLocalizedMessage();
                    messages += "\n";
                }
                RequestContext context = RequestContext.getCurrentInstance();
                context.addCallbackParam("errormsg", messages);
                return;
            }
            SkSalesAllowances existsSalesAllowances = salesAllowancesFacade.find(salesAllowances.getId());
            existsSalesAllowances.setOrderNumber(salesAllowances.getOrderNumber());
            existsSalesAllowances.setSalesAllowances(salesAllowances.getSalesAllowances());
            existsSalesAllowances = salesAllowancesFacade.editThenReturn(existsSalesAllowances);
            salesAllowancesListController.getSalesAllowancesList().remove(existsSalesAllowances);
            salesAllowancesListController.getSalesAllowancesList().add(existsSalesAllowances);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            message = e.getLocalizedMessage();
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("errormsg", message);
        }
    }

    public void delete(SkSalesAllowances salesAllowances) {
        salesAllowancesListController.getSalesAllowancesList().remove(salesAllowances);
        salesAllowancesFacade.remove(salesAllowances);
    }

    /**
     * 檢查銷貨折讓輸入欄位: 1.訂單編號: 必填, 是否已存在. 2.銷貨折讓金額: 必填, 需要正整數.
     *
     * @return exception list.
     */
    public List<Exception> validate() {
        boolean validate = true;
        List<Exception> exceptions = new ArrayList<Exception>();
        //1.訂單編號
        if (StringUtils.isEmpty(this.salesAllowances.getOrderNumber())) {
            exceptions.add(new Exception(rb.getString("salesAllowances.error.orderNumberIsRequired")));
        } else {
            String reasonCode = this.salesAllowances.getReason();
            boolean reason101_105 = "101".equals(reasonCode) || "105".equals(reasonCode);
            if (reason101_105) {
                List<SkSalesAllowances> existsSalesAllowanceses = salesAllowancesFacade.findByOrderNumberAndItem(this.salesAllowances.getOrderNumber(), this.salesAllowances.getItem());
                logger.debug("existsSalesAllowances.size()={}", (existsSalesAllowanceses == null) ? 0 : existsSalesAllowanceses.size());
                if (existsSalesAllowanceses != null && existsSalesAllowanceses.size() > 0) {
                    for (SkSalesAllowances existsSalesAllowances : existsSalesAllowanceses) {
                        if (!existsSalesAllowances.getId().equals(this.salesAllowances.getId())) {
                            exceptions.add(new Exception(rb.getString("salesAllowances.error.exists")));
                        }
                    }
                }
            }
        }
        //2.銷貨折讓金額
        BigDecimal amount = this.salesAllowances.getSalesAllowances();
        BigDecimal amountRoundToZero = (amount == null) ? null : amount.setScale(0, BigDecimal.ROUND_UP);
        if (amount == null) {
            exceptions.add(new Exception(rb.getString("salesAllowances.error.salesAllowancesIsRequired")));
        } else if (amount.doubleValue() <= 0 || (!amount.equals(amountRoundToZero))) {
            exceptions.add(new Exception(rb.getString("salesAllowances.error.salesAllowancesShouldBePostiveNumber")));
        }
        return exceptions;
    }

    /**
     *
     * @param vo SalesAllowancesVO
     * @return
     */
    private List<Exception> validateVO(SalesAllowancesVO vo) {
        List<Exception> exceptions = new ArrayList<Exception>();
        List<SkSalesAllowances> existsSalesAllowances = salesAllowancesFacade.findByOrderNumberAndItem(vo.getOrderNumber(), vo.getItem());
        if (!existsSalesAllowances.isEmpty()) {
            exceptions.add(new Exception(rb.getString("salesAllowances.error.exists") + " " + vo.getOrderNumber() + "/" + vo.getItem()));
        }
        return exceptions;
    }
}
