/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.controller;

import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.enums.ReportSheetEnum;
import com.tcci.rpt.entity.RptDValue;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kyle.Cheng
 */
//D0202 D0204
public class ReportCompareVO {
    private FcCompany coid1;
    private FcCompany coid2;
    private BigDecimal sum1 = BigDecimal.ZERO; // 資產加總(D0202)
    private BigDecimal sum2 = BigDecimal.ZERO; // 負債加總(D0204)
    private boolean modified = false;
    private String note;
    private Date modifytimestamp;
    // internal data
    private Map<String, RptDValue> values;
    
    public ReportCompareVO(FcCompany coid1, FcCompany coid2) {
        this.coid1 = coid1;
        this.coid2 = coid2;
        values = new HashMap<>();
    }
    
    public void addValue(RptDValue rv) {
        values.put(rv.getCode(), rv);
        BigDecimal amount = rv.getAmount();
        if (amount != null) {
            String sheet = rv.getSheet();
            if (ReportSheetEnum.D0202.getCode().equals(sheet)) {
                sum1 = sum1.add(amount);
            }
            if (ReportSheetEnum.D0206.getCode().equals(sheet)) {
                sum2 = sum2.add(amount);
            }
        }
//        if (rv.isModified()) {
//            modified = true;
//        }
    }
    
    public BigDecimal getAmount(String code) {
        RptDValue rv = values.get(code);
        return (null==rv) ? null : rv.getAmount();
    }
    
    public BigDecimal getAmountXls(String code) {
        RptDValue rv = values.get(code);
        return (null==rv) ? null : rv.getAmountXls();
    }
    
    public BigDecimal getDiff() {
        return sum1.subtract(sum2);
    }
    
    // getter, setter
    public FcCompany getCoid1() {
        return coid1;
    }

    public void setCoid1(FcCompany coid1) {
        this.coid1 = coid1;
    }

    public FcCompany getCoid2() {
        return coid2;
    }

    public void setCoid2(FcCompany coid2) {
        this.coid2 = coid2;
    }

    public BigDecimal getSum1() {
        return sum1;
    }

    public void setSum1(BigDecimal sum1) {
        this.sum1 = sum1;
    }

    public BigDecimal getSum2() {
        return sum2;
    }

    public void setSum2(BigDecimal sum2) {
        this.sum2 = sum2;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
    }
    
}
