/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model;

//import com.tcci.pp.facade.datawarehouse.BaseQueryCondVO;
//import com.tcci.pp.entity.log.QueryCondFieldMeta;
import java.util.List;

/**
 *
 * @author gilbert
 */
//public class SupplierCondVO extends BaseQueryCondVO {
public class SupplierCondVO {
    private String mandt;
    private String supplier = "";//供應商
    private String supplierName = "";//供應商名稱
    private String supplierDesc = "";//廠商類型說明
    private String ekorgListString = "";//多筆採購組織(,隔開)    
    private List<String> ekorgList;//多筆採購組織
    private boolean includeDeletedSupplier = true;//是否包含已刪除供應商 → 預設改為包含 2016/8/29
    private boolean queryAll=true;//true: 查詢全部 ; false: 只顯示凍結供應商 → 預設顯示全部供應商，可只顯示已凍結 2016/8/29
    private boolean queryAllFactories = false; //查詢所有廠別: 等於true時 忽略queryFactories條件
    private List<TcFactoryVO> queryFactories;//所選廠別 
    
//    @QueryCondFieldMeta(headerName = "語系")
    private String language;
    
    // 曾採購過料號相關查詢
    private List<String> matnrList;//多筆料號
    
    private String sortl = ""; // SAP 搜尋條件
    private String keyword = ""; //供應商、供應商名稱、廠商類型說明、SAP 搜尋條件

    private String holderKeyword; // 法人 zartiPerson、 股東 zstockholder
    
    private boolean showTradeInfo = false; //  是否顯示過去採購資訊
    
    private boolean simpleFieldsOnly = false;// return key fields only
    private int returnRowNum;
    private int maxResultsSize;//最大查詢結果筆數限制(若超過可throw exception)

    //<editor-fold defaultstate="collapsed" desc="getter and setter">  
    public boolean isShowTradeInfo() {
        return showTradeInfo;
    }

    public void setShowTradeInfo(boolean showTradeInfo) {
        this.showTradeInfo = showTradeInfo;
    }

    public boolean isSimpleFieldsOnly() {
        return simpleFieldsOnly;
    }

    public void setSimpleFieldsOnly(boolean simpleFieldsOnly) {
        this.simpleFieldsOnly = simpleFieldsOnly;
    }

    public int getReturnRowNum() {
        return returnRowNum;
    }

    public void setReturnRowNum(int returnRowNum) {
        this.returnRowNum = returnRowNum;
    }

    public String getHolderKeyword() {
        return holderKeyword;
    }

    public void setHolderKeyword(String holderKeyword) {
        this.holderKeyword = holderKeyword;
    }
    
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSortl() {
        return sortl;
    }

    public void setSortl(String sortl) {
        this.sortl = sortl;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public List<String> getMatnrList() {
        return matnrList;
    }

    public void setMatnrList(List<String> matnrList) {
        this.matnrList = matnrList;
    }

    public String getSupplierDesc() {
        return supplierDesc;
    }

    public void setSupplierDesc(String supplierDesc) {
        this.supplierDesc = supplierDesc;
    }
    
    public String getSupplier() {
        return supplier;
    }
    
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    public String getSupplierName() {
        return supplierName;
    }
    
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    
    public String getEkorgListString() {
        return ekorgListString;
    }

    public void setEkorgListString(String ekorgListString) {
        this.ekorgListString = ekorgListString;
    }

    public List<String> getEkorgList() {
        return ekorgList;
    }

    public void setEkorgList(List<String> ekorgList) {
        this.ekorgList = ekorgList;
    }    
    public boolean isIncludeDeletedSupplier() {
        return includeDeletedSupplier;
    }

    public void setIncludeDeletedSupplier(boolean includeDeletedSupplier) {
        this.includeDeletedSupplier = includeDeletedSupplier;
    }
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    public boolean isQueryAll() {
        return queryAll;
    }

    public void setQueryAll(boolean queryAll) {
        this.queryAll = queryAll;
    }

    public boolean isQueryAllFactories() {
        return queryAllFactories;
    }

    public void setQueryAllFactories(boolean queryAllFactories) {
        this.queryAllFactories = queryAllFactories;
    }

    public List<TcFactoryVO> getQueryFactories() {
        return queryFactories;
    }

    public void setQueryFactories(List<TcFactoryVO> queryFactories) {
        this.queryFactories = queryFactories;
    }

    public int getMaxResultsSize() {
        return maxResultsSize;
    }

    public void setMaxResultsSize(int maxResultsSize) {
        this.maxResultsSize = maxResultsSize;
    }
    
    //</editor-fold>

    @Override
    public String toString() {
        return "SupplierCondVO{" + "mandt=" + mandt + ", supplier=" + supplier + ", supplierName=" + supplierName + ", supplierDesc=" + supplierDesc + ", ekorgListString=" + ekorgListString + ", ekorgList=" + ekorgList + ", includeDeletedSupplier=" + includeDeletedSupplier + ", queryAll=" + queryAll + ", language=" + language + ", matnrList=" + matnrList + ", sortl=" + sortl + ", keyword=" + keyword + ", holderKeyword=" + holderKeyword + ", showTradeInfo=" + showTradeInfo + ", simpleFieldsOnly=" + simpleFieldsOnly + ", returnRowNum=" + returnRowNum + ", queryAllFactories=" + queryAllFactories + '}';
    }



}
