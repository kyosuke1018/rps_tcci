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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@XmlRootElement
public class PrdDetailVO extends BaseResponseVO implements Serializable {
    private Long id;
    private Long storeId;
    private Long prdId;
    private Integer sortnum;
    private String contentType;
    private String contentTxt;
    private Long contentImg;
    private Long styleId;
    
    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;

    private String title;
    private String savedir;
    private String savename;
    private String filename; 
    private String fileContentType;
    private String url;
    
    public PrdDetailVO() {
    }

    public PrdDetailVO(Long id) {
        this.id = id;
    }

    /**
     *  genUrl("http://localhost:8080/ec-seller-serv", "/ImageServlet", "O")
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
        qstr.append("?publicImg=Y&contentType=").append(fileContentType)
                .append("&mappingKey=").append(savename)
                .append("&imgType=").append(imgType)
                .append("&imgSrc=").append(FileEnum.PRD_DETAIL.getCode());
        if( storeId!=null ){
            qstr.append("&storeId=").append(storeId);
        }
        
        sb.append(qstr);
        
        return sb.toString();
    }
    
    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentTxt() {
        return contentTxt;
    }

    public void setContentTxt(String contentTxt) {
        this.contentTxt = contentTxt;
    }

    public Long getContentImg() {
        return contentImg;
    }

    public void setContentImg(Long contentImg) {
        this.contentImg = contentImg;
    }

    public Long getStyleId() {
        return styleId;
    }

    public void setStyleId(Long styleId) {
        this.styleId = styleId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrdDetailVO)) {
            return false;
        }
        PrdDetailVO other = (PrdDetailVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.PrdDetailVO[ id=" + id + " ]";
    }
    
}
