/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.worklist.entity.datawarehouse;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jackson.Lee
 */
@Entity
@Cacheable(false)
@Table(name = "MG_RELFILENO_EMP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RelfilenoEmp.findAll", query = "SELECT r FROM RelfilenoEmp r")})
public class RelfilenoEmp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "BNAME")
    private String bname;
    @Size(max = 20)
    @Column(name = "EMP_CODE")
    private String empCode;
    @Size(max = 60)
    @Column(name = "NOTES")
    private String notes;

    public RelfilenoEmp() {
    }

    public RelfilenoEmp(String id) {
        this.id = id;
    }

    public RelfilenoEmp(String id, String bname) {
        this.id = id;
        this.bname = bname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        if (!(object instanceof RelfilenoEmp)) {
            return false;
        }
        RelfilenoEmp other = (RelfilenoEmp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.mygui.entity.RelfilenoEmp[ id=" + id + " ]";
    }
    
}
