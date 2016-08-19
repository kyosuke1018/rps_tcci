/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.datawarehouse;

import com.tcci.tccstore.entity.datawarehouse.Kna1VO;
import com.tcci.tccstore.entity.datawarehouse.PernrVO;
import com.tcci.tccstore.entity.datawarehouse.ZperCnVO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Named
@Stateless
public class ZperCnFacade {

    @PersistenceContext(unitName = "DatawarehousePU")
    private EntityManager em;

    public List<Kna1VO> findAllCustomer() {
        List<Kna1VO> result = new ArrayList<>();
        String sql = "SELECT SUBSTR(KUNNR, 6) AS CODE, NAME1 AS NAME"
                + " FROM TC_ZTAB_EXP_KNA1"
                + " WHERE MANDT='268' AND KUNNR LIKE '00000%'";
        Query q = em.createNativeQuery(sql);
        List list = q.getResultList();
        for (Object row : list) {
            Object[] columns = (Object[]) row;
            int idx = 0;
            String code = (String) columns[idx++];
            String name = (String) columns[idx++];
            Kna1VO vo = new Kna1VO(code, name);
            result.add(vo);
        }
        return result;
    }

    public List<PernrVO> findAllSales() {
        List<PernrVO> result = new ArrayList<>();
        String sql = "SELECT DISTINCT PERNR AS CODE, SNAME AS NAME,"
                + " CASE"
                + " WHEN INSTR(SNAME, '离职')=0 THEN 1"
                + " ELSE 0"
                + " END AS ACTIVE"
                + " FROM ZPER_CN"
                + " WHERE PERNR<>'20000000'";
        Query q = em.createNativeQuery(sql);
        List list = q.getResultList();
        for (Object row : list) {
            Object[] columns = (Object[]) row;
            int idx = 0;
            String code = (String) columns[idx++];
            String name = (String) columns[idx++];
            boolean active = ((BigDecimal) columns[idx++]).equals(BigDecimal.ONE);
            PernrVO vo = new PernrVO(code, name, active);
            result.add(vo);
        }
        return result;
    }

    public List<ZperCnVO> findAll() {
        List<ZperCnVO> result = new ArrayList<>();
        String sql = "SELECT KUNNR, PERNR, SNAME"
                + " FROM ZPER_CN"
                + " WHERE PERNR<>'20000000'";
        Query q = em.createNativeQuery(sql);
        List list = q.getResultList();
        for (Object row : list) {
            Object[] columns = (Object[]) row;
            int idx = 0;
            String kunnr = (String) columns[idx++]; // customer code
            String pernr = (String) columns[idx++]; // sales code
            ZperCnVO vo = new ZperCnVO(kunnr, pernr);
            result.add(vo);
        }
        return result;
    }

}
