/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.plant;

import com.tcci.tccstore.entity.EcContract;
import com.tcci.tccstore.entity.EcDeliveryPlace;
import com.tcci.tccstore.entity.EcPlant;
import com.tcci.tccstore.entity.EcPlantProduct;
import com.tcci.tccstore.entity.EcPlantProductPK;
import com.tcci.tccstore.entity.EcPlantSalesarea;
import com.tcci.tccstore.entity.EcPlantSalesareaPK;
import com.tcci.tccstore.entity.EcProduct;
import com.tcci.tccstore.entity.EcSalesarea;
import com.tcci.tccstore.facade.AbstractFacade;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Neo.Fu
 */
@Named
@Stateless
public class EcPlantFacade extends AbstractFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcPlantFacade() {
        super(EcPlant.class);
    }

    public void save(EcPlant entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public EcPlant find(Long id) {
        return em.find(EcPlant.class, id);
    }
    
    public EcPlant findByCode(String code) {
        Query q = em.createNamedQuery("EcPlant.findByCode");
        q.setParameter("code", code);
        List<EcPlant> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    // 回傳送達地點的 省,市,區
    /*
    public List<String> query(String province, String city, List<EcPlant> ecPlants) {
        String select = "SELECT DISTINCT ";
        String from = "FROM EcDeliveryPlace e ";
        String where = "WHERE e.active=TRUE ";
        String orderby = "ORDER BY ";
        Map<String, Object> params = new HashMap<>();
        if (province != null && city != null) {
            select += "e.district ";
            where += "AND e.province=:province AND e.city=:city ";
            orderby +="e.district";
            params.put("province", province);
            params.put("city", city);
        } else if (province != null) {
            select += "e.city ";
            where += "AND e.province=:province ";
            orderby +="e.city";
            params.put("province", province);
        } else {
            select += "e.province ";
            orderby +="e.province";
        }
        if (ecPlants != null) {
            where += " AND EXISTS (SELECT 1 FROM EcPlantSalesarea ps"
                    + " WHERE ps.ecSalesarea=e.ecSalesarea"
                    + " AND ps.ecPlant in :ecPlants) ";
            params.put("ecPlants", ecPlants);
        }
        Query q = em.createQuery(select + from + where + orderby);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        return q.getResultList();
    }
    
    // 回傳送達地點
    public List<EcDeliveryPlace> query(String province, String city, String district, List<EcPlant> ecPlants) {
        String sql = "SELECT e FROM EcDeliveryPlace e WHERE e.active=TRUE"
                + " AND e.province=:province AND e.city=:city";
        if (district != null) {
            sql += " AND e.district=:district";
        }
        if (ecPlants != null) {
            sql += " AND EXISTS (SELECT 1 FROM EcPlantSalesarea ps"
                    + " WHERE ps.ecSalesarea=e.ecSalesarea"
                    + " AND ps.ecPlant in :ecPlants) ";
        }
        sql += " ORDER BY e.name";
        Query q = em.createQuery(sql);
        q.setParameter("province", province);
        q.setParameter("city", city);
        if (district != null) {
            q.setParameter("district", district);
        }
        if (ecPlants != null) {
            q.setParameter("ecPlants", ecPlants);
        }
        return q.getResultList();
    }
    */
    
    // 回傳送達地點的 省,市,區
    public List<String> query(String province, String city, List<EcPlant> ecPlants) {
        String select = "SELECT DISTINCT ";
        String from = "FROM EC_DELIVERY_PLACE dp "
                + "INNER JOIN EC_DELIVERY_VKORG dv ON dv.DELIVERY_ID=dp.ID AND dp.ACTIVE=1 "
                + "INNER JOIN EC_PLANT pl ON pl.VKORG=dv.VKORG AND pl.ACTIVE=1 "
                + "INNER JOIN EC_PLANT_SALESAREA ps ON ps.PLANT_ID=pl.ID AND ps.SALESAREA_ID=dv.SALESAREA_ID "
                + "INNER JOIN EC_SALESAREA sa ON sa.ID=ps.SALESAREA_ID AND sa.ACTIVE=1 ";
        String where = "WHERE 1=1 ";
        String orderby = "ORDER BY ";
        Map<String, Object> params = new HashMap<>();
        if (province != null && city != null) { // 省,市 -> 區
            select += "dp.DISTRICT ";
            where += "AND dp.PROVINCE=#province AND dp.CITY=#city ";
            orderby +="dp.DISTRICT";
            params.put("province", province);
            params.put("city", city);
        } else if (province != null) {
            select += "dp.CITY ";
            where += "AND dp.PROVINCE=#province ";
            orderby +="dp.CITY";
            params.put("province", province);
        } else {
            select += "dp.PROVINCE ";
            orderby +="dp.PROVINCE";
        }
        if (ecPlants != null && !ecPlants.isEmpty()) {
            // eclipselink IN clause has bug
            //where += "AND pl.ID IN (#plants) ";
            //List<Long> plants = new ArrayList<>();
            //for (EcPlant ecPlant : ecPlants) {
            //    plants.add(ecPlant.getId());
            //}
            //params.put("plants", plants);
            StringBuilder sb = new StringBuilder();
            for (EcPlant ecPlant : ecPlants) {
                if (sb.length()>0) {
                    sb.append(',');
                }
                sb.append(ecPlant.getId());
            }
            where += "AND pl.ID IN (" + sb.toString() + ") ";
        }
        String sql = select + from + where + orderby;
        System.out.println(sql);
        Query q = em.createNativeQuery(sql);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        return q.getResultList();
    }
    
    // 回傳送達地點
    public List<EcDeliveryPlace> query(String province, String city, String district, List<EcPlant> ecPlants) {
        String select = "SELECT DISTINCT dp.* ";
        String from = "FROM EC_DELIVERY_PLACE dp "
                + "INNER JOIN EC_DELIVERY_VKORG dv ON dv.DELIVERY_ID=dp.ID AND dp.ACTIVE=1 "
                + "INNER JOIN EC_PLANT pl ON pl.VKORG=dv.VKORG AND pl.ACTIVE=1 "
                + "INNER JOIN EC_PLANT_SALESAREA ps ON ps.PLANT_ID=pl.ID AND ps.SALESAREA_ID=dv.SALESAREA_ID "
                + "INNER JOIN EC_SALESAREA sa ON sa.ID=ps.SALESAREA_ID AND sa.ACTIVE=1 ";
        String where = "WHERE dp.PROVINCE=#province AND dp.CITY=#city AND dp.DISTRICT=#district ";
        String orderby = "ORDER BY dp.name";
        Map<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        if (ecPlants != null && !ecPlants.isEmpty()) {
            // eclipselink IN clause has bug
            //where += "AND pl.ID IN (#plants) ";
            //List<Long> plants = new ArrayList<>();
            //for (EcPlant ecPlant : ecPlants) {
            //    plants.add(ecPlant.getId());
            //}
            //params.put("plants", plants);
            StringBuilder sb = new StringBuilder();
            for (EcPlant ecPlant : ecPlants) {
                if (sb.length()>0) {
                    sb.append(',');
                }
                sb.append(ecPlant.getId());
            }
            where += "AND pl.ID IN (" + sb.toString() + ") ";
        }
        Query q = em.createNativeQuery(select + from + where + orderby, EcDeliveryPlace.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        return q.getResultList();
    }

    // 移到 EcDeliveryPlaceFacde
    // 回傳客戶的常用送達地點
    /*
    public List<EcDeliveryPlace> query(EcCustomer ecCustomer) {
        Query q = em.createQuery("SELECT e.ecDeliveryPlace FROM EcCustomerDelivery e"
                + " WHERE e.ecCustomer=:ecCustomer AND e.ecDeliveryPlace.active=TRUE"
                + " ORDER BY e.ecDeliveryPlace.name");
        q.setParameter("ecCustomer", ecCustomer);
        return q.getResultList();
    }
    
    public void preferenceAdd(EcCustomer ecCustomer, EcDeliveryPlace ecDeliveryPlace) {
        EcCustomerDeliveryPK pk = new EcCustomerDeliveryPK(ecCustomer.getId(), ecDeliveryPlace.getId());
        EcCustomerDelivery entity = em.find(EcCustomerDelivery.class, pk);
        if (entity == null) {
            entity = new EcCustomerDelivery(pk);
            entity.setEcCustomer(ecCustomer);
            entity.setEcDeliveryPlace(ecDeliveryPlace);
            em.persist(entity);
        }
    }
    
    public void preferenceRemove(EcCustomer ecCustomer, EcDeliveryPlace ecDeliveryPlace) {
        EcCustomerDeliveryPK pk = new EcCustomerDeliveryPK(ecCustomer.getId(), ecDeliveryPlace.getId());
        EcCustomerDelivery entity = em.find(EcCustomerDelivery.class, pk);
        if (entity != null) {
            em.remove(entity);
        }
    }
    */
    
    public List<EcPlant> findBySalesarea(EcSalesarea ecSalesarea) {
        Query q = em.createQuery("SELECT e.ecPlant FROM EcPlantSalesarea e WHERE e.ecSalesarea=:ecSalesarea");
        q.setParameter("ecSalesarea", ecSalesarea);
        return q.getResultList();
    }
    
    public List<EcPlant> findByContract(EcContract ecContract) {
        Query q = em.createQuery("SELECT DISTINCT e.ecPlant FROM EcContractProduct e WHERE e.ecContract=:ecContract");
        q.setParameter("ecContract", ecContract);
        return q.getResultList();
    }
    
    public List<EcPlant> findByDelivery(EcDeliveryPlace ecDeliveryPlace) {
        String sql = "SELECT DISTINCT pl.* "
                + "FROM EC_DELIVERY_PLACE dp "
                + "INNER JOIN EC_DELIVERY_VKORG dv ON dv.DELIVERY_ID=dp.ID AND dp.ACTIVE=1 "
                + "INNER JOIN EC_PLANT pl ON pl.VKORG=dv.VKORG AND pl.ACTIVE=1 "
                + "INNER JOIN EC_PLANT_SALESAREA ps ON ps.PLANT_ID=pl.ID AND ps.SALESAREA_ID=dv.SALESAREA_ID "
                + "INNER JOIN EC_SALESAREA sa ON sa.ID=ps.SALESAREA_ID AND sa.ACTIVE=1 "
                + "WHERE dp.id=#delivery_id "
                + "ORDER BY pl.code";
        Query q = em.createNativeQuery(sql, EcPlant.class);
        q.setParameter("delivery_id", ecDeliveryPlace.getId());
        return q.getResultList();
    }

    public EcPlantProduct findPlantProduct(EcPlant ecPlant, EcProduct ecProduct) {
        EcPlantProductPK pk = new EcPlantProductPK(ecPlant.getId(), ecProduct.getId());
        return em.find(EcPlantProduct.class, pk);
    }
    
    public void savePlantProduct(EcPlantProduct entity) {
        if (entity.getEcPlantProductPK() == null) {
            long plantId = entity.getEcPlant().getId();
            long productId = entity.getEcProduct().getId();
            EcPlantProductPK pk = new EcPlantProductPK(plantId, productId);
            entity.setEcPlantProductPK(pk);
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public EcPlantSalesarea findPlantSalesarea(EcPlant ecPlant, EcSalesarea ecSalesarea) {
        EcPlantSalesareaPK pk = new EcPlantSalesareaPK(ecPlant.getId(), ecSalesarea.getId());
        return em.find(EcPlantSalesarea.class, pk);
    }
    
    public void savePlantSalesarea(EcPlantSalesarea entity) {
        if (entity.getEcPlantSalesareaPK() == null) {
            long plantId = entity.getEcPlant().getId();
            long salesaredId = entity.getEcSalesarea().getId();
            EcPlantSalesareaPK pk = new EcPlantSalesareaPK(plantId, salesaredId);
            entity.setEcPlantSalesareaPK(pk);
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

    public List<EcPlantSalesarea> findAllPlantSalesarea() {
        return em.createNamedQuery("EcPlantSalesarea.findAll").getResultList();
    }

}
