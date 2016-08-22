package com.tcci.sksp.controller.remit;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.SelectCustomerController;
import com.tcci.sksp.controller.util.SelectEmployeeController;
import com.tcci.sksp.controller.util.SessionController;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkAdvancePayment;
import com.tcci.sksp.entity.ar.SkArRemitItem;
import com.tcci.sksp.entity.ar.SkArRemitMaster;
import com.tcci.sksp.entity.ar.SkCheckMaster;
import com.tcci.sksp.entity.ar.SkFiMasterInterface;
import com.tcci.sksp.entity.enums.PaymentTypeEnum;
import com.tcci.sksp.entity.enums.RemitMasterStatusEnum;
import com.tcci.sksp.facade.AdvancePaymentFilter;
import com.tcci.sksp.facade.SkAdvancePaymentFacade;
import com.tcci.sksp.facade.SkArRemitMasterFacade;
import com.tcci.sksp.facade.SkCheckMasterFacade;
import com.tcci.sksp.facade.SkCustomerFacade;
import com.tcci.sksp.facade.SkFiMasterInterfaceFacade;
import com.tcci.sksp.vo.AdvancePaymentVO;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@ManagedBean
@ViewScoped
public class AdvancePaymentController {

    //<editor-fold defaultstate="collapsed" desc="parameters">
    protected final static Logger logger = LoggerFactory.getLogger(AdvancePaymentController.class);
    private ResourceBundle rb = ResourceBundle.getBundle("messages");
    private String action;
    private String remitNumber;
    private String modifier;
    private Date beginModifytimestamp;
    private String beginDate;
    private Date endModifytimestamp;
    private String endDate;
    private AdvancePaymentFilter filter = new AdvancePaymentFilter();
    private String submit;
    private String oid;
    private List<AdvancePaymentVO> advancePaymentVOs = new ArrayList<AdvancePaymentVO>();
    private AdvancePaymentVO advancePaymentVO;
    private SkAdvancePayment advancePayment;
    private Map<Integer, SkCheckMaster> checks = new HashMap<Integer, SkCheckMaster>();
    private List<String> errorMessages;
    private boolean selectAll;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    SkAdvancePaymentFacade facade;
    @EJB
    TcUserFacade userFacade;
    @EJB
    SkCheckMasterFacade checkFacade;
    @EJB
    SkArRemitMasterFacade remitMasterFacade;
    @EJB
    SkCustomerFacade customerFacade;
    @EJB
    SkFiMasterInterfaceFacade interfaceFacade;
    @ManagedProperty(value = "#{selectCustomerController}")
    private SelectCustomerController selectCustomerController;

    public void setSelectCustomerController(SelectCustomerController selectCustomerController) {
        this.selectCustomerController = selectCustomerController;
    }
    @ManagedProperty(value = "#{selectEmployeeController}")
    private SelectEmployeeController selectEmployeeController;

    public void setSelectEmployeeController(SelectEmployeeController selectEmployeeController) {
        this.selectEmployeeController = selectEmployeeController;
    }
    @ManagedProperty(value = "#{sessionController}")
    private SessionController userSession;

    public void setUserSession(SessionController userSession) {
        this.userSession = userSession;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getter, setter">

    public String getRemitNumber() {
        return remitNumber;
    }

    public void setRemitNumber(String remitNumber) {
        this.remitNumber = remitNumber;
    }

    public Date getBeginModifytimestamp() {
        return beginModifytimestamp;
    }

    public void setBeginModifytimestamp(Date beginModifytimestamp) {
        this.beginModifytimestamp = beginModifytimestamp;
    }

    public Date getEndModifytimestamp() {
        return endModifytimestamp;
    }

    public void setEndModifytimestamp(Date endModifytimestamp) {
        this.endModifytimestamp = endModifytimestamp;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public SkAdvancePayment getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(SkAdvancePayment advancePayment) {
        this.advancePayment = advancePayment;
    }

    public Map<Integer, SkCheckMaster> getChecks() {
        return checks;
    }

    public void setChecks(Map<Integer, SkCheckMaster> checks) {
        this.checks = checks;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<AdvancePaymentVO> getAdvancePaymentVOs() {
        return advancePaymentVOs;
    }

    public void setAdvancePaymentVOs(List<AdvancePaymentVO> advancePaymentVOs) {
        this.advancePaymentVOs = advancePaymentVOs;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }
    //</editor-fold>

    @PostConstruct
    private void init() {
        logger.debug("init()");
        String action = FacesUtil.getRequestParameter("action");
        this.action = "";
        try {
            if (!StringUtils.isEmpty(FacesUtil.getRequestParameter("previousAction"))) {
                this.action = FacesUtil.getRequestParameter("previousAction");
            }

            if (!StringUtils.isEmpty(FacesUtil.getRequestParameter("remitNumber"))) {
                String remitNumber = FacesUtil.getRequestParameter("remitNumber");
                this.setRemitNumber(remitNumber);
                this.filter.setRemitNumber(remitNumber);
            } else {
                this.setRemitNumber("");
                this.filter.setRemitNumber("");
            }
            if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("modifier")) {
                String modifier = FacesUtil.getRequestParameter("modifier");
                if (!StringUtils.isEmpty(modifier)) {
                    TcUser user = userFacade.findUserByEmpId(modifier);
                    if (user == null) {
                        user = new TcUser();
                        user.setCname("維護者不存在!");
                    }
                    selectEmployeeController.setInputUserCname(user.getCname());
                    selectEmployeeController.setInputUser(modifier);
                    this.filter.setModifier(user);
                } else {
                    selectEmployeeController.setInputUserCname("");
                    selectEmployeeController.setInputUser("");
                    this.filter.setModifier(null);

                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            if (!StringUtils.isEmpty(FacesUtil.getRequestParameter("beginDate"))) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(sdf.parse(FacesUtil.getRequestParameter("beginDate")));
                this.setBeginModifytimestamp(calendar.getTime());
                this.filter.setBeginModifytimestamp(calendar.getTime());
            } else {
                this.setBeginModifytimestamp(null);
                this.filter.setBeginModifytimestamp(null);
            }
            if (!StringUtils.isEmpty(FacesUtil.getRequestParameter("endDate"))) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(sdf.parse(FacesUtil.getRequestParameter("endDate")));
                this.setEndModifytimestamp(calendar.getTime());
                calendar.add(Calendar.DATE, 1);
                this.filter.setEndModifytimestamp(calendar.getTime());
            } else {
                this.setEndModifytimestamp(null);
                this.filter.setEndModifytimestamp(null);
            }

            if (!StringUtils.isEmpty(FacesUtil.getRequestParameter("submit"))) {
                this.submit = FacesUtil.getRequestParameter("submit");
            } else {
                this.submit = "";
                Date now = new Date();
                this.setBeginModifytimestamp(now);
                this.setEndModifytimestamp(now);
            }

            if (!StringUtils.isEmpty(FacesUtil.getRequestParameter("oid"))) {
                this.oid = FacesUtil.getRequestParameter("oid");
            } else {
                this.oid = "";
            }
            //<editor-fold defaultstate="collapsed" desc="debug message">
            logger.debug("action={}", action);
            logger.debug("previousAction={}", this.action);
            logger.debug("remitNumber={}", remitNumber);
            logger.debug("modifier={}", this.modifier);
            logger.debug("submit={}", this.submit);
            logger.debug("oid={}", this.oid);
            //</editor-fold>
            if ("query".equalsIgnoreCase(action)) {
                advancePaymentVOs.clear();
                if ("true".equalsIgnoreCase(submit)) {
                    List<SkAdvancePayment> advancePayments = facade.findByCriteria(this.filter);
                    logger.debug("advancePayments.size()={}", advancePayments.size());
                    for (SkAdvancePayment advancePayment : advancePayments) {
                        AdvancePaymentVO vo = new AdvancePaymentVO(advancePayment);
                        List<SkCheckMaster> checkMasters = checkFacade.findByAdvancePayment(advancePayment);
                        vo.setSelectable(true);
                        advancePaymentVOs.add(vo);
                        for (SkCheckMaster checkMaster : checkMasters) {
                            AdvancePaymentVO checkVO = new AdvancePaymentVO(advancePayment);
                            checkVO.setCheckMaster(checkMaster);
                            checkVO.setSelectable(false);
                            advancePaymentVOs.add(checkVO);
                        }
                    }
                } else if (!StringUtils.isEmpty(this.oid)) {
                    SkAdvancePayment advancePayment = (SkAdvancePayment) facade.getObject(this.oid);
                    AdvancePaymentVO vo = new AdvancePaymentVO(advancePayment);
                    List<SkCheckMaster> checkMasters = checkFacade.findByAdvancePayment(advancePayment);
                    for (SkCheckMaster checkMaster : checkMasters) {
                        AdvancePaymentVO checkVO = new AdvancePaymentVO(advancePayment);
                        checkVO.setCheckMaster(checkMaster);
                        checkVO.setSelectable(false);
                        advancePaymentVOs.add(checkVO);
                    }
                    advancePaymentVOs.add(vo);
                }
            } else if ("edit".equalsIgnoreCase(action)) {
                edit((SkAdvancePayment) facade.getObject(this.oid));
            } else if ("create".equalsIgnoreCase(action)) {
                if (StringUtils.isEmpty(this.oid)) {
                    create();
                } else {
                    String missingChecks = create((SkArRemitMaster) facade.getObject(this.oid));
                    if (!StringUtils.isEmpty(missingChecks)) {
                        for (String checkNumber : missingChecks.split(",")) {
                            Object[] args = new Object[]{checkNumber};
                            FacesUtil.addFacesMessage(null, FacesMessage.SEVERITY_WARN, MessageFormat.format(rb.getString("advancepayment.msg.checkmastermissing"), args));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("init(), e={}", e);
            FacesUtil.addFacesMessage(null, FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage());
        }
    }

    public void query() {
        logger.debug("query()");
        //creator
        String empId = "";
        if (selectEmployeeController.getSelectedUser() != null) {
            empId = selectEmployeeController.getSelectedUser();
        }
        if (!StringUtils.isEmpty(empId)) {
            TcUser user = userFacade.findUserByEmpId(empId);
            if (user == null) {
                user = new TcUser();
                user.setCname("建立者不存在!");
            }
            selectEmployeeController.setCname(user.getCname());
        } else {
            selectEmployeeController.setCname("");
        }
        this.modifier = selectEmployeeController.getSelectedUser();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if (beginModifytimestamp != null) {
            this.beginDate = sdf.format(beginModifytimestamp);
        } else {
            this.beginDate = "";
        }
        if (endModifytimestamp != null) {
            this.endDate = sdf.format(endModifytimestamp);
        } else {
            this.endDate = "";
        }
        String page = "advancePayment.xhtml";
        this.action = "query";
        this.submit = "true";
        redirectPage(page, false);
    }

    private void redirectPage(String page, boolean keepMessage) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            String url = page
                    + "?"
                    + "action="
                    + this.action
                    + "&"
                    + "remitNumber="
                    + this.remitNumber
                    + "&"
                    + "submit="
                    + this.submit
                    + "&"
                    + "oid="
                    + this.oid;
            if (!StringUtils.isEmpty(this.modifier)) {
                 url = url + "&"
                    + "modifier="
                    + this.modifier;                
            }
            if (!StringUtils.isEmpty(this.beginDate)) {
                url = url + "&"
                        + "beginDate="
                        + this.beginDate;
            }
            if (!StringUtils.isEmpty(this.endDate)) {
                url = url + "&"
                        + "endDate="
                        + this.endDate;
            }

            context.getExternalContext().getFlash().setKeepMessages(keepMessage);
            context.getExternalContext().redirect(url);
        } catch (Exception e) {
            logger.error("redirectPage(), e={}", e);
        }
    }

    public double getCheckTotalAmount() {
        double totalAmount = 0;
        for (SkCheckMaster checkMaster : checks.values()) {
            totalAmount += checkMaster.getAmount().doubleValue();
        }
        return totalAmount;
    }

    public double getCheckTotalAmount(SkAdvancePayment advancePayment) {
        logger.debug("advancePayment={}", advancePayment);
        double totalAmount = 0;
        for (SkCheckMaster checkMaster : advancePayment.getSkCheckMasterCollection()) {
            totalAmount += checkMaster.getAmount().doubleValue();
        }
        return totalAmount;
    }

    private void create() {
        advancePayment = new SkAdvancePayment();
        advancePayment.setAmount(new BigDecimal(0));
        checks.clear();
    }

    private String create(SkArRemitMaster remitMaster) {
        logger.debug("create(), remitMaster={}", remitMaster);
        advancePayment = new SkAdvancePayment();
        advancePayment.setArRemitMaster(remitMaster);
        advancePayment.setAmount(remitMaster.getRemittanceAmount());
        advancePayment.setCustomer(remitMaster.getCustomer());
        selectCustomerController.setSelectedCustomer(remitMaster.getCustomer().getSimpleCode());
        selectCustomerController.setName(remitMaster.getCustomer().getName());
        checks.clear();
        String ngChecks = "";
        for (SkArRemitItem item : remitMaster.getSkArRemitItemCollection()) {
            if (PaymentTypeEnum.CHECK.equals(item.getPaymentType())
                    && !StringUtils.isEmpty(item.getCheckNumber())) {
                SkCheckMaster check = checkFacade.findByCheckNumber(item.getCheckNumber());
                logger.debug("check={}", check);
                if (check != null) {
                    checks.put(System.identityHashCode(check), check);
                } else {
                    if (ngChecks.length() > 0) {
                        ngChecks += ",";
                    }
                    ngChecks += item.getCheckNumber();
                }
            }
        }
        return ngChecks;
    }

    private void edit(SkAdvancePayment advancePayment) {
        logger.debug("edit(), advancePayment={}", advancePayment);
        this.advancePayment = advancePayment;
        selectCustomerController.setSelectedCustomer(advancePayment.getCustomer().getSimpleCode());
        selectCustomerController.setName(advancePayment.getCustomer().getName());
        checks.clear();
        for (SkCheckMaster check : advancePayment.getSkCheckMasterCollection()) {
            logger.debug("check=" + check);
            checks.put(new Integer(System.identityHashCode(check)), check);
        }
    }

    public String remove() {
        //reset remit's status to not yet.
        SkArRemitMaster remitMaster = advancePaymentVO.getAdvancePayment().getArRemitMaster();
        if (remitMaster != null) {
            remitMaster.setStatus(RemitMasterStatusEnum.NOT_YET);
            remitMasterFacade.edit(remitMaster);
            //remove link between advance payment & check master only.
            for (SkCheckMaster checkMaster : advancePaymentVO.getAdvancePayment().getSkCheckMasterCollection()) {
                checkMaster.setAdvancePayment(null);
                checkFacade.edit(checkMaster);
            }
            //remove check master if advance payment not generate by remit master.
        } else {
            for (SkCheckMaster checkMaster : advancePaymentVO.getAdvancePayment().getSkCheckMasterCollection()) {
                checkFacade.remove(checkMaster);
            }
        }
        facade.remove(advancePaymentVO.getAdvancePayment());
        advancePaymentVOs.remove(advancePaymentVO);
        return null;
    }

    public String save() {
        Date now = new Date();
        TcUser sessionUser = userSession.getUser();
        String successMessage = "";

        try {
            //customer
            String customerCode = "";
            if (selectCustomerController.getSelectedCustomer() != null) {
                customerCode = selectCustomerController.getSelectedCustomer();
            }
            logger.debug("customerCode={}", customerCode);
            if (!StringUtils.isEmpty(customerCode)) {
                SkCustomer customer = customerFacade.findBySimpleCode(customerCode);
                logger.debug("customer={}", customer);
                if (customer == null) {
                    customer = new SkCustomer();
                    customer.setName(rb.getString("customer.msg.notexists"));
                }
                advancePayment.setCustomer(customer);
                selectCustomerController.setName(customer.getName());
            } else {
                selectCustomerController.setName("");
            }
            //default 0 if amount is null.
            if (advancePayment.getAmount() == null) {
                advancePayment.setAmount(new BigDecimal(0));
            }
            valid();
            if (!errorMessages.isEmpty()) {
                for (String errorMessage : errorMessages) {
                    FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage);
                }
                return null;
            }
            //mean create, need assign creator & createtimestamp.
            if (advancePayment.getId() == null) {
                advancePayment.setCreator(sessionUser);
                advancePayment.setCreatetimestamp(now);
                if (advancePayment.getArRemitMaster() != null) {
                    successMessage = rb.getString("advancepayment.msg.createbyremitsuccess");
                } else {
                    successMessage = rb.getString("advancepayment.msg.createsuccess");
                }
            } else {
                successMessage = rb.getString("advancepayment.msg.updatesuccess");
            }
            advancePayment.setModifier(sessionUser);
            advancePayment.setModifytimestamp(now);
            advancePayment = facade.editPayment(advancePayment);
            //update remit master's status to TRANSFER_ADVANCE.
            if (advancePayment.getArRemitMaster() != null) {
                advancePayment.getArRemitMaster().setStatus(RemitMasterStatusEnum.TRANSFER_ADVANCE);
                remitMasterFacade.edit(advancePayment.getArRemitMaster());
            } else {
                //remove exists check master for advance payment not create by remit.
                for (SkCheckMaster checkMaster : advancePayment.getSkCheckMasterCollection()) {
                    checkFacade.remove(checkMaster);
                }
            }
            //link check master to advance payment
            List<SkCheckMaster> newCheckMasters = new ArrayList<SkCheckMaster>();
            for (SkCheckMaster checkMaster : getCheckMasters()) {
                checkMaster.setCustomer(advancePayment.getCustomer());
                if (checkMaster.getId() == null) {
                    checkMaster.setCreator(sessionUser);
                    checkMaster.setCreatetimestamp(now);
                }
                checkMaster.setModifier(sessionUser);
                checkMaster.setModifytimestamp(now);
                checkMaster.setAdvancePayment(advancePayment);
                checkMaster = checkFacade.editCheckMaster(checkMaster);
                newCheckMasters.add(checkMaster);
            }
            advancePayment.getSkCheckMasterCollection().clear();
            advancePayment.setSkCheckMasterCollection(newCheckMasters);
            facade.edit(advancePayment);
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, successMessage);
        } catch (Exception e) {
            logger.error("save(), e={}", e);
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage());
            return null;
        }
        this.action = "query";
        this.oid = advancePayment.toString();
        redirectPage("advancePayment.xhtml", true);
        return null;
    }

    public void valid() {
        errorMessages = new ArrayList<String>();
        //customer
        logger.debug("advancePayment.getCustomer()={}", advancePayment.getCustomer());
        if (advancePayment.getCustomer().getName().equals(rb.getString("customer.msg.notexists"))) {
            errorMessages.add(rb.getString("customer.msg.notexists"));
        }

        //cash
        if (advancePayment.getAmount().doubleValue() < 0) {
            errorMessages.add(rb.getString("advancepayment.msg.cashshouldbeapostivenumber"));
        } else if (advancePayment.getAmount().doubleValue() == 0
                && checks.isEmpty()) {
            errorMessages.add(rb.getString("advancepayment.msg.cashorcheckrequired"));
        }
    }

    public String getTitle() {
        //mean create
        logger.debug("advancePayment={}", advancePayment);
        if (advancePayment.getId() == null) {
            if (advancePayment.getArRemitMaster() != null) {
                return rb.getString("advancepayment.label.createByRemit");
            } else {
                return rb.getString("advancepayment.label.create");
            }
        } else {
            return rb.getString("advancepayment.label.update");
        }
    }

    public void removeCheck(SkCheckMaster check) {
        Integer key = System.identityHashCode(check);
        logger.debug("check={}", check);
        logger.debug("key={}", key);
        checks.remove(key);
    }

    public void createOrEditSetup(String oid) {
        try {
            if (StringUtils.isEmpty(oid)) {
                this.action = "create";
                this.oid = "";
            } else {
                Object obj = facade.getObject(oid);
                SkArRemitMaster remitMaster = null;
                SkAdvancePayment advancePayment = null;
                if (obj instanceof SkArRemitMaster) {
                    remitMaster = (SkArRemitMaster) obj;
                    advancePayment = facade.findByRemitMaster(remitMaster);
                } else if (obj instanceof SkAdvancePayment) {
                    advancePayment = (SkAdvancePayment) obj;
                }
                if (advancePayment == null) {
                    this.action = "create";
                    this.oid = oid;
                } else {
                    this.action = "edit";
                    this.oid = advancePayment.toString();
                }
            }
            redirectPage("editAdvancePayment.xhtml", false);
        } catch (Exception e) {
            logger.error("createOrEditSetup(), e={}", e);
        }
    }

    public List<SkCheckMaster> getCheckMasters() {
        List<SkCheckMaster> checkMasters = new ArrayList<SkCheckMaster>();
        if (checks.size() > 0) {
            for (SkCheckMaster checkMaster : checks.values()) {
                checkMasters.add(checkMaster);
            }
        }
        return checkMasters;
    }

    public void cancel() {
        //TODO: 由解繳單轉預收是否要轉回至reviewRemit.xhtml頁面 (前提是reviewRemit.xhtml必須改成 redirect 的寫法).
        this.action = "query";
        this.oid = "";
        redirectPage("advancePayment.xhtml", false);
    }

    public void selectAllChange(AjaxBehaviorEvent event) {
        for (AdvancePaymentVO vo : advancePaymentVOs) {
            if (vo.isSelectable()) {
                vo.setSelected(selectAll);
            }
        }
    }

    public void setAdvancePaymentVOToDelete(AdvancePaymentVO vo) {
        this.advancePaymentVO = vo;
    }
}
