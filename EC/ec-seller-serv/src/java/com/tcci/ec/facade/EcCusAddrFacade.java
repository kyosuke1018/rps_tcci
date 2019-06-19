/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcCusAddr;
import com.tcci.ec.model.CusAddrVO;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.ec.model.rs.StrOptionVO;
import java.util.ArrayList;
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
public class EcCusAddrFacade extends AbstractFacade<EcCusAddr> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcCusAddrFacade() {
        super(EcCusAddr.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcCusAddr entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytime(new Date());
                this.edit(entity, simulated);
                logger.info("save update "+entity);
            }else{
                entity.setCreator(operator);
                entity.setCreatetime(new Date());
                this.create(entity, simulated);
                logger.info("save new "+entity);
            }
        }
    }
    
    /**
     * 單筆儲存
     * @param vo
     * @param operator
     * @param simulated 
     */
    public void saveVO(CusAddrVO vo, EcMember operator, boolean simulated){
        if( vo!=null ){
            EcCusAddr entity = (vo.getId()!=null && vo.getId()>0)? this.find(vo.getId()):new EcCusAddr();
            // 需保存的系統產生欄位
            vo.setCreatetime(entity.getCreatetime());
            // 複製 UI 輸入欄位
            ExtBeanUtils.copyProperties(entity, vo);
            // DB 儲存
            save(entity, operator, simulated);
            // 回傳 VO 欄位
            vo.setId(entity.getId());
            vo.setCreatorId(entity.getCreator()!=null? entity.getCreator().getId():null);
            vo.setCreatetime(entity.getCreatetime());
            vo.setModifierId(entity.getModifier()!=null? entity.getModifier().getId():null);
            vo.setModifytime(entity.getModifytime());
        }
    }

    /**
     * @param criteriaVO
     * @return 
     */
    public List<CusAddrVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append(", DP.NAME DP_NAME, DP.PROVINCE, DP.CITY cityEC10, DP.DISTRICT, DP.TOWN, DP.SALESAREA_ID \n");
        sql.append("FROM EC_CUS_ADDR S \n");
        sql.append("LEFT OUTER JOIN TCCSTORE_USER.EC_DELIVERY_PLACE DP ON DP.ACTIVE=1 AND DP.ID=S.DELIVERY_ID \n");
        sql.append("WHERE 1=1 \n");

        if( criteriaVO.getStoreId()!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");       
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        if( criteriaVO.getMemberId()!=null ){
            sql.append("AND S.MEMBER_ID=#MEMBER_ID \n");       
            params.put("MEMBER_ID", criteriaVO.getMemberId());
        }
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }

        return this.selectBySql(CusAddrVO.class, sql.toString(), params);
    }

    /**
     * 產生選單 
     * @param memberId
     * @param storeId
     * @param fieldname
     * @return 
     */
    public List<LongOptionVO> findCusAddrOptions(Long memberId, Long storeId, String fieldname) {
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setMemberId(memberId);
        criteriaVO.setStoreId(storeId);
        List<CusAddrVO> list = findByCriteria(criteriaVO);
        
        List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
        try{
            if( list!=null ){
                for(CusAddrVO vo : list){
                    // use cname or ename by opLang (C/E)
                    String label = ExtBeanUtils.getProperty(vo, fieldname);
                    if( !sys.isBlank(label) ){
                        ops.add(new LongOptionVO(vo.getId(), label));
                    }
                }
            }
        }catch(Exception e){
            logger.error("findCusAddrOptions exception :\n", e);
        }
        return ops;
    }   

    /**
     * for EC1.0 送達地點 選單 
     * @param memberId
     * @param storeId
     * @param fieldname
     * @return 
     */
    public List<LongOptionVO> findCusAddrOptions(Long memberId, Long storeId) {
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setMemberId(memberId);
        criteriaVO.setStoreId(storeId);
        List<CusAddrVO> list = findByCriteria(criteriaVO);
        
        List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
        try{
            if( list!=null ){
                for(CusAddrVO vo : list){
                    // use cname or ename by opLang (C/E)
                    String label = vo.getDeliveryPlaceLabel();
                    Long id = vo.getDeliveryId();
                    if( sys.isValidId(id) && !sys.isBlank(label) ){
                        ops.add(new LongOptionVO(id, label));
                    }
                }
            }
        }catch(Exception e){
            logger.error("findDeliveryPlaceOptions exception :\n", e);
        }
        return ops;
    }   
    
    /**
     * for EC1.0 車號 選單 
     * @param memberId
     * @param storeId
     * @param fieldname
     * @return 
     */
    public List<StrOptionVO> findCarNoOptions(Long memberId, Long storeId) {
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setMemberId(memberId);
        criteriaVO.setStoreId(storeId);
        List<CusAddrVO> list = findByCriteria(criteriaVO);
        
        List<StrOptionVO> ops = new ArrayList<StrOptionVO>();
        try{
            if( list!=null ){
                for(CusAddrVO vo : list){
                    String carNo = vo.getCarNo();
                    if( !sys.isBlank(carNo) ){
                        ops.add(new StrOptionVO(carNo, carNo));
                    }
                }
            }
        }catch(Exception e){
            logger.error("findCarNoOptions exception :\n", e);
        }
        return ops;
    }   

}
