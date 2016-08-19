/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.model.partner;

import com.tcci.tccstore.model.member.Member;
import java.util.Date;

/**
 *
 * @author Neo.Fu
 */
public class PartnerComment {
    private Long id;
    private double rate;
    private String message;
    private Date createtime;
    private Partner partner;
    private Member member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    
    public String toString() {
        return "PartnerComment["+"\n"
                +"id="+this.id+"\n"
                +"rate="+this.rate+"\n"
                +"message="+this.message+"\n"
                +"createtime="+this.createtime+"\n"
                +"partner="+(this.partner!=null?"partner[id="+this.partner.getId()+"]":"null")+"\n"
                +"partner="+(this.member!=null?"member[account="+this.member.getLoginAccount()+"]":"null")+"\n"
                +"]";
    }
}
