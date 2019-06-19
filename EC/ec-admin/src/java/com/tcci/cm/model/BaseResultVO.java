/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Datawarehouse 查詢結果Base VO，包含基本結果: 資料同步時間
 * @author gilbert
 */
public abstract class BaseResultVO implements Serializable, Comparable {
    protected static final long serialVersionUID = 1L;
    protected Date syncTimeStamp;//資料同步時間
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public Date getSyncTimeStamp() {
        return syncTimeStamp;
    }
    public void setSyncTimeStamp(Date syncTimeStamp) {
        this.syncTimeStamp = syncTimeStamp;
    }
    //</editor-fold>   
}
