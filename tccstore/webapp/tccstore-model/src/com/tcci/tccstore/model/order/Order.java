/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.model.order;

import com.tcci.tccstore.model.contract.Contract;
import com.tcci.tccstore.model.customer.Customer;
import com.tcci.tccstore.model.deliveryplace.DeliveryPlace;
import com.tcci.tccstore.model.member.Member;
import com.tcci.tccstore.model.plant.Plant;
import com.tcci.tccstore.model.product.Product;
import com.tcci.tccstore.model.sales.Sales;
import com.tcci.tccstore.model.salesarea.Salesarea;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Neo.Fu
 */
public class Order {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String productCode;
    private String productName;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal amount;
    private String vehicle;
    private String deliveryDate;
    private String method;
    private String contractCode;
    private String contractName;
    private String plantCode;
    private String plantName;
    private String salesareaCode;
    private String salesareaName;
    private String deliveryCode;
    private String deliveryName;
    private String salesCode;
    private String salesName;
    private String siteLoc;
    private Integer posnr;
    private int bonus;
    private String status;
    private String sapOrdernum;
    private Date createtime;
    private Salesarea salesarea;
    private Sales sales;
    private Product product;
    private Plant plant;
    private DeliveryPlace delivery;
    private Customer customer;
    private Contract contract;
    private Member member;

    public Order() {
    }

    //getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getSalesareaCode() {
        return salesareaCode;
    }

    public void setSalesareaCode(String salesareaCode) {
        this.salesareaCode = salesareaCode;
    }

    public String getSalesareaName() {
        return salesareaName;
    }

    public void setSalesareaName(String salesareaName) {
        this.salesareaName = salesareaName;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getSiteLoc() {
        return siteLoc;
    }

    public void setSiteLoc(String siteLoc) {
        this.siteLoc = siteLoc;
    }

    public Integer getPosnr() {
        return posnr;
    }

    public void setPosnr(Integer posnr) {
        this.posnr = posnr;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSapOrdernum() {
        return sapOrdernum;
    }

    public void setSapOrdernum(String sapOrdernum) {
        this.sapOrdernum = sapOrdernum;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Salesarea getSalesarea() {
        return salesarea;
    }

    public void setSalesarea(Salesarea salesarea) {
        this.salesarea = salesarea;
    }

    public Sales getSales() {
        return sales;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public DeliveryPlace getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryPlace delivery) {
        this.delivery = delivery;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "com.tcci.ecdemo.model.order["
                + "id=" + id + "\n"
                + "productCode=" + productCode + "\n"
                + "productName=" + productName + "\n"
                + "untiPrice=" + unitPrice + "\n"
                + "quantity=" + quantity + "\n"
                + "amount=" + amount + "\n"
                + "vehicle=" + vehicle + "\n"
                + "deliveryDate=" + deliveryDate + "\n"
                + "method=" + method + "\n"
                + "contractCode=" + contractCode + "\n"
                + "plantCode=" + plantCode + "\n"
                + "plantName=" + plantName + "\n"
                + "salesareaCode=" + salesareaCode + "\n"
                + "salesareaName=" + salesareaName + "\n"
                + "deliveryCode=" + deliveryCode + "\n"
                + "deliveryName=" + deliveryName + "\n"
                + "salesCode=" + salesCode + "\n"
                + "salesName=" + salesName + "\n"
                + "posnr=" + posnr + "\n"
                + "bonus=" + bonus + "\n"
                + "status=" + status + "\n"
                + "sapOrdernum=" + sapOrdernum + "\n"
                + "createtime=" + createtime + "\n"
                + "salesarea=" + (salesarea != null ? "salesarea[id=" + salesarea.getId() + "]" : "null")+"\n"
                + "sales=" + (sales != null ? "sales[id=" + sales.getId() + "]" : "null")+"\n"
                + "product=" + (product != null ? "product[id=" + product.getId() + "]" : "null")+"\n"
                + "plant=" + (plant != null ? "plant[id=" + plant.getId() + "]" : "null")+"\n"
                + "delivery=" + (delivery != null ? "delivery[id=" + delivery.getId() + "]" : "null")+"\n"
                + "customer=" + (customer != null ? "customer[id=" + customer.getId() + "]" : "null")+"\n"
                + "contract=" + (contract != null ? "contract[id=" + contract.getId() + "]" : "null")+"\n"
                + "member=" + (member != null ? "member[account=" + member.getLoginAccount()+ "]" : "null")+"\n"
                + "]";
    }
}
