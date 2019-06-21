/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.et.enums.ContentStatusEnum;
import com.tcci.et.enums.DataTypeEnum;
import com.tcci.et.enums.LanguageEnum;
import com.tcci.et.enums.OpenMethodEnum;
import com.tcci.et.enums.PublicationEnum;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@XmlRootElement
public class PublicationVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String type;
    private Long parent;
    private String dataType;
    private Date dataDate;
    private String code;
    private String title;
    private String summary;
    private String status;
    private Boolean news;
    private String lang = LanguageEnum.TRANDITIONAL_CHINESE.getShortCode();// 預設繁體中文

    private TcUser creator;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    private TcUser modifier;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;

    private Long linkId;
    private String url;
    private String openMethod = OpenMethodEnum.NEW.getCode();// 新開視窗

    private Long htmlId;
    private String contents;
    
    private List<AttachmentVO> docs;
    private List<AttachmentVO> removedDocs;
        
    private String folderTitle;

    private String lastUserName;
    private Date lastTime;
    
    private boolean temp;

    // 封面圖示
    private String coverImg;
    private AttachmentVO coverImgVO;
    private Long coverDomain;
    private String coverFilename;
    private String coverContenttype;
    private String coverUrl;
    
    // for 百種興盛專屬欄位
    /*
    private Long hid;// KbHundreds.ID
    private Long cdocId;
    private Long edocId;
    private Long colId;
    private Long familyId;
    private String ename;
    private String cname;
    private String protect;
    private String locateDes;
    private String sponsor;   
    private String elocateDes;
    private String esponsor;
    private String memo;
    private String cprotect;
    private String eprotect;
    
    private String familyCname;
    private String familyEname;
    private String colTypeCname;
    private String colTypeEname;
    private Integer hasRelsFlag;
    private Boolean hasRels;// 有無關聯資訊
    */
    
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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<AttachmentVO> getRemovedDocs() {
        return removedDocs;
    }

    public void setRemovedDocs(List<AttachmentVO> removedDocs) {
        this.removedDocs = removedDocs;
    }

    public PublicationEnum getTypeEnum(){
        return PublicationEnum.getFromCode(type);
    }
    
    public String getTypeName(){
        PublicationEnum enum1 = getTypeEnum();
        return (enum1!=null)?enum1.getName():"";
    }

    public DataTypeEnum getDataTypeEnum(){
        return DataTypeEnum.getFromCode(dataType);
    }
    
    public String getDataTypeName(){
        DataTypeEnum enum1 = getDataTypeEnum();
        return (enum1!=null)?enum1.getName():"";
    }

    public ContentStatusEnum getStatusEnum(){
        return ContentStatusEnum.getFromCode(status);
    }
    
    public String getStatusName(){
        ContentStatusEnum enum1 = getStatusEnum();
        return (enum1!=null)?enum1.getName():"";
    }

    public PublicationVO() {
    }

    public PublicationVO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataDate() {
        return dataDate;
    }

    public void setDataDate(Date dataDate) {
        this.dataDate = dataDate;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public AttachmentVO getCoverImgVO() {
        return coverImgVO;
    }

    public void setCoverImgVO(AttachmentVO coverImgVO) {
        this.coverImgVO = coverImgVO;
    }

    public boolean isTemp() {
        return temp;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }

    public Boolean getNews() {
        return news;
    }

    public void setNews(Boolean news) {
        this.news = news;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public List<AttachmentVO> getDocs() {
        return docs;
    }

    public void setDocs(List<AttachmentVO> docs) {
        this.docs = docs;
    }

    public String getOpenMethod() {
        return openMethod;
    }

    public void setOpenMethod(String openMethod) {
        this.openMethod = openMethod;
    }

    public String getFolderTitle() {
        return folderTitle;
    }

    public void setFolderTitle(String folderTitle) {
        this.folderTitle = folderTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    public Long getHtmlId() {
        return htmlId;
    }

    public void setHtmlId(Long htmlId) {
        this.htmlId = htmlId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCoverFilename() {
        return coverFilename;
    }

    public void setCoverFilename(String coverFilename) {
        this.coverFilename = coverFilename;
    }

    public Long getCoverDomain() {
        return coverDomain;
    }

    public void setCoverDomain(Long coverDomain) {
        this.coverDomain = coverDomain;
    }

    public String getCoverContenttype() {
        return coverContenttype;
    }

    public void setCoverContenttype(String coverContenttype) {
        this.coverContenttype = coverContenttype;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
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
        if (!(object instanceof PublicationVO)) {
            return false;
        }
        PublicationVO other = (PublicationVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.PublicationVO[ id=" + id + " ]";
    }
    
}
