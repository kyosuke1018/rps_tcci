/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import com.tcci.rpt.entity.RptSheetUpload;
import com.tcci.rpt.entity.RptTbValue;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class RptTbValueFacade {
    private static final Logger logger = LoggerFactory.getLogger(RptTbValueFacade.class);
    
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public void saveExcelValue(RptSheetUpload reportUpload, List<ReportBaseVO> values, TcUser modifier, Date modifytimestamp) {
        // remove old data
        Query q = em.createQuery("DELETE FROM RptTbValue v WHERE v.upload=:upload");
        q.setParameter("upload", reportUpload);
        q.executeUpdate();
        // insert new data
        for (ReportBaseVO rv : values) {
            RptTbValue tb = new RptTbValue();
            tb.setUpload(reportUpload);
            tb.setCode(rv.getAccCode());
            tb.setAmount(rv.getAmount());
            tb.setModifier(modifier);
            tb.setModifytimestamp(modifytimestamp);
            em.persist(tb);
        }
    }
    
    public List<ReportBaseVO> findMonthly(String yearmonth, FcCompany company){
        if (company == null || yearmonth == null) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("u.yearmonth as ym, \n");
        sb.append("c.code as compCode, \n");
        sb.append("c.name as compName, \n");
        sb.append("v.code as accCode, \n");
        sb.append("acc.ZAADET_NM as accName, \n");
        sb.append("ISNULL(v.amount,0) as amount \n");
        sb.append("from RPT_TB_VALUE v \n");
        sb.append("join RPT_SHEET_UPLOAD u on v.UPLOAD_ID = u.ID \n");
        sb.append("join FC_COMPANY c on u.company = c.id \n");
        sb.append("left outer join ZTFI_AFCS_ACMA acc on acc.ZAADET = v.code ");
        if (company != null) {
            String sapGroupCode = company.getGroup().getSapGroupCode();
            sb.append("and acc.ZGROUP = #sapGroupCode \n");
            params.put("sapGroupCode", sapGroupCode);
        }
        sb.append("where 1=1 \n");
        sb.append("and u.YEARMONTH = #yearmonth \n");
        params.put("yearmonth", yearmonth);
        if (company != null) {
            sb.append("and u.company = #company \n");
            params.put("company", company.getId());
        }
        sb.append("order by c.sort, v.code ");
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
	    resultList.add(vo);
        }
        return resultList;
    }
}
