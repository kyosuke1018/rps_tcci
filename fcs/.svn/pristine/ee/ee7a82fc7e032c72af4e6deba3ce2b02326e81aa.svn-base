/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.controller;

import com.tcci.fc.util.ListUtils;
import com.tcci.rpt.entity.ZtfiAfcsCsba;
import com.tcci.rpt.facade.ZtfiAfcsCsbaFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "queryCsbaController")
@ViewScoped
public class QueryCsbaController {
    private final static Logger logger = LoggerFactory.getLogger(QueryCsbaController.class);
    
    @EJB
    private ZtfiAfcsCsbaFacade ztfiAfcsCsbaFacade;
    
    private List<SelectItem> ymOptions;
    private String quoteYM;
    private List<ZtfiAfcsCsba> csbaList = new ArrayList<>();
    
    @PostConstruct
    protected void init() {
        
        this.bulidYM();
        this.loadData();
    }
    
    private void bulidYM(){
        List<String> ymList = ztfiAfcsCsbaFacade.findDataYMList();//依data產生YM選單
        ymOptions = ListUtils.getOptions(ymList);
        if (CollectionUtils.isNotEmpty(ymList)) {
            quoteYM = ymList.get(0);
        }
    }
    
    public void loadData(){
        csbaList = ztfiAfcsCsbaFacade.findAllByYM(quoteYM);
    }
    
    public void changeYm() {
        logger.debug("changeYm:" + this.quoteYM);
        this.loadData();
    }

    public List<SelectItem> getYmOptions() {
        return ymOptions;
    }

    public String getQuoteYM() {
        return quoteYM;
    }

    public void setQuoteYM(String quoteYM) {
        this.quoteYM = quoteYM;
    }

    public List<ZtfiAfcsCsba> getCsbaList() {
        return csbaList;
    }
    
    
    
}
