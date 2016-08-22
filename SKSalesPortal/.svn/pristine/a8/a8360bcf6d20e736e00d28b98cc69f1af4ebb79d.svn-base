/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.remit;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.util.ExcelUtil;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.controller.util.RemitMasterDataModel;
import com.tcci.sksp.controller.util.SelectCustomerController;
import com.tcci.sksp.controller.util.SelectEmployeeController;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkArRemitItem;
import com.tcci.sksp.entity.ar.SkArRemitMaster;
import com.tcci.sksp.entity.ar.SkFiMasterInterface;
import com.tcci.sksp.entity.enums.RemitMasterStatusEnum;
import com.tcci.sksp.facade.SkArRemitMasterFacade;
import com.tcci.sksp.facade.SkCustomerFacade;
import com.tcci.sksp.facade.SkFiMasterInterfaceFacade;
import com.tcci.sksp.vo.RemitExcelVO;
import com.tcci.sksp.vo.RemitMasterVO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class RemitQueryController {

    //<editor-fold defaultstate="collapsed" desc="parameters">
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ResourceBundle rb = ResourceBundle.getBundle("messages");
    private List<RemitMasterVO> remitMasterVOList = new ArrayList<RemitMasterVO>();
    private RemitMasterVO[] selectedMasterVOList;
    private RemitMasterDataModel masterDataModel;
    private SkArRemitMaster abandonedRemit;
    //private String view = "";
    private boolean selectable = false;
    private boolean editable = false;
    private boolean reviewable = false;
    private boolean selectAll = false;
    //
    private StreamedContent exportFile; // 匯出檔案
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    SkArRemitMasterFacade arRemitMasterFacade;
    @EJB
    SkCustomerFacade customerFacade;
    @EJB
    TcUserFacade userFacade;
    @EJB
    SkFiMasterInterfaceFacade interfaceFacade;
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
    @ManagedProperty(value = "#{selectCustomerController}")
    SelectCustomerController selectCustomerController;

    public void setSelectCustomerController(SelectCustomerController selectCustomerController) {
        this.selectCustomerController = selectCustomerController;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getter, setter">

    public RemitMasterDataModel getMasterDataModel() {
        return masterDataModel;
    }

    public void setMasterDataModel(RemitMasterDataModel masterDataModel) {
        this.masterDataModel = masterDataModel;
    }

    public List<RemitMasterVO> getRemitMasterVOList() {
        return remitMasterVOList;
    }

    public void setRemitMasterVOList(List<RemitMasterVO> remitMasterVOList) {
        this.remitMasterVOList = remitMasterVOList;
    }

    public RemitMasterVO[] getSelectedMasterVOList() {
        return selectedMasterVOList;
    }

    public void setSelectedMasterVOList(RemitMasterVO[] selectedMasterVOList) {
        this.selectedMasterVOList = selectedMasterVOList;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isReviewable() {
        return reviewable;
    }

    public void setReviewable(boolean reviewable) {
        this.reviewable = reviewable;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    } 
    
    public SkArRemitMaster getAbandonedRemit() {
        return abandonedRemit;
    }

    public void setAbandonedRemit(SkArRemitMaster abandonedRemit) {
        this.abandonedRemit = abandonedRemit;
    }    

    public StreamedContent getExportFile() {
        return exportFile;
    }
    //</editor-fold>

    @PostConstruct
    private void init() {
        logger.debug("init()");
        FacesContext context = FacesContext.getCurrentInstance();
        //TODO: hard code folder name in source code, need change code when web page folder (finance) change name.
        if (context.getViewRoot().getViewId().contains("finance")) {
            //for uploadRemit.xhtml
            if (context.getViewRoot().getViewId().contains("upload")) {
                selectable = true;
                editable = false;
                reviewable = false;
            } else {
                selectable = false;
                editable = false;
                reviewable = true;
            }
        } else {
            selectable = true;
            editable = true;
            reviewable = false;
        }             
        String doSearch = FacesUtil.getRequestParameter("doSearch");
        if ("true".equals(doSearch)) {
            doSearchRemitMasterAction();            
        }
        // fix redirect keep message has bug issue
        String showConfirmMsg = FacesUtil.getRequestParameter("showConfirmMsg");
        if ("true".equals(showConfirmMsg)) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, FacesUtil.getMessage("remit.confirm.success"));
        }
    }

    public String doSearchRemitMasterAction() {
        logger.debug("doSearchRemitMasterAction()");
        remitMasterVOList.clear();
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
            queryCriteriaController.getFilter().setSkCustomer(customer);
            selectCustomerController.setName(customer.getName());
        } else {
            queryCriteriaController.getFilter().setSkCustomer(null);
            selectCustomerController.setName("");
        }
        //creator
        String empId = "";
        if (selectEmployeeController.getSelectedUser() != null) {
            empId = selectEmployeeController.getSelectedUser();
        }
        logger.debug("empId={}", empId);
        if (!StringUtils.isEmpty(empId)) {
            TcUser user = userFacade.findUserByEmpId(empId);
            logger.debug("user={}", user);
            if (user == null) {
                user = new TcUser();
                user.setCname("建立者不存在!");
            }
            queryCriteriaController.getFilter().setCreator(user);
            selectEmployeeController.setCname(user.getCname());
        } else {
            queryCriteriaController.getFilter().setCreator(null);
            selectEmployeeController.setCname("");
        }

        logger.debug("filter={}", queryCriteriaController.getFilter());
        List<SkArRemitMaster> masters = arRemitMasterFacade.findRemitMasterByCriteria(queryCriteriaController.getFilter());
        for (SkArRemitMaster master : masters) {
            SkFiMasterInterface masterInterface = interfaceFacade.findByReferenceObject(master);
            boolean selected = true;
            if (masterInterface != null || master.getStatus().equals(RemitMasterStatusEnum.CANCELED)) {
                selected = false;
            }
            RemitMasterVO vo = new RemitMasterVO();
            vo.setRemitMaster(master);
            vo.setSelected(selected);
            remitMasterVOList.add(vo);
        }

        logger.debug("remitMasterList={}", remitMasterVOList);
        masterDataModel = new RemitMasterDataModel(remitMasterVOList);
        return null;
    }

    public void exportRemitItems() {
        List<Long> masterIds = new ArrayList<Long>();
        for (RemitMasterVO master : remitMasterVOList ) {
            masterIds.add(master.getRemitMaster().getId());
        }
        List<SkArRemitItem> items = arRemitMasterFacade.findRemitItemByMasters(masterIds);
        List<RemitExcelVO> listVO = new ArrayList<RemitExcelVO>();
        for (SkArRemitItem item : items) {
            listVO.add(new RemitExcelVO(item));
        }
        InputStream in = this.getClass().getResourceAsStream("/remitItem.xlsx");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ExcelUtil.exportList(in, listVO, 1, out);
        } catch (Exception ex) {
            logger.error("exportList exception", ex);
        }
        exportFile = new DefaultStreamedContent(
                new ByteArrayInputStream(out.toByteArray()),
                "application/octet-stream",
                "remitItem.xlsx");
    }

    public void selectAllChange(AjaxBehaviorEvent event) {
        for (RemitMasterVO vo : remitMasterVOList) {
            vo.setSelected(selectAll);
        }
    }

    // dummy listener
    // 確認 checkbox 會被更新
    public void selectedChange(AjaxBehaviorEvent event) {
        /*
        int selectedCount = 0;
        for (RemitMasterVO vo : remitMasterVOList) {
            if (vo.isSelected())
                selectedCount++;
        }
        logger.debug("selectedChange, selectedCount = {}", selectedCount);
        * 
        */
    }

    public List<SkArRemitMaster> getSelectedMasters() {
        List<SkArRemitMaster> masters = new ArrayList<SkArRemitMaster>();
        for (RemitMasterVO vo : remitMasterVOList) {
            if (vo.isSelected()) {
                masters.add(vo.getRemitMaster());
            }
        }
        return masters;
    }

    public SkArRemitMaster[] getSelectedMasterList() {
        List<SkArRemitMaster> masters = getSelectedMasters();
        SkArRemitMaster[] list = new SkArRemitMaster[masters.size()];
        return masters.toArray(list);
    }

    /**
     * A function to detect remit can confirm or not.
     * (1) if remit has been transfer to advance payment, it can't be confirm.
     * (2) if remit has been upload to SAP, it can't be confirm.
     * @param remitMaster A skArRemitMaster class.
     * @return boolean a flag to indicate remit can confirm or not.
     */
    public boolean canConfirm(SkArRemitMaster remitMaster) {
        boolean canConfirm = true;
        if (RemitMasterStatusEnum.TRANSFER_ADVANCE.equals(remitMaster.getStatus())) {
            canConfirm = false;
        }
        SkFiMasterInterface fiInterface = interfaceFacade.findByReferenceObject(remitMaster);
        if (fiInterface != null) {
            canConfirm = false;
        }
        return canConfirm;
    }

    /**
     *  A function to detect remit can transfer advance payment or not.
     * @param remitMaster A skArRemitMaster class.
     * @return  boolean A flag to indicate remit can transfer to advance payment or not.
     */
    public boolean canTransfer(SkArRemitMaster remitMaster) {
        boolean canTransfer = true;
        if (RemitMasterStatusEnum.REVIEWED.equals(remitMaster.getStatus())) {
            canTransfer = false;
        }
        if (RemitMasterStatusEnum.TRANSFER_ADVANCE.equals(remitMaster.getStatus())) {
            canTransfer = false;
        }
        SkFiMasterInterface fiInterface = interfaceFacade.findByReferenceObject(remitMaster);
        if (fiInterface != null) {
            canTransfer = false;
        }
        return canTransfer;
    }
    
    public void cancel() {
        if (abandonedRemit != null) {
            abandonedRemit.setStatus(RemitMasterStatusEnum.CANCELED);
            arRemitMasterFacade.edit(abandonedRemit);        
        }
    }    
    
}
