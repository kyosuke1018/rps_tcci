/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkSalesOrderMaster;
import com.tcci.sksp.entity.enums.PaymentTermEnum;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.vo.CustomerOrderYearVO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
@Stateless
public class SkSalesOrderMasterFacade extends AbstractFacade<SkSalesOrderMaster> {

    @EJB
    SkSalesMemberFacade salesMemberFacade;
    protected final static Logger logger = LoggerFactory.getLogger(SkSalesOrderMasterFacade.class);
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkSalesOrderMasterFacade() {
        super(SkSalesOrderMaster.class);
    }

    public List<SkSalesOrderMaster> findByOrderOrInvoiceNumber(String number) {
        logger.debug("findBYOrderOrInvocieNumber(), number={}", number);
        HashMap<SkSalesOrderMaster, SkSalesOrderMaster> uniqueSalesOrders = new HashMap<SkSalesOrderMaster, SkSalesOrderMaster>();
        if (StringUtils.isEmpty(number)) {
            return null;
        }
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<SkSalesOrderMaster> cq = builder.createQuery(SkSalesOrderMaster.class);
        Root<SkSalesOrderMaster> root = cq.from(SkSalesOrderMaster.class);
        //find by order number first.
        cq.select(root);
        //--Begin--Modified by nEO Fu on 20120705 bug fixed for 1 invoice n order number (filter out order number prefix with 7960).
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p1 = builder.like(root.get("orderNumber").as(String.class), number.toUpperCase() + "%");
        predicateList.add(p1);
        // Jimmy 20140617, 僅保留 5960 或 7969 開頭的訂單號碼
        // Predicate p2 = builder.notLike(root.get("orderNumber").as(String.class),"7960%");
        // predicateList.add(p2);
        Predicate p5960  = builder.like(root.get("orderNumber").as(String.class),"5960%");
        Predicate p7969  = builder.like(root.get("orderNumber").as(String.class),"7969%");
        Predicate p2 = builder.or(p5960, p7969);
        predicateList.add(p2);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        //---End---Modified by nEO Fu on 20120705 bug fixed for 1 invoice n order number (filter out order number prefix with 7960).
        List<SkSalesOrderMaster> salesOrdersByOrderNumber = getEntityManager().createQuery(cq).getResultList();
        logger.debug("salesOrdersByOrderNumber.size()=" + salesOrdersByOrderNumber.size());
        for (SkSalesOrderMaster salesOrderByOrderNumber : salesOrdersByOrderNumber) {
            uniqueSalesOrders.put(salesOrderByOrderNumber, salesOrderByOrderNumber);
        }

        //then find by invoice number.
        cq.select(root);
        //--Begin--Modified by nEO Fu on 20120705 bug fixed for 1 invoice n order number (filter out order number prefix with 7960).
        predicateList.clear();
        p1 = builder.like(root.get("invoiceNumber").as(String.class), number.toUpperCase() + "%");
        predicateList.add(p1);
        // Jimmy 20140617, 僅保留 5960 或 7969 開頭的訂單號碼
        // p2 = builder.notLike(root.get("orderNumber").as(String.class),"7960%");
        p5960  = builder.like(root.get("orderNumber").as(String.class),"5960%");
        p7969  = builder.like(root.get("orderNumber").as(String.class),"7969%");
        p2 = builder.or(p5960, p7969);
        predicateList.add(p2);
        predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        //---End---Modified by nEO Fu on 20120705 bug fixed for 1 invoice n order number (filter out order number prefix with 7960).
        List<SkSalesOrderMaster> salesOrdersByInvoiceNumber = getEntityManager().createQuery(cq).getResultList();
        logger.debug("salesOrdersByInvoiceNumber.size()=" + salesOrdersByInvoiceNumber.size());
        for (SkSalesOrderMaster salesOrderByInvoiceNumber : salesOrdersByInvoiceNumber) {
            uniqueSalesOrders.put(salesOrderByInvoiceNumber, salesOrderByInvoiceNumber);
        }
        List<SkSalesOrderMaster> resultSalesOrders = new ArrayList<SkSalesOrderMaster>();
        resultSalesOrders.addAll(uniqueSalesOrders.values());
        logger.debug("resultSalesOrders.size()=" + resultSalesOrders.size());
        return resultSalesOrders;
    }

    public List<SkSalesOrderMaster> findByCriteria(TcUser user, SkCustomer customer, Date startTime, Date endTime, PaymentTermEnum paymentTerm) {
        SkSalesMember member = salesMemberFacade.findByMember(user);
        return findByCriteria(member, customer, startTime, endTime, paymentTerm);
    }

    public List<SkSalesOrderMaster> findByCriteria(SkSalesMember member, SkCustomer customer, Date startTime, Date endTime, PaymentTermEnum paymentTerm) {
        List<SkSalesOrderMaster> list = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<SkSalesOrderMaster> cq = builder.createQuery(SkSalesOrderMaster.class);
        Root<SkSalesOrderMaster> root = cq.from(SkSalesOrderMaster.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (member != null) {
            //Predicate p = builder.equal(root.get("sapid"), member.getCode());
            Predicate p = builder.equal(root.get("customer").get("sapid"), member.getCode());
            predicateList.add(p);
        }
        if (customer != null) {
            Predicate p = builder.equal(root.get("customer"), customer);
            predicateList.add(p);
        }
        if ((startTime != null) && (endTime == null)) {
            Predicate p = builder.greaterThanOrEqualTo(root.get("invoiceTimestamp").as(Date.class), startTime);
            predicateList.add(p);
        } else if ((startTime == null) && (endTime != null)) {
            Predicate p = builder.lessThanOrEqualTo(root.get("invoiceTimestamp").as(Date.class), endTime);
            predicateList.add(p);
        } else if ((startTime != null) && (endTime != null)) {
            Predicate p = builder.between(root.get("invoiceTimestamp").as(Date.class), startTime, endTime);
            predicateList.add(p);
        }
        if (paymentTerm != null) {
            Predicate p = builder.equal(root.get("customer").get("paymentTerm").as(String.class), paymentTerm.toString());
            predicateList.add(p);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        //cq.orderBy(builder.asc(root.get("orderNumber")), builder.asc(root.get("invoiceTimestamp")));
        cq.orderBy(builder.asc(root.get("customer").get("code")), builder.asc(root.get("orderNumber")));
        list = getEntityManager().createQuery(cq).getResultList();
        return list;
    }

    public List<CustomerOrderYearVO> findByCriteria(SkCustomer skCustomer, String year) {
        List<CustomerOrderYearVO> customerOrderYearVOList = new ArrayList<CustomerOrderYearVO>();
        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT AA.PRODUCT_NUMBER, PM.NAME, ");
            sql.append("T1.QTY AS QTY01, T1.AMT AS AMT01, T2.QTY AS QTY02, T2.AMT AS AMT02, T3.QTY AS QTY03, T3.AMT AS AMT03, ");
            sql.append("T4.QTY AS QTY04, T4.AMT AS AMT04, T5.QTY AS QTY05, T5.AMT AS AMT05, T6.QTY AS QTY06, T6.AMT AS AMT06, ");
            sql.append("T7.QTY AS QTY07, T7.AMT AS AMT07, T8.QTY AS QTY08, T8.AMT AS AMT08, T9.QTY AS QTY09, T9.AMT AS AMT09, ");
            sql.append("T10.QTY AS QTY10, T10.AMT AS AMT10, T11.QTY AS QTY11, T11.AMT AS AMT11, T12.QTY AS QTY12, T12.AMT AS AMT12 FROM ");
            sql.append("(SELECT DISTINCT PRODUCT_NUMBER FROM ");
            sql.append("(SELECT  ID,INVOICE_TIMESTAMP,CUSTOMER FROM SK_SALES_ORDER_MASTER ");
            sql.append("WHERE to_char(INVOICE_TIMESTAMP,'YYYY')=#year AND CUSTOMER=#customer ) A ");
            sql.append("JOIN ");
            sql.append("(SELECT * FROM SK_SALES_ORDER_DETAIL ) B ");
            sql.append("ON A.ID=B.SALES_ORDER GROUP BY PRODUCT_NUMBER ) AA ");


            for (Integer i = 1; i <= 12; i++) {
                sql.append("LEFT JOIN ");
                sql.append("(SELECT PRODUCT_NUMBER,SUM(QUANTITY) QTY, SUM(PRICE) AMT FROM  ");
                sql.append("(SELECT  ID,INVOICE_TIMESTAMP,CUSTOMER FROM SK_SALES_ORDER_MASTER ");
                sql.append("WHERE to_char(INVOICE_TIMESTAMP,'YYYYMM') = #year");
                sql.append(i);
                sql.append(" ) A");
                sql.append(i);
                sql.append(" JOIN  ");
                sql.append("(SELECT * FROM SK_SALES_ORDER_DETAIL ) B");
                sql.append(i);
                sql.append(" ON A");
                sql.append(i);
                sql.append(".ID=B");
                sql.append(i);
                sql.append(".SALES_ORDER GROUP BY PRODUCT_NUMBER ) T");
                sql.append(i);
                sql.append(" ON AA.PRODUCT_NUMBER=T");
                sql.append(i);
                sql.append(".PRODUCT_NUMBER ");
            }

            sql.append("LEFT JOIN SK_PRODUCT_MASTER PM ON AA.PRODUCT_NUMBER = PM.CODE ");
            sql.append("ORDER BY AA.PRODUCT_NUMBER ");

            Query q = em.createNativeQuery(sql.toString());
            List results = q.setParameter("year", year).setParameter("customer", skCustomer.getId()).setParameter("year1", year + "01").setParameter("year2", year + "02").setParameter("year3", year + "03").setParameter("year4", year + "04").setParameter("year5", year + "05").setParameter("year6", year + "06").setParameter("year7", year + "07").setParameter("year8", year + "08").setParameter("year9", year + "09").setParameter("year10", year + "10").setParameter("year11", year + "11").setParameter("year12", year + "12").getResultList();
            if (results.size() <= 0) {
                return customerOrderYearVOList;
            }


            BigDecimal janQtyTotal = BigDecimal.ZERO;
            BigDecimal janAmtTotal = BigDecimal.ZERO;
            BigDecimal febQtyTotal = BigDecimal.ZERO;
            BigDecimal febAmtTotal = BigDecimal.ZERO;
            BigDecimal marQtyTotal = BigDecimal.ZERO;
            BigDecimal marAmtTotal = BigDecimal.ZERO;
            BigDecimal aprQtyTotal = BigDecimal.ZERO;
            BigDecimal aprAmtTotal = BigDecimal.ZERO;
            BigDecimal mayQtyTotal = BigDecimal.ZERO;
            BigDecimal mayAmtTotal = BigDecimal.ZERO;
            BigDecimal junQtyTotal = BigDecimal.ZERO;
            BigDecimal junAmtTotal = BigDecimal.ZERO;
            BigDecimal julQtyTotal = BigDecimal.ZERO;
            BigDecimal julAmtTotal = BigDecimal.ZERO;
            BigDecimal augQtyTotal = BigDecimal.ZERO;
            BigDecimal augAmtTotal = BigDecimal.ZERO;
            BigDecimal sepQtyTotal = BigDecimal.ZERO;
            BigDecimal sepAmtTotal = BigDecimal.ZERO;
            BigDecimal octQtyTotal = BigDecimal.ZERO;
            BigDecimal octAmtTotal = BigDecimal.ZERO;
            BigDecimal novQtyTotal = BigDecimal.ZERO;
            BigDecimal novAmtTotal = BigDecimal.ZERO;
            BigDecimal decQtyTotal = BigDecimal.ZERO;
            BigDecimal decAmtTotal = BigDecimal.ZERO;
            for (Object o : results) {
                Object[] v = (Object[]) o;
                CustomerOrderYearVO vo = new CustomerOrderYearVO();
                vo.setProduct((String) v[0]);
                //數量 
                BigDecimal productQtyTotal = BigDecimal.ZERO;
                vo.setLabel("數量");
                int i = 1;
                i++;
                vo.setJan((BigDecimal) v[i++]);
                productQtyTotal = productQtyTotal.add(vo.getJan() == null ? BigDecimal.ZERO : vo.getJan());
                janQtyTotal = janQtyTotal.add(vo.getJan() == null ? BigDecimal.ZERO : vo.getJan());
                i++;
                vo.setFeb((BigDecimal) v[i++]);
                productQtyTotal = productQtyTotal.add(vo.getFeb() == null ? BigDecimal.ZERO : vo.getFeb());
                febQtyTotal = febQtyTotal.add(vo.getFeb() == null ? BigDecimal.ZERO : vo.getFeb());
                i++;
                vo.setMar((BigDecimal) v[i++]);
                productQtyTotal = productQtyTotal.add(vo.getMar() == null ? BigDecimal.ZERO : vo.getMar());
                marQtyTotal = marQtyTotal.add(vo.getMar() == null ? BigDecimal.ZERO : vo.getMar());
                i++;
                vo.setApr((BigDecimal) v[i++]);
                productQtyTotal = productQtyTotal.add(vo.getApr() == null ? BigDecimal.ZERO : vo.getApr());
                aprQtyTotal = aprQtyTotal.add(vo.getApr() == null ? BigDecimal.ZERO : vo.getApr());
                i++;
                vo.setMay((BigDecimal) v[i++]);
                productQtyTotal = productQtyTotal.add(vo.getMay() == null ? BigDecimal.ZERO : vo.getMay());
                mayQtyTotal = mayQtyTotal.add(vo.getMay() == null ? BigDecimal.ZERO : vo.getMay());
                i++;
                vo.setJun((BigDecimal) v[i++]);
                productQtyTotal = productQtyTotal.add(vo.getJun() == null ? BigDecimal.ZERO : vo.getJun());
                junQtyTotal = junQtyTotal.add(vo.getJun() == null ? BigDecimal.ZERO : vo.getJun());
                i++;
                vo.setJul((BigDecimal) v[i++]);
                productQtyTotal = productQtyTotal.add(vo.getJul() == null ? BigDecimal.ZERO : vo.getJul());
                julQtyTotal = julQtyTotal.add(vo.getJul() == null ? BigDecimal.ZERO : vo.getJul());
                i++;
                vo.setAug((BigDecimal) v[i++]);
                productQtyTotal = productQtyTotal.add(vo.getAug() == null ? BigDecimal.ZERO : vo.getAug());
                augQtyTotal = augQtyTotal.add(vo.getAug() == null ? BigDecimal.ZERO : vo.getJun());
                i++;
                vo.setSep((BigDecimal) v[i++]);
                productQtyTotal = productQtyTotal.add(vo.getSep() == null ? BigDecimal.ZERO : vo.getSep());
                sepQtyTotal = sepQtyTotal.add(vo.getSep() == null ? BigDecimal.ZERO : vo.getSep());
                i++;
                vo.setOct((BigDecimal) v[i++]);
                productQtyTotal = productQtyTotal.add(vo.getOct() == null ? BigDecimal.ZERO : vo.getOct());
                octQtyTotal = octQtyTotal.add(vo.getOct() == null ? BigDecimal.ZERO : vo.getOct());
                i++;
                vo.setNov((BigDecimal) v[i++]);
                productQtyTotal = productQtyTotal.add(vo.getNov() == null ? BigDecimal.ZERO : vo.getNov());
                novQtyTotal = novQtyTotal.add(vo.getNov() == null ? BigDecimal.ZERO : vo.getNov());
                i++;
                vo.setDec((BigDecimal) v[i++]);
                productQtyTotal = productQtyTotal.add(vo.getDec() == null ? BigDecimal.ZERO : vo.getDec());
                decQtyTotal = decQtyTotal.add(vo.getDec() == null ? BigDecimal.ZERO : vo.getDec());
                vo.setProductSubTotal(productQtyTotal);
                customerOrderYearVOList.add(vo);

                //金額
                CustomerOrderYearVO vo2 = new CustomerOrderYearVO();
                BigDecimal productAmtTotal = BigDecimal.ZERO;
                vo2.setLabel("金額");
                vo2.setProduct((String) v[1]);
                int j = 2;
                j++;
                vo2.setJan((BigDecimal) v[j++]);
                productAmtTotal = productAmtTotal.add(vo2.getJan() == null ? BigDecimal.ZERO : vo2.getJan());
                janAmtTotal = janAmtTotal.add(vo2.getJan() == null ? BigDecimal.ZERO : vo2.getJan());
                j++;
                vo2.setFeb((BigDecimal) v[j++]);
                productAmtTotal = productAmtTotal.add(vo2.getFeb() == null ? BigDecimal.ZERO : vo2.getFeb());
                febAmtTotal = febAmtTotal.add(vo2.getFeb() == null ? BigDecimal.ZERO : vo2.getFeb());
                j++;
                vo2.setMar((BigDecimal) v[j++]);
                productAmtTotal = productAmtTotal.add(vo2.getMar() == null ? BigDecimal.ZERO : vo2.getMar());
                marAmtTotal = marAmtTotal.add(vo2.getMar() == null ? BigDecimal.ZERO : vo2.getMar());
                j++;
                vo2.setApr((BigDecimal) v[j++]);
                productAmtTotal = productAmtTotal.add(vo2.getApr() == null ? BigDecimal.ZERO : vo2.getApr());
                aprAmtTotal = aprAmtTotal.add(vo2.getApr() == null ? BigDecimal.ZERO : vo2.getApr());
                j++;
                vo2.setMay((BigDecimal) v[j++]);
                productAmtTotal = productAmtTotal.add(vo2.getMay() == null ? BigDecimal.ZERO : vo2.getMay());
                mayAmtTotal = mayAmtTotal.add(vo2.getMay() == null ? BigDecimal.ZERO : vo2.getMay());
                j++;
                vo2.setJun((BigDecimal) v[j++]);
                productAmtTotal = productAmtTotal.add(vo2.getJun() == null ? BigDecimal.ZERO : vo2.getJun());
                junAmtTotal = junAmtTotal.add(vo2.getJun() == null ? BigDecimal.ZERO : vo2.getJun());
                j++;
                vo2.setJul((BigDecimal) v[j++]);
                productAmtTotal = productAmtTotal.add(vo2.getJul() == null ? BigDecimal.ZERO : vo2.getJul());
                julAmtTotal = julAmtTotal.add(vo2.getJul() == null ? BigDecimal.ZERO : vo2.getJul());
                j++;
                vo2.setAug((BigDecimal) v[j++]);
                productAmtTotal = productAmtTotal.add(vo2.getAug() == null ? BigDecimal.ZERO : vo2.getAug());
                augAmtTotal = augAmtTotal.add(vo2.getAug() == null ? BigDecimal.ZERO : vo2.getAug());
                j++;
                vo2.setSep((BigDecimal) v[j++]);
                productAmtTotal = productAmtTotal.add(vo2.getSep() == null ? BigDecimal.ZERO : vo2.getSep());
                sepAmtTotal = sepAmtTotal.add(vo2.getSep() == null ? BigDecimal.ZERO : vo2.getSep());
                j++;
                vo2.setOct((BigDecimal) v[j++]);
                productAmtTotal = productAmtTotal.add(vo2.getOct() == null ? BigDecimal.ZERO : vo2.getOct());
                octAmtTotal = octAmtTotal.add(vo2.getOct() == null ? BigDecimal.ZERO : vo2.getOct());
                j++;
                vo2.setNov((BigDecimal) v[j++]);
                productAmtTotal = productAmtTotal.add(vo2.getNov() == null ? BigDecimal.ZERO : vo2.getNov());
                novAmtTotal = novAmtTotal.add(vo2.getNov() == null ? BigDecimal.ZERO : vo2.getNov());
                j++;
                vo2.setDec((BigDecimal) v[j++]);
                productAmtTotal = productAmtTotal.add(vo2.getDec() == null ? BigDecimal.ZERO : vo2.getDec());
                decAmtTotal = decAmtTotal.add(vo2.getDec() == null ? BigDecimal.ZERO : vo2.getDec());
                vo2.setProductSubTotal(productAmtTotal);
                customerOrderYearVOList.add(vo2);
            }

            CustomerOrderYearVO vo = new CustomerOrderYearVO();
            vo.setProduct("總計");
            vo.setLabel("數量");
            vo.setJan(janQtyTotal.equals(BigDecimal.ZERO) ? null : janQtyTotal);
            vo.setFeb(febQtyTotal.equals(BigDecimal.ZERO) ? null : febQtyTotal);
            vo.setMar(marQtyTotal.equals(BigDecimal.ZERO) ? null : marQtyTotal);
            vo.setApr(aprQtyTotal.equals(BigDecimal.ZERO) ? null : aprQtyTotal);
            vo.setMay(mayQtyTotal.equals(BigDecimal.ZERO) ? null : mayQtyTotal);
            vo.setJun(junQtyTotal.equals(BigDecimal.ZERO) ? null : junQtyTotal);
            vo.setJul(julQtyTotal.equals(BigDecimal.ZERO) ? null : julQtyTotal);
            vo.setAug(augQtyTotal.equals(BigDecimal.ZERO) ? null : augQtyTotal);
            vo.setSep(sepQtyTotal.equals(BigDecimal.ZERO) ? null : sepQtyTotal);
            vo.setOct(octQtyTotal.equals(BigDecimal.ZERO) ? null : octQtyTotal);
            vo.setNov(novQtyTotal.equals(BigDecimal.ZERO) ? null : novQtyTotal);
            vo.setDec(decQtyTotal.equals(BigDecimal.ZERO) ? null : decQtyTotal);
            vo.setProductSubTotal(janQtyTotal.add(febQtyTotal).add(marQtyTotal).add(aprQtyTotal).add(mayQtyTotal).add(junQtyTotal).add(julQtyTotal).add(augQtyTotal).add(sepQtyTotal).add(octQtyTotal).add(novQtyTotal).add(decQtyTotal));
            customerOrderYearVOList.add(0, vo);
            CustomerOrderYearVO vo2 = new CustomerOrderYearVO();
            vo2.setLabel("金額");
            vo2.setJan(janAmtTotal.equals(BigDecimal.ZERO) ? null : janAmtTotal);
            vo2.setFeb(febAmtTotal.equals(BigDecimal.ZERO) ? null : febAmtTotal);
            vo2.setMar(marAmtTotal.equals(BigDecimal.ZERO) ? null : marAmtTotal);
            vo2.setApr(aprAmtTotal.equals(BigDecimal.ZERO) ? null : aprAmtTotal);
            vo2.setMay(mayAmtTotal.equals(BigDecimal.ZERO) ? null : mayAmtTotal);
            vo2.setJun(junAmtTotal.equals(BigDecimal.ZERO) ? null : junAmtTotal);
            vo2.setJul(julAmtTotal.equals(BigDecimal.ZERO) ? null : julAmtTotal);
            vo2.setAug(augAmtTotal.equals(BigDecimal.ZERO) ? null : augAmtTotal);
            vo2.setSep(sepAmtTotal.equals(BigDecimal.ZERO) ? null : sepAmtTotal);
            vo2.setOct(octAmtTotal.equals(BigDecimal.ZERO) ? null : octAmtTotal);
            vo2.setNov(novAmtTotal.equals(BigDecimal.ZERO) ? null : novAmtTotal);
            vo2.setDec(decAmtTotal.equals(BigDecimal.ZERO) ? null : decAmtTotal);
            vo2.setProductSubTotal(janAmtTotal.add(febAmtTotal).add(marAmtTotal).add(aprAmtTotal).add(mayAmtTotal).add(junAmtTotal).add(julAmtTotal).add(augAmtTotal).add(sepAmtTotal).add(octAmtTotal).add(novAmtTotal).add(decAmtTotal));
            customerOrderYearVOList.add(1, vo2);

        } catch (Exception e) {
            logger.error("getCashSummaryByCriteria(), e = {}", e);
        }
        return customerOrderYearVOList;

    }
}
