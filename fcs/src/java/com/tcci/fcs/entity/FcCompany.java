/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.entity;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.enums.CompanyGroupEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "FC_COMPANY")
@NamedQueries({
    @NamedQuery(name = "FcCompany.findByCode", query = "SELECT c FROM FcCompany c WHERE c.code=:code"),
    @NamedQuery(name = "FcCompany.findAll", query = "SELECT c FROM FcCompany c ORDER BY c.code"),
    @NamedQuery(name = "FcCompany.findAllActive", query = "SELECT c FROM FcCompany c WHERE c.active=true ORDER BY c.code"),
    @NamedQuery(name = "FcCompany.findAllActiveNonsap", query = "SELECT c FROM FcCompany c WHERE c.active=true AND c.group.code = :group AND c.nonSap=true ORDER BY c.code")
})
public class FcCompany implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "CODE")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;
    @JoinColumn(name = "UPLOADER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser uploader;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVE")
    private boolean active = true;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fcCompany")
    private List<FcUploaderR> uploaderList;
    @Column(name = "SORT")
    private int sort;
//    @Enumerated(EnumType.STRING)
//    @Column(name = "COMP_GROUP")
//    private CompanyGroupEnum group;
    @JoinColumn(name = "COMP_GROUP", referencedColumnName = "CODE")
    @ManyToOne
    private FcCompGroup group;
    @JoinColumn(name = "CURRENCY", referencedColumnName = "ID")
    @ManyToOne
    private FcCurrency currency;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COMBINE_INCOME")
    private boolean consolidationRevenue = true;//20151013 與關係人對帳等其他系統共用公司主檔 以此欄位區別須公告營收的公司
    @Size(max = 20)
    @Column(name = "ABBREVIATION")
    private String abbreviation;//公司簡稱 提供報表呈現
    @Column(name = "ENAME")
    private String ename;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NON_SAP")
    private boolean nonSap = true;//20151123 非SAP上傳公司flag
    @Column(name = "UN_DO")
    private boolean undo = false;//20160314無需處理的公司flag
    @NotNull
    @Column(name = "CONSOLIDATION_RPT")
    private boolean consolidationRpt = true;//20160204 與其他系統共用公司主檔 以此欄位區別須上傳合併報表的公司, 20160317合併報表公司列表顯示的公司
    @Column(name = "CONSOLIDATION_RPT_UPLOAD")
    private boolean consolidationRptUpload = true;//20160317 合併報表 web上傳的公司
    @Column(name = "VIRTUAL")
    private boolean virtual = true;//20160323 虛擬:非實際交易 對帳的公司
    
    public FcCompany() {
        this.uploaderList = new ArrayList();
    }

    public FcCompany(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public FcCompany(String code, String name, String abbreviation, int sort) {
        this.code = code;
        this.name = name;
	this.abbreviation = abbreviation;
	this.sort = sort;
    }
    
    public FcCompany(Long id) {
        this.id = id;
    }

    public FcCompany(Long id, String code, String name, boolean active) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.active = active;
    }
    
    public FcCompany(String code, String name, boolean active, TcUser uploader, int sort, FcCompGroup group) {
        this.code = code;
        this.name = name;
        this.active = active;
        this.uploader = uploader;
        this.sort = sort;
        this.group = group;
//        this.currency = currency;
    }
    
    public void init(FcCompany comp) {
        if (null == comp) {
            id = null;
            code = null;
            name = null;
            uploader = null;
            active = true;
            sort = 0;
        } else {
            id = comp.id;
            code = comp.code;
            name = comp.name;
            uploader = comp.uploader;
            active = comp.active;
            sort = comp.sort;
        }
    }

    // getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public TcUser getUploader() {
        return uploader;
    }

    public void setUploader(TcUser uploader) {
        this.uploader = uploader;
    }
    
    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    @XmlTransient
    public List<FcUploaderR> getUploaderList() {
        return uploaderList;
    }

    public void setUploaderList(List<FcUploaderR> uploaderList) {
        this.uploaderList = uploaderList;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public FcCompGroup getGroup() {
        return group;
    }

    public void setGroup(FcCompGroup group) {
        this.group = group;
    }

    public FcCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(FcCurrency currency) {
        this.currency = currency;
    }

    public boolean isConsolidationRevenue() {
        return consolidationRevenue;
    }

    public void setConsolidationRevenue(boolean consolidationRevenue) {
        this.consolidationRevenue = consolidationRevenue;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public boolean isNonSap() {
        return nonSap;
    }

    public void setNonSap(boolean nonSap) {
        this.nonSap = nonSap;
    }

    public boolean isConsolidationRpt() {
        return consolidationRpt;
    }

    public void setConsolidationRpt(boolean consolidationRpt) {
        this.consolidationRpt = consolidationRpt;
    }

    public boolean isConsolidationRptUpload() {
        return consolidationRptUpload;
    }

    public void setConsolidationRptUpload(boolean consolidationRptUpload) {
        this.consolidationRptUpload = consolidationRptUpload;
    }

    public boolean isUndo() {
        return undo;
    }

    public void setUndo(boolean undo) {
        this.undo = undo;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
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
        if (!(object instanceof FcCompany)) {
            return false;
        }
        FcCompany other = (FcCompany) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "com.tcci.fcs.entity.FcCompany[ id=" + id + " ]";
         String name_this = StringUtils.isBlank(this.abbreviation)?"":this.abbreviation;
         return this.code +"("+ name_this +")";
    }

}
