/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.model.member;

import java.util.List;

/**
 *
 * @author Jimmy.Lee
 */
public class MemberCredit {

    private List<CreditInfo> creditList;
    private int bonus; // 紅利
    private int gold; // 金幣

    // getter, setter
    public List<CreditInfo> getCreditList() {
        return creditList;
    }

    public void setCreditList(List<CreditInfo> creditList) {
        this.creditList = creditList;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

}
