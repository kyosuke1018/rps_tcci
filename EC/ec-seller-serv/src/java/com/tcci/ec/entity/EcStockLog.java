/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import com.tcci.cm.annotation.InputCheckMeta;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author peter.pan
 */
@Entity
@Table(name = "EC_STOCK_LOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcStockLog.findAll", query = "SELECT e FROM EcStockLog e")
    , @NamedQuery(name = "EcStockLog.findById", query = "SELECT e FROM EcStockLog e WHERE e.id = :id")
    , @NamedQuery(name = "EcStockLog.findByStoreId", query = "SELECT e FROM EcStockLog e WHERE e.storeId = :storeId")
    , @NamedQuery(name = "EcStockLog.findByPrdId", query = "SELECT e FROM EcStockLog e WHERE e.prdId = :prdId")
    , @NamedQuery(name = "EcStockLog.findByVarId", query = "SELECT e FROM EcStockLog e WHERE e.varId = :varId")})
public class EcStockLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @com.sun.istack.NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_STOCK_LOG", sequenceName = "SEQ_STOCK_LOG", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STOCK_LOG")        
    private Long id;
    @Column(name = "STORE_ID")
    private Long storeId;
    @Column(name = "PRD_ID")
    private Long prdId;
    @Column(name = "VAR_ID")
    private Long varId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
    @Column(name = "DATA_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTime;
    @Column(name = "TYPE")
    private String type;
    @InputCheckMeta(key="EC_STOCK_LOG.MEMO")
    @Column(name = "MEMO")
    private String memo;
    @Column(name = "CLOSED")
    private Boolean closed;
    @Column(name = "STOCK_ID")
    private Long stockId;
    @Column(name = "ORDER_ID")
    private Long orderId;
    @Column(name = "DETAIL_ID")
    private Long detailId;
    @Column(name = "DISABLED")
    private Boolean disabled;
    
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EcMember creator;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private EcMember modifier;

    public EcStockLog() {
    }

    public EcStockLog(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getVarId() {
        return varId;
    }

    public void setVarId(Long varId) {
        this.varId = varId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcStockLog)) {
            return false;
        }
        EcStockLog other = (EcStockLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcStockLog[ id=" + id + " ]";
    }
    
}
