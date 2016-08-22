package com.tcci.sksp.entity.enums;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author Lynn.Huang
 */
public enum BankEnum {    
    CHINATRUST("S0006", "015540108800","BK0001"),
    FIRSTBANK("S0002", "16410000367","BK0002");
    private String code;
    private String account;
    private String generalLedgerCode;
    
    BankEnum(String code, String account, String generalLedgerCode) {
        this.code = code;
        this.account = account;
        this.generalLedgerCode= generalLedgerCode;
    }
    
    public String getDisplayName(){
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName() , this.toString());
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGeneralLedgerCode() {
        return generalLedgerCode;
    }

    public void setGeneralLedgerCode(String generalLedgerCode) {
        this.generalLedgerCode = generalLedgerCode;
    }
}
