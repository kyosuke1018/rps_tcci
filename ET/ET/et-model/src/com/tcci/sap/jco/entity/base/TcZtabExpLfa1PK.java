package com.tcci.sap.jco.entity.base;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

/**
 *　供應商與採購組織關聯檔
 * @author Peter.pan
 */
@Embeddable
public class TcZtabExpLfa1PK implements Serializable, Comparable {
    private static final long serialVersionUID = 1L;
    @Size(max = 6)
    @Column(name = "MANDT")
    private String mandt;
    @Size(max = 20)
    @Column(name = "LIFNR")
    private String lifnr;
    @Size(max = 8)
    @Column(name = "EKORG")
    private String ekorg;

    public TcZtabExpLfa1PK() {
    }

    public TcZtabExpLfa1PK(String mandt, String lifnr, String ekorg) {
        this.mandt = mandt;
        this.lifnr = lifnr;
        this.ekorg = ekorg;
    }
    
    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getLifnr() {
        return lifnr;
    }

    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }

    public String getEkorg() {
        return ekorg;
    }

    public void setEkorg(String ekorg) {
        this.ekorg = ekorg;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TcZtabExpLfa1PK)) {
            return false;
        }
        
        if( compareTo(object)==0 ){
            return true;
        }
        
        return false;
    }    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.mandt != null ? this.mandt.hashCode() : 0);
        hash = 83 * hash + (this.lifnr != null ? this.lifnr.hashCode() : 0);
        hash = 83 * hash + (this.ekorg != null ? this.ekorg.hashCode() : 0);
        return hash;
    }
    
    @Override
    public int compareTo(Object o) {
        if( o == null ){
            return -1;
        }
        
        TcZtabExpLfa1PK compObj = (TcZtabExpLfa1PK)o;
        if( mandt.equals(compObj.getMandt())
                && lifnr.equals(compObj.getLifnr())
                && ekorg.equals(compObj.getEkorg()) ){
            return 0;
        }
        
        String thisKey = mandt + "_" + lifnr + "_" + ekorg;
        String compKey = compObj.getMandt() + "_" + compObj.getLifnr() + "_" + compObj.getEkorg();
        
        return thisKey.compareTo(compKey);
    }
}
