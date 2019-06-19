/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vo;

public enum PaymentTypeEnum {

    A("A", "付款後出貨"),
//    B("B", "貨到付款"),
//    C("C", "分期付款"),//部分付款後出貨
    D("D", "授信");
    
    private String code;
    private String name;

    PaymentTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static PaymentTypeEnum fromCode(String code) {
        if (code != null) {
            for (PaymentTypeEnum thisEnum : PaymentTypeEnum.values()) {
                if (code.equals(thisEnum.getCode())) {
                    return thisEnum;
                }
            }
        }
        return null;
    }    
}
