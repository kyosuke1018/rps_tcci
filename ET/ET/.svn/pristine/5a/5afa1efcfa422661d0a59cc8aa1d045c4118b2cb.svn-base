package com.tcci.et.rfq.report;

import com.tcci.et.model.rs.BaseResponseVO;
import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

public class RFQPrintDtlVo extends BaseResponseVO implements Serializable {

    private Long id;
    @Column(name = "TENDER_ID")
    private Long tenderId;
    @Column(name = "RFQ_ID")
    private Long rfqId;
    @Size(max = 6)
    @Column(name = "MANDT")
    private String mandt;
    @Size(max = 20)
    @Column(name = "EBELN")
    private String ebeln;
    @Column(name = "EBELP")
    private Long ebelp;
    @Size(max = 2)
    @Column(name = "LOEKZ")
    private String loekz;
    @Size(max = 80)
    @Column(name = "TXZ01")
    private String txz01;
    @Size(max = 36)
    @Column(name = "MATNR")
    private String matnr;
    @Size(max = 8)
    @Column(name = "BUKRS")
    private String bukrs;
    @Size(max = 8)
    @Column(name = "WERKS")
    private String werks;
    @Size(max = 8)
    @Column(name = "LGORT")
    private String lgort;
    @Size(max = 20)
    @Column(name = "BEDNR")
    private String bednr;
    @Size(max = 18)
    @Column(name = "MATKL")
    private String matkl;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "MENGE")
    private BigDecimal menge;
    @Size(max = 6)
    @Column(name = "MEINS")
    private String meins;
    @Size(max = 6)
    @Column(name = "BPRME")
    private String bprme;
    @Column(name = "BPUMZ")
    private BigDecimal bpumz;
    @Column(name = "BPUMN")
    private BigDecimal bpumn;
    @Column(name = "NETPR")
    private BigDecimal netpr;
    @Column(name = "PEINH")
    private BigDecimal peinh;
    @Column(name = "NETWR")
    private BigDecimal netwr;
    @Column(name = "BRTWR")
    private BigDecimal brtwr;
    @Column(name = "UEBTO")
    private BigDecimal uebto;
    @Size(max = 2)
    @Column(name = "UEBTK")
    private String uebtk;
    @Column(name = "UNTTO")
    private BigDecimal untto;
    @Size(max = 20)
    @Column(name = "BWTAR")
    private String bwtar;
    @Size(max = 2)
    @Column(name = "ELIKZ")
    private String elikz;
    @Size(max = 2)
    @Column(name = "EREKZ")
    private String erekz;
    @Size(max = 2)
    @Column(name = "PSTYP")
    private String pstyp;
    @Size(max = 2)
    @Column(name = "KNTTP")
    private String knttp;
    @Size(max = 2)
    @Column(name = "WEPOS")
    private String wepos;
    @Size(max = 2)
    @Column(name = "REPOS")
    private String repos;
    @Size(max = 2)
    @Column(name = "WEBRE")
    private String webre;
    @Size(max = 20)
    @Column(name = "KONNR")
    private String konnr;
    @Column(name = "KTPNR")
    private Long ktpnr;
    @Size(max = 6)
    @Column(name = "INCO1")
    private String inco1;
    @Size(max = 56)
    @Column(name = "INCO2")
    private String inco2;
    @Size(max = 20)
    @Column(name = "ANFNR")
    private String anfnr;
    @Column(name = "ANFPS")
    private Long anfps;
    @Size(max = 20)
    @Column(name = "BANFN")
    private String banfn;
    @Column(name = "BNFPO")
    private Long bnfpo;
    @Size(max = 8)
    @Column(name = "MTART")
    private String mtart;

    @Column(name = "EINDT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eindt;// EKET.EINDT 項目交貨日期

    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEindt() {
        return eindt;
    }

    public void setEindt(Date eindt) {
        this.eindt = eindt;
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

    public void setMatnr(String matnr) {
        this.matnr = matnr;
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

    public Long getKtpnr() {
        return ktpnr;
    }

    public void setKtpnr(Long ktpnr) {
        this.ktpnr = ktpnr;
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

    public String getMtart() {
        return mtart;
    }

    public void setMtart(String mtart) {
        this.mtart = mtart;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }
}
