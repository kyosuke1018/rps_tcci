/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcCurrency;
import com.tcci.fcs.entity.FcReportUpload;
import com.tcci.fcs.entity.FcReportValue;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.enums.ReportSheetEnum;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class FcReportValueFacade {
    private static final Logger logger = LoggerFactory.getLogger(FcReportValueFacade.class);
            
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public void saveExcelValue(FcReportUpload reportUpload, Collection<FcReportValue> values, TcUser modifier, Date modifytimestamp) {
        // remove old data
        Query q = em.createQuery("DELETE FROM FcReportValue v WHERE v.report=:report");
        q.setParameter("report", reportUpload);
        q.executeUpdate();
        // insert new data
        for (FcReportValue rv : values) {
            rv.setReport(reportUpload);
            rv.setModifier(modifier);
            rv.setModifytimestamp(modifytimestamp);
            em.persist(rv);
        }
    }
    
    public void updateValues(FcReportUpload report, FcCompany coid2, String sheet, Map<String, BigDecimal> values, TcUser modifier, Date modifytimestamp) {
        // 舊資料要刪除或更新
        List<FcReportValue> origValues = find(report, coid2, sheet);
        Set<String> origCodes = new HashSet<String>();
        for (FcReportValue orig : origValues) {
            String code = orig.getCode();
            origCodes.add(code);
            BigDecimal newAmount = values.get(code); // 如果newAmount是null表示要刪除(amountOrig無資料時)或改成0
            updateValue(orig, newAmount, modifier, modifytimestamp);
        }
        // 新增資料
        for (Map.Entry<String, BigDecimal> entry : values.entrySet()) {
            String code = entry.getKey();
            if (origCodes.contains(code)) {
                continue;
            }
            BigDecimal amount = entry.getValue();
            FcReportValue reportValue = new FcReportValue(report, sheet, coid2, code, amount, modifier, modifytimestamp);
            reportValue.setAmountOrig(null); // 不是excel原始值
            // logger.warn("insert sheet:{}, code:{}, amount:{}", new Object[]{sheet, code, amount});
            em.persist(reportValue);
        }
        
    }
    
    private List<FcReportValue> find(FcReportUpload report, FcCompany coid2, String sheet) {
        Query q = em.createQuery("SELECT v FROM FcReportValue v WHERE v.report=:report AND v.coid2=:coid2 AND v.sheet=:sheet");
        q.setParameter("report", report);
        q.setParameter("coid2", coid2);
        q.setParameter("sheet", sheet);
        return q.getResultList();
    }

    /*
     * 更新 FcReportValue rv及更新時間
     * 如果 newAmount 是 null
     *      如果 rv.amountOrig 是 null -> 刪除 rv
     *      如果 rv.amount != ZERO -> rv.amount=BigDecimal.ZERO
     * 如果 newAmount 不是 null
     *      如果 rv.amount != newAmount -> rv.amount = newAmount
     */
    private void updateValue(FcReportValue rv, BigDecimal newAmount, TcUser modifier, Date modifytimestamp) {
        if (null == newAmount) {
            if (null == rv.getAmountOrig()) {
                // logger.warn("remove sheet:{}, code:{}, amount:{}", new Object[]{rv.getSheet(), rv.getCode(), rv.getAmount()});
                em.remove(rv);
            } else {
                BigDecimal amount = rv.getAmount();
                if (amount != null && amount.compareTo(BigDecimal.ZERO) != 0) {
                    rv.setAmount(BigDecimal.ZERO);
                    rv.setModifier(modifier);
                    rv.setModifytimestamp(modifytimestamp);
                    // logger.warn("update sheet:{}, code:{}, amount:{} to ZERO", new Object[]{rv.getSheet(), rv.getCode(), rv.getAmount()});
                    em.merge(rv);
                }
            }
        } else {
            BigDecimal amount = rv.getAmount();
            if (amount != null && amount.compareTo(newAmount) != 0) {
                rv.setAmount(newAmount);
                rv.setModifier(modifier);
                rv.setModifytimestamp(modifytimestamp);
                // logger.warn("update sheet:{}, code:{}, new amount:{}", new Object[]{rv.getSheet(), rv.getCode(), rv.getAmount()});
                em.merge(rv);
            }
        }
    }
    
    //沖銷彙總表 只取D0206作統計
    public List<ReportBaseVO> findByYM(String yearmonth, //
	    CompanyGroupEnum companyGroup){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("u.yearmonth as ym, \n");
        sb.append("c.code as compCode, \n");
        sb.append("c.name as compName, \n");
        sb.append("v.code as accCode, \n");
        sb.append("acc.name as accName, \n");
        sb.append("sum(v.amount) as amount, \n");
        sb.append("sum(v.amount_xls) as amountXls \n");
        sb.append("from FC_REPORT_VALUE v \n");
        sb.append("join FC_REPORT_UPLOAD u on v.report_id = u.id \n");
        sb.append("join FC_COMPANY c on u.company = c.id \n");
        sb.append("left outer join FC_ACCOUNTS acc on acc.code = v.code ");
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
        sb.append("and v.sheet = #sheetD0206 \n");
        params.put("sheetD0206", ReportSheetEnum.D0206.getCode());
        sb.append("and u.YEARMONTH = #yearmonth \n");
        params.put("yearmonth", yearmonth);
	sb.append("and c.COMBINE_INCOME = 'true' \n");
	if (companyGroup != null) {
	    sb.append("and c.COMP_GROUP = #group \n");
            params.put("group", companyGroup.getCode());
	}
        sb.append("group by u.yearmonth,c.code,c.name,v.code,acc.name ");
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
        
        List<ReportBaseVO> resultList = new ArrayList<ReportBaseVO>();
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
            resultList.add(vo);
        }
        return resultList;
    }
    
    public List<ReportBaseVO> findByYM(String yearmonth){
        return this.findByYM(yearmonth, null);
    }
    
    /*
      單月D0206營業及營業外收入
      單月D0208營業及營業外支出
    */
    public List<ReportBaseVO> findMonlyByYM(String yearmonth, FcCompany company, ReportSheetEnum sheet){
        
        boolean isJan = "01".equals(yearmonth.substring(4, 6));//報表年月是否為一月
        
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("u.yearmonth as ym, \n");
        sb.append("c.code as compCode, \n");
        sb.append("c.name as compName, \n");
        sb.append("v.code as accCode, \n");
        sb.append("acc.name as accName, \n");
        sb.append("c2.code comp2Code, \n");
        sb.append("c2.name as comp2Name, \n");
        if(isJan){
            sb.append("ISNULL(v.amount,0) as amount, \n");
            sb.append("ISNULL(v.amount_xls,0) as amountXls \n");
        }else{
            sb.append("(ISNULL(v.amount,0) - ISNULL(last.amount,0)) as amount, \n");
            sb.append("(ISNULL(v.amount_xls,0) - ISNULL(last.amount_xls,0)) as amountXls \n");
        }
        sb.append("from FC_REPORT_VALUE v \n");
        sb.append("join FC_REPORT_UPLOAD u on v.report_id = u.id \n");
        sb.append("join FC_COMPANY c on u.company = c.id \n");
        sb.append("join FC_COMPANY c2 on v.coid2 = c2.id \n");
        sb.append("left outer join FC_ACCOUNTS acc on acc.code = v.code ");
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
        if(!isJan){
            sb.append("left outer join \n");
            sb.append("  select  \n");
            sb.append("  u.company, \n");
            sb.append("  v.sheet, \n");
            sb.append("  v.coid2 as company2, \n");
            sb.append("  v.code as accCode, \n");
            sb.append("  v.amount, \n");
            sb.append("  v.amount_xls \n");
            sb.append("  from FC_REPORT_VALUE v \n");
            sb.append("  join FC_REPORT_UPLOAD u on v.report_id = u.id \n");
            sb.append("  join FC_COMPANY c on u.company = c.id \n");
            sb.append("  where 1=1 \n");
            sb.append("  and u.YEARMONTH = convert(char(6), DATEADD(day,-1,convert(datetime, #yearmonth + '01')), 112) \n");//取得前月份
            sb.append(") last on last.company = u.company \n");
            sb.append("and last.company2 = v.coid2 \n");
            sb.append("and last.sheet = v. sheet \n");
            sb.append("and last.accCode = v.code \n");
        }
        sb.append("where 1=1 \n");
        sb.append("and u.YEARMONTH = #yearmonth \n");
        params.put("yearmonth", yearmonth);
        sb.append("and u.company = #company \n");
        params.put("company", company.getId());
        sb.append("and v.sheet = #sheetCode \n");
        params.put("sheetCode", sheet.getCode());
        sb.append("order by c2.code, v.code ");
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
            vo.setComp2Code((String) columns[5]);
            vo.setComp2Name((String) columns[6]);
            vo.setAmount((BigDecimal) columns[7]);
            vo.setAmountXls((BigDecimal) columns[8]);
            resultList.add(vo);
        }
        return resultList;
    }
    
    //依幣別 年月 查詢
    public List<FcReportValue> findByYmCurr(FcCurrency currency, FcCurrency toCurrency, String ym){
        Query q = em.createQuery("SELECT v FROM FcReportValue v WHERE v.report.company.currency=:currency AND v.report.company.group.currency=:toCurrency AND v.report.yearmonth = :yearmonth ");
        q.setParameter("currency", currency);
        q.setParameter("toCurrency", toCurrency);
        q.setParameter("yearmonth", ym);
        List<FcReportValue> list = q.getResultList();
        return q.getResultList();
    }
    
}
