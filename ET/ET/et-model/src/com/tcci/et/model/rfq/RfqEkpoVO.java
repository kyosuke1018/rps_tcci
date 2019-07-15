/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rfq;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ET_RFQ_EKPO
 * @author Peter.pan
 */
public class RfqEkpoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id; // PK ID
    private Long tenderId; // 標案 ID
    private Long rfqId; // FK ET_RFQ_EKKO.ID
    private String mandt; // 用戶端
    private String ebeln; // 詢價文件號碼
    private Long ebelp; // 詢價文件的項目號碼 (採購單)
    private String loekz; // 詢價文件的刪除指示碼
    private String txz01; // 短文
    private String matnr; // 物料號碼
    private String bukrs; // 公司代碼
    private String werks; // 工廠
    private String lgort; // 儲存地點
    private String bednr; // 需求追蹤號碼
    private String matkl; // 物料群組
    // (MENGE / PEINH) * NETPR = BRTWR
    private BigDecimal menge; // 詢價單數量
    private String meins; // 訂購單位

    /////////////////
    private String bprme; // 訂單價格單位（採購）    
    private BigDecimal bpumz; // 將訂貨價格單位轉換為訂貨單位的分子
    private BigDecimal bpumn; // 將訂單價格單位轉換為訂單單位的分母值
    
    private BigDecimal netpr; // 單價 - 報價淨價（以文件貨幣計算）
    private BigDecimal peinh; // 價格單位 = 計價數量
    private BigDecimal netwr; // 單價 - 以詢價單貨幣計算的訂單淨值
    private BigDecimal brtwr; // 以詢價單貨幣計算總訂購值
    
    private BigDecimal uebto; // 過量交貨允差限制
    private String uebtk; // 指示碼：允許的無限制之超量交貨
    private BigDecimal untto; // 交貨不足允差限制
    private String bwtar; // 評價類型
    private String elikz; // 交貨完成 指示碼
    private String erekz; // 最後發票指示碼
    private String pstyp; // 詢價文件中的項目種類 (9:服務類)
    private String knttp; // 科目指派種類
    private String wepos; // 收貨指示碼
    private String repos; // 發票接收指示碼
    private String webre; // 指示碼：以 GR 為基礎的發票驗證
    private String konnr; // 主要採購協議的號碼
    private Integer ktpnr; // 主要採購購協議的項目號碼
    private String inco1; // 國貿條件 (第一部份)
    private String inco2; // 國貿條件（第 2 部分）
    private String anfnr; // RFQ 號碼
    private Integer anfps; // RFQ 的項目號碼
    /////////////////
    
    private String banfn; // 請購單號碼
    private Long bnfpo; // 請購單的項目號碼
    private String mtart; // 物料類型
    private Long creatorId; // 建立人
    private Date createtime; // 建立時間
    private Long modifierId; // 修改人
    private Date modifytime; // 修改時間
    
    // RFQ EKKO
    private String waers; // 幣別碼
    // RFQ EKET
    private Date eindt;// EKET.EINDT 項目交貨日期
    // PR EKPO
    private String afnam;// 申請人
    private BigDecimal preis;// 請購單中的價格
    
    // 報價
    private BigDecimal quoteMenge;// 報價數量
    
    private String matnrUI;
    private BigDecimal awardQuantity = BigDecimal.ZERO;// 決標數量
    
    public RfqEkpoVO() {
    }

    public RfqEkpoVO(Long id) {
        this.id = id;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
        if( matnr!=null ){
            matnrUI = matnr.startsWith("000000")?matnr.substring(6):matnr;
        }else{
            matnrUI = null;
        }
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
    }

    public BigDecimal getAwardQuantity() {
        return awardQuantity;
    }

    public void setAwardQuantity(BigDecimal awardQuantity) {
        this.awardQuantity = awardQuantity;
    }

    public BigDecimal getQuoteMenge() {
        return quoteMenge;
    }

    public void setQuoteMenge(BigDecimal quoteMenge) {
        this.quoteMenge = quoteMenge;
    }

    public String getAfnam() {
        return afnam;
    }

    public void setAfnam(String afnam) {
        this.afnam = afnam;
    }

    public BigDecimal getPreis() {
        return preis;
    }

    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

    public Integer getKtpnr() {
        return ktpnr;
    }

    public void setKtpnr(Integer ktpnr) {
        this.ktpnr = ktpnr;
    }

    public Integer getAnfps() {
        return anfps;
    }

    public void setAnfps(Integer anfps) {
        this.anfps = anfps;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getEbeln() {
        return ebeln;
    }

    public void setEbeln(String ebeln) {
        this.ebeln = ebeln;
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

    public String getTxz01() {
        return txz01;
    }

    public void setTxz01(String txz01) {
        this.txz01 = txz01;
    }

    public String getMatnr() {
        return matnr;
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

    public String getLgort() {
        return lgort;
    }

    public void setLgort(String lgort) {
        this.lgort = lgort;
    }

    public String getBednr() {
        return bednr;
    }

    public void setBednr(String bednr) {
        this.bednr = bednr;
    }

    public String getMatkl() {
        return matkl;
    }

    public void setMatkl(String matkl) {
        this.matkl = matkl;
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

    public String getBprme() {
        return bprme;
    }

    public void setBprme(String bprme) {
        this.bprme = bprme;
    }

    public BigDecimal getBpumz() {
        return bpumz;
    }

    public void setBpumz(BigDecimal bpumz) {
        this.bpumz = bpumz;
    }

    public BigDecimal getBpumn() {
        return bpumn;
    }

    public void setBpumn(BigDecimal bpumn) {
        this.bpumn = bpumn;
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

    public BigDecimal getUebto() {
        return uebto;
    }

    public void setUebto(BigDecimal uebto) {
        this.uebto = uebto;
    }

    public String getUebtk() {
        return uebtk;
    }

    public void setUebtk(String uebtk) {
        this.uebtk = uebtk;
    }

    public BigDecimal getUntto() {
        return untto;
    }

    public void setUntto(BigDecimal untto) {
        this.untto = untto;
    }

    public String getBwtar() {
        return bwtar;
    }

    public void setBwtar(String bwtar) {
        this.bwtar = bwtar;
    }

    public String getElikz() {
        return elikz;
    }

    public void setElikz(String elikz) {
        this.elikz = elikz;
    }

    public String getErekz() {
        return erekz;
    }

    public void setErekz(String erekz) {
        this.erekz = erekz;
    }

    public String getPstyp() {
        return pstyp;
    }

    public void setPstyp(String pstyp) {
        this.pstyp = pstyp;
    }

    public String getKnttp() {
        return knttp;
    }

    public void setKnttp(String knttp) {
        this.knttp = knttp;
    }

    public String getWepos() {
        return wepos;
    }

    public void setWepos(String wepos) {
        this.wepos = wepos;
    }

    public String getRepos() {
        return repos;
    }

    public void setRepos(String repos) {
        this.repos = repos;
    }

    public String getWebre() {
        return webre;
    }

    public void setWebre(String webre) {
        this.webre = webre;
    }

    public String getKonnr() {
        return konnr;
    }

    public void setKonnr(String konnr) {
        this.konnr = konnr;
    }

    public String getInco1() {
        return inco1;
    }

    public void setInco1(String inco1) {
        this.inco1 = inco1;
    }

    public String getInco2() {
        return inco2;
    }

    public void setInco2(String inco2) {
        this.inco2 = inco2;
    }

    public String getAnfnr() {
        return anfnr;
    }

    public void setAnfnr(String anfnr) {
        this.anfnr = anfnr;
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

    public String getMtart() {
        return mtart;
    }

    public void setMtart(String mtart) {
        this.mtart = mtart;
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
        if (!(object instanceof RfqEkpoVO)) {
            return false;
        }
        RfqEkpoVO other = (RfqEkpoVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.RfqEkpoVO[ id=" + id + " ]";
    }
    
}