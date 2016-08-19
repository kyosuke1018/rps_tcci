package com.tcci.cas.oauth.profile;

/**
 * 新浪微博用户信息属性
 *
 */
public class WeiboAttributesDefinition {

    /**
     * 用户昵称
     */
    public final static String SCREEN_NAME = "screen_name";

    /**
     * 用户友好显示名称
     */
    public final static String NAME = "name";

    /**
     * 用户所在省级ID
     */
    public final static String PROVINCE = "province";

    /**
     * 用户所在城市ID
     */
    public final static String CITY = "city";

    /**
     * 用户所在地
     */
    public final static String LOCATION = "location";

    /**
     * 用户个人描述
     */
    public final static String DESCRIPTION = "description";

    /**
     * 用户博客地址
     */
    public final static String URL = "url";

    /**
     * 用户头像地址（中图），50×50像素
     */
    public final static String PROFILE_IMAGE_URL = "profile_image_url";

    /**
     * 用户的微博统一URL地址
     */
    public final static String PROFILE_URL = "profile_url";

    /**
     * 用户的个性化域名
     */
    public final static String DOMAIN = "domain";

    /**
     * 用户微号
     */
    public final static String WEIHAO = "weihao";

    /**
     * 用户性别
     */
    public final static String GENDER = "gender";

    /**
     * 粉丝数
     */
    public final static String FOLLOWERS_COUNT = "followers_count";

    /**
     * 关注数
     */
    public final static String FRIENDS_COUNT = "friends_count";

    /**
     * 微博数
     */
    public final static String STATUSES_COUNT = "statuses_count";

    /**
     * 收藏数
     */
    public final static String FAVOURITES_COUNT = "favourites_count";

    /**
     * 用户创建（注册）时间
     */
    public final static String CREATED_AT = "created_at";

    /**
     * 暂未支持
     *
     * @deprecated
     */
    public final static String FOLLOWING = "following";

    /**
     * 是否允许所有人给我发私信，true：是，false：否
     */
    public final static String ALLOW_ALL_ACT_MSG = "allow_all_act_msg";

    /**
     * 是否允许标识用户的地理位置，true：是，false：否
     */
    public final static String GEO_ENABLED = "geo_enabled";

    /**
     * 是否是微博认证用户，即加V用户，true：是，false：否
     */
    public final static String VERIFIED = "verified";

    /**
     * 暂未支持
     *
     * @deprecated
     */
    public final static String VERIFIED_TYPE = "verified_type";

    /**
     * 用户备注信息，只有在查询用户关系时才返回此字段
     */
    public final static String REMARK = "remark";

    /**
     * 用户的最近一条微博信息字段
     */
    public final static String STATUS = "status";

    /**
     * 是否允许所有人对我的微博进行评论，true：是，false：否
     */
    public final static String ALLOW_ALL_COMMENT = "allow_all_comment";

    /**
     * 用户头像地址（大图），180×180像素
     */
    public final static String AVATAR_LARGE = "avatar_large";

    /**
     * 用户头像地址（高清），高清头像原图
     */
    public final static String AVATAR_HD = "avatar_hd";

    /**
     * 认证原因
     */
    public final static String VERIFIED_REASON = "verified_reason";

    /**
     * 该用户是否关注当前登录用户，true：是，false：否
     */
    public final static String FOLLOW_ME = "follow_me";

    /**
     * 用户的在线状态，0：不在线、1：在线
     */
    public final static String ONLINE_STATUS = "online_status";

    /**
     * 用户的互粉数
     */
    public final static String BI_FOLLOWERS_COUNT = "bi_followers_count";

    /**
     * 用户当前的语言版本
     */
    public final static String LANG = "lang";

}
