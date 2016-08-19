/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.content;

import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gilbert.Lin
 */
@Entity
@Table(name = "TC_APPLICATIONDATA")
public class TcApplicationdata implements Serializable, ContentItem {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    private Long id;
    @Size(max = 255)
    @Column(name = "CONTAINERCLASSNAME")
    private String containerclassname;
    @Column(name = "CONTAINERID")
    private Long containerid;
    @Column(name = "CONTENTROLE")
    private Character contentrole;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "DESCRIPTION")
    private String description;
    @JoinColumn(name = "FVITEM", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcFvitem fvitem;

    public TcApplicationdata() {
    }

    public TcApplicationdata(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContainerclassname() {
        return containerclassname;
    }

    public void setContainerclassname(String containerclassname) {
        this.containerclassname = containerclassname;
    }

    public Long getContainerid() {
        return containerid;
    }

    public void setContainerid(Long containerid) {
        this.containerid = containerid;
    }

    public Character getContentrole() {
        return contentrole;
    }

    public void setContentrole(Character contentrole) {
        this.contentrole = contentrole;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TcFvitem getFvitem() {
        return fvitem;
    }

    public void setFvitem(TcFvitem fvitem) {
        this.fvitem = fvitem;
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
        if (!(object instanceof TcApplicationdata)) {
            return false;
        }
        TcApplicationdata other = (TcApplicationdata) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + ":" + getId();
    }

    /**
     * @return the creator
     */
    public TcUser getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(TcUser creator) {
        this.creator = creator;
    }
}
