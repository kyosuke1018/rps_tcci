/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.criteria;

import com.tcci.cm.model.interfaces.IQueryCriteria;
import java.io.Serializable;

/**
 *
 * @author peter.pan
 */
public class PublicationCriteriaVO extends BaseCriteriaVO implements IQueryCriteria, Serializable {
    private Boolean news;
    private Long htmlId;
    private Long linkId;
    private Boolean docOnly;
    private Boolean ignoreCoverImage; // 不需抓封面資訊圖檔 for 全文檢索
    private Long hundredsId; 

    public Boolean getNews() {
        return news;
    }

    public void setNews(Boolean news) {
        this.news = news;
    }    

    public Long getHundredsId() {
        return hundredsId;
    }

    public void setHundredsId(Long hundredsId) {
        this.hundredsId = hundredsId;
    }

    public Boolean getIgnoreCoverImage() {
        return ignoreCoverImage;
    }

    public void setIgnoreCoverImage(Boolean ignoreCoverImage) {
        this.ignoreCoverImage = ignoreCoverImage;
    }

    public Boolean getDocOnly() {
        return docOnly;
    }

    public void setDocOnly(Boolean docOnly) {
        this.docOnly = docOnly;
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

}
