/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * {"data": {"id": 21, "name": "實木地板", "levelnum": 3, "parent": 2, "parentName": "地板分類", "sortnum": 1, "parentL1": 1}},
 * @author Peter.pan
 */
@XmlRootElement
public class PrdTypeVO implements Serializable {
    private Long id;
    private Long storeId;
    private String cname;
    private String ename;
    private String code;
    private String memo;
    private Long parent;
    private Boolean leaf;
    private Long levelnum;
    private Long sortnum;
    private Boolean hasPrd;
    private Boolean disabled;
    
    private String parentName;
    private String parentEname;
    private Long parentL1;
    private Long parentL2;

    private String parentNameL1;
    private String parentEnameL1;
    private String parentNameL2;
    private String parentEnameL2;
    
    private Integer prdNum;
    private Integer attrNum;

    public PrdTypeVO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentNameL1() {
        return parentNameL1;
    }

    public void setParentNameL1(String parentNameL1) {
        this.parentNameL1 = parentNameL1;
    }

    public String getParentEnameL1() {
        return parentEnameL1;
    }

    public void setParentEnameL1(String parentEnameL1) {
        this.parentEnameL1 = parentEnameL1;
    }

    public String getParentNameL2() {
        return parentNameL2;
    }

    public void setParentNameL2(String parentNameL2) {
        this.parentNameL2 = parentNameL2;
    }

    public String getParentEnameL2() {
        return parentEnameL2;
    }

    public void setParentEnameL2(String parentEnameL2) {
        this.parentEnameL2 = parentEnameL2;
    }

    public String getParentEname() {
        return parentEname;
    }

    public void setParentEname(String parentEname) {
        this.parentEname = parentEname;
    }

    public Integer getAttrNum() {
        return attrNum;
    }

    public void setAttrNum(Integer attrNum) {
        this.attrNum = attrNum;
    }

    public Integer getPrdNum() {
        return prdNum;
    }

    public void setPrdNum(Integer prdNum) {
        this.prdNum = prdNum;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Long getParentL1() {
        return parentL1;
    }

    public void setParentL1(Long parentL1) {
        this.parentL1 = parentL1;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getParentL2() {
        return parentL2;
    }

    public void setParentL2(Long parentL2) {
        this.parentL2 = parentL2;
    }

    public Long getLevelnum() {
        return levelnum;
    }

    public void setLevelnum(Long levelnum) {
        this.levelnum = levelnum;
    }

    public Long getSortnum() {
        return sortnum;
    }

    public void setSortnum(Long sortnum) {
        this.sortnum = sortnum;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Boolean getHasPrd() {
        return hasPrd;
    }

    public void setHasPrd(Boolean hasPrd) {
        this.hasPrd = hasPrd;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
    
}
