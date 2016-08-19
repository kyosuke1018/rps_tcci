/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.controller.queryCondition;

import java.io.Serializable;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *
 */
public class VoucherQueryCondition implements Serializable {

    private Long id;
    private String accountDate;
    private String voucherType;
    private String summary;

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

    public String getSummary() {
	return summary;
    }

    public void setSummary(String summary) {
	this.summary = summary;
    }
    
    @Override
    public String toString() {
	return ReflectionToStringBuilder
		.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
