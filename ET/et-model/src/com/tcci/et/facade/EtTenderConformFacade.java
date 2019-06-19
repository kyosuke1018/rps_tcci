/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.ec.model.TenderConformVO;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.et.entity.EtTenderConform;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.StringUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EtTenderConformFacade extends AbstractFacade<EtTenderConform> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtTenderConformFacade() {
        super(EtTenderConform.class);
    }

    /**
     * 單筆儲存
     *
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtTenderConform entity, TcUser operator, boolean simulated) {
        if (entity != null) {
            if (entity.getId() != null && entity.getId() > 0) {
                entity.setModifier(operator);
                entity.setModifytime(new Date());
                this.edit(entity, simulated);
                logger.info("save update " + entity);
            } else {
                entity.setCreator(operator);
                entity.setCreatetime(new Date());
                this.create(entity, simulated);
                logger.info("save new " + entity);
            }
        }
    }

    /**
     * 依輸入條件查詢
     *
     * @param criteriaVO
     * @param params
     * @return
     */
    public List<TenderConformVO> findByCriteria(BaseCriteriaVO criteriaVO) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.ID, S.MEMBER_ID, S.MANDT, S.LIFNR_UI venderCode, S.VENDER_NAME \n");
        sql.append(", CONCAT(CF.CODE,CF.NAME) as FACTORYNAME \n");
        sql.append(", CASE WHEN S.MODIFYTIME IS NULL THEN S.CREATETIME ELSE S.MODIFYTIME END datadate \n");
        sql.append(", M.ID MEMBER_ID, M.LOGIN_ACCOUNT, M.NAME \n");
        sql.append(", T.ID TENDER_ID, T.CODE, T.TITLE \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));

        if (criteriaVO.getOrderBy() != null) {
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        } else {
            sql.append("ORDER BY S.MANDT, S.LIFNR_UI");
        }

        logger.debug("findByCriteria sql:" + sql.toString());
        List<TenderConformVO> list = null;
        if (criteriaVO.getFirstResult() != null && criteriaVO.getMaxResults() != null) {
            list = this.selectBySql(TenderConformVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        } else if (criteriaVO.getMaxResults() != null) {
            list = this.selectBySql(TenderConformVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        } else {
            list = this.selectBySql(TenderConformVO.class, sql.toString(), params);
        }
        return list;
    }

    /**
     * 依輸入條件查詢
     *
     * @param criteriaVO
     * @param params
     * @return
     */
    public String findByCriteriaSQL(BaseCriteriaVO criteriaVO, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("FROM ET_TENDER_CONFORM S \n");
        sql.append("JOIN EC_MEMBER M ON M.ID=S.MEMBER_ID \n");
        sql.append("JOIN ET_TENDER T ON T.ID=S.TENDER_ID \n");
        sql.append("LEFT OUTER JOIN CM_FACTORY CF ON CF.ID=T.FACTORY_ID \n");

        sql.append("WHERE 1=1 \n");
        if (criteriaVO.getId() != null) {
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        if (criteriaVO.getMemberId() != null) {
            sql.append("AND M.ID=#MEMBER_ID \n");
            params.put("MEMBER_ID", criteriaVO.getMemberId());
        }
        if (criteriaVO.getTenderId() != null) {
            sql.append("AND T.ID=#TENDER_ID \n");
            params.put("TENDER_ID", criteriaVO.getTenderId());
        }
        if (!StringUtils.isBlank(criteriaVO.getType())) {
            sql.append("AND S.MANDT=#MANDT \n");
            params.put("MANDT", criteriaVO.getType());
        }
        if (!StringUtils.isBlank(criteriaVO.getCode())) {
            sql.append("AND S.LIFNR_UI=#LIFNR_UI \n");
            params.put("LIFNR_UI", criteriaVO.getCode());
        }

        if (criteriaVO.getFactoryId() != null) {
            sql.append("AND T.FACTORY_ID=#factoryId \n");
            params.put("factoryId", criteriaVO.getFactoryId());
        }

        if (!StringUtils.isBlank(criteriaVO.getKeyword())) {
            String kw = "%" + criteriaVO.getKeyword().trim() + "%";
            sql.append("AND ( \n");
            sql.append("S.VENDER_NAME LIKE #KW \n");
            sql.append("OR S.LIFNR_UI LIKE #KW \n");
            sql.append(") \n");
            params.put("KW", kw);
        }

        return sql.toString();
    }
}
