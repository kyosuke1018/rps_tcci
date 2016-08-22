/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade.legacy;

import com.tcci.sksp.controller.util.DateUtil;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkPaymentItem;
import com.tcci.sksp.entity.ar.SkPaymentRate;
import com.tcci.sksp.facade.SkCustomerFacade;
import com.tcci.sksp.facade.SkPaymentItemFacade;
import com.tcci.sksp.facade.SkPaymentRateFacade;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
@Stateless
public class LegacyPaymentRateFacade {

    //@EJB SkCustomerFacade customerFacade;
    @EJB SkPaymentItemFacade paymentItemFacade;
    @EJB SkPaymentRateFacade paymentRateFacade;
    protected final static Logger logger = LoggerFactory.getLogger(LegacyPaymentRateFacade.class);
    @PersistenceContext(unitName = "pgmUserPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public Connection getConnection() {
        return getEntityManager().unwrap(java.sql.Connection.class);
    }
    //@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void deletePaymentItemAndRate(Date baselineTimestamp){
        paymentItemFacade.deletePaymentItemByBaselineTimestamp(baselineTimestamp);
        paymentRateFacade.deletePaymentRateByBaselineTimestamp(baselineTimestamp);
    }
    public void syncPaymentItemAndRate(Map<String,SkCustomer> customerMap,Date baselineTimestamp) throws Exception{
        //findAndSavePaymentItem(customerMap,baselineTimestamp);
        findAndSavePaymentRate(baselineTimestamp);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void findAndSavePaymentItem(Map<String,SkCustomer> customerMap,Date baselineTimestamp) throws SQLException {
        String sqlCommand = "select kunnr customer_number,zuonr order_number,sgtxt invoice_number,hzuon sapid,amt_a ar_amount,amt_b premium_discount,amt_c sales_return,amt_d sales_discount,amt_e payment_amount from ZBSEGCOLL";
        //String sqlCommand = "SELECT KUNNR ,ZUONR ,SGTXT ,HZUON ,AMT_A ,AMT_B ,AMT_C ,AMT_D ,AMT_E FROM ZBSEGCOLL";
        //return getEntityManager().createNativeQuery(sqlcommand).getResultList();
        //logger.debug("findPaymentItem sqlCommand=" + sqlCommand);    
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection conn = getConnection();
        statement = conn.prepareStatement(sqlCommand);
        rs = statement.executeQuery();
        //List<SkPaymentItem> itemList = new ArrayList<SkPaymentItem>();
        if (rs != null) {
            int i = 0;
            for (; rs.next();) {
                SkPaymentItem item = new SkPaymentItem();
                String customerNumber = rs.getString(1);
                SkCustomer customer = customerMap.get(customerNumber.trim()) ; //customerFacade.findByCode(customerNumber.trim());
                String orderNumber = rs.getString(2);
                String invoiceNumber = rs.getString(3);
                String sapid = rs.getString(4);
                BigDecimal arAmount = rs.getBigDecimal(5);
                BigDecimal premiumDiscount = rs.getBigDecimal(6);
                BigDecimal salesReturn = rs.getBigDecimal(7);
                BigDecimal salesDiscount = rs.getBigDecimal(8);
                BigDecimal paymentAmount = rs.getBigDecimal(9);
                item.setCustomer(customer);
                item.setOrderNumber(orderNumber.trim());
                item.setInvoiceNumber(invoiceNumber.trim());
                item.setSapid(sapid.trim());
                item.setArAmount(arAmount);
                item.setPremiumDiscount(premiumDiscount);
                item.setSalesReturn(salesReturn);
                item.setSalesDiscount(salesDiscount);
                item.setPaymentAmount(paymentAmount);
                item.setBaselineTimestamp(baselineTimestamp);
                item.setCreatetimestamp(new Date() );
                //itemList.add(item);
                paymentItemFacade.create(item);
            }
            //logger.debug("itemList size=" + itemList.size());            
        };
        //return itemList;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void findAndSavePaymentRate(Date baselineTimestamp) throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append("select t2.sapid,");
        sb.append("t2.ar_amount,-t2.premium_discount as premium_discount,-t2.sales_return as sales_return,-t2.sales_discount as sales_discount,-t2.payment_amount as payment_amount,");
        sb.append("-t2.payment_amount/( t2.ar_amount + t2.premium_discount + t2.sales_return + t2.sales_discount ) as payment_rate ");
        sb.append("from ");
        sb.append("(");
        sb.append("select t.hzuon as sapid,sum(t.amt_a) as ar_amount,sum(t.amt_b) as premium_discount,sum(t.amt_c) as sales_return,sum(t.amt_d) as sales_discount,sum(t.amt_e) as payment_amount ");
        sb.append("from zbsegcoll t ");
        sb.append("group by t.hzuon");
        sb.append(") t2 ");
        sb.append("where (t2.ar_amount + t2.premium_discount + t2.sales_return + t2.sales_discount) != 0");
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection conn = getConnection();
        statement = conn.prepareStatement(sb.toString());
        rs = statement.executeQuery();
        if (rs != null) {
            int i = 0;
            for (; rs.next();) {
                
                String sapid = rs.getString(1);
                BigDecimal arAmount = rs.getBigDecimal(2);
                BigDecimal premiumDiscount = rs.getBigDecimal(3);
                BigDecimal salesReturn = rs.getBigDecimal(4);
                BigDecimal salesDiscount = rs.getBigDecimal(5);
                BigDecimal paymentAmount = rs.getBigDecimal(6);
                BigDecimal paymentRate = rs.getBigDecimal(7);
                SkPaymentRate rate = new SkPaymentRate();
                rate.setBaselineTimestamp(null);
                rate.setArAmount(arAmount);
                rate.setPremiumDiscount(premiumDiscount);
                rate.setSalesReturn(salesReturn);
                rate.setSalesDiscount(salesDiscount);
                rate.setPaymentAmount(paymentAmount);
                rate.setPaymentRate(paymentRate);
                rate.setCreatetimestamp(new Date());
                paymentRateFacade.create(rate);
            }
        };
    }

    /*
    public List findAndSavePaymentRate() throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append("select t2.sapid,");
        sb.append("t2.ar_amount,-t2.premium_discount as premium_discount,-t2.sales_return as sales_return,-t2.sales_discount as sales_discount,-t2.payment_amount as payment_amount,");
        sb.append("-t2.payment_amount/( t2.ar_amount + t2.premium_discount + t2.sales_return + t2.sales_discount ) as payment_rate ");
        sb.append("from ");
        sb.append("(");
        sb.append("select t.hzuon as sapid,sum(t.amt_a) as ar_amount,sum(t.amt_b) as premium_discount,sum(t.amt_c) as sales_return,sum(t.amt_d) as sales_discount,sum(t.amt_e) as payment_amount ");
        sb.append("from zbsegcoll t ");
        sb.append("group by t.hzuon");
        sb.append(") t2 ");
        sb.append("where (t2.ar_amount + t2.premium_discount + t2.sales_return + t2.sales_discount) != 0");
        return getEntityManager().createNativeQuery(sb.toString()).getResultList();
    }
     * 
     */
}
