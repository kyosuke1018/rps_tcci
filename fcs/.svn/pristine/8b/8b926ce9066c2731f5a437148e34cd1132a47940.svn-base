/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.facade.sheetdata;

import com.tcci.fc.facade.util.NativeSQLUtils;
import com.tcci.fc.util.time.DateUtils;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.irs.entity.sheetdata.IrsCompanyClose;
import com.tcci.irs.facade.AbstractFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
public class IrsCompanyCloseFacade extends AbstractFacade<IrsCompanyClose> {
    @EJB
    private FcCompanyFacade companyFacade;
    
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IrsCompanyCloseFacade() {
        super(IrsCompanyClose.class);
    }
    
//    public void init(IrsCompanyClose condVO){
//        FcCompany company = condVO.getCompany();
//        
//        List<IrsCompanyClose> list = find(company);
//        if(CollectionUtils.isEmpty(list)){
//            IrsCompanyClose entity = new IrsCompanyClose();
//            entity.setCompany(company);
////            entity.setCompanyCode(company.getCode());
//            String yearMonth = DateUtils.getYearMonth(Calendar.getInstance());
//            entity.setYearMonth(yearMonth);
//            
//            entity.setCreatetimestamp(new Date());
//            entity.setCreator(condVO.getCreator());
//
//            create(entity);
//        }
//
//    }
    
    public List<IrsCompanyClose> find(FcCompany company){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT ");
        sql.append("ID, ");
        sql.append("1 AS X ");
        sql.append("from IRS_COMPANY_CLOSE COMPANY_CLOSE ");
        sql.append("WHERE 1=1 ");
        
        if(null != company){
            String pColumnName = "COMPANY_CLOSE.COMPANY_CODE";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, company.getCode(), params));
        }

        
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List<Object[]> list = query.getResultList();
        
        List<IrsCompanyClose> resultList = new ArrayList<>();
        for (Object[] row : list) {
            long id = (Long)row[0];
            IrsCompanyClose entity = find(id);
            
            resultList.add(entity);
        }
        return resultList;
    }
    
    public IrsCompanyClose findByGroup(CompanyGroupEnum group){
        Query q;
        if(group !=null){
            q = em.createNamedQuery("IrsCompanyClose.findByGroup");
            q.setParameter("group", group);
        }else{
            q = em.createNamedQuery("IrsCompanyClose.findAll");
        }
        List<IrsCompanyClose> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public IrsCompanyClose findByYearMonth(String yearMonth){
        Query q;
        if(StringUtils.isNotEmpty(yearMonth)){
            q = em.createNamedQuery("IrsCompanyClose.findByYearMonth");
            q.setParameter("yearMonth", yearMonth);
        }else{
            q = em.createNamedQuery("IrsCompanyClose.findAll");
        }
        List<IrsCompanyClose> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
//    public List<IrsCompanyClose> findByGroup(CompanyGroupEnum group){
//        Map<String, Object> params = new HashMap<>();
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT ");
//        sql.append("icc.ID, ");
//        sql.append("fc.ID ");
////        sql.append("icc.YEAR_MONTH, ");
////        sql.append("icc.CREATETIMESTAMP ");
//        sql.append("from FC_COMPANY fc ");
//        sql.append("left outer join IRS_COMPANY_CLOSE icc on fc.code = icc.COMPANY_CODE  ");
//        sql.append("WHERE 1=1 ");
//        sql.append("and fc.ACTIVE = 'true' ");
//        if(null != group){
//            sql.append("and fc.COMP_GROUP = #group ");
//            params.put("group", group.getCode());
//        }
//        sql.append("order by fc.code ");
//        
//        Query query = em.createNativeQuery(sql.toString());
//        for (String key : params.keySet()) {
//            query.setParameter(key, params.get(key));
//        }
//        List<Object[]> list = query.getResultList();
//        
//        List<IrsCompanyClose> resultList = new ArrayList<>();
//        for (Object[] row : list) {
//            if(row[0] != null){
//                long id = (Long)row[0];
//                IrsCompanyClose entity = find(id);
//                resultList.add(entity);
//            }else{
//                long cid = (Long)row[1];
//                IrsCompanyClose newEntity = new IrsCompanyClose();
//                newEntity.setCompany(companyFacade.find(cid));
//                resultList.add(newEntity);
//            }
//        }
//        return resultList;
//    }
}
