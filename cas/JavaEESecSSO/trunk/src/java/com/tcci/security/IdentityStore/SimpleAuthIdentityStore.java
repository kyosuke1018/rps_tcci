/*
 * Copyright (c) 2017 Payara Foundation and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://github.com/payara/Payara/blob/master/LICENSE.txt
 * See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * The Payara Foundation designates this particular file as subject to the "Classpath"
 * exception as provided by the Payara Foundation in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.tcci.security.IdentityStore;

import static java.util.Collections.singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.security.enterprise.identitystore.IdentityStore.ValidationType;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.VALIDATE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class SimpleAuthIdentityStore {//{implements IdentityStore {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private Map<String, String> callerToPassword;

    @PostConstruct
    public void init() {
        LOG.debug("init ...");
        callerToPassword = new HashMap<>();
        callerToPassword.put("payara", "fish");
        callerToPassword.put("duke", "secret");
        callerToPassword.put("admin", "abcd1234");
    }

    //@Override
    public CredentialValidationResult validate(Credential credential) {
        LOG.debug("validate ...");
        CredentialValidationResult result;
        if( !(credential instanceof UsernamePasswordCredential) ){
            LOG.debug("validate not UsernamePasswordCredential ...");
            return NOT_VALIDATED_RESULT;
        }
        
        UsernamePasswordCredential usernamePassword = (UsernamePasswordCredential) credential;
        if( usernamePassword.getCaller()==null ){
            LOG.error("validate usernamePassword.getCaller()==null");
            result = INVALID_RESULT;
        }else{
            String expectedPW = callerToPassword.get(usernamePassword.getCaller());
            if (expectedPW != null && expectedPW.equals(usernamePassword.getPasswordAsString())) {
                result = new CredentialValidationResult(usernamePassword.getCaller());
            } else {
                LOG.error("validate Password INVALID");
                result = INVALID_RESULT;
            }
        }

        return result;
    }

    //@Override
    public Set<ValidationType> validationTypes() {
        return singleton(VALIDATE);
    }
}
