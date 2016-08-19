/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.controller;

import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fcs.facade.schedule.FcsScheduleFacade;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name="runBatchController")
@ViewScoped
public class RunBatchController {
    private final static Logger logger = LoggerFactory.getLogger(RunBatchController.class);
    @EJB
    private FcsScheduleFacade fcsScheduleFacade;
    
    @PostConstruct
    private void init() {
        
    }
    
    //更新SAP上傳時間
    public void updateSapUpload(boolean thisSeason){
        long startTime = System.currentTimeMillis();
        try {
            fcsScheduleFacade.updateUploadRecord(thisSeason);
            
            long excTime = System.currentTimeMillis() - startTime;
            logger.debug("updateSapUpload execute sucess time:["+excTime+"]");
            if(excTime>5000){
                JsfUtil.addSuccessMessage("執行完畢! execute time[ms]:["+excTime+"]");
            }else{
                JsfUtil.addSuccessMessage("執行完畢!");
            }
        } catch (Exception ex) {
//            ex.printStackTrace();
            long excTime = System.currentTimeMillis() - startTime;
            logger.debug("updateSapUpload execute fail time:["+excTime+"]");
            JsfUtil.addErrorMessage("執行失敗! execute time[ms]:["+excTime+"] Exception:"+ex);
        }
    }
}
