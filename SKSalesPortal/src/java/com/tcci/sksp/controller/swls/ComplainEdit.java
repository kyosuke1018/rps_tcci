/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.swls;

import com.tcci.sksp.controller.util.SelectCustomerController;
import com.tcci.sksp.entity.swls.ComplainUpload;
import com.tcci.sksp.facade.swls.ComplainUploadFacade;
import com.tcci.worklist.controller.util.JsfUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ViewScoped
public class ComplainEdit {
    
    private String id;
    private ComplainUpload complain;
    private Date date;
    private boolean editable = false;
    
    @ManagedProperty(value = "#{selectCustomerController}")
    private SelectCustomerController selectCustomerController;
            
    @EJB
    private ComplainUploadFacade complainUploadFacade;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    
    @PostConstruct
    private void init() {
        id = JsfUtils.getRequestParameter("id");
        if (null == id) {
            createComplain();
        } else {
            editComplain();
        }
    }

    // action
    public void save() {
        try {
            complain.setYmd(sdf.format(date));
            String custCode = selectCustomerController.getSelectedCustomer();
            String custName = selectCustomerController.getCustomerName(custCode);
            if (null == custName)  {
                throw new Exception("客戶資料不正確!");
            }
            complain.setCustId(custCode);
            complain.setCustName(custName);
            complainUploadFacade.save(complain);
            JsfUtils.addSuccessMessage("save success!");
        } catch (Exception ex) {
            JsfUtils.addErrorMessage(ex.getMessage());
        }
    }
    
    // helper
    /*
     * 新增客訴
     */
    private void createComplain() {
        complain = new ComplainUpload();
        date = new Date();
        complain.setYmd(sdf.format(date));
        complain.setReasonClass("外觀異常");
        complain.setCtDrug("N");
        complain.setChStatus("N");
        complain.setReportStatus("N");
        complain.setTurnStatus("N");
        editable = true;
    }
    
    /*
     * 編輯客訴
     */
    private void editComplain() {
        try {
            Long pk = Long.valueOf(id);
            complain = complainUploadFacade.load(pk);
        } catch (Exception ex) {
            JsfUtils.addErrorMessage("id必須為數字!");
            return;
        }
        if (null == complain) {
            JsfUtils.addErrorMessage("資料不存在!");
            return;
        }
        selectCustomerController.setCode(complain.getCustId());
        selectCustomerController.setName(complain.getCustName());
        selectCustomerController.setSelectedCustomer(complain.getCustId());
        try {
            date = sdf.parse(complain.getYmd());
        } catch (ParseException ex) {
        }
        editable = true;
    }
    
    // getter, setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ComplainUpload getComplain() {
        return complain;
    }

    public void setComplain(ComplainUpload complain) {
        this.complain = complain;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public SelectCustomerController getSelectCustomerController() {
        return selectCustomerController;
    }

    public void setSelectCustomerController(SelectCustomerController selectCustomerController) {
        this.selectCustomerController = selectCustomerController;
    }
    
    
}
