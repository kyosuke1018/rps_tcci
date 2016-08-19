/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.dataimport;

import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.util.ExcelUtil;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Jimmy.Lee
 */
public abstract class ImportExcelBase<T extends ExcelVOBase> {
    
    private String fileName;      // 匯入的 excel 檔名
    private int headerRow = 1;    // 變數名稱列 (0-based)
    private int maxRows = 0;   // 不限筆數
    private List<T> datalist;     // 匯入結果
    private List<T> filtedlist;   // 過濾後的結果
    private boolean invalidOnly = false; // 只顯示 invalid row

    // subclass必需implement的methods
    abstract protected boolean postInit(T vo); // init VO 其它欄位資料, 包含status. return true:資料正確, false:資料有誤
    abstract protected boolean insert(T vo);   // 新增 VO, return true:成功, false:失敗(請將錯誤設息寫入message)
    abstract protected boolean update(T vo);   // 更新 VO, return true:成功, false:失敗(請將錯誤設息寫入message)
    
    // VO class
    private final Class<T> clazz;

    // c'tor
    public ImportExcelBase(Class<T> clazz) {
        this.clazz = clazz;
        filtedlist = new ArrayList<T>();
    }
    
    // action
    // excel 檔案上傳
    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile tfile = event.getFile();
        fileName = truncateFilename(tfile.getFileName());
        try {
            datalist = ExcelUtil.importList(tfile.getInputstream(),
                    clazz, headerRow, maxRows);
            JsfUtil.addSuccessMessage(datalist.size() + " rows loaded.");
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
    public void save() {
        if (null==datalist || datalist.isEmpty()) {
            JsfUtil.addErrorMessage("no data!");
            return;
        }
        boolean success = true;
        beforeSave();
        for (T vo : datalist) {
            // 資料不正確或未異動不需儲存
            if (!vo.isValid() || vo.getStatus() == ExcelVOBase.Status.ST_NOCHANGE) {
                continue;
            }
            if (vo.getStatus() == ExcelVOBase.Status.ST_INSERT) {
                if (insert(vo)) {
                    vo.setStatus(ExcelVOBase.Status.ST_NOCHANGE);
                    vo.setMessage("insert success");
                } else {
                    success = false;
                    vo.setValid(false);
                }
            } else if (vo.getStatus() == ExcelVOBase.Status.ST_UPDATE) {
                if (update(vo)) {
                    vo.setStatus(ExcelVOBase.Status.ST_NOCHANGE);
                    vo.setMessage("update success");
                } else {
                    success = false;
                    vo.setValid(false);
                }
            }
        }
        if (success)
            JsfUtil.addSuccessMessage("save successed!");
        else
            JsfUtil.addErrorMessage("save exception happen!");
    }
    
    // helper
    protected void beforeSave() {
    }
    
    private void verify() {
        if (null==datalist || datalist.isEmpty()) {
            JsfUtil.addErrorMessage("no data!");
            return;
        }
        boolean invalid = false;
        for (T vo : datalist) {
            if (!vo.isValid()) {
                // 匯入時資料已經有誤(VO field validation), 不需再做 postInit
                invalid = true;
                continue; 
            }
            if (postInit(vo)) {
                if (vo.getStatus() == ExcelVOBase.Status.ST_INSERT) {
                    vo.setMessage("insert");
                } else if (vo.getStatus() == ExcelVOBase.Status.ST_UPDATE) {
                    vo.setMessage("update");
                } else {
                    vo.setMessage("no change");
                }
            } else {
                vo.setValid(false);
                invalid = true;
            }
        }
        if (invalid) {
            JsfUtil.addErrorMessage("some data invalid!");
        }
    }

    private String truncateFilename(String pathname) {
        int index = pathname.lastIndexOf('\\');
        if (index < 0) {
            index = pathname.lastIndexOf('/');
        }
        return pathname.substring(index + 1);
    }

    // getter, setter
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
    
}
