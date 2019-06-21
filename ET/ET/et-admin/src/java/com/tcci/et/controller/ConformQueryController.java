package com.tcci.et.controller;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExceptionHandlerUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.model.TenderConformVO;
import com.tcci.et.facade.EtTenderConformFacade;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;

@ManagedBean(name = "conformQueryController")
public class ConformQueryController extends SessionAwareController implements Serializable {

    public static final long FUNC_OPTION = 33;
    public static final String DATATABLE_RESULT = "fmMain:dtResult";
    // 查詢條件
    private BaseCriteriaVO criteriaVO;
    private List<SelectItem> factoryOptions;

    @EJB
    private EtTenderConformFacade etTenderConformFacade;

    // 結果
    private BaseLazyDataModel<TenderConformVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<TenderConformVO> filterResultList; // datatable filter 後的結果
    private boolean isAdmin;
    private List<CmFactory> owenerfactoryList;

    @PostConstruct
    private void init() {

        criteriaVO = new BaseCriteriaVO();
        criteriaVO.setActive(Boolean.TRUE);
        getInputParams();// 取得輸入參數

        isAdmin = sessionController.isUserInRole("ADMINISTRATORS");
        factoryOptions = buildFactoryOptions();

        doQuery();
    }
    
    /**
     * 取得輸入參數
     */
    private void getInputParams() {
        String idStr = JsfUtils.getRequestParameter("tenderId");
        if (idStr != null) {
            try {
                Long tenderId = Long.parseLong(idStr);
                criteriaVO.setTenderId(tenderId);
            } catch (Exception e) {
                logger.error("getInputParams exception:\n", e);
            }
        }
    }

    public void doQuery() {

        if (!doCheck()) {
            return;
        }
        resetDataTable();
        criteriaVO.setSetMaxResultsSize(GlobalConstant.DEF_MAX_RESULT_SIZE); //設定最大回傳筆數
        try {
            List<TenderConformVO> resList = etTenderConformFacade.findByCriteria(criteriaVO);
            lazyModel = new BaseLazyDataModel<>(resList);
        } catch (Exception e) {
            String msg = ExceptionHandlerUtils.getSimpleMessage("查詢失敗:", e);
            logger.error(msg);
            JsfUtils.addErrorMessage(msg);
        }
    }

    public boolean doCheck() {
        if (criteriaVO.getKeyword() != null) {
            criteriaVO.setKeyword(criteriaVO.getKeyword().trim());
        }
        return true;
    }

    public void resetDataTable() {
        JsfUtils.resetDataTable(DATATABLE_RESULT);
    }

    public void doReset() {
        if (lazyModel != null) {
            lazyModel.reset();
        }
        criteriaVO = new BaseCriteriaVO();
        criteriaVO.reset();

        resetDataTable();
    }

    List<SelectItem> buildFactoryOptions() {
        List<SelectItem> options = new ArrayList<>();
        owenerfactoryList = new ArrayList<>();
        if (this.isAdmin) {
            owenerfactoryList = cmUserfactoryFacade.findAllFactories();
        } else {
            owenerfactoryList = cmUserfactoryFacade.findUserFactoryPermission(this.getLoginUser());
            if (CollectionUtils.isNotEmpty(owenerfactoryList)) {
                logger.debug("owenerfactoryList :" + owenerfactoryList.size());
            }
        }
        logger.debug("buildFactoryOptions owenerfactoryList:" + owenerfactoryList.size());

        // 查詢工廠選單
        List<CmFactory> result = cmFactoryFacade.findByAreaCode(owenerfactoryList, null, null);
        if (result != null) {
            logger.debug("buildFactoryOptions options:" + result.size());
            for (CmFactory g : result) {
                options.add(new SelectItem(g.getId(), g.getCode() + "-" + g.getName()));
            }
        }

        return options;
    }

    @Override
    public String getFuncTitle() {
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }

    public BaseLazyDataModel<TenderConformVO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<TenderConformVO> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<TenderConformVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<TenderConformVO> filterResultList) {
        this.filterResultList = filterResultList;
    }

    public BaseCriteriaVO getCriteriaVO() {
        return criteriaVO;
    }

    public List<SelectItem> getFactoryOptions() {
        return factoryOptions;
    }

}
