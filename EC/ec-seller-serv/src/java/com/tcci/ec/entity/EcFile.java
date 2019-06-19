/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
import com.tcci.cm.annotation.InputCheckMeta;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_FILE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcFile.findAll", query = "SELECT e FROM EcFile e")
    , @NamedQuery(name = "EcFile.findById", query = "SELECT e FROM EcFile e WHERE e.id = :id")
    , @NamedQuery(name = "EcFile.findByPrimaryType", query = "SELECT e FROM EcFile e WHERE e.primaryType = :primaryType")
    , @NamedQuery(name = "EcFile.findByPrimaryId", query = "SELECT e FROM EcFile e WHERE e.primaryId = :primaryId")
    , @NamedQuery(name = "EcFile.findByPrimary", query = "SELECT e FROM EcFile e WHERE e.primaryId = :primaryId and e.primaryType = :primaryType")
    , @NamedQuery(name = "EcFile.findByStoreId", query = "SELECT e FROM EcFile e WHERE e.storeId = :storeId")})
public class EcFile implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_FILE", sequenceName = "SEQ_FILE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FILE")        
    private Long id;
    @Column(name = "PRIMARY_TYPE")
    private String primaryType;
    @Column(name = "PRIMARY_ID")
    private Long primaryId;
    @Column(name = "STORE_ID")
    private Long storeId;
    @InputCheckMeta(key="EC_FILE.NAME")
    @Column(name = "NAME")
    private String name;
    @InputCheckMeta(key="EC_FILE.DESCRIPTION")
    @Column(name = "DESCRIPTION")
    private String description;
    @InputCheckMeta(key="EC_FILE.FILENAME")
    @Column(name = "FILENAME")
    private String filename;
    @InputCheckMeta(key="EC_FILE.SAVENAME")
    @Column(name = "SAVENAME")
    private String savename;
    @InputCheckMeta(key="EC_FILE.SAVEDIR")
    @Column(name = "SAVEDIR")
    private String savedir;
    @InputCheckMeta(key="EC_FILE.CONTENT_TYPE")
    @Column(name = "CONTENT_TYPE")
    private String contentType;
    @Column(name = "FILESIZE")
    private Integer fileSize;
    
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EcMember creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private EcMember modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public EcFile() {
    }

    public EcFile(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
    }

    public EcMember getModifier() {
        return modifier;
    }

    public void setModifier(EcMember modifier) {
        this.modifier = modifier;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public Long getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(Long primaryId) {
        this.primaryId = primaryId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSavename() {
        return savename;
    }

    public void setSavename(String savename) {
        this.savename = savename;
    }

    public String getSavedir() {
        return savedir;
    }

    public void setSavedir(String savedir) {
        this.savedir = savedir;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
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
        if (!(object instanceof EcFile)) {
            return false;
        }
        EcFile other = (EcFile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcFile[ id=" + id + " ]";
    }
    
}
