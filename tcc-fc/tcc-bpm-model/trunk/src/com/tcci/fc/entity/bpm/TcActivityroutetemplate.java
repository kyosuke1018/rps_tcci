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
@Table(name = "TC_ACTIVITYROUTETEMPLATE")
@NamedQueries({
    @NamedQuery(name = "TcActivityroutetemplate.findAll", query = "SELECT t FROM TcActivityroutetemplate t")})
public class TcActivityroutetemplate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    private Long id;
    @Column(name = "ROUTENAME")
    private String routename;
    @JoinColumn(name = "FROMACTIVITY", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcActivitytemplate fromactivity;
    @JoinColumn(name = "TOACTIVITY", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcActivitytemplate toactivity;

    public TcActivityroutetemplate() {
    }

    public TcActivityroutetemplate(Long id) {
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

    public TcActivitytemplate getFromactivity() {
        return fromactivity;
    }

    public void setFromactivity(TcActivitytemplate fromactivity) {
        this.fromactivity = fromactivity;
    }

    public TcActivitytemplate getToactivity() {
        return toactivity;
    }

    public void setToactivity(TcActivitytemplate toactivity) {
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
        if (!(object instanceof TcActivityroutetemplate)) {
            return false;
        }
        TcActivityroutetemplate other = (TcActivityroutetemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.bpm.TcActivityroutetemplate[ id=" + id + " ]";
    }
    
}
