/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.productUpload;

import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.parser.ExcelParser;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import org.primefaces.model.UploadedFile;
import com.tcci.sksp.facade.SkProductUploadFacade;
import com.tcci.sksp.vo.ProductUploadVO;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
/**
 *
 * @author carl.lin
 */
@ManagedBean
@ViewScoped
public class UploadProductController {
    private String selectedYear;
    private List<SelectItem> yearList;
    private UploadedFile uploadedFile;
    @EJB
    SkProductUploadFacade ejbFacade;
    
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
    
      public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        uploadedFile = event.getFile();
        uploadProducts(uploadedFile);
    }
    
    public String doUploadProduct() {
        uploadProducts(this.uploadedFile);
        return null;
    }
    
    public void uploadProducts(UploadedFile uploadedFile) {
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
            prepareUpload(vector);
            String msg = FacesUtil.getMessage("uploadfile") + "\"" + uploadedFile.getFileName() + "\"" + FacesUtil.getMessage("complete");
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, msg);
        } catch (Exception ex) {
            String msg = FacesUtil.getMessage("upload.fail") + "  " + ex.getMessage();
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, msg);
            ex.printStackTrace();
        }
    }
    
    private void prepareUpload(List<List<Object>> vector) {
        List<ProductUploadVO> list = new ArrayList<ProductUploadVO>();
        for (int i = 1; i < vector.size(); i++) {
            List<Object> v = (List<Object>) vector.get(i);

            if (StringUtils.isNotEmpty((String) v.get(0))) {
                ProductUploadVO pd = new ProductUploadVO();
                pd.setYearMonth((String) v.get(0));
                pd.setMatnr((String) v.get(1));
                pd.setUnit(convertString((String) v.get(2)));

                list.add(pd);
            }
        }
        if (list != null) {
            ejbFacade.save(list, selectedYear);
            String msg = "Loader total records is " + list.size();
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, msg);
        }
    }
    
    private BigDecimal convertString(String sAmount) {
        BigDecimal m1Amount = BigDecimal.ZERO;

        if (null != sAmount) {
            m1Amount = new BigDecimal(sAmount).setScale(2, RoundingMode.HALF_UP);
        }
        return m1Amount;
    }
    
} // end of class
