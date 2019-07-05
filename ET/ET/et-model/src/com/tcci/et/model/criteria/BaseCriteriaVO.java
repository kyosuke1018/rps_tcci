/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.criteria;

import com.tcci.cm.model.global.AbstractCriteriaVO;
import com.tcci.cm.model.interfaces.IQueryCriteria;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author peter.pan
 */
public class BaseCriteriaVO extends AbstractCriteriaVO implements IQueryCriteria, Serializable {
    private boolean fromWeb;// 來自 WEB 查詢，關鍵字查詢限定WEB顯示欄位
    
    protected Long id;
    protected Long memberId;
    protected Long tenderId;
    protected Long rfqId;
    protected Long quoteId;
    protected Long awardId;
    protected Long ebelp;
    protected Long venderId;
    protected String currency;
    
    protected Long factoryId;
    protected Long typeId;
    protected Long categoryId;
    protected String loginAccount;
    protected String code;
    protected String type;
    protected String status;
    protected Long parent;
    protected String keyword;
    protected String nameKeyword;// 只針對代表名稱欄位的關鍵字
    protected String name;
    protected String lang;
    protected Long ignoreId;
    
    protected Long user;
    protected Date startDate;
    protected Date endDate;
    
    protected String dataType;
    
    //private Long clevel;
    protected String clevel;
    
    protected String primaryType;
    protected Long primaryId;
    
    protected List<Long> includeList;
    protected List<Long> excludeList;
    protected List<String> types;
    
    protected boolean mainOnly;// 只考慮主Table
    protected boolean fullData;// 完整資訊(效能考量有時不JOIN完整資訊)
    protected boolean showCoverImg;// 代表圖
    
    protected Boolean active;
    protected Boolean disabled;
    protected int category;
    protected Date startAt;
    protected Date endAt;
    protected String startYM;
    protected String endYM;
    
    protected List<Long> idList;
    protected List<String> factoryList;
    protected List<String> statusList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
//    public int getCategoryId() {
//        return categoryId;
//    }
//
//    public void setCategoryId(int categoryId) {
//        this.categoryId = categoryId;
//    }

    public Long getRfqId() {
        return rfqId;
    }

    public void setRfqId(Long rfqId) {
        this.rfqId = rfqId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public Long getEbelp() {
        return ebelp;
    }

    public void setEbelp(Long ebelp) {
        this.ebelp = ebelp;
    }

    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public Long getAwardId() {
        return awardId;
    }

    public void setAwardId(Long awardId) {
        this.awardId = awardId;
    }

    public boolean isShowCoverImg() {
        return showCoverImg;
    }

    public void setShowCoverImg(boolean showCoverImg) {
        this.showCoverImg = showCoverImg;
    }

    public boolean isFromWeb() {
        return fromWeb;
    }

    public void setFromWeb(boolean fromWeb) {
        this.fromWeb = fromWeb;
    }

    public Long getIgnoreId() {
        return ignoreId;
    }

    public void setIgnoreId(Long ignoreId) {
        this.ignoreId = ignoreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMainOnly() {
        return mainOnly;
    }

    public void setMainOnly(boolean mainOnly) {
        this.mainOnly = mainOnly;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public Long getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(Long primaryId) {
        this.primaryId = primaryId;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<Long> getIncludeList() {
        return includeList;
    }

    public void setIncludeList(List<Long> includeList) {
        this.includeList = includeList;
    }

    public List<Long> getExcludeList() {
        return excludeList;
    }

    public void setExcludeList(List<Long> excludeList) {
        this.excludeList = excludeList;
    }

    public String getClevel() {
        return clevel;
    }

    public void setClevel(String clevel) {
        this.clevel = clevel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isFullData() {
        return fullData;
    }

    public void setFullData(boolean fullData) {
        this.fullData = fullData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getNameKeyword() {
        return nameKeyword;
    }

    public void setNameKeyword(String nameKeyword) {
        this.nameKeyword = nameKeyword;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<String> getFactoryList() {
        return factoryList;
    }

    public void setFactoryList(List<String> factoryList) {
        this.factoryList = factoryList;
    }

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public String getStartYM() {
        return startYM;
    }

    public void setStartYM(String startYM) {
        this.startYM = startYM;
    }

    public String getEndYM() {
        return endYM;
    }

    public void setEndYM(String endYM) {
        this.endYM = endYM;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
    
}
