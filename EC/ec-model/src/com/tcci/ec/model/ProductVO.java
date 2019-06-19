/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.tcci.cm.annotation.ExcelFileFieldMeta;
import com.tcci.cm.annotation.ExcelFileMeta;
import com.tcci.cm.annotation.InputCheckMeta;
import com.tcci.cm.annotation.enums.DataTypeEnum;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.ProductStatusEnum;
import com.tcci.ec.model.rs.BaseResponseVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@XmlRootElement
@ExcelFileMeta(headerRow = 0, rowNumColumnName = "rowNum")
public class ProductVO extends BaseResponseVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelFileFieldMeta(exportIndex = 0, dataType = DataTypeEnum.LONG, headerName = "ID")
    private Long id;
    private Long storeId;
    @ExcelFileFieldMeta(exportIndex = 1, dataType = DataTypeEnum.STRING, headerName = "商店名稱")
    private String storeName;
    @ExcelFileFieldMeta(exportIndex = 2, dataType = DataTypeEnum.STRING, headerName = "商品名稱")
    @InputCheckMeta(key="EC_PRODUCT.CNAME")
    private String cname;
    @InputCheckMeta(key="EC_PRODUCT.ENAME")
    private String ename;

    @ExcelFileFieldMeta(exportIndex = 3, dataType = DataTypeEnum.STRING, headerName = "商品代碼")
    @InputCheckMeta(key="EC_PRODUCT.CODE")
    private String code;
    @InputCheckMeta(key="EC_PRODUCT.STATUS")
    private String status;
    @ExcelFileFieldMeta(exportIndex = 4, dataType = DataTypeEnum.STRING, headerName = "狀態")
    private String statusName;

    @ExcelFileFieldMeta(exportIndex = 5, dataType = DataTypeEnum.STRING, headerName = "供應商")
    private String vendorName;
    private String brandName;
    
    @ExcelFileFieldMeta(exportIndex = 6, dataType = DataTypeEnum.STRING, headerName = "類別")
    private String typeName;
    private Long typeL2;
    private String typeL2Name;
    private Long typeL1;
    private String typeL1Name;

    private Long typeId;
    private Long vendorId;
    private Long brandId;
    private Long serialId;
    
//    @ExcelFileFieldMeta(exportIndex = 7, dataType = DataTypeEnum.BIG_DECIMAL, headerName = "訂價")
    private BigDecimal compareAtPrice;
    @ExcelFileFieldMeta(exportIndex = 7, dataType = DataTypeEnum.BIG_DECIMAL, headerName = "售價")
    private BigDecimal price;
    
    private Long priceUnit;
    private String priceUnitName;
    private BigDecimal priceAmt;
    private Long itemUnit;
    private Long itemUnitName;
    private BigDecimal itemAmt;
    @InputCheckMeta(key="EC_PRODUCT.SKU")
    private String sku;
    @InputCheckMeta(key="EC_PRODUCT.BARCODE")
    private String barcode;
    
    private Boolean hasVariations;
    private Boolean hasImages;
 
    private BigDecimal minAmt;
    private BigDecimal maxAmt;
    private Boolean intAmt;
    private Long currencyId;
    private String currency;
    @ExcelFileFieldMeta(exportIndex = 8, dataType = DataTypeEnum.STRING, headerName = "幣別")
    private String curName;
    
    private BigDecimal stockSettle;
    private Date stockSettleDate;
    @ExcelFileFieldMeta(exportIndex = 9, dataType = DataTypeEnum.BIG_DECIMAL, headerName = "庫存")
    private BigDecimal stock;

    @ExcelFileFieldMeta(exportIndex = 10, dataType = DataTypeEnum.INT, headerName = "被追蹤數")
    private int favCount;// 追蹤
    private Boolean disabled;
    @ExcelFileFieldMeta(exportIndex = 11, dataType = DataTypeEnum.DATETIME, headerName = "上架時間")
    private Date publishTime;
    @InputCheckMeta(key="EC_PRODUCT.PUBLISH_SCOPE")
    private String publishScope;
    private Long creatorId;
    @ExcelFileFieldMeta(exportIndex = 12, dataType = DataTypeEnum.DATETIME, headerName = "建立時間")
    private Date createtime;
    private Long modifierId;
    @ExcelFileFieldMeta(exportIndex = 13, dataType = DataTypeEnum.DATETIME, headerName = "最近修改時間")
    private Date modifytime;
    
    private BigDecimal weight;
    private Long weightUnit;
    private String weightUnitName;
    @InputCheckMeta(key="EC_PRODUCT.VOLUME")
    private String volume;
    
    private Long applicant;
    private Date applyTime;
    private String applicantAccount;
    
    private List<LongOptionVO> colors;
    private List<LongOptionVO> sizes;
    private List<PrdVariantVO> variants;
    
    private List<String> introductions;
    
    private List<LongOptionVO> events;
    private List<LongOptionVO> shippings;
    private List<LongOptionVO> payments;
    
    private List<PrdDetailVO> details;
    private List<PrdAttrVO> typeAttrs;
    private List<PrdAttrValVO> attrs;
    
    private Long coverPicId;
    private String picName;
    private String picDescription;
    private String filename;
    private String savename;
    private String savedir;
    private String contentType;
    private Integer fileSize;
    private String url;
    private ImageVO image;

    private List<ImageVO> images;
    
    private String statusOri;
    
    private List<StoreAreaVO> areas;//額外顯示用
    
    /**
     *  genUrl("http://localhost:8080/ec-seller-serv", "/ImageServlet", "D", "O")
     * @param urlRoot
     * @param path
     * @param imgType
     * @return 
     */
    public String genUrl(String urlRoot, String path, String imgType){
        if( savename==null ){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(urlRoot).append(path);

        StringBuilder qstr = new StringBuilder();
        qstr.append("?publicImg=Y&storeId=").append(storeId)
                .append("&contentType=").append(contentType)
                .append("&mappingKey=").append(savename)
                .append("&imgType=").append(imgType)
                .append("&imgSrc=").append(FileEnum.PRD_PIC.getCode());
        
        sb.append(qstr);
        
        return sb.toString();
    }
    
    public void genStatusName(Locale locale){
        ProductStatusEnum enum1 = ProductStatusEnum.getFromCode(this.status);
        statusName = enum1!=null?enum1.getDisplayName(locale):"";
    }
    
    public void setColorsByVoList(List<PrdVarOptionVO> list){
        colors = null;
        if( list!=null ){
            colors = new ArrayList<LongOptionVO>();
            for(PrdVarOptionVO vo : list){
                LongOptionVO op = new LongOptionVO(vo.getId(), vo.getCname());
                colors.add(op);
            }
        }
    }
    
    public void setSizesByVoList(List<PrdVarOptionVO> list){
        sizes = null;
        if( list!=null ){
            sizes = new ArrayList<LongOptionVO>();
            for(PrdVarOptionVO vo : list){
                LongOptionVO op = new LongOptionVO(vo.getId(), vo.getCname());
                sizes.add(op);
            }
        }
    }
    
    public void setShippingsByVoList(List<PrdShippingVO> list){
        shippings = null;
        if( list!=null ){
            shippings = new ArrayList<LongOptionVO>();
            for(PrdShippingVO vo : list){
                LongOptionVO op = new LongOptionVO(vo.getShipId(), vo.getTitle());
                shippings.add(op);
            }
        }
    }
    
    public void setPaymentsByVoList(List<PrdPaymentVO> list){
        payments = null;
        if( list!=null ){
            payments = new ArrayList<LongOptionVO>();
            for(PrdPaymentVO vo : list){
                LongOptionVO op = new LongOptionVO(vo.getPayId(), vo.getTitle());
                payments.add(op);
            }
        }
    }

    public void setIntroductionsByVO(List<PrdIntroVO> introList) {
        if( introList!=null ){
            introductions = new ArrayList<String>();
            for(PrdIntroVO vo : introList){
                introductions.add(vo.getText());
            }
        }else{
            introductions = null;
        }
    }
    
    public ProductVO(){}

    public ProductVO(Long id){
        this.id = id;
    }

    public Long getApplicant() {
        return applicant;
    }

    public void setApplicant(Long applicant) {
        this.applicant = applicant;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApplicantAccount() {
        return applicantAccount;
    }

    public void setApplicantAccount(String applicantAccount) {
        this.applicantAccount = applicantAccount;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public BigDecimal getStockSettle() {
        return stockSettle;
    }

    public void setStockSettle(BigDecimal stockSettle) {
        this.stockSettle = stockSettle;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public Date getStockSettleDate() {
        return stockSettleDate;
    }

    public void setStockSettleDate(Date stockSettleDate) {
        this.stockSettleDate = stockSettleDate;
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

    public String getPicDescription() {
        return picDescription;
    }

    public void setPicDescription(String picDescription) {
        this.picDescription = picDescription;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSavename() {
        return savename;
    }

    public void setSavename(String savename) {
        this.savename = savename;
    }

    public String getSavedir() {
        return savedir;
    }

    public void setSavedir(String savedir) {
        this.savedir = savedir;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Long getCoverPicId() {
        return coverPicId;
    }

    public void setCoverPicId(Long coverPicId) {
        this.coverPicId = coverPicId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFavCount() {
        return favCount;
    }

    public void setFavCount(int favCount) {
        this.favCount = favCount;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getPriceUnitName() {
        return priceUnitName;
    }

    public void setPriceUnitName(String priceUnitName) {
        this.priceUnitName = priceUnitName;
    }

    public Long getItemUnitName() {
        return itemUnitName;
    }

    public void setItemUnitName(Long itemUnitName) {
        this.itemUnitName = itemUnitName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getStatusOri() {
        return statusOri;
    }

    public void setStatusOri(String statusOri) {
        this.statusOri = statusOri;
    }

    public BigDecimal getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(BigDecimal minAmt) {
        this.minAmt = minAmt;
    }

    public BigDecimal getMaxAmt() {
        return maxAmt;
    }

    public void setMaxAmt(BigDecimal maxAmt) {
        this.maxAmt = maxAmt;
    }

    public Boolean getIntAmt() {
        return intAmt;
    }

    public void setIntAmt(Boolean intAmt) {
        this.intAmt = intAmt;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<PrdAttrVO> getTypeAttrs() {
        return typeAttrs;
    }

    public void setTypeAttrs(List<PrdAttrVO> typeAttrs) {
        this.typeAttrs = typeAttrs;
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

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
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
        this.weightUnit = weightUnit;
    }

    public String getWeightUnitName() {
        return weightUnitName;
    }

    public void setWeightUnitName(String weightUnitName) {
        this.weightUnitName = weightUnitName;
    }

    public BigDecimal getCompareAtPrice() {
        return compareAtPrice;
    }

    public void setCompareAtPrice(BigDecimal compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    public List<LongOptionVO> getColors() {
        return colors;
    }

    public void setColors(List<LongOptionVO> colors) {
        this.colors = colors;
    }

    public List<LongOptionVO> getSizes() {
        return sizes;
    }

    public void setSizes(List<LongOptionVO> sizes) {
        this.sizes = sizes;
    }

    public List<String> getIntroductions() {
        return introductions;
    }

    public void setIntroductions(List<String> introductions) {
        this.introductions = introductions;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getTypeL2() {
        return typeL2;
    }

    public void setTypeL2(Long typeL2) {
        this.typeL2 = typeL2;
    }

    public String getTypeL2Name() {
        return typeL2Name;
    }

    public void setTypeL2Name(String typeL2Name) {
        this.typeL2Name = typeL2Name;
    }

    public Long getTypeL1() {
        return typeL1;
    }

    public void setTypeL1(Long typeL1) {
        this.typeL1 = typeL1;
    }

    public String getTypeL1Name() {
        return typeL1Name;
    }

    public void setTypeL1Name(String typeL1Name) {
        this.typeL1Name = typeL1Name;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public List<PrdVariantVO> getVariants() {
        return variants;
    }

    public void setVariants(List<PrdVariantVO> variants) {
        this.variants = variants;
    }

    public List<LongOptionVO> getEvents() {
        return events;
    }

    public void setEvents(List<LongOptionVO> events) {
        this.events = events;
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

    public List<PrdDetailVO> getDetails() {
        return details;
    }

    public void setDetails(List<PrdDetailVO> details) {
        this.details = details;
    }

    public List<PrdAttrValVO> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<PrdAttrValVO> attrs) {
        this.attrs = attrs;
    }

    public List<ImageVO> getImages() {
        return images;
    }

    public void setImages(List<ImageVO> images) {
        this.images = images;
    }

    public ImageVO getImage() {
        return image;
    }

    public void setImage(ImageVO image) {
        this.image = image;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getSerialId() {
        return serialId;
    }

    public void setSerialId(Long serialId) {
        this.serialId = serialId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Long priceUnit) {
        this.priceUnit = priceUnit;
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
        this.itemUnit = itemUnit;
    }

    public BigDecimal getItemAmt() {
        return itemAmt;
    }

    public void setItemAmt(BigDecimal itemAmt) {
        this.itemAmt = itemAmt;
    }

    public Boolean getHasVariations() {
        return hasVariations;
    }

    public void setHasVariations(Boolean hasVariations) {
        this.hasVariations = hasVariations;
    }

    public Boolean getHasImages() {
        return hasImages;
    }

    public void setHasImages(Boolean hasImages) {
        this.hasImages = hasImages;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublishScope() {
        return publishScope;
    }

    public void setPublishScope(String publishScope) {
        this.publishScope = publishScope;
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

    public List<StoreAreaVO> getAreas() {
        return areas;
    }

    public void setAreas(List<StoreAreaVO> areas) {
        this.areas = areas;
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
        if (!(object instanceof ProductVO)) {
            return false;
        }
        ProductVO other = (ProductVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.ProductVO[ id=" + id + " ]";
    }
    
}
