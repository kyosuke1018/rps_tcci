/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPrdType;
import com.tcci.ec.enums.ProductStatusEnum;
import com.tcci.ec.model.PrdTypeTreeNodeVO;
import com.tcci.ec.model.PrdTypeTreeVO;
import com.tcci.ec.model.PrdTypeVO;
import com.tcci.ec.model.criteria.ProductCriteriaVO;
import com.tcci.fc.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcPrdTypeFacade extends AbstractFacade<EcPrdType> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcPrdTypeFacade() {
        super(EcPrdType.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcPrdType entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getDisabled()==null ){ entity.setDisabled(false); }
            if( entity.getSortnum()==null ){ entity.setSortnum(0); } 

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
     * 產品樹狀圖 (3層)
     * @param storeId
     * @param typeOnly
     * @return 
     */
    public PrdTypeTreeVO findPrdTypeTree(Long storeId, boolean typeOnly, String opLang){
        List<PrdTypeVO> list = findForTree(storeId, typeOnly);
        if( list==null || list.isEmpty() ){
            logger.error("findPrdTypeTree list==null");
            return null;
        }
        
        // 組成 tree data for client UI
        PrdTypeTreeVO tree = new PrdTypeTreeVO();
        List<PrdTypeTreeNodeVO> nodes = new ArrayList<PrdTypeTreeNodeVO>();
        
        for(PrdTypeVO vo : list){
            if( opLang!=null ){
                // use cname or ename by opLang (C/E)
                String name = "E".equals(opLang)?(StringUtils.isBlank(vo.getEname())?vo.getCname():vo.getEname()):vo.getCname();
                String parentName = "E".equals(opLang)?(StringUtils.isBlank(vo.getParentEname())?vo.getParentName():vo.getParentEname()):vo.getParentName();
                vo.setCname(name);
                vo.setParentEname(parentName);
            }
            if( vo.getParent()==null || vo.getParent().equals(0L) ){// 第一層
                PrdTypeTreeNodeVO node = buildSubTree(vo, list, GlobalConstant.PRD_TYPE_LEVEL);
                
                nodes.add(node);
            }
        }
        
        tree.setNodes(nodes);
        
        return tree;
    }
    public List<PrdTypeVO> findForTree(Long storeId, boolean typeOnly){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        // 商品分類
        sql.append("SELECT S.*, S.PARENT PARENTL2, P.CNAME PARENTNAME, P.ENAME PARENTENAME, P.PARENT PARENTL1 \n");
        
        if( !typeOnly ){
            sql.append(", NVL(D.CC, 0) PRD_NUM \n");
            sql.append(", NVL(T.CC, 0) ATTR_NUM \n");
        }
        
        sql.append("FROM EC_PRD_TYPE S \n");
        sql.append("LEFT OUTER JOIN EC_PRD_TYPE P ON P.ID=S.PARENT \n");
        
        if( !typeOnly ){
            sql.append("LEFT OUTER JOIN ( \n");
            sql.append("     SELECT A.TYPE_ID, COUNT(*) CC \n");
            sql.append("     FROM EC_PRODUCT A \n");
            sql.append("     WHERE A.DISABLED=0 AND A.STATUS IN ('").append(ProductStatusEnum.PUBLISH.getCode()).append("') \n"); 
            if( storeId!=null && storeId>0 ){
                sql.append("     AND A.STORE_ID=#PRD_STORE_ID \n");
                params.put("PRD_STORE_ID", storeId);
            }
            sql.append("     GROUP BY A.TYPE_ID \n");
            sql.append(") D ON D.TYPE_ID=S.ID \n");
            
            sql.append("LEFT OUTER JOIN ( \n"); 
            sql.append("    SELECT STORE_ID, TYPE_ID, COUNT(*) CC \n");
            sql.append("    FROM EC_PRD_ATTR WHERE DISABLED=0 AND TYPE_ID IS NOT NULL  \n");
            sql.append("    GROUP BY STORE_ID, TYPE_ID \n");
            sql.append(") T ON T.STORE_ID=S.STORE_ID AND T.TYPE_ID=S.ID \n");
        }
        
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");
        sql.append("AND S.STORE_ID=#STORE_ID \n");
        sql.append("ORDER BY S.LEVELNUM, S.SORTNUM \n");
        
        params.put("STORE_ID", GlobalConstant.SHARE_PRD_TYPE?GlobalConstant.SHARE_STORE_ID:storeId);
        
        List<PrdTypeVO> list = this.selectBySql(PrdTypeVO.class, sql.toString(), params);
        return list;
    }

    /**
     * 建立產品樹狀圖
     * @param parent
     * @param list
     * @param maxLevel
     * @return 
     */
    public PrdTypeTreeNodeVO buildSubTree(PrdTypeVO parent, List<PrdTypeVO> list, int maxLevel){
        PrdTypeTreeNodeVO parentNode = new PrdTypeTreeNodeVO();
        parentNode.setData(parent);
        
        int prdNum = parent.getPrdNum()==null?0:parent.getPrdNum();
        if( parent.getLevelnum() < maxLevel ){
            List<PrdTypeTreeNodeVO> children = new ArrayList<PrdTypeTreeNodeVO>();
            for(PrdTypeVO vo : list){
                if( parent.getId().equals(vo.getParent()) ){
                    PrdTypeTreeNodeVO node = buildSubTree(vo, list, maxLevel);
                    children.add(node);
                    prdNum += node.getData().getPrdNum();
                }
            }
            parentNode.setChildren(children);    
        }
        parentNode.getData().setPrdNum(prdNum);
        
        return parentNode;
    }

    public boolean checkInput(EcPrdType entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }

    public List<PrdTypeVO> findByCriteria(ProductCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        // 商品分類
        sql.append("SELECT S.* \n");
        sql.append(", L2.ID PARENTL2, L2.CNAME PARENTNAMEL2, L2.ENAME PARENTENAMEL2 \n");
        sql.append(", L1.ID PARENTL1, L1.CNAME PARENTNAMEL1, L1.ENAME PARENTENAMEL1 \n");
        sql.append("FROM EC_PRD_TYPE S \n");
        sql.append("LEFT OUTER JOIN EC_PRD_TYPE L2 ON L2.ID=S.PARENT AND L2.DISABLED=0 \n");
        sql.append("LEFT OUTER JOIN EC_PRD_TYPE L1 ON L1.ID=L2.PARENT AND L1.DISABLED=0 \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");
        
        if( criteriaVO.getTccPrd()!=null && criteriaVO.getTccPrd() ){
            sql.append("AND S.CODE=#CODE \n");
            sql.append("AND S.LEVELNUM=#LEVELNUM \n");

            params.put("CODE", GlobalConstant.TCC_PRD_TYPE);
            params.put("LEVELNUM", GlobalConstant.PRD_TYPE_LEVEL);
        }
        sql.append("ORDER BY S.LEVELNUM, S.SORTNUM \n");
        
        List<PrdTypeVO> list = this.selectBySql(PrdTypeVO.class, sql.toString(), params);
        return list;
    }
    
    /**
     * 台泥商品歸屬分類
     * @return 
     */
    public PrdTypeVO findTccPrdType(){
        ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
        criteriaVO.setTccPrd(Boolean.TRUE);
        List<PrdTypeVO> list = findByCriteria(criteriaVO);
        
        return (list!=null && !list.isEmpty())?list.get(0):null;
    }
}
