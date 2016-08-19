/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.facade.attachment;

import com.tcci.fc.facade.util.NativeSQLUtils;
import com.tcci.fc.util.time.DateUtils;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcReportUpload;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.irs.entity.attachment.IrsReportUpload;
import com.tcci.irs.facade.AbstractFacade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
public class IrsReportUploadFacade extends AbstractFacade<IrsReportUpload> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IrsReportUploadFacade() {
        super(IrsReportUpload.class);
    }
    
    public List<IrsReportUpload> find(String year, String factoryCode, List<String> pageCodeList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        String tableName_alias = "REPORT_UPLOAD";
sql.append("SELECT ");
sql.append("MAx(REPORT_UPLOAD.ID) ID, ");
sql.append(tableName_alias).append(".YEAR_MONTH,                           ");
sql.append("1 AS X ");
sql.append("FROM IRS_REPORT_UPLOAD ").append(tableName_alias).append(" ");
sql.append("WHERE 1=1 ");

        if(StringUtils.isNotBlank(year)){
            List<String> yearMonthList = DateUtils.getYearMonthList(year);
            String pColumnName = tableName_alias+".YEAR_MONTH";
            sql.append(NativeSQLUtils.getInSQL(pColumnName, yearMonthList, params));
        }
        if(StringUtils.isNotBlank(factoryCode)){
            String pColumnName = tableName_alias+".COMPANY_CODE";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, factoryCode, params));
        }
        if(CollectionUtils.isNotEmpty(pageCodeList)){
            String pColumnName = tableName_alias+".PAGE_CODE";
            sql.append(NativeSQLUtils.getInSQL(pColumnName, pageCodeList, params));
        }
sql.append("GROUP BY REPORT_UPLOAD.YEAR_MONTH, REPORT_UPLOAD.COMPANY_CODE, REPORT_UPLOAD.PAGE_CODE ");
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List<Object[]> list = query.getResultList();
        
        List<IrsReportUpload> entityList = new ArrayList<>();
        for (Object[] row : list) {
            long id =((Long)row[0]);
            IrsReportUpload entitiy = find(id);
            entityList.add(entitiy);
        }
        return entityList;
    }    
    
    public List<IrsReportUpload> findList(String yearmonth, String factoryCode, List<String> pageCodeList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        String tableName_alias = "REPORT_UPLOAD";
sql.append("SELECT ");
sql.append(tableName_alias).append(".ID,                           ");
sql.append("1 AS X ");
sql.append("FROM IRS_REPORT_UPLOAD ").append(tableName_alias).append(" ");
sql.append("WHERE 1=1 ");

        if(StringUtils.isNotBlank(yearmonth)){
            String pColumnName = tableName_alias+".YEAR_MONTH";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, yearmonth, params));
        }
        if(StringUtils.isNotBlank(factoryCode)){
            String pColumnName = tableName_alias+".COMPANY_CODE";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, factoryCode, params));
        }
        if(CollectionUtils.isNotEmpty(pageCodeList)){
            String pColumnName = tableName_alias+".PAGE_CODE";
            sql.append(NativeSQLUtils.getInSQL(pColumnName, pageCodeList, params));
        }

        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List<Object[]> list = query.getResultList();
        
        List<IrsReportUpload> entityList = new ArrayList<>();
        for (Object[] row : list) {
            long id =((Long)row[0]);
            IrsReportUpload entitiy = find(id);
            entityList.add(entitiy);
        }
        return entityList;
    } 
    
    public List<IrsReportUpload> findByYearmonth(String yearmonth, CompanyGroupEnum group) {
        Query q = em.createNamedQuery("IrsReportUpload.findByYearmonth");
        q.setParameter("yearmonth", yearmonth);
        q.setParameter("group", group.getCode());
        return q.getResultList();
    }
    
    public IrsReportUpload findByYearmonthCompany(String yearmonth, FcCompany company) {
        Query q = em.createNamedQuery("IrsReportUpload.findByYearmonthCompany");
        q.setParameter("yearmonth", yearmonth);
        q.setParameter("company", company);
        List<IrsReportUpload> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
}
