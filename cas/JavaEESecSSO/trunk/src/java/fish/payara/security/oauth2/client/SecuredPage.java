/*
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 *  Copyright (c) [2018] Payara Foundation and/or its affiliates. All rights reserved.
 * 
 *  The contents of this file are subject to the terms of either the GNU
 *  General Public License Version 2 only ("GPL") or the Common Development
 *  and Distribution License("CDDL") (collectively, the "License").  You
 *  may not use this file except in compliance with the License.  You can
 *  obtain a copy of the License at
 *  https://github.com/payara/Payara/blob/master/LICENSE.txt
 *  See the License for the specific
 *  language governing permissions and limitations under the License.
 * 
 *  When distributing the software, include this License Header Notice in each
 *  file and include the License file at glassfish/legal/LICENSE.txt.
 * 
 *  GPL Classpath Exception:
 *  The Payara Foundation designates this particular file as subject to the "Classpath"
 *  exception as provided by the Payara Foundation in the GPL Version 2 section of the License
 *  file that accompanied this code.
 * 
 *  Modifications:
 *  If applicable, add the following below the License Header, with the fields
 *  enclosed by brackets [] replaced by your own identifying information:
 *  "Portions Copyright [year] [name of copyright owner]"
 * 
 *  Contributor(s):
 *  If you wish your version of this file to be governed by only the CDDL or
 *  only the GPL Version 2, indicate your decision by adding "[Contributor]
 *  elects to include this software in this distribution under the [CDDL or GPL
 *  Version 2] license."  If you don't indicate a single choice of license, a
 *  recipient has the option to distribute your version of this file under
 *  either the CDDL, the GPL Version 2 or to extend the choice of license to
 *  its licensees as provided above.  However, if you add GPL Version 2 code
 *  and therefore, elected the GPL Version 2 license, then the option applies
 *  only if the new code is made subject to such option by the copyright
 *  holder.
 */
package fish.payara.security.oauth2.client;

import java.io.IOException;

import javax.annotation.security.DeclareRoles;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fish.payara.security.annotations.OAuth2AuthenticationDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 避免出現 WELD-001335: Ambiguous dependencies for type HttpAuthenticationMechanism with qualifiers
 * 測試前註解掉其他 AuthenticationMechanism、AuthenticationDefinition 
 * 
 * http://localhost:8080/JavaEESecSSO/Secured
 * 
 */
/*@GoogleAuthenticationDefinition(
    clientId = "992703581191-55nvg8ihqst9245uumj7dhct8mtqj1k3.apps.googleusercontent.com", 
    clientSecret = "SsIOgYQh4FvwdIhwkp_V_COt",
    scope = {"openid", "email", "profile"},
    responseType = "code", // token (return info in url frag.)
    useNonce = true, // false
    redirectURI = "http://localhost:8080/JavaEESecSSO/Callback" 
)*/
/*@OAuth2AuthenticationDefinition(
    authEndpoint = "https://accounts.google.com/o/oauth2/auth", 
    tokenEndpoint = "https://www.googleapis.com/oauth2/v4/token",
    clientId = "992703581191-55nvg8ihqst9245uumj7dhct8mtqj1k3.apps.googleusercontent.com", 
    clientSecret = "SsIOgYQh4FvwdIhwkp_V_COt",
    scope="https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email",
    redirectURI = "http://localhost:8080/JavaEESecSSO/Callback"
)*/
@OAuth2AuthenticationDefinition(
    authEndpoint = "http://localhost:8080/JavaEESecSSO/Endpoint", 
    tokenEndpoint = "http://localhost:8080/JavaEESecSSO/Endpoint", 
    clientId = "qwertyuiop",
    clientSecret = "asdfghjklzxcvbnm", 
    redirectURI = "http://localhost:8080/JavaEESecSSO/Callback"
)
// OAuth2 : Authorization Code Grant Flow
@WebServlet("/Secured")
@DeclareRoles("ROLE_USER")
@ServletSecurity(@HttpConstraint(rolesAllowed = "ROLE_USER"))
public class SecuredPage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("SecuredPage doGet ...");
        
        response.getWriter().println("This is a secured web page");
    }
}
