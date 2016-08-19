/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.controller.sheetdata;

import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.irs.exception.ReclException;
import com.tcci.irs.facade.ReclMgmtFacade;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author David.Jen
 */
@ManagedBean(name = "sheetDataGenerator")
@ViewScoped
public class SheetDataGenerator {
    //
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //
    @EJB
    private ReclMgmtFacade reclMgmtFacade;
    //
    private int reclYear;
    private int reclMonth;
    private FcCompany reportEntity;
    private FcCompany counterpart;
    //

    public int getReclYear() {
        return reclYear;
    }

    public void setReclYear(int reclYear) {
        this.reclYear = reclYear;
    }

    public int getReclMonth() {
        return reclMonth;
    }

    public void setReclMonth(int reclMonth) {
        this.reclMonth = reclMonth;
    }

    public FcCompany getReportEntity() {
        return reportEntity;
    }

    public void setReportEntity(FcCompany reportEntity) {
        this.reportEntity = reportEntity;
    }

    public FcCompany getCounterpart() {
        return counterpart;
    }

    public void setCounterpart(FcCompany counterpart) {
        this.counterpart = counterpart;
    }
    
    public void generate(){
        //
        logger.debug("year: " + reclYear + ", month: " + reclMonth + 
                ", reportEntity: " + reportEntity + ", counterpart: " + counterpart);
        RequestContext context = RequestContext.getCurrentInstance();
        long startTime = System.currentTimeMillis();
        try {
            reclMgmtFacade.generateRecociling(reclYear, reclMonth, reportEntity, counterpart, true);
            
            long excTime = System.currentTimeMillis() - startTime;
            logger.debug("sheetDataGenerator generate execute sucess time:["+excTime+"]");
            JsfUtil.addSuccessMessage("執行完畢! execute time[ms]:["+excTime+"]");
        } catch (ReclException ex) {
            ex.printStackTrace();
            //java.util.logging.Logger.getLogger(SheetDataGenerator.class.getName()).log(Level.SEVERE, null, ex);
            long excTime = System.currentTimeMillis() - startTime;
            logger.debug("sheetDataGenerator generate execute fail time:["+excTime+"]");
        }
    }
}
