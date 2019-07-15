/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.rs;

import com.tcci.et.model.rs.StrOptionVO;
import com.tcci.et.model.rs.LongOptionVO;
import com.tcci.et.model.OptionVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 注意：Long 傳回 0 時要特別處理
 * @author Peter.pan
 */
@XmlRootElement
public class SubmitVO implements Serializable {
    // 共用
    private Long id;
    private Long memberId;
    private Long sellerId;
    private Long storeId;
    private Long prdId;
    private Long orderId;
    private Long cartId;
    private Long typeId;
    private Long appId;//TC_APPLICATIONDATA
    private String type;

    private String cname;
    private String ename;
    private List<String> errors;
    private Integer sortnum;
    private String title;
    private String desc;

    private Integer offset;
    private Integer rows;
    private String sortField;
    private String sortOrder;
    
    private String status;
    private String keyword;
    private String orderNumber;
    private String orderStatus;
    private String rfqStatus;
    private String payStatus;
    private String shipStatus;  
    private String transactionType;// PAYMENT_TYPE
    private Boolean replyMsg;
    
    private String primaryType;
    private Long primaryId;
    private Long parent;

    private Date startAt;
    private Date endAt;
    private String endYM;
    private String dateStr;

    private List<Long> idList;
    private List<Long> storeList;
    private List<Long> prdList;
    private List<Long> orderList;
    
    private Boolean fullData;
    private String orderby;
    private String memberType;
    private BigDecimal quantity;
    private String memo;
            
    private Long areaId;
    private List<Long> areaList;
    
    // for mobile login
    private String from;
    private String to;
    private String token;
    
    // for EC_STORE 
    private String remitAccount;
    private Boolean opened;
    
    // for ET_MEMBER
    private String loginAccount;
    private String name;
    private String email;
    private String phone;
    private Boolean active;
    private String pwd;
    
    private String pwdOri;
    private String pwdNew;
    private String pwdNew2;
    //VENDER APPLY
    private Long venderId;
    private Long factoryId;
    private String applyVenderCode;
    private String applyVenderName;
    private List<String> industryList;
    
    //TENDER
    private Long tenderId;
    
    //private Boolean disabled;
    private Boolean adminUser;
    private Boolean sellerApply;
    private Boolean sellerApprove;
    private Date approvetime;
    private Boolean forAdmin;
    private Boolean forDealer;
    private String salt;
    private String iv;
    private String passPhrase;
    private Integer iterationCount;
    
    private Boolean tccDealer;
    private Boolean tccDs;
    private Boolean identityUnion;// 身分別查詢條件 AND or OR
    private String verifyCode;
    
    // EC_PERSON
    private String gender;
    private Date birthday;
    private Integer age;
    
    // EC_COMPAMY 
    private Long mainId;
    private String idCode;
    private String nickname;
    private String email1;
    private String email2;
    private String email3;
    private String tel1;
    private String tel2;
    private String tel3;
    private String tel4;
    private String fax1;
    private String fax2;
    private Long country;
    private String countryCode;//國別代碼
    private Long state;
    private String addr1;
    private String addr2;
    private String idType;
    private String brief;
    private String brief2;
    private String brief3;
    private String owner1;
    private String owner2;
    private String owner1Tile;
    private String owner2Tile;
    private String contact1;
    private String contact2;
    private String contact3;
    private String webId1;
    private String webId2;
    private String longitude;
    private String latitude;
    private String url1;
    private String url2;
//    private Date startAt;//創立時間
    private Long category;
    private String industry;//行業別
    private BigDecimal capital;//資本額
    private BigDecimal yearIncome;//年收入
    private BigDecimal yearRevenue;//年營業額
    private BigDecimal employee;//員工人數
    private Long currency;//訂單幣別
    private String currencyCode;//幣別代碼
    private String bank1;
    private String bank2;
    private String bank1Beneficiary;
    private String bank2Beneficiary;
    private String bank1Code;
    private String bank2Code;
    private String bank1Account;
    private String bank2Account;
    private String bank1Addr;
    private String bank2Addr;
    
    // 商品基本資訊
    private Long vendorId;
    private Long brandId;
    private Long serialId;
    private BigDecimal price;
    private BigDecimal compareAtPrice;
    private Long priceUnit;
    private BigDecimal priceAmt;
    private Long itemUnit;
    private BigDecimal itemAmt;
    private String code;
    private BigDecimal weight;
    private Long weightUnit;
    private String volume;
    private BigDecimal minAmt;
    private BigDecimal maxAmt;
    private Boolean intAmt;
    private Date publishTime;
    private Boolean hasVariations;
    private Boolean pass;
    private Long coverPicId;
    private Long currencyId;
    
    // 商品多型別資訊
    private Long colorId;
    private Long sizeId;
    private String barcode;
    private String sku;
    private String colorName;
    private String sizeName;
    
    // 商品分類
    private Integer levelnum;
    private Long typeL1;
    private Long typeL2;
    // 商品簡介
    private List<String> introductions; 
    // 商品配送方式
    private List<LongOptionVO> shippings;
    // 商品付款方式
    private List<LongOptionVO> payments;
    // 商品明細
    private String contentTxt;
    private String contentType;
    // 商品圖片
    private String description;
    
    // 會員查詢
    private String telKeyword;
    private String emailKeyword;
    
    // 客戶查詢
    private Long cusLevel;
    private Double cumulativeS;// 累積消費
    private Double cumulativeE;
    private String creditStatus;// 信用額度狀態
    
    // 客戶資料
    private Long levelId;
    private BigDecimal credits;
    private String cusType;

    // 訂單處理 & 客戶意見反映處理
    private String process;
    private Date processTime;
    
    // 客戶意見反映處理
    //private Long typeId;
    private String action;
    
    // 訂單查詢
    private String cusKeyword;
    private List<String> statusList;
    private List<String> ignoreStatusList;
    private Boolean closed;
    
    // 訂單評價
    private Integer sellerRate;
    private String sellerMessage;
    
    // 詢價單
    private Boolean rfq;
    private BigDecimal shippingTotal;
    private BigDecimal subTotal;
    private BigDecimal total;
    private String message;
    
    // 商店
    private List<StrOptionVO> shipMethods;
    private List<StrOptionVO> payMethods;
    //private List<PaymentVO> paymentList; 
    //private List<ShippingVO> shippingList; 
    private List<LongOptionVO> storeAreas;
    
    private List<OptionVO> optionList;
    // 匯入商品
    private List<ImportProductVO> importPrdList;
    // 系統公告
    private Date starttime;
    private Date endtime;
    private String content;
    
    private List<String> headers;// for export
    
    private Boolean existed;
    
    // PO 報價、改量
    //private Long orderId;
    //private String status;
    //private BigDecimal total;
    //private BigDecimal subTotal;
    //private BigDecimal shippingTotal;
    private List<OrderQuoteVO> quoteList;
    private String statusType;
    
    private Long dealerId;
    private Long dsId;// downstream Id
    private Boolean noPicOnly;
    
    // for 商店多人管理
    private Boolean storeManager;// 商店管理員
    private Long manageStoreId;
    private String roleCode;
    private Boolean enable;
    
    private String language;
    
    public SubmitVO() {}

    public Boolean getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(Boolean adminUser) {
        this.adminUser = adminUser;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getForDealer() {
        return forDealer;
    }

    public void setForDealer(Boolean forDealer) {
        this.forDealer = forDealer;
    }

    public Long getManageStoreId() {
        return manageStoreId;
    }

    public void setManageStoreId(Long manageStoreId) {
        this.manageStoreId = manageStoreId;
    }

    public Boolean getStoreManager() {
        return storeManager;
    }

    public void setStoreManager(Boolean storeManager) {
        this.storeManager = storeManager;
    }

    public String getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(String creditStatus) {
        this.creditStatus = creditStatus;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public Boolean getNoPicOnly() {
        return noPicOnly;
    }

    public void setNoPicOnly(Boolean noPicOnly) {
        this.noPicOnly = noPicOnly;
    }

    public Boolean getIdentityUnion() {
        return identityUnion;
    }

    public void setIdentityUnion(Boolean identityUnion) {
        this.identityUnion = identityUnion;
    }

    public Boolean getExisted() {
        return existed;
    }

    public void setExisted(Boolean existed) {
        this.existed = existed;
    }

    public Long getDealerId() {
        return dealerId;
    }

    public void setDealerId(Long dealerId) {
        this.dealerId = dealerId;
    }

    public Long getDsId() {
        return dsId;
    }

    public void setDsId(Long dsId) {
        this.dsId = dsId;
    }

    public List<OrderQuoteVO> getQuoteList() {
        return quoteList;
    }

    public void setQuoteList(List<OrderQuoteVO> quoteList) {
        this.quoteList = quoteList;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public Boolean getReplyMsg() {
        return replyMsg;
    }

    public void setReplyMsg(Boolean replyMsg) {
        this.replyMsg = replyMsg;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public BigDecimal getYearIncome() {
        return yearIncome;
    }

    public void setYearIncome(BigDecimal yearIncome) {
        this.yearIncome = yearIncome;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public List<Long> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Long> areaList) {
        this.areaList = areaList;
    }

    public String getPwdOri() {
        return pwdOri;
    }

    public void setPwdOri(String pwdOri) {
        this.pwdOri = pwdOri;
    }

    public String getPwdNew() {
        return pwdNew;
    }

    public void setPwdNew(String pwdNew) {
        this.pwdNew = pwdNew;
    }

    public String getPwdNew2() {
        return pwdNew2;
    }

    public void setPwdNew2(String pwdNew2) {
        this.pwdNew2 = pwdNew2;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<LongOptionVO> getStoreAreas() {
        return storeAreas;
    }

    public void setStoreAreas(List<LongOptionVO> storeAreas) {
        this.storeAreas = storeAreas;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }

    public Boolean getPass() {
        return pass;
    }

    public void setPass(Boolean pass) {
        this.pass = pass;
    }

    public String getRemitAccount() {
        return remitAccount;
    }

    public void setRemitAccount(String remitAccount) {
        this.remitAccount = remitAccount;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Long getCoverPicId() {
        return coverPicId;
    }

    public void setCoverPicId(Long coverPicId) {
        this.coverPicId = coverPicId;
    }

    public Boolean getForAdmin() {
        return forAdmin;
    }

    public void setForAdmin(Boolean forAdmin) {
        this.forAdmin = forAdmin;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public Boolean getHasVariations() {
        return hasVariations;
    }

    public void setHasVariations(Boolean hasVariations) {
        this.hasVariations = hasVariations;
    }

    public List<StrOptionVO> getShipMethods() {
        return shipMethods;
    }

    public void setShipMethods(List<StrOptionVO> shipMethods) {
        this.shipMethods = shipMethods;
    }

    public List<StrOptionVO> getPayMethods() {
        return payMethods;
    }

    public void setPayMethods(List<StrOptionVO> payMethods) {
        this.payMethods = payMethods;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public Integer getSellerRate() {
        return sellerRate;
    }

    public void setSellerRate(Integer sellerRate) {
        this.sellerRate = sellerRate;
    }

    public List<ImportProductVO> getImportPrdList() {
        return importPrdList;
    }

    public void setImportPrdList(List<ImportProductVO> importPrdList) {
        this.importPrdList = importPrdList;
    }

    public BigDecimal getMaxAmt() {
        return maxAmt;
    }

    public void setMaxAmt(BigDecimal maxAmt) {
        this.maxAmt = maxAmt;
    }

    public BigDecimal getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(BigDecimal minAmt) {
        this.minAmt = minAmt;
    }

    public Boolean getIntAmt() {
        return intAmt;
    }

    public void setIntAmt(Boolean intAmt) {
        this.intAmt = intAmt;
    }

    public String getSellerMessage() {
        return sellerMessage;
    }

    public void setSellerMessage(String sellerMessage) {
        this.sellerMessage = sellerMessage;
    }

    public String getPassPhrase() {
        return passPhrase;
    }

    public void setPassPhrase(String passPhrase) {
        this.passPhrase = passPhrase;
    }

    public Integer getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(Integer iterationCount) {
        this.iterationCount = iterationCount;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getRfq() {
        return rfq;
    }

    public void setRfq(Boolean rfq) {
        this.rfq = rfq;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    public List<String> getIgnoreStatusList() {
        return ignoreStatusList;
    }

    public void setIgnoreStatusList(List<String> ignoreStatusList) {
        this.ignoreStatusList = ignoreStatusList;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getRfqStatus() {
        return rfqStatus;
    }

    public void setRfqStatus(String rfqStatus) {
        this.rfqStatus = rfqStatus;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Boolean getSellerApply() {
        return sellerApply;
    }

    public void setSellerApply(Boolean sellerApply) {
        this.sellerApply = sellerApply;
    }

    public Boolean getSellerApprove() {
        return sellerApprove;
    }

    public void setSellerApprove(Boolean sellerApprove) {
        this.sellerApprove = sellerApprove;
    }

    public Date getApprovetime() {
        return approvetime;
    }

    public void setApprovetime(Date approvetime) {
        this.approvetime = approvetime;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getLevelId() {
        return levelId;
    }
    
    public void setLevelId(Long levelId) {
        this.levelId = (levelId==null||levelId==0)?null:levelId;
    }

    public String getCusKeyword() {
        return cusKeyword;
    }

    public void setCusKeyword(String cusKeyword) {
        this.cusKeyword = cusKeyword;
    }

    public Long getCusLevel() {
        return cusLevel;
    }

    public void setCusLevel(Long cusLevel) {
        this.cusLevel = (cusLevel==null||cusLevel==0)?null:cusLevel;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = (colorId==null||colorId==0)?null:colorId;
    }

    public Long getSizeId() {
        return sizeId;
    }

    public void setSizeId(Long sizeId) {
        this.sizeId = (sizeId==null||sizeId==0)?null:sizeId;
    }

    public List<OptionVO> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<OptionVO> optionList) {
        this.optionList = optionList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getCumulativeS() {
        return cumulativeS;
    }

    public void setCumulativeS(Double cumulativeS) {
        this.cumulativeS = cumulativeS;
    }

    public Double getCumulativeE() {
        return cumulativeE;
    }

    public void setCumulativeE(Double cumulativeE) {
        this.cumulativeE = cumulativeE;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentTxt() {
        return contentTxt;
    }

    public void setContentTxt(String contentTxt) {
        this.contentTxt = contentTxt;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = (vendorId==null||vendorId==0)?null:vendorId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = (brandId==null||brandId==0)?null:brandId;
    }

    public Long getSerialId() {
        return serialId;
    }

    public void setSerialId(Long serialId) {
        this.serialId = (serialId==null||serialId==0)?null:serialId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCompareAtPrice() {
        return compareAtPrice;
    }

    public void setCompareAtPrice(BigDecimal compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    public Long getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Long priceUnit) {
        this.priceUnit = (priceUnit==null||priceUnit==0)?null:priceUnit;
    }

    public BigDecimal getPriceAmt() {
        return priceAmt;
    }

    public void setPriceAmt(BigDecimal priceAmt) {
        this.priceAmt = priceAmt;
    }

    public Long getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(Long itemUnit) {
        this.itemUnit = (itemUnit==null||itemUnit==0)?null:itemUnit;
    }

    public BigDecimal getItemAmt() {
        return itemAmt;
    }

    public void setItemAmt(BigDecimal itemAmt) {
        this.itemAmt = itemAmt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Long getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(Long weightUnit) {
        this.weightUnit = (weightUnit==null||weightUnit==0)?null:weightUnit;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public List<String> getIntroductions() {
        return introductions;
    }

    public void setIntroductions(List<String> introductions) {
        this.introductions = introductions;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public Integer getLevelnum() {
        return levelnum;
    }

    public void setLevelnum(Integer levelnum) {
        this.levelnum = levelnum;
    }

    public Long getTypeL1() {
        return typeL1;
    }

    public void setTypeL1(Long typeL1) {
        this.typeL1 = (typeL1==null||typeL1==0)?null:typeL1;
    }

    public Long getTypeL2() {
        return typeL2;
    }

    public void setTypeL2(Long typeL2) {
        this.typeL2 = (typeL2==null||typeL2==0)?null:typeL2;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getMainId() {
        return mainId;
    }

    public void setMainId(Long mainId) {
        this.mainId = mainId;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getTel3() {
        return tel3;
    }

    public void setTel3(String tel3) {
        this.tel3 = tel3;
    }

    public String getFax1() {
        return fax1;
    }

    public void setFax1(String fax1) {
        this.fax1 = fax1;
    }

    public String getFax2() {
        return fax2;
    }

    public void setFax2(String fax2) {
        this.fax2 = fax2;
    }

    public Long getCountry() {
        return country;
    }

    public void setCountry(Long country) {
        this.country = country;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOwner1() {
        return owner1;
    }

    public void setOwner1(String owner1) {
        this.owner1 = owner1;
    }

    public String getOwner2() {
        return owner2;
    }

    public void setOwner2(String owner2) {
        this.owner2 = owner2;
    }

    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    public String getContact3() {
        return contact3;
    }

    public void setContact3(String contact3) {
        this.contact3 = contact3;
    }

    public String getWebId1() {
        return webId1;
    }

    public void setWebId1(String webId1) {
        this.webId1 = webId1;
    }

    public String getWebId2() {
        return webId2;
    }

    public void setWebId2(String webId2) {
        this.webId2 = webId2;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public List<LongOptionVO> getShippings() {
        return shippings;
    }

    public void setShippings(List<LongOptionVO> shippings) {
        this.shippings = shippings;
    }

    public List<LongOptionVO> getPayments() {
        return payments;
    }

    public void setPayments(List<LongOptionVO> payments) {
        this.payments = payments;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = (storeId==null||storeId==0)?null:storeId;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = (prdId==null||prdId==0)?null:prdId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = (orderId==null||orderId==0)?null:orderId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(String shipStatus) {
        this.shipStatus = shipStatus;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public Long getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(Long primaryId) {
        this.primaryId = primaryId;
    }

    public Date getStartAt() {
        return startAt;
    }

    public BigDecimal getShippingTotal() {
        return shippingTotal;
    }

    public void setShippingTotal(BigDecimal shippingTotal) {
        this.shippingTotal = shippingTotal;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public String getEndYM() {
        return endYM;
    }

    public void setEndYM(String endYM) {
        this.endYM = endYM;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public List<Long> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Long> storeList) {
        this.storeList = storeList;
    }

    public List<Long> getPrdList() {
        return prdList;
    }

    public void setPrdList(List<Long> prdList) {
        this.prdList = prdList;
    }

    public List<Long> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Long> orderList) {
        this.orderList = orderList;
    }

    public Boolean getFullData() {
        return fullData;
    }

    public void setFullData(Boolean fullData) {
        this.fullData = fullData;
    }

    public String getOrderBy() {
        return orderby;
    }

    public void setOrderBy(String orderby) {
        this.orderby = orderby;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getTelKeyword() {
        return telKeyword;
    }

    public void setTelKeyword(String telKeyword) {
        this.telKeyword = telKeyword;
    }

    public String getEmailKeyword() {
        return emailKeyword;
    }

    public void setEmailKeyword(String emailKeyword) {
        this.emailKeyword = emailKeyword;
    }

    public Boolean getTccDealer() {
        return tccDealer;
    }

    public void setTccDealer(Boolean tccDealer) {
        this.tccDealer = tccDealer;
    }

    public Boolean getTccDs() {
        return tccDs;
    }

    public void setTccDs(Boolean tccDs) {
        this.tccDs = tccDs;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public String getApplyVenderCode() {
        return applyVenderCode;
    }

    public void setApplyVenderCode(String applyVenderCode) {
        this.applyVenderCode = applyVenderCode;
    }

    public String getApplyVenderName() {
        return applyVenderName;
    }

    public void setApplyVenderName(String applyVenderName) {
        this.applyVenderName = applyVenderName;
    }

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getTel4() {
        return tel4;
    }

    public void setTel4(String tel4) {
        this.tel4 = tel4;
    }

    public String getBrief2() {
        return brief2;
    }

    public void setBrief2(String brief2) {
        this.brief2 = brief2;
    }

    public String getBrief3() {
        return brief3;
    }

    public void setBrief3(String brief3) {
        this.brief3 = brief3;
    }

    public String getOwner1Tile() {
        return owner1Tile;
    }

    public void setOwner1Tile(String owner1Tile) {
        this.owner1Tile = owner1Tile;
    }

    public String getOwner2Tile() {
        return owner2Tile;
    }

    public void setOwner2Tile(String owner2Tile) {
        this.owner2Tile = owner2Tile;
    }

    public BigDecimal getYearRevenue() {
        return yearRevenue;
    }

    public void setYearRevenue(BigDecimal yearRevenue) {
        this.yearRevenue = yearRevenue;
    }

    public BigDecimal getEmployee() {
        return employee;
    }

    public void setEmployee(BigDecimal employee) {
        this.employee = employee;
    }

    public Long getCurrency() {
        return currency;
    }

    public void setCurrency(Long currency) {
        this.currency = currency;
    }

    public String getBank1() {
        return bank1;
    }

    public void setBank1(String bank1) {
        this.bank1 = bank1;
    }

    public String getBank2() {
        return bank2;
    }

    public void setBank2(String bank2) {
        this.bank2 = bank2;
    }

    public String getBank1Beneficiary() {
        return bank1Beneficiary;
    }

    public void setBank1Beneficiary(String bank1Beneficiary) {
        this.bank1Beneficiary = bank1Beneficiary;
    }

    public String getBank2Beneficiary() {
        return bank2Beneficiary;
    }

    public void setBank2Beneficiary(String bank2Beneficiary) {
        this.bank2Beneficiary = bank2Beneficiary;
    }

    public String getBank1Code() {
        return bank1Code;
    }

    public void setBank1Code(String bank1Code) {
        this.bank1Code = bank1Code;
    }

    public String getBank2Code() {
        return bank2Code;
    }

    public void setBank2Code(String bank2Code) {
        this.bank2Code = bank2Code;
    }

    public String getBank1Account() {
        return bank1Account;
    }

    public void setBank1Account(String bank1Account) {
        this.bank1Account = bank1Account;
    }

    public String getBank2Account() {
        return bank2Account;
    }

    public void setBank2Account(String bank2Account) {
        this.bank2Account = bank2Account;
    }

    public String getBank1Addr() {
        return bank1Addr;
    }

    public void setBank1Addr(String bank1Addr) {
        this.bank1Addr = bank1Addr;
    }

    public String getBank2Addr() {
        return bank2Addr;
    }

    public void setBank2Addr(String bank2Addr) {
        this.bank2Addr = bank2Addr;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public List<String> getIndustryList() {
        return industryList;
    }

    public void setIndustryList(List<String> industryList) {
        this.industryList = industryList;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}