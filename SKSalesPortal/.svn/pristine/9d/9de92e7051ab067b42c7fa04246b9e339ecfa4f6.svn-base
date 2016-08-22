/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.ar;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.sksp.entity.enums.InterfaceMasterStatusEnum;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_FI_MASTER_INTERFACE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkFiMasterInterface.findAll", query = "SELECT s FROM SkFiMasterInterface s"),
    @NamedQuery(name = "SkFiMasterInterface.findById", query = "SELECT s FROM SkFiMasterInterface s WHERE s.id = :id"),
    @NamedQuery(name = "SkFiMasterInterface.findByReferenceclassname", query = "SELECT s FROM SkFiMasterInterface s WHERE s.referenceclassname = :referenceclassname"),
    @NamedQuery(name = "SkFiMasterInterface.findByReferenceid", query = "SELECT s FROM SkFiMasterInterface s WHERE s.referenceid = :referenceid"),
    @NamedQuery(name = "SkFiMasterInterface.findByStatus", query = "SELECT s FROM SkFiMasterInterface s WHERE s.status = :status"),
    @NamedQuery(name = "SkFiMasterInterface.findByReturnCode", query = "SELECT s FROM SkFiMasterInterface s WHERE s.returnCode = :returnCode"),
    @NamedQuery(name = "SkFiMasterInterface.findByReturnMessage", query = "SELECT s FROM SkFiMasterInterface s WHERE s.returnMessage = :returnMessage")})
public class SkFiMasterInterface implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "SEQ_TCC")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "TRANSACTION_NO")
    private String transactionNo;
    @Basic(optional = false)
    @Column(name = "REFERENCECLASSNAME")
    private String referenceclassname;
    @Basic(optional = false)
    @Column(name = "REFERENCEID")
    private long referenceid;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private InterfaceMasterStatusEnum status;
    @Column(name = "RETURN_CODE")
    private String returnCode;
    @Column(name = "RETURN_MESSAGE")
    private String returnMessage;
    @JoinColumn(name = "UPLOADER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser uploader;
    @Column(name = "UPLOAD_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadTimestamp;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "master")
    private Collection<SkFiDetailInterface> skFiDetailInterfaceCollection;
    @Transient
    private HashMap<String,List<Long>> generatedBy;
    public SkFiMasterInterface() {
    }

    public SkFiMasterInterface(Long id) {
        this.id = id;
    }

    public SkFiMasterInterface(String referenceclassname, long referenceid) {
        this.referenceclassname = referenceclassname;
        this.referenceid = referenceid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getReferenceclassname() {
        return referenceclassname;
    }

    public void setReferenceclassname(String referenceclassname) {
        this.referenceclassname = referenceclassname;
    }

    public long getReferenceid() {
        return referenceid;
    }

    public void setReferenceid(long referenceid) {
        this.referenceid = referenceid;
    }

    public InterfaceMasterStatusEnum getStatus() {
        return status;
    }

    public void setStatus(InterfaceMasterStatusEnum status) {
        this.status = status;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public Date getUploadTimestamp() {
        return uploadTimestamp;
    }

    public void setUploadTimestamp(Date uploadTimestamp) {
        this.uploadTimestamp = uploadTimestamp;
    }

    public TcUser getUploader() {
        return uploader;
    }

    public void setUploader(TcUser uploader) {
        this.uploader = uploader;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public HashMap<String, List<Long>> getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(HashMap<String, List<Long>> generatedBy) {
        this.generatedBy = generatedBy;
    }

    @XmlTransient
    public Collection<SkFiDetailInterface> getSkFiDetailInterfaceCollection() {
        return skFiDetailInterfaceCollection;
    }

    public void setSkFiDetailInterfaceCollection(Collection<SkFiDetailInterface> skFiDetailInterfaceCollection) {
        this.skFiDetailInterfaceCollection = skFiDetailInterfaceCollection;
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
        if (!(object instanceof SkFiMasterInterface)) {
            return false;
        }
        SkFiMasterInterface other = (SkFiMasterInterface) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.ar.SkFiMasterInterface[ id=" + id + " ]";
    }
}
