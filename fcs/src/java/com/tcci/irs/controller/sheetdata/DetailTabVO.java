/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.irs.controller.sheetdata;

import com.tcci.irs.entity.sheetdata.ZtfiAfrcInvo;
import com.tcci.irs.entity.sheetdata.ZtfiAfrcTran;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Kyle.Cheng
 */
public class DetailTabVO {
    private String code;
    private String Name;
    private BigDecimal totalAmount;
    private List<ZtfiAfrcTran> tranDetailList;
    private List<ZtfiAfrcInvo> invoDetailList;
    private List<ZtfiAfrcTran> filterTranList;
    private List<ZtfiAfrcInvo> filterInvoList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ZtfiAfrcTran> getTranDetailList() {
        return tranDetailList;
    }

    public void setTranDetailList(List<ZtfiAfrcTran> tranDetailList) {
        this.tranDetailList = tranDetailList;
    }

    public List<ZtfiAfrcInvo> getInvoDetailList() {
        return invoDetailList;
    }

    public void setInvoDetailList(List<ZtfiAfrcInvo> invoDetailList) {
        this.invoDetailList = invoDetailList;
    }

    public List<ZtfiAfrcTran> getFilterTranList() {
        return filterTranList;
    }

    public void setFilterTranList(List<ZtfiAfrcTran> filterTranList) {
        this.filterTranList = filterTranList;
    }

    public List<ZtfiAfrcInvo> getFilterInvoList() {
        return filterInvoList;
    }

    public void setFilterInvoList(List<ZtfiAfrcInvo> filterInvoList) {
        this.filterInvoList = filterInvoList;
    }
    
}
