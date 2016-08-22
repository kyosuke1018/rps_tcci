/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.remit;

import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.controller.util.SelectCustomerController;
import com.tcci.sksp.controller.util.SelectEmployeeController;
import com.tcci.sksp.controller.util.SessionController;
import com.tcci.sksp.entity.SkBank;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkArRemitItem;
import com.tcci.sksp.entity.ar.SkCheckMaster;
import com.tcci.sksp.entity.enums.RemitMasterStatusEnum;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkArRemitItemFacade;
import com.tcci.sksp.facade.SkBankFacade;
import com.tcci.sksp.facade.SkCheckMasterFacade;
import com.tcci.sksp.facade.SkCustomerFacade;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.persistence.Tuple;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class CheckMasterMaintenanceController {

    //<editor-fold defaultstate="collapsed" desc="veriables">
    protected final Logger logger = LoggerFactory.getLogger(getClass().getCanonicalName());
    private final ResourceBundle rb = ResourceBundle.getBundle("messages");
    private List<SkCheckMaster> checkMasterList;
    private SkCheckMaster checkMaster;
    private List<SkArRemitItem> relatedArRemitItems = new ArrayList<SkArRemitItem>();
    private String action;
    private String previousAction;
    private String previousPage;
    private String salesCode;
    private String checkNumber;
    private String number;
    private String amount;
    private String modifier;
    private String query;
    private List<SkCustomer> customerFromARList;
    private String selectedCustomerFromAR;
    private String customerCode;
    private String customerName;
    private String bankCode;
    private String bankName;
    private List<String> errorMessages = new ArrayList<String>();
    private String orginalCheckNumber = "";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    SkArRemitItemFacade arRemitItemFacade;
    @EJB
    SkCheckMasterFacade checkMasterFacade;
    @EJB
    SkBankFacade bankFacade;
    @EJB
    SkCustomerFacade customerFacade;
    @EJB
    TcUserFacade userFacade;
    @ManagedProperty(value = "#{selectCustomerController}")
    private SelectCustomerController selectCustomerController;

    public void setSelectCustomerController(SelectCustomerController selectCustomerController) {
        this.selectCustomerController = selectCustomerController;
    }
    @ManagedProperty(value = "#{queryCriteriaController}")
    QueryCriteriaController queryCriteriaController;

    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }
    @ManagedProperty(value = "#{selectEmployeeController}")
    SelectEmployeeController selectEmployeeController;

    public void setSelectEmployeeController(SelectEmployeeController selectEmployeeController) {
        this.selectEmployeeController = selectEmployeeController;
    }
    @ManagedProperty(value = "#{sessionController}")
    SessionController userSession;

    public void setUserSession(SessionController userSession) {
        this.userSession = userSession;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getter, setter">

    public List<SkCheckMaster> getCheckMasterList() {
        return checkMasterList;
    }

    public void setCheckMasterList(List<SkCheckMaster> checkMasterList) {
        this.checkMasterList = checkMasterList;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSelectedCustomerFromAR() {
        return selectedCustomerFromAR;
    }

    public void setSelectedCustomerFromAR(String selectedCustomerFromAR) {
        this.selectedCustomerFromAR = selectedCustomerFromAR;
    }

    public SkCheckMaster getCheckMaster() {
        return checkMaster;
    }

    public void setCheckMaster(SkCheckMaster checkMaster) {
        this.checkMaster = checkMaster;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<SkCustomer> getCustomerFromARList() {
        return customerFromARList;
    }

    public void setCustomerFromARList(List<SkCustomer> customerFromARList) {
        this.customerFromARList = customerFromARList;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public String getPreviousAction() {
        return previousAction;
    }

    public void setPreviousAction(String previousAction) {
        this.previousAction = previousAction;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
    //</editor-fold>

    @PostConstruct
    public void init() {
        try {
            String action = FacesUtil.getRequestParameter("action");
            logger.debug("action=" + action);
            this.action = "";
            this.modifier = "";
            if (!StringUtils.isEmpty(FacesUtil.getRequestParameter("previousAction"))) {
                this.action = FacesUtil.getRequestParameter("previousAction");
            }
            this.salesCode = "";
            if (!StringUtils.isEmpty(FacesUtil.getRequestParameter("salesCode"))) {
                this.salesCode = FacesUtil.getRequestParameter("salesCode");
            }
            this.checkNumber = "";
            if (!StringUtils.isEmpty(FacesUtil.getRequestParameter("checkNumber"))) {
                this.checkNumber = FacesUtil.getRequestParameter("checkNumber");
            }
            this.modifier = "";
            if (!StringUtils.isEmpty(FacesUtil.getRequestParameter("modifier"))) {
                this.modifier = FacesUtil.getRequestParameter("modifier");
            }
            this.previousPage = "";
            if (!StringUtils.isEmpty(FacesUtil.getRequestParameter("previousPage"))) {
                this.previousPage = FacesUtil.getRequestParameter("previousPage");
            }
            this.number = "";
            if (!StringUtils.isEmpty(FacesUtil.getRequestParameter("number"))) {
                this.number = FacesUtil.getRequestParameter("number");
            }
            this.amount = "";
            if (!StringUtils.isEmpty(FacesUtil.getRequestParameter("amount"))) {
                this.amount = FacesUtil.getRequestParameter("amount");
            }
            this.query = "";
            if (!StringUtils.isEmpty(FacesUtil.getRequestParameter("query"))) {
                this.query = FacesUtil.getRequestParameter("query");
            }
            //<editor-fold defaultstate="collapsed" desc="debug message">
            logger.debug("previousAction={}", this.action);
            logger.debug("salesCode={}", this.salesCode);
            logger.debug("checkNumber={}", this.checkNumber);
            logger.debug("modifier={}", this.modifier);
            logger.debug("previousPage={}", this.previousPage);
            logger.debug("query={}", this.query);
            //</editor-fold>
            if ("create".equalsIgnoreCase(action)) {
                checkMaster = new SkCheckMaster();
                action = (String) FacesUtil.getRequestParameter("action");
            } else if ("update".equalsIgnoreCase(action)) {
                this.orginalCheckNumber = this.number;
                this.checkMaster = checkMasterFacade.findByCheckNumber(this.number);
                logger.info("checkMaster=" + checkMaster);
                relatedArRemitItems = arRemitItemFacade.findByCriteria(null, this.number, null);
                if (checkMaster == null) {
                    checkMaster = new SkCheckMaster();
                    checkMaster.setCheckNumber(this.number);
                    checkMaster.setAmount(new BigDecimal(Double.parseDouble(this.amount)));
                }
                initCustomerFromAR();
                if (previousPage.equals("check_query.xhtml")) {
                    if (customerFromARList == null || customerFromARList.isEmpty()) {
                        if (checkMaster.getCustomer() != null) {
                            customerFromARList = new ArrayList<SkCustomer>();
                            customerFromARList.add(checkMaster.getCustomer());
                        }
                    }
                }
                selectCustomerController.setSelectedCustomer(customerFromARList.get(0).getSimpleCode());
                selectCustomerController.setName(customerFromARList.get(0).getName());
                if (checkMaster.getId() != null) {
                    //init customer & bank
                    initBankAndCustomer();
                }
                action = (String) FacesUtil.getRequestParameter("action");
            } else if ("query".equalsIgnoreCase(action) || "finance_query".equalsIgnoreCase(action)) {
                logger.debug("query or");
                if ("true".equalsIgnoreCase(query)) {
                    queryCriteriaController.getFilter().setSales(new SkSalesMember());
                    queryCriteriaController.getFilter().getSales().setCode(this.salesCode);
                    queryCriteriaController.setCheckNumber(this.checkNumber);

                    boolean isCheckOne = true;
                    List<Tuple> checkNumberOneList = arRemitItemFacade.findCheckTotalAmount(
                            queryCriteriaController.getFilter().getSales(),
                            queryCriteriaController.getCheckNumber(), isCheckOne);
                    List<Tuple> checkNumberTwoList = arRemitItemFacade.findCheckTotalAmount(
                            queryCriteriaController.getFilter().getSales(),
                            queryCriteriaController.getCheckNumber(), !isCheckOne);
                    List<Tuple> checkTotalAmountList = new ArrayList<Tuple>();
                    checkTotalAmountList.addAll(checkNumberOneList);
                    checkTotalAmountList.addAll(checkNumberTwoList);
                    logger.debug("arRemitItemList.size()={}", checkTotalAmountList.size());
                    transferDataToList(checkTotalAmountList);
                    logger.debug("checkMasterList.size()={}", checkMasterList.size());
                }
            }
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(false);
        } catch (Exception ex) {
            logger.error("init(), e={}", ex);
        }
    }

    private void initBankAndCustomer() {
        if (getCheckMaster().getCustomer() != null) {
            setSelectedCustomerFromAR(getCheckMaster().getCustomer().getSimpleCode());
            selectCustomerController.setSelectedCustomer(getCheckMaster().getCustomer().getSimpleCode());
            selectCustomerController.setName(getCheckMaster().getCustomer().getName());
        }
        if (getCheckMaster().getBillingBank() != null) {
            this.setBankCode(getCheckMaster().getBillingBank().getCode());
            this.setBankName(getCheckMaster().getBillingBank().getName());
        }
    }

    private void initCustomerFromAR() {
        logger.debug("checkNumber={}", this.number);
        List<SkArRemitItem> list = arRemitItemFacade.findByCheckNumber(this.number);
        logger.debug("list = {}", list);
        if (list != null) {
            customerFromARList = new ArrayList<SkCustomer>();
            for (SkArRemitItem item : list) {
                SkCustomer customer = item.getArRemitMaster().getCustomer();
                if (!customerFromARList.contains(customer)) {
                    customerFromARList.add(customer);
                }
            }
        }
    }

    public void doSearchArRemitItemAction() {
        this.action = "query";
        this.salesCode = queryCriteriaController.getFilter().getSales().getCode();
        this.checkNumber = null == queryCriteriaController.getCheckNumber() ? "" : queryCriteriaController.getCheckNumber();
        logger.debug("this.checkNumber={}", this.checkNumber);
        this.modifier = "";
        this.query = "true";
        redirectPage("check_query.xhtml", false);
    }

    public void doSearchCheckMasterAction() {
        this.action = "finance_query";
        if (queryCriteriaController.getFilter().getSales() != null) {
            this.salesCode = queryCriteriaController.getFilter().getSales().getCode();
        } else {
            this.salesCode = "";
        }
        this.checkNumber = null == queryCriteriaController.getCheckNumber() ? "" : queryCriteriaController.getCheckNumber();
        this.modifier = selectEmployeeController.getSelectedUser();
        this.query = "true";
        redirectPage("finance_check_query.xhtml", false);
    }

    private void transferDataToList(List<Tuple> tupleList) throws Exception {
        Map<String, SkCheckMaster> checkMasterMap = new LinkedHashMap<String, SkCheckMaster>();
        if (tupleList != null) {
            for (Tuple tuple : tupleList) {
                String number = (String) tuple.get("checkNumber");
                Long customerId = (Long) tuple.get("customerId");
                BigDecimal amount = (BigDecimal) tuple.get("checkAmount");
                SkCheckMaster checkMaster = (SkCheckMaster) checkMasterMap.get(number);
                if (checkMaster == null) {
                    checkMaster = checkMasterFacade.findByCheckNumber(number);
                } else if (checkMaster.getId() == null) {
                    checkMaster.setAmount(checkMaster.getAmount().add(amount));
                    continue;
                }
                logger.debug("this.modifier={}", this.modifier);
                if ("".equals(this.modifier)) {
                    logger.debug("modifier blank");
                    if (checkMaster == null) {
                        checkMaster = new SkCheckMaster();
                        checkMaster.setCheckNumber(number);
                        checkMaster.setAmount(amount);
                        checkMaster.setCustomer(customerFacade.find(customerId));
                    }
                    checkMasterMap.put(number, checkMaster);
                } else {
                    logger.debug("modifier not blank");

                    if (checkMaster != null) {
                        logger.debug("checkMaster.getModifier()={}", checkMaster.getModifier());
                        logger.debug("checkMaster.getModifier().getEmpId()={}", checkMaster.getModifier().getEmpId());
                        if (checkMaster.getModifier() != null && this.modifier.equals(checkMaster.getModifier().getEmpId())) {
                            logger.debug("add");
                            checkMasterMap.put(number, checkMaster);
                        }
                    }
                }
            }
        }
        checkMasterList = new ArrayList<SkCheckMaster>();
        checkMasterList.addAll(checkMasterMap.values());
    }

    public String doSaveCheckMasterAction() {
        logger.info("doSaveCheckMasterAction start!");
        Date now = new Date();
        try {
            if (!StringUtils.isEmpty(bankCode)) {
                SkBank bank = (SkBank) bankFacade.findByCode(bankCode);
                if (bank != null) {
                    checkMaster.setBillingBank(bank);
                }
            }
            String simpleCode = "";
            if (this.previousPage.equals("check_query.xhtml")) {
                simpleCode = selectedCustomerFromAR;
            } else {
                simpleCode = selectCustomerController.getSelectedCustomer();
            }
            logger.debug("simpleCode={}", simpleCode);
            if (!StringUtils.isEmpty(simpleCode)) {
                SkCustomer customer = (SkCustomer) customerFacade.findBySimpleCode(simpleCode);
                if (customer != null) {
                    checkMaster.setCustomer(customer);
                }
            }
            logger.debug("checkMaster={}", checkMaster);
            if (!valid(checkMaster)) {
                for (String errorMessage : this.errorMessages) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    errorMessage,
                                    errorMessage));
                }
                return null;
            };
            //財務修改支票號碼
            for (SkArRemitItem arRemitItem : relatedArRemitItems) {
                if (this.orginalCheckNumber.equals(arRemitItem.getCheckNumber())) {
                    arRemitItem.setCheckNumber(checkMaster.getCheckNumber());
                }
                if (this.orginalCheckNumber.equals(arRemitItem.getCheckNumber2())) {
                    arRemitItem.setCheckNumber2(checkMaster.getCheckNumber());
                }
                arRemitItemFacade.edit(arRemitItem);
            }
            checkMaster.setModifier(userSession.getUser());
            checkMaster.setModifytimestamp(now);
            if (checkMaster.getId() == null) {
                checkMaster.setCreator(userSession.getUser());
                checkMaster.setCreatetimestamp(now);
                checkMasterFacade.create(checkMaster);

            } else {
                checkMasterFacade.edit(checkMaster);
            }
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, rb.getString("check.msg.success"));
            FacesContext context = FacesContext.getCurrentInstance();
            logger.debug("this.action={}", this.action);
            logger.debug("this.salesCode={}", this.salesCode);
            logger.debug("this.checkNumber={}", this.checkNumber);

            redirectPage(this.previousPage, true);
        } catch (Exception e) {
            //logger.finest(e.getMessage());
            logger.error("doSaveCheckMasterAction(), e={}", e);
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
        return null;
    }

    public boolean valid(SkCheckMaster checkMaster) {
        boolean valid = true;
        errorMessages.clear();
        //check number
        if (StringUtils.isEmpty(checkMaster.getCheckNumber())) {
            errorMessages.add(rb.getString("check.error.numberisrequired"));
            valid = false;
        } else {
            logger.debug("checkMaster={}", checkMaster);
            if (checkMaster.getId() == null) {
                SkCheckMaster existsCheck = checkMasterFacade.findByCheckNumber(checkMaster.getCheckNumber());
                if (existsCheck != null) {
                    errorMessages.add(rb.getString("check.error.numberexists"));
                    valid = false;
                }
            }
        }

        //billing account
        if (StringUtils.isEmpty(checkMaster.getBillingAccount())) {
            errorMessages.add(rb.getString("check.error.billingaccountisrequired"));
            valid = false;
        }

        //billing bank
        if (checkMaster.getBillingBank() == null) {
            errorMessages.add(rb.getString("check.error.billingbankisrequired"));
            valid = false;
        }

        //amount
        if (checkMaster.getAmount() == null || checkMaster.getAmount().doubleValue() == 0) {
            errorMessages.add(rb.getString("check.error.amountisrequired"));
            valid = false;
        } else if (!checkMaster.getAmount().abs().equals(checkMaster.getAmount())) {
            errorMessages.add(rb.getString("check.error.amountisnegative"));
            valid = false;
        }

        //due date
        if (checkMaster.getDueDate() == null) {
            errorMessages.add(rb.getString("check.error.duedateisrequired"));
            valid = false;
        }
        return valid;
    }

    private void redirectPage(String page, boolean keepMessage) {
        FacesContext context = FacesContext.getCurrentInstance();
        String url = page + "?"
                + "action="
                + this.action
                + "&"
                + "salesCode="
                + this.salesCode
                + "&"
                + "checkNumber="
                + this.checkNumber
                + "&"
                + "modifier="
                + (null == this.modifier ? "" : this.modifier)
                + "&"
                + "number="
                + this.number
                + "&"
                + "amount="
                + this.amount
                + "&"
                + "query="
                + this.query;
        if ("update".equalsIgnoreCase(this.action)
                || "create".equalsIgnoreCase(action)) {
            url = url + "&"
                    + "previousAction="
                    + this.previousAction
                    + "&"
                    + "previousPage="
                    + this.previousPage;
        }
        context.getExternalContext().getFlash().setKeepMessages(keepMessage);
        try {
            context.getExternalContext().redirect(url);
        } catch (Exception e) {
            logger.error("redirectPage, {}", e);
        }
    }

    public void editSetup() {
        logger.debug("editSetup()");
        FacesContext context = FacesContext.getCurrentInstance();
        this.action = "update";
        this.previousPage = (String) context.getExternalContext().getFlash().get("previousPage");
        String previousAction = "";
        if ("check_query.xhtml".equals(this.previousPage)) {
            previousAction = "query";
        } else {
            previousAction = "finance_query";
        }
        this.previousAction = previousAction;
        this.number = (String) context.getExternalContext().getFlash().get("number");
        BigDecimal amount = (BigDecimal) context.getExternalContext().getFlash().get("amount");
        if (amount != null) {
            DecimalFormat formatter = new DecimalFormat("######");
            this.amount = formatter.format(amount.doubleValue());
        }
        logger.debug("this.amount={}", this.amount);
        redirectPage("check_maintenance.xhtml", false);
    }

    public void createSetup() {
        FacesContext context = FacesContext.getCurrentInstance();
        this.action = "create";
        this.previousAction = "finance_query";
        this.previousPage = (String) context.getExternalContext().getFlash().get("previousPage");
        redirectPage("check_maintenance.xhtml", false);
    }

    public void cancel() {
        redirectPage(this.previousPage, false);
    }

    public boolean canEdit(String checkNumber) {
        boolean canEdit = true;
        List<SkArRemitItem> arRemitItems = arRemitItemFacade.findByCheckNumber(checkNumber);
        logger.debug("checkNumber={}", checkNumber);
        for (SkArRemitItem arRemitItem : arRemitItems) {
            logger.debug("arRemitItem={}", arRemitItem);
            if (!(arRemitItem.getArRemitMaster().getStatus().equals(RemitMasterStatusEnum.NOT_YET) || arRemitItem.getArRemitMaster().getStatus().equals(RemitMasterStatusEnum.CANCELED))) {
                canEdit = false;
            }
            break;
        }
        return canEdit;
    }

    public List<String> completeBank(String code) {
        List<SkBank> banks = bankFacade.findByCriteria(code, null);
        List<String> bankCodes = new ArrayList<String>();
        for (SkBank bank : banks) {
            bankCodes.add(bank.getCode());
        }
        return bankCodes;
    }
    
    public List<String> findBillingAccount(String account)
    {
        List<String> list = this.checkMasterFacade.findBillingAccount(account);
        return list;
    }

    public void handleSelect(SelectEvent event) {
        logger.debug("handleSelect(), selection={}", event.getObject());
        SkBank bank = bankFacade.findByCode((String) event.getObject());
        setBankName(bank.getName());
        setBankCode(bank.getCode());
    }
    
    public void findBank(SelectEvent event)
    {
        String account = (String)event.getObject();
        SkBank skBank = this.checkMasterFacade.findSkBankByBillingAccount(account);
        this.setBankName(skBank.getName());
        this.setBankCode(skBank.getCode());
        
        
        
    }

    public String findBankName(String bankCode) {
        SkBank bank = bankFacade.findByCode(bankCode);
        if (bank != null) {
            return bank.getName();
        } else {
            return "";
        }
    }
}
