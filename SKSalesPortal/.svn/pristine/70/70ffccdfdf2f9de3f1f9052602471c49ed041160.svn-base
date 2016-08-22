package com.tcci.sksp.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.entity.ar.SkBudget;
import com.tcci.sksp.entity.org.SkSalesMember;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.tcci.sksp.vo.SalesAchievementVO;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Stateless
public class SkBudgetFacade extends AbstractFacade<SkBudget> {

    protected final static Logger logger = LoggerFactory.getLogger(SkBudgetFacade.class);
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @EJB
    private TcUserFacade tcUserFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SkBudgetFacade() {
        super(SkBudget.class);
    }
    
    public void insertOrUpdate(List<SkBudget> budgets, TcUser user) {
        if (budgets != null && !budgets.isEmpty()) {
            for (SkBudget budget : budgets) {
                insertOrUpdate(budget, user);
            }
        }
    }
    
    public void insertOrUpdate(SkBudget budget, TcUser user) {    
        if (budget.getId() != null) {
            budget.setModifier(user);
            budget.setModifytimestamp(new Date());
            edit(budget);
        } else {
            budget.setCreator(user);
            budget.setCreatetimestamp(new Date());            
            create(budget);
        }
    }    
    
    public SkBudget findByCriteria(String sapid, Date baselineTimestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM SK_BUDGET s ");
            sql.append("WHERE s.SAPID = #sapid ");
            sql.append("  AND to_char(s.BASELINE_TIMESTAMP,'yyyy-MM-dd') = #baselineTimestampStr");
            Query q = em.createNativeQuery(sql.toString());
            q.setParameter("sapid", sapid);
            q.setParameter("baselineTimestampStr", sdf.format(baselineTimestamp)); 
            
            List<Object> results = q.getResultList();
            if (results != null && !results.isEmpty()) {
                Object[] result = (Object[])(results.get(0));
                SkBudget budget = new SkBudget();
                budget.setId(((BigDecimal)result[0]).longValue());
                budget.setSapid((String)result[1]);
                budget.setBaselineTimestamp((Date) result[2]);
                budget.setBudget((BigDecimal) result[3]);
                Object creatorId = result[4];
                if (creatorId != null) {
                    TcUser creator = tcUserFacade.find(((BigDecimal)creatorId).longValue());
                    budget.setCreator(creator);
                    budget.setCreatetimestamp((Date)result[5]);
                }
                Object modifierId = result[6];
                if (modifierId != null) {
                    TcUser modifier = tcUserFacade.find(((BigDecimal)modifierId).longValue());
                    budget.setModifier(modifier);
                    budget.setModifytimestamp((Date)result[7]);
                }
                return budget;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }       
        return null;
    }
    
    public SkBudget findByCriteria(SkSalesMember member, String year, String month) {
        SkBudget skBudget = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<SkBudget> cq = builder.createQuery(SkBudget.class);        
        Root<SkBudget> root = cq.from(SkBudget.class);        
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (member != null) {
            Predicate p = builder.equal(root.get("sapid"), member.getCode());
            predicateList.add(p);
        }
        
        if (!StringUtils.isEmpty(year) && !StringUtils.isEmpty(month)) {
            Expression[] args = {root.get("baselineTimestamp"), builder.literal("YYYY/MM")};
            Expression<String> ym = builder.function("to_char", String.class, args);
            Predicate p = builder.equal(ym, year+"/"+month);
            predicateList.add(p);
        }
       
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }

        try{
            skBudget = getEntityManager().createQuery(cq).getSingleResult();
        }catch(javax.persistence.NoResultException e){
            return null;
        }
        
        return skBudget;
    }

    public List<SalesAchievementVO> findForSalesAchievement(SkSalesMember member, String year) {
        List<SalesAchievementVO> salesAchievementVOList = new ArrayList<SalesAchievementVO>();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT t.* ");
            sql.append(", case when NET_PROFIT<>0 then (NET_PROFIT-TOTAL_COST+SALES_RETURN)/NET_PROFIT else 0 end as GROSS_PROFIT_RATE ");
            sql.append(", nvl(ar.INVOICE_AMOUNT,0) as OVERDUE_AMOUNT ");
            sql.append(", case when BUDGET<>0 then ");
            sql.append("       case when substr(t.SAPID,0,1)<'4' then (NET_PROFIT-nvl(ar.INVOICE_AMOUNT,0))/b.BUDGET else NET_PROFIT/b.BUDGET end ");
            sql.append("  else 0 end as ACHIEVEMENT_RATE");            
            sql.append(", nvl(b.BUDGET,0) as BUDGET ");
            sql.append("FROM( ");
            sql.append("  SELECT to_char(s.INVOICE_TIMESTAMP,'YYYY/MM') as BASELINE_TIMESTAMP, SAPID, sum(nvl(AMOUNT,0)) as AMOUNT, sum(nvl(PREMIUM_DISCOUNT,0)) as PREMIUM_DISCOUNT, sum(nvl(SALES_RETURN,0)) as SALES_RETURN, sum(nvl(SALES_DISCOUNT,0)) as SALES_DISCOUNT ");
            sql.append("  , nvl(sum(AMOUNT-PREMIUM_DISCOUNT-SALES_RETURN-SALES_DISCOUNT),0) as NET_PROFIT ");
            sql.append("  , sum(nvl(TOTAL_COST,0)) as TOTAL_COST ");
            sql.append("  FROM SK_SALES_ORDER_MASTER s ");
            sql.append("  WHERE to_char(s.INVOICE_TIMESTAMP, 'YYYY')=#year AND SAPID=#sapid ");
            sql.append("  GROUP BY to_char(s.INVOICE_TIMESTAMP,'YYYY/MM'), SAPID ");
            sql.append(") t LEFT JOIN ");
            sql.append("SK_BUDGET b ON (t.BASELINE_TIMESTAMP = to_char(b.BASELINE_TIMESTAMP,'YYYY/MM') AND t.SAPID = b.SAPID) LEFT JOIN ");
            sql.append("( ");
            sql.append("SELECT to_char(BASELINE_TIMESTAMP,'YYYY/MM') as BASELINE_TIMESTAMP, SAPID, sum(nvl(INVOICE_AMOUNT,0)) as INVOICE_AMOUNT ");
            sql.append("FROM SK_OVERDUE_AR  ");
            sql.append("WHERE OVERDUE_DAYS_NUMBER >= 200 ");
            sql.append("GROUP BY to_char(BASELINE_TIMESTAMP,'YYYY/MM'), SAPID ");
            sql.append(") ar ON (t.BASELINE_TIMESTAMP = ar.BASELINE_TIMESTAMP AND t.SAPID = ar.SAPID) ");
            sql.append("ORDER BY t.BASELINE_TIMESTAMP");
            
            
            Query q = em.createNativeQuery(sql.toString());
            List results = q.setParameter("year", year)
                    .setParameter("sapid", member.getCode())                    
                    .getResultList();
            if(results.size()<=0 ) return salesAchievementVOList;
            
            SalesAchievementVO totalVO = new SalesAchievementVO();             
            totalVO.setAmount(BigDecimal.ZERO);
            totalVO.setPremiumDiscount(BigDecimal.ZERO);
            totalVO.setSalesReturn(BigDecimal.ZERO);
            totalVO.setSalesDiscount(BigDecimal.ZERO);
            totalVO.setNetProfit(BigDecimal.ZERO);
            totalVO.setTotalCost(BigDecimal.ZERO);
            totalVO.setGrossProfitRate(BigDecimal.ZERO);
            totalVO.setOverdueAmount(BigDecimal.ZERO);
            totalVO.setAchievementRate(BigDecimal.ZERO);
            totalVO.setBudget(BigDecimal.ZERO);      

             for (Object o : results) {
                Object[] v = (Object[]) o;                
                SalesAchievementVO vo = new SalesAchievementVO();                                
                int i = 0;
                vo.setYm((String)v[i++]);
                vo.setSapid((String)v[i++]);
                vo.setAmount((BigDecimal)v[i++]);
                vo.setPremiumDiscount((BigDecimal)v[i++]);
                vo.setSalesReturn((BigDecimal)v[i++]);
                vo.setSalesDiscount((BigDecimal)v[i++]);
                vo.setNetProfit((BigDecimal)v[i++]);
                vo.setTotalCost((BigDecimal)v[i++]);
                vo.setGrossProfitRate((BigDecimal)v[i++]);
                vo.setOverdueAmount((BigDecimal)v[i++]);
                vo.setAchievementRate((BigDecimal)v[i++]);
                vo.setBudget((BigDecimal)v[i++]);          
                salesAchievementVOList.add(vo);
                
                totalVO.setAmount(totalVO.getAmount().add(vo.getAmount()));
                totalVO.setPremiumDiscount(totalVO.getPremiumDiscount().add(vo.getPremiumDiscount()));
                totalVO.setSalesReturn(totalVO.getSalesReturn().add(vo.getSalesReturn()));
                totalVO.setSalesDiscount(totalVO.getSalesDiscount().add(vo.getSalesDiscount()));
                totalVO.setNetProfit(totalVO.getNetProfit().add(vo.getNetProfit()));
                totalVO.setTotalCost(totalVO.getTotalCost().add(vo.getTotalCost()));
                totalVO.setGrossProfitRate(totalVO.getGrossProfitRate().add(vo.getGrossProfitRate()));
                totalVO.setOverdueAmount(totalVO.getOverdueAmount().add(vo.getOverdueAmount()));
                totalVO.setAchievementRate(totalVO.getAchievementRate().add(vo.getAchievementRate()));
                totalVO.setBudget(totalVO.getBudget().add(vo.getBudget()));
             }
             salesAchievementVOList.add(0, totalVO);

        } catch (Exception e) {
            logger.error("findByCriteria(), e = {}", e);
        }
        
        return salesAchievementVOList;
    }
    
    public List<SalesAchievementVO> findForSalesAchievement(SkSalesMember member, String year, String month) {
        List<SalesAchievementVO> salesAchievementVOList = new ArrayList<SalesAchievementVO>();
        try {
  
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT t.* ");
            sql.append(", case when NET_PROFIT<>0 then (NET_PROFIT-TOTAL_COST+SALES_RETURN)/NET_PROFIT else 0 end as GROSS_PROFIT_RATE ");
            sql.append(", nvl(b.BUDGET, 0) /30 as BUDGET ");            
            sql.append("FROM( ");
            sql.append("  SELECT s.INVOICE_TIMESTAMP, SAPID, nvl(AMOUNT, 0) as AMOUNT, nvl(PREMIUM_DISCOUNT, 0) as PREMIUM_DISCOUNT, nvl(SALES_RETURN, 0) as SALES_RETURN, nvl(SALES_DISCOUNT, 0) as SALES_DISCOUNT ");
            sql.append("  , nvl(AMOUNT-PREMIUM_DISCOUNT-SALES_RETURN-SALES_DISCOUNT, 0) as NET_PROFIT ");
            sql.append("  , nvl(TOTAL_COST, 0) as TOTAL_COST ");
            sql.append("  FROM SK_SALES_ORDER_MASTER s ");
            sql.append("  WHERE to_char(s.INVOICE_TIMESTAMP, 'YYYY/MM')=#yymm AND SAPID=#sapid ");           
            sql.append(") t LEFT JOIN ");
            sql.append("SK_BUDGET b ON (to_char(t.INVOICE_TIMESTAMP,'YYYY/MM') = to_char(b.BASELINE_TIMESTAMP,'YYYY/MM') AND t.SAPID = b.SAPID) ");            
            sql.append("ORDER BY t.INVOICE_TIMESTAMP");
            
            
            Query q = em.createNativeQuery(sql.toString());
            List results = q.setParameter("yymm", year+"/"+month)
                    .setParameter("sapid", member.getCode())                    
                    .getResultList();
            if(results.size()<=0 ) return salesAchievementVOList;
            
            SalesAchievementVO totalVO = new SalesAchievementVO();             
            totalVO.setAmount(BigDecimal.ZERO);
            totalVO.setPremiumDiscount(BigDecimal.ZERO);
            totalVO.setSalesReturn(BigDecimal.ZERO);
            totalVO.setSalesDiscount(BigDecimal.ZERO);
            totalVO.setNetProfit(BigDecimal.ZERO);
            totalVO.setTotalCost(BigDecimal.ZERO);
            totalVO.setGrossProfitRate(BigDecimal.ZERO);
            totalVO.setOverdueAmount(BigDecimal.ZERO);
            totalVO.setAchievementRate(BigDecimal.ZERO);
            totalVO.setBudget(BigDecimal.ZERO);      

             for (Object o : results) {
                Object[] v = (Object[]) o;                
                SalesAchievementVO vo = new SalesAchievementVO();                                
                int i = 0;
                vo.setInvoiceTimestamp((Date)v[i++]);
                vo.setSapid((String)v[i++]);
                vo.setAmount((BigDecimal)v[i++]);
                vo.setPremiumDiscount((BigDecimal)v[i++]);
                vo.setSalesReturn((BigDecimal)v[i++]);
                vo.setSalesDiscount((BigDecimal)v[i++]);
                vo.setNetProfit((BigDecimal)v[i++]);
                vo.setTotalCost((BigDecimal)v[i++]);
                vo.setGrossProfitRate((BigDecimal)v[i++]);
                vo.setAchievementRate(BigDecimal.ZERO);
                vo.setBudget((BigDecimal)v[i++]);          
                salesAchievementVOList.add(vo);
                
                totalVO.setAmount(totalVO.getAmount().add(vo.getAmount()));
                totalVO.setPremiumDiscount(totalVO.getPremiumDiscount().add(vo.getPremiumDiscount()));
                totalVO.setSalesReturn(totalVO.getSalesReturn().add(vo.getSalesReturn()));
                totalVO.setSalesDiscount(totalVO.getSalesDiscount().add(vo.getSalesDiscount()));
                totalVO.setNetProfit(totalVO.getNetProfit().add(vo.getNetProfit()));
                totalVO.setTotalCost(totalVO.getTotalCost().add(vo.getTotalCost()));
                totalVO.setGrossProfitRate(totalVO.getGrossProfitRate().add(vo.getGrossProfitRate()));
                totalVO.setAchievementRate(totalVO.getAchievementRate().add(vo.getAchievementRate()));
             }
             salesAchievementVOList.add(0, totalVO);

        } catch (Exception e) {
            logger.error("findByCriteria(), e = {}", e);
        }
        return salesAchievementVOList;

    }
}
