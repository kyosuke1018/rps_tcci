/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.entity.admin;

import com.tcci.fc.entity.org.TcGroup;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Peter
 */
@Entity
@Table(name = "CM_GROUP_FUNCTION_R")
@NamedQueries({
    @NamedQuery(name = "CmGroupFunctionR.findAll", query = "SELECT p FROM CmGroupFunctionR p")})
public class CmGroupFunctionR implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "CM_GROUP_FUNCTION_R_ID_SEQ", sequenceName = "CM_GROUP_FUNCTION_R_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CM_GROUP_FUNCTION_R_ID_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")
    @ManyToOne
    private TcGroup groupId;
    @JoinColumn(name = "FUNC_ID", referencedColumnName = "ID")
    @ManyToOne
    private CmFunction funcId;    
    @Column(name = "AUTH")
    private String auth;
    @Column(name = "CREATOR")
    private Long creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public CmGroupFunctionR() {
    }

    public CmGroupFunctionR(Long id) {
        this.id = id;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public CmFunction getFuncId() {
        return funcId;
    }

    public void setFuncId(CmFunction funcId) {
        this.funcId = funcId;
    }

    public TcGroup getGroupId() {
        return groupId;
    }

    public void setGroupId(TcGroup groupId) {
        this.groupId = groupId;
    }
    //</editor-fold>
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CmGroupFunctionR)) {
            return false;
        }
        
        CmGroupFunctionR other = (CmGroupFunctionR) object;
        if( other.getId()==null ){
            return false;
        }
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "com.tcci.pp.entity.admin.CmGroupFunctionR[ id=" + id + " ]";
    }
    
}
