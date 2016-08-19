/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.plant;

import com.tcci.tccstore.entity.EcContract;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcDeliveryPlace;
import com.tcci.tccstore.entity.EcDeliveryPreference;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcPlant;
import com.tcci.tccstore.service.EntityToModel;
import com.tcci.tccstore.model.deliveryplace.DeliveryPlace;
import com.tcci.tccstore.model.plant.DeliveryPreference;
import com.tcci.tccstore.model.plant.Plant;
import com.tcci.tccstore.model.plant.PlantQueryResult;
import com.tcci.tccstore.service.ServiceBase;
import com.tcci.tccstore.service.ServiceException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Path("plant")
public class PlantService extends ServiceBase {
    private final static Logger logger = LoggerFactory.getLogger(PlantService.class);
            
    // 停用了, 改用 query2
    @Deprecated
    @POST
    @Path("query")
    @Produces(MediaType.APPLICATION_JSON)
    public PlantQueryResult query(
            @FormParam(PARAM_CUSTOMER_ID) Long customer_id,
            @FormParam(PARAM_CONTRACT_ID) Long contract_id,
            @FormParam(PARAM_DELIVERY_ID) Long delivery_id,
            @FormParam(PARAM_DELIVERY_CODE) String delivery_code,
            @FormParam(PARAM_PREFERENCE) String preference,
            @FormParam(PARAM_PROVINCE) String province,
            @FormParam(PARAM_CITY) String city,
            @FormParam(PARAM_DISTRICT) String district) {
        EcMember loginMember = getAuthMember();
        EcCustomer ecCustomer = getAuthCustomer(customer_id);
        PlantQueryResult result = new PlantQueryResult();
        List<EcDeliveryPlace> ecDeliveries = null;
        if (contract_id != null) {
            EcContract ecContract = getAuthContract(contract_id, ecCustomer);
            List<EcPlant> ecPlants = ecPlantFacade.findByContract(ecContract);
            result.setPlants(convertToPlants(ecPlants));
        } else if (delivery_id != null) {
            EcDeliveryPlace ecDeliveryPlace = getAuthDeliveryPlace(delivery_id, ecCustomer);
            List<EcPlant> ecPlants = ecPlantFacade.findBySalesarea(ecDeliveryPlace.getEcSalesarea());
            result.setPlants(convertToPlants(ecPlants));
        } else if (delivery_code != null) {
            // 同時回傳送達地點及廠
            EcDeliveryPlace ecDeliveryPlace = getAuthDeliveryPlace(delivery_code, ecCustomer);
            ecDeliveries = new ArrayList<>();
            ecDeliveries.add(ecDeliveryPlace);
            List<EcPlant> ecPlants = ecPlantFacade.findBySalesarea(ecDeliveryPlace.getEcSalesarea());
            result.setPlants(convertToPlants(ecPlants));
        } else if (preference != null) {
            ecDeliveries = ecDeliveryPlaceFacade.findPreference(loginMember);
        } else {
            boolean findDeliverys = false;
            if (province != null && city != null && district != null) {
                findDeliverys = true;
            } else {
                List<String> list = ecPlantFacade.query(province, city, null);
                result.setResult(list);
                // 區僅一筆時, 順便帶出送達地點
                // findDeliverys = list.size() == 1;
            }
            if (findDeliverys) {
                ecDeliveries = ecPlantFacade.query(province, city, district, null);
            }
        }
        if (ecDeliveries != null) {
            List<DeliveryPlace> deliveryPlaces = new ArrayList<>();
            for (EcDeliveryPlace entity : ecDeliveries) {
                deliveryPlaces.add(EntityToModel.buildDeliveryPlace(entity));
            }
            result.setDeliveryPlaces(deliveryPlaces);
        }
        return result;
    }

    @POST
    @Path("query2")
    @Produces(MediaType.APPLICATION_JSON)
    public PlantQueryResult query2(
            @FormParam(PARAM_CUSTOMER_ID) Long customer_id,
            @FormParam(PARAM_CONTRACT_ID) Long contract_id,
            @FormParam(PARAM_DELIVERY_ID) Long delivery_id,
            @FormParam(PARAM_DELIVERY_CODE) String delivery_code,
            @FormParam(PARAM_PREFERENCE) String preference,
            @FormParam(PARAM_PROVINCE) String province,
            @FormParam(PARAM_CITY) String city,
            @FormParam(PARAM_DISTRICT) String district) {
        EcMember loginMember = getAuthMember();
        EcCustomer ecCustomer = getAuthCustomer(customer_id);
        // 有合約時，則要限定符合合約商品的廠 (雖然有合約商品有設定銷售區域，但開訂單時是可以更換的)
        EcContract ecContract = contract_id==null ? null : getAuthContract(contract_id, ecCustomer);
        List<EcPlant> contractPlants = ecContract==null ? null : ecPlantFacade.findByContract(ecContract);
        //
        List<EcDeliveryPlace> ecDeliveries = null;
        PlantQueryResult result = new PlantQueryResult();
        if (delivery_code != null) { // by送達地點代碼, 回傳plants
            // 同時回傳送達地點及廠
            EcDeliveryPlace ecDeliveryPlace = getAuthDeliveryPlace(delivery_code, ecCustomer);
            //List<EcPlant> ecPlants = ecPlantFacade.findBySalesarea(ecDeliveryPlace.getEcSalesarea());
            List<EcPlant> ecPlants = ecPlantFacade.findByDelivery(ecDeliveryPlace);
            // 限定符合合約商品的廠
            if (contractPlants != null) {
                ecPlants.retainAll(contractPlants);
                if (ecPlants.isEmpty()) {
                    String error = "不符合目前所选择的合约!";
                    logger.error(error);
                    throw new ServiceException(error);
                }
            }
            ecDeliveries = new ArrayList<>();
            ecDeliveries.add(ecDeliveryPlace);
            result.setPlants(convertToPlants(ecPlants));
        } else if (preference != null) { // by常用送達地點, 回傳 deliveryPreferences
            List<EcDeliveryPreference> list = ecDeliveryPlaceFacade.findPreference2(loginMember);
            List<DeliveryPreference> deliveryPreferences = new ArrayList<>();
            for (EcDeliveryPreference dp : list) {
                // 限定符合合約商品的廠
                if (contractPlants != null && !contractPlants.contains(dp.getEcPlant())) {
                    continue;
                }
                DeliveryPlace delivery = EntityToModel.buildDeliveryPlace(dp.getEcDeliveryPlace());
                Plant plant = EntityToModel.buildPlant(dp.getEcPlant());
                deliveryPreferences.add(new DeliveryPreference(delivery, plant));
            }
            result.setDeliveryPreferences(deliveryPreferences);
        } else if (delivery_id != null) { // by送達地點id, 回傳plants
            EcDeliveryPlace ecDeliveryPlace = getAuthDeliveryPlace(delivery_id, ecCustomer);
            //List<EcPlant> ecPlants = ecPlantFacade.findBySalesarea(ecDeliveryPlace.getEcSalesarea());
            List<EcPlant> ecPlants = ecPlantFacade.findByDelivery(ecDeliveryPlace);
            // 限定符合合約商品的廠
            if (contractPlants != null) {
                ecPlants.retainAll(contractPlants);
                if (contractPlants.isEmpty()) {
                    String error = "不符合目前所选择的合约!";
                    logger.error(error);
                    throw new ServiceException(error);
                }
            }
            result.setPlants(convertToPlants(ecPlants));
        } else { // by省市區鎮
            boolean findDeliverys = false;
            if (province != null && city != null && district != null) {
                findDeliverys = true;
            } else {
                List<String> list = ecPlantFacade.query(province, city, contractPlants);
                result.setResult(list);
            }
            if (findDeliverys) {
                ecDeliveries = ecPlantFacade.query(province, city, district, contractPlants);
            }
        }
        if (ecDeliveries != null) {
            List<DeliveryPlace> deliveryPlaces = new ArrayList<>();
            for (EcDeliveryPlace entity : ecDeliveries) {
                deliveryPlaces.add(EntityToModel.buildDeliveryPlace(entity));
            }
            result.setDeliveryPlaces(deliveryPlaces);
        }
        return result;
    }
    
    @POST
    @Path("preference/add")
    @Produces(MediaType.TEXT_PLAIN)
    public String preferenceAdd(@FormParam(PARAM_CUSTOMER_ID) Long customer_id,
            @FormParam(PARAM_DELIVERY_ID) Long delivery_id,
            @FormParam(PARAM_PLANT_ID) Long plant_id) {
        EcMember loginMember = getAuthMember();
        EcCustomer ecCustomer = getAuthCustomer(customer_id);
        EcDeliveryPlace ecDeliveryPlace = getAuthDeliveryPlace(delivery_id, ecCustomer);
        if (null == plant_id) {
            return "未設定出貨工廠參數!";
        }
        EcPlant ecPlant = getAuthPlant(plant_id, ecCustomer);
        ecDeliveryPlaceFacade.addPreference(loginMember, ecDeliveryPlace, ecPlant);
        return "OK";
    }

    @POST
    @Path("preference/remove")
    @Produces(MediaType.TEXT_PLAIN)
    public String preferenceRemove(@FormParam(PARAM_CUSTOMER_ID) Long customer_id,
            @FormParam(PARAM_DELIVERY_ID) Long delivery_id,
            @FormParam(PARAM_PLANT_ID) Long plant_id) {
        EcMember loginMember = getAuthMember();
        EcCustomer ecCustomer = getAuthCustomer(customer_id);
        EcDeliveryPlace ecDeliveryPlace = getAuthDeliveryPlace(delivery_id, ecCustomer);
        if (null == plant_id) {
            return "未設定出貨工廠參數!";
        }
        EcPlant ecPlant = getAuthPlant(plant_id, ecCustomer);
        ecDeliveryPlaceFacade.removePreference(loginMember, ecDeliveryPlace, ecPlant);
        return "OK";
    }

    private List<Plant> convertToPlants(List<EcPlant> ecPlants) {
        List<Plant> result = new ArrayList<>();
        for (EcPlant entity : ecPlants) {
            result.add(EntityToModel.buildPlant(entity));
        }
        return result;
    }

}
