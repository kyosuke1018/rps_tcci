/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service;

import com.tcci.tccstore.entity.EcArticle;
import com.tcci.tccstore.entity.EcBanner;
import com.tcci.tccstore.entity.EcBulletin;
import com.tcci.tccstore.entity.EcCampaign;
import com.tcci.tccstore.entity.EcContract;
import com.tcci.tccstore.entity.EcContractProduct;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcDeliveryPlace;
import com.tcci.tccstore.entity.EcGoods;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcNotify;
import com.tcci.tccstore.entity.EcOrder;
import com.tcci.tccstore.entity.EcPartner;
import com.tcci.tccstore.entity.EcPartnerComment;
import com.tcci.tccstore.entity.EcPlant;
import com.tcci.tccstore.entity.EcProduct;
import com.tcci.tccstore.entity.EcRewardLog;
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
import com.tcci.tccstore.model.article.Article;
import com.tcci.tccstore.model.banner.Banner;
import com.tcci.tccstore.model.campaign.Campaign;
import com.tcci.tccstore.model.contract.Contract;
import com.tcci.tccstore.model.customer.Customer;
import com.tcci.tccstore.model.deliveryplace.DeliveryPlace;
import com.tcci.tccstore.model.goods.Goods;
import com.tcci.tccstore.model.home.Bulletin;
import com.tcci.tccstore.model.member.Member;
import com.tcci.tccstore.model.member.RewardLog;
import com.tcci.tccstore.model.notify.Notify;
import com.tcci.tccstore.model.order.Order;
import com.tcci.tccstore.model.partner.Partner;
import com.tcci.tccstore.model.partner.PartnerComment;
import com.tcci.tccstore.model.plant.Plant;
import com.tcci.tccstore.model.product.Product;
import com.tcci.tccstore.model.sales.Sales;
import com.tcci.tccstore.model.salesarea.Salesarea;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Named
public class EntityToModel {

    private final static Logger logger = LoggerFactory.getLogger(EntityToModel.class);
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

    public static Customer buildCustomer(EcCustomer entity) {
        Customer model = new Customer();
        final String[] fields = {"id", "code", "name"};
        copyFields(entity, model, fields);
        return model;
    }

    public static Contract buildContract(EcContract entity) {
        Contract model = new Contract();
        final String[] fields = {"id", "code", "name"};
        copyFields(entity, model, fields);
        return model;
    }

    public static Plant buildPlant(EcPlant entity) {
        Plant model = new Plant();
        final String[] fields = {"id", "code", "name", "incoFlag"};
        copyFields(entity, model, fields);
        return model;
    }

    public static Salesarea buildSalesarea(EcSalesarea entity) {
        Salesarea model = new Salesarea();
        final String[] fields = {"id", "code", "name"};
        copyFields(entity, model, fields);
        return model;
    }

    public static Product buildProduct(EcProduct entity) {
        Product model = new Product();
        final String[] fields = {"id", "code", "name", "description"};
        copyFields(entity, model, fields);
        return model;
    }

    public static Goods buildGoods(EcGoods entity) {
        Goods model = new Goods();
        final String[] fields = {"id", "code", "name", "description"};
        copyFields(entity, model, fields);
        return model;
    }

    public static Product buildContractProduct(EcContractProduct entity) {
        Product product = buildProduct(entity.getEcProduct());
        product.setPlant(buildPlant(entity.getEcPlant()));
        product.setSalsearae(buildSalesarea(entity.getEcSalesarea()));
        // product.setUnitprice(unitprice);
        product.setMethod(entity.getMethod());
        product.setPosnr(entity.getEcContractProductPK().getPosnr());
        return product;
    }

    public static Sales buildSales(EcSales entity) {
        Sales model = new Sales();
        final String[] fields = {"id", "code", "name"};
        copyFields(entity, model, fields);
        return model;
    }

    public static Member buildMember(EcMember entity) {
        Member model = new Member();
        final String[] fields = {"id", "loginAccount", "name", "email", "phone"};
        copyFields(entity, model, fields);
        return model;
    }

    public static DeliveryPlace buildDeliveryPlace(EcDeliveryPlace entity) {
        DeliveryPlace vo = new DeliveryPlace();
        final String[] fields = {"id", "code", "name", "province", "city", "district", "town"};
        copyFields(entity, vo, fields);
        vo.setSalesarea(buildSalesarea(entity.getEcSalesarea()));
        return vo;
    }

    public static Banner buildBanner(EcBanner entity) {
        Banner vo = new Banner();
        final String[] fields = {"id", "category", "contentUrl", "link", "description"};
        copyFields(entity, vo, fields);
        return vo;
    }

    public static Partner buildPartnerSimple(EcPartner entity) {
        Partner vo = new Partner();
        final String[] fields = {"id", "name", "active"};
        copyFields(entity, vo, fields);
        Double averageRate = entity.getAverageRate();
        vo.setAverageRate(null==averageRate ? 5.0 : averageRate);
        return vo;
    }
    
    public static Partner buildPartner(EcPartner entity) {
        Partner vo = new Partner();
        final String[] fields = {"id", "name", "active", "phone", "social", "province", "city", "district", "town", "contact", "address"};
        copyFields(entity, vo, fields);
        Double averageRate = entity.getAverageRate();
        vo.setAverageRate(null==averageRate ? 5.0 : averageRate);
        return vo;
    }

    public static PartnerComment buildPartnerCommentSimple(EcPartnerComment entity) {
        PartnerComment vo = new PartnerComment();
        final String[] fields = {"rate", "message", "createtime"};
        copyFields(entity, vo, fields);
        return vo;
    }
    
    public static RewardLog buildRewardLog(EcRewardLog entity) {
        RewardLog vo = new RewardLog();
        final String[] fields = {"points", "eventType", "eventDetail", "eventTime"};
        copyFields(entity, vo, fields);
        return vo;
    }

    public static Campaign buildCampaign(EcCampaign entity, boolean visited) {
        Campaign vo = new Campaign();
        final String[] fields = {"id", "type", "subject", "description", "startDate", "endDate"};
        copyFields(entity, vo, fields);
        vo.setVisited(visited);
        return vo;
    }
    
    public static Article buildArticle(EcArticle entity) {
        Article vo = new Article();
        final String[] fields = {"id", "title", "pubdate", "link"};
        copyFields(entity, vo, fields);
        return vo;
    }
    
    public static Bulletin buildBulletin(EcBulletin entity) {
        Bulletin vo = new Bulletin();
        final String[] fields = {"id", "title"};
        copyFields(entity, vo, fields);
        return vo;
    }

    public static void copyFields(Object src, Object dst, String[] fields) {
        for (String field : fields) {
            try {
                Object value = PropertyUtils.getSimpleProperty(src, field);
                PropertyUtils.setSimpleProperty(dst, field, value);
            } catch (Exception ex) {
                logger.error("{}, {}", new Object[]{src, field}, ex);
            }
        }
    }

    public static Notify buildNotify(EcNotify entity) {
        Notify vo = new Notify();
        //final String[] fields = {"id", "subject", "notifyClassname", "notifyClassid","createtime", "readCount"};
        final String[] fields = {"id", "subject", "createtime"};
        copyFields(entity, vo, fields);
        vo.setType(entity.getType().toString());
        return vo;
    }

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
            contract.setName(ecContract.getName());
            order.setContract(contract);
            order.setPosnr(ecOrder.getPosnr());
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
            deliveryPlace.setTown(ecDeliveryPlace.getTown());
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
