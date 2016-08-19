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
package org.pac4j.oauth.client;

import org.pac4j.core.context.WebContext;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.OAuthAttributesDefinitions;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;

import com.fasterxml.jackson.databind.JsonNode;
import java.nio.charset.Charset;
import java.util.List;
import org.pac4j.core.exception.HttpCommunicationException;
import static org.pac4j.oauth.client.BaseOAuth20Client.logger;
import org.pac4j.oauth.profile.qq.QqAttributesDefinition;
import org.pac4j.oauth.profile.qq.QqProfile;
import org.scribe.builder.api.QqApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.ProxyOAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.oauth.QqOAuth20ServiceImpl;

/**
 * <p>
 * This class is the OAuth client to authenticate users in Weibo.</p>
 * <p>
 * The <i>scope</i> can be defined to require specific permissions from the user
 * by using the {@link #setScope(String)} method. By default, the <i>scope</i>
 * is : <code>user</code>.</p>
 * <p>
 * It returns a {@link org.pac4j.oauth.profile.weibo.QqProfile}.</p>
 * <p>
 * More information at http://wiki.open.qq.com/wiki/v3/user/get_info</p>
 *
 * @author Neo.Fu
 * @see org.pac4j.oauth.profile.weibo.QqProfile
 * @since 1.0.0
 */
public class QqClient extends BaseOAuth20Client<QqProfile> {

    public final static String DEFAULT_SCOPE = "user";
    private Token accessToken = null;
    protected String scope = DEFAULT_SCOPE;

    public QqClient() {
    }

    public QqClient(final String key, final String secret) {
        setKey(key);
        setSecret(secret);
    }

    @Override
    protected QqClient newClient() {
        QqClient client = new QqClient();
        client.setScope(this.scope);
        return client;
    }

    @Override
    protected void internalInit() {
        super.internalInit();
        this.service = new QqOAuth20ServiceImpl(new QqApi(), new OAuthConfig(this.key, this.secret,
                this.callbackUrl,
                SignatureType.Header, this.scope,
                null), this.connectTimeout, this.readTimeout, this.proxyHost, this.proxyPort);
    }

    @Override
    protected String getProfileUrl(final Token accessToken) {
        this.accessToken = accessToken;
        return "https://graph.qq.com/oauth2.0/me";
    }

    @Override
    protected QqProfile extractUserProfile(String body) {
        final QqProfile profile = new QqProfile();
        JsonNode user = JsonHelper.getFirstNode(body.substring("callback( ".length(), body.length() - 2));
        logger.debug("json={}", user);
        if (user != null) {
            String openid = JsonHelper.get(user, "openid").toString();
            String userdataUrl = "https://graph.qq.com/user/get_user_info?openid=" + openid + "&appid=" + this.key;

            JsonNode userdataBody = JsonHelper.getFirstNode(sendRequestForData(this.accessToken, userdataUrl));

            List<String> principalAttributes = OAuthAttributesDefinitions.qqDefinition
                    .getPrincipalAttributes();
            profile.setId(openid);
            for (final String name : principalAttributes) {
                logger.debug("add {} = {}", new Object[]{name, JsonHelper.get(userdataBody, name)});
                Object value = JsonHelper.get(userdataBody, name);
                profile.addAttribute(name, value);
            }
        }

        return profile;
    }

    @Override
    protected boolean requiresStateParameter() {
        return false;
    }

    @Override
    protected boolean hasBeenCancelled(final WebContext context) {
        return false;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(final String scope) {
        this.scope = scope;
    }
}
