/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcci.tccstore.entity.EcContract;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcDeliveryPlace;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcOrder;
import com.tcci.tccstore.entity.EcPlant;
import com.tcci.tccstore.facade.contract.EcContractFacade;
import com.tcci.tccstore.facade.customer.EcCustomerFacade;
import com.tcci.tccstore.facade.deliveryplace.EcDeliveryPlaceFacade;
import com.tcci.tccstore.facade.member.EcMemberFacade;
import com.tcci.tccstore.facade.order.EcOrderFacade;
import com.tcci.tccstore.facade.plant.EcPlantFacade;
import com.tcci.tccstore.model.ModelConstant;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class ServiceBase {

    private final static Logger logger = LoggerFactory.getLogger(ServiceBase.class);
    
    protected final static String PARAM_CLIENT_KEY = "Client-Key";
    protected final static String PARAM_CUSTOMER_ID = "customer_id";
    protected final static String PARAM_CONTRACT_ID = "contract_id";
    protected final static String PARAM_DELIVERY_ID = "delivery_id";
    protected final static String PARAM_DELIVERY_CODE = "delivery_code";
    protected final static String PARAM_PLANT_ID = "plant_id";
    protected final static String PARAM_PRODUCT_ID = "product_id";
    protected final static String PARAM_PREFERENCE = "preference";
    protected final static String PARAM_PROVINCE = "province";
    protected final static String PARAM_CITY = "city";
    protected final static String PARAM_DISTRICT = "district";
    protected final static String PARAM_DIVISION = "division";
    protected final static String PARAM_GOODS_ID = "goods_id";
    protected final static String PARAM_ALLPARTNER = "allpartner";
    protected final static String PARAM_QUANTITY = "quantity";
    protected final static String PARAM_UOM = "uom";
    protected final static String PARAM_PARTNER_ID = "partner_id";
    protected final static String PARAM_RATE = "rate";
    protected final static String PARAM_MESSAGE = "message";
    protected final static String PARAM_LOTNO = "lotno";
    protected final static String PARAM_AREACODE = "areacode";
    protected final static String PARAM_OLD_PASSWORD = "old_password";
    protected final static String PARAM_NEW_PASSWORD = "new_password";
    protected final static String PARAM_ACCOUNT = "account";
    protected final static String PARAM_NAME = "name";
    protected final static String PARAM_EMAIL = "email";
    protected final static String PARAM_PHONE = "phone";

    @Context
    protected SecurityContext sc;

    @EJB
    protected EcMemberFacade ecMemberFacade;
    @EJB
    protected EcCustomerFacade ecCustomerFacade;
    @EJB
    protected EcContractFacade ecContractFacade;
    @EJB
    protected EcDeliveryPlaceFacade ecDeliveryPlaceFacade;
    @EJB
    protected EcPlantFacade ecPlantFacade;
    @EJB
    protected EcOrderFacade orderFacade;

    private EcMember loginMember;

    private final static ObjectMapper mapper = new ObjectMapper();
    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat(ModelConstant.ISO8601Format);
        mapper.setDateFormat(dateFormat);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    @PostConstruct
    private void init() {
        Principal principal = sc == null ? null : sc.getUserPrincipal();
        String loginAccount = principal == null ? null : principal.getName();
        loginMember = loginAccount == null ? null : ecMemberFacade.findActiveByLoginAccount(loginAccount);
        if (loginMember==null && loginAccount!=null) {
            logger.error("member not active/found, loginAccount={}", loginAccount);
        }
    }

    protected EcMember getAuthMember() {
        if (null == loginMember) {
            String error = "尚未建立会员资料或会员帐号已停用!";
            logger.error(error);
            throw new ServiceException(error);
        }
        return loginMember;
    }

    protected EcCustomer getAuthCustomer(Long customer_id) {
        if (null == customer_id) {
            String error = "未指定客户参数!";
            logger.error(error);
            throw new ServiceException(error);
        }
        EcCustomer ecCustomer = ecCustomerFacade.find(customer_id);
        if (null == ecCustomer || !ecCustomer.isActive() || !ecMemberFacade.isMemberCusomerExist(loginMember, ecCustomer)) {
            String error = "无权使用此客户或此客户帐号已停用!";
            logger.error("unauthorized customer id:{}, member:{}", customer_id, loginMember.getId());
            throw new ServiceException(error);
        }
        return ecCustomer;
    }

    protected EcCustomer getAuthCustomer(String customer_code) {
        if (null == customer_code) {
            String error = "未指定客户参数!";
            logger.error(error);
            throw new ServiceException(error);
        }
        EcCustomer ecCustomer = ecCustomerFacade.findByCode(customer_code);
        if (null == ecCustomer || !ecCustomer.isActive() || !ecMemberFacade.isMemberCusomerExist(loginMember, ecCustomer)) {
            String error = "无权使用此客户或此客户帐号已停用!";
            logger.error("unauthorized customer code:{}, member:{}", customer_code, loginMember.getId());
            throw new ServiceException(error);
        }
        return ecCustomer;
    }

    protected EcContract getAuthContract(Long contract_id, EcCustomer ecCustomer) {
        if (null == contract_id) {
            String error = "未指定合约参数!";
            logger.error(error);
            throw new ServiceException(error);
        }
        EcContract ecContract = ecContractFacade.find(contract_id);
        if (null == ecContract || !ecContract.isActive() || !ecContract.getEcCustomer().equals(ecCustomer)) {
            logger.error("unauthorized contract:{}, customer:{}", contract_id, ecCustomer.getId());
            String error = "无权使用此合约或此合约已停用!";
            throw new ServiceException(error);
        }
        return ecContract;
    }

    protected EcDeliveryPlace getAuthDeliveryPlace(Long delivery_id, EcCustomer ecCustomer) {
        if (null == delivery_id) {
            String error = "未设定送达地点参数!";
            logger.error(error);
            throw new ServiceException(error);
        }
        EcDeliveryPlace ecDeliveryPlace = ecDeliveryPlaceFacade.find(delivery_id);
        // TODO: 送達地點-客戶有關聯嗎?
        if (null == ecDeliveryPlace || !ecDeliveryPlace.isActive()) {
            logger.error("unauthorized deliveryPlace:{}", delivery_id);
            String error = "无此送达地点或此送达地点已停用!";
            throw new ServiceException(error);
        }
        return ecDeliveryPlace;
    }

    protected EcDeliveryPlace getAuthDeliveryPlace(String delivery_code, EcCustomer ecCustomer) {
        if (null == delivery_code) {
            String error = "未设定送达地点参数!";
            logger.error(error);
            throw new ServiceException(error);
        }
        EcDeliveryPlace ecDeliveryPlace = ecDeliveryPlaceFacade.findByCode(delivery_code);
        // TODO: 送達地點-客戶有關聯嗎?
        if (null == ecDeliveryPlace || !ecDeliveryPlace.isActive()) {
            logger.error("unauthorized deliveryPlace:{}, customer:{}", delivery_code, ecCustomer.getId());
            String error = "无此送达地点或此送达地点已停用!";
            throw new ServiceException(error);
        }
        return ecDeliveryPlace;
    }

    protected EcPlant getAuthPlant(Long plant_id, EcCustomer ecCustomer) {
        if (null == plant_id) {
            String error = "未设定出货工厂参数!";
            logger.error(error);
            throw new ServiceException(error);
        }
        EcPlant ecPlant = ecPlantFacade.find(plant_id);
        // TODO: 廠-客戶關聯
        if (null == ecPlant || !ecPlant.isActive()) {
            logger.error("unauthorized plant:{}", plant_id);
            String error = "无此出货工厂或此出货工厂已停用!";
            throw new ServiceException(error);
        }
        return ecPlant;
    }
    
    protected EcOrder getAuthOrder(Long order_id) {
        if (null == order_id) {
            String error = "未设定订单参数!";
            logger.error(error);
            throw new ServiceException(error);
        }
        EcOrder order = orderFacade.find(order_id);
        if (null == order || !order.getMemberId().equals(loginMember)) {
            logger.error("not exist or not owner, order:{}, member:{}", order_id, loginMember.getId());
            throw new ServiceException("订单不存在或不是订单owner!");
        }
        return order;
    }
    
    protected <T> T fromJson(String json, Class<T> clazz) throws ServiceException {
        try {
            return clazz==String.class ? (T) json : mapper.readValue(json, clazz);
        } catch (IOException ex) {
            throw new ServiceException("mapper.readValue exception!", ex);
        }
    }

    protected String toJson(Object obj) throws ServiceException {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new ServiceException("apper.writeValueAsString exception", ex);
        }
    }
}
