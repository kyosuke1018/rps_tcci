/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.repository;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.vc.Mastered;
import com.tcci.fc.entity.vc.Versioned;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author Wayne.Hu
 */
@Entity
@Table(name = "TC_DOCUMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TcDocument.findAll", query = "SELECT t FROM TcDocument t"),
    @NamedQuery(name = "TcDocument.findByFolder", query = "SELECT t FROM TcDocument t WHERE t.islatestiteration = 1 AND t.islatestversion = 1 AND t.folder = :folder")
})
public class TcDocument implements Serializable, Foldered, Versioned, ContentHolder {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    private Long id;
    @Basic(optional = false)
    @Column(name = "VERSIONNUMBER")
    private String versionnumber;
    @Basic(optional = false)
    @Column(name = "ITERATIONNUMBER")
    private String iterationnumber;
    @Column(name = "ISLATESTITERATION")
    private Boolean islatestiteration;
    @Column(name = "ISLATESTVERSION")
    private Boolean islatestversion;    
    @Column(name = "DOCTYPE")
    private String doctype;
    @Column(name = "DESCRIPTION")
    private String description;
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
    @JoinColumn(name = "folder", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private TcFolder folder;
    @JoinColumn(name = "master", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcDocumentMaster master;

    public TcDocument() {
    }

    public TcDocument(Long id) {
        this.id = id;
    }

    public TcDocument(Long id, String versionnumber, String iterationnumber) {
        this.id = id;
        this.versionnumber = versionnumber;
        this.iterationnumber = iterationnumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersionnumber() {
        return versionnumber;
    }

    public void setVersionnumber(String versionnumber) {
        this.versionnumber = versionnumber;
    }

    public String getIterationnumber() {
        return iterationnumber;
    }

    public void setIterationnumber(String iterationnumber) {
        this.iterationnumber = iterationnumber;
    }

    public Boolean getIslatestiteration() {
        return islatestiteration;
    }

    public void setIslatestiteration(Boolean islatestiteration) {
        this.islatestiteration = islatestiteration;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcDocument)) {
            return false;
        }
        TcDocument other = (TcDocument) object;
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
        return name;
    }


    @Override
    public Mastered getMaster() {
        return master;
    }

    @Override
    public void setMaster(Mastered master) {
        this.master = (TcDocumentMaster)master;
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

    /**
     * @return the folder
     */
    public TcFolder getFolder() {
        return folder;
    }

    /**
     * @param folder the folder to set
     */
    public void setFolder(TcFolder folder) {
        this.folder = folder;
    }

    /**
     * @return the islatestversion
     */
    public Boolean getIslatestversion() {
        return islatestversion;
    }

    /**
     * @param islatestversion the islatestversion to set
     */
    public void setIslatestversion(Boolean islatestversion) {
        this.islatestversion = islatestversion;
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

    @Override
    public Versioned clone() {
        TcDocument item = null;
        try {

            item = this.getClass().newInstance();
            PropertyUtils.copyProperties(item, this);

            item.setId(null);
            
            //item.setCreator(null);
            //item.setCreatetimestamp(null);
            //item.setModifier(null);
            //item.setModifytimestamp(null);
           
        } catch (InvocationTargetException ex) {
        } catch (NoSuchMethodException ex) {
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        }

        return item;
    }
   
}
