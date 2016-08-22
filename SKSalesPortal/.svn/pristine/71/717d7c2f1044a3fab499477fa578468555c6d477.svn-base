/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkArRemitItem;
import com.tcci.sksp.entity.ar.SkArRemitMaster;
import com.tcci.sksp.entity.ar.SkFiMasterInterface;
import com.tcci.sksp.entity.enums.BankEnum;
import com.tcci.sksp.entity.enums.RemitMasterStatusEnum;
import com.tcci.sksp.vo.CashSummaryVO;
import com.tcci.sksp.vo.CheckSummaryVO;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lynn.Huang
 */
@Stateless
public class SkArRemitMasterFacade extends AbstractFacade<SkArRemitMaster> {
    
    private Logger logger = LoggerFactory.getLogger(SkArRemitMasterFacade.class);
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public SkArRemitMasterFacade() {
        super(SkArRemitMaster.class);
    }
    
    public List<SkArRemitMaster> findRemitMasterByCriteria(RemitFilter filter) {
        List<SkArRemitMaster> list = null;
        try {
            CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery<SkArRemitMaster> cq = builder.createQuery(SkArRemitMaster.class);
            Root<SkArRemitMaster> root = cq.from(SkArRemitMaster.class);
            cq.select(root);
            List<Predicate> predicateList = new ArrayList<Predicate>();

            //sales
            if (filter.getSales() != null) {
                Predicate p = builder.equal(root.get("sapid"), filter.getSales().getCode());
                predicateList.add(p);
            }

            //createtimestamp
            if ((filter.getPayingStart() != null) && (filter.getPayingEnd() == null)) {
                Predicate p = builder.greaterThanOrEqualTo(root.get("createtimestamp").as(Date.class), filter.getPayingStart());
                predicateList.add(p);
            } else if ((filter.getPayingStart() == null) && (filter.getPayingEnd() != null)) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(filter.getPayingEnd());
                calendar.add(Calendar.DATE, 1);
                logger.debug("calendar={}", calendar.getTime());
                Predicate p = builder.lessThanOrEqualTo(root.get("createtimestamp").as(Date.class), calendar.getTime());
                predicateList.add(p);
            } else if ((filter.getPayingStart() != null) && (filter.getPayingEnd() != null)) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(filter.getPayingEnd());
                calendar.add(Calendar.DATE, 1);
                logger.debug("calendar={}", calendar.getTime());
                Predicate p = builder.between(root.get("createtimestamp").as(Date.class), filter.getPayingStart(), calendar.getTime());
                predicateList.add(p);
            }

            //remit number
            if (filter.getFromRemitNumber() == null) {
                filter.setFromRemitNumber(new Integer(0));
            }
            if (filter.getToRemitNumber() == null) {
                filter.setToRemitNumber(new Integer(0));
            }
            if (filter.getFromRemitNumber() != 0 && filter.getToRemitNumber() != 0) {
                Predicate p = builder.between(root.get("id").as(Integer.class), filter.getFromRemitNumber(), filter.getToRemitNumber());
                predicateList.add(p);
            } else if (filter.getFromRemitNumber() != 0 && filter.getToRemitNumber() == 0) {
                Predicate p = builder.greaterThanOrEqualTo(root.get("id").as(Integer.class), filter.getFromRemitNumber());
                predicateList.add(p);
            } else if (filter.getFromRemitNumber() == 0 && filter.getToRemitNumber() != 0) {
                Predicate p = builder.lessThanOrEqualTo(root.get("id").as(Integer.class), filter.getToRemitNumber());
                predicateList.add(p);
            }

            //customer
            if (filter.getSkCustomer() != null) {
                Predicate p = builder.equal(root.get("customer").as(SkCustomer.class), filter.getSkCustomer());
                predicateList.add(p);
            }

            //creator
            if (filter.getCreator() != null) {
                Predicate p = builder.equal(root.get("creator").as(TcUser.class), filter.getCreator());
                predicateList.add(p);
            }

            //status
            if (filter.getStatus() != null) {
                Predicate p = builder.equal(root.get("status").as(RemitMasterStatusEnum.class), filter.getStatus());
                predicateList.add(p);
            } else if (filter.getStatusList() != null && !filter.getStatusList().isEmpty()) {
                Predicate p = root.get("status").in(filter.getStatusList());
                predicateList.add(p);
            }

            //financeReviewer
            if (filter.getFinanceReviewer() != null) {
                Predicate p = builder.equal(root.get("financeReviewer").as(TcUser.class), filter.getFinanceReviewer());
                predicateList.add(p);
            }

            //review timestamp
            if ((filter.getReviewDateStart() != null) && (filter.getReviewDateEnd() == null)) {
                Predicate p = builder.greaterThanOrEqualTo(root.get("reviewTimestamp").as(Date.class), filter.getReviewDateStart());
                predicateList.add(p);
            } else if ((filter.getReviewDateStart() == null) && (filter.getReviewDateEnd() != null)) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(filter.getReviewDateEnd());
                calendar.add(Calendar.DATE, 1);
                logger.debug("calendar={}", calendar.getTime());
                Predicate p = builder.lessThanOrEqualTo(root.get("reviewTimestamp").as(Date.class), calendar.getTime());
                predicateList.add(p);
            } else if ((filter.getReviewDateStart() != null) && (filter.getReviewDateEnd() != null)) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(filter.getReviewDateEnd());
                calendar.add(Calendar.DATE, 1);
                logger.debug("calendar={}", calendar.getTime());
                Predicate p = builder.between(root.get("reviewTimestamp").as(Date.class), filter.getReviewDateStart(), calendar.getTime());
                predicateList.add(p);
            }
            
            if (predicateList.size() > 0) {
                Predicate[] predicates = new Predicate[predicateList.size()];
                predicateList.toArray(predicates);
                cq.where(predicates);
            }
            //先用客戶排序避免預收款沖帳錯誤
            List<Order> orders = new ArrayList<Order>();
            orders.add(builder.asc(root.get("customer").get("simpleCode")));
            orders.add(builder.asc(root.get("createtimestamp")));
            cq.orderBy(orders);
            list = getEntityManager().createQuery(cq).getResultList();
        } catch (Exception e) {
            logger.error("findRemitMasterByCriteria(), e={}", e);
        }
        return list;
    }

    // 繳款單查詢與覆核, 匯出 excel 使用
    public List<SkArRemitItem> findRemitItemByMasters(List<Long> masterIds) {
        if (null==masterIds || masterIds.isEmpty())
            return Collections.EMPTY_LIST;
        String sql = "SELECT i FROM SkArRemitItem i"
                + " WHERE i.arRemitMaster.id in :masterIds"
                + " ORDER BY i.arRemitMaster.customer.simpleCode, i.arRemitMaster.createtimestamp";
        Query q = em.createQuery(sql);
        q.setParameter("masterIds", masterIds);
        return q.getResultList();
    }
    
    public List<CashSummaryVO> getCashSummaryByCriteria(RemitFilter filter) {
        List<CashSummaryVO> cashSummaryList = new ArrayList<CashSummaryVO>();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT SUM(REMITTANCE_AMOUNT) AS AMOUNT, BANK ");
            sql.append("FROM SK_AR_REMIT_MASTER ");
            sql.append("WHERE SAPID = #sapid ");
            sql.append("  AND (#payingStart IS NULL OR CREATETIMESTAMP >= to_date(#payingStart,'YYYY/mm/dd')) ");
            sql.append("  AND (#payingEnd IS NULL OR CREATETIMESTAMP <= to_date(#payingEnd,'YYYY/mm/dd')) ");
            sql.append("GROUP BY BANK ");
            
            SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
            Query q = em.createNativeQuery(sql.toString());
            List results = q.setParameter("sapid", filter.getSales().getCode()).setParameter("payingStart", filter.getPayingStart() != null ? f.format(filter.getPayingStart()) : null).setParameter("payingEnd", filter.getPayingEnd() != null ? f.format(filter.getPayingEnd()) : null).getResultList();
            for (Object o : results) {
                Object[] v = (Object[]) o;
                int idx = 0;
                CashSummaryVO cashSummaryVO = new CashSummaryVO();
                cashSummaryVO.setCashTotal((BigDecimal) v[idx++]);
                cashSummaryVO.setBankName(BankEnum.valueOf((String) v[idx]).getDisplayName());
                cashSummaryList.add(cashSummaryVO);
            }
        } catch (Exception e) {
            logger.error("getCashSummaryByCriteria(), e = {}", e);
        }
        return cashSummaryList;
    }
    
    public List<CheckSummaryVO> getCheckSummaryByCriteria(RemitFilter filter) {
        List<CheckSummaryVO> checkSummaryList = new ArrayList<CheckSummaryVO>();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT SUM(Y.AMOUNT), Y.CHECK_NUMBER ");
            sql.append("FROM SK_AR_REMIT_MASTER X ");
            sql.append("    INNER JOIN SK_AR_REMIT_ITEM Y ON X.ID = Y.AR_REMIT_MASTER ");
            sql.append("WHERE X.SAPID = #sapid AND Y.CHECK_NUMBER IS NOT NULL AND Y.PAYMENT_TYPE = 'CHECK' ");
            sql.append("  AND (#payingStart IS NULL OR X.CREATETIMESTAMP >= to_date(#payingStart,'YYYY/mm/dd')) ");
            sql.append("  AND (#payingEnd IS NULL OR X.CREATETIMESTAMP <= to_date(#payingEnd,'YYYY/mm/dd')) ");
            sql.append("GROUP BY Y.CHECK_NUMBER ");
            
            SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
            Query q = em.createNativeQuery(sql.toString());
            List results = q.setParameter("sapid", filter.getSales().getCode()).setParameter("payingStart", filter.getPayingStart() != null ? f.format(filter.getPayingStart()) : null).setParameter("payingEnd", filter.getPayingEnd() != null ? f.format(filter.getPayingEnd()) : null).getResultList();
            for (Object o : results) {
                Object[] v = (Object[]) o;
                int idx = 0;
                CheckSummaryVO checkSummaryVO = new CheckSummaryVO();
                checkSummaryVO.setCheckTotal((BigDecimal) v[idx++]);
                checkSummaryVO.setCheckNumber((String) v[idx]);
                checkSummaryList.add(checkSummaryVO);
            }
        } catch (Exception e) {
            logger.error("getCheckSummaryByCriteria(), e = {}", e);
        }
        return checkSummaryList;
    }
    
    public void insertOrUpdate(SkArRemitMaster master, TcUser user) {
        if (master.getId() != null) {
            master.setModifier(user);
            master.setModifytimestamp(new Date());
            edit(master);
        } else {
            master.setCreator(user);
            master.setCreatetimestamp(new Date());
            master.setStatus(RemitMasterStatusEnum.NOT_YET);
            create(master);
        }
    }
    
    public int getRemitMinId(Date from, Date to) {
        return getFirstRemitId(from, to, "ASC");
    }
    
    public int getRemitMaxId(Date from, Date to) {
        return getFirstRemitId(from, to, "DESC");
    }
    
    private int getFirstRemitId(Date from, Date to, String order) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery(SkArRemitMaster.class);
        Root<SkArRemitMaster> root = cq.from(SkArRemitMaster.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (from != null) {
            Predicate p = builder.greaterThanOrEqualTo(root.get("createtimestamp").as(Date.class), from);
            predicateList.add(p);
        }
        if (to != null) {
            Predicate p = builder.lessThanOrEqualTo(root.get("createtimestamp").as(Date.class), to);
            predicateList.add(p);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicates = predicateList.toArray(predicates);
            cq.where(predicates);
        }
        if (order.equals("ASC")) {
            cq.orderBy(builder.asc(root.get("id")));
        } else if (order.equals("DESC")) {
            cq.orderBy(builder.desc(root.get("id")));
        }
        List<SkArRemitMaster> masters = getEntityManager().createQuery(cq).setMaxResults(1).getResultList();
        if (masters.size() > 0) {
            return masters.get(0).getId().intValue();
        } else {
            return 0;
        }
    }
    
    public List<SkArRemitMaster> findByInterface(SkFiMasterInterface fiInterface) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(SkArRemitMaster.class);
        Root<SkArRemitMaster> root = cq.from(SkArRemitMaster.class);
        Predicate p1 = cb.equal(root.get("fiInterface"),fiInterface);
        cq.where(p1);
        return getEntityManager().createQuery(cq).getResultList();
    }
}
