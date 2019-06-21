/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.admin;

import com.tcci.cm.model.interfaces.IPresentationVO;
import java.io.Serializable;

/**
 * 功能選單
 * @author Peter
 */
public class MenuFunctionVO implements IPresentationVO, Comparable, Serializable {
    private long id;
    private String title;
    private String titleCN;
    private String url;
    private int sortnum;
    private String rptTypes;
    private String code;
    private String rptCode;
    private String titleCode;
    
    private String description;
    private String risk;
    private String ctrlBefore;
    private String ctrlNow;
    private String ctrlAfter;
    
    private long sid;
    private String stitle;
    private String stitleCN;
    private int ssortnum;
    private String srptTypes;
    private String scode;
    
    private long mid;
    private String mtitle;
    private String mtitleCN;
    private int msortnum;
    private String mrptTypes;
    private String mcode;

    private String auth;// 功能授權階層
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }

    public String getRptCode() {
        return rptCode;
    }

    public void setRptCode(String rptCode) {
        this.rptCode = rptCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getCtrlBefore() {
        return ctrlBefore;
    }

    public void setCtrlBefore(String ctrlBefore) {
        this.ctrlBefore = ctrlBefore;
    }

    public String getCtrlNow() {
        return ctrlNow;
    }

    public void setCtrlNow(String ctrlNow) {
        this.ctrlNow = ctrlNow;
    }

    public String getCtrlAfter() {
        return ctrlAfter;
    }

    public void setCtrlAfter(String ctrlAfter) {
        this.ctrlAfter = ctrlAfter;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public String getMcode() {
        return mcode;
    }

    public void setMcode(String mcode) {
        this.mcode = mcode;
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

    public int getSortnum() {
        return sortnum;
    }

    public void setSortnum(int sortnum) {
        this.sortnum = sortnum;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getStitle() {
        return stitle;
    }

    public void setStitle(String stitle) {
        this.stitle = stitle;
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public String getMtitle() {
        return mtitle;
    }

    public void setMtitle(String mtitle) {
        this.mtitle = mtitle;
    }

    public int getSsortnum() {
        return ssortnum;
    }

    public void setSsortnum(int ssortnum) {
        this.ssortnum = ssortnum;
    }

    public int getMsortnum() {
        return msortnum;
    }

    public void setMsortnum(int msortnum) {
        this.msortnum = msortnum;
    }

    public String getTitleCN() {
        return titleCN;
    }

    public void setTitleCN(String titleCN) {
        this.titleCN = titleCN;
    }

    public String getStitleCN() {
        return stitleCN;
    }

    public void setStitleCN(String stitleCN) {
        this.stitleCN = stitleCN;
    }

    public String getRptTypes() {
        return rptTypes;
    }

    public void setRptTypes(String rptTypes) {
        this.rptTypes = rptTypes;
    }

    public String getSrptTypes() {
        return srptTypes;
    }

    public void setSrptTypes(String srptTypes) {
        this.srptTypes = srptTypes;
    }

    public String getMrptTypes() {
        return mrptTypes;
    }

    public void setMrptTypes(String mrptTypes) {
        this.mrptTypes = mrptTypes;
    }

    public String getMtitleCN() {
        return mtitleCN;
    }

    public void setMtitleCN(String mtitleCN) {
        this.mtitleCN = mtitleCN;
    }
    //</editor-fold>
    
    @Override
    public boolean equals(Object other){
        if( other!=null ){
            if (!(other instanceof MenuFunctionVO)) {
                return false;
            }
            
            return (this.getId() == ((MenuFunctionVO)other).getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
    
    @Override
    public int compareTo(Object o) {
        return (int)(this.getId() - ((MenuFunctionVO)o).getId());
    }
    
    /**
     * 是否包含於指定模組
     * @param module
     * @return 
     */
    public boolean inModule(String module){
        return (this.mcode!=null && this.mcode.equals(module));
        //return (this.rptModules==null)? false:(this.rptModules.indexOf("~"+module+"~")>-1);
    }
    
    /**
     * 有無指定類型報表
     * @param key = [ctrl phase] + [period], ex. AM, AD, NM, ND, ...
     * @return 
     */
    public boolean hasReport(String key){
        return (this.rptTypes==null)? false:(this.rptTypes.indexOf("~"+key+"~")>-1);
    }
    
    /**
     * 無報表
     * @param period
     * @return 
     */
    public boolean isNoReport(){
        return (this.mrptTypes!=null && this.mrptTypes.equals("X")) // 從主分類關閉
                || (this.srptTypes!=null && this.srptTypes.equals("X")) // 從子分類關閉
                || (this.rptTypes==null || this.rptTypes.isEmpty());
    }
}
