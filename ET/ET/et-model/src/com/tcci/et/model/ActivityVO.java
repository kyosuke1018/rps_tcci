/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.et.enums.ActivityStatusEnum;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@XmlRootElement
public class ActivityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    @Size(max = 180)
    private String name;
    @Size(max = 600)
    private String summary;
    @Size(max = 3)
    private String status;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endtime;
    
    private TcUser creator;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    private TcUser modifier;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;

    private String lastUserName;
    private Date lastTime;
    
    public TcUser getLastUpdateUser(){
        return (modifier!=null)?modifier:creator;
    }
    public Date getLastUpdateTime(){
        return (modifytimestamp!=null)?modifytimestamp:createtimestamp;
    }

    public ActivityStatusEnum getStatusEnum(){
        return ActivityStatusEnum.getFromCode(status);
    }
    
    public String getStatusName(){
        ActivityStatusEnum enum1 = getStatusEnum();
        return (enum1!=null)?enum1.getName():"";
    }
    
    public ActivityVO() {
    }

    public ActivityVO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActivityVO)) {
            return false;
        }
        ActivityVO other = (ActivityVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.ActivityVO[ id=" + id + " ]";
    }
    
}
