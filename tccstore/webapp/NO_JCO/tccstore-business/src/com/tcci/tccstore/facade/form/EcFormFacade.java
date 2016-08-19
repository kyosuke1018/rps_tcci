/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.form;

import com.tcci.tccstore.entity.EcForm;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.enums.FormTypeEnum;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Named
@Stateless
public class EcFormFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcForm createFormMember(String name, String email, String phone) {
        EcForm ecForm = new EcForm(FormTypeEnum.MEMBER, name, email, phone, null);
        em.persist(ecForm);
        return ecForm;
    }

    public EcForm createFormCustomer(String name, String email, String phone, EcMember ecMember, String province) {
        EcForm ecForm = new EcForm(FormTypeEnum.CUSTOMER, name, email, phone, ecMember);
        ecForm.setProvince(province);
        em.persist(ecForm);
        return ecForm;
    }
    
    public String findContact(String province) {
        String sql = "SELECT SALES_CONTACT FROM EC_DIVISIONS_CN WHERE NAME=#province";
        Query q = em.createNativeQuery(sql);
        q.setParameter("province", province);
        List list = q.getResultList();
        return list.isEmpty() ? "andy.sung@taiwancement.com" : (String) list.get(0);
    }

}
