/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.content;

import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.entity.content.TcFvvault;
import com.tcci.fc.facade.AbstractFacade;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
public class TcFvvaultFacade extends AbstractFacade<TcFvvault> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public TcFvvaultFacade() {
        super(TcFvvault.class);
    }

    public List<TcFvvault> getTcFvvaultByDomain(TcDomain tcDomain) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TcFvvault> cq = cb.createQuery(TcFvvault.class);
        Root<TcFvvault> from = cq.from(TcFvvault.class);

        List<Predicate> predicateList = new ArrayList<Predicate>();

        Predicate p1;
        p1 = cb.equal(from.<TcDomain>get("domain"), tcDomain);
        predicateList.add(p1);

        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        cq.where(predicates);
        return em.createQuery(cq).getResultList();
    }

    public TcFvvault getTcFvvaultByLocalhost(TcDomain tcDomain) {
        List<TcFvvault> list = getTcFvvaultByDomain(tcDomain);
        TcFvvault tcFvvault = null;
        for (int i = 0; i < list.size(); i++) {
            tcFvvault = list.get(i);
            String hostName = tcFvvault.getHostname();
            if ("localhost".equalsIgnoreCase(hostName)) {
                break;
            }
        }
        return tcFvvault;
    }
}
