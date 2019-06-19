/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

//import com.sun.istack.NotNull;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_HOT_PRD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcHotPrd.findAll", query = "SELECT e FROM EcHotPrd e")
    , @NamedQuery(name = "EcHotPrd.findById", query = "SELECT e FROM EcHotPrd e WHERE e.id = :id")
    , @NamedQuery(name = "EcHotPrd.findByPrdId", query = "SELECT e FROM EcHotPrd e WHERE e.prdId = :prdId")
    , @NamedQuery(name = "EcHotPrd.findByStarttime", query = "SELECT e FROM EcHotPrd e WHERE e.starttime = :starttime")
    , @NamedQuery(name = "EcHotPrd.findByEndtime", query = "SELECT e FROM EcHotPrd e WHERE e.endtime = :endtime")
    , @NamedQuery(name = "EcHotPrd.findByApproveUser", query = "SELECT e FROM EcHotPrd e WHERE e.approveUser = :approveUser")})
public class EcHotPrd implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
//    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_HOT_PRD", sequenceName = "SEQ_HOT_PRD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HOT_PRD")        
    private Long id;
    
    @Column(name = "STORE_ID")
    private Long storeId;
    @Column(name = "PRD_ID")
    private Long prdId;
    @Column(name = "MESSAGE")
    private String message;
    @Column(name = "STARTTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttime;
    @Column(name = "ENDTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endtime;
    
    @Column(name = "APPROVE_USER")
    private Long approveUser;
    @Column(name = "APPROVE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approveTime;
    
    @Column(name = "SORTNUM")
    private Integer sortnum = 0;
    
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EcMember creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private EcMember modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public EcHotPrd() {
    }

    public EcHotPrd(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
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

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(Long approveUser) {
        this.approveUser = approveUser;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
    }

    public EcMember getModifier() {
        return modifier;
    }

    public void setModifier(EcMember modifier) {
        this.modifier = modifier;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        if (!(object instanceof EcHotPrd)) {
            return false;
        }
        EcHotPrd other = (EcHotPrd) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcHotPrd[ id=" + id + " ]";
    }
    
}
