/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.Locale;

/**
 *
 * @author peter.pan
 */
public enum FileEnum {
    // image
    // admin
//    AD("AD", "廣告圖示", EcAd.class.getName(), GlobalConstant.DIR_ADMIN_IMG, false),
    MEMBER_PIC("MP", "會員照片", "MP", GlobalConstant.DIR_ADMIN_IMG, false),
//    TCC_PRD_PIC("TP", "台泥商品照片", "TP", GlobalConstant.DIR_ADMIN_IMG, false),// 共用圖片
    // store
//    PRD_PIC("PP", "商品照片", EcProduct.class.getName(), GlobalConstant.DIR_STORE_IMG, true),
//    PRD_DETAIL("PD", "商品詳細介紹插圖", EcPrdDetail.class.getName(), GlobalConstant.DIR_STORE_IMG, true),
//    STORE_LOGO("SL", "商店 Logo", "SL", GlobalConstant.DIR_STORE_IMG, true),
//    STORE_BANNER("SB", "商店 Banner", "SB", GlobalConstant.DIR_STORE_IMG, true),

    // file
    // import
    IMP_VENDER_APPLY("IMP_VENDER_APPLY", "綁定供應商申請文件", "IMP_VENDER_APPLY", GlobalConstant.DIR_IMPORT, false),
    IMP_PRD("IMP_PRD", "商品匯入檔", "IMP_PRD", GlobalConstant.DIR_IMPORT, false),
    IMP_TCC_DEALER("IMP_DEALER", "台泥經銷商匯入檔", "IMP_DEALER", GlobalConstant.DIR_IMPORT, false),
    IMP_TCC_DS("IMP_DS", "台泥經銷商下游客戶匯入檔", "IMP_DS", GlobalConstant.DIR_IMPORT, false),
    // export
    EXP_PRD("EXP_PD", "台泥經銷商下游客戶匯入檔", "EXP_PD", GlobalConstant.DIR_EXPORT, false),
    ;

    private String code;
    private String name;
    private String primaryType;
    private String rootDir;
    private boolean byStore;
    
    FileEnum(String code, String name, String primaryType, String rootDir, boolean byStore){
        this.code = code;
        this.name = name;
        this.primaryType = primaryType;
        this.rootDir = rootDir;
        this.byStore = byStore;
    }
    
    public static FileEnum getFromCode(String code){
        for (FileEnum enum1 : FileEnum.values()) {
            if( code!=null && enum1.getCode().equals(code) ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static FileEnum getFromPrimaryType(String primaryType){
        for (FileEnum enum1 : FileEnum.values()) {
            if( primaryType!=null && enum1.getPrimaryType().equals(primaryType) ) {
                return enum1;
            }
        }
        return null; // default
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
    public String getDisplayName(Locale locale){
        String res = ResourceBundleUtils.getDisplayName(locale, GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
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

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public boolean isByStore() {
        return byStore;
    }

    public void setByStore(boolean byStore) {
        this.byStore = byStore;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
}

