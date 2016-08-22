/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.org.SkSalesChannelMember;
import com.tcci.sksp.entity.org.SkSalesChannels;
import com.tcci.sksp.entity.org.SkSalesMember;
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
 * @author Lynn.Huang
 */
@Stateless
public class SkSalesChannelMemberFacade extends AbstractFacade<SkSalesChannelMember> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkSalesChannelMemberFacade() {
        super(SkSalesChannelMember.class);
    }
    
    public SkSalesChannelMember findByChannel(SkSalesChannels salesChannel) {        
        List<SkSalesChannelMember> list = null;
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SkSalesChannelMember> cq = builder.createQuery(SkSalesChannelMember.class);             
        Root<SkSalesChannelMember> root = cq.from(SkSalesChannelMember.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (salesChannel != null) {
            Predicate p = builder.equal(root.get("salesChannel"), salesChannel);
            predicateList.add(p);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        list = getEntityManager().createQuery(cq).getResultList();
        if (list.size() > 0) {
            return (SkSalesChannelMember)list.get(0);
        }
        return null;
    }
    
    public SkSalesChannels findBySalesMember(SkSalesMember salesMember) {   
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SkSalesChannelMember> cq = builder.createQuery(SkSalesChannelMember.class);             
        Root<SkSalesChannelMember> root = cq.from(SkSalesChannelMember.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (salesMember != null) {
            Predicate p = builder.equal(root.get("salesMember"),  salesMember);
            predicateList.add(p);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        SkSalesChannels sc = new SkSalesChannels();
        List<SkSalesChannelMember> list = getEntityManager().createQuery(cq).getResultList();
        if (list.size() >0){
           sc = list.get(0).getSalesChannel();
        }
        return sc;
    }   
    
     public List<SkSalesChannelMember> findByChannels(SkSalesChannels salesChannel) {        
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SkSalesChannelMember> cq = builder.createQuery(SkSalesChannelMember.class);             
        Root<SkSalesChannelMember> root = cq.from(SkSalesChannelMember.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (salesChannel != null) {
            Predicate p = builder.equal(root.get("salesChannel"), salesChannel);
            predicateList.add(p);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        return getEntityManager().createQuery(cq).getResultList();       
    }
  
}
