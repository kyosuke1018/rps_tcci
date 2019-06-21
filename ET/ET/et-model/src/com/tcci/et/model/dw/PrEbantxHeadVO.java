/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.dw;

import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class PrEbantxHeadVO {
    private String mandt;// 用戶端
    private String banfn;// 請購單號碼
    private String tdid;// 內文 ID
    private String tdtext;// 短文
    private Integer lineNo;// 表格行號
    private String textLine;// 內文行
    private Date syncTimeStamp;

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getBanfn() {
        return banfn;
    }

    public void setBanfn(String banfn) {
        this.banfn = banfn;
    }

    public String getTdid() {
        return tdid;
    }

    public void setTdid(String tdid) {
        this.tdid = tdid;
    }

    public Integer getLineNo() {
        return lineNo;
    }

    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }

    public String getTdtext() {
        return tdtext;
    }

    public void setTdtext(String tdtext) {
        this.tdtext = tdtext;
    }

    public String getTextLine() {
        return textLine;
    }

    public void setTextLine(String textLine) {
        this.textLine = textLine;
    }

    public Date getSyncTimeStamp() {
        return syncTimeStamp;
    }

    public void setSyncTimeStamp(Date syncTimeStamp) {
        this.syncTimeStamp = syncTimeStamp;
    }
    
}
