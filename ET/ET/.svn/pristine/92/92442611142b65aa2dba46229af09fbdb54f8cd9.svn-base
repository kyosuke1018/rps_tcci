/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *　圖庫類型
 * @author Peter.Pan    
 */
public enum PhotoGalleryEnum {
    CUSTOM("C", "自訂相簿/圖庫", true, null),// Albums
    DOC("D", "文章插圖", true, GlobalConstant.DOMAIN_NAME_DOC_IMG),// Articles illustrations

    IMAGE("I","圖片", false, GlobalConstant.DOMAIN_NAME_CUSTOM_IMG),
    ;

    private String code;
    private String name;
    private boolean directory;
    private String domainName;
    
    PhotoGalleryEnum(String code, String name, boolean directory, String domainName){
        this.code = code;
        this.name = name;
        this.directory = directory;
        this.domainName = domainName;
    }
    
    public static PhotoGalleryEnum getFromCode(String code){
        for (PhotoGalleryEnum enum1 : PhotoGalleryEnum.values()) {
            if( code!=null && enum1.getCode().equals(code) ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static List<PhotoGalleryEnum> getDirectories(boolean directory){
        List<PhotoGalleryEnum> enums = new ArrayList<PhotoGalleryEnum>();
        for (PhotoGalleryEnum enum1 : PhotoGalleryEnum.values()) {
            if( directory == enum1.isDirectory() ) {
                enums.add(enum1);
            }
        }
        return enums; // default
    }
    
    /**
     * 顯示名稱 (取自enum.properties => [class name].[enum name])
     * @return 
     */
    public String getDisplayName(){
        String res = ResourceBundleUtils.getDisplayName(GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
        if( res==null ){
            res = name;
        }
        return res;
    } 
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
}

