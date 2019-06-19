/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.enums;

public enum MsgTypeEnum {

    A("A", "系統意見留言"),
    S("S", "指定商店留言"),
    P("P", "指定商品留言");
    
    private String code;
    private String name;

    MsgTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static MsgTypeEnum fromCode(String code) {
        if (code != null) {
            for (MsgTypeEnum thisEnum : MsgTypeEnum.values()) {
                if (code.equals(thisEnum.getCode())) {
                    return thisEnum;
                }
            }
        }
        return null;
    }    
    
    public static MsgTypeEnum getFromCode(String code){
        for (MsgTypeEnum enum1 : MsgTypeEnum.values()) {
            if( code!=null && enum1.getCode().equals(code) ) {
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
//        String res = ResourceBundleUtils.getDisplayName(GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
//        if( res==null ){
//            res = name;
//        }
//        return res;
        return name;
    } 
}
