/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CsEmployee;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.et.model.admin.EmployeeCriteriaVO;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResultSetHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author peter.pan
 */
@Stateless
public class CsEmployeeFacade extends AbstractFacade<CsEmployee> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CsEmployeeFacade() {
        super(CsEmployee.class);
    }
    
    /**
     * 依條件找員工
     * 
     * @param criteriaVO
     * @return 
     */
    public List<CsEmployee> findEmployeeByCriteria(EmployeeCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT A.ID, A.ADACCOUNT, A.NAME \n");
        sql.append("FROM CS_EMPLOYEE A \n");
        sql.append("JOIN CS_COMPANY B ON B.ID=A.COMPANYID \n");
        sql.append("WHERE A.DISABLED=0 \n");
        
        // 過濾非人員帳號
        for(int i=0; i<GlobalConstant.CS_EMP_FILTERS.length; i++){
            String keyword = GlobalConstant.CS_EMP_FILTERS[i];
            String filter = "%" + keyword + "%";
            sql.append("AND A.NAME NOT LIKE #filter").append(i).append(" \n");
            params.put("filter"+i, filter);
        }
        
        if( criteriaVO!=null && criteriaVO.getKeyword()!=null ){
            String keyword = '%' + criteriaVO.getKeyword() + "%";
            // sql.append("and (a.adaccount like #keyword OR a.name like #keyword) \n");
            sql.append("AND (A.ADACCOUNT||A.NAME LIKE #keyword) \n");
            params.put("keyword", keyword);
        }
        if( criteriaVO!=null && criteriaVO.getComCode()!=null ){
            sql.append("AND B.CODE=#code \n");
            params.put("code", criteriaVO.getComCode());
        }

        sql.append("ORDER BY A.NAME, A.ADACCOUNT");// 預設依名稱排序
        // sql.append("ORDER BY A.ADACCOUNT, A.NAME");

        logger.debug("findEmployeeByCriteria sql = \n"+sql.toString());
        
        ResultSetHelper<CsEmployee> resultSetHelper = new ResultSetHelper(CsEmployee.class);
        List<CsEmployee> resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);

        return resList;
    }
    
    // find by company
    public List<CsEmployee> findByComCode(String comCode){
        EmployeeCriteriaVO theCriteriaVO = new EmployeeCriteriaVO();
        theCriteriaVO.setComCode(comCode);
        return findEmployeeByCriteria(theCriteriaVO);
    }
}
