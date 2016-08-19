package com.tcci.cas.oauth.profile;

/**
 * QQ OAuth attributes definition
 *
 */
public class QqAttributesDefinition {

    /**
     * 用户QQ号码转化得到的ID
     */
    public final static String OPEN_ID = "openid";

    /**
     * 用户在QQ空间的昵称。
     */
    public final static String NICKNAME = "nickname";

    /**
     * 性别。 如果获取不到则默认返回"男"
     */
    public final static String GENDER = "gender";

    /**
     * 国家（当pf=qzone、pengyou或qplus时返回）。
     */
    public final static String COUNTRY = "country";

    /**
     * 省（当pf=qzone、pengyou或qplus时返回）。
     */
    public final static String PROVINCE = "province";

    /**
     * 市（当pf=qzone、pengyou或qplus时返回）。
     */
    public final static String CITY = "city";

    /**
     * 大小為 30x30 像素的头像 URL
     */
    public final static String FIGUREURL = "figureurl";

    /**
     * 大小為 50x50 像素的頭像 URL
     */
    public final static String FIGUREURL_1 = "figureurl_1";

    /**
     * 大小為 100x100 像素的頭像 URL
     */
    public final static String FIGUREURL_2 = "figureurl_2";

    /**
     * 大小为 40×40 像素的 QQ 头像 URL
     */
    public final static String FIGUREURL_QQ_1 = "figureurl_qq_1";

    /**
     * 大小为 100×100 像素的 QQ头像URL。需要注意，不是所有的用户都拥有 QQ 的100x100 的头像
     */
    public final static String FIGUREURL_QQ_2 = "figureurl_qq_2";

    /**
     * 标识用户是否为黄钻用户（0：不是；1：是）。
     */
    public final static String IS_YELLOW_VIP = "is_yellow_vip";

    /**
     * 标识用户是否为黄钻用户（0：不是；1：是）
     */
    public final static String VIP = "vip";

    /**
     * 黄钻等级
     */
    public final static String YELLO_VIP_LEVEL = "yellow_vip_level";
    
    /**
     * 黄钻等级
     */
    public final static String LEVEL = "level";
    
    /**
     * 标识是否为年费黄钻用户（0：不是；+1：是）
     */
    public final static String IS_YELLOW_YEAR_VIP="is_yellow_year_vip";
}
