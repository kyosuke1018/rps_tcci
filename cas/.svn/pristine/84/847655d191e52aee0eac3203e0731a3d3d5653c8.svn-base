package com.tcci.security.IdentityStore;

import com.tcci.security.AuthFacade;
import com.tcci.security.OAuth2Result;
import java.io.StringReader;
import java.util.EnumSet;
import java.util.Set;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.security.enterprise.credential.RememberMeCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.security.enterprise.identitystore.IdentityStore.ValidationType;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.VALIDATE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  tokenEndpoint return  => RememberMeCredential
 * 
    Google ex.
    {
        "access_token": "ya29.GlxHBrroiEIRbqwiQhMyNFtAdwmfuN-DfdBzWSYXM6HN2f0W3R9MuS58Tvo8BzRg4gn_crMPysLcP_9jKGxu7SnGiRDpdyvXgWw4MiC9m2WMFXD1wuLR4IczVTbmZA",
        "expires_in": 3600,
        "scope": "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email",
        "token_type": "Bearer",
        "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjgyODlkNTQyODBiNzY3MTJkZTQxY2QyZWY5NTk3MmIxMjNiZTlhYzAiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI5OTI3MDM1ODExOTEtNTVudmc4aWhxc3Q5MjQ1dXVtajdkaGN0OG10cWoxazMuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI5OTI3MDM1ODExOTEtNTVudmc4aWhxc3Q5MjQ1dXVtajdkaGN0OG10cWoxazMuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDI0ODI3Nzg1MzgxMzk3OTkzMjUiLCJlbWFpbCI6InBlbnBsY3BhbkBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6Ind4ZWtlUXUxTDFsdndhaEZvUXZUYXciLCJuYW1lIjoiUGV0ZXIgUGFuIiwicGljdHVyZSI6Imh0dHBzOi8vbGg2Lmdvb2dsZXVzZXJjb250ZW50LmNvbS8tRzZsR25ZWjk3U0EvQUFBQUFBQUFBQUkvQUFBQUFBQUFBQUEvQUJ0TmxiRHNfZE1mQ2NsZmhhSHlPYUJULXkwNkJ5eE9wZy9zOTYtYy9waG90by5qcGciLCJnaXZlbl9uYW1lIjoiUGV0ZXIiLCJmYW1pbHlfbmFtZSI6IlBhbiIsImxvY2FsZSI6InpoLVRXIiwiaWF0IjoxNTQxMDM3ODAzLCJleHAiOjE1NDEwNDE0MDN9.j0mLOhjo7P1sQdcV_ih1pFogMlSbFGuK7c7JInnH6zEbyMuRz914G5HOb3OqW9mTe-D224tS84Ds7p_zN2TahHw-5fzDGHlZ4A1y4PF7hTIqAzjxBtStG00kV_wtTgJ48qbDfAa_WrP9C-cGDEFbZE1WkFokBnBw-5NxjK5HblE-waW_ZIzCkYnVOAwFW0671KmHbDNfS_I_7IHzCVSnJsP3G00Ox1ddwnjJAgXo422tRZhNUNl2DZOHxxUXwgV6o5_gNEj71zQgjXLcwuQ2W7ce7JCkLpY2X9IbayeZ22IvjUL_F5316ExCyP_Y6phFPPouHec4Eq2f51qx5L54ig"
    }

    ex. {"access_token":"qazwsxedc","state":"ffd2e06d-ec91-4dfc-bc31-6d95d6bdc04b","token_type":"bearer"}
    @author Peter.pan
 */
public class OAuthIdentityStore {//implements IdentityStore {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Inject
    private AuthFacade authFacade;
    /**
     * Returns a valid {@link CredentialValidationResult}.
     * <p>
     * If further validation is required this method should be overridden in a sub-class
     * or alternative {@link IdentityStore}. Calling {@link RememberMeCredential#getToken()}
     * on the credential passed in will get the authorisation token which can be used to get
     * more information about the user from the OAuth provider by sending a GET request to
     * an endpoint i.e. https://oauthprovider/user&token=exampletoken.
     * @param credential
     * @return 
     */
    public CredentialValidationResult validate(RememberMeCredential credential){
        //return new CredentialValidationResult(credential.toString()); // 預設 OAuthIdentityStore do nothing ???
        LOG.debug("validate ...");
        if( credential!=null && credential.getToken()!=null && !credential.getToken().trim().isEmpty() ){
            OAuth2Result token = getOAuth2ResultFromCredential(credential);
            LOG.debug("validate accessToken = "+token.getAccessToken());
            
            String callerName = getOAuthUserInfo(token);
            LOG.debug("validate callerName = "+callerName);
            if( callerName!=null && !callerName.trim().isEmpty() ){
                return new CredentialValidationResult(callerName);
            }else{
                return CredentialValidationResult.INVALID_RESULT;
            }
        }
        return CredentialValidationResult.NOT_VALIDATED_RESULT;
    }
    
    public String getOAuthUserInfo(OAuth2Result token) {
        // TODO: call authorization provider's userinfo service api 
        return "peter.pan";
    }
    
    //@Override
    public int priority(){
        return 199;// 預設 OAuthIdentityStore 是 200，要小於它才會有效;
    }
    
    //@Override
    public Set<ValidationType> validationTypes() {
        return EnumSet.of(VALIDATE);
    }

    public OAuth2Result getOAuth2ResultFromCredential(RememberMeCredential credential){
        OAuth2Result token = new OAuth2Result();
        try {
            String jsonResult = credential.getToken();
            JsonReader reader = Json.createReader(new StringReader(jsonResult));
            JsonStructure jsonst = reader.read();

            JsonObject object = (JsonObject) jsonst;
            JsonString access_token = (JsonString) object.get("access_token");
            JsonString scope = (JsonString) object.get("scope");
            JsonString token_type = (JsonString) object.get("token_type");
            JsonString refresh_token = (JsonString) object.get("refresh_token");
            JsonString id_token = (JsonString) object.get("id_token");
            JsonNumber expires_in = (JsonNumber) object.get("expires_in");
            JsonString state = (JsonString) object.get("state");
            
            token.setAccessToken((access_token!=null)?access_token.toString():null);
            token.setRefreshToken((refresh_token!=null)?refresh_token.toString():null);
            token.setExpiresIn((expires_in!=null)?expires_in.intValue():null);
            
            LOG.debug("getOAuth2ResultFromCredential access_token = "+token.getAccessToken());
        } catch (Exception e) {
            LOG.error("getOAuth2ResultFromCredential Exception:\n", e);
        }
        return token;
    }
}
