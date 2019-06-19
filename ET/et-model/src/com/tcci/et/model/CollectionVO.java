/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model;

import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author peter.pan
 */
@XmlRootElement
public class CollectionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date cdate;
    private Long country;
    private Long province;
    @Size(max = 600)
    private String localityEname;
    @Size(max = 600)
    private String localityCname;
    @Size(max = 300)
    private String habitat;
    @Size(max = 50)
    private String collectorNo;
    @Size(max = 90)
    private String altitude;
    @Size(max = 50)
    private String latitude;
    @Size(max = 50)
    private String longitude;
    @Size(max = 600)
    private String memo;

    private TcUser creator;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;

    private TcUser modifier;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;    

    private String lastUserName;
    private Date lastTime;
    
    public TcUser getLastUpdateUser(){
        return (modifier!=null)?modifier:creator;
    }
    public Date getLastUpdateTime(){
        return (modifytimestamp!=null)?modifytimestamp:createtimestamp;
    }

    public CollectionVO() {
    }

    public CollectionVO(Long id) {
        this.id = id;
    }

    public String getCollectorNo() {
        return collectorNo;
    }

    public void setCollectorNo(String collectorNo) {
        this.collectorNo = collectorNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public String getLocalityEname() {
        return localityEname;
    }

    public void setLocalityEname(String localityEname) {
        this.localityEname = localityEname;
    }

    public String getLocalityCname() {
        return localityCname;
    }

    public void setLocalityCname(String localityCname) {
        this.localityCname = localityCname;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getCountry() {
        return country;
    }

    public void setCountry(Long country) {
        this.country = country;
    }

    public Long getProvince() {
        return province;
    }

    public void setProvince(Long province) {
        this.province = province;
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

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
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
        if (!(object instanceof CollectionVO)) {
            return false;
        }
        CollectionVO other = (CollectionVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.CollectionVO[ id=" + id + " ]";
    }
    
}
