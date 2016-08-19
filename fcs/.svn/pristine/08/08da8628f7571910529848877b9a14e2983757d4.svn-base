/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.facade.sheetdata;

import com.tcci.fc.facade.util.NativeSQLUtils;
import com.tcci.fc.util.time.DateUtils;
import com.tcci.irs.entity.sheetdata.ZtfiAfrcInvo;
import com.tcci.irs.facade.AbstractFacade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
public class ZtfiAfrcInvoFacade extends AbstractFacade<ZtfiAfrcInvo> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZtfiAfrcInvoFacade() {
        super(ZtfiAfrcInvo.class);
    }
    
    public void save(ZtfiAfrcInvo etity) {
        if (etity.getId()==null) {
            create(etity);
//            em.persist(etity);
        } else {
            edit(etity);
//            em.merge(etity);
        }
    }
    public List<ZtfiAfrcInvo> find(String companyCode, String yearMonth) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT                                ");
        sql.append("AFRC_INVO.ID,                           ");
        sql.append("1 X ");
        sql.append("FROM ZTFI_AFRC_INVO AFRC_INVO ");
        sql.append("WHERE 1=1                             ");
        if(StringUtils.isNotBlank(yearMonth)){
            String[] string_array = DateUtils.getYearAndMonth(yearMonth);
            String year = string_array[0];
            String month = string_array[1];
            if(StringUtils.isNotBlank(year)){
                String pColumnName = "AFRC_INVO.ZGJAHR";
                sql.append(NativeSQLUtils.genEqualSQL(pColumnName, year, params));
            }
            if(StringUtils.isNotBlank(month)){
                String pColumnName = "AFRC_INVO.ZMONAT";
                sql.append(NativeSQLUtils.genEqualSQL(pColumnName, month, params));
            }
        }
        if(StringUtils.isNotBlank(companyCode)){
            String pColumnName = "AFRC_INVO.BUKRS";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, companyCode, params));
        }
        
        
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List<Object[]> list = query.getResultList();
        
        List<ZtfiAfrcInvo> resultList = new ArrayList<>();
        for (Object[] row : list) {
            long id =((long)row[0]);
            ZtfiAfrcInvo entity = find(id);
            
            resultList.add(entity);
        }
        return resultList;
//        List<ZtcoRelaGui> resultList = new ArrayList<>();
//        
//        return resultList;
    }
    public void remove(String companyCode, String yearMonth) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
//        String tableName_alias = "";
        sql.append("DELETE ZTFI_AFRC_INVO ");
        sql.append("WHERE 1=1 ");
        if(StringUtils.isNotBlank(yearMonth)){
            String[] string_array = DateUtils.getYearAndMonth(yearMonth);
            String year = string_array[0];
            String month = string_array[1];
            if(StringUtils.isNotBlank(year)){
                String pColumnName = "ZGJAHR";
                sql.append(NativeSQLUtils.genEqualSQL(pColumnName, year, params));
            }
            if(StringUtils.isNotBlank(month)){
                String pColumnName = "ZMONAT";
                sql.append(NativeSQLUtils.genEqualSQL(pColumnName, month, params));
            }
        }
        if(StringUtils.isNotBlank(companyCode)){
            String pColumnName = "BUKRS";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, companyCode, params));
        }
        
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        
        query.executeUpdate();
    }
}
