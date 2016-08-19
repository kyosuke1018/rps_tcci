/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.vehicle;

import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcVehiclePreference;
import com.tcci.tccstore.entity.EcVehiclePreferencePK;
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
public class EcVehicleFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public void addPreference(EcMember ecMember, String vehicle) {
        EcVehiclePreferencePK pk = new EcVehiclePreferencePK(ecMember.getId(), vehicle);
        EcVehiclePreference entity = em.find(EcVehiclePreference.class, pk);
        if (null == entity) {
            entity = new EcVehiclePreference(pk);
            em.persist(entity);
        }
    }

    public void removePreference(EcMember ecMember, String vehicle) {
        EcVehiclePreferencePK pk = new EcVehiclePreferencePK(ecMember.getId(), vehicle);
        EcVehiclePreference entity = em.find(EcVehiclePreference.class, pk);
        if (entity != null) {
            em.remove(entity);
        }
    }

    public List<String> findByMember(EcMember ecMember) {
        Query q = em.createNamedQuery("EcVehiclePreference.findByMember");
        q.setParameter("ecMember", ecMember);
        return q.getResultList();
    }

}
