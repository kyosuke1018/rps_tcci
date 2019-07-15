package com.tcci.et.rfq.report.comparisonList;

import com.tcci.et.entity.EtRfqEkpo;
import java.util.List;
import javax.ejb.Stateless;
import com.tcci.cm.facade.AbstractFacade;
import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ComparisonListFacade extends AbstractFacade<EtRfqEkpo> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComparisonListFacade() {
        super(EtRfqEkpo.class);
    }

    public List<ComparisonListMaterialVo> getComparisonListMateriallList() {
        HashMap<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select * \n");
        sql.append("From ET_RFQ_EKPO \n");
        List<ComparisonListMaterialVo> list = this.selectBySql(ComparisonListMaterialVo.class, sql.toString(), params);
        return list;
    }

    public List<ComparisonListVenderlVo> getVenderList() {
        HashMap<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select * \n");
        sql.append("From ET_RFQ_VENDER \n");
        List<ComparisonListVenderlVo> list = this.selectBySql(ComparisonListVenderlVo.class, sql.toString(), params);
        return list;
    }

}
