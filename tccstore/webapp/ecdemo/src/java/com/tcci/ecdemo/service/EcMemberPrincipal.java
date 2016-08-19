/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.service;

import com.tcci.ecdemo.entity.EcMember;
import java.security.Principal;

/**
 *
 * @author Jimmy.Lee
 */
public class EcMemberPrincipal implements Principal {

    private EcMember ecMember;

    public EcMemberPrincipal(EcMember ecMember) {
        this.ecMember = ecMember;
    }

    @Override
    public String getName() {
        return ecMember.getLoginAccount();
    }

    public EcMember getEcMember() {
        return ecMember;
    }

}
