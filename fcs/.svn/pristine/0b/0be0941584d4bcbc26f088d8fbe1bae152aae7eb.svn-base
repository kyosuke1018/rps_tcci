/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.controller;

import com.tcci.fc.controller.attachment.AttachmentController;
import com.tcci.fc.controller.attachment.AttachmentEventListener;
import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fcs.entity.FcReportTemplate;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.facade.FcMonthlyExchangeRateFacade;
import com.tcci.fcs.facade.FcReportTemplateFacade;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name="templateUpload")
@ViewScoped
public class TemplateUploadController implements AttachmentEventListener {
    private final static Logger logger = LoggerFactory.getLogger(TemplateUploadController.class);
    private final static String[] MONTHS = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    
    private List<String> years;
    private String id;
    private String year;
    private String month;
    private String yearmonth;
    private FcReportTemplate reportTemplate;
    private CompanyGroupEnum group;
    
    // managed bean
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    @ManagedProperty(value = "#{attachmentController}")
    private AttachmentController attachmentController;
    
    @EJB
    private FcReportTemplateFacade reportTemplateFacade;
    @EJB
    private FcMonthlyExchangeRateFacade fcMonthlyExchangeRateFacade;

    @PostConstruct
    private void init() {
        id = JsfUtil.getRequestParameter("id");
        if (null!=JsfUtil.getRequestParameter("groupCode")) {
            group = CompanyGroupEnum.getFromCode(JsfUtil.getRequestParameter("groupCode"));
        }
        if (null==id) {
            appendReportTemplate();
        } else {
            editReportTempate(id);
        }
    }
    
    // action
    public void yearMonthConfirm() {
        yearmonth = year + month;
        //20151005新增樣板前 先檢查開帳年月是否已維護每月匯率
        if (CollectionUtils.isEmpty(fcMonthlyExchangeRateFacade.findByYM(yearmonth))) {
            JsfUtil.addErrorMessage("此報表年月未設定匯率，請至每月匯率設定功能進行設定!");
            return;
        }
        reportTemplate = reportTemplateFacade.findByYearmonth(yearmonth, group);
        if (null == reportTemplate) {
            reportTemplate = new FcReportTemplate(yearmonth);
        }
        attachmentController.init(reportTemplate, false); // readonly false
        attachmentController.setEventListener(this);// for event uploadVerify
    }
    
    // interface implements
    @Override
    public boolean uploadVerify(UploadedFile uploadFile) {
        logger.debug("uploadVerify yearmonth:"+yearmonth);
//        String version = this.fetchVersion(uploadFile);
//        if(null == version){
//            JsfUtil.addErrorMessage("未設定模板版本!");
//        }
//        reportTemplate.setVersion(version);
        return true;
    }
    
    //取得模板報表內的版本號碼
//    private String fetchVersion(UploadedFile uploadFile){
//        InputStream in = null;
//        String versionXls = null;
//        try {
//            in = uploadFile.getInputstream();
//            Workbook workbook = WorkbookFactory.create(in);
//            ExcelValidator validator = new ExcelValidator(workbook);
//            String[] versionSetting = ReportConfig.VERSION_SETTING;
//            Sheet poiSheet = workbook.getSheet(versionSetting[0]);
//            versionXls = validator.getCellValueString(poiSheet, Integer.parseInt(versionSetting[2]), CellReference.convertColStringToIndex(versionSetting[1]));
//        } catch (Exception ex) { // 會有 runtime exception (Could not resolve external workbook name)
//            JsfUtil.addErrorMessage(ex.getMessage());
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException ex) {
//                }
//            }
//        }
//        return versionXls;
//    }
    
    public void save() {
        reportTemplate.setGroup(group);
        reportTemplate.setModifier(userSession.getTcUser());
        reportTemplate.setModifytimestamp(new Date());
        try {
            reportTemplateFacade.save(reportTemplate, attachmentController.getAttachmentVOList());
            
            JsfUtil.addSuccessMessage("儲存成功!");
            // redirect to home
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            try {
//                context.getExternalContext().redirect("home.xhtml");
                context.getExternalContext().redirect("consolidationRevenue_home.xhtml");
            } catch (IOException ex) {
            }
            context.responseComplete();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }
    
    // helper
    public String getPageTitle() {
        return null==id ? "新增樣版" : "編輯樣版";
    }
    
    public String[] getMonths() {
        return MONTHS;
    }
    
    public List<String> getYears() {
        return years;
    }
    
    private void appendReportTemplate() {
        FcReportTemplate last = reportTemplateFacade.findLast(group);
        if (null==last) {
            // 目前月份
            Date now = new Date();
            year = String.format("%tY", now);
            month = String.format("%tm", now);
        } else {
            // 下個月
            int yyyy = Integer.valueOf(last.getYearmonth().substring(0, 4));
            int mm = Integer.valueOf(last.getYearmonth().substring(4, 6));
            mm++;
            if (mm>12) {
                mm = 1;
                yyyy++;
            }
            year = String.format("%04d", yyyy);
            month = String.format("%02d", mm);
        }
        years = new ArrayList<String>();
        int yyyy = Integer.valueOf(year);
        years.add(String.valueOf(yyyy-1));
        years.add(String.valueOf(yyyy));
        years.add(String.valueOf(yyyy+1));
    }
    
    private void editReportTempate(String id) {
        try {
            reportTemplate = reportTemplateFacade.find(Long.valueOf(id));
            if (null == reportTemplate) {
//                appendReportTemplate();
                JsfUtil.addErrorMessage("查無編輯樣板!");
            } else {
                group = reportTemplate.getGroup();
                attachmentController.init(reportTemplate, false); // readonly false
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }
    
    // getter, setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }

    public FcReportTemplate getReportTemplate() {
        return reportTemplate;
    }

    public void setReportTemplate(FcReportTemplate reportTemplate) {
        this.reportTemplate = reportTemplate;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public AttachmentController getAttachmentController() {
        return attachmentController;
    }

    public void setAttachmentController(AttachmentController attachmentController) {
        this.attachmentController = attachmentController;
    }
}
