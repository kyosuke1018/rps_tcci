package com.tcci.sksp.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.ar.SkAdvancePayment;
import com.tcci.sksp.entity.ar.SkArRemitMaster;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
public class SkAdvancePaymentFacade extends AbstractFacade<SkAdvancePayment> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkAdvancePaymentFacade() {
        super(SkAdvancePayment.class);
    }

    public List<SkAdvancePayment> findByCriteria(AdvancePaymentFilter filter) {
        List<SkAdvancePayment> advancePayments = null;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(SkAdvancePayment.class);
        Root<SkAdvancePayment> root = cq.from(SkAdvancePayment.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (!StringUtils.isEmpty(filter.getRemitNumber())) {
            Predicate p = cb.equal(root.get("arRemitMaster").get("id"), filter.getRemitNumber());
            predicateList.add(p);
        }
        if (filter.getModifier() != null) {
            Predicate p = cb.equal(root.get("modifier").as(TcUser.class), filter.getModifier());
            predicateList.add(p);
        }
        if (filter.getBeginModifytimestamp() != null && filter.getEndModifytimestamp() == null) {
            Predicate p = cb.greaterThanOrEqualTo(root.get("modifytimestamp").as(Date.class), filter.getBeginModifytimestamp());
            predicateList.add(p);
        } else if (filter.getBeginModifytimestamp() == null && filter.getEndModifytimestamp() != null) {
            Predicate p = cb.lessThan(root.get("modifytimestamp").as(Date.class), filter.getEndModifytimestamp());
            predicateList.add(p);
        } else if (filter.getBeginModifytimestamp() != null && filter.getEndModifytimestamp() != null) {
            Predicate p = cb.between(root.get("modifytimestamp").as(Date.class), filter.getBeginModifytimestamp(), filter.getEndModifytimestamp());
            predicateList.add(p);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        advancePayments = getEntityManager().createQuery(cq).getResultList();
        return advancePayments;
    }

    public SkAdvancePayment findByRemitMaster(SkArRemitMaster remitMaster) {
        SkAdvancePayment advancePayment = null;
        if (remitMaster != null) {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery(SkAdvancePayment.class);
            Root<SkAdvancePayment> root = cq.from(SkAdvancePayment.class);
            Predicate predicate = cb.equal(root.get("arRemitMaster").as(SkArRemitMaster.class), remitMaster);
            cq.where(predicate);
            try {
                advancePayment = (SkAdvancePayment) getEntityManager().createQuery(cq).getSingleResult();
            } catch (NoResultException nre) {
            }
        }
        return advancePayment;

    }

    public SkAdvancePayment editPayment(SkAdvancePayment skAdvancePayment) {
        super.edit(skAdvancePayment);
        return getEntityManager().merge(skAdvancePayment);
    }
}
