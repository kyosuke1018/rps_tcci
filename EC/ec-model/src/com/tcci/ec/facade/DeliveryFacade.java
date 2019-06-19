/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.ec.vo.DeliveryPlace;
import com.tcci.fc.util.ResultSetHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class DeliveryFacade {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * 依條件查詢送達地點
     * @return 
     */
    public List<DeliveryPlace> findByCriteria(String account, Long deliveryId, String delivery_code, 
            String province, String city, String district){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT distinct \n");
        if (deliveryId !=null
                || StringUtils.isNotBlank(delivery_code)
                || StringUtils.isNotBlank(district)) {
//            sql.append("dp.code, dp.NAME, dp.PROVINCE, dp.CITY, dp.DISTRICT, dp.TOWN ");
            sql.append("dp.* ");
            sql.append(", s.id as salesarea, s.code as salesareaCode \n");
        }else{
            if (StringUtils.isBlank(province) && StringUtils.isBlank(city)){
                sql.append("dp.PROVINCE ");
            }else if (StringUtils.isNotBlank(province) && StringUtils.isBlank(city)){
                sql.append("dp.CITY ");
            }else if (StringUtils.isNotBlank(province) && StringUtils.isNotBlank(city)){
                sql.append("dp.DISTRICT ");
            }
        }
        
//        if (StringUtils.isNotBlank(account)) {
//            sql.append("FROM TCCSTORE_USER.EC_MEMBER m \n");
//            sql.append("join TCCSTORE_USER.EC_MEMBER_CUSTOMER mc on mc.MEMBER_ID = m.id \n");
//            sql.append("join TCCSTORE_USER.EC_CUSTOMER c on mc.CUSTOMER_ID = c.id \n");
//            sql.append("join TCCSTORE_USER.EC_CUSTOMER_VKORG cv on cv.CUSTOMER_ID = c.id \n");
//            sql.append("join TCCSTORE_USER.EC_DELIVERY_VKORG dv on dv.VKORG = cv.VKORG \n");
//            sql.append("join ( \n");
//            sql.append(" select id, CODE, NAME, SALESAREA_ID, \n");
//            sql.append(" PROVINCE, CITY, DISTRICT, TOWN \n");
//            sql.append(" from TCCSTORE_USER.EC_DELIVERY_PLACE \n");
//            sql.append(" where 1=1 and ACTIVE = 1 \n");
//            sql.append(") dp on dp.id = dv.DELIVERY_ID \n");
//            sql.append("join TCCSTORE_USER.EC_SALESAREA s on s.id = dp.SALESAREA_ID \n");
//        }else{
            sql.append("FROM TCCSTORE_USER.EC_DELIVERY_PLACE dp \n");
            sql.append("join TCCSTORE_USER.EC_SALESAREA s on s.id = dp.SALESAREA_ID \n");
//        }
        
        sql.append("WHERE 1=1 \n");
//        if (StringUtils.isNotBlank(account)) {
//            sql.append("AND m.LOGIN_ACCOUNT=#account \n");
//            params.put("account", account);
//        }else{
            sql.append("AND dp.ACTIVE = 1 \n");
//        }
        if (deliveryId!=null) {
            sql.append("AND dp.ID=#id \n");
            params.put("id", deliveryId);
        }
        if (StringUtils.isNotBlank(delivery_code)) {
            sql.append("AND dp.code=#code \n");
            params.put("code", delivery_code);
        }
        if (StringUtils.isNotBlank(province)) {
            sql.append("AND dp.PROVINCE=#province \n");
            params.put("province", province);
        }
        if (StringUtils.isNotBlank(city)) {
            sql.append("AND dp.CITY=#city \n");
            params.put("city", city);
        }
        if (StringUtils.isNotBlank(district)) {
            sql.append("AND dp.DISTRICT=#district \n");
            params.put("district", district);
        }
        
        ResultSetHelper resultSetHelper = new ResultSetHelper(DeliveryPlace.class);
        List<DeliveryPlace> list = resultSetHelper.queryToPOJOList(em, sql.toString(), params);
        
        return list;
    }
    public List<DeliveryPlace> findById(Long deliveryId){
        return this.findByCriteria(null, deliveryId, null, null, null, null);
    }
    
    
}
