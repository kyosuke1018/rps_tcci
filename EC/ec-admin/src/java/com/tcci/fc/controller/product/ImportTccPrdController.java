/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.fc.controller.product;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.JsfUtils;
import com.tcci.ec.entity.EcTccProduct;
import com.tcci.ec.enums.ProductStatusEnum;
import com.tcci.ec.facade.EcStoreFacade;
import com.tcci.ec.facade.EcTccProductFacade;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.e10.ProductE10VO;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.Query;

/**
 *
 * @author Peter.pan
 */
@ManagedBean(name = "impTccPrd")
@ViewScoped
public class ImportTccPrdController extends SessionAwareController implements Serializable {
    private static final long FUNC_OPTION = 32;
    private static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    private @EJB EcTccProductFacade tccProductFacade;// 
    private @EJB EcStoreFacade storeFacade;
    //private List<SelectItem> sysOptions; // 巡檢系統別
    
    // 查詢結果
    private List<ProductE10VO> resultList;
    private BaseLazyDataModel<ProductE10VO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<ProductE10VO> filterResultList;
    
    @PostConstruct
    private void init() {
        // 權限檢核
        if( this.functionDenied ){
            logger.debug(this.getClass().getSimpleName()+" init functionDenied = "+functionDenied);
            return;
        }
        doQuery();
    }
    
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }  
    
    public boolean checkParams(BaseCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            // 未輸入查詢條件!
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("msg.txt001"));
            return false;
        }
        return true;
    }
    
    /**
     * 查詢
     */
    public void doQuery(){
        logger.debug("doQuery ...");
        try {
            // 移除 datatable 目前排序、filter 效果
            JsfUtils.resetDataTable(DATATABLE_RESULT);
            filterResultList = null; // filterValue 初始化
            resultList = tccProductFacade.findProductForImport();
            
            lazyModel = new BaseLazyDataModel<ProductE10VO>(resultList);
        } catch (Exception e) {
            sys.processUnknowException(this.getLoginUser(), "doQuery", e);
        }
    }
    
    /**
     * 清除
     */
    public void doReset() {
        logger.debug("doReset ...");
        try {
            // 移除 datatable 目前排序、filter 效果
            JsfUtils.resetDataTable(DATATABLE_RESULT);
            filterResultList = null; // filterValue 初始化
            resultList = null;
            lazyModel = new BaseLazyDataModel<ProductE10VO>();
        } catch (Exception e) {
            sys.processUnknowException(this.getLoginUser(), "doReset", e);
        }
    }
    
    /**
     * 上、下架
     * @param vo 
     */
    public void doUpDown(ProductE10VO vo){
        logger.debug("doUpDown vo = "+vo.getCode()+", "+vo.isExisted());
        
        List<EcTccProduct> list = tccProductFacade.findByCodeOnly(vo.getCode());
        
        if( sys.isEmpty(list) ){
            if( vo.isExisted() ){
                EcTccProduct entity = new  EcTccProduct();
                entity.setCode(vo.getCode());
                entity.setName(vo.getName());
                entity.setActive(true);
                tccProductFacade.save(entity, this.getLoginMember(), false);
            }
        }else{
            for(EcTccProduct entity : list){
                entity.setActive(vo.isExisted());
                tccProductFacade.save(entity, this.getLoginMember(), false);
            }
            
            // 台泥下架，其他商店也需下架
            if( !vo.isExisted() ){
                tccProductFacade.disabledByTccProduct(vo.getCode());
            }
        }
        
        JsfUtils.buildSuccessCallback();
    }

    //<editor-fold defaultstate="collapsed" desc="getter & setter">
    public EcTccProductFacade getTccProductFacade() {
        return tccProductFacade;
    }

    public void setTccProductFacade(EcTccProductFacade tccProductFacade) {
        this.tccProductFacade = tccProductFacade;
    }

    public EcStoreFacade getStoreFacade() {
        return storeFacade;
    }

    public void setStoreFacade(EcStoreFacade storeFacade) {
        this.storeFacade = storeFacade;
    }

    public List<ProductE10VO> getResultList() {
        return resultList;
    }

    public void setResultList(List<ProductE10VO> resultList) {
        this.resultList = resultList;
    }

    public BaseLazyDataModel<ProductE10VO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<ProductE10VO> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<ProductE10VO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<ProductE10VO> filterResultList) {
        this.filterResultList = filterResultList;
    }
    //</editor-fold>
}
