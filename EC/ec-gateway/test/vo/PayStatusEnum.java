/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vo;

public enum PayStatusEnum {

    A("A", "未付款"),
    B("B", "已收款"),
    C("C", "已收到部分款項");
    
    private String code;
    private String name;

    PayStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static PayStatusEnum fromCode(String code) {
        if (code != null) {
            for (PayStatusEnum thisEnum : PayStatusEnum.values()) {
                if (code.equals(thisEnum.getCode())) {
                    return thisEnum;
                }
            }
        }
        return null;
    }    
}
