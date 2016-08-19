/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.util;

import com.tcci.tccstore.entity.EcPartner;
import com.tcci.tccstore.enums.CommentStatusEnum;
import java.util.Date;

/**
 *
 * @author Neo.Fu
 */
public class PartnerCommentFilter {

    private Boolean active;
    private EcPartner partner;
    private Date createtimeBegin;
    private Date createtimeEnd;
    private CommentStatusEnum status;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public EcPartner getPartner() {
        return partner;
    }

    public void setPartner(EcPartner partner) {
        this.partner = partner;
    }

    public Date getCreatetimeBegin() {
        return createtimeBegin;
    }

    public void setCreatetimeBegin(Date createtimeBegin) {
        this.createtimeBegin = createtimeBegin;
    }

    public Date getCreatetimeEnd() {
        return createtimeEnd;
    }

    public void setCreatetimeEnd(Date createtimeEnd) {
        this.createtimeEnd = createtimeEnd;
    }

    public CommentStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CommentStatusEnum status) {
        this.status = status;
    }

}
