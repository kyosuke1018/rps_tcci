/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class TokenUtils {
    private final static Logger logger = LoggerFactory.getLogger(TokenUtils.class);
    public static final String JWT_HEADER_PREFIX = "Bearer ";
    public static final String JWT_CLAIM_USERID = "userId";
    
    public static String issueToken(String login, String issuer, Map<String, Object> claims, long expiredMinutes, KeyGenerator keyGenerator) {
        Key key = keyGenerator.generateKey();
        String jwtToken = Jwts.builder()
                                .setSubject(login)
                                .setIssuer(issuer) // (uriInfo.getAbsolutePath().toString())
                                .setIssuedAt(new Date())
                                .setClaims(claims)
                                .setExpiration(toDate(LocalDateTime.now().plusMinutes(expiredMinutes)))
                                .signWith(SignatureAlgorithm.HS512, key)
                                .compact();
        logger.info("#### generating token for a key : " + jwtToken + " - " + key);
        return jwtToken;
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    public static void printTokenInfo(String token, KeyGenerator keyGenerator) {
        logger.debug("printTokenInfo ...");
        Key key = keyGenerator.generateKey();
        Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        logger.debug("#### valid Header size : " + claims.getHeader().size());// 1
        logger.debug("#### valid getAlgorithm : " + claims.getHeader().getAlgorithm());// HS512
        logger.debug("#### valid Body size : " + claims.getBody().size());// 4
        logger.debug("#### valid getSubject : " + claims.getBody().getSubject());
        logger.debug("#### valid getIssuer : " + claims.getBody().getIssuer());
        logger.debug("#### valid getIssuedAt : " + claims.getBody().getIssuedAt());
        logger.debug("#### valid getExpiration : " + claims.getBody().getExpiration());
        logger.debug("#### valid getSignature : " + claims.getSignature());
    }
}
