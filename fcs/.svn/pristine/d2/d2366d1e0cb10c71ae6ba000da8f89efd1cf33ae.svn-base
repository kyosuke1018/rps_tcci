/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.facade.sheetdata;

import com.tcci.fc.util.time.DateUtils;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.irs.entity.sheetdata.IrsSheetdataM;
import com.tcci.irs.facade.AbstractFacade;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class IrsSheetdataMFacade extends AbstractFacade<IrsSheetdataM> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IrsSheetdataMFacade() {
        super(IrsSheetdataM.class);
    }
    
    /*不好的寫法
    public List<IrsSheetdataM> find(IrsSheetdataM condVO){
    Map<String, Object> params = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    
    sql.append("SELECT ");
    sql.append("ID, ");
    sql.append("1 AS X ");
    sql.append("from IRS_SHEETDATA_M SHEETDATA_M ");
    sql.append("WHERE 1=1 ");
    String yearMonth = condVO.getYearMonth();
    if(StringUtils.isNotBlank(yearMonth)){
    String[] string_array = DateUtils.getYearAndMonth(yearMonth);
    String year = string_array[0];
    String month = string_array[1];
    if(StringUtils.isNotBlank(year)){
    String pColumnName = "SHEETDATA_M.YEAR";
    sql.append(NativeSQLUtils.genEqualSQL(pColumnName, Integer.valueOf(year), params));
    }
    if(StringUtils.isNotBlank(month)){
    String pColumnName = "SHEETDATA_M.MONTH";
    sql.append(NativeSQLUtils.genEqualSQL(pColumnName, Integer.valueOf(month), params));
    }
    }
    FcCompany reCompany = condVO.getReCompany();
    FcCompany paCompany = condVO.getPaCompany();
    if(null != reCompany && null != paCompany){
    sql.append("and ( ");
    
    sql.append("( ");
    sql.append("[RE_COMPANY_ID] = "+ reCompany.getId() +" and [PA_COMPANY_ID] = "+ paCompany.getId() +" ");
    sql.append(") ");
    sql.append("or ");
    sql.append("( ");
    sql.append("[RE_COMPANY_ID] = "+ paCompany.getId() +" and [PA_COMPANY_ID] = "+ reCompany.getId() +" ");
    sql.append(") ");
    
    sql.append(") ");
    }
    //        if(null != reCompany){
    //            String pColumnName = "SHEETDATA_M.RE_COMPANY_ID";
    //            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, reCompany.getId(), params));
    //        }
    //
    //        if(null != paCompany){
    //            String pColumnName = "SHEETDATA_M.PA_COMPANY_ID";
    //            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, paCompany.getId(), params));
    //        }
    sql.append("order by SHEETDATA_M.SHEET_TYPE DESC, SHEETDATA_M.RE_COMPANY_ID ASC  ");
    //sql.append("order by SHEETDATA_M.[RE_ACCOUNT_CODE] ASC ");
    
    Query query = em.createNativeQuery(sql.toString());
    for (String key : params.keySet()) {
    query.setParameter(key, params.get(key));
    }
    List<Object[]> list = query.getResultList();
    
    List<IrsSheetdataM> resultList = new ArrayList<>();
    for (Object[] row : list) {
    long id = (Long)row[0];
    IrsSheetdataM entity = find(id);
    
    resultList.add(entity);
    }
    return resultList;
    }
    **/
    
    public List<IrsSheetdataM> find(IrsSheetdataM condVO){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select m from IrsSheetdataM m ");
        sql.append("WHERE 1=1 ");
        String yearMonth = condVO.getYearMonth();
        if(StringUtils.isNotBlank(yearMonth)){
            String[] string_array = DateUtils.getYearAndMonth(yearMonth);
            String year = string_array[0];
            String month = string_array[1];
            if(StringUtils.isNotBlank(year)){
                sql.append("and m.year = :year ");
                params.put("year", Integer.valueOf(year));
            }
            if(StringUtils.isNotBlank(month)){
                sql.append("and m.month = :month ");
                params.put("month", Integer.valueOf(month));
            }
        }
        
        FcCompany reCompany = condVO.getReCompany();
        FcCompany paCompany = condVO.getPaCompany();
        if(null != reCompany && null != paCompany){
            sql.append("and ( ");
            
            sql.append("( ");
            sql.append("m.reCompany = :reCompany1 and m.paCompany = :paCompany1 ");
            sql.append(") ");
            sql.append("or ");
            sql.append("( ");
            sql.append("m.reCompany = :reCompany2 and m.paCompany = :paCompany2 ");
            sql.append(") ");
            
            sql.append(") ");
            params.put("reCompany1", reCompany);
            params.put("paCompany1", paCompany);
            params.put("reCompany2", paCompany);
            params.put("paCompany2", reCompany);
            
            sql.append("order by m.sheetType ASC, m.reCompany ASC, m.reAccount.reclOrder ASC ");
        }else if(null != reCompany){//paCompany = null
            sql.append("and ( ");
            
            sql.append("( ");
            sql.append("m.reCompany = :reCompany ");
//            and m.paCompany = :paCompany1
            sql.append(") ");
            sql.append("or ");
            sql.append("( ");
            sql.append(" m.paCompany = :paCompany ");
//            m.reCompany = :reCompany2 and
            sql.append(") ");
            
            sql.append(") ");
            params.put("reCompany", reCompany);
            params.put("paCompany", reCompany);
            
            sql.append("order by m.sheetType ASC, m.reCompany ASC, m.reAccount.reclOrder ASC, m.paCompany ASC ");
        }
         //20160106 增加考量科目順序 應調整排序
//        sql.append("order by m.sheetType ASC, m.reCompany ASC, m.reAccount.reclOrder ASC ");
        
        Query q = em.createQuery(sql.toString());
        if( params!=null && !params.isEmpty() ){
            for(String key : params.keySet()){
                q.setParameter(key, params.get(key));
            }
        }
        
        return q.getResultList();
    }
    
    public List<String> findDataYMList(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("distinct YEAR, MONTH ");
        sql.append("from IRS_SHEETDATA_M ");
        sql.append("WHERE 1=1 ");
        sql.append("order by YEAR desc, MONTH desc ");
        
        Query query = em.createNativeQuery(sql.toString());
        List list = query.getResultList();
        
        List<String> resultList = new ArrayList<>();
        Date dt;
        Calendar calendar;
        for (Object row : list) {
            Object[] columns = (Object[]) row;
            int yy = (short) columns[0];
            int mm = (short) columns[1];
            
            dt = DateUtils.getDate(yy, mm, 1);
            calendar = Calendar.getInstance();
            calendar.setTime(dt);
            String ym = DateUtils.getYearMonth(calendar);
            resultList.add(ym);
        }
        
        return resultList;
    }
    
    /**
     * 指定公司 月份 對帳表是否完成調整
     * @param company
     * @param ym
     * @return 
     */
    public boolean companyIsAllEditByYM(FcCompany company, String ym){
        boolean result = true;
        Query q = em.createNamedQuery("IrsSheetdataM.findByCompanyAndYM");
        q.setParameter("company", company);
        if(StringUtils.isNotBlank(ym)){
            String[] string_array = DateUtils.getYearAndMonth(ym);
            String year = string_array[0];
            String month = string_array[1];
            q.setParameter("year", Integer.valueOf(year));
            q.setParameter("month", Integer.valueOf(month));
        }
        List<IrsSheetdataM> list = q.getResultList();
        if(list.isEmpty()){
            return false;
        }
        //只要有一筆未調節且不平 即表示 AllEdit = false
        BigDecimal zero = new BigDecimal("0.00");
        for(IrsSheetdataM m : list){
            if (null == m.getCreatetimestamp()) {//未調節
                BigDecimal diff = m.getDiff();
                if (!zero.equals(diff)) {//且不平
                    return false;
                }
            }
        }
        
        return result;
    }
    
    
}
