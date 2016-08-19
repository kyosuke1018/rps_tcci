/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.service.product;

import com.tcci.ecdemo.model.product.Product;
import com.tcci.ecdemo.service.HMACServiceBase;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jimmy.Lee
 */
@Path("product")
public class ProductService extends HMACServiceBase {

    // 合約商品
    @GET
    @Path("contract/{contract_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> contract(@PathParam("contract_id") Long contract_id) {
        List<Product> result = new ArrayList<>();
        result.add(new Product(1L, "100C35302000", "P.S.A 32.5 袋装水泥", "decription..."));
        result.add(new Product(2L, "100C37532000", "P.Ⅱ 52.5R 袋装", "decription..."));
        result.add(new Product(3L, "100C38332000", "P.C 32.5R 袋装", "decription..."));
        return result;
    }
    
    // 廠商品
    @GET
    @Path("plant/{plant_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> plant(@PathParam("plant_id") Long plant_id) {
        List<Product> result = new ArrayList<>();
        result.add(new Product(1L, "100C35302000", "P.S.A 32.5 袋装水泥", "decription..."));
        result.add(new Product(2L, "100C37532000", "P.Ⅱ 52.5R 袋装", "decription..."));
        result.add(new Product(3L, "100C38332000", "P.C 32.5R 袋装", "decription..."));
        return result;
    }
    
}
