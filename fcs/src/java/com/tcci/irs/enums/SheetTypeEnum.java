/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.enums;

/**
 *
 * @author Gilbert.Lin
 */
public enum SheetTypeEnum {
 BS("資產負債表","BS"),   
 IS("損益表","IS");

    
    private String label;
    private String value;
    
    SheetTypeEnum(String label, String value) {
        this.label = label;
        this.value = value;
    }


  
    @Override
    public String toString() {
        return getValue()+"("+ getLabel() +")";
    }      
    public static SheetTypeEnum getByValue(String value) {
        for (SheetTypeEnum harborSEnum : SheetTypeEnum.values()) {
            if (value.equals(harborSEnum.getValue())) {
                return harborSEnum;
            }
        }
        return null;
    }     
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    //</editor-fold>
  
}
