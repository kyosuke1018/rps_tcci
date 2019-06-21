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
public enum VideoLibraryEnum {
    CUSTOM("C", "自訂影片庫", true),
    DOC("D", "文章穿插影片", true),
    VIDEO("V", "影片", false)
    ;

    private String code;
    private String name;
    private boolean directory;
    
    VideoLibraryEnum(String code, String name, boolean directory){
        this.code = code;
        this.name = name;
        this.directory = directory;
    }
    
    public static VideoLibraryEnum getFromCode(String code){
        for (VideoLibraryEnum enum1 : VideoLibraryEnum.values()) {
            if( code!=null && enum1.getCode().equals(code) ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static List<VideoLibraryEnum> getDirectories(boolean directory){
        List<VideoLibraryEnum> enums = new ArrayList<VideoLibraryEnum>();
        for (VideoLibraryEnum enum1 : VideoLibraryEnum.values()) {
            if( directory == enum1.isDirectory() ) {
                enums.add(enum1);
            }
        }
        return enums; // default
    }
    
    public static List<String> getDirectoryCodes(boolean directory){
        List<String> codes = new ArrayList<String>();
        for (VideoLibraryEnum enum1 : VideoLibraryEnum.values()) {
            if( directory == enum1.isDirectory() ) {
                codes.add(enum1.getCode());
            }
        }
        return codes;
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

