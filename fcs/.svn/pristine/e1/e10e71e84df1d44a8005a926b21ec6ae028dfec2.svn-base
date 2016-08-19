package com.tcci.fc.entity.org;

import com.tcci.fcs.entity.FcUserCompGroupR;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
import org.apache.commons.collections.CollectionUtils;

/**
 *
 * @author Kyle.Cheng
 */
@Entity
@Table(name = "TC_USER")
@NamedQueries(value = {
    @NamedQuery(name = "TcUser.findAll", query = "SELECT t FROM TcUser t")})
public class TcUser implements Serializable, TcPrincipal {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "LOGIN_ACCOUNT")
    private String loginAccount;
    @NotNull
    @Column(name = "CREATETIMESTAMP")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "userId")
    private Collection<TcUsergroup> tcUsergroupCollection;
    @Size(max = 20)
    @Column(name = "EMP_ID")
    private String empId;
    @Size(max = 60)
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "DISABLED")
    private Boolean disabled;
    @Size(max = 20)
    @Column(name = "CNAME")
    private String cname;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tcUser")
    private List<FcUserCompGroupR> compGroupList;

    public TcUser() {
    }

    public TcUser(Long id) {
        this.id = id;
    }

    public TcUser(Long id, String loginAccount) {
        this.id = id;
        this.loginAccount = loginAccount;
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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

    public List<FcUserCompGroupR> getCompGroupList() {
        return compGroupList;
    }

    public void setCompGroupList(List<FcUserCompGroupR> compGroupList) {
        this.compGroupList = compGroupList;
    }
    
    public String getCompGroups() {
        String result = "";
        if (CollectionUtils.isNotEmpty(this.compGroupList)) {
            int i = 1;
            for(FcUserCompGroupR compGroupR : this.compGroupList){
                result = result.concat(compGroupR.getGroup().getCode());
                if (i < this.compGroupList.size()) {
                    result = result.concat(", ");
                }
                i++;
            }
        }
        return result;
    }
    //</editor-fold>

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
}

