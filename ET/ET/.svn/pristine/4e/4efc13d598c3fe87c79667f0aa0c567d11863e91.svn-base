package com.tcci.et.rfq.report.priceLog;

import com.tcci.et.rfq.report.comparisonList.*;
import com.tcci.et.entity.EtRfqEkpo;
import java.util.List;
import javax.ejb.Stateless;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.et.model.rfq.QuotationVO;
import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PriceLogFacade extends AbstractFacade<EtRfqEkpo> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PriceLogFacade() {
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

    public List<QuotationVO> getQuotation() {
        StringBuilder sql = new StringBuilder();
        sql.append("select * \n");
        sql.append("From ET_QUOTATION \n");
        List<QuotationVO> list = this.selectBySql(QuotationVO.class, sql.toString(), new HashMap<>());
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
