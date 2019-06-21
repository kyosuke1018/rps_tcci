/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.entity.admin;

import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
 * @author Peter.pan
 */
@Entity
@Table(name = "CM_FACTORYGROUP")
@NamedQueries({
    @NamedQuery(name = "CmFactorygroup.findAll", query = "SELECT p FROM CmFactorygroup p order by p.grouptype, p.sortnum, p.code, p.id"),
    @NamedQuery(name = "CmFactorygroup.findAndSort", query = "SELECT p FROM CmFactorygroup p WHERE p.disabled=0 order by p.grouptype, p.sortnum, p.code, p.id"),
    @NamedQuery(name = "CmFactorygroup.findByType", query = "SELECT p FROM CmFactorygroup p WHERE p.disabled=0 and p.grouptype=:grouptype order by p.sortnum, p.code, p.id"),
    @NamedQuery(name = "CmFactorygroup.findByTypeAndCode", query = "SELECT p FROM CmFactorygroup p WHERE p.grouptype=:grouptype and p.code=:code order by p.sortnum")
})
public class CmFactorygroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_TCC")
    @SequenceGenerator (name="SEQ_TCC", sequenceName="SEQ_TCC", allocationSize=1)    
    private Long id;
    @Size(max = 1)
    @Column(name = "GROUPTYPE")
    private String grouptype;
    @Size(max = 64)
    @Column(name = "GROUPNAME")
    private String groupname;
    @Size(max = 6)
    @Column(name = "CODE")
    private String code;
    @Size(max = 256)
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SORTNUM")
    private Integer sortnum;
    @Column(name = "DISABLED")
    private Boolean disabled;
    
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
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factorygroupId")
    private List<CmFactoryGroupR> cmFactoryGroupRList;
    
    @OneToMany(mappedBy = "factorygroupId")
    private List<CmUserFactorygroupR> cmUserFactorygroupRList;

    public CmFactorygroup() {
    }

    public CmFactorygroup(Long id) {
        this.id = id;
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getGrouptype() {
        return grouptype;
    }

    public void setGrouptype(String grouptype) {
        this.grouptype = grouptype;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
    
    public List<CmFactoryGroupR> getIcsFactoryGroupRList() {
        return cmFactoryGroupRList;
    }

    public void setIcsFactoryGroupRList(List<CmFactoryGroupR> cmFactoryGroupRList) {
        this.cmFactoryGroupRList = cmFactoryGroupRList;
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
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

    public List<CmFactoryGroupR> getCmFactoryGroupRList() {
        return cmFactoryGroupRList;
    }

    public void setCmFactoryGroupRList(List<CmFactoryGroupR> cmFactoryGroupRList) {
        this.cmFactoryGroupRList = cmFactoryGroupRList;
    }

    public List<CmUserFactorygroupR> getCmUserFactorygroupRList() {
        return cmUserFactorygroupRList;
    }

    public void setCmUserFactorygroupRList(List<CmUserFactorygroupR> cmUserFactorygroupRList) {
        this.cmUserFactorygroupRList = cmUserFactorygroupRList;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CmFactorygroup)) {
            return false;
        }
        CmFactorygroup other = (CmFactorygroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ic.entity.admin.CmFactorygroup[ id=" + id + " ]";
    }
    
}
