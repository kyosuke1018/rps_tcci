package com.tcci.worklist.entity.datawarehouse;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nEO.Fu
 */
@Entity
@Cacheable(false)
@Table(name = "SD_ZTAB_EXP_VBPA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZtabExpVbpa.findAll", query = "SELECT z FROM ZtabExpVbpa z"),
    @NamedQuery(name = "ZtabExpVbpa.findByMandt", query = "SELECT z FROM ZtabExpVbpa z WHERE z.ztabExpVbpaPK.mandt = :mandt"),
    @NamedQuery(name = "ZtabExpVbpa.findByVbeln", query = "SELECT z FROM ZtabExpVbpa z WHERE z.ztabExpVbpaPK.vbeln = :vbeln"),
    @NamedQuery(name = "ZtabExpVbpa.findByPosnr", query = "SELECT z FROM ZtabExpVbpa z WHERE z.posnr = :posnr"),
    @NamedQuery(name = "ZtabExpVbpa.findByParvw", query = "SELECT z FROM ZtabExpVbpa z WHERE z.parvw = :parvw"),
    @NamedQuery(name = "ZtabExpVbpa.findByKunnr", query = "SELECT z FROM ZtabExpVbpa z WHERE z.kunnr = :kunnr"),
    @NamedQuery(name = "ZtabExpVbpa.findByLifnr", query = "SELECT z FROM ZtabExpVbpa z WHERE z.lifnr = :lifnr"),
    @NamedQuery(name = "ZtabExpVbpa.findByPernr", query = "SELECT z FROM ZtabExpVbpa z WHERE z.pernr = :pernr")})
public class ZtabExpVbpa implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ZtabExpVbpaPK ztabExpVbpaPK;
    @Column(name = "POSNR")
    private Long posnr;
    @Size(max = 4)
    @Column(name = "PARVW")
    private String parvw;
    @Size(max = 20)
    @Column(name = "KUNNR")
    private String kunnr;
    @Size(max = 20)
    @Column(name = "LIFNR")
    private String lifnr;
    @Column(name = "PERNR")
    private Long pernr;
    @JoinColumn(name = "SD_ZTAB_EXP_RELFILENO_SD_ID", referencedColumnName = "ID")
    @ManyToOne
    private ZtabExpRelfilenoSd sdZtabExpRelfilenoSdId;

    public ZtabExpVbpa() {
    }

    public ZtabExpVbpa(ZtabExpVbpaPK ztabExpVbpaPK) {
        this.ztabExpVbpaPK = ztabExpVbpaPK;
    }

    public ZtabExpVbpa(String mandt, String vbeln) {
        this.ztabExpVbpaPK = new ZtabExpVbpaPK(mandt, vbeln);
    }

    public ZtabExpVbpaPK getZtabExpVbpaPK() {
        return ztabExpVbpaPK;
    }

    public void setZtabExpVbpaPK(ZtabExpVbpaPK ztabExpVbpaPK) {
        this.ztabExpVbpaPK = ztabExpVbpaPK;
    }

    public Long getPosnr() {
        return posnr;
    }

    public void setPosnr(Long posnr) {
        this.posnr = posnr;
    }

    public String getParvw() {
        return parvw;
    }

    public void setParvw(String parvw) {
        this.parvw = parvw;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getLifnr() {
        return lifnr;
    }

    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }

    public Long getPernr() {
        return pernr;
    }

    public void setPernr(Long pernr) {
        this.pernr = pernr;
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
        hash += (ztabExpVbpaPK != null ? ztabExpVbpaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtabExpVbpa)) {
            return false;
        }
        ZtabExpVbpa other = (ZtabExpVbpa) object;
        if ((this.ztabExpVbpaPK == null && other.ztabExpVbpaPK != null) || (this.ztabExpVbpaPK != null && !this.ztabExpVbpaPK.equals(other.ztabExpVbpaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.mygui.entity.datawarehouse.ZtabExpVbpa[ ztabExpVbpaPK=" + ztabExpVbpaPK + " ]";
    }
    
}
