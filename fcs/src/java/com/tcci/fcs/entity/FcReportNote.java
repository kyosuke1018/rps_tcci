/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.entity;

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
@Table(name = "FC_REPORT_NOTE")
@NamedQueries({
    @NamedQuery(name = "FcReportNote.findAll", query = "SELECT f FROM FcReportNote f")})
public class FcReportNote implements Serializable {
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
    @Column(name = "YEARMONTH")
    private String yearmonth;
    @Size(max = 500)
    @Column(name = "NOTE")
    private String note;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser modifier;
    @JoinColumn(name = "COID1", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FcCompany coid1;
    @JoinColumn(name = "COID2", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FcCompany coid2;

    public FcReportNote() {
    }

    public FcReportNote(String yearmonth, FcCompany coid1, FcCompany coid2) {
        this.yearmonth = yearmonth;
        this.coid1 = coid1;
        this.coid2 = coid2;
    }
    
    public FcReportNote(Long id) {
        this.id = id;
    }

    public FcReportNote(Long id, String yearmonth, Date modifytimestamp) {
        this.id = id;
        this.yearmonth = yearmonth;
        this.modifytimestamp = modifytimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public FcCompany getCoid1() {
        return coid1;
    }

    public void setCoid1(FcCompany coid1) {
        this.coid1 = coid1;
    }

    public FcCompany getCoid2() {
        return coid2;
    }

    public void setCoid2(FcCompany coid2) {
        this.coid2 = coid2;
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
        if (!(object instanceof FcReportNote)) {
            return false;
        }
        FcReportNote other = (FcReportNote) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fcs.entity.FcReportNote[ id=" + id + " ]";
    }

}
