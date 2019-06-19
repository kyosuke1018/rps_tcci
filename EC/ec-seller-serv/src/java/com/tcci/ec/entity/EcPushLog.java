/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_PUSH_LOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcPushLog.findAll", query = "SELECT e FROM EcPushLog e")
    , @NamedQuery(name = "EcPushLog.findById", query = "SELECT e FROM EcPushLog e WHERE e.id = :id")
    , @NamedQuery(name = "EcPushLog.findByCategory", query = "SELECT e FROM EcPushLog e WHERE e.category = :category")
    , @NamedQuery(name = "EcPushLog.findByPushType", query = "SELECT e FROM EcPushLog e WHERE e.pushType = :pushType")
    , @NamedQuery(name = "EcPushLog.findByTitle", query = "SELECT e FROM EcPushLog e WHERE e.title = :title")})
public class EcPushLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_PUSH_LOG", sequenceName = "SEQ_PUSH_LOG", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PUSH_LOG")        
    private Long id;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "PUSH_TYPE")
    private String pushType;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "ALERT")
    private String alert;
    @Column(name = "AUDIENCE")
    private String audience;
    @Column(name = "PUSH_RESULT")
    private String pushResult;
    @Basic(optional = false)
    @Column(name = "SUCCESS")
    private Boolean success;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;

    public EcPushLog() {
    }
    
    public EcPushLog(String category, String pushType, String title, String alert, String audience) {
        this.category = category;
        this.pushType = pushType;
        this.title = title;
        this.alert = alert;
        this.audience = audience;
        this.createtime = new Date();
    }

    public EcPushLog(Long id) {
        this.id = id;
    }

    public EcPushLog(Long id, Boolean success) {
        this.id = id;
        this.success = success;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getPushResult() {
        return pushResult;
    }

    public void setPushResult(String pushResult) {
        this.pushResult = pushResult;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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
        if (!(object instanceof EcPushLog)) {
            return false;
        }
        EcPushLog other = (EcPushLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcPushLog[ id=" + id + " ]";
    }
    
}
