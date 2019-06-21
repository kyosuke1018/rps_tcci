/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.enterprise.context.Dependent;
import org.apache.commons.codec.digest.DigestUtils;
import org.glassfish.soteria.identitystores.hash.PasswordHashCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Dependent
public class AESPasswordHashImpl implements AESPasswordHash {
    private static final Logger logger = LoggerFactory.getLogger(AESPasswordHashImpl.class);

    private static final String DEFAULT_ALGORITHM  = "SHA-256";
    private static final String DEFAULT_CHARSET  = "UTF-8";
//    private static final String FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";
//    private static final String KEY_ALGORITHM = "AES";
//    private static final int KEYSPEC_ITERATION_COUNT = 65536;
//    private static final int KEYSPEC_LENGTH = 256;

//    private final SecureRandom random = new SecureRandom();

    @Override
    public void initialize(Map<String, String> parameters) {
    }

    @Override
    public String generate(char[] password) {
//        byte[] salt = getRandomSalt(new byte[configuredSaltSizeBytes]);
//        logger.debug("generate salt:"+salt);
//        byte[] hash = pbkdf2(password, salt, configuredAlgorithm, configuredIterations, configuredKeySizeBytes);
        return this.encrypt(String.valueOf(password));
    }

    @Override
    public boolean verify(char[] password, String hashedPassword) {
        logger.debug("verify password:"+String.valueOf(password));
        logger.debug("verify hashedPassword:"+hashedPassword);
        try {
            String encryptPassword = this.encrypt(String.valueOf(password));
            logger.debug("verify password sha256 encryptPassword:"+encryptPassword);
            return PasswordHashCompare.compareBytes(encryptPassword.getBytes(DEFAULT_CHARSET), hashedPassword.getBytes(DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException ex) {
            logger.error("verify Exception ="+ex);
            return false;
        }
    }
    
//    private SecretKey getDefaultSecretKey(char[] password, final byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
//        return getSecretKey(password, salt, FACTORY_ALGORITHM, KEY_ALGORITHM, KEYSPEC_ITERATION_COUNT, 256);
//    }
    
//    private SecretKey getSecretKey(char[] password,
//            final byte[] salt,
//            final String factoryAlgorithm,
//            final String keyAlgorithm,
//            final int iterationCount,
//            final int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException{
//        SecretKeyFactory factory = SecretKeyFactory.getInstance(factoryAlgorithm);
//        return new SecretKeySpec(factory.generateSecret(new PBEKeySpec(password, salt, iterationCount, keyLength)).getEncoded(), keyAlgorithm);
//    }
    
/**    
    //SHA-256, Hex UTF-8  
    public String encrypt(String generatedKey) {
        try {
            MessageDigest md = MessageDigest.getInstance(DEFAULT_ALGORITHM);//SHA-256
            md.update(generatedKey.getBytes(DEFAULT_CHARSET));//UTF-8
            byte digest[] = md.digest();
//            return (new BASE64Encoder()).encode(digest);
            return Hex.encodeHexString(digest);//Hex
        }
        catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            return null;
        }
    }
     * @param generatedKey
     * @return 
    */
    public String encrypt(String generatedKey) {
        return DigestUtils.sha256Hex(generatedKey);
    }
    
}
