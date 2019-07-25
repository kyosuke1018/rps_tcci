/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rs;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.et.enums.CurrencyCodeEnum;
import com.tcci.et.model.rfq.QuotationPmVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author penpl
 */
public class QuoteItemRsVO implements Serializable {

    private static final long serialVersionUID = 1L;
    // ET_RFQ_EKPO
    //private String mandt;
    //private String bukrs;// 公司代碼
    //private String werks;// 工廠
    private String pstyp;// 詢價文件中的項目種類 (9:服務類)
    
    private Long ebelp; // 詢價文件的項目號碼 (採購單)
    private String loekz; // 詢價文件的刪除指示碼
    private String matnr; // 物料號碼
    private String txz01; // 短文
    private BigDecimal rfqNetpr; // 詢價單單價
    private BigDecimal rfqMenge; // 詢價單數量 
    private Date rfqEindt;// EKET.EINDT 項目交貨日期
    
    //private String anfnr;
    //private Long anfps;
    //private String banfn;
    //private Long bnfpo;
    
    // ET_QUOTATION_ITEM
    private Long id; // PK ID
    private Long tenderId; // 標案 ID
    private Long rfqId; // FK ET_RFQ_EKKO.ID
    private Long quoteId; // FK: ET_QUOTATION
    
    // (MENGE / PEINH) * NETPR = BRTWR
    private BigDecimal menge; // 報價數量
    private String meins; // 訂購單位
    private BigDecimal netpr; // 單價 - 報價淨價（以文件貨幣計算）
    private BigDecimal peinh; // 價格單位
    //private BigDecimal netwr; // 以詢價單貨幣計算的訂單淨值 X
    private BigDecimal brtwr; // 以詢價單貨幣計算總訂購值
    
    private Date eindt;//  廠商可交貨日期
    
    //private String memo; // 備註
    
    //private Long creatorId; // 建立人
    //private Date createtime; // 建立時間
    //private Long modifierId; // 修改人
    //private Date modifytime; // 修改時間

    private Long venderId;// 供應商ID
    private boolean winning; // 得標
    private BigDecimal winningQuantity;// 得標數量
    
    private boolean hasQuote;// 此項目有報價
    
    // for UI
    private String curQuo; // 報價幣別
    private String matnrUI;
    //private boolean selected;
    //private boolean curSrvItem = false;// 目前操作中服務類明細
    //private boolean editSrvItems = false;// 有編輯服務類明細
    private List<QuotePmRsVO> pmList;
    
    public boolean isSrvItem(){// 是否是服務類項目
        return GlobalConstant.PSTYP_SERVICE.equals(this.pstyp);
    }
    
    public CurrencyCodeEnum getCurrency(){
        CurrencyCodeEnum cur = CurrencyCodeEnum.getFromCode(curQuo);
        return cur;
    }
    
    public BigDecimal getTotalBudget(){// 預算總價(RFQ幣別)
        if( rfqMenge == null || peinh==null || peinh.longValue()==0 || rfqNetpr==null ){
            return null;
        }
        return rfqMenge.divide(peinh).multiply(rfqNetpr);//.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    public BigDecimal getTotalPriceQuote(){// 總價(報價幣別、報價單數量)
        if( menge == null || peinh==null || peinh.longValue()==0 || netpr==null ){
            return null;
        }
        return menge.divide(peinh).multiply(netpr);
    }

    public BigDecimal getTotalPriceRfq(){// 總價(報價幣別、RFQ數量)
        if( menge == null || peinh==null || peinh.longValue()==0 || netpr==null ){
            return null;
        }
        return menge.divide(peinh).multiply(netpr);
    }
    
    public BigDecimal getTotalPriceAward(){// 總價(報價幣別、得標單數量)
        if( winningQuantity == null || peinh==null || peinh.longValue()==0 || netpr==null ){
            return null;
        }
        return winningQuantity.divide(peinh).multiply(netpr);
    }
    
    public void setMatnr(String matnr) {
        this.matnr = matnr;
        if( matnr!=null ){
            matnrUI = matnr.startsWith("000000")?matnr.substring(6):matnr;
        }else{
            matnrUI = null;
        }
    }

    public QuoteItemRsVO() {
    }

    public QuoteItemRsVO(Long id) {
        this.id = id;
    }

    public BigDecimal getRfqNetpr() {
        return rfqNetpr;
    }

    public void setRfqNetpr(BigDecimal rfqNetpr) {
        this.rfqNetpr = rfqNetpr;
    }

    public List<QuotePmRsVO> getPmList() {
        return pmList;
    }

    public void setPmList(List<QuotePmRsVO> pmList) {
        this.pmList = pmList;
    }

    public String getPstyp() {
        return pstyp;
    }

    public void setPstyp(String pstyp) {
        this.pstyp = pstyp;
    }

    public BigDecimal getRfqMenge() {
        return rfqMenge;
    }

    public void setRfqMenge(BigDecimal rfqMenge) {
        this.rfqMenge = rfqMenge;
    }

    public String getCurQuo() {
        return curQuo;
    }

    public void setCurQuo(String curQuo) {
        this.curQuo = curQuo;
    }

    public Date getRfqEindt() {
        return rfqEindt;
    }

    public void setRfqEindt(Date rfqEindt) {
        this.rfqEindt = rfqEindt;
    }

    public Date getEindt() {
        return eindt;
    }

    public void setEindt(Date eindt) {
        this.eindt = eindt;
    }

    public String getMatnrUI() {
        return matnrUI;
    }

    public void setMatnrUI(String matnrUI) {
        this.matnrUI = matnrUI;
    }

    public boolean isHasQuote() {
        return hasQuote;
    }

    public void setHasQuote(boolean hasQuote) {
        this.hasQuote = hasQuote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public boolean isWinning() {
        return winning;
    }

    public void setWinning(boolean winning) {
        this.winning = winning;
    }

    public BigDecimal getWinningQuantity() {
        return winningQuantity;
    }

    public void setWinningQuantity(BigDecimal winningQuantity) {
        this.winningQuantity = winningQuantity;
    }

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public Long getRfqId() {
        return rfqId;
    }

    public void setRfqId(Long rfqId) {
        this.rfqId = rfqId;
    }

    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public Long getEbelp() {
        return ebelp;
    }

    public void setEbelp(Long ebelp) {
        this.ebelp = ebelp;
    }

    public String getLoekz() {
        return loekz;
    }

    public void setLoekz(String loekz) {
        this.loekz = loekz;
    }

    public String getMatnr() {
        return matnr;
    }

    public String getTxz01() {
        return txz01;
    }

    public void setTxz01(String txz01) {
        this.txz01 = txz01;
    }

    public BigDecimal getMenge() {
        return menge;
    }

    public void setMenge(BigDecimal menge) {
        this.menge = menge;
    }

    public String getMeins() {
        return meins;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public BigDecimal getNetpr() {
        return netpr;
    }

    public void setNetpr(BigDecimal netpr) {
        this.netpr = netpr;
    }

    public BigDecimal getPeinh() {
        return peinh;
    }

    public void setPeinh(BigDecimal peinh) {
        this.peinh = peinh;
    }

    public BigDecimal getBrtwr() {
        return brtwr;
    }

    public void setBrtwr(BigDecimal brtwr) {
        this.brtwr = brtwr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuoteItemRsVO)) {
            return false;
        }
        QuoteItemRsVO other = (QuoteItemRsVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.rs.QuoteItemRsVO[ id=" + id + " ]";
    }
    
}
