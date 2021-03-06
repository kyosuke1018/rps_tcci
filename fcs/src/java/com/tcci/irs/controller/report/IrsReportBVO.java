/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.irs.controller.report;

import com.tcci.irs.enums.SheetTypeEnum;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Kyle.Cheng
 */
public class IrsReportBVO implements Serializable {
    private String sheetType;//BS IS
    private String compCode;
    private String compName;
    private String comp2Code;
    private String comp2Name;
    private String comp2Rtype;//關係人公司關係
    //BS RE 資產 1161 1172 1181 1175 1206 1212 1300 1419 1421 1268 1479 1600 1920 1930 1990  (15 O)
    //IS RE 收入 4111 4114 4115 4116 4117 4119 4190 4300 4500 4600 4800 7100 7110 7130 7210 7010  (16 P)
    private BigDecimal amountReA;
    private BigDecimal amountReB;
    private BigDecimal amountReC;
    private BigDecimal amountReD;
    private BigDecimal amountReE;
    private BigDecimal amountReF;
    private BigDecimal amountReG;
    private BigDecimal amountReH;
    private BigDecimal amountReI;
    private BigDecimal amountReJ;
    private BigDecimal amountReK;
    private BigDecimal amountReL;
    private BigDecimal amountReM;
    private BigDecimal amountReN;
    private BigDecimal amountReO;
    private BigDecimal amountReP;
    //BS PA 負債 2100 2160 2170 2180 2200 2220 2310 2399 2620 2645 2670  (11 K)
    //IS PA 支出 5000 5800 6000 7510 7590  (5 E)
    private BigDecimal amountPaA;
    private BigDecimal amountPaB;
    private BigDecimal amountPaC;
    private BigDecimal amountPaD;
    private BigDecimal amountPaE;
    private BigDecimal amountPaF;
    private BigDecimal amountPaG;
    private BigDecimal amountPaH;
    private BigDecimal amountPaI;
    private BigDecimal amountPaJ;
    private BigDecimal amountPaK;
    
    //公司幣別金額
    private String compCurrCode;
    private String comp2CurrCode;
    
    
    //<editor-fold defaultstate="collapsed" desc="getter, setter">
    public String getSheetType() {
        return sheetType;
    }

    public void setSheetType(String sheetType) {
        this.sheetType = sheetType;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getComp2Code() {
        return comp2Code;
    }

    public void setComp2Code(String comp2Code) {
        this.comp2Code = comp2Code;
    }

    public String getComp2Name() {
        return comp2Name;
    }

    public void setComp2Name(String comp2Name) {
        this.comp2Name = comp2Name;
    }

    public String getComp2Rtype() {
        return comp2Rtype;
    }

    public void setComp2Rtype(String comp2Rtype) {
        this.comp2Rtype = comp2Rtype;
    }

    public String getCompCurrCode() {
        return compCurrCode;
    }

    public void setCompCurrCode(String compCurrCode) {
        this.compCurrCode = compCurrCode;
    }

    public String getComp2CurrCode() {
        return comp2CurrCode;
    }

    public void setComp2CurrCode(String comp2CurrCode) {
        this.comp2CurrCode = comp2CurrCode;
    }

    public BigDecimal getAmountReA() {
        return amountReA;
    }

    public void setAmountReA(BigDecimal amountReA) {
        this.amountReA = amountReA;
    }

    public BigDecimal getAmountReB() {
        return amountReB;
    }

    public void setAmountReB(BigDecimal amountReB) {
        this.amountReB = amountReB;
    }

    public BigDecimal getAmountReC() {
        return amountReC;
    }

    public void setAmountReC(BigDecimal amountReC) {
        this.amountReC = amountReC;
    }

    public BigDecimal getAmountReD() {
        return amountReD;
    }

    public void setAmountReD(BigDecimal amountReD) {
        this.amountReD = amountReD;
    }

    public BigDecimal getAmountReE() {
        return amountReE;
    }

    public void setAmountReE(BigDecimal amountReE) {
        this.amountReE = amountReE;
    }

    public BigDecimal getAmountReF() {
        return amountReF;
    }

    public void setAmountReF(BigDecimal amountReF) {
        this.amountReF = amountReF;
    }

    public BigDecimal getAmountReG() {
        return amountReG;
    }

    public void setAmountReG(BigDecimal amountReG) {
        this.amountReG = amountReG;
    }

    public BigDecimal getAmountReH() {
        return amountReH;
    }

    public void setAmountReH(BigDecimal amountReH) {
        this.amountReH = amountReH;
    }

    public BigDecimal getAmountReI() {
        return amountReI;
    }

    public void setAmountReI(BigDecimal amountReI) {
        this.amountReI = amountReI;
    }

    public BigDecimal getAmountReJ() {
        return amountReJ;
    }

    public void setAmountReJ(BigDecimal amountReJ) {
        this.amountReJ = amountReJ;
    }

    public BigDecimal getAmountReK() {
        return amountReK;
    }

    public void setAmountReK(BigDecimal amountReK) {
        this.amountReK = amountReK;
    }

    public BigDecimal getAmountReL() {
        return amountReL;
    }

    public void setAmountReL(BigDecimal amountReL) {
        this.amountReL = amountReL;
    }

    public BigDecimal getAmountReM() {
        return amountReM;
    }

    public void setAmountReM(BigDecimal amountReM) {
        this.amountReM = amountReM;
    }

    public BigDecimal getAmountReN() {
        return amountReN;
    }

    public void setAmountReN(BigDecimal amountReN) {
        this.amountReN = amountReN;
    }

    public BigDecimal getAmountReO() {
        return amountReO;
    }

    public void setAmountReO(BigDecimal amountReO) {
        this.amountReO = amountReO;
    }

    public BigDecimal getAmountReP() {
        return amountReP;
    }

    public void setAmountReP(BigDecimal amountReP) {
        this.amountReP = amountReP;
    }

    public BigDecimal getAmountPaA() {
        return amountPaA;
    }

    public void setAmountPaA(BigDecimal amountPaA) {
        this.amountPaA = amountPaA;
    }

    public BigDecimal getAmountPaB() {
        return amountPaB;
    }

    public void setAmountPaB(BigDecimal amountPaB) {
        this.amountPaB = amountPaB;
    }

    public BigDecimal getAmountPaC() {
        return amountPaC;
    }

    public void setAmountPaC(BigDecimal amountPaC) {
        this.amountPaC = amountPaC;
    }

    public BigDecimal getAmountPaD() {
        return amountPaD;
    }

    public void setAmountPaD(BigDecimal amountPaD) {
        this.amountPaD = amountPaD;
    }

    public BigDecimal getAmountPaE() {
        return amountPaE;
    }

    public void setAmountPaE(BigDecimal amountPaE) {
        this.amountPaE = amountPaE;
    }

    public BigDecimal getAmountPaF() {
        return amountPaF;
    }

    public void setAmountPaF(BigDecimal amountPaF) {
        this.amountPaF = amountPaF;
    }

    public BigDecimal getAmountPaG() {
        return amountPaG;
    }

    public void setAmountPaG(BigDecimal amountPaG) {
        this.amountPaG = amountPaG;
    }

    public BigDecimal getAmountPaH() {
        return amountPaH;
    }

    public void setAmountPaH(BigDecimal amountPaH) {
        this.amountPaH = amountPaH;
    }

    public BigDecimal getAmountPaI() {
        return amountPaI;
    }

    public void setAmountPaI(BigDecimal amountPaI) {
        this.amountPaI = amountPaI;
    }

    public BigDecimal getAmountPaJ() {
        return amountPaJ;
    }

    public void setAmountPaJ(BigDecimal amountPaJ) {
        this.amountPaJ = amountPaJ;
    }

    public BigDecimal getAmountPaK() {
        return amountPaK;
    }

    public void setAmountPaK(BigDecimal amountPaK) {
        this.amountPaK = amountPaK;
    }

    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="helper">
    public BigDecimal getTotalRe(){
        BigDecimal result = new BigDecimal("0");
        if(SheetTypeEnum.BS.getValue().equals(this.sheetType)){
            if(amountReA!=null) result = result.add(amountReA);
            if(amountReB!=null) result = result.add(amountReB);
            if(amountReC!=null) result = result.add(amountReC);
            if(amountReD!=null) result = result.add(amountReD);
            if(amountReE!=null) result = result.add(amountReE);
            if(amountReF!=null) result = result.add(amountReF);
            if(amountReG!=null) result = result.add(amountReG);
            if(amountReH!=null) result = result.add(amountReH);
            if(amountReI!=null) result = result.add(amountReI);
            if(amountReJ!=null) result = result.add(amountReJ);
            if(amountReK!=null) result = result.add(amountReK);
            if(amountReL!=null) result = result.add(amountReL);
            if(amountReM!=null) result = result.add(amountReM);
            if(amountReN!=null) result = result.add(amountReN);
            if(amountReO!=null) result = result.add(amountReO);
        }else{
            if(amountReA!=null) result = result.add(amountReA);
            if(amountReB!=null) result = result.add(amountReB);
            if(amountReC!=null) result = result.add(amountReC);
            if(amountReD!=null) result = result.add(amountReD);
            if(amountReE!=null) result = result.add(amountReE);
            if(amountReF!=null) result = result.add(amountReF);
            if(amountReG!=null) result = result.add(amountReG);
            if(amountReH!=null) result = result.add(amountReH);
            if(amountReI!=null) result = result.add(amountReI);
            if(amountReJ!=null) result = result.add(amountReJ);
            if(amountReK!=null) result = result.add(amountReK);
            if(amountReL!=null) result = result.add(amountReL);
            if(amountReM!=null) result = result.add(amountReM);
            if(amountReN!=null) result = result.add(amountReN);
            if(amountReO!=null) result = result.add(amountReO);
            if(amountReP!=null) result = result.add(amountReP);
        }
        return result;
    }
    
    public BigDecimal getTotalPa(){
        BigDecimal result = new BigDecimal("0");
        if(SheetTypeEnum.BS.getValue().equals(this.sheetType)){
            if(amountPaA!=null) result = result.add(amountPaA);
            if(amountPaB!=null) result = result.add(amountPaB);
            if(amountPaC!=null) result = result.add(amountPaC);
            if(amountPaD!=null) result = result.add(amountPaD);
            if(amountPaE!=null) result = result.add(amountPaE);
            if(amountPaF!=null) result = result.add(amountPaF);
            if(amountPaG!=null) result = result.add(amountPaG);
            if(amountPaH!=null) result = result.add(amountPaH);
            if(amountPaI!=null) result = result.add(amountPaI);
            if(amountPaJ!=null) result = result.add(amountPaJ);
            if(amountPaK!=null) result = result.add(amountPaK);
        }else{
            if(amountPaA!=null) result = result.add(amountPaA);
            if(amountPaB!=null) result = result.add(amountPaB);
            if(amountPaC!=null) result = result.add(amountPaC);
            if(amountPaD!=null) result = result.add(amountPaD);
            if(amountPaE!=null) result = result.add(amountPaE);
        }
        return result;
    }
    
    public BigDecimal getDiff(){
        BigDecimal result = new BigDecimal("0");
        result = result.add(this.getTotalRe()).add(this.getTotalPa());
        return result;
    }
    //</editor-fold>
}
