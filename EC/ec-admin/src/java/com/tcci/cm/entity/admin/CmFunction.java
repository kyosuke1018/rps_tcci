/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.entity.admin;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Peter
 */
@Entity
@Table(name = "CM_FUNCTION")
@NamedQueries({
    @NamedQuery(name = "CmFunction.findAll", query = "SELECT p FROM CmFunction p"),
    @NamedQuery(name = "CmFunction.findByRuleCode", query = "SELECT p FROM CmFunction p where p.code=:code")
})
public class CmFunction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_GLOBAL")
    @SequenceGenerator (name="SEQ_GLOBAL", sequenceName="SEQ_GLOBAL", allocationSize=1)
    private Long id;
    @Size(max = 1)
    @Column(name = "CTYPE")
    private String ctype;
    @Column(name = "CLEVEL")
    private int clevel;
    @Size(max = 64)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 128)
    @Column(name = "URL")
    private String url;
    @Size(max = 256)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CREATOR")
    private Long creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Column(name = "PARENT")
    private Long parent;
    @Column(name = "SORTNUM")
    private int sortnum;
   
    @Size(max = 10)
    @Column(name = "CODE")
    private String code;
    /*
    // ===== for ICS ==============
    @Size(max = 30)
    @Column(name = "RPTTYPES")
    private String rptTypes;
    //@Size(max = 60)
    //@Column(name = "RPTMODULES")
    //private String rptModules;
    @Size(max = 10)
    @Column(name = "RPTCODE")
    private String rptCode;

    @Size(max = 2)
    @Column(name = "QUESTION_LEVEL")
    private String questionLevel;// 決定是否為問題層級(0:無;1:總處;2:廠端)
    @Size(max = 2)
    @Column(name = "CLOSE_LEVEL")
    private String closeLevel;// 決定是否可結案層級(0:無;1:總處;2:廠端)
    
    @Column(name = "FEEDBACK")
    private Boolean feedback;// 有無回饋機制(0:無; 1:有)
    @Column(name = "LIGHT")
    private Boolean light;// 有無支援燈號(0:無; 1:有)
    @Column(name = "SCORE")
    private Boolean score;// 有無支援扣考(0:無; 1:有)
    @Column(name = "SCORE_DEDUCTION")
    private Integer scoreDeduction;// 扣分
    @Column(name = "SCORE_DEADLINE")
    private Integer scoreDeadline;// 扣考修改期限

    @Column(name = "FEEDBACK_STEP")
    private Integer feedbackStep;// 回饋關數 3
    @Column(name = "SCORE_PRE_MONTH")
    private Integer scorePreMonth;// 扣考報表產出月(前幾月) 1
    // ===== for ICS ==============
    */
    
    @OneToMany(mappedBy = "funcId")
    private List<CmGroupFunctionR> cmGroupFunctionRList;

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">    
    public CmFunction() {
    }

    public CmFunction(Long id) {
        this.id = id;
    }

    public List<CmGroupFunctionR> getCmGroupFunctionRList() {
        return cmGroupFunctionRList;
    }

    public void setCmGroupFunctionRList(List<CmGroupFunctionR> cmGroupFunctionRList) {
        this.cmGroupFunctionRList = cmGroupFunctionRList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public int getClevel() {
        return clevel;
    }

    public void setClevel(int clevel) {
        this.clevel = clevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
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

    public int getSortnum() {
        return sortnum;
    }

    public void setSortnum(int sortnum) {
        this.sortnum = sortnum;
    }
    //</editor-fold>    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CmFunction)) {
            return false;
        }
        
        CmFunction other = (CmFunction) object;
        if( other.getId()==null ){
            return false;
        }
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "com.tcci.pp.entity.my.PpFunction[ id=" + id + " ]";
    }
    
}
