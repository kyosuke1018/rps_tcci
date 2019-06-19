/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.criteria;

import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class TenderCriteriaVO extends BaseCriteriaVO {
    protected Long areaId;
    protected Date startCloseDate;
    protected Date endCloseDate;
    protected Date startVerifyDate;
    protected Date endVerifyDate;
    protected Date startPublishDate;
    protected Date endPublishDate;
    protected Boolean closed;
    protected Boolean notDraft;//草稿
    protected Boolean notice;//發佈通知
    
    private Long htmlId;
    private Long linkId;
    protected String lang;

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Date getStartCloseDate() {
        return startCloseDate;
    }

    public void setStartCloseDate(Date startCloseDate) {
        this.startCloseDate = startCloseDate;
    }

    public Date getEndCloseDate() {
        return endCloseDate;
    }

    public void setEndCloseDate(Date endCloseDate) {
        this.endCloseDate = endCloseDate;
    }

    public Date getStartVerifyDate() {
        return startVerifyDate;
    }

    public void setStartVerifyDate(Date startVerifyDate) {
        this.startVerifyDate = startVerifyDate;
    }

    public Date getEndVerifyDate() {
        return endVerifyDate;
    }

    public void setEndVerifyDate(Date endVerifyDate) {
        this.endVerifyDate = endVerifyDate;
    }

    public Long getHtmlId() {
        return htmlId;
    }

    public void setHtmlId(Long htmlId) {
        this.htmlId = htmlId;
    }

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Boolean getNotDraft() {
        return notDraft;
    }

    public void setNotDraft(Boolean notDraft) {
        this.notDraft = notDraft;
    }

    public Boolean getNotice() {
        return notice;
    }

    public void setNotice(Boolean notice) {
        this.notice = notice;
    }

    public Date getStartPublishDate() {
        return startPublishDate;
    }

    public void setStartPublishDate(Date startPublishDate) {
        this.startPublishDate = startPublishDate;
    }

    public Date getEndPublishDate() {
        return endPublishDate;
    }

    public void setEndPublishDate(Date endPublishDate) {
        this.endPublishDate = endPublishDate;
    }

}
