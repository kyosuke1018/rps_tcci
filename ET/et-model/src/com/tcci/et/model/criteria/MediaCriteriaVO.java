/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.criteria;

import com.tcci.cm.model.interfaces.IQueryCriteria;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author peter.pan
 */
public class MediaCriteriaVO extends BaseCriteriaVO implements IQueryCriteria, Serializable {
    private PublicationCriteriaVO docCriteriaVO;
    private boolean useDocCriteria;// 有使用文章專屬條件(需多JOIN 文章 TABLE)

    private boolean getFvInfo;// 效能考量-直接取得 attachmentVO 資訊 (1對1適用)
    private Boolean incMedia;// 有包含圖片、影片
    
    private List<Long> parents;
    private List<Long> exCludeParents;
    private List<Long> ids;
    private List<Long> exCludeIds;
    
    private boolean forPlantCol; // for 植物蒐藏

    public MediaCriteriaVO(){
        // for doc photos
        docCriteriaVO = new PublicationCriteriaVO();
    }

    public boolean isForPlantCol() {
        return forPlantCol;
    }

    public void setForPlantCol(boolean forPlantCol) {
        this.forPlantCol = forPlantCol;
    }

    public boolean isGetFvInfo() {
        return getFvInfo;
    }

    public void setGetFvInfo(boolean getFvInfo) {
        this.getFvInfo = getFvInfo;
    }

    public List<Long> getExCludeParents() {
        return exCludeParents;
    }

    public void setExCludeParents(List<Long> exCludeParents) {
        this.exCludeParents = exCludeParents;
    }

    public List<Long> getExCludeIds() {
        return exCludeIds;
    }

    public void setExCludeIds(List<Long> exCludeIds) {
        this.exCludeIds = exCludeIds;
    }

    public List<Long> getParents() {
        return parents;
    }

    public void setParents(List<Long> parents) {
        this.parents = parents;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public PublicationCriteriaVO getDocCriteriaVO() {
        return docCriteriaVO;
    }

    public void setDocCriteriaVO(PublicationCriteriaVO docCriteriaVO) {
        this.docCriteriaVO = docCriteriaVO;
    }

    public boolean isUseDocCriteria() {
        return useDocCriteria;
    }

    public void setUseDocCriteria(boolean useDocCriteria) {
        this.useDocCriteria = useDocCriteria;
    }

    public Boolean isIncMedia() {
        return incMedia;
    }

    public void setIncMedia(Boolean incMedia) {
        this.incMedia = incMedia;
    }
    
}
