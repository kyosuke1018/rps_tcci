package com.tcci.et.rfq.report;

import com.tcci.et.entity.EtRfqEkpo;
import java.util.List;
import javax.ejb.Stateless;
import com.tcci.cm.facade.AbstractFacade;
import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class RFQFacade extends AbstractFacade<EtRfqEkpo> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RFQFacade() {
        super(EtRfqEkpo.class);
    }

    public List<RFQPrintCtlVo> getRFQCtlAll() {
        HashMap<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select * \n");
        sql.append("From ET_RFQ_EKKO \n");
        List<RFQPrintCtlVo> list = this.selectBySql(RFQPrintCtlVo.class, sql.toString(), params);
        return list;
    }

    public List<RFQPrintDtlVo> getRFQDtlAll() {

        HashMap<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select * \n");
        sql.append("From ET_RFQ_EKPO \n");
        List<RFQPrintDtlVo> list = this.selectBySql(RFQPrintDtlVo.class, sql.toString(), params);
        return list;
    }

}
