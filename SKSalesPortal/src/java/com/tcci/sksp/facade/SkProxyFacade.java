package com.tcci.sksp.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.SkProxy;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author nEO.Fu
 */
@Stateless
public class SkProxyFacade extends AbstractFacade<SkProxy> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkProxyFacade() {
        super(SkProxy.class);
    }

    public List<SkProxy> findByProxy(TcUser proxy) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SkProxy> cq = builder.createQuery(SkProxy.class);
        Root<SkProxy> root = cq.from(SkProxy.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p = builder.equal(root.get("proxy").as(TcUser.class), proxy);
        predicateList.add(p);
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        return getEntityManager().createQuery(cq).getResultList();
    }
}
