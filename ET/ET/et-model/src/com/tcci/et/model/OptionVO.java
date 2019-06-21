/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model;

import com.tcci.et.enums.OptionEnum;
import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class OptionVO implements Serializable {
    private static final long serialVersionUID = 1L;  
    private Long id;
    private String type;
    private String cname;
    private String ename;
    private String code;
    private Long parent;
    private String memo;
    private Integer levelnum;
    private Integer sortnum;
    private Boolean disabled;
    private Boolean readonly;
    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;

    private TcUser creator;
    private TcUser modifier;
    
    protected String lastUserName;
    protected Date lastTime;

    // for JS
    private boolean clientModified;
    
    public OptionEnum getTypeEnum(){
        return OptionEnum.getFromCode(type);
    }
    
    public String getTypeName(){
        OptionEnum enum1 = getTypeEnum();
        return (enum1!=null)?enum1.getName():"";
    }
    
    public String getLabel(){
        String label = (cname==null || cname.isEmpty())? "":cname;
        label = label.isEmpty()? ename:(ename==null || ename.isEmpty())? label:label+"("+ename+")";
        return label;
    }
    
    public TcUser getLastUpdateUser(){
        return (modifier!=null)?modifier:creator;
    }
    public Date getLastUpdateTime(){
        return (modifytime!=null)?modifytime:createtime;
    }

    public OptionVO() {
    }

    public OptionVO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isClientModified() {
        return clientModified;
    }

    public void setClientModified(boolean clientModified) {
        this.clientModified = clientModified;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getLevelnum() {
        return levelnum;
    }

    public void setLevelnum(Integer levelnum) {
        this.levelnum = levelnum;
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
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
        if (!(object instanceof OptionVO)) {
            return false;
        }
        OptionVO other = (OptionVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.OptionVO[ id=" + id + " ]";
    }
    
}
