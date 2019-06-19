package com.tcci.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.DateUtils;
import java.net.URI;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class TokenProvider {
    public final static Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    
    public static final String JWT_URL = "http://www.taiwancement.com";
    public static final String ISSUER = "http://www.taiwancement.com";
    public static final String HEADER_NAME_AUTH = "Authorization";
    public static final String BEARER = "Bearer ";
    // customize claims
    public static final String AUTH_ROLES = "roles";
    public static final String MEMBER_ID = "memberId";
    public static final String STORE_ID = "storeId";
    public static final String ADMIN_USER = "adminUser";
    public static final String TCC_DEALER = "tccDealer";// 台泥經銷商
    public static final String STORE_OWNER = "storeOwner";
    public static final String FI_USER = "fiUser";
    public static final String SESSION_KEY = "session";
    
    private static final boolean USE_HS256 = true;// 目前情境只適合使用 HS256
    
    // for HS256
    private String secretKey;
    // for RSA
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    
    private long tokenValidity;
    
    @PostConstruct
    public void init() {
        this.tokenValidity = TimeUnit.MINUTES.toMillis(GlobalConstant.JWT_EXPIRED_MINUTE);
        //this.tokenValidity = TimeUnit.SECONDS.toMillis(10);  // for test

        if( USE_HS256 ){// for HS256
            this.secretKey = GlobalConstant.SECURITY_KEY;
        }else{
            // for RSA
            try{
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(2048);
                
                KeyPair kp = kpg.genKeyPair();
                publicKey = (RSAPublicKey)kp.getPublic();
                privateKey = (RSAPrivateKey)kp.getPrivate();
            }catch(Exception e){
                logger.error("init Exception :\n", e);
            }
        }
    }
    
    public String createToken(JWTCredential credential) {
        return createToken(credential.getCaller(), credential.getAuthorities(),
                credential.getMemberId(), credential.getStoreId(), 
                credential.isAdminUser(), credential.isTccDealer(), 
                credential.isStoreOwner(), credential.isFiUser(), 
                credential.getFrom(), credential.getSession());
    }

    public JWTCredential getCredential(String token) throws ParseException, JOSEException {
        SignedJWT jwt = SignedJWT.parse(token);
        JWSVerifier verifier = USE_HS256?new MACVerifier(secretKey.getBytes()):new RSASSAVerifier(publicKey);
        
        if( jwt.verify(verifier) ){
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            String caller = claimsSet.getSubject();
            String rolesStr = claimsSet.getClaim(AUTH_ROLES).toString();
            logger.debug("getCredential rolesStr = {}", rolesStr);
            Set<String> roles = Arrays.asList(claimsSet.getClaim(AUTH_ROLES).toString().split(","))
                    .stream()
                    .collect(Collectors.toSet());
            String from = (claimsSet.getAudience()!=null && !claimsSet.getAudience().isEmpty())? claimsSet.getAudience().get(0):null;
            logger.debug("getCredential from = {}", from);
            
            Long memberId = claimsSet.getLongClaim(MEMBER_ID);
            Long storeId = claimsSet.getLongClaim(STORE_ID);
            boolean adminUser = claimsSet.getBooleanClaim(ADMIN_USER);
            boolean tccDealer = claimsSet.getBooleanClaim(TCC_DEALER);
            boolean storeOwner = claimsSet.getBooleanClaim(STORE_OWNER);
            boolean fiUser = claimsSet.getBooleanClaim(FI_USER);
            String session = GlobalConstant.SESSION_KEY_ENABLED?claimsSet.getStringClaim(SESSION_KEY):"";
            
            return new JWTCredential(caller, roles, memberId, storeId, adminUser, tccDealer, storeOwner, fiUser, from, session);
        }
        return null;
    }
    
    public String createToken(String username, Set<String> authorities, Long memberId, Long storeId, 
            Boolean adminUser, Boolean tccDealer, Boolean storeOwner, Boolean fiUser, String from, String session) {
        return createToken(this.secretKey, this.tokenValidity, username, authorities, memberId, storeId, 
            adminUser, tccDealer, storeOwner, fiUser, from, session);
    }
    public String createToken(String secretKey, Long tokenValidity, String username, Set<String> authorities, Long memberId, Long storeId, 
            Boolean adminUser, Boolean tccDealer, Boolean storeOwner, Boolean fiUser, String from, String session) {
        logger.debug("createToken: username:{}", username);
        try{
            long now = (new Date()).getTime();
            logger.debug("createToken now = "+now);
            logger.debug("createToken tokenValidity = "+tokenValidity);
            Date expirationTime = new Date(now + tokenValidity);
            logger.debug("createToken expirationTime = "+DateUtils.format(expirationTime));
            
            StringBuilder sb = new StringBuilder();
            if( authorities!=null ){
                for(String role : authorities){
                    sb.append(sb.toString().isEmpty()?"":",");
                    sb.append(role);
                }
            }
            logger.debug("createToken roles = "+sb.toString());
            logger.debug("createToken memberId = "+memberId);
            logger.debug("createToken storeId = "+storeId);
            logger.debug("createToken from = "+from);
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issueTime(new Date())
                    .issuer(ISSUER)
                    .audience(from)
                    .claim(AUTH_ROLES, sb.toString())
                    .claim(MEMBER_ID, memberId)
                    .claim(STORE_ID, storeId)
                    .claim(ADMIN_USER, adminUser)
                    .claim(TCC_DEALER, tccDealer)
                    .claim(STORE_OWNER, storeOwner)
                    .claim(FI_USER, fiUser)
                    .expirationTime(expirationTime);

            JWTClaimsSet claimsSet;
            // 加入自訂 Session Key 防止 Client 仿造 JWT
            if( GlobalConstant.SESSION_KEY_ENABLED && session!=null ){
                claimsSet = builder.claim(SESSION_KEY, session).build();
            }else{
                claimsSet = builder.build();
            }
            
            JWSHeader header = new JWSHeader.Builder(USE_HS256?JWSAlgorithm.HS256:JWSAlgorithm.RS256)
                    .keyID(Long.toString(System.currentTimeMillis()))
                    .jwkURL(new URI(JWT_URL))
                    .build();
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            JWSSigner signer = USE_HS256?new MACSigner(secretKey.getBytes()):new RSASSASigner(privateKey);
            
            signedJWT.sign(signer);
            
            String serializedJWT = signedJWT.serialize();
            return serializedJWT;
        }catch(Exception e){
            logger.error("init Exception :\n", e);
        }
        return null;
    }
    
    /**
     * 驗證 Token
     * @param token
     * @param checkExpired
     * @return 
     */
    public boolean validateToken(String token, boolean checkExpired){
        return validateToken(this.secretKey, token, checkExpired);
    }
    public boolean validateToken(String secretKey, String token, boolean checkExpired) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            JWSVerifier verifier = USE_HS256?new MACVerifier(secretKey.getBytes()):new RSASSAVerifier(publicKey);
            if( jwt.verify(verifier) ){
                if( !checkExpired || !isTokenExpired(token) ){
                    return true;
                }
            }else{
                logger.warn("validateToken verify fail .");
            }
        } catch (ParseException | JOSEException e) {
            logger.debug("validateToken Invalid JWT signature: {}", e.getMessage());
        }
        return false;
    }
    
    /**
     * Token過期檢查 (不含Token驗證)
     * @param token
     * @return 
     */
    public boolean isTokenExpired(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            if( claimsSet.getExpirationTime()!=null && claimsSet.getExpirationTime().after(new Date()) ){
                return false;
            }else{
                logger.warn("isTokenExpired expired .");
            }
        } catch (Exception e) {
            logger.debug("isTokenExpired Invalid JWT signature: {}", e.getMessage());
        }
        
        return true;
    }
    
    /**
     * 是否需 Refresh Token
     * @param token
     * @return 
     */
    public boolean canRefreshToken(String token) {
        try {
            long time = TimeUnit.MINUTES.toMillis(GlobalConstant.JWT_REFRESH_MINUTE);

            SignedJWT jwt = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            if( claimsSet.getExpirationTime()!=null && claimsSet.getExpirationTime().after(new Date()) ){
                long stime = claimsSet.getExpirationTime().getTime()-time;
                long ntime = (new Date()).getTime();
                logger.info(MessageFormat.format("canRefreshToken stime = {0} : ntime = {1} : stime < ntime = {2}", stime, ntime, (stime < ntime)));
                if( stime < ntime ){
                    return true;// 可(且需)更新 Token
                }
            }
        } catch (Exception e) {
            logger.debug("canRefreshToken Invalid JWT signature: {}", e.getMessage());
        }
        
        return false;
    }
    
    public Object getClaim(String token, String name){
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            return jwt.getJWTClaimsSet().getClaim(name);
        } catch (ParseException e) {
            logger.debug("getClaim ParseException: {}", e.getMessage());
            return null;
        }
    }
    
    public String getSubject(String token){
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            return jwt.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            logger.debug("getSubject ParseException: {}", e.getMessage());
            return null;
        }
    }

    public String getAudience(String token){
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            List<String> audiences =  jwt.getJWTClaimsSet().getAudience();
            return (audiences!=null &&  !audiences.isEmpty())? audiences.get(0):null;
        } catch (ParseException e) {
            logger.debug("getSubject ParseException: {}", e.getMessage());
            return null;
        }

    }
    
    /**
     * Token過期檢查 (含Token驗證)
     * @param token
     * @return 
     */
    public boolean isExpired(String token) {
        return isExpired(this.secretKey, token);
    }
    public boolean isExpired(String secretKey, String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            JWSVerifier verifier = USE_HS256?new MACVerifier(secretKey.getBytes()):new RSASSAVerifier(publicKey);
            if( jwt.verify(verifier) ){
                //return true;
                JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
                if( claimsSet.getExpirationTime()!=null && claimsSet.getExpirationTime().after(new Date()) ){
                    return false;
                }else{
                    logger.warn("isTokenExpired expired .");
                }
            }else{
                logger.warn("isTokenExpired verify fail .");
            }
        } catch (ParseException | JOSEException e) {
            logger.debug("isTokenExpired Invalid JWT signature: {}", e.getMessage());
        }
        return true;
    }
}
