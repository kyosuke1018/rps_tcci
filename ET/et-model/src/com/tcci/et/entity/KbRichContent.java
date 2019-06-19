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
import javax.persistence.Lob;
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
@Table(name = "KB_RICH_CONTENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KbRichContent.findAll", query = "SELECT k FROM KbRichContent k"),
    @NamedQuery(name = "KbRichContent.findById", query = "SELECT k FROM KbRichContent k WHERE k.id = :id"),
    @NamedQuery(name = "KbRichContent.findByPrimaryType", query = "SELECT k FROM KbRichContent k WHERE k.primaryType = :primaryType"),
    @NamedQuery(name = "KbRichContent.findByPrimaryId", query = "SELECT k FROM KbRichContent k WHERE k.primaryId = :primaryId"),
    @NamedQuery(name = "KbRichContent.findBySaveType", query = "SELECT k FROM KbRichContent k WHERE k.saveType = :saveType")})
public class KbRichContent implements ContentHolder, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name="KB_RICH_CONTENT_ID_SEQ",sequenceName = "KB_RICH_CONTENT_ID_SEQ", allocationSize=1)       
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="KB_RICH_CONTENT_ID_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Size(max = 3)
    @Column(name = "PRIMARYTYPE")
    private String primaryType;
    @Column(name = "PRIMARYID")
    private Long primaryId;
    @Size(max = 3)
    @Column(name = "SAVETYPE")
    private String saveType;
    @Size(max = 6000)
    @Column(name = "CONTENTS")
    private String contents;
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
//    @Lob
//    @Column(name = "CONTENT")
//    private String content;

    public KbRichContent() {
    }

    public KbRichContent(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
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
        if (!(object instanceof KbRichContent)) {
            return false;
        }
        KbRichContent other = (KbRichContent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.entity.KbRichContent[ id=" + id + " ]";
    }
    
}
