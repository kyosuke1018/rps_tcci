/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.datawarehouse;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class WBDRFacade {

    @PersistenceContext(unitName = "DatawarehousePU")
    private EntityManager em;

    public boolean isBatchExist(String lotno, String areacode) {
        if (lotno != null) {
            lotno = lotno.toLowerCase();
        }
        if (areacode != null) {
            areacode = areacode.toLowerCase();
        }
        /*
        String sql = "SELECT 1 FROM WB_DR dr, WB_DO do"
                + " WHERE dr.DONO=do.DONO"
                + " AND LOWER(dr.LOTNO)=#lotno"
                + " AND LOWER(do.AREACODE)=#areacode";
        */
        String sql = "SELECT 1 FROM ST_LOTNO"
                + " WHERE LOWER(LOTNO)=#lotno"
                + " AND LOWER(AREACODE)=#areacode";
        Query q = em.createNativeQuery(sql);
        List list = q.setParameter("lotno", lotno)
                .setParameter("areacode", areacode)
                .setMaxResults(1)
                .getResultList();
        return !list.isEmpty();
    }

}
