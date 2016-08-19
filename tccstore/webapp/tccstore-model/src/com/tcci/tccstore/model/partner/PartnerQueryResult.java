/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.model.partner;

import java.util.List;

/**
 *
 * @author Jimmy.Lee
 */
public class PartnerQueryResult {

    private List<String> result; // 省, 市, 區
    private List<Partner> partners;

    // getter, setter
    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    public List<Partner> getPartners() {
        return partners;
    }

    public void setPartners(List<Partner> partners) {
        this.partners = partners;
    }

}
