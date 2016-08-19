/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.controller;

import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcReportValue;
import com.tcci.fcs.enums.ReportSheetEnum;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
//D0206 D0208
public class ReportCompareVO {
    private FcCompany coid1;
    private FcCompany coid2;
    private BigDecimal sum1 = BigDecimal.ZERO; // 收入加總(D0206)
    private BigDecimal sum2 = BigDecimal.ZERO; // 支出加總(D0208)
    private boolean modified = false;
    private String note;
    private Date modifytimestamp;
    
    // internal data
    private Map<String, FcReportValue> values;
    
    public ReportCompareVO(FcCompany coid1, FcCompany coid2) {
        this.coid1 = coid1;
        this.coid2 = coid2;
        values = new HashMap<String, FcReportValue>();
    }
    
    public void addValue(FcReportValue rv) {
        values.put(rv.getCode(), rv);
        BigDecimal amount = rv.getAmount();
        if (amount != null) {
            String sheet = rv.getSheet();
            if (ReportSheetEnum.D0206.getCode().equals(sheet)) {
                sum1 = sum1.add(amount);
            }
            if (ReportSheetEnum.D0208.getCode().equals(sheet)) {
                sum2 = sum2.add(amount);
            }
        }
        if (rv.isModified()) {
            modified = true;
        }
    }
    
    public BigDecimal getAmount(String code) {
        FcReportValue rv = values.get(code);
        return (null==rv) ? null : rv.getAmount();
    }
    
    public BigDecimal getAmountOrig(String code) {
        FcReportValue rv = values.get(code);
        return (null==rv) ? null : rv.getAmountOrig();
    }
    
    public BigDecimal getAmountXls(String code) {
        FcReportValue rv = values.get(code);
        return (null==rv) ? null : rv.getAmountXls();
    }
    
    public boolean isModified(String code) {
        FcReportValue rv = values.get(code);
        return (null==rv) ? false : rv.isModified();
    }
    
    public BigDecimal getDiff() {
        return sum1.subtract(sum2);
    }
    
    public String getFlag() {
        if (!StringUtils.isBlank(note) || modified) {
            return "flag_warn.png";
        }
        if (sum1.compareTo(sum2)==0) {
            return "flag_ok.png";
        } else {
            return "flag_ng.png";
        }
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
