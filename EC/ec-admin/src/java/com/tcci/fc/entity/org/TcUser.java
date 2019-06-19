package com.tcci.fc.entity.org;

import com.tcci.cm.entity.admin.CmUserFactorygroupR;
import com.tcci.cm.entity.admin.CmUserOrg;
import com.tcci.cm.entity.admin.CmUserfactory;
import com.tcci.cm.entity.admin.CmUsercompany;
import com.tcci.cm.model.interfaces.IOperator;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gilbert.Lin
 */
@Entity
@Table(name = "TC_USER")
@NamedQueries({
    @NamedQuery(name = "TcUser.findAll", query = "SELECT t FROM TcUser t")})
@Cacheable(value=false)
public class TcUser implements Serializable, TcPrincipal, IOperator {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_TCC")
    @SequenceGenerator (name="SEQ_TCC", sequenceName="SEQ_TCC", allocationSize=1)
    private Long id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "LOGIN_ACCOUNT")
    private String loginAccount;
    
    @Column(name = "EMP_ID")
    private String empId;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CNAME")
    private String cname;
    
    @Column(name = "DISABLED")
    private Boolean disabled;
    
    @Column(name = "SEND_EMAIL")
    private Boolean sendEmail;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<TcUsergroup> tcUsergroupCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<CmUserOrg> cmUserOrgCollection;
    
    @Column(name = "ORG_ID")
    private Long orgId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private List<CmUserfactory> cmUserfactoryList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private List<CmUsercompany> cmUsercompanyList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private List<CmUserFactorygroupR> cmUserFactorygroupRList;
    
    @Column(name = "MEMBER_ID")
    private Long memberId;

    public TcUser() {
    }

    public TcUser(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String getLabel() {
        return getDisplayIdentifier();
    }
    
    @Override
    public String getDisplayIdentifier() {
        StringBuilder displayIdentifier = new StringBuilder("");
        boolean nameIsEmpty = true;
        if (getCname() != null) {
            nameIsEmpty = false;
            displayIdentifier.append(getCname());
        }
        if (!nameIsEmpty) {
            displayIdentifier.append("(").append(getLoginAccount()).append(")");
        } else {
            displayIdentifier.append(getLoginAccount());
        }
        if (null!=getDisabled() && getDisabled()) {
            displayIdentifier.append("{Disabled}");
        }
        return displayIdentifier.toString();
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TcUser)) {
            return false;
        }
        TcUser other = (TcUser) object;

        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + ":" + getId();
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public List<CmUserfactory> getCmUserfactoryList() {
        return cmUserfactoryList;
    }

    public void setCmUserfactoryList(List<CmUserfactory> cmUserfactoryList) {
        this.cmUserfactoryList = cmUserfactoryList;
    }

    public List<CmUsercompany> getCmUsercompanyList() {
        return cmUsercompanyList;
    }

    public void setCmUsercompanyList(List<CmUsercompany> cmUsercompanyList) {
        this.cmUsercompanyList = cmUsercompanyList;
    }

    public List<CmUserFactorygroupR> getCmUserFactorygroupRList() {
        return cmUserFactorygroupRList;
    }

    public void setCmUserFactorygroupRList(List<CmUserFactorygroupR> cmUserFactorygroupRList) {
        this.cmUserFactorygroupRList = cmUserFactorygroupRList;
    }

    public Collection<CmUserOrg> getCmUserOrgCollection() {
        return cmUserOrgCollection;
    }

    public void setCmUserOrgCollection(Collection<CmUserOrg> cmUserOrgCollection) {
        this.cmUserOrgCollection = cmUserOrgCollection;
    }

    public TcUser(Long id, String loginAccount) {
        this.id = id;
        this.loginAccount = loginAccount;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }
    
    public Collection<TcUsergroup> getTcUsergroupCollection() {
        return tcUsergroupCollection;
    }

    public void setTcUsergroupCollection(Collection<TcUsergroup> tcUsergroupCollection) {
        this.tcUsergroupCollection = tcUsergroupCollection;
    }
   
    /**
     * @return the empId
     */
    public String getEmpId() {
        return empId;
    }

    /**
     * @param empId the empId to set
     */
    public void setEmpId(String empId) {
        this.empId = empId;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }  

    @Override
    public String getName() {
        return getCname();
    }

    @Override
    public void setName(String name) {
        cname = name;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
    
    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }
    
    //</editor-fold>
        
}
