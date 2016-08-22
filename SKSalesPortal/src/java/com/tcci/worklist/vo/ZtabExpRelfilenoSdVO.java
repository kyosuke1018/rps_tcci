package com.tcci.worklist.vo;

import com.tcci.worklist.entity.datawarehouse.ZtabExpRelfilenoSd;

/**
 *
 * @author nEO.Fu
 */
public class ZtabExpRelfilenoSdVO {

    private ZtabExpRelfilenoSd ztabExpRelfilenoSd;
    private boolean reviewable = false;
    private boolean selected;
    private boolean commentAB; //A/B級客戶
    private boolean commentVQ; //量/價提升

    public ZtabExpRelfilenoSdVO(ZtabExpRelfilenoSd ztabExpRelfilenoSd) {
        this.ztabExpRelfilenoSd = ztabExpRelfilenoSd;
    }

    public boolean isCommentAB() {
        return commentAB;
    }

    public void setCommentAB(boolean commentAB) {
        this.commentAB = commentAB;
    }

    public boolean isCommentVQ() {
        return commentVQ;
    }

    public void setCommentVQ(boolean commentVQ) {
        this.commentVQ = commentVQ;
    }

    public ZtabExpRelfilenoSd getZtabExpRelfilenoSd() {
        return ztabExpRelfilenoSd;
    }

    public void setZtabExpRelfilenoSd(ZtabExpRelfilenoSd ztabExpRelfilenoSd) {
        this.ztabExpRelfilenoSd = ztabExpRelfilenoSd;
    }

    public boolean isReviewable() {
        return reviewable;
    }

    public void setReviewable(boolean reviewable) {
        this.reviewable = reviewable;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
