/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.controller;

import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcReportUpload;
import com.tcci.rpt.entity.RptDataUploadRecord;
import com.tcci.rpt.entity.RptSheetUpload;

/**
 *
 * @author Jimmy.Lee
 */
public class ReportHomeVO {
    private FcCompany company;
    private FcReportUpload reportUpload;
    private RptSheetUpload rptSheetUpload;//web file
    private RptDataUploadRecord rptUploadRecord;//sap web public info

    public ReportHomeVO(FcCompany company, FcReportUpload reportUpload) {
        this.company = company;
        this.reportUpload = reportUpload;
    }
    
    public ReportHomeVO(FcCompany company, RptSheetUpload rptSheetUpload) {
        this.company = company;
        this.rptSheetUpload = rptSheetUpload;
    }
    
    // getter, setter
    public FcCompany getCompany() {
        return company;
    }

    public void setCompany(FcCompany company) {
        this.company = company;
    }

    public FcReportUpload getReportUpload() {
        return reportUpload;
    }

    public void setReportUpload(FcReportUpload reportUpload) {
        this.reportUpload = reportUpload;
    }

    public RptSheetUpload getRptSheetUpload() {
        return rptSheetUpload;
    }

    public void setRptSheetUpload(RptSheetUpload rptSheetUpload) {
        this.rptSheetUpload = rptSheetUpload;
    }

    public RptDataUploadRecord getRptUploadRecord() {
        return rptUploadRecord;
    }

    public void setRptUploadRecord(RptDataUploadRecord rptUploadRecord) {
        this.rptUploadRecord = rptUploadRecord;
    }
}
