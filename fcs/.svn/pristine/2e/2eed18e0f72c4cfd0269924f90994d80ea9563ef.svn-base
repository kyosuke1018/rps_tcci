/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.entity.sheetdata;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.time.DateUtils;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcCurrency;
import com.tcci.irs.entity.AccountNode;
import com.tcci.irs.entity.reconciling.IrsSheetdataReconcilingD;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Gilbert.Lin
 */
@Entity
@Table(name = "IRS_SHEETDATA_M")
@NamedQueries({
    @NamedQuery(name = "IrsSheetdataM.findAll", query = "SELECT i FROM IrsSheetdataM i"),
    @NamedQuery(name = "IrsSheetdataM.findByCompanyAndYM", query = "SELECT i FROM IrsSheetdataM i WHERE i.year = :year AND i.month = :month AND (i.reCompany = :company or i.paCompany = :company)")})
public class IrsSheetdataM implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "YEAR")
    private short year;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MONTH")
    private short month;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "RE_COMPANY_ID")
    @JoinColumn(name = "RE_COMPANY_ID", referencedColumnName = "ID")
    @ManyToOne
    private FcCompany reCompany;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "PA_COMPANY_ID")
    @JoinColumn(name = "PA_COMPANY_ID", referencedColumnName = "ID")
    @ManyToOne
    private FcCompany paCompany;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "RE_ACCOUNT_CODE")
    private String reAccountCode;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "CURRENCY_ID")
    @JoinColumn(name = "CURRENCY_ID", referencedColumnName = "ID")
    @ManyToOne
    private FcCurrency currency;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SORT")
    private short sort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "SHEET_TYPE")
    private String sheetType;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 50)
    @Column(name = "RE_ACCOUNT_NAME")
    private String reAccountName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "PA_ACCOUNT_CODE")
    private String paAccountCode;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 50)
    @Column(name = "PA_ACCOUNT_NAME")
    private String paAccountName;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "RE_AMOUNT_ORIG")
    private BigDecimal reAmountOrig;
    @Column(name = "PA_AMOUNT_ORIG")
    private BigDecimal paAmountOrig;
    @Column(name = "ACCOUNTPAIR_ID")
    private BigInteger accountpairId;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Column(name = "RE_AGREED_AMOUNT")
    private BigDecimal reAgreedAmount;//收款方本幣金額
    @Column(name = "RE_RAW_AMOUNT")
    private BigDecimal reRawAmount;//收款方當月交易金額
    @Column(name = "PA_AGREED_AMOUNT")
    private BigDecimal paAgreedAmount;//支付方本幣金額
    @Column(name = "PA_RAW_AMOUNT")
    private BigDecimal paRawAmount;//支付方當月交易金額
    @JoinColumn(name = "RE_ACCOUNT_ID", referencedColumnName = "ID")
    @ManyToOne
    private AccountNode reAccount;//收款方 account node
    @JoinColumn(name = "PA_ACCOUNT_ID", referencedColumnName = "ID")
    @ManyToOne
    private AccountNode paAccount;//支付方 account node
    
    
    
    @Transient
    private String yearMonth;
    public String getYearMonth() {
        return yearMonth;
    }
    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }
    
    @Transient
    private FcCompany individualCompany;//個體公司 公告主體
    public FcCompany getIndividualCompany() {
        return individualCompany;
    }
    public void setIndividualCompany(FcCompany individualCompany) {
        this.individualCompany = individualCompany;
    }
    //add
    @OneToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL,mappedBy="sheetdatam")
    private Collection<IrsSheetdataReconcilingD> irsSheetdataReconcilingDCollection;
    public Collection<IrsSheetdataReconcilingD> getIrsSheetdataReconcilingDCollection() {
        return irsSheetdataReconcilingDCollection;
    }
    public void setIrsSheetdataReconcilingDCollection(Collection<IrsSheetdataReconcilingD> irsSheetdataReconcilingDCollection) {
        this.irsSheetdataReconcilingDCollection = irsSheetdataReconcilingDCollection;
    }
    
    
    public IrsSheetdataM() {
    }

    public IrsSheetdataM(Long id) {
        this.id = id;
    }

    public IrsSheetdataM(Long id, short year, short month, FcCompany reCompany, FcCompany paCompany, String reAccountCode, FcCurrency currency, short sort, String sheetType, String reAccountName, String paAccountCode, String paAccountName) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.reCompany = reCompany;
        this.paCompany = paCompany;
        this.reAccountCode = reAccountCode;
        this.currency = currency;
        this.sort = sort;
        this.sheetType = sheetType;
        this.reAccountName = reAccountName;
        this.paAccountCode = paAccountCode;
        this.paAccountName = paAccountName;
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public short getMonth() {
        return month;
    }

    public void setMonth(short month) {
        this.month = month;
    }

    public FcCompany getReCompany() {
//        if(reCompany.equals(individualCompany)){
//            return reCompany;
//        }else{
//            return paCompany;
//        }
        return reCompany;
    }

    public void setReCompany(FcCompany reCompany) {
        this.reCompany = reCompany;
    }

    public FcCompany getPaCompany() {
//        if(reCompany.equals(individualCompany)){
//            return paCompany;
//        }else{
//            return reCompany;
//        }
        return paCompany;
    }

    public void setPaCompany(FcCompany paCompany) {
        this.paCompany = paCompany;
    }

    public String getReAccountCode() {
//        if(reCompany.equals(individualCompany)){
//            return reAccountCode;
//        }else{
//            return paAccountCode;
//        }
        return reAccountCode;
    }

    public void setReAccountCode(String reAccountCode) {
        this.reAccountCode = reAccountCode;
    }

    public FcCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(FcCurrency currency) {
        this.currency = currency;
    }

    public short getSort() {
        return sort;
    }

    public void setSort(short sort) {
        this.sort = sort;
    }

    public String getSheetType() {
        return sheetType;
    }

    public void setSheetType(String sheetType) {
        this.sheetType = sheetType;
    }

    public String getReAccountName() {
//        if(reCompany.equals(individualCompany)){
//            return reAccountName;
//        }else{
//            return paAccountName;
//        }
        return reAccountName;
    }

    public void setReAccountName(String reAccountName) {
        this.reAccountName = reAccountName;
    }

    public String getPaAccountCode() {
//        if(reCompany.equals(individualCompany)){
//            return paAccountCode;
//        }else{
//            return reAccountCode;
//        }
        return paAccountCode;
    }

    public void setPaAccountCode(String paAccountCode) {
        this.paAccountCode = paAccountCode;
    }

    public String getPaAccountName() {
//        if(reCompany.equals(individualCompany)){
//            return paAccountName;
//        }else{
//            return reAccountName;
//        }
        return paAccountName;
    }

    public void setPaAccountName(String paAccountName) {
        this.paAccountName = paAccountName;
    }

    public BigDecimal getReAmountOrig() {
//        if(reCompany.equals(individualCompany)){
//            return reAmountOrig;
//        }else{
//            return paAmountOrig;
//        }
        return reAmountOrig;
    }

    public void setReAmountOrig(BigDecimal reAmountOrig) {
        this.reAmountOrig = reAmountOrig;
    }

    public BigDecimal getPaAmountOrig() {
//        if(reCompany.equals(individualCompany)){
//            return paAmountOrig;
//        }else{
//            return reAmountOrig;
//        }
        return paAmountOrig;
    }

    public void setPaAmountOrig(BigDecimal paAmountOrig) {
        this.paAmountOrig = paAmountOrig;
    }

    public BigInteger getAccountpairId() {
        return accountpairId;
    }

    public void setAccountpairId(BigInteger accountpairId) {
        this.accountpairId = accountpairId;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public BigDecimal getReAgreedAmount() {
        return reAgreedAmount;
    }

    public void setReAgreedAmount(BigDecimal reAgreedAmount) {
        this.reAgreedAmount = reAgreedAmount;
    }

    public BigDecimal getReRawAmount() {
        return reRawAmount;
    }

    public void setReRawAmount(BigDecimal reRawAmount) {
        this.reRawAmount = reRawAmount;
    }

    public BigDecimal getPaAgreedAmount() {
        return paAgreedAmount;
    }

    public void setPaAgreedAmount(BigDecimal paAgreedAmount) {
        this.paAgreedAmount = paAgreedAmount;
    }

    public BigDecimal getPaRawAmount() {
        return paRawAmount;
    }

    public void setPaRawAmount(BigDecimal paRawAmount) {
        this.paRawAmount = paRawAmount;
    }

    public AccountNode getReAccount() {
        return reAccount;
    }

    public void setReAccount(AccountNode reAccount) {
        this.reAccount = reAccount;
    }

    public AccountNode getPaAccount() {
        return paAccount;
    }

    public void setPaAccount(AccountNode paAccount) {
        this.paAccount = paAccount;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IrsSheetdataM)) {
            return false;
        }
        IrsSheetdataM other = (IrsSheetdataM) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.irs.entity.sheetdata.IrsSheetdataM[ id=" + id + " ]";
    }
    
    //<editor-fold defaultstate="collapsed" desc="helper">
    public BigDecimal getDiff() {
        //RE+PA+ReconcilingAmount
        BigDecimal reconcilingAmount_sum = getReconcilingAmount();
        return getReAmountOrig().add(getPaAmountOrig()).add(reconcilingAmount_sum);
    }
    public BigDecimal getReconcilingAmount() {
        //reconcilingAmount_sum=Reconciling.RE+Reconciling.PA
        BigDecimal result = new BigDecimal("0");
        for (IrsSheetdataReconcilingD entity : irsSheetdataReconcilingDCollection) {
            String accountType = entity.getAccountType();
            BigDecimal amountAdjustments = entity.getAmountAdjustments();
            if(null != amountAdjustments){
                if("RE".equalsIgnoreCase(accountType)){
                    result = result.add(amountAdjustments);
                }else{//PA
                    result = result.add(amountAdjustments);
                }
                
            }

        }
        return result;
    }
    
    public String getFlag() {
//        if (null != createtimestamp) {
//            return "flag_warn.png";
//        }
        //20160315 modify
        //重新產生對帳表後createtimestamp備清掉  flag改以有無調節資料判斷
        if (CollectionUtils.isNotEmpty(irsSheetdataReconcilingDCollection)) {
            return "flag_warn.png";
        }
        
        BigDecimal zero = new BigDecimal("0.00");
        BigDecimal diff = getDiff();
        if (zero.equals(diff)) {
            return "flag_ok.png";
        } else {
            return "flag_ng.png";
        }
    }
    public String getAccountName(String accountType) {
        String result = "";
        if("RE".equalsIgnoreCase(accountType)){
            result = getReAccountCode()+"("+ getReAccountName() +")";
        }else{
            result = getPaAccountCode()+"("+ getPaAccountName() +")";
        }
        return result;
    }

    public FcCompany getReCompanyDisPlay() {
        if(reCompany.equals(individualCompany)){
            return reCompany;
        }else{
            return paCompany;
        }
    }
    public FcCompany getPaCompanyDisPlay() {
        if(reCompany.equals(individualCompany)){
            return paCompany;
        }else{
            return reCompany;
        }
    }
    public String getReAccountCodeDisPlay() {
        if(reCompany.equals(individualCompany)){
            return reAccountCode;
        }else{
            return paAccountCode;
        }
    }
    public String getReAccountNameDisPlay() {
        if(reCompany.equals(individualCompany)){
            return reAccountName;
        }else{
            return paAccountName;
        }
    }
    public String getPaAccountCodeDisPlay() {
        if(reCompany.equals(individualCompany)){
            return paAccountCode;
        }else{
            return reAccountCode;
        }
    }
    public String getPaAccountNameDisPlay() {
        if(reCompany.equals(individualCompany)){
            return paAccountName;
        }else{
            return reAccountName;
        }
    }
    public BigDecimal getReAmountOrigDisPlay() {
        if(reCompany.equals(individualCompany)){
            return reAmountOrig;
        }else{
            return paAmountOrig;
        }
    }
    public BigDecimal getPaAmountOrigDisPlay() {
        if(reCompany.equals(individualCompany)){
            return paAmountOrig;
        }else{
            return reAmountOrig;
        }
    }
    
    public String getYMString() {
        Date dt = DateUtils.getDate(year, month, 1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        return DateUtils.getYearMonth(calendar);
    }
    
    public BigDecimal getReAgreedAmountDisPlay() {
        if(reCompany.equals(individualCompany)){
            return reAgreedAmount;
        }else{
            return paAgreedAmount;
        }
    }
    public BigDecimal getPaAgreedAmountDisPlay() {
        if(reCompany.equals(individualCompany)){
            return paAgreedAmount;
        }else{
            return reAgreedAmount;
        }
    }
    public AccountNode getReAccountDisPlay() {
        if(reCompany.equals(individualCompany)){
            return reAccount;
        }else{
            return paAccount;
        }
    }
    public AccountNode getPaAccountDisPlay() {
        if(reCompany.equals(individualCompany)){
            return paAccount;
        }else{
            return reAccount;
        }
    }
    //</editor-fold>
    
}
