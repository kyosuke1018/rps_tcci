/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.controller;

import com.tcci.fc.util.ListUtils;
import com.tcci.rpt.entity.ZtfiAfcsCsba;
import com.tcci.rpt.entity.ZtfiAfcsCsbu;
import com.tcci.rpt.facade.ZtfiAfcsCsbuFacade;
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
@ManagedBean(name = "queryCsbuController")
@ViewScoped
public class QueryCsbuController {
    private final static Logger logger = LoggerFactory.getLogger(QueryCsbuController.class);
    
    @EJB
    private ZtfiAfcsCsbuFacade ztfiAfcsCsbuFacade;
    
    private List<ZtfiAfcsCsbu> csbuList = new ArrayList<>();
    
    @PostConstruct
    protected void init() {
        
        this.loadData();
    }
    
    private void loadData(){
        csbuList = ztfiAfcsCsbuFacade.findAll();
    }

    public List<ZtfiAfcsCsbu> getCsbuList() {
        return csbuList;
    }
    

    
    
    
}
