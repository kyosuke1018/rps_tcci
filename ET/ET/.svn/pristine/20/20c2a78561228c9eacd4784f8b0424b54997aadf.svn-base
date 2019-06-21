/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.rs;

import com.tcci.et.model.rs.BaseResponseVO;
import com.tcci.cm.annotation.ExcelFileFieldMeta;
import com.tcci.cm.annotation.ExcelFileMeta;
import com.tcci.cm.annotation.enums.DataTypeEnum;
import com.tcci.fc.util.ResourceBundleUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author peter.pan
 */
@ExcelFileMeta(headerRow = 1, rowNumColumnName = "rowNum")
public class ImportProductVO extends BaseResponseVO implements Serializable {
    private Long id;
    private Long storeId;

    @ExcelFileFieldMeta(importIndex = 1, headerName="title.imp.prdname", dataType = DataTypeEnum.STRING)
    private String cname;// 商品名稱
    //@ExcelFileFieldMeta(importIndex = 2, headerName="商品英文名稱", dataType = DataTypeEnum.STRING)
    private String ename;
    @ExcelFileFieldMeta(importIndex = 2, headerName="title.imp.prdcode", dataType = DataTypeEnum.STRING, isOptional=true) 
    private String code;// 商品編號
    
    private Long typeL1;
    @ExcelFileFieldMeta(importIndex = 3, headerName="title.imp.typeL1", dataType = DataTypeEnum.STRING)
    private String typeL1Name;// 分類(第一層)
    private Long typeL2;
    @ExcelFileFieldMeta(importIndex = 4, headerName="title.imp.typeL2", dataType = DataTypeEnum.STRING)
    private String typeL2Name;// 分類(第二層)
    private Long typeId;
    @ExcelFileFieldMeta(importIndex = 5, headerName="title.imp.typeL3", dataType = DataTypeEnum.STRING)
    private String typeName;// 分類(第三層)

    private Long vendorId;
    @ExcelFileFieldMeta(importIndex = 6, headerName="title.imp.vendor", dataType = DataTypeEnum.STRING, isOptional=true)
    private String vendorName;// 供應商
    private Long brandId;
    @ExcelFileFieldMeta(importIndex = 7, headerName="title.imp.brand", dataType = DataTypeEnum.STRING, isOptional=true)
    private String brandName;// 品牌
    
    private Long serialId;
    @ExcelFileFieldMeta(importIndex = 8, headerName="title.imp.price", dataType = DataTypeEnum.BIG_DECIMAL)
    private BigDecimal price;// 售價
    //@ExcelFileFieldMeta(importIndex = 9, headerName="商品訂價", dataType = DataTypeEnum.BIG_DECIMAL)
    private BigDecimal compareAtPrice;
    @ExcelFileFieldMeta(importIndex = 9, headerName="title.imp.unit", dataType = DataTypeEnum.STRING)
    private String priceUnitName;// 計價單位
    private Long priceUnit;
    //@ExcelFileFieldMeta(importIndex = 11, headerName="計價數量", dataType = DataTypeEnum.BIG_DECIMAL)
    private BigDecimal priceAmt;
    @ExcelFileFieldMeta(importIndex = 10, headerName="title.imp.stock", dataType = DataTypeEnum.BIG_DECIMAL)
    private BigDecimal stock;// 庫存
    
    private Long itemUnit;
    private BigDecimal itemAmt;
    //@ExcelFileFieldMeta(importIndex = 13, headerName="SKU", dataType = DataTypeEnum.STRING)
    private String sku;
    //@ExcelFileFieldMeta(importIndex = 14, headerName="BARCODE", dataType = DataTypeEnum.STRING)
    private String barcode;
    private String status;
    
    @ExcelFileFieldMeta(importIndex = 11, headerName="title.imp.minamt", dataType = DataTypeEnum.BIG_DECIMAL, isOptional=true)
    private BigDecimal minAmt;// 下單單筆最少數量
    @ExcelFileFieldMeta(importIndex = 12, headerName="title.imp.maxamt", dataType = DataTypeEnum.BIG_DECIMAL, isOptional=true)
    private BigDecimal maxAmt;// 下單單筆最大數量
    @ExcelFileFieldMeta(importIndex = 13, headerName="title.imp.intamt", dataType = DataTypeEnum.STRING, isOptional=true)
    private String intAmtYN;// 下單數量須為整數(Y/N)

    // for import
    private List<String> errList; // 資料檢查錯誤
    private List<String> warnList; // 資料檢查提示
    private boolean importResult;// 最後匯入結果
    private int rowNum;
    private boolean hasData;
    private boolean hasError;
    private String errMsgs;// 錯誤訊息彙總字串
    
    public Boolean getIntAmt() {
        return "Y".equalsIgnoreCase(getIntAmtYN());
    }
    
    // 產生錯誤訊息彙總字串
    public void genErrMsgs(Locale locale){
        StringBuilder sb = new StringBuilder();
        int num = 1;
        if( errList!=null ){
            for(String msg: errList){
                sb.append((num>1)?" | ":"").append("(").append(num).append(")").append(msg);
                num++;
            }
        }else{
            if( !hasData ){
                sb.append(ResourceBundleUtils.getMessage(locale, "nodata"));
            }
        }
        
        this.errMsgs = sb.toString();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getPriceUnitName() {
        return priceUnitName;
    }

    public void setPriceUnitName(String priceUnitName) {
        this.priceUnitName = priceUnitName;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public List<String> getErrList() {
        return errList;
    }

    public void setErrList(List<String> errList) {
        this.errList = errList;
    }

    public List<String> getWarnList() {
        return warnList;
    }

    public void setWarnList(List<String> warnList) {
        this.warnList = warnList;
    }

    public boolean isImportResult() {
        return importResult;
    }

    public void setImportResult(boolean importResult) {
        this.importResult = importResult;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public boolean isHasData() {
        return hasData;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public String getErrMsgs() {
        return errMsgs;
    }

    public void setErrMsgs(String errMsgs) {
        this.errMsgs = errMsgs;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Long getSerialId() {
        return serialId;
    }

    public void setSerialId(Long serialId) {
        this.serialId = serialId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCompareAtPrice() {
        return compareAtPrice;
    }

    public void setCompareAtPrice(BigDecimal compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    public Long getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Long priceUnit) {
        this.priceUnit = priceUnit;
    }

    public BigDecimal getPriceAmt() {
        return priceAmt;
    }

    public void setPriceAmt(BigDecimal priceAmt) {
        this.priceAmt = priceAmt;
    }

    public Long getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(Long itemUnit) {
        this.itemUnit = itemUnit;
    }

    public BigDecimal getItemAmt() {
        return itemAmt;
    }

    public void setItemAmt(BigDecimal itemAmt) {
        this.itemAmt = itemAmt;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getTypeL2() {
        return typeL2;
    }

    public void setTypeL2(Long typeL2) {
        this.typeL2 = typeL2;
    }

    public String getTypeL2Name() {
        return typeL2Name;
    }

    public void setTypeL2Name(String typeL2Name) {
        this.typeL2Name = typeL2Name;
    }

    public Long getTypeL1() {
        return typeL1;
    }

    public void setTypeL1(Long typeL1) {
        this.typeL1 = typeL1;
    }

    public String getTypeL1Name() {
        return typeL1Name;
    }

    public void setTypeL1Name(String typeL1Name) {
        this.typeL1Name = typeL1Name;
    }

    public BigDecimal getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(BigDecimal minAmt) {
        this.minAmt = minAmt;
    }

    public BigDecimal getMaxAmt() {
        return maxAmt;
    }

    public void setMaxAmt(BigDecimal maxAmt) {
        this.maxAmt = maxAmt;
    }

    public String getIntAmtYN() {
        return intAmtYN;
    }

    public void setIntAmtYN(String intAmtYN) {
        this.intAmtYN = intAmtYN;
    }
 
}
