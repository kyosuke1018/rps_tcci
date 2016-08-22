/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.SkSalesBuget;
import com.tcci.sksp.vo.SalesBugetVo;
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
public class SkSalesBugetFacade extends AbstractFacade<SkSalesBuget> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SkSalesBugetFacade() {
        super(SkSalesBuget.class);
    }

    public void save(List<SalesBugetVo> list, String year) {
        int count = findByYear(year);
        if (count > 0) {
            deleteByBaseline(year);
        }
        for (SalesBugetVo vo : list) {
            for (int i = 1; i < 13; i++) {
                SkSalesBuget buget = new SkSalesBuget();
                buget.setSapId(vo.getSapId());
                buget.setCustCode(vo.getCustCode());
                buget.setMatnr(vo.getMatnr());
                buget.setPrdType(vo.getPrdType());
                String month = String.valueOf(i);
                if (month.length() == 1) {
                    month = "0".concat(month);
                }
                buget.setYearMonth(year.concat(month));
                switch (i) {
                    case 1: {
                        buget.setUnits(vo.getM1unit());
                        buget.setAmount(vo.getM1Amount());
                        create(buget);
                    }
                    break;
                    case 2: {
                        buget.setUnits(vo.getM2unit());
                        buget.setAmount(vo.getM2Amount());
                        create(buget);
                    }
                    break;
                    case 3: {
                        buget.setUnits(vo.getM3unit());
                        buget.setAmount(vo.getM3Amount());
                        create(buget);
                    }
                    break;
                    case 4: {
                        buget.setUnits(vo.getM4unit());
                        buget.setAmount(vo.getM4Amount());
                        create(buget);
                    }
                    break;
                    case 5: {
                        buget.setUnits(vo.getM5unit());
                        buget.setAmount(vo.getM5Amount());
                        create(buget);
                    }
                    break;
                    case 6: {
                        buget.setUnits(vo.getM6unit());
                        buget.setAmount(vo.getM6Amount());
                        create(buget);
                    }
                    break;
                    case 7: {
                        buget.setUnits(vo.getM7unit());
                        buget.setAmount(vo.getM7Amount());
                        create(buget);
                    }
                    break;
                    case 8: {
                        buget.setUnits(vo.getM8unit());
                        buget.setAmount(vo.getM8Amount());
                        create(buget);
                    }
                    break;
                    case 9: {
                        buget.setUnits(vo.getM9unit());
                        buget.setAmount(vo.getM9Amount());
                        create(buget);
                    }
                    break;
                    case 10: {
                        buget.setUnits(vo.getM10unit());
                        buget.setAmount(vo.getM10Amount());
                        create(buget);
                    }
                    break;
                    case 11: {
                        buget.setUnits(vo.getM11unit());
                        buget.setAmount(vo.getM11Amount());
                        create(buget);
                    }
                    break;
                    case 12: {
                        buget.setUnits(vo.getM12unit());
                        buget.setAmount(vo.getM12Amount());
                        create(buget);
                    }
                    break;
                }
            }
        }
    }

    public int deleteByBaseline(String year) {
        String month1 = year.concat("01");
        String month12 = year.concat("12");
        String sqlCommand = "delete from SK_SALES_BUGET t where t.YEAR_MONTH >=? and t.YEAR_MONTH<=?";
        Query query = getEntityManager().createNativeQuery(sqlCommand);
        query.setParameter(1, month1);
        query.setParameter(2, month12);
        int count = query.executeUpdate();
        //System.out.println("count=" + count);
        return count;
    }

    public int findByYear(String year) {
        String month1 = year.concat("01");
        String month12 = year.concat("12");
        String sqlCommand = "select t.ID  from SK_SALES_BUGET t where t.YEAR_MONTH >=? and t.YEAR_MONTH<=?";
        Query query = getEntityManager().createNativeQuery(sqlCommand);
        query.setParameter(1, month1);
        query.setParameter(2, month12);
        int count = query.executeUpdate();
        //System.out.println("count=" + count);
        return count;
    }
}
