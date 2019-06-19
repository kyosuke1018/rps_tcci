/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.vo;

import com.tcci.ec.enums.MsgTypeEnum;
import java.util.List;

/**
 *
 * @author Kyle.Cheng
 */
public class MsgBoard {
    private String title;
    private Long prdId;
    private Long storeId;
    private String storeTitle;
    private MsgTypeEnum type;//(TYPE : A:系統意見留言、S:指定商店留言、P:指定商品留言)
    private Long parentId;
    List<MemberMsg> msgList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<MemberMsg> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<MemberMsg> msgList) {
        this.msgList = msgList;
    }

    public String getStoreTitle() {
        return storeTitle;
    }

    public void setStoreTitle(String storeTitle) {
        this.storeTitle = storeTitle;
    }
    
    
}
