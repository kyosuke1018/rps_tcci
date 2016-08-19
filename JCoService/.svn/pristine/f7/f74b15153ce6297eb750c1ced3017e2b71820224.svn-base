/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.entity;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author peter.pan
 */
@Entity
@Table(name = "JCO_SERVICE_LOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JcoServiceLog.findAll", query = "SELECT j FROM JcoServiceLog j"),
    @NamedQuery(name = "JcoServiceLog.findById", query = "SELECT j FROM JcoServiceLog j WHERE j.id = :id")})
public class JcoServiceLog implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_JCO_LOG", sequenceName = "SEQ_JCO_LOG", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_JCO_LOG")        
    private Long id;
    @Column(name = "SID")
    private Long sid;
    @Size(max = 20)
    @Column(name = "CLIENT_IP")
    private String clientIp;
    @Size(max = 20)
    @Column(name = "SERVER_IP")
    private String serverIp;
    @Size(max = 60)
    @Column(name = "CLIENT_CODE")
    private String clientCode;
    @Size(max = 60)
    @Column(name = "OPERATOR")
    private String operator;
    @Size(max = 60)
    @Column(name = "FUNCTION_NAME")
    private String functionName;
    @Size(max = 10)
    @Column(name = "SAP_CLIENT_CODE")
    private String sapClientCode;
    @Size(max = 250)
    @Column(name = "INPUT_BRIEF")
    private String inputBrief;
    @Column(name = "RUN_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date runTime;
    @Column(name = "TIME_CONSUMING")
    private Long timeConsuming;
    @Column(name = "SUCCESS")
    private Boolean success;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;

    public JcoServiceLog() {
    }

    public JcoServiceLog(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getSapClientCode() {
        return sapClientCode;
    }

    public void setSapClientCode(String sapClientCode) {
        this.sapClientCode = sapClientCode;
    }

    public String getInputBrief() {
        return inputBrief;
    }

    public void setInputBrief(String inputBrief) {
        this.inputBrief = inputBrief;
    }

    public Date getRunTime() {
        return runTime;
    }

    public void setRunTime(Date runTime) {
        this.runTime = runTime;
    }

    public Long getTimeConsuming() {
        return timeConsuming;
    }

    public void setTimeConsuming(Long timeConsuming) {
        this.timeConsuming = timeConsuming;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
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
        if (!(object instanceof JcoServiceLog)) {
            return false;
        }
        JcoServiceLog other = (JcoServiceLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.jco.entity.JcoServiceLog[ id=" + id + " ]";
    }
    
}
