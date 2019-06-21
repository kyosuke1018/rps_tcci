/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.entity;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.org.TcUser;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "KB_PHOTO_GALLERY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KbPhotoGallery.findAll", query = "SELECT k FROM KbPhotoGallery k"),
    @NamedQuery(name = "KbPhotoGallery.findById", query = "SELECT k FROM KbPhotoGallery k WHERE k.id = :id"),
    @NamedQuery(name = "KbPhotoGallery.findByPrimaryType", query = "SELECT k FROM KbPhotoGallery k WHERE k.primaryType = :primaryType"),
    @NamedQuery(name = "KbPhotoGallery.findByPrimaryId", query = "SELECT k FROM KbPhotoGallery k WHERE k.primaryId = :primaryId"),
    @NamedQuery(name = "KbPhotoGallery.findByModifytimestamp", query = "SELECT k FROM KbPhotoGallery k WHERE k.modifytimestamp = :modifytimestamp")})
public class KbPhotoGallery implements ContentHolder, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name="KB_PHOTO_GALLERY_ID_SEQ",sequenceName = "KB_PHOTO_GALLERY_ID_SEQ", allocationSize=1)       
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="KB_PHOTO_GALLERY_ID_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Size(max = 10)
    private String code;
    @Size(max = 3)
    @Column(name = "PRIMARYTYPE")
    private String primaryType;
    @Column(name = "PRIMARYID")
    private Long primaryId;
    @Size(max = 120)
    @Column(name = "CNAME")
    private String cname;
    @Size(max = 120)
    @Column(name = "ENAME")
    private String ename;
    @Size(max = 300)
    @Column(name = "SUBJECT")
    private String subject;
    @Size(max = 1500)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "DATADATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDate;
    
    @Column(name = "PARENT")
    private Long parent;
    @Size(max = 3)
    @Column(name = "LANG")
    private String lang;
    
    @Column(name = "WIDTH")
    private Integer width;
    @Column(name = "HEIGHT")
    private Integer height;
    @Column(name = "COMPRESSED")
    private Boolean compressed;
    @Column(name = "IDENTIFY_IMG")
    private Boolean identifyImg;
    
    @Size(max = 3)
    @Column(name = "STATUS")
    private String status;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;

    public KbPhotoGallery() {
    }

    public KbPhotoGallery(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Boolean getIdentifyImg() {
        return identifyImg;
    }

    public void setIdentifyImg(Boolean identifyImg) {
        this.identifyImg = identifyImg;
    }

    public Boolean getCompressed() {
        return compressed;
    }

    public void setCompressed(Boolean compressed) {
        this.compressed = compressed;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(Long primaryId) {
        this.primaryId = primaryId;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public Date getDataDate() {
        return dataDate;
    }

    public void setDataDate(Date dataDate) {
        this.dataDate = dataDate;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
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
        if (!(object instanceof KbPhotoGallery)) {
            return false;
        }
        KbPhotoGallery other = (KbPhotoGallery) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.entity.KbPhotoGallery[ id=" + id + " ]";
    }
    
}
