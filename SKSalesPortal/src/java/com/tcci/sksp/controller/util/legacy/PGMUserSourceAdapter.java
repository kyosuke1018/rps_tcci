/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.util.legacy;

import com.tcci.sksp.controller.util.FileUtil;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkPaymentItem;
import com.tcci.sksp.entity.ar.SkPaymentRate;
import com.tcci.sksp.facade.SkCustomerFacade;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jason.Yu
 */
public class PGMUserSourceAdapter extends DatabaseUtil {

    private final Logger logger = Logger.getLogger(PGMUserSourceAdapter.class.getName());
    public SkCustomerFacade lookupCustomerFacade() throws Exception{
        String jndiName = "java:global/SKSalesPortal/SkCustomerFacade";
        return (SkCustomerFacade)lookup(jndiName);
    }
    private String trimSpace(String data){
        if( data != null)
            data = data.trim();
        return data;
    }
    public List<SkPaymentItem> queryPaymentItem(Connection connection, Map<String, SkCustomer> customerMap, Date baselineTimestamp) throws Exception {
        //String sqlCommand = "select kunnr customer_number,zuonr order_number,sgtxt invoice_number,hzuon sapid,amt_a ar_amount,amt_b premium_discount,amt_c sales_return,amt_d sales_discount,amt_e payment_amount from ZBSEGCOLL";
        StringBuilder sqlCommand = new StringBuilder(200);
        /*
sqlCommand.append("(select kunnr customer_number,zuonr order_number,sgtxt invoice_number,t.hzuon as sapid,t.amt_a as ar_amount,t.amt_b as premium_discount,t.amt_c as sales_return,t.amt_d as sales_discount,t.amt_e as payment_amount ");
sqlCommand.append("from ZBSEGCOLL t  ");
sqlCommand.append("where t.hzuon in (select vkgrp from zkna1) ");
sqlCommand.append(") ");
sqlCommand.append("union ");
sqlCommand.append("(select t.kunnr customer_number,zuonr order_number,sgtxt invoice_number,t2.vkgrp as sapid,t.amt_a as ar_amount,t.amt_b as premium_discount,t.amt_c as sales_return,t.amt_d as sales_discount,t.amt_e as payment_amount ");
sqlCommand.append("from ZBSEGCOLL t,zkna1 t2  ");
sqlCommand.append("where t.hzuon=t2.kunnr ");
sqlCommand.append(")"); */
        sqlCommand.append("select t.kunnr as customer_number ")
                  .append(", t.zuonr as order_number ")
                  .append(", t.sgtxt as invoice_number ")
                  .append(", case  ")
                  .append("  when z.vkgrp is null then t.hzuon ")
                  .append("  else z.vkgrp ")
                  .append("  end as sapid ")
                  .append(", t.amt_a as ar_amount ")
                  .append(", t.amt_b as premium_discount ")
                  .append(", t.amt_c as sales_return ")
                  .append(", t.amt_d as sales_discount ")
                  .append(", t.amt_e as payment_amount ")
                  .append("from ZBSEGCOLL t ")
                  .append("left join ZKNA1 z on t.hzuon=z.kunnr ");
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<SkPaymentItem> itemList = null;
        try {
            statement = connection.prepareStatement(sqlCommand.toString());
            rs = statement.executeQuery();
            if (rs != null) {
                SkCustomerFacade customerFacade = lookupCustomerFacade();
                itemList = new ArrayList<SkPaymentItem>();
                int i = 0;
                for (; rs.next();) {
                    SkPaymentItem item = new SkPaymentItem();
                    String customerNumber = rs.getString(1);
                    customerNumber = trimSpace( customerNumber );
                    SkCustomer customer = customerMap.get(customerNumber.trim()); 
                    if( customer == null ){
                        customer = customerFacade.findByCode(customerNumber.trim());
                        if( customer != null){
                            customerMap.put(customer.getCode(), customer);
                        }
                    }
                    
                    String orderNumber = rs.getString(2);
                    String invoiceNumber = rs.getString(3);
                    String sapid = rs.getString(4);
                    orderNumber = trimSpace( orderNumber );
                    invoiceNumber = trimSpace( invoiceNumber );
                    sapid = trimSpace( sapid );
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
                    item.setCreatetimestamp(new Date());
                    itemList.add(item);
                }
                logger.log(Level.INFO, "itemList size=" + itemList.size());
            };
        } finally {
            close(connection, statement, rs);
        }
        return itemList;
    }

    public List<SkPaymentRate> queryPaymentRate(Connection connection, Date baselineTimestamp) throws Exception {
        String fullPathFileName = "/report/sql/payment_rate.txt";
        FileUtil util = new FileUtil();
        String sqlcommand = util.getQueryCommand(fullPathFileName);
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<SkPaymentRate> rateList = null;
        try {
            //System.out.println("sql="+sqlcommand);
            statement = connection.prepareStatement(sqlcommand);
            rs = statement.executeQuery();
            if (rs != null) {
                // SkCustomerFacade customerFacade = lookupCustomerFacade();
                rateList = new ArrayList<SkPaymentRate>();
                int i = 0;
                for (; rs.next();) {

                    String sapid = rs.getString(1);
                    sapid = trimSpace( sapid );
                    BigDecimal arAmount = setScale( rs.getBigDecimal(2),2 );
                    BigDecimal premiumDiscount = setScale( rs.getBigDecimal(3),2 );
                    BigDecimal salesReturn = setScale( rs.getBigDecimal(4),2 );
                    BigDecimal salesDiscount = setScale( rs.getBigDecimal(5),2 );
                    BigDecimal paymentAmount = setScale( rs.getBigDecimal(6),2 );
                    BigDecimal paymentRate = setScale( rs.getBigDecimal(7),4);
                    BigDecimal weight = setScale( rs.getBigDecimal(8),4);
                    if(paymentRate.compareTo(BigDecimal.ONE)>0)
                        paymentRate = BigDecimal.ONE;
                    /*if( StringUtils.isEmpty(sapid) )
                        ;
                    else{
                        if( sapid.length() >3 ){
                            SkCustomer customer = customerFacade.findByCode(sapid);
                            if( customer != null ){
                                sapid = customer.getSapid();
                            }
                        }
                    }*/
                    SkPaymentRate rate = new SkPaymentRate();
                    rate.setBaselineTimestamp(null);
                    rate.setArAmount(arAmount);
                    rate.setPremiumDiscount(premiumDiscount);
                    rate.setSalesReturn(salesReturn);
                    rate.setSalesDiscount(salesDiscount);
                    rate.setPaymentAmount(paymentAmount);
                    rate.setPaymentRate(paymentRate);
                    rate.setCreatetimestamp(new Date());
                    rate.setWeight(weight);
                    rate.setBaselineTimestamp(baselineTimestamp);
                    rate.setSapid(sapid);
                    rateList.add(rate);
                }
            };
        } finally {
            close(connection, statement, rs);
        }
        return rateList;
    }
    private BigDecimal setScale(BigDecimal d,int scale){
        if( d == null)
            return null;
        else
            return d.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }
    /*
    private BigDecimal prepareWeigth(BigDecimal rate){
        double weight = 0;
        double payment_rate = rate.doubleValue();
        if( payment_rate >=0.7){
                weight= 1.3;
        }else if( payment_rate >=0.7){
                weight= 1.3;
        }else if( payment_rate >=0.6){
                weight= 1.2;
        }else if( payment_rate >=0.5){
                weight= 1.1;
        }else if( payment_rate >=0.4){
                weight= 1.0;
        }else if( payment_rate >=0.3){
                weight= 0.7;
        }else {
                weight= 0;
        }
        return BigDecimal.valueOf(weight);
    }
    * 
    */
}
