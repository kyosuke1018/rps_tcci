/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 
 * (the "License"); you may not use this file except in compliance with the License. You may obtain 
 * a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 * =================================================================================================
 * 
 * This software consists of voluntary contributions made by many individuals on behalf of the
 * Apache Software Foundation. For more information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 * 
 * +------------------------------------------------------------------------------------------------+
 * | License: http://oauth-client.buession.com.cn/LICENSE 											|
 * | Author: Yong.Teng <webmaster@buession.com> 													|
 * | Copyright @ 2013-2014 Buession.com Inc.														|
 * +------------------------------------------------------------------------------------------------+
 */
package org.pac4j.oauth.profile.qq;

import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.core.profile.converter.Converters;
import org.pac4j.core.profile.converter.GenderConverter;

/**
 * QQ OAuth attributes definition
 *
 */
public class QqAttributesDefinition extends AttributesDefinition {

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
    
    private final static GenderConverter genderConverter = new GenderConverter("男", "女");

    public QqAttributesDefinition() {
        addAttribute(OPEN_ID, Converters.stringConverter);
        addAttribute(NICKNAME, Converters.stringConverter);
        addAttribute(GENDER, genderConverter);
        addAttribute(COUNTRY, Converters.stringConverter);
        addAttribute(PROVINCE, Converters.stringConverter);
        addAttribute(CITY, Converters.stringConverter);
        addAttribute(FIGUREURL, Converters.stringConverter);
        addAttribute(FIGUREURL_1, Converters.stringConverter);
        addAttribute(FIGUREURL_2, Converters.stringConverter);
        addAttribute(FIGUREURL_QQ_1, Converters.stringConverter);
        addAttribute(FIGUREURL_QQ_2, Converters.stringConverter);
        addAttribute(IS_YELLOW_VIP, Converters.stringConverter);
        addAttribute(VIP, Converters.stringConverter);
        addAttribute(YELLO_VIP_LEVEL, Converters.stringConverter);
        addAttribute(LEVEL, Converters.stringConverter);
    }

}
