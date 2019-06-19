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
    
    //private Long clevel;
    protected String clevel;
    
    protected String primaryType;
    protected Long primaryId;
    
    protected String dataType;
    
    protected Long accessionId;
    protected Long tagId;
    protected String rfid;
    protected Integer mapId;
    protected String tagName;
    
    protected Long contactsId;
    
    protected List<Long> includeList;
    protected List<Long> excludeList;
    protected List<String> types;
    
    protected boolean mainOnly;// 只考慮主Table
    protected boolean fullData;// 完整資訊(效能考量有時不JOIN完整資訊)
    protected boolean showCoverImg;// 代表圖
    
    protected Boolean disabled;
    protected int categoryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public Long getTagId() {
        return tagId;
    }

    public String getRfid() {
        return rfid;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public Long getIgnoreId() {
        return ignoreId;
    }

    public void setIgnoreId(Long ignoreId) {
        this.ignoreId = ignoreId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Integer getMapId() {
        return mapId;
    }

    public void setMapId(Integer mapId) {
        this.mapId = mapId;
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

    public Long getContactsId() {
        return contactsId;
    }

    public void setContactsId(Long contactsId) {
        this.contactsId = contactsId;
    }

    public Long getAccessionId() {
        return accessionId;
    }

    public void setAccessionId(Long accessionId) {
        this.accessionId = accessionId;
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
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
    
}
