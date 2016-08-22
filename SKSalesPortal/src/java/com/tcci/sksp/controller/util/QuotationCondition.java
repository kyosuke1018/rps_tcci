package com.tcci.sksp.controller.util;

import com.google.gson.annotations.Expose;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.enums.QuotationStatusEnum;
import com.tcci.sksp.entity.quotation.SkQuotationDetail;
import com.tcci.sksp.entity.quotation.SkQuotationMaster;
import java.util.Date;
import java.util.List;

/**
 *
 * @author nEO.Fu
 */
public class QuotationCondition {

    @Expose
    private String area;                        //銷售群組前兩碼
    @Expose
    private String sapid;                       //銷售群組
    @Expose
    private SkCustomer customer;                //客戶
    @Expose
    private List<String> sapidList;             //可查詢的銷售群組
    @Expose
    private SkQuotationMaster master;           //報價單主檔
    @Expose
    private SkQuotationDetail detail;           //報價單明細
    @Expose
    private Date beginDate;                     //報價單開始時間
    @Expose
    private Date endDate;                       //報價單結束時間
    @Expose
    private List<QuotationStatusEnum> statusList;   //狀態清單

    public QuotationCondition() {
        this.master = new SkQuotationMaster();
        this.detail = new SkQuotationDetail();
    }

    public SkCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(SkCustomer customer) {
        this.customer = customer;
    }

    public SkQuotationMaster getMaster() {
        if (this.master == null) {
            this.master = new SkQuotationMaster();
        }
        return master;
    }

    public void setMaster(SkQuotationMaster master) {
        this.master = master;
    }

    public SkQuotationDetail getDetail() {
        if (this.detail == null) {
            this.detail = new SkQuotationDetail();
        }
        return detail;
    }

    public void setDetail(SkQuotationDetail detail) {
        this.detail = detail;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    public List<String> getSapidList() {
        return sapidList;
    }

    public void setSapidList(List<String> sapidList) {
        this.sapidList = sapidList;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<QuotationStatusEnum> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<QuotationStatusEnum> statusList) {
        this.statusList = statusList;
    }
}
