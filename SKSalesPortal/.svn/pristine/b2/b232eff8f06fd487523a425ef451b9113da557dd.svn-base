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
public class FactSkAchievementChannelPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "YEAR_MONTH")
    private String yearMonth;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CHANNEL")
    private String channel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "CREATEDATE")
    private String createdate;

    public FactSkAchievementChannelPK() {
    }

    public FactSkAchievementChannelPK(String yearMonth, String channel, String createdate) {
        this.yearMonth = yearMonth;
        this.channel = channel;
        this.createdate = createdate;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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
        hash += (channel != null ? channel.hashCode() : 0);
        hash += (createdate != null ? createdate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactSkAchievementChannelPK)) {
            return false;
        }
        FactSkAchievementChannelPK other = (FactSkAchievementChannelPK) object;
        if ((this.yearMonth == null && other.yearMonth != null) || (this.yearMonth != null && !this.yearMonth.equals(other.yearMonth))) {
            return false;
        }
        if ((this.channel == null && other.channel != null) || (this.channel != null && !this.channel.equals(other.channel))) {
            return false;
        }
        if ((this.createdate == null && other.createdate != null) || (this.createdate != null && !this.createdate.equals(other.createdate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.FactSkAchievementChannelPK[ yearMonth=" + yearMonth + ", channel=" + channel + ", createdate=" + createdate + " ]";
    }
    
}
