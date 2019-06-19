/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import java.io.Serializable;


/**
 *
 * @author gilbert
 */
public class TcFactoryVO implements Serializable, Comparable{
    private static final long serialVersionUID = 1L;
    private String mandt;
    private String sapClientCode;
    private String factoryCode;
    private String factoryName;
    private String categoryCode;
    private String categoryName;

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getSapClientCode() {
        return sapClientCode;
    }

    public void setSapClientCode(String sapClientCode) {
        this.sapClientCode = sapClientCode;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
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
    
    public String getCategoryCode() {
        return categoryCode;
    }
    
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    //</editor-fold>

    @Override
    public int compareTo(Object o) {
        TcFactoryVO target = (TcFactoryVO)o;
        return this.getFactoryCode().compareTo(target.getFactoryCode());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.factoryCode != null ? this.factoryCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TcFactoryVO other = (TcFactoryVO) obj;
        if ((this.factoryCode == null) ? (other.factoryCode != null) : !this.factoryCode.equals(other.factoryCode)) {
            return false;
        }
        return true;
    }
    
    
    
}
