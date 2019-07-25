package com.tcci.et.rfq.report.comparisonList;

import com.tcci.et.rfq.report.*;
import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "comparisonTableController")
@ViewScoped
public class ComparisonTableController extends SessionAwareController implements Serializable {

    public static final long FUNC_OPTION = 51;
    public static final String DATATABLE_RESULT = "fmMain:dtResult";
    // 查詢條件
    private BaseCriteriaVO criteriaVO;

    private int totalMean;
    private int totalMin;

    @EJB
    private ComparisonListFacade comparisonListFacade;

    // 結果
    private BaseLazyDataModel<ComparisonListMaterialVo> lazyModel;
    private List<ComparisonListMaterialVo> resList;
    List<ComparisonListVenderlVo> comparisonListVenderlList;

    private List<RFQPrintDtlVo> filterResultList;

    private int totalPoPrice;
    private int totalBudget;

    private List<Values> finalValues;

    @PostConstruct
    private void init() {
        criteriaVO = new BaseCriteriaVO();
        criteriaVO.setActive(Boolean.TRUE);
    }

    public void doQuery() {

        resList = new ArrayList<>();

        resList = comparisonListFacade.getComparisonListMateriallList();
        resList.remove(14);
        resList.remove(13);
        resList.remove(12);
        resList.remove(11);
        resList.remove(10);
        resList.remove(9);
        resList.remove(8);
        resList.remove(7);
        resList.remove(6);
        resList.remove(5);
        resList.remove(4);
        resList.remove(3);
        resList.remove(2);

        resList.get(0).setBudget(BigDecimal.valueOf(7200));
        resList.get(1).setBudget(BigDecimal.valueOf(6900));

        resList.get(0).setPoPrice(BigDecimal.valueOf(7200));
        resList.get(1).setPoPrice(BigDecimal.valueOf(7300));

        lazyModel = new BaseLazyDataModel<>(resList);
        this.comparisonListVenderlList = new ArrayList<>();
        comparisonListVenderlList = comparisonListFacade.getVenderList();
        this.doCalculation();
        this.doMean();
        this.doMin();
        this.doTotalValues();

    }

    public void doCalculation() {
        this.comparisonListVenderlList.remove(3);
        this.comparisonListVenderlList.remove(2);
        this.comparisonListVenderlList.remove(1);

        int totalPirce1 = 6900;
        int totalPirce2 = 7200;

        Values value1 = new Values();
        value1.setTotalPrice(totalPirce1);
        value1.setPerPrice(totalPirce1 / resList.get(0).getMenge().intValue());
        value1.setRank(1);
        value1.setPercentage(totalPirce1 * 100 / resList.get(0).getBudget().intValue());
        Values value2 = new Values();
        value2.setTotalPrice(totalPirce2);
        value2.setPerPrice(totalPirce2 / resList.get(0).getMenge().intValue());
        value2.setRank(2);
        value2.setPercentage(totalPirce2 * 100 / resList.get(0).getBudget().intValue());
        comparisonListVenderlList.get(0).setValues(value1);
        comparisonListVenderlList.get(1).setValues(value2);

        HashMap<String, Object> qq = new HashMap<>();
        qq.put(comparisonListVenderlList.get(0).getName1(), value1);
        qq.put(comparisonListVenderlList.get(1).getName1(), value2);
        this.resList.get(0).setFactorValue(qq);

        value1 = new Values();
        value1.setTotalPrice(8000);
        value1.setPerPrice(8000 / resList.get(1).getMenge().intValue());
        value1.setRank(1);
        value1.setPercentage(8000 * 100 / resList.get(1).getBudget().intValue());
        qq = new HashMap<>();
        qq.put(comparisonListVenderlList.get(0).getName1(), value1);
        qq.put(comparisonListVenderlList.get(1).getName1(), value2);
        this.resList.get(1).setFactorValue(qq);

        for (ComparisonListMaterialVo comparisonListMaterialVo : this.resList) {
            comparisonListMaterialVo.setPerPreis(comparisonListMaterialVo.getPoPrice().divide(comparisonListMaterialVo.getMenge()));
            comparisonListMaterialVo.setPerbudget(comparisonListMaterialVo.getBudget().divide(comparisonListMaterialVo.getMenge()));
        }
    }

    public void doTotalValues() {
        finalValues = new ArrayList<>();
        this.totalBudget = 0;
        this.totalPoPrice = 0;

        for (ComparisonListMaterialVo comparisonListMaterialVo : this.resList) {
            this.totalBudget += comparisonListMaterialVo.getBudget().intValue();
            this.totalPoPrice += comparisonListMaterialVo.getPoPrice().intValue();
        }

        for (ComparisonListVenderlVo comparisonListVenderlVo : this.comparisonListVenderlList) {
            int sum = 0;
            for (ComparisonListMaterialVo comparisonListMaterialVo : this.resList) {
                Values temp = (Values) comparisonListMaterialVo.getFactorValue().get(comparisonListVenderlVo.getName1());
                sum += temp.getTotalPrice();
            }
            comparisonListVenderlVo.getFinalValues().setTotalPrice(sum);
            comparisonListVenderlVo.getFinalValues().setPercentage(sum * 100 / totalBudget);
        }
    }

    public void doRecalculation(String input) {
        for (ComparisonListVenderlVo comparisonListVenderlVo : this.comparisonListVenderlList) {
            if (comparisonListVenderlVo.getLifnr().contains(input)) {
                comparisonListVenderlVo.setShow(comparisonListVenderlVo.isCheck());
            }
        }
        this.doMean();
        this.doMin();
        this.doTotalValues();
    }

    public void doMean() {
        if (this.comparisonListVenderlList.isEmpty()) {
            return;
        }
        totalMean = 0;
        for (ComparisonListMaterialVo comparisonListMaterialVo : this.resList) {
            int sum = 0;
            int count = 0;
            for (ComparisonListVenderlVo comparisonListVenderlVo : this.comparisonListVenderlList) {
                if (comparisonListVenderlVo.isShow()) {
                    Values temp = (Values) comparisonListMaterialVo.getFactorValue().get(comparisonListVenderlVo.getName1());
                    sum += temp.getTotalPrice();
                    count++;
                }
            }
            comparisonListMaterialVo.setMean(sum / count);
            totalMean += comparisonListMaterialVo.getMean();
            comparisonListMaterialVo.setPerMean(comparisonListMaterialVo.getMean() / comparisonListMaterialVo.getMenge().intValue());
        }
    }

    public void doMin() {
        if (this.comparisonListVenderlList.isEmpty()) {
            return;
        }
        int temp = Integer.MAX_VALUE;
        this.totalMin = 0;
        for (ComparisonListMaterialVo comparisonListMaterialVo : this.resList) {
            for (ComparisonListVenderlVo comparisonListVenderlVo : this.comparisonListVenderlList) {
                if (comparisonListVenderlVo.isShow()) {

                    Values tempValues = (Values) comparisonListMaterialVo.getFactorValue().get(comparisonListVenderlVo.getName1());
                    if (temp > tempValues.getTotalPrice()) {
                        temp = tempValues.getTotalPrice();
                    }
                }
            }
            comparisonListMaterialVo.setMin(temp);
            this.totalMin += comparisonListMaterialVo.getMin();
            comparisonListMaterialVo.setPerMin(comparisonListMaterialVo.getMin() / comparisonListMaterialVo.getMenge().intValue());
        }

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

    public int getTotalPoPirce() {
        return totalPoPrice;
    }

    public void setTotalPoPirce(int totalPoPrice) {
        this.totalPoPrice = totalPoPrice;
    }

    public int getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(int totalBudget) {
        this.totalBudget = totalBudget;
    }

    public List<Values> getFinalValues() {
        return finalValues;
    }

    public void setFinalValues(List<Values> finalValues) {
        this.finalValues = finalValues;
    }

    public int getTotalMean() {
        return totalMean;
    }

    public void setTotalMean(int totalMean) {
        this.totalMean = totalMean;
    }

    public int getTotalMin() {
        return totalMin;
    }

    public void setTotalMin(int totalMin) {
        this.totalMin = totalMin;
    }

}
