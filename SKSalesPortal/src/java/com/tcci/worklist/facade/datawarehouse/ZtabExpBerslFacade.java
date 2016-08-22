package com.tcci.worklist.facade.datawarehouse;

import com.tcci.worklist.facade.AbstractFacade;
import com.tcci.worklist.entity.datawarehouse.ZtabExpBersl;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author nEO.Fu
 */
@Stateless
public class ZtabExpBerslFacade extends AbstractFacade<ZtabExpBersl> {
    @PersistenceContext(unitName = "datawarehousePU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ZtabExpBerslFacade() {
        super(ZtabExpBersl.class);
    }
    
    /**
     * 以 bname 帶出權限碼
     * @param bname
     * @return 
     */
    public List<ZtabExpBersl> getBerslsByBname(String bname) {
//        Query q = getEntityManager().createNamedQuery("SdZtabExpBersl.findByBname");
//        q.setParameter("bname",bname);
//        return q.getResultList();
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ZtabExpBersl> cq = cb.createQuery(ZtabExpBersl.class);
        Root root = cq.from(ZtabExpBersl.class);
        Predicate p = cb.equal(root.get("ztabExpBerslPK").get("bname").as(String.class), bname);
        cq.where(p);
        return getEntityManager().createQuery(cq).getResultList();
    }
}
