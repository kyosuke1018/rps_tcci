/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.ResultSetHelper;
import com.tcci.et.entity.KbRichContent;
import com.tcci.et.model.RichContentVO;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class KbRichContentFacade extends AbstractFacade<KbRichContent> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KbRichContentFacade() {
        super(KbRichContent.class);
    }

    /**
     * 單筆儲存
     * 
     * @param entity 
     * @param operator 
     * @param simulated 
     */
    public void save(KbRichContent entity, TcUser operator, boolean simulated){
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytimestamp(new Date());
                this.edit(entity, simulated);
            }else{
                entity.setCreator(operator);
                entity.setCreatetimestamp(new Date());
                this.create(entity, simulated);
            }
        }
    }    
    public void saveVO(RichContentVO pubVO, TcUser operator, boolean simulated){
        KbRichContent entity = (pubVO.getId()!=null)?find(pubVO.getId()):new KbRichContent();
        this.copyVoToEntity(pubVO, entity);
        this.save(entity, operator, simulated);
        this.copyEntityToVo(entity, pubVO);
    }
    
    /**
     * 複製
     * @param entity
     * @param vo
     */
    public void copyEntityToVo(KbRichContent entity, RichContentVO vo){
        ExtBeanUtils.copyProperties(vo, entity);
        
        vo.setLastTime(vo.getLastUpdateTime());
        vo.setLastUserName((vo.getLastUpdateUser()!=null)?vo.getLastUpdateUser().getCname():null);
    }
    public void copyVoToEntity(RichContentVO vo, KbRichContent entity){
        ExtBeanUtils.copyProperties(entity, vo);
    }
    
    /**
     * 依查詢條件抓取資料
     * @param criteriaVO
     * @return 
     */
    public List<RichContentVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        // order by 
        if( criteriaVO!=null && criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.CREATETIMESTAMP DESC \n");
        }
        
        logger.debug("findByCriteria ...");
        ResultSetHelper<RichContentVO> resultSetHelper = new ResultSetHelper(RichContentVO.class);
        List<RichContentVO> resList;
        if( criteriaVO!=null && criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO!=null && criteriaVO.getMaxResults()!=null ){
            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);
        }
        
        return resList;
    }
    
}
