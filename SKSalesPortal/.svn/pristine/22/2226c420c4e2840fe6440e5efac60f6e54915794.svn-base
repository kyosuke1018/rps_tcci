package com.tcci.sksp.controller.remit;

import com.tcci.sksp.controller.util.SessionController;
import com.tcci.sksp.entity.ar.SkFiMasterInterface;
import com.tcci.sksp.entity.ar.SkPremiumDiscount;
import com.tcci.sksp.entity.ar.SkSalesOrderDetail;
import com.tcci.sksp.entity.ar.SkSalesOrderMaster;
import com.tcci.sksp.facade.PremiumDiscountFilter;
import com.tcci.sksp.facade.SkPremiumDiscountFacade;
import com.tcci.sksp.facade.SkSalesOrderMasterFacade;
import com.tcci.sksp.vo.PremiumDiscountVO;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
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
public class EditPremiumDiscountController {

    //<editor-fold defaultstate="collapsed" desc="parameters">
    protected final static Logger logger = LoggerFactory.getLogger(EditPremiumDiscountController.class);
    private ResourceBundle rb = ResourceBundle.getBundle("messages");
    private String number;
    private List<SelectItem> yearList = new ArrayList<SelectItem>();
    private String[] months = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    private List<SelectItem> monthList = new ArrayList<SelectItem>();
    private SkPremiumDiscount discount = new SkPremiumDiscount();
    private SkFiMasterInterface fiInterface = new SkFiMasterInterface();
    private List<SkPremiumDiscount> discounts = new ArrayList<SkPremiumDiscount>();
    private String errorMessage = "";
    private String action;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    SkPremiumDiscountFacade facade;
    @EJB
    SkSalesOrderMasterFacade salesOrderMasterFacade;
    @ManagedProperty(value = "#{sessionController}")
    private SessionController userSession;
    
    public void setUserSession(SessionController userSession) {
        this.userSession = userSession;
    }
    @ManagedProperty(value = "#{premiumDiscountController}")
    private PremiumDiscountController premiumDiscountController;
    
    public void setPremiumDiscountController(PremiumDiscountController premiumDiscountController) {
        this.premiumDiscountController = premiumDiscountController;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getter, setter">
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
    
    public SkPremiumDiscount getDiscount() {
        return discount;
    }
    
    public void setDiscount(SkPremiumDiscount discount) {
        this.discount = discount;
    }
    
    public List<SkPremiumDiscount> getDiscounts() {
        return discounts;
    }
    
    public void setDiscounts(List<SkPremiumDiscount> discounts) {
        this.discounts = discounts;
    }
    
    public String getNumber() {
        return number;
    }
    
    public void setNumber(String number) {
        this.number = number;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public SkFiMasterInterface getFiInterface() {
        return fiInterface;
    }
    
    public void setFiInterface(SkFiMasterInterface fiInterface) {
        this.fiInterface = fiInterface;
    }
    //</editor-fold>

    public void create() {
        logger.debug("create()");
        number = "";
        discount = new SkPremiumDiscount();
        discounts = new ArrayList<SkPremiumDiscount>();
        action = "Create";
        Date now = new Date();
        discount.setYear(String.valueOf(now.getYear() + 1900));
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        discount.setMonth(sdf.format(now));
        init();
    }
    
    public void edit(SkPremiumDiscount discount) {
        logger.debug("edit({})", discount);
        this.discount = discount;
        action = "Edit";
        discounts.clear();
        this.discounts.add(this.discount);
        init();
    }
    
    private void init() {
        logger.debug("init()");
        initYearList();
        initMonthList();
    }
    
    public String getDialogHeader() {
        if ("Create".equals(action)) {
            return rb.getString("premiumDiscount.title.createPremiumDiscount");
        } else {
            return rb.getString("premiumDiscount.title.editPremiumDiscount");
        }
    }
    
    private void initYearList() {
        logger.debug("innitYearList()");
        yearList.clear();
        Date now = new Date();
        for (int i = 0; i <= 5; i++) {
            String year = String.valueOf(now.getYear() + 1900 - i);
            yearList.add(new SelectItem(year, year));
        }
    }
    
    public void initMonthList() {
        logger.debug("initMonthList()");
        monthList.clear();
        for (int i = 0; i < months.length; i++) {
            monthList.add(new SelectItem(months[i], months[i]));
        }
    }
    
    public void query() {
        logger.debug("query()");
        try {
            discounts.clear();
            discount = new SkPremiumDiscount();
            discount.setMonth(new SimpleDateFormat("MM").format(new Date()));
            if (StringUtils.isEmpty(number)) {
                errorMessage = rb.getString("premiumDiscount.error.numberRequired");
                return;
            }
            List<SkSalesOrderMaster> salesOrderMasters = salesOrderMasterFacade.findByOrderOrInvoiceNumber(number);
            logger.debug("salesOrderMasters=" + salesOrderMasters);
            //TODO: show only one master now, need show all of masters?
            if (salesOrderMasters != null && salesOrderMasters.size() > 0) {
                BigDecimal newDiscount = new BigDecimal(0);
                BigDecimal tax = new BigDecimal(0);
                for (SkSalesOrderMaster salesOrderMaster : salesOrderMasters) {
                    discount.setOrderNumber(salesOrderMaster.getOrderNumber());
                    discount.setOrderTimestamp(salesOrderMaster.getOrderTimestamp());
                    discount.setInvoiceNumber(salesOrderMaster.getInvoiceNumber());
                    discount.setInvoiceTimestamp(salesOrderMaster.getInvoiceTimestamp());
                    discount.setCustomer(salesOrderMaster.getCustomer());
                    discount.setSapid(salesOrderMaster.getSapid());
                    //2016/1/14 若同一張發票(或訂單), 若同一料號有一筆以上的, 只取一筆 (因批號不同, SAP 拋 portal 的溢價折讓會double).
                    Set<String> processedProductSet = new HashSet();
                    for (SkSalesOrderDetail salesOrderDetail : salesOrderMaster.getSkSalesOrderDetailCollection()) {
                        if (processedProductSet.contains(salesOrderDetail.getProductNumber())) {
                            continue;
                        }
                        if (salesOrderDetail.getPremiumDiscount() != null) {
                            newDiscount = newDiscount.add(salesOrderDetail.getPremiumDiscount());
                        }
                        if (salesOrderDetail.getPremiumDiscountTax() != null) {
                            tax = tax.add(salesOrderDetail.getPremiumDiscountTax());
                        }
                        processedProductSet.add(salesOrderDetail.getProductNumber());
                    }
                }
                discount.setDiscount(newDiscount);
                discount.setTax(tax);
                errorMessage = "";
                discounts.add(discount);
            } else {
                errorMessage = rb.getString("premiumDiscount.error.orderOrInvoiceNotFound");
            }
            logger.debug("errorMessage=" + errorMessage);
        } catch (Exception e) {
            logger.error("error occur, e={}", e);
        }
    }
    
    public void save() {
        logger.debug("save()");
        String message = "";
        try {
            valid();            
            Date now = new Date();
            //mean create discount, need add creator.
            if (discount.getId() == null) {
                discount.setCreator(userSession.getUser());
                discount.setCreatetimestamp(now);
                exists(discount);
                message = rb.getString("premiumDiscount.info.createSuccess");
            } else {
                message = rb.getString("premiumDiscount.info.updateSuccess");
            }
            discount.setModifier(userSession.getUser());
            discount.setModifytimestamp(now);
            discount = facade.editAndReturn(discount);
            //add discount to query list.
            if (premiumDiscountController.getDiscountVOs() == null) {
                List<PremiumDiscountVO> discountVOs = new ArrayList<PremiumDiscountVO>();
                PremiumDiscountVO vo = new PremiumDiscountVO();
                vo.setDiscount(discount);
                discountVOs.add(vo);
                premiumDiscountController.setDiscountVOs(discountVOs);
            } else {
                premiumDiscountController.getDiscountVOs().clear();
                PremiumDiscountVO vo = new PremiumDiscountVO();
                vo.setDiscount(discount);
                premiumDiscountController.getDiscountVOs().add(vo);
            }
//            FacesMessage message = new FacesMessage(
//                    FacesMessage.SEVERITY_INFO,
//                    successMessage,
//                    successMessage);
//            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            message = e.getLocalizedMessage();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("errormsg", message);
    }
    
    private void valid() throws Exception {
        //order number
        if (StringUtils.isEmpty(discount.getOrderNumber())) {
            throw new Exception(rb.getString("premiumDiscount.error.numberRequired"));
        }
        //discount
        if (discount.getDiscount() == null) {
            throw new Exception(rb.getString("premiumDiscount.error.discountRequired"));
        } else if (discount.getDiscount().intValue() == 0) {
            throw new Exception(rb.getString("premiumDiscount.error.discountNot0"));
        } else if (discount.getDiscount().intValue() < 0) {
            throw new Exception(rb.getString("premiumDiscount.error.discountNotNegative"));
        } else {
            try {
                BigInteger discountInInteger = discount.getDiscount().toBigIntegerExact();
            } catch (ArithmeticException ae) {
                throw new Exception(rb.getString("premiumDiscount.error.discountShouldBeInteger"));
            }
        }

        //tax
        if (discount.getTax().intValue() < 0) {
            throw new Exception(rb.getString("premiumDiscount.error.taxNotNegative"));
        } else {
            try {
                BigInteger taxInInteger = discount.getTax().toBigIntegerExact();
            } catch (ArithmeticException ae) {
                throw new Exception(rb.getString("premiumDiscount.error.taxShouldBeInteger"));
            }
        }
    }
    
    public void exists(SkPremiumDiscount discount) throws Exception {
        //exists already
        PremiumDiscountFilter filter = new PremiumDiscountFilter();
        filter.setNumber(discount.getInvoiceNumber());
        List<SkPremiumDiscount> existsDiscounts = facade.findByCriteria(filter);
        if (existsDiscounts != null && !existsDiscounts.isEmpty()) {
            throw new Exception(rb.getString("premiumDiscount.error.exists"));
        }
    }
//    public List<String> completeInvoiceOrderNumber(String input) {
//        List<String> result = new ArrayList<String>();
//        List<SkSalesOrderMaster> salesOrderMasters = salesOrderMasterFacade.findByOrderOrInvoiceNumber(input);
//        for (SkSalesOrderMaster salesOrderMaster : salesOrderMasters) {
//            result.add(salesOrderMaster.getOrderNumber());
//        }
//        return result;
//    }
//    
//    public String getInvoiceNumber(String orderNumber) {
//        List<SkSalesOrderMaster> masters = salesOrderMasterFacade.findByOrderOrInvoiceNumber(orderNumber);
//        if (masters != null && !masters.isEmpty()) {
//            return masters.get(0).getInvoiceNumber();
//        }
//        return "";
//    }
}
