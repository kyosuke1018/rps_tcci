/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.facade.sheetdata;

import com.tcci.fc.facade.util.NativeSQLUtils;
import com.tcci.fc.util.time.DateUtils;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.enums.AccountTypeEnum;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import com.tcci.irs.entity.sheetdata.IrsSheetdataM;
import com.tcci.irs.entity.sheetdata.ZtfiAfrcTran;
import com.tcci.irs.enums.SheetTypeEnum;
import com.tcci.irs.facade.AbstractFacade;
import com.tcci.rpt.entity.RptCompanyOrg;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
public class ZtfiAfrcTranFacade extends AbstractFacade<ZtfiAfrcTran> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZtfiAfrcTranFacade() {
        super(ZtfiAfrcTran.class);
    }
    
    public void save(ZtfiAfrcTran etity) {
        if (etity.getId()==null) {
            create(etity);
//            em.persist(etity);
        } else {
            edit(etity);
//            em.merge(etity);
        }
    }
    
    /*不好的寫法
    public List<ZtfiAfrcTran> find(String companyCode, String yearMonth) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT                                ");
        sql.append("AFRC_TRAN.ID,                           ");
        sql.append("1 X ");
        sql.append("FROM ZTFI_AFRC_TRAN AFRC_TRAN ");
        sql.append("WHERE 1=1                             ");
        if(StringUtils.isNotBlank(yearMonth)){
            String[] string_array = DateUtils.getYearAndMonth(yearMonth);
            String year = string_array[0];
            String month = string_array[1];
            if(StringUtils.isNotBlank(year)){
                String pColumnName = "AFRC_TRAN.ZGJAHR";
                sql.append(NativeSQLUtils.genEqualSQL(pColumnName, year, params));
            }
            if(StringUtils.isNotBlank(month)){
                String pColumnName = "AFRC_TRAN.ZMONAT";
                sql.append(NativeSQLUtils.genEqualSQL(pColumnName, month, params));
            }
        }
        if(StringUtils.isNotBlank(companyCode)){
            String pColumnName = "AFRC_TRAN.BUKRS";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, companyCode, params));
        }
        
        
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List<Object[]> list = query.getResultList();
        
        List<ZtfiAfrcTran> resultList = new ArrayList<>();
        for (Object[] row : list) {
            long id =((long)row[0]);
            ZtfiAfrcTran entity = find(id);
            
            resultList.add(entity);
        }
        return resultList;
//        List<ZtcoRelaGui> resultList = new ArrayList<>();
//        
//        return resultList;
    }
    **/
    
    public List<ZtfiAfrcTran> find(String companyCode, String yearMonth) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select t from ZtfiAfrcTran t ");
        sql.append("WHERE 1=1 ");
        if(StringUtils.isNotBlank(yearMonth)){
            String[] string_array = DateUtils.getYearAndMonth(yearMonth);
            String year = string_array[0];
            String month = string_array[1];
            if(StringUtils.isNotBlank(year)){
                sql.append("and t.zgjahr = :year ");
                params.put("year", Integer.valueOf(year));
            }
            if(StringUtils.isNotBlank(month)){
                sql.append("and t.zmonat = :month ");
                params.put("month", Integer.valueOf(month));
            }
        }
        if(StringUtils.isNotBlank(companyCode)){
            sql.append("and t.bukrs = :compCode ");
            params.put("compCode", companyCode);
        }
        Query q = em.createQuery(sql.toString());
        if( params!=null && !params.isEmpty() ){
            for(String key : params.keySet()){
                q.setParameter(key, params.get(key));
            }
        }
        
        return q.getResultList();
    }
    
    public void remove(String companyCode, String yearMonth) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
//        String tableName_alias = "";
        sql.append("DELETE ZTFI_AFRC_TRAN ");
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
    
    //detailAccType:表示查詢金額明細是個體還是對帳方
    //上層科目對應一組code list
    public List<ZtfiAfrcTran> findDetail(IrsSheetdataM m, AccountTypeEnum detailAccType) {
        SheetTypeEnum sheetType = SheetTypeEnum.getByValue(m.getSheetType());
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t FROM ZtfiAfrcTran t ");
        sql.append("where 1=1 ");
        sql.append("and t.zgjahr =:year ");
        if(SheetTypeEnum.BS.equals(sheetType)){//資產負債 查當期
            sql.append("and t.zmonat = :month ");
        }else{//損益類 查年度累計
            sql.append("and t.zmonat <= :month ");
        }
        sql.append("and t.bukrs =:compCode1 ");
        sql.append("and t.zafbuk =:compCode2 ");
        sql.append("and t.zafcat =:dcType ");
        sql.append("and t.zaftyp =:accType ");
        sql.append("and t.waers =:currCode ");
//        sql.append("order by syncTimeStamp desc");
        
        Query q =em.createQuery(sql.toString());
        q.setParameter("year", m.getYear());
        q.setParameter("month", m.getMonth());
        if(m.getReCompany().equals(m.getIndividualCompany())){//主體公司
            if((AccountTypeEnum.RE.equals(detailAccType))){//個體方金額明細
                q.setParameter("compCode1", m.getReCompany().getCode());
                q.setParameter("compCode2", m.getPaCompany().getCode());
                
                q.setParameter("accType", m.getReAccountCode());
            }else{//對帳方金額明細
                q.setParameter("compCode1", m.getPaCompany().getCode());
                q.setParameter("compCode2", m.getReCompany().getCode());
                
                q.setParameter("accType", m.getPaAccountCode());
            }
            q.setParameter("dcType", detailAccType.getCode());
        }else{
            if((AccountTypeEnum.RE.equals(detailAccType))){//個體方金額明細
                q.setParameter("compCode1", m.getPaCompany().getCode());
                q.setParameter("compCode2", m.getReCompany().getCode());
                q.setParameter("dcType", AccountTypeEnum.PA.getCode());
                q.setParameter("accType", m.getPaAccountCode());
            }else{//對帳方金額明細
                q.setParameter("compCode1", m.getReCompany().getCode());
                q.setParameter("compCode2", m.getPaCompany().getCode());
                q.setParameter("dcType", AccountTypeEnum.RE.getCode());
                q.setParameter("accType", m.getReAccountCode());
            }
        }
        q.setParameter("currCode", m.getCurrency().getCode());
        return q.getResultList();
    }
    
    public List<ZtfiAfrcTran> findDetailByAccCode(IrsSheetdataM m, AccountTypeEnum detailAccType, String accCode) {
        SheetTypeEnum sheetType = SheetTypeEnum.getByValue(m.getSheetType());
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t FROM ZtfiAfrcTran t ");
        sql.append("where 1=1 ");
        sql.append("and t.zgjahr =:year ");
        if(SheetTypeEnum.BS.equals(sheetType)){//資產負債 查當期
            sql.append("and t.zmonat = :month ");
        }else{//損益類 查年度累計
            sql.append("and t.zmonat <= :month ");
        }
        sql.append("and t.bukrs =:compCode1 ");
        sql.append("and t.zafbuk =:compCode2 ");
        sql.append("and t.zafcat =:dcType ");
        sql.append("and t.zaftyp =:accType ");
        sql.append("and t.waers =:currCode ");
//        sql.append("order by syncTimeStamp desc");
        
        Query q =em.createQuery(sql.toString());
        q.setParameter("year", m.getYear());
        q.setParameter("month", m.getMonth());
        if(m.getReCompany().equals(m.getIndividualCompany())){//主體公司
            if((AccountTypeEnum.RE.equals(detailAccType))){//個體方金額明細
                q.setParameter("compCode1", m.getReCompany().getCode());
                q.setParameter("compCode2", m.getPaCompany().getCode());
//                q.setParameter("accType", accCode);
            }else{//對帳方金額明細
                q.setParameter("compCode1", m.getPaCompany().getCode());
                q.setParameter("compCode2", m.getReCompany().getCode());
//                q.setParameter("accType", m.getPaAccountCode());
            }
            q.setParameter("dcType", detailAccType.getCode());
            q.setParameter("accType", accCode);
        }else{
            if((AccountTypeEnum.RE.equals(detailAccType))){//個體方金額明細
                q.setParameter("compCode1", m.getPaCompany().getCode());
                q.setParameter("compCode2", m.getReCompany().getCode());
                q.setParameter("dcType", AccountTypeEnum.PA.getCode());
//                q.setParameter("accType", m.getPaAccountCode());
            }else{//對帳方金額明細
                q.setParameter("compCode1", m.getReCompany().getCode());
                q.setParameter("compCode2", m.getPaCompany().getCode());
                q.setParameter("dcType", AccountTypeEnum.RE.getCode());
//                q.setParameter("accType", m.getReAccountCode());
            }
            q.setParameter("accType", accCode);
        }
        q.setParameter("currCode", m.getCurrency().getCode());
        return q.getResultList();
    }
    
    //IRS_REPORT
    /**
    public List<ReportBaseVO> findIrsReportData(CompanyGroupEnum group, String yearMonth) {
        String year;
        String month;
        if(StringUtils.isNotBlank(yearMonth)){
            String[] string_array = DateUtils.getYearAndMonth(yearMonth);
            year = string_array[0];
            month = string_array[1];
        }else{
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("t.ZAFBUK, ");
        sql.append("t.ACCOUNT_CODE, ");
        sql.append("sum(t.DMBTR) as amount ");
        sql.append("from ZTFI_AFRC_TRAN t ");
        sql.append("where 1=1 ");
        if(CompanyGroupEnum.TCC.equals(group)){
            sql.append("  and t.bukrs = '1000' ");
        }else{
            sql.append("  and t.bukrs = '8000' ");
        }
        if(StringUtils.isNotBlank(year)){
            String pColumnName = "t.ZGJAHR";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, year, params));
        }
        if(StringUtils.isNotBlank(month)){
            String pColumnName = "t.ZMONAT";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, month, params));
        }
        
        sql.append("group by ");
        sql.append("t.ZAFBUK,t.ACCOUNT_CODE ");
        sql.append("order by t.ZAFBUK ");
//        logger.debug("sql: "+sql);
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
//            logger.debug("list.size()= 0 ");
            return list;
        }
        
        List<ReportBaseVO> resultList = new ArrayList<>();
        for (Object row : list) {
            ReportBaseVO vo = new ReportBaseVO();
            Object[] columns = (Object[]) row;
            vo.setCompCode((String) columns[0]);
            vo.setAccCode((String) columns[1]);
            vo.setAmount((BigDecimal) columns[2]);
	    resultList.add(vo);
        }
        return resultList;
    }*/
    
    /**
     *
     * @param company
     * @param yearMonth
     * @param sheetType:BS IS
     * @param host company is host or guest
     * @return
     */
    public List<ReportBaseVO> findIrsReportData(FcCompany company, String yearMonth, String sheetType, boolean host) {
        String year;
        String month;
        if(company!=null && StringUtils.isNotBlank(yearMonth) && StringUtils.isNotBlank(sheetType)){
            String[] string_array = DateUtils.getYearAndMonth(yearMonth);
            year = string_array[0];
            month = string_array[1];
        }else{
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("t.bukrs as c1Code, ");
        sql.append("c1.ABBREVIATION as c1Name, ");
        sql.append("t.ZAFBUK as c2code, ");
        sql.append("c2.ABBREVIATION as c2Name, ");
        sql.append("t.ZAFCAT as repaType, ");
        sql.append("t.ZAFTYP as accCode, ");
        sql.append("curr.code as currCode, ");
        sql.append("sum(t.DMBTR) as amount ");
        sql.append("from ZTFI_AFRC_TRAN t ");
        sql.append("join FC_COMPANY c1 on c1.code = t.bukrs ");
        sql.append("join FC_COMPANY c2 on c2.code = t.ZAFBUK ");
        sql.append("join FC_CURRENCY curr on curr.id = c1.CURRENCY ");
        sql.append("join IRS_ACCOUNT_NODE node on node.code = t.ZAFTYP and node.RECL_ROLE = t.ZAFCAT ");
        String pColumnName1 = "node.COMP_GROUP";
        sql.append(NativeSQLUtils.genEqualSQL(pColumnName1, company.getGroup().getCode(), params));
        String pColumnName2 = "node.CATEGORY";
        sql.append(NativeSQLUtils.genEqualSQL(pColumnName2, sheetType, params));
        
        sql.append("where 1=1 ");
        if(StringUtils.isNotBlank(year)){
            String pColumnName = "t.ZGJAHR";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, year, params));
        }
        if(StringUtils.isNotBlank(month)){
            if(SheetTypeEnum.BS.getValue().equals(sheetType)){
                String pColumnName = "t.ZMONAT";
                sql.append(NativeSQLUtils.genEqualSQL(pColumnName, month, params));
            }else{//IS 累計同年 之前各月
                String pColumnName = "t.ZMONAT";
                sql.append(NativeSQLUtils.getBetweenSQL(pColumnName, null, month));
            }
        }
        if(host){
            String pColumnName = "t.BUKRS";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, company.getCode(), params));
        }else{
            String pColumnName = "t.ZAFBUK";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, company.getCode(), params));
        }
        sql.append("group by t.bukrs,c1.ABBREVIATION,t.ZAFBUK,c2.ABBREVIATION,t.ZAFCAT,t.ZAFTYP,curr.code ");
        if(host){
            sql.append("order by t.ZAFCAT desc,t.ZAFBUK ");
        }else{
            sql.append("order by t.ZAFCAT asc,t.bukrs ");
        }
//        logger.debug("sql: "+sql);
//        System.out.println("sql: "+sql);
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
//            logger.debug("list.size()= 0 ");
            return list;
        }
        
        List<ReportBaseVO> resultList = new ArrayList<>();
        for (Object row : list) {
            ReportBaseVO vo = new ReportBaseVO();
            Object[] columns = (Object[]) row;
            vo.setCompCode((String) columns[0]);
            vo.setCompName((String) columns[1]);
            vo.setComp2Code((String) columns[2]);
            vo.setComp2Name((String) columns[3]);
            vo.setSheetCode((String) columns[4]);//RE PA 借用欄位
            vo.setAccCode((String) columns[5]);
            vo.setCurrCode((String) columns[6]);
            vo.setAmount((BigDecimal) columns[7]);
	    resultList.add(vo);
        }
        return resultList;
    }
    
    //B 關係人對帳調節
    //20160505 boolean consolidation 是否為合併聯署公司(依集團分)
    //20160525 irs reportB透過RPT_COMPANY_ORG 向上合併到第一層
    public List<ReportBaseVO> findIrsReportBData(CompanyGroupEnum group, String yearMonth, String sheetType, boolean consolidation) {
        return this.findIrsReportBData(group, yearMonth, sheetType, consolidation, 1);
    }
    public List<ReportBaseVO> findIrsReportBData(CompanyGroupEnum group, String yearMonth, String sheetType, boolean consolidation, int hlevel) {
        String year;
        String month;
        if(StringUtils.isNotBlank(yearMonth)){
            String[] string_array = DateUtils.getYearAndMonth(yearMonth);
            year = string_array[0];
            month = string_array[1];
        }else{
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        if(consolidation){
            sql.append("c1.h1code as c1Code, ");
            sql.append("c1.name as c1Name, ");
            sql.append("c2.h1code as c2code, ");
            sql.append("c2.name as c2Name, ");
        }else{
            sql.append("t.bukrs as c1Code, ");
            sql.append("c1.ABBREVIATION as c1Name, ");
            sql.append("t.ZAFBUK as c2code, ");
            sql.append("c2.ABBREVIATION as c2Name, ");
        }
        sql.append("acc.REPA_TYPE as repaType, ");//t.ZAFCAT
        sql.append("acc.code as accCode, ");//t.ACCOUNT_CODE
        sql.append("curr.code as currCode, ");
        sql.append("sum(t.DMBTR) as amount ");
        sql.append("from ZTFI_AFRC_TRAN t ");
        if(consolidation){
            //向上找到HLEVEL階層1的公司代碼
            //c1要多幣別欄位
            sql.append("join (select ");
            sql.append("c.code, ");
            //hlevel 改為參數
            sql.append("case when o1.hlevel = #hlevel then o1.code ");
            sql.append("when o2.hlevel = #hlevel then o2.code ");
            sql.append("when o3.hlevel = #hlevel then o3.code ");
            sql.append("when o4.hlevel = #hlevel then o4.code ");
            sql.append("when o5.hlevel = #hlevel then o5.code ");
            sql.append("when o6.hlevel = #hlevel then o6.code ");
            sql.append("end as h1code, ");
            sql.append("case when o1.hlevel = #hlevel then c.ABBREVIATION ");
            sql.append("when o2.hlevel = #hlevel then c2.ABBREVIATION ");
            sql.append("when o3.hlevel = #hlevel then c3.ABBREVIATION ");
            sql.append("when o4.hlevel = #hlevel then c4.ABBREVIATION ");
            sql.append("when o5.hlevel = #hlevel then c5.ABBREVIATION ");
            sql.append("when o6.hlevel = #hlevel then c6.ABBREVIATION ");
            sql.append("else '' ");
            sql.append("end as name, ");
            params.put("hlevel", hlevel);
            sql.append("c.COMP_GROUP, ");
            sql.append("c.CURRENCY ");
            sql.append("from FC_COMPANY c ");
            sql.append("join RPT_COMPANY_ORG o1 on c.code = o1.code and c.COMP_GROUP = o1.COMP_GROUP and o1.hlevel != 0 ");
            sql.append("left join RPT_COMPANY_ORG o2 on o1.PARENT_ID = o2.ID and o2.hlevel != 0 ");
            sql.append("left join FC_COMPANY c2 on o2.code = c2.code ");
            sql.append("left join RPT_COMPANY_ORG o3 on o2.PARENT_ID = o3.ID and o3.hlevel != 0 and o2.hlevel is not null ");
            sql.append("left join FC_COMPANY c3 on o3.code = c3.code ");
            sql.append("left join RPT_COMPANY_ORG o4 on o3.PARENT_ID = o4.ID and o4.hlevel != 0 and o3.hlevel is not null ");
            sql.append("left join FC_COMPANY c4 on o4.code = c4.code ");
            sql.append("left join RPT_COMPANY_ORG o5 on o4.PARENT_ID = o5.ID and o5.hlevel != 0 and o4.hlevel is not null ");
            sql.append("left join FC_COMPANY c5 on o5.code = c5.code ");
            sql.append("left join RPT_COMPANY_ORG o6 on o5.PARENT_ID = o6.ID and o6.hlevel != 0 and o5.hlevel is not null ");//level 5(ex:5800,5900)
            sql.append("left join FC_COMPANY c6 on o6.code = c6.code ");
            sql.append("where 1=1 ");
            String pColumnName1 = "c.COMP_GROUP";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName1, group.getCode(), params));
            String pColumnName2 = "o1.COMP_GROUP";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName2, group.getCode(), params));
            sql.append(") c1 on c1.code = t.bukrs ");
            
            //向上找到HLEVEL階層1的公司代碼
            sql.append("join (select ");
            sql.append("c.code, ");
            //hlevel 改為參數
            sql.append("case when o1.hlevel = #hlevel then o1.code ");
            sql.append("when o2.hlevel = #hlevel then o2.code ");
            sql.append("when o3.hlevel = #hlevel then o3.code ");
            sql.append("when o4.hlevel = #hlevel then o4.code ");
            sql.append("when o5.hlevel = #hlevel then o5.code ");
            sql.append("when o6.hlevel = #hlevel then o6.code ");
            sql.append("end as h1code, ");
            sql.append("case when o1.hlevel = #hlevel then c.ABBREVIATION ");
            sql.append("when o2.hlevel = #hlevel then c2.ABBREVIATION ");
            sql.append("when o3.hlevel = #hlevel then c3.ABBREVIATION ");
            sql.append("when o4.hlevel = #hlevel then c4.ABBREVIATION ");
            sql.append("when o5.hlevel = #hlevel then c5.ABBREVIATION ");
            sql.append("when o6.hlevel = #hlevel then c6.ABBREVIATION ");
            sql.append("else '' ");
            sql.append("end as name, ");
            params.put("hlevel", hlevel);
            sql.append("c.COMP_GROUP ");
            sql.append("from FC_COMPANY c ");
            sql.append("join RPT_COMPANY_ORG o1 on c.code = o1.code and c.COMP_GROUP = o1.COMP_GROUP and o1.hlevel != 0 ");
            sql.append("left join RPT_COMPANY_ORG o2 on o1.PARENT_ID = o2.ID and o2.hlevel != 0 ");
            sql.append("left join FC_COMPANY c2 on o2.code = c2.code ");
            sql.append("left join RPT_COMPANY_ORG o3 on o2.PARENT_ID = o3.ID and o3.hlevel != 0 and o2.hlevel is not null ");
            sql.append("left join FC_COMPANY c3 on o3.code = c3.code ");
            sql.append("left join RPT_COMPANY_ORG o4 on o3.PARENT_ID = o4.ID and o4.hlevel != 0 and o3.hlevel is not null ");
            sql.append("left join FC_COMPANY c4 on o4.code = c4.code ");
            sql.append("left join RPT_COMPANY_ORG o5 on o4.PARENT_ID = o5.ID and o5.hlevel != 0 and o4.hlevel is not null ");
            sql.append("left join FC_COMPANY c5 on o5.code = c5.code ");
            sql.append("left join RPT_COMPANY_ORG o6 on o5.PARENT_ID = o6.ID and o6.hlevel != 0 and o5.hlevel is not null ");//level 5(ex:5800,5900)
            sql.append("left join FC_COMPANY c6 on o6.code = c6.code ");
            sql.append("where 1=1 ");
            String pColumnName3 = "c.COMP_GROUP";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName3, group.getCode(), params));
            String pColumnName4 = "o1.COMP_GROUP";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName4, group.getCode(), params));
            sql.append(") c2 on c2.code = t.ZAFBUK ");
            
            //舊的做法
//            sql.append("join FC_COMPANY c1 on c1.code = t.bukrs ");
//            sql.append("and c1.COMBINE_INCOME = 'true' ");
//            String pColumnName1 = "c1.COMP_GROUP";
//            sql.append(NativeSQLUtils.genEqualSQL(pColumnName1, group.getCode(), params));
//            sql.append("join FC_COMPANY c2 on c2.code = t.ZAFBUK ");
//            sql.append("and c2.COMBINE_INCOME = 'true' ");
//            String pColumnName2 = "c2.COMP_GROUP";
//            sql.append(NativeSQLUtils.genEqualSQL(pColumnName2, group.getCode(), params));
        }else{//非合併聯署
            sql.append("join FC_NON_CONSOLIDATION_MAP fncm on ( ");
            sql.append("(fncm.HOME_COMP_CODE = t.bukrs and fncm.GUEST_COMP_CODE = t.ZAFBUK) or ");
            sql.append("(fncm.GUEST_COMP_CODE = t.bukrs and fncm.HOME_COMP_CODE = t.ZAFBUK) ) ");
            String pColumnName1 = "fncm.COMP_GROUP";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName1, group.getCode(), params));
            sql.append("join FC_COMPANY c1 on c1.code = t.bukrs ");
            sql.append("join FC_COMPANY c2 on c2.code = t.ZAFBUK ");
        }
        sql.append("join FC_CURRENCY curr on curr.id = c1.CURRENCY ");
        sql.append("join FC_ACCOUNTS_DETAIL_MAP dm on dm.DETAIL_CODE = t.ACCOUNT_CODE ");
        sql.append("join IRS_ACCOUNTS acc on acc.code = dm.NOTE_LINE_CODE  ");
        sql.append(" and acc.CATEGORY = 'B'");//irs Report B
        //20160418 TCC CSRC 先使用同一組報表會科
//        String pColumnName3 = "acc.COMP_GROUP";
//        sql.append(NativeSQLUtils.genEqualSQL(pColumnName3, group.getCode(), params));
        String pColumnName4 = "acc.BSIS_TYPE";
        sql.append(NativeSQLUtils.genEqualSQL(pColumnName4, sheetType, params));
        sql.append("where 1=1 ");
        if(StringUtils.isNotBlank(year)){
            String pColumnName = "t.ZGJAHR";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, year, params));
        }
        if(StringUtils.isNotBlank(month)){
            if(SheetTypeEnum.BS.getValue().equals(sheetType)){
                String pColumnName = "t.ZMONAT";
                sql.append(NativeSQLUtils.genEqualSQL(pColumnName, month, params));
            }else{//IS 累計同年 之前各月
                String pColumnName = "t.ZMONAT";
                sql.append(NativeSQLUtils.getBetweenSQL(pColumnName, null, month));
            }
        }
        sql.append("group by ");
        if(consolidation){
            sql.append("c1.h1code,c1.name,c2.h1code,c2.name,acc.REPA_TYPE,acc.code,curr.code ");
        }else{
            sql.append("t.bukrs,c1.ABBREVIATION,t.ZAFBUK,c2.ABBREVIATION,acc.REPA_TYPE,acc.code,curr.code ");
        }
//        sql.append("order by t.ZAFBUK ");
//        System.out.println("sql: "+sql);
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
//            logger.debug("list.size()= 0 ");
            return list;
        }
        
        List<ReportBaseVO> resultList = new ArrayList<>();
        for (Object row : list) {
            ReportBaseVO vo = new ReportBaseVO();
            Object[] columns = (Object[]) row;
            vo.setCompCode((String) columns[0]);
            vo.setCompName((String) columns[1]);
            vo.setComp2Code((String) columns[2]);
            vo.setComp2Name((String) columns[3]);
            vo.setSheetCode((String) columns[4]);//RE PA 借用欄位
            vo.setAccCode((String) columns[5]);
            vo.setCurrCode((String) columns[6]);
            vo.setAmount((BigDecimal) columns[7]);
	    resultList.add(vo);
        }
        return resultList;
    }
    
    //20160601 ReportB detail 第一層以下的對帳明細
    //consolidation 合併:內部; 非合併:外部
    public List<ReportBaseVO> findIrsReportBDetail(CompanyGroupEnum group, String yearMonth, String sheetType, boolean consolidation, RptCompanyOrg org) {
        String year;
        String month;
        if(StringUtils.isNotBlank(yearMonth)){
            String[] string_array = DateUtils.getYearAndMonth(yearMonth);
            year = string_array[0];
            month = string_array[1];
        }else{
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("t.bukrs as c1Code, ");
        sql.append("c1.ABBREVIATION as c1Name, ");
        sql.append("t.ZAFBUK as c2code, ");
        sql.append("c2.ABBREVIATION as c2Name, ");
        sql.append("acc.REPA_TYPE as repaType, ");//t.ZAFCAT
        sql.append("acc.code as accCode, ");//t.ACCOUNT_CODE
        sql.append("curr.code as currCode, ");
        sql.append("sum(t.DMBTR) as amount ");
        sql.append("from ZTFI_AFRC_TRAN t ");
        //向上找到HLEVEL階層1的公司代碼
        //c1要多幣別欄位
        sql.append("join (select ");
        sql.append("c.code, ");
        //hlevel 改為參數
        sql.append("case when o1.hlevel = #hlevel then o1.code ");
        sql.append("when o2.hlevel = #hlevel then o2.code ");
        sql.append("when o3.hlevel = #hlevel then o3.code ");
        sql.append("when o4.hlevel = #hlevel then o4.code ");
        sql.append("when o5.hlevel = #hlevel then o5.code ");
        sql.append("when o6.hlevel = #hlevel then o6.code ");
        sql.append("end as h1code, ");
        params.put("hlevel", org.getHlevel());
        sql.append("c.ABBREVIATION, ");
        sql.append("c.COMP_GROUP, ");
        sql.append("c.CURRENCY ");
        sql.append("from FC_COMPANY c ");
        sql.append("join RPT_COMPANY_ORG o1 on c.code = o1.code and c.COMP_GROUP = o1.COMP_GROUP and o1.hlevel != 0 ");
        sql.append("left join RPT_COMPANY_ORG o2 on o1.PARENT_ID = o2.ID and o2.hlevel != 0 ");
        sql.append("left join FC_COMPANY c2 on o2.code = c2.code ");
        sql.append("left join RPT_COMPANY_ORG o3 on o2.PARENT_ID = o3.ID and o3.hlevel != 0 and o2.hlevel is not null ");
        sql.append("left join FC_COMPANY c3 on o3.code = c3.code ");
        sql.append("left join RPT_COMPANY_ORG o4 on o3.PARENT_ID = o4.ID and o4.hlevel != 0 and o3.hlevel is not null ");
        sql.append("left join FC_COMPANY c4 on o4.code = c4.code ");
        sql.append("left join RPT_COMPANY_ORG o5 on o4.PARENT_ID = o5.ID and o5.hlevel != 0 and o4.hlevel is not null ");
        sql.append("left join FC_COMPANY c5 on o5.code = c5.code ");
        sql.append("left join RPT_COMPANY_ORG o6 on o5.PARENT_ID = o6.ID and o6.hlevel != 0 and o5.hlevel is not null ");//level 5(ex:5800,5900)
        sql.append("left join FC_COMPANY c6 on o6.code = c6.code ");
        sql.append("where 1=1 ");
        String pColumnName1 = "c.COMP_GROUP";
        sql.append(NativeSQLUtils.genEqualSQL(pColumnName1, group.getCode(), params));
        String pColumnName2 = "o1.COMP_GROUP";
        sql.append(NativeSQLUtils.genEqualSQL(pColumnName2, group.getCode(), params));
        sql.append(") c1 on c1.code = t.bukrs ");
        
        //向上找到HLEVEL階層1的公司代碼
        sql.append("join (select ");
        sql.append("c.code, ");
        //hlevel 改為參數
        sql.append("case when o1.hlevel = #hlevel then o1.code ");
        sql.append("when o2.hlevel = #hlevel then o2.code ");
        sql.append("when o3.hlevel = #hlevel then o3.code ");
        sql.append("when o4.hlevel = #hlevel then o4.code ");
        sql.append("when o5.hlevel = #hlevel then o5.code ");
        sql.append("when o6.hlevel = #hlevel then o6.code ");
        sql.append("end as h1code, ");
        params.put("hlevel", org.getHlevel());
        sql.append("c.ABBREVIATION, ");
        sql.append("c.COMP_GROUP ");
        sql.append("from FC_COMPANY c ");
        sql.append("join RPT_COMPANY_ORG o1 on c.code = o1.code and c.COMP_GROUP = o1.COMP_GROUP and o1.hlevel != 0 ");
        sql.append("left join RPT_COMPANY_ORG o2 on o1.PARENT_ID = o2.ID and o2.hlevel != 0 ");
        sql.append("left join FC_COMPANY c2 on o2.code = c2.code ");
        sql.append("left join RPT_COMPANY_ORG o3 on o2.PARENT_ID = o3.ID and o3.hlevel != 0 and o2.hlevel is not null ");
        sql.append("left join FC_COMPANY c3 on o3.code = c3.code ");
        sql.append("left join RPT_COMPANY_ORG o4 on o3.PARENT_ID = o4.ID and o4.hlevel != 0 and o3.hlevel is not null ");
        sql.append("left join FC_COMPANY c4 on o4.code = c4.code ");
        sql.append("left join RPT_COMPANY_ORG o5 on o4.PARENT_ID = o5.ID and o5.hlevel != 0 and o4.hlevel is not null ");
        sql.append("left join FC_COMPANY c5 on o5.code = c5.code ");
        sql.append("left join RPT_COMPANY_ORG o6 on o5.PARENT_ID = o6.ID and o6.hlevel != 0 and o5.hlevel is not null ");//level 5(ex:5800,5900)
        sql.append("left join FC_COMPANY c6 on o6.code = c6.code ");
        sql.append("where 1=1 ");
        String pColumnName3 = "c.COMP_GROUP";
        sql.append(NativeSQLUtils.genEqualSQL(pColumnName3, group.getCode(), params));
        String pColumnName4 = "o1.COMP_GROUP";
        sql.append(NativeSQLUtils.genEqualSQL(pColumnName4, group.getCode(), params));
        sql.append(") c2 on c2.code = t.ZAFBUK ");
        
        sql.append("join FC_CURRENCY curr on curr.id = c1.CURRENCY ");
        sql.append("join FC_ACCOUNTS_DETAIL_MAP dm on dm.DETAIL_CODE = t.ACCOUNT_CODE ");
        sql.append("join IRS_ACCOUNTS acc on acc.code = dm.NOTE_LINE_CODE  ");
        sql.append(" and acc.CATEGORY = 'B'");//irs Report B
        //20160418 TCC CSRC 先使用同一組報表會科
//        String pColumnName3 = "acc.COMP_GROUP";
//        sql.append(NativeSQLUtils.genEqualSQL(pColumnName3, group.getCode(), params));
        String pColumnName5 = "acc.BSIS_TYPE";
        sql.append(NativeSQLUtils.genEqualSQL(pColumnName5, sheetType, params));
        sql.append("where 1=1 ");
        if(StringUtils.isNotBlank(year)){
            String pColumnName = "t.ZGJAHR";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, year, params));
        }
        if(StringUtils.isNotBlank(month)){
            if(SheetTypeEnum.BS.getValue().equals(sheetType)){
                String pColumnName = "t.ZMONAT";
                sql.append(NativeSQLUtils.genEqualSQL(pColumnName, month, params));
            }else{//IS 累計同年 之前各月
                String pColumnName = "t.ZMONAT";
                sql.append(NativeSQLUtils.getBetweenSQL(pColumnName, null, month));
            }
        }
        if(consolidation){
            sql.append("and c1.h1code = #h1code and c2.h1code = #h1code ");
            params.put("h1code", org.getCode());
        }else{
            sql.append("and ( ");
            sql.append("(c1.h1code = #h1code and c2.h1code != #h1code ) ");
            sql.append("or (c1.h1code != #h1code and c2.h1code = #h1code ) ");
            sql.append(") ");
            params.put("h1code", org.getCode());
        }
        sql.append("group by ");
        sql.append("t.bukrs,c1.ABBREVIATION,t.ZAFBUK,c2.ABBREVIATION,acc.REPA_TYPE,acc.code,curr.code ");
//        sql.append("order by t.ZAFBUK ");
//        System.out.println("sql: "+sql);
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
//            logger.debug("list.size()= 0 ");
            return list;
        }
        
        List<ReportBaseVO> resultList = new ArrayList<>();
        for (Object row : list) {
            ReportBaseVO vo = new ReportBaseVO();
            Object[] columns = (Object[]) row;
            vo.setCompCode((String) columns[0]);
            vo.setCompName((String) columns[1]);
            vo.setComp2Code((String) columns[2]);
            vo.setComp2Name((String) columns[3]);
            vo.setSheetCode((String) columns[4]);//RE PA 借用欄位
            vo.setAccCode((String) columns[5]);
            vo.setCurrCode((String) columns[6]);
            vo.setAmount((BigDecimal) columns[7]);
	    resultList.add(vo);
        }
        return resultList;
    }
    
    //合併聯屬公司對帳表 勘誤 對應會科不符
    //20160505 boolean consolidation 是否為合併聯署公司(依集團分)
    //20160525 合併聯署 公司代碼要存在於RPT_COMPANY_ORG
    public List<ZtfiAfrcTran> findIrsReportCheckList(CompanyGroupEnum group, String yearMonth, boolean consolidation) {
        return this.findIrsReportCheckList(group, yearMonth, consolidation, 1);
    }
    public List<ZtfiAfrcTran> findIrsReportCheckList(CompanyGroupEnum group, String yearMonth, boolean consolidation, int hlevel) {
        String year;
        String month;
        if(StringUtils.isNotBlank(yearMonth)){
            String[] string_array = DateUtils.getYearAndMonth(yearMonth);
            year = string_array[0];
            month = string_array[1];
        }else{
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("t.* ");
        sql.append("FROM Ztfi_Afrc_Tran t ");
        if(consolidation){
            //向上找到HLEVEL階層1的公司代碼
            //c1要多幣別欄位
            sql.append("join (select ");
            sql.append("c.code, ");
            //hlevel 改為參數
            sql.append("case when o1.hlevel = #hlevel then o1.code ");
            sql.append("when o2.hlevel = #hlevel then o2.code ");
            sql.append("when o3.hlevel = #hlevel then o3.code ");
            sql.append("when o4.hlevel = #hlevel then o4.code ");
            sql.append("when o5.hlevel = #hlevel then o5.code ");
            sql.append("when o6.hlevel = #hlevel then o6.code ");
            sql.append("end as h1code, ");
            params.put("hlevel", hlevel);
            sql.append("c.ABBREVIATION, ");
            sql.append("c.COMP_GROUP, ");
            sql.append("c.CURRENCY ");
            sql.append("from FC_COMPANY c ");
            sql.append("join RPT_COMPANY_ORG o1 on c.code = o1.code and c.COMP_GROUP = o1.COMP_GROUP and o1.hlevel != 0 ");
            sql.append("left join RPT_COMPANY_ORG o2 on o1.PARENT_ID = o2.ID and o2.hlevel != 0 ");
            sql.append("left join FC_COMPANY c2 on o2.code = c2.code ");
            sql.append("left join RPT_COMPANY_ORG o3 on o2.PARENT_ID = o3.ID and o3.hlevel != 0 and o2.hlevel is not null ");
            sql.append("left join FC_COMPANY c3 on o3.code = c3.code ");
            sql.append("left join RPT_COMPANY_ORG o4 on o3.PARENT_ID = o4.ID and o4.hlevel != 0 and o3.hlevel is not null ");
            sql.append("left join FC_COMPANY c4 on o4.code = c4.code ");
            sql.append("left join RPT_COMPANY_ORG o5 on o4.PARENT_ID = o5.ID and o5.hlevel != 0 and o4.hlevel is not null ");
            sql.append("left join FC_COMPANY c5 on o5.code = c5.code ");
            sql.append("left join RPT_COMPANY_ORG o6 on o5.PARENT_ID = o6.ID and o6.hlevel != 0 and o5.hlevel is not null ");//level 5(ex:5800,5900)
            sql.append("left join FC_COMPANY c6 on o6.code = c6.code ");
            sql.append("where 1=1 ");
            String pColumnName1 = "c.COMP_GROUP";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName1, group.getCode(), params));
            String pColumnName2 = "o1.COMP_GROUP";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName2, group.getCode(), params));
            sql.append(") c1 on c1.code = t.bukrs ");
            
            //向上找到HLEVEL階層1的公司代碼
            sql.append("join (select ");
            sql.append("c.code, ");
            //hlevel 改為參數
            sql.append("case when o1.hlevel = #hlevel then o1.code ");
            sql.append("when o2.hlevel = #hlevel then o2.code ");
            sql.append("when o3.hlevel = #hlevel then o3.code ");
            sql.append("when o4.hlevel = #hlevel then o4.code ");
            sql.append("when o5.hlevel = #hlevel then o5.code ");
            sql.append("when o6.hlevel = #hlevel then o6.code ");
            sql.append("end as h1code, ");
            params.put("hlevel", hlevel);
            sql.append("c.COMP_GROUP ");
            sql.append("from FC_COMPANY c ");
            sql.append("join RPT_COMPANY_ORG o1 on c.code = o1.code and c.COMP_GROUP = o1.COMP_GROUP and o1.hlevel != 0 ");
            sql.append("left join RPT_COMPANY_ORG o2 on o1.PARENT_ID = o2.ID and o2.hlevel != 0 ");
            sql.append("left join FC_COMPANY c2 on o2.code = c2.code ");
            sql.append("left join RPT_COMPANY_ORG o3 on o2.PARENT_ID = o3.ID and o3.hlevel != 0 and o2.hlevel is not null ");
            sql.append("left join FC_COMPANY c3 on o3.code = c3.code ");
            sql.append("left join RPT_COMPANY_ORG o4 on o3.PARENT_ID = o4.ID and o4.hlevel != 0 and o3.hlevel is not null ");
            sql.append("left join FC_COMPANY c4 on o4.code = c4.code ");
            sql.append("left join RPT_COMPANY_ORG o5 on o4.PARENT_ID = o5.ID and o5.hlevel != 0 and o4.hlevel is not null ");
            sql.append("left join FC_COMPANY c5 on o5.code = c5.code ");
            sql.append("left join RPT_COMPANY_ORG o6 on o5.PARENT_ID = o6.ID and o6.hlevel != 0 and o5.hlevel is not null ");//level 5(ex:5800,5900)
            sql.append("left join FC_COMPANY c6 on o6.code = c6.code ");
            sql.append("where 1=1 ");
            String pColumnName3 = "c.COMP_GROUP";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName3, group.getCode(), params));
            String pColumnName4 = "o1.COMP_GROUP";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName4, group.getCode(), params));
            sql.append(") c2 on c2.code = t.ZAFBUK ");
            //舊的做法 20160601
//            sql.append("join RPT_COMPANY_ORG o1 on o1.code = t.bukrs ");
//            String pColumnName1 = "o1.COMP_GROUP";
//            sql.append(NativeSQLUtils.genEqualSQL(pColumnName1, group.getCode(), params));
//            sql.append("join RPT_COMPANY_ORG o2 on o2.code = t.ZAFBUK ");
//            String pColumnName2 = "o2.COMP_GROUP";
//            sql.append(NativeSQLUtils.genEqualSQL(pColumnName2, group.getCode(), params));
        }else{//非合併聯署
            sql.append("join FC_NON_CONSOLIDATION_MAP fncm on ( ");
            sql.append("(fncm.HOME_COMP_CODE = t.bukrs and fncm.GUEST_COMP_CODE = t.ZAFBUK) or ");
            sql.append("(fncm.GUEST_COMP_CODE = t.bukrs and fncm.HOME_COMP_CODE = t.ZAFBUK) ) ");
            String pColumnName1 = "fncm.COMP_GROUP";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName1, group.getCode(), params));
            sql.append("join FC_COMPANY c1 on c1.code = t.bukrs ");
            sql.append("join FC_COMPANY c2 on c2.code = t.ZAFBUK ");
        }
        sql.append("left join FC_ACCOUNTS_DETAIL_MAP dm on dm.DETAIL_CODE = t.ACCOUNT_CODE ");
        sql.append("where 1=1 ");
        if(StringUtils.isNotBlank(year)){
            String pColumnName = "t.ZGJAHR";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, year, params));
        }
        if(StringUtils.isNotBlank(month)){
            String pColumnName = "t.ZMONAT";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, month, params));
        }
        sql.append("and dm.DETAIL_CODE is null ");//對應會科不符
        if(consolidation){
            sql.append("and c1.h1code != c2.h1code ");//排除掉內部交易
        }
        sql.append("order by t.bukrs, t.ZAFBUK ");
        
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
//            logger.debug("list.size()= 0 ");
            return list;
        }
        
        List<ZtfiAfrcTran> resultList = new ArrayList<>();
        for (Object row : list) {
//            ZtfiAfrcTran vo = new ZtfiAfrcTran();
            Object[] columns = (Object[]) row;
            ZtfiAfrcTran vo = new ZtfiAfrcTran(
                    (Long) columns[0],
                    (short) columns[1],
                    (short) columns[2],
                    (String) columns[3],
                    (String) columns[4],
                    (String) columns[5],
                    (String) columns[6],
                    (short) columns[7],
                    (String) columns[8],
                    (String) columns[9]
            );
            vo.setKunnr((String) columns[10]);
            vo.setLifnr((String) columns[11]);
            vo.setHkont((String) columns[12]);
            vo.setBudat((Date) columns[13]);
            vo.setBlart((String) columns[14]);
            vo.setWaers((String) columns[15]);
            vo.setWrbtr((BigDecimal) columns[16]);
            vo.setDmbtr((BigDecimal) columns[17]);
            vo.setZuonr((String) columns[18]);
            vo.setSgtxt((String) columns[19]);
            vo.setZaftlSaco((String) columns[20]);
            vo.setZafbukNm((String) columns[21]);
            vo.setTxt50((String) columns[22]);
            vo.setZckflg((String) columns[23]);
            vo.setZlupdt((Date) columns[24]);
            //25
            vo.setHkontIfrs((String) columns[26]);
            vo.setAccountCode((String) columns[27]);
	    resultList.add(vo);
        }
        return resultList;
    }
    //20160706 Level1 第一層以下公司上傳交易 會科勘誤表 (暫不分內外)
    public List<ZtfiAfrcTran> findIrsReportCheckList(CompanyGroupEnum group, String yearMonth, RptCompanyOrg org) {
        String year;
        String month;
        if(StringUtils.isNotBlank(yearMonth)){
            String[] string_array = DateUtils.getYearAndMonth(yearMonth);
            year = string_array[0];
            month = string_array[1];
        }else{
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("t.* ");
        sql.append("FROM Ztfi_Afrc_Tran t ");
        if(org.getHlevel() != null){
            //向上找到HLEVEL階層1的公司代碼
            //c1要多幣別欄位
            sql.append("join (select ");
            sql.append("c.code, ");
            //hlevel 改為參數
            sql.append("case when o1.hlevel = #hlevel then o1.code ");
            sql.append("when o2.hlevel = #hlevel then o2.code ");
            sql.append("when o3.hlevel = #hlevel then o3.code ");
            sql.append("when o4.hlevel = #hlevel then o4.code ");
            sql.append("when o5.hlevel = #hlevel then o5.code ");
            sql.append("when o6.hlevel = #hlevel then o6.code ");
            sql.append("end as h1code, ");
            params.put("hlevel", org.getHlevel());
            sql.append("c.ABBREVIATION, ");
            sql.append("c.COMP_GROUP, ");
            sql.append("c.CURRENCY ");
            sql.append("from FC_COMPANY c ");
            sql.append("join RPT_COMPANY_ORG o1 on c.code = o1.code and c.COMP_GROUP = o1.COMP_GROUP and o1.hlevel != 0 ");
            sql.append("left join RPT_COMPANY_ORG o2 on o1.PARENT_ID = o2.ID and o2.hlevel != 0 ");
            sql.append("left join FC_COMPANY c2 on o2.code = c2.code ");
            sql.append("left join RPT_COMPANY_ORG o3 on o2.PARENT_ID = o3.ID and o3.hlevel != 0 and o2.hlevel is not null ");
            sql.append("left join FC_COMPANY c3 on o3.code = c3.code ");
            sql.append("left join RPT_COMPANY_ORG o4 on o3.PARENT_ID = o4.ID and o4.hlevel != 0 and o3.hlevel is not null ");
            sql.append("left join FC_COMPANY c4 on o4.code = c4.code ");
            sql.append("left join RPT_COMPANY_ORG o5 on o4.PARENT_ID = o5.ID and o5.hlevel != 0 and o4.hlevel is not null ");
            sql.append("left join FC_COMPANY c5 on o5.code = c5.code ");
            sql.append("left join RPT_COMPANY_ORG o6 on o5.PARENT_ID = o6.ID and o6.hlevel != 0 and o5.hlevel is not null ");//level 5(ex:5800,5900)
            sql.append("left join FC_COMPANY c6 on o6.code = c6.code ");
            sql.append("where 1=1 ");
            String pColumnName1 = "c.COMP_GROUP";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName1, group.getCode(), params));
            String pColumnName2 = "o1.COMP_GROUP";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName2, group.getCode(), params));
            sql.append(") c1 on c1.code = t.bukrs ");
            
            //向上找到HLEVEL階層1的公司代碼
            sql.append("join (select ");
            sql.append("c.code, ");
            //hlevel 改為參數
            sql.append("case when o1.hlevel = #hlevel then o1.code ");
            sql.append("when o2.hlevel = #hlevel then o2.code ");
            sql.append("when o3.hlevel = #hlevel then o3.code ");
            sql.append("when o4.hlevel = #hlevel then o4.code ");
            sql.append("when o5.hlevel = #hlevel then o5.code ");
            sql.append("when o6.hlevel = #hlevel then o6.code ");
            sql.append("end as h1code, ");
            params.put("hlevel", org.getHlevel());
            sql.append("c.COMP_GROUP ");
            sql.append("from FC_COMPANY c ");
            sql.append("join RPT_COMPANY_ORG o1 on c.code = o1.code and c.COMP_GROUP = o1.COMP_GROUP and o1.hlevel != 0 ");
            sql.append("left join RPT_COMPANY_ORG o2 on o1.PARENT_ID = o2.ID and o2.hlevel != 0 ");
            sql.append("left join FC_COMPANY c2 on o2.code = c2.code ");
            sql.append("left join RPT_COMPANY_ORG o3 on o2.PARENT_ID = o3.ID and o3.hlevel != 0 and o2.hlevel is not null ");
            sql.append("left join FC_COMPANY c3 on o3.code = c3.code ");
            sql.append("left join RPT_COMPANY_ORG o4 on o3.PARENT_ID = o4.ID and o4.hlevel != 0 and o3.hlevel is not null ");
            sql.append("left join FC_COMPANY c4 on o4.code = c4.code ");
            sql.append("left join RPT_COMPANY_ORG o5 on o4.PARENT_ID = o5.ID and o5.hlevel != 0 and o4.hlevel is not null ");
            sql.append("left join FC_COMPANY c5 on o5.code = c5.code ");
            sql.append("left join RPT_COMPANY_ORG o6 on o5.PARENT_ID = o6.ID and o6.hlevel != 0 and o5.hlevel is not null ");//level 5(ex:5800,5900)
            sql.append("left join FC_COMPANY c6 on o6.code = c6.code ");
            sql.append("where 1=1 ");
            String pColumnName3 = "c.COMP_GROUP";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName3, group.getCode(), params));
            String pColumnName4 = "o1.COMP_GROUP";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName4, group.getCode(), params));
            sql.append(") c2 on c2.code = t.ZAFBUK ");
        }
        sql.append("left join FC_ACCOUNTS_DETAIL_MAP dm on dm.DETAIL_CODE = t.ACCOUNT_CODE ");
        sql.append("where 1=1 ");
        if(StringUtils.isNotBlank(year)){
            String pColumnName = "t.ZGJAHR";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, year, params));
        }
        if(StringUtils.isNotBlank(month)){
            String pColumnName = "t.ZMONAT";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, month, params));
        }
        //指定報表公司 ex:2B00XX
        String pColumnName = "c1.h1code";
        sql.append(NativeSQLUtils.genEqualSQL(pColumnName, org.getCode(), params));
        
        sql.append("and dm.DETAIL_CODE is null ");//對應會科不符
        
        //初步設計不排除內部
//        if(consolidation){
//            sql.append("and c1.h1code != c2.h1code ");//排除掉內部交易
//        }
        sql.append("order by t.bukrs, t.ZAFBUK ");
        
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
//            logger.debug("list.size()= 0 ");
            return list;
        }
        
        List<ZtfiAfrcTran> resultList = new ArrayList<>();
        for (Object row : list) {
            Object[] columns = (Object[]) row;
            ZtfiAfrcTran vo = new ZtfiAfrcTran(
                    (Long) columns[0],
                    (short) columns[1],
                    (short) columns[2],
                    (String) columns[3],
                    (String) columns[4],
                    (String) columns[5],
                    (String) columns[6],
                    (short) columns[7],
                    (String) columns[8],
                    (String) columns[9]
            );
            vo.setKunnr((String) columns[10]);
            vo.setLifnr((String) columns[11]);
            vo.setHkont((String) columns[12]);
            vo.setBudat((Date) columns[13]);
            vo.setBlart((String) columns[14]);
            vo.setWaers((String) columns[15]);
            vo.setWrbtr((BigDecimal) columns[16]);
            vo.setDmbtr((BigDecimal) columns[17]);
            vo.setZuonr((String) columns[18]);
            vo.setSgtxt((String) columns[19]);
            vo.setZaftlSaco((String) columns[20]);
            vo.setZafbukNm((String) columns[21]);
            vo.setTxt50((String) columns[22]);
            vo.setZckflg((String) columns[23]);
            vo.setZlupdt((Date) columns[24]);
            //25
            vo.setHkontIfrs((String) columns[26]);
            vo.setAccountCode((String) columns[27]);
	    resultList.add(vo);
        }
        return resultList;
    }
    
    
    //關係人交易 檔案上傳 會科列表
    public List<String> findDetailAccount(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("dm.DETAIL_CODE ");
        sql.append("from FC_ACCOUNTS_DETAIL_MAP dm ");
        Query query = em.createNativeQuery(sql.toString());
        
        List<String> resultList = query.getResultList();
        return resultList;
    }
    
    public List<ReportBaseVO> findIrsReportB1Data(CompanyGroupEnum group, String yearMonth, String sheetType) {
        String year;
        String month;
        if(StringUtils.isNotBlank(yearMonth)){
            String[] string_array = DateUtils.getYearAndMonth(yearMonth);
            year = string_array[0];
            month = string_array[1];
        }else{
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("c1.code as c1code, ");
        sql.append("c2.code as c2code, ");
        sql.append("c2.name as c2Name, ");
        sql.append("c2.rtype as rtype, ");
        sql.append("acc.REPA_TYPE as repaType, ");//t.ZAFCAT
        sql.append("acc.code as accCode, ");//t.ACCOUNT_CODE
        sql.append("curr.code as currCode, ");
        sql.append("sum(t.DMBTR) as amount ");
        sql.append("from ZTFI_AFRC_TRAN t ");
        
        sql.append("join (select ");
        sql.append("c.code, ");
        sql.append("c.COMP_GROUP, ");
        sql.append("c.CURRENCY ");
        sql.append("from FC_COMPANY c ");
        sql.append("where 1=1 ");
        //只抓台泥1000上傳金額
        if(CompanyGroupEnum.TCC.equals(group)){
            sql.append(" and c.code = '1000' ");
        }else{
            sql.append(" and c.code = '8000' ");
        }
        sql.append(") c1 on c1.code = t.bukrs ");
        sql.append("join (select ");
        sql.append("c.code, ");
        sql.append("c.ABBREVIATION as name, ");
        sql.append("ict.R_TYPE as rtype ");
        sql.append("from FC_COMPANY c ");
        //對象公司 關係人公司關係設定(companyType)
        sql.append("join IRS_COMPANY_TYPE ict on c.id = ict.COMPANY_ID ");
        String pColumnName1 = "ict.COMP_GROUP";
        sql.append(NativeSQLUtils.genEqualSQL(pColumnName1, group.getCode(), params));
        sql.append("where 1=1 ");
        sql.append(") c2 on c2.code = t.ZAFBUK ");
        
        sql.append("join FC_CURRENCY curr on curr.id = c1.CURRENCY ");
        sql.append("join FC_ACCOUNTS_DETAIL_MAP dm on dm.DETAIL_CODE = t.ACCOUNT_CODE ");
        sql.append("join IRS_ACCOUNTS acc on acc.code = dm.NOTE_LINE_CODE  ");
        sql.append(" and acc.CATEGORY = 'B'");//irs Report B
        //20160418 TCC CSRC 先使用同一組報表會科
//        String pColumnName3 = "acc.COMP_GROUP";
//        sql.append(NativeSQLUtils.genEqualSQL(pColumnName3, group.getCode(), params));
        String pColumnName4 = "acc.BSIS_TYPE";
        sql.append(NativeSQLUtils.genEqualSQL(pColumnName4, sheetType, params));
        sql.append("where 1=1 ");
        if(StringUtils.isNotBlank(year)){
            String pColumnName = "t.ZGJAHR";
            sql.append(NativeSQLUtils.genEqualSQL(pColumnName, year, params));
        }
        if(StringUtils.isNotBlank(month)){
            if(SheetTypeEnum.BS.getValue().equals(sheetType)){
                String pColumnName = "t.ZMONAT";
                sql.append(NativeSQLUtils.genEqualSQL(pColumnName, month, params));
            }else{//IS 累計同年 之前各月
                String pColumnName = "t.ZMONAT";
                sql.append(NativeSQLUtils.getBetweenSQL(pColumnName, null, month));
            }
        }
        sql.append("group by ");
        sql.append("c1.code,c2.code,c2.name,c2.rtype,acc.REPA_TYPE,acc.code,curr.code ");
//        System.out.println("sql: "+sql);
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
//            logger.debug("list.size()= 0 ");
            return list;
        }
        
        List<ReportBaseVO> resultList = new ArrayList<>();
        for (Object row : list) {
            ReportBaseVO vo = new ReportBaseVO();
            Object[] columns = (Object[]) row;
            vo.setCompCode((String) columns[0]);
            vo.setComp2Code((String) columns[1]);
            vo.setComp2Name((String) columns[2]);
            vo.setAbbreviation((String) columns[3]);//R_TYPE 借用欄位
            vo.setSheetCode((String) columns[4]);//RE PA 借用欄位
            vo.setAccCode((String) columns[5]);
            vo.setCurrCode((String) columns[6]);
            vo.setAmount((BigDecimal) columns[7]);
	    resultList.add(vo);
        }
        return resultList;
    }
}
