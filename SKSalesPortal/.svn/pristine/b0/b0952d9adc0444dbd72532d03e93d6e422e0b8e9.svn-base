package com.tcci.worklist.facade.datawarehouse;

import com.tcci.worklist.facade.AbstractFacade;
import com.tcci.worklist.entity.datawarehouse.ZtabExpTvkot;
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
public class ZtabExpTvkotFacade extends AbstractFacade<ZtabExpTvkot> {

    @PersistenceContext(unitName = "datawarehousePU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ZtabExpTvkotFacade() {
        super(ZtabExpTvkot.class);
    }

    /**
     * 以 vkorg + 語言代碼帶出銷售組織物件
     *
     * @param spras 語言碼
     * @param vkorg 銷售組織
     * @return
     */
    public ZtabExpTvkot getTvkotByBerslSpras(String mandt, String spras, String vkorg) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ZtabExpTvkot> cq = cb.createQuery(ZtabExpTvkot.class);
        Root root = cq.from(ZtabExpTvkot.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p0 = cb.equal(root.get("ZtabExpTvkotPK").get("mandt").as(String.class), mandt);
        predicateList.add(p0);
        Predicate p1 = cb.equal(root.get("ZtabExpTvkotPK").get("spras").as(String.class), spras);
        predicateList.add(p1);
        Predicate p2 = cb.equal(root.get("ZtabExpTvkotPK").get("vkorg").as(String.class), vkorg);
        predicateList.add(p2);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        return getEntityManager().createQuery(cq).getSingleResult();
    }
    
    /**
     * 以語言代碼帶出所有銷售組織物件
     *
     * @param spras 語言碼
     * @return
     */
    public List<ZtabExpTvkot> getTvkotBySpras(String mandt, String spras) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ZtabExpTvkot> cq = cb.createQuery(ZtabExpTvkot.class);
        Root root = cq.from(ZtabExpTvkot.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p0 = cb.equal(root.get("ztabExpTvkotPK").get("mandt").as(String.class), mandt);
        predicateList.add(p0);
        Predicate p1 = cb.equal(root.get("ztabExpTvkotPK").get("spras").as(String.class), spras);
        predicateList.add(p1);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        return getEntityManager().createQuery(cq).getResultList();
    }
}
