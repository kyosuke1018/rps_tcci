package com.tcci.sksp.facade;

import com.google.gson.Gson;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.ar.SkProductMaster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
@Path(value = "product")
@Stateless
public class SkProductMasterFacade extends AbstractFacade<SkProductMaster> {

    Logger logger = LoggerFactory.getLogger(SkProductMasterFacade.class);
    private ResourceBundle rb = ResourceBundle.getBundle("/messages");
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkProductMasterFacade() {
        super(SkProductMaster.class);
    }

    @GET
    @Path("findbycode")
    @Produces("text/plain; charset=UTF-8;")
    public String findByCode(@Context HttpServletRequest request, @QueryParam("code") String code) {
        SkProductMaster result = null;
        if (StringUtils.isNotEmpty(code)) {
            result = findByCode(code);
        } else {
            return "ERROR:" + rb.getString("quotation.codeIsRequired");
        }
        Gson gson = new Gson();
        return gson.toJson(result);

    }

    public SkProductMaster findByCode(String code) {
        SkProductMaster result = null;
        List<SkProductMaster> list = findByCode(code, false);
        if (!list.isEmpty()) {
            result = list.get(0);
        }
        return result;
    }

    public List<SkProductMaster> findByCode(String code, boolean like) {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SkProductMaster> cq = cb.createQuery(SkProductMaster.class);
        Root<SkProductMaster> root = cq.from(SkProductMaster.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (!StringUtils.isEmpty(code)) {
            Predicate p = null;
            if (like) {
                p = cb.like(root.get("code").as(String.class), code + "%");
            } else {
                p = cb.equal(root.get("code"), code);
            }
            predicateList.add(p);
        }
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        return getEntityManager().createQuery(cq).getResultList();
    }

    @GET
    @Path("findbycategories")
    @Produces("text/plain; charset=UTF-8;")
    public String findByCategories(@Context HttpServletRequest request, @QueryParam("categories") String jsonCategories) {
        Gson gson = new Gson();
        String[] categories = null;
        if (StringUtils.isNotEmpty(jsonCategories)) {
            categories = gson.fromJson(jsonCategories, String[].class);
            return gson.toJson(findByCategories(categories));
        } else {
            return "ERROR:" + rb.getString("quotation.categoriesIsRequired");
        }
    }

    public List<SkProductMaster> findByCategories(String[] categories) {
        if (null == categories || categories.length == 0) {
            return Collections.EMPTY_LIST;
        }
        String sql = "SELECT p FROM SkProductMaster p"
                + " WHERE p.category IN :categories"
                + " ORDER BY p.code";
        Query q = em.createQuery(sql);
        q.setParameter("categories", Arrays.asList(categories));
        return q.getResultList();
    }

    public List<SkProductMaster> findAll() {
        String sql = "SELECT p FROM SkProductMaster p"
                + " ORDER BY p.code";
        Query q = em.createQuery(sql);
        return q.getResultList();
    }

    public boolean isIce(SkProductMaster product) {
        boolean isIce = false;
        if (product != null && product.getMvgr2() != null) {
            isIce = "001".equals(product.getMvgr2());
        }
        return isIce;
    }
    
    public boolean isPrescription(SkProductMaster product) {
        boolean isPrescription = false;
        if(product!=null && product.getMvgr3()!=null) {
            isPrescription = "001".equals(product.getMvgr3());
        }
        return isPrescription;
    }
    
    public boolean isInstruction(SkProductMaster product) {
        boolean isInstruction = false;
        if(product!=null && product.getMvgr3()!=null) {
            isInstruction = "002".equals(product.getMvgr3());
        }
        return isInstruction;
    }
}
