/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Mendel.lee
 */
public class VoucherMaster {
    
    private Long id;
    private String accountDate;
    private String voucherType;
    private String voucherStatus;
    private String summary;
    private BigDecimal debitBalance;
    private BigDecimal creditBalance;
    private BigDecimal differBalance;
    private String postingPerson;
    private Date postingDate;
    
    private List<VoucherDetail> voucherDetailList = new ArrayList<VoucherDetail>();

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getAccountDate() {
	return accountDate;
    }

    public void setAccountDate(String accountDate) {
	this.accountDate = accountDate;
    }

    public String getVoucherType() {
	return voucherType;
    }

    public void setVoucherType(String voucherType) {
	this.voucherType = voucherType;
    }

    public String getVoucherStatus() {
	return voucherStatus;
    }

    public void setVoucherStatus(String voucherStatus) {
	this.voucherStatus = voucherStatus;
    }

    public String getSummary() {
	return summary;
    }

    public void setSummary(String summary) {
	this.summary = summary;
    }

    public BigDecimal getDebitBalance() {
	return debitBalance;
    }

    public void setDebitBalance(BigDecimal debitBalance) {
	this.debitBalance = debitBalance;
    }

    public BigDecimal getCreditBalance() {
	return creditBalance;
    }

    public void setCreditBalance(BigDecimal creditBalance) {
	this.creditBalance = creditBalance;
    }

    public BigDecimal getDifferBalance() {
	return differBalance;
    }

    public void setDifferBalance(BigDecimal differBalance) {
	this.differBalance = differBalance;
    }

    public List<VoucherDetail> getVoucherDetailList() {
	return voucherDetailList;
    }

    public void setVoucherDetailList(List<VoucherDetail> voucherDetailList) {
	this.voucherDetailList = voucherDetailList;
    }

    public String getPostingPerson() {
	return postingPerson;
    }

    public void setPostingPerson(String postingPerson) {
	this.postingPerson = postingPerson;
    }

    public Date getPostingDate() {
	return postingDate;
    }

    public void setPostingDate(Date postingDate) {
	this.postingDate = postingDate;
    }
}
