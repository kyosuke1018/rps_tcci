/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gilbert.Lin
 */
public enum PagecodeRoleEnum {
 I11("I11","Administrator")   
    ;

    
    private String pageCode;
    private String roles;
    
    PagecodeRoleEnum(String pageCode, String roles) {
        this.pageCode = pageCode;
        this.roles = roles;
    }

    public String getDisplayIdentifier() {
        return this.pageCode +"("+ this.roles +")";
    }    

  
    @Override
    public String toString() {
        return getDisplayIdentifier();
    }      
    public static List<String> getPageCodes(String role) {
        List<String> list = new ArrayList<>();
        for (PagecodeRoleEnum thisEnum : PagecodeRoleEnum.values()) {
            String roles = thisEnum.roles;
            if (roles.contains(role)) {
                list.add(thisEnum.pageCode);
            }
        }
        return list;
    }     
    public static List<PagecodeRoleEnum> getEnumList(String role) {
        List<PagecodeRoleEnum> list = new ArrayList<>();
        for (PagecodeRoleEnum thisEnum : PagecodeRoleEnum.values()) {
            String roles = thisEnum.roles;
            if (roles.contains(role)) {
                list.add(thisEnum);
            }
        }
        return list;
    }     
    public static PagecodeRoleEnum getEnum(String pageCode) {
        for (PagecodeRoleEnum thisEnum : PagecodeRoleEnum.values()) {
            String thisPageCode = thisEnum.pageCode;
            if (thisPageCode.equalsIgnoreCase(pageCode)) {
                return thisEnum;
            }
        }
        return null;
    }
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    //</editor-fold>



  
}
