/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.controller;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.util.StringUtils;
import com.tcci.fcs.controller.ReportHomeVO;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcUploaderR;
import com.tcci.fcs.entity.FcUserCompGroupR;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.enums.FcConfigKeyEnum;
import com.tcci.fcs.enums.ReportSheetEnum;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.facade.FcConfigFacade;
import com.tcci.fcs.facade.schedule.FcsScheduleFacade;
import com.tcci.fcs.model.global.GlobalConstant;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import com.tcci.fcs.util.ReportConfig;
import com.tcci.rpt.entity.RptDValue;
import com.tcci.rpt.entity.RptDataUploadRecord;
import com.tcci.rpt.entity.RptSheetUpload;
import com.tcci.rpt.entity.ZtfiAfcsCsbu;
import com.tcci.rpt.facade.RptDValueFacade;
import com.tcci.rpt.facade.RptDataUploadRecordFacade;
import com.tcci.rpt.facade.RptSheetUploadFacade;
import com.tcci.rpt.facade.RptTbValueFacade;
import com.tcci.rpt.facade.ZtfiAfcsCsbaFacade;
import com.tcci.rpt.facade.ZtfiAfcsCsbuFacade;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name="consolidationRptHome")
@ViewScoped
public class ConsolidationRptHomeController {
    private final static Logger logger = LoggerFactory.getLogger(ConsolidationRptHomeController.class);
    
    //<editor-fold defaultstate="collapsed" desc="Injects">
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    
    @EJB
    private FcCompanyFacade companyFacade;
    @EJB
    private FcConfigFacade configFacade;
    @EJB
    private RptSheetUploadFacade rptSheetUploadFacade;
    @EJB
    private ContentFacade contentFacade;
    @EJB
    private RptDValueFacade valueFacade;
    @EJB
    private RptTbValueFacade tbValueFacade;
    @EJB
    private ZtfiAfcsCsbaFacade ztfiAfcsCsbaFacade;
    @EJB
    private ZtfiAfcsCsbuFacade ztfiAfcsCsbuFacade;
    @EJB
    private RptDataUploadRecordFacade rptDataUploadRecordFacade;
    @EJB
    private FcsScheduleFacade fcsScheduleFacade;
    //</editor-fold>
    
//    private static final String TEMP_FILENAME = "RPT_nonSAP_template_v102.xlsx";//<!--20160316因原提出此報表需求之user離職 暫不使用D0202 D0204-->
    //20160513 刪除TB 8200本期淨利(淨損), version 1.1.0==>1.1.1
    private static final String TEMP_FILENAME = "RPT_nonSAP_template_v112.xlsx";
    private final int DISPMODE_NOT_UPLOADED_SAP = 4; // 顯示未上傳(SAP)
    private final int DISPMODE_NOT_UPLOADED_WEB = 5; // 顯示未上傳(WEB)
    private String yearmonth;
    private List<RptSheetUpload> uploadList; // 該年月上傳的報表
    private List<ReportHomeVO> voList = new ArrayList<>();
    private StreamedContent downloadFile;
    private int dispMode = GlobalConstant.DISPMODE_ALL;
    private boolean stopUpload = false;
    private List<ReportHomeVO> allHomeVO = new ArrayList<>();
    private List<SelectItem> groups;//企業團
    private CompanyGroupEnum group;
    private List<FcUserCompGroupR> cgList;
    private boolean isAdmin;
    private boolean noPermission = false;
    private StreamedContent exportFile; // 匯出檔案
    private boolean isPowerUsers;
    private Map<FcCompany, RptSheetUpload> mapCompReport;
    private Map<String, ReportCompareVO> mapCompVO; // coid1:coid2 -> ReportCompareVO
    private List<ReportCompareVO> compareVOList;
    private List<RptDataUploadRecord> uploadRecopdList;
    
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
        mapCompReport = new HashMap<>();
        mapCompVO = new HashMap<>();
        isAdmin = userSession.isUserInRole("ADMINISTRATORS");
        isPowerUsers = userSession.isUserInRole("ADMINISTRATORS,FINANCIAL_HQ");
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
        yearmonth = configFacade.findValue(FcConfigKeyEnum.RPT_OPEN_YM, group);
        if (null == yearmonth) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            yearmonth = sdf.format(new Date());
        }
//        findLastReportTemplate();
        this.findUploadReports();
        this.findUploadRecord();
        this.buildHomeVOList();
//        this.loadCompareRptValue();//20160316因原提出此報表需求之user離職 暫不使用D0202 D0204
        //20151020 企業團獨立開關帳
        stopUpload = configFacade.findValueBoolean(FcConfigKeyEnum.RPT_STOPUPLOAD, false, group);
    }
    
    //<editor-fold defaultstate="collapsed" desc="private method">
    private List<SelectItem> buildGroupOptions() {
        List<SelectItem> options = new ArrayList();
//        CompanyGroupEnum item = CompanyGroupEnum.TCC;//目前合併報表只出台泥
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
    
    private void findUploadReports() {
        uploadList = rptSheetUploadFacade.findByYearmonth(yearmonth, group);
    }
    
    private void findUploadRecord() {
        uploadRecopdList = rptDataUploadRecordFacade.findByYearmonth(yearmonth);
    }
    
    private void buildHomeVOList() {
        List<FcCompany> companyList = new ArrayList<>();
        allHomeVO.clear();
        if (userSession.isUserInRole("ADMINISTRATORS,FINANCIAL_HQ")) {
            companyList = companyFacade.findAllActiveByGroup(group, false, true);
//            companyList = companyFacade.findAllActiveNonsap(group);
        } else {
//            companyList = companyFacade.findByUploaderR(userSession.getTcUser(), group);//上傳人關聯
            List<FcCompany> result = companyFacade.findByUploaderR(userSession.getTcUser(), group);//上傳人關聯
            for (FcCompany company : result) {
                if (company.isConsolidationRpt()) {//合併營收 web上傳公司
		    companyList.add(company);
		}
	    }
        }
        for (FcCompany company : companyList) {
            RptSheetUpload rptUpload = null;
            for (RptSheetUpload rpt : uploadList) {
                if (rpt.getCompany().equals(company)) {
                    rptUpload = rpt;
                    break;
                }
            }
            RptDataUploadRecord rptRecord = null;
            for(RptDataUploadRecord record : uploadRecopdList){
                if (record.getCompanyCode().equals(company.getCode())) {
                    rptRecord = record;
                    break;
                }
            }
            
            ReportHomeVO vo = new ReportHomeVO(company, rptUpload);
            vo.setRptUploadRecord(rptRecord);
            allHomeVO.add(vo);
        }
        this.dispModeChange();
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
    
    //20160316因原提出此報表需求之user離職 暫不使用D0202 D0204
    private void loadCompareRptValue(){
        List<RptDValue> reportValues = valueFacade.findReportValue(yearmonth, group);
        TcUser user = userSession.getTcUser();
        for (RptDValue rv : reportValues) {
            boolean isD0202 = ReportSheetEnum.D0202.getCode().equals(rv.getSheet());
            boolean isD0204 = ReportSheetEnum.D0204.getCode().equals(rv.getSheet());
            if (!isD0202 && !isD0204) {
                continue;
            }
            FcCompany coid1 = (isD0202)  ? rv.getUpload().getCompany() : rv.getCoid2();//資產方
            FcCompany coid2 = (!isD0204) ? rv.getUpload().getCompany() : rv.getCoid2();//負債方
            if (!isPowerUsers 
                    && !this.companyUploaderPermission(coid1, user) 
                    && !this.companyUploaderPermission(coid2, user)) {
                continue;
            }
            if (!mapCompReport.containsKey(rv.getUpload().getCompany())) {
                mapCompReport.put(rv.getUpload().getCompany(), rv.getUpload());
            }
            String key = coid1.getCode() + ":" + coid2.getCode();
            ReportCompareVO vo = mapCompVO.get(key);
            if (null == vo) {
                vo = new ReportCompareVO(coid1, coid2);
                mapCompVO.put(key, vo);
            }
            vo.addValue(rv);
        }
        compareVOList = new ArrayList<>(mapCompVO.values());
        Collections.sort(compareVOList, new Comparator<ReportCompareVO>() {
            @Override
            public int compare(ReportCompareVO v1, ReportCompareVO v2) {
                int c = v1.getCoid1().getCode().compareTo(v2.getCoid1().getCode());
                if (0 == c) {
                    c = v1.getCoid2().getCode().compareTo(v2.getCoid2().getCode());
                }
                return c;
            }
        });
    }
    
    //是否為公司上傳人之一
    private boolean companyUploaderPermission(FcCompany comp, TcUser user){
        List<FcUploaderR> uploadererLiser = comp.getUploaderList();
        if(uploadererLiser != null){
            for(FcUploaderR fcUploaderR:uploadererLiser){
//                if(fcUploaderR.getTcUser().equals(user)){
                //以ID比對才安全
                if(fcUploaderR.getTcUser().getId().equals(user.getId())){
                    return true;
                }
            }
        }
        return false;
    }
    //</editor-fold>
    
    public void changeGroup() {
        logger.debug("changeGroup:" + this.group);
        yearmonth = configFacade.findValue(FcConfigKeyEnum.RPT_OPEN_YM, group);
        
        //TEMP_FILENAME change TODO CSRC 導入?!
        
        //依開帳月份查詢
        this.findUploadReports();
        this.findUploadRecord();
        this.buildHomeVOList();
        //20151020 企業團獨立開關帳
        stopUpload = configFacade.findValueBoolean(FcConfigKeyEnum.RPT_STOPUPLOAD, false, group);
    }
    
    public void editRptYM() throws Exception {
        configFacade.saveValue(FcConfigKeyEnum.RPT_OPEN_YM, yearmonth, group);
        logger.debug("editRptYM finished");
        //init
        this.findUploadReports();
        this.findUploadRecord();
        this.buildHomeVOList();
    }
    
    public void dispModeChange() {
        logger.debug("dispModeChange dispMode:"+dispMode);
        voList.clear();
        if (GlobalConstant.DISPMODE_UPLOADED==dispMode) {//已上傳
            for (ReportHomeVO vo : allHomeVO) {
                if (vo.getRptUploadRecord() != null) {
                    voList.add(vo);
                }
            }
        } else if (GlobalConstant.DISPMODE_NOT_UPLOADED==dispMode) {//未上傳
            for (ReportHomeVO vo : allHomeVO) {
                if (vo.getRptUploadRecord() == null) {
                    voList.add(vo);
                }
            }
        } else if (this.DISPMODE_NOT_UPLOADED_SAP==dispMode) {//顯示未上傳(SAP)
            for (ReportHomeVO vo : allHomeVO) {
                if (vo.getRptUploadRecord() == null && !vo.getCompany().isConsolidationRptUpload()) {
                    voList.add(vo);
                }
            }
        } else if (this.DISPMODE_NOT_UPLOADED_WEB==dispMode) {//顯示未上傳(WEB)
            for (ReportHomeVO vo : allHomeVO) {
                if (vo.getRptUploadRecord() == null && vo.getCompany().isConsolidationRptUpload()) {
                    voList.add(vo);
                }
            }
        } else {
            voList.addAll(allHomeVO);
        }
        JsfUtil.saveCookie(GlobalConstant.COOKIE_DISPMODE, String.valueOf(dispMode), GlobalConstant.COOKIE_DISPMODE_MAXAGE);
    }
    
    public void download(ReportHomeVO vo) {
        download(vo.getRptSheetUpload(), vo);
    }
    
    public boolean isUploadable() {
        return !stopUpload && yearmonth != null;
    }
    
    public void stopUploadChange() {
        logger.debug("stopUploadChange:" + this.group);
        stopUpload = !stopUpload;
        //企業團獨立開關帳
        configFacade.saveValueBoolean(FcConfigKeyEnum.RPT_STOPUPLOAD, stopUpload, group);
    }
    
    //更新SAP上傳時間
    public void updateSapUpload(){
        long startTime = System.currentTimeMillis();
        try {
            if(StringUtils.isBlank(yearmonth)){
                JsfUtil.addWarningMessage("不可執行!");
                return;
            }
            //20160511合併報表供財務使用者triger, 只更新指定月份
            fcsScheduleFacade.updateUploadRecord("B", yearmonth);//只更新合併報表
            
            long excTime = System.currentTimeMillis() - startTime;
            logger.debug("updateSapUpload execute sucess time:["+excTime+"]");
            if(excTime>5000){
                JsfUtil.addSuccessMessage("執行完畢! execute time[ms]:["+excTime+"]");
            }else{
                JsfUtil.addSuccessMessage("執行完畢!");
            }
            
            //init
            this.findUploadReports();
            this.findUploadRecord();
            this.buildHomeVOList();
            
            excTime = System.currentTimeMillis() - startTime;
            logger.debug("total execute sucess time:["+excTime+"]");
        } catch (Exception ex) {
//            ex.printStackTrace();
            long excTime = System.currentTimeMillis() - startTime;
            logger.debug("updateSapUpload execute fail time:["+excTime+"]");
            JsfUtil.addErrorMessage("執行失敗! execute time[ms]:["+excTime+"]");
        }
    }
    
    public void export() {
        String path = ReportConfig.REPORT_COMPARE_PATH + ReportConfig.COMPARE_TEMPLATE_2.get(group);
        InputStream in = this.getClass().getResourceAsStream(path);
        try {
            Workbook workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);
            int idxRow = ReportConfig.EXP_STARTROW;
            for (ReportCompareVO vo : compareVOList) {
                Row row = sheet.getRow(idxRow);
                if (null == row) {
                    row = sheet.createRow(idxRow);
                }
                int col = 0;
                String[] expCodes;
                if(group.equals(CompanyGroupEnum.TCC)){
                    expCodes = ReportConfig.EXP_CODES_AL_TCC;
                }else{
//                    expCodes = ReportConfig.EXP_CODES_AL_CSRC;
                    expCodes = null;
                    return;
                }
                for (String str : expCodes) {
                    if ("COID1".equals(str)) {
                        writeCell(row, col, vo.getCoid1().getCode());
                    } else if ("COID1_NAME".equals(str)) {
                        writeCell(row, col, vo.getCoid1().getName());
                    } else if ("COID2".equals(str)) {
                        writeCell(row, col, vo.getCoid2().getCode());
                    } else if ("COID2_NAME".equals(str)) {
                        writeCell(row, col, vo.getCoid2().getName());
                    } else if ("SUM1".equals(str)) {
                        writeCell(row, col, vo.getSum1());
                    } else if ("SUM2".equals(str)) {
                        writeCell(row, col, vo.getSum2());
                    } else if ("DIFF".equals(str)) {
                        writeCell(row, col, vo.getDiff());
                    } else if ("NOTE".equals(str)) {
//                        writeCell(row, col, vo.getNote());//沒有編寫對帳的功能
                    } else {
                        BigDecimal amount = vo.getAmount(str);
                        if (null == amount) {
                            amount = BigDecimal.ZERO;
                        }
                        writeCell(row, col, amount);
                    }
                    col++;
                }
                idxRow++;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            String filename = "AL_" + yearmonth + ".xlsx";
            exportFile = new DefaultStreamedContent(new ByteArrayInputStream(out.toByteArray()), 
                    "application/octet-stream", filename);
        } catch (Exception ex) {
            logger.error("export exception!", ex);
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }
    
    private void writeCell(Row row, int col, String str) {
        Cell cell = row.getCell(col);
        if (null == cell) {
            cell = row.createCell(col);
        }
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(str);
    }

    private void writeCell(Row row, int col, BigDecimal amount) {
        Cell cell = row.getCell(col);
        if (null == cell) {
            cell = row.createCell(col);
        }
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(amount.doubleValue());
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
    
    //合併公司設定CSBU 異動後 可能會影響目標合併公司ZBUKTO
    //TB 重新寫入會科餘額檔CSBA
    //依開帳年月 指定公司 重新寫入CSBA 並紀錄
    public void saveTB(ReportHomeVO vo){
        ZtfiAfcsCsbu ztfiAfcsCsbu = ztfiAfcsCsbuFacade.findByCompany(vo.getCompany());
        if(ztfiAfcsCsbu != null){
            String zbukto = ztfiAfcsCsbu.getZbukto();
            logger.debug("zbukto:{}", zbukto);
            List<ReportBaseVO> tabAccounts = tbValueFacade.findMonthly(yearmonth, vo.getCompany());
            if (CollectionUtils.isNotEmpty(tabAccounts)) {
                try {
                    ztfiAfcsCsbaFacade.saveExcelValue(vo.getRptSheetUpload(), tabAccounts, zbukto);
                    
                    //另存一份 與SAP共用的上傳紀錄檔
//                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
//                    SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
                    Date now = new Date();
                    RptDataUploadRecord rptDataUploadRecord = rptDataUploadRecordFacade.findByYearmonthCompany(yearmonth, vo.getCompany());
                    rptDataUploadRecord.setModifier(userSession.getTcUser());
                    rptDataUploadRecord.setModifytimestamp(now);
//                    rptDataUploadRecord.setUploadDate(sdfDate.format(now));
//                    rptDataUploadRecord.setUploadTime(sdfTime.format(now));
                    rptDataUploadRecordFacade.save(rptDataUploadRecord);
                    
                    JsfUtil.addSuccessMessage("重新寫入會科餘額檔CSBA　成功!");
//                    FacesContext context = FacesContext.getCurrentInstance();
//                    context.responseComplete();
                } catch (Exception ex) {
                    JsfUtil.addErrorMessage(ex.getMessage());
                    logger.error("saveTB end:{}", vo.getCompany().toString());
                }
            }else{
                JsfUtil.addWarningMessage("無TB資料");
            }
        }else{
            JsfUtil.addWarningMessage("無CSBU資料");
        }
        logger.debug("saveTB end:{}", vo.getCompany().toString());
    }
    
    public String getCompanyNote(ReportHomeVO vo){
//        if(vo.getCompany().isUndo()){
//            return "無需處理";
//        }
        if(vo.getCompany().isConsolidationRptUpload()){
            return "WEB上傳";
        }else{
            return "SAP上傳";
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getYearmonth() {
        return yearmonth;
    }
    
    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }
    
    public List<RptSheetUpload> getUploadList() {
        return uploadList;
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
    
    public boolean isNoPermission() {
        return noPermission;
    }
    
    public int getDispMode() {
        return dispMode;
    }

    public void setDispMode(int dispMode) {
        this.dispMode = dispMode;
    }
    
    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public StreamedContent getExportFile() {
        return exportFile;
    }
    //</editor-fold>
}
