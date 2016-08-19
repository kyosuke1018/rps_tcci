/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.content;

import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gilbert.Lin
 */
@Entity
@Table(name = "TC_FVITEM")
public class TcFvitem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "FILENAME")
    private String filename;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILESIZE")
    private long filesize;
    @Size(max = 255)
    @Column(name = "CONTENTTYPE")
    private String contenttype;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fvitem")
    private Collection<TcApplicationdata> tcApplicationdataCollection;
    @JoinColumn(name = "DOMAIN", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcDomain domain;

    public TcFvitem() {
    }

    public TcFvitem(Long id) {
        this.id = id;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public Collection<TcApplicationdata> getTcApplicationdataCollection() {
        return tcApplicationdataCollection;
    }

    public void setTcApplicationdataCollection(Collection<TcApplicationdata> tcApplicationdataCollection) {
        this.tcApplicationdataCollection = tcApplicationdataCollection;
    }

    public TcDomain getDomain() {
        return domain;
    }

    public void setDomain(TcDomain domain) {
        this.domain = domain;
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
        if (!(object instanceof TcFvitem)) {
            return false;
        }
        TcFvitem other = (TcFvitem) object;
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
