/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs.model;

import com.tcci.fc.util.DateUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@XmlRootElement
public class PublicationRsVO implements Serializable {
    private ResponseVO res;

    private Long id;
    private String type;
    private Long parent;
    private String dataType;
    private Date dataDate;
    private String dataDateStr;
    
    private String code;
    private String title;
    private String summary;
    private String status;
    private Boolean news;

    //private TcUser creator;
    //private Date createtimestamp;
    //private TcUser modifier;
    //private Date modifytimestamp;

    private Long linkId;
    private String url;
    private String openMethod;

    private Long htmlId;
    private String contents;
    
    private List<String> urls;
        
    private String folderTitle;

    private String lastUserName;
    private Date lastTime;

    private boolean temp;

    // 封面圖示
    private Long coverDomain;
    private String coverFilename;
    private String coverContenttype;
    private String coverUrl;

    public PublicationRsVO(){};

    public ResponseVO getRes() {
        return res;
    }

    public void setRes(ResponseVO res) {
        this.res = res;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataDate() {
        return dataDate;
    }

    public void setDataDate(Date dataDate) {
        this.dataDate = dataDate;
        if( dataDate!=null ){
            dataDateStr = DateUtils.formatDate(dataDate);
        }
    }

    public String getDataDateStr() {
        return dataDateStr;
    }

    public void setDataDateStr(String dataDateStr) {
        this.dataDateStr = dataDateStr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
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

    public Boolean getNews() {
        return news;
    }

    public void setNews(Boolean news) {
        this.news = news;
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

    public String getOpenMethod() {
        return openMethod;
    }

    public void setOpenMethod(String openMethod) {
        this.openMethod = openMethod;
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

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getFolderTitle() {
        return folderTitle;
    }

    public void setFolderTitle(String folderTitle) {
        this.folderTitle = folderTitle;
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
        //if( lastTime!=null ){
        //    this.setDataDate(DateUtils.formatDate(lastTime));
        //}
    }

    public boolean isTemp() {
        return temp;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }

    public Long getCoverDomain() {
        return coverDomain;
    }

    public void setCoverDomain(Long coverDomain) {
        this.coverDomain = coverDomain;
    }

    public String getCoverFilename() {
        return coverFilename;
    }

    public void setCoverFilename(String coverFilename) {
        this.coverFilename = coverFilename;
    }

    public String getCoverContenttype() {
        return coverContenttype;
    }

    public void setCoverContenttype(String coverContenttype) {
        this.coverContenttype = coverContenttype;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
