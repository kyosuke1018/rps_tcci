/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_CONTRACT_PRODUCT")
@NamedQueries({
    @NamedQuery(name = "EcContractProduct.findAll", query = "SELECT e FROM EcContractProduct e"),
    @NamedQuery(name = "EcContractProduct.findByContract", query = "SELECT e FROM EcContractProduct e WHERE e.ecContract=:ecContract"),
    @NamedQuery(name = "EcContractProduct.findByContractPlant", query = "SELECT e FROM EcContractProduct e WHERE e.ecContract=:ecContract AND e.ecPlant=:ecPlant ORDER BY e.ecProduct.name"),
})
public class EcContractProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EcContractProductPK ecContractProductPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "UNIT_PRICE")
    private BigDecimal unitPrice;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "METHOD")
    private String method;
    @JoinColumn(name = "SALESAREA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcSalesarea ecSalesarea;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcProduct ecProduct;
    @JoinColumn(name = "PLANT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcPlant ecPlant;
    @JoinColumn(name = "CONTRACT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcContract ecContract;

    public EcContractProduct() {
    }

    public EcContractProduct(EcContractProductPK ecContractProductPK) {
        this.ecContractProductPK = ecContractProductPK;
    }

    public EcContractProduct(EcContractProductPK ecContractProductPK, String method) {
        this.ecContractProductPK = ecContractProductPK;
        this.method = method;
    }

    public EcContractProduct(long contractId, int posnr) {
        this.ecContractProductPK = new EcContractProductPK(contractId, posnr);
    }

    public EcContractProductPK getEcContractProductPK() {
        return ecContractProductPK;
    }

    public void setEcContractProductPK(EcContractProductPK ecContractProductPK) {
        this.ecContractProductPK = ecContractProductPK;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public EcSalesarea getEcSalesarea() {
        return ecSalesarea;
    }

    public void setEcSalesarea(EcSalesarea ecSalesarea) {
        this.ecSalesarea = ecSalesarea;
    }

    public EcProduct getEcProduct() {
        return ecProduct;
    }

    public void setEcProduct(EcProduct ecProduct) {
        this.ecProduct = ecProduct;
    }

    public EcPlant getEcPlant() {
        return ecPlant;
    }

    public void setEcPlant(EcPlant ecPlant) {
        this.ecPlant = ecPlant;
    }

    public EcContract getEcContract() {
        return ecContract;
    }

    public void setEcContract(EcContract ecContract) {
        this.ecContract = ecContract;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecContractProductPK != null ? ecContractProductPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcContractProduct)) {
            return false;
        }
        EcContractProduct other = (EcContractProduct) object;
        if ((this.ecContractProductPK == null && other.ecContractProductPK != null) || (this.ecContractProductPK != null && !this.ecContractProductPK.equals(other.ecContractProductPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcContractProduct[ ecContractProductPK=" + ecContractProductPK + " ]";
    }
    
}
