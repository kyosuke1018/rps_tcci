/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.remit;

import com.tcci.sksp.controller.util.DateUtil;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.legacy.PGMUserSourceAdapter;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkPaymentItem;
import com.tcci.sksp.entity.ar.SkPaymentRate;
import com.tcci.sksp.facade.SkCustomerFacade;
import com.tcci.sksp.facade.SkPaymentItemFacade;
import com.tcci.sksp.facade.SkPaymentRateFacade;
import com.tcci.sksp.facade.legacy.LegacyPaymentRateFacade;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class SyncPaymentRateController {
//<editor-fold defaultstate="collapsed" desc="Properties">

    protected final static Logger logger = LoggerFactory.getLogger(LegacyPaymentRateFacade.class);
    private String selectedYear;
    private String selectedMonth;
    private List<SelectItem> yearList;
    private List<SelectItem> monthList;
    private int paymentItemCount;
    private int paymentRateCount;
    private SkPaymentItem summaryPaymentItem;
    private SkPaymentRate summaryPaymentRate; 
    private boolean renderRowCount;
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="EJB">
    @EJB
    LegacyPaymentRateFacade legacyPaymentRateFacade;
    @EJB
    SkCustomerFacade customerFacade;
    @EJB SkPaymentRateFacade paymentRateFacade;
    @EJB SkPaymentItemFacade paymentItemFacade;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="method">
    public List<SelectItem> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<SelectItem> monthList) {
        this.monthList = monthList;
    }

    public String getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public String getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(String selectedYear) {
        this.selectedYear = selectedYear;
    }

    public List<SelectItem> getYearList() {
        return yearList;
    }

    public void setYearList(List<SelectItem> yearList) {
        this.yearList = yearList;
    }

    public int getPaymentItemCount() {
        return paymentItemCount;
    }

    public void setPaymentItemCount(int paymentItemCount) {
        this.paymentItemCount = paymentItemCount;
    }

    public int getPaymentRateCount() {
        return paymentRateCount;
    }

    public void setPaymentRateCount(int paymentRateCount) {
        this.paymentRateCount = paymentRateCount;
    }

    public boolean isRenderRowCount() {
        return renderRowCount;
    }

    public void setRenderRowCount(boolean renderRowCount) {
        this.renderRowCount = renderRowCount;
    }

    public SkPaymentItem getSummaryPaymentItem() {
        return summaryPaymentItem;
    }

    public void setSummaryPaymentItem(SkPaymentItem summaryPaymentItem) {
        this.summaryPaymentItem = summaryPaymentItem;
    }

    public SkPaymentRate getSummaryPaymentRate() {
        return summaryPaymentRate;
    }

    public void setSummaryPaymentRate(SkPaymentRate summaryPaymentRate) {
        this.summaryPaymentRate = summaryPaymentRate;
    }

//</editor-fold>

    @PostConstruct
    public void init() {
        initYear();
        initMonth();
    }

    private void initYear() {
        int startYear = 2012;
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        yearList = new ArrayList<SelectItem>();
        for (int i = startYear; i <= currentYear; i++) {
            String year = String.valueOf(i);
            yearList.add(new SelectItem(year, year));
        }
    }

    private void initMonth() {
        monthList = new ArrayList<SelectItem>();
        for (int i = 1; i <= 12; i++) {
            String month = String.valueOf(i);
            monthList.add(new SelectItem(month, month));
        }
    }

    public String doSyncPaymentRateAction() {
        boolean isNull = false;
        if (StringUtils.isEmpty(selectedYear)) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Please select year");
            isNull = true;
        }
        if (StringUtils.isEmpty(selectedMonth)) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Please select month");
            isNull = true;
        }
        logger.debug("doSyncPaymentRateAction isNull=" + isNull);
        if (isNull) {
            return null;
        }
        //queryAndSyncByEJB();
        queryByJDBCAndSyncByEJB();
        changeRowCount(null);
        return null;
    }

    private void queryByJDBCAndSyncByEJB() {
        try {
            Date baselineTimestamp = prepareBaselineTimestamp();
            PGMUserSourceAdapter adapter = new PGMUserSourceAdapter();
            Map<String, SkCustomer> customerMap = new HashMap<String, SkCustomer>();//prepareCustomer();
            Connection conn = adapter.getConnection("jdbc/pgmUser", null, null);
            List<SkPaymentItem> itemList = adapter.queryPaymentItem(conn, customerMap, baselineTimestamp);
            conn = adapter.getConnection("jdbc/pgmUser", null, null);
            List<SkPaymentRate> rateList = adapter.queryPaymentRate(conn, baselineTimestamp);
            paymentRateFacade.syncPaymentItemAndRate(baselineTimestamp, itemList, rateList);
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, "Done the sync.");
        } catch (Exception ex) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            java.util.logging.Logger.getLogger(SyncPaymentRateController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void queryAndSyncByEJB() {
        try {
            Date baselineTimestamp = prepareBaselineTimestamp();
            legacyPaymentRateFacade.deletePaymentItemAndRate(baselineTimestamp);
            Map<String, SkCustomer> customerMap = prepareCustomer();
            legacyPaymentRateFacade.syncPaymentItemAndRate(customerMap, baselineTimestamp);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SyncPaymentRateController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Date prepareBaselineTimestamp() {
        Calendar c = Calendar.getInstance();
        c.set(Integer.valueOf(selectedYear).intValue(), Integer.valueOf(selectedMonth).intValue() - 1, 1, 0, 0, 0);
        return c.getTime();
    }

    private Map<String, SkCustomer> prepareCustomer() {
        List<SkCustomer> customerList = customerFacade.findAll();
        Map<String, SkCustomer> map = new HashMap<String, SkCustomer>();
        for (SkCustomer c : customerList) {
            if (!map.containsKey(c.getCode())) {
                map.put(c.getCode(), c);
            }
        }
        return map;
    }
    
    public void changeRowCount(AjaxBehaviorEvent event ){
        if( !StringUtils.isEmpty(selectedYear) && !StringUtils.isEmpty(selectedMonth) ){
            Date baselineTimestamp = prepareBaselineTimestamp();
            String date = DateUtil.getDateFormat(baselineTimestamp, "yyyy/MM");
            setPaymentRateCount(paymentRateFacade.findRowCount(date));
            //setPaymentRateTotalPaymentAmount( paymentRateFacade.findTotalPaymentAmount(date));
            Object[] amountList = paymentRateFacade.findTotalPaymentAmount(date);
            if( amountList != null){
                summaryPaymentRate = new SkPaymentRate();
                if( amountList.length>=1 )
                summaryPaymentRate.setArAmount( (BigDecimal)amountList[0]);
                if( amountList.length>=2 )
                summaryPaymentRate.setPremiumDiscount((BigDecimal)amountList[1]);
                if( amountList.length>=3 )
                summaryPaymentRate.setSalesReturn((BigDecimal)amountList[2]);
                if( amountList.length>=4 )
                summaryPaymentRate.setSalesDiscount((BigDecimal)amountList[3]);
                if( amountList.length>=5 )
                summaryPaymentRate.setPaymentAmount((BigDecimal)amountList[4]);
            }
            setPaymentItemCount(paymentItemFacade.findRowCount(date));
            amountList = paymentItemFacade.findTotalPaymentAmount(date);
            if( amountList != null){
                summaryPaymentItem = new SkPaymentItem();
                if( amountList.length>=1 )
                summaryPaymentItem.setArAmount( (BigDecimal)amountList[0]);
                if( amountList.length>=2 )
                summaryPaymentItem.setPremiumDiscount((BigDecimal)amountList[1]);
                if( amountList.length>=3 )
                summaryPaymentItem.setSalesReturn((BigDecimal)amountList[2]);
                if( amountList.length>=4 )
                summaryPaymentItem.setSalesDiscount((BigDecimal)amountList[3]);
                if( amountList.length>=5 )
                summaryPaymentItem.setPaymentAmount((BigDecimal)amountList[4]);
            }
            setRenderRowCount(true);
        }else
            setRenderRowCount(false);
    }
}
