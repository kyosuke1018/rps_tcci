/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.facade;

import com.tcci.fc.facade.util.NativeSQLUtils;
import com.tcci.fc.util.StringUtils;
import com.tcci.fcs.entity.FcSapUploadRecord;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class FcSapUploadRecordFacade {
    private static final Logger logger = LoggerFactory.getLogger(FcSapUploadRecordFacade.class);
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    
    
    public List<FcSapUploadRecord> findLastUpdateList(boolean thisSeason){
//        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("r.FUNC, ");
        sb.append("r.COMPANY_CODE, ");
        sb.append("r.YEARMONTH, ");
        sb.append("mdt.maxDate, ");
        sb.append("mdt.maxTime, ");
        sb.append("r.USER_NAME ");
        sb.append("from FC_SAP_UPLOAD_RECORD r \n");
        sb.append("join (");//求maxTime
        sb.append("select ");
        sb.append("r.FUNC, ");
        sb.append("r.COMPANY_CODE, ");
        sb.append("r.YEARMONTH, ");
        sb.append("md.maxDate, ");
        sb.append("max(r.UPLOAD_TIME) as maxTime ");
        sb.append("from FC_SAP_UPLOAD_RECORD r \n");
        sb.append("join (");//先求maxDate
        sb.append(" select");
        sb.append(" FUNC,");
        sb.append(" COMPANY_CODE,");
        sb.append(" YEARMONTH,");
        sb.append(" max(UPLOAD_DATE) as maxDate");
        sb.append(" from FC_SAP_UPLOAD_RECORD");
        sb.append(" group by FUNC,COMPANY_CODE,YEARMONTH");
        sb.append(") md on r.FUNC = md.FUNC \n");
        sb.append("and r.COMPANY_CODE = md.COMPANY_CODE ");
        sb.append("and r.YEARMONTH = md.YEARMONTH ");
        sb.append("and r.UPLOAD_DATE = md.maxDate  \n");
        if(thisSeason){
            //避免資料量成長影響校能
            //只更新(報表年月為)近三個月內的上傳時間
            sb.append("where 1=1 ");
            sb.append("and r.YEARMONTH >= substring(convert(varchar, convert(datetime, DATEADD(MONTH,-3,GETDATE())), 112),1,6) ");//近三個月
        }
        sb.append("group by r.FUNC,r.COMPANY_CODE,r.YEARMONTH,md.maxDate");
        sb.append(") mdt on r.FUNC = mdt.FUNC \n");
        sb.append("and r.COMPANY_CODE = mdt.COMPANY_CODE ");
        sb.append("and r.YEARMONTH = mdt.YEARMONTH ");
        sb.append("and r.UPLOAD_DATE = mdt.maxDate  ");
        sb.append("and r.UPLOAD_TIME = mdt.maxTime  \n");
//        logger.debug("sql: "+sb);
        
        Query query = em.createNativeQuery(sb.toString());
//        for (String key : params.keySet()) {
//            query.setParameter(key, params.get(key));
//        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
            logger.debug("list.size()= 0 ");
            return list;
        }
        
        List<FcSapUploadRecord> resultList = new ArrayList<>();
        for (Object row : list) {
            FcSapUploadRecord vo = new FcSapUploadRecord();
            Object[] columns = (Object[]) row;
            vo.setFunc((String) columns[0]);
            vo.setCompanyCode((String) columns[1]);
            vo.setYearmonth((String) columns[2]);
            vo.setUploadDate((String) columns[3]);
            vo.setUploadTime((String) columns[4]);
            vo.setUserName((String) columns[5]);//20160523 新增
	    resultList.add(vo);
        }
        return resultList;
    }
    
    public List<FcSapUploadRecord> findLastUpdateList(String yearmonth){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("r.FUNC, ");
        sb.append("r.COMPANY_CODE, ");
        sb.append("r.YEARMONTH, ");
        sb.append("mdt.maxDate, ");
        sb.append("mdt.maxTime, ");
        sb.append("r.USER_NAME ");
        sb.append("from FC_SAP_UPLOAD_RECORD r \n");
        sb.append("join (");//求maxTime
        sb.append("select ");
        sb.append("r.FUNC, ");
        sb.append("r.COMPANY_CODE, ");
        sb.append("r.YEARMONTH, ");
        sb.append("md.maxDate, ");
        sb.append("max(r.UPLOAD_TIME) as maxTime ");
        sb.append("from FC_SAP_UPLOAD_RECORD r \n");
        sb.append("join (");//先求maxDate
        sb.append(" select");
        sb.append(" FUNC,");
        sb.append(" COMPANY_CODE,");
        sb.append(" YEARMONTH,");
        sb.append(" max(UPLOAD_DATE) as maxDate");
        sb.append(" from FC_SAP_UPLOAD_RECORD");
        sb.append(" group by FUNC,COMPANY_CODE,YEARMONTH");
        sb.append(") md on r.FUNC = md.FUNC \n");
        sb.append("and r.COMPANY_CODE = md.COMPANY_CODE ");
        sb.append("and r.YEARMONTH = md.YEARMONTH ");
        sb.append("and r.UPLOAD_DATE = md.maxDate  \n");
        sb.append("where 1=1 ");
        if(StringUtils.isNotBlank(yearmonth)){
            //只更新指定月份
            String pColumnName = "r.YEARMONTH";
            sb.append(NativeSQLUtils.genEqualSQL(pColumnName, yearmonth, params));
        }
        sb.append("group by r.FUNC,r.COMPANY_CODE,r.YEARMONTH,md.maxDate");
        sb.append(") mdt on r.FUNC = mdt.FUNC \n");
        sb.append("and r.COMPANY_CODE = mdt.COMPANY_CODE ");
        sb.append("and r.YEARMONTH = mdt.YEARMONTH ");
        sb.append("and r.UPLOAD_DATE = mdt.maxDate  ");
        sb.append("and r.UPLOAD_TIME = mdt.maxTime  \n");
//        logger.debug("sql: "+sb);
        
        Query query = em.createNativeQuery(sb.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
            logger.debug("list.size()= 0 ");
            return list;
        }
        
        List<FcSapUploadRecord> resultList = new ArrayList<>();
        for (Object row : list) {
            FcSapUploadRecord vo = new FcSapUploadRecord();
            Object[] columns = (Object[]) row;
            vo.setFunc((String) columns[0]);
            vo.setCompanyCode((String) columns[1]);
            vo.setYearmonth((String) columns[2]);
            vo.setUploadDate((String) columns[3]);
            vo.setUploadTime((String) columns[4]);
            vo.setUserName((String) columns[5]);//20160523 新增
	    resultList.add(vo);
        }
        return resultList;
    }
}
