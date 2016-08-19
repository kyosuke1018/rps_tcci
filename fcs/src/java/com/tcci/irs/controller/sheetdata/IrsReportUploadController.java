/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.controller.sheetdata;

import com.tcci.fc.controller.BaseController;
import com.tcci.fc.controller.attachment.AttachmentController;
import com.tcci.fc.controller.attachment.AttachmentEventListener;
import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcCurrency;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.facade.FcCurrencyFacade;
import com.tcci.irs.entity.IrsTranUploadRecord;
import com.tcci.irs.entity.attachment.IrsReportUpload;
import com.tcci.irs.entity.sheetdata.IrsCompanyClose;
import com.tcci.irs.entity.sheetdata.ZtfiAfrcInvo;
import com.tcci.irs.entity.sheetdata.ZtfiAfrcTran;
import com.tcci.irs.facade.IrsTranUploadRecordFacade;
import com.tcci.irs.facade.attachment.IrsReportUploadFacade;
import com.tcci.irs.facade.sheetdata.IrsCompanyCloseFacade;
import com.tcci.irs.facade.sheetdata.ZtfiAfrcInvoFacade;
import com.tcci.irs.facade.sheetdata.ZtfiAfrcTranFacade;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Mendel.lee
 */
@ManagedBean(name = "irsReportUpload")
@ViewScoped
public class IrsReportUploadController extends BaseController implements AttachmentEventListener {

    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
    
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    @ManagedProperty(value = "#{attachmentController}")
    private AttachmentController attachmentController;
    @ManagedProperty(value = "#{importZtcoRelaGuiController}")
    private ImportZtcoRelaGuiController importZtcoRelaGuiController;
    @ManagedProperty(value = "#{importZtcoRelaDtlController}")
    private ImportZtcoRelaDtlController importZtcoRelaDtlController;

    @Inject
    private ContentFacade contentFacade;
    @Inject
    private IrsReportUploadFacade irsReportUploadFacade;
    @Inject
    private FcCompanyFacade companyFacade;
    @Inject
    private FcCurrencyFacade currencyFacade;
    @Inject
    private IrsCompanyCloseFacade irsCompanyCloseFacade;
    @Inject
    private ZtfiAfrcInvoFacade ztfiAfrcInvoFacade;
    @Inject
    private ZtfiAfrcTranFacade ztfiAfrcTranFacade;
    @Inject
    private IrsTranUploadRecordFacade irsTranUploadRecordFacade;

    private String yearMonth;
    private FcCompany company;
    private IrsReportUpload reportUpload;
    private IrsCompanyClose irsCompanyClose;

    private boolean isShowMessage = false;
    private String showMessage = "";

    // 檔案上傳驗證－公司
    private Set<String> companySet = new HashSet<>();
    // 檔案上傳驗證－幣別
    private Set<String> currencySet = new HashSet<>();
    // 檔案上傳驗證－會科
    private Set<String> accountsSet = new HashSet<>();

    @PostConstruct
    @Override
    protected void init() {
	String code = JsfUtil.getRequestParameter("code");
	String yyyymm = JsfUtil.getRequestParameter("yyyymm");

	try {
	    if (code != null) {
		company = companyFacade.findByCode(code);
	    }
            
            if (yyyymm!=null) {
                yearMonth = yyyymm;
                irsCompanyClose = irsCompanyCloseFacade.findByYearMonth(yearMonth);
            }else{
                irsCompanyClose = irsCompanyCloseFacade.findByGroup(null);
                yearMonth = irsCompanyClose.getYearMonth();
            }
//	    irsCompanyClose = irsCompanyCloseFacade.findByYearMonth(yearMonth);
	} catch (Exception ex) {
	}
	
	if (null == yearMonth || null == company) {
	    JsfUtil.addErrorMessage("參數有誤!");
	    return;
	}

	// 檢查權限
	boolean isNonSap = company.isNonSap();
	if (!isNonSap) {
	    JsfUtil.addErrorMessage("無此公司上傳權限!");
	    return;
	}

	boolean isAdmin = userSession.isUserInRole("ADMINISTRATORS,FINANCIAL_HQ");
	boolean isCompanyUploader = companyFacade.isUploader(userSession.getTcUser(), company);
	if (!isAdmin && !isCompanyUploader) {
	    JsfUtil.addErrorMessage("無此公司上傳權限!");
	    return;
	}
	
	boolean stopUpload = false;
//        if (yyyymm!=null && !yearMonth.equals(yyyymm)) {
	if (irsCompanyClose == null) {
	    JsfUtil.addErrorMessage("該月份未開帳(停止報表上傳)");
	    stopUpload = true;
	} else {
	    stopUpload = irsCompanyClose.isStopUpload();
	    if (stopUpload) {
		JsfUtil.addErrorMessage("關帳中(停止報表上傳)");
	    }
	}

	reportUpload = irsReportUploadFacade.findByYearmonthCompany(yearMonth, company);
	attachmentController.init(reportUpload, stopUpload);
	attachmentController.setEventListener(this);
	if (null == reportUpload) {
	    reportUpload = new IrsReportUpload();
	}

	// 檔案上傳驗證－公司
	List<FcCompany> companyList = companyFacade.findAll();
	for (FcCompany company : companyList) {
	    companySet.add(company.getCode());
	}
	importZtcoRelaGuiController.setCompanySet(companySet);
	importZtcoRelaDtlController.setCompanySet(companySet);
	
	// 檔案上傳驗證－幣別
	List<FcCurrency> currencyList = currencyFacade.findAll();
	for (FcCurrency currency : currencyList) {
	    currencySet.add(currency.getCode());
	}
	importZtcoRelaGuiController.setCurrencySet(currencySet);
	importZtcoRelaDtlController.setCurrencySet(currencySet);
        
        // 檔案上傳驗證－會科
        List<String> accountList = ztfiAfrcTranFacade.findDetailAccount();
        for (String account : accountList) {
	    accountsSet.add(account);
	}
        importZtcoRelaDtlController.setAccountsSet(accountsSet);
	
	// 取得已上傳資料－交易資料
	List<ZtfiAfrcTranVO> voTranlist = new ArrayList<>();
	List<ZtfiAfrcTran> entityTranList = ztfiAfrcTranFacade.find(company.getCode(), yearMonth);
	if (entityTranList != null) {
	    for (ZtfiAfrcTran entity : entityTranList) {
		ZtfiAfrcTranVO vo = new ZtfiAfrcTranVO();
		try {
		    BeanUtils.copyProperties(vo, entity);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		vo.setBudat(sdfDate.format(entity.getBudat()));
		voTranlist.add(vo);
	    }
	}
	importZtcoRelaDtlController.setFiltedlist(voTranlist);
	
	// 取得已上傳資料－傳票資料
	List<ZtfiAfrcInvoVO> voInvolist =  new ArrayList<ZtfiAfrcInvoVO>();
	List<ZtfiAfrcInvo> entityInvoList = ztfiAfrcInvoFacade.find(company.getCode(), yearMonth);
	if (entityInvoList != null) {
	    for (ZtfiAfrcInvo entity : entityInvoList) {
		ZtfiAfrcInvoVO vo = new ZtfiAfrcInvoVO();
		try {
		    BeanUtils.copyProperties(vo, entity);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		vo.setBudat(sdfDate.format(entity.getBudat()));
		voInvolist.add(vo);
	    }
	}
	importZtcoRelaGuiController.setFiltedlist(voInvolist);
    }

    @Override
    public boolean uploadVerify(UploadedFile uploadFile) {

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

	return true;
    }

    //<editor-fold defaultstate="collapsed" desc="saveData">
    public boolean allowSave() {
	if (importZtcoRelaDtlController.isAllSuccess()
		&& importZtcoRelaGuiController.isAllSuccess()
		) {
	    return valid;
	}

	return !valid;
    }

    @Override
    protected boolean saveData() throws Exception {

	// 檢查機制
	if (StringUtils.isEmpty(importZtcoRelaDtlController.getFileName())
		&& StringUtils.isEmpty(importZtcoRelaDtlController.getFileName())) {
	    JsfUtil.addErrorMessage("請上傳報表檔案！");
            return !valid;
	}
//	if (CollectionUtils.isEmpty(importZtcoRelaDtlController.getDatalist())
//		&& CollectionUtils.isEmpty(importZtcoRelaGuiController.getDatalist())) {
//	    JsfUtil.addErrorMessage("檢核失敗！二個Sheet皆無資料！");
//            return !valid;
//	}
	
	String companyCode = company.getCode();	
	ztfiAfrcTranFacade.remove(companyCode, yearMonth);
	ztfiAfrcInvoFacade.remove(companyCode, yearMonth);

	importZtcoRelaDtlController.save();
	importZtcoRelaGuiController.save();

	// 記錄上傳資料到IRS_REPORT_UPLOAD
	reportUpload.setYearMonth(yearMonth);
	reportUpload.setCompanyCode(company);
	reportUpload.setPageCode("I11");

	reportUpload.setCreatetimestamp(new Date());
	reportUpload.setCreator(userSession.getTcUser());
	if (reportUpload.getId() == null) {
            irsReportUploadFacade.create(reportUpload);
        } else {
            irsReportUploadFacade.edit(reportUpload);
        }
	
	// 記錄上傳檔案
	contentFacade.saveContent(reportUpload, attachmentController.getAttachmentVOList(), userSession.getTcUser());
	
	// 新增一筆到IRS_TRAN_UPLOAD_RECORD
	IrsTranUploadRecord irsTranUploadRecord = irsTranUploadRecordFacade.findByYearmonthCompany(yearMonth, company);
	if (irsTranUploadRecord == null) {
	    irsTranUploadRecord = new IrsTranUploadRecord();
	    irsTranUploadRecord.setYearmonth(yearMonth);
	    irsTranUploadRecord.setCompanyCode(companyCode);
	}
	Date now = new Date();
	irsTranUploadRecord.setModifier(userSession.getTcUser());
	irsTranUploadRecord.setModifytimestamp(now);
	if (irsTranUploadRecord.getId() == null) {
	    irsTranUploadRecordFacade.create(irsTranUploadRecord);
	} else {
	    irsTranUploadRecordFacade.edit(irsTranUploadRecord);
	}

	// 結束後畫面導回非SAP合併首頁
	FacesContext context = FacesContext.getCurrentInstance();
	context.getExternalContext().getFlash().setKeepMessages(true);
	context.getExternalContext().redirect("importSheetData.xhtml");
	context.responseComplete();

	return super.saveData(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean check4Save() throws Exception {
	int size = attachmentController.getAttachmentVOList().size();
	if (size == 0) {
	    String errMsg = "附件尚未傳!";
	    JsfUtil.addErrorMessage(errMsg);
	    return !valid;
	}
	return super.check4Save(); //To change body of generated methods, choose Tools | Templates.
    }

//</editor-fold>
    
    // help
    public String getPageTitle() {
        return "報表上傳";
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
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

    public ImportZtcoRelaGuiController getImportZtcoRelaGuiController() {
	return importZtcoRelaGuiController;
    }

    public void setImportZtcoRelaGuiController(ImportZtcoRelaGuiController importZtcoRelaGuiController) {
	this.importZtcoRelaGuiController = importZtcoRelaGuiController;
    }

    public ImportZtcoRelaDtlController getImportZtcoRelaDtlController() {
	return importZtcoRelaDtlController;
    }

    public void setImportZtcoRelaDtlController(ImportZtcoRelaDtlController importZtcoRelaDtlController) {
	this.importZtcoRelaDtlController = importZtcoRelaDtlController;
    }

    public String getYearMonth() {
	return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
	this.yearMonth = yearMonth;
    }

    public FcCompany getCompany() {
	return company;
    }

    public void setCompany(FcCompany company) {
	this.company = company;
    }
//</editor-fold>
}
