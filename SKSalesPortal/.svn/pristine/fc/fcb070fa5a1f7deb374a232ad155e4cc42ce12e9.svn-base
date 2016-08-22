package com.tcci.worklist.entity.datawarehouse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nEO.Fu
 */
@Entity
@Cacheable(false)
@Table(name = "SD_ZTAB_EXP_VBAP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZtabExpVbap.findAll", query = "SELECT z FROM ZtabExpVbap z"),
    @NamedQuery(name = "ZtabExpVbap.findByMandt", query = "SELECT z FROM ZtabExpVbap z WHERE z.ztabExpVbapPK.mandt = :mandt"),
    @NamedQuery(name = "ZtabExpVbap.findByVbeln", query = "SELECT z FROM ZtabExpVbap z WHERE z.ztabExpVbapPK.vbeln = :vbeln"),
    @NamedQuery(name = "ZtabExpVbap.findByPosnr", query = "SELECT z FROM ZtabExpVbap z WHERE z.posnr = :posnr"),
    @NamedQuery(name = "ZtabExpVbap.findByMatnr", query = "SELECT z FROM ZtabExpVbap z WHERE z.matnr = :matnr"),
    @NamedQuery(name = "ZtabExpVbap.findByMatkl", query = "SELECT z FROM ZtabExpVbap z WHERE z.matkl = :matkl"),
    @NamedQuery(name = "ZtabExpVbap.findByArktx", query = "SELECT z FROM ZtabExpVbap z WHERE z.arktx = :arktx"),
    @NamedQuery(name = "ZtabExpVbap.findByPstyv", query = "SELECT z FROM ZtabExpVbap z WHERE z.pstyv = :pstyv"),
    @NamedQuery(name = "ZtabExpVbap.findByUepos", query = "SELECT z FROM ZtabExpVbap z WHERE z.uepos = :uepos"),
    @NamedQuery(name = "ZtabExpVbap.findByNetwr", query = "SELECT z FROM ZtabExpVbap z WHERE z.netwr = :netwr"),
    @NamedQuery(name = "ZtabExpVbap.findByWaerk", query = "SELECT z FROM ZtabExpVbap z WHERE z.waerk = :waerk"),
    @NamedQuery(name = "ZtabExpVbap.findByKwmeng", query = "SELECT z FROM ZtabExpVbap z WHERE z.kwmeng = :kwmeng"),
    @NamedQuery(name = "ZtabExpVbap.findByVrkme", query = "SELECT z FROM ZtabExpVbap z WHERE z.vrkme = :vrkme"),
    @NamedQuery(name = "ZtabExpVbap.findByVgbel", query = "SELECT z FROM ZtabExpVbap z WHERE z.vgbel = :vgbel"),
    @NamedQuery(name = "ZtabExpVbap.findByVgpos", query = "SELECT z FROM ZtabExpVbap z WHERE z.vgpos = :vgpos"),
    @NamedQuery(name = "ZtabExpVbap.findByWerks", query = "SELECT z FROM ZtabExpVbap z WHERE z.werks = :werks"),
    @NamedQuery(name = "ZtabExpVbap.findByLgort", query = "SELECT z FROM ZtabExpVbap z WHERE z.lgort = :lgort"),
    @NamedQuery(name = "ZtabExpVbap.findByVstel", query = "SELECT z FROM ZtabExpVbap z WHERE z.vstel = :vstel"),
    @NamedQuery(name = "ZtabExpVbap.findByErdat", query = "SELECT z FROM ZtabExpVbap z WHERE z.erdat = :erdat"),
    @NamedQuery(name = "ZtabExpVbap.findByErnam", query = "SELECT z FROM ZtabExpVbap z WHERE z.ernam = :ernam"),
    @NamedQuery(name = "ZtabExpVbap.findByErzet", query = "SELECT z FROM ZtabExpVbap z WHERE z.erzet = :erzet"),
    @NamedQuery(name = "ZtabExpVbap.findByNetpr", query = "SELECT z FROM ZtabExpVbap z WHERE z.netpr = :netpr"),
    @NamedQuery(name = "ZtabExpVbap.findByKpein", query = "SELECT z FROM ZtabExpVbap z WHERE z.kpein = :kpein"),
    @NamedQuery(name = "ZtabExpVbap.findByKmein", query = "SELECT z FROM ZtabExpVbap z WHERE z.kmein = :kmein"),
    @NamedQuery(name = "ZtabExpVbap.findByAedat", query = "SELECT z FROM ZtabExpVbap z WHERE z.aedat = :aedat"),
    @NamedQuery(name = "ZtabExpVbap.findByMwsbp", query = "SELECT z FROM ZtabExpVbap z WHERE z.mwsbp = :mwsbp")})
public class ZtabExpVbap implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ZtabExpVbapPK ztabExpVbapPK;
    @Column(name = "POSNR")
    private Long posnr;
    @Size(max = 36)
    @Column(name = "MATNR")
    private String matnr;
    @Size(max = 18)
    @Column(name = "MATKL")
    private String matkl;
    @Size(max = 80)
    @Column(name = "ARKTX")
    private String arktx;
    @Size(max = 8)
    @Column(name = "PSTYV")
    private String pstyv;
    @Column(name = "UEPOS")
    private Long uepos;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "NETWR")
    private BigDecimal netwr;
    @Size(max = 10)
    @Column(name = "WAERK")
    private String waerk;
    @Column(name = "KWMENG")
    private BigDecimal kwmeng;
    @Column(name = "VRKME")
    private Long vrkme;
    @Size(max = 20)
    @Column(name = "VGBEL")
    private String vgbel;
    @Column(name = "VGPOS")
    private Long vgpos;
    @Size(max = 8)
    @Column(name = "WERKS")
    private String werks;
    @Size(max = 8)
    @Column(name = "LGORT")
    private String lgort;
    @Size(max = 8)
    @Column(name = "VSTEL")
    private String vstel;
    @Column(name = "ERDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date erdat;
    @Size(max = 24)
    @Column(name = "ERNAM")
    private String ernam;
    @Column(name = "ERZET")
    @Temporal(TemporalType.TIMESTAMP)
    private Date erzet;
    @Column(name = "NETPR")
    private BigDecimal netpr;
    @Size(max = 10)
    @Column(name = "KPEIN")
    private String kpein;
    @Column(name = "KMEIN")
    private Long kmein;
    @Column(name = "AEDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date aedat;
    @Column(name = "MWSBP")
    private BigDecimal mwsbp;
    @JoinColumn(name = "SD_ZTAB_EXP_RELFILENO_SD_ID", referencedColumnName = "ID")
    @ManyToOne
    private ZtabExpRelfilenoSd sdZtabExpRelfilenoSdId;

    public ZtabExpVbap() {
    }

    public ZtabExpVbap(ZtabExpVbapPK ztabExpVbapPK) {
        this.ztabExpVbapPK = ztabExpVbapPK;
    }

    public ZtabExpVbap(String mandt, String vbeln) {
        this.ztabExpVbapPK = new ZtabExpVbapPK(mandt, vbeln);
    }

    public ZtabExpVbapPK getZtabExpVbapPK() {
        return ztabExpVbapPK;
    }

    public void setZtabExpVbapPK(ZtabExpVbapPK ztabExpVbapPK) {
        this.ztabExpVbapPK = ztabExpVbapPK;
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

    public String getMatkl() {
        return matkl;
    }

    public void setMatkl(String matkl) {
        this.matkl = matkl;
    }

    public String getArktx() {
        return arktx;
    }

    public void setArktx(String arktx) {
        this.arktx = arktx;
    }

    public String getPstyv() {
        return pstyv;
    }

    public void setPstyv(String pstyv) {
        this.pstyv = pstyv;
    }

    public Long getUepos() {
        return uepos;
    }

    public void setUepos(Long uepos) {
        this.uepos = uepos;
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

    public BigDecimal getKwmeng() {
        return kwmeng;
    }

    public void setKwmeng(BigDecimal kwmeng) {
        this.kwmeng = kwmeng;
    }

    public Long getVrkme() {
        return vrkme;
    }

    public void setVrkme(Long vrkme) {
        this.vrkme = vrkme;
    }

    public String getVgbel() {
        return vgbel;
    }

    public void setVgbel(String vgbel) {
        this.vgbel = vgbel;
    }

    public Long getVgpos() {
        return vgpos;
    }

    public void setVgpos(Long vgpos) {
        this.vgpos = vgpos;
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

    public String getVstel() {
        return vstel;
    }

    public void setVstel(String vstel) {
        this.vstel = vstel;
    }

    public Date getErdat() {
        return erdat;
    }

    public void setErdat(Date erdat) {
        this.erdat = erdat;
    }

    public String getErnam() {
        return ernam;
    }

    public void setErnam(String ernam) {
        this.ernam = ernam;
    }

    public Date getErzet() {
        return erzet;
    }

    public void setErzet(Date erzet) {
        this.erzet = erzet;
    }

    public BigDecimal getNetpr() {
        return netpr;
    }

    public void setNetpr(BigDecimal netpr) {
        this.netpr = netpr;
    }

    public String getKpein() {
        return kpein;
    }

    public void setKpein(String kpein) {
        this.kpein = kpein;
    }

    public Long getKmein() {
        return kmein;
    }

    public void setKmein(Long kmein) {
        this.kmein = kmein;
    }

    public Date getAedat() {
        return aedat;
    }

    public void setAedat(Date aedat) {
        this.aedat = aedat;
    }

    public BigDecimal getMwsbp() {
        return mwsbp;
    }

    public void setMwsbp(BigDecimal mwsbp) {
        this.mwsbp = mwsbp;
    }

    public ZtabExpRelfilenoSd getSdZtabExpRelfilenoSdId() {
        return sdZtabExpRelfilenoSdId;
    }

    public void setSdZtabExpRelfilenoSdId(ZtabExpRelfilenoSd sdZtabExpRelfilenoSdId) {
        this.sdZtabExpRelfilenoSdId = sdZtabExpRelfilenoSdId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ztabExpVbapPK != null ? ztabExpVbapPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtabExpVbap)) {
            return false;
        }
        ZtabExpVbap other = (ZtabExpVbap) object;
        if ((this.ztabExpVbapPK == null && other.ztabExpVbapPK != null) || (this.ztabExpVbapPK != null && !this.ztabExpVbapPK.equals(other.ztabExpVbapPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.mygui.entity.datawarehouse.ZtabExpVbap[ ztabExpVbapPK=" + ztabExpVbapPK + " ]";
    }
    
}
