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
@Table(name = "SD_ZTAB_EXP_VBUK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZtabExpVbuk.findAll", query = "SELECT z FROM ZtabExpVbuk z"),
    @NamedQuery(name = "ZtabExpVbuk.findByMandt", query = "SELECT z FROM ZtabExpVbuk z WHERE z.ztabExpVbukPK.mandt = :mandt"),
    @NamedQuery(name = "ZtabExpVbuk.findByVbeln", query = "SELECT z FROM ZtabExpVbuk z WHERE z.ztabExpVbukPK.vbeln = :vbeln"),
    @NamedQuery(name = "ZtabExpVbuk.findByLfstk", query = "SELECT z FROM ZtabExpVbuk z WHERE z.lfstk = :lfstk"),
    @NamedQuery(name = "ZtabExpVbuk.findByAbstk", query = "SELECT z FROM ZtabExpVbuk z WHERE z.abstk = :abstk"),
    @NamedQuery(name = "ZtabExpVbuk.findByGbstk", query = "SELECT z FROM ZtabExpVbuk z WHERE z.gbstk = :gbstk"),
    @NamedQuery(name = "ZtabExpVbuk.findByCmgst", query = "SELECT z FROM ZtabExpVbuk z WHERE z.cmgst = :cmgst")})
public class ZtabExpVbuk implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ZtabExpVbukPK ztabExpVbukPK;
    @Size(max = 2)
    @Column(name = "LFSTK")
    private String lfstk;
    @Size(max = 2)
    @Column(name = "ABSTK")
    private String abstk;
    @Size(max = 2)
    @Column(name = "GBSTK")
    private String gbstk;
    @Size(max = 2)
    @Column(name = "CMGST")
    private String cmgst;
    @JoinColumn(name = "SD_ZTAB_EXP_RELFILENO_SD_ID", referencedColumnName = "ID")
    @ManyToOne
    private ZtabExpRelfilenoSd sdZtabExpRelfilenoSdId;

    public ZtabExpVbuk() {
    }

    public ZtabExpVbuk(ZtabExpVbukPK ztabExpVbukPK) {
        this.ztabExpVbukPK = ztabExpVbukPK;
    }

    public ZtabExpVbuk(String mandt, String vbeln) {
        this.ztabExpVbukPK = new ZtabExpVbukPK(mandt, vbeln);
    }

    public ZtabExpVbukPK getZtabExpVbukPK() {
        return ztabExpVbukPK;
    }

    public void setZtabExpVbukPK(ZtabExpVbukPK ztabExpVbukPK) {
        this.ztabExpVbukPK = ztabExpVbukPK;
    }

    public String getLfstk() {
        return lfstk;
    }

    public void setLfstk(String lfstk) {
        this.lfstk = lfstk;
    }

    public String getAbstk() {
        return abstk;
    }

    public void setAbstk(String abstk) {
        this.abstk = abstk;
    }

    public String getGbstk() {
        return gbstk;
    }

    public void setGbstk(String gbstk) {
        this.gbstk = gbstk;
    }

    public String getCmgst() {
        return cmgst;
    }

    public void setCmgst(String cmgst) {
        this.cmgst = cmgst;
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
        hash += (ztabExpVbukPK != null ? ztabExpVbukPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtabExpVbuk)) {
            return false;
        }
        ZtabExpVbuk other = (ZtabExpVbuk) object;
        if ((this.ztabExpVbukPK == null && other.ztabExpVbukPK != null) || (this.ztabExpVbukPK != null && !this.ztabExpVbukPK.equals(other.ztabExpVbukPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.mygui.entity.datawarehouse.ZtabExpVbuk[ ztabExpVbukPK=" + ztabExpVbukPK + " ]";
    }
    
}
