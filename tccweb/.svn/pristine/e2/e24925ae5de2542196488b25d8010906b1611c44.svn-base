/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccweb.tcc.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author Jackson.Lee
 */
@Entity
@Table(name = "news")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "News.findAll", query = "SELECT n FROM News n ORDER BY n.yy, n.idnum"),
    @NamedQuery(name = "News.findAllEnable", query = "SELECT n FROM News n WHERE n.enable=1  ORDER BY n.yy, n.idnum"),
    @NamedQuery(name = "News.findByIdnum", query = "SELECT n FROM News n WHERE n.idnum = :idnum and n.enable=1"),
    @NamedQuery(name = "News.findByYy", query = "SELECT n FROM News n WHERE n.yy = :yy and n.enable=1" ),
})
public class News implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idnum")
    private Integer idnum;
    @Size(max = 50)
    @Column(name = "yy")
    private String yy;
    @Size(max = 200)
    @Column(name = "subject")
    private String subject;
    @Size(max = 200)
    @Column(name = "subjectst")
    private String subjectst;
    @Size(max = 200)
    @Column(name = "subjectCHS")
    private String subjectCHS;
    @Size(max = 400)
    @Column(name = "subjecten")
    private String subjecten;
    @Column(name = "class_id")
    private Integer classId;
    @Size(max = 1073741823)
    @Column(name = "memo")
    private String memo;
    @Size(max = 1073741823)
    @Column(name = "memost")
    private String memost;
    @Size(max = 1073741823)
    @Column(name = "memoCHS")
    private String memoCHS;
    @Size(max = 1073741823)
    @Column(name = "memoen")
    private String memoen;
    @Column(name = "up_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date upDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Size(max = 1073741823)
    @Column(name = "file_path_title")
    private String filePathTitle;
    @Size(max = 1073741823)
    @Column(name = "file_path_titlest")
    private String filePathTitlest;
    @Size(max = 1073741823)
    @Column(name = "file_path_titleen")
    private String filePathTitleen;
    @Size(max = 1073741823)
    @Column(name = "file_path")
    private String filePath;
    @Size(max = 1073741823)
    @Column(name = "file_pathst")
    private String filePathst;
    @Size(max = 1073741823)
    @Column(name = "file_pathen")
    private String filePathen;
    @Column(name = "sort")
    private Integer sort;
    @Column(name = "status")
    private Short status;
    @Column(name = "enable")
    private Short enable;
    @Column(name = "cdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cdate;
    @Size(max = 50)
    @Column(name = "creater")
    private String creater;
    @Column(name = "mdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mdate;
    @Size(max = 50)
    @Column(name = "modifier")
    private String modifier;

    public News() {
    }

    public News(Integer idnum) {
        this.idnum = idnum;
    }

    public Integer getIdnum() {
        return idnum;
    }

    public void setIdnum(Integer idnum) {
        this.idnum = idnum;
    }

    public String getYy() {
        return yy;
    }

    public void setYy(String yy) {
        this.yy = yy;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectst() {
        return subjectst;
    }

    public void setSubjectst(String subjectst) {
        this.subjectst = subjectst;
    }

    public String getSubjectCHS() {
        return subjectCHS;
    }

    public void setSubjectCHS(String subjectCHS) {
        this.subjectCHS = subjectCHS;
    }

    public String getSubjecten() {
        return subjecten;
    }

    public void setSubjecten(String subjecten) {
        this.subjecten = subjecten;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemost() {
        return memost;
    }

    public void setMemost(String memost) {
        this.memost = memost;
    }

    public String getMemoCHS() {
        return memoCHS;
    }

    public void setMemoCHS(String memoCHS) {
        this.memoCHS = memoCHS;
    }

    public String getMemoen() {
        return memoen;
    }

    public void setMemoen(String memoen) {
        this.memoen = memoen;
    }

    public Date getUpDate() {
        return upDate;
    }

    public void setUpDate(Date upDate) {
        this.upDate = upDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFilePathTitle() {
        return filePathTitle;
    }

    public void setFilePathTitle(String filePathTitle) {
        this.filePathTitle = filePathTitle;
    }

    public String getFilePathTitlest() {
        return filePathTitlest;
    }

    public void setFilePathTitlest(String filePathTitlest) {
        this.filePathTitlest = filePathTitlest;
    }

    public String getFilePathTitleen() {
        return filePathTitleen;
    }

    public void setFilePathTitleen(String filePathTitleen) {
        this.filePathTitleen = filePathTitleen;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePathst() {
        return filePathst;
    }

    public void setFilePathst(String filePathst) {
        this.filePathst = filePathst;
    }

    public String getFilePathen() {
        return filePathen;
    }

    public void setFilePathen(String filePathen) {
        this.filePathen = filePathen;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Short getEnable() {
        return enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getMdate() {
        return mdate;
    }

    public void setMdate(Date mdate) {
        this.mdate = mdate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idnum != null ? idnum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof News)) {
            return false;
        }
        News other = (News) object;
        if ((this.idnum == null && other.idnum != null) || (this.idnum != null && !this.idnum.equals(other.idnum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccweb.entity.tcc.News[ idnum=" + idnum + " ]";
    }
    
}
