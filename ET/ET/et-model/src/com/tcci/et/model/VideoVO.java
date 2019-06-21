/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.CollectionUtils;
import com.tcci.et.enums.LanguageEnum;
import com.tcci.et.enums.VideoLibraryEnum;
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
public class VideoVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    @Size(max = 3)
    private String primaryType;
    private Long primaryId;
    @Size(max = 120)
    private String cname;
    @Size(max = 120)
    private String ename;
    @Size(max = 300)
    private String title;
    @Size(max = 1500)
    private String description;
    @Size(max = 300)
    private String etitle;
    @Size(max = 1500)
    private String edescription;
    private Long parent;
    private String lang = LanguageEnum.TRANDITIONAL_CHINESE.getShortCode();// 預設繁體中文
    @Size(max = 3)
    private String status;
    
    private String channel;
    private Date releaseDate;
     
    private Integer width;
    private Integer height;
    
    private TcUser creator;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    private TcUser modifier;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;

    private String lastUserName;
    private Date lastTime;

    private String openMethod;
    private Long linkId;
    private String url;
    private String embedUrl;// from select sql
    
    private boolean canEdit;
    
    // for 植物影片
    private String accessionNo;
    
    // for 文章插入影片
    private String type;
    private String docTitle;
    
    private int subCounts;
    
    private String parentName;
    private String parentIdStr;
    
    /**
     * 傳回直屬上層列表
     * @param theLevel
     * @param allList
     * @param asc
     * @return 
     */
    public List<VideoVO> genParentList(Long theLevel, List<VideoVO> allList, boolean asc){
        if( theLevel==null || theLevel<=0 || allList==null || allList.isEmpty() ){
            return null;
        }
        
        List<VideoVO> retList = new ArrayList<VideoVO>();
        Long nowLevel = theLevel;
        boolean end = false;
        while( !end ){
            for(VideoVO vo : allList){
                if( nowLevel.equals(vo.getId()) ){
                    retList.add(vo);
                    nowLevel = vo.getParent();
                    break;
                }
            }
            end = (nowLevel<=0);
        }
        retList = (retList.size()<=1 || asc)?retList:(List<VideoVO>)CollectionUtils.reverse(retList);
        return retList;
    }
    
    public TcUser getLastUpdateUser(){
        return (modifier!=null)?modifier:creator;
    }
    public Date getLastUpdateTime(){
        return (modifytimestamp!=null)?modifytimestamp:createtimestamp;
    }

    public VideoLibraryEnum getTypeEnum(){
        return VideoLibraryEnum.getFromCode(primaryType);
    }
    
    public String getTypeName(){
        VideoLibraryEnum enum1 = getTypeEnum();
        return (enum1!=null)?enum1.getName():"";
    }

    public LanguageEnum getLangEnum(){
        return LanguageEnum.getFromCode(lang);
    }
    
    public String getLangName(){
        LanguageEnum enum1 = getLangEnum();
        return (enum1!=null)?enum1.getName():"";
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

    public VideoVO() {
    }

    public VideoVO(Long id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getSubCounts() {
        return subCounts;
    }

    public void setSubCounts(int subCounts) {
        this.subCounts = subCounts;
    }

    public String getAccessionNo() {
        return accessionNo;
    }

    public void setAccessionNo(String accessionNo) {
        this.accessionNo = accessionNo;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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

    public Long getId() {
        return id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
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

    public String getOpenMethod() {
        return openMethod;
    }

    public void setOpenMethod(String openMethod) {
        this.openMethod = openMethod;
    }

    public String getEmbedUrl() {
        return embedUrl;
    }

    public void setEmbedUrl(String embedUrl) {
        this.embedUrl = embedUrl;
    }

    public String getEtitle() {
        return etitle;
    }

    public void setEtitle(String etitle) {
        this.etitle = etitle;
    }

    public String getEdescription() {
        return edescription;
    }

    public void setEdescription(String edescription) {
        this.edescription = edescription;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        if (!(object instanceof VideoVO)) {
            return false;
        }
        VideoVO other = (VideoVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.VideoVO[ id=" + id + " ]";
    }
    
}
