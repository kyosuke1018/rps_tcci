/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.facade;

import com.tcci.fc.facade.util.NativeSQLUtils;
import com.tcci.fcs.entity.ZtfiAfcsAcma;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;

/**
 *
 * @author kyle.cheng
 */
@Stateless
public class ZtfiAfcsAcmaFacade {
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public ZtfiAfcsAcma find(Long id) {
        return em.find(ZtfiAfcsAcma.class, id);
    }
    
    public ZtfiAfcsAcma findByCode(String group,String code) {
        Query q = em.createNamedQuery("ZtfiAfcsAcma.findByCode");
        q.setParameter("group", group);
        q.setParameter("code", code);
        List<ZtfiAfcsAcma> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<ZtfiAfcsAcma> findAll(String group) {
        Query q = em.createNamedQuery("ZtfiAfcsAcma.findAll");
        q.setParameter("group", group);
        return q.getResultList();
    }
    
    //非合併聯署公司群組
    public List<ReportBaseVO> findIrsReportAccount(String group) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ia.CATEGORY,fadm.NOTE_LINE_CODE,ia.NAME,fadm.DETAIL_CODE,acma.ZAADET_NM ");
        sql.append("from FC_ACCOUNTS_DETAIL_MAP fadm ");
        sql.append("join IRS_ACCOUNTS ia on ia.CODE = fadm.NOTE_LINE_CODE ");
        String pColumnName1 = "ia.COMP_GROUP";
        sql.append(NativeSQLUtils.genEqualSQL(pColumnName1, group, params));
        sql.append("left outer join ZTFI_AFCS_ACMA acma on acma.ZAADET = fadm.DETAIL_CODE ");
        sql.append(" and acma.ZGROUP = 'A' ");//目前不分集團都是A
        sql.append("where 1=1 ");
        sql.append("order by ia.CATEGORY,fadm.NOTE_LINE_CODE,fadm.DETAIL_CODE ");
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
            //借欄位
            vo.setAbbreviation((String) columns[0]);
            vo.setCompCode((String) columns[1]);
            vo.setCompName((String) columns[2]);
            vo.setComp2Code((String) columns[3]);
            vo.setComp2Name((String) columns[4]);
	    resultList.add(vo);
        }
        return resultList;
    }
}
