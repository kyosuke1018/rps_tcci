/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.SkProductUpload;
import com.tcci.sksp.vo.ProductUploadVO;
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
public class SkProductUploadFacade extends AbstractFacade<SkProductUpload> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SkProductUploadFacade() {
        super(SkProductUpload.class);
    }

    public void save(List<ProductUploadVO> list, String year) {
        int count = findByYear(year);
        if (count > 0) {
            deleteByBaseline(year);
        }
        
         for (ProductUploadVO vo : list) {
             SkProductUpload pd= new SkProductUpload();
             pd.setMatnr(vo.getMatnr());
             pd.setYearMonth(vo.getYearMonth());
             pd.setUnit(vo.getUnit());
             create(pd);             
         }
    }

    public int findByYear(String year) {
        String month1 = year.concat("01");
        String month12 = year.concat("12");
        String sqlCommand = "select t.ID  from SK_PRODUCT_UPLOAD t where t.YEAR_MONTH >=? and t.YEAR_MONTH<=?";
        Query query = getEntityManager().createNativeQuery(sqlCommand);
        query.setParameter(1, month1);
        query.setParameter(2, month12);
        int count = query.executeUpdate();
        return count;
    }
    
    public int deleteByBaseline(String year) {
        String month1 = year.concat("01");
        String month12 = year.concat("12");
        String sqlCommand = "delete from SK_PRODUCT_UPLOAD t where t.YEAR_MONTH >=? and t.YEAR_MONTH<=?";
        Query query = getEntityManager().createNativeQuery(sqlCommand);
        query.setParameter(1, month1);
        query.setParameter(2, month12);
        int count = query.executeUpdate();
        return count;
    }

} // end of