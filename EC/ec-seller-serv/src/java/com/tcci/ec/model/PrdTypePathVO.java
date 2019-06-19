/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * T3.ID T3ID, T3.CNAME T3CNAME, T3.ENAME T3ENAME
, T2.ID T2ID, T2.CNAME T2CNAME, T2.ENAME T2ENAME
, T1.ID T1ID, T1.CNAME T1CNAME, T1.ENAME T1ENAME
 * @author Peter.pan
 */
@XmlRootElement
public class PrdTypePathVO implements Serializable {
    private Long t1Id;
    private String t1Cname;
    private String t1Ename;
    private Integer t1Levelnum;
    private Integer t1Sortnum;
    
    private Long t2Id;
    private String t2Cname;
    private String t2Ename;
    private Integer t2Levelnum;
    private Integer t2Sortnum;
    
    private Long t3Id;
    private String t3Cname;
    private String t3Ename;
    private Integer t3Levelnum;
    private Integer t3Sortnum;
    
    public PrdTypePathVO(){}

    public Long getT1Id() {
        return t1Id;
    }

    public void setT1Id(Long t1Id) {
        this.t1Id = t1Id;
    }

    public Integer getT1Levelnum() {
        return t1Levelnum;
    }

    public void setT1Levelnum(Integer t1Levelnum) {
        this.t1Levelnum = t1Levelnum;
    }

    public Integer getT1Sortnum() {
        return t1Sortnum;
    }

    public void setT1Sortnum(Integer t1Sortnum) {
        this.t1Sortnum = t1Sortnum;
    }

    public Integer getT2Levelnum() {
        return t2Levelnum;
    }

    public void setT2Levelnum(Integer t2Levelnum) {
        this.t2Levelnum = t2Levelnum;
    }

    public Integer getT2Sortnum() {
        return t2Sortnum;
    }

    public void setT2Sortnum(Integer t2Sortnum) {
        this.t2Sortnum = t2Sortnum;
    }

    public Integer getT3Levelnum() {
        return t3Levelnum;
    }

    public void setT3Levelnum(Integer t3Levelnum) {
        this.t3Levelnum = t3Levelnum;
    }

    public Integer getT3Sortnum() {
        return t3Sortnum;
    }

    public void setT3Sortnum(Integer t3Sortnum) {
        this.t3Sortnum = t3Sortnum;
    }

    public String getT1Cname() {
        return t1Cname;
    }

    public void setT1Cname(String t1Cname) {
        this.t1Cname = t1Cname;
    }

    public String getT1Ename() {
        return t1Ename;
    }

    public void setT1Ename(String t1Ename) {
        this.t1Ename = t1Ename;
    }

    public Long getT2Id() {
        return t2Id;
    }

    public void setT2Id(Long t2Id) {
        this.t2Id = t2Id;
    }

    public String getT2Cname() {
        return t2Cname;
    }

    public void setT2Cname(String t2Cname) {
        this.t2Cname = t2Cname;
    }

    public String getT2Ename() {
        return t2Ename;
    }

    public void setT2Ename(String t2Ename) {
        this.t2Ename = t2Ename;
    }

    public Long getT3Id() {
        return t3Id;
    }

    public void setT3Id(Long t3Id) {
        this.t3Id = t3Id;
    }

    public String getT3Cname() {
        return t3Cname;
    }

    public void setT3Cname(String t3Cname) {
        this.t3Cname = t3Cname;
    }

    public String getT3Ename() {
        return t3Ename;
    }

    public void setT3Ename(String t3Ename) {
        this.t3Ename = t3Ename;
    }

    
}
