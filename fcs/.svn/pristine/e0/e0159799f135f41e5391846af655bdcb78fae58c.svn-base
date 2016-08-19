/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.entity;

import com.tcci.fcs.enums.AccountTypeEnum;
import com.tcci.fcs.enums.CompanyGroupEnum;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author David.Jen
 */
@Entity
@Table(name = "IRS_ACCOUNT_NODE")
@XmlRootElement
public class AccountNode {
    //
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id; 
    
    @Enumerated(EnumType.STRING)
    @Column(name = "COMP_GROUP")
    private CompanyGroupEnum group;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "CODE")
    private String code; 
    
    @ManyToOne
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
    private AccountNode parent;
    
    @OneToMany(mappedBy="parent")
    private Collection<AccountNode> children;
    
    @Column(name = "NAME")
    private String name; 
    @Enumerated(EnumType.STRING)
    @Column(name = "RECL_ROLE")
    private AccountTypeEnum reclRole;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "RECL_ORDER")
    private int reclOrder;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AccountNode other = (AccountNode) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompanyGroupEnum getGroup() {
        return group;
    }

    public void setGroup(CompanyGroupEnum group) {
        this.group = group;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AccountNode getParent() {
        return parent;
    }

    public void setParent(AccountNode parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountTypeEnum getReclRole() {
        return reclRole;
    }

    public void setReclRole(AccountTypeEnum reclRole) {
        this.reclRole = reclRole;
    }
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getReclOrder() {
        return reclOrder;
    }

    public void setReclOrder(int reclOrder) {
        this.reclOrder = reclOrder;
    }

    public AccountNode() {
    }
    
    @Override
    public String toString() {
         return this.code +"("+ this.name +")";
    }

    public Collection<AccountNode> getChildren() {
        return children;
    }

    public void setChildren(Collection<AccountNode> children) {
        this.children = children;
    }


}
