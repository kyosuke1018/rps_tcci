/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.controller.sheetdata;

import com.tcci.fc.controller.BaseController;
import com.tcci.fc.controller.attachment.AttachmentController;
import com.tcci.fc.controller.attachment.AttachmentEventListener;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcUserCompGroupR;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.model.global.GlobalConstant;
import com.tcci.irs.entity.attachment.IrsReportUpload;
import com.tcci.irs.entity.sheetdata.IrsCompanyClose;
import com.tcci.irs.enums.PagecodeRoleEnum;
import com.tcci.irs.facade.attachment.IrsReportUploadFacade;
import com.tcci.irs.facade.sheetdata.IrsCompanyCloseFacade;
import com.tcci.irs.facade.sheetdata.ZtfiAfrcInvoFacade;
import com.tcci.irs.facade.sheetdata.ZtfiAfrcTranFacade;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author gilbert
 */
@ManagedBean(name = "importSheetDataTabsController")
@ViewScoped
public class ImportSheetDataTabsController extends BaseController implements AttachmentEventListener {

    //<editor-fold defaultstate="collapsed" desc="Injects">
    @EJB
    private ContentFacade contentFacade;
    @EJB
    ZtfiAfrcInvoFacade ztfiAfrcInvoFacade;
    @EJB
    ZtfiAfrcTranFacade ztfiAfrcTranFacade;
    @EJB
    IrsCompanyCloseFacade irsCompanyCloseFacade;
    @EJB
    IrsReportUploadFacade irsReportUploadFacade;
    @EJB
    private FcCompanyFacade companyFacade;
    //
    @ManagedProperty(value = "#{attachmentController}")
    private AttachmentController attachmentController;

    public void setAttachmentController(AttachmentController attachmentController) {
	this.attachmentController = attachmentController;
    }
    //

    @ManagedProperty(value = "#{importZtcoRelaGuiController}")
    private ImportZtcoRelaGuiController importZtcoRelaGuiController;

    public ImportZtcoRelaGuiController getImportZtcoRelaGuiController() {
	return importZtcoRelaGuiController;
    }

    public void setImportZtcoRelaGuiController(ImportZtcoRelaGuiController importZtcoRelaGuiController) {
	this.importZtcoRelaGuiController = importZtcoRelaGuiController;
    }
    @ManagedProperty(value = "#{importZtcoRelaDtlController}")
    private ImportZtcoRelaDtlController importZtcoRelaDtlController;

    public ImportZtcoRelaDtlController getImportZtcoRelaDtlController() {
	return importZtcoRelaDtlController;
    }

    public void setImportZtcoRelaDtlController(ImportZtcoRelaDtlController importZtcoRelaDtlController) {
	this.importZtcoRelaDtlController = importZtcoRelaDtlController;
    }
    //

    //
    //</editor-fold>    
    
    private static final String TEMP_FILENAME = "IRS_nonSAP_template_v102.xls";
    private FcCompany company;
    private boolean skip;
    //
    private String yearMonth;
    private List<IrsCompanyVO> allCompanyVO = new ArrayList<IrsCompanyVO>();
    private List<IrsCompanyVO> voList = new ArrayList<IrsCompanyVO>();
    private IrsReportUpload reportUpload;
    private IrsCompanyClose irsCompanyClose;
    private boolean stopUpload = false;
    private List<IrsReportUpload> uploadList; // 該年月上傳的報表

    private List<SelectItem> groupsOptionList;//企業團
    private CompanyGroupEnum groupCondition;
    private List<FcUserCompGroupR> cgList;
    private boolean isAdmin;

    private List<SelectItem> companyOptions;//工廠
//    private IrsReportUpload reportUpload;
    private boolean noPermission = false;

    private int dispMode = GlobalConstant.DISPMODE_ALL;
//    private List<FcCompany> companyList;
    private StreamedContent downloadFile;

    @PostConstruct
    @Override
    protected void init() {
	String controllerClass = getClass().getSimpleName();
	String controllerInstance = controllerClass.substring(0, 1).toLowerCase() + controllerClass.substring(1);
	setPageTitle(rb.getString(controllerInstance + ".pageTitle"));
	setPageCode(PagecodeRoleEnum.I11.getPageCode());

	try {
	    String strDispMode = JsfUtil.getCookieValue(GlobalConstant.COOKIE_DISPMODE);
	    dispMode = Integer.valueOf(strDispMode).intValue();
	} catch (Exception ex) {
	}
	//20151119 增加多選公司群組
	cgList = userSession.getTcUser().getCompGroupList();
	isAdmin = userSession.isUserInRole("ADMINISTRATORS");
	//公司群組權限
	groupsOptionList = this.buildGroupOptions();
	if (groupsOptionList == null || groupsOptionList.isEmpty()) {
	    noPermission = true;
	    return;
	}
	if (this.isAdmin) {
	    groupCondition = CompanyGroupEnum.TCC;//預設台泥
	} else {
	    if (this.cgList != null && !this.cgList.isEmpty()) {
		groupCondition = this.cgList.get(0).getGroup();
	    }
	}
	irsCompanyClose = irsCompanyCloseFacade.findByGroup(null);
	yearMonth = irsCompanyClose.getYearMonth();
	stopUpload = irsCompanyClose.isStopUpload();
	
	findUploadReports();
	buildCompanyVOList();
    }

    private List<SelectItem> buildGroupOptions() {
	List<SelectItem> options = new ArrayList();
	for (CompanyGroupEnum item : CompanyGroupEnum.values()) {
	    //admin有所有公司群組權限
	    if (this.isAdmin) {
		options.add(new SelectItem(item, item.getName()));
	    } else {
		if (this.cgList != null && !this.cgList.isEmpty()) {
		    for (FcUserCompGroupR cg : this.cgList) {
			if (item.equals(cg.getGroup())) {
			    options.add(new SelectItem(item, item.getName()));
			}
		    }
		}
	    }
	}
	return options;
    }

    private void findUploadReports() {
	uploadList = irsReportUploadFacade.findByYearmonth(yearMonth, groupCondition);
    }

    private void buildCompanyVOList() {
	List<FcCompany> companyList = fetchCompanyList();
	allCompanyVO.clear();
	for (FcCompany company : companyList) {
	    IrsReportUpload reportUpload = null;
	    for (IrsReportUpload rpt : uploadList) {
		if (rpt.getCompanyCode().getCode().equals(company.getCode())) {
		    reportUpload = rpt;
		    break;
		}
	    }
	    IrsCompanyVO vo = new IrsCompanyVO();
	    vo.setCompany(company);
	    vo.setReportUpload(reportUpload);
	    allCompanyVO.add(vo);
	}
	dispModeChange();
    }

    public void dispModeChange() {
	logger.debug("dispModeChange dispMode:" + dispMode);
	voList.clear();
	if (GlobalConstant.DISPMODE_UPLOADED == dispMode) {
	    for (IrsCompanyVO vo : allCompanyVO) {
		if (vo.getReportUpload() != null) {
		    voList.add(vo);
		}
	    }
	} else if (GlobalConstant.DISPMODE_NOT_UPLOADED == dispMode) {
	    for (IrsCompanyVO vo : allCompanyVO) {
		if (vo.getReportUpload() == null) {
		    voList.add(vo);
		}
	    }
	} else {
	    voList.addAll(allCompanyVO);
	}
	JsfUtil.saveCookie(GlobalConstant.COOKIE_DISPMODE, String.valueOf(dispMode), GlobalConstant.COOKIE_DISPMODE_MAXAGE);
    }

    public void changeGroup() {
	logger.debug("changeGroup:" + this.groupCondition);
	findUploadReports();
	buildCompanyVOList();
    }

    public boolean isUploadable() {
	return !stopUpload;
    }

    public boolean isUploadExpired(IrsCompanyVO vo) {
	if (vo.getReportUpload() != null) {
	    return true;
	}
	return false;
    }

    private List<FcCompany> fetchCompanyList() {
	List<FcCompany> companyList = new ArrayList<>();
	if (userSession.isUserInRole("ADMINISTRATORS,FINANCIAL_HQ")) {
	    companyList = companyFacade.findAllActiveNonsap(groupCondition);
	} else {
	    List<FcCompany> result = companyFacade.findByUploaderR(userSession.getTcUser(), groupCondition);
	    for (FcCompany company : result) {
		if (company.isNonSap()) {//非SAP上傳公司
		    companyList.add(company);
		}
	    }
	}
	return companyList;
    }

    public void selectCompany() {
//待補
//yearMonth
//        List<IrsCompanyClose> yearMonthlist = irsCompanyCloseFacade.find(company);
//        IrsCompanyClose companyClose = ListUtils.getFirstElement(yearMonthlist);
//        if(null != companyClose){
//            yearMonth = companyClose.getYearMonth();     
//        }
//待補

    }

    @Override
    public String onFlowProcess(FlowEvent event) {
	if (skip) {
	    skip = false;   //reset in case user goes back
	    return "confirm";
	} else {
//            if (null==reportUpload) {
//                reportUpload = new IrsReportUpload();
//            }
	    boolean stopUpload = false;
	    attachmentController.init(reportUpload, stopUpload);
	    attachmentController.setEventListener(this);

	    return event.getNewStep();
	}
    }

    @Override
    public boolean uploadVerify(UploadedFile uploadFile) {
//待補
	//importZtcoRelaDtlController
	importZtcoRelaDtlController.reset();
	importZtcoRelaDtlController.setCompany(company);
	importZtcoRelaDtlController.setYearMonth(yearMonth);
	importZtcoRelaDtlController.handleFileUpload(uploadFile);
	//importMonUserController
	importZtcoRelaGuiController.reset();
	importZtcoRelaGuiController.setCompany(company);
	importZtcoRelaGuiController.setYearMonth(yearMonth);
	importZtcoRelaGuiController.handleFileUpload(uploadFile);

//待補
	//
	return true;
    }
    
    public void downloadTemplate() {
        String path = GlobalConstant.REPORT_TEMPLATE_PATH + TEMP_FILENAME;
        InputStream in = this.getClass().getResourceAsStream(path);
        try {
            downloadFile = new DefaultStreamedContent(in, "application/octet-stream", TEMP_FILENAME);
        } catch (Exception ex) {
            logger.error("export exception!", ex);
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="private method">
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public FcCompany getCompany() {
	return company;
    }

    public void setCompany(FcCompany company) {
	this.company = company;
    }
    
    public List<SelectItem> getGroupsOptionList() {
	return groupsOptionList;
    }

    public void setGroupsOptionList(List<SelectItem> groupsOptionList) {
	this.groupsOptionList = groupsOptionList;
    }

    public CompanyGroupEnum getGroupCondition() {
	return groupCondition;
    }

    public void setGroupCondition(CompanyGroupEnum groupCondition) {
	this.groupCondition = groupCondition;
    }

    public int getDispMode() {
	return dispMode;
    }

    public void setDispMode(int dispMode) {
	this.dispMode = dispMode;
    }

//    public List<FcCompany> getCompanyList() {
//	return companyList;
//    }
//
//    public void setCompanyList(List<FcCompany> companyList) {
//	this.companyList = companyList;
//    }
    public List<IrsCompanyVO> getVoList() {
	return voList;
    }

    public void setVoList(List<IrsCompanyVO> voList) {
	this.voList = voList;
    }

    public String getYearMonth() {
	return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
	this.yearMonth = yearMonth;
    }

    public boolean isStopUpload() {
	return stopUpload;
    }

    public void setStopUpload(boolean stopUpload) {
	this.stopUpload = stopUpload;
    }
    
    public StreamedContent getDownloadFile() {
        return downloadFile;
    }
    //</editor-fold>
}
