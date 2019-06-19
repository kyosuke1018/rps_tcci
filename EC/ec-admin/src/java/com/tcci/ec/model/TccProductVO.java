/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.model.rs.ImageVO;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author penpl
 */
@XmlRootElement
public class TccProductVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String name;
    private String description;
    private Boolean active;
    
    private Long storeId;
    private Long productId;// EC_PRODUCT.ID
    private BigDecimal price;// EC_PRODUCT.PRICE
    
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
    
    private Long typeId;
    private Long vendorId;
    private Long brandId;
    private Long priceUnit;
    
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
        qstr.append("?publicImg=Y&contentType=").append(contentType)
                .append("&mappingKey=").append(savename)
                .append("&imgType=").append(imgType)
                .append("&imgSrc=").append(FileEnum.TCC_PRD_PIC.getCode());
        if( storeId!=null ){
            qstr.append("&storeId=").append(storeId);
        }
        
        sb.append(qstr);
        
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Long priceUnit) {
        this.priceUnit = priceUnit;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getCoverPicId() {
        return coverPicId;
    }

    public void setCoverPicId(Long coverPicId) {
        this.coverPicId = coverPicId;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageVO getImage() {
        return image;
    }

    public void setImage(ImageVO image) {
        this.image = image;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
