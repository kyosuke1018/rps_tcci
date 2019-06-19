/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@XmlRootElement
public class UseLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String type;
    private String category;
    private Long memberId;
    private Long period;
    private String src;
    private String clientIp;
    private String clientInfo;
    private String memo;
    private Double patrolLatitude;
    private Double patrolLongitude;
    
    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;

    private String memberLabel;
    
    public UseLogVO() {
    }

    public UseLogVO(Long id) {
        this.id = id;
    }

    public UseLogVO(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public Double getPatrolLatitude() {
        return patrolLatitude;
    }

    public void setPatrolLatitude(Double patrolLatitude) {
        this.patrolLatitude = patrolLatitude;
    }

    public Double getPatrolLongitude() {
        return patrolLongitude;
    }

    public void setPatrolLongitude(Double patrolLongitude) {
        this.patrolLongitude = patrolLongitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public String getMemberLabel() {
        return memberLabel;
    }

    public void setMemberLabel(String memberLabel) {
        this.memberLabel = memberLabel;
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
        if (!(object instanceof UseLogVO)) {
            return false;
        }
        UseLogVO other = (UseLogVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.UseLogVO[ id=" + id + " ]";
    }
    
}
