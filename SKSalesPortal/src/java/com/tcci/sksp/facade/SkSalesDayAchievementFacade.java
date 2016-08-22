/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.ar.SkSalesDayAchievement;
import com.tcci.sksp.entity.org.SkSalesMember;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jason.Yu
 */
@Stateless
public class SkSalesDayAchievementFacade extends AbstractFacade<SkSalesDayAchievement> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkSalesDayAchievementFacade() {
        super(SkSalesDayAchievement.class);
    }
    public List<SkSalesDayAchievement> findByCriteria(SkSalesMember member, String yyyymm ){
        List<SkSalesDayAchievement> list = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<SkSalesDayAchievement> cq = builder.createQuery(SkSalesDayAchievement.class);
        Root<SkSalesDayAchievement> root = cq.from(SkSalesDayAchievement.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (member != null) {
            Predicate p = builder.equal(root.get("sapid"), member.getCode());
            predicateList.add(p);
        }
        if (!StringUtils.isEmpty(yyyymm)) {
            Expression[] args = {root.get("baselineTimestamp"),builder.literal( "YYYYMM")};
            Expression<String> yearAndMonth = builder.function("to_char", String.class, args);
            Predicate p = builder.equal( yearAndMonth, yyyymm );
            predicateList.add(p);
        }
        
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(builder.asc(root.get("sapid")), builder.asc(root.get("baselineTimestamp")));
        list = getEntityManager().createQuery(cq).getResultList();
        return list;
    }
    public void callDailyAchievement(String yearmm){
        String sqlCommand ="call p_sales_daily_achievement(?)";
        Query query = getEntityManager().createNativeQuery(sqlCommand);
        query.setParameter(1, yearmm );
        int count = query.executeUpdate();
    }
}
