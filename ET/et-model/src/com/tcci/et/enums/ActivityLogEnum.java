/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 * 
 * @author Peter
 */
public enum ActivityLogEnum {
    //A
    A_MEMBER("A11", "新增會員", "EC_MEMBER"),
    U_MEMBER("A11", "修改會員", "EC_MEMBER"),
    RESET_PWD("A11", "重設密碼", "EC_MEMBER"),
    
    U_VENDER("A21", "修改供應商分類", "ET_VENDER_CATEGORY"),
    
    A_TENDER("A31", "新增標案", "ET_TENDER"),
    U_TENDER("A31", "修改標案", "ET_TENDER"),
    D_TENDER("A31", "刪除標案", "ET_TENDER"),
    D_TENDER_FILE("A31", "刪除標案附件", "ET_TENDER"),
    
    // B.網站內容管理
    A_DOC("B11", "新增文章", "KB_PUBLICATION"),
    U_DOC("B12", "修改文章", "KB_PUBLICATION"),
    D_DOC("B13", "刪除文章", "KB_PUBLICATION"),
    D_DOC_FILE("B14", "刪除文章附件", "KB_PUBLICATION"),
    D_DOC_COVER("B15", "刪除文章封面", "KB_PUBLICATION"),
    
    A_PHOTO("B21", "新增圖片", "KB_PHOTO_GALLERY"),
    U_PHOTO("B22", "修改圖片", "KB_PHOTO_GALLERY"),
    D_PHOTO("B23", "刪除圖片", "KB_PHOTO_GALLERY"),

    A_PHOTO_FOLDER("B24", "新增相簿", "KB_PHOTO_GALLERY"),
    U_PHOTO_FOLDER("B25", "修改相簿", "KB_PHOTO_GALLERY"),
    D_PHOTO_FOLDER("B26", "刪除相簿", "KB_PHOTO_GALLERY"),
    
    A_VIDEO("B31", "新增影片", "KB_VIDEO"),
    U_VIDEO("B32", "修改影片", "KB_VIDEO"),
    D_VIDEO("B33", "刪除影片", "KB_VIDEO"),
    
    A_VIDEO_FOLDER("B34", "新增影片夾", "KB_VIDEO"),
    U_VIDEO_FOLDER("B35", "修改影片夾", "KB_VIDEO"),
    D_VIDEO_FOLDER("B36", "刪除影片夾", "KB_VIDEO"),
    
    // S. 系統管理
    A_USER("S11", "新增使用者", "TC_USER"),
    U_USER("S12", "修改使用者", "TC_USER"),
    D_USER("S13", "刪除使用者", "TC_USER"),

    A_OPTION("S21", "新增系統選項", "EC_OPTION"),
    U_OPTION("S22", "修改系統選項", "EC_OPTION"),
    D_OPTION("S23", "刪除系統選項", "EC_OPTION"),
    
    U_FUNC_PERMISSION("S2", "變更功能權限", "TC_USERGROUP"),
    U_PLANT_PERMISSION("S3", "變更廠別權限", "CM_USERFACTORY"),
    U_USER_IMPORT("S5", "匯入使用者", "TC_USER"),
    U_MEMBER_IMPORT("S7", "匯入會員", "EC_MEMBER"),
    ;
    
    private String code;
    private String name;
    private String table;// 主要變更 Table
    
    ActivityLogEnum(String code, String name, String table){
        this.code = code;
        this.name = name;
        this.table = table;
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
    
    /**
     * 依代碼取得 ActivityLogEnum
     * @param code
     * @return 
     */
    public static ActivityLogEnum getFromCode(String code){
        for (ActivityLogEnum enum1 : ActivityLogEnum.values()) {
            if( code!=null && enum1.getCode().equals(code) ) {
                return enum1;
            }
        }
        return null; // default
    }

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>

}