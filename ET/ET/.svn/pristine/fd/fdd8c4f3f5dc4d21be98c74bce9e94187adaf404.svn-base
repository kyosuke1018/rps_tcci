/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.model.admin;

import java.io.Serializable;

/**
 *
 * @author Peter
 */
public class CmFactoryGroupVO implements Serializable {
    private long usergroupId;
    private String usergroupCode;
    private String factoryCode;
    private String factoryName;
    private String groupCode;
    private String groupName;

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public long getUsergroupId() {
        return usergroupId;
    }

    public void setUsergroupId(long usergroupId) {
        this.usergroupId = usergroupId;
    }

    public String getUsergroupCode() {
        return usergroupCode;
    }

    public void setUsergroupCode(String usergroupCode) {
        this.usergroupCode = usergroupCode;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    //</editor-fold>
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CmFactoryGroupVO)) {
            return false;
        }
        CmFactoryGroupVO other = (CmFactoryGroupVO) obj;
        
        return this.usergroupId==other.usergroupId 
                && ((this.groupCode==null && other.groupCode==null) || (this.groupCode!=null && this.groupCode.equals(other.groupCode)))
                && ((this.factoryCode==null && other.factoryCode==null) || (this.factoryCode!=null && this.factoryCode.equals(other.factoryCode)));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (int) (this.usergroupId ^ (this.usergroupId >>> 32));
        hash = 19 * hash + (this.factoryCode != null ? this.factoryCode.hashCode() : 0);
        hash = 19 * hash + (this.groupCode != null ? this.groupCode.hashCode() : 0);
        return hash;
    }
}
