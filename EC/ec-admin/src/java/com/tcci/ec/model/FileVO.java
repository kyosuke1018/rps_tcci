/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class FileVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String primaryType;
    private Long primaryId;
    private Long storeId;
    private String name;
    private String description;
    private String filename;
    private String savename;
    private String savedir;
    private String contentType;
    private Integer fileSize;
    
    private String saveFileNameFull;
    
    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;

    private String url;
    
    /**
     *  genUrl("http://localhost:8080/ec-seller-serv", "/ImageServlet", "D", "O")
     * @param urlRoot
     * @param path
     * @param imgSrc
     * @param imgType
     * @return 
     */
    public String genUrl(String urlRoot, String path, String imgSrc, String imgType){
        if( savename==null ){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(urlRoot).append(path);

        StringBuilder qstr = new StringBuilder();
        qstr.append("?publicImg=Y&contentType=").append(contentType)
                .append("&mappingKey=").append(savename)
                .append("&imgSrc=").append(imgSrc)
                .append("&imgType=").append(imgType);
        if( storeId!=null ){
            qstr.append("&storeId=").append(storeId);
        }
        
        sb.append(qstr);
        
        return sb.toString();
    }
    
    public FileVO() {
    }

    public FileVO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getSaveFileNameFull() {
        return saveFileNameFull;
    }

    public void setSaveFileNameFull(String saveFileNameFull) {
        this.saveFileNameFull = saveFileNameFull;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FileVO)) {
            return false;
        }
        FileVO other = (FileVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.FileVO[ id=" + id + " ]";
    }
    
}
