package com.tcci.et.rfq.report.priceLog;

import com.tcci.et.rfq.report.comparisonList.*;
import com.tcci.et.rfq.report.*;
import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import com.tcci.et.model.rfq.QuotationVO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "priceLogController")
@ViewScoped
public class PriceLogController extends SessionAwareController implements Serializable {

    public static final long FUNC_OPTION = 52;
    public static final String DATATABLE_RESULT = "fmMain:dtResult";
    // 查詢條件
    private BaseCriteriaVO criteriaVO;

    @EJB
    private ComparisonListFacade comparisonListFacade;

    @EJB
    private PriceLogFacade priceLogFacade;

    // 結果
    private BaseLazyDataModel<QuotationVO> lazyModel;
    List<ComparisonListVenderlVo> comparisonListVenderlList;
    private List<RFQPrintDtlVo> filterResultList;

    @PostConstruct
    private void init() {

        criteriaVO = new BaseCriteriaVO();
        criteriaVO.setActive(Boolean.TRUE);

        doQuery();
    }

    public void doQuery() {

        this.comparisonListVenderlList = new ArrayList<>();
        comparisonListVenderlList = comparisonListFacade.getVenderList();

        List<QuotationVO> resList = new ArrayList<>();
        resList = priceLogFacade.getQuotation();
        lazyModel = new BaseLazyDataModel<>(resList);

        this.organizeData(resList, comparisonListVenderlList);
    }

    public void organizeData(List<QuotationVO> quotationList, List comparisonListVenderlList) {

        List<QuotationVO> resList = new ArrayList<>();

    }

    public void doReset() {
        if (lazyModel != null) {
            lazyModel.reset();
        }
        criteriaVO = new BaseCriteriaVO();
        criteriaVO.reset();

        resetDataTable();
    }

    public void resetDataTable() {
        JsfUtils.resetDataTable(DATATABLE_RESULT);
    }

    @Override
    public String getFuncTitle() {
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }

    public BaseLazyDataModel<QuotationVO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<QuotationVO> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<RFQPrintDtlVo> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<RFQPrintDtlVo> filterResultList) {
        this.filterResultList = filterResultList;
    }

    public List<ComparisonListVenderlVo> getComparisonListVenderlList() {
        return comparisonListVenderlList;
    }

    public void setComparisonListVenderlList(List<ComparisonListVenderlVo> comparisonListVenderlList) {
        this.comparisonListVenderlList = comparisonListVenderlList;
    }

}
