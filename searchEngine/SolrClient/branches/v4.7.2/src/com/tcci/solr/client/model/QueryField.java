package com.tcci.solr.client.model;

import com.tcci.solr.client.conf.TcSolrConfig;
import com.tcci.solr.client.exception.SolrProxyException;
import com.tcci.solr.client.util.SolrUtils;
import java.io.Serializable;

/**
 * 欄位查詢資訊
 * @author Peter
 */
public class QueryField implements Serializable {
    private String name;
    private String value;
    private double boost = TcSolrConfig.DEF_BOOST; // 權重 use 0 ~ 2, default 1
    private boolean isPhrase = false;// 片語
    private boolean zhcode = TcSolrConfig.ZHCODER_ENABLED;// 支援繁簡轉換

    public QueryField(String name){
        this.name = name;
    }
    
    public QueryField(String name, String value){
        this.name = name;
        this.value = value;
    }
    
    public QueryField(String name, String value, double boost){
        this.name = name;
        this.value = value;
        this.boost = boost;
    }

    public QueryField(String name, String value, boolean zhcode){
        this.name = name;
        this.value = value;
        this.zhcode = zhcode;
    }
    
    public String getQueryValue() throws SolrProxyException{
        if( isPhrase ){
            return SolrUtils.phraseEscape(value);
        }else{
            return value;
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBoost() {
        return boost;
    }

    public void setBoost(double boost) {
        this.boost = boost;
    }

    public boolean getIsPhrase() {
        return isPhrase;
    }

    public void setIsPhrase(boolean isPhrase) {
        this.isPhrase = isPhrase;
    }

    public boolean isZhcode() {
        return zhcode;
    }

    public void setZhcode(boolean zhcode) {
        this.zhcode = zhcode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    //</editor-fold>
    
}
