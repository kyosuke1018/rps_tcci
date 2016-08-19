/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.bpm;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author wayne
 */
@Entity
@Table(name = "TC_ACTIVITYROUTE")
@NamedQueries({
    @NamedQuery(name = "TcActivityroute.findAll", query = "SELECT t FROM TcActivityroute t")})
public class TcActivityroute implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator (name="SEQ_TCC", sequenceName="SEQ_TCC", allocationSize=1)
    private Long id;
    @Column(name = "ROUTENAME")
    private String routename;
    @Column(name = "TALLY")
    private Long tally;
    @JoinColumn(name = "FROMACTIVITY", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcActivity fromactivity;
    @JoinColumn(name = "TOACTIVITY", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcActivity toactivity;

    public TcActivityroute() {
    }

    public TcActivityroute(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoutename() {
        return routename;
    }

    public void setRoutename(String routename) {
        this.routename = routename;
    }

    public Long getTally() {
        return tally;
    }

    public void setTally(Long tally) {
        this.tally = tally;
    }

    public TcActivity getFromactivity() {
        return fromactivity;
    }

    public void setFromactivity(TcActivity fromactivity) {
        this.fromactivity = fromactivity;
    }

    public TcActivity getToactivity() {
        return toactivity;
    }

    public void setToactivity(TcActivity toactivity) {
        this.toactivity = toactivity;
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
        if (!(object instanceof TcActivityroute)) {
            return false;
        }
        TcActivityroute other = (TcActivityroute) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.bpm.TcActivityroute[ id=" + id + " ]";
    }
    
}
