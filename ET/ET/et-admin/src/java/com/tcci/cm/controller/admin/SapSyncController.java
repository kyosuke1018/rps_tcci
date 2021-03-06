/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.controller.admin;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.enums.SapClientEnum;
import com.tcci.cm.facade.admin.CmFactoryFacade;
import com.tcci.cm.model.global.StrOptionVO;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.facade.jco.JCoClientFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author peter.pan
 */
@ManagedBean(name = "sap")
@ViewScoped
public class SapSyncController extends SessionAwareController implements Serializable {
    private static final long FUNC_OPTION = 15;
    
    @EJB JCoClientFacade jco;
    @EJB CmFactoryFacade factoryFacade;   

    private String sapClientCode;
    
    List<SelectItem> werksOps = new ArrayList<SelectItem>();
    List<StrOptionVO> bukrsOps = new ArrayList<StrOptionVO>();
    
    Map<String, String> werksMap = new HashMap<String, String>();
    Map<String, String> bukrsMap = new HashMap<String, String>();
    Map<String, String> sprasMap = new HashMap<String, String>();

    @PostConstruct
    private void init(){
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        // Get view Id
        viewId = JsfUtils.getViewId();
        logger.debug("SapSyncController viewId = "+viewId);
        
        // default
        sapClientCode = SapClientEnum.SAP_CN.getSapClientCode();
        // 選項
        bukrsOps = factoryFacade.findToSapBukrs(sapClientCode);// 公司別
        
        // 廠別條件
        werksMap.put("T024", null);// 採購群組
        // 公司別條件
        bukrsMap.put("T024E", null);// 採購組織
        // 語言別條件
        sprasMap.put("T052U", null);// 付款方式

        // for TEST 
        //jco.syncPayCond(SapClientEnum.SAP_CN.getSapClientCode(), "1", this.getLoginUser());
    }
    
    public void onChangeSapClient(){
        logger.info("onChangeSapClient ... "+this.sapClientCode);
    }
    
    /**
     * Sync Sap Data To DB
     * @param code 
     */
    public void syncSapData(String code){
        logger.info("syncSapData ... code = "+code+", user = "+this.getLoginUserCode());
        try{
            String werks = werksMap.get(code);
            String bukrs = bukrsMap.get(code);
            String spras = sprasMap.get(code);
            logger.debug("syncSapData sapClientCode = "+sapClientCode+", werks = "+werks+", bukrs = "+bukrs+", spras = "+spras);

            if( "T024".equals(code) ){// 採購群組
                jco.syncPurGroup(sapClientCode, werks, this.getLoginUser());
            }else if( "T024E".equals(code) ){// 採購組織
                jco.syncPurOrg(sapClientCode, bukrs, this.getLoginUser());
            }else if( "T052U".equals(code) ){// 付款方式
                jco.syncPurOrg(sapClientCode, spras, this.getLoginUser());
            }
            
            JsfUtils.addSuccessMessage(code+" 同步完成!");
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "doSaveBargain", e, true);
        }
    }
    
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }  
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public List<SelectItem> getWerksOps() {
        return werksOps;
    }

    public void setWerksOps(List<SelectItem> werksOps) {
        this.werksOps = werksOps;
    }

    public String getSapClientCode() {
        return sapClientCode;
    }

    public void setSapClientCode(String sapClientCode) {
        this.sapClientCode = sapClientCode;
    }

    public List<StrOptionVO> getBukrsOps() {
        return bukrsOps;
    }

    public void setBukrsOps(List<StrOptionVO> bukrsOps) {
        this.bukrsOps = bukrsOps;
    }

    public Map<String, String> getWerksMap() {
        return werksMap;
    }

    public void setWerksMap(Map<String, String> werksMap) {
        this.werksMap = werksMap;
    }

    public Map<String, String> getBukrsMap() {
        return bukrsMap;
    }

    public void setBukrsMap(Map<String, String> bukrsMap) {
        this.bukrsMap = bukrsMap;
    }

    public Map<String, String> getSprasMap() {
        return sprasMap;
    }

    public void setSprasMap(Map<String, String> sprasMap) {
        this.sprasMap = sprasMap;
    }
    //</editor-fold>
}
