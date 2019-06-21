/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.controller.global;

import com.tcci.cm.enums.SapClientEnum;
import com.tcci.cm.facade.admin.CmCompanyFacade;
import com.tcci.cm.facade.admin.CmFactoryFacade;
import com.tcci.cm.model.admin.CmCompanyVO;
import com.tcci.cm.model.admin.CmFactoryVO;
import com.tcci.et.facade.rfq.RfqCommonFacade;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Peter.pan
 */
@ManagedBean(name = "selFactory")
@ViewScoped
public class CmFactoryController extends SessionAwareController implements Serializable {
    @EJB CmCompanyFacade companyFacade;
    @EJB CmFactoryFacade factoryFacade;
    @EJB RfqCommonFacade rfqCommonFacade;
    
    public List<CmCompanyVO> companys;
    public List<CmFactoryVO> factorys;
    public CmCompanyVO selCompany;
    public CmFactoryVO selFactory;
    public List<CmFactoryVO> selFactorys;
    public List<Long> selFactoryIds;
    
    public String selFactoryDesc;
    
    //public List<SelectItem> companyOps;
    //public List<SelectItem> factoryOps;
    
    public Long companyId;
    public Long companyOri;
    public Long factoryId;
    public Long factoryOri;

    public List<Long> filterCompanyIds;
    public List<Long> filterFactoryIds;   

    public SapClientEnum sapClientEnum;
    public boolean multiFactory;// 廠可多選
    
    @PostConstruct
    public void init(){
        logger.debug("init ...");
    }
    
    public void initConfig(List<Long> filterCompanyIds, List<Long> filterFactoryIds, Long companyId, Long factoryId, boolean needDef){
        // 過濾(權限)
        this.filterCompanyIds = new ArrayList<Long>();
        this.filterFactoryIds = new ArrayList<Long>();
        
        if( filterCompanyIds!=null ){
            this.filterCompanyIds.addAll(filterFactoryIds);
        }
        if( filterFactoryIds!=null ){
            this.filterFactoryIds.addAll(filterFactoryIds);
        }
        
        // 初始公司別選單
        initCompanyOps();

        // now selected company
        setNowCompany(companyId, needDef);
        
        // build company & factory options
        onChangeCompany(this.companyId, true);
        
        // now selected factory
        setNowFactory(factoryId, needDef);
    }

    /**
     * 初始公司別選單
     */
    public void initCompanyOps(){
        this.companys = new ArrayList<CmCompanyVO>();
        List<CmCompanyVO> companyAll = companyFacade.findAllCompanies();
        if( filterCompanyIds!=null ){
            for(CmCompanyVO company : companyAll){
                if( filterCompanyIds.contains(company.getId()) ){
                    this.companys.add(company);
                }
            }
        }else{
            this.companys.addAll(companyAll);
        }
        //companyOps = rfqCommonFacade.buildCompanyOptions(this.companys);
    }
    
    /**
     * 變更公司別 (回傳是否確認變更)
     * @param confirm 
     */
    public boolean onChangeCompany(Long companyId, boolean confirm){
        try{
            logger.debug("onChangeCompany companyId = "+companyId+", confirm = "+confirm);
            if( !confirm ){
                if( companyOri==null || companyOri.equals(companyId) ){
                    confirm = true;// 不需確認=已確認
                }
            }
            if( confirm ){// 確認變更
                this.companyId = companyId;
                this.companyOri = this.companyId;
                logger.debug("onChangeCompany companyOri="+companyOri+", companyId="+companyId);
                setSelCompany(rfqCommonFacade.getSelectedCompany(this.companys, this.companyId));

                // 初始廠別
                initFactoryOps(this.selCompany);
                this.selFactoryDesc = null;
            }
            
            return confirm;
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onChangeCompany", e, true);
            return false;
        }
    }
    
    public void onCancelCompany(){
        logger.debug("onCancelCompany companyId = "+companyId+", companyOri = "+companyOri);
        this.companyId = this.companyOri;
    }
    
    /**
     * 初始廠別
     */
    public void initFactoryOps(CmCompanyVO company){
        if( company==null ){
            logger.debug("initFactoryOps company==null");
            return;
        }

        this.factorys = new ArrayList<CmFactoryVO>();
        List<CmFactoryVO> factoryAll = factoryFacade.findVOByCompany(company.getId());
        if( filterFactoryIds!=null ){
            for(CmFactoryVO factory : factoryAll){
                if( filterFactoryIds.contains(factory.getId()) ){
                    this.factorys.add(factory);
                }
            }
        }else{
            this.factorys.addAll(factoryAll);
        }
        //factoryOps = rfqCommonFacade.buildFactoryOptions(this.factorys);
    }

    /**
     * 設定公司別
     * @param companyId
     * @param needDef 
     */
    public void setNowCompany(Long companyId, boolean needDef){
        if( this.companys!=null ){
            if( companyId!=null ){
                setSelCompany(rfqCommonFacade.getSelectedCompany(this.companys, companyId));
            }else if( needDef ){
                if( this.companys!=null ){
                    setSelCompany(this.companys.get(0));
                }
            }
        }
        
        if( this.selCompany!=null ){
            this.companyId = this.selCompany.getId();
            this.companyOri = this.companyId;
        }else{
            this.companyId = null;
            this.companyOri = null;
        }
    }

    /**
     * 設定廠別
     * @param factoryId
     * @param needDef 
     */
    public void setNowFactory(Long factoryId, boolean needDef){
        if( this.factorys!=null ){
            if( factoryId!=null ){
                this.selFactory = rfqCommonFacade.getSelectedFactory(this.factorys, factoryId);
            }else if( needDef ){
                if( this.factorys!=null ){
                    this.selFactory = this.factorys.get(0);
                }
            }
        }
        
        if( this.selFactory!=null ){
            this.factoryId = this.selFactory.getId();
            this.factoryOri = this.factoryId;
        }else{
            this.factoryId = null;
            this.factoryOri = null;
        }
    }

    /**
     * 變更廠別 (回傳是否確認變更) -- for 單選 only
     * @param confirm 
     */
    public boolean onChangeFactory(Long factoryId, boolean confirm){
        try{
            logger.debug("onChangeFactory factoryId = "+factoryId);
            if( !confirm ){
                if( factoryOri==null || factoryOri.equals(factoryId) ){
                    confirm = true;// 不需確認=已確認
                }
            }
            if( confirm ){
                this.factoryId = factoryId;
                this.factoryOri = this.factoryId;
                logger.debug("onChangeFactory factoryOri="+factoryOri+", factoryId="+factoryId);
                this.selFactory = rfqCommonFacade.getSelectedFactory(this.factorys, this.factoryId);
            }
            
            return confirm;
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onChangeFactory", e, true);
            return false;
        }
    }

    /**
     * 變更廠別 (回傳是否確認變更) -- for 多選 only
     */
    public void setMultiFactorys() {
        selFactorys = new ArrayList<CmFactoryVO>();
        selFactoryIds = new ArrayList<Long>();
        
        if( factorys!=null ){
            for(CmFactoryVO factory : factorys){
                if( factory.isSelected() ){
                    selFactorys.add(factory);
                    selFactoryIds.add(factory.getId());
                }
            }
        }
        
        genSelFactoryDesc();// 選取廠別敘述
    }

    /**
     * 選取廠別敘述 -- for 多選 only
     */
    public void genSelFactoryDesc(){
        if( sys.isEmpty(selFactorys) ){
            this.selFactoryDesc = null;
        }else{
            StringBuilder sb = new StringBuilder();
            sb.append(MessageFormat.format("已選取 {0} 個廠", selFactorys.size()));
            if( selFactorys.size()<=3 ){
                sb.append(":");
                boolean first = true;
                for(CmFactoryVO vo : selFactorys){
                    if( first ){
                        sb.append(vo.getDisplayLabel());
                        first = false;
                    }else{
                        sb.append("、").append(vo.getDisplayLabel());
                    }
                }
            }
            this.selFactoryDesc = sb.toString();
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public List<CmCompanyVO> getCompanys() {
        return companys;
    }

    public void setCompanys(List<CmCompanyVO> companys) {
        this.companys = companys;
    }

    public SapClientEnum getSapClientEnum() {
        return sapClientEnum;
    }

    public void setSapClientEnum(SapClientEnum sapClientEnum) {
        this.sapClientEnum = sapClientEnum;
    }

    public List<CmFactoryVO> getFactorys() {
        return factorys;
    }

    public void setFactorys(List<CmFactoryVO> factorys) {
        this.factorys = factorys;
    }

    public CmCompanyVO getSelCompany() {
        return selCompany;
    }

    public void setSelCompany(CmCompanyVO selCompany) {
        this.selCompany = selCompany;
        if( selCompany!=null ){
            this.sapClientEnum = SapClientEnum.getFromSapClientCode(selCompany.getSapClientCode());
            this.multiFactory = sapClientEnum.isMultiPlantRfq();
        }else{
            this.sapClientEnum = null;
            this.multiFactory = false;
        }
    }

    public CmFactoryVO getSelFactory() {
        return selFactory;
    }

    public void setSelFactory(CmFactoryVO selFactory) {
        this.selFactory = selFactory;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCompanyOri() {
        return companyOri;
    }

    public void setCompanyOri(Long companyOri) {
        this.companyOri = companyOri;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public Long getFactoryOri() {
        return factoryOri;
    }

    public void setFactoryOri(Long factoryOri) {
        this.factoryOri = factoryOri;
    }

    public List<Long> getFilterCompanyIds() {
        return filterCompanyIds;
    }

    public void setFilterCompanyIds(List<Long> filterCompanyIds) {
        this.filterCompanyIds = filterCompanyIds;
    }

    public List<CmFactoryVO> getSelFactorys() {
        return selFactorys;
    }

    public void setSelFactorys(List<CmFactoryVO> selFactorys) {
        this.selFactorys = selFactorys;
    }

    public List<Long> getSelFactoryIds() {
        return selFactoryIds;
    }

    public void setSelFactoryIds(List<Long> selFactoryIds) {
        this.selFactoryIds = selFactoryIds;
    }

    public String getSelFactoryDesc() {
        return selFactoryDesc;
    }

    public void setSelFactoryDesc(String selFactoryDesc) {
        this.selFactoryDesc = selFactoryDesc;
    }

    public boolean isMultiFactory() {
        return multiFactory;
    }

    public void setMultiFactory(boolean multiFactory) {
        this.multiFactory = multiFactory;
    }

    public List<Long> getFilterFactoryIds() {
        return filterFactoryIds;
    }

    public void setFilterFactoryIds(List<Long> filterFactoryIds) {
        this.filterFactoryIds = filterFactoryIds;
    }
    //</editor-fold>
}
