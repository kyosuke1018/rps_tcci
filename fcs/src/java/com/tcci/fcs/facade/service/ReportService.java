/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.facade.service;

import com.tcci.fcs.entity.FcMonthlyExchangeRate;
import com.tcci.fcs.enums.AccountTypeEnum;
import com.tcci.fcs.enums.CurrencyEnum;
import com.tcci.fcs.model.global.GlobalConstant;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import com.tcci.irs.controller.report.IrsReportBVO;
import com.tcci.irs.enums.SheetTypeEnum;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class ReportService {
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
    
    public final static String[] BS_REPA_COLS = { 
        "RE_A", "RE_B", "RE_C", "RE_D", "RE_E", "RE_F", "RE_G", "RE_H", "RE_I", "RE_J", 
        "RE_K", "RE_L", "RE_M", "RE_N", "RE_O",
        "PA_A", "PA_B", "PA_C", "PA_D", "PA_E", "PA_F", "PA_G", "PA_H", "PA_I", "PA_J", 
        "PA_K"
    };
    
    public final static String[] IS_REPA_COLS = { 
        "RE_A", "RE_B", "RE_C", "RE_D", "RE_E", "RE_F", "RE_G", "RE_H", "RE_I", "RE_J", 
        "RE_K", "RE_L", "RE_M", "RE_N", "RE_O", "RE_P",
        "PA_A", "PA_B", "PA_C", "PA_D", "PA_E"
    };
    
    public final static String[] BS_REPA_COLS_HEADER = {
        "公司名稱",
        "1161應收票據-關係人",
        "1172應收帳款-關係人",
        "1181應收帳款-關係人",
        "1175應收租賃款",
        "1206其他應收款",
        "1212其他應收款-關係人",
        "1300存貨",
        "1419預付費用",
        "1421預付貨款",
        "1268其他預付款",
        "1479其他流動資產",
        "1600不動產、廠房及設備",
        "1920存出保證金品",
        "1930長期應收票據及款項-關係人",
        "1990其他非流動資產",
        "合計數",
        "公司名稱",
        "2100其他短期借款",
        "2160應付票據-關係人",
        "2170應付帳款",
        "2180應付帳款-關係人",
        "2200其他應付款項",
        "2220其他應付款項–關係人",
        "2310預收款項",
        "2399其他流動負債",
        "2620長期應付票據及款項",
        "2645存入保證金(品)",
        "2670其他負債-其他",
        "合計數",
        "差異數"
    };
    
    public final static String[] IS_REPA_COLS_HEADER = {
        "公司名稱",
        "4111銷貨收入",
        "4114銷貨收入",
        "4115銷貨收入",
        "4116銷貨收入",
        "4117銷貨收入",
        "4119銷貨收入",
        "4190銷貨退回及折讓",
        "4300收入金額",
        "4500收入金額",
        "4600收入金額",
        "4800收入金額",
        "7100利息收入金額",
        "7110收入金額",
        "7130股利收入金額",
        "7210收入金額",
        "7010收入金額",
        "合計數",
        "公司名稱",
        "5000進貨、運輸成本、運什費等營業成本",
        "5800其他營業成本",
        "6000營業費用",
        "7510利息費用",
        "7590支出金額",
        "合計數",
        "差異數"
    };

    //依廠別代碼 加總
    public static Map<String, BigDecimal> sumByCompCode(List<ReportBaseVO> list){
        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        if (CollectionUtils.isNotEmpty(list)) {
            for(ReportBaseVO reportBaseVO: list){
                // 加總
                String sumKey = reportBaseVO.getCompCode();
                BigDecimal sum = result.get(sumKey);
                if (null == sum) {
                    result.put(sumKey, reportBaseVO.getAmount());
                } else {
                    result.put(sumKey, sum.add(reportBaseVO.getAmount()));
                }
            }
        }
        return result;
    }
    
    //依會科代碼 加總
    public static Map<String, BigDecimal> sumByAccCode(List<ReportBaseVO> list){
        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        if (CollectionUtils.isNotEmpty(list)) {
            for(ReportBaseVO reportBaseVO: list){
                // 加總
                String sumKey = reportBaseVO.getAccCode();
                BigDecimal sum = result.get(sumKey);
                if (null == sum) {
                    result.put(sumKey, reportBaseVO.getAmount());
                } else {
                    result.put(sumKey, sum.add(reportBaseVO.getAmount()));
                }
            }
        }
        return result;
    }
    
    //reportB vo 處理金額轉台幣 寫入指定會科欄位 processAccTwdAmount
    //改在ReportService 實作
    public IrsReportBVO processReportBVO(IrsReportBVO vo, String repaType, String accCode, String currCode, 
            BigDecimal amount, String bsisType,
            String yearmonth, List<FcMonthlyExchangeRate> rateList){
        if(null == amount){
            return vo;
        }
        BigDecimal twdAmount = BigDecimal.ZERO;
        if(!CurrencyEnum.TWD.name().equals(currCode)){
            for(FcMonthlyExchangeRate rateVO : rateList){
                if(rateVO.getCurrency().getCode().equals(currCode)){
                    BigDecimal rate;
                    if(SheetTypeEnum.BS.getValue().equals(bsisType)){
                        rate = rateVO.getRate();
                    }else{
                        rate = rateVO.getAvgRate();
                    }
                    if(rate == null){
                        logger.error("processAccTwdAmount 查無匯率! {currCode, yearmonth}:{" + currCode + "," + yearmonth + "}");
                        return vo;
                    }
                    twdAmount = amount.multiply(rate).setScale(GlobalConstant.AMOUNT_SCALE, RoundingMode.HALF_UP);
                    break;
                }
            }
        }else{
            twdAmount = amount;
        }
        
        //debug用
//        if("6800".equals(vo.getComp2Code())){
//            logger.debug("processAccTwdAmount vo:{accCode,amount,twdAmount}=={" + accCode + "," + amount + "," + twdAmount + "}");
//        }
        
        //寫入指定會科欄位

        //該欄位已有值 用加的
        /*if(vo.getAmountReC() != null) result = vo.getAmountReC();
                        vo.setAmountReC(result.add(twdAmount));
        */
        
        //BS IS
        //RE PA
        //accCode
        BigDecimal result = BigDecimal.ZERO;//該欄位已有值 用加的
        if(SheetTypeEnum.BS.getValue().equals(bsisType)){
            if(AccountTypeEnum.RE.getCode().equals(repaType)){
                if(null != accCode)switch (accCode) {
                    case "1161":
//                        vo.setAmountReA(twdAmount);
                        if(vo.getAmountReA() != null) result = vo.getAmountReA();
                        vo.setAmountReA(result.add(twdAmount));
                        break;
                    case "1172":
                        if(vo.getAmountReB() != null) result = vo.getAmountReB();
                        vo.setAmountReB(result.add(twdAmount));
                        break;
                    case "1181":
                        if(vo.getAmountReC() != null) result = vo.getAmountReC();
                        vo.setAmountReC(result.add(twdAmount));
                        break;
                    case "1175":
                        if(vo.getAmountReD() != null) result = vo.getAmountReD();
                        vo.setAmountReD(result.add(twdAmount));
                        break;    
                    case "1206":
                        if(vo.getAmountReE() != null) result = vo.getAmountReE();
                        vo.setAmountReE(result.add(twdAmount));
                        break;
                    case "1212":
                        if(vo.getAmountReF() != null) result = vo.getAmountReF();
                        vo.setAmountReF(result.add(twdAmount));
                        break;
                    case "1300":
                        if(vo.getAmountReG() != null) result = vo.getAmountReG();
                        vo.setAmountReG(result.add(twdAmount));
                        break;
                    case "1419":
                        if(vo.getAmountReH() != null) result = vo.getAmountReH();
                        vo.setAmountReH(result.add(twdAmount));
                        break;
                    case "1421":
                        if(vo.getAmountReI() != null) result = vo.getAmountReI();
                        vo.setAmountReI(result.add(twdAmount));
                        break;
                    case "1268":
                        if(vo.getAmountReJ() != null) result = vo.getAmountReJ();
                        vo.setAmountReJ(result.add(twdAmount));
                        break;
                    case "1479":
                        if(vo.getAmountReK() != null) result = vo.getAmountReK();
                        vo.setAmountReK(result.add(twdAmount));
                        break;
                    case "1600":
                        if(vo.getAmountReL() != null) result = vo.getAmountReL();
                        vo.setAmountReL(result.add(twdAmount));
                        break;
                    case "1920":
                        if(vo.getAmountReM() != null) result = vo.getAmountReM();
                        vo.setAmountReM(result.add(twdAmount));
                        break;
                    case "1930":
                        if(vo.getAmountReN() != null) result = vo.getAmountReN();
                        vo.setAmountReN(result.add(twdAmount));
                        break;
                    case "1990":
                        if(vo.getAmountReO() != null) result = vo.getAmountReO();
                        vo.setAmountReO(result.add(twdAmount));
                        break;
                }
            }else{//PA
                if(null != accCode)switch (accCode) {
                    case "2100":
                        if(vo.getAmountPaA() != null) result = vo.getAmountPaA();
                        vo.setAmountPaA(result.add(twdAmount));
                        break;
                    case "2160":
                        if(vo.getAmountPaB() != null) result = vo.getAmountPaB();
                        vo.setAmountPaB(result.add(twdAmount));
                        break;
                    case "2170":
                        if(vo.getAmountPaC() != null) result = vo.getAmountPaC();
                        vo.setAmountPaC(result.add(twdAmount));
                        break;
                    case "2180":
                        if(vo.getAmountPaD() != null) result = vo.getAmountPaD();
                        vo.setAmountPaD(result.add(twdAmount));
                        break;
                    case "2200":
                        if(vo.getAmountPaE() != null) result = vo.getAmountPaE();
                        vo.setAmountPaE(result.add(twdAmount));
                        break;
                    case "2220":
                        if(vo.getAmountPaF() != null) result = vo.getAmountPaF();
                        vo.setAmountPaF(result.add(twdAmount));
                        break;
                    case "2310":
                        if(vo.getAmountPaG() != null) result = vo.getAmountPaG();
                        vo.setAmountPaG(result.add(twdAmount));
                        break;
                    case "2399":
                        if(vo.getAmountPaH() != null) result = vo.getAmountPaH();
                        vo.setAmountPaH(result.add(twdAmount));
                        break;
                    case "2620":
                        if(vo.getAmountPaI() != null) result = vo.getAmountPaI();
                        vo.setAmountPaI(result.add(twdAmount));
                        break;
                    case "2645":
                        if(vo.getAmountPaJ() != null) result = vo.getAmountPaJ();
                        vo.setAmountPaJ(result.add(twdAmount));
                        break;    
                    case "2670":
                        if(vo.getAmountPaK() != null) result = vo.getAmountPaK();
                        vo.setAmountPaK(result.add(twdAmount));
                        break;
                }
            }
        }else{//IS
            if(AccountTypeEnum.RE.getCode().equals(repaType)){
                if(null != accCode)switch (accCode) {
                    case "4111":
                        if(vo.getAmountReA() != null) result = vo.getAmountReA();
                        vo.setAmountReA(result.add(twdAmount));
                        break;
                    case "4114":
                        if(vo.getAmountReB() != null) result = vo.getAmountReB();
                        vo.setAmountReB(result.add(twdAmount));
                        break;
                    case "4115":
                        if(vo.getAmountReC() != null) result = vo.getAmountReC();
                        vo.setAmountReC(result.add(twdAmount));
                        break;
                    case "4116":
                        if(vo.getAmountReD() != null) result = vo.getAmountReD();
                        vo.setAmountReD(result.add(twdAmount));
                        break;
                    case "4117":
                        if(vo.getAmountReE() != null) result = vo.getAmountReE();
                        vo.setAmountReE(result.add(twdAmount));
                        break;
                    case "4119":
                        if(vo.getAmountReF() != null) result = vo.getAmountReF();
                        vo.setAmountReF(result.add(twdAmount));
                        break;
                    case "4190":
                        if(vo.getAmountReG() != null) result = vo.getAmountReG();
                        vo.setAmountReG(result.add(twdAmount));
                        break;
                    case "4300":
                        if(vo.getAmountReH() != null) result = vo.getAmountReH();
                        vo.setAmountReH(result.add(twdAmount));
                        break;
                    case "4500":
                        if(vo.getAmountReI() != null) result = vo.getAmountReI();
                        vo.setAmountReI(result.add(twdAmount));
                        break;
                    case "4600":
                        if(vo.getAmountReJ() != null) result = vo.getAmountReJ();
                        vo.setAmountReJ(result.add(twdAmount));
                        break;
                    case "4800":
                        if(vo.getAmountReK() != null) result = vo.getAmountReK();
                        vo.setAmountReK(result.add(twdAmount));
                        break;    
                    case "7100":
                        if(vo.getAmountReL() != null) result = vo.getAmountReL();
                        vo.setAmountReL(result.add(twdAmount));
                        break;
                    case "7110":
                        if(vo.getAmountReM() != null) result = vo.getAmountReM();
                        vo.setAmountReM(result.add(twdAmount));
                        break;
                    case "7130":
                        if(vo.getAmountReN() != null) result = vo.getAmountReN();
                        vo.setAmountReN(result.add(twdAmount));
                        break;
                    case "7210":
                        if(vo.getAmountReO() != null) result = vo.getAmountReO();
                        vo.setAmountReO(result.add(twdAmount));
                        break;
                    case "7010":
                        if(vo.getAmountReP() != null) result = vo.getAmountReP();
                        vo.setAmountReP(result.add(twdAmount));
                        break;
                }
            }else{//PA
                if(null != accCode)switch (accCode) {
                    case "5000":
                        if(vo.getAmountPaA() != null) result = vo.getAmountPaA();
                        vo.setAmountPaA(result.add(twdAmount));
                        break;
                    case "5800":
                        if(vo.getAmountPaB() != null) result = vo.getAmountPaB();
                        vo.setAmountPaB(result.add(twdAmount));
                        break;
                    case "6000":
                        if(vo.getAmountPaC() != null) result = vo.getAmountPaC();
                        vo.setAmountPaC(result.add(twdAmount));
                        break;
                    case "7510":
                        if(vo.getAmountPaD() != null) result = vo.getAmountPaD();
                        vo.setAmountPaD(result.add(twdAmount));
                        break;
                    case "7590":
                        if(vo.getAmountPaE() != null) result = vo.getAmountPaE();
                        vo.setAmountPaE(result.add(twdAmount));
                        break;
                }
            }
        }
        return vo;
    }
    //20160818 增加指定幣別金額轉換
    public IrsReportBVO processReportBVO(IrsReportBVO vo, String repaType, String accCode, String currCode, 
            BigDecimal amount, String bsisType, String yearmonth, 
            String toCurrency, List<FcMonthlyExchangeRate> rateList){
        if(null == amount){
            return vo;
        }
        BigDecimal toCurrencyAmount = BigDecimal.ZERO;
        if(!toCurrency.equals(currCode)){
            for(FcMonthlyExchangeRate rateVO : rateList){
                if(rateVO.getCurrency().getCode().equals(currCode)){
                    BigDecimal rate;
                    if(SheetTypeEnum.BS.getValue().equals(bsisType)){
                        rate = rateVO.getRate();
                    }else{
                        rate = rateVO.getAvgRate();
                    }
                    if(rate == null){
                        logger.error("processAccTwdAmount 查無匯率! {currCode, yearmonth}:{" + currCode + "," + yearmonth + "}");
                        return vo;
                    }
                    if(CurrencyEnum.TWD.name().equals(toCurrency)){
                        toCurrencyAmount = amount.multiply(rate).setScale(GlobalConstant.AMOUNT_SCALE, RoundingMode.HALF_UP);
                    }else{
                        toCurrencyAmount = amount.multiply(rate).setScale(GlobalConstant.AMOUNT_SCALE_2, RoundingMode.HALF_UP);
                    }
                    break;
                }
            }
        }else{
            toCurrencyAmount = amount;
        }
        
        //debug用
//        if("6800".equals(vo.getComp2Code())){
//            logger.debug("processAccTwdAmount vo:{accCode,amount,twdAmount}=={" + accCode + "," + amount + "," + twdAmount + "}");
//        }
        
        //寫入指定會科欄位

        //該欄位已有值 用加的
        /*if(vo.getAmountReC() != null) result = vo.getAmountReC();
                        vo.setAmountReC(result.add(twdAmount));
        */
        
        //BS IS
        //RE PA
        //accCode
        BigDecimal result = BigDecimal.ZERO;//該欄位已有值 用加的
        if(SheetTypeEnum.BS.getValue().equals(bsisType)){
            if(AccountTypeEnum.RE.getCode().equals(repaType)){
                if(null != accCode)switch (accCode) {
                    case "1161":
//                        vo.setAmountReA(twdAmount);
                        if(vo.getAmountReA() != null) result = vo.getAmountReA();
                        vo.setAmountReA(result.add(toCurrencyAmount));
                        break;
                    case "1172":
                        if(vo.getAmountReB() != null) result = vo.getAmountReB();
                        vo.setAmountReB(result.add(toCurrencyAmount));
                        break;
                    case "1181":
                        if(vo.getAmountReC() != null) result = vo.getAmountReC();
                        vo.setAmountReC(result.add(toCurrencyAmount));
                        break;
                    case "1175":
                        if(vo.getAmountReD() != null) result = vo.getAmountReD();
                        vo.setAmountReD(result.add(toCurrencyAmount));
                        break;    
                    case "1206":
                        if(vo.getAmountReE() != null) result = vo.getAmountReE();
                        vo.setAmountReE(result.add(toCurrencyAmount));
                        break;
                    case "1212":
                        if(vo.getAmountReF() != null) result = vo.getAmountReF();
                        vo.setAmountReF(result.add(toCurrencyAmount));
                        break;
                    case "1300":
                        if(vo.getAmountReG() != null) result = vo.getAmountReG();
                        vo.setAmountReG(result.add(toCurrencyAmount));
                        break;
                    case "1419":
                        if(vo.getAmountReH() != null) result = vo.getAmountReH();
                        vo.setAmountReH(result.add(toCurrencyAmount));
                        break;
                    case "1421":
                        if(vo.getAmountReI() != null) result = vo.getAmountReI();
                        vo.setAmountReI(result.add(toCurrencyAmount));
                        break;
                    case "1268":
                        if(vo.getAmountReJ() != null) result = vo.getAmountReJ();
                        vo.setAmountReJ(result.add(toCurrencyAmount));
                        break;
                    case "1479":
                        if(vo.getAmountReK() != null) result = vo.getAmountReK();
                        vo.setAmountReK(result.add(toCurrencyAmount));
                        break;
                    case "1600":
                        if(vo.getAmountReL() != null) result = vo.getAmountReL();
                        vo.setAmountReL(result.add(toCurrencyAmount));
                        break;
                    case "1920":
                        if(vo.getAmountReM() != null) result = vo.getAmountReM();
                        vo.setAmountReM(result.add(toCurrencyAmount));
                        break;
                    case "1930":
                        if(vo.getAmountReN() != null) result = vo.getAmountReN();
                        vo.setAmountReN(result.add(toCurrencyAmount));
                        break;
                    case "1990":
                        if(vo.getAmountReO() != null) result = vo.getAmountReO();
                        vo.setAmountReO(result.add(toCurrencyAmount));
                        break;
                }
            }else{//PA
                if(null != accCode)switch (accCode) {
                    case "2100":
                        if(vo.getAmountPaA() != null) result = vo.getAmountPaA();
                        vo.setAmountPaA(result.add(toCurrencyAmount));
                        break;
                    case "2160":
                        if(vo.getAmountPaB() != null) result = vo.getAmountPaB();
                        vo.setAmountPaB(result.add(toCurrencyAmount));
                        break;
                    case "2170":
                        if(vo.getAmountPaC() != null) result = vo.getAmountPaC();
                        vo.setAmountPaC(result.add(toCurrencyAmount));
                        break;
                    case "2180":
                        if(vo.getAmountPaD() != null) result = vo.getAmountPaD();
                        vo.setAmountPaD(result.add(toCurrencyAmount));
                        break;
                    case "2200":
                        if(vo.getAmountPaE() != null) result = vo.getAmountPaE();
                        vo.setAmountPaE(result.add(toCurrencyAmount));
                        break;
                    case "2220":
                        if(vo.getAmountPaF() != null) result = vo.getAmountPaF();
                        vo.setAmountPaF(result.add(toCurrencyAmount));
                        break;
                    case "2310":
                        if(vo.getAmountPaG() != null) result = vo.getAmountPaG();
                        vo.setAmountPaG(result.add(toCurrencyAmount));
                        break;
                    case "2399":
                        if(vo.getAmountPaH() != null) result = vo.getAmountPaH();
                        vo.setAmountPaH(result.add(toCurrencyAmount));
                        break;
                    case "2620":
                        if(vo.getAmountPaI() != null) result = vo.getAmountPaI();
                        vo.setAmountPaI(result.add(toCurrencyAmount));
                        break;
                    case "2645":
                        if(vo.getAmountPaJ() != null) result = vo.getAmountPaJ();
                        vo.setAmountPaJ(result.add(toCurrencyAmount));
                        break;    
                    case "2670":
                        if(vo.getAmountPaK() != null) result = vo.getAmountPaK();
                        vo.setAmountPaK(result.add(toCurrencyAmount));
                        break;
                }
            }
        }else{//IS
            if(AccountTypeEnum.RE.getCode().equals(repaType)){
                if(null != accCode)switch (accCode) {
                    case "4111":
                        if(vo.getAmountReA() != null) result = vo.getAmountReA();
                        vo.setAmountReA(result.add(toCurrencyAmount));
                        break;
                    case "4114":
                        if(vo.getAmountReB() != null) result = vo.getAmountReB();
                        vo.setAmountReB(result.add(toCurrencyAmount));
                        break;
                    case "4115":
                        if(vo.getAmountReC() != null) result = vo.getAmountReC();
                        vo.setAmountReC(result.add(toCurrencyAmount));
                        break;
                    case "4116":
                        if(vo.getAmountReD() != null) result = vo.getAmountReD();
                        vo.setAmountReD(result.add(toCurrencyAmount));
                        break;
                    case "4117":
                        if(vo.getAmountReE() != null) result = vo.getAmountReE();
                        vo.setAmountReE(result.add(toCurrencyAmount));
                        break;
                    case "4119":
                        if(vo.getAmountReF() != null) result = vo.getAmountReF();
                        vo.setAmountReF(result.add(toCurrencyAmount));
                        break;
                    case "4190":
                        if(vo.getAmountReG() != null) result = vo.getAmountReG();
                        vo.setAmountReG(result.add(toCurrencyAmount));
                        break;
                    case "4300":
                        if(vo.getAmountReH() != null) result = vo.getAmountReH();
                        vo.setAmountReH(result.add(toCurrencyAmount));
                        break;
                    case "4500":
                        if(vo.getAmountReI() != null) result = vo.getAmountReI();
                        vo.setAmountReI(result.add(toCurrencyAmount));
                        break;
                    case "4600":
                        if(vo.getAmountReJ() != null) result = vo.getAmountReJ();
                        vo.setAmountReJ(result.add(toCurrencyAmount));
                        break;
                    case "4800":
                        if(vo.getAmountReK() != null) result = vo.getAmountReK();
                        vo.setAmountReK(result.add(toCurrencyAmount));
                        break;    
                    case "7100":
                        if(vo.getAmountReL() != null) result = vo.getAmountReL();
                        vo.setAmountReL(result.add(toCurrencyAmount));
                        break;
                    case "7110":
                        if(vo.getAmountReM() != null) result = vo.getAmountReM();
                        vo.setAmountReM(result.add(toCurrencyAmount));
                        break;
                    case "7130":
                        if(vo.getAmountReN() != null) result = vo.getAmountReN();
                        vo.setAmountReN(result.add(toCurrencyAmount));
                        break;
                    case "7210":
                        if(vo.getAmountReO() != null) result = vo.getAmountReO();
                        vo.setAmountReO(result.add(toCurrencyAmount));
                        break;
                    case "7010":
                        if(vo.getAmountReP() != null) result = vo.getAmountReP();
                        vo.setAmountReP(result.add(toCurrencyAmount));
                        break;
                }
            }else{//PA
                if(null != accCode)switch (accCode) {
                    case "5000":
                        if(vo.getAmountPaA() != null) result = vo.getAmountPaA();
                        vo.setAmountPaA(result.add(toCurrencyAmount));
                        break;
                    case "5800":
                        if(vo.getAmountPaB() != null) result = vo.getAmountPaB();
                        vo.setAmountPaB(result.add(toCurrencyAmount));
                        break;
                    case "6000":
                        if(vo.getAmountPaC() != null) result = vo.getAmountPaC();
                        vo.setAmountPaC(result.add(toCurrencyAmount));
                        break;
                    case "7510":
                        if(vo.getAmountPaD() != null) result = vo.getAmountPaD();
                        vo.setAmountPaD(result.add(toCurrencyAmount));
                        break;
                    case "7590":
                        if(vo.getAmountPaE() != null) result = vo.getAmountPaE();
                        vo.setAmountPaE(result.add(toCurrencyAmount));
                        break;
                }
            }
        }
        return vo;
    }
    
    //對於footer列 運算處理
    //改在ReportService 實作(initFooterMap==>footerTotal==>insertFooter)
    public void insertFooter(List<IrsReportBVO> list, String bsisType){
        Map<String, BigDecimal> footerMap = this.footerTotal(list, bsisType);
        
        IrsReportBVO footer = new IrsReportBVO();
        footer.setSheetType(bsisType);
        footer.setCompCode("Total");
        footer.setCompName("");
        footer.setComp2Code("");
        footer.setComp2Name("");
        if(SheetTypeEnum.BS.getValue().equals(bsisType)){
            footer.setAmountReA(footerMap.get("RE_A"));
            footer.setAmountReB(footerMap.get("RE_B"));
            footer.setAmountReC(footerMap.get("RE_C"));
            footer.setAmountReD(footerMap.get("RE_D"));
            footer.setAmountReE(footerMap.get("RE_E"));
            footer.setAmountReF(footerMap.get("RE_F"));
            footer.setAmountReG(footerMap.get("RE_G"));
            footer.setAmountReH(footerMap.get("RE_H"));
            footer.setAmountReI(footerMap.get("RE_I"));
            footer.setAmountReJ(footerMap.get("RE_J"));
            footer.setAmountReK(footerMap.get("RE_K"));
            footer.setAmountReL(footerMap.get("RE_L"));
            footer.setAmountReM(footerMap.get("RE_M"));
            footer.setAmountReN(footerMap.get("RE_N"));
            footer.setAmountReO(footerMap.get("RE_O"));
            
            footer.setAmountPaA(footerMap.get("PA_A"));
            footer.setAmountPaB(footerMap.get("PA_B"));
            footer.setAmountPaC(footerMap.get("PA_C"));
            footer.setAmountPaD(footerMap.get("PA_D"));
            footer.setAmountPaE(footerMap.get("PA_E"));
            footer.setAmountPaF(footerMap.get("PA_F"));
            footer.setAmountPaG(footerMap.get("PA_G"));
            footer.setAmountPaH(footerMap.get("PA_H"));
            footer.setAmountPaI(footerMap.get("PA_I"));
            footer.setAmountPaJ(footerMap.get("PA_J"));
            footer.setAmountPaK(footerMap.get("PA_K"));
        }else{
            footer.setAmountReA(footerMap.get("RE_A"));
            footer.setAmountReB(footerMap.get("RE_B"));
            footer.setAmountReC(footerMap.get("RE_C"));
            footer.setAmountReD(footerMap.get("RE_D"));
            footer.setAmountReE(footerMap.get("RE_E"));
            footer.setAmountReF(footerMap.get("RE_F"));
            footer.setAmountReG(footerMap.get("RE_G"));
            footer.setAmountReH(footerMap.get("RE_H"));
            footer.setAmountReI(footerMap.get("RE_I"));
            footer.setAmountReJ(footerMap.get("RE_J"));
            footer.setAmountReK(footerMap.get("RE_K"));
            footer.setAmountReL(footerMap.get("RE_L"));
            footer.setAmountReM(footerMap.get("RE_M"));
            footer.setAmountReN(footerMap.get("RE_N"));
            footer.setAmountReO(footerMap.get("RE_O"));
            footer.setAmountReP(footerMap.get("RE_P"));
            
            footer.setAmountPaA(footerMap.get("PA_A"));
            footer.setAmountPaB(footerMap.get("PA_B"));
            footer.setAmountPaC(footerMap.get("PA_C"));
            footer.setAmountPaD(footerMap.get("PA_D"));
            footer.setAmountPaE(footerMap.get("PA_E"));
        }
        
        list.add(footer);
    }
    
    private Map<String, BigDecimal> footerTotal(List<IrsReportBVO> list, String bsisType){
        Map<String, BigDecimal> footerMap = this.initFooterMap(bsisType);
        for(IrsReportBVO reportVO:list){
            if(SheetTypeEnum.BS.getValue().equals(bsisType)){
                //RE
                if(reportVO.getAmountReA()!=null) {
                    BigDecimal result = footerMap.get("RE_A");
                    result = result.add(reportVO.getAmountReA());
                    footerMap.put("RE_A", result);
                }
                if(reportVO.getAmountReB()!=null) {
                    BigDecimal result = footerMap.get("RE_B");
                    result = result.add(reportVO.getAmountReB());
                    footerMap.put("RE_B", result);
                }
                if(reportVO.getAmountReC()!=null) {
                    BigDecimal result = footerMap.get("RE_C");
                    result = result.add(reportVO.getAmountReC());
                    footerMap.put("RE_C", result);
                }
                if(reportVO.getAmountReD()!=null) {
                    BigDecimal result = footerMap.get("RE_D");
                    result = result.add(reportVO.getAmountReD());
                    footerMap.put("RE_D", result);
                }
                if(reportVO.getAmountReE()!=null) {
                    BigDecimal result = footerMap.get("RE_E");
                    result = result.add(reportVO.getAmountReE());
                    footerMap.put("RE_E", result);
                }
                if(reportVO.getAmountReF()!=null) {
                    BigDecimal result = footerMap.get("RE_F");
                    result = result.add(reportVO.getAmountReF());
                    footerMap.put("RE_F", result);
                }
                if(reportVO.getAmountReG()!=null) {
                    BigDecimal result = footerMap.get("RE_G");
                    result = result.add(reportVO.getAmountReG());
                    footerMap.put("RE_G", result);
                }
                if(reportVO.getAmountReH()!=null) {
                    BigDecimal result = footerMap.get("RE_H");
                    result = result.add(reportVO.getAmountReH());
                    footerMap.put("RE_H", result);
                }
                if(reportVO.getAmountReI()!=null) {
                    BigDecimal result = footerMap.get("RE_I");
                    result = result.add(reportVO.getAmountReI());
                    footerMap.put("RE_I", result);
                }
                if(reportVO.getAmountReJ()!=null) {
                    BigDecimal result = footerMap.get("RE_J");
                    result = result.add(reportVO.getAmountReJ());
                    footerMap.put("RE_J", result);
                }
                if(reportVO.getAmountReK()!=null) {
                    BigDecimal result = footerMap.get("RE_K");
                    result = result.add(reportVO.getAmountReK());
                    footerMap.put("RE_K", result);
                }
                if(reportVO.getAmountReL()!=null) {
                    BigDecimal result = footerMap.get("RE_L");
                    result = result.add(reportVO.getAmountReL());
                    footerMap.put("RE_L", result);
                }
                if(reportVO.getAmountReM()!=null) {
                    BigDecimal result = footerMap.get("RE_M");
                    result = result.add(reportVO.getAmountReM());
                    footerMap.put("RE_M", result);
                }
                if(reportVO.getAmountReN()!=null) {
                    BigDecimal result = footerMap.get("RE_N");
                    result = result.add(reportVO.getAmountReN());
                    footerMap.put("RE_N", result);
                }
                if(reportVO.getAmountReO()!=null) {
                    BigDecimal result = footerMap.get("RE_O");
                    result = result.add(reportVO.getAmountReO());
                    footerMap.put("RE_O", result);
                }
                //PA
                if(reportVO.getAmountPaA()!=null) {
                    BigDecimal result = footerMap.get("PA_A");
                    result = result.add(reportVO.getAmountPaA());
                    footerMap.put("PA_A", result);
                }
                if(reportVO.getAmountPaB()!=null) {
                    BigDecimal result = footerMap.get("PA_B");
                    result = result.add(reportVO.getAmountPaB());
                    footerMap.put("PA_B", result);
                }
                if(reportVO.getAmountPaC()!=null) {
                    BigDecimal result = footerMap.get("PA_C");
                    result = result.add(reportVO.getAmountPaC());
                    footerMap.put("PA_C", result);
                }
                if(reportVO.getAmountPaD()!=null) {
                    BigDecimal result = footerMap.get("PA_D");
                    result = result.add(reportVO.getAmountPaD());
                    footerMap.put("PA_D", result);
                }
                if(reportVO.getAmountPaE()!=null) {
                    BigDecimal result = footerMap.get("PA_E");
                    result = result.add(reportVO.getAmountPaE());
                    footerMap.put("PA_E", result);
                }
                if(reportVO.getAmountPaF()!=null) {
                    BigDecimal result = footerMap.get("PA_F");
                    result = result.add(reportVO.getAmountPaF());
                    footerMap.put("PA_F", result);
                }
                if(reportVO.getAmountPaG()!=null) {
                    BigDecimal result = footerMap.get("PA_G");
                    result = result.add(reportVO.getAmountPaG());
                    footerMap.put("PA_G", result);
                }
                if(reportVO.getAmountPaH()!=null) {
                    BigDecimal result = footerMap.get("PA_H");
                    result = result.add(reportVO.getAmountPaH());
                    footerMap.put("PA_H", result);
                }
                if(reportVO.getAmountPaI()!=null) {
                    BigDecimal result = footerMap.get("PA_I");
                    result = result.add(reportVO.getAmountPaI());
                    footerMap.put("PA_I", result);
                }
                if(reportVO.getAmountPaJ()!=null) {
                    BigDecimal result = footerMap.get("PA_J");
                    result = result.add(reportVO.getAmountPaJ());
                    footerMap.put("PA_J", result);
                }
                if(reportVO.getAmountPaK()!=null) {
                    BigDecimal result = footerMap.get("PA_K");
                    result = result.add(reportVO.getAmountPaK());
                    footerMap.put("PA_K", result);
                }
            }else{
                //RE
                if(reportVO.getAmountReA()!=null) {
                    BigDecimal result = footerMap.get("RE_A");
                    result = result.add(reportVO.getAmountReA());
                    footerMap.put("RE_A", result);
                }
                if(reportVO.getAmountReB()!=null) {
                    BigDecimal result = footerMap.get("RE_B");
                    result = result.add(reportVO.getAmountReB());
                    footerMap.put("RE_B", result);
                }
                if(reportVO.getAmountReC()!=null) {
                    BigDecimal result = footerMap.get("RE_C");
                    result = result.add(reportVO.getAmountReC());
                    footerMap.put("RE_C", result);
                }
                if(reportVO.getAmountReD()!=null) {
                    BigDecimal result = footerMap.get("RE_D");
                    result = result.add(reportVO.getAmountReD());
                    footerMap.put("RE_D", result);
                }
                if(reportVO.getAmountReE()!=null) {
                    BigDecimal result = footerMap.get("RE_E");
                    result = result.add(reportVO.getAmountReE());
                    footerMap.put("RE_E", result);
                }
                if(reportVO.getAmountReF()!=null) {
                    BigDecimal result = footerMap.get("RE_F");
                    result = result.add(reportVO.getAmountReF());
                    footerMap.put("RE_F", result);
                }
                if(reportVO.getAmountReG()!=null) {
                    BigDecimal result = footerMap.get("RE_G");
                    result = result.add(reportVO.getAmountReG());
                    footerMap.put("RE_G", result);
                }
                if(reportVO.getAmountReH()!=null) {
                    BigDecimal result = footerMap.get("RE_H");
                    result = result.add(reportVO.getAmountReH());
                    footerMap.put("RE_H", result);
                }
                if(reportVO.getAmountReI()!=null) {
                    BigDecimal result = footerMap.get("RE_I");
                    result = result.add(reportVO.getAmountReI());
                    footerMap.put("RE_I", result);
                }
                if(reportVO.getAmountReJ()!=null) {
                    BigDecimal result = footerMap.get("RE_J");
                    result = result.add(reportVO.getAmountReJ());
                    footerMap.put("RE_J", result);
                }
                if(reportVO.getAmountReK()!=null) {
                    BigDecimal result = footerMap.get("RE_K");
                    result = result.add(reportVO.getAmountReK());
                    footerMap.put("RE_K", result);
                }
                if(reportVO.getAmountReL()!=null) {
                    BigDecimal result = footerMap.get("RE_L");
                    result = result.add(reportVO.getAmountReL());
                    footerMap.put("RE_L", result);
                }
                if(reportVO.getAmountReM()!=null) {
                    BigDecimal result = footerMap.get("RE_M");
                    result = result.add(reportVO.getAmountReM());
                    footerMap.put("RE_M", result);
                }
                if(reportVO.getAmountReN()!=null) {
                    BigDecimal result = footerMap.get("RE_N");
                    result = result.add(reportVO.getAmountReN());
                    footerMap.put("RE_N", result);
                }
                if(reportVO.getAmountReO()!=null) {
                    BigDecimal result = footerMap.get("RE_O");
                    result = result.add(reportVO.getAmountReO());
                    footerMap.put("RE_O", result);
                }
                if(reportVO.getAmountReP()!=null) {
                    BigDecimal result = footerMap.get("RE_P");
                    result = result.add(reportVO.getAmountReP());
                    footerMap.put("RE_P", result);
                }
                //PA
                if(reportVO.getAmountPaA()!=null) {
                    BigDecimal result = footerMap.get("PA_A");
                    result = result.add(reportVO.getAmountPaA());
                    footerMap.put("PA_A", result);
                }
                if(reportVO.getAmountPaB()!=null) {
                    BigDecimal result = footerMap.get("PA_B");
                    result = result.add(reportVO.getAmountPaB());
                    footerMap.put("PA_B", result);
                }
                if(reportVO.getAmountPaC()!=null) {
                    BigDecimal result = footerMap.get("PA_C");
                    result = result.add(reportVO.getAmountPaC());
                    footerMap.put("PA_C", result);
                }
                if(reportVO.getAmountPaD()!=null) {
                    BigDecimal result = footerMap.get("PA_D");
                    result = result.add(reportVO.getAmountPaD());
                    footerMap.put("PA_D", result);
                }
                if(reportVO.getAmountPaE()!=null) {
                    BigDecimal result = footerMap.get("PA_E");
                    result = result.add(reportVO.getAmountPaE());
                    footerMap.put("PA_E", result);
                }
            }
        }
        
        return footerMap;
    }
    
    //改在ReportService 實作
    private Map<String,BigDecimal> initFooterMap(String bsisType){
        Map<String, BigDecimal> map = new HashMap<>();
        String[] cols;
        if(SheetTypeEnum.BS.getValue().equals(bsisType)){
            cols = BS_REPA_COLS;
        }else{
            cols = IS_REPA_COLS;
        }
        
        for(String col:cols){
            map.put(col, BigDecimal.ZERO);
        }
        return map;
    }
    
}
