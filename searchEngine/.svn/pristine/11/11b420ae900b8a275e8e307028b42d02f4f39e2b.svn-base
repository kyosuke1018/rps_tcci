/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.solr.client.model;

import com.tcci.solr.client.util.SolrUtils;
import java.io.Serializable;

/**
 *
 * @author Peter
 */
public class TcSolrSource implements Serializable {
    protected String id;// solr pk

    protected String source; // 來源 (系統代號)
    protected long cid; // 各別系統 pk id
    protected String title;// customized title
    protected String description;// customized description
    protected String path;// real file path
    protected String filename;// filename

    public TcSolrSource(){
        id = SolrUtils.getSolrId();// 產生唯一KEY
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    //</editor-fold>

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("id = ").append(id).append(";")
          .append("source = ").append(source).append(";")
          .append("cid = ").append(cid).append(";")
          .append("title = ").append(title).append("\n")
          .append("description = ").append(description).append("\n")
          .append("filename = ").append(filename).append("\n")
          .append("path = ").append(path);
        
        return sb.toString();
    }
}
