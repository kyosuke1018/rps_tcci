/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.ar.SkProductCategory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Type;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gilbert.Lin
 */
@Path("category")
@Stateless
public class SkProductCategoryFacade extends AbstractFacade<SkProductCategory> {

    Logger logger = LoggerFactory.getLogger(SkProductCategoryFacade.class);
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkProductCategoryFacade() {
        super(SkProductCategory.class);
    }

    @GET
    @Path("findall")
    @Produces("text/plain; charset=UTF-8;")
    public String findAll(@Context HttpServletRequest request) {
        List<SkProductCategory> categoryList = findAll();
        String jsonList = new Gson().toJson(categoryList);
        Type listType = new TypeToken<ArrayList<SkProductCategory>>() {
        }.getType();
        List<SkProductCategory> newCategoryList = new Gson().fromJson(jsonList, listType);
        logger.debug("newCategoryList.size()={}", newCategoryList.size());
        for (SkProductCategory newCategory : newCategoryList) {
            logger.debug("category={}", newCategory);
        }
//        List<SkProductCategory> newCategoryList = new Gson().fromJson(jsonList, List.class);
//        logger.debug("newCategoryList.size()={}",newCategoryList.size());
//        for(Object object: newCategoryList) {
//            SkProductCategory category = new Gson().fromJson(object.toString(),SkProductCategory.class);
//            logger.debug("category={}",category);
//        }
        return new Gson().toJson(categoryList);
    }
}
