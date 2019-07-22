/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rs;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author penpl
 */
public class RfsQuoteRsVO extends BaseResponseVO implements Serializable {
    private TenderBaseRsVO tender;
    private VenderRsVO vender;
    private QuoteRsVO quote;
    private List<QuoteItemRsVO> rfqQuoteItems;
    private List<FileRsVO> docs;

    public TenderBaseRsVO getTender() {
        return tender;
    }

    public void setTender(TenderBaseRsVO tender) {
        this.tender = tender;
    }

    public VenderRsVO getVender() {
        return vender;
    }

    public void setVender(VenderRsVO vender) {
        this.vender = vender;
    }

    public QuoteRsVO getQuote() {
        return quote;
    }

    public void setQuote(QuoteRsVO quote) {
        this.quote = quote;
    }

    public List<QuoteItemRsVO> getRfqQuoteItems() {
        return rfqQuoteItems;
    }

    public void setRfqQuoteItems(List<QuoteItemRsVO> rfqQuoteItems) {
        this.rfqQuoteItems = rfqQuoteItems;
    }

    public List<FileRsVO> getDocs() {
        return docs;
    }

    public void setDocs(List<FileRsVO> docs) {
        this.docs = docs;
    }
    
    
}
