/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.vo;

import java.util.List;

/**
 *
 * @author Kyle.Cheng
 */
public class MemberInfo {
    private Member member;
    private List<Store> facoriteStore;//喜愛店家
    private List<Product> facoritePrd;//喜愛商品
    private List<CusAddr> addrList;//常用地址
    //v1.5
    private List<Store> dealerStoreList;//台泥經銷商 賣家商店
    

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<Store> getFacoriteStore() {
        return facoriteStore;
    }

    public void setFacoriteStore(List<Store> facoriteStore) {
        this.facoriteStore = facoriteStore;
    }

    public List<Product> getFacoritePrd() {
        return facoritePrd;
    }

    public void setFacoritePrd(List<Product> facoritePrd) {
        this.facoritePrd = facoritePrd;
    }

    public List<CusAddr> getAddrList() {
        return addrList;
    }

    public void setAddrList(List<CusAddr> addrList) {
        this.addrList = addrList;
    }

    public List<Store> getDealerStoreList() {
        return dealerStoreList;
    }

    public void setDealerStoreList(List<Store> dealerStoreList) {
        this.dealerStoreList = dealerStoreList;
    }
    
    
}
