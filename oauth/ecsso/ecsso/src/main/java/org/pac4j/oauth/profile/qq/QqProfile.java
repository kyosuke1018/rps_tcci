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
package org.pac4j.oauth.profile.qq;

import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.core.profile.Gender;
import org.pac4j.oauth.profile.OAuth20Profile;
import org.pac4j.oauth.profile.OAuthAttributesDefinitions;

/**
 * QQ OAuth profile
 *
 */
public class QqProfile extends OAuth20Profile {

    private static final long serialVersionUID = 991316880740957137L;

    /**
     * Return the openid of the user.
     *
     * @return the openid of the user
     */
    public String getOpenid() {
        return (String) getAttribute(QqAttributesDefinition.OPEN_ID);
    }

    /**
     * 返回用户在 QQ 空间的昵称
     *
     * @return 用户在 QQ 空间的昵称
     */
    public String getNickName() {
        return (String) getAttribute(QqAttributesDefinition.NICKNAME);
    }

    @Override
    public String getDisplayName() {
        return getNickName();
    }

    /**
     * 返回用户名
     *
     * @return 用户名
     */
    @Override
    public String getUsername() {
        return getNickName();
    }

    /**
     * 返回用户性别
     *
     * @return 用户性别
     */
    @Override
    public Gender getGender() {
        return (Gender) getAttribute(QqAttributesDefinition.GENDER);
    }

    /**
     * 返回国家
     *
     * @return 国家
     */
    public String getCountry() {
        return (String) getAttribute(QqAttributesDefinition.COUNTRY);
    }

    /**
     * 返回省
     *
     * @return 省
     */
    public String getProvince() {
        return (String) getAttribute(QqAttributesDefinition.PROVINCE);
    }

    /**
     * 返回市
     *
     * @return 市
     */
    public String getCity() {
        return (String) getAttribute(QqAttributesDefinition.CITY);
    }

    /**
     * 返回用户大小为 30×30 像素的头像 URL
     *
     * @return 用户大小为 30×30 像素的头像 URL
     */
    public String getFigureurl() {
        return (String) getAttribute(QqAttributesDefinition.FIGUREURL);
    }

    /**
     * 返回用户大小为 50×50 像素的头像 URL
     *
     * @return 用户大小为 50×50 像素的头像 URL
     */
    public String getFigureurl_1() {
        return (String) getAttribute(QqAttributesDefinition.FIGUREURL_1);
    }

    /**
     * 返回用户大小为 100×100 像素的头像 URL
     *
     * @return 用户大小为 100×100 像素的头像 URL
     */
    public String getFigureurl_2() {
        return (String) getAttribute(QqAttributesDefinition.FIGUREURL_2);
    }

    /**
     * 返回用户大小为 40×40 像素的 QQ 头像URL
     *
     * @return 用户大小为 40×40 像素的 QQ 头像URL
     */
    public String getFigureurlQq1() {
        return (String) getAttribute(QqAttributesDefinition.FIGUREURL_QQ_1);
    }

    /**
     * 返回用户大小为 100×100 像素的 QQ 头像URL
     *
     * @return 用户大小为 100×100 像素的 QQ 头像URL
     */
    public String getFigureurlQq2() {
        return (String) getAttribute(QqAttributesDefinition.FIGUREURL_QQ_2);
    }

    /**
     * 返回用户是否为黄钻用户
     *
     * @return 用户是否为黄钻用户（0：不是；1：是）
     */
    public String getIsYellowVip() {
        return (String) getAttribute(QqAttributesDefinition.IS_YELLOW_VIP);
    }

    /**
     * 返回用户是否为黄钻用户
     *
     * @return 用户是否为黄钻用户（0：不是；1：是）
     */
    public String getVip() {
        return (String) getAttribute(QqAttributesDefinition.VIP);
    }

    /**
     * 返回用户是否为黄钻用户
     *
     * @return 用户是否为黄钻用户（0：不是；1：是）
     */
    public String getYellowVipLevel() {
        return (String) getAttribute(QqAttributesDefinition.YELLO_VIP_LEVEL);
    }

    /**
     * 返回用户是否为黄钻用户
     *
     * @return 用户是否为黄钻用户（0：不是；1：是）
     */
    public String getLevel() {
        return (String) getAttribute(QqAttributesDefinition.LEVEL);
    }

    /**
     * 返回用户资料属性属性定义
     *
     * @return 用户资料属性属性定义
     */
    @Override
    protected AttributesDefinition getAttributesDefinition() {
        return OAuthAttributesDefinitions.qqDefinition;
    }

}
