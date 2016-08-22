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
@Table(name = "SD_ZTAB_EXP_VBKD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZtabExpVbkd.findAll", query = "SELECT z FROM ZtabExpVbkd z"),
    @NamedQuery(name = "ZtabExpVbkd.findByMandt", query = "SELECT z FROM ZtabExpVbkd z WHERE z.ztabExpVbkdPK.mandt = :mandt"),
    @NamedQuery(name = "ZtabExpVbkd.findByVbeln", query = "SELECT z FROM ZtabExpVbkd z WHERE z.ztabExpVbkdPK.vbeln = :vbeln"),
    @NamedQuery(name = "ZtabExpVbkd.findByPosnr", query = "SELECT z FROM ZtabExpVbkd z WHERE z.posnr = :posnr"),
    @NamedQuery(name = "ZtabExpVbkd.findByInco1", query = "SELECT z FROM ZtabExpVbkd z WHERE z.inco1 = :inco1"),
    @NamedQuery(name = "ZtabExpVbkd.findByInco2", query = "SELECT z FROM ZtabExpVbkd z WHERE z.inco2 = :inco2"),
    @NamedQuery(name = "ZtabExpVbkd.findByZterm", query = "SELECT z FROM ZtabExpVbkd z WHERE z.zterm = :zterm"),
    @NamedQuery(name = "ZtabExpVbkd.findByKursk", query = "SELECT z FROM ZtabExpVbkd z WHERE z.kursk = :kursk"),
    @NamedQuery(name = "ZtabExpVbkd.findByPrsdt", query = "SELECT z FROM ZtabExpVbkd z WHERE z.prsdt = :prsdt"),
    @NamedQuery(name = "ZtabExpVbkd.findByBstkd", query = "SELECT z FROM ZtabExpVbkd z WHERE z.bstkd = :bstkd")})
public class ZtabExpVbkd implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ZtabExpVbkdPK ztabExpVbkdPK;
    @Column(name = "POSNR")
    private Long posnr;
    @Size(max = 6)
    @Column(name = "INCO1")
    private String inco1;
    @Size(max = 56)
    @Column(name = "INCO2")
    private String inco2;
    @Size(max = 8)
    @Column(name = "ZTERM")
    private String zterm;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "KURSK")
    private BigDecimal kursk;
    @Column(name = "PRSDT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date prsdt;
    @Size(max = 70)
    @Column(name = "BSTKD")
    private String bstkd;
    @JoinColumn(name = "SD_ZTAB_EXP_RELFILENO_SD_ID", referencedColumnName = "ID")
    @ManyToOne
    private ZtabExpRelfilenoSd sdZtabExpRelfilenoSdId;

    public ZtabExpVbkd() {
    }

    public ZtabExpVbkd(ZtabExpVbkdPK ztabExpVbkdPK) {
        this.ztabExpVbkdPK = ztabExpVbkdPK;
    }

    public ZtabExpVbkd(String mandt, String vbeln) {
        this.ztabExpVbkdPK = new ZtabExpVbkdPK(mandt, vbeln);
    }

    public ZtabExpVbkdPK getZtabExpVbkdPK() {
        return ztabExpVbkdPK;
    }

    public void setZtabExpVbkdPK(ZtabExpVbkdPK ztabExpVbkdPK) {
        this.ztabExpVbkdPK = ztabExpVbkdPK;
    }

    public Long getPosnr() {
        return posnr;
    }

    public void setPosnr(Long posnr) {
        this.posnr = posnr;
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

    public String getZterm() {
        return zterm;
    }

    public void setZterm(String zterm) {
        this.zterm = zterm;
    }

    public BigDecimal getKursk() {
        return kursk;
    }

    public void setKursk(BigDecimal kursk) {
        this.kursk = kursk;
    }

    public Date getPrsdt() {
        return prsdt;
    }

    public void setPrsdt(Date prsdt) {
        this.prsdt = prsdt;
    }

    public String getBstkd() {
        return bstkd;
    }

    public void setBstkd(String bstkd) {
        this.bstkd = bstkd;
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
        hash += (ztabExpVbkdPK != null ? ztabExpVbkdPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtabExpVbkd)) {
            return false;
        }
        ZtabExpVbkd other = (ZtabExpVbkd) object;
        if ((this.ztabExpVbkdPK == null && other.ztabExpVbkdPK != null) || (this.ztabExpVbkdPK != null && !this.ztabExpVbkdPK.equals(other.ztabExpVbkdPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.mygui.entity.datawarehouse.ZtabExpVbkd[ ztabExpVbkdPK=" + ztabExpVbkdPK + " ]";
    }
    
}
