package com.tcci.worklist.facade.datawarehouse;

import com.tcci.worklist.facade.AbstractFacade;
import com.tcci.worklist.entity.datawarehouse.ZtabExpTvtwt;
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
public class ZtabExpTvtwtFacade extends AbstractFacade<ZtabExpTvtwt> {

    @PersistenceContext(unitName = "datawarehousePU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ZtabExpTvtwtFacade() {
        super(ZtabExpTvtwt.class);
    }

    /**
     * 以 tvtwt + 語言代碼帶出配銷通路物件
     *
     * @param spras 語言碼
     * @param vtweg 配銷通路碼
     * @return
     */
    public ZtabExpTvtwt getTvtwtByBerslTvtwt(String mandt, String spras, String vtweg) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ZtabExpTvtwt> cq = cb.createQuery(ZtabExpTvtwt.class);
        Root root = cq.from(ZtabExpTvtwt.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p0 = cb.equal(root.get("ZtabExpTvtwtPK").get("mandt").as(String.class), mandt);
        predicateList.add(p0);
        Predicate p1 = cb.equal(root.get("ZtabExpTvtwtPK").get("spras").as(String.class), spras);
        predicateList.add(p1);
        Predicate p2 = cb.equal(root.get("ZtabExpTvtwtPK").get("vtweg").as(String.class), vtweg);
        predicateList.add(p2);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        return getEntityManager().createQuery(cq).getSingleResult();
    }
    
    /**
     * 以語言代碼帶出所有配銷通路物件
     *
     * @param spras 語言碼
     * @return
     */
    public List<ZtabExpTvtwt> getTvtwtBySpras(String mandt, String spras) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ZtabExpTvtwt> cq = cb.createQuery(ZtabExpTvtwt.class);
        Root root = cq.from(ZtabExpTvtwt.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p0 = cb.equal(root.get("ztabExpTvtwtPK").get("mandt").as(String.class), mandt);
        predicateList.add(p0);
        Predicate p1 = cb.equal(root.get("ztabExpTvtwtPK").get("spras").as(String.class), spras);
        predicateList.add(p1);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        return getEntityManager().createQuery(cq).getResultList();
    }
}
