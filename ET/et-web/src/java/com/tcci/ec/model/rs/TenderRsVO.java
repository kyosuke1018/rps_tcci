/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.rs;

import com.tcci.et.enums.LanguageEnum;
import com.tcci.et.enums.TenderStatusEnum;
import com.tcci.fc.vo.AttachmentVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Kyle.Cheng
 */
public class TenderRsVO extends BaseResponseVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    
    private String code;//案件編號
    private String title;//標題
    private String summary;//投標公告
    private String status;//狀態代碼
    private Date datadate;//發佈日期
    
    private Date ssaleDate;//標書發售日 起
    private Date esaleDate;//迄
    private Date stenderDate;//投標日 起
    private Date etenderDate;//迄
    private Date verifyDate;//評標日期
    private Date closeDate;//決標日期
    private Long areaId;//地區
    private String areaName;
    private Integer areaSort;
    private Long categoryId;//案件分類
    private String categoryName;
    private Integer categorySort;
    
    private BigDecimal amount;//標書金額
    private BigDecimal insuranceAmount;//保證金
    
    private String lang = LanguageEnum.SIMPLIFIED_CHINESE.getShortCode();// 預設繁體中文
    
    private Date createtime;
    private Date modifytime;
    private String lastUserName;
    private Date lastTime;
    
    private Long linkId;
    private String url;
    private Long htmlId;
    private String contents;
    private List<AttachmentVO> docs;
    private List<AttachmentVO> removedDocs;
    
    private boolean temp;
    
    private Long domain;
    private String filename;
    private String contenttype;
    
    public Date getLastUpdateTime(){
        return (modifytime!=null)?modifytime:createtime;
    }
    
    public TenderStatusEnum getStatusEnum(){
        return TenderStatusEnum.getFromCode(status);
    }
    
    public String getStatusName(){
        TenderStatusEnum enum1 = getStatusEnum();
        return (enum1!=null)?enum1.getName():"";
    }
    
    public TenderRsVO() {
    }

    public TenderRsVO(Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getHtmlId() {
        return htmlId;
    }

    public void setHtmlId(Long htmlId) {
        this.htmlId = htmlId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isTemp() {
        return temp;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }

    public String getLastUserName() {
        return lastUserName;
    }

    public void setLastUserName(String lastUserName) {
        this.lastUserName = lastUserName;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Long getDomain() {
        return domain;
    }

    public void setDomain(Long domain) {
        this.domain = domain;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public Date getSsaleDate() {
        return ssaleDate;
    }

    public void setSsaleDate(Date ssaleDate) {
        this.ssaleDate = ssaleDate;
    }

    public Date getEsaleDate() {
        return esaleDate;
    }

    public void setEsaleDate(Date esaleDate) {
        this.esaleDate = esaleDate;
    }

    public Date getStenderDate() {
        return stenderDate;
    }

    public void setStenderDate(Date stenderDate) {
        this.stenderDate = stenderDate;
    }

    public Date getEtenderDate() {
        return etenderDate;
    }

    public void setEtenderDate(Date etenderDate) {
        this.etenderDate = etenderDate;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getAreaSort() {
        return areaSort;
    }

    public void setAreaSort(Integer areaSort) {
        this.areaSort = areaSort;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategorySort() {
        return categorySort;
    }

    public void setCategorySort(Integer categorySort) {
        this.categorySort = categorySort;
    }

    public List<AttachmentVO> getDocs() {
        return docs;
    }

    public void setDocs(List<AttachmentVO> docs) {
        this.docs = docs;
    }

    public List<AttachmentVO> getRemovedDocs() {
        return removedDocs;
    }

    public void setRemovedDocs(List<AttachmentVO> removedDocs) {
        this.removedDocs = removedDocs;
    }

    public Date getDatadate() {
        return datadate;
    }

    public void setDatadate(Date datadate) {
        this.datadate = datadate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }
    
    
}
