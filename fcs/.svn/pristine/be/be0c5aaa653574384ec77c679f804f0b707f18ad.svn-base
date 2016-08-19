/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.facade;

import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.rpt.entity.RptCompanyOrg;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class RptCompanyOrgFacade {
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public RptCompanyOrg find(Long id) {
        return em.find(RptCompanyOrg.class, id);
    }
    
    public List<RptCompanyOrg> findAll(CompanyGroupEnum group) {
        Query q = em.createNamedQuery("RptCompanyOrg.findAll");
        q.setParameter("group", group.getCode());
        return q.getResultList();
    }
    
    public RptCompanyOrg findByCompany(CompanyGroupEnum group, String companyCode) {
        Query q = em.createNamedQuery("RptCompanyOrg.findByCompany");
        q.setParameter("group", group.getCode());
        q.setParameter("companyCode", companyCode);
        List<RptCompanyOrg> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<RptCompanyOrg> findByParent(CompanyGroupEnum group, RptCompanyOrg parent) {
        Query q = em.createNamedQuery("RptCompanyOrg.findByParent");
        q.setParameter("group", group.getCode());
        q.setParameter("parent", parent);
        return q.getResultList();
    }
    
    public void save(RptCompanyOrg rptCompanyOrg) {
        if (rptCompanyOrg.getId()==null) {
            em.persist(rptCompanyOrg);
        } else {
            em.merge(rptCompanyOrg);
        }
    }
    
    public void remove(Long id){
        if (id!=null) {
            em.remove(this.find(id));
        }
    }
    
    //for合併公司各自報表需求 找出第一層(25家)中虛擬的 合併公司 ==> 7家
    public List<RptCompanyOrg> findVirtualCompany(CompanyGroupEnum group, int hlevel) {
        StringBuilder sql = new StringBuilder();
        sql.append("select org from RptCompanyOrg org ");
        sql.append("where 1=1 ");
        sql.append("and org.group.code = :group ");
        sql.append("and org.hlevel = :hlevel ");
        sql.append("and org.company.virtual = true ");
        Query q = em.createQuery(sql.toString());
        q.setParameter("group", group.getCode());
        q.setParameter("hlevel", hlevel);
        return q.getResultList();
    }
}
