/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.google.gson.Gson;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.vo.SalesDetailsVO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lynn.Huang
 */
@Path("customer")
@Stateless
public class SkCustomerFacade extends AbstractFacade<SkCustomer> {

    private static final Logger logger = LoggerFactory.getLogger(SkCustomerFacade.class);
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkCustomerFacade() {
        super(SkCustomer.class);
    }

    public SkCustomer findByCode(String code) {
        try {
            if (!StringUtils.isEmpty(code)) {
                return (SkCustomer) em.createNamedQuery("SkCustomer.findByCode").setParameter("code", code).getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public SkCustomer findBySimpleCode(String simpleCode) {
        try {
            if (!StringUtils.isEmpty(simpleCode)) {
                return (SkCustomer) em.createNamedQuery("SkCustomer.findBySimpleCode").setParameter("simpleCode", simpleCode).getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public SkCustomer findBySimpleCodeSalesCode(String simpleCode, String salesCode) {
        try {
            return (SkCustomer) em.createQuery("SELECT s FROM SkCustomer s WHERE s.simpleCode = :simpleCode and s.code = :code").setParameter("simpleCode", simpleCode).setParameter("code", salesCode).getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public List<SkCustomer> findByConditions(String customerCode, String salesGroupCode) {
        logger.debug("customerCode={}, salesGroupCode={}", new Object[]{customerCode, salesGroupCode});
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT s FROM SkCustomer s WHERE 1 = 1 ");
            if (!StringUtils.isEmpty(customerCode)) {
                sql.append("  AND s.simpleCode LIKE :simpleCode ");
            }
            if (!StringUtils.isEmpty(salesGroupCode)) {
                sql.append("  AND s.sapid = :sapid");
            }
            sql.append(" order by s.code");
            Query q = em.createQuery(sql.toString());
            if (!StringUtils.isEmpty(customerCode)) {
                q.setParameter("simpleCode", customerCode.concat("%"));
            }
            if (!StringUtils.isEmpty(salesGroupCode)) {
                q.setParameter("sapid", salesGroupCode);
            }
            return q.getResultList();
        } catch (Exception e) {
            logger.error("findByConditions(), error={}", e);
        }
        return new ArrayList<SkCustomer>();
    }

    public List<SkCustomer> findByCriteria(String code, String name, String simpleCode) {
        List<SkCustomer> list = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SkCustomer> cq = builder.createQuery(SkCustomer.class);
        Root<SkCustomer> root = cq.from(SkCustomer.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (!StringUtils.isEmpty(code)) {
            Predicate p = builder.like(root.get("code").as(String.class), code + "%");
            predicateList.add(p);
        }

        if (!StringUtils.isEmpty(name)) {
            Predicate p = builder.like(root.get("name").as(String.class), name + "%");
            predicateList.add(p);
        }

        if (!StringUtils.isEmpty(simpleCode)) {
            Predicate p = builder.like(root.get("simpleCode").as(String.class), simpleCode + "%");
            predicateList.add(p);
        }

        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(builder.asc(root.get("code")));
        list = getEntityManager().createQuery(cq).getResultList();
        return list;
    }

    public List<SkCustomer> findByCriteria(String code, String name) {
        return findByCriteria(code, name, null);
    }

    public List<String> getAllOfPaymentTerm() {
        List results = getEntityManager().createNativeQuery("select distinct t.payment_term from sk_customer t order by t.payment_term").getResultList();
        if (results != null) {
            List<String> list = new ArrayList<String>();
            for (Object o : results) {
                if (o != null) {
                    String s = (String) o;
                    list.add(s);
                }
            }
            return list;
        } else {
            return null;
        }
    }

    @GET
    @Path("findbycriteria")
    @Produces("text/plain; charset=UTF8;")
    public String findByCustomer(@Context HttpServletRequest request,
            @QueryParam("sales_member") String jsonSalesMember,
            @QueryParam("code") String code,
            @QueryParam("name") String name,
            @QueryParam("index") int index,
            @QueryParam("limit") int limit) {
        Gson gson = new Gson();
        SkSalesMember salesMember = null;
        logger.debug("index={}", index);
        logger.debug("limit={}", limit);

        if (StringUtils.isNotEmpty(jsonSalesMember)) {
            salesMember = gson.fromJson(jsonSalesMember, SkSalesMember.class);
        }
        if (limit == 0) {
            return "ERROR:limit is required!";
        } else if (limit > 100) {
            return "ERROR:limit only allow 1 to 100!";
        }

        return gson.toJson(findByCriteria(salesMember, code, name, null, null, null, null, null, index, limit));
    }

    @GET
    @Path("findconsigneebycriteria")
    @Produces("text/plain; charset=UTF-8")
    public String findConsigneeByCriteria(@Context HttpServletRequest request,
            @QueryParam("code") String code,
            @QueryParam("name") String name,
            @QueryParam("index") int index,
            @QueryParam("limit") int limit) {
        if (limit == 0) {
            return "ERROR:limit is required!";
        } else if (limit > 100) {
            return "ERROR:limit only allow 1 to 100!";
        }

        return new Gson().toJson(findByCriteria(null, code, name, null, null, null, null, "3", index, limit));
    }

    public List<SkCustomer> findConsigneeByCriteria(String code, String name) {
        return findByCriteria(null, code, name, null, null, null, null, "3");
    }

    public List<SkCustomer> findByCriteria(SkSalesMember member, String code, String name, String zipcode, String city, String street, String paymentTerm) {
        return findByCriteria(member, code, name, zipcode, city, street, paymentTerm, "");
    }

    public List<SkCustomer> findByCriteria(SkSalesMember member, String code, String name, String zipcode, String city, String street, String paymentTerm, String prefix) {
        return findByCriteria(member, code, name, zipcode, city, street, paymentTerm, prefix, 0, 0);
    }

    private List<SkCustomer> findByCriteria(SkSalesMember member, String code, String name, String zipcode, String city, String street, String paymentTerm, String prefix, int index, int limit) {
        List<SkCustomer> list = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<SkCustomer> cq = builder.createQuery(SkCustomer.class);
        Root<SkCustomer> root = cq.from(SkCustomer.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (member != null) {
            Predicate p = builder.equal(root.get("sapid"), member.getCode());
            predicateList.add(p);
        }

        if (StringUtils.isNotEmpty(prefix)) {
            Predicate p = builder.like(root.get("simpleCode").as(String.class), prefix + "%");
            predicateList.add(p);
        }

        if (code != null) {
            Predicate p = builder.like(root.get("code").as(String.class), "%" + code + "%");
            predicateList.add(p);
        }

        if (name != null) {
            Predicate p = builder.like(root.get("name").as(String.class), "%" + name + "%");
            predicateList.add(p);
        }

        if (zipcode != null) {
            Predicate p = builder.like(root.get("zipCode").as(String.class), "%" + zipcode + "%");
            predicateList.add(p);
        }

        if (city != null) {
            Predicate p = builder.like(root.get("city").as(String.class), "%" + city + "%");
            predicateList.add(p);
        }

        if (street != null) {
            Predicate p = builder.like(root.get("street").as(String.class), "%" + street + "%");
            predicateList.add(p);
        }
        if (paymentTerm != null) {
            Predicate p = builder.equal(root.get("paymentTerm"), paymentTerm);
            predicateList.add(p);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(builder.asc(root.get("zipCode")), builder.asc(root.get("code")));

        if (limit > 0) {
            TypedQuery typedQuery = getEntityManager().createQuery(cq);
            typedQuery.setFirstResult(index);
            typedQuery.setMaxResults(limit);
            list = typedQuery.getResultList();
        } else {
            list = getEntityManager().createQuery(cq).getResultList();
        }
        logger.debug("list.size()={}", list.size());
        return list;
    }

    public void saveAllowancePageAndTaxRate(List<SalesDetailsVO> list) {
        if (list != null) {
            for (SalesDetailsVO vo : list) {
                SkCustomer customer = findBySimpleCode(vo.getCustomerSimpleCode());
                //System.out.println("saveAllowancePage customer=" + customer.getSimpleCode() +",page=" + vo.getSalesAllowancesPage());
                customer.setSalesAllowancesPage(vo.getSalesAllowancesPage());
                customer.setDiscountRate(vo.getDiscountRate());
                customer = getEntityManager().merge(customer);
            }
        }
    }
}
