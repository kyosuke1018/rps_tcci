package com.tcci.sksp.event.quotation;

import com.tcci.sksp.entity.quotation.SkQuotationMaster;

/**
 *
 * @author nEO
 */
public class SkQuotationMasterEvent {
    public static final int CREATE_EVENT = 0;
    public static final int EDIT_EVENT = 1;
    public static final int DESTROY_EVENT = 2;
    public static final int DOWNLOAD_EVENT = 3;
    private int action;
    private SkQuotationMaster quotationMaster;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public SkQuotationMaster getQuotationMaster() {
        return quotationMaster;
    }

    public void setQuotationMaster(SkQuotationMaster quotationMaster) {
        this.quotationMaster = quotationMaster;
    }
}
