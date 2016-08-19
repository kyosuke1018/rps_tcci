/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcCurrency;
import com.tcci.fcs.entity.FcReportA0102;
import com.tcci.fcs.entity.FcReportUpload;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
public class FcReportA0102Facade {
    private static final Logger logger = LoggerFactory.getLogger(FcReportA0102Facade.class);
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public void saveExcelValue(FcReportUpload reportUpload, Collection<FcReportA0102> values, TcUser modifier, Date modifytimestamp) {
        // remove old data
        Query q = em.createQuery("DELETE FROM FcReportA0102 a WHERE a.report=:report");
        q.setParameter("report", reportUpload);
        q.executeUpdate();
        // insert new data
        for (FcReportA0102 rv : values) {
            rv.setReport(reportUpload);
            rv.setModifier(modifier);
            rv.setModifytimestamp(modifytimestamp);
            em.persist(rv);
        }
    }
    
    public List<FcReportA0102> findByReport(FcReportUpload reportUpload){
        Query q = em.createQuery("SELECT a FROM FcReportA0102 a WHERE a.report=:report");
        q.setParameter("report", reportUpload);
        return q.getResultList();
    }
    
    public FcReportA0102 findByAccount(FcCompany company, String ym, String accCode){
        Query q = em.createQuery("SELECT a FROM FcReportA0102 a WHERE a.report.company=:company AND a.report.yearmonth = :yearmonth AND a.code = :code ");
        q.setParameter("company", company);
        q.setParameter("yearmonth", ym);
        q.setParameter("code", accCode);
        List<FcReportA0102> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    //依幣別 年月 查詢
    public List<FcReportA0102> findByYmCurr(FcCurrency currency, FcCurrency toCurrency, String ym){
        Query q = em.createQuery("SELECT a FROM FcReportA0102 a WHERE a.report.company.currency=:currency AND a.report.company.group.currency=:toCurrency AND a.report.yearmonth = :yearmonth ");
        q.setParameter("currency", currency);
        q.setParameter("toCurrency", toCurrency);
        q.setParameter("yearmonth", ym);
        List<FcReportA0102> list = q.getResultList();
        return q.getResultList();
    }
    
    public List<ReportBaseVO> findReporVOByReport(FcReportUpload reportUpload){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("u.yearmonth as ym, \n");
        sb.append("c.code as compCode, \n");
        sb.append("c.name as compName, \n");
        sb.append("c.abbreviation as abbreviation, \n");
        sb.append("a.code as accCode, \n");
        sb.append("acc.name as accName, \n");
        sb.append("a.amount, \n");
        sb.append("a.amount_xls as amountXls \n");
        sb.append("from FC_REPORT_A0102 a \n");
        sb.append("join FC_REPORT_UPLOAD u on a.report_id = u.id \n");
        sb.append("join FC_COMPANY c on u.company = c.id \n");
        sb.append("left outer join FC_ACCOUNTS acc on acc.code = a.code and acc.COMP_GROUP = c.COMP_GROUP \n");
        if (reportUpload != null) {
            String accCompGroup;
//            if(reportUpload.getCompany().getGroup().equals(CompanyGroupEnum.CSRC_BVI)){//CSRC_BVI 使用CSRC會科
//                accCompGroup = CompanyGroupEnum.CSRC.getCode();
//            }else{
                accCompGroup = reportUpload.getCompany().getGroup().getCode();
//            }
            sb.append("and acc.COMP_GROUP = #accCompGroup \n");
            params.put("accCompGroup", accCompGroup);
        }else{
            sb.append("and acc.COMP_GROUP = c.COMP_GROUP \n");
        }
        sb.append("where 1=1 \n");
        if (reportUpload != null) {
            sb.append("and u.ID = #uploadId \n");
            params.put("uploadId", reportUpload.getId());
        }
        sb.append("order by a.code ");
        logger.debug("sql: "+sb);
        
        Query query = em.createNativeQuery(sb.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
            logger.debug("list.size()= 0 ");
            return list;
        }
        
        List<ReportBaseVO> resultList = new ArrayList<>();
        for (Object row : list) {
            ReportBaseVO vo = new ReportBaseVO();
            Object[] columns = (Object[]) row;
            vo.setYm((String) columns[0]);
            vo.setCompCode((String) columns[1]);
            vo.setCompName((String) columns[2]);
            vo.setAbbreviation((String) columns[3]);
            vo.setAccCode((String) columns[4]);
            vo.setAccName((String) columns[5]);
            vo.setAmount((BigDecimal) columns[6]);
            vo.setAmountXls((BigDecimal) columns[7]);
            resultList.add(vo);
        }
        return resultList;
        
    }
    
    //損益表 累計營收
    public List<ReportBaseVO> findByYM(String yearmonth
	    , CompanyGroupEnum companyGroup, FcCompany company
	    ){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("u.yearmonth as ym, \n");
        sb.append("c.code as compCode, \n");
        sb.append("c.name as compName, \n");
        sb.append("c.abbreviation as abbreviation, \n");
        sb.append("a.code as accCode, \n");
        sb.append("acc.name as accName, \n");
        sb.append("a.amount, \n");
        sb.append("a.amount_xls as amountXls \n");
        sb.append("from FC_REPORT_A0102 a \n");
        sb.append("join FC_REPORT_UPLOAD u on a.report_id = u.id \n");
        sb.append("join FC_COMPANY c on u.company = c.id \n");
        sb.append("left outer join FC_ACCOUNTS acc on acc.code = a.code ");
        if (companyGroup != null) {
            String accCompGroup;
//            if(companyGroup.equals(CompanyGroupEnum.CSRC_BVI)){//CSRC_BVI 使用CSRC會科
//                accCompGroup = CompanyGroupEnum.CSRC.getCode();
//            }else{
                accCompGroup = companyGroup.getCode();
//            }
            sb.append("and acc.COMP_GROUP = #accCompGroup \n");
            params.put("accCompGroup", accCompGroup);
        }else{
            sb.append("and acc.COMP_GROUP = c.COMP_GROUP \n");
        }
        sb.append("where 1=1 \n");
	sb.append("and c.COMBINE_INCOME = 'true' \n");
        sb.append("and u.YEARMONTH = #yearmonth \n");
        params.put("yearmonth", yearmonth);
        if(company != null){
            sb.append("and u.COMPANY = #company \n");
            params.put("company", company.getId());
        }
	if (companyGroup != null) {
	    sb.append("and c.COMP_GROUP = #group \n");
            params.put("group", companyGroup.getCode());
	}
//        sb.append("order by c.sort, a.code ");
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
        
        List<ReportBaseVO> resultList = new ArrayList<>();
        for (Object row : list) {
            ReportBaseVO vo = new ReportBaseVO();
            Object[] columns = (Object[]) row;
            vo.setYm((String) columns[0]);
            vo.setCompCode((String) columns[1]);
            vo.setCompName((String) columns[2]);
            vo.setAbbreviation((String) columns[3]);
            vo.setAccCode((String) columns[4]);
            vo.setAccName((String) columns[5]);
            vo.setAmount((BigDecimal) columns[6]);
            vo.setAmountXls((BigDecimal) columns[7]);
	    resultList.add(vo);
        }
        return resultList;
    }
    public List<ReportBaseVO> findByYM(String yearmonth
	    , CompanyGroupEnum companyGroup){
        
	return this.findByYM(yearmonth, companyGroup, null);
    }
    public List<ReportBaseVO> findByYM(String yearmonth, FcCompany company){
        
	return this.findByYM(yearmonth, null, company);
    }
    public List<ReportBaseVO> findByYM(String yearmonth){
        return this.findByYM(yearmonth, null, null);
    }
    
    //損益表 單月營收
    public List<ReportBaseVO> findMonthlyByYM(String yearmonth, FcCompany company){
        
        boolean isJan = "01".equals(yearmonth.substring(4, 6));//報表年月是否為一月
        if(isJan){
            return this.findByYM(yearmonth, company);
        }
        
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("u.yearmonth as ym, \n");
        sb.append("c.code as compCode, \n");
        sb.append("c.name as compName, \n");
        sb.append("a.code as accCode, \n");
        sb.append("acc.name as accName, \n");
        sb.append("ISNULL(a.amount,0) as amount, \n");
        sb.append("ISNULL(a.amount_xls,0) as amountXls, \n");
        sb.append("(ISNULL(a.amount,0) - ISNULL(last.amount,0)) as amountMonthly, \n");
        sb.append("(ISNULL(a.amount_xls,0) - ISNULL(last.amount_xls,0)) as amountXlsMonthly \n");
        sb.append("from FC_REPORT_A0102 a \n");
        sb.append("join FC_REPORT_UPLOAD u on a.report_id = u.id \n");
        sb.append("join FC_COMPANY c on u.company = c.id \n");
        sb.append("left outer join FC_ACCOUNTS acc on acc.code = a.code ");
        if (company != null) {
            String accCompGroup;
//            if(company.getGroup().equals(CompanyGroupEnum.CSRC_BVI)){//CSRC_BVI 使用CSRC會科
//                accCompGroup = CompanyGroupEnum.CSRC.getCode();
//            }else{
                accCompGroup = company.getGroup().getCode();
//            }
            sb.append("and acc.COMP_GROUP = #accCompGroup \n");
            params.put("accCompGroup", accCompGroup);
        }else{
            sb.append("and acc.COMP_GROUP = c.COMP_GROUP \n");
        }
        sb.append("left outer join ( \n");
        sb.append("  select \n");
        sb.append("  u.company, \n");
        sb.append("  a.code as accCode, \n");
        sb.append("  a.amount, \n");
        sb.append("  a.amount_xls \n");
        sb.append("  from FC_REPORT_A0102 a \n");
        sb.append("  join FC_REPORT_UPLOAD u on a.report_id = u.id \n");
        sb.append("  join FC_COMPANY c on u.company = c.id \n");
        sb.append("  where 1=1 \n");
        sb.append("  and u.YEARMONTH = convert(char(6), DATEADD(day,-1,convert(datetime, #yearmonth + '01')), 112) \n");//取得前月份
        sb.append(") last on last.company = u.company \n");
        sb.append("and last.accCode = a.code \n");
        sb.append("where 1=1 \n");
        sb.append("and u.YEARMONTH = #yearmonth \n");
        params.put("yearmonth", yearmonth);
        if (company != null) {
            sb.append("and u.company = #company \n");
            params.put("company", company.getId());
        }
        sb.append("order by c.sort, a.code ");
        logger.debug("sql: "+sb);
        
        Query query = em.createNativeQuery(sb.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
            logger.debug("list.size()= 0 ");
            return list;
        }
        
        List<ReportBaseVO> resultList = new ArrayList<>();
        for (Object row : list) {
            ReportBaseVO vo = new ReportBaseVO();
            Object[] columns = (Object[]) row;
            vo.setYm((String) columns[0]);
            vo.setCompCode((String) columns[1]);
            vo.setCompName((String) columns[2]);
            vo.setAccCode((String) columns[3]);
            vo.setAccName((String) columns[4]);
            vo.setAmount((BigDecimal) columns[5]);
            vo.setAmountXls((BigDecimal) columns[6]);
            vo.setAmountMonthly((BigDecimal) columns[7]);
            vo.setAmountXlsMonthly((BigDecimal) columns[8]);
	    resultList.add(vo);
        }
        return resultList;
    }
    
}
