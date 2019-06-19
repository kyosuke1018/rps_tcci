/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.tcci.cm.annotation.ExcelFileFieldMeta;
import com.tcci.cm.annotation.ExcelFileMeta;
import com.tcci.cm.annotation.enums.DataTypeEnum;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.model.interfaces.IOperator;
import com.tcci.ec.model.rs.BaseResponseVO;
import com.tcci.fc.util.ResourceBundleUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Peter.pan
 */
@ExcelFileMeta(headerRow = 1, rowNumColumnName = "rowNum")
public class ImportTccDealerVO extends BaseResponseVO implements ImportTcc, IOperator, Serializable {

    @ExcelFileFieldMeta(importIndex = 1, headerName="title.imp.comid", dataType = DataTypeEnum.STRING)
    private String idCode;// 統編
    @ExcelFileFieldMeta(importIndex = 2, headerName="title.imp.comname", dataType = DataTypeEnum.STRING)
    private String cname;// 公司名稱
    @ExcelFileFieldMeta(importIndex = 3, headerName="title.imp.nickname", dataType = DataTypeEnum.STRING)
    private String nickname;// 公司簡稱

    @ExcelFileFieldMeta(importIndex = 4, headerName="title.imp.loginAccount", dataType = DataTypeEnum.STRING)
    private String loginAccount;// 會員帳號(手機)
    @ExcelFileFieldMeta(importIndex = 5, headerName="title.imp.memname", dataType = DataTypeEnum.STRING)
    private String name;// 會員名稱
    
    @ExcelFileFieldMeta(importIndex = 6, headerName="title.imp.mainuser", dataType = DataTypeEnum.STRING)
    private String storeOwnerYN;//是否為最高權限管理員(是/否)
    private Boolean storeOwner;//是否為最高權限管理員
    @ExcelFileFieldMeta(importIndex = 7, headerName="title.imp.finuser", dataType = DataTypeEnum.STRING)
    private String finUserYN;//是否為訂單結案人(是/否)
    private Boolean finUser;//是否為訂單結案人

    // 電話(2) E-mail(2) 負責人 統編　
    // 產業別 所在區域
    @ExcelFileFieldMeta(importIndex = 8, headerName="title.imp.tel", dataType = DataTypeEnum.STRING, isOptional=true)
    private String phone;
    private String tel2;// 電話(2)
    
    @ExcelFileFieldMeta(importIndex = 9, headerName="title.imp.email", dataType = DataTypeEnum.STRING)
    private String email;// E-mail
    private String email2;// E-mail(2)
    @ExcelFileFieldMeta(importIndex = 10, headerName="title.imp.owner", dataType = DataTypeEnum.STRING, isOptional=true)
    private String owner1;// 負責人
	

    @ExcelFileFieldMeta(importIndex = 11, headerName="title.imp.capital", dataType = DataTypeEnum.BIG_DECIMAL, isOptional=true)
    private BigDecimal capital;//資本額(元)
    @ExcelFileFieldMeta(importIndex = 12, headerName="title.imp.yearIncome", dataType = DataTypeEnum.BIG_DECIMAL, isOptional=true)
    private BigDecimal yearIncome;//年收入(元)
    
    @ExcelFileFieldMeta(importIndex = 13, headerName="title.imp.industry", dataType = DataTypeEnum.STRING, isOptional=true)
    private String categoryName;//產業別
    private Long category;//產業別
    @ExcelFileFieldMeta(importIndex = 14, headerName="title.imp.area", dataType = DataTypeEnum.STRING, isOptional=true)
    private String stateName;//所在區域
    private Long state;//所在區域

    @ExcelFileFieldMeta(importIndex = 15, headerName="title.imp.startAt", dataType = DataTypeEnum.DATE, isOptional=true)
    private Date startAt;//創立時間
    
    // for import
    private List<String> errList; // 資料檢查錯誤
    private List<String> warnList; // 資料檢查提示
    private boolean importResult;// 最後匯入結果
    private int rowNum;
    private boolean hasData;
    private boolean hasError;
    private String errMsgs;// 錯誤訊息彙總字串
    private boolean add;// 新增
    
    // 產生錯誤訊息彙總字串
    @Override
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

    public String getStoreOwnerYN() {
        return storeOwnerYN;
    }

    public void setStoreOwnerYN(String storeOwnerYN) {
        this.storeOwnerYN = storeOwnerYN;
        if( storeOwnerYN!=null ){
            if( Arrays.asList(GlobalConstant.YES).contains(storeOwnerYN.toUpperCase()) ){
                this.storeOwner = true;
                return;
            }
        }
        this.storeOwner = false;
    }

    public Boolean getStoreOwner() {
        return storeOwner;
    }

    public void setStoreOwner(Boolean storeOwner) {
        this.storeOwner = storeOwner;
    }

    public String getFinUserYN() {
        return finUserYN;
    }

    public void setFinUserYN(String finUserYN) {
        this.finUserYN = finUserYN;
        if( finUserYN!=null ){
            if( Arrays.asList(GlobalConstant.YES).contains(finUserYN.toUpperCase()) ){
                this.finUser = true;
                return;
            }
        }
        this.finUser = false;
    }

    public Boolean getFinUser() {
        return finUser;
    }

    public void setFinUser(Boolean finUser) {
        this.finUser = finUser;
    }

    @Override
    public String getCname() {
        return cname;
    }

    @Override
    public void setCname(String cname) {
        this.cname = cname;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public BigDecimal getYearIncome() {
        return yearIncome;
    }

    public void setYearIncome(BigDecimal yearIncome) {
        this.yearIncome = yearIncome;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    @Override
    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getOwner1() {
        return owner1;
    }

    public void setOwner1(String owner1) {
        this.owner1 = owner1;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    @Override
    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public Long getCategory() {
        return category;
    }

    @Override
    public void setCategory(Long category) {
        this.category = category;
    }

    @Override
    public String getStateName() {
        return stateName;
    }

    @Override
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public Long getState() {
        return state;
    }

    @Override
    public void setState(Long state) {
        this.state = state;
    }

    @Override
    public List<String> getErrList() {
        return errList;
    }

    @Override
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

    @Override
    public int getRowNum() {
        return rowNum;
    }

    @Override
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public boolean isHasData() {
        return hasData;
    }

    @Override
    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    @Override
    public boolean isHasError() {
        return hasError;
    }

    @Override
    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public String getErrMsgs() {
        return errMsgs;
    }

    public void setErrMsgs(String errMsgs) {
        this.errMsgs = errMsgs;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getLabel(){
        return this.name + "(" + this.loginAccount + ")";
    }
    
}
