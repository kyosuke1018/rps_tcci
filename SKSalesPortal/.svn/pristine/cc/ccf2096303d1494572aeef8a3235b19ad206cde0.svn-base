/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author carl.lin
 */
@Embeddable
public class FactSkAchievementMonthPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "YEAR_MONTH")
    private String yearMonth;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "SAP_ID")
    private String sapId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "CREATEDATE")
    private String createdate;

    public FactSkAchievementMonthPK() {
    }

    public FactSkAchievementMonthPK(String yearMonth, String sapId, String createdate) {
        this.yearMonth = yearMonth;
        this.sapId = sapId;
        this.createdate = createdate;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getSapId() {
        return sapId;
    }

    public void setSapId(String sapId) {
        this.sapId = sapId;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (yearMonth != null ? yearMonth.hashCode() : 0);
        hash += (sapId != null ? sapId.hashCode() : 0);
        hash += (createdate != null ? createdate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactSkAchievementMonthPK)) {
            return false;
        }
        FactSkAchievementMonthPK other = (FactSkAchievementMonthPK) object;
        if ((this.yearMonth == null && other.yearMonth != null) || (this.yearMonth != null && !this.yearMonth.equals(other.yearMonth))) {
            return false;
        }
        if ((this.sapId == null && other.sapId != null) || (this.sapId != null && !this.sapId.equals(other.sapId))) {
            return false;
        }
        if ((this.createdate == null && other.createdate != null) || (this.createdate != null && !this.createdate.equals(other.createdate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.FactSkAchievementMonthPK[ yearMonth=" + yearMonth + ", sapId=" + sapId + ", createdate=" + createdate + " ]";
    }
    
}
