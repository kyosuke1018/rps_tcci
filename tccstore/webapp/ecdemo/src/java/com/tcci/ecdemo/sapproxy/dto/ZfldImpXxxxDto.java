package com.tcci.ecdemo.sapproxy.dto;

/**
 * 對應SAP的結構 "ZFLD_IMP_xxxx" 。
 * @author Neo.Fu
 */
public class ZfldImpXxxxDto {
    /**
     * 表格名稱。
     */
    private String tableName;
    /**
     * 
     */
    private String scope;
    /**
     * 範圍符號 - 接受或排除。
     * <ul>
     * <li>I: 接受</li>
     * <li>E: 排除</li>
     * </ul>
     */    
    private String opera;
    /**
     * 關係運算子 。
     * <ul>
     * <li>EQ: =, 等於</li>
     * <li>NE: <>, 不等於 </li>
     * <li>LT: <, 小於</li>
     * <li>GT: >, 大於</li>
     * <li>LE: <=, 小於或等於 </li>
     * <li>GE: >=, 大於或等於 </li>
     * <li>BT: 範圍</li>
     * <li>NB: 範圍以外</li>
     * <li>Cp: 模糊比對</li>
     * </ul>
     */    
    private String low;
    /**
     * 下限值 
     */    
    private String high;
    /**
     * 上限值 
     */    
    public ZfldImpXxxxDto() {
    }

    public ZfldImpXxxxDto(
            String tableName,
            String scope,
            String opera,
            String low,
            String high) {
        this.tableName = tableName;
        this.scope = scope;
        this.opera = opera;
        this.low = low;
        this.high = high;
    }

    public String getOpera() {
        return opera;
    }

    public void setOpera(String opera) {
        this.opera = opera;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }
}
