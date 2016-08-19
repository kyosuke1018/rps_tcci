/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.facade.member;

import com.tcci.ecdemo.entity.EcMember;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class EcMemberFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcMember findActiveByLoginAccount(String loginAccount) {
        Query q = em.createNamedQuery("EcMember.findActiveByLoginAccount");
        q.setParameter("loginAccount", loginAccount);
        List<EcMember> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

}
