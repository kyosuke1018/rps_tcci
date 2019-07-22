/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rfq;;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.et.enums.CurrencyCodeEnum;
import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ET_AWARD_ITEM
 * @author Peter.pan
 */
public class AwardItemVO implements Serializable {
    private static final long serialVersionUID = 1L;

    // ET_AWARD_ITEM
    private Long id;
    private Long awardId;
    private Long tenderId;
    private Long rfqId;
    private Long venderId;
    private Long quoteId;
    private Long ebelp; // 詢價文件的項目號碼
    //private BigDecimal menge;// 得標數量
    private BigDecimal winningQuantity;// 得標數量
    
    private Boolean disabled;
    private String pstyp; // 詢價文件中的項目種類 (9:服務類)
    
    private TcUser creatorId;
    private Date createtime;
    private TcUser modifierId;
    private Date modifytime;
    
    // ET_AWARD
    private String awardCode;
    private Date awardTime;
    private String awardStatus;
    
    // ET_QUOTATION_ITEM
    // (MENGE / PEINH) * NETPR = BRTWR
    private BigDecimal menge; // 報價數量
    private String meins; // 訂購單位
    private BigDecimal netpr; // 單價 - 報價淨價（以文件貨幣計算）
    private BigDecimal peinh; // 價格單位
    private BigDecimal brtwr; // 以詢價單貨幣計算總訂購值
    private Date eindt;//  廠商可交貨日期    
    private String memo; // 備註
    
    // ET_RFQ_EKPO
    private String mandt;
    private String bukrs;// 公司代碼
    private String werks;// 工廠
    //private String pstyp;// 詢價文件中的項目種類 (9:服務類)
    //private Long ebelp; // 詢價文件的項目號碼 (採購單)
    private String loekz; // 詢價文件的刪除指示碼
    private String matnr; // 物料號碼
    private String txz01; // 短文
    private BigDecimal rfqNetpr; // 詢價單單價
    private BigDecimal rfqMenge; // 詢價單數量 
    private Date rfqEindt;// EKET.EINDT 項目交貨日期
    private String anfnr;
    private Long anfps;
    private String banfn;
    private Long bnfpo;
    
    // for UI
    private String curQuo; // 報價幣別
    private String matnrUI;
    private boolean selected;
    private boolean curSrvItem = false;// 目前操作中服務類明細
    
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

    public AwardItemVO() {
    }

    public AwardItemVO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getWinningQuantity() {
        return winningQuantity;
    }

    public void setWinningQuantity(BigDecimal winningQuantity) {
        this.winningQuantity = winningQuantity;
    }

    public BigDecimal getRfqNetpr() {
        return rfqNetpr;
    }

    public void setRfqNetpr(BigDecimal rfqNetpr) {
        this.rfqNetpr = rfqNetpr;
    }

    public String getAwardCode() {
        return awardCode;
    }

    public void setAwardCode(String awardCode) {
        this.awardCode = awardCode;
    }

    public Date getAwardTime() {
        return awardTime;
    }

    public void setAwardTime(Date awardTime) {
        this.awardTime = awardTime;
    }

    public String getAwardStatus() {
        return awardStatus;
    }

    public void setAwardStatus(String awardStatus) {
        this.awardStatus = awardStatus;
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

    public Date getEindt() {
        return eindt;
    }

    public void setEindt(Date eindt) {
        this.eindt = eindt;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getBukrs() {
        return bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
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

    public BigDecimal getRfqMenge() {
        return rfqMenge;
    }

    public void setRfqMenge(BigDecimal rfqMenge) {
        this.rfqMenge = rfqMenge;
    }

    public Date getRfqEindt() {
        return rfqEindt;
    }

    public void setRfqEindt(Date rfqEindt) {
        this.rfqEindt = rfqEindt;
    }

    public String getAnfnr() {
        return anfnr;
    }

    public void setAnfnr(String anfnr) {
        this.anfnr = anfnr;
    }

    public Long getAnfps() {
        return anfps;
    }

    public void setAnfps(Long anfps) {
        this.anfps = anfps;
    }

    public String getBanfn() {
        return banfn;
    }

    public void setBanfn(String banfn) {
        this.banfn = banfn;
    }

    public Long getBnfpo() {
        return bnfpo;
    }

    public void setBnfpo(Long bnfpo) {
        this.bnfpo = bnfpo;
    }

    public String getCurQuo() {
        return curQuo;
    }

    public void setCurQuo(String curQuo) {
        this.curQuo = curQuo;
    }

    public String getMatnrUI() {
        return matnrUI;
    }

    public void setMatnrUI(String matnrUI) {
        this.matnrUI = matnrUI;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isCurSrvItem() {
        return curSrvItem;
    }

    public void setCurSrvItem(boolean curSrvItem) {
        this.curSrvItem = curSrvItem;
    }

    public Long getEbelp() {
        return ebelp;
    }

    public void setEbelp(Long ebelp) {
        this.ebelp = ebelp;
    }

    public String getPstyp() {
        return pstyp;
    }

    public void setPstyp(String pstyp) {
        this.pstyp = pstyp;
    }

    public Long getAwardId() {
        return awardId;
    }

    public void setAwardId(Long awardId) {
        this.awardId = awardId;
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

    public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public BigDecimal getMenge() {
        return menge;
    }

    public void setMenge(BigDecimal menge) {
        this.menge = menge;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public TcUser getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(TcUser creatorId) {
        this.creatorId = creatorId;
    }

    public TcUser getModifierId() {
        return modifierId;
    }

    public void setModifierId(TcUser modifierId) {
        this.modifierId = modifierId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
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
        if (!(object instanceof AwardItemVO)) {
            return false;
        }
        AwardItemVO other = (AwardItemVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.rfq.AwardItemVO[ id=" + id + " ]";
    }
    
}
