/*
* 台灣水泥新聞訊息
*/
package com.tcci.tccweb.tcc.facade;

import com.tcci.tccweb.tcc.entity.News;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 新聞訊息-台泥
 * @author Jackson.Lee
 */
@Stateless
public class NewsFacade extends AbstractFacade<News> {
    
    protected static final Logger logger = LoggerFactory.getLogger(News.class);
    
    @PersistenceContext(unitName = "tccwebPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public NewsFacade() {
        super(News.class);
    }
    
    /**
     * 取得所有已發佈的新聞訊息
     * enable=1
     * @return
     */
    public List<News> findAllReleasedNews() {
        return super.findByNamedQuery("News.findAllEnable", null);
    }
    
    /**
     * 依id查詢已發佈的新聞資料
     * @param idnum
     * @return
     */
    public List<News> findById(int idnum) {
        Map<String, Object> params = new HashMap<>();
        params.put("idnum", idnum);
        return super.findByNamedQuery("News.findByIdnum", params);
    }
    
    
    
    /**
     * 依年度範圍，查詢新聞資料
     *
     * @param fromYY 開始年度(含)
     * @param toYY 結束年度(含)
     * @param orderBy 排序方式(0: 正序，依idnum小到大; 非0: 反序，依idnum大到小)
     * @return
     */
    public List<News> findReleasedNewsByYY(String fromYY, String toYY, int orderBy) {
        
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        
        sql.append(" SELECT ");
        sql.append(" n ");
        sql.append(" FROM News n");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND n.enable = 1 "); //已發佈
        
        
        if (StringUtils.isNotBlank(fromYY)) {
            sql.append(" AND SUBSTRING(n.yy, 0, 5) >= :fromYY ");
            params.put("fromYY", fromYY);
        }
        
        if (StringUtils.isNotBlank(toYY)) {
            sql.append(" AND SUBSTRING(n.yy, 0, 5) <= :toYY ");
            params.put("toYY", toYY);
        }
        
        if (orderBy==1){
            sql.append("ORDER BY n.idnum ");
        }else{
            sql.append("ORDER BY n.idnum desc ");
        }
        
        Query query = em.createQuery(sql.toString());
        if (StringUtils.isNotBlank(fromYY)) {
            query.setParameter("fromYY", fromYY);
        }
        if (StringUtils.isNotBlank(toYY)) {
            query.setParameter("toYY", toYY);
        }
        List<News> queryResults = query.getResultList();
        
        logger.debug("sql = "+sql.toString());
        
        return queryResults;
    }
    
}
