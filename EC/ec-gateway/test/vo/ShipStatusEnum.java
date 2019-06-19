/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vo;

public enum ShipStatusEnum {

    A("A", "未出貨"),
    B("B", "已出貨"),
    C("C", "部分出貨"),
    D("D", "已到貨");
    
    private String code;
    private String name;

    ShipStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static ShipStatusEnum fromCode(String code) {
        if (code != null) {
            for (ShipStatusEnum thisEnum : ShipStatusEnum.values()) {
                if (code.equals(thisEnum.getCode())) {
                    return thisEnum;
                }
            }
        }
        return null;
    }    
}
