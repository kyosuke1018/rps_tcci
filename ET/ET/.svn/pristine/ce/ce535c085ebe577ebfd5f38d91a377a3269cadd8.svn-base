/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.entity.base;

import com.tcci.sap.jco.entity.TcRfcZtab;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "TC_ZTAB_EXP_LFA1")
@NamedQueries({
    @NamedQuery(name = "TcZtabExpLfa1.findAll", query = "SELECT t FROM TcZtabExpLfa1 t")})
public class TcZtabExpLfa1 implements TcRfcZtab, Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private TcZtabExpLfa1PK tcZtabExpLfa1PK;
    
    @Size(max = 70)
    @Column(name = "NAME1")
    private String name1;
    @Size(max = 2)
    @Column(name = "SPERM")
    private String sperm;
    @Size(max = 40)
    @Column(name = "EKOTX")
    private String ekotx;
    @Size(max = 60)
    @Column(name = "VERKF")
    private String verkf;
    @Size(max = 32)
    @Column(name = "TELF1")
    private String telf1;
    @Size(max = 241)
    @Column(name = "TELBX")
    private String telbx;
    @Size(max = 10)
    @Column(name = "WAERS")
    private String waers;
    @Size(max = 8)
    @Column(name = "ZTERM")
    private String zterm;
    @Size(max = 70)
    @Column(name = "ORT01")
    private String ort01;
    @Size(max = 80)
    @Column(name = "STR_SUPPL1")
    private String strSuppl1;
    @Size(max = 6)
    @Column(name = "LAND1")
    private String land1;
    @Size(max = 20)
    @Column(name = "SORTL")
    private String sortl;
    @Size(max = 2)
    @Column(name = "LOEVM")
    private String loevm;
    @Size(max = 20)
    @Column(name = "LIFNR_UI")
    private String lifnrUi;
    @Size(max = 70)
    @Column(name = "NAME2")
    private String name2;
    @Size(max = 70)
    @Column(name = "NAME3")
    private String name3;
    @Size(max = 70)
    @Column(name = "NAME4")
    private String name4;
    @Column(name = "SYNC_TIME_STAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date syncTimeStamp;
    @Size(max = 62)
    @Column(name = "TELFX")
    private String telfx;
    @Size(max = 1)
    @Column(name = "RISK")
    private String risk;

    @Size(max = 1)
    @Column(name = "ZVENDOR_TYPE")
    private String zvendorType;// 高風險廠商供應商A1/A2/A3/B/C分類
    
    @Size(max = 1)
    @Column(name = "CSPERM")
    private String csperm;// 中央採購凍結("X"->代表凍結)
    @Size(max = 2)
    @Column(name = "SPERQ")
    private String sperq;
    
    @Column(name = "Z_NBLOCK_SDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date zNblockSdate;
    @Column(name = "Z_NBLOCK_EDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date zNblockEdate;
    @Size(max = 1)
    @Column(name = "Z_CANNOT_BLOCK")
    private String zCannotBlock;// 永久不可凍結

    @Size(max = 60)
    @Column(name = "ZARTI_PERSON1")
    private String zartiPerson1;
    @Size(max = 60)
    @Column(name = "ZARTI_PERSON2")
    private String zartiPerson2;
    @Size(max = 60)
    @Column(name = "ZARTI_PERSON3")
    private String zartiPerson3;
    
    @Size(max = 60)
    @Column(name = "ZSTOCKHOLDER1")
    private String zstockholder1;
    @Size(max = 60)
    @Column(name = "ZSTOCKHOLDER2")
    private String zstockholder2;
    @Size(max = 60)
    @Column(name = "ZSTOCKHOLDER3")
    private String zstockholder3;
    @Size(max = 60)
    @Column(name = "ZSTOCKHOLDER4")
    private String zstockholder4;
    @Size(max = 60)
    @Column(name = "ZSTOCKHOLDER5")
    private String zstockholder5;
    @Size(max = 60)
    @Column(name = "MOB_NUMBER")
    private String mobNumber;    
    
    public TcZtabExpLfa1() {
    }

    public TcZtabExpLfa1PK getTcZtabExpLfa1PK() {
        return tcZtabExpLfa1PK;
    }

    public void setTcZtabExpLfa1PK(TcZtabExpLfa1PK tcZtabExpLfa1PK) {
        this.tcZtabExpLfa1PK = tcZtabExpLfa1PK;
        // 補 UI供應商編號 欄位
        String ori_lifnr = tcZtabExpLfa1PK.getLifnr();
        this.lifnrUi = (ori_lifnr!=null && ori_lifnr.length()==10 && ori_lifnr.startsWith("000"))? ori_lifnr.substring(3):ori_lifnr;
    }

    @Override
    public Object getPrimaryKey(){
        return tcZtabExpLfa1PK;
    }

    @Override
    public String getMandt() {
        return tcZtabExpLfa1PK!=null?tcZtabExpLfa1PK.getMandt():null;
    }

    @Override
    public String getDelKey() {
        return tcZtabExpLfa1PK!=null?tcZtabExpLfa1PK.getLifnr():null;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getZvendorType() {
        return zvendorType;
    }

    public void setZvendorType(String zvendorType) {
        this.zvendorType = zvendorType;
    }

    public String getZartiPerson1() {
        return zartiPerson1;
    }

    public void setZartiPerson1(String zartiPerson1) {
        this.zartiPerson1 = zartiPerson1;
    }

    public String getZartiPerson2() {
        return zartiPerson2;
    }

    public void setZartiPerson2(String zartiPerson2) {
        this.zartiPerson2 = zartiPerson2;
    }

    public String getZartiPerson3() {
        return zartiPerson3;
    }

    public void setZartiPerson3(String zartiPerson3) {
        this.zartiPerson3 = zartiPerson3;
    }

    public String getZstockholder1() {
        return zstockholder1;
    }

    public void setZstockholder1(String zstockholder1) {
        this.zstockholder1 = zstockholder1;
    }

    public String getZstockholder2() {
        return zstockholder2;
    }

    public void setZstockholder2(String zstockholder2) {
        this.zstockholder2 = zstockholder2;
    }

    public String getZstockholder3() {
        return zstockholder3;
    }

    public void setZstockholder3(String zstockholder3) {
        this.zstockholder3 = zstockholder3;
    }

    public String getZstockholder4() {
        return zstockholder4;
    }

    public void setZstockholder4(String zstockholder4) {
        this.zstockholder4 = zstockholder4;
    }

    public String getZstockholder5() {
        return zstockholder5;
    }

    public void setZstockholder5(String zstockholder5) {
        this.zstockholder5 = zstockholder5;
    }

    public String getzCannotBlock() {
        return zCannotBlock;
    }

    public void setzCannotBlock(String zCannotBlock) {
        this.zCannotBlock = zCannotBlock;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getSperm() {
        return sperm;
    }

    public void setSperm(String sperm) {
        this.sperm = sperm;
    }

    public String getEkotx() {
        return ekotx;
    }

    public void setEkotx(String ekotx) {
        this.ekotx = ekotx;
    }

    public String getVerkf() {
        return verkf;
    }

    public void setVerkf(String verkf) {
        this.verkf = verkf;
    }

    public String getTelf1() {
        return telf1;
    }

    public void setTelf1(String telf1) {
        this.telf1 = telf1;
    }

    public String getTelbx() {
        return telbx;
    }

    public void setTelbx(String telbx) {
        this.telbx = telbx;
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
    }

    public String getZterm() {
        return zterm;
    }

    public void setZterm(String zterm) {
        this.zterm = zterm;
    }

    public String getOrt01() {
        return ort01;
    }

    public void setOrt01(String ort01) {
        this.ort01 = ort01;
    }

    public String getStrSuppl1() {
        return strSuppl1;
    }

    public void setStrSuppl1(String strSuppl1) {
        this.strSuppl1 = strSuppl1;
    }

    public String getLand1() {
        return land1;
    }

    public void setLand1(String land1) {
        this.land1 = land1;
    }

    public String getSortl() {
        return sortl;
    }

    public void setSortl(String sortl) {
        this.sortl = sortl;
    }

    public String getLoevm() {
        return loevm;
    }

    public void setLoevm(String loevm) {
        this.loevm = loevm;
    }

    public String getLifnrUi() {
        return lifnrUi;
    }

    public void setLifnrUi(String lifnrUi) {
        this.lifnrUi = lifnrUi;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getName4() {
        return name4;
    }

    public void setName4(String name4) {
        this.name4 = name4;
    }

    public String getTelfx() {
        return telfx;
    }

    public void setTelfx(String telfx) {
        this.telfx = telfx;
    }
    
    public Date getSyncTimeStamp() {
        return syncTimeStamp;
    }

    @Override
    public void setSyncTimeStamp(Date syncTimeStamp) {
        this.syncTimeStamp = syncTimeStamp;
    }

    public String getCsperm() {
        return csperm;
    }

    public void setCsperm(String csperm) {
        this.csperm = csperm;
    }

    public String getSperq() {
        return sperq;
    }

    public void setSperq(String sperq) {
        this.sperq = sperq;
    }

    public Date getzNblockSdate() {
        return zNblockSdate;
    }

    public void setzNblockSdate(Date zNblockSdate) {
        this.zNblockSdate = zNblockSdate;
    }

    public Date getzNblockEdate() {
        return zNblockEdate;
    }

    public void setzNblockEdate(Date zNblockEdate) {
        this.zNblockEdate = zNblockEdate;
    }
    
    public String getMobNumber() {
        return mobNumber;
    }

    public void setMobNumber(String mobNumber) {
        this.mobNumber = mobNumber;
    }    
    //</editor-fold>

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TcZtabExpLfa1)) {
            return false;
        }
        
        TcZtabExpLfa1 other = (TcZtabExpLfa1) object;
        if ((this.tcZtabExpLfa1PK == null && other.tcZtabExpLfa1PK != null) 
                || (this.tcZtabExpLfa1PK != null && !this.tcZtabExpLfa1PK.equals(other.tcZtabExpLfa1PK))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.tcZtabExpLfa1PK != null ? this.tcZtabExpLfa1PK.hashCode() : 0);
        return hash;
    }
    
    /*@Override
    public int compareTo(Object o) {
        if( o == null ){
            return -1;
        }
        
        TcZtabExpLfa1 compObj = (TcZtabExpLfa1)o;
        
        return tcZtabExpLfa1PK.compareTo(compObj.getTcZtabExpLfa1PK());
    }*/
}
