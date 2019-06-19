/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CsEmployee;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.admin.EmployeeCriteriaVO;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResultSetHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    public List<CsEmployee> findEmployeeByCriteria(EmployeeCriteriaVO criteriaVO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select a.id, a.adaccount, a.name, a.email \n");
        sql.append("from CS_EMPLOYEE a \n");
        sql.append("join cs_company b on b.id=a.companyid \n");
        sql.append("where a.disabled=0 \n");

        // 過濾非人員帳號
        for (int i = 0; i < GlobalConstant.CS_EMP_FILTERS.length; i++) {
            String keyword = GlobalConstant.CS_EMP_FILTERS[i];
            String filter = "%" + keyword + "%";
            sql.append("and a.name not like #filter").append(i).append(" \n");
            params.put("filter" + i, filter);
        }

        if (criteriaVO != null && criteriaVO.getKeyword() != null) {
            String keyword = '%' + criteriaVO.getKeyword() + "%";
            // sql.append("and (a.adaccount like #keyword OR a.name like #keyword) \n");
            sql.append("and (a.adaccount||a.name like #keyword) \n");
            params.put("keyword", keyword);
        }
        if (criteriaVO != null && criteriaVO.getComCode() != null) {
            sql.append("and b.code=#code \n");
            params.put("code", criteriaVO.getComCode());
        }

        sql.append("order by a.name, a.adaccount");// 預設依名稱排序
        // sql.append("order by a.adaccount, a.name");

        logger.debug("findEmployeeByCriteria sql = \n" + sql.toString());

        ResultSetHelper<CsEmployee> resultSetHelper = new ResultSetHelper(CsEmployee.class);
        List<CsEmployee> resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);

        return resList;
    }

    /**
     * 依條件找員工
     *
     * @param criteriaVO
     * @return
     */
    public List<CsEmployee> findEmployeeByCriteriaForRe(EmployeeCriteriaVO criteriaVO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select a.id, a.adaccount, a.name, a.email \n");
        sql.append("from CS_EMPLOYEE a \n");
        sql.append("join cs_company b on b.id=a.companyid \n");
        sql.append("where a.disabled=0 \n");

        // 過濾非人員帳號
        for (int i = 0; i < GlobalConstant.CS_EMP_FILTERS.length; i++) {
            String keyword = GlobalConstant.CS_EMP_FILTERS[i];
            String filter = "%" + keyword + "%";
            sql.append("and a.name not like #filter").append(i).append(" \n");
            params.put("filter" + i, filter);
        }

        if (criteriaVO != null && criteriaVO.getKeyword() != null) {
            String keyword = '%' + criteriaVO.getKeyword() + "%";
            // sql.append("and (a.adaccount like #keyword OR a.name like #keyword) \n");
            sql.append("and (a.name||'('||a.adaccount||')' like #keyword) \n");
            params.put("keyword", keyword);
        }
        if (criteriaVO != null && criteriaVO.getComCode() != null) {
            sql.append("and b.code=#code \n");
            params.put("code", criteriaVO.getComCode());
        }

        sql.append("order by a.name, a.adaccount");// 預設依名稱排序
        // sql.append("order by a.adaccount, a.name");

        logger.debug("findEmployeeByCriteria sql = \n" + sql.toString());

        ResultSetHelper<CsEmployee> resultSetHelper = new ResultSetHelper(CsEmployee.class);
        List<CsEmployee> resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);

        return resList;
    }

    // find by company
    public List<CsEmployee> findByComCode(String comCode) {
        EmployeeCriteriaVO theCriteriaVO = new EmployeeCriteriaVO();
        theCriteriaVO.setComCode(comCode);
        return findEmployeeByCriteria(theCriteriaVO);
    }

    /**
     * 依條件找員工
     *
     * @param adAccount
     * @return
     */
    public List<CsEmployee> findEmployeeByAdAccount(String adAccount) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select a.id, a.adaccount, a.name, a.email \n");
        sql.append("from CS_EMPLOYEE a \n");
        sql.append("join cs_company b on b.id=a.companyid \n");
        sql.append("where 1=1 \n");

        sql.append("and a.adaccount=#adaccount \n");
        params.put("adaccount", adAccount);

        logger.debug("findEmployeeByAdAccount sql = \n" + sql.toString());

        ResultSetHelper<CsEmployee> resultSetHelper = new ResultSetHelper(CsEmployee.class);
        List<CsEmployee> resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);

        return resList;
    }

}
