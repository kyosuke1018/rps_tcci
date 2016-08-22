package com.tcci.worklist.facade.datawarehouse;

import com.tcci.worklist.facade.AbstractFacade;
import com.tcci.worklist.entity.datawarehouse.ZtabExpTj10t;
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
 * @author nEO.Fu
 */
@Stateless
public class ZtabExpTj10tFacade extends AbstractFacade<ZtabExpTj10t> {

    @PersistenceContext(unitName = "datawarehousePU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ZtabExpTj10tFacade() {
        super(ZtabExpTj10t.class);
    }

    /**
     * 以 bersl + 語言代碼帶出權限碼物件
     *
     * @param bersl 權限碼
     * @param spras 語言碼
     * @return
     */
    public ZtabExpTj10t getTj10tBySprasBersl(String mandt, String spras, String bersl) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ZtabExpTj10t> cq = cb.createQuery(ZtabExpTj10t.class);
        Root root = cq.from(ZtabExpTj10t.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p0 = cb.equal(root.get("ztabExpTj10tPK").get("mandt").as(String.class), mandt);
        predicateList.add(p0);
        Predicate p1 = cb.equal(root.get("ztabExpTj10tPK").get("spras").as(String.class), spras);
        predicateList.add(p1);
        Predicate p2 = cb.equal(root.get("ztabExpTj10tPK").get("bersl").as(String.class), bersl);
        predicateList.add(p2);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    /**
     * 以語言代碼帶出所有權限碼物件
     *
     * @param spras 語言碼
     * @return
     */
    public List<ZtabExpTj10t> getTj10tBySpras(String mandt, String spras) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ZtabExpTj10t> cq = cb.createQuery(ZtabExpTj10t.class);
        Root root = cq.from(ZtabExpTj10t.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p0 = cb.equal(root.get("ztabExpTj10tPK").get("mandt").as(String.class), mandt);
        predicateList.add(p0);
        Predicate p1 = cb.equal(root.get("ztabExpTj10tPK").get("spras").as(String.class), spras);
        predicateList.add(p1);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        return getEntityManager().createQuery(cq).getResultList();
    }
}
