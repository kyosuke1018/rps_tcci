/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.vo;

import com.tcci.sksp.entity.quotation.SkQuotationDetail;
import com.tcci.sksp.entity.quotation.SkQuotationGift;
import com.tcci.sksp.entity.quotation.SkQuotationMaster;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Neo.Fu
 */
public class QuotationMasterVO {

    private boolean selected;
    private SkQuotationMaster master;
    private List<QuotationDetailVO> detailVOList;

    public QuotationMasterVO(SkQuotationMaster master) {
        this.master = master;
        this.detailVOList = new ArrayList();
        List<QuotationDetailVO> giftList = new ArrayList();
        for (SkQuotationDetail detail : master.getDetailCollection()) {
            QuotationDetailVO detailVO = new QuotationDetailVO(detail);
            this.detailVOList.add(detailVO);
            for (SkQuotationGift gift : detail.getGiftList()) {
                giftList.add(new QuotationDetailVO(gift));
            }
        }
        this.detailVOList.addAll(giftList);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public SkQuotationMaster getMaster() {
        return master;
    }

    public List<QuotationDetailVO> getDetailVOList() {
        return detailVOList;
    }
}
