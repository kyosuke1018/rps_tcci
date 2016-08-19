/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.controller;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcReportNote;
import com.tcci.fcs.entity.FcReportUpload;
import com.tcci.fcs.entity.FcReportValue;
import com.tcci.fcs.entity.FcUploaderR;
import com.tcci.fcs.entity.FcUserCompGroupR;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.enums.ReportSheetEnum;
import com.tcci.fcs.facade.FcConfigFacade;
import com.tcci.fcs.enums.FcConfigKeyEnum;
import com.tcci.fcs.facade.FcReportUploadFacade;
import com.tcci.fcs.util.ExcelAccountItem;
import com.tcci.fcs.util.ReportConfig;
import com.tcci.fcs.facade.service.ReportConfigService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;
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
 * @author Jimmy.Lee
 */
@ManagedBean(name="reportCompare")
@ViewScoped
public class ReportCompareController {
    private static final Logger logger = LoggerFactory.getLogger(ReportCompareController.class);
    
    private String yearmonth;
    private List<ReportCompareVO> list;
    private List<ReportCompareVO> editList;
    private ReportCompareVO editVO;
    private String note;
    private String code1Values; // code:value code1:value
    private String code2Values;
    private StreamedContent exportFile; // 匯出檔案
    private boolean stopUpload;
    private boolean editLock;//20151007 編輯鎖定

    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    
    @EJB
    private FcReportUploadFacade reportUplodaFacade;
    @EJB
    private FcConfigFacade configFacade;
    @EJB
    private ReportConfigService reportConfigService;
    
    private Map<FcCompany, FcReportUpload> mapCompReport;
    private Map<String, ReportCompareVO> mapCompVO; // coid1:coid2 -> ReportCompareVO
    private boolean isPowerUsers;
    private List<SelectItem> groups;//企業團
    private CompanyGroupEnum group;
    private List<FcUserCompGroupR> cgList;
    private boolean isAdmin;
    private boolean noPermission = false;
    
    @PostConstruct
    private void init() {
        isPowerUsers = userSession.isUserInRole("ADMINISTRATORS,FINANCIAL_HQ");
        mapCompReport = new HashMap<>();
        mapCompVO = new HashMap<>();
        //20151119 增加多選公司群組
        cgList =  userSession.getTcUser().getCompGroupList();
        isAdmin = userSession.isUserInRole("ADMINISTRATORS");
        
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
        yearmonth = configFacade.findValue(FcConfigKeyEnum.YEARMONTH, group);
        stopUpload = configFacade.findValueBoolean(FcConfigKeyEnum.STOPUPLOAD, false, group);
        editLock = configFacade.findValueBoolean(FcConfigKeyEnum.EDITLOCK, false, group);
        loadValues();
        loadNotes();
        editList = new ArrayList<>();
    }
    
    // action
    public void edit(ReportCompareVO vo) {
        editList.clear();
        editList.add(vo);
        editVO = vo;
        note = vo.getNote();
    }
    
    public void edit_OK() {
        try {
            TcUser modifier = userSession.getTcUser();
            Date modifytimestamp = new Date();
            FcCompany coid1 = editVO.getCoid1();
            FcCompany coid2 = editVO.getCoid2();
            FcReportUpload report1 = mapCompReport.get(coid1);
            if (report1 != null && isEditable(coid1)) {
                // 更新report1對coid2的收入
                Map<String, BigDecimal> values = getCodeValues(code1Values);
                reportUplodaFacade.updateValues(report1, coid2, ReportSheetEnum.D0206.getCode(), values, modifier, modifytimestamp);
            }
            FcReportUpload report2 = mapCompReport.get(coid2);
            if (report2 != null && isEditable(coid2)) {
                // 更新report2對coid1的支出
                Map<String, BigDecimal> values = getCodeValues(code2Values);
                reportUplodaFacade.updateValues(report2, coid1, ReportSheetEnum.D0208.getCode(), values, modifier, modifytimestamp);
            }
            if (!StringUtils.equals(note, editVO.getNote())) {
                reportUplodaFacade.updateNote(yearmonth, coid1, coid2, note, modifier, modifytimestamp);
            }
            JsfUtil.addSuccessMessage("資料更新成功!");
            // reload
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            context.getExternalContext().redirect("reportCompare.xhtml");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("資料更新失敗，" + ex.getMessage());
        }
    }

    public void export() {
//        InputStream in = this.getClass().getResourceAsStream("/GA_template.xls");
        String path = ReportConfig.REPORT_COMPARE_PATH + ReportConfig.COMPARE_TEMPLATE.get(group);
        InputStream in = this.getClass().getResourceAsStream(path);
        try {
            Workbook workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);
            int idxRow = ReportConfig.EXP_STARTROW;
            for (ReportCompareVO vo : list) {
                Row row = sheet.getRow(idxRow);
                if (null == row) {
                    row = sheet.createRow(idxRow);
                }
                int col = 0;
                String[] expCodes;
                if(group.equals(CompanyGroupEnum.TCC)){
                    expCodes = ReportConfig.EXP_CODES_IE_TCC;
                }else{
                    expCodes = ReportConfig.EXP_CODES_IE_CSRC;
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
                        writeCell(row, col, vo.getNote());
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
            String filename = "IE_" + yearmonth + ".xls";
            exportFile = new DefaultStreamedContent(new ByteArrayInputStream(out.toByteArray()), 
                    "application/octet-stream", filename);
        } catch (Exception ex) {
            logger.error("export exception!", ex);
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }
    
    // helper
    public List<ExcelAccountItem> getAcitem1() {
//        return ReportConfig.D0206_ACITEMS;
        return reportConfigService.findAccountItems(group, ReportSheetEnum.D0206);
    }
    
    public List<ExcelAccountItem> getAcitem2() {
//        return ReportConfig.D0208_ACITEMS;
        return reportConfigService.findAccountItems(group, ReportSheetEnum.D0208);
    }
    
    public boolean isEditable(FcCompany comp) {
        if (isPowerUsers) {
            return true;
        }
//        TcUser uploader = (null==comp) ? null : comp.getUploader();
//        return (null==uploader) ? false : uploader.equals(userSession.getTcUser());
        return this.companyUploaderPermission(comp, userSession.getTcUser());
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

    private void loadValues() {
        List<FcReportValue> reportValues = reportUplodaFacade.findReportValue(yearmonth, group);
        TcUser user = userSession.getTcUser();
        for (FcReportValue rv : reportValues) {
            boolean isD0206 = ReportSheetEnum.D0206.getCode().equals(rv.getSheet());
            boolean isD0208 = ReportSheetEnum.D0208.getCode().equals(rv.getSheet());
            if (!isD0206 && !isD0208) {
                continue;
            }
            FcCompany coid1 = (isD0206)  ? rv.getReport().getCompany() : rv.getCoid2();//收入公司
            FcCompany coid2 = (!isD0206) ? rv.getReport().getCompany() : rv.getCoid2();//支出公司
//            if (!isPowerUsers && !user.equals(coid1.getUploader()) && !user.equals(coid2.getUploader())) {
            if (!isPowerUsers 
                    && !this.companyUploaderPermission(coid1, user) 
                    && !this.companyUploaderPermission(coid2, user)) {
                continue;
            }
            if (!mapCompReport.containsKey(rv.getReport().getCompany())) {
                mapCompReport.put(rv.getReport().getCompany(), rv.getReport());
            }
            String key = coid1.getCode() + ":" + coid2.getCode();
            ReportCompareVO vo = mapCompVO.get(key);
            if (null == vo) {
                vo = new ReportCompareVO(coid1, coid2);
                mapCompVO.put(key, vo);
            }
            vo.addValue(rv);
        }
        list = new ArrayList<>(mapCompVO.values());
        Collections.sort(list, new Comparator<ReportCompareVO>() {
            @Override
            public int compare(ReportCompareVO v1, ReportCompareVO v2) {
                int c = v1.getCoid1().getCode().compareTo(v2.getCoid1().getCode());
                if (0 == c) {
                    c = v1.getCoid2().getCode().compareTo(v2.getCoid2().getCode());
                }
                /*20150923改為先以sort欄位排序, 再以code欄位排序
                20151007 sort欄位只用在損益表 沖銷彙總表 營收彙總表
                int c = 0;
                if(v1.getCoid1().getSort() == v2.getCoid1().getSort()){
                    c = v1.getCoid1().getCode().compareTo(v2.getCoid1().getCode());
                    if (0 == c) {
                        if(v1.getCoid2().getSort() == v2.getCoid2().getSort()){
                            c = v1.getCoid2().getCode().compareTo(v2.getCoid2().getCode());
                        }else if(v1.getCoid2().getSort() > v2.getCoid2().getSort()){
                            return 1;
                        }else if(v1.getCoid2().getSort() < v2.getCoid2().getSort()){
                            return -1;
                        }
                    }
                }else if(v1.getCoid1().getSort() > v2.getCoid1().getSort()){
                    return 1;
                }else if(v1.getCoid1().getSort() < v2.getCoid1().getSort()){
                    return -1;
                }*/
                return c;
            }
        });
    }
    
    private void loadNotes() {
        List<FcReportNote> rptNotes = reportUplodaFacade.findNotes(yearmonth);
        for (FcReportNote rptNote : rptNotes) {
            String key = rptNote.getCoid1().getCode() + ":" + rptNote.getCoid2().getCode();
            ReportCompareVO vo = mapCompVO.get(key);
            if (vo != null) {
                vo.setNote(rptNote.getNote());
                vo.setModifytimestamp(rptNote.getModifytimestamp());
            }
        }
    }
    
    private Map<String, BigDecimal> getCodeValues(String strCodeValues) {
        Map<String, BigDecimal> result = new HashMap<>();
        String[] codeValues = StringUtils.split(strCodeValues);
        if (codeValues != null) {
            for (String codeValue : codeValues) {
                String[] ary = StringUtils.split(codeValue, ':');
                if (ary.length==2) {
                    String code = ary[0];
                    String value = ary[1];
                    try {
                        BigDecimal amount = new BigDecimal(value);
                        result.put(code, amount);
                    } catch (Exception ex) {
                    }
                }
            }
        }
        return result;
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
    
    public void editLockChange() {
        editLock = !editLock;
        configFacade.saveValueBoolean(FcConfigKeyEnum.EDITLOCK, editLock, group);
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
        mapCompReport = new HashMap<>();
        mapCompVO = new HashMap<>();
        //依企業團查詢
        yearmonth = configFacade.findValue(FcConfigKeyEnum.YEARMONTH, group);
        stopUpload = configFacade.findValueBoolean(FcConfigKeyEnum.STOPUPLOAD, false, group);
        editLock = configFacade.findValueBoolean(FcConfigKeyEnum.EDITLOCK, false, group);
        loadValues();
        loadNotes();
        editList = new ArrayList<>();
    }

    // getter, setter
    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }

    public List<ReportCompareVO> getList() {
        return list;
    }

    public void setList(List<ReportCompareVO> list) {
        this.list = list;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public List<ReportCompareVO> getEditList() {
        return editList;
    }

    public void setEditList(List<ReportCompareVO> editList) {
        this.editList = editList;
    }

    public ReportCompareVO getEditVO() {
        return editVO;
    }

    public void setEditVO(ReportCompareVO editVO) {
        this.editVO = editVO;
    }

    public String getCode1Values() {
        return code1Values;
    }

    public void setCode1Values(String code1Values) {
        this.code1Values = code1Values;
    }

    public String getCode2Values() {
        return code2Values;
    }

    public void setCode2Values(String code2Values) {
        this.code2Values = code2Values;
    }

    public StreamedContent getExportFile() {
        return exportFile;
    }

    public void setExportFile(StreamedContent exportFile) {
        this.exportFile = exportFile;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isStopUpload() {
        return stopUpload;
    }

    public void setStopUpload(boolean stopUpload) {
        this.stopUpload = stopUpload;
    }

    public boolean isEditLock() {
        return editLock;
    }

    public void setEditLock(boolean editLock) {
        this.editLock = editLock;
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

}
