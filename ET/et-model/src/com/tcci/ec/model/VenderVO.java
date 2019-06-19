/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.tcci.cm.model.interfaces.IOperator;
import com.tcci.ec.model.rs.BaseResponseVO;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Kyle.Cheng
 */
public class VenderVO extends BaseResponseVO implements IOperator, Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long mainId;
    private String mandt;
    private String venderCode;
    private String cname;
    private String ename;
    private String nickname;
    
    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;
    
    //category
    private Long categoryId;
    private String cids;
    private String cnames;
    //待核 申請
    private Long factoryId;
    private String factoryName;
    private Long processId;
    private String executionstate;
    private Date starttime;
    private Date endtime;
//    private String applyCids;
//    private String applyCnames;
    private String cidsOri;
    private String cnamesOri;
    private String status;
    private String applicantAd;//申請人
    private String applicantName;
    
    
    @Override
    public String getLabel(){
        return this.mandt + "_(" +this.venderCode + "(" + this.cname + ")";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMainId() {
        return mainId;
    }

    public void setMainId(Long mainId) {
        this.mainId = mainId;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getVenderCode() {
        return venderCode;
    }

    public void setVenderCode(String venderCode) {
        this.venderCode = venderCode;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCids() {
        return cids;
    }

    public void setCids(String cids) {
        this.cids = cids;
    }

    public String getCnames() {
        return cnames;
    }

    public void setCnames(String cnames) {
        this.cnames = cnames;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getExecutionstate() {
        return executionstate;
    }

    public void setExecutionstate(String executionstate) {
        this.executionstate = executionstate;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getCidsOri() {
        return cidsOri;
    }

    public void setCidsOri(String cidsOri) {
        this.cidsOri = cidsOri;
    }

    public String getCnamesOri() {
        return cnamesOri;
    }

    public void setCnamesOri(String cnamesOri) {
        this.cnamesOri = cnamesOri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplicantAd() {
        return applicantAd;
    }

    public void setApplicantAd(String applicantAd) {
        this.applicantAd = applicantAd;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

}
