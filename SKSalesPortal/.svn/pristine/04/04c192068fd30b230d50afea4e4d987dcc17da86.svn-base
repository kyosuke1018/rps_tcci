/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.remit;

import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.parser.ExcelParser;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkOverdueAr;
import com.tcci.sksp.facade.SkCustomerFacade;
import com.tcci.sksp.facade.SkOverdueArFacade;
import com.tcci.sksp.vo.OverdueArVO;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class UploadOverdueARController {

    private String selectedYear;
    private String selectedMonth;
    private List<SelectItem> yearList;
    private List<SelectItem> monthList;
    private UploadedFile uploadedFile;
    @EJB
    SkCustomerFacade customerFacade;
    @EJB
    SkOverdueArFacade OverdueArFacade;
    
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

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public void handleFileUpload(FileUploadEvent event) {
        //fileUploadListener="#{uploadOverdueARController.handleFileUpload}"
        uploadedFile = event.getFile();
        uploadOverdueData(uploadedFile);
    }
    public String doUploadOverdueARAction(){
        uploadOverdueData( this.uploadedFile );
        return null;
    }
    public void uploadOverdueData(UploadedFile uploadedFile) {
        Logger.getLogger(UploadOverdueARController.class.getName()).log(Level.INFO, "uploadOverdueData start!");
        boolean isNull = false;
        if (StringUtils.isEmpty(selectedYear)) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Please select year");
            isNull = true;
        }
        if (StringUtils.isEmpty(selectedMonth)) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Please select month");
            isNull = true;
        }
        Logger.getLogger(UploadOverdueARController.class.getName()).log(Level.INFO, "uploadOverdueData isNull=" + isNull);
        if (isNull) {
            return;
        }
        Logger.getLogger(UploadOverdueARController.class.getName()).log(Level.INFO, "uploadOverdueData after check year,month");
        try {
            Logger.getLogger(UploadOverdueARController.class.getName()).log(Level.INFO, uploadedFile.getInputstream() + ",size=" + uploadedFile.getSize());
            InputStream is = uploadedFile.getInputstream();
            ExcelParser parser = new ExcelParser();
            parser.setInputStream(is);
            List<List<Object>> vector = parser.parse();
            //printOverdueData(vector);
            prepareOverdueData(vector);
            String msg = FacesUtil.getMessage("uploadfile") + "\"" + uploadedFile.getFileName() + "\"" + FacesUtil.getMessage("complete");
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, msg);
            Logger.getLogger(UploadOverdueARController.class.getName()).log(Level.INFO, msg);
        } catch (Exception ex) {
            String msg = FacesUtil.getMessage("upload.fail") + "  "+ ex.getMessage();
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, msg);
            Logger.getLogger(UploadOverdueARController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    private void printOverdueData(List<List<Object>> vector) {
        for (int i = 0; i < vector.size(); i++) {
            StringBuilder msg = new StringBuilder();
            List<Object> v = (List<Object>) vector.get(i);
            for (int j = 0; j < v.size(); j++) {
                msg.append(v.get(j));
                msg.append(",");
            }
            Logger.getLogger(UploadOverdueARController.class.getName()).log(Level.INFO, msg.toString());
        }
    }
    
    private Date prepareBaselineTimestamp(){
        Calendar c = Calendar.getInstance();
        c.set(Integer.valueOf(selectedYear).intValue(), Integer.valueOf(selectedMonth).intValue()-1, 1, 0, 0, 0);
        return c.getTime();
    }
    
    private void prepareOverdueData(List<List<Object>> vector) {
        List<SkOverdueAr> list = new ArrayList<SkOverdueAr>();
        for (int i = 0; i < vector.size(); i++) {
            List<Object> v = (List<Object>) vector.get(i);
            //for (int j = 0; j < v.size(); j++) {
                /*
                0<td>OVERDUE_AR</td>
                1<td>#{msg['upload.overdue.customer.number']}</td>
                2<td>#{msg['upload.overdue.ar.customer.name']}</td>
                3<td>#{msg['upload.overdue.ar.order.number']}</td>
                4<td>#{msg['upload.overdue.ar.invoice.number']}</td>
                5<td>#{msg['upload.overdue.ar.amount']}</td>
                6<td>#{msg['upload.overdue.ar.sapid']}</td>
                7<td>#{msg['upload.overdue.ar.invoice.date']}</td>
                8<td>#{msg['upload.overdue.ar.day.number']}</td>
                 */
                String key = (String) v.get(0);
                if ("OVERDUE_AR".equalsIgnoreCase(key)) {
                    String customerNumber = (String) v.get(1);
                    //Date baselineTime = (Date) v.get(3);
                    String orderNumber = (String) v.get(3);
                    String invoiceNumber = (String) v.get(4);
                    String amount = (String) v.get(5);
                    String sapid = (String) v.get(6);
                    sapid = checkSapid(sapid);
                    Date invoiceTime = (Date) v.get(7);
                    String overdue = (String) v.get(8);
                    SkCustomer customer = customerFacade.findBySimpleCode(customerNumber);
                    SkOverdueAr overdueAr = new SkOverdueAr();
                    overdueAr.setCustomer(customer);
                    overdueAr.setInvoiceNumber(invoiceNumber);
                    overdueAr.setOrderNumber(orderNumber);
                    overdueAr.setOverdueDaysNumber(Integer.valueOf(overdue));
                    //overdueAr.setBaselineTimestamp(baselineTime);
                    overdueAr.setSapid(sapid);
                    overdueAr.setInvoiceTimestamp(invoiceTime);
                    overdueAr.setInvoiceAmount(BigDecimal.valueOf(Long.valueOf(amount)));
                    list.add(overdueAr);
                }
            //}
        }
        if( list != null ){
            OverdueArFacade.save( list, prepareBaselineTimestamp() );
            String msg = "Loader total records is " + list.size();
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, msg);
        }
    }

    private String checkSapid(String sapid) {
        if (StringUtils.isEmpty(sapid)) {
        }else if (sapid.length() == 2) {
            sapid = "0" + sapid;
        } /*else if (sapid.length() == 1) {
            sapid = "00" + sapid;
        } */
        return sapid;
    }
}
