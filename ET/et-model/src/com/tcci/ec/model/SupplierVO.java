/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import java.util.Date;

/**
 *
 * @author gilbert
 */
//public class SupplierVO extends BaseResultVO {
public class SupplierVO {
    private String mandt;//SAP Client
    private String freezeCode;//凍結代碼
    private String supplier;//供應商
    private String supplierName;//供應商名稱
    private String supplierDesc;//廠商類型說明
    private String procurementOrg;//採購組織
    private String procurementOrgDesc;//採購組織說明
    private String salesman;//銷售員
    private String phone;//電話
    private String fax; // 傳真
    private String email;//銷售員e-mail
    private String orderCurrency;//訂單貨幣     
    private String paymentTerms;//付款條件
    private String city;//城市  
    private String street;//街道  
    private String country;//國家    
    private String search;//搜尋條件
    private String purchasingOrganizationFlag;//採購組織的刪除旗標
    
    private Date lastTradeDate; // 最近交易日
    private String orglist; //採購組織合併顯示
    private String ebeln; //最近採購單號
    
    private boolean canSelect = true;
    private String risk; // 高風險(Y/N)
    private String csperm;//中央採購凍結("X"->代表凍結)
    private String sperm;//採購組織層次的採購凍結("X"->代表凍結)
    private String sperq;//凍結原因
    private String sperqDesc;//凍結原因說明 90系統凍結 99黑名單
    
    private String zartiPerson;
    private String zartiPerson1;// 法人
    private String zartiPerson2;
    private String zartiPerson3;
    
    private String zstockholder;
    private String zstockholder1;// 股東
    private String zstockholder2;
    private String zstockholder3;
    private String zstockholder4;
    private String zstockholder5;
    
    private Date syncTimeStamp;
    
    public String getCbCodeNameOrgs(){// 組合欄位 供應商代碼、名稱、採購組織合併
        StringBuilder sb = new StringBuilder().append(supplier).append(supplierName).append(orglist);
        return sb.toString();
    }
    
    public void genZartiPerson(){
        StringBuilder sb = new StringBuilder();
        if( zartiPerson1!=null ){ sb.append(zartiPerson1); }
        if( zartiPerson2!=null ){ sb.append(sb.toString().isEmpty()?"":"/").append(zartiPerson2); }
        if( zartiPerson3!=null ){ sb.append(sb.toString().isEmpty()?"":"/").append(zartiPerson3); }
        zartiPerson = sb.toString();
    }

    public void genZstockholder(){
        StringBuilder sb = new StringBuilder();
        if( zstockholder1!=null ){ sb.append(zstockholder1); }
        if( zstockholder2!=null ){ sb.append(sb.toString().isEmpty()?"":"/").append(zstockholder2); }
        if( zstockholder3!=null ){ sb.append(sb.toString().isEmpty()?"":"/").append(zstockholder3); }
        if( zstockholder4!=null ){ sb.append(sb.toString().isEmpty()?"":"/").append(zstockholder4); }
        if( zstockholder5!=null ){ sb.append(sb.toString().isEmpty()?"":"/").append(zstockholder5); }
        zstockholder = sb.toString();
    }

    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }
    
    public boolean isCanSelect() {
        return canSelect;
    }

    public void setCanSelect(boolean canSelect) {
        this.canSelect = canSelect;
    }

    public String getZartiPerson() {
        return zartiPerson;
    }

    public void setZartiPerson(String zartiPerson) {
        this.zartiPerson = zartiPerson;
    }

    public String getZstockholder() {
        return zstockholder;
    }

    public void setZstockholder(String zstockholder) {
        this.zstockholder = zstockholder;
    }

    public String getZartiPerson1() {
        return zartiPerson1;
    }

    public void setZartiPerson1(String zartiPerson1) {
        this.zartiPerson1 = zartiPerson1;
        if( zartiPerson1!=null && !zartiPerson1.isEmpty() ){
            genZartiPerson();
        }
    }

    public String getZartiPerson2() {
        return zartiPerson2;
    }

    public void setZartiPerson2(String zartiPerson2) {
        this.zartiPerson2 = zartiPerson2;
        if( zartiPerson2!=null && !zartiPerson2.isEmpty() ){
            genZartiPerson();
        }
    }

    public String getZartiPerson3() {
        return zartiPerson3;
    }

    public void setZartiPerson3(String zartiPerson3) {
        this.zartiPerson3 = zartiPerson3;
        if( zartiPerson3!=null && !zartiPerson3.isEmpty() ){
            genZartiPerson();
        }
    }

    public String getZstockholder1() {
        return zstockholder1;
    }

    public void setZstockholder1(String zstockholder1) {
        this.zstockholder1 = zstockholder1;
        if( zstockholder1!=null && !zstockholder1.isEmpty() ){
            genZstockholder();
        }
    }

    public String getZstockholder2() {
        return zstockholder2;
    }

    public void setZstockholder2(String zstockholder2) {
        this.zstockholder2 = zstockholder2;
        if( zstockholder2!=null && !zstockholder2.isEmpty() ){
            genZstockholder();
        }
    }

    public String getZstockholder3() {
        return zstockholder3;
    }

    public void setZstockholder3(String zstockholder3) {
        this.zstockholder3 = zstockholder3;
        if( zstockholder3!=null && !zstockholder3.isEmpty() ){
            genZstockholder();
        }
    }

    public String getZstockholder4() {
        return zstockholder4;
    }

    public void setZstockholder4(String zstockholder4) {
        this.zstockholder4 = zstockholder4;
        if( zstockholder4!=null && !zstockholder4.isEmpty() ){
            genZstockholder();
        }
    }

    public String getZstockholder5() {
        return zstockholder5;
    }

    public void setZstockholder5(String zstockholder5) {
        this.zstockholder5 = zstockholder5;
        if( zstockholder5!=null && !zstockholder5.isEmpty() ){
            genZstockholder();
        }
    }

    public String getCsperm() {
        return csperm;
    }

    public void setCsperm(String csperm) {
        this.csperm = csperm;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEbeln() {
        return ebeln;
    }

    public void setEbeln(String ebeln) {
        this.ebeln = ebeln;
    }

    public String getOrglist() {
        return orglist;
    }

    public void setOrglist(String orglist) {
        this.orglist = orglist;
    }

    public Date getLastTradeDate() {
        return lastTradeDate;
    }

    public void setLastTradeDate(Date lastTradeDate) {
        this.lastTradeDate = lastTradeDate;
    }

    public String getSupplierDesc() {
        return supplierDesc;
    }

    public void setSupplierDesc(String supplierDesc) {
        this.supplierDesc = supplierDesc;
    }
    
    public String getSupplier() {
        return supplier;
    }
    
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    public String getSupplierName() {
        return supplierName;
    }
    
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    
    public String getSalesman() {
        return salesman;
    }
    
    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getProcurementOrg() {
        return procurementOrg;
    }
    
    public void setProcurementOrg(String procurementOrg) {
        this.procurementOrg = procurementOrg;
    }
    
    public String getProcurementOrgDesc() {
        return procurementOrgDesc;
    }
    
    public void setProcurementOrgDesc(String procurementOrgDesc) {
        this.procurementOrgDesc = procurementOrgDesc;
    }
    
    public String getPaymentTerms() {
        return paymentTerms;
    }
    
    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }
    
    public String getOrderCurrency() {
        return orderCurrency;
    }
    
    public void setOrderCurrency(String orderCurrency) {
        this.orderCurrency = orderCurrency;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }

    public String getFreezeCode() {
        return freezeCode;
    }

    public void setFreezeCode(String freezeCode) {
        this.freezeCode = freezeCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getPurchasingOrganizationFlag() {
        return purchasingOrganizationFlag;
    }

    public void setPurchasingOrganizationFlag(String purchasingOrganizationFlag) {
        this.purchasingOrganizationFlag = purchasingOrganizationFlag;
    }

    public String getSperq() {
        return sperq;
    }

    public void setSperq(String sperq) {
        this.sperq = sperq;
//        if (StringUtils.isNotBlank(sperq)) {
//            SupplierSperqEnum enum1 = SupplierSperqEnum.getFromCode(sperq);
//            this.sperqDesc = (enum1==null)? "":enum1.getName();
//        }
    }

    public String getSperqDesc() {
        return sperqDesc;
    }

    public String getSperm() {
        return sperm;
    }

    public void setSperm(String sperm) {
        this.sperm = sperm;
    }
    
    public Date getSyncTimeStamp() {
        return syncTimeStamp;
    }

    public void setSyncTimeStamp(Date syncTimeStamp) {
        this.syncTimeStamp = syncTimeStamp;
    }    

    //</editor-fold>

    /**
     * 依供應商代碼識別
     * @param o
     * @return 
     */
//    @Override
//    public int compareTo(Object o) {
//        return supplier.compareTo(((SupplierVO)o).getSupplier());
//    }
    
    /**
     *  資料更新時間
     * @return 
     */
//    public String getSyncTimeStampStr() {
//        if (null==syncTimeStamp){
//            return "";
//        }
//        return "資料更新時間: "+DateUtils.getCommonDateTimeStr(syncTimeStamp);
//    }    
}
