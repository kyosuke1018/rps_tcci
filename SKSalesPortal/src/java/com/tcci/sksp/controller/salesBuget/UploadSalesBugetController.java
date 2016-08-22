/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.salesBuget;

import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.parser.ExcelParser;
import com.tcci.sksp.facade.SkSalesBugetFacade;
import com.tcci.sksp.vo.SalesBugetVo;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
 * @author carl.lin
 */
@ManagedBean
@ViewScoped
public class UploadSalesBugetController {

    private String selectedYear;
    private List<SelectItem> yearList;
    private UploadedFile uploadedFile;
    @EJB
    SkSalesBugetFacade bugetFacade;

    // getter and setter
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

    @PostConstruct
    public void init() {
        initYear();
    }

    private void initYear() {
        int startYear = 2015;
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        yearList = new ArrayList<SelectItem>();
        for (int i = startYear; i <= currentYear; i++) {
            String year = String.valueOf(i);
            yearList.add(new SelectItem(year, year));
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
        uploadSalesBuget(uploadedFile);
    }

    public String doUploadSalesBuget() {
        uploadSalesBuget(this.uploadedFile);
        return null;
    }

    public void uploadSalesBuget(UploadedFile uploadedFile) {
        boolean isNull = false;
        if (StringUtils.isEmpty(selectedYear)) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Please select year");
            isNull = true;
        }
        try {
            InputStream is = uploadedFile.getInputstream();
            ExcelParser parser = new ExcelParser();
            parser.setInputStream(is);
            List<List<Object>> vector = parser.parse();
            prepareSalesBuget(vector);
            String msg = FacesUtil.getMessage("uploadfile") + "\"" + uploadedFile.getFileName() + "\"" + FacesUtil.getMessage("complete");
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, msg);
        } catch (Exception ex) {
            String msg = FacesUtil.getMessage("upload.fail") + "  " + ex.getMessage();
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, msg);
            ex.printStackTrace();
        }
    }

    private void prepareSalesBuget(List<List<Object>> vector) {
        List<SalesBugetVo> list = new ArrayList<SalesBugetVo>();
        for (int i = 1; i < vector.size(); i++) {
            List<Object> v = (List<Object>) vector.get(i);
//            
//            SAP_ID	CUST_CODE	PRD_TYPE	MATNR	M1_AMOUNT	M1_UNIT	
//            M2_AMOUNT	M2_UNIT	M3_AMOUNT	M3_UNIT	M4_AMOUNT	M4_UNIT	M5_AMOUNT
//            M5_UNIT	M6_AMOUNT	M6_UNIT	M7_AMOUNT	M7_UNIT	M8_AMOUNT	
//             M8_UNIT	M9_AMOUNT	M9_UNIT	M10_AMOUNT	M10_UNIT	M11_AMOUNT
//             M11_UNIT	M12_AMOUNT	M12_UNIT
//                 
            if (StringUtils.isNotEmpty((String) v.get(0))) {
                SalesBugetVo buget = new SalesBugetVo();
                buget.setYearMonth(selectedYear);
                buget.setSapId((String) v.get(0));
                
                String custCode ="NA";
                if (StringUtils.isNotBlank((String) v.get(1))) {
                     custCode = (String) v.get(1);
                }
                buget.setCustCode(custCode);
                buget.setPrdType((String) v.get(2));
                buget.setMatnr((String) v.get(3));

                buget.setM1Amount(convertString((String) v.get(4)));
                buget.setM1unit(convertString((String) v.get(5)));
                buget.setM2Amount(convertString((String) v.get(6)));
                buget.setM2unit(convertString((String) v.get(7)));
                buget.setM3Amount(convertString((String) v.get(8)));
                buget.setM3unit(convertString((String) v.get(9)));
                buget.setM4Amount(convertString((String) v.get(10)));
                buget.setM4unit(convertString((String) v.get(11)));
                buget.setM5Amount(convertString((String) v.get(12)));
                buget.setM5unit(convertString((String) v.get(13)));
                buget.setM6Amount(convertString((String) v.get(14)));
                buget.setM6unit(convertString((String) v.get(15)));
                buget.setM7Amount(convertString((String) v.get(16)));
                buget.setM7unit(convertString((String) v.get(17)));
                buget.setM8Amount(convertString((String) v.get(18)));
                buget.setM8unit(convertString((String) v.get(19)));
                buget.setM9Amount(convertString((String) v.get(20)));
                buget.setM9unit(convertString((String) v.get(21)));
                buget.setM10Amount(convertString((String) v.get(22)));
                buget.setM10unit(convertString((String) v.get(23)));
                buget.setM11Amount(convertString((String) v.get(24)));
                buget.setM11unit(convertString((String) v.get(25)));
                buget.setM12Amount(convertString((String) v.get(26)));
                buget.setM12unit(convertString((String) v.get(27)));

                list.add(buget);
            }
        }
        if (list != null) {
            bugetFacade.save(list, selectedYear);
            String msg = "Loader total records is " + list.size();
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, msg);
        }
    }
    
    private BigDecimal convertString(String sAmount) {
        BigDecimal m1Amount = BigDecimal.ZERO;

        if (null != sAmount) {
            m1Amount = new BigDecimal(sAmount).setScale(3, RoundingMode.HALF_UP);
        }
        return m1Amount;
    }
}
