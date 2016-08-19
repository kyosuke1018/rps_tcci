/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.entity;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.entity.FcCompany;
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

/**
 *
 * @author Kyle.Cheng
 */
@Entity
@Table(name = "RPT_SHEET_UPLOAD")
@NamedQueries({
    @NamedQuery(name = "RptSheetUpload.findByYearmonthCompany",
        query = "SELECT u FROM RptSheetUpload u"
        + " WHERE u.yearmonth=:yearmonth AND u.company=:company"),
    @NamedQuery(name = "RptSheetUpload.findByYearmonth",
        query = "SELECT u FROM RptSheetUpload u"
        + " WHERE u.yearmonth=:yearmonth AND u.company.group.code = :group ")
})
public class RptSheetUpload implements Serializable, ContentHolder {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    private Long id;
    @JoinColumn(name = "COMPANY", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FcCompany company;
    @Basic(optional = false)
    @NotNull
    @Column(name = "YEARMONTH")
    private String yearmonth;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser modifier;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    
    public RptSheetUpload() {
    }
    
    // getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FcCompany getCompany() {
        return company;
    }

    public void setCompany(FcCompany company) {
        this.company = company;
    }

    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
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
    
}
