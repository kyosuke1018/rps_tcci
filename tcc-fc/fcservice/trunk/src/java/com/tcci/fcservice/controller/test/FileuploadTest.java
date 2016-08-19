/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.controller.test;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.essential.Persistable;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Louisz.Cheng
 */
@Entity
@Table(name = "FILEUPLOAD_TEST")
@NamedQueries({
    @NamedQuery(name = "FileuploadTest.findAll", query = "SELECT f FROM FileuploadTest f")})
public class FileuploadTest implements Serializable, Persistable, ContentHolder {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_FC_TEST", sequenceName = "SEQ_FC_TEST", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FC_TEST")
    private Long id;
    @Size(max = 20)
    @Column(name = "NAME")
    private String name;
    @Size(max = 20)
    @Column(name = "CONTENT")
    private String content;
    
    public FileuploadTest() {
    }

    public FileuploadTest(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        if (!(object instanceof FileuploadTest)) {
            return false;
        }
        FileuploadTest other = (FileuploadTest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fcservice.controller.test.FileuploadTest[ id=" + id + " ]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    
}
