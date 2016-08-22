package com.tcci.sksp.controller.util;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkArRemitItem;
import com.tcci.sksp.entity.enums.OrderTypeEnum;
import com.tcci.sksp.entity.enums.PaymentTermEnum;
import com.tcci.sksp.entity.enums.RemitMasterStatusEnum;
import com.tcci.sksp.entity.org.SkSalesChannels;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.RemitFilter;
import com.tcci.sksp.facade.SkArRemitItemFacade;
import com.tcci.sksp.facade.SkArRemitMasterFacade;
import com.tcci.sksp.facade.SkCustomerFacade;
import com.tcci.sksp.facade.SkSalesChannelsFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lynn.Huang
 */
@ManagedBean
@ViewScoped
public class QueryCriteriaController {

    protected final static Logger logger = LoggerFactory.getLogger(QueryCriteriaController.class);
    @EJB
    private SkCustomerFacade customerFacade;
    @EJB
    private SkSalesMemberFacade memberFacade;
    @EJB
    private SkSalesChannelsFacade channelsFacade;
    @EJB
    private SkArRemitMasterFacade remitMasterFacade;
    @EJB
    private SkArRemitItemFacade remitDetailFacade;
    @EJB
    private TcUserFacade userFacade;
    @ManagedProperty(value = "#{selectCustomerController}")
    SelectCustomerController selectCustomerController;
    @ManagedProperty(value = "#{selectEmployeeController}")
    SelectEmployeeController selectEmployeeController;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;

    public void setSelectCustomerController(SelectCustomerController selectCustomerController) {
        this.selectCustomerController = selectCustomerController;
    }

    public void setSelectEmployeeController(SelectEmployeeController selectEmployeeController) {
        this.selectEmployeeController = selectEmployeeController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }
    private List<SkSalesMember> salesList = new ArrayList<SkSalesMember>();
    private List<SelectItem> paymentTermList = new ArrayList<SelectItem>();
    private List<SelectItem> yearList = new ArrayList<SelectItem>();
    private List<SelectItem> monthList = new ArrayList<SelectItem>();
    private List<SelectItem> orderTypeList = new ArrayList<SelectItem>();
    private boolean salesSelectable = true;
    private boolean renderInvoiceStart;
    private boolean invalidCustomerRelation;
    private boolean customerNotExists;
    private String checkNumber = ""; //use in check maintenance
    private String productNumber; //產品號碼, 訂單報表查詢
    private RemitFilter filter = new RemitFilter();
    private List<SelectItem> statusList = new ArrayList<SelectItem>();
    //private HtmlInputHidden initInvoiceStartHidden = new HtmlInputHidden();
    //private HtmlInputHidden initInvoiceEndHidden = new HtmlInputHidden();
    private HtmlInputHidden initSalesHidden = new HtmlInputHidden();
    private HtmlInputHidden initRemitTimeRangeHidden = new HtmlInputHidden();
    private HtmlInputHidden initSalesCustomerOrderDateStartEndHidden = new HtmlInputHidden();
    private HtmlInputHidden initSalesInvoiceNotStartEndAsYesterdayHidden = new HtmlInputHidden();
    private HtmlInputHidden initSalesInvoiceAsLastMonthHidden = new HtmlInputHidden();
    private HtmlInputHidden initSalesRemitTimeRangeHidden = new HtmlInputHidden();
    private HtmlInputHidden initSalesYearMonthHidden = new HtmlInputHidden();
    private HtmlInputHidden initSalesAndCheckNumberHidden = new HtmlInputHidden();
    private HtmlInputHidden initFinanceSalesAndCheckNumberHidden = new HtmlInputHidden();
    private HtmlInputHidden initSalesCustomerOrderYearlyHidden = new HtmlInputHidden();
    private HtmlInputHidden initStatusList = new HtmlInputHidden();
    private HtmlInputHidden initStatusWithDefaultList = new HtmlInputHidden();
    private HtmlInputHidden initSalesStatusYearMonthHidden = new HtmlInputHidden();
    private HtmlInputHidden initOrderDateStartEndHidden = new HtmlInputHidden();
    private HtmlInputHidden initSalesInvoiceTimestampHidden = new HtmlInputHidden();
    private HtmlInputHidden initSalesOrderDateStartEndHidden = new HtmlInputHidden(); //退貨事後折讓明細表
    private HtmlInputHidden initSalesCustomerInvoiceStartEndHidden = new HtmlInputHidden(); //訂單報表
    private HtmlInputHidden initSalesDefaultBlankHidden = new HtmlInputHidden(); //報價單查詢
    private final String DONE = "done";

    @PostConstruct
    public void init() {
        /*
         * salesSelectable = true; Date now = new Date();
         * filter.setMonth(String.valueOf(now.getMonth() + 1)); initSalesList();
         * //initInvoiceDateRange(); initStatusList(); initRemitMinMaxNumber();
         */
    }

    private void initRemitMinMaxNumber() {
        Date to = new Date();
        Date from = new Date(to.getYear(), to.getMonth(), 1);
        logger.debug("from={}", from);
        logger.debug("to={}", to);
        initRemitMinMaxNumber(from, to);
    }

    private void initRemitMinMaxNumber(Date from, Date to) {
        if (from == null || to == null) {
            return;
        }
        int min = remitMasterFacade.getRemitMinId(from, to);
        logger.debug("min={}", min);
        int max = remitMasterFacade.getRemitMaxId(from, to);
        logger.debug("max={}", max);
        filter.setMinRemitNumber(min);
        filter.setMaxRemitNumber(max);
        filter.setPayingStart(from); //put createtimestamp start here.
        filter.setPayingEnd(to); //put createtimestamp end here.
    }

    public void changeMinMaxNumber(AjaxBehaviorEvent event) {
        logger.debug("year={}", filter.getYear());
        logger.debug("month={}", filter.getMonth());
        Date from = new GregorianCalendar(
                Integer.parseInt(filter.getYear()), //year
                Integer.parseInt(filter.getMonth()) - 1, //month
                1 //date
        ).getTime();
        Calendar calendar = new GregorianCalendar(
                Integer.parseInt(filter.getYear()), //year
                Integer.parseInt(filter.getMonth()) - 1, //month
                1, //date
                23, //hour
                59, //minute
                59 //second
        );
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        Date to = calendar.getTime();
        logger.debug("from={}", from);
        logger.debug("to={}", to);
        initRemitMinMaxNumber(from, to);
        filter.setPayingStart(from); //put createtimestamp start here.
        filter.setPayingEnd(to); //put createtimestamp end here.
    }

    private void initStatusList() {
        statusList.clear();
        //RemitMasterStatusEnum[] remitMasterStatusEnums = RemitMasterStatusEnum.values();
        RemitMasterStatusEnum[] remitMasterStatusEnums = new RemitMasterStatusEnum[]{
            RemitMasterStatusEnum.NOT_YET,
            RemitMasterStatusEnum.REVIEWED,
            RemitMasterStatusEnum.TRANSFER_ADVANCE,
            RemitMasterStatusEnum.CANCELED};

        for (int i = 0; i < remitMasterStatusEnums.length; i++) {
            statusList.add(new SelectItem(remitMasterStatusEnums[i], remitMasterStatusEnums[i].getDisplayName()));
        }
    }

    private void initNotYetStatusAsFirst() {
        if (statusList != null && statusList.size() > 0) {
            filter.setStatus(RemitMasterStatusEnum.NOT_YET);
        }
    }

    private void initDisabledOnlySalesListAndDefaultAsFirst() {
        initSalesList(false, true);
        initDefaultSalesAsFirst();
        //initDefaultSalesAsLoginUser();
    }

    private void initSalesListAndDefaultAsFirst() {
        initSalesList(false);
        initDefaultSalesAsFirst();
        //initDefaultSalesAsLoginUser();
    }

    private void initSalesList(boolean includeBlank) {
        initSalesList(includeBlank, false);
    }

    private void initSalesList(boolean includeBlank, boolean disableUserOnly) {
        TcUser user = userFacade.getSessionUser();
        initSalesList(includeBlank, disableUserOnly, user);
        initYearList();
        initMonthList();
    }

    public void initSalesList(boolean includeBlank, boolean disableUserOnly, TcUser user) {
        List<SkSalesMember> salesMembers = new ArrayList<SkSalesMember>();
        salesList = new ArrayList<SkSalesMember>();
        if (includeBlank) {
            salesList.add(new SkSalesMember());
        }
        HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
        if (request.isUserInRole(ConstantsUtil.ROLE_SALES)) {
            if (isSalesManager(user)) {
                logger.debug("manager");
                salesMembers = memberFacade.findByChannels(user, disableUserOnly);
            } else {
                logger.debug("sales");
                salesMembers = memberFacade.findByChannels(user, false);
            }
        } else {
            salesMembers = memberFacade.findAllSelectable();
        }
        /*
         else if (request.isUserInRole(ConstantsUtil.ROLE_FINANCE)) {
         //logger.debug("finance");
         salesMembers = memberFacade.findAllSelectable();
         } else if (request.isUserInRole(ConstantsUtil.ROLE_ADMINISTRATORS) || request.isUserInRole(ConstantsUtil.ROLE_ASSISTANT)) {
         //logger.debug("administrator");
         salesMembers = memberFacade.findAllSelectable();
         }
         */
        //initDefaultSalesAsFirst();
        salesList.addAll(salesMembers);
    }

    private boolean isSalesManager(TcUser user) {
        boolean isManager = false;
        SkSalesChannels channel = channelsFacade.findByManager(user);
        if (channel != null) {
            isManager = true;
        }
        return isManager;
    }

    public void initYearList() {
        logger.debug("innitYearList()");
        yearList.clear();
        // Date now = new Date();
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        for (int i = 0; i <= 5; i++) {
            String year = String.valueOf(y - i);
            yearList.add(new SelectItem(year, year));
        }
        filter.setYear(String.valueOf(y));
    }

    public void initMonthList() {
        logger.debug("initMonthList()");
        String[] months = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        monthList.clear();
        for (int i = 0; i < months.length; i++) {
            monthList.add(new SelectItem(months[i], months[i]));
        }
        // Date now = new Date();
        Calendar c = Calendar.getInstance();
        int m = c.get(Calendar.MONTH) + 1;
        String defaultM = String.valueOf(m);
        if (defaultM.length() == 1) {
            defaultM = "0" + defaultM;
        }
        //System.out.println("initYearList defaultM=" + defaultM);
        filter.setMonth(defaultM);
    }

    public List<SelectItem> getPaymentTermList() {
        return paymentTermList;
    }

    public void setPaymentTermList(List<SelectItem> paymentTermList) {
        this.paymentTermList = paymentTermList;
    }

    public Date getMonthFirstDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    private Date getYesterday() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -1);
        return c.getTime();
    }

    //--Begin--Modified by nEO Fu on 20120222 fucntion to get today.
    private Date getToday() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }
    //---End---Modified by nEO Fu on 20120222 fucntion to get today.

    public RemitFilter getFilter() {
        return filter;
    }

    public void setFilter(RemitFilter filter) {
        this.filter = filter;
    }

    public List<SkSalesMember> getSalesList() {
        return salesList;
    }

    public void setSalesList(List<SkSalesMember> salesList) {
        this.salesList = salesList;
    }

    public boolean isSalesSelectable() {
        return salesSelectable;
    }

    public List<String> completeCustomer(String customerCode) {
        List<String> codes = new ArrayList<String>();
        String salesCode = null;
        if (filter != null && filter.getSales() != null) {
            salesCode = filter.getSales().getCode();
        }
        List<SkCustomer> customers = customerFacade.findByConditions(customerCode, salesCode);
        for (SkCustomer customer : customers) {
            codes.add(customer.getSimpleCode());
        }
        return codes;
    }

    public List<String> completeCheckNumber(String number) {
        List<SkArRemitItem> itemList = remitDetailFacade.findByCriteria(filter.getSales(), number, RemitMasterStatusEnum.NOT_YET);
        List<String> list = setRemitItemToCheckNumber(itemList);
        //System.out.println("completeCheckNumber list=" + list);
        return list;
    }

    private List<String> setRemitItemToCheckNumber(List<SkArRemitItem> itemList) {
        HashMap<String, String> checkNumbers = new HashMap<String, String>();
        List<String> list = new ArrayList<String>();
        if (itemList != null) {
            for (SkArRemitItem item : itemList) {
                if (item.getCheckNumber() != null && item.getCheckNumber().length() > 0
                        && !checkNumbers.containsKey(item.getCheckNumber())) {
                    list.add(item.getCheckNumber());
                    checkNumbers.put(item.getCheckNumber(), item.getCheckNumber());
                }
                if (item.getCheckNumber2() != null && item.getCheckNumber2().length() > 0
                        && !checkNumbers.containsKey(item.getCheckNumber2())) {
                    list.add(item.getCheckNumber2());
                    checkNumbers.put(item.getCheckNumber2(), item.getCheckNumber2());
                }
            }
        }
        return list;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public List<SelectItem> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<SelectItem> monthList) {
        this.monthList = monthList;
    }

    public List<SelectItem> getYearList() {
        return yearList;
    }

    public void setYearList(List<SelectItem> yearList) {
        this.yearList = yearList;
    }

    public List<SelectItem> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<SelectItem> statusList) {
        this.statusList = statusList;
    }
    /*
     * public HtmlInputHidden getInitInvoiceStartHidden() { String done = DONE;
     * if (!done.equals((String) (initInvoiceStartHidden.getValue()))) {
     * filter.setInvoiceStart(previousMonthOfDate(true));
     * initInvoiceStartHidden.setValue(done); }
     * //logger.info("getInitInvoiceStart InvoiceStart=" +
     * filter.getInvoiceStart());
     *
     * return initInvoiceStartHidden; }
     *
     * public void setInitInvoiceStartHidden(HtmlInputHidden
     * initInvoiceStartHidden) { this.initInvoiceStartHidden =
     * initInvoiceStartHidden; }
     *
     * public HtmlInputHidden getInitInvoiceEndHidden() { String done = DONE; if
     * (!done.equals((String) (initInvoiceEndHidden.getValue()))) {
     * filter.setInvoiceEnd(getYesterday());
     * initInvoiceStartHidden.setValue(done); } return initInvoiceEndHidden; }
     *
     * public void setInitInvoiceEndHidden(HtmlInputHidden initInvoiceEndHidden)
     * { this.initInvoiceEndHidden = initInvoiceEndHidden; }
     */

    public void setInitSalesHidden(HtmlInputHidden initSalesHidden) {
        this.initSalesHidden = initSalesHidden;
    }

    public HtmlInputHidden getInitSalesHidden() {
        //set sales default as null
        String done = DONE;
        logger.debug("getInitSalesHidden(),initSalesHidden.getValue()={}", initSalesHidden.getValue());
        logger.debug("filter.getSales()={}", filter.getSales());
        if (!done.equals((String) (initSalesHidden.getValue()))) {
            filter.setSales(null);
            //initSalesListAndDefaultAsFirst();
            initDisabledOnlySalesListAndDefaultAsFirst(); //in create ar page, manager can only create AR for disabled sales
            initSalesHidden.setValue(done);
        }
        return initSalesHidden;
    }

    public List<SelectItem> getOrderTypeList() {
        return orderTypeList;
    }

    public void setOrderTypeList(List<SelectItem> orderTypeList) {
        this.orderTypeList = orderTypeList;
    }

    public void initCustomerDialog() {
        //selectCustomerController.setSales(filter.getSales().getCode());
        selectCustomerController.setSalesMember(filter.getSales());
        selectCustomerController.init();
    }

    private void initRemitTimeRange() {
        //int Remit time range in current month
        filter.setPayingStart(getMonthFirstDay());
        //filter.setPayingEnd(getYesterday());
        filter.setPayingEnd(getToday());

    }
    /*
     * public HtmlInputHidden getInitRemitTimeRangeHidden() { String done =
     * DONE; if (!done.equals((String) (initRemitTimeRangeHidden.getValue()))) {
     * initRemitTimeRange(); } return initRemitTimeRangeHidden; }
     *
     * public void setInitRemitTimeRangeHidden(HtmlInputHidden
     * initRemitTimeRangeHidden) { this.initRemitTimeRangeHidden =
     * initRemitTimeRangeHidden; }
     */

    public HtmlInputHidden getInitSalesCustomerInvoiceStartEndHidden() {
        logger.info("customer simple code=" + FacesUtil.getRequestParameter(ConstantsUtil.PARA_NAME_CUSTOMER_SIMPLE_CODE));
        String done = DONE;
        if (!done.equals((String) (initSalesCustomerInvoiceStartEndHidden.getValue()))) {
            initSalesList(false);
            initInvoiceStartAsMonthFirstDay();
            initInvoiceEndAsToday();
            String customerSimpleCode = FacesUtil.getRequestParameter(ConstantsUtil.PARA_NAME_CUSTOMER_SIMPLE_CODE);
            String salesCode = FacesUtil.getRequestParameter(ConstantsUtil.ROLE_SALES);//PARA_NAME_SALES_CODE
            if (!StringUtils.isEmpty(customerSimpleCode)) {
                selectCustomerController.setSelectedCustomer(customerSimpleCode);
                selectCustomerController.setName(selectCustomerController.getCustomerName(customerSimpleCode));
            }
            if (!StringUtils.isEmpty(salesCode)) {
                initDefaultSales(salesCode);
            } else {
                initDefaultSalesAsFirst();
                //initDefaultSalesAsLoginUser();
            }
            initSalesCustomerInvoiceStartEndHidden.setValue(done);
        }
        return initSalesCustomerInvoiceStartEndHidden;
    }

    public void setInitSalesCustomerInvoiceStartEndHidden(HtmlInputHidden initSalesCustomerInvoiceStartEndHidden) {
        this.initSalesCustomerInvoiceStartEndHidden = initSalesCustomerInvoiceStartEndHidden;
    }

    public HtmlInputHidden getInitSalesCustomerOrderDateStartEndHidden() {
        logger.info("customer simple code=" + FacesUtil.getRequestParameter(ConstantsUtil.PARA_NAME_CUSTOMER_SIMPLE_CODE));
        String done = DONE;
        if (!done.equals((String) (initSalesCustomerOrderDateStartEndHidden.getValue()))) {
            initSalesList(false);
            initOrderDateStartAsMonthFirstDay();
            initOrderDateEndAsToday();
            String customerSimpleCode = FacesUtil.getRequestParameter(ConstantsUtil.PARA_NAME_CUSTOMER_SIMPLE_CODE);
            String salesCode = FacesUtil.getRequestParameter(ConstantsUtil.ROLE_SALES);//PARA_NAME_SALES_CODE
            if (!StringUtils.isEmpty(customerSimpleCode)) {
                selectCustomerController.setSelectedCustomer(customerSimpleCode);
                selectCustomerController.setName(selectCustomerController.getCustomerName(customerSimpleCode));
            }
            if (!StringUtils.isEmpty(salesCode)) {
                initDefaultSales(salesCode);
            } else {
                initDefaultSalesAsFirst();
                //initDefaultSalesAsLoginUser();
            }
            initSalesCustomerOrderDateStartEndHidden.setValue(done);
        }
        return initSalesCustomerOrderDateStartEndHidden;
    }

    public void setInitSalesCustomerOrderDateStartEndHidden(HtmlInputHidden initSalesCustomerOrderDateStartEndHidden) {
        this.initSalesCustomerOrderDateStartEndHidden = initSalesCustomerOrderDateStartEndHidden;
    }

    private void initDefaultSales(String salesCode) {
        if (salesList != null && !salesList.isEmpty()) {
            for (SkSalesMember m : salesList) {
                if (m.getCode().equals(salesCode)) {
                    filter.setSales(m);
                }
            }
        }
    }

    private void initDefaultSalesAsFirst() {
        if (salesList != null && !salesList.isEmpty()) {
            filter.setSales(salesList.get(0));
        }
    }
    /*
     private void initDefaultSalesAsLoginUser() {
     if(salesList!=null && !salesList.isEmpty()) {
     SkSalesMember salesMember = memberFacade.findByMember(sessionController.getUser());
     filter.setSales(salesMember);
     }
     }
     */

    private void initOrderDateStartAsMonthFirstDay() {
        filter.setOrderDateStart(getMonthFirstDay());
    }

    private void initOrderDateEndAsToday() {
        filter.setOrderDateEnd(new Date());
    }

    private void initInvoiceStartAsMonthFirstDay() {
        filter.setInvoiceStart(getMonthFirstDay());
    }

    private void initInvoiceEndAsToday() {
        filter.setInvoiceEnd(new Date());
    }

    public HtmlInputHidden getInitSalesYearMonthHidden() {
        String done = DONE;
        if (!done.equals((String) (initSalesYearMonthHidden.getValue()))) {
            initSalesList(false);
            initDefaultSalesAsFirst();
            //initDefaultSalesAsLoginUser();
            String sapid = FacesUtil.getRequestParameter("sapid");
            if (!StringUtils.isEmpty(sapid)) {
//                if (salesList != null && !salesList.isEmpty()) {
                for (SkSalesMember sales : salesList) {
                    if (sales.getCode().equals(sapid)) {
                        filter.setSales(sales);
                    }
                }
//                }
            }

            String year = FacesUtil.getRequestParameter("year");
            if (!StringUtils.isEmpty(year)) {
                filter.setYear(year);
            }

            String month = FacesUtil.getRequestParameter("month");
            if (!StringUtils.isEmpty(month)) {
                filter.setMonth(month);
            }

            initSalesYearMonthHidden.setValue(done);
        }
        return initSalesYearMonthHidden;
    }

    public void setInitSalesYearMonthHidden(HtmlInputHidden initSalesYearMonthHidden) {
        this.initSalesYearMonthHidden = initSalesYearMonthHidden;
    }

    public HtmlInputHidden getInitSalesInvoiceNotStartEndAsYesterdayHidden() {
        String done = DONE;
        if (!done.equals((String) (initSalesInvoiceNotStartEndAsYesterdayHidden.getValue()))) {
            boolean toRenderInvoiceStart = false;
            initSalesListAndDefaultAsFirst();
            filter.setInvoiceEnd(getYesterday());
            setRenderInvoiceStart(toRenderInvoiceStart);
            initSalesInvoiceNotStartEndAsYesterdayHidden.setValue(done);
        }
        return initSalesInvoiceNotStartEndAsYesterdayHidden;
    }

    public void setInitSalesInvoiceNotStartEndAsYesterdayHidden(HtmlInputHidden initSalesInvoiceNotStartEndAsYesterdayHidden) {
        this.initSalesInvoiceNotStartEndAsYesterdayHidden = initSalesInvoiceNotStartEndAsYesterdayHidden;
    }

    public boolean isRenderInvoiceStart() {
        return renderInvoiceStart;
    }

    public void setRenderInvoiceStart(boolean renderInvoiceStart) {
        this.renderInvoiceStart = renderInvoiceStart;
    }

    public HtmlInputHidden getInitSalesInvoiceAsLastMonthHidden() {
        String done = DONE;
        if (!done.equals((String) (initSalesInvoiceAsLastMonthHidden.getValue()))) {
            initSalesListAndDefaultAsFirst();
            initPaymentTermList();
            filter.setInvoiceStart(previousMonthStartOrEndDate(true));
            filter.setInvoiceEnd(previousMonthStartOrEndDate(false));
            filter.setPaymentTerm(null);
            initSalesInvoiceAsLastMonthHidden.setValue(done);
        }
        return initSalesInvoiceAsLastMonthHidden;
    }

    public void setInitSalesInvoiceAsLastMonthHidden(HtmlInputHidden initSalesInvoiceAsLastMonthHidden) {
        this.initSalesInvoiceAsLastMonthHidden = initSalesInvoiceAsLastMonthHidden;
    }

    /**
     * @param isStart : true be first day of previous month, false be lalest day
     * of previous month
     */
    private Date previousMonthStartOrEndDate(boolean isStart) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        if (isStart) {
            c.add(Calendar.MONTH, -1);
        } else {
            c.add(Calendar.DAY_OF_YEAR, -1);
        }
        return c.getTime();
    }

    public HtmlInputHidden getInitSalesRemitTimeRangeHidden() {
        String done = DONE;
        if (!done.equals((String) (initSalesRemitTimeRangeHidden.getValue()))) {
            initSalesListAndDefaultAsFirst();
            initRemitTimeRange();
            initSalesRemitTimeRangeHidden.setValue(done);
        }
        return initSalesRemitTimeRangeHidden;
    }

    public void setInitSalesRemitTimeRangeHidden(HtmlInputHidden initSalesRemitTimeRangeHidden) {
        this.initSalesRemitTimeRangeHidden = initSalesRemitTimeRangeHidden;
    }

    public HtmlInputHidden getInitSalesAndCheckNumberHidden() {
        logger.debug("getInitSalesAndCheckNumberHidden()");
        String done = DONE;
        if (!done.equals((String) initSalesAndCheckNumberHidden.getValue())) {
            initSalesListAndSetByParameter(false);
            initCheckNumber();
            initSalesAndCheckNumberHidden.setValue(done);
        }
        return initSalesAndCheckNumberHidden;
    }

    public HtmlInputHidden getInitFinanceSalesAndCheckNumberHidden() {
        logger.debug("getInitFinanceSalesAndCheckNumberHidden()");
        String done = DONE;
        if (!done.equals((String) initFinanceSalesAndCheckNumberHidden.getValue())) {
            initSalesListAndSetByParameter(true);
            initCheckNumber();
            initFinanceSalesAndCheckNumberHidden.setValue(done);
        }
        return initSalesAndCheckNumberHidden;
    }

    public HtmlInputHidden getInitSalesCustomerOrderYearlyHidden() {
        String done = DONE;
        if (!done.equals((String) (initSalesCustomerOrderYearlyHidden.getValue()))) {
            initSalesListAndDefaultAsFirst();
            String customerSimpleCode = FacesUtil.getRequestParameter(ConstantsUtil.PARA_NAME_CUSTOMER_SIMPLE_CODE);
            if (!StringUtils.isEmpty(customerSimpleCode)) {
                selectCustomerController.setSelectedCustomer(customerSimpleCode);
                selectCustomerController.setName(selectCustomerController.getCustomerName(customerSimpleCode));
            }

            initYearList();
            initSalesCustomerOrderYearlyHidden.setValue(done);
        }
        return initSalesCustomerOrderYearlyHidden;
    }

    public void setInitSalesCustomerOrderYearlyHidden(HtmlInputHidden initSalesCustomerOrderYearlyHidden) {
        this.initSalesCustomerOrderYearlyHidden = initSalesCustomerOrderYearlyHidden;
    }

    public void setInitSalesAndCheckNumberHidden(HtmlInputHidden initSalesAndCheckNumberHidden) {
        this.initSalesAndCheckNumberHidden = initSalesAndCheckNumberHidden;
    }

    public void setInitFinanceSalesAndCheckNumberHidden(HtmlInputHidden initSalesAndCheckNumberHidden) {
        this.initSalesAndCheckNumberHidden = initSalesAndCheckNumberHidden;
    }

    private void initSalesListAndSetByParameter(boolean includeBlank) {
        initSalesList(includeBlank);
        String salesCode = FacesUtil.getRequestParameter("salesCode");
        if (StringUtils.isNotEmpty(salesCode)) {
            this.filter.setSales(memberFacade.findByCode(salesCode));
        } else {
            initSalesListAndDefaultAsFirst();
        }
    }

    private void initCheckNumber() {
        logger.debug("FacesUtil.getRequestParameter(checkNumber)={}", FacesUtil.getRequestParameter("checkNumber"));
        this.checkNumber = "null".equals(FacesUtil.getRequestParameter("checkNumber")) ? "" : FacesUtil.getRequestParameter("checkNumber");
    }

    public HtmlInputHidden getInitStatusList() {
        String done = DONE;
        if (!done.equals((String) initStatusList.getValue())) {
            initStatusList();
            initStatusList.setValue(done);
        }
        return initStatusList;
    }

    public void setInitStatusList(HtmlInputHidden initStatusList) {
        this.initStatusList = initStatusList;
    }

    public HtmlInputHidden getInitStatusWithDefaultList() {
        String done = DONE;
        if (!done.equals((String) initStatusWithDefaultList.getValue())) {
            initStatusList();
            initNotYetStatusAsFirst();
            initStatusWithDefaultList.setValue(done);
        }
        return initStatusWithDefaultList;
    }

    public void setInitStatusWithDefaultList(HtmlInputHidden initStatusWithDefaultList) {
        this.initStatusWithDefaultList = initStatusWithDefaultList;
    }

    public HtmlInputHidden getInitSalesStatusYearMonthHidden() {
        String done = DONE;
        if (!done.equals((String) initSalesStatusYearMonthHidden.getValue())) {
            initStatusList();
            initSalesList(false);
            initYearList();
            initMonthList();
            initRemitMinMaxNumber();

            // start setting default value                  
            String hasDefaultValue = FacesUtil.getRequestParameter("hasDefaultValue");
            if ("true".equals(hasDefaultValue)) {
                RemitFilter tempFilter = new RemitFilter();
                tempFilter.setYear(FacesUtil.getRequestParameter("year"));
                tempFilter.setMonth(FacesUtil.getRequestParameter("month"));

                String fromRemitNumber = FacesUtil.getRequestParameter("fromRemitNumber");
                if (!StringUtils.isEmpty(fromRemitNumber)) {
                    tempFilter.setFromRemitNumber(Integer.valueOf(fromRemitNumber));
                }
                String toRemitNumber = FacesUtil.getRequestParameter("toRemitNumber");
                if (!StringUtils.isEmpty(toRemitNumber)) {
                    tempFilter.setToRemitNumber(Integer.valueOf(toRemitNumber));
                }
                String sapId = FacesUtil.getRequestParameter("sapId");
                if (!StringUtils.isEmpty(sapId)) {
                    tempFilter.setSales(memberFacade.findByCode(sapId));
                }
                String customerSimpleCode = FacesUtil.getRequestParameter("customerSimpleCode");
                if (!StringUtils.isEmpty(customerSimpleCode)) {
                    SkCustomer customer = customerFacade.findBySimpleCode(customerSimpleCode);
                    if (customer != null) {
                        tempFilter.setSkCustomer(customer);
                        selectCustomerController.setSelectedCustomer(customer.getSimpleCode());
                        selectCustomerController.setName(customer.getName());
                    }
                }
                String creatorEmpId = FacesUtil.getRequestParameter("creatorEmpId");
                if (!StringUtils.isEmpty(creatorEmpId)) {
                    TcUser creator = userFacade.findUserByEmpId(creatorEmpId);
                    if (creator != null) {
                        tempFilter.setCreator(creator);
                        selectEmployeeController.setSelectedUser(creator.getEmpId());
                        selectEmployeeController.setCname(creator.getCname());
                    }
                }
                String status = FacesUtil.getRequestParameter("status");
                if (!StringUtils.isEmpty(status)) {
                    tempFilter.setStatus(RemitMasterStatusEnum.valueOf(status));
                }

                filter = tempFilter;
                changeMinMaxNumber(null);
            }
            // end setting default value

            initSalesStatusYearMonthHidden.setValue(done);
        }
        return initSalesStatusYearMonthHidden;
    }

    public void setInitSalesStatusYearMonthHidden(HtmlInputHidden initSalesStatusYearMonthHidden) {
        this.initSalesStatusYearMonthHidden = initSalesStatusYearMonthHidden;
    }

    public boolean latestStepToCheckCustomerCode(boolean isCustomerRequire, boolean isOnlySalesCode) {
        boolean invalidCustomerCode = false;
        invalidCustomerRelation = false;
        customerNotExists = false;
        ResourceBundle rb = ResourceBundle.getBundle("messages");
        SkCustomer customer = null;
        String customerName = null;
        logger.debug("selectCustomerController.getSelectedCustomer()={}", selectCustomerController.getSelectedCustomer());
        if (!StringUtils.isEmpty(selectCustomerController.getSelectedCustomer())) {
            /*
             * if ( isOnlySalesCode ) customer =
             * customerFacade.findBySimpleCodeSalesCode(selectCustomerController.getSelectedCustomer(),filter.getSales().getCode());
             * else
             */

            customer = customerFacade.findBySimpleCode(selectCustomerController.getSelectedCustomer());
            if (customer == null) {
                customer = new SkCustomer();
                customer.setName(rb.getString("customer.msg.notexists"));
                customerName = (rb.getString("customer.msg.notexists"));
            }
            logger.debug("customer.getId()={}", customer.getId());
            if (customer.getId() != null) {
                //TODO: 看不懂這段在做啥? 
                if (isOnlySalesCode) {
                    if (customer.getSapid().equals(filter.getSales().getCode())) {
                        getFilter().setSkCustomer(customer);
                        customerName = customer.getName();
                    } else {
                        invalidCustomerRelation = true;
                        String msg = rb.getString("customer.msg.not.your.customer");
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                } else {
                    getFilter().setSkCustomer(customer);
                    customerName = customer.getName();
                }
            } else {
                customerNotExists = true;
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("customer.msg.notexists"), rb.getString("customer.msg.notexists"));
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        } else if (isCustomerRequire) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("customer.msg.required"), rb.getString("customer.msg.required"));
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        getFilter().setSkCustomer(customer);
        selectCustomerController.setName(customerName);
        return invalidCustomerCode;
    }

    public boolean isWrongCustomerCode() {
        return getFilter().getSkCustomer() == null && !StringUtils.isEmpty(selectCustomerController.getSelectedCustomer());
    }

    public boolean isInvalidCustomerRelation() {
        return invalidCustomerRelation;
    }

    public HtmlInputHidden getInitOrderDateStartEndHidden() {
        String done = DONE;
        if (!done.equals((String) (initOrderDateStartEndHidden.getValue()))) {
            initOrderDateStartAsMonthFirstDay();
            initOrderDateEndAsToday();
            initOrderDateStartEndHidden.setValue(done);
        }
        return initOrderDateStartEndHidden;
    }

    public void setInitOrderDateStartEndHidden(HtmlInputHidden initOrderDateStartEndHidden) {
        this.initOrderDateStartEndHidden = initOrderDateStartEndHidden;
    }

    public HtmlInputHidden getInitSalesInvoiceTimestampHidden() {
        String done = DONE;
        if (!done.equals((String) (initSalesInvoiceTimestampHidden.getValue()))) {
            initDefaultSalesAsFirst();
            //initDefaultSalesAsLoginUser();
            String sapid = FacesUtil.getRequestParameter("sapid");
            if (!StringUtils.isEmpty(sapid)) {
                if (salesList != null && !salesList.isEmpty()) {
                    for (SkSalesMember sales : salesList) {
                        if (sales.getCode().equals(sapid)) {
                            filter.setSales(sales);
                        }
                    }
                }
            }

            String year = FacesUtil.getRequestParameter("year");
            filter.setYear(year);

        }

        initSalesInvoiceTimestampHidden.setValue(done);
        return initSalesInvoiceTimestampHidden;
    }

    public void setInitSalesInvoiceTimestampHidden(HtmlInputHidden initSalesInvoiceTimestampHidden) {
        this.initSalesInvoiceTimestampHidden = initSalesInvoiceTimestampHidden;
    }

    private void initPaymentTermList() {
        this.paymentTermList = new ArrayList<SelectItem>();
        for (PaymentTermEnum paymentTermEnum : PaymentTermEnum.values()) {
            this.paymentTermList.add(new SelectItem(paymentTermEnum.toString(), paymentTermEnum.toString()));
        }
    }

    private void initOrderTypeList() {
        this.orderTypeList = new ArrayList<SelectItem>();
        for (OrderTypeEnum orderTypeEnum : OrderTypeEnum.values()) {
            this.orderTypeList.add(new SelectItem(orderTypeEnum, orderTypeEnum.getDisplayName()));
        }
    }

    public HtmlInputHidden getInitSalesOrderDateStartEndHidden() {
        String done = DONE;
        if (!done.equals((String) (initSalesOrderDateStartEndHidden.getValue()))) {
            initSalesList(false);
            initOrderDateStartAsMonthFirstDay();
            initOrderDateEndAsToday();
            initOrderTypeList();
            String salesCode = FacesUtil.getRequestParameter(ConstantsUtil.ROLE_SALES);//PARA_NAME_SALES_CODE           
            if (!StringUtils.isEmpty(salesCode)) {
                initDefaultSales(salesCode);
            } else {
                initDefaultSalesAsFirst();
                //initDefaultSalesAsLoginUser();
            }
            initSalesOrderDateStartEndHidden.setValue(done);
        }
        return initSalesOrderDateStartEndHidden;
    }

    public void setInitSalesOrderDateStartEndHidden(HtmlInputHidden initSalesOrderDateStartEndHidden) {
        this.initSalesOrderDateStartEndHidden = initSalesOrderDateStartEndHidden;
    }

    public String toConfirmRemitPage() {
        StringBuilder builder = new StringBuilder();
        builder.append("confirmRemit.xhtml?faces-redirect=true&");
        builder.append(assembleParametersFromFilter());
        return builder.toString();
    }

    public String toReviewRemitPage() {
        StringBuilder builder = new StringBuilder();
        builder.append("reviewRemit.xhtml?faces-redirect=true&doSearch=true&");
        String paramStr = assembleParametersFromRequest();
        builder.append("hasDefaultValue=" + !StringUtils.isEmpty(paramStr) + "&");
        builder.append(paramStr);
        return builder.toString();
    }

    public String assembleParametersFromFilter() {
        StringBuilder builder = new StringBuilder("");
        builder.append("year=" + filter.getYear());
        builder.append("&month=" + filter.getMonth());
        if (filter.getFromRemitNumber() != null) {
            builder.append("&fromRemitNumber=" + filter.getFromRemitNumber());
        }
        if (filter.getToRemitNumber() != null) {
            builder.append("&toRemitNumber=" + filter.getToRemitNumber());
        }
        if (filter.getSales() != null) {
            builder.append("&sapId=" + filter.getSales().getCode());
        }
        if (filter.getSkCustomer() != null) {
            builder.append("&customerSimpleCode=" + filter.getSkCustomer().getSimpleCode());
        }
        if (filter.getCreator() != null) {
            builder.append("&creatorEmpId=" + filter.getCreator().getEmpId());
        }
        if (filter.getStatus() != null) {
            builder.append("&status=" + filter.getStatus());
        }
        return builder.toString();
    }

    public String assembleParametersFromRequest() {
        String year = FacesUtil.getRequestParameter("year");
        String month = FacesUtil.getRequestParameter("month");
        String fromRemitNumber = FacesUtil.getRequestParameter("fromRemitNumber");
        String toRemitNumber = FacesUtil.getRequestParameter("toRemitNumber");
        String sapId = FacesUtil.getRequestParameter("sapId");
        String customerSimpleCode = FacesUtil.getRequestParameter("customerSimpleCode");
        String creatorEmpId = FacesUtil.getRequestParameter("creatorEmpId");
        String status = FacesUtil.getRequestParameter("status");

        StringBuilder builder = new StringBuilder("");
        if (!StringUtils.isEmpty(year)) {
            builder.append("year=" + year);
        }
        if (!StringUtils.isEmpty(month)) {
            builder.append("&month=" + month);
        }
        if (!StringUtils.isEmpty(fromRemitNumber)) {
            builder.append("&fromRemitNumber=" + fromRemitNumber);
        }
        if (!StringUtils.isEmpty(toRemitNumber)) {
            builder.append("&toRemitNumber=" + toRemitNumber);
        }
        if (!StringUtils.isEmpty(sapId)) {
            builder.append("&sapId=" + sapId);
        }
        if (!StringUtils.isEmpty(customerSimpleCode)) {
            builder.append("&customerSimpleCode=" + customerSimpleCode);
        }
        if (!StringUtils.isEmpty(creatorEmpId)) {
            builder.append("&creatorEmpId=" + creatorEmpId);
        }
        if (!StringUtils.isEmpty(status)) {
            builder.append("&status=" + status);
        }
        return builder.toString();
    }

    public boolean isCustomerNotExists() {
        return customerNotExists;
    }

    public void setCustomerNotExists(boolean customerNotExists) {
        this.customerNotExists = customerNotExists;
    }

    public HtmlInputHidden getInitSalesDefaultBlankHidden() {
        String done = "done";
        if (!done.equals((String) initSalesDefaultBlankHidden.getValue())) {
            initSalesList(false, false);
            initSalesDefaultBlankHidden.setValue(done);
        }
        return initSalesDefaultBlankHidden;
    }

    public void setInitSalesDefaultBlankHidden(HtmlInputHidden initSalesDefaultBlankHidden) {
        this.initSalesDefaultBlankHidden = initSalesDefaultBlankHidden;
    }

    public void salesChange() {
        // 強制 filter.sales 更新, 客戶編號的autocomplete才不會出錯
        logger.debug("salesChange:{}", null == filter.getSales() ? null : filter.getSales().getDisplayIdentifier());
    }
}
