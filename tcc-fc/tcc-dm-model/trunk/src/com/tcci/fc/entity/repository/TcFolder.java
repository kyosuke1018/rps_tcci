/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.repository;

import com.tcci.fc.entity.access.FolderedAccessControlled;
import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Wayne.Hu
 */
@Entity
@Table(name = "TC_FOLDER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TcFolder.findAll", query = "SELECT t FROM TcFolder t")})
public class TcFolder implements Serializable, FolderedAccessControlled {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Column(name = "ISREMOVED")
    private Boolean isremoved;     
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @JoinColumn(name = "creator", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @JoinColumn(name = "modifier", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;    
    @Column(name = "ICON")
    private String icon;
    @JoinColumn(name = "FOLDER", referencedColumnName = "ID")
    @ManyToOne
    private TcFolder folder;

    public TcFolder() {
    }

    public TcFolder(long id) {
        this.id = id;
    }

    public TcFolder(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

  

    public TcFolder getFolder() {
        return folder;
    }

    public void setFolder(TcFolder folder) {
        this.folder = folder;
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
        if (!(object instanceof TcFolder)) {
            return false;
        }
        TcFolder other = (TcFolder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + ":" + getId();
    }

    /**
     * @return the creator
     */
    public TcUser getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    /**
     * @return the modifier
     */
    public TcUser getModifier() {
        return modifier;
    }

    /**
     * @param modifier the modifier to set
     */
    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }
    
    @Override
    public String getDisplayIdentifier() {
        return name;
    }

    /**
     * @return the isremoved
     */
    public Boolean getIsremoved() {
        return isremoved;
    }

    /**
     * @param isremoved the isremoved to set
     */
    public void setIsremoved(Boolean isremoved) {
        this.isremoved = isremoved;
    }

}
