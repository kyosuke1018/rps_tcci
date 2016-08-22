/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.customer;

import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcCustomerSales;
import com.tcci.tccstore.entity.EcCustomerSalesPK;
import com.tcci.tccstore.entity.EcCustomerVkorg;
import com.tcci.tccstore.entity.EcCustomerVkorgPK;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcSales;
import com.tcci.tccstore.facade.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@Named
@Stateless
public class EcCustomerFacade extends AbstractFacade {
    private final static Logger logger = LoggerFactory.getLogger(EcCustomerFacade.class);
            
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcCustomerFacade() {
        super(EcCustomer.class);
    }

    public void save(EcCustomer entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public EcCustomer find(Long id) {
        return em.find(EcCustomer.class, id);
    }
    
    public List<EcCustomer> findByKeyword(String keyword) {
        String sql = "SELECT c from EcCustomer c where c.code like :code or c.name like :name";
        Query q = em.createQuery(sql);
        q.setParameter("code","%".concat(keyword).concat("%"));
        q.setParameter("name","%".concat(keyword).concat("%"));
        return q.getResultList();
    }
    
    public EcCustomer findByCode(String code) {
        Query q = em.createNamedQuery("EcCustomer.findByCode");
        q.setParameter("code", code);
        List<EcCustomer> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<EcCustomer> findByMember(EcMember ecMember) {
        String sql = "SELECT e.ecCustomer FROM EcMemberCustomer e WHERE e.ecMember=:ecMember ORDER BY e.ecCustomer.name";
        Query q = em.createQuery(sql);
        q.setParameter("ecMember", ecMember);
        return q.getResultList();
    }
    
    public boolean isCustomerSalesExist(EcCustomer ecCustomer, EcSales ecSales) {
        EcCustomerSalesPK pk = new EcCustomerSalesPK(ecCustomer.getId(), ecSales.getId());
        EcCustomerSales entity = em.find(EcCustomerSales.class, pk);
        return entity != null;
    }
    
    public EcCustomer getCustomersByCode(String code) {
        EcCustomer obj = null;
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root<EcCustomer> root = criteriaQuery.from(EcCustomer.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("code"), code));
        try{
            obj= (EcCustomer)getEntityManager().createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
        }
        return obj;
    }
    
    public List<EcCustomer> findByCriteria(String code, String name) {
        String queryStr = "SELECT object(o) FROM EcCustomer o where 1=1 ";
        if (!code.isEmpty()) {
            queryStr += " and o.code like '%" + code +"%'";
        }
        if (!name.isEmpty()) {
            queryStr += " and o.name like '%" + name +"%'";
        }
        Query query = em.createQuery(queryStr);
        List<EcCustomer> resultList = query.getResultList();
        return resultList;
    }

    // CustomerSales API
    public void insertCustomerSales(EcCustomer ecCustomer, EcSales ecSales) {
        EcCustomerSalesPK pk = new EcCustomerSalesPK(ecCustomer.getId(), ecSales.getId());
        EcCustomerSales entity = new EcCustomerSales(pk);
        entity.setEcCustomer(ecCustomer);
        entity.setEcSales(ecSales);
        em.persist(entity);
    }
    
    public void removeCustomerSales(EcCustomerSales entity) {
        em.remove(em.merge(entity));
    }

    public List<EcCustomerSales> findCustomerSales(EcCustomer ecCustomer) {
        Query q = em.createNamedQuery("EcCustomerSales.findByCustomer");
        q.setParameter("ecCustomer", ecCustomer);
        return q.getResultList();
    }
    
    public List<EcCustomerSales> findAllCustomerSales() {
        return em.createNamedQuery("EcCustomerSales.findAll").getResultList();
    }
    
    public void updateCustomerSales(EcCustomer ecCustomer, List<EcSales> salesList) {
        if (salesList == null) {
            salesList = new ArrayList<>();
        }
        List<EcCustomerSales> origList = findCustomerSales(ecCustomer);
        List<EcSales> insertList = new ArrayList<>(salesList);
        for (EcCustomerSales cs : origList) {
            EcSales ecSales = cs.getEcSales();
            if (insertList.contains(ecSales)) {
                insertList.remove(ecSales);
            } else {
                logger.warn("remove customer({}):sales({})", ecCustomer.getId(), ecSales.getId());
                em.remove(cs);
            }
        }
        for (EcSales ecSales : insertList) {
            logger.warn("insert customer({}):sales({})", ecCustomer.getId(), ecSales.getId());
            EcCustomerSales cs = new EcCustomerSales(ecCustomer, ecSales);
            em.persist(cs);
        }
    }

    // CustomerVkorg API
    public void insertCustomerVkorg(EcCustomer ecCustomer, String vkorg) {
        EcCustomerVkorgPK pk = new EcCustomerVkorgPK(ecCustomer.getId(), vkorg);
        EcCustomerVkorg entity = new EcCustomerVkorg(pk);
        entity.setEcCustomer(ecCustomer);
        em.persist(entity);
    }
    
    public void removeCustomerVkorg(EcCustomerVkorg entity) {
        em.remove(em.merge(entity));
    }
    
    public List<EcCustomerVkorg> findAllCustomerVkorg() {
        return em.createNamedQuery("EcCustomerVkorg.findAll").getResultList();
    }

}
