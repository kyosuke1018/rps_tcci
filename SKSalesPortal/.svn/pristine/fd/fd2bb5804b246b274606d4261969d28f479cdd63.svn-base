package com.tcci.sksp.controller.report;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.controller.util.SelectEmployeeController;
import com.tcci.sksp.entity.ar.SkArRemitItem;
import com.tcci.sksp.entity.ar.SkCheckMaster;
import com.tcci.sksp.entity.enums.RemitMasterStatusEnum;
import com.tcci.sksp.facade.SkArRemitItemFacade;
import com.tcci.sksp.facade.SkCheckMasterFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.ejb.EJB;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@ViewScoped
public class CheckMasterReportController {

    //<editor-fold defaultstate="collapsed" desc="variables">
    private Logger logger = LoggerFactory.getLogger(CheckMasterReportController.class);
    private List<SkCheckMaster> checkMasterList;
    private SkCheckMaster[] selectedCheckMasterList;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    SkCheckMasterFacade facade;
    @EJB
    SkArRemitItemFacade remitFacade;
    @EJB
    TcUserFacade userFacade;
    @ManagedProperty(value = "#{selectEmployeeController}")
    private SelectEmployeeController selectEmployeeController;

    public void setSelectEmployeeController(SelectEmployeeController selectEmployeeController) {
        this.selectEmployeeController = selectEmployeeController;
    }
    @ManagedProperty(value = "#{queryCriteriaController}")
    QueryCriteriaController queryCriteriaController;

    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getter, setter">

    public SkCheckMaster[] getSelectedCheckMasterList() {
        return selectedCheckMasterList;
    }

    public void setSelectedCheckMasterList(SkCheckMaster[] selectedCheckMasterList) {
        this.selectedCheckMasterList = selectedCheckMasterList;
    }

    public List<SkCheckMaster> getCheckMasterList() {
        return checkMasterList;
    }

    public void setCheckMasterList(List<SkCheckMaster> checkMasterList) {
        this.checkMasterList = checkMasterList;
    }
    //</editor-fold>

    public CheckMasterReportController() {
    }

    @PostConstruct
    public void init() {
        setQueryCriteriaController(queryCriteriaController);
    }

    public String doSearchAction() {
        Date startTime = queryCriteriaController.getFilter().getStartTime();
        //--Begin--Modified by nEO Fu on 20120306 fixed bug if only pass end time to query check (lessThenOrEqualTo 2012/02/29 00:00:00, will lost all data which belong 2012/02/29).                    
        Calendar calendar = new GregorianCalendar();
        Date endTime = null;
        if (queryCriteriaController.getFilter().getEndTime() != null) {
            calendar.setTime(queryCriteriaController.getFilter().getEndTime());
            calendar.add(Calendar.DATE, 1);
            endTime = calendar.getTime();
        }
        //<editor-fold defaultstate="collapsed" desc="debug message">
        logger.debug("startTime={}", startTime);
        logger.debug("endTime={}", endTime);
        //</editor-fold>
        //---End---Modified by nEO Fu on 20120306 fixed bug if only pass end time to query check (lessThenOrEqualTo 2012/02/29 00:00:00, will lost all data which belong 2012/02/29).                    
        if (!StringUtils.isEmpty(selectEmployeeController.getSelectedUser())) {
            TcUser user = userFacade.findUserByEmpId(selectEmployeeController.getSelectedUser());
            if (user != null) {
            } else {
                user = new TcUser();
                user.setCname("覆核人不存在!");
            }
            selectEmployeeController.setCname(user.getCname());
            queryCriteriaController.getFilter().setFinanceReviewer(user);
        } else {
            selectEmployeeController.setCname("");
            queryCriteriaController.getFilter().setFinanceReviewer(null);
        }
        try {
            TcUser reviewer = queryCriteriaController.getFilter().getFinanceReviewer();
            Date reviewDateStart = queryCriteriaController.getFilter().getReviewDateStart();
            Date reviewDateEnd = null;
            checkMasterList = new ArrayList<SkCheckMaster>();
            if (queryCriteriaController.getFilter().getReviewDateEnd() != null) {
                calendar = new GregorianCalendar();
                calendar.setTime(queryCriteriaController.getFilter().getReviewDateEnd());
                calendar.add(Calendar.DATE, 1);
                reviewDateEnd = calendar.getTime();
            }
            //<editor-fold defaultstate="collapsed" desc="debug message">
            logger.debug("reviewer={}", reviewer);
            logger.debug("reviewDateStart={}", reviewDateStart);
            logger.debug("reviewDateEnd={}", reviewDateEnd);
            //</editor-fold>
            List<SkCheckMaster> advanceChecks = facade.findByCriteria(startTime,endTime,queryCriteriaController.getFilter().getFinanceReviewer(),reviewDateStart,reviewDateEnd);
            checkMasterList.addAll(advanceChecks);
            List<SkCheckMaster> remitChecks = facade.findByCheckNumbersAndCreatetimestamp(startTime,endTime,queryCriteriaController.getFilter().getFinanceReviewer(),reviewDateStart,reviewDateEnd);
            checkMasterList.addAll(remitChecks);
        } catch (Exception e) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
        return null;
    }

    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        ResourceBundle bundle = ResourceBundle.getBundle("messages");
        String[] title = {
            bundle.getString("checkMaster.createtimestamp"),
            bundle.getString("checkMaster.amount"),
            bundle.getString("checkMaster.billingBank.code"),
            bundle.getString("checkMaster.billingBank.name"),
            bundle.getString("checkMaster.billingAccount"),
            bundle.getString("checkMaster.checkNumber"),
            bundle.getString("checkMaster.dueDate"),
            bundle.getString("checkMaster.customer.code"),
            bundle.getString("checkMaster.customer.name")};
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            header.getCell(i).setCellStyle(cellStyle);
            header.getCell(i).setCellValue(title[i]);
        }

    }
}
