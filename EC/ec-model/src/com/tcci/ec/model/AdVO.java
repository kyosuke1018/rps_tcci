/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.model.rs.BaseResponseVO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class AdVO extends BaseResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;
 
    private Long id;
    private Long storeId;
    private Long prdId;
    private String message;
    private Date starttime;
    private Date endtime;
    private Integer sortnum = 0;
    
    private Long approveUser;
    private Date approveTime;
    
    private String approveUserName;
    private String creatorName;
    private String modifierName;
    
    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;

    private String storeName;
    private String prdName;
    
    private Long imgId;
    private String title;
    private String savedir;
    private String savename;
    private String filename; 
    private String fileContentType;
    private String url;

    private boolean clientModified;
    
    private List<LongOptionVO> prdList;// 關聯商店商品 for UI select options
    /**
     *  genUrl("http://localhost:8080/ec-seller-serv", "/ImageServlet", "O")
     * @param urlRoot
     * @param path
     * @param imgType
     * @return 
     */
    public String genUrl(String urlRoot, String path, String imgType){
        StringBuilder sb = new StringBuilder();
        sb.append(urlRoot).append(path);

        StringBuilder qstr = new StringBuilder();
        qstr.append("?publicImg=Y&storeId=").append(storeId)
                .append("&contentType=").append(fileContentType)
                .append("&mappingKey=").append(savename)
                .append("&imgType=").append(imgType)
                .append("&imgSrc=").append(FileEnum.AD.getCode());
        
        sb.append(qstr);
        
        return sb.toString();
    }
    
    public AdVO() {
    }

    public AdVO(Long id) {
        this.id = id;
    }

    public List<LongOptionVO> getPrdList() {
        return prdList;
    }

    public void setPrdList(List<LongOptionVO> prdList) {
        this.prdList = prdList;
    }

    public String getApproveUserName() {
        return approveUserName;
    }

    public void setApproveUserName(String approveUserName) {
        this.approveUserName = approveUserName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public boolean isClientModified() {
        return clientModified;
    }

    public void setClientModified(boolean clientModified) {
        this.clientModified = clientModified;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPrdName() {
        return prdName;
    }

    public void setPrdName(String prdName) {
        this.prdName = prdName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSavedir() {
        return savedir;
    }

    public void setSavedir(String savedir) {
        this.savedir = savedir;
    }

    public String getSavename() {
        return savename;
    }

    public void setSavename(String savename) {
        this.savename = savename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getImgId() {
        return imgId;
    }

    public void setImgId(Long imgId) {
        this.imgId = imgId;
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
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

    public Long getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(Long approveUser) {
        this.approveUser = approveUser;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        if (!(object instanceof AdVO)) {
            return false;
        }
        AdVO other = (AdVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.AdVO[ id=" + id + " ]";
    }
    
}
