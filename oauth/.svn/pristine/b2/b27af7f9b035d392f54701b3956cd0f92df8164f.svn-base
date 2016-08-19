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
package org.scribe.oauth;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.ProxyOAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

/**
 * QQ Oauth 2.0 Service
 *
 */
public class QqOAuth20ServiceImpl extends ProxyOAuth20ServiceImpl {

    /**
     * @param api OAuth 2.0 protocol Api
     * @param config OAuth config
     * @param proxyHost 代理主机地址
     * @param proxyPort 代理主机地址端口
     */
    public QqOAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config, int connectTimeout, int readTimeout, String proxyHost,
            int proxyPort) {
        super(api, config,  connectTimeout, readTimeout, proxyHost, proxyPort);
    }

    /**
     * 获取 Access Token
     *
     * @param requestToken Request Token
     * @param verifier Verifier code
     */
    @Override
    public Token getAccessToken(final Token requestToken, final Verifier verifier) {
        // PATCH : + grant_type parameter
        String getAccessTokenUrl = api.getAccessTokenEndpoint() + "?grant_type=authorization_code";
        final OAuthRequest request = new ProxyOAuthRequest(api.getAccessTokenVerb(),
                getAccessTokenUrl, 3000, 3000, proxyHost, proxyPort);

        request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
        request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());

        if (config.hasScope() == true) {
            request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
        }

        final Response response = request.send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

}
