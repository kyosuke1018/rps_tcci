/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model;

import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class BuyingVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date buydate;
    @Size(max = 150)
    private String source;
    @Size(max = 120)
    private String seller;
    @Size(max = 120)
    private String buyer;

    private BigDecimal price;
    @Size(max = 300)
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

    public BuyingVO() {
    }

    public BuyingVO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBuydate() {
        return buydate;
    }

    public void setBuydate(Date buydate) {
        this.buydate = buydate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
        if (!(object instanceof BuyingVO)) {
            return false;
        }
        BuyingVO other = (BuyingVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.BuyingVO[ id=" + id + " ]";
    }
    
}
