/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.ar;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.essential.TcObject;
import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lynn.Huang
 */
@Entity
@Table(name = "SK_PRODUCT_MASTER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkProductMaster.findAll", query = "SELECT s FROM SkProductMaster s"),
    @NamedQuery(name = "SkProductMaster.findById", query = "SELECT s FROM SkProductMaster s WHERE s.id = :id"),
    @NamedQuery(name = "SkProductMaster.findByCode", query = "SELECT s FROM SkProductMaster s WHERE s.code = :code"),
    @NamedQuery(name = "SkProductMaster.findByName", query = "SELECT s FROM SkProductMaster s WHERE s.name = :name"),
    @NamedQuery(name = "SkProductMaster.findByCreatetimestamp", query = "SELECT s FROM SkProductMaster s WHERE s.createtimestamp = :createtimestamp"),
    @NamedQuery(name = "SkProductMaster.findByModifytimestamp", query = "SELECT s FROM SkProductMaster s WHERE s.modifytimestamp = :modifytimestamp")})
public class SkProductMaster implements Serializable, ContentHolder, TcObject {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 18)
    @Column(name = "CODE")
    private String code;
    @Size(max = 64)
    @Column(name = "NAME")
    private String name;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @Size(max = 10)
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "MVGR1")
    private String mvgr1;
    @Column(name = "MVGR2")
    private String mvgr2;
    @Column(name = "MVGR3")
    private String mvgr3;
    @Column(name = "MVGR4")
    private String mvgr4;
    @Column(name = "MVGR5")
    private String mvgr5;

    public SkProductMaster() {
    }

    public SkProductMaster(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMvgr1() {
        return mvgr1;
    }

    public void setMvgr1(String mvgr1) {
        this.mvgr1 = mvgr1;
    }

    public String getMvgr2() {
        return mvgr2;
    }

    public void setMvgr2(String mvgr2) {
        this.mvgr2 = mvgr2;
    }

    public String getMvgr3() {
        return mvgr3;
    }

    public void setMvgr3(String mvgr3) {
        this.mvgr3 = mvgr3;
    }

    public String getMvgr4() {
        return mvgr4;
    }

    public void setMvgr4(String mvgr4) {
        this.mvgr4 = mvgr4;
    }

    public String getMvgr5() {
        return mvgr5;
    }

    public void setMvgr5(String mvgr5) {
        this.mvgr5 = mvgr5;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        if (!(object instanceof SkProductMaster)) {
            return false;
        }
        SkProductMaster other = (SkProductMaster) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + ":" + getId();
    }

    @Override
    public String getDisplayIdentifier() {
        return this.name;
    }
}
