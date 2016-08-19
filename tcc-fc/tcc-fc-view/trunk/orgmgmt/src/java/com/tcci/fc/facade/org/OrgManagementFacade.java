/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.org;

import com.tcci.fc.entity.org.OrgCompany;
import com.tcci.fc.entity.org.OrgDepartment;
import com.tcci.fc.entity.org.OrgEmployee;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class OrgManagementFacade {
    @PersistenceContext(unitName="Model")
    private EntityManager em;

    /************************
     * OrgCompany Services:
     * save
     * findCompany
     * findAllCompany
     ***********************/

    public void save(OrgCompany entity) {
        if (entity.getId() == null) {em.persist(entity);}
        else { em.merge(entity); }
    }

    public OrgCompany findCompany(String code) {
        Query q = em.createQuery("SELECT c FROM OrgCompany c WHERE c.code=:code");
        q.setParameter("code", code);
        List<OrgCompany> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public List<OrgCompany> findAllCompany() {
        return em.createQuery("SELECT c FROM OrgCompany c").getResultList();
    }


    /************************
     * OrgDepartment Services
     * save
     * findDepartment
     * findTopDepartment
     * findSubDepartment
     ***********************/

    public void save(OrgDepartment entity) {
        if (entity.getId() == null) {em.persist(entity);}
        else { em.merge(entity); }
    }

    public OrgDepartment findDepartment(OrgCompany orgCompany, String deptCode) {
        Query q = em.createQuery("SELECT d FROM OrgDepartment d WHERE d.orgCompany=:orgCompany AND d.code=:code");
        q.setParameter("code", deptCode);
        q.setParameter("orgCompany", orgCompany);
        List<OrgDepartment> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public List<OrgDepartment> findTopDepartment(OrgCompany orgCompany) {
        StringBuilder sb = new StringBuilder("SELECT d FROM OrgDepartment d WHERE d.orgCompany=:orgCompany AND d.parent IS NULL");
        Query q = em.createQuery(sb.toString());
        q.setParameter("orgCompany", orgCompany);
        return q.getResultList();
    }

    public List<OrgDepartment> findSubDepartment(OrgDepartment parent) {
        String sql = "SELECT d FROM OrgDepartment d WHERE d.parent=:parent";
        Query q = em.createQuery(sql);
        q.setParameter("parent", parent);
        return q.getResultList();
    }

    public List<OrgDepartment> findDepartmentTree(OrgDepartment dept) {
        List<OrgDepartment> result = new ArrayList<OrgDepartment>();
        if (null == dept) {
            return result;
        }
        result.add(dept);
        String sql = "SELECT d FROM OrgDepartment d WHERE d.parent.id IN :parents";
        Query q = em.createQuery(sql);
        List<Long> parents = new ArrayList<Long>();
        parents.add(dept.getId());
        Collection<OrgDepartment> children;
        do {
            q.setParameter("parents", parents);
            children = q.getResultList();
            children.removeAll(result);
            result.addAll(children);
            parents.clear();
            for (OrgDepartment d : children) {
                parents.add(d.getId());
            }
        } while (!children.isEmpty());
        return result;
    }

    /**********************
     * OrgEmployee Services
     * save
     * findEmployee
     * findAllEmployee
     * findEmployeeByCompany
     * findEmployeeByDepartment
     *********************/

    public void save(OrgEmployee entity) {
        if (entity.getId() == null) {em.persist(entity);}
        else { em.merge(entity); }
    }

    public OrgEmployee findEmployee(String code) {
        Query q = em.createQuery("SELECT e FROM OrgEmployee e WHERE e.code=:code");
        q.setParameter("code", code);
        List<OrgEmployee> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public List<OrgEmployee> findAllEmployee() {
        return em.createQuery("SELECT e FROM OrgEmployee e").getResultList();
    }

    public List<OrgEmployee> findEmployeeByCompany(OrgCompany orgCompany) {
        String sql = "SELECT e FROM OrgEmployee e WHERE e.orgCompany=:orgCompany";
        Query q = em.createQuery(sql);
        q.setParameter("orgCompany", orgCompany);
        return q.getResultList();
    }

    public List<OrgEmployee> findEmployeeByDepartment(OrgDepartment orgDepartment, boolean recursive) {
        if (!recursive) {
            String sql = "SELECT e FROM OrgEmployee e WHERE e.orgDepartment=:orgDepartment";
            Query q = em.createQuery(sql);
            q.setParameter("orgDepartment", orgDepartment);
            return q.getResultList();
        }
        List<OrgDepartment> deptTree = findDepartmentTree(orgDepartment);
        if (deptTree.isEmpty()) {
            return new ArrayList<OrgEmployee>();
        }
        List<Long> depts = new ArrayList<Long>();
        for (OrgDepartment dept : deptTree) {
            depts.add(dept.getId());
        }
        String sql = "SELECT e FROM OrgEmployee e WHERE e.orgDepartment.id IN :depts";
        Query q = em.createQuery(sql);
        q.setParameter("depts", depts);
        return q.getResultList();
    }

}
