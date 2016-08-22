/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.google.gson.Gson;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.datawarehouse.TcZtabExpTvv1t;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 *
 * @author Gilbert.Lin
 */
@Path("tvv1t")
@Stateless
public class TcZtabExpTvv1tFacade extends AbstractFacade<TcZtabExpTvv1t> {

    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "datawarehousePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TcZtabExpTvv1tFacade() {
        super(TcZtabExpTvv1t.class);
    }

    @GET
    @Path("findall")
    @Produces("text/plain; charset=UTF-8;")
    public String findAll(@Context HttpServletRequest request) {
        return new Gson().toJson(findAll());
    }

    public String findByKvgr1(String kvgr1) {
        String bezei = "";
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TcZtabExpTvv1t> cq = cb.createQuery(TcZtabExpTvv1t.class);
        Root<TcZtabExpTvv1t> root = cq.from(TcZtabExpTvv1t.class);
        cq.select(root);

        cq.where(cb.equal(root.get("tcZtabExpTvv1tPK").get("kvgr1"), kvgr1));
        List<TcZtabExpTvv1t> list = em.createQuery(cq).getResultList();
        if (list != null && !list.isEmpty()) {
            bezei = list.get(0).getBezei();
        }
        return bezei;
    }
}
