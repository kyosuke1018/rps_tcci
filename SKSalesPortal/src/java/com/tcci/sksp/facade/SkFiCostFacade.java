/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.SkFiCost;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author carl.lin
 */
@Stateless
public class SkFiCostFacade extends AbstractFacade<SkFiCost> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SkFiCostFacade() {
        super(SkFiCost.class);
    }

    public void save(List<SkFiCost> list, String year) {
        int count = findByYear(year);
        if (count > 0) {
            deleteByBaseline(year);
        }
        for (SkFiCost co : list) {
            SkFiCost cost = new SkFiCost();
            cost.setUnitCost(co.getUnitCost());
            cost.setMatnr(co.getMatnr());
            cost.setYear(year);
            create(cost);
        }
    }

    public int deleteByBaseline(String year) {
        String sqlCommand = "delete from SK_FI_COST t where t.YEAR =? ";
        Query query = getEntityManager().createNativeQuery(sqlCommand);
        query.setParameter(1, year);
        int count = query.executeUpdate();
        //System.out.println("count=" + count);
        return count;
    }

    public int findByYear(String year) {
        String sqlCommand = "select t.ID  from SK_FI_COST t where t.YEAR =? ";
        Query query = getEntityManager().createNativeQuery(sqlCommand);
        query.setParameter(1, year);
        int count = query.executeUpdate();
        //System.out.println("count=" + count);
        return count;
    }
}
