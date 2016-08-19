/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.controller;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.util.StringUtils;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcReportTemplate;
import com.tcci.fcs.entity.FcReportUpload;
import com.tcci.fcs.entity.FcUserCompGroupR;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.enums.FcConfigKeyEnum;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.facade.FcConfigFacade;
import com.tcci.fcs.facade.FcReportTemplateFacade;
import com.tcci.fcs.facade.FcReportUploadFacade;
import com.tcci.fcs.model.global.GlobalConstant;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name="reportHome")
@ViewScoped
public class ReportHomeController {
    private final static Logger logger = LoggerFactory.getLogger(ReportHomeController.class);
    
    private String yearmonth;
    private FcReportTemplate reportTemplate;
    private List<FcReportUpload> uploadList; // 該年月上傳的報表
    private List<ReportHomeVO> voList = new ArrayList<>();
    private StreamedContent downloadFile;
    private int dispMode = GlobalConstant.DISPMODE_ALL;
    private boolean stopUpload = false;
    
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    
    @EJB
    private FcCompanyFacade companyFacade;
    @EJB
    private FcReportTemplateFacade reportTemplateFacade;
    @EJB
    private FcReportUploadFacade reportUploadFacade;
    @EJB
    private FcConfigFacade configFacade;
    @EJB
    private ContentFacade contentFacade;
    
    private List<ReportHomeVO> allHomeVO = new ArrayList<ReportHomeVO>();
    private List<SelectItem> groups;//企業團
    private CompanyGroupEnum group;
    private List<TcApplicationdata> templateFileList;//母版list
    private List<FcUserCompGroupR> cgList;
    private boolean isAdmin;
    private boolean noPermission = false;
    
    @PostConstruct
    private void init() {
        try {
            String strDispMode = JsfUtil.getCookieValue(GlobalConstant.COOKIE_DISPMODE);
            if(StringUtils.isNotBlank(strDispMode)){
                dispMode = Integer.valueOf(strDispMode).intValue();
            }
        } catch (Exception ex) {
        }
        //20151119 增加多選公司群組
        cgList =  userSession.getTcUser().getCompGroupList();
        isAdmin = userSession.isUserInRole("ADMINISTRATORS");
        //公司群組權限
        groups = this.buildGroupOptions();
        if (groups == null || groups.isEmpty()) {
            noPermission = true;
            return;
        }
        if(this.isAdmin){
            group = CompanyGroupEnum.TCC;//預設台泥
        }else{
            if (this.cgList != null && !this.cgList.isEmpty()) {
                group = this.cgList.get(0).getGroup();
            }
        }
        //依企業團查詢
        findLastReportTemplate();
        findUploadReports();
        buildHomeVOList();
        //20151020 企業團獨立開關帳
        stopUpload = configFacade.findValueBoolean(FcConfigKeyEnum.STOPUPLOAD, false, group);
    }
    
    // action
    public void downloadTemplate(TcApplicationdata appdata) {
//        download(reportTemplate, null);
        download(appdata);
    }
    
    public void download(ReportHomeVO vo) {
        download(vo.getReportUpload(), vo);
    }
    
    public void downloadAll() throws IOException {
        File temp = File.createTempFile("fcsrpt_" + yearmonth + "_", ".zip");
        FileOutputStream fos = new FileOutputStream(temp);
        ZipOutputStream zos = new ZipOutputStream(fos);
        for (ReportHomeVO vo : allHomeVO) {
            TcApplicationdata appData = findAppData(vo.getReportUpload());
            if (null == appData) {
                continue;
            }
            TcFvitem fvItem = appData.getFvitem();
            if (null == fvItem) {
                continue;
            }
            // fileName: <code>_台泥集團合併報表財務資訊.xls
            String origFileName = fvItem.getFilename();
            String fileName = vo.getCompany().getCode() + 
                    "_台泥集團合併報表財務資訊" + 
                    origFileName.substring(origFileName.lastIndexOf('.'));
            ZipEntry ze= new ZipEntry(fileName);
            zos.putNextEntry(ze);
            InputStream in = contentFacade.findContentStream(appData);
            IOUtils.copy(in, zos);
            in.close();
        }
        zos.closeEntry();
        zos.close();
        InputStream in = new FileInputStream(temp);
        downloadFile = new DefaultStreamedContent(in, "application/zip", 
                URLEncoder.encode("合併報表_" + yearmonth + ".zip","UTF-8"));
    }
    
    public void dispModeChange() {
        logger.debug("dispModeChange dispMode:"+dispMode);
        voList.clear();
        if (GlobalConstant.DISPMODE_UPLOADED==dispMode) {
            for (ReportHomeVO vo : allHomeVO) {
                if (vo.getReportUpload() != null) {
                    voList.add(vo);
                }
            }
        } else if (GlobalConstant.DISPMODE_NOT_UPLOADED==dispMode) {
            for (ReportHomeVO vo : allHomeVO) {
                if (vo.getReportUpload() == null) {
                    voList.add(vo);
                }
            }
        } else {
            voList.addAll(allHomeVO);
        }
        JsfUtil.saveCookie(GlobalConstant.COOKIE_DISPMODE, String.valueOf(dispMode), GlobalConstant.COOKIE_DISPMODE_MAXAGE);
    }
    
    public void stopUploadChange() {
        stopUpload = !stopUpload;
        //20151020 企業團獨立開關帳
        configFacade.saveValueBoolean(FcConfigKeyEnum.STOPUPLOAD, stopUpload, group);
        if(!stopUpload){//開帳時取消鎖定
            configFacade.saveValueBoolean(FcConfigKeyEnum.EDITLOCK, Boolean.FALSE, group);
        }
    }
    
    // helper
    public boolean isUploadable() {
        return !stopUpload && reportTemplate != null;
    }
    
    public boolean isUploadExpired(ReportHomeVO vo) {
        if (reportTemplate != null && vo.getReportUpload() != null) {
            return reportTemplate.getModifytimestamp().after(vo.getReportUpload().getModifytimestamp());
        }
        return false;
    }
    
    private void findLastReportTemplate() {
        yearmonth = configFacade.findValue(FcConfigKeyEnum.YEARMONTH, group);
        if (null != yearmonth) {
            reportTemplate = reportTemplateFacade.findByYearmonth(yearmonth, group);
            templateFileList = contentFacade.getApplicationdata(reportTemplate);//母版list
            if (templateFileList != null && !templateFileList.isEmpty()) {
                 logger.debug(""+templateFileList);
            }
        } else {
            reportTemplate = reportTemplateFacade.findLast(group);
        }
        if (null == reportTemplate) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            yearmonth = sdf.format(new Date());
        } else {
            yearmonth = reportTemplate.getYearmonth();
        }
    }
    
    private void findUploadReports() {
        uploadList = reportUploadFacade.findByYearmonth(yearmonth, group);
    }
    
    private void buildHomeVOList() {
        List<FcCompany> companyList;
        allHomeVO.clear();
        if (userSession.isUserInRole("ADMINISTRATORS,FINANCIAL_HQ")) {
            companyList = companyFacade.findAllActiveByGroup(group, true, false);
        } else {
//            companyList = companyFacade.findByUploader(userSession.getTcUser());
            companyList = companyFacade.findByUploaderR(userSession.getTcUser(), group);//上傳人關聯
        }
        for (FcCompany company : companyList) {
            FcReportUpload reportUpload = null;
            for (FcReportUpload rpt : uploadList) {
                if (rpt.getCompany().equals(company)) {
                    reportUpload = rpt;
                    break;
                }
            }
            ReportHomeVO vo = new ReportHomeVO(company, reportUpload);
            allHomeVO.add(vo);
        }
        this.dispModeChange();
    }
    
    private TcApplicationdata findAppData(ContentHolder container) {
        if (null == container) {
            return null;
        }
        List<TcApplicationdata> applicationDataList = contentFacade.getApplicationdata(container);
        if (applicationDataList != null && !applicationDataList.isEmpty()) {
            return applicationDataList.get(0);
        }
        return null;
    }
    
    private void download(TcApplicationdata appdata) {
        logger.debug("download");
        TcFvitem fvItem = appdata.getFvitem();
        try {
            String fileName = fvItem.getName();
            fileName = URLEncoder.encode(fileName, "UTF-8");
            InputStream in = contentFacade.findContentStream(appdata);
            downloadFile = new DefaultStreamedContent(in, fvItem.getContenttype(), fileName);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex.getLocalizedMessage());
            throw new AbortProcessingException(ex.getMessage());
        }
    }
    
    private void download(ContentHolder container, ReportHomeVO vo) {
        logger.debug("download");
        List<TcApplicationdata> applicationDataList = contentFacade.getApplicationdata(container);
        if (applicationDataList == null || applicationDataList.isEmpty()) {
            throw new AbortProcessingException("no file uploaded!");
        }
        TcFvitem fvItem = applicationDataList.get(0).getFvitem();
        try {
            String fileName = fvItem.getName();
            if (vo != null) {
                fileName = vo.getCompany().getCode() + 
                        "_台泥集團合併報表財務資訊" + 
                        fileName.substring(fileName.lastIndexOf('.'));
            }
            fileName = URLEncoder.encode(fileName, "UTF-8");
            InputStream in = contentFacade.findContentStream(applicationDataList.get(0));
            downloadFile = new DefaultStreamedContent(in, fvItem.getContenttype(), fileName);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex.getLocalizedMessage());
            throw new AbortProcessingException(ex.getMessage());
        }
    }
    
    private List<SelectItem> buildGroupOptions() {
        List<SelectItem> options = new ArrayList();
        for (CompanyGroupEnum item : CompanyGroupEnum.values()) {
            //admin有所有公司群組權限
            if(this.isAdmin){
                options.add(new SelectItem(item, item.getName()));
            }else{
                if (this.cgList != null && !this.cgList.isEmpty()) {
                    for(FcUserCompGroupR cg : this.cgList){
                        if (item.equals(cg.getGroup())) {
                            options.add(new SelectItem(item, item.getName()));
                        }
                    }
                }
            }
        }
        return options;
    }
    
    public void changeGroup() {
        logger.debug("changeGroup:" + this.group);
        findLastReportTemplate();
        findUploadReports();
        buildHomeVOList();
        //20151020 企業團獨立開關帳
        yearmonth = configFacade.findValue(FcConfigKeyEnum.YEARMONTH, group);
        stopUpload = configFacade.findValueBoolean(FcConfigKeyEnum.STOPUPLOAD, false, group);
    }
    
    // getter, setter
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

    public List<FcReportUpload> getUploadList() {
        return uploadList;
    }

    public void setUploadList(List<FcReportUpload> uploadList) {
        this.uploadList = uploadList;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public List<ReportHomeVO> getVoList() {
        return voList;
    }

    public void setVoList(List<ReportHomeVO> voList) {
        this.voList = voList;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    public int getDispMode() {
        return dispMode;
    }

    public void setDispMode(int dispMode) {
        this.dispMode = dispMode;
    }

    public boolean isStopUpload() {
        return stopUpload;
    }

    public void setStopUpload(boolean stopUpload) {
        this.stopUpload = stopUpload;
    }

    public List<SelectItem> getGroups() {
        return groups;
    }

    public CompanyGroupEnum getGroup() {
        return group;
    }

    public void setGroup(CompanyGroupEnum group) {
        this.group = group;
    }

    public boolean showDownloadAll() {
        //FCS-5 選擇尚未上傳合併營收報表的公司，不應該顯示下載icon
        if(this.dispMode == 3){
            return false;
        }
        //FCS-16 若所有的公司都未上傳報表，不應該還出現全部下載icon
        if (CollectionUtils.isNotEmpty(this.voList)) {
            for(ReportHomeVO homeVO : this.voList){
                if(homeVO.getReportUpload() != null){
                    return true;
                }
            }
        }
        
        return false;
    }

    public List<TcApplicationdata> getTemplateFileList() {
        return templateFileList;
    }
    
    public boolean isNoPermission() {
        return noPermission;
    }

}
