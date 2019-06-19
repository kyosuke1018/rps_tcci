/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.vo;

import com.tcci.ec.enums.MsgTypeEnum;
import java.util.Date;

/**
 *
 * @author Kyle.Cheng
 */
public class MemberMsg {
    private Long id;
    private Long memberId;
    private Long prdId;
    private Long storeId;
    private MsgTypeEnum type;//(TYPE : A:系統意見留言、S:指定商店留言、P:指定商品留言)
    private String message;
    private String creator;
    private String createtime;
    private Long parentId;//回覆原留言ID (EC_MEMBER_MSG.ID)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public MsgTypeEnum getType() {
        return type;
    }

    public void setType(MsgTypeEnum type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
}
