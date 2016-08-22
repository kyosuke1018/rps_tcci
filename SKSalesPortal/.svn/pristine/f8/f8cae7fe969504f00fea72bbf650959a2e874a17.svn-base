/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.controller.util.DateUtil;
import com.tcci.sksp.controller.util.FileUtil;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkProductMaster;
import com.tcci.sksp.entity.ar.SkSalesDetails;
import com.tcci.sksp.entity.enums.OrderTypeEnum;
import com.tcci.sksp.entity.enums.PaymentTermEnum;
import com.tcci.sksp.entity.enums.SalesAllowancesPageEnum;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.vo.SalesAllowancesVO;
import com.tcci.sksp.vo.SalesDetailsVO;
import com.tcci.sksp.vo.SalesReturnDiscountVO;
import com.tcci.sksp.vo.YearlySalesDetailsVO;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
@Path("order")
@Stateless
public class SkSalesDetailsFacade extends AbstractFacade<SkSalesDetails> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    @EJB
    private SkProductMasterFacade productMasterFacade;
    @EJB
    private SkCustomerFacade customerFacade;
    @EJB
    private SkSalesDiscountLogFacade salesDiscountLogFacade;

    public SkSalesDetailsFacade() {
        super(SkSalesDetails.class);
    }

    public List<SalesReturnDiscountVO> findByCriteria(RemitFilter filter) {
        try {
            String billingTypeParam = "";
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT d.BILLING_DOC, d.ORDER_NUMBER, d.INVOICE_NUMBER, ");
            builder.append("       d.INVOICE_TIMESTAMP, d.BUYER_CUSTOMER, d.PRODUCT_NUMBER,");
            builder.append("      d.SELLING_PRICE, SUM(d.QUANTITY) AS QUANTITY, ");
            builder.append("      SUM(d.RETURN_AMOUNT) AS AMOUNT, SUM(d.RETURN_AMOUNT_TAX) AS TAX ");
            builder.append("FROM SK_SALES_DETAILS d INNER JOIN SK_CUSTOMER c ON d.BUYER_CUSTOMER = c.CODE ");
            builder.append("                        INNER JOIN SK_PRODUCT_MASTER p ON d.PRODUCT_NUMBER = p.CODE ");
            builder.append("AND d.SAPID = #sapid ");
            builder.append("AND (#startdate IS NULL OR d.INVOICE_TIMESTAMP>=#startdate) ");
            builder.append("AND (#enddate IS NULL OR d.INVOICE_TIMESTAMP<=#enddate) ");
            if (filter.getOrderType().equals(OrderTypeEnum.SALES_RETURN)) {
                builder.append(" AND d.BILLING_TYPE = #orderType ");
                billingTypeParam = OrderTypeEnum.SALES_RETURN.getCode();
            } else if (filter.getOrderType().equals(OrderTypeEnum.SALES_ALLOWANCES)) {
                builder.append(" AND d.BILLING_TYPE LIKE #orderType ");
                billingTypeParam = "%" + OrderTypeEnum.SALES_ALLOWANCES.getCode() + "%";
            }
            builder.append("GROUP BY d.BILLING_DOC, d.ORDER_NUMBER, d.INVOICE_NUMBER, ");
            builder.append("  d.INVOICE_TIMESTAMP, d.BUYER_CUSTOMER, d.PRODUCT_NUMBER, d.SELLING_PRICE ");
            builder.append("ORDER BY d.BUYER_CUSTOMER, d.INVOICE_TIMESTAMP, d.BILLING_DOC ");

            Query q = em.createNativeQuery(builder.toString());
            List list = q.setParameter("sapid", filter.getSales().getCode()).setParameter("startdate", filter.getOrderDateStart()).setParameter("enddate", filter.getOrderDateEnd()).setParameter("orderType", billingTypeParam).getResultList();
            List<SalesReturnDiscountVO> voList = new ArrayList<SalesReturnDiscountVO>();
            for (Object o : list) {
                Object[] v = (Object[]) o;
                SalesReturnDiscountVO vo = new SalesReturnDiscountVO();
                int index = 0;
                String billingDoc = (String) v[index++];
                String orderNumber = (String) v[index++];
                String invoiceNumber = (String) v[index++];
                Date invoiceDate = (Date) v[index++];
                String buyerNumber = (String) v[index++];
                String productNumber = (String) v[index++];
                BigDecimal sellingPrice = (BigDecimal) v[index++];
                BigDecimal quantity = (BigDecimal) v[index++];
                BigDecimal amount = (BigDecimal) v[index++];
                BigDecimal tax = (BigDecimal) v[index++];

                SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
                String invoiceDateStr = fmt.format(invoiceDate);

                String buyerInfo = "";
                SkCustomer buyer = customerFacade.findByCode(buyerNumber);
                if (buyer != null) {
                    buyerInfo = buyer.getCode() + " - " + buyer.getName();
                }

                String productInfo = "";
                SkProductMaster master = productMasterFacade.findByCode(productNumber);
                productInfo = master.getCode() + " - " + master.getName();

                vo.setBillingDoc(billingDoc);
                vo.setOrderNumber(orderNumber);
                vo.setInvoiceNumber(invoiceNumber);
                vo.setInvoiceDateStr(invoiceDateStr);
                vo.setBuyer(buyerInfo);
                vo.setProduct(productInfo);
                vo.setPrice(sellingPrice);
                vo.setQuantity(quantity);
                vo.setAmount(amount);
                vo.setTax(tax);
                voList.add(vo);
            }
            return voList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
//          List<SkSalesDetails> list = null; CriteriaBuilder builder =
//          getEntityManager().getCriteriaBuilder();
//          CriteriaQuery<SkSalesDetails> cq =
//          builder.createQuery(SkSalesDetails.class); Root<SkSalesDetails> root
//          = cq.from(SkSalesDetails.class); cq.select(root); List<Predicate>
//          predicateList = new ArrayList<Predicate>();
//         
//          Selection<String> billingDoc =
//          root.get("billingDoc").as(String.class).alias("billingDoc");
//          Selection<String> orderNumber =
//          root.get("orderNumber").as(String.class).alias("orderNumber");
//          Selection<Date> orderTimestamp =
//          root.get("orderTimestamp").as(Date.class).alias("orderTimestamp");
//          Selection<String> invoiceNumber =
//          root.get("invoiceNumber").as(String.class).alias("invoiceNumber");
//          Selection<String> buyerCustomer =
//          root.get("buyerCustomer").as(String.class).alias("buyerCustomer");
//          Selection<String> productNumber =
//          root.get("productNumber").as(String.class).alias("productNumber");
//          Selection<BigDecimal> price =
//          root.get("sellingPrice").as(BigDecimal.class).alias("sellingPrice");
//          Selection<BigDecimal> qantity = builder.sum((Expression<BigDecimal>)
//          root.get("quantity").as(BigDecimal.class)).alias("quantity");
//          Selection<BigDecimal> amount = builder.sum((Expression<BigDecimal>)
//          root.get("amount").as(BigDecimal.class)).alias("amount");
//          Selection<BigDecimal> tax = builder.sum((Expression<BigDecimal>)
//          root.get("returnAmountTax").as(BigDecimal.class)).alias("returnAmountTax");
//          cq.multiselect(billingDoc, orderNumber, orderTimestamp,
//          invoiceNumber, buyerCustomer, productNumber, price, qantity, amount,
//          tax);
//         
//          //sales if (filter.getSales() != null) { Predicate p =
//          builder.equal(root.get("sapid"), filter.getSales().getCode());
//          predicateList.add(p); }          *
//          //order timestamp if ((filter.getOrderDateStart() != null) &&
//          (filter.getOrderDateEnd() == null)) { Predicate p =
//          builder.greaterThanOrEqualTo(root.get("orderTimestamp").as(Date.class),
//          filter.getPayingStart()); predicateList.add(p); } else if
//          ((filter.getOrderDateStart() == null) && (filter.getOrderDateEnd() !=
//          null)) { Calendar calendar = new GregorianCalendar();
//          calendar.setTime(filter.getPayingEnd()); calendar.add(Calendar.DATE,
//          1); Predicate p =
//          builder.lessThanOrEqualTo(root.get("orderTimestamp").as(Date.class),
//          calendar.getTime()); predicateList.add(p); } else if
//          ((filter.getOrderDateStart() != null) && (filter.getOrderDateEnd() !=
//          null)) { Calendar calendar = new GregorianCalendar();
//          calendar.setTime(filter.getPayingEnd()); calendar.add(Calendar.DATE,
//          1); Predicate p =
//          builder.between(root.get("orderTimestamp").as(Date.class),
//          filter.getPayingStart(), calendar.getTime()); predicateList.add(p); }          *
//          //order type if (filter.getOrderType() != null) { if
//          (filter.getOrderType().equals(OrderTypeEnum.SALES_RETURN)) {
//          Predicate p = builder.equal(root.get("orderType"),
//          OrderTypeEnum.SALES_RETURN.getCode()); predicateList.add(p); } else
//          if (filter.getOrderType().equals(OrderTypeEnum.SALES_ALLOWANCES)) {
//          Predicate p = builder.like(root.get("orderType").as(String.class),
//          "%" + OrderTypeEnum.SALES_ALLOWANCES.getCode() + "%");
//          predicateList.add(p); } }
//         
//          List<Path> selectionList = new ArrayList<Path>();
//          selectionList.add(root.get("billingDoc"));
//          selectionList.add(root.get("orderNumber"));
//          selectionList.add(root.get("orderTimestamp"));
//          selectionList.add(root.get("invoiceNumber"));
//          selectionList.add(root.get("buyerCustomer"));
//          selectionList.add(root.get("productNumber"));
//          selectionList.add(root.get("sellingPrice"));
//          cq.groupBy(selectionList);
    }

    public List<SalesAllowancesVO> findBySalesMemberOrderNumberAndOrderDate(SkSalesMember salesMember, String orderNumber) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT d.ORDER_NUMBER, d.INVOICE_NUMBER, ");
            builder.append("       d.SALES_ORG, d.SALES_CHANNEL, d.ORDER_ITEM, d.UNIT, ");
            builder.append("       d.ORDER_TIMESTAMP, d.BUYER_CUSTOMER, d.PRODUCT_NUMBER, d.SHIPPING_CONDITIONS, ");
            builder.append("      d.SELLING_PRICE, SUM(d.QUANTITY) AS QUANTITY, ");
            builder.append("      SUM(d.AMOUNT) AS AMOUNT, SUM(d.PREMIUM_DISCOUNT) AS PREMIUM_DISCOUNT, ");
            builder.append("      SUM(D.PREMIUM_DISCOUNT_TAX) AS PREMIUM_DISCOUNT_TAX ");
            builder.append("FROM SK_SALES_DETAILS d INNER JOIN SK_PRODUCT_MASTER p ON d.PRODUCT_NUMBER = p.CODE ");
            builder.append("WHERE d.SAPID = #sapid ");
            builder.append("  AND d.ORDER_NUMBER like #orderNumber ");
            builder.append("  AND AMOUNT<>0 "); // 金額不為 0
            builder.append("  AND QUANTITY<>0 "); // 數量不為 0
            builder.append("GROUP BY d.BILLING_DOC, d.ORDER_NUMBER, d.INVOICE_NUMBER, ");
            builder.append(" d.SALES_ORG, d.SALES_CHANNEL, d.ORDER_ITEM,");
            builder.append(" d.UNIT, d.BILLING_TYPE,");
            builder.append(" d.ORDER_TIMESTAMP, d.BUYER_CUSTOMER, d.PRODUCT_NUMBER, d.SHIPPING_CONDITIONS, d.SELLING_PRICE ");
            builder.append("ORDER BY d.ORDER_NUMBER, d.ORDER_ITEM");

            Query q = em.createNativeQuery(builder.toString());
            q.setParameter("sapid", salesMember.getCode());
            q.setParameter("orderNumber", orderNumber);

            List list = q.getResultList();
            List<SalesAllowancesVO> voList = new ArrayList<SalesAllowancesVO>();
            BigDecimal sumAmount = BigDecimal.ZERO;
            BigDecimal sumSalesAllowances = BigDecimal.ZERO;
            BigDecimal five = BigDecimal.valueOf(5);
            BigDecimal hundred = BigDecimal.valueOf(100);
            for (Object o : list) {
                Object[] v = (Object[]) o;
                SalesAllowancesVO salesAllowancesVO = new SalesAllowancesVO();
                int index = 0;
                String order_number = (String) v[index++];
                String invoiceNumber = (String) v[index++];
                String org = (String) v[index++];
                String channel = (String) v[index++];
                String item = ((BigDecimal) v[index++]).toString();
                String unit = (String) v[index++];
                Date orderDate = (Date) v[index++];
                String customer = (String) v[index++];
                String productNumber = (String) v[index++];
                String conditions = (String) v[index++];
                BigDecimal sellingPrice = (BigDecimal) v[index++];
                BigDecimal quantity = (BigDecimal) v[index++];
                BigDecimal amount = (BigDecimal) v[index++];
                BigDecimal premiumDiscount = (BigDecimal) v[index++];
                BigDecimal premiumDiscountTax = (BigDecimal) v[index++];
                salesAllowancesVO.setSelected(true);
                salesAllowancesVO.setSapid(salesMember.getCode());
                salesAllowancesVO.setOrg(org);
                // Jimmy, issue#20120604, 之前 channel 是取客戶資料的SAPID前兩碼, 修正為 SK_SALES_DETAILS.SALES_CHANNEL
                salesAllowancesVO.setChannel(channel);
                //
                salesAllowancesVO.setOrderNumber(order_number);
                salesAllowancesVO.setInvoiceNumber(invoiceNumber);
                salesAllowancesVO.setItem(item);
                salesAllowancesVO.setProduct(productNumber);
                salesAllowancesVO.setConditions(conditions);
                salesAllowancesVO.setQuantity(quantity);
                salesAllowancesVO.setUnit(unit);
                salesAllowancesVO.setPrice(sellingPrice);
                salesAllowancesVO.setAmount(amount);
                salesAllowancesVO.setCustomer(customer);
                salesAllowancesVO.setSapid(salesMember.getCode());
                BigDecimal netPremiumDiscount = premiumDiscount.add(premiumDiscountTax);
                salesAllowancesVO.setPremiumDiscount(netPremiumDiscount);
                BigDecimal netAmount = amount.subtract(netPremiumDiscount);
                BigDecimal salesAllowances = netAmount.multiply(five).divide(hundred, 0, BigDecimal.ROUND_HALF_UP);
                salesAllowancesVO.setSalesAllowances(salesAllowances);
                logger.debug("vo.getAmount()={}", salesAllowancesVO.getAmount());
                logger.debug("vo.getPremiumDiscount()={}", salesAllowancesVO.getPremiumDiscount());
                logger.debug("vo.getSalesAllowances()={}", salesAllowancesVO.getSalesAllowances());
                voList.add(salesAllowancesVO);
                sumAmount = sumAmount.add(netAmount);
                sumSalesAllowances = sumSalesAllowances.add(salesAllowances);
            }
            BigDecimal sumAmount5 = sumAmount.multiply(five).divide(hundred, 0, BigDecimal.ROUND_HALF_UP);
            if (sumAmount5.compareTo(sumSalesAllowances) != 0) {
                // 最後一筆補上尾差
                SalesAllowancesVO lastVO = voList.get(list.size() - 1);
                lastVO.setSalesAllowances(lastVO.getSalesAllowances().add(sumAmount5.subtract(sumSalesAllowances)));
            }
            return voList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SalesDetailsVO> findSalesAllowances(SkSalesMember member, SkCustomer customer, Date startTime, Date endTime, PaymentTermEnum paymentTerm) throws IOException {
        List<SalesDetailsVO> list = new ArrayList<SalesDetailsVO>();
        String fullPathFileName = "/report/sql/sales_allowances.txt";
        FileUtil util = new FileUtil();
        String sqlcommand = util.getQueryCommand(fullPathFileName);
        StringBuilder builder = new StringBuilder();
        builder.append(sqlcommand);
        if (member != null) {
            builder.append(" and t2.sapid= #sapid ");
        }
        if (customer != null) {
            builder.append(" and t2.code= #customerCode ");
        }
        if (paymentTerm != null) {
            builder.append(" and t2.payment_term= #paymentTerm ");
        }
        if (startTime != null) {
            builder.append(" and to_char(t1.invoice_timestamp,'yyyy/MM/dd') >= #startTime ");
        }
        if (endTime != null) {
            builder.append(" and to_char(t1.invoice_timestamp,'yyyy/MM/dd') <= #endTime  ");
        }
        builder.append(" order by t2.zip_code,t2.code,t1.invoice_timestamp,invoice_number");
        Query q = em.createNativeQuery(builder.toString());
        if (member != null) {
            q.setParameter("sapid", member.getCode());
        }
        if (customer != null) {
            q.setParameter("customerCode", customer.getCode());
        }
        if (startTime != null) {
            q.setParameter("startTime", DateUtil.getDateFormat(startTime, "yyyy/MM/dd"));
        }
        if (endTime != null) {
            q.setParameter("endTime", DateUtil.getDateFormat(endTime, "yyyy/MM/dd"));
        }
        if (paymentTerm != null) {
            q.setParameter("paymentTerm", paymentTerm.toString());
        }
        List l1 = q.getResultList();
        for (Object o : l1) {
            Object[] v = (Object[]) o;
            SalesDetailsVO vo = new SalesDetailsVO();
            int index = 0;
            //t1.sapid, t1.customer,t1.invoice_number,t1.order_number,
//t1.INVOICE_TIMESTAMP, t1.AMOUNT,t1.TAX,t1.PREMIUM_DISCOUNT, t1.PREMIUM_DISCOUNT_TAX,
//t2.payment_term,t2.simple_code,name,vat,city,street,shipping_condition 
            String sapid = (String) v[index++];
            String customerCode = (String) v[index++];
            String invoiceNumber = (String) v[index++];
            String orderNumber = (String) v[index++];
            Date invoiceTimestamp = (Date) v[index++];
            BigDecimal amount_no_tax = (BigDecimal) v[index++];
            BigDecimal tax = (BigDecimal) v[index++];
            BigDecimal premiumDiscount = (BigDecimal) v[index++];
            BigDecimal premiumDiscountTax = (BigDecimal) v[index++];
            String payment_term = (String) v[index++];
            String customerSimpleCode = (String) v[index++];
            String customerName = (String) v[index++];
            String vat = (String) v[index++];
            String city = (String) v[index++];
            String street = (String) v[index++];
            String shippingCondition = (String) v[index++];
            String salesAllowancePage = (String) v[index++];
            BigDecimal discountRate = (BigDecimal) v[index++];
            vo.setAmount_no_tax(amount_no_tax);
            vo.setCity(city);
            vo.setCustomerName(customerName);
            vo.setCustomerSimpleCode(customerSimpleCode);
            vo.setInvoiceNumber(invoiceNumber);
            vo.setInvoiceTimestamp(invoiceTimestamp);
            vo.setOrderNumber(orderNumber);
            vo.setPaymentTerm(payment_term);
            vo.setPremiumDiscount(premiumDiscount);
            vo.setPremiumDiscountTax(premiumDiscountTax);
            vo.setShippingCondition(shippingCondition);
            vo.setStreet(street);
            vo.setTax(tax);
            vo.setVat(vat);
            vo.setSapid(sapid);
            vo.setDiscountRate(discountRate);
            // #issue2012072701, 當銷貨折讓<=0時,不需列印
            if (vo.getSalesAllowances().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            if (salesAllowancePage == null) {
                salesAllowancePage = SalesAllowancesPageEnum.ALL.toString();
            }
            vo.setSalesAllowancesPage(SalesAllowancesPageEnum.valueOf(salesAllowancePage));
            int printNumber = salesDiscountLogFacade.findRowCount(invoiceNumber);
            vo.setPrintNumber(printNumber);
            list.add(vo);
        }
        return list;
    }

    //訂單資料
    private String buildQueryOrderSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("select p.name,c.name,c.simple_code,t.product_number,t.invoice_number,");
        sb.append("t.invoice_timestamp,t.order_number,t.order_timestamp,t.quantity,t.unit,");
        sb.append("nvl(t.selling_price, 0) selling_price,nvl(t.amount,0) amount,nvl(t.premium_discount,0) premium_discount,nvl(t.premium_discount_tax,0) premium_discount_tax,t.category,t.lot_number, c.payment_term, t.shipping_conditions ");
        sb.append("from sk_sales_details t,sk_product_master p,sk_customer c ");
        sb.append("where t.product_number = p.code(+) and t.buyer_customer=c.code(+) ");
        // issue#2012071001, 補上下列條件(比照年度訂單報表)
        sb.append("and (t.billing_type like 'ZF%' OR t.billing_type like 'ZL%') ");
        return sb.toString();
    }

    @GET
    @Path("findbycriteria")
    @Produces("text/plain; charset=UTF-8;")
    public String findByCriteria(@Context HttpServletRequest request,
            @QueryParam("member") String jsonMember,
            @QueryParam("customer") String jsonCustomer,
            @QueryParam("start_date") String jsonStartDate,
            @QueryParam("end_date") String jsonEndDate,
            @QueryParam("products") String jsonProducts,
            @QueryParam("exclude_gift") boolean excludeGift,
            @QueryParam("index") int index,
            @QueryParam("limit") int limit,
            @QueryParam("unit") String unit) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        logger.debug("jsonMember={}", jsonMember);

        SkSalesMember member = null;
        if (StringUtils.isNotEmpty(jsonMember)) {
            member = gson.fromJson(jsonMember, SkSalesMember.class);
        }

        SkCustomer customer = null;
        if (StringUtils.isNotEmpty(jsonCustomer)) {
            customer = gson.fromJson(jsonCustomer, SkCustomer.class);
        }

        Date startTime = null;
        if (StringUtils.isNotEmpty(jsonStartDate)) {
            startTime = gson.fromJson(jsonStartDate, Date.class);
        }

        Date endTime = null;
        if (StringUtils.isNotEmpty(jsonEndDate)) {
            endTime = gson.fromJson(jsonEndDate, Date.class);
        }

        String[] productArray = null;
        if (StringUtils.isNotEmpty(jsonProducts)) {
            productArray = gson.fromJson(jsonProducts, String[].class);
        }
        if (limit == 0) {
            return "ERROR:limit is required!";
        } else if (limit > 100) {
            return "ERROR:limit only allow 1 to 100!";
        }
        if (StringUtils.isEmpty(unit)) {
            unit = "part";
        }
        if ("order".equals(unit)) {
            return gson.toJson(findHistory(customer == null ? "" : customer.getSapid(), customer == null ? "" : customer.getSimpleCode(), startTime, endTime, productArray, excludeGift, index, limit));
        } else {
            return gson.toJson(findByCriteria(member, customer == null ? "" : customer.getSimpleCode(), startTime, endTime, productArray, excludeGift, index, limit));
        }
    }

    public List<SalesDetailsVO> findHistory(String sapid, String simpleCode, Date startTime, Date endTime, String[] selectedParts, boolean excludeGift, int index, int limit) {
        List<SalesDetailsVO> resultList = new ArrayList();
        List<SalesDetailsVO> orderNumberList = findByCriteria(sapid, simpleCode, startTime, endTime, selectedParts, true, index, limit);
        Set<String> processedOrderNumber = new HashSet();
        for (SalesDetailsVO detailsVO : orderNumberList) {
            String orderNumber = detailsVO.getOrderNumber();
            if (StringUtils.isNotEmpty(orderNumber)) {
                if (processedOrderNumber.contains(orderNumber)) {
                    continue;
                }
                resultList.addAll(findByOrderNumber(orderNumber));
                processedOrderNumber.add(orderNumber);
            }
        }
        return resultList;
    }

    public List<SalesDetailsVO> findByOrderNumber(String orderNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildQueryOrderSQL());
        sb.append("and t.order_number = #orderNumber");

        Query q = em.createNativeQuery(sb.toString());
        q.setParameter("orderNumber", orderNumber);
        List l1 = q.getResultList();
        return generateSalesDetailsList(l1);
    }

    public List<SalesDetailsVO> findByCriteria(SkSalesMember member, String customerSimpleCode, Date startTime, Date endTime, String filterProductNumber) {
        return findByCriteria(member, customerSimpleCode, startTime, endTime, StringUtils.split(filterProductNumber, ","), false, 0, 0);
    }

    public List<SalesDetailsVO> findByCriteria(SkSalesMember member, String customerSimpleCode, Date startTime, Date endTime, String[] products, boolean excludeGift, int begin, int limit) {
        String sapid = "";
        if (member != null) {
            sapid = member.getCode();
        }
        return findByCriteria(sapid, customerSimpleCode, startTime, endTime, products, excludeGift, begin, limit);
    }

    public List<SalesDetailsVO> findByCriteria(String sapid, String customerSimpleCode, Date startTime, Date endTime, String[] products, boolean excludeGift, int begin, int limit) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildQueryOrderSQL());

        if (StringUtils.isNotEmpty(sapid)) {
            sb.append("and c.sapid = #sapid ");
        }
        if (!StringUtils.isEmpty(customerSimpleCode)) {
            sb.append(" and c.simple_code = #simpleCode ");
        }
        //if (!StringUtils.isEmpty(filterProductNumber)) {
        //    sb.append(" and t.product_number = #productNumber ");
        //}
        if (products != null && products.length > 0) {
            sb.append(" and t.product_number in ( ");
            for (int i = 0; i < products.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append("#prod").append(i);
            }
            sb.append(" ) ");
        }
        if ((startTime != null) && (endTime == null)) {
            sb.append(" and t.invoice_timestamp > to_date(#startTime,'yyyy/mm/dd') ");
        } else if ((startTime == null) && (endTime != null)) {
            sb.append(" and t.invoice_timestamp < (to_date(#endTime,'yyyy/mm/dd') +1) ");
        } else if ((startTime != null) && (endTime != null)) {
            sb.append(" and t.invoice_timestamp between to_date(#startTime,'yyyy/mm/dd') and (to_date(#endTime,'yyyy/mm/dd') +1) ");
        }

        if (excludeGift) {
            sb.append("and t.category <> 'TANN' ");
        }

        sb.append(" order by t.invoice_timestamp desc, c.simple_code, t.billing_item, t.product_number desc, t.category ");
        Query q = em.createNativeQuery(sb.toString());
        q.setFirstResult(begin);
        q.setMaxResults(limit);
        if (StringUtils.isNotEmpty(sapid)) {
            q.setParameter("sapid", sapid);
        }
        if (!StringUtils.isEmpty(customerSimpleCode)) {
            q.setParameter("simpleCode", customerSimpleCode);
        }
        //if (!StringUtils.isEmpty(filterProductNumber)) {
        //    q.setParameter("productNumber", filterProductNumber);
        //}
        if (products != null && products.length > 0) {
            for (int i = 0; i < products.length; i++) {
                q.setParameter("prod" + i, products[i]);
            }
        }
        if (startTime != null) {
            q.setParameter("startTime", DateUtil.getDateFormat(startTime, "yyyy/MM/dd"));
        }
        if (endTime != null) {
            q.setParameter("endTime", DateUtil.getDateFormat(endTime, "yyyy/MM/dd"));
        }
        List l1 = q.getResultList();
        return generateSalesDetailsList(l1);
    }

    private List<SalesDetailsVO> generateSalesDetailsList(List l1) {
        List<SalesDetailsVO> list = new ArrayList(l1.size());
        String currentOrderNumber = "";
        SalesDetailsVO currentVO = null;
        BigDecimal invoiceTotalAmount = BigDecimal.ZERO;
        if (l1 != null && !l1.isEmpty()) {
            list = new ArrayList<SalesDetailsVO>(l1.size());
            for (Object o : l1) {
                Object[] v = (Object[]) o;
                SalesDetailsVO vo = new SalesDetailsVO();
                int index = 0;
                /*
                 * select p.name,c.name,
                 * t.buyer_customer,t.product_number,t.invoice_number,");
                 * sb.append("t.invoice_timestamp,t.order_number,t.order_timestamp,t.quantity,t.unit,");
                 * sb.append("t.selling_price,t.amount,t.premium_discount,t.premium_discount_tax,t.category
                 * ");
                 */
                String productName = (String) v[index++];
                String customerName = (String) v[index++];
                String simpleCode = (String) v[index++];
                String productNumber = (String) v[index++];
                String invoiceNumber = (String) v[index++];

                Date invoiceTimestamp = (Date) v[index++];
                String orderNumber = (String) v[index++];
                Date orderTimestamp = (Date) v[index++];
                BigDecimal quantity = (BigDecimal) v[index++];
                String unit = (String) v[index++];

                BigDecimal sellingPrice = (BigDecimal) v[index++];
                logger.debug("sellingPrice={}", sellingPrice);
                if (sellingPrice == null) {
                    sellingPrice = BigDecimal.ZERO;
                }
                BigDecimal invoiceAmount = (BigDecimal) v[index++];
                BigDecimal premiumDiscount = (BigDecimal) v[index++];
                BigDecimal premiumDiscountTax = (BigDecimal) v[index++];
                String category = (String) v[index++];
                String logNumber = (String) v[index++];
                String paymentTerm = (String) v[index++];
                String shippingConditions = (String) v[index++];

                if (currentOrderNumber.equals(orderNumber)) {
                    vo.setCustomerName(null);
                    vo.setCustomerSimpleCode(null);
                    vo.setInvoiceNumber(null);
                    //vo.setInvoiceTimestamp(null);  // -> for ui: sortBy invoiceTimestamp :issue#2012072001
                    vo.setOrderNumber(null);
                    vo.setOrderTimestamp(null);
                    invoiceTotalAmount = invoiceTotalAmount.add(invoiceAmount == null ? BigDecimal.ZERO : invoiceAmount);
                } else {
                    vo.setCustomerName(customerName);
                    vo.setCustomerSimpleCode(simpleCode);
                    vo.setInvoiceNumber(invoiceNumber);
                    //vo.setInvoiceTimestamp(invoiceTimestamp); // -> for ui: sortBy invoiceTimestamp : issue#2012072001
                    vo.setOrderNumber(orderNumber);
                    vo.setOrderTimestamp(orderTimestamp);
                    if (currentVO != null) {
                        currentVO.setInvoiceTotalAmount(invoiceTotalAmount);
                        invoiceTotalAmount = BigDecimal.ZERO;
                    }
                    currentVO = vo;
                    invoiceTotalAmount = (invoiceAmount == null ? BigDecimal.ZERO : invoiceAmount);
                    currentOrderNumber = orderNumber;
                }

                vo.setInvoiceTimestamp(invoiceTimestamp); //->for ui: sortBy invoiceTimestamp : issue#2012072001

                vo.setQuantity(quantity);
                vo.setUnit(unit);
                vo.setInvoiceAmount(invoiceAmount);
                vo.setPremiumDiscount(premiumDiscount);
                vo.setPremiumDiscountTax(premiumDiscountTax);
                vo.setCategory(category);
                vo.setSellingPrice(sellingPrice);
                vo.setProductNumber(productNumber);
                vo.setProductName(productName);
                vo.setLotNumber(logNumber);
                vo.setPaymentTerm(paymentTerm);
                vo.setShippingCondition(shippingConditions);
                list.add(vo);
            }
            if (currentVO != null) {
                currentVO.setInvoiceTotalAmount(invoiceTotalAmount);
            }
        }
        return list;
    }

    // issue #2012071801
    public List<YearlySalesDetailsVO> findYearlySalesDetailsByCriteria(SkSalesMember sales, SkCustomer customer, String theYear) {
        LinkedHashMap<String, YearlySalesDetailsVO> productDetailsMap = new LinkedHashMap<String, YearlySalesDetailsVO>();
        findYearlySalesDetailsByCriteria(sales, customer, theYear, false, productDetailsMap); //買量
        findYearlySalesDetailsByCriteria(sales, customer, theYear, true, productDetailsMap);  //贈量      
        List<YearlySalesDetailsVO> list = new ArrayList<YearlySalesDetailsVO>();
        list.addAll(productDetailsMap.values());
        return list;
    }

    // issue #2012071801
    private void findYearlySalesDetailsByCriteria(SkSalesMember sales, SkCustomer customer, String theYear, boolean isGift, LinkedHashMap<String, YearlySalesDetailsVO> detailsMap) {
        findYearlySalesDetailsByCriteria(sales, customer, theYear, null, isGift, detailsMap);
    }

    public List<YearlySalesDetailsVO> findYearlySalesDetailsByCriteria(SkSalesMember sales, SkCustomer customer, String theYear, String productNumber, boolean includeGift) {
        LinkedHashMap<String, YearlySalesDetailsVO> productDetailsMap = new LinkedHashMap<String, YearlySalesDetailsVO>();
        findYearlySalesDetailsByCriteria(sales, customer, theYear, productNumber, false, productDetailsMap); //買量
        if (includeGift) {
            findYearlySalesDetailsByCriteria(sales, customer, theYear, productNumber, true, productDetailsMap);  //贈量      
        }
        List<YearlySalesDetailsVO> list = new ArrayList<YearlySalesDetailsVO>();
        list.addAll(productDetailsMap.values());
        return list;
    }

    private void findYearlySalesDetailsByCriteria(SkSalesMember sales, SkCustomer customer, String theYear, String productNumber, boolean isGift, LinkedHashMap<String, YearlySalesDetailsVO> detailsMap) {
        if (detailsMap == null) {
            detailsMap = new LinkedHashMap<String, YearlySalesDetailsVO>();
        }
        StringBuilder builder = new StringBuilder();
        // issue#2012071002, 金額需扣除 premium_discount, premium_discount_tax
        builder.append("SELECT * FROM (");
        builder.append("    SELECT sd.product_number, pm.name, sum(sd.amount-sd.premium_discount-sd.premium_discount_tax) as amount, sum(sd.quantity) as quantity, ");
        builder.append("           to_char(invoice_timestamp,'MM') as month, to_char(invoice_timestamp,'YYYY') as year ");
        builder.append("    FROM SK_SALES_DETAILS sd, SK_PRODUCT_MASTER pm ");
        builder.append("    WHERE (sd.billing_type like 'ZF%' OR sd.billing_type like 'ZL%') AND sd.product_number IS NOT NULL ");
        if (StringUtils.isNotEmpty(productNumber)) {
            builder.append(" AND sd.product_number = #productNumber");
        }
        builder.append("        AND sd.product_number = pm.code AND (#customerCode IS NULL OR sd.buyer_customer = #customerCode) ");
        if (sales != null) {
            builder.append("        AND (#sapid IS NULL OR sd.sapid = #sapid)");
        }
        builder.append("        AND sd.category" + (isGift ? " = " : " <> ") + "'TANN'");
        builder.append("    GROUP BY sd.product_number, pm.name, to_char(invoice_timestamp,'MM'), to_char(invoice_timestamp,'YYYY') ");
        builder.append("    ORDER BY year, month ");
        builder.append(") tmp ");
        builder.append("WHERE (#year IS NULL OR tmp.year = #year)");
        builder.append("ORDER BY tmp.product_number, tmp.month ");

        Query q = em.createNativeQuery(builder.toString());
        q.setParameter("customerCode", customer != null ? customer.getCode() : null);
        if (sales != null) {
            q.setParameter("sapid", sales != null ? sales.getCode() : null);
        }
        q.setParameter("year", theYear);
        if (StringUtils.isNotEmpty(productNumber)) {
            q.setParameter("productNumber", productNumber);
        }

        List results = q.getResultList();
        if (results != null && !results.isEmpty()) {
            YearlySalesDetailsVO summaryVO = (YearlySalesDetailsVO) detailsMap.get("summary");
            if (summaryVO == null) {
                summaryVO = new YearlySalesDetailsVO();
                summaryVO.setProductCode("總計");
            }
            for (Object o : results) {
                Object[] v = (Object[]) o;
                int index = 0;

                String product_number = (String) v[index++];
                String name = (String) v[index++];
                BigDecimal amount = (BigDecimal) v[index++];
                BigDecimal quantity = (BigDecimal) v[index++];
                String month = (String) v[index++];

                YearlySalesDetailsVO vo = (YearlySalesDetailsVO) detailsMap.get(product_number);
                if (vo == null) {
                    vo = new YearlySalesDetailsVO();
                    vo.setProductCode(product_number);
                    vo.setProductName(name);
                    detailsMap.put(product_number, vo);
                }
                vo.setYy(theYear);
                if (isGift) {
                    vo.getGiftQuantityHashMap().put(month, quantity);
                    vo.setTotalGiftQuantity(vo.getTotalGiftQuantity().add(quantity));
                    // deal with summary
                    BigDecimal summaryMonthGiftQuantity = (BigDecimal) summaryVO.getGiftQuantityHashMap().get(month);
                    summaryVO.getGiftQuantityHashMap().put(month, (summaryMonthGiftQuantity == null ? BigDecimal.ZERO : summaryMonthGiftQuantity).add(quantity));
                    summaryVO.setTotalGiftQuantity(summaryVO.getTotalGiftQuantity().add(quantity));
                } else {
                    vo.getQuantityHashMap().put(month, quantity);
                    vo.setTotalQuantity(vo.getTotalQuantity().add(quantity));
                    vo.getAmountHashMap().put(month, amount);
                    vo.setTotalAmount(vo.getTotalAmount().add(amount));
                    // deal with summary
                    BigDecimal summaryMonthAmount = (BigDecimal) summaryVO.getAmountHashMap().get(month);
                    BigDecimal summaryMonthQuantity = (BigDecimal) summaryVO.getQuantityHashMap().get(month);
                    summaryVO.getAmountHashMap().put(month, (summaryMonthAmount == null ? BigDecimal.ZERO : summaryMonthAmount).add(amount));
                    summaryVO.getQuantityHashMap().put(month, (summaryMonthQuantity == null ? BigDecimal.ZERO : summaryMonthQuantity).add(quantity));
                    summaryVO.setTotalAmount(summaryVO.getTotalAmount().add(amount));
                    summaryVO.setTotalQuantity(summaryVO.getTotalQuantity().add(quantity));
                    // set default initial value
                    vo.getGiftQuantityHashMap().put(month, BigDecimal.ZERO);
                    vo.setTotalGiftQuantity(vo.getTotalGiftQuantity().add(BigDecimal.ZERO));
                    summaryVO.getGiftQuantityHashMap().put(month, BigDecimal.ZERO);
                    summaryVO.setTotalGiftQuantity(summaryVO.getTotalGiftQuantity().add(BigDecimal.ZERO));
                }
            }
            detailsMap.put("summary", summaryVO);
        }
    }
}
