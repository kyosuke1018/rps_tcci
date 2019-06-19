/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 * 系統通知類別
 * @author peter.pan
 */
public enum NotifyTypeEnum {
    CONTRACT_APPLY("1", "◎統計申請中的合約筆數：(通知有此廠權限的總處窗口)")
    ,CONTRACT_REJECT("2", "◎統計已駁回的合約筆數：(通知原合約管理人員)")
    ,CONTRACT_DEL("3", "◎統計被刪除的已駁回合約筆數：(通知有此廠權限的總處窗口)")
    ,PO_APPLY("4", "◎統計需控管SAP採購單，待轉合約筆數：(通知有此廠權限的合約管理人員)")
    ,PO_FINISH_CHECK("5", "◎統計請(付)款作業，待[經辦確認]的採購單筆數：(通知有此廠權限的請購經辦)")
    ,PO_PAY_CHECK("6", "◎統計請(付)款作業，待[主管確認]的採購單筆數：(通知有此廠權限的請購主管)")
    ,PO_FINISH_TODOLIST("7", "◎請(付)款作業，待[經辦確認]的採購單：(通知有此廠權限的請購經辦)")
    ,PO_PAY_TODOLIST("8", "◎請(付)款作業，待[主管確認]的採購單：(通知有此廠權限的請購主管)")
    ;
    
    private String code;
    private String name;
    
    NotifyTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
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
    
    public static NotifyTypeEnum getFromCode(String code){
        for (NotifyTypeEnum enum1 : NotifyTypeEnum.values()) {
            if( enum1.getCode().equals(code) ) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>

}

