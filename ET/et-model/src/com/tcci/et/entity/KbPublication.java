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
@Table(name = "KB_PUBLICATION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KbPublication.findAll", query = "SELECT k FROM KbPublication k"),
    @NamedQuery(name = "KbPublication.findById", query = "SELECT k FROM KbPublication k WHERE k.id = :id"),
    @NamedQuery(name = "KbPublication.findByType", query = "SELECT k FROM KbPublication k WHERE k.type = :type"),
    @NamedQuery(name = "KbPublication.findByCode", query = "SELECT k FROM KbPublication k WHERE k.code = :code")})
public class KbPublication implements ContentHolder, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name="KB_PUBLICATION_ID_SEQ",sequenceName = "KB_PUBLICATION_ID_SEQ", allocationSize=1)       
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="KB_PUBLICATION_ID_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Size(max = 3)
    @Column(name = "TYPE")
    private String type;
    @Column(name = "PARENT")
    private Long parent;
    @Size(max = 3)
    @Column(name = "DATATYPE")
    private String dataType;
    @Column(name = "DATADATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDate;
    @Size(max = 40)
    @Column(name = "CODE")
    private String code;
    @Size(max = 600)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 1500)
    @Column(name = "SUMMARY")
    private String summary;
    @Size(max = 3)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "NEWS")
    private Boolean news;
    @Size(max = 3)
    @Column(name = "LANG")
    private String lang;
    
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

    public KbPublication() {
    }

    public KbPublication(Long id) {
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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getNews() {
        return news;
    }

    public void setNews(Boolean news) {
        this.news = news;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Date getDataDate() {
        return dataDate;
    }

    public void setDataDate(Date dataDate) {
        this.dataDate = dataDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        if (!(object instanceof KbPublication)) {
            return false;
        }
        KbPublication other = (KbPublication) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.entity.KbPublication[ id=" + id + " ]";
    }
    
}
