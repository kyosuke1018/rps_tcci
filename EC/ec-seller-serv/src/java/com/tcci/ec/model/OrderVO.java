/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tcci.cm.annotation.ExcelFileFieldMeta;
import com.tcci.cm.annotation.ExcelFileMeta;
import com.tcci.cm.annotation.InputCheckMeta;
import com.tcci.cm.annotation.enums.DataTypeEnum;
import com.tcci.cm.facade.rs.model.ISODateTimeDeserializer;
import com.tcci.cm.facade.rs.model.ISODateTimeSerializer;
import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.PayStatusEnum;
import com.tcci.ec.enums.RfqStatusEnum;
import com.tcci.ec.enums.ShipStatusEnum;
import com.tcci.ec.model.rs.BaseResponseVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 
 * @author Peter.pan
 */
@ExcelFileMeta(headerRow = 0, rowNumColumnName = "rowNum")
public class OrderVO extends BaseResponseVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelFileFieldMeta(exportIndex = 0, dataType = DataTypeEnum.LONG, headerName = "ID")
    private Long id;
    private Long storeId;

    @InputCheckMeta(key="EC_ORDER.STATUS")
    private String status;
    @ExcelFileFieldMeta(exportIndex = 1, dataType = DataTypeEnum.STRING, headerName = "狀態")
    private String statusLabel;
    
    @InputCheckMeta(key="EC_ORDER.PAY_STATUS")
    private String payStatus;
    @ExcelFileFieldMeta(exportIndex = 2, dataType = DataTypeEnum.STRING, headerName = "付款狀態")
    private String payStatusLabel;
    @InputCheckMeta(key="EC_ORDER.SHIP_STATUS")
    private String shipStatus;
    @ExcelFileFieldMeta(exportIndex = 3, dataType = DataTypeEnum.STRING, headerName = "出貨狀態")
    private String shipStatusLabel;

    @ExcelFileFieldMeta(exportIndex = 4, dataType = DataTypeEnum.STRING, headerName = "編號")
    @InputCheckMeta(key="EC_ORDER.ORDER_NUMBER")
    private String orderNumber;

    @InputCheckMeta(key="EC_ORDER.PAYMENT_TYPE")
    private String paymentType;
    @ExcelFileFieldMeta(exportIndex = 5, dataType = DataTypeEnum.DATETIME, headerName = "訂單時間")
    private Date approvetime;
    private Date shippingTime;
    @ExcelFileFieldMeta(exportIndex = 6, dataType = DataTypeEnum.INT, headerName = "商品筆數")
    private Integer itemCount;// 商品筆數
    
    @ExcelFileFieldMeta(exportIndex = 7, dataType = DataTypeEnum.BIG_DECIMAL, headerName = "報價總金額")
    private BigDecimal total;
    @ExcelFileFieldMeta(exportIndex = 8, dataType = DataTypeEnum.BIG_DECIMAL, headerName = "原商品總價")
    private BigDecimal subTotal;
    @ExcelFileFieldMeta(exportIndex = 9, dataType = DataTypeEnum.BIG_DECIMAL, headerName = "運費")
    private BigDecimal shippingTotal;
    private BigDecimal discountTotal;
    //@InputCheckMeta(key="EC_ORDER.SHIPPING_DATE")
    //private String shippingDate;
    private Integer bonus;
    //
    private Long currencyId;
    private String currency;
    @ExcelFileFieldMeta(exportIndex = 10, dataType = DataTypeEnum.STRING, headerName = "幣別")
    private String curName;
    
    // customer
    private Long customerId;
    private Long memberId;
    private String loginAccount;
    @ExcelFileFieldMeta(exportIndex = 11, dataType = DataTypeEnum.STRING, headerName = "幣別")
    private String cname;
    private String ename;
    @ExcelFileFieldMeta(exportIndex = 12, dataType = DataTypeEnum.STRING, headerName = "聯絡電話")
    private String tel1;
    private String tel2;
    @ExcelFileFieldMeta(exportIndex = 13, dataType = DataTypeEnum.STRING, headerName = "聯絡E-Mail")
    private String email1;
    private String email2;

    @ExcelFileFieldMeta(exportIndex = 14, dataType = DataTypeEnum.STRING, headerName = "付款方式")
    private String payment;// 付款方式
    @ExcelFileFieldMeta(exportIndex = 15, dataType = DataTypeEnum.STRING, headerName = "出貨方式")
    private String shipping;// 運送方式
    private String payCode;// PayMethodEnum.code
    private String payType;// PayMethodEnum.type
    
    //@ExcelFileFieldMeta(exportIndex = 16, dataType = DataTypeEnum.STRING, headerName = "備註事項")
    //@InputCheckMeta(key="EC_ORDER.MESSAGE")
    private String message;

    private Date deliveryDate;// 交貨日期
    private BigDecimal oriTotal;
    private Boolean buyerCheck;

    // EC_ORDER_DETAIL (EC1.5 單筆DETAIL)
    private BigDecimal oriUnitPrice;
    private BigDecimal oriQuantity;
    private BigDecimal quantity;
    private String productName;
    private String productCode;
    private Long productId;

    private Date createtime;
    private Date modifytime;
    private Long creatorId;
    private Long modifierId;
    
    // EC_ORDER_SHIP_INFO
    @InputCheckMeta(key="EC_ORDER_SHIP_INFO.RECIPIENT")
    private String recipient;
    @InputCheckMeta(key="EC_ORDER_SHIP_INFO.ADDRESS")
    private String address;
    @InputCheckMeta(key="EC_ORDER_SHIP_INFO.PHONE")
    private String phone;
    @InputCheckMeta(key="EC_ORDER_SHIP_INFO.CAR_NO")
    private String carNo;
    @InputCheckMeta(key="EC_ORDER_SHIP_INFO.DRIVER")
    private String driver;
    @InputCheckMeta(key="EC_ORDER_SHIP_INFO.SHIPPING_CODE")
    private String shippingCode;

    private BigDecimal patrolLatitude;
    private BigDecimal patrolLongitude;
    
    private Integer customerRate;
    private String customerMessage;
    private Integer sellerRate;
    private String sellerMessage;    

    private Long deliveryId;
    private Long salesareaId;
    
    private String province; // 省
    private String city; // 市
    private String district; // 區
    private String town; // 鎮
    
    private String deliveryPlaceCode;
    private String deliveryPlaceName;
    private String salesareaCode;
    private String salesareaName;
    
    //private Long tccOrderId;
    
    private List<OrderDetailVO> items;
    private List<OrderProcessVO> records;
    private List<OrderMessageVO> messages;
    private List<OrderLogVO> logs;
    
    private List<OrderCarInfoVO> carList;
    private String cars;

    private boolean tranToEC10;
    private TccOrderVO tccOrder;// 拆單時雖有多筆，但除車號、數量不同，其他欄位應相同，只傳一份即可
    private List<OrderCarInfoVO> carListEC10;// 承上，傳 ID、車號、數量 欄位

    public void genStatusLabel(Locale locale){
        if( status!=null ){
            if( this.buyerCheck!=null && !this.buyerCheck ){
                // 待買方確認訂單量、價
                this.setStatusLabel(OrderStatusEnum.Waiting.getDisplayName(locale));
                return;
            }
            OrderStatusEnum enumPo = OrderStatusEnum.getFromCode(status);
            this.setStatusLabel(enumPo!=null?enumPo.getDisplayName(locale):"");
            if( this.getStatusLabel().isEmpty() ){
                RfqStatusEnum enumRfq = RfqStatusEnum.getFromCode(status);
                this.setStatusLabel(enumRfq!=null?enumRfq.getDisplayName(locale):"");
            }
        }else{
            this.setStatusLabel("");
        }
    }
    public void genPayStatusLabel(Locale locale){
        if( payStatus!=null ){
            PayStatusEnum enumPo = PayStatusEnum.getFromCode(payStatus);
            this.setPayStatusLabel(enumPo!=null?enumPo.getDisplayName(locale):"");
        }else{
            this.setPayStatusLabel("");
        }
    }
    public void genShipStatusLabel(Locale locale){
        if( shipStatus!=null ){
            ShipStatusEnum enumPo = ShipStatusEnum.getFromCode(shipStatus);
            this.setShipStatusLabel(enumPo!=null?enumPo.getDisplayName(locale):"");
        }else{
            this.setShipStatusLabel("");
        }
    }
    
    public OrderVO() {
    }

    public OrderVO(Long id) {
        this.id = id;
    }

    public OrderVO(Long id, Long storeId, String orderNumber, BigDecimal total, BigDecimal subTotal, BigDecimal shippingTotal, BigDecimal discountTotal, String status) {
        this.id = id;
        this.storeId = storeId;
        this.orderNumber = orderNumber;
        this.total = total;
        this.subTotal = subTotal;
        this.shippingTotal = shippingTotal;
        this.discountTotal = discountTotal;
        this.status = status;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public boolean isTranToEC10() {
        return tranToEC10;
    }

    public void setTranToEC10(boolean tranToEC10) {
        this.tranToEC10 = tranToEC10;
    }

    public TccOrderVO getTccOrder() {
        return tccOrder;
    }

    public void setTccOrder(TccOrderVO tccOrder) {
        this.tccOrder = tccOrder;
    }

    public List<OrderCarInfoVO> getCarListEC10() {
        return carListEC10;
    }

    public void setCarListEC10(List<OrderCarInfoVO> carListEC10) {
        this.carListEC10 = carListEC10;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<OrderCarInfoVO> getCarList() {
        return carList;
    }

    public void setCarList(List<OrderCarInfoVO> carList) {
        this.carList = carList;
    }

    public String getCars() {
        return cars;
    }

    public void setCars(String cars) {
        this.cars = cars;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Long getSalesareaId() {
        return salesareaId;
    }

    public void setSalesareaId(Long salesareaId) {
        this.salesareaId = salesareaId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getDeliveryPlaceCode() {
        return deliveryPlaceCode;
    }

    public void setDeliveryPlaceCode(String deliveryPlaceCode) {
        this.deliveryPlaceCode = deliveryPlaceCode;
    }

    public String getDeliveryPlaceName() {
        return deliveryPlaceName;
    }

    public void setDeliveryPlaceName(String deliveryPlaceName) {
        this.deliveryPlaceName = deliveryPlaceName;
    }

    public String getSalesareaCode() {
        return salesareaCode;
    }

    public void setSalesareaCode(String salesareaCode) {
        this.salesareaCode = salesareaCode;
    }

    public String getSalesareaName() {
        return salesareaName;
    }

    public void setSalesareaName(String salesareaName) {
        this.salesareaName = salesareaName;
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode;
    }

    public BigDecimal getPatrolLatitude() {
        return patrolLatitude;
    }

    public void setPatrolLatitude(BigDecimal patrolLatitude) {
        this.patrolLatitude = patrolLatitude;
    }

    public BigDecimal getPatrolLongitude() {
        return patrolLongitude;
    }

    public void setPatrolLongitude(BigDecimal patrolLongitude) {
        this.patrolLongitude = patrolLongitude;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Boolean getBuyerCheck() {
        return buyerCheck;
    }

    public void setBuyerCheck(Boolean buyerCheck) {
        this.buyerCheck = buyerCheck;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public BigDecimal getOriTotal() {
        return oriTotal;
    }

    public void setOriTotal(BigDecimal oriTotal) {
        this.oriTotal = oriTotal;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(Date shippingTime) {
        this.shippingTime = shippingTime;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getPayStatusLabel() {
        return payStatusLabel;
    }

    public void setPayStatusLabel(String payStatusLabel) {
        this.payStatusLabel = payStatusLabel;
    }

    public String getShipStatusLabel() {
        return shipStatusLabel;
    }

    public void setShipStatusLabel(String shipStatusLabel) {
        this.shipStatusLabel = shipStatusLabel;
    }

    public Integer getCustomerRate() {
        return customerRate;
    }

    public void setCustomerRate(Integer customerRate) {
        this.customerRate = customerRate;
    }

    public String getCustomerMessage() {
        return customerMessage;
    }

    public void setCustomerMessage(String customerMessage) {
        this.customerMessage = customerMessage;
    }

    public Integer getSellerRate() {
        return sellerRate;
    }

    public void setSellerRate(Integer sellerRate) {
        this.sellerRate = sellerRate;
    }

    public String getSellerMessage() {
        return sellerMessage;
    }

    public void setSellerMessage(String sellerMessage) {
        this.sellerMessage = sellerMessage;
    }

    public List<OrderMessageVO> getMessages() {
        return messages;
    }

    public void setMessages(List<OrderMessageVO> messages) {
        this.messages = messages;
    }

    public List<OrderLogVO> getLogs() {
        return logs;
    }

    public void setLogs(List<OrderLogVO> logs) {
        this.logs = logs;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Date getApprovetime() {
        return approvetime;
    }

    public void setApprovetime(Date approvetime) {
        this.approvetime = approvetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderDetailVO> getItems() {
        return items;
    }

    public void setItems(List<OrderDetailVO> items) {
        this.items = items;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public List<OrderProcessVO> getRecords() {
        return records;
    }

    public void setRecords(List<OrderProcessVO> records) {
        this.records = records;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getShippingTotal() {
        return shippingTotal;
    }

    public void setShippingTotal(BigDecimal shippingTotal) {
        this.shippingTotal = shippingTotal;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(BigDecimal discountTotal) {
        this.discountTotal = discountTotal;
    }

    public Integer getBonus() {
        return bonus;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
    @JsonSerialize(using = ISODateTimeSerializer.class)
    public Date getCreatetime() {
        return createtime;
    }
    @JsonDeserialize(using = ISODateTimeDeserializer.class)
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getOriUnitPrice() {
        return oriUnitPrice;
    }

    public void setOriUnitPrice(BigDecimal oriUnitPrice) {
        this.oriUnitPrice = oriUnitPrice;
    }

    public BigDecimal getOriQuantity() {
        return oriQuantity;
    }

    public void setOriQuantity(BigDecimal oriQuantity) {
        this.oriQuantity = oriQuantity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        if (!(object instanceof OrderVO)) {
            return false;
        }
        OrderVO other = (OrderVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.OrderVO[ id=" + id + " ]";
    }
    
}
