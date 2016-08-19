/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.enums;

/**
 *
 * @author Gilbert.Lin
 */
public enum SecurityRoleEnum {
 ValidUsers("有效登入者","valid-users")
,ADMINISTRATORS("系統管理者","ADMINISTRATORS") 
,SwitchUser("切換用戶","switch-user")
    ;

    
    private String label;
    private String value;
    
    SecurityRoleEnum(String label, String value) {
        this.label = label;
        this.value = value;
    }


  
    @Override
    public String toString() {
        return getValue();
    }      
    public static SecurityRoleEnum getByValue(String value) {
        for (SecurityRoleEnum harborSEnum : SecurityRoleEnum.values()) {
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
