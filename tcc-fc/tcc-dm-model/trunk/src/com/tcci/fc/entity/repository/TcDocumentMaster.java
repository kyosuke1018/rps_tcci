/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.repository;

import com.tcci.fc.entity.vc.Mastered;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Wayne.Hu
 */
@Entity
@Table(name = "TC_DOCUMENT_MASTER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TcDocumentMaster.findAll", query = "SELECT t FROM TcDocumentMaster t")})
public class TcDocumentMaster implements Serializable, Mastered {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    private Long id;
    @Basic(optional = false)
    @Column(name = "DOCNUMBER")
    private String docnumber;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @OneToMany( cascade = CascadeType.ALL,  mappedBy = "master")
    private Collection<TcDocument> tcDocumentCollection;

    public TcDocumentMaster() {
    }

    public TcDocumentMaster(Long id) {
        this.id = id;
    }

    public TcDocumentMaster(Long id, String docnumber, String name) {
        this.id = id;
        this.docnumber = docnumber;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getDocnumber() {
        return docnumber;
    }

    public void setDocnumber(String docnumber) {
        this.docnumber = docnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<TcDocument> getTcDocumentCollection() {
        return tcDocumentCollection;
    }

    public void setTcDocumentCollection(Collection<TcDocument> tcDocumentCollection) {
        this.tcDocumentCollection = tcDocumentCollection;
    }
    
    @Transient
    @Override
    public Collection<TcDocument> getIterations() {
        return  getTcDocumentCollection();
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
        if (!(object instanceof TcDocumentMaster)) {
            return false;
        }
        TcDocumentMaster other = (TcDocumentMaster) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + ":" + getId();
    }
    
}
