/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.entity;

import com.tcci.fcs.enums.CompanyGroupEnum;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Kyle.Cheng
 */
@Entity
@Table(name = "FC_COMP_GROUP")
@NamedQueries({
    @NamedQuery(name = "FcCompGroup.findAll", query = "SELECT c FROM FcCompGroup c"),
    @NamedQuery(name = "FcCompGroup.findByCode", query = "SELECT c FROM FcCompGroup c WHERE c.code=:code")
})
public class FcCompGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "CODE")
    private String code;
//    @Enumerated(EnumType.STRING)
//    @Column(name = "CODE")
//    private CompanyGroupEnum code;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NAME")
    private String name;
    @JoinColumn(name = "CURRENCY", referencedColumnName = "ID")
    @ManyToOne
    private FcCurrency currency;
    @Column(name = "SAP_GROUP_CODE")
    private String sapGroupCode;
    
    public FcCompGroup() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FcCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(FcCurrency currency) {
        this.currency = currency;
    }

    public String getSapGroupCode() {
        return sapGroupCode;
    }

    public void setSapGroupCode(String sapGroupCode) {
        this.sapGroupCode = sapGroupCode;
    }
    
    
}
