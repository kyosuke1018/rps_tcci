/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model.admin;

import com.tcci.cm.entity.admin.CmOrg;
import java.io.Serializable;

/**
 *
 * @author Peter.pan
 */
public class OrganizationVO implements Serializable {
    private long key; // for UI : before save to DB
    private boolean hasChild;
    private CmOrg cmOrg;

    public OrganizationVO(){    
    }
    
    public OrganizationVO(long key, boolean hasChild, CmOrg cmOrg){
        this.key = key;
        this.hasChild = hasChild;
        this.cmOrg = cmOrg;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public CmOrg getCmOrg() {
        return cmOrg;
    }

    public void setCmOrg(CmOrg cmOrg) {
        this.cmOrg = cmOrg;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }
    
    @Override
    public String toString(){
        if( cmOrg!=null ){
            if( cmOrg.getCode()!=null && !cmOrg.getCode().isEmpty() ){
                return cmOrg.getCode() + "-" + cmOrg.getName();
            }else{
                return cmOrg.getName();
            }
        }
        return "";
    }
}
