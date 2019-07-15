/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security.IdentityStore;

import com.tcci.security.AuthFacade;
import java.util.Collections;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * get authorization info from db
 * @author Peter.pan
 */
public class AuthorizationIdentityStore {//implements IdentityStore {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Inject
    private AuthFacade authFacade;
    
    @PostConstruct
    public void init() {
        LOG.debug("init ...");
    }

    /**
     * 取得授權群組
     * @param validationResult
     * @return 
     */
    //@Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        // RememberMeCredential
        LOG.debug("getCallerGroups ...");
        LOG.debug("getCallerGroups Status = " + validationResult.getStatus());
        LOG.debug("getCallerGroups CallerPrincipal getName = " + validationResult.getCallerPrincipal().getName());
        
        if( validationResult.getCallerPrincipal()==null || validationResult.getCallerPrincipal().getName()==null ){
            return Collections.emptySet();
        }
        return authFacade.getCallerGroups(validationResult.getCallerPrincipal().getName());
    }

    //@Override
    public Set<IdentityStore.ValidationType> validationTypes() {
        return Collections.singleton(PROVIDE_GROUPS);
    }
}
