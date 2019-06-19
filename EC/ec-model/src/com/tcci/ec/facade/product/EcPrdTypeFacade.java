/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.product;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPrdType;
import com.tcci.ec.enums.ProductStatusEnum;
import com.tcci.ec.facade.AbstractFacade;
import com.tcci.ec.model.PrdTypeTreeNodeVO;
import com.tcci.ec.model.PrdTypeTreeVO;
import com.tcci.ec.model.PrdTypeVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EcPrdTypeFacade extends AbstractFacade {
    private final static Logger logger = LoggerFactory.getLogger(EcPrdTypeFacade.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcPrdTypeFacade() {
        super(EcPrdType.class);
    }

    public void save(EcPrdType entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public EcPrdType find(Long id) {
        return em.find(EcPrdType.class, id);
    }
    
    @Override
    public List<EcPrdType> findAll() {
        return em.createNamedQuery("EcPrdType.findAll").getResultList();
    }
    
    public List<EcPrdType> findAllActive() {
        return em.createNamedQuery("EcPrdType.findAllActive").getResultList();
    }
    
    public List<EcPrdType> findLevel1() {
        return em.createNamedQuery("EcPrdType.findLevel1").getResultList();
    }
    
    public List<EcPrdType> findByParent( EcPrdType ecPrdType) {
        Query q = em.createNamedQuery("EcPrdType.findByParent");
        q.setParameter("parent", ecPrdType);
        return q.getResultList();
        
    }
    
    /**
     * 產品樹狀圖 (3層)
     * @param storeId
     * @param typeOnly
     * @return 
     */
    public PrdTypeTreeVO findPrdTypeTree(Long storeId, boolean typeOnly){
        List<PrdTypeVO> list = findForTree(storeId, typeOnly);
        if( list==null || list.isEmpty() ){
            logger.error("findPrdTypeTree list==null");
            return null;
        }
        
        // 組成 tree data for client UI
        PrdTypeTreeVO tree = new PrdTypeTreeVO();
        List<PrdTypeTreeNodeVO> nodes = new ArrayList<PrdTypeTreeNodeVO>();
        
        for(PrdTypeVO vo : list){
            if( vo.getParent()==null ){// 第一層
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
        sql.append("SELECT S.*, S.PARENT PARENTL2, P.CNAME PARENTNAME, P.PARENT PARENTL1 \n");
        
        if( !typeOnly ){
            sql.append(", NVL(D.CC, 0) PRD_NUM \n");
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
}
