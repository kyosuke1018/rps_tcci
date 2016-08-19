/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.facade;

import com.tcci.fc.facade.service.CacheFlushFacade;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.irs.entity.ReclPortfolio;
import com.tcci.irs.enums.ReclPortfolioStateEnum;
import com.tcci.irs.exception.PortfolioExistingException;
import com.tcci.irs.exception.PortfolioLockedException;
import com.tcci.irs.exception.ReclException;
import com.tcci.irs.facade.sheetdata.IrsSheetdataMFacade;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author David.Jen
 */
@Stateless
public class ReclMgmtFacade {
    @EJB
    private ReclPortfolioFacade portfolioFacade;
    
    @EJB
    private IrsSheetdataMFacade sheetDataFacade;
    
    @EJB
    private CacheFlushFacade cacheFacade;
    
    //
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public void generateRecociling(int year, int month, 
            FcCompany reportEntity, FcCompany counterpart,
            boolean overwrite) throws ReclException{
        this.generateReconciling(year, month, 
                reportEntity == null? null: reportEntity.getCode(), 
                counterpart == null? null: counterpart.getCode());
    }
    
    public void demo_generateRecociling(int year, int month, 
            FcCompany reportEntity, FcCompany counterpart,
            boolean overwrite) throws ReclException{
        //
        ReclPortfolio portfolio = portfolioFacade.findByYearMonth(
                year, month, reportEntity, counterpart);
        //check existency
        if (portfolio != null && !overwrite){
            //
            throw new PortfolioExistingException();
        }
        
        //lock the portfolio
        if (portfolio == null){
            portfolio = new ReclPortfolio();
            //
            portfolio.setYear(year);
            portfolio.setMonth(month);
            portfolio.setCompany1(reportEntity);
            portfolio.setCompany2(counterpart);
        }
        //
        lockPortfolio(portfolio);
        
        //generate reconcilings 
        this.generateReconciling(portfolio);
        
        //unlock the portfolio
        unlockPortfolio(portfolio);
    }
    
    private void generateReconciling(ReclPortfolio portfolio){
        //backup defenses
        backupDefenses(portfolio);
        //remove defenses
        removeDefenses(portfolio);
        //delete existing reconciling
        removeReclItems(portfolio);
        //generate reconciling-parts
        generateReclParts(portfolio);
        //compose parts into reconcilings
        generateReclItems(portfolio);
        //recover defenses
        recoverDefenses(portfolio);
        //refresh all
        cacheFacade.cacheFlush();
    }
    
    private void generateReconciling(int reclYear, int reclMonth, String company1Code, String company2Code){
        //backup defenses
        backupDefenses(reclYear, reclMonth, company1Code, company2Code);
        //remove defenses
        removeDefenses(reclYear, reclMonth, company1Code, company2Code);
        //delete existing reconciling
        removeReclItems(reclYear, reclMonth, company1Code, company2Code);
        //generate reconciling-parts
        generateReclParts(reclYear, reclMonth, company1Code, company2Code);
        //compose parts into reconcilings
        generateReclItems(reclYear, reclMonth, company1Code, company2Code);
        //recover defenses
        recoverDefenses(reclYear, reclMonth, company1Code, company2Code);
        //refresh all
        cacheFacade.cacheFlush();
    }
    
    private void backupDefenses(ReclPortfolio portfolio) {
        backupDefenses(portfolio.getYear(), portfolio.getMonth(), 
                portfolio.getCompany1().getCode(), portfolio.getCompany2().getCode());
    }
    
    private void backupDefenses(int reclYear, int reclMonth, String company1Code, String company2Code) {
        String sqlRemoval = new StringBuilder(
                "delete from TMP_SHEETDATA_RECONCILING_D where  ").append(
                        "year = #reclYear and month = #reclMonth and  ").append(
                        //"((RE_COMPANY_CODE=#company1Code and PA_COMPANY_CODE=#company2Code ) or  ").append(
                        //" (RE_COMPANY_CODE=#company2Code and PA_COMPANY_CODE=#company1Code ))   ").append(
                        "( ").append(
                        " ((''=#company1Code or RE_COMPANY_CODE=#company1Code ) and  ").append(
                        "  (''=#company2Code or PA_COMPANY_CODE=#company2Code)) or  ").append(
                        " ((''=#company2Code or RE_COMPANY_CODE=#company2Code ) and  ").append(
                        "  (''=#company1Code or PA_COMPANY_CODE=#company1Code)) ").append(
                        ")").append(
                        " ").toString();
        Query stmtRemoval = this.em.createNativeQuery(sqlRemoval);
        stmtRemoval.setParameter("reclYear", reclYear);
        stmtRemoval.setParameter("reclMonth", reclMonth);
        stmtRemoval.setParameter("company1Code", company1Code == null ? "": company1Code);
        stmtRemoval.setParameter("company2Code", company2Code == null ? "": company2Code);
        stmtRemoval.executeUpdate();
        //
        String sqlInsert = new StringBuilder(
                "INSERT INTO TMP_SHEETDATA_RECONCILING_D (  ").append(
                        "ID,ACCOUNT_TYPE,AMOUNT_ADJUSTMENTS,REMARK,CREATOR, ").append(
                        "CREATETIMESTAMP,YEAR,MONTH,RE_COMPANY_ID,PA_COMPANY_ID, ").append(
                        "RE_COMPANY_CODE,PA_COMPANY_CODE,RE_ACCOUNT_CODE,CURRENCY_ID,CURRENCY_CODE, ").append(
                        "OLD_SHEETDATAM_ID,ACCOUNT_NODE_ID,REMARK_ONLY ").append(
                        ") SELECT ").append(
                        "ID,ACCOUNT_TYPE,AMOUNT_ADJUSTMENTS,REMARK,CREATOR, ").append(
                        "CREATETIMESTAMP,YEAR,MONTH,RE_COMPANY_ID,PA_COMPANY_ID, ").append(
                        "RE_COMPANY_CODE,PA_COMPANY_CODE,RE_ACCOUNT_CODE,CURRENCY_ID,CURRENCY_CODE, ").append(
                        "OLD_SHEETDATAM_ID,ACCOUNT_NODE_ID,REMARK_ONLY  ").append(
                        "from ( ").append(
                        " select  ").append(
                        "defense.ID as ID, ").append(
                        "defense.ACCOUNT_TYPE, ").append(
                        "defense.AMOUNT_ADJUSTMENTS, ").append(
                        "defense.REMARK, ").append(
                        "defense.CREATOR, ").append(
                        "defense.CREATETIMESTAMP, ").append(
                        "reclItem.YEAR, reclItem.MONTH,   ").append(
                        "reclItem.RE_COMPANY_ID, ").append(
                        "reclItem.PA_COMPANY_ID, ").append(
                        "reComp.CODE as RE_COMPANY_CODE, ").append(
                        "paComp.CODE as PA_COMPANY_CODE, ").append(
                        "reclItem.RE_ACCOUNT_CODE, ").append(
                        "reclItem.CURRENCY_ID,  ").append(
                        "curr.CODE as CURRENCY_CODE, ").append(
                        "defense.SHEETDATAM_ID as OLD_SHEETDATAM_ID, ").append(
                        "defense.ACCOUNT_NODE_ID, ").append(      
                        "defense.REMARK_ONLY ").append(              
                        "from  ").append(
                        "IRS_SHEETDATA_RECONCILING_D defense   ").append(
                        "inner join IRS_SHEETDATA_M reclItem on defense.SHEETDATAM_ID=reclItem.ID  ").append(
                        "inner join FC_COMPANY reComp on reComp.ID = reclItem.RE_COMPANY_ID  ").append(
                        "inner join FC_COMPANY paComp on paComp.ID = reclItem.PA_COMPANY_ID  ").append(
                        "inner join FC_CURRENCY curr on curr.ID = reclItem.CURRENCY_ID  ").append(
                        "where reclItem.year = #reclYear and reclItem.month = #reclMonth and  ").append(
                        "( ").append(
                        //"(reComp.CODE=#company1Code and paComp.CODE=#company2Code) or  ").append(
                        //"(reComp.CODE=#company2Code and paComp.CODE=#company1Code)  ").append(
                        " ((''=#company1Code or reComp.CODE=#company1Code ) and  ").append(
                        "  (''=#company2Code or paComp.CODE=#company2Code)) or  ").append(
                        " ((''=#company2Code or reComp.CODE=#company2Code ) and  ").append(
                        "  (''=#company1Code or paComp.CODE=#company1Code))").append(
                        ") ").append(
                        ") pass1").append(
                        " ").toString();
        Query stmtInsert = this.em.createNativeQuery(sqlInsert);
        stmtInsert.setParameter("reclYear", reclYear);
        stmtInsert.setParameter("reclMonth", reclMonth);
        stmtInsert.setParameter("company1Code", company1Code == null ? "": company1Code);
        stmtInsert.setParameter("company2Code", company2Code == null ? "": company2Code);
        stmtInsert.executeUpdate();
    }
    
    //int reclYear, int reclMonth, String company1Code, String company2Code
    //(portfolio.getYear(), portfolio.getMonth(), portfolio.getCompany1().getCode(), portfolio.getCompany2().getCode());
    
    private void removeDefenses(ReclPortfolio portfolio) {
        removeDefenses(portfolio.getYear(), portfolio.getMonth(), 
                portfolio.getCompany1().getCode(), portfolio.getCompany2().getCode());
    }
    
    private void removeDefenses(int reclYear, int reclMonth, String company1Code, String company2Code) {
        String sqlRemoval = new StringBuilder(
                "delete defense   ").append(
                        "from IRS_SHEETDATA_RECONCILING_D defense  ").append(
                        "inner join IRS_SHEETDATA_M reclItem on defense.SHEETDATAM_ID=reclItem.ID  ").append(
                        "inner join FC_COMPANY reComp on reComp.ID = reclItem.RE_COMPANY_ID  ").append(
                        "inner join FC_COMPANY paComp on paComp.ID = reclItem.PA_COMPANY_ID  ").append(
                        "where  ").append(
                        "reclItem.year = #reclYear and reclItem.month = #reclMonth  and  ").append(
                        "( ").append(
                        //"(reComp.CODE=#company1Code and paComp.CODE=#company2Code) or  ").append(
                        //"(reComp.CODE=#company2Code and paComp.CODE=#company1Code)  ").append(
                        "((reComp.CODE=#company1Code or ''=#company1Code ) and  ").append(
                        " (paComp.CODE=#company2Code or ''=#company2Code )) or  ").append(
                        "((reComp.CODE=#company2Code or ''=#company2Code ) and  ").append(
                        " (paComp.CODE=#company1Code or ''=#company1Code )) ").append(
                        ") ").append(
                        " ").toString();
        Query stmtRemoveParts = this.em.createNativeQuery(sqlRemoval);
        stmtRemoveParts.setParameter("reclYear", reclYear);
        stmtRemoveParts.setParameter("reclMonth", reclMonth);
        stmtRemoveParts.setParameter("company1Code", company1Code == null ? "": company1Code);
        stmtRemoveParts.setParameter("company2Code", company2Code == null ? "": company2Code);
        stmtRemoveParts.executeUpdate();
    }
    
    //int reclYear, int reclMonth, String company1Code, String company2Code
    //(portfolio.getYear(), portfolio.getMonth(), portfolio.getCompany1().getCode(), portfolio.getCompany2().getCode());
    private void removeReclItems(ReclPortfolio portfolio){
        removeReclItems(portfolio.getYear(), portfolio.getMonth(), portfolio.getCompany1().getCode(), portfolio.getCompany2().getCode());
    }
    
    private void removeReclItems(int reclYear, int reclMonth, String company1Code, String company2Code){
        //
        String sqlRemoveParts = new StringBuilder(
                "DELETE from TMP_RECONCILING_PART  ").append(
                        "where ").append(
                        "year = #reclYear and month = #reclMonth ").append(
                        "and ").append(
                        "(").append(
                        //"(BUKRS=#company1Code and ZAFBUK=#company2Code) ").append( 
                        //"or ").append( 
                        //"(BUKRS=#company2Code and ZAFBUK=#company1Code) ").append(        
                        "((''=#company1Code or BUKRS= #company1Code ) and  ").append(
                        " (''=#company2Code or ZAFBUK=#company2Code)) or  ").append(
                        "((''=#company2Code or BUKRS= #company2Code ) and  ").append(
                        " (''=#company1Code or ZAFBUK=#company1Code)) ").append(
                        ")").append(
                        " ").toString();
        Query stmtRemoveParts = this.em.createNativeQuery(sqlRemoveParts);
        stmtRemoveParts.setParameter("reclYear", reclYear);
        stmtRemoveParts.setParameter("reclMonth", reclMonth);
        stmtRemoveParts.setParameter("company1Code", company1Code == null ? "": company1Code);
        stmtRemoveParts.setParameter("company2Code", company2Code == null ? "": company2Code);
        stmtRemoveParts.executeUpdate();
        //
        String sqlRemoveReclItems = new StringBuilder(
                "DELETE reclM from IRS_SHEETDATA_M reclM ").append(
                        "inner join FC_COMPANY reCompany on reCompany.id=reclM.RE_COMPANY_ID ").append(
                        "inner join FC_COMPANY paCompany on paCompany.id=reclM.PA_COMPANY_ID ").append(
                        "where ").append(
                        "  reclM.YEAR = #reclYear and reclM.MONTH = #reclMonth and ").append(
                        "( ").append(
                        // "  (reCompany.CODE=#company1Code  and paCompany.CODE=#company2Code) or ").append(
                        // "  (reCompany.CODE=#company2Code  and paCompany.CODE=#company1Code)").append(
                        "((reCompany.CODE=#company1Code or ''=#company1Code ) and  ").append(
                        " (paCompany.CODE=#company2Code or ''=#company2Code )) or  ").append(
                        "((reCompany.CODE=#company2Code or ''=#company2Code ) and  ").append(
                        " (paCompany.CODE=#company1Code or ''=#company1Code )) ").append(
                        ")  ").toString();

        Query stmtRemoveReclItems = this.em.createNativeQuery(sqlRemoveReclItems);
        stmtRemoveReclItems.setParameter("reclYear", reclYear);
        stmtRemoveReclItems.setParameter("reclMonth", reclMonth);
        stmtRemoveReclItems.setParameter("company1Code", company1Code == null ? "": company1Code);
        stmtRemoveReclItems.setParameter("company2Code", company2Code == null ? "": company2Code);
        stmtRemoveReclItems.executeUpdate();
        
        
    }
    
    //int reclYear, int reclMonth, String company1Code, String company2Code
    //(portfolio.getYear(), portfolio.getMonth(), portfolio.getCompany1().getCode(), portfolio.getCompany2().getCode());
    private void generateReclParts(ReclPortfolio portfolio) {
        generateReclParts(portfolio.getYear(), portfolio.getMonth(), portfolio.getCompany1().getCode(), portfolio.getCompany2().getCode());
    }
    
    private void generateReclParts(int reclYear, int reclMonth, String company1Code, String company2Code) {
        String sqlInsert = new StringBuilder(
                "INSERT INTO TMP_RECONCILING_PART (  ").append(
                        "YEAR,MONTH,BUKRS,ZAFBUK,SUBJECT_ID,").append(
                        "ZAFCAT,WAERS,AMOUNT,COUNT,AGREED_AMOUNT,RAW_AMOUNT  ").append(
                        ") SELECT ").append(
                        "YEAR,MONTH,BUKRS,ZAFBUK,SUBJECT_ID,").append(
                        "ZAFCAT,WAERS,AMOUNT,COUNT,AGREED_AMOUNT,RAW_AMOUNT  ").append(
                        "from  (").append(
                        "select #reclYear as YEAR, #reclMonth as MONTH, ").append(
                        "BUKRS,ZAFBUK,SUBJECT_ID,ZAFCAT,WAERS, ").append(
                        "sum(WRBTR) as AMOUNT, ").append(
                        "count(*) as COUNT,  ").append(
                        "sum(DMBTR) as AGREED_AMOUNT,sum(AMOUNT2) as RAW_AMOUNT  ").append(        
                        "from ").append(
                        "( ").append(
                        "select ").append(
                        "subjectMapping.SUBJECT_ID, ").append(
                        "subjectMapping.NODE_ID,  ").append(
                        "tranData.*,  ").append(
                        "case  ").append(
                        "when tranData.ZMONAT = #reclMonth then WRBTR else 0  ").append(
                        "end as AMOUNT2   ").append(
                        "from  ").append(
                        "( ").append(
                        "select * from ZTFI_AFRC_TRAN  ").append(
                        "where  ").append(
                        "(  ").append(
                        //"  ((BUKRS=#company1Code  and ZAFBUK=#company2Code) or  ").append(
                        //"   (BUKRS=#company2Code  and ZAFBUK=#company1Code)) and ").append(
                        "(((''=#company1Code or BUKRS= #company1Code ) and  ").append( 
                        "  (''=#company2Code or ZAFBUK=#company2Code)) or  ").append( 
                        " ((''=#company2Code or BUKRS= #company2Code ) and  ").append( 
                        "  (''=#company1Code or ZAFBUK=#company1Code))) and ").append(
                        "  ZGJAHR=#reclYear and ( ").append(
                        "  (ZAFTYP in ('ARAP','GLBS','SGLI','GLOT') and ZMONAT = #reclMonth) or ").append(
                        "  (ZAFTYP in ('SACO','GLPL','ASET','OTHE') and ZMONAT <= #reclMonth)  ").append(
                        "  ) ").append(
                        ") ").append(
                        ") tranData  ").append(
                        "inner join FC_COMPANY company1 on company1.CODE=tranData.BUKRS  ").append(
                        "inner join FC_COMPANY company2 on company2.CODE=tranData.ZAFBUK   ").append(
                        "inner join ( ").append(
                        "select tmp1.*, ").append(
                        "case when  tmp1.RECL_ROLE='RE' then tmp1.RE_COMPGROUP else tmp1.PA_COMPGROUP end as COMPGROUP1, ").append(
                        "case when  tmp1.RECL_ROLE='RE' then tmp1.PA_COMPGROUP else tmp1.RE_COMPGROUP end as COMPGROUP2     ").append(
                        "from IRS_GRPTRAN_SUBJECT_MAPPING tmp1   ").append(
                        ") subjectMapping on ( ").append(
                        "subjectMapping.CODE=tranData.ZAFTYP and ").append(
                        "subjectMapping.RECL_ROLE= tranData.ZAFCAT and ").append(
                        //updated by david: workaround for group 'OTHER'        
                        //{        
                        //"subjectMapping.COMPGROUP1=company1.COMP_GROUP and  ").append(
                        //"subjectMapping.COMPGROUP2=company2.COMP_GROUP  ").append(
                        "(subjectMapping.COMPGROUP1=company1.COMP_GROUP or (subjectMapping.COMPGROUP1='TCC' and company1.COMP_GROUP='OTHER')) and ").append(
                        "(subjectMapping.COMPGROUP2=company2.COMP_GROUP or (subjectMapping.COMPGROUP2='TCC' and company2.COMP_GROUP='OTHER'))").append(        
                        //}        
                        ") ").append(
                        ") pass1  ").append(
                        "group by BUKRS,ZAFBUK,SUBJECT_ID,ZAFCAT,WAERS  ").append(
                        ") parts").append(
                        " ").toString();
        Query stmtDeleteEvent = this.em.createNativeQuery(sqlInsert);
        stmtDeleteEvent.setParameter("reclYear", reclYear);
        stmtDeleteEvent.setParameter("reclMonth", reclMonth);
        stmtDeleteEvent.setParameter("company1Code", company1Code == null ? "": company1Code);
        stmtDeleteEvent.setParameter("company2Code", company2Code == null ? "": company2Code);
        stmtDeleteEvent.executeUpdate();
    }
    
    //int reclYear, int reclMonth, String company1Code, String company2Code
    //(portfolio.getYear(), portfolio.getMonth(), portfolio.getCompany1().getCode(), portfolio.getCompany2().getCode());
    private void generateReclItems(ReclPortfolio portfolio) {
        generateReclItems(portfolio.getYear(), portfolio.getMonth(), portfolio.getCompany1().getCode(), portfolio.getCompany2().getCode());
    }
    
    private void generateReclItems(int reclYear, int reclMonth, String company1Code, String company2Code) {
        String sqlInertReclItems = new StringBuilder(
                "INSERT INTO IRS_SHEETDATA_M (").append(
                 "YEAR,MONTH,RE_AMOUNT_ORIG,PA_AMOUNT_ORIG,RE_ACCOUNT_CODE, ").append(
                 "PA_ACCOUNT_CODE,RE_ACCOUNT_NAME,PA_ACCOUNT_NAME,CURRENCY_ID,SHEET_TYPE, ").append(
                 "RE_COMPANY_ID,PA_COMPANY_ID,SORT,RE_AGREED_AMOUNT,RE_RAW_AMOUNT, ").append(    
                 "PA_AGREED_AMOUNT,PA_RAW_AMOUNT,RE_ACCOUNT_ID,PA_ACCOUNT_ID ").append(        
                 ") SELECT ").append(
                 "YEAR,MONTH,reAmount as RE_AMOUNT_ORIG,paAmount as PA_AMOUNT_ORIG,RE_ACCOUNT_CODE, ").append(
                 "PA_ACCOUNT_CODE,RE_ACCOUNT_NAME,PA_ACCOUNT_NAME,CURRENCY_ID,SHEET_TYPE, ").append(
                 "RE_COMPANY_ID,PA_COMPANY_ID,SORT, reAgreedAmount as RE_AGREED_AMOUNT, reRawAmount as RE_RAW_AMOUNT, ").append(     
                 "paAgreedAmount as PA_AGREED_AMOUNT, paRawAmount as PA_RAW_AMOUNT,RE_ACCOUNT_ID,PA_ACCOUNT_ID ").append(             
                 "from (").append(              
                 "select ").append(        
                        "pass1.*, ").append(
                        "reNode.ID as RE_ACCOUNT_ID, paNode.ID as PA_ACCOUNT_ID,").append(      
                        "reNode.CODE as RE_ACCOUNT_CODE, paNode.CODE as PA_ACCOUNT_CODE, ").append(
                        "reNode.NAME as RE_ACCOUNT_NAME, paNode.NAME as PA_ACCOUNT_NAME, ").append(
                        "currency.ID as CURRENCY_ID, ").append(
                        "reNode.CATEGORY as SHEET_TYPE,  ").append(
                        "reComapny.ID as RE_COMPANY_ID, paComapny.ID as PA_COMPANY_ID, ").append(
                        "0 as SORT   ").append(
                        "from  ").append(
                        "( ").append(
                        "select  ").append(
                        "case when rePart.YEAR is not null then rePart.YEAR  ").append(
                        "     else paPart.YEAR end as YEAR, ").append(
                        "case when rePart.MONTH is not null then rePart.MONTH  ").append(
                        "     else paPart.MONTH end as MONTH, ").append(
                        "case when rePart.SUBJECT_ID is not null then rePart.SUBJECT_ID  ").append(
                        "     else paPart.SUBJECT_ID end as SUBJECT_ID, ").append(
                        "case when rePart.WAERS is not null then rePart.WAERS  ").append(
                        "     else paPart.WAERS end as CURRENCY_CODE, ").append(
                        "case when rePart.BUKRS is not null then rePart.BUKRS  ").append(
                        "     else paPart.ZAFBUK end as reCompCode, ").append(
                        "case when rePart.ZAFBUK is not null then rePart.ZAFBUK  ").append(
                        "     else paPart.BUKRS end as reTranSubject, ").append(
                        "case when rePart.AMOUNT is not null then rePart.AMOUNT  ").append(
                        "     else 0 end as reAmount, ").append(
                        "case when rePart.COUNT is not null then rePart.COUNT  ").append(
                        "     else 0 end as reCount, ").append(
                        "case when rePart.AGREED_AMOUNT is not null then rePart.AGREED_AMOUNT  ").append(
                        "     else 0 end as reAgreedAmount, ").append(
                        "case when rePart.RAW_AMOUNT is not null then rePart.RAW_AMOUNT  ").append(
                        "     else 0 end as reRawAmount, ").append(        
                        "case when paPart.BUKRS is not null then paPart.BUKRS  ").append(
                        "     else rePart.ZAFBUK end as paCompCode, ").append(
                        "case when paPart.ZAFBUK is not null then paPart.ZAFBUK  ").append(
                        "     else rePart.BUKRS end as paTranSubject, ").append(
                        "case when paPart.AMOUNT is not null then paPart.AMOUNT  ").append(
                        "     else 0 end as paAmount, ").append(
                        "case when paPart.COUNT is not null then paPart.COUNT  ").append(
                        "     else 0 end as paCount, 	   ").append(
                        "case when paPart.AGREED_AMOUNT is not null then paPart.AGREED_AMOUNT  ").append(
                        "     else 0 end as paAgreedAmount, ").append(
                        "case when paPart.RAW_AMOUNT is not null then paPart.RAW_AMOUNT  ").append(
                        "     else 0 end as paRawAmount   ").append(        
                        "from  ").append(
                        "( ").append(
                        "select * from TMP_RECONCILING_PART  ").append(
                        "where ZAFCAT='RE' and  ").append(
                        "year = #reclYear and month = #reclMonth and (  ").append(
                        //"  (BUKRS=#company1Code and ZAFBUK=#company2Code) or  ").append(
                        //"  (BUKRS=#company2Code and ZAFBUK=#company1Code) ").append(
                        "   ((BUKRS= #company1Code  or ''=#company1Code ) and  ").append(
                        "    (ZAFBUK=#company2Code or ''= #company2Code )) or  ").append(
                        "   ((BUKRS= #company2Code  or ''=#company2Code ) and  ").append(
                        "    (ZAFBUK=#company1Code or ''= #company1Code )) ").append(        
                        " ) ").append(
                        ") rePart  ").append(
                        "full join  ").append(
                        "( ").append(
                        "select * from TMP_RECONCILING_PART  ").append(
                        "where ZAFCAT='PA' and  ").append(
                        "year = #reclYear and month = #reclMonth and ( ").append(
                        //"  (BUKRS=#company1Code and ZAFBUK=#company2Code) or  ").append(
                        //"  (BUKRS=#company2Code and ZAFBUK=#company1Code) ").append(
                        "   ((BUKRS= #company1Code  or ''=#company1Code ) and  ").append(
                        "    (ZAFBUK=#company2Code or ''= #company2Code )) or  ").append(
                        "   ((BUKRS= #company2Code  or ''=#company2Code ) and  ").append(
                        "    (ZAFBUK=#company1Code or ''= #company1Code )) ").append(              
                        " ) ").append(
                        ") paPart on ( ").append(
                        "paPart.YEAR=rePart.YEAR and paPart.MONTH=rePart.MONTH and   ").append(
                        "paPart.SUBJECT_ID = rePart.SUBJECT_ID and  ").append(
                        "paPart.WAERS=rePart.WAERS and  ").append(
                        "paPart.ZAFBUK=rePart.BUKRS and paPart.BUKRS=rePart.ZAFBUK  ").append(
                        ") ").append(
                        ") pass1  ").append(
                        "inner join IRS_RECLSUBJECT reclSubject on reclSubject.ID=pass1.SUBJECT_ID   ").append(
                        "inner join IRS_ACCOUNT_NODE reNode on reNode.ID=reclSubject.RE_ACCNODE_ID  ").append(
                        "inner join IRS_ACCOUNT_NODE paNode on paNode.ID=reclSubject.PA_ACCNODE_ID  ").append(
                        "inner join FC_CURRENCY currency on currency.code=pass1.CURRENCY_CODE  ").append(
                        "inner join FC_COMPANY reComapny on reComapny.code = pass1.reCompCode  ").append(
                        "inner join FC_COMPANY paComapny on paComapny.code = pass1.paCompCode ").append(
                 ") reclItems ").append(
                 "").append(        
                 "").toString();

        Query stmtInsert = this.em.createNativeQuery(sqlInertReclItems);
        stmtInsert.setParameter("reclYear", reclYear);
        stmtInsert.setParameter("reclMonth", reclMonth);
        stmtInsert.setParameter("company1Code", company1Code == null ? "": company1Code);
        stmtInsert.setParameter("company2Code", company2Code == null ? "": company2Code);
        stmtInsert.executeUpdate();
    }
    
    //int reclYear, int reclMonth, String company1Code, String company2Code
    //(portfolio.getYear(), portfolio.getMonth(), portfolio.getCompany1().getCode(), portfolio.getCompany2().getCode());
    
    private void recoverDefenses(ReclPortfolio portfolio) {
        recoverDefenses(portfolio.getYear(), portfolio.getMonth(), portfolio.getCompany1().getCode(), portfolio.getCompany2().getCode());
    }
    
    private void recoverDefenses(int reclYear, int reclMonth, String company1Code, String company2Code) {
        String sqlInsert = new StringBuilder(
                "INSERT INTO IRS_SHEETDATA_RECONCILING_D (  ").append(
                        "ACCOUNT_TYPE,AMOUNT_ADJUSTMENTS,REMARK,CREATOR,CREATETIMESTAMP, ").append(
                        "SHEETDATAM_ID,ACCOUNT_NODE_ID,REMARK_ONLY ").append(
                        ") SELECT ").append(
                        "ACCOUNT_TYPE,AMOUNT_ADJUSTMENTS,REMARK,CREATOR,CREATETIMESTAMP, ").append(
                        "SHEETDATAM_ID,ACCOUNT_NODE_ID,REMARK_ONLY ").append(
                        "from ( ").append(
                        " select  ").append(
                        "defense.ACCOUNT_TYPE, ").append(
                        "defense.AMOUNT_ADJUSTMENTS, ").append(
                        "defense.REMARK, ").append(
                        "defense.CREATOR, ").append(
                        "defense.CREATETIMESTAMP, ").append(
                        "reclItem.ID as SHEETDATAM_ID, ").append(
                        "defense.ACCOUNT_NODE_ID, ").append(
                        "defense.REMARK_ONLY ").append(        
                        "from  ").append(
                        "TMP_SHEETDATA_RECONCILING_D defense  ").append(
                        "inner join IRS_SHEETDATA_M reclItem on ( ").append(
                        "  defense.YEAR = reclItem.YEAR and defense.MONTH = reclItem.MONTH and  ").append(
                        "  defense.RE_COMPANY_ID = reclItem.RE_COMPANY_ID and  ").append(
                        "  defense.PA_COMPANY_ID = reclItem.PA_COMPANY_ID and  ").append(
                        "  defense.CURRENCY_ID = reclItem.CURRENCY_ID and  ").append(
                        "  defense.RE_ACCOUNT_CODE = reclItem.RE_ACCOUNT_CODE  ").append(
                        ") ").append(
                        "where reclItem.year = #reclYear and reclItem.month = #reclMonth and  ").append(
                        "( ").append(
                        //"(defense.RE_COMPANY_CODE=#company1Code and defense.PA_COMPANY_CODE=#company2Code) or  ").append(
                        //"(defense.RE_COMPANY_CODE=#company2Code and defense.PA_COMPANY_CODE=#company1Code)  ").append(
                        " ((''=#company1Code or defense.RE_COMPANY_CODE=#company1Code ) and  ").append(
                        "  (''=#company2Code or defense.PA_COMPANY_CODE=#company2Code )) or  ").append(
                        " ((''=#company2Code or defense.RE_COMPANY_CODE=#company2Code ) and  ").append(
                        "  (''=#company1Code or defense.PA_COMPANY_CODE=#company1Code )) ").append(
                        ") ").append(
                        ") pass1").append(
                        " ").toString();
        Query stmtDeleteEvent = this.em.createNativeQuery(sqlInsert);
        stmtDeleteEvent.setParameter("reclYear", reclYear);
        stmtDeleteEvent.setParameter("reclMonth", reclMonth);
        stmtDeleteEvent.setParameter("company1Code", company1Code == null ? "": company1Code);
        stmtDeleteEvent.setParameter("company2Code", company2Code == null ? "": company2Code);
        stmtDeleteEvent.executeUpdate();
    }
    
    //temporarily open for 'controller' to manually lock/unlock the portfolio.
    @TransactionAttribute(REQUIRES_NEW)
    public void lockPortfolio(ReclPortfolio portfolio) throws PortfolioLockedException{
        ReclPortfolioStateEnum state = portfolio.getState();
        if (portfolio.getState() != null && !state.equals(ReclPortfolioStateEnum.VALID)){
            throw new PortfolioLockedException();
        }
        //
        portfolio.setState(ReclPortfolioStateEnum.LOCKED);
        //
        if (portfolio.getId() == null){
            portfolioFacade.create(portfolio);
        } else {
            portfolioFacade.edit(portfolio);
        }
    }
    
    public void unlockPortfolio(ReclPortfolio portfolio){
        portfolio.setState(ReclPortfolioStateEnum.VALID);
        portfolioFacade.edit(portfolio);
    }
}
