/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security;

import static java.util.Collections.singleton;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;
import javax.security.enterprise.identitystore.IdentityStore;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.VALIDATE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class DatabaseIdentityStore implements IdentityStore {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
        logger.debug("DatabaseIdentityStore init ...");
    }
    
    /**
     * 驗證主程式
     * @param credential
     * @return 
     */
    @Override
    public CredentialValidationResult validate(Credential credential) {
        logger.debug("validate ...");
        CredentialValidationResult result = NOT_VALIDATED_RESULT;// INVALID_RESULT
        try{
            JWTCredential jwtCredential = (JWTCredential)credential;
            String loginAccount = jwtCredential.getCaller();
            String password = jwtCredential.getPassword();
            boolean encrypted = jwtCredential.isEncrypted();

        }catch(Exception e){
            logger.error("validate Exception :\n", e);
        }

        logger.info("validate result ="+result.getStatus());
        return result;
    }
    
    /**
     * 自定 IdentityStore 執行順序 - 預設 priority 為100，越小越先執行
     * @return 
     */
    @Override
    public int priority(){ 
         return 80;
    }

    @Override
    public Set<ValidationType> validationTypes() {
        Set<ValidationType> types = new HashSet<ValidationType>();
        types.add(VALIDATE);
        types.add(PROVIDE_GROUPS);
        
        return types;
    }

}
