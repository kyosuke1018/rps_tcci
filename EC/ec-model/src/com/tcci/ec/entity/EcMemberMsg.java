/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import com.tcci.ec.enums.MsgTypeEnum;
import com.tcci.ec.enums.PaymentTypeEnum;
import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_MEMBER_MSG")
@NamedQueries({
    @NamedQuery(name = "EcMemberMsg.findAll", query = "SELECT e FROM EcMemberMsg e"),
    @NamedQuery(name = "EcMemberMsg.findByMember", query = "SELECT e FROM EcMemberMsg e WHERE e.member=:member ORDER BY e.createtime ASC"),
    @NamedQuery(name = "EcMemberMsg.findByPrd", query = "SELECT e FROM EcMemberMsg e WHERE e.product=:product and e.type=:type ORDER BY e.createtime ASC"),
    @NamedQuery(name = "EcMemberMsg.findByStore", query = "SELECT e FROM EcMemberMsg e WHERE e.store=:store and e.type=:type ORDER BY e.createtime ASC"),
    @NamedQuery(name = "EcMemberMsg.findRootByPrd", query = "SELECT e FROM EcMemberMsg e WHERE e.product=:product and e.type=:type and e.parent is null ORDER BY e.createtime DESC"),
    @NamedQuery(name = "EcMemberMsg.findRootByStore", query = "SELECT e FROM EcMemberMsg e WHERE e.store=:store and e.type=:type and e.parent is null ORDER BY e.createtime DESC"),
    @NamedQuery(name = "EcMemberMsg.findByParent", query = "SELECT e FROM EcMemberMsg e WHERE e.parent=:parent ORDER BY e.createtime ASC")
})
public class EcMemberMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_MEMBER_MSG",sequenceName = "SEQ_MEMBER_MSG", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_MEMBER_MSG")
    private Long id;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcMember member;
    @JoinColumn(name = "PRD_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcProduct product;
    @JoinColumn(name = "STORE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcStore store;
    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private MsgTypeEnum type;
    
    
    @Size(max = 100)
    @Column(name = "MESSAGE")
    private String message;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EcMember creator;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @Column(name = "PARENT")
    private Long parent;

    public EcMemberMsg() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EcMember getMember() {
        return member;
    }

    public void setMember(EcMember member) {
        this.member = member;
    }

    public EcProduct getProduct() {
        return product;
    }

    public void setProduct(EcProduct product) {
        this.product = product;
    }

    public EcStore getStore() {
        return store;
    }

    public void setStore(EcStore store) {
        this.store = store;
    }

    public MsgTypeEnum getType() {
        return type;
    }

    public void setType(MsgTypeEnum type) {
        this.type = type;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
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
        if (!(object instanceof EcMemberMsg)) {
            return false;
        }
        EcMemberMsg other = (EcMemberMsg) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcOrderMessage[ id=" + id + " ]";
    }
    
}
