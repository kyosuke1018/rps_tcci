/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.model.admin;

import com.tcci.cm.model.global.AbstractCriteriaVO;
import com.tcci.cm.model.interfaces.IQueryCriteria;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Peter
 */
public class ActivityLogCriteriaVO extends AbstractCriteriaVO implements IQueryCriteria, Serializable {
    private String keyword;
    private String keywordUrl;
    private String code;
    
    private Date dateStart;
    private Date dateEnd;
    
    private String successFlag;

    @Override
    public void clear(){
        keyword = "";
        keywordUrl = "";
        code = "";
        dateStart = null;
        dateEnd = null;
        successFlag = null;
    }
    
    public Boolean getSuccess(){
        if( successFlag==null ){
            return null;
        }
        try{
            return Boolean.valueOf(successFlag);
        }catch(Exception e){
            // ignore
        }
        return null;
    }

    public String getKeywordUrl() {
        return keywordUrl;
    }

    public void setKeywordUrl(String keywordUrl) {
        this.keywordUrl = keywordUrl;
    }
    
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(String successFlag) {
        this.successFlag = successFlag;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }
       
}
