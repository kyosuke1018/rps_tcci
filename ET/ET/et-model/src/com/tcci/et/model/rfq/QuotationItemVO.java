/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rfq;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.et.enums.CurrencyCodeEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * ET_QUOTATION_ITEM
 * @author Peter.pan
 */
public class QuotationItemVO implements Serializable {

    private static final long serialVersionUID = 1L;
    // RFQ EKPO
    private String mandt;
    private String bukrs;// 公司代碼
    private String werks;// 工廠
    private String pstyp;// 詢價文件中的項目種類 (9:服務類)
    private String anfnr;
    private Long anfps;
    private String banfn;
    private Long bnfpo;
    
    private BigDecimal rfqMenge; // 詢價單數量 
    private Date rfqEindt;// EKET.EINDT 項目交貨日期
    
    ////////////
    private Long id; // PK ID
    private Long tenderId; // 標案 ID
    private Long rfqId; // FK ET_RFQ_EKKO.ID
    private Long quoteId; // FK: ET_QUOTATION
    private Long ebelp; // 詢價文件的項目號碼 (採購單)
    private String loekz; // 詢價文件的刪除指示碼
    private String matnr; // 物料號碼
    private String txz01; // 短文
    
    // (MENGE / PEINH) * NETPR = BRTWR
    private BigDecimal menge; // 報價數量
    private String meins; // 訂購單位
    private BigDecimal netpr; // 單價 - 報價淨價（以文件貨幣計算）
    private BigDecimal peinh; // 價格單位
    private BigDecimal netwr; // 以詢價單貨幣計算的訂單淨值 X
    private BigDecimal brtwr; // 以詢價單貨幣計算總訂購值
    
    private Date eindt;//  廠商可交貨日期
    
    private String memo; // 備註
    
    private Long creatorId; // 建立人
    private Date createtime; // 建立時間
    private Long modifierId; // 修改人
    private Date modifytime; // 修改時間

    private Long venderId;// 供應商ID
    private boolean winning; // 得標
    private BigDecimal winningQuantity;// 得標數量
    
    private boolean hasQuote;// 此項目有報價
    
    // for UI
    private String curQuo; // 報價幣別
    private String matnrUI;
    private boolean selected;
    private boolean curSrvItem = false;// 目前操作中服務類明細
    private boolean editSrvItems = false;// 有編輯服務類明細
    private List<QuotationPmVO> pmList;
    
    public boolean isSrvItem(){// 是否是服務類項目
        return GlobalConstant.PSTYP_SERVICE.equals(this.pstyp);
    }
    
    public CurrencyCodeEnum getCurrency(){
        CurrencyCodeEnum cur = CurrencyCodeEnum.getFromCode(curQuo);
        return cur;
    }
    
    public BigDecimal getTotalPrice(){// 報價幣別 - 總價
        if( menge == null || peinh==null || peinh.longValue()==0 || netpr==null ){
            return null;
        }
        return menge.divide(peinh).multiply(netpr);//.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    public void setMatnr(String matnr) {
        this.matnr = matnr;
        if( matnr!=null ){
            matnrUI = matnr.startsWith("000000")?matnr.substring(6):matnr;
        }else{
            matnrUI = null;
        }
    }

    public QuotationItemVO() {
    }

    public QuotationItemVO(Long id) {
        this.id = id;
    }

    public boolean isCurSrvItem() {
        return curSrvItem;
    }

    public void setCurSrvItem(boolean curSrvItem) {
        this.curSrvItem = curSrvItem;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<QuotationPmVO> getPmList() {
        return pmList;
    }

    public void setPmList(List<QuotationPmVO> pmList) {
        this.pmList = pmList;
    }

    public boolean isEditSrvItems() {
        return editSrvItems;
    }

    public void setEditSrvItems(boolean editSrvItems) {
        this.editSrvItems = editSrvItems;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getPstyp() {
        return pstyp;
    }

    public void setPstyp(String pstyp) {
        this.pstyp = pstyp;
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

    public String getBukrs() {
        return bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
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

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
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

    public BigDecimal getNetwr() {
        return netwr;
    }

    public void setNetwr(BigDecimal netwr) {
        this.netwr = netwr;
    }

    public BigDecimal getBrtwr() {
        return brtwr;
    }

    public void setBrtwr(BigDecimal brtwr) {
        this.brtwr = brtwr;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
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
        if (!(object instanceof QuotationItemVO)) {
            return false;
        }
        QuotationItemVO other = (QuotationItemVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.QuotationItemVO[ id=" + id + " ]";
    }
    
}
