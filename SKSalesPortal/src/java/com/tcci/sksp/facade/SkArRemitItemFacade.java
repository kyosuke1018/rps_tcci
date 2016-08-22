/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.ar.SkArRemitItem;
import com.tcci.sksp.entity.ar.SkArRemitMaster;
import com.tcci.sksp.entity.enums.PaymentTypeEnum;
import com.tcci.sksp.entity.enums.RemitMasterStatusEnum;
import com.tcci.sksp.entity.org.SkSalesMember;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Lynn.Huang
 */
@Stateless
public class SkArRemitItemFacade extends AbstractFacade<SkArRemitItem> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @EJB
    private SkArRemitMasterFacade skArRemitMasterFacade;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkArRemitItemFacade() {
        super(SkArRemitItem.class);
    }

    public List findCheckTotalAmount(SkSalesMember member, String checkNumber, boolean isCheckOne) {
        List list = null;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<SkArRemitItem> root = cq.from(SkArRemitItem.class);

        Selection<String> number = root.get(isCheckOne ? "checkNumber" : "checkNumber2").as(String.class).alias("checkNumber");
        Selection<BigDecimal> checkTotalAmount = cb.sum((Expression<BigDecimal>) root.get(isCheckOne ? "amount" : "amount2").as(BigDecimal.class)).alias("checkAmount");
        Selection<Long> customerId = cb.min(root.get("arRemitMaster").get("customer").get("id").as(Long.class)).alias("customerId");
        cq.multiselect(number, customerId, checkTotalAmount);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (member != null && StringUtils.isNotEmpty(member.getCode())) {
            Predicate p = cb.equal(root.get("arRemitMaster").get("customer").get("sapid"), member.getCode());
            predicateList.add(p);
        }
        if (!StringUtils.isEmpty(checkNumber)) {
            Predicate p = cb.like(root.get(isCheckOne ? "checkNumber" : "checkNumber2").as(String.class), "%" + checkNumber + "%");
            predicateList.add(p);
        } else {
            Predicate p = cb.isNotNull(root.get(isCheckOne ? "checkNumber" : "checkNumber2"));
            predicateList.add(p);
        }
        Predicate p = cb.equal(root.get(isCheckOne ? "paymentType" : "paymentType2"), PaymentTypeEnum.CHECK);
        predicateList.add(p);
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.groupBy(root.get(isCheckOne ? "checkNumber" : "checkNumber2"));
        list = getEntityManager().createQuery(cq).getResultList();
        return (list == null ? new ArrayList() : list);
    }

    public List<SkArRemitItem> findByCheckNumber(String checkNumber) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SkArRemitItem> cq = cb.createQuery(SkArRemitItem.class);
        Root<SkArRemitItem> root = cq.from(SkArRemitItem.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p1_1 = cb.equal(root.get("checkNumber").as(String.class), checkNumber);
        Predicate p1_2 = cb.equal(root.get("paymentType").as(PaymentTypeEnum.class), PaymentTypeEnum.CHECK);
        Predicate p1 = cb.and(p1_1, p1_2);
        Predicate p2_1 = cb.equal(root.get("checkNumber2").as(String.class), checkNumber);
        Predicate p2_2 = cb.equal(root.get("paymentType2").as(PaymentTypeEnum.class), PaymentTypeEnum.CHECK);
        Predicate p2 = cb.and(p2_1, p2_2);
        Predicate p = cb.or(p1, p2);
        predicateList.add(p);
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<SkArRemitItem> findByCriteria(SkSalesMember member, String checkNumber, RemitMasterStatusEnum statusEnum) {
        List<SkArRemitItem> list = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SkArRemitItem> cq = builder.createQuery(SkArRemitItem.class);
        Root<SkArRemitItem> root = cq.from(SkArRemitItem.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (statusEnum != null) {
            Predicate p = builder.equal(root.get("arRemitMaster").get("status"), statusEnum);
            predicateList.add(p);
        }
        if (member != null) {
            Predicate p = builder.equal(root.get("arRemitMaster").get("sapid"), member.getCode());
            predicateList.add(p);
        }
        if (!StringUtils.isEmpty(checkNumber)) {
            Predicate p1 = builder.like(root.get("checkNumber").as(String.class), "%" + checkNumber + "%");
            Predicate p2 = builder.like(root.get("checkNumber2").as(String.class), "%" + checkNumber + "%");
            Predicate p = builder.or(p1, p2);
            predicateList.add(p);
        } else {
            //Predicate p = builder.isNotNull(root.get("checkNumber"));
            Predicate p1_1 = builder.equal(root.get("paymentType"), PaymentTypeEnum.CHECK);
            Predicate p1_2 = builder.isNotNull(root.get("checkNumber"));
            Predicate p1 = builder.and(p1_1, p1_2);
            Predicate p2_1 = builder.equal(root.get("paymentType2"), PaymentTypeEnum.CHECK);
            Predicate p2_2 = builder.isNotNull(root.get("checkNumber2"));
            Predicate p2 = builder.and(p2_1, p2_2);
            Predicate p = builder.or(p1, p2);
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

    public void insertOrUpdate(SkArRemitMaster master, List<SkArRemitItem> items, TcUser user) {
        List<SkArRemitItem> needRemoveItemList = new ArrayList();
        if (master.getId() != null) {
            SkArRemitMaster originalMaster = skArRemitMasterFacade.find(master.getId());
            if (!originalMaster.getSkArRemitItemCollection().isEmpty()) {
                needRemoveItemList.addAll(originalMaster.getSkArRemitItemCollection());
            }
        }
        if (items != null && !items.isEmpty()) {
            for (SkArRemitItem item : items) {
                needRemoveItemList.remove(item);
                insertOrUpdate(item, user);
            }
            // update master
            master.setSkArRemitItemCollection(items);
            skArRemitMasterFacade.edit(master);
        }
        if (!needRemoveItemList.isEmpty()) {
            for (SkArRemitItem needRemoveItem : needRemoveItemList) {
                remove(needRemoveItem);
            }
        }
    }

    public void insertOrUpdate(SkArRemitItem item, TcUser user) {
        if (PaymentTypeEnum.CASH.equals(item.getPaymentType())) {
            item.setCheckNumber(null);
        }
        if (PaymentTypeEnum.CASH.equals(item.getPaymentType2())) {
            item.setCheckNumber2(null);
        }
        if (item.getArAmount().doubleValue() >= 0) {
            if (item.getSalesDiscount() == null) {
                item.setSalesDiscount(BigDecimal.ZERO);
            }
            if (item.getSalesReturn() == null) {
                item.setSalesReturn(BigDecimal.ZERO);
            }
            if (item.getNegativeAr() == null) {
                item.setNegativeAr(BigDecimal.ZERO);
            }
            if (item.getDifferenceCharge() == null) {
                item.setDifferenceCharge((short) 0);
            }
            if (item.getAdvanceReceiptsA() == null) {
                item.setAdvanceReceiptsA(BigDecimal.ZERO);
            }
            if (item.getAdvanceReceiptsJ() == null) {
                item.setAdvanceReceiptsJ(BigDecimal.ZERO);
            }
            if (item.getAmount2() == null) {
                item.setAmount2(BigDecimal.ZERO);
            }
        }
        if (item.getId() != null) {
            item.setModifier(user);
            item.setModifytimestamp(new Date());
            edit(item);
        } else {
            item.setCreator(user);
            item.setCreatetimestamp(new Date());
            create(item);
        }
    }
}
