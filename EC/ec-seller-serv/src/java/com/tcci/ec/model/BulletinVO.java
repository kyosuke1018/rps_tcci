/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.tcci.ec.model.rs.BaseResponseVO;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class BulletinVO extends BaseResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;
       
    private Long id;
    private String type;
    private Date starttime;
    private Date endtime;
    private String content;
    private Integer sortnum = 0;
    private Boolean disabled = false;
    
    private String creatorLabel;
    private Long creatorId;
    private Date createtime;
    private String modifierLabel;
    private Long modifierId;
    private Date modifytime;
    
    private Date lastUpdateDate;
    private boolean onlined;

    public BulletinVO() {
    }

    public BulletinVO(Long id) {
        this.id = id;
    }

    public boolean isOnlined() {
        return onlined;
    }

    public void setOnlined(boolean onlined) {
        this.onlined = onlined;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getCreatorLabel() {
        return creatorLabel;
    }

    public void setCreatorLabel(String creatorLabel) {
        this.creatorLabel = creatorLabel;
    }

    public String getModifierLabel() {
        return modifierLabel;
    }

    public void setModifierLabel(String modifierLabel) {
        this.modifierLabel = modifierLabel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BulletinVO)) {
            return false;
        }
        BulletinVO other = (BulletinVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.BulletinVO[ id=" + id + " ]";
    }
    
}
