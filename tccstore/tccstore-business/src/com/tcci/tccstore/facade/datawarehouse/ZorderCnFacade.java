/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.datawarehouse;

import com.tcci.tccstore.entity.datawarehouse.ZorderCn;
import com.tcci.tccstore.facade.AbstractFacade;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Neo.Fu
 */
@Named
@Stateless
public class ZorderCnFacade extends AbstractFacade<ZorderCn> {
    @PersistenceContext(unitName = "DatawarehousePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZorderCnFacade() {
        super(ZorderCn.class);
    }

    public List<ZorderCn> findByCustomerYearMonth(String kunnr, String yearMonth) {
        List<ZorderCn> result = null;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ZorderCn> cq = cb.createQuery(ZorderCn.class);
        Root<ZorderCn> root = cq.from(ZorderCn.class);
        cq.select(root);

        List<Predicate> predicateList = new ArrayList();
        if (StringUtils.isNotEmpty(kunnr)) {
            predicateList.add(cb.equal(root.get("kunnr"), kunnr));
        }
        if (StringUtils.isNotEmpty(yearMonth)) {
            predicateList.add(cb.like(root.get("fkdat").as(String.class), yearMonth.concat("%")));
        }
        predicateList.add(cb.notEqual(root.get("flag"), "D")); // 'D'表示已刪除
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(cb.desc(root.get("fkdat"))); // 依出貨日排序, CRM用fkdat當提貨日, wadat可能出現00000000
        Query q = em.createQuery(cq);
        if (StringUtils.isEmpty(yearMonth)) {
            q.setMaxResults(500);
        }
        result = q.getResultList();
        return result;
    }

    public List<ZorderCn> findAll(ZorderCn obj, Date dateStr, Date dateStr1) {
        List<ZorderCn> result = null;
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String tmp = sf.format(dateStr);
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        String tmp1 = sf1.format(dateStr1);

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<ZorderCn> root = cq.from(ZorderCn.class);
        cq.select(root);
        
        List<Predicate> predicateList = new ArrayList();
        if (StringUtils.isNotEmpty(obj.getKunnr())) {
            predicateList.add(cb.like(root.get("kunnr").as(String.class), "%" +obj.getKunnr()));
        }
        
        if (dateStr != null) {
            predicateList.add(cb.greaterThanOrEqualTo(
                    root.get("fkdat").as(String.class), tmp));
        }
        if (dateStr1 != null) {
             predicateList.add(cb.lessThanOrEqualTo(
                     root.get("fkdat").as(String.class), tmp1));
        }
        
        if (!obj.getMatnr().equals("ALL")){
            predicateList.add(cb.equal(root.get("matnr").as(String.class), obj.getMatnr()));
        }
        
        if (!obj.getWerks().equals("ALL")){
            predicateList.add(cb.equal(root.get("werks").as(String.class), obj.getWerks()));
        }
        
        if (!obj.getVkorg().equals("ALL")){
            predicateList.add(cb.equal(root.get("vkorg").as(String.class), obj.getVkorg()));
        }
        
        if (!obj.getBzirk().equals("ALL")){
            predicateList.add(cb.equal(root.get("bzirk").as(String.class), obj.getBzirk()));
        }

        if (StringUtils.isNotEmpty(obj.getXblnr())) {
            predicateList.add(cb.like(root.get("xblnr").as(String.class), 
                    "%" +obj.getXblnr()+ "%"));
        }
        
        if (StringUtils.isNotEmpty(obj.getVgbel())) {
            predicateList.add(cb.like(root.get("vgbel").as(String.class), 
                    "%" +obj.getVgbel()+ "%"));
        }
        
        if (StringUtils.isNotEmpty(obj.getInco1())) {
            predicateList.add(cb.like(root.get("inco1").as(String.class), 
                    "%"+obj.getInco1()+ "%"));
        }
        predicateList.add(cb.notEqual(root.get("flag"), "D")); // 'D'表示已刪除
       if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(cb.asc(root.get("werks")), cb.asc(root.get("aubel")) );

        result = em.createQuery(cq).getResultList();
        return result;
    }

}
