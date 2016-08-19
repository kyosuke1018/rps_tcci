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
import org.scribe.oauth.ProxyOAuth20ServiceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import org.pac4j.core.exception.HttpCommunicationException;
import static org.pac4j.oauth.client.BaseOAuthClient.logger;
import org.pac4j.oauth.profile.weibo.WeiboAttributesDefinition;
import org.pac4j.oauth.profile.weibo.WeiboProfile;
import org.scribe.builder.api.SinaWeiboApi20;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

/**
 * <p>
 * This class is the OAuth client to authenticate users in Weibo.</p>
 * <p>
 * The <i>scope</i> can be defined to require specific permissions from the user
 * by using the {@link #setScope(String)} method. By default, the <i>scope</i>
 * is : <code>user</code>.</p>
 * <p>
 * It returns a {@link org.pac4j.oauth.profile.weibo.WeiboProfile}.</p>
 * <p>
 * More information at http://open.weibo.com/wiki/2/users/show</p>
 *
 * @author Neo.Fu
 * @see org.pac4j.oauth.profile.weibo.WeiboProfile
 * @since 1.0.0
 */
public class WeiboClient extends BaseOAuth20Client<WeiboProfile> {

    public final static String DEFAULT_SCOPE = "user";
    private Token accessToken = null;
    protected String scope = DEFAULT_SCOPE;

    public WeiboClient() {
    }

    public WeiboClient(final String key, final String secret) {
        setKey(key);
        setSecret(secret);
    }

    @Override
    protected WeiboClient newClient() {
        WeiboClient client = new WeiboClient();
        client.setScope(this.scope);
        return client;
    }

    @Override
    protected void internalInit() {
        super.internalInit();
        this.service = new ProxyOAuth20ServiceImpl(new SinaWeiboApi20(), new OAuthConfig(this.key, this.secret,
                this.callbackUrl,
                SignatureType.Header, this.scope,
                null), this.connectTimeout,
                this.readTimeout, this.proxyHost, this.proxyPort);
    }

    @Override
    protected String getProfileUrl(final Token accessToken) {
        this.accessToken = accessToken;
        return "https://api.weibo.com/2/account/get_uid.json";
    }

    @Override
    protected WeiboProfile extractUserProfile(final String body) {
        final WeiboProfile profile = new WeiboProfile();
        JsonNode user = JsonHelper.getFirstNode(body);
        System.out.println("user=" + user);
        if (user != null) {
            String uid = user.get("uid").toString();
            String userdataUrl = "https://api.weibo.com/2/users/show.json?uid=" + uid;
            profile.setId(uid);
            JsonNode userdataBody = JsonHelper.getFirstNode(sendRequestForData(this.accessToken, userdataUrl));
            logger.debug("userdataBody={}", userdataBody);
            List<String> principalAttributes = OAuthAttributesDefinitions.weiboDefinition
                    .getPrincipalAttributes();
            for (final String name : principalAttributes) {
                Object value = JsonHelper.get(userdataBody, name);
                logger.debug("add {}, value = {}", new Object[]{name, value});
                profile.addAttribute(name, value);
            }

        }
        logger.debug("profile = {}", profile);
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
