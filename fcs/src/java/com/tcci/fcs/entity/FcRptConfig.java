/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.entity;

import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.enums.ReportSheetEnum;
import com.tcci.fcs.enums.RptConfigCategoryEnum;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Kyle.Cheng
 */
@Entity
@Table(name = "FC_RPT_CONFIG")
@NamedQueries({
    @NamedQuery(name = "FcRptConfig.findByComp", query = "SELECT f FROM FcRptConfig f WHERE f.group = :group"),
    @NamedQuery(name = "FcRptConfig.findByCompAndCategory", query = "SELECT f FROM FcRptConfig f WHERE f.group = :group AND f.category=:category"),
    @NamedQuery(name = "FcRptConfig.findByCompAndCategoryAndSheet", query = "SELECT f FROM FcRptConfig f WHERE f.group = :group AND f.category=:category AND f.sheet=:sheet")
})
public class FcRptConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "COMP_GROUP")
    private CompanyGroupEnum group;
    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY")
    private RptConfigCategoryEnum category;
    @Enumerated(EnumType.STRING)
    @Column(name = "SHEET")
    private ReportSheetEnum sheet;
    @Column(name = "CELL_RANGE")
    private String cellRange;
    @Column(name = "COL_CODE")
    private String colCode;
    @Column(name = "ACC_CODE")
    private String accCode;
    @Column(name = "ACC_DESC")
    private String accDesc;
    @Column(name = "ACC_EDESC")
    private String accEdesc;
    @Column(name = "START_ROW")
    private int startRow;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;

    public FcRptConfig() {
    }
    
    // getter, setter
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

    public RptConfigCategoryEnum getCategory() {
        return category;
    }

    public void setCategory(RptConfigCategoryEnum category) {
        this.category = category;
    }

    public ReportSheetEnum getSheet() {
        return sheet;
    }

    public void setSheet(ReportSheetEnum sheet) {
        this.sheet = sheet;
    }

    public String getCellRange() {
        return cellRange;
    }

    public void setCellRange(String cellRange) {
        this.cellRange = cellRange;
    }

    public String getColCode() {
        return colCode;
    }

    public void setColCode(String colCode) {
        this.colCode = colCode;
    }

    public String getAccCode() {
        return accCode;
    }

    public void setAccCode(String accCode) {
        this.accCode = accCode;
    }

    public String getAccDesc() {
        return accDesc;
    }

    public void setAccDesc(String accDesc) {
        this.accDesc = accDesc;
    }
    
    public String getAccEdesc() {
        return accEdesc;
    }

    public void setAccEdesc(String accEdesc) {
        this.accEdesc = accEdesc;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }
    
    
}
