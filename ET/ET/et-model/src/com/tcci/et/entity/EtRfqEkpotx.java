/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.entity;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "ET_RFQ_EKPOTX")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtRfqEkpotx.findAll", query = "SELECT e FROM EtRfqEkpotx e")
    , @NamedQuery(name = "EtRfqEkpotx.findById", query = "SELECT e FROM EtRfqEkpotx e WHERE e.id = :id")
    , @NamedQuery(name = "EtRfqEkpotx.findByTenderId", query = "SELECT e FROM EtRfqEkpotx e WHERE e.tenderId = :tenderId")
    , @NamedQuery(name = "EtRfqEkpotx.findByRfqId", query = "SELECT e FROM EtRfqEkpotx e WHERE e.rfqId = :rfqId")
    , @NamedQuery(name = "EtRfqEkpotx.findByMandt", query = "SELECT e FROM EtRfqEkpotx e WHERE e.mandt = :mandt")
    , @NamedQuery(name = "EtRfqEkpotx.findByEbeln", query = "SELECT e FROM EtRfqEkpotx e WHERE e.ebeln = :ebeln")
    , @NamedQuery(name = "EtRfqEkpotx.findByEbelp", query = "SELECT e FROM EtRfqEkpotx e WHERE e.ebelp = :ebelp")
    , @NamedQuery(name = "EtRfqEkpotx.findByTdid", query = "SELECT e FROM EtRfqEkpotx e WHERE e.tdid = :tdid")})
public class EtRfqEkpotx implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_RFQ_EKPOTX", sequenceName = "SEQ_RFQ_EKPOTX", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RFQ_EKPOTX")        
    private Long id;
    @Column(name = "TENDER_ID")
    private Long tenderId;
    @Column(name = "RFQ_ID")
    private Long rfqId;
    @Size(max = 6)
    @Column(name = "MANDT")
    private String mandt;
    @Size(max = 20)
    @Column(name = "EBELN")
    private String ebeln;
    @Column(name = "EBELP")
    private Long ebelp;
    @Size(max = 8)
    @Column(name = "TDID")
    private String tdid;
    @Size(max = 60)
    @Column(name = "TDTEXT")
    private String tdtext;
    @Column(name = "LINE_NO")
    private Integer lineNo;
    @Size(max = 264)
    @Column(name = "TEXT_LINE")
    private String textLine;
    
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public EtRfqEkpotx() {
    }

    public EtRfqEkpotx(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public Long getRfqId() {
        return rfqId;
    }

    public void setRfqId(Long rfqId) {
        this.rfqId = rfqId;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getEbeln() {
        return ebeln;
    }

    public void setEbeln(String ebeln) {
        this.ebeln = ebeln;
    }

    public Long getEbelp() {
        return ebelp;
    }

    public void setEbelp(Long ebelp) {
        this.ebelp = ebelp;
    }

    public String getTdid() {
        return tdid;
    }

    public void setTdid(String tdid) {
        this.tdid = tdid;
    }

    public String getTdtext() {
        return tdtext;
    }

    public void setTdtext(String tdtext) {
        this.tdtext = tdtext;
    }

    public Integer getLineNo() {
        return lineNo;
    }

    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }

    public String getTextLine() {
        return textLine;
    }

    public void setTextLine(String textLine) {
        this.textLine = textLine;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
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
        if (!(object instanceof EtRfqEkpotx)) {
            return false;
        }
        EtRfqEkpotx other = (EtRfqEkpotx) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.entity.EtRfqEkpotx[ id=" + id + " ]";
    }
    
}
