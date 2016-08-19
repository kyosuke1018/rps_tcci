/*
  Copyright 2012 - 2015 pac4j organization

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.pac4j.oauth.profile.weibo;

import java.sql.Date;
import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.core.profile.Gender;
import org.pac4j.oauth.profile.OAuth20Profile;
import org.pac4j.oauth.profile.OAuthAttributesDefinitions;

/**
 * Sina Weibo OAuth profile
 *
 */
public class WeiboProfile extends OAuth20Profile {

    private static final long serialVersionUID = -3526611200283762984L;

    @Override
    public String getUsername() {
        return getScreenName();
    }

    /**
     * 返回用户昵称
     *
     * @return 用户昵称
     */
    public String getScreenName() {
        return (String) getAttribute(WeiboAttributesDefinition.SCREEN_NAME);
    }

    /**
     * 返回用户友好显示名称
     *
     * @return 用户友好显示名称
     */
    public String getName() {
        return (String) getAttribute(WeiboAttributesDefinition.NAME);
    }

    /**
     * 返回用户所在省级ID
     *
     * @return 用户所在省级ID
     */
    public String getProvince() {
        return (String) getAttribute(WeiboAttributesDefinition.PROVINCE);
    }

    /**
     * 返回用户所在城市ID
     *
     * @return 用户所在城市ID
     */
    public String getCity() {
        return (String) getAttribute(WeiboAttributesDefinition.CITY);
    }

    /**
     * 返回用户所在地
     *
     * @return 用户所在地
     */
    @Override
    public String getLocation() {
        return (String) getAttribute(WeiboAttributesDefinition.LOCATION);
    }

    /**
     * 返回用户个人描述
     *
     * @return 用户个人描述
     */
    public String getDescription() {
        return (String) getAttribute(WeiboAttributesDefinition.DESCRIPTION);
    }

    /**
     * 返回用户博客地址
     *
     * @return 用户博客地址
     */
    public String getUrl() {
        return (String) getAttribute(WeiboAttributesDefinition.URL);
    }

    /**
     * 返回用户头像地址（中图），50×50像素址
     *
     * @return 用户头像地址（中图），50×50像素
     */
    public String getProfileImageUrl() {
        return (String) getAttribute(WeiboAttributesDefinition.PROFILE_IMAGE_URL);
    }

    /**
     * 返回用户的微博统一URL地址
     *
     * @return 用户的微博统一URL地址
     */
    @Override
    public String getProfileUrl() {
        return (String) getAttribute(WeiboAttributesDefinition.PROFILE_URL);
    }

    /**
     * 返回用户的个性化域名
     *
     * @return 用户的个性化域名
     */
    public String getDomain() {
        return (String) getAttribute(WeiboAttributesDefinition.DOMAIN);
    }

    /**
     * 返回用户微号
     *
     * @return 用户微号
     */
    public Object getWeiHao() {
        return getAttribute(WeiboAttributesDefinition.WEIHAO);
    }

    /**
     * 返回用户性别
     *
     * @return 用户性别
     */
    @Override
    public Gender getGender() {
        return (Gender) getAttribute(WeiboAttributesDefinition.GENDER);
    }

    /**
     * 返回粉丝数
     *
     * @return 粉丝数
     */
    public int getFollowersCount() {
        return (Integer) getAttribute(WeiboAttributesDefinition.FOLLOWERS_COUNT);
    }

    /**
     * 返回关注数
     *
     * @return 关注数
     */
    public int getFriendsCount() {
        return (Integer) getAttribute(WeiboAttributesDefinition.FRIENDS_COUNT);
    }

    /**
     * 返回微博数
     *
     * @return 微博数
     */
    public int getStatusesCount() {
        return (Integer) getAttribute(WeiboAttributesDefinition.STATUSES_COUNT);
    }

    /**
     * 返回用户收藏数
     *
     * @return 用户收藏数
     */
    public int getFavouritesCount() {
        return (Integer) getAttribute(WeiboAttributesDefinition.FAVOURITES_COUNT);
    }

    /**
     * 返回用户用户创建（注册）时间
     *
     * @return 用户用户创建（注册）时间
     */
    public Date getCreatedAt() {
        return (Date) getAttribute(WeiboAttributesDefinition.CREATED_AT);
    }

    /**
     * 尚未支援
     *
     * @deprecated
     * @return 尚未支援
     */
    public Boolean getFollowing() {
        return (Boolean) getAttribute(WeiboAttributesDefinition.FOLLOWING);
    }

    /**
     * 返回是否允许所有人给我发私信，true：是，false：否
     *
     * @return 是否允许所有人给我发私信，true：是，false：否
     */
    public boolean getIsAllowAllActMsg() {
        return (Boolean) getAttribute(WeiboAttributesDefinition.ALLOW_ALL_ACT_MSG);
    }

    /**
     * 返回是否允许标识用户的地理位置，true：是，false：否
     *
     * @return 是否允许标识用户的地理位置，true：是，false：否
     */
    public boolean getIsGeoEnabled() {
        return (Boolean) getAttribute(WeiboAttributesDefinition.GEO_ENABLED);
    }

    /**
     * 返回是否是微博认证用户，即加V用户，true：是，false：否
     *
     * @return 是否是微博认证用户，即加V用户，true：是，false：否
     */
    public boolean getIsVerified() {
        return (Boolean) getAttribute(WeiboAttributesDefinition.VERIFIED);
    }

    /**
     * Return the verified type of the user.
     *
     * @deprecated
     * @return the verified type of the user
     */
    public String getVerifiedType() {
        return (String) getAttribute(WeiboAttributesDefinition.VERIFIED_TYPE);
    }

    /**
     * 返回用户备注信息，只有在查询用户关系时才返回此字段
     *
     * @return 用户备注信息，只有在查询用户关系时才返回此字段
     */
    public String getRemark() {
        return (String) getAttribute(WeiboAttributesDefinition.REMARK);
    }

    /**
     * 返回用户的最近一条微博信息字段
     *
     * @return 用户的最近一条微博信息字段
     */
    public Object getStatus() {
        return (Object) getAttribute(WeiboAttributesDefinition.STATUS);
    }

    /**
     * 返回是否允许所有人对我的微博进行评论，true：是，false：否
     *
     * @return 是否允许所有人对我的微博进行评论，true：是，false：否
     */
    public boolean getIsAllowAllComment() {
        return (Boolean) getAttribute(WeiboAttributesDefinition.ALLOW_ALL_COMMENT);
    }

    /**
     * 返回用户头像地址（大图），180×180像素
     *
     * @return 用户头像地址（大图），180×180像素
     */
    public String getAvatarLarge() {
        return (String) getAttribute(WeiboAttributesDefinition.AVATAR_LARGE);
    }

    /**
     * 返回用户头像地址（高清），高清头像原图
     *
     * @return 用户头像地址（高清），高清头像原图
     */
    public String getAvatarHd() {
        return (String) getAttribute(WeiboAttributesDefinition.AVATAR_HD);
    }

    /**
     * 返回认证原因
     *
     * @return 认证原因
     */
    public String getVerifiedReason() {
        return (String) getAttribute(WeiboAttributesDefinition.VERIFIED_REASON);
    }

    /**
     * 返回该用户是否关注当前登录用户，true：是，false：否
     *
     * @return 该用户是否关注当前登录用户，true：是，false：否
     */
    public boolean getIsFollowedMe() {
        return (Boolean) getAttribute(WeiboAttributesDefinition.FOLLOW_ME);
    }

    /**
     * 返回用户的在线状态，0：不在线、1：在线
     *
     * @return 用户的在线状态，0：不在线、1：在线
     */
    public int getIsOnline() {
        return (Integer) getAttribute(WeiboAttributesDefinition.ONLINE_STATUS);
    }

    /**
     * 返回用户的互粉数
     *
     * @return 用户的互粉数
     */
    public int getBiFollowersCount() {
        return (Integer) getAttribute(WeiboAttributesDefinition.BI_FOLLOWERS_COUNT);
    }

    /**
     * 返回用户当前的语言版本
     *
     * @return 用户当前的语言版本
     */
    public String getLang() {
        return (String) getAttribute(WeiboAttributesDefinition.LANG);
    }

    /**
     * 返回用户资料属性属性定义
     *
     * @return 用户资料属性属性定义
     */
    @Override
    protected AttributesDefinition getAttributesDefinition() {
        return OAuthAttributesDefinitions.weiboDefinition;
    }

}
