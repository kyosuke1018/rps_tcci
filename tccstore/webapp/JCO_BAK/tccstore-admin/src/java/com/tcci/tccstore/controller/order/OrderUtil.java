/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.order;

import com.tcci.tccstore.entity.EcContract;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcDeliveryPlace;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcOrder;
import com.tcci.tccstore.entity.EcPlant;
import com.tcci.tccstore.entity.EcProduct;
import com.tcci.tccstore.entity.EcSales;
import com.tcci.tccstore.entity.EcSalesarea;
import com.tcci.tccstore.enums.OrderStatusEnum;
import com.tcci.tccstore.facade.contract.EcContractFacade;
import com.tcci.tccstore.facade.customer.EcCustomerFacade;
import com.tcci.tccstore.facade.deliveryplace.EcDeliveryPlaceFacade;
import com.tcci.tccstore.facade.plant.EcPlantFacade;
import com.tcci.tccstore.facade.product.EcProductFacade;
import com.tcci.tccstore.facade.sales.EcSalesFacade;
import com.tcci.tccstore.facade.salesarea.EcSalesareaFacade;
import com.tcci.tccstore.model.contract.Contract;
import com.tcci.tccstore.model.customer.Customer;
import com.tcci.tccstore.model.deliveryplace.DeliveryPlace;
import com.tcci.tccstore.model.member.Member;
import com.tcci.tccstore.model.order.Order;
import com.tcci.tccstore.model.plant.Plant;
import com.tcci.tccstore.model.product.Product;
import com.tcci.tccstore.model.sales.Sales;
import com.tcci.tccstore.model.salesarea.Salesarea;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Neo.Fu
 */
@Named
@Stateless
public class OrderUtil {
    @Inject
    private EcProductFacade productFacade;
    @Inject
    private EcPlantFacade plantFacade;
    @Inject
    private EcDeliveryPlaceFacade deliveryPlaceFacade;
    @Inject
    private EcSalesFacade salesFacade;
    @Inject
    private EcSalesareaFacade salesareaFacade;
    @Inject
    private EcCustomerFacade customerFacade;
    @Inject
    private EcContractFacade contractFacade;

    public EcOrder convertOrderToEcOrder(Order order, EcOrder ecOrder) {
        if (ecOrder == null) {
            ecOrder = new EcOrder();
        }
        ecOrder.setId(order.getId());
        ecOrder.setProductCode(order.getProductCode());
        ecOrder.setProductName(order.getProductName());
        ecOrder.setUnitPrice(order.getUnitPrice());
        ecOrder.setQuantity(order.getQuantity());
        ecOrder.setAmount(order.getAmount());
        ecOrder.setVehicle(order.getVehicle());
        ecOrder.setDeliveryDate(order.getDeliveryDate());
        ecOrder.setMethod(order.getMethod());
        ecOrder.setContractCode(order.getContractCode());
        ecOrder.setContractName(order.getContractName());
        ecOrder.setPosnr(order.getPosnr());
        ecOrder.setPlantCode(order.getPlantCode());
        ecOrder.setPlantName(order.getPlantName());
        ecOrder.setSalesareaCode(order.getSalesareaCode());
        ecOrder.setSalesareaName(order.getSalesareaName());
        ecOrder.setDeliveryCode(order.getDeliveryCode());
        ecOrder.setDeliveryName(order.getDeliveryName());
        ecOrder.setSalesCode(order.getSalesCode());
        ecOrder.setSalesName(order.getSalesName());
        ecOrder.setSiteLoc(order.getSiteLoc());
        ecOrder.setBonus(order.getBonus());
        ecOrder.setStatus(OrderStatusEnum.fromString(order.getStatus()));
        ecOrder.setSapOrdernum(order.getSapOrdernum());
        ecOrder.setProductId(productFacade.find(order.getProduct().getId()));
        ecOrder.setPlantId(plantFacade.find(order.getPlant().getId()));
        if (order.getDelivery() != null) {
            ecOrder.setDeliveryId(deliveryPlaceFacade.find(order.getDelivery().getId()));
        }
        ecOrder.setSalesareaId(salesareaFacade.find(order.getSalesarea().getId()));
        ecOrder.setSalesId(salesFacade.find(order.getSales().getId()));
        ecOrder.setCustomerId(customerFacade.find(order.getCustomer().getId()));
        if (order.getContract() != null) {
            ecOrder.setContractId(contractFacade.find(order.getContract().getId()));
        }
        //ecOrder.setMemberId(memberFacade.findActiveByLoginAccount(order.getMember().getLoginAccount()));
        return ecOrder;
    }

    public Order convertEcOrderToOrder(EcOrder ecOrder) {
        Order order = new Order();
        order.setId(ecOrder.getId());
        order.setProductCode(ecOrder.getProductCode());
        order.setProductName(ecOrder.getProductName());
        order.setUnitPrice(ecOrder.getUnitPrice());
        order.setQuantity(ecOrder.getQuantity());
        order.setAmount(ecOrder.getAmount());
        order.setVehicle(ecOrder.getVehicle());
        order.setDeliveryDate(ecOrder.getDeliveryDate());
        order.setMethod(ecOrder.getMethod());
        order.setContractCode(ecOrder.getContractCode());
        order.setContractName(ecOrder.getContractName());
        order.setPosnr(ecOrder.getPosnr());
        order.setPlantCode(ecOrder.getPlantCode());
        order.setPlantName(ecOrder.getPlantName());
        order.setSalesareaCode(ecOrder.getSalesareaCode());
        order.setSalesareaName(ecOrder.getSalesareaName());
        order.setDeliveryCode(ecOrder.getDeliveryCode());
        order.setDeliveryName(ecOrder.getDeliveryName());
        order.setSalesCode(ecOrder.getSalesCode());
        order.setSalesName(ecOrder.getSalesName());
        order.setSiteLoc(ecOrder.getSiteLoc());
        order.setBonus(ecOrder.getBonus());
        order.setStatus(ecOrder.getStatus().toString());
        order.setSapOrdernum(ecOrder.getSapOrdernum());
        order.setCreatetime(ecOrder.getCreatetime());

        Plant plant = new Plant();
        EcPlant ecPlant = ecOrder.getPlantId();
        plant.setId(ecPlant.getId());
        plant.setCode(ecPlant.getCode());
        plant.setName(ecPlant.getName());
        plant.setIncoFlag(ecPlant.getIncoFlag());
        order.setPlant(plant);

        Customer customer = new Customer();
        EcCustomer ecCustomer = ecOrder.getCustomerId();
        customer.setId(ecCustomer.getId());
        customer.setCode(ecCustomer.getCode());
        customer.setName(ecCustomer.getName());
        order.setCustomer(customer);

        if (ecOrder.getContractId() != null) {
            Contract contract = new Contract();
            EcContract ecContract = ecOrder.getContractId();
            contract.setId(ecContract.getId());
            contract.setCode(ecContract.getCode());
            order.setContract(contract);
        }

        Salesarea salesarea = new Salesarea();
        EcSalesarea ecSalesarea = ecOrder.getSalesareaId();
        salesarea.setId(ecSalesarea.getId());
        salesarea.setCode(ecSalesarea.getCode());
        salesarea.setName(ecSalesarea.getName());
        order.setSalesarea(salesarea);

        if (ecOrder.getDeliveryId() != null) {
            DeliveryPlace deliveryPlace = new DeliveryPlace();
            EcDeliveryPlace ecDeliveryPlace = ecOrder.getDeliveryId();
            deliveryPlace.setId(ecDeliveryPlace.getId());
            deliveryPlace.setCode(ecDeliveryPlace.getCode());
            deliveryPlace.setName(ecDeliveryPlace.getName());
            deliveryPlace.setProvince(ecDeliveryPlace.getProvince());
            deliveryPlace.setCity(ecDeliveryPlace.getCity());
            deliveryPlace.setDistrict(ecDeliveryPlace.getDistrict());
            order.setDelivery(deliveryPlace);
        }

        Product product = new Product();
        EcProduct ecProduct = ecOrder.getProductId();
        product.setId(ecProduct.getId());
        product.setCode(ecProduct.getCode());
        product.setName(ecProduct.getName());
        order.setProduct(product);

        Member member = new Member();
        EcMember ecMember = ecOrder.getMemberId();
        member.setLoginAccount(ecMember.getLoginAccount());
        member.setName(ecMember.getName());
        member.setEmail(ecMember.getEmail());
        // member.setActive(ecMember.isActive());
        member.setPhone(ecMember.getPhone());
        order.setMember(member);

        Sales sales = new Sales();
        EcSales ecSales = ecOrder.getSalesId();
        sales.setId(ecSales.getId());
        sales.setName(ecSales.getName());
        sales.setCode(ecSales.getCode());
        order.setSales(sales);

        return order;
    }
}
