/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.security.IdentityStore;

import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.security.enterprise.identitystore.IdentityStoreHandler;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;
import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;
import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.INVALID;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.VALIDATE;
import static org.glassfish.soteria.cdi.CdiUtils.getBeanReferencesByType;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用 @OAuth2AuthenticationDefinition 時 CusIdentityStoreHandler 無效
 * @author Peter.pan
 */
//@Alternative
//@Priority(Interceptor.Priority.APPLICATION)
//@ApplicationScoped
public class CusIdentityStoreHandler {//implements IdentityStoreHandler {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    
    private List<IdentityStore> authenticationIdentityStores;
    private List<IdentityStore> authorizationIdentityStores;
    
    @PostConstruct
    public void init() {
        LOG.debug("CusIdentityStoreHandler init ...");
        List<IdentityStore> identityStores = getBeanReferencesByType(IdentityStore.class, false);
        // 認證用的 IdentityStore (可能含授權)
        authenticationIdentityStores = identityStores.stream()
                .filter(i -> i.validationTypes().contains(VALIDATE))
                .sorted(comparing(IdentityStore::priority))
                .collect(toList());
        LOG.debug("authentication I.S. = "+(authenticationIdentityStores!=null?authenticationIdentityStores.size():0));
        // 授權用的 IdentityStore
        authorizationIdentityStores = identityStores.stream()
                .filter(i -> i.validationTypes().contains(PROVIDE_GROUPS) && !i.validationTypes().contains(VALIDATE))
                .sorted(comparing(IdentityStore::priority))
                .collect(toList());
        LOG.debug("authorization I.S. = "+(authorizationIdentityStores!=null?authorizationIdentityStores.size():0));
    }
    
    //@Override
    public CredentialValidationResult validate(Credential credential) {
        LOG.debug("validate ...");
        CredentialValidationResult validationResult = null;
        IdentityStore identityStore = null;
        boolean isGotAnInvalidResult = false;
        
        // Check stores to authenticate until one succeeds.
        if( authenticationIdentityStores!=null ){
            for (IdentityStore authenticationIdentityStore : authenticationIdentityStores) {
                LOG.debug("authentication I.S. "+authenticationIdentityStore.getClass().getName());
                try{
                    validationResult = authenticationIdentityStore.validate(credential);
                }catch(Exception e){
                    // 避免預設 identityStore 問題設定前端出現 500 Error
                    LOG.error("validateRequest Exception:\n", e);
                    validationResult = CredentialValidationResult.INVALID_RESULT;
                }

                if (validationResult.getStatus() == VALID) {
                    identityStore = authenticationIdentityStore;
                    break;// 任一項通過驗證
                }
                else if (validationResult.getStatus() == INVALID) {
                    isGotAnInvalidResult = true;// 有驗證失敗的項目
                }
            }
        }
        
        // 無驗證 或 有驗證失敗
        if( validationResult == null || validationResult.getStatus() != VALID ){
            // Didn't get a VALID result. If we got an INVALID result at any point,
            // return INVALID_RESULT. Otherwise, return NOT_VALIDATED_RESULT.
            if( isGotAnInvalidResult ){// 有驗證失敗的項目
                return INVALID_RESULT;
            }
            else {
                return NOT_VALIDATED_RESULT;// 無驗證結果
            }
        }
        
        // 授權資訊
        Set<String> groups = new HashSet<>();
        
        // Take the groups from the identity store that validated the credentials only
        // if it has been set to provide groups.
        if( identityStore!=null && identityStore.validationTypes()!=null ){
            if( identityStore.validationTypes().contains(PROVIDE_GROUPS) ){
                groups.addAll(validationResult.getCallerGroups());
            }
        }
        
        // Ask all stores that were configured for group providing only to get the groups for the
        // authenticated caller
        CredentialValidationResult finalResult = validationResult; // compiler didn't like validationResult in the enclosed scope
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                for (IdentityStore authorizationIdentityStore : authorizationIdentityStores) {
                    LOG.debug("authorization I.S. "+authorizationIdentityStore.getClass().getName());
                    groups.addAll(authorizationIdentityStore.getCallerGroups(finalResult));
                }
                return null;
            }
        });
        
        return new CredentialValidationResult(
                validationResult.getIdentityStoreId(),
                validationResult.getCallerPrincipal(),
                validationResult.getCallerDn(),
                validationResult.getCallerUniqueId(),
                groups);
    }
    
}