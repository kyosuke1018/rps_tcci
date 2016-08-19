/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.global;


import com.tcci.fc.controller.BaseController;
import com.tcci.fc.util.ExcelUtils;
import com.tcci.irs.enums.SheetTypeEnum;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


/**
 *
 * @author kyle.cheng
 */
@ManagedBean(name = "commonUIController")
@ViewScoped
public class CommonUIController extends BaseController {

    
    //<editor-fold defaultstate="collapsed" desc="EJB">

  
    
    
    //
//    @ManagedProperty(value = "#{userSession}")
//    protected UserSession userSession;
//    public void setUserSession(UserSession userSession) {
//        this.userSession = userSession;
//    }

    //</editor-fold>
    
    private List<SelectItem> companyOptions;//公司
    
    @PostConstruct
    @Override
    protected void init(){
        
//        companyOptions = buildCompnayOptions();
    }
    

    
    public String genFileName(String templateName) {
        String extensionName = "";
        return ExcelUtils.genFileName(templateName, extensionName);
    }    
    public SheetTypeEnum getSheetTypeEnum(String sheetType){
//        String result = "";
        SheetTypeEnum thistEnum = SheetTypeEnum.getByValue(sheetType);
//        if(null != thistEnum){
//            result = thistEnum.toString();
//        }
        return thistEnum;
    }
}
