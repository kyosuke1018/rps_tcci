/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model;

import com.tcci.cm.enums.SapClientEnum;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.et.enums.LanguageEnum;
import com.tcci.et.enums.TenderMethodEnum;
import com.tcci.et.model.rs.AttachmentRsVO;
import com.tcci.et.model.rs.BaseResponseVO;
import com.tcci.et.enums.TenderStatusEnum;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.vo.AttachmentVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Kyle.Cheng
 */
public class TenderVO extends BaseResponseVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String type;
    private String code;
    private String title;
    private String summary;
//    private String content;
    private String status;
    private Date datadate;
    
    private Date ssaleDate;
    private Date esaleDate;
    private Date stenderDate;
    private Date etenderDate;
    private String tenderDateStr;
    private Date verifyDate;
    private Date closeDate;
    private Long factoryId;
    private String factoryName;
    private Long areaId;
    private String areaName;
    private Integer areaSort;
    private Long categoryId;
    private String categoryName;
    private Integer categorySort;
    private List<Long> categoryIds;
    private List<TenderCategoryVO> categoryList;
    private String categorys;
    
    private BigDecimal amount;//標書金額
    private BigDecimal insuranceAmount;//保證金
    private BigDecimal performanceBond;// 履約金
    private BigDecimal warranty;// 質保金(%)
    
    private String lang = LanguageEnum.SIMPLIFIED_CHINESE.getShortCode();// 預設繁體中文
    
    private TcUser creator;
    private Date createtime;
    private TcUser modifier;
    private Date modifytime;
    private String lastUserName;
    private Date lastTime;
    
    private Long linkId;
    private String url;
    private Long htmlId;//KB_RICHCONTENT
    private String contents;
    private List<AttachmentVO> docs;
    private List<AttachmentVO> removedDocs;
    private List<AttachmentRsVO> docRsList;
    
    private boolean temp;
    
    private Long domain;
    private String filename;
    private String contenttype;
    
    private String sapClientCode;
    private Long companyId;
    private String companyName;
    private String sapClient;
    private String language;
    private String currency;
    private SapClientEnum sapClientEnum;
    private TenderMethodEnum tenderMethod;
    private Long rfqId;
    
    public TcUser getLastUpdateUser(){
        return (modifier!=null)?modifier:creator;
    }
    public Date getLastUpdateTime(){
        return (modifytime!=null)?modifytime:createtime;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<TenderCategoryVO> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<TenderCategoryVO> categoryList) {
        this.categoryList = categoryList;
    }

    public String getCategorys() {
        return categorys;
    }

    public void setCategorys(String categorys) {
        this.categorys = categorys;
    }

    public BigDecimal getPerformanceBond() {
        return performanceBond;
    }

    public void setPerformanceBond(BigDecimal performanceBond) {
        this.performanceBond = performanceBond;
    }

    public BigDecimal getWarranty() {
        return warranty;
    }

    public void setWarranty(BigDecimal warranty) {
        this.warranty = warranty;
    }
    
    public TenderStatusEnum getStatusEnum(){
        return TenderStatusEnum.getFromCode(status);
    }
    
    public String getStatusName(){
        TenderStatusEnum enum1 = getStatusEnum();
        return (enum1!=null)?enum1.getName():"";
    }

    public TenderMethodEnum getTenderMethod() {
        return tenderMethod;
    }

    public void setTenderMethod(TenderMethodEnum tenderMethod) {
        this.tenderMethod = tenderMethod;
        if( tenderMethod!=null ){
            this.type = tenderMethod.getCode();
        }else{
            this.type = null;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        if( type!=null ){
            tenderMethod = TenderMethodEnum.getFromCode(type);
        }else{
            tenderMethod = null;
        }
    }
    
    public TenderVO() {
    }

    public TenderVO(Long id) {
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

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
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

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getTenderDateStr() {
        if(stenderDate!=null&etenderDate!=null){
            SimpleDateFormat sdf = new SimpleDateFormat(GlobalConstant.FORMAT_DATE);
            this.tenderDateStr = sdf.format(stenderDate)+"~"+sdf.format(etenderDate);
        }
        return tenderDateStr;
    }

    public void setTenderDateStr(String tenderDateStr) {
        this.tenderDateStr = tenderDateStr;
    }

    public List<AttachmentRsVO> getDocRsList() {
        return docRsList;
    }

    public void setDocRsList(List<AttachmentRsVO> docRsList) {
        this.docRsList = docRsList;
    }

    public String getSapClientCode() {
        return sapClientCode;
    }

    public void setSapClientCode(String sapClientCode) {
        this.sapClientCode = sapClientCode;
        if( sapClientCode!=null ){
            sapClientEnum = SapClientEnum.getFromSapClientCode(sapClientCode);
        }else{
            sapClientEnum = null;
        }
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSapClient() {
        return sapClient;
    }

    public void setSapClient(String sapClient) {
        this.sapClient = sapClient;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public SapClientEnum getSapClientEnum() {
        return sapClientEnum;
    }

    public void setSapClientEnum(SapClientEnum sapClientEnum) {
        this.sapClientEnum = sapClientEnum;
        if( sapClientEnum!=null ){
            this.sapClientCode = sapClientEnum.getSapClientCode();
        }else{
            this.sapClientCode =null;
        }
    }

    public Long getRfqId() {
        return rfqId;
    }

    public void setRfqId(Long rfqId) {
        this.rfqId = rfqId;
    }
    
}
