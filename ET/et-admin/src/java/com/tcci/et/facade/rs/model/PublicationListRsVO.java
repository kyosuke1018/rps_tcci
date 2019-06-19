/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs.model;

import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class PublicationListRsVO {
    private List<PublicationRsVO> list;
    private int totalCount;
    private int offset;
    private int limit;
    private ResponseVO res;

    public PublicationListRsVO(){};

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<PublicationRsVO> getList() {
        return list;
    }

    public void setList(List<PublicationRsVO> list) {
        this.list = list;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public ResponseVO getRes() {
        return res;
    }

    public void setRes(ResponseVO res) {
        this.res = res;
    }
}
