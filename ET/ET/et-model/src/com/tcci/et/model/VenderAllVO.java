/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model;

import com.tcci.et.enums.RfqVenderSrcEnum;
import com.tcci.et.enums.VenderStatusEnum;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author peter.pan
 */
public class VenderAllVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id; // PK ID
    private String name; // 中文名稱
    private Long applyId; // 新商申請ID (ET_MEMBER_FORM.ID)
    private String mandt; // SAP CLIENT
    private String lifnr; // SAP 供應商代碼
    private String lifnrUi; // LIFNR 去前 3 碼 0
    private Boolean disabled; // 無效
    private Date synctimestamp; // SAP同步時間
    private String status; // 預留欄位做特殊判斷
    
    private Long creatorId; // 建立人
    private Date createtime; // 建立時間
    private Long modifierId; // 修改人
    private Date modifytime; // 修改時間
    
    // TC_ZTAB_EXP_LFA1
    //private String mandt; // 用戶端
    //private String lifnr; // 供應商或貸方的帳號
    private String name1; // 名稱 1
    private String sperm; // 採購組織層次的採購凍結
    private String ekorg; // 採購組織
    private String ekotx; // 採購組織說明
    private String verkf; // 供應商辦公室裡的負責銷售員
    private String telf1; // 供應商電話號碼
    private String telbx; // 電子信箱號碼
    private String waers; // 採購單幣別
    private String zterm; // 付款條件碼
    private String ort01; // 城市
    private String strSuppl1; // 街道 2
    private String land1; // 國家代碼
    private String sortl; // 搜尋條件 1
    private String loevm; // 供應商在採購層次的刪除旗標
    //private String lifnrUi; // AP用供應商代碼，去掉Prefix 3個0
    private Date syncTimeStamp; // SAP資料同步時間
    private String name2; // 名稱 2
    private String name3; // 名稱 3
    private String name4; // 名稱 4
    private String telfx; // 傳真號碼
    private String risk; // 高風險
    private String csperm; // 中央採購凍結
    private String sperq; // 將被凍結的功能
    private String zCannot_block; // 永久不可凍結
    private Date zNblock_sdate; // 不可凍結開始日期
    private Date zNblock_edate; // 不可凍結結束日期
    private String zartiPerson1; // 法人１ 
    private String zartiPerson2; // 高風險
    private String zartiPerson3; // 法人3
    private String zstockholder1; // 股東１
    private String zstockholder2; // 股東２
    private String zstockholder3; // 股東３
    private String zstockholder4; // 股東４
    private String zstockholder5; // 股東５
    private String zvendorType; // 供應商A1/A2/A3/B/C分類
    private String pstlz; // 郵遞區號
    private String sort2; // 搜尋條件 2
    private String street; // 街道
    private String strSuppl2; // 街道 3
    private String strSuppl3; // 街道 4
    private String spras; // 語言代碼
    private String telNumber; // 第一個電話號碼：撥號代碼 + 號碼
    private String telExtens; // 第一個電話號碼：分機
    private String telf2; // 第二個電話號碼
    private String adrnr; // 地址
    private String konzs; // 群組碼
    private String ktokk; // 供應商科目群組
    private String stceg; // 加值稅登記號碼
    private String lnrza; // 代理收款人帳號
    private String banks; // 銀行國碼
    private String bankl; // 凍結原因
    private String banka; // 銀行名稱
    private String brnch; // 銀行分行
    private String bankn; // 銀行帳號
    private String bvtyp; // 夥伴銀行類型
    private String bkref; // 銀行明細的參考規範
    private String koinh; // 帳戶所有人名稱
    private String loevmX; // 主檔記錄的中央刪除旗標
    private String sperr; // 中央過帳凍結
    private String confs; // 更改權限狀態（中央） 
    private String zzremark; // 備註/說明
    private String bukrs; // 公司代碼
    private String butxt; // 公司代碼或公司名稱
    private String loevmB; // 主檔記錄的刪除旗標（公司代碼層次） 
    private String zuawa; // 根據指派號碼的排序碼
    private String akont; // 總帳科目中的統制科目
    private String begru; // 權限群組
    private String zwels; // 待考慮之付款方式清單
    private String zahls; // 付款的凍結碼
    private String ztermB; // 付款條件碼
    private String fdgrv; // 規劃群組
    private String lnrzb; // 替代受款人帳號
    private String reprf; // 雙重發票或貸項通知單的核查旗標
    private String mindk; // 少數指示碼
    private String uzawe; // 付款方法補充
    private String webre; // 指示碼：以 GR 為基礎的發票驗證
    private String mobNumber; // 
    private Date erdat; // 紀錄建立日期
    
    // ET_RFQ_VENDER
    private Long rfqVenderId;// ET_RFQ_VENDER.ID
    private String source;
    
    // for UI
    private boolean selected;
    private Date syncTime;//  max sync_time_stamp

    public VenderStatusEnum getStatusEnum(){
        return VenderStatusEnum.getFromCode(status);
    }
    public String getStatusLabel(){
        VenderStatusEnum statusEnum = getStatusEnum();
        return statusEnum!=null?statusEnum.getDisplayName():status;
    }

    public RfqVenderSrcEnum getSourceEnum(){
        return RfqVenderSrcEnum.getFromCode(source);
    }
    public String getSourceLabel(){
        RfqVenderSrcEnum srcEnum = getSourceEnum();
        return srcEnum!=null?srcEnum.getDisplayName():source;
    }
            
    public String getVenderLabel(){
        StringBuilder sb = new StringBuilder();
        sb.append(name!=null? name:"");
        
        if( lifnrUi !=null ){
            sb.append(" (").append(lifnrUi).append(")");
        }else{
            sb.append(" (").append(applyId).append(")");
        }
        
        return sb.toString();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getSperm() {
        return sperm;
    }

    public void setSperm(String sperm) {
        this.sperm = sperm;
    }

    public String getEkorg() {
        return ekorg;
    }

    public void setEkorg(String ekorg) {
        this.ekorg = ekorg;
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

    public Date getSyncTimeStamp() {
        return syncTimeStamp;
    }

    public void setSyncTimeStamp(Date syncTimeStamp) {
        this.syncTimeStamp = syncTimeStamp;
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

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
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

    public String getzCannot_block() {
        return zCannot_block;
    }

    public void setzCannot_block(String zCannot_block) {
        this.zCannot_block = zCannot_block;
    }

    public Date getzNblock_sdate() {
        return zNblock_sdate;
    }

    public void setzNblock_sdate(Date zNblock_sdate) {
        this.zNblock_sdate = zNblock_sdate;
    }

    public Date getzNblock_edate() {
        return zNblock_edate;
    }

    public void setzNblock_edate(Date zNblock_edate) {
        this.zNblock_edate = zNblock_edate;
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

    public String getZvendorType() {
        return zvendorType;
    }

    public void setZvendorType(String zvendorType) {
        this.zvendorType = zvendorType;
    }

    public String getPstlz() {
        return pstlz;
    }

    public void setPstlz(String pstlz) {
        this.pstlz = pstlz;
    }

    public String getSort2() {
        return sort2;
    }

    public void setSort2(String sort2) {
        this.sort2 = sort2;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStrSuppl2() {
        return strSuppl2;
    }

    public void setStrSuppl2(String strSuppl2) {
        this.strSuppl2 = strSuppl2;
    }

    public String getStrSuppl3() {
        return strSuppl3;
    }

    public void setStrSuppl3(String strSuppl3) {
        this.strSuppl3 = strSuppl3;
    }

    public String getSpras() {
        return spras;
    }

    public void setSpras(String spras) {
        this.spras = spras;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getTelExtens() {
        return telExtens;
    }

    public void setTelExtens(String telExtens) {
        this.telExtens = telExtens;
    }

    public String getTelf2() {
        return telf2;
    }

    public void setTelf2(String telf2) {
        this.telf2 = telf2;
    }

    public String getAdrnr() {
        return adrnr;
    }

    public void setAdrnr(String adrnr) {
        this.adrnr = adrnr;
    }

    public String getKonzs() {
        return konzs;
    }

    public void setKonzs(String konzs) {
        this.konzs = konzs;
    }

    public String getKtokk() {
        return ktokk;
    }

    public void setKtokk(String ktokk) {
        this.ktokk = ktokk;
    }

    public String getStceg() {
        return stceg;
    }

    public void setStceg(String stceg) {
        this.stceg = stceg;
    }

    public String getLnrza() {
        return lnrza;
    }

    public void setLnrza(String lnrza) {
        this.lnrza = lnrza;
    }

    public String getBanks() {
        return banks;
    }

    public void setBanks(String banks) {
        this.banks = banks;
    }

    public String getBankl() {
        return bankl;
    }

    public void setBankl(String bankl) {
        this.bankl = bankl;
    }

    public String getBanka() {
        return banka;
    }

    public void setBanka(String banka) {
        this.banka = banka;
    }

    public String getBrnch() {
        return brnch;
    }

    public void setBrnch(String brnch) {
        this.brnch = brnch;
    }

    public String getBankn() {
        return bankn;
    }

    public void setBankn(String bankn) {
        this.bankn = bankn;
    }

    public String getBvtyp() {
        return bvtyp;
    }

    public void setBvtyp(String bvtyp) {
        this.bvtyp = bvtyp;
    }

    public String getBkref() {
        return bkref;
    }

    public void setBkref(String bkref) {
        this.bkref = bkref;
    }

    public String getKoinh() {
        return koinh;
    }

    public void setKoinh(String koinh) {
        this.koinh = koinh;
    }

    public String getLoevmX() {
        return loevmX;
    }

    public void setLoevmX(String loevmX) {
        this.loevmX = loevmX;
    }

    public String getSperr() {
        return sperr;
    }

    public void setSperr(String sperr) {
        this.sperr = sperr;
    }

    public String getConfs() {
        return confs;
    }

    public void setConfs(String confs) {
        this.confs = confs;
    }

    public String getZzremark() {
        return zzremark;
    }

    public void setZzremark(String zzremark) {
        this.zzremark = zzremark;
    }

    public String getBukrs() {
        return bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }

    public String getButxt() {
        return butxt;
    }

    public void setButxt(String butxt) {
        this.butxt = butxt;
    }

    public String getLoevmB() {
        return loevmB;
    }

    public void setLoevmB(String loevmB) {
        this.loevmB = loevmB;
    }

    public String getZuawa() {
        return zuawa;
    }

    public void setZuawa(String zuawa) {
        this.zuawa = zuawa;
    }

    public String getAkont() {
        return akont;
    }

    public void setAkont(String akont) {
        this.akont = akont;
    }

    public String getBegru() {
        return begru;
    }

    public void setBegru(String begru) {
        this.begru = begru;
    }

    public String getZwels() {
        return zwels;
    }

    public void setZwels(String zwels) {
        this.zwels = zwels;
    }

    public String getZahls() {
        return zahls;
    }

    public void setZahls(String zahls) {
        this.zahls = zahls;
    }

    public String getZtermB() {
        return ztermB;
    }

    public void setZtermB(String ztermB) {
        this.ztermB = ztermB;
    }

    public String getFdgrv() {
        return fdgrv;
    }

    public void setFdgrv(String fdgrv) {
        this.fdgrv = fdgrv;
    }

    public String getLnrzb() {
        return lnrzb;
    }

    public void setLnrzb(String lnrzb) {
        this.lnrzb = lnrzb;
    }

    public String getReprf() {
        return reprf;
    }

    public void setReprf(String reprf) {
        this.reprf = reprf;
    }

    public String getMindk() {
        return mindk;
    }

    public void setMindk(String mindk) {
        this.mindk = mindk;
    }

    public String getUzawe() {
        return uzawe;
    }

    public void setUzawe(String uzawe) {
        this.uzawe = uzawe;
    }

    public String getWebre() {
        return webre;
    }

    public void setWebre(String webre) {
        this.webre = webre;
    }

    public String getMobNumber() {
        return mobNumber;
    }

    public void setMobNumber(String mobNumber) {
        this.mobNumber = mobNumber;
    }

    public Date getErdat() {
        return erdat;
    }

    public void setErdat(Date erdat) {
        this.erdat = erdat;
    }

    public Long getRfqVenderId() {
        return rfqVenderId;
    }

    public void setRfqVenderId(Long rfqVenderId) {
        this.rfqVenderId = rfqVenderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
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

    public String getLifnrUi() {
        return lifnrUi;
    }

    public void setLifnrUi(String lifnrUi) {
        this.lifnrUi = lifnrUi;
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

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Date getSynctimestamp() {
        return synctimestamp;
    }

    public void setSynctimestamp(Date synctimestamp) {
        this.synctimestamp = synctimestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        if (!(object instanceof VenderAllVO)) {
            return false;
        }
        VenderAllVO other = (VenderAllVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.EtVender[ id=" + id + " ]";
    }
    
}
