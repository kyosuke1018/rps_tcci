/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.entity;

import com.tcci.et.enums.FormStatusEnum;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcUser;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "ET_MEMBER_FORM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtMemberForm.findAll", query = "SELECT e FROM EtMemberForm e")
    , @NamedQuery(name = "EtMemberForm.findById", query = "SELECT e FROM EtMemberForm e WHERE e.id = :id")
    , @NamedQuery(name = "EtMemberForm.findByMember", query = "SELECT e FROM EtMemberForm e WHERE e.memberId = :memberId")
, @NamedQuery(name = "EtMemberForm.findByFactory", query = "SELECT e FROM EtMemberForm e WHERE e.factoryId = :factoryId")
})
public class EtMemberForm implements Serializable, Persistable, ContentHolder {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_MEMBER_FORM", sequenceName = "SEQ_MEMBER_FORM", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MEMBER_FORM")        
    private Long id;
    @Column(name = "MEMBER_ID")
    private Long memberId;
    @Column(name = "COMPANY_ID")
    private Long companyId;
    @Column(name = "FACTORY_ID")
    private Long factoryId;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "MANDT")
    private String mandt;
    @Column(name = "LIFNR_UI")
    private String venderCode;
    @Column(name = "APPLY_VENDER_CODE")
    private String applyVenderCode;
    @Column(name = "APPLY_VENDER_NAME")
    private String applyVenderName;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private FormStatusEnum status;
    @JoinColumn(name = "PROCESS", referencedColumnName = "ID")
    @ManyToOne
    private TcProcess process;//流程
    
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

    public EtMemberForm() {
    }

    public EtMemberForm(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApplyVenderCode() {
        return applyVenderCode;
    }

    public void setApplyVenderCode(String applyVenderCode) {
        this.applyVenderCode = applyVenderCode;
    }

    public String getApplyVenderName() {
        return applyVenderName;
    }

    public void setApplyVenderName(String applyVenderName) {
        this.applyVenderName = applyVenderName;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getVenderCode() {
        return venderCode;
    }

    public void setVenderCode(String venderCode) {
        this.venderCode = venderCode;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
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

    public FormStatusEnum getStatus() {
        return status;
    }

    public void setStatus(FormStatusEnum status) {
        this.status = status;
    }

    public TcProcess getProcess() {
        return process;
    }

    public void setProcess(TcProcess process) {
        this.process = process;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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
        if (!(object instanceof EtMemberForm)) {
            return false;
        }
        EtMemberForm other = (EtMemberForm) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EtMemberForm[ id=" + id + " ]";
    }
    
}
