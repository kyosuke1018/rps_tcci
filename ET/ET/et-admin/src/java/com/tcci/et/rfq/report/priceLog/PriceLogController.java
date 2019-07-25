package com.tcci.et.rfq.report.priceLog;

import com.tcci.et.rfq.report.comparisonList.*;
import com.tcci.et.rfq.report.*;
import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    List<QuotationVO> resList;
    private List<RFQPrintDtlVo> filterResultList;

    private PriceLogData priceLogData;

    @PostConstruct
    private void init() {
        criteriaVO = new BaseCriteriaVO();
        criteriaVO.setActive(Boolean.TRUE);

    }

    public void doQuery() {

        this.comparisonListVenderlList = new ArrayList<>();
        comparisonListVenderlList = comparisonListFacade.getVenderList();

        this.getDummydata();

        lazyModel = new BaseLazyDataModel<>(resList);

        this.organizeData();
    }

    public void getDummydata() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            this.resList = new ArrayList<>();
            QuotationVO quotationVO1 = new QuotationVO();
            quotationVO1.setQuotationTitle("開標日期");
            quotationVO1.setQuotationTitleDate(format.parse("2018-12-31"));

            QuotationVO quotationVO2 = new QuotationVO();
            quotationVO2.setQuotationTitle("第一次議價日期");
            quotationVO2.setQuotationTitleDate(format.parse("2019-01-31"));

            QuotationVO quotationVO3 = new QuotationVO();
            quotationVO3.setQuotationTitle("第二次議價日期");
            quotationVO3.setQuotationTitleDate(format.parse("2019-02-31"));

            QuotationVO quotationVO4 = new QuotationVO();
            quotationVO4.setQuotationTitle("最終議價結果");
            quotationVO4.setQuotationTitleDate(format.parse("2019-04-25"));

            this.resList.add(quotationVO1);
            this.resList.add(quotationVO2);
            this.resList.add(quotationVO3);
            this.resList.add(quotationVO4);
        } catch (Exception e) {
        }
    }

    public void organizeData() {
        Random rand = new Random();

        for (ComparisonListVenderlVo comparisonListVenderlVo : this.comparisonListVenderlList) {
            comparisonListVenderlVo.setTelNumber(String.valueOf(rand.nextInt(500000)));
            comparisonListVenderlVo.setMemberId(Long.valueOf(rand.nextInt(5000)));
        }

        this.priceLogData = new PriceLogData();
        priceLogData.setRfqNo("AABBCCDD");
        priceLogData.setTitle("whatsoever title");
        priceLogData.setFactory("TCC china");
        priceLogData.setIsFirstTIme("No");
        priceLogData.setBudget("999");

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

    public PriceLogData getPriceLogData() {
        return priceLogData;
    }

    public void setPriceLogData(PriceLogData priceLogData) {
        this.priceLogData = priceLogData;
    }

}
