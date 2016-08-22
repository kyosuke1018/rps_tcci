/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.SkBank;
import com.tcci.sksp.entity.ar.SkAdvancePayment;
import com.tcci.sksp.entity.ar.SkCheckMaster;
import com.tcci.sksp.entity.enums.RemitMasterStatusEnum;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
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
 * @author Lynn.Huang
 */
@Stateless
public class SkCheckMasterFacade extends AbstractFacade<SkCheckMaster> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkCheckMasterFacade() {
        super(SkCheckMaster.class);
    }

    public SkCheckMaster findByCheckNumber(String checkNumber) {
        SkCheckMaster checkMaster = null;
        List<SkCheckMaster> list = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SkCheckMaster> cq = builder.createQuery(SkCheckMaster.class);
        Root<SkCheckMaster> root = cq.from(SkCheckMaster.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (checkNumber != null) {
            Predicate p = builder.equal(root.get("checkNumber"), checkNumber);
            predicateList.add(p);
        }

        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }

        list = getEntityManager().createQuery(cq).getResultList();
        if (list != null && !list.isEmpty()) {
            checkMaster = list.get(0);
        }
        return checkMaster;
    }

    public List<SkCheckMaster> findByUserData(String empId, String checkNumber, String salesCode) {
        List<SkCheckMaster> list = null;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SkCheckMaster> cq = cb.createQuery(SkCheckMaster.class);
        Root<SkCheckMaster> root = cq.from(SkCheckMaster.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (!StringUtils.isEmpty(empId)) {
            //Predicate[] predicates2 = new Predicate[6];
//            Predicate[] predicates2 = new Predicate[3];
//            int i = 0;
//            Predicate p = builder.like(root.get("creator").get("loginAccount").as(String.class), userData );
//            predicates2[i++] = p;
//            p = builder.like(root.get("creator").get("cname").as(String.class), userData );
//            predicates2[i++] = p;
            Predicate p = cb.equal(root.get("modifier").get("empId").as(String.class), empId);
//            predicates2[i++] = p;
            /*
             * p =
             * builder.like(root.get("modifier").get("loginAccount").as(String.class),
             * userData ); predicates2[i++] = p; p =
             * builder.like(root.get("modifier").get("cname").as(String.class),
             * userData ); predicates2[i++] = p; p =
             * builder.like(root.get("modifier").get("empId").as(String.class),
             * userData ); predicates2[i++] = p;
             *
             */
            //predicateList.add(builder.or(predicates2));
            predicateList.add(p);
        }
        if (!StringUtils.isEmpty(checkNumber)) {
            Predicate p = cb.like(root.get("checkNumber").as(String.class), "%" + checkNumber + "%");
            predicateList.add(p);
        }
        if (!StringUtils.isEmpty(salesCode)) {
            Predicate p = cb.equal(root.get("customer").get("sapid").as(String.class), salesCode);
            predicateList.add(p);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }

        list = getEntityManager().createQuery(cq).getResultList();
        return list;
    }

    public SkCheckMaster editCheckMaster(SkCheckMaster checkMaster) {
        super.edit(checkMaster);
        return getEntityManager().merge(checkMaster);
    }

    public List<SkCheckMaster> findByAdvancePayment(SkAdvancePayment advancePayment) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(SkCheckMaster.class);
        Root root = cq.from(SkCheckMaster.class);
        cq.where(cb.equal(root.get("advancePayment").as(SkAdvancePayment.class), advancePayment));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<SkCheckMaster> findByCriteria(Date createtimestampBegin, Date createtimestampEnd, TcUser modifier, Date modifytimestampBegin, Date modifytimestampEnd) {
        List<SkCheckMaster> list = null;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SkCheckMaster> cq = cb.createQuery(SkCheckMaster.class);
        Root<SkCheckMaster> root = cq.from(SkCheckMaster.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p0 = cb.not(cb.isNull(root.get("advancePayment").as(SkAdvancePayment.class)));
        predicateList.add(p0);
        if (createtimestampBegin != null && createtimestampEnd != null) {
            Predicate p1 = cb.between(root.get("createtimestamp").as(Date.class), createtimestampBegin, createtimestampEnd);
            predicateList.add(p1);
        } else if (createtimestampBegin != null) {
            Predicate p1 = cb.greaterThanOrEqualTo(root.get("createtimestamp").as(Date.class), createtimestampBegin);
            predicateList.add(p1);
        } else if (createtimestampEnd != null) {
            Predicate p1 = cb.lessThan(root.get("createtimestamp").as(Date.class), createtimestampEnd);
            predicateList.add(p1);
        }

        if (modifier != null) {
            Predicate p2 = cb.equal(root.get("modifier").as(TcUser.class), modifier);
            predicateList.add(p2);
        }

        if (modifytimestampBegin != null && modifytimestampEnd != null) {
            Predicate p3 = cb.between(root.get("modifytimestamp").as(Date.class), modifytimestampBegin, modifytimestampEnd);
            predicateList.add(p3);
        } else if (modifytimestampBegin != null) {
            Predicate p3 = cb.greaterThanOrEqualTo(root.get("modifytimestamp").as(Date.class), modifytimestampBegin);
            predicateList.add(p3);
        } else if (modifytimestampEnd != null) {
            Predicate p3 = cb.lessThan(root.get("modifytimestamp").as(Date.class), modifytimestampEnd);
            predicateList.add(p3);
        }

        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }

        list = getEntityManager().createQuery(cq).getResultList();
        return list;
    }

//        List<SkArRemitItem> list = null;
//
//        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<SkArRemitItem> cq = cb.createQuery(SkArRemitItem.class);
//        Root<SkArRemitItem> root = cq.from(SkArRemitItem.class);
//        List<Predicate> predicateList = new ArrayList<Predicate>();
//        List<RemitMasterStatusEnum> statusList = new ArrayList<RemitMasterStatusEnum>();
//        statusList.add(RemitMasterStatusEnum.NOT_YET);
//        statusList.add(RemitMasterStatusEnum.CANCELED);
//        Predicate p = cb.not(root.get("arRemitMaster").get("status").as(RemitMasterStatusEnum.class).in(statusList));
//        predicateList.add(p);
//        Predicate p01 = cb.equal(root.get("paymentType").as(PaymentTypeEnum.class), PaymentTypeEnum.CHECK);
//        Predicate p02 = cb.equal(root.get("paymentType2").as(PaymentTypeEnum.class), PaymentTypeEnum.CHECK);
//        Predicate p0 = cb.or(p01, p02);
//        predicateList.add(p0);
//        if (reviewTimestampBegin != null && reviewTimestampEnd != null) {
//            Predicate p1 = cb.between(root.get("arRemitMaster").get("reviewTimestamp").as(Date.class), reviewTimestampBegin, reviewTimestampEnd);
//            predicateList.add(p1);
//        } else if (reviewTimestampBegin != null) {
//            Predicate p2 = cb.greaterThanOrEqualTo(root.get("arRemitMaster").get("reviewTimestamp").as(Date.class), reviewTimestampBegin);
//            predicateList.add(p2);
//        } else if (reviewTimestampEnd != null) {
//            Predicate p3 = cb.lessThan(root.get("arRemitMaster").get("reviewTimestamp").as(Date.class), reviewTimestampEnd);
//            predicateList.add(p3);
//        }
//        if (reviewer != null) {
//            Predicate p4 = cb.equal(root.get("arRemitMaster").get("financeReviewer").as(TcUser.class), reviewer);
//            predicateList.add(p4);
//        }
//        if (predicateList.size() > 0) {
//            Predicate[] predicates = new Predicate[predicateList.size()];
//            predicateList.toArray(predicates);
//            cq.where(predicates);
//        }
//
//        list = getEntityManager().createQuery(cq).getResultList();
//        return list;
    public List<SkCheckMaster> findByCheckNumbersAndCreatetimestamp(Date createtimestampBegin, Date createtimestampEnd, TcUser reviewer, Date reviewTimestampBegin, Date reviewTimestampEnd) {
        List<SkCheckMaster> list = null;

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT distinct c from SkCheckMaster c, SkArRemitItem i");
        sb.append(" WHERE (c.checkNumber = i.checkNumber or c.checkNumber = i.checkNumber2) ");
        sb.append(" AND c.advancePayment is null");
        if (reviewer != null) {
            sb.append(" AND i.arRemitMaster.financeReviewer = :financeReviewer");
        }
        if (reviewTimestampBegin != null & reviewTimestampEnd != null) {
            sb.append(" AND i.arRemitMaster.reviewTimestamp between :reviewTimestampBegin and :reviewTimestampEnd");
        } else if (reviewTimestampBegin != null) {
            sb.append(" AND i.arRemitMaster.reviewTimestamp >= :reviewTimestampBegin");
        } else if (reviewTimestampEnd != null) {
            sb.append(" AND i.arRemitMaster.reviewTimestamp < :reviewTimestampEnd");
        }
        if (createtimestampBegin != null && createtimestampEnd != null) {
            sb.append(" AND c.createtimestamp between :createtimestampBegin and :createtimestampEnd");
        } else if (createtimestampBegin != null) {
            sb.append(" AND c.createtimestamp >= :createtimestampBegin");
        } else if (createtimestampEnd != null) {
            sb.append(" AND c.createtimestamp < :createtimestampEnd");
        }
        Query q = getEntityManager().createQuery(sb.toString());
        if (reviewer != null) {
            q.setParameter("financeReviewer", reviewer);
        }
        if (reviewTimestampBegin != null && reviewTimestampEnd != null) {
            q.setParameter("reviewTimestampBegin", reviewTimestampBegin);
            q.setParameter("reviewTimestampEnd", reviewTimestampEnd);
        } else if (reviewTimestampBegin != null) {
            q.setParameter("reviewTimestampBegin", reviewTimestampBegin);
        } else if (reviewTimestampEnd != null) {
            q.setParameter("reviewTimestampEnd", reviewTimestampEnd);
        }

        if (createtimestampBegin != null && createtimestampEnd != null) {
            q.setParameter("createtimestampBegin", createtimestampBegin);
            q.setParameter("createtimestampEnd", createtimestampEnd);
        } else if (createtimestampBegin != null) {
            q.setParameter("createtimestampBegin", createtimestampBegin);
        } else if (createtimestampEnd != null) {
            q.setParameter("createtimestampEnd", createtimestampEnd);
        }

        list = q.getResultList();
        sb = new StringBuilder();
        sb.append("SELECT distinct c from SkCheckMaster c, SkArRemitItem i, SkArRemitMaster m");
        sb.append(" WHERE (c.checkNumber = i.checkNumber or c.checkNumber = i.checkNumber2) ");
        sb.append(" AND i.arRemitMaster = m");
        sb.append(" AND m.status = :status");
        Query query = getEntityManager().createQuery(sb.toString());
        query.setParameter("status", RemitMasterStatusEnum.NOT_YET);
        List<SkCheckMaster> excludeChecks = query.getResultList();
        list.removeAll(excludeChecks);

        return list;
    }

    public List<String> findBillingAccount(String account) {
        List<String> list = new ArrayList<String>();
        if (null == account) {
            return list;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct c.billingAccount from SkCheckMaster c");
        sb.append(" where ");
        sb.append("c.billingAccount like :billingAccount");
        Query query = getEntityManager().createQuery(sb.toString());
        query.setParameter("billingAccount", "%" + account + "%");
        list = query.getResultList();
        return list;        
    }
    
    public SkBank findSkBankByBillingAccount(String account)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct c.billingBank from SkCheckMaster c");
        sb.append(" where ");
        sb.append("c.billingAccount = :billingAccount");
        Query query = this.getEntityManager().createQuery(sb.toString());
        query.setParameter("billingAccount", account);
        SkBank skBank = (SkBank)query.getSingleResult();
        return skBank;
    }
}
