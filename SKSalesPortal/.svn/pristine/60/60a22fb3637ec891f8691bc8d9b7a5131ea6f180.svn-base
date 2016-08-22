package com.tcci.sksp.controller.util;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkAccountsReceivable;
import com.tcci.sksp.entity.ar.SkAdvanceRemit;
import com.tcci.sksp.entity.ar.SkArRemitItem;
import com.tcci.sksp.entity.ar.SkArRemitMaster;
import com.tcci.sksp.entity.enums.AdvanceRemitTypeEnum;
import com.tcci.sksp.entity.enums.BankEnum;
import com.tcci.sksp.entity.enums.PaymentTypeEnum;
import com.tcci.sksp.entity.enums.RemitMasterStatusEnum;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkAdvanceRemitFacade;
import com.tcci.sksp.facade.SkArRemitItemFacade;
import com.tcci.sksp.facade.SkArRemitMasterFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lynn.Huang
 */
@ManagedBean
@ViewScoped
public class EditPaymentDetailController {

    //<editor-fold defaultstate="collapsed" desc="variables">
    private Logger logger = LoggerFactory.getLogger(EditPaymentDetailController.class);
    private SkSalesMember skSales;
    private SkCustomer skCustomer;
    private Date today;
    private SkArRemitMaster skArRemitMaster;
    private List<SkArRemitItem> skArRemitItemList;
    private List<BankEnum> bankList;
    private List<PaymentTypeEnum> paymentTypeList;
    private Long advancedReceiptsA, advancedReceiptsJ;
    private List<SkAdvanceRemit> advancedReceiptsAList, advancedReceiptsJList, advancedReceiptsList;
    private SkArRemitItem needDeleteItem;
    private Long cashTotal;
    private Long checkTotal;
    private String dialogTitle;
    private String from;
    private String returnPage_cancelRemit;
    private String returnPage_confirmRemit;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @ManagedProperty(value = "#{sessionController}")
    private SessionController userSession;

    public void setUserSession(SessionController userSession) {
        this.userSession = userSession;
    }

    @ManagedProperty(value = "#{selectOrderNumberController}")
    private SelectOrderNumberController selectOrderNumberController;

    public void setSelectOrderNumberController(SelectOrderNumberController selectOrderNumberController) {
        this.selectOrderNumberController = selectOrderNumberController;
    }

    @ManagedProperty(value = "#{queryCriteriaController}")
    private QueryCriteriaController queryCriteriaController;

    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }

    @EJB
    private SkAdvanceRemitFacade skAdvanceRemitFacade;
    @EJB
    private SkArRemitItemFacade skArRemitItemFacade;
    @EJB
    private SkArRemitMasterFacade skArRemitMasterFacade;
    @EJB
    private SkSalesMemberFacade skSalesMemberFacade;
    @EJB
    private SkAdvanceRemitFacade advanceRemitFacade;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getter, setter">
    public SkCustomer getSkCustomer() {
        return skCustomer;
    }

    public void setSkCustomer(SkCustomer skCustomer) {
        this.skCustomer = skCustomer;
    }

    public SkSalesMember getSkSales() {
        return skSales;
    }

    public Date getToday() {
        return today;
    }

    public SkArRemitMaster getSkArRemitMaster() {
        return skArRemitMaster;
    }

    public void setSkArRemitMaster(SkArRemitMaster skArRemitMaster) {
        this.skArRemitMaster = skArRemitMaster;
    }

    public List<SkArRemitItem> getSkArRemitItemList() {
        return skArRemitItemList;
    }

    public List<BankEnum> getBankList() {
        return bankList;
    }

    public List<PaymentTypeEnum> getPaymentTypeList() {
        return paymentTypeList;
    }

    public Long getAdvancedReceiptsA() {
        return advancedReceiptsA;
    }

    public Long getAdvancedReceiptsJ() {
        return advancedReceiptsJ;
    }

    public void setNeedDeleteItem(SkArRemitItem needDeleteItem) {
        this.needDeleteItem = needDeleteItem;
    }

    public Long getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(Long cashTotal) {
        this.cashTotal = cashTotal;
    }

    public Long getCheckTotal() {
        return checkTotal;
    }

    public void setCheckTotal(Long checkTotal) {
        this.checkTotal = checkTotal;
    }

    public List<SkAdvanceRemit> getAdvancedReceiptsList() {
        return advancedReceiptsList;
    }

    public void setAdvancedReceiptsList(List<SkAdvanceRemit> advancedReceiptsList) {
        this.advancedReceiptsList = advancedReceiptsList;
    }

    public List<SkAdvanceRemit> getAdvancedReceiptsAList() {
        return advancedReceiptsAList;
    }

    public void setAdvancedReceiptsAList(List<SkAdvanceRemit> advancedReceiptsAList) {
        this.advancedReceiptsAList = advancedReceiptsAList;
    }

    public List<SkAdvanceRemit> getAdvancedReceiptsJList() {
        return advancedReceiptsJList;
    }

    public void setAdvancedReceiptsJList(List<SkAdvanceRemit> advancedReceiptsJList) {
        this.advancedReceiptsJList = advancedReceiptsJList;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReturnPage_cancelRemit() {
        return returnPage_cancelRemit;
    }

    public void setReturnPage_cancelRemit(String returnPage_cancelRemit) {
        this.returnPage_cancelRemit = returnPage_cancelRemit;
    }

    public String getReturnPage_confirmRemit() {
        return returnPage_confirmRemit;
    }

    public void setReturnPage_confirmRemit(String returnPage_confirmRemit) {
        this.returnPage_confirmRemit = returnPage_confirmRemit;
    }

    //</editor-fold>
    @PostConstruct
    public void init() {
        today = new Date();
        FacesContext context = FacesContext.getCurrentInstance();
        SkArRemitMaster master = (SkArRemitMaster) context.getExternalContext().getFlash().get("skArRemitMaster");
        logger.debug("master={}", master);
        if (master == null) {
            skSales = selectOrderNumberController.getSkSales();
            skCustomer = selectOrderNumberController.getSkCustomer();
            initSkArRemitMaster();
            initSkArRemitItemList();
        } else {
            skArRemitMaster = master;
            skCustomer = master.getCustomer();
            selectOrderNumberController.setSkCustomer(skCustomer);
            skSales = skSalesMemberFacade.findByCode(master.getSapid());
            selectOrderNumberController.setSkSales(skSales);
            skArRemitItemList = new ArrayList<SkArRemitItem>();
            skArRemitItemList.addAll(master.getSkArRemitItemCollection());
            cashTotal = skArRemitMaster.getRemittanceAmount().longValue();
            checkTotal = skArRemitMaster.getCheckAmount().longValue();
        }
        //--Begin--Modified by nEO Fu on 20120223 advance remit by customer group.
        advancedReceiptsA = skAdvanceRemitFacade.getAdvancedAmountByCustomerGroup(skCustomer, AdvanceRemitTypeEnum.A) * (-1); // The advanced receipts should be displayed as positive number.
        advancedReceiptsJ = skAdvanceRemitFacade.getAdvancedAmountByCustomerGroup(skCustomer, AdvanceRemitTypeEnum.J) * (-1);
        advancedReceiptsAList = new ArrayList<SkAdvanceRemit>();
        advancedReceiptsJList = new ArrayList<SkAdvanceRemit>();
        List<SkAdvanceRemit> advanceRemitAs = advanceRemitFacade.findByCustomer(skCustomer, AdvanceRemitTypeEnum.A, null);
        advanceRemitAs = advanceRemitFacade.sortAdvanceRemit(advanceRemitAs);
        advancedReceiptsAList.addAll(advanceRemitAs);
        List<SkAdvanceRemit> advanceRemitJs = advanceRemitFacade.findByCustomer(skCustomer, AdvanceRemitTypeEnum.J, null);
        advanceRemitJs = advanceRemitFacade.sortAdvanceRemit(advanceRemitJs);
        advancedReceiptsJList.addAll(advanceRemitJs);
        if (skCustomer.getParentCustomer() != null) {
            List<SkAdvanceRemit> groupAdvanceRemitAs = advanceRemitFacade.findByCustomer(skCustomer.getParentCustomer(), AdvanceRemitTypeEnum.A, null);
            groupAdvanceRemitAs = advanceRemitFacade.sortAdvanceRemit(groupAdvanceRemitAs);
            advancedReceiptsAList.addAll(groupAdvanceRemitAs);
            List<SkAdvanceRemit> groupAdvanceRemitJs = advanceRemitFacade.findByCustomer(skCustomer.getParentCustomer(), AdvanceRemitTypeEnum.J, null);
            groupAdvanceRemitJs = advanceRemitFacade.sortAdvanceRemit(groupAdvanceRemitJs);
            advancedReceiptsJList.addAll(groupAdvanceRemitJs);
        }
        //---End---Modified by nEO Fu on 20120223 advance remit by customer group.
        initBanks();
        initPaymentTypes();

        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        from = (String) flash.get("from");

        returnPage_cancelRemit = queryCriteriaController.toReviewRemitPage();
        returnPage_confirmRemit = returnPage_cancelRemit + "&showConfirmMsg=true";
    }

    public String confirmInit(SkArRemitMaster master) {
        logger.debug("confirmInit(), master=()", master);
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().put("skArRemitMaster", master);
        return "confirmRemit.xhtml";
    }

    public void removeSkArRemitItem() {
        if (needDeleteItem != null) {
            // delete AR
            SkAccountsReceivable removedAR = null;
            List<SkAccountsReceivable> arlist = new ArrayList<SkAccountsReceivable>();
            for (SkAccountsReceivable ar : selectOrderNumberController.getSelectedArList()) {
                if (needDeleteItem.getInvoiceNumber().equals(ar.getInvoiceNumber())) {
                    removedAR = ar;
                    continue;
                }
                arlist.add(ar);
            }
            selectOrderNumberController.setSelectedArList((SkAccountsReceivable[]) arlist.toArray(new SkAccountsReceivable[arlist.size()]));
            selectOrderNumberController.setRemovedAR(removedAR);
            // delete RemitItem
            for (int i = 0; i < skArRemitItemList.size(); i++) {
                SkArRemitItem item = skArRemitItemList.get(i);
                if (needDeleteItem.getInvoiceNumber().equals(item.getInvoiceNumber())) {
                    skArRemitItemList.remove(i);
                    break;
                }
            }
        }
    }

    public String save() {
        if (validThenSave()) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, FacesUtil.getMessage("remitmaintenance.msg.save.success"));
        } else {
            return null;
        }
        return ("query".equals(from) ? "remit_query" : "remit_maintenance_step1");
    }

    public boolean validThenSave() {
        boolean validThenSave = validate();

        if (validThenSave) {
            TcUser loginUser = userSession.getUser();
            skArRemitMasterFacade.insertOrUpdate(skArRemitMaster, loginUser);
            skArRemitItemFacade.insertOrUpdate(skArRemitMaster, skArRemitItemList, loginUser);
        }
        return validThenSave;
    }

    public String confirm() {
        logger.debug("confirm()");
        if (validThenSave()) {
            logger.debug("master={}", skArRemitMaster);
            skArRemitMaster.setFinanceReviewer(userSession.getUser());
            skArRemitMaster.setReviewTimestamp(new Date());
            skArRemitMaster.setStatus(RemitMasterStatusEnum.REVIEWED);
            skArRemitMasterFacade.edit(skArRemitMaster);
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, FacesUtil.getMessage("remit.confirm.success"));
            return (StringUtils.isEmpty(returnPage_confirmRemit) ? "reviewRemit.xhtml" : returnPage_confirmRemit);
        } else {
            return null;
        }
    }

    public void initSkArRemitItemList() {
        SkAccountsReceivable[] selectedArs = selectOrderNumberController.getSelectedArList();
        if (skArRemitItemList == null) {
            skArRemitItemList = new ArrayList<SkArRemitItem>();
        }
        if (selectedArs != null && selectedArs.length > 0) {
            long cashAmount = (skArRemitMaster.getRemittanceAmount() == null ? 0 : skArRemitMaster.getRemittanceAmount().longValue());
            for (SkAccountsReceivable ar : selectedArs) {
                boolean existed = false;
                for (SkArRemitItem item : skArRemitItemList) {
                    if (item.getInvoiceNumber().equals(ar.getInvoiceNumber())) {
                        existed = true;
                        break;
                    }
                }
                if (!existed) {
                    SkArRemitItem item = new SkArRemitItem();
                    item.setArRemitMaster(skArRemitMaster);
                    item.setOrderNumber(ar.getOrderNumber());
                    item.setInvoiceNumber(ar.getInvoiceNumber());
                    item.setInvoiceTimestamp(ar.getInvoiceTimestamp());
                    item.setArAmount(ar.getAmount());
                    item.setPremiumDiscount(ar.getPremiumDiscount());
                    if (item.getArAmount() != null && item.getArAmount().longValue() >= 0) {
                        item.setPaymentType(PaymentTypeEnum.CASH);
                        item.setSalesDiscount(BigDecimal.ZERO);
                        item.setSalesReturn(BigDecimal.ZERO);
                        item.setNegativeAr(BigDecimal.ZERO);
                        item.setDifferenceCharge((short) 0);
                        item.setAdvanceReceiptsA(BigDecimal.ZERO);
                        item.setAdvanceReceiptsJ(BigDecimal.ZERO);
                        item.setAmount((item.getArAmount() == null ? BigDecimal.ZERO : item.getArAmount()).add(item.getPremiumDiscount() == null ? BigDecimal.ZERO : item.getPremiumDiscount()));
                        // add the second PaymentType                        
                        item.setPaymentType2(PaymentTypeEnum.CASH);
                        item.setAmount2(BigDecimal.ZERO);
                        cashAmount += item.getAmount().longValue();
                    }
                    skArRemitItemList.add(item);
                }
            }
            skArRemitMaster.setRemittanceAmount(BigDecimal.valueOf(cashAmount));
            skArRemitMaster.setCheckAmount(skArRemitMaster.getCheckAmount() == null ? BigDecimal.ZERO : skArRemitMaster.getCheckAmount());

            // refresh ArDataModel
            ArDataModel arDataModel = selectOrderNumberController.getArDataModel();
            if (arDataModel != null) {
                List<SkAccountsReceivable> data = (List<SkAccountsReceivable>) arDataModel.getWrappedData();
                data.removeAll(Arrays.asList(selectedArs));
                arDataModel.setWrappedData(data);
                selectOrderNumberController.setArDataModel(arDataModel);
            }
        }
    }

    /**
     * 1.付款方式為支票時, 支票號碼必填. 2.尾差不可以大於100. 3.沖帳金額不可小於0. 4.-AR加總不可大於選進來的-AR加總.
     * 5.預收A. 6.預收J. 7.現金總額. 8.支票總額.
     *
     * @return boolean 解繳單是否可儲存
     */
    private boolean validate() {
        boolean validate = true;
        long totalAR = 0;
        long totalNegativeAR = 0, totalNegativeARItems = 0;
        long totalAdvancedReceiptItems_A = 0, totalAdvancedReceiptItems_J = 0;
        if (skArRemitItemList.isEmpty()) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("remitmaintenance.msg.at.least.one.ordernumber"));
            //直接返回
            return false;
        }
        for (SkArRemitItem item : skArRemitItemList) {
            //1.付款方式為支票時, 支票號碼必填.
            if (item.getPaymentType() != null && item.getPaymentType().equals(PaymentTypeEnum.CHECK) && StringUtils.isEmpty(item.getCheckNumber())) {
                FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("remitmaintenance.msg.checknumber.empty.error"));
                validate = false;
            }
            if (item.getPaymentType2() != null && item.getPaymentType2().equals(PaymentTypeEnum.CHECK) && StringUtils.isEmpty(item.getCheckNumber2())) {
                FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("remitmaintenance.msg.checknumber.empty.error"));
                validate = false;
            }
            //2.尾差不可以大於99.
            if (item.getDifferenceCharge() != null && item.getDifferenceCharge() > 99) {
                FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("remitmaintenance.msg.tolerance.greaterthan.99.error"));
                validate = false;
            }
            //3.沖帳金額不可小於0.
            if (item.getAmount() != null && item.getAmount().longValue() < 0) {
                FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("remitmaintenance.msg.remitamount.error"));
                validate = false;
            }
            if (item.getAmount2() != null && item.getAmount2().longValue() < 0) {
                FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("remitmaintenance.msg.remitamount.error"));
                validate = false;
            }
            //加總選進來的-AR.
            if (item.getArAmount() != null && item.getArAmount().longValue() < 0) {
                totalNegativeAR += item.getArAmount().longValue() * (-1);
            } else {
                totalAR += item.getArAmount().longValue();
            }
            //加總-AR, 預收A & 預收J.
            totalNegativeARItems += (item.getNegativeAr() != null ? item.getNegativeAr().longValue() : 0);
            totalAdvancedReceiptItems_A += (item.getAdvanceReceiptsA() != null ? item.getAdvanceReceiptsA().longValue() : 0);
            totalAdvancedReceiptItems_J += (item.getAdvanceReceiptsJ() != null ? item.getAdvanceReceiptsJ().longValue() : 0);
        }
        //4.-AR加總不可大於選進來的-AR加總.
        if (totalNegativeARItems > totalNegativeAR) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("remitmaintenance.msg.negative.ar.error"));
            validate = false;
        }
        //5.預收A.
        if (advancedReceiptsA >= 0 && totalAdvancedReceiptItems_A > advancedReceiptsA) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("remitmaintenance.msg.advanced.receipt.A.error"));
            validate = false;
        }
        //6.預收J.
        if (advancedReceiptsJ >= 0 && totalAdvancedReceiptItems_J > advancedReceiptsJ) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("remitmaintenance.msg.advanced.receipt.J.error"));
            validate = false;
        }
        //7.現金總額.
        if (cashTotal == null) {
            cashTotal = Long.valueOf(0);
        }
        if (cashTotal != skArRemitMaster.getRemittanceAmount().longValue()) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("remitmaintenance.msg.cashtotal.error"));
            validate = false;
        }
        //8.支票總額.
        if (checkTotal == null) {
            checkTotal = Long.valueOf(0);
        }
        if (checkTotal != skArRemitMaster.getCheckAmount().longValue()) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("remitmaintenance.msg.checktotal.error"));
            validate = false;
        }
        return validate;
    }

    private void initSkArRemitMaster() {
        skArRemitMaster = new SkArRemitMaster();
        skArRemitMaster.setBank(BankEnum.CHINATRUST);
        logger.debug("skSales={}", skSales);
        skArRemitMaster.setSapid(skSales.getCode());
        skArRemitMaster.setCustomer(skCustomer);
        cashTotal = Long.valueOf(0);
        checkTotal = Long.valueOf(0);
    }

    private void initBanks() {
        bankList = new ArrayList<BankEnum>();
        bankList.addAll(Arrays.asList(BankEnum.values()));
    }

    private void initPaymentTypes() {
        paymentTypeList = new ArrayList<PaymentTypeEnum>();
        paymentTypeList.addAll(Arrays.asList(PaymentTypeEnum.values()));
    }

    public void changeAdvancedReceiptList(String type, List<SkAdvanceRemit> list) {
        logger.debug("list={}", list);
        advancedReceiptsList = list;
        if ("A".equals(type)) {
            dialogTitle = AdvanceRemitTypeEnum.A.getDisplayName();
        } else if ("J".equals(type)) {
            dialogTitle = AdvanceRemitTypeEnum.J.getDisplayName();
        }
    }

    public String goBack() {
        return ("query".equals(from) ? "remit_query" : "remit_maintenance_step1");
    }

}
