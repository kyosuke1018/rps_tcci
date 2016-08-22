package com.tcci.worklist.entity.datawarehouse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nEO.Fu
 */
@Entity
@Cacheable(false)
@Table(name = "SD_ZTAB_EXP_RELFILENO_SD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SdZtabExpRelfilenoSd.findAll", query = "SELECT s FROM ZtabExpRelfilenoSd s")})
public class ZtabExpRelfilenoSd implements Serializable {

    @Column(name = "CREATE_TIME_STAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTimeStamp;
    @Column(name = "DELETED")
    private Boolean deleted = false;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_MG_ZST_EXP_RELFILENO", sequenceName = "SEQ_MG_ZST_EXP_RELFILENO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MG_ZST_EXP_RELFILENO")
    private Long id;
    @Size(max = 20)
    @Column(name = "MANDT")
    private String mandt;
    @Size(max = 20)
    @Column(name = "BNAME")
    private String bname;
    @Size(max = 24)
    @Column(name = "BERSL")
    private String bersl;
    @Size(max = 8)
    @Column(name = "VKORG")
    private String vkorg;
    @Size(max = 4)
    @Column(name = "VTWEG")
    private String vtweg;
    @Size(max = 4)
    @Column(name = "SPART")
    private String spart;
    @Size(max = 6)
    @Column(name = "VKGRP")
    private String vkgrp;
    @Size(max = 20)
    @Column(name = "VBELN")
    private String vbeln;
    @Size(max = 8)
    @Column(name = "AUART")
    private String auart;
    @Column(name = "AUDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date audat;
    @Column(name = "POSNR")
    private Long posnr;
    @Size(max = 36)
    @Column(name = "MATNR")
    private String matnr;
    @Size(max = 80)
    @Column(name = "ARKTX")
    private String arktx;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "KWMENG")
    private BigDecimal kwmeng;
    @Size(max = 6)
    @Column(name = "VRKME")
    private String vrkme;
    @Column(name = "UNIT_PRICE")
    private BigDecimal unitPrice;
    @Column(name = "NETWR")
    private BigDecimal netwr;
    @Size(max = 10)
    @Column(name = "WAERK")
    private String waerk;
    @Column(name = "DISCOUNT")
    private BigDecimal discount;
    @Column(name = "FREE")
    private BigDecimal free;
    @Size(max = 20)
    @Column(name = "KUNNR")
    private String kunnr;
    @Size(max = 70)
    @Column(name = "NAME1")
    private String name1;
    @Size(max = 10)
    @Column(name = "DBRTG")
    private String dbrtg;
    @Size(max = 60)
    @Column(name = "NAME")
    private String name;
    @Column(name = "ASP")
    private BigDecimal asp;
    @Column(name = "ASP_S")
    private BigDecimal aspS;
    @Column(name = "VPRS")
    private BigDecimal vprs;
    @Column(name = "ITMPF")
    private BigDecimal itmpf;
    @Column(name = "ITMDN")
    private BigDecimal itmdn;
    @Column(name = "QTYDF")
    private BigDecimal qtydf;
    @Column(name = "PRMNG")
    private BigDecimal prmng;
    @Column(name = "PRASP")
    private BigDecimal prasp;
    @Column(name = "PRASP_S")
    private BigDecimal praspS;
    @Size(max = 8)
    @Column(name = "BSTZD")
    private String bstzd;
    @Size(max = 40)
    @Column(name = "BSTZD_T")
    private String bstzdT;
    @Size(max = 44)
    @Column(name = "OBJNR")
    private String objnr;
    @Size(max = 16)
    @Column(name = "STSMA")
    private String stsma;
    @Size(max = 10)
    @Column(name = "CSTAT")
    private String cstat;
    @Size(max = 10)
    @Column(name = "NSTAT")
    private String nstat;
    @Size(max = 180)
    @Column(name = "REL_TEXT")
    private String relText;
    @Size(max = 30)
    @Column(name = "ALTKN")
    private String altkn;
    @Size(max = 30)
    @Column(name = "PSTLZ")
    private String pstlz;
    @Size(max = 105)
    @Column(name = "ORT01")
    private String ort01;
    @OneToMany(mappedBy = "sdZtabExpRelfilenoSdId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ZtabExpVbak> ztabExpVbakList;
    @OneToMany(mappedBy = "sdZtabExpRelfilenoSdId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ZtabExpVbap> ztabExpVbapList;
    @OneToMany(mappedBy = "sdZtabExpRelfilenoSdId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ZtabExpVbkd> ztabExpVbkdList;
    @OneToMany(mappedBy = "sdZtabExpRelfilenoSdId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ZtabExpVbpa> ztabExpVbpaList;
    @OneToMany(mappedBy = "sdZtabExpRelfilenoSdId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ZtabExpVbuk> ztabExpVbukList;

    public ZtabExpRelfilenoSd() {
    }

    public ZtabExpRelfilenoSd(Long id) {
        this.id = id;
    }

    public Date getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Date createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getBersl() {
        return bersl;
    }

    public void setBersl(String bersl) {
        this.bersl = bersl;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public String getVtweg() {
        return vtweg;
    }

    public void setVtweg(String vtweg) {
        this.vtweg = vtweg;
    }

    public String getSpart() {
        return spart;
    }

    public void setSpart(String spart) {
        this.spart = spart;
    }

    public String getVkgrp() {
        return vkgrp;
    }

    public void setVkgrp(String vkgrp) {
        this.vkgrp = vkgrp;
    }

    public String getVbeln() {
        return vbeln;
    }

    public void setVbeln(String vbeln) {
        this.vbeln = vbeln;
    }

    public String getAuart() {
        return auart;
    }

    public void setAuart(String auart) {
        this.auart = auart;
    }

    public Date getAudat() {
        return audat;
    }

    public void setAudat(Date audat) {
        this.audat = audat;
    }

    public Long getPosnr() {
        return posnr;
    }

    public void setPosnr(Long posnr) {
        this.posnr = posnr;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getArktx() {
        return arktx;
    }

    public void setArktx(String arktx) {
        this.arktx = arktx;
    }

    public BigDecimal getKwmeng() {
        return kwmeng;
    }

    public void setKwmeng(BigDecimal kwmeng) {
        this.kwmeng = kwmeng;
    }

    public String getVrkme() {
        return vrkme;
    }

    public void setVrkme(String vrkme) {
        this.vrkme = vrkme;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getNetwr() {
        return netwr;
    }

    public void setNetwr(BigDecimal netwr) {
        this.netwr = netwr;
    }

    public String getWaerk() {
        return waerk;
    }

    public void setWaerk(String waerk) {
        this.waerk = waerk;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getFree() {
        return free;
    }

    public void setFree(BigDecimal free) {
        this.free = free;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getDbrtg() {
        return dbrtg;
    }

    public void setDbrtg(String dbrtg) {
        this.dbrtg = dbrtg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAsp() {
        return asp;
    }

    public void setAsp(BigDecimal asp) {
        this.asp = asp;
    }

    public BigDecimal getAspS() {
        return aspS;
    }

    public void setAspS(BigDecimal aspS) {
        this.aspS = aspS;
    }

    public BigDecimal getVprs() {
        return vprs;
    }

    public void setVprs(BigDecimal vprs) {
        this.vprs = vprs;
    }

    public BigDecimal getItmpf() {
        return itmpf;
    }

    public void setItmpf(BigDecimal itmpf) {
        this.itmpf = itmpf;
    }

    public BigDecimal getItmdn() {
        return itmdn;
    }

    public void setItmdn(BigDecimal itmdn) {
        this.itmdn = itmdn;
    }

    public BigDecimal getQtydf() {
        return qtydf;
    }

    public void setQtydf(BigDecimal qtydf) {
        this.qtydf = qtydf;
    }

    public BigDecimal getPrmng() {
        return prmng;
    }

    public void setPrmng(BigDecimal prmng) {
        this.prmng = prmng;
    }

    public BigDecimal getPrasp() {
        return prasp;
    }

    public void setPrasp(BigDecimal prasp) {
        this.prasp = prasp;
    }

    public BigDecimal getPraspS() {
        return praspS;
    }

    public void setPraspS(BigDecimal praspS) {
        this.praspS = praspS;
    }

    public String getBstzd() {
        return bstzd;
    }

    public void setBstzd(String bstzd) {
        this.bstzd = bstzd;
    }

    public String getBstzdT() {
        return bstzdT;
    }

    public void setBstzdT(String bstzdT) {
        this.bstzdT = bstzdT;
    }

    public String getObjnr() {
        return objnr;
    }

    public void setObjnr(String objnr) {
        this.objnr = objnr;
    }

    public String getStsma() {
        return stsma;
    }

    public void setStsma(String stsma) {
        this.stsma = stsma;
    }

    public String getCstat() {
        return cstat;
    }

    public void setCstat(String cstat) {
        this.cstat = cstat;
    }

    public String getNstat() {
        return nstat;
    }

    public void setNstat(String nstat) {
        this.nstat = nstat;
    }

    public String getRelText() {
        return relText;
    }

    public void setRelText(String relText) {
        this.relText = relText;
    }

    public String getAltkn() {
        return altkn;
    }

    public void setAltkn(String altkn) {
        this.altkn = altkn;
    }

    public String getPstlz() {
        return pstlz;
    }

    public void setPstlz(String pstlz) {
        this.pstlz = pstlz;
    }

    public String getOrt01() {
        return ort01;
    }

    public void setOrt01(String ort01) {
        this.ort01 = ort01;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public List<ZtabExpVbak> getZtabExpVbakList() {
        return ztabExpVbakList;
    }

    public void setZtabExpVbakList(List<ZtabExpVbak> ztabExpVbakList) {
        this.ztabExpVbakList = ztabExpVbakList;
    }

    public List<ZtabExpVbap> getZtabExpVbapList() {
        return ztabExpVbapList;
    }

    public void setZtabExpVbapList(List<ZtabExpVbap> ztabExpVbapList) {
        this.ztabExpVbapList = ztabExpVbapList;
    }

    public List<ZtabExpVbkd> getZtabExpVbkdList() {
        return ztabExpVbkdList;
    }

    public void setZtabExpVbkdList(List<ZtabExpVbkd> ztabExpVbkdList) {
        this.ztabExpVbkdList = ztabExpVbkdList;
    }

    public List<ZtabExpVbpa> getZtabExpVbpaList() {
        return ztabExpVbpaList;
    }

    public void setZtabExpVbpaList(List<ZtabExpVbpa> ztabExpVbpaList) {
        this.ztabExpVbpaList = ztabExpVbpaList;
    }

    public List<ZtabExpVbuk> getZtabExpVbukList() {
        return ztabExpVbukList;
    }

    public void setZtabExpVbukList(List<ZtabExpVbuk> ztabExpVbukList) {
        this.ztabExpVbukList = ztabExpVbukList;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtabExpRelfilenoSd)) {
            return false;
        }
        ZtabExpRelfilenoSd other = (ZtabExpRelfilenoSd) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.mygui.entity.datawarehouse.SdZtabExpRelfilenoSd[ id=" + id + " ]";
    }
}
