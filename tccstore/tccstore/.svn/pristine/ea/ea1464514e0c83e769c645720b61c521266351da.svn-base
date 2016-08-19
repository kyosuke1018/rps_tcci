/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.product;

import com.tcci.tccstore.entity.EcContract;
import com.tcci.tccstore.entity.EcContractProduct;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcDeliveryPlace;
import com.tcci.tccstore.entity.EcPlant;
import com.tcci.tccstore.entity.EcProduct;
import com.tcci.tccstore.service.EntityToModel;
import com.tcci.tccstore.facade.datawarehouse.WBDRFacade;
import com.tcci.tccstore.facade.product.EcProductFacade;
import com.tcci.tccstore.model.product.Product;
import com.tcci.tccstore.service.ServiceBase;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jimmy.Lee
 */
@Path("product")
public class ProductService extends ServiceBase {

    @EJB
    private EcProductFacade ecProductFacade;
    @EJB
    private WBDRFacade wbdrFacade;

    // 一般購買或合約購買 商品清單
    @POST
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> byContractPlant(@FormParam(PARAM_CUSTOMER_ID) Long customer_id,
            @FormParam(PARAM_DELIVERY_ID) Long delivery_id,
            @FormParam(PARAM_PLANT_ID) Long plant_id,
            @FormParam(PARAM_CONTRACT_ID) Long contract_id) {
        // 一般購買 contract_id 是 null
        EcCustomer ecCustomer = getAuthCustomer(customer_id);
        EcPlant ecPlant = this.getAuthPlant(plant_id, ecCustomer);
        // 有合約時，則要限定符合合約商品的廠 (雖然有合約商品有設定銷售區域，但開訂單時是可以更換的)
        // delivery_id 目前無作用
        // EcDeliveryPlace ecDeliveryPlace = delivery_id==null ? null : getAuthDeliveryPlace(delivery_id, ecCustomer);
        List<Product> result = new ArrayList<>();
        if (contract_id != null) {
            // 合約購買
            EcContract ecContract = this.getAuthContract(contract_id, ecCustomer);
            List<EcContractProduct> list = ecContractFacade.findByContract(ecContract);
            for (EcContractProduct entity : list) {
                // 廠與送達地點必需符合
                if (!entity.getEcPlant().equals(ecPlant)) {
                    continue;
                }
                result.add(EntityToModel.buildContractProduct(entity));
            }
        } else {
            // 一般購買
            List<EcProduct> ecProducts = ecProductFacade.findByPlant(ecPlant);
            for (EcProduct ecProduct : ecProducts) {
                Product product = EntityToModel.buildProduct(ecProduct);
                result.add(product);
            }
        }
        return result;
    }

    // 批號檢查
    // 回傳 true:有此批號, false:無此批號
    @POST
    @Path("batchcheck")
    @Produces(MediaType.TEXT_PLAIN)
    public String batchCheck(@FormParam(PARAM_LOTNO) String lotno, @FormParam(PARAM_AREACODE) String areacode) {
        boolean exist = wbdrFacade.isBatchExist(lotno, areacode);
        return exist ? "true" : "false";
    }

    /*
    @GET
    @Path("allpartner")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> findAllPartnerProduct() {
        List<EcProduct> ecProducts = ecProductFacade.findAllPartnerProduct();
        List<Product> result = new ArrayList<>();
        for (EcProduct entity : ecProducts) {
            result.add(EntityToModel.buildContractProduct(entity));
        }
        return result;
    }
    
    // 散客購買記錄
    @POST
    @Path("buy")
    @Produces(MediaType.TEXT_PLAIN)
    public String productBuy(
            @FormParam(PARAM_PRODUCT_ID) Long product_id, 
            @FormParam(PARAM_QUANTITY) int quantity,
            @FormParam(PARAM_UOM) String uom) {
        EcProduct ecProduct = ecProductFacade.find(product_id);
        if (null == ecProduct || !ecProduct.isActive()) {
            throw new ServiceException("此商品已停用!");
        }
        ecProductFacade.productBuy(ecProduct, loginMember, quantity, uom);
        return "OK";
    }
    */
    
}
