/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import com.tcci.fc.entity.org.TcUser;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_STORE")
@NamedQueries({
    @NamedQuery(name = "EcStore.findAll", query = "SELECT e FROM EcStore e"),
    @NamedQuery(name = "EcStore.findBySeller", query = "SELECT e FROM EcStore e where e.seller=:seller"),
    @NamedQuery(name = "EcStore.findByMember", query = "SELECT e FROM EcStore e where e.seller.member=:member")
})
public class EcStore implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_STORE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    private Long id;
    @JoinColumn(name = "SELLER_ID", referencedColumnName = "ID")
    @ManyToOne
    private EcSeller seller;
    @Column(name = "type")
    private String type;
    @Column(name = "CNAME")
    private String cname;
    @Column(name = "ENAME")
    private String ename;
    @Column(name = "BRIEF")
    private String brief;
    
    //DETAIL_ID 明細檔ID (by類型)
    @Column(name = "STYLE_ID")
    private Long styleId;//網頁樣板ID
    @Column(name = "FLOW_ID")
    private Long flowId;//流程樣板ID
    @Column(name = "PRD_TYPE_LEVEL")
    private Long prdTypeLevel;//商品分類階層
    @Column(name = "DISABLED")
    private Boolean disabled;
    @Column(name = "REMIT_ACCOUNT")
    private String remitAccount;
    @Column(name = "OPENED")
    private Boolean opened;
    @Column(name = "DEF_STORE")
    private Boolean defStore;
    
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;
    

    public EcStore() {
    }

    public EcStore(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EcSeller getSeller() {
        return seller;
    }

    public void setSeller(EcSeller seller) {
        this.seller = seller;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public Boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Long getStyleId() {
        return styleId;
    }

    public void setStyleId(Long styleId) {
        this.styleId = styleId;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Long getPrdTypeLevel() {
        return prdTypeLevel;
    }

    public void setPrdTypeLevel(Long prdTypeLevel) {
        this.prdTypeLevel = prdTypeLevel;
    }

    public String getRemitAccount() {
        return remitAccount;
    }

    public void setRemitAccount(String remitAccount) {
        this.remitAccount = remitAccount;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }
    
    public Boolean getDefStore() {
        return defStore;
    }

    public void setDefStore(Boolean defStore) {
        this.defStore = defStore;
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
        if (!(object instanceof EcStore)) {
            return false;
        }
        EcStore other = (EcStore) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcStore[ id=" + id + " ]";
    }

}
