/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.CollectionUtils;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.et.enums.LanguageEnum;
import com.tcci.et.enums.PhotoGalleryEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@XmlRootElement
public class PhotoGalleryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    @Size(max = 10)
    private String code;
    @Size(max = 3)
    private String primaryType;
    private Long primaryId;
    @Size(max = 120)
    private String cname;
    @Size(max = 120)
    private String ename;
    @Size(max = 300)
    private String subject;
    @Size(max = 1500)
    private String description;
    private Long parent;
    private String lang = LanguageEnum.TRANDITIONAL_CHINESE.getShortCode();// 預設繁體中文
    @Size(max = 3)
    private String status;
    
    private Date dataDate;

    private Integer width;
    private Integer height;
    private Boolean compressed;
    
    private TcUser creator;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    private TcUser modifier;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;

    private String lastUserName;
    private Date lastTime;
    
    private int subCounts;
    private int imgCounts;
    private int folderCounts;
    
    private AttachmentVO attachmentVO;
    private String url;
    private boolean canEdit;
    private boolean canAddSub;
    private boolean canUpload;
                   
    // for TC File Vault Info
    private Long appid;
    private Long fvitemId;
    private Long domain;
    private String fileName;
    private String oriFileName;
    private String contentType;
    private Integer filesize;
    private String domainName;
    private String location;
    
    private Boolean identifyImg;// 封面圖
          
    // for 文章插圖
    private String type;
    private String title;
    private String summary;
    
    private String parentName;
    private String parentIdStr;
    
    /**
     * 傳回直屬上層列表
     * @param theLevel
     * @param allList
     * @param asc
     * @return 
     */
    public List<PhotoGalleryVO> genParentList(Long theLevel, List<PhotoGalleryVO> allList, boolean asc){
        if( theLevel==null || theLevel<=0 || allList==null || allList.isEmpty() ){
            return null;
        }
        
        List<PhotoGalleryVO> retList = new ArrayList<PhotoGalleryVO>();
        Long nowLevel = theLevel;
        boolean end = false;
        while( !end ){
            for(PhotoGalleryVO vo : allList){
                if( nowLevel.equals(vo.getId()) ){
                    retList.add(vo);
                    nowLevel = vo.getParent();
                    break;
                }
            }
            end = (nowLevel<=0);
        }
        retList = (retList.size()<=1 || asc)?retList:(List<PhotoGalleryVO>)CollectionUtils.reverse(retList);
        return retList;
    }
    
    public TcUser getLastUpdateUser(){
        return (modifier!=null)?modifier:creator;
    }
    public Date getLastUpdateTime(){
        return (modifytimestamp!=null)?modifytimestamp:createtimestamp;
    }

    public LanguageEnum getLangEnum(){
        return LanguageEnum.getFromCode(lang);
    }
    
    public String getLangName(){
        LanguageEnum enum1 = getLangEnum();
        return (enum1!=null)?enum1.getName():"";
    }

    public PhotoGalleryEnum getPrimaryTypeEnum(){
        return PhotoGalleryEnum.getFromCode(primaryType);
    }
    
    public String getPrimaryTypeName(){
        PhotoGalleryEnum enum1 = getPrimaryTypeEnum();
        return (enum1!=null)?enum1.getName():"";
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Boolean getIdentifyImg() {
        return identifyImg;
    }

    public void setIdentifyImg(Boolean identifyImg) {
        this.identifyImg = identifyImg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDataDate() {
        return dataDate;
    }

    public void setDataDate(Date dataDate) {
        this.dataDate = dataDate;
    }

    public String getParentIdStr() {
        return parentIdStr;
    }

    public void setParentIdStr(String parentIdStr) {
        this.parentIdStr = parentIdStr;
        if( parentIdStr!=null ){
            this.parent = Long.parseLong(parentIdStr);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getCompressed() {
        return compressed;
    }

    public void setCompressed(Boolean compressed) {
        this.compressed = compressed;
    }

    public PhotoGalleryVO() {
    }

    public PhotoGalleryVO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public AttachmentVO getAttachmentVO() {
        return attachmentVO;
    }

    public void setAttachmentVO(AttachmentVO attachmentVO) {
        this.attachmentVO = attachmentVO;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(Long primaryId) {
        this.primaryId = primaryId;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
        if( parent!=null ){
            this.parentIdStr = Long.toString(parent);
        }
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isCanAddSub() {
        return canAddSub;
    }

    public void setCanAddSub(boolean canAddSub) {
        this.canAddSub = canAddSub;
    }

    public boolean isCanUpload() {
        return canUpload;
    }

    public void setCanUpload(boolean canUpload) {
        this.canUpload = canUpload;
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

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
    }

    public String getLastUserName() {
        return lastUserName;
    }

    public void setLastUserName(String lastUserName) {
        this.lastUserName = lastUserName;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public int getSubCounts() {
        return subCounts;
    }

    public void setSubCounts(int subCounts) {
        this.subCounts = subCounts;
    }

    public int getImgCounts() {
        return imgCounts;
    }

    public void setImgCounts(int imgCounts) {
        this.imgCounts = imgCounts;
    }

    public int getFolderCounts() {
        return folderCounts;
    }

    public void setFolderCounts(int folderCounts) {
        this.folderCounts = folderCounts;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getAppid() {
        return appid;
    }

    public void setAppid(Long appid) {
        this.appid = appid;
    }

    public Long getFvitemId() {
        return fvitemId;
    }

    public void setFvitemId(Long fvitemId) {
        this.fvitemId = fvitemId;
    }

    public Long getDomain() {
        return domain;
    }

    public void setDomain(Long domain) {
        this.domain = domain;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriFileName() {
        return oriFileName;
    }

    public void setOriFileName(String oriFileName) {
        this.oriFileName = oriFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getFilesize() {
        return filesize;
    }

    public void setFilesize(Integer filesize) {
        this.filesize = filesize;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
        if (!(object instanceof PhotoGalleryVO)) {
            return false;
        }
        PhotoGalleryVO other = (PhotoGalleryVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.PhotoGalleryVO[ id=" + id + " ]";
    }
    
}
