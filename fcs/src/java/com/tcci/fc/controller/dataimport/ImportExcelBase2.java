/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.dataimport;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.util.ExcelUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 * @param <T>
 */
public abstract class ImportExcelBase2<T extends ExcelVOBase2> {
    
    //<editor-fold defaultstate="collapsed" desc="Injects">
//    @EJB
//    protected ActivityLogService activityLogService;     
    
    @ManagedProperty(value = "#{userSession}")
    protected UserSession userSession;
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
    //
    protected transient ResourceBundle rb = ResourceBundle.getBundle("msgApp",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());    
    //</editor-fold>
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected String fileName;      // 匯入的 excel 檔名
    protected int headerRow = 1;    // 變數名稱列 (0-based)
    protected int maxRows = 1000;   // 匯入最多筆數
    protected List<T> datalist;     // 匯入結果
    protected List<T> filtedlist;   // 過濾後的結果
    protected boolean invalidOnly = false; // 只顯示 invalid row
    protected String pageTitle;
    final protected boolean valid = true;
    protected int verifyRow;
    protected String sheetName;

    // subclass必需implement的methods
    abstract protected boolean postInit(T vo); // init VO 其它欄位資料, 包含status. return true:資料正確, false:資料有誤
    abstract protected boolean insert(T vo);   // 新增 VO, return true:成功, false:失敗(請將錯誤設息寫入message)
    abstract protected boolean update(T vo);   // 更新 VO, return true:成功, false:失敗(請將錯誤設息寫入message)
    
    // VO class
    protected final Class<T> clazz;

    // c'tor
    public ImportExcelBase2(Class<T> clazz) {
        this.clazz = clazz;
        filtedlist = new ArrayList<>();
    }
    
    // action
    // excel 檔案上傳
    public void handleFileUpload(FileUploadEvent event) {
        reset();
        UploadedFile tfile = event.getFile();
        fileName = truncateFilename(tfile.getFileName());
        try {
            datalist = ExcelUtil.importList(tfile.getInputstream(),
                    clazz, headerRow, maxRows);
            JsfUtil.addSuccessMessage(datalist.size() + " 筆資料載入.");
            // 驗證資料並執行postInit
            verify();
            invalidOnlyChange();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
        }        
    }
    
    public void invalidOnlyChange() {
        filtedlist.clear();
        if (datalist == null || datalist.isEmpty()) {
            return;
        }
        if (invalidOnly) {
            for (T vo : datalist) {
                if (!vo.isValid()) {
                    filtedlist.add(vo);
                }
            }
        } else {
            filtedlist.addAll(datalist);
        }
    }
    
    // 儲存匯入資料
    public boolean save() {
        if (!isAllSuccess()) {
            JsfUtil.addErrorMessage("檢核失敗! sheetName="+sheetName);
            return !valid;
        }
        beforeSave();
        for (T vo : datalist) {
            // 資料不正確或未異動不需儲存
            if (!vo.isValid() || vo.getStatus() == ExcelVOBase2.Status.ST_NOCHANGE) {
                continue;
            }
            if (vo.getStatus() == ExcelVOBase2.Status.ST_INSERT) {
                if (insert(vo)) {
                    vo.setStatus(ExcelVOBase2.Status.ST_NOCHANGE);
                    vo.setMessage("新增成功! sheetName="+sheetName);
                } else {
                    vo.setValid(false);
                }
            } else if (vo.getStatus() == ExcelVOBase2.Status.ST_UPDATE) {
                if (update(vo)) {
                    vo.setStatus(ExcelVOBase2.Status.ST_NOCHANGE);
                    vo.setMessage("更新成功! sheetName="+sheetName);
                } else {
                    vo.setValid(false);
                }
            }
        }
        afterSave();
        if (isAllSuccess()){
            JsfUtil.addSuccessMessage("儲存成功! sheetName="+sheetName);
            return valid;
        }else{
            JsfUtil.addErrorMessage("儲存失敗! sheetName="+sheetName);
            return !valid;
        }
    }
    public boolean isAllSuccess(){
        if(CollectionUtils.isNotEmpty(datalist)){
            for (T vo : datalist) {
                if(!vo.isValid()){
                    return false;
                }
            }
        }
        return true;
    }
    
    // helper
    protected void beforeSave() {
    }
    protected void afterSave() {
    }    
    protected void init4Verify() {
    }
    
    protected void verify() {
        init4Verify();
        verifyRow = -1;
        for (T vo : datalist) {
            verifyRow++;
            if (!vo.isValid()) {
                // 匯入時資料已經有誤(VO field validation), 不需再做 postInit
                continue; 
            }
            if (postInit(vo)) {
                if (vo.getStatus() == ExcelVOBase2.Status.ST_INSERT) {
                    vo.setMessage("新增");
                } else if (vo.getStatus() == ExcelVOBase2.Status.ST_UPDATE) {
                    vo.setMessage("修改");
                } else {
                    vo.setMessage("無異動");
                }
            } else {
                vo.setValid(false);
            }
        }
        if (!isAllSuccess()) {
            JsfUtil.addErrorMessage("檢核失敗! sheetName="+sheetName);
        }
    }

    protected String truncateFilename(String pathname) {
        int index = pathname.lastIndexOf('\\');
        if (index < 0) {
            index = pathname.lastIndexOf('/');
        }
        return pathname.substring(index + 1);
    }
    public void reset(){
        datalist = null;     // 匯入結果
        filtedlist = new ArrayList<>();   // 過濾後的結果
        invalidOnly = false; // 只顯示 invalid row
    }
//    protected BigDecimal defaultScale(BigDecimal value){
//       //20150805:設定Excel數值欄位的精度:小數5位4捨5入
//        if(null != value){
//            value = value.setScale(5, BigDecimal.ROUND_HALF_UP);
//        }
//        return value;
//    }
    
    //<editor-fold defaultstate="collapsed" desc="getter, setter">
    public String getFileName() {
	return fileName;
    }
    
    public void setFileName(String fileName) {
	this.fileName = fileName;
    }
    
    public int getHeaderRow() {
	return headerRow;
    }
    
    public void setHeaderRow(int headerRow) {
	this.headerRow = headerRow;
    }
    
    public int getMaxRows() {
	return maxRows;
    }
    
    public void setMaxRows(int maxRows) {
	this.maxRows = maxRows;
    }
    
    public List<T> getDatalist() {
	return datalist;
    }
    
    public void setDatalist(List<T> datalist) {
	this.datalist = datalist;
    }
    
    public List<T> getFiltedlist() {
	return filtedlist;
    }
    
    public void setFiltedlist(List<T> filtedlist) {
	this.filtedlist = filtedlist;
    }
    
    public boolean isInvalidOnly() {
	return invalidOnly;
    }
    
    public void setInvalidOnly(boolean invalidOnly) {
	this.invalidOnly = invalidOnly;
    }
    public String getPageTitle() {
	return pageTitle;
    }
    
    public void setPageTitle(String pageTitle) {
	this.pageTitle = pageTitle;
    }
    
    public String getSheetName() {
	return sheetName;
    }
    
    public void setSheetName(String sheetName) {
	this.sheetName = sheetName;
    }
//</editor-fold>
    
}
