/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.bpm;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author louiszHome
 */
@Entity
@Table(name = "TC_VARIABLE")
@NamedQueries({
    @NamedQuery(name = "TcVariable.findAll", query = "SELECT t FROM TcVariable t")})
public class TcVariable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator (name="SEQ_TCC", sequenceName="SEQ_TCC", allocationSize=1)
    private Long id;
    @Basic(optional = false)  
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)   
    @Column(name = "VALUE")
    private String value;
    @Basic(optional = false)
    @Column(name = "CONTAINERCLASSNAME")
    private String containerclassname;
    @Basic(optional = false)
    @Column(name = "CONTAINERID")
    private long containerid;
    
     @Basic(optional = false)
    @NotNull
    @Column(name = "VISIBLE")
    private boolean visible;

    public TcVariable() {
    }

    public TcVariable(Long id) {
        this.id = id;
    }

    public TcVariable(Long id, String name, String value, String containerclassname, long containerid) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.containerclassname = containerclassname;
        this.containerid = containerid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getContainerclassname() {
        return containerclassname;
    }

    public void setContainerclassname(String containerclassname) {
        this.containerclassname = containerclassname;
    }

    public long getContainerid() {
        return containerid;
    }

    public void setContainerid(long containerid) {
        this.containerid = containerid;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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
        if (!(object instanceof TcVariable)) {
            return false;
        }
        TcVariable other = (TcVariable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.bpm.TcVariable[ id=" + id + " ]";
    }
    
}
