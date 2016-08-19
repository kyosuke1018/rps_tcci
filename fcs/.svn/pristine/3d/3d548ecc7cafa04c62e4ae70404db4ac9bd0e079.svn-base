/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.facade.service;

import com.tcci.fcs.entity.FcRptConfig;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.enums.ReportSheetEnum;
import com.tcci.fcs.enums.RptConfigCategoryEnum;
import com.tcci.fcs.facade.FcRptConfigFacade;
import com.tcci.fcs.util.ExcelAccountItem;
import com.tcci.fcs.util.ExcelValidatorItem;
import com.tcci.fcs.util.ExcelValueItem;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class ReportConfigService {
    @EJB
    private FcRptConfigFacade rptConfigFacade;
    
    public List<ExcelValidatorItem> findValidatorItems(CompanyGroupEnum group){
        List<FcRptConfig> validatorList = rptConfigFacade.findByCompAndCategory(group, RptConfigCategoryEnum.VALIDATOR_ITEM);
        List<ExcelValidatorItem> validatorItems = new ArrayList<>();
        for(FcRptConfig validatorConfig : validatorList){
//            if(group.equals(CompanyGroupEnum.TCC)){
//                validatorItems.add(new ExcelValidatorItem(validatorConfig.getSheet().getName(), validatorConfig.getCellRange()));
//            }else{//CSR、CSRC_BVI需要SHEET CODE當作英文SHEET NAME
            //20160201 一律寫入sheet code
                validatorItems.add(new ExcelValidatorItem(validatorConfig.getSheet().getName(), validatorConfig.getSheet().getCode(), validatorConfig.getCellRange()));
//            }
        }
        return validatorItems;
    }
    
    public List<ExcelValueItem> findValueItems(CompanyGroupEnum group){
        //CSRC_BVI excel value 使用CSRC設定
//        if(group.equals(CompanyGroupEnum.CSRC_BVI)){
//            group = CompanyGroupEnum.CSRC;
//        }
        List<FcRptConfig> valueList = rptConfigFacade.findByCompAndCategory(group, RptConfigCategoryEnum.VALUE_ITEM);
        List<ExcelValueItem> valueItems = new ArrayList<>();
        List<FcRptConfig> accountList;
        for(FcRptConfig valueConfig : valueList){
            accountList = rptConfigFacade.findByCompAndCategoryAndSheet(group, RptConfigCategoryEnum.ACCOUNT_ITEM, valueConfig.getSheet());
            List<ExcelAccountItem> accountItems = new ArrayList<>();
            for(FcRptConfig accountConfig : accountList){
                accountItems.add(new ExcelAccountItem(accountConfig.getColCode(), accountConfig.getAccCode(), accountConfig.getAccDesc()));
            }
            valueItems.add(new ExcelValueItem(valueConfig.getSheet().getCode(), valueConfig.getSheet().getName(), valueConfig.getStartRow(), accountItems));
        }
        return valueItems;
    }
    
    public List<ExcelAccountItem> findAccountItems(CompanyGroupEnum group, ReportSheetEnum sheet){
        //CSRC_BVI excel value 使用CSRC設定
//        if(group.equals(CompanyGroupEnum.CSRC_BVI)){
//            group = CompanyGroupEnum.CSRC;
//        }
        List<FcRptConfig> accountList = rptConfigFacade.findByCompAndCategoryAndSheet(group, RptConfigCategoryEnum.ACCOUNT_ITEM, sheet);
        List<ExcelAccountItem> accountItems = new ArrayList<>();
        for(FcRptConfig accountConfig : accountList){
            if(group.equals(CompanyGroupEnum.TCC)){
                accountItems.add(new ExcelAccountItem(accountConfig.getColCode(), accountConfig.getAccCode(), accountConfig.getAccDesc()));
            }else{//CSRC CSRC_BVI 取得英文會科
                accountItems.add(new ExcelAccountItem(accountConfig.getColCode(), accountConfig.getAccCode(), accountConfig.getAccDesc(), accountConfig.getAccEdesc()));
            }
        }
        return accountItems;
    }
}
