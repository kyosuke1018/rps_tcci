/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.ZrtBsegArtl;
import com.tcci.sksp.entity.org.SkSalesMember;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author carl.lin
 */
@Stateless
public class ZrtBsegArtlFacade extends AbstractFacade<ZrtBsegArtl> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZrtBsegArtlFacade() {
        super(ZrtBsegArtl.class);
    }

    public List<ZrtBsegArtl> Ztbsegartl() {
        List<ZrtBsegArtl> listArt = new ArrayList<ZrtBsegArtl>();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select sarea, vkgrp,sum(amt030),sum(amt060),sum(amt090),");
            sql.append(" sum(amt120),sum(amt150),sum(amt180),sum(amt270), ");
            sql.append(" sum(amt365),sum(amtlyr) ,sum(amttot)  from zrt_bseg_artl ");
            sql.append("  where vkgrp <> '000' ");
            sql.append(" group by sarea, vkgrp order by sarea, vkgrp ");
            Query q = em.createNativeQuery(sql.toString());
            List<Object> results = q.getResultList();

            long idx = 0;
            for (Object o : results) {
                Object[] v = (Object[]) o;
                ZrtBsegArtl zrtBsegArtl = new ZrtBsegArtl();

                zrtBsegArtl.setId(idx++);
                zrtBsegArtl.setSarea((String) v[0]);
                zrtBsegArtl.setVkgrp((String) v[1]);
                zrtBsegArtl.setAmt030((BigDecimal) v[2]);
                zrtBsegArtl.setAmt060((BigDecimal) v[3]);
                zrtBsegArtl.setAmt090((BigDecimal) v[4]);
                zrtBsegArtl.setAmt120((BigDecimal) v[5]);
                zrtBsegArtl.setAmt150((BigDecimal) v[6]);
                zrtBsegArtl.setAmt180((BigDecimal) v[7]);
                zrtBsegArtl.setAmt270((BigDecimal) v[8]);
                zrtBsegArtl.setAmt365((BigDecimal) v[9]);
                zrtBsegArtl.setAmtlyr((BigDecimal) v[10]);
                zrtBsegArtl.setAmttot((BigDecimal) v[11]);
                listArt.add(zrtBsegArtl);

            }
        } catch (Exception me) {
            me.printStackTrace();
        }
        return listArt;
    }

    public List<ZrtBsegArtl> ZtbsegartlBySarea(SkSalesMember sales) {
        List<ZrtBsegArtl> listArt = null;
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<ZrtBsegArtl> cq = builder.createQuery(ZrtBsegArtl.class);
        Root<ZrtBsegArtl> root = cq.from(ZrtBsegArtl.class);
        cq.select(root);

        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p = builder.equal(root.get("sarea"), sales.getCode().substring(0, 2));
        predicateList.add(p);

        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(builder.asc(root.get("sarea")));
        listArt = getEntityManager().createQuery(cq).getResultList();

        return listArt;
    }

    public ZrtBsegArtl sumZtbsegartlTotal(String strsarea, String strvkgrp) {
        ZrtBsegArtl sum_ztbsegartl = new ZrtBsegArtl();

        List<ZrtBsegArtl> list = null;
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<ZrtBsegArtl> cq = builder.createQuery(ZrtBsegArtl.class);
        Root<ZrtBsegArtl> root = cq.from(ZrtBsegArtl.class);
        cq.select(root);

        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (!strvkgrp.isEmpty()) {
            Predicate p = builder.equal(root.get("vkgrp"), strvkgrp);
            predicateList.add(p);
        }
        if (!strsarea.isEmpty()) {
            Predicate p = builder.equal(root.get("sarea"), strsarea);
            predicateList.add(p);
        }

        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }

        list = getEntityManager().createQuery(cq).getResultList();

        double sumAtm030 = 0, sumAtm060 = 0, sumAtm090 = 0, sumAtm120 = 0,
                sumAtm150 = 0, sumAtm180 = 0, sumAtm270 = 0, sumAtm365 = 0,
                sumAtmTot = 0;

        for (ZrtBsegArtl zrtBsegArtl : list) {
            sumAtm030 += zrtBsegArtl.getAmt030().doubleValue();
            sumAtm060 += zrtBsegArtl.getAmt060().doubleValue();
            sumAtm090 += zrtBsegArtl.getAmt090().doubleValue();
            sumAtm120 += zrtBsegArtl.getAmt120().doubleValue();
            sumAtm150 += zrtBsegArtl.getAmt150().doubleValue();
            sumAtm180 += zrtBsegArtl.getAmt180().doubleValue();
            sumAtm270 += zrtBsegArtl.getAmt270().doubleValue();
            sumAtm365 += zrtBsegArtl.getAmt365().doubleValue();
            sumAtmTot += zrtBsegArtl.getAmttot().doubleValue();
        }
        sum_ztbsegartl.setAmt030(new java.math.BigDecimal(sumAtm030));
        sum_ztbsegartl.setAmt060(new java.math.BigDecimal(sumAtm060));
        sum_ztbsegartl.setAmt090(new java.math.BigDecimal(sumAtm090));
        sum_ztbsegartl.setAmt120(new java.math.BigDecimal(sumAtm120));
        sum_ztbsegartl.setAmt150(new java.math.BigDecimal(sumAtm150));
        sum_ztbsegartl.setAmt180(new java.math.BigDecimal(sumAtm180));
        sum_ztbsegartl.setAmt270(new java.math.BigDecimal(sumAtm270));
        sum_ztbsegartl.setAmt365(new java.math.BigDecimal(sumAtm365));
        sum_ztbsegartl.setAmttot(new java.math.BigDecimal(sumAtmTot));
        return sum_ztbsegartl;
    }

    public ZrtBsegArtl sumZtbsegartlBetween(String startVkgrp, String endVkgrp) {
        ZrtBsegArtl sum_ztbsegartl = new ZrtBsegArtl();

        List<ZrtBsegArtl> list = null;
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<ZrtBsegArtl> cq = builder.createQuery(ZrtBsegArtl.class);
        Root<ZrtBsegArtl> root = cq.from(ZrtBsegArtl.class);
        cq.select(root);

        List<Predicate> predicateList = new ArrayList<Predicate>();

        if (!startVkgrp.isEmpty()) {
            Predicate p = builder.greaterThan(root.<String>get("vkgrp"), startVkgrp);
            predicateList.add(p);

        }
        if (!endVkgrp.isEmpty()) {
            Predicate p = builder.lessThan(root.<String>get("vkgrp"), endVkgrp);
            predicateList.add(p);
        }


        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }

        list = getEntityManager().createQuery(cq).getResultList();

        double sumAtm030 = 0, sumAtm060 = 0, sumAtm090 = 0, sumAtm120 = 0,
                sumAtm150 = 0, sumAtm180 = 0, sumAtm270 = 0, sumAtm365 = 0,
                sumAtmTot = 0;

        for (ZrtBsegArtl zrtBsegArtl : list) {
            sumAtm030 += zrtBsegArtl.getAmt030().doubleValue();
            sumAtm060 += zrtBsegArtl.getAmt060().doubleValue();
            sumAtm090 += zrtBsegArtl.getAmt090().doubleValue();
            sumAtm120 += zrtBsegArtl.getAmt120().doubleValue();
            sumAtm150 += zrtBsegArtl.getAmt150().doubleValue();
            sumAtm180 += zrtBsegArtl.getAmt180().doubleValue();
            sumAtm270 += zrtBsegArtl.getAmt270().doubleValue();
            sumAtm365 += zrtBsegArtl.getAmt365().doubleValue();
            sumAtmTot += zrtBsegArtl.getAmttot().doubleValue();
        }
        sum_ztbsegartl.setAmt030(new java.math.BigDecimal(sumAtm030));
        sum_ztbsegartl.setAmt060(new java.math.BigDecimal(sumAtm060));
        sum_ztbsegartl.setAmt090(new java.math.BigDecimal(sumAtm090));
        sum_ztbsegartl.setAmt120(new java.math.BigDecimal(sumAtm120));
        sum_ztbsegartl.setAmt150(new java.math.BigDecimal(sumAtm150));
        sum_ztbsegartl.setAmt180(new java.math.BigDecimal(sumAtm180));
        sum_ztbsegartl.setAmt270(new java.math.BigDecimal(sumAtm270));
        sum_ztbsegartl.setAmt365(new java.math.BigDecimal(sumAtm365));
        sum_ztbsegartl.setAmttot(new java.math.BigDecimal(sumAtmTot));
        return sum_ztbsegartl;
    }
}
