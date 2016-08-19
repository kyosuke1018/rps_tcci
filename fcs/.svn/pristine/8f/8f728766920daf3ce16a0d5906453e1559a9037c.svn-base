/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.facade.reconciling;

import com.tcci.fc.facade.util.NativeSQLUtils;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.irs.entity.reconciling.IrsSheetdataReconcilingD;
import com.tcci.irs.entity.sheetdata.IrsCompanyClose;
import com.tcci.irs.entity.sheetdata.IrsSheetdataM;
import com.tcci.irs.facade.AbstractFacade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
public class IrsSheetdataReconcilingDFacade extends AbstractFacade<IrsSheetdataReconcilingD> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IrsSheetdataReconcilingDFacade() {
        super(IrsSheetdataReconcilingD.class);
    }
    /*不好的寫法
    public List<IrsSheetdataReconcilingD> find(IrsSheetdataReconcilingD condVO){
    Map<String, Object> params = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    
    sql.append("SELECT ");
    sql.append("ID, ");
    sql.append("1 AS X ");
    sql.append("from IRS_SHEETDATA_RECONCILING_D RECONCILING_D ");
    sql.append("WHERE 1=1 ");
    IrsSheetdataM irsSheetdataM = condVO.getSheetdatam();
    if(null != irsSheetdataM){
    String pColumnName = "RECONCILING_D.SHEETDATAM_ID";
    sql.append(NativeSQLUtils.genEqualSQL(pColumnName, irsSheetdataM.getId(), params));
    }
    
    
    Query query = em.createNativeQuery(sql.toString());
    for (String key : params.keySet()) {
    query.setParameter(key, params.get(key));
    }
    List<Object[]> list = query.getResultList();
    
    List<IrsSheetdataReconcilingD> resultList = new ArrayList<>();
    for (Object[] row : list) {
    long id = (Long)row[0];
    IrsSheetdataReconcilingD entity = find(id);
    
    resultList.add(entity);
    }
    return resultList;
    }
    
    public List<IrsSheetdataReconcilingD> findBySheetdataMId(Long irsSheetdataMId){
    Map<String, Object> params = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    
    sql.append("SELECT ");
    sql.append("ID, ");
    sql.append("1 AS X ");
    sql.append("from IRS_SHEETDATA_RECONCILING_D RECONCILING_D ");
    sql.append("WHERE 1=1 ");
    if(null != irsSheetdataMId){
    String pColumnName = "RECONCILING_D.SHEETDATAM_ID";
    sql.append(NativeSQLUtils.genEqualSQL(pColumnName, irsSheetdataMId, params));
    }
    
    
    Query query = em.createNativeQuery(sql.toString());
    for (String key : params.keySet()) {
    query.setParameter(key, params.get(key));
    }
    List<Object[]> list = query.getResultList();
    
    List<IrsSheetdataReconcilingD> resultList = new ArrayList<>();
    for (Object[] row : list) {
    long id = (Long)row[0];
    IrsSheetdataReconcilingD entity = find(id);
    
    resultList.add(entity);
    }
    return resultList;
    }
    **/
    
    public List<IrsSheetdataReconcilingD> find(IrsSheetdataReconcilingD condVO){
        StringBuilder sql = new StringBuilder();
        sql.append("select d from IrsSheetdataReconcilingD d ");
        sql.append("WHERE 1=1 and d.sheetdatam = :master");
        Query q = em.createQuery(sql.toString());
        q.setParameter("master", condVO.getSheetdatam());
        
        return q.getResultList();
    }
    
    public List<IrsSheetdataReconcilingD> findBySheetdataMId(Long irsSheetdataMId){
        StringBuilder sql = new StringBuilder();
        sql.append("select d from IrsSheetdataReconcilingD d ");
        sql.append("WHERE 1=1 and d.sheetdatam.id = :masterId");
        Query q = em.createQuery(sql.toString());
        q.setParameter("masterId", irsSheetdataMId);
        
        return q.getResultList();
    }
    
    
}
