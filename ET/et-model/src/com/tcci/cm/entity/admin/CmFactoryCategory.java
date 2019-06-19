/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.entity.admin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author gilbert
 */
@Entity
@Table(name = "CM_FACTORYCATEGORY")
@NamedQueries({
    @NamedQuery(name = "CmFactoryCategory.findAll", query = "SELECT p FROM CmFactoryCategory p order by p.sortNum, p.id")})
public class CmFactoryCategory implements Serializable, Comparable {
    public final static int ID_USER_ADMIN = 1; // for 權限管理介面
    public final static int ID_PR_QUERY = 2; // for 採購查詢系統工廠分類 (公司,地區,預拌廠)
    
    public final static String TCC_CAT_CODE = "1";
    public final static String TCC_CN_CAT_CODE = "2";
    public final static String TCC_PREMIXED_CAT_CODE = "3";// 預拌廠
    public final static String TCC_HUB_CAT_CODE = "8";// 中轉站
    
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CODE")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;
    @Column(name = "SORT_NUM")
    private Integer sortNum;
    
    @OneToMany(mappedBy = "categoryId")
    private List<CmFactory> cmFactoryList;

    public CmFactoryCategory() {
    }

    public CmFactoryCategory(BigDecimal id) {
        this.id = id;
    }

    public CmFactoryCategory(BigDecimal id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    //<editor-fold defaultstate="collapsed" desc="for getter & setter">
    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof CmFactoryCategory)) {
            return false;
        }
        CmFactoryCategory other = (CmFactoryCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName()+"[ id=" + id + " ]";
    }

    public List<CmFactory> getCmFactoryList() {
        //Collections.sort(cmFactoryList, new EntityComparator<CmFactory>("code", OrderTypeEnum.ASC));
        Collections.sort(cmFactoryList);
        return cmFactoryList;
    }

    public void setCmFactoryList(List<CmFactory> cmFactoryList) {
        this.cmFactoryList = cmFactoryList;
    }

    @Override
    public int compareTo(Object o) {
        CmFactoryCategory target = (CmFactoryCategory)o;
        return this.getId().intValue() - target.getId().intValue();
    }

    public String getTitle() {
        String title = null;
        if(TCC_CAT_CODE.equalsIgnoreCase(code)){
            title = "包含台灣各水泥廠";
        }else if(TCC_CN_CAT_CODE.equalsIgnoreCase(code)){
            title = "包含大陸各水泥廠";
        }else if(TCC_PREMIXED_CAT_CODE.equalsIgnoreCase(code)){
            title = "包含台灣各水泥發貨站及預拌廠";
        }else if(TCC_HUB_CAT_CODE.equalsIgnoreCase(code)){
            title = "包含大陸中轉站";
        }
        return title;
    }
    
}
