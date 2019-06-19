/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.ec.vo.TccArticle;
import com.tcci.fc.util.ResultSetHelper;
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
public class TccArticleFacade {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * 依條件查詢台泥動態
     * @return 
     */
    public List<TccArticle> findByCriteria(){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT \n");
        sql.append("a.id, a.title, a.PUBDATE, a.link \n");
        sql.append("FROM TCCSTORE_USER.ec_article a \n");
        sql.append("WHERE 1=1 and a.ACTIVE=1 \n");
//        sql.append("and a.LINK is not null \n");
        sql.append("and a.PUBDATE is not null \n");
        sql.append("order by a.PUBDATE DESC ");
        
        ResultSetHelper resultSetHelper = new ResultSetHelper(TccArticle.class);
        List<TccArticle> list = resultSetHelper.queryToPOJOList(em, sql.toString(), null, 0 , 5);
        
        return list;
    }
}
