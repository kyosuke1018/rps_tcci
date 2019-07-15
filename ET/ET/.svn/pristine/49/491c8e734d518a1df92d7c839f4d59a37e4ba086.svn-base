package com.tcci.et.rfq.report.priceLog;

import com.tcci.et.rfq.report.comparisonList.*;
import com.tcci.et.rfq.report.*;
import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.model.criteria.BaseCriteriaVO;
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
//    private List<SelectItem> factoryOptions;

    @EJB
    private ComparisonListFacade comparisonListFacade;

    // 結果
    private BaseLazyDataModel<ComparisonListMaterialVo> lazyModel;
    List<ComparisonListVenderlVo> comparisonListVenderlList;
    private List<RFQPrintDtlVo> filterResultList; // datatable filter 後的結果

    @PostConstruct
    private void init() {

        criteriaVO = new BaseCriteriaVO();
        criteriaVO.setActive(Boolean.TRUE);

        doQuery();
    }

    public void doQuery() {

        List<ComparisonListMaterialVo> resList = new ArrayList<>();
        resList = comparisonListFacade.getComparisonListMateriallList();
        lazyModel = new BaseLazyDataModel<>(resList);

        this.comparisonListVenderlList = new ArrayList<>();
        comparisonListVenderlList = comparisonListFacade.getVenderList();

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

    public BaseLazyDataModel<ComparisonListMaterialVo> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<ComparisonListMaterialVo> lazyModel) {
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
