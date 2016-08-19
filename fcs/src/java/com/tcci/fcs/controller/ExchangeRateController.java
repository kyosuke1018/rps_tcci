/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.controller;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fcs.entity.FcCurrency;
import com.tcci.fcs.entity.FcMonthlyExchangeRate;
import com.tcci.fcs.entity.FcReportA0102;
import com.tcci.fcs.entity.FcReportValue;
import com.tcci.fcs.enums.CurrencyEnum;
import com.tcci.fcs.facade.FcCurrencyFacade;
import com.tcci.fcs.facade.FcMonthlyExchangeRateFacade;
import com.tcci.fcs.facade.FcReportA0102Facade;
import com.tcci.fcs.facade.FcReportValueFacade;
import com.tcci.fcs.facade.service.ExchangeRateFacade;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name="exchangeRateController")
@ViewScoped
public class ExchangeRateController {
    private final static Logger logger = LoggerFactory.getLogger(ExchangeRateController.class);
    
    //<editor-fold defaultstate="collapsed" desc="Injects">
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
    
    @EJB
    private FcMonthlyExchangeRateFacade fcMonthlyExchangeRateFacade;
    @EJB
    private ExchangeRateFacade exchangeRateFacade;
    @EJB
    private FcCurrencyFacade fcCurrencyFacade;
    @EJB
    private FcReportA0102Facade a0102Facade;
    @EJB
    private FcReportValueFacade reportValueFacade;
    //</editor-fold>
    
    
    private String editQuoteYM;
    private List<FcMonthlyExchangeRate> editMonthlyExchangeRateList;
    private FcMonthlyExchangeRate editRate;
    private List<SelectItem> ymOptions;
    
    @PostConstruct
    private void init() {
//        editQuoteYM = this.fetchLastYM();
        ymOptions = this.fetchLastYM();
//        editMonthlyExchangeRateList = fcMonthlyExchangeRateFacade.findByYM(editQuoteYM);
        this.fetchRateList();
    }
    
    private List<FcMonthlyExchangeRate> fetchRateList(){
        logger.debug("fetchRateList!!!");
        editMonthlyExchangeRateList = new ArrayList<>();
        List<FcCurrency> allList = fcCurrencyFacade.findAll();
        List<FcCurrency> toList = fcCurrencyFacade.findToCurrency();
//        logger.debug("toList:"+toList.size());
        
        List<FcMonthlyExchangeRate> results = fcMonthlyExchangeRateFacade.findByYM(editQuoteYM);
        for(FcCurrency toCurrency : toList){
            for(FcCurrency currency : allList){
//            logger.debug("fetchRateList currency:"+currency.getCode());
                //from 台幣排除
//                if(CurrencyEnum.TWD.name().equals(currency.getCode())){
//                    continue;
//                }
                //from == to 排除
                if(currency.getCode().equals(toCurrency.getCode())){
                    continue;
                }
                
                boolean exist = false;
                for(FcMonthlyExchangeRate exchangeRate : results){
                    if(exchangeRate.getCurrency() != null
                            && exchangeRate.getToCurrency() != null){
                        if(exchangeRate.getCurrency().getCode().equals(currency.getCode())
                                && exchangeRate.getToCurrency().getCode().equals(toCurrency.getCode())){
                            editMonthlyExchangeRateList.add(exchangeRate);
                            exist = true;
                        }
                    }
                }
//                logger.debug("fetchRateList exist:{" + currency.getCode() + "}==>{"+ toCurrency.getCode() + "}_" + exist);
                if(!exist){
                    FcMonthlyExchangeRate exchangeRate = new FcMonthlyExchangeRate();
                    exchangeRate.setCurrency(currency);
                    exchangeRate.setToCurrency(toCurrency);
                    exchangeRate.setYearmonth(editQuoteYM);
                    exchangeRate.setRate(BigDecimal.ONE);
                    exchangeRate.setAvgRate(BigDecimal.ONE);
                    editMonthlyExchangeRateList.add(exchangeRate);
                }
            }
        }
        return editMonthlyExchangeRateList;
    }
    
    private List<SelectItem> fetchLastYM(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Date now =new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MONTH, -1);
//        return sdf.format(calendar.getTime());
        editQuoteYM = sdf.format(calendar.getTime());
        
        List<SelectItem> options = new ArrayList();
        for (int i = 0;i<3;i++) {
            calendar.setTime(now);
            calendar.add(Calendar.MONTH, -i);
            options.add(new SelectItem(sdf.format(calendar.getTime()), sdf.format(calendar.getTime())));
        }
        
        return options;
    }
    
    public void updateMonthlyRate(){
        String excMsg = exchangeRateFacade.update(userSession.getTcUser());//登入者執行更新
        if("OK".equals(excMsg)){
            JsfUtil.addSuccessMessage("完成匯率更新");
            this.reload();
        }else{
            JsfUtil.addErrorMessage("匯率更新失敗, 請保留畫面並通知系統管理員:"+excMsg);
        }
    }
    
    private void reload() {
        logger.debug("reload");
//        editMonthlyExchangeRateList = fcMonthlyExchangeRateFacade.findByYM(editQuoteYM);
        this.fetchRateList();
    }
    
    public void editMonthlyRate(FcMonthlyExchangeRate monthlyRate) {
        editRate = (null == monthlyRate) ? new FcMonthlyExchangeRate() : monthlyRate;
    }
    
    public void saveRate(){
        RequestContext context = RequestContext.getCurrentInstance();
        // 輸入資料檢查
        if( !checkSaveParams() ){
//            JsfUtil.buildErrorCallback();
            this.fetchRateList();//編輯失敗 更新表單
            context.addCallbackParam("saved", false);
            return;
        }
        try {
            fcMonthlyExchangeRateFacade.save(editRate, userSession.getTcUser());
            JsfUtil.addSuccessMessage("儲存成功!");
            context.addCallbackParam("saved", true);
            reload();
        } catch (Exception ex) {
            logger.error("save exception", ex);
            JsfUtil.addErrorMessage("儲存失敗! Exception:"+ex);
            context.addCallbackParam("saved", false);
        }
    }
    
    public boolean canEditRate(FcMonthlyExchangeRate monthlyExchangeRate){
        if(null != monthlyExchangeRate){
            //20151021 依幣別 匯率年月 查詢匯率使用狀況 決定可否編輯
            //如已被使用則不可修改 使用者須提出程式需求單 評估修改幅度
            //A0102 損益表
            List<FcReportA0102> a1002List = a0102Facade.findByYmCurr(monthlyExchangeRate.getCurrency(), monthlyExchangeRate.getToCurrency(), monthlyExchangeRate.getYearmonth());
            if (CollectionUtils.isNotEmpty(a1002List)) {
//                JsfUtil.addWarningMessage("匯率已使用:"+monthlyExchangeRate.getCurrency().getName());
                return false;
            }
            //D0206 D0208 營業對帳資料
            List<FcReportValue> reportValueList = reportValueFacade.findByYmCurr(monthlyExchangeRate.getCurrency(), monthlyExchangeRate.getToCurrency(), monthlyExchangeRate.getYearmonth());
            if (CollectionUtils.isNotEmpty(reportValueList)) {
//                JsfUtil.addWarningMessage("匯率已使用:"+monthlyExchangeRate.getCurrency().getName());
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 檢核儲存條件
     * @return 
     */
    public boolean checkSaveParams(){
        logger.debug("checkSaveParams ...");
        if(editRate.getRate() == null){
            JsfUtil.addErrorMessage("請輸入匯率!");
            return false;
        }else{
            if(BigDecimal.ZERO.compareTo(editRate.getRate()) >= 0){
                JsfUtil.addErrorMessage("匯率設定不可小於等於零!");
                return false;
            }
        }
        if(editRate.getAvgRate() == null){
            JsfUtil.addErrorMessage("請輸入匯率!");
            return false;
        }else{
            if(BigDecimal.ZERO.compareTo(editRate.getAvgRate()) >= 0){
                JsfUtil.addErrorMessage("匯率設定不可小於等於零!");
                return false;
            }
        }
        
        return true;
    }
    
    public void changeYm() {
        logger.debug("changeYm:" + this.editQuoteYM);
        this.fetchRateList();
    }
    
    //<editor-fold defaultstate="collpased" desc="getter, setter">
    public String getEditQuoteYM() {
        return editQuoteYM;
    }

    public void setEditQuoteYM(String editQuoteYM) {
        this.editQuoteYM = editQuoteYM;
    }

    public List<FcMonthlyExchangeRate> getEditMonthlyExchangeRateList() {
        return editMonthlyExchangeRateList;
    }

    public void setEditMonthlyExchangeRateList(List<FcMonthlyExchangeRate> editMonthlyExchangeRateList) {
        this.editMonthlyExchangeRateList = editMonthlyExchangeRateList;
    }

    public FcMonthlyExchangeRate getEditRate() {
        return editRate;
    }

    public void setEditRate(FcMonthlyExchangeRate editRate) {
        this.editRate = editRate;
    }

    public List<SelectItem> getYmOptions() {
        return ymOptions;
    }
    //</editor-fold>
}