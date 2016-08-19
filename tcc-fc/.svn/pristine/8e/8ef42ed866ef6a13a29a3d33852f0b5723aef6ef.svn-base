package com.tcci.fc.entity.content;

import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Neo.Fu
 */
@Entity
@Table(name = "tc_urldata")
public class TcURLData implements Serializable, Persistable, ContentItem  {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Column(name = "containerclassname")
    private String containerclassname;
    @Column(name = "containerid")
    private Long containerid;
    @Column(name = "createtimestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Column(name = "contentrole")
    private Character contentrole;
    @Column(name = "description")
    private String description;
    @Column(name = "url")
    private String url;
    @JoinColumn(name = "creator", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;

    public TcURLData() {
    }

    public TcURLData(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContainerclassname() {
        return containerclassname;
    }

    public void setContainerclassname(String containerclassname) {
        this.containerclassname = containerclassname;
    }

    public Long getContainerid() {
        return containerid;
    }

    public void setContainerid(Long containerid) {
        this.containerid = containerid;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public Character getContentrole() {
        return contentrole;
    }

    public void setContentrole(Character contentrole) {
        this.contentrole = contentrole;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        if (!(object instanceof TcURLData)) {
            return false;
        }
        TcURLData other = (TcURLData) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.content.TcURLData[id=" + id + "]";
    }

}
