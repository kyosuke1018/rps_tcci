/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkAdvanceRemit;
import com.tcci.sksp.entity.ar.SkArRemitItem;
import com.tcci.sksp.entity.ar.SkArRemitMaster;
import com.tcci.sksp.entity.enums.AdvanceRemitTypeEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lynn.Huang
 */
@Stateless
public class SkAdvanceRemitFacade extends AbstractFacade<SkAdvanceRemit> {

    private Logger logger = LoggerFactory.getLogger(SkAdvanceRemitFacade.class);
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkAdvanceRemitFacade() {
        super(SkAdvanceRemit.class);
    }

    public Long getAdvancedAmountByCustomer(SkCustomer customer, AdvanceRemitTypeEnum advanceRemitType, String amountType) {
        try {
            if (customer != null && advanceRemitType != null) {
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT SUM(a.amount) ");
                sql.append("FROM SkAdvanceRemit a ");
                sql.append("WHERE a.customer = :customer");
                sql.append("  AND a.remitType = :remitType");
                if (StringUtils.isNotEmpty(amountType)) {
                    if ("greaterThan".equals(amountType)) {
                        sql.append("  AND a.amount > 0");
                    } else if ("lessThan".equals(amountType)) {
                        sql.append("  AND a.amount < 0");
                    }
                }
//                sql.append("  AND a.amount < 0");
                Query query = em.createQuery(sql.toString());
                query.setParameter("customer", customer);
                query.setParameter("remitType", advanceRemitType);
                BigDecimal totalAmount = (BigDecimal) query.getSingleResult();
                if (totalAmount != null) {
                    return totalAmount.longValue();
                }
            }
        } catch (Exception e) {
            logger.debug("getAdvancedAmountByCustomer(), e={}", e);
        }
        return Long.valueOf(0);
    }

    public Long getAdvancedAmountByCustomerGroup(SkCustomer customer, AdvanceRemitTypeEnum advanceRemitType) {
        try {
            if (customer != null && advanceRemitType != null) {
                CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
                CriteriaQuery cq = cb.createQuery(SkAdvanceRemit.class);
                Root<SkAdvanceRemit> root = cq.from(SkAdvanceRemit.class);
                Expression<BigDecimal> totalAmountExpression = cb.sum((Expression<BigDecimal>) root.get("amount").as(BigDecimal.class));
                cq.select(totalAmountExpression);
                cq.from(SkAdvanceRemit.class);
                List<Predicate> predicateList = new ArrayList<Predicate>();

                //目前只支援一層的母子關係.
                List<Long> customerIds = new ArrayList<Long>();
                customerIds.add(customer.getId());

                if (customer.getParentCustomer() != null) {
                    customerIds.add(customer.getParentCustomer().getId());
                }
                Predicate p1 = root.get("customer").get("id").in(customerIds);
                predicateList.add(p1);
                Predicate p2 = cb.equal(root.get("remitType").as(AdvanceRemitTypeEnum.class), advanceRemitType);
                predicateList.add(p2);
//                Predicate p3 = cb.le(root.get("amount").as(BigDecimal.class), 0);
//                predicateList.add(p3);

                Predicate[] predicates = new Predicate[predicateList.size()];
                for (int i = 0; i < predicateList.size(); i++) {
                    predicates[i] = predicateList.get(i);
                }
                cq.where(predicates);
                BigDecimal totalAmount = (BigDecimal) getEntityManager().createQuery(cq).getSingleResult();
                if (totalAmount != null) {
                    return totalAmount.longValue();
                }
            }
        } catch (Exception e) {
            logger.debug("getAdvancedAmountByCustomerGroup(), e={}", e);
        }
        return Long.valueOf(0);
    }

    public List<SkAdvanceRemit> findByCustomer(SkCustomer customer, AdvanceRemitTypeEnum advanceRemitType, String amountType) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(SkAdvanceRemit.class);
        Root<SkAdvanceRemit> root = cq.from(SkAdvanceRemit.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p1 = cb.equal(root.get("customer").as(SkCustomer.class), customer);
        predicateList.add(p1);
        Predicate p2 = cb.equal(root.get("remitType").as(AdvanceRemitTypeEnum.class), advanceRemitType);
        predicateList.add(p2);
        if (StringUtils.isNotEmpty(amountType)) {
            if ("greaterThan".equals(amountType)) {
                Predicate p3 = cb.ge(root.get("amount").as(BigDecimal.class), 0);
                predicateList.add(p3);
            } else if ("lessThan".equals(amountType)) {
                Predicate p3 = cb.le(root.get("amount").as(BigDecimal.class), 0);
                predicateList.add(p3);
            }
        }
        Predicate[] predicates = new Predicate[predicateList.size()];
        for (int i = 0; i < predicateList.size(); i++) {
            predicates[i] = predicateList.get(i);
        }
        cq.where(predicates);
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<SkAdvanceRemit> findByCustomerGroup(SkCustomer customer, AdvanceRemitTypeEnum advanceRemitType, String amountType) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(SkAdvanceRemit.class);
        Root<SkAdvanceRemit> root = cq.from(SkAdvanceRemit.class);

        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();

        //目前只支援一層的母子關係.
        List<Long> customerIds = new ArrayList<Long>();
        customerIds.add(customer.getId());

        if (customer.getParentCustomer() != null) {
            customerIds.add(customer.getParentCustomer().getId());
        }
        Predicate p1 = root.get("customer").get("id").in(customerIds);
        predicateList.add(p1);
        Predicate p2 = cb.equal(root.get("remitType").as(AdvanceRemitTypeEnum.class), advanceRemitType);
        predicateList.add(p2);
        if (StringUtils.isNotEmpty(amountType)) {
            if ("greaterThan".equals(amountType)) {
                Predicate p3 = cb.ge(root.get("amount").as(BigDecimal.class), 0);
                predicateList.add(p3);
            } else if ("lessThan".equals(amountType)) {
                Predicate p3 = cb.le(root.get("amount").as(BigDecimal.class), 0);
                predicateList.add(p3);
            }
        }
        Predicate[] predicates = new Predicate[predicateList.size()];
        for (int i = 0; i < predicateList.size(); i++) {
            predicates[i] = predicateList.get(i);
        }
        cq.where(predicates);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * 1.sort by remit customer in ascending. 2.sort by remit timestamp in
     * ascending (if have). 3.sort by amount in ascending (if advance remit
     * without remit timestamp).
     *
     * @param advanceRemits list need to be sorting.
     * @return sorted advance remit list.
     */
    public List<SkAdvanceRemit> sortAdvanceRemit(List<SkAdvanceRemit> advanceRemits) {
        Collections.sort(advanceRemits, new Comparator<SkAdvanceRemit>() {

            public int compare(SkAdvanceRemit s1, SkAdvanceRemit s2) {
                if (s1.getCustomer().equals(s2.getCustomer())) {
                    if (s1.getRemitTimestamp() == null && s2.getRemitTimestamp() == null) {
                        logger.debug("return amount compare");
                        return s1.getAmount().abs().compareTo(s2.getAmount().abs());
                    } else if (s1.getRemitTimestamp() == null) {
                        logger.debug("return 0");
                        return 0;
                    } else if (s2.getRemitTimestamp() == null) {
                        logger.debug("return 1");
                        return 1;
                    } else {
                        logger.debug("return remitTImestamp compare");
                        return s1.getRemitTimestamp().compareTo(s2.getRemitTimestamp());
                    }
                } else {
                    return s1.getCustomer().getCode().compareTo(s2.getCustomer().getCode());
                }
            }
        });
        return advanceRemits;
    }

    /**
     *
     * @param advanceRemits original advance remit list.
     * @param arRemitMaster minus remit.
     * @return result advance remit list.
     */
    public HashMap<String, List<SkAdvanceRemit>> advanceRemitMinusRemit(HashMap<String, List<SkAdvanceRemit>> advanceRemits, SkArRemitMaster arRemitMaster) {
        for (SkArRemitItem remitItem : arRemitMaster.getSkArRemitItemCollection()) {
            advanceRemits = advanceRemitMinusRemitItem(advanceRemits, remitItem);
        }
        return advanceRemits;
    }

    /**
     *
     * @param advanceRemits original advance remit list.
     * @param arRemitMaster minus remit.
     * @return result advance remit list.
     */
    public HashMap<String, List<SkAdvanceRemit>> advanceRemitMinusRemitItem(HashMap<String, List<SkAdvanceRemit>> advanceRemits, SkArRemitItem arRemitItem) {
        List<SkAdvanceRemit> advanceRemitAs = advanceRemits.get("advanceRemitA");
        //<editor-fold defaultstate="collapsed" desc="debug messages">
        if (advanceRemitAs != null && !advanceRemitAs.isEmpty()) {
            logger.debug("advnceRemitA.size()= {} = [", advanceRemitAs.size());
            int item = 1;
            for (SkAdvanceRemit advanceRemitA : advanceRemitAs) {
                logger.debug("item {}={}", new Object[]{item, advanceRemitA.getAmount().abs()});
                item++;
            }
            logger.debug("]");
        }
        //</editor-fold>
        List<SkAdvanceRemit> groupAdvanceRemitAs = advanceRemits.get("groupAdvanceRemitA");
        //<editor-fold defaultstate="collapsed" desc="debug messages">
        if (advanceRemitAs != null && !advanceRemitAs.isEmpty()) {
            logger.debug("groupAdvnceRemitA.size()= {} = [", groupAdvanceRemitAs.size());
            int item = 1;
            for (SkAdvanceRemit groupAdvanceRemitA : groupAdvanceRemitAs) {
                logger.debug("item {}={}", new Object[]{item, groupAdvanceRemitA.getAmount().abs()});
                item++;
            }
            logger.debug("]");
        }
        //</editor-fold>
        List<SkAdvanceRemit> advanceRemitJs = advanceRemits.get("advanceRemitJ");
        //<editor-fold defaultstate="collapsed" desc="debug messages">
        if (advanceRemitJs != null && !advanceRemitJs.isEmpty()) {
            logger.debug("advanceRemitJs.size()= {} = [", advanceRemitJs.size());
            int item = 1;
            for (SkAdvanceRemit advanceRemitJ : advanceRemitJs) {
                logger.debug("item {}={}", new Object[]{item, advanceRemitJ.getAmount().abs()});
                item++;
            }
            logger.debug("]");
        }
        //</editor-fold>
        List<SkAdvanceRemit> groupAdvanceRemitJs = advanceRemits.get("groupAdvanceRemitJ");
        //<editor-fold defaultstate="collapsed" desc="debug messages">
        if (groupAdvanceRemitJs != null && !groupAdvanceRemitJs.isEmpty()) {
            logger.debug("groupAdvanceRemitJs.size()= {} = [", groupAdvanceRemitJs.size());
            int item = 1;
            for (SkAdvanceRemit groupAdvanceRemitJ : groupAdvanceRemitJs) {
                logger.debug("item {}={}", new Object[]{item, groupAdvanceRemitJ.getAmount().abs()});
                item++;
            }
            logger.debug("]");
        }
        //</editor-fold>
        //先扣自己的預收A
        BigDecimal itemAdvanceRemitA = arRemitItem.getAdvanceReceiptsA();
        logger.debug("itemAdvanceRemitA={}", itemAdvanceRemitA);
        BigDecimal itemAdvanceRemitJ = arRemitItem.getAdvanceReceiptsJ();
        logger.debug("itemAdvanceRemitJ={}", itemAdvanceRemitJ);
        if (advanceRemitAs != null && !advanceRemitAs.isEmpty()) {
            if (itemAdvanceRemitA != null && itemAdvanceRemitA.doubleValue() > 0) {
                List<SkAdvanceRemit> newAdvanceRemitAs = new ArrayList<SkAdvanceRemit>();
                for (SkAdvanceRemit advanceRemitA : advanceRemitAs) {
                    //表示該筆繳款單明細的預收完全沖抵該筆預收.
                    if (itemAdvanceRemitA.doubleValue() > advanceRemitA.getAmount().abs().doubleValue()) {
                        //將該筆預收的金額由繳款單明細的預收中扣除 (因為預收為負數, 所以直接用 add 即可.
                        itemAdvanceRemitA = itemAdvanceRemitA.add(advanceRemitA.getAmount());
                        //因該筆預收金額已被繳款單明細沖抵, 所以不需要放回餘額清單中.
                        continue;
                    } else {
                        advanceRemitA.setAmount(advanceRemitA.getAmount().add(itemAdvanceRemitA));
                        itemAdvanceRemitA = new BigDecimal(0);
                    }
                    if (advanceRemitA.getAmount().doubleValue() < 0) {
                        newAdvanceRemitAs.add(advanceRemitA);
                    }
                }
                advanceRemits.put("advanceRemitA", newAdvanceRemitAs);
            }
        }
        //再扣 parentCustomer 的預收A
        if (groupAdvanceRemitAs != null && !groupAdvanceRemitAs.isEmpty()) {
            if (itemAdvanceRemitA != null && itemAdvanceRemitA.doubleValue() > 0) {
                List<SkAdvanceRemit> newGroupAdvanceRemitAs = new ArrayList<SkAdvanceRemit>();
                for (SkAdvanceRemit groupAdvanceRemitA : groupAdvanceRemitAs) {
                    //表示該筆繳款單明細的預收完全沖抵該筆預收.
                    if (itemAdvanceRemitA.doubleValue() > groupAdvanceRemitA.getAmount().abs().doubleValue()) {
                        //將該筆預收的金額由繳款單明細的預收中扣除 (因為預收為負數, 所以直接用 add 即可.
                        itemAdvanceRemitA = itemAdvanceRemitA.add(groupAdvanceRemitA.getAmount());
                        //因該筆預收金額已被繳款單明細沖抵, 所以不需要放回餘額清單中.
                        continue;
                    } else {
                        groupAdvanceRemitA.setAmount(groupAdvanceRemitA.getAmount().add(itemAdvanceRemitA));
                        itemAdvanceRemitA = new BigDecimal(0);
                    }
                    if (groupAdvanceRemitA.getAmount().doubleValue() < 0) {
                        newGroupAdvanceRemitAs.add(groupAdvanceRemitA);
                    }
                }
                advanceRemits.put("groupAdvanceRemitA", newGroupAdvanceRemitAs);
            }
        }
        //先扣自己的預收J
        if (advanceRemitJs != null && !advanceRemitJs.isEmpty()) {
            if (itemAdvanceRemitJ != null && itemAdvanceRemitJ.doubleValue() > 0) {
                List<SkAdvanceRemit> newAdvanceRemitJs = new ArrayList<SkAdvanceRemit>();
                for (SkAdvanceRemit advanceRemitJ : advanceRemitJs) {
                    //表示該筆繳款單明細的預收完全沖抵該筆預收.
                    if (itemAdvanceRemitJ.doubleValue() > advanceRemitJ.getAmount().abs().doubleValue()) {
                        //將該筆預收的金額由繳款單明細的預收中扣除 (因為預收為負數, 所以直接用 add 即可.
                        itemAdvanceRemitJ = itemAdvanceRemitJ.add(advanceRemitJ.getAmount());
                        //因該筆預收金額已被繳款單明細沖抵, 所以不需要放回餘額清單中.
                        continue;
                    } else {
                        advanceRemitJ.setAmount(advanceRemitJ.getAmount().add(itemAdvanceRemitJ));
                        itemAdvanceRemitJ = new BigDecimal(0);
                    }
                    if (advanceRemitJ.getAmount().doubleValue() < 0) {
                        newAdvanceRemitJs.add(advanceRemitJ);
                    }
                }
                //<editor-fold defaultstate="collapsed" desc="debug messages">
                logger.debug("newAdvanceRemitJs.size()={}", newAdvanceRemitJs.size());
                //</editor-fold>
                advanceRemits.put("advanceRemitJ", newAdvanceRemitJs);
            }
        }
        //再扣 parentCustomer 的預收J
        if (groupAdvanceRemitJs != null && !groupAdvanceRemitJs.isEmpty()) {
            if (itemAdvanceRemitJ != null && itemAdvanceRemitJ.doubleValue() > 0) {
                List<SkAdvanceRemit> newGroupAdvanceRemitJs = new ArrayList<SkAdvanceRemit>();
                for (SkAdvanceRemit groupAdvanceRemitJ : groupAdvanceRemitJs) {
                    //表示該筆繳款單明細的預收完全沖抵該筆預收.
                    if (itemAdvanceRemitJ.doubleValue() > groupAdvanceRemitJ.getAmount().abs().doubleValue()) {
                        //將該筆預收的金額由繳款單明細的預收中扣除 (因為預收為負數, 所以直接用 add 即可.
                        itemAdvanceRemitJ = itemAdvanceRemitJ.add(groupAdvanceRemitJ.getAmount());
                        //因該筆預收金額已被繳款單明細沖抵, 所以不需要放回餘額清單中.
                        continue;
                    } else {
                        groupAdvanceRemitJ.setAmount(groupAdvanceRemitJ.getAmount().add(itemAdvanceRemitJ));
                        itemAdvanceRemitJ = new BigDecimal(0);
                    }
                    if (groupAdvanceRemitJ.getAmount().doubleValue() < 0) {
                        newGroupAdvanceRemitJs.add(groupAdvanceRemitJ);
                    }
                }
                advanceRemits.put("groupAdvanceRemitJ", newGroupAdvanceRemitJs);
            }
        }
        //<editor-fold defaultstate="collapsed" desc="debug messages">
        advanceRemitAs = advanceRemits.get("advanceRemitA");
        if (advanceRemitAs != null && !advanceRemitAs.isEmpty()) {
            logger.debug("advnceRemitA.size()= {} = [", advanceRemitAs.size());
            int item = 1;
            for (SkAdvanceRemit advanceRemitA : advanceRemitAs) {
                logger.debug("item {}={}", new Object[]{item, advanceRemitA.getAmount().abs()});
                item++;
            }
            logger.debug("]");
        }
        groupAdvanceRemitAs = advanceRemits.get("groupAdvanceRemitA");
        if (advanceRemitAs != null && !advanceRemitAs.isEmpty()) {
            logger.debug("groupAdvnceRemitA.size()= {} = [", groupAdvanceRemitAs.size());
            int item = 1;
            for (SkAdvanceRemit groupAdvanceRemitA : groupAdvanceRemitAs) {
                logger.debug("item {}={}", new Object[]{item, groupAdvanceRemitA.getAmount().abs()});
                item++;
            }
            logger.debug("]");
        }
        advanceRemitJs = advanceRemits.get("advanceRemitJ");
        if (advanceRemitJs != null && !advanceRemitJs.isEmpty()) {
            logger.debug("advanceRemitJs.size()= {} = [", advanceRemitJs.size());
            int item = 1;
            for (SkAdvanceRemit advanceRemitJ : advanceRemitJs) {
                logger.debug("item {}={}", new Object[]{item, advanceRemitJ.getAmount().abs()});
                item++;
            }
            logger.debug("]");
        }
        groupAdvanceRemitJs = advanceRemits.get("groupAdvanceRemitJ");
        if (groupAdvanceRemitJs != null && !groupAdvanceRemitJs.isEmpty()) {
            logger.debug("groupAdvanceRemitJs.size()= {} = [", groupAdvanceRemitJs.size());
            int item = 1;
            for (SkAdvanceRemit groupAdvanceRemitJ : groupAdvanceRemitJs) {
                logger.debug("item {}={}", new Object[]{item, groupAdvanceRemitJ.getAmount().abs()});
                item++;
            }
            logger.debug("]");
        }
        //</editor-fold>
        return advanceRemits;
    }

    public List<SkAdvanceRemit> minusUsedAdvanceRemit(List<SkAdvanceRemit> advanceRemitList, SkCustomer customer, AdvanceRemitTypeEnum remitType) {
        BigDecimal usedAdvanceRemitAmount = BigDecimal.valueOf(getAdvancedAmountByCustomer(customer, remitType, "greaterThan"));
        //<editor-fold defaultstate="collapsed" desc="debug message">
        logger.debug("usedAdvanceRemitAmount={}", usedAdvanceRemitAmount);
        //</editor-fold>
        List<SkAdvanceRemit> newAdvanceRemitList = new ArrayList<SkAdvanceRemit>();
        newAdvanceRemitList.addAll(advanceRemitList);
        if (usedAdvanceRemitAmount != null && usedAdvanceRemitAmount.doubleValue() > 0) {
            for (SkAdvanceRemit advanceRemit : advanceRemitList) {
                usedAdvanceRemitAmount = usedAdvanceRemitAmount.add(advanceRemit.getAmount()); //用 + 的即可, 因為可沖的預收為負數.
                if (usedAdvanceRemitAmount.doubleValue() > 0) {
                    logger.debug("remove advanceRemit");
                    newAdvanceRemitList.remove(advanceRemit);
                } else {
                    logger.debug("remove & add advanceRemit");
                    newAdvanceRemitList.remove(advanceRemit);
                    advanceRemit.setAmount(usedAdvanceRemitAmount);
                    newAdvanceRemitList.add(advanceRemit);
                    break;
                }
            }
        }
        return newAdvanceRemitList;
    }
}
