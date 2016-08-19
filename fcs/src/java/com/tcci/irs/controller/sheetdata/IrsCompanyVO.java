/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.irs.controller.sheetdata;

import com.tcci.fcs.entity.FcCompany;
import com.tcci.irs.entity.IrsCompanyNoTrans;
import com.tcci.irs.entity.IrsTranUploadRecord;
import com.tcci.irs.entity.attachment.IrsReportUpload;

/**
 *
 * @author Kyle.Cheng
 */
public class IrsCompanyVO {
    private FcCompany company;
    private IrsReportUpload reportUpload;
    private boolean allEdit;
    private IrsTranUploadRecord uploadRecord;
    private IrsCompanyNoTrans noTrans;

    public FcCompany getCompany() {
        return company;
    }

    public void setCompany(FcCompany company) {
        this.company = company;
    }

    public IrsReportUpload getReportUpload() {
	return reportUpload;
    }

    public void setReportUpload(IrsReportUpload reportUpload) {
	this.reportUpload = reportUpload;
    }

    public boolean isAllEdit() {
        return allEdit;
    }

    public void setAllEdit(boolean allEdit) {
        this.allEdit = allEdit;
    }

    public IrsTranUploadRecord getUploadRecord() {
        return uploadRecord;
    }

    public void setUploadRecord(IrsTranUploadRecord uploadRecord) {
        this.uploadRecord = uploadRecord;
    }

    public IrsCompanyNoTrans getNoTrans() {
        return noTrans;
    }

    public void setNoTrans(IrsCompanyNoTrans noTrans) {
        this.noTrans = noTrans;
    }
}
