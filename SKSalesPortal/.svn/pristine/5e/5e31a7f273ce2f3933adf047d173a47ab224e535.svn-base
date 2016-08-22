/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.vo;

import com.google.gson.annotations.Expose;
import com.tcci.sksp.entity.enums.SalesAllowancesPageEnum;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
public class SalesDetailsVO {

    Logger logger = LoggerFactory.getLogger(this.getClass().getCanonicalName());
    //private SkSalesDetails salesDetails;
    @Expose
    private Integer invoiceYear;//internal calculate
    @Expose
    private Integer invoiceMonth;//internal calculate
    @Expose
    private Integer invoiceDay;//internal calculate
    @Expose
    private String alphabet; //internal calculate
    @Expose
    private String number; //internal calculate
    @Expose
    private BigDecimal discountRate;
    @Expose
    private BigDecimal salesAllowances; //internal calculate
    @Expose
    private BigDecimal salesAllowancesTax;
    @Expose
    private BigDecimal salesAllowancesExcludeTax;
    @Expose
    private String salesAllowancesStr;
    //private SkCustomer customer;
    @Expose
    private String sapid;
    @Expose
    private String invoiceNumber;
    @Expose
    private String orderNumber;
    @Expose
    private Date invoiceTimestamp;
    @Expose
    private BigDecimal amount; //internal calculate
    @Expose
    private BigDecimal amount_no_tax;
    @Expose
    private BigDecimal tax;
    @Expose
    private BigDecimal premiumDiscount;
    @Expose
    private BigDecimal premiumDiscountTax;
    @Expose
    private String customerSimpleCode;
    @Expose
    private String customerName;
    @Expose
    private String vat;
    @Expose
    private String city;
    @Expose
    private String street;
    @Expose
    private String address; //internal calculate
    @Expose
    private String shippingCondition;
    @Expose
    private String paymentTerm;
    //following for UI
    @Expose
    private boolean selected;
    @Expose
    private boolean printed;
    @Expose
    private int printNumber;
    @Expose
    private Date orderTimestamp;
    @Expose
    private BigDecimal quantity;
    @Expose
    private String unit;
    @Expose
    private String category;
    @Expose
    private BigDecimal invoiceAmount;
    @Expose
    private BigDecimal sellingPrice;
    @Expose
    private boolean gift;
    @Expose
    private String productNumber;
    @Expose
    private String productName;
    @Expose
    private SalesAllowancesPageEnum salesAllowancesPage;
    @Expose
    private String lotNumber;
    @Expose
    private BigDecimal invoiceTotalAmount;

    /*
     public void setSalesDetails(SkSalesDetails salesDetails) {
     this.salesDetails = salesDetails;
     if( salesDetails !=null ){
     if( salesDetails.getInvoiceTimestamp() != null){
     Calendar c = Calendar.getInstance();
     c.setTime(salesDetails.getInvoiceTimestamp());
     setInvoiceYear( Integer.valueOf(c.get(Calendar.YEAR) -1911) );
     setInvoiceMonth( c.get(Calendar.MONTH) + 1);
     setInvoiceDay( c.get(Calendar.DAY_OF_MONTH) );
     }
     if( StringUtils.isEmpty( salesDetails.getInvoiceNumber() ) ){
     setAlphabet(salesDetails.getInvoiceNumber().substring(0,3));
     setNumber(salesDetails.getInvoiceNumber().substring(3));
     }
     }
     }
     */
    public Integer getInvoiceDay() {
        return invoiceDay;
    }
    /*
     public void setInvoiceDay(Integer invoiceDay) {
     this.invoiceDay = invoiceDay;
     }
     */

    public Integer getInvoiceMonth() {
        return invoiceMonth;
    }
    /*
     public void setInvoiceMonth(Integer invoiceMonth) {
     this.invoiceMonth = invoiceMonth;
     }
     */

    public Integer getInvoiceYear() {
        return invoiceYear;
    }
    /*
     public void setInvoiceYear(Integer invoiceYear) {
     this.invoiceYear = invoiceYear;
     }
     * */

    public String getAlphabet() {
        return alphabet;
    }
    /*
     public void setAlphabet(String alphabet) {
     this.alphabet = alphabet;
     }
     */

    public String getNumber() {
        return number;
    }
    /*
     public void setNumber(String number) {
     this.number = number;
     }
     */

    public BigDecimal getSalesAllowances() {
        if (premiumDiscount == null) {
            premiumDiscount = BigDecimal.ZERO;
        }
        if (premiumDiscountTax == null) {
            premiumDiscountTax = BigDecimal.ZERO;
        }
        if (discountRate == null) {
            discountRate = BigDecimal.ZERO;
        }
        BigDecimal hundred = BigDecimal.valueOf(100);
        logger.debug("discountRate={}", discountRate);
        salesAllowances = getAmount().subtract(premiumDiscount.add(premiumDiscountTax)).multiply(discountRate).divide(hundred, 0, RoundingMode.HALF_UP);
        return salesAllowances;
    }
    /*
     public void setSalesAllowances(BigDecimal salesAllowances) {
     this.salesAllowances = salesAllowances;
     }
     */

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        address = "";
        if (!StringUtils.isEmpty(this.getCity())) {
            address = getCity();
        }
        if (!StringUtils.isEmpty(this.getStreet())) {
            address = address + getStreet();
        }
        return address;
    }
    /*
     public void setAddress(String address) {
     this.address = address;
     }
     */

    public BigDecimal getAmount() {
        if (amount_no_tax == null) {
            amount_no_tax = BigDecimal.ZERO;
        }
        if (tax == null) {
            tax = BigDecimal.ZERO;
        }
        amount = amount_no_tax.add(tax);
        return amount;
    }
    /*
     public void setAmount(BigDecimal amount) {
     this.amount = amount;
     }
     */

    public BigDecimal getAmount_no_tax() {
        return amount_no_tax;
    }

    public void setAmount_no_tax(BigDecimal amount_no_tax) {
        this.amount_no_tax = amount_no_tax;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSimpleCode() {
        return customerSimpleCode;
    }

    public void setCustomerSimpleCode(String customerSimpleCode) {
        this.customerSimpleCode = customerSimpleCode;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        if (!StringUtils.isEmpty(this.invoiceNumber)) {
            alphabet = this.invoiceNumber.substring(0, 2);
            number = this.invoiceNumber.substring(2);
        }
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getInvoiceTimestamp() {
        return invoiceTimestamp;
    }

    public void setInvoiceTimestamp(Date invoiceTimestamp) {
        this.invoiceTimestamp = invoiceTimestamp;
        if (this.invoiceTimestamp != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(this.invoiceTimestamp);
            invoiceYear = Integer.valueOf(c.get(Calendar.YEAR) - 1911);
            invoiceMonth = Integer.valueOf(c.get(Calendar.MONTH) + 1);
            invoiceDay = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    public BigDecimal getPremiumDiscount() {
        return premiumDiscount;
    }

    public void setPremiumDiscount(BigDecimal premiumDiscount) {
        this.premiumDiscount = premiumDiscount;
    }

    public BigDecimal getPremiumDiscountTax() {
        return premiumDiscountTax;
    }

    public void setPremiumDiscountTax(BigDecimal premiumDiscountTax) {
        this.premiumDiscountTax = premiumDiscountTax;
    }

    public String getShippingCondition() {
        return shippingCondition;
    }

    public void setShippingCondition(String shippingCondition) {
        this.shippingCondition = shippingCondition;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public int getPrintNumber() {
        return printNumber;
    }

    public void setPrintNumber(int printNumber) {
        this.printNumber = printNumber;
    }

    public boolean isPrinted() {
        return printed;
    }

    public void setPrinted(boolean printed) {
        this.printed = printed;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    public String getSalesAllowancesStr() {
        if (salesAllowances != null) {
            salesAllowancesStr = salesAllowances.toString();
        }
        //NumberFormat.getNumberInstance().format( salesAllowances );
        return salesAllowancesStr;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        if ("TANN".equals(category)) {
            setGift(true);
        }
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public Date getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(Date orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public boolean isGift() {
        return gift;
    }

    public void setGift(boolean gift) {
        this.gift = gift;
    }

    public BigDecimal getSalesAllowancesExcludeTax() {
//        if( "01".equals(this.getShippingCondition() ) ){
            //BigDecimal hundred = BigDecimal.valueOf(100);
            //BigDecimal ninefive = BigDecimal.valueOf(95);
            BigDecimal onePointZeroFive = BigDecimal.valueOf(1.05);
            BigDecimal total = getSalesAllowances();
            if( total != null){
                salesAllowancesExcludeTax = total.divide(onePointZeroFive,0,RoundingMode.HALF_UP);
                salesAllowancesTax = total.subtract(salesAllowancesExcludeTax);
            }
//        }else{
//            salesAllowancesExcludeTax= getSalesAllowances();
//            setSalesAllowancesTax(BigDecimal.ZERO);
//        }  
        return salesAllowancesExcludeTax;
    }

    public void setSalesAllowancesExcludeTax(BigDecimal salesAllowancesExcludeTax) {
        this.salesAllowancesExcludeTax = salesAllowancesExcludeTax;
    }

    public BigDecimal getSalesAllowancesTax() {
        return salesAllowancesTax;
    }

    public void setSalesAllowancesTax(BigDecimal salesAllowancesTax) {
        this.salesAllowancesTax = salesAllowancesTax;
    }

    public SalesAllowancesPageEnum getSalesAllowancesPage() {
        return salesAllowancesPage;
    }

    public void setSalesAllowancesPage(SalesAllowancesPageEnum salesAllowancesPage) {
        this.salesAllowancesPage = salesAllowancesPage;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public BigDecimal getInvoiceTotalAmount() {
        return invoiceTotalAmount;
    }

    public void setInvoiceTotalAmount(BigDecimal invoiceTotalAmount) {
        this.invoiceTotalAmount = invoiceTotalAmount;
    }
}
