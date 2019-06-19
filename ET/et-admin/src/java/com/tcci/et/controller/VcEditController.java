/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.util.JsfUtils;
import com.tcci.ec.model.VenderVO;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import static com.tcci.et.controller.VenderCategoryController.FUNC_OPTION;
import com.tcci.et.enums.FormStatusEnum;
import com.tcci.et.facade.EtVenderFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "vcEdit")
@ViewScoped
public class VcEditController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 31;
    
    @EJB private EtVenderFacade etVenderFacade;
    
    protected VenderVO form;
    private boolean initSuccess;
    
    private String selectedVenderTxt;
    private List<VenderVO> allLfa1List; // 可選取全部供應商 
    
    @PostConstruct
    private void init(){
        
        fetchInputParameters();
        
        allLfa1List = etVenderFacade.findLfa1ByCriteria(new BaseCriteriaVO());
        
    }
    
    /**
     * 取得輸入參數
     */
    private void fetchInputParameters() {
        // 回饋
//        String mandt = JsfUtils.getRequestParameter("mandt");
//        String venderCode = JsfUtils.getRequestParameter("venderCode");
        try {
            if (JsfUtils.getRequestParameter("id") == null) {
//                applicant = userSession.getEmployee();
//                logger.debug("init"+applicant);
                initNewForm();
            } else {
                loadForm();
            }
            
            initSuccess = true;
        } catch (Exception ex) {
            JsfUtils.addErrorMessage(ex.getMessage());
            initSuccess = false;
        }
    }
    
    private void initNewForm() throws Exception {
        form = new VenderVO();
        form.setCreatorId(this.getLoginUserId());
//        form.setStatus(FormStatusEnum.DRAFT);
    }
    
    protected void loadForm() throws Exception {
//        Long id = getLongParam("id");
//        form = formFacade.find(id);
//        if (null == form) {
//            throw new Exception("無此申請單(" + id + ")!");
//        }
//        if (form.getProcess() != null) {
//            activities = bpmEngine.findProcessActivitiesFlow(form.getProcess(), true);
//        }
//        if (!isFormViewable()) {
//            form = null;
//            activities = null;
//            throw new Exception("無權檢視此申請單(" + id + ")");
//        }
//        FormStatusEnum status = form.getStatus();
//        if (activities != null && !activities.isEmpty()) {
//            activeIndex = 1;
//        } else {
//            activeIndex = 0;
//        }
    }
    
    /**
     * 選取 autoComplete 供應商列表
     * @param intxt
     * @return 
     */
    public List<String> autoCompleteVenderOptions(String intxt){
        List<String> resList = new ArrayList<>();
        
        for(VenderVO vender : allLfa1List){// 有權選取的所有User
            String txt = vender.getLabel();
            if( txt.toUpperCase().indexOf(intxt.toUpperCase()) >= 0 ){// 符合輸入
                resList.add(txt);
            }
        }
        
        return resList;
    }
    
    public void venderCheck(){
        VenderVO selectedVender = null;
        for(VenderVO vender : allLfa1List){// 有權選取的所有User
            String txt = vender.getLabel();
            if( txt.toUpperCase().indexOf(selectedVenderTxt.toUpperCase()) >= 0 ){// 符合輸入
                selectedVender = vender;
            }
        }
        if(selectedVender!=null){
            form.setMandt(selectedVender.getMandt());
            form.setVenderCode(selectedVender.getVenderCode());
            form.setCname(selectedVender.getCname());
        }
    }
    
    @Override
    public String getFuncTitle(){
//        return sessionController.getFunctionTitle(FUNC_OPTION);
        return "vc edit";
    }

    
    // getter, setter
    public VenderVO getForm() {
        return form;
    }

    public void setForm(VenderVO form) {
        this.form = form;
    }

    public String getSelectedVenderTxt() {
        return selectedVenderTxt;
    }

    public void setSelectedVenderTxt(String selectedVenderTxt) {
        this.selectedVenderTxt = selectedVenderTxt;
    }
    
    
}
