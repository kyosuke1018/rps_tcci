/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.fc.util.ResourceBundleUtils;
import com.tcci.cm.model.global.GlobalConstant;
import java.util.ArrayList;
import java.util.List;

/**
 * 網站刊物類型
 * @author Peter.Pan    
 */
public enum PublicationEnum {
    NEWS("N", "最新消息", 20, DataTypeEnum.HTML, true, true, true, false),
    FIXEDPAGE("F", "固定網頁", 0, DataTypeEnum.HTML, true, true, true, false),
    TENDER("T", "標案", 0, DataTypeEnum.HTML, true, false, false, false),
    /*
    ARTICLE("A", "保種專案", 20, DataTypeEnum.HTML, true, true, true, true),
    REPORT("R", "媒體報導", 21, DataTypeEnum.HTML, true, true, true, false),
    PAPER("P", "相關論文", 22, DataTypeEnum.UPLOAD, true, true, true, false),
    EXPRESS("K", "KBCC出版品", 39, DataTypeEnum.HTML, true, true, true, true),
    HUNDREDS("H", "百種興盛", 43, DataTypeEnum.UPLOAD, true, false, false, true),
    //VISIT("V", "參訪記錄", 21, DataTypeEnum.UPLOAD, true, true, true, false),
    //TEACHING("T", "教材", 0, DataTypeEnum.UPLOAD, true, true, true, false),
    */
    IMAGE("I", "圖片", 0, null, false, false, false, false), // 插入圖片
    VIDEO("O", "影片", 0, null, false, false, false, false), // 插入影片
    DOC("D", "文件", 0, null, false, false, false, false), // 插入文件
    
    //NEWS("N", "最新消息", 0, null, false, false, false, false),
    TEMP("NA", "系統暫存", 0, null, false, false, false, false),// for 插入物件暫存關聯
    ;

    private String code;
    private String name;
    private long funcId;
    private DataTypeEnum defDataType;// 預設資料類型
    private Boolean richContent;// 是否有線上編輯 RichContent 格式
    private Boolean news;// 是否可加入[最新消息]
    private Boolean defNews;// 預設是否加入[最新消息]
    private Boolean hasCoverImg;// 有封面圖示
    
    // check type for restful input 
    public static boolean validDocType(String pubType){
        PublicationEnum publicationEnum = getFromCode(pubType);
        return (publicationEnum!=null && publicationEnum.funcId>0);
    }
    
    PublicationEnum(String code, String name, long funcId, DataTypeEnum defDataType
            , boolean richContent, boolean news, boolean defNews, boolean hasCoverImg){
        this.code = code;
        this.name = name;
        this.funcId = funcId;
        this.defDataType = defDataType;
        this.richContent = richContent;
        this.news = news;
        this.defNews = defNews;
        this.hasCoverImg = hasCoverImg;
    }
    
    public static List<PublicationEnum> findValidList(){
        List<PublicationEnum> list = new ArrayList<PublicationEnum>();
        for (PublicationEnum enum1 : PublicationEnum.values()) {
            if( validDocType(enum1.getCode()) ) {
                list.add(enum1);
            }
        }
        return list;
    }
    
    public static List<PublicationEnum> richContentList(){
        List<PublicationEnum> list = new ArrayList<PublicationEnum>();
        for (PublicationEnum enum1 : PublicationEnum.values()) {
            if( enum1.isRichContent() ) {
                list.add(enum1);
            }
        }
        return list;
    }
    
    public static PublicationEnum getFromCode(String code){
        for (PublicationEnum enum1 : PublicationEnum.values()) {
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

    public Boolean getHasCoverImg() {
        return hasCoverImg;
    }

    public void setHasCoverImg(Boolean hasCoverImg) {
        this.hasCoverImg = hasCoverImg;
    }

    public Boolean getDefNews() {
        return defNews;
    }

    public void setDefNews(Boolean defNews) {
        this.defNews = defNews;
    }

    public DataTypeEnum getDefDataType() {
        return defDataType;
    }

    public void setDefDataType(DataTypeEnum defDataType) {
        this.defDataType = defDataType;
    }

    public Boolean getNews() {
        return news;
    }

    public void setNews(Boolean news) {
        this.news = news;
    }

    public long getFuncId() {
        return funcId;
    }

    public void setFuncId(long funcId) {
        this.funcId = funcId;
    }

    public Boolean isRichContent() {
        return richContent;
    }

    public void setRichContent(Boolean richContent) {
        this.richContent = richContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
}

