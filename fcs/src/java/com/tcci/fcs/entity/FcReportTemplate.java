/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.entity;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.enums.CompanyGroupEnum;
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
@Table(name = "FC_REPORT_TEMPLATE")
@NamedQueries({
    @NamedQuery(name = "FcReportTemplate.findByYearmonth", 
        query = "SELECT t FROM FcReportTemplate t"
        + " WHERE t.yearmonth=:yearmonth AND t.group = :group "),
    @NamedQuery(name = "FcReportTemplate.findLast", 
        query = "SELECT t FROM FcReportTemplate t where t.group = :group "
        + " ORDER BY t.yearmonth DESC")
})
public class FcReportTemplate implements Serializable, ContentHolder {
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @Size(max = 1000)
    @Column(name = "NOTE")
    private String note;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser modifier;
    @Size(max = 20)
    @Column(name = "VERSION")
    private String version;
    @Enumerated(EnumType.STRING)
    @Column(name = "COMP_GROUP")
    private CompanyGroupEnum group;

    public FcReportTemplate() {
    }

    public FcReportTemplate(Long id) {
        this.id = id;
    }

    public FcReportTemplate(String yearmonth) {
        this.yearmonth = yearmonth;
    }
    
    public FcReportTemplate(Long id, String yearmonth, Date modifytimestamp) {
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

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public CompanyGroupEnum getGroup() {
        return group;
    }

    public void setGroup(CompanyGroupEnum group) {
        this.group = group;
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
        if (!(object instanceof FcReportTemplate)) {
            return false;
        }
        FcReportTemplate other = (FcReportTemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fcs.entity.FcReportTemplate[ id=" + id + " ]";
    }

}
