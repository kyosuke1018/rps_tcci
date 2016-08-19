/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.facade;

import com.tcci.fc.util.time.DateUtils;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import com.tcci.irs.facade.AbstractFacade;
import com.tcci.rpt.entity.RptSheetUpload;
import com.tcci.rpt.entity.ZtfiAfcsCsba;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class ZtfiAfcsCsbaFacade extends AbstractFacade<ZtfiAfcsCsba> {
    private static final Logger logger = LoggerFactory.getLogger(ZtfiAfcsCsbaFacade.class);
    
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZtfiAfcsCsbaFacade() {
        super(ZtfiAfcsCsba.class);
    }
    
    public void saveExcelValue(RptSheetUpload reportUpload, List<ReportBaseVO> values, String zbukto) {
        FcCompany company = reportUpload.getCompany();
	if (StringUtils.isBlank(reportUpload.getYearmonth())
                || company == null) {
            logger.error("ZtfiAfcsCsbaFacade save fail, yearAndMonth or company error");
            return;
        }
//        String zbukto;//TODO find by ZTFI_AFCS_CSBU
//        ZtfiAfcsCsbu ztfiAfcsCsbu = ztfiAfcsCsbuFacade.findByCompany(company);
//        if(ztfiAfcsCsbu != null){
//            zbukto = ztfiAfcsCsbu.getZbukto();
//        }else{
//            zbukto = "1000XX";
//            logger.error("ZtfiAfcsCsbaFacade save fail, zbukto error");
//            return;
//        }
        
        String[] string_array = DateUtils.getYearAndMonth(reportUpload.getYearmonth());
        String year = string_array[0];
        String month = string_array[1];
        Short jgjahr = Short.valueOf(year);
        Short zmonat = Short.valueOf(month);
        
        
        // remove old data
        Query q = em.createNamedQuery("ZtfiAfcsCsba.deleteByCompany");
        q.setParameter("year", jgjahr);
        q.setParameter("month", zmonat);
        q.setParameter("compCode", company.getCode());
        q.executeUpdate();
         
        // insert new data
        for (ReportBaseVO rv : values) {
            ZtfiAfcsCsba entity = new ZtfiAfcsCsba();
            logger.debug("ZtfiAfcsCsbaFacade compCode:"+company.getCode());
            logger.debug("ZtfiAfcsCsbaFacade accCode:"+rv.getAccCode());
            logger.debug("ZtfiAfcsCsbaFacade jgjahr:"+jgjahr);
            logger.debug("ZtfiAfcsCsbaFacade zmonat:"+zmonat);
            logger.debug("ZtfiAfcsCsbaFacade zbukto:"+zbukto);
            entity.setZgjahr(jgjahr);
            entity.setZmonat(zmonat);
            entity.setZbukto(zbukto);
            entity.setZbukfm(company.getCode());
            entity.setZaadet(rv.getAccCode());
            entity.setWaers(company.getCurrency().getCode());
            entity.setDmbtr(rv.getAmount());
//            em.persist(entity);
            create(entity);
        }
    }
    
    public List<ZtfiAfcsCsba> findAllByYM(String yearmonth){
        if (StringUtils.isBlank(yearmonth)) {
            logger.error("ZtfiAfcsCsbaFacade findAllByYM fail, yearmonth error");
            return null;
        }
        String[] string_array = DateUtils.getYearAndMonth(yearmonth);
        String year = string_array[0];
        String month = string_array[1];
        Short jgjahr = Short.valueOf(year);
        Short zmonat = Short.valueOf(month);
        Query q = em.createNamedQuery("ZtfiAfcsCsba.findAll");
        q.setParameter("year", jgjahr);
        q.setParameter("month", zmonat);
        return q.getResultList();
    }
    
    public List<String> findDataYMList(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("distinct ZGJAHR, ZMONAT ");
        sql.append("from ZTFI_AFCS_CSBA ");
        sql.append("WHERE 1=1 ");
        sql.append("order by ZGJAHR desc, ZMONAT desc ");
        
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
}
