/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2015-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
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

package org.glassfish.soteria.test.servlet;

import com.tcci.ec.servlet.requesthandler.MethodNotAllowedException;
import com.tcci.ec.servlet.responsehandler.ResponseHandler;
import com.tcci.ec.servlet.responsehandler.SSOResponseHandler;
import com.tcci.tccstore.facade.TccMemberFacade;
import com.tcci.tccstore.facade.vo.TccMemberVO;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import javax.annotation.Resource;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Servlet that prints out the name of the authenticated caller and whether
 * this caller is in any of the roles {foo, bar, kaz}
 * 
 *
 */
@DeclareRoles({ "ROLE_ADMIN", "ROLE_USER", "ADMINISTRATORS" })
@WebServlet("/servlet")
public class Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    private static final String ssoTicketUrl = "http://192.168.203.81/ecsso/v1/tickets";
//    private static final String ssoTicketUrl = "http://192.168.203.50/storegateway/login";
//    private static final String ssoTicketUrl = "https://tccstore.taiwancement.com/storegateway/login";
    @EJB
    private TccMemberFacade tccMemberFacade;
    @Resource(mappedName = "jndi/ec.config")
    private Properties config;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("doGet ...");
//        response.getWriter().write("This is a servlet \n");

        String webName = null;
        if (request.getUserPrincipal() != null) {
            webName = request.getUserPrincipal().getName();
        }
        ResponseHandler responseHandler = null;
        try {
            String name = request.getParameter("name");
//            logger.debug("validateRequest name ="+name);
//            logger.debug("validateRequest password ="+request.getParameter("password"));

            //確認帳號是否在1.0
            TccMemberVO member = tccMemberFacade.findActiveByLoginAccount(name);
            if(member != null){
                logger.debug("TccMember password ="+member.getPassword());
                responseHandler = tccStorelogin(request, response);
            }else{
//                response.getWriter().write(webName + " 於tccstore帳號不存在!");
                String errMsg = webName + "　於tccstore帳號不存在!";
                logger.warn(errMsg);
                OutputStream responseStream = response.getOutputStream();
                IOUtils.write(errMsg, responseStream, "UTF-8");
                responseStream.flush();
                responseStream.close();
            }
        } catch (Exception ex) {
            logger.error("tccstore login fail: " + webName + " 登入失敗\n");
            logger.error("ex ..."+ex);
        } finally {
            if (responseHandler != null) {
                responseHandler.close();
            }
        }

//        response.getWriter().write("web username: " + webName + "\n");

//        response.getWriter().write("web user has role \"ROLE_ADMIN\": " + request.isUserInRole("ROLE_ADMIN") + "\n");
//        response.getWriter().write("web user has role \"ROLE_USER\": " + request.isUserInRole("ROLE_USER") + "\n");
//        response.getWriter().write("web user has role \"ADMINISTRATORS\": " + request.isUserInRole("ADMINISTRATORS") + "\n");
    }
    
    private ResponseHandler tccStorelogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws MethodNotAllowedException, IOException {
        String username = httpRequest.getParameter("name");
        String password = httpRequest.getParameter("password");
        if (username != null) {
            username = username.toLowerCase().trim();
        }
        logger.warn("login account:{}", username);
        String ssoTicketUrl = config.getProperty("ssoTicketUrl");
//        logger.warn("ssoTicketUrl:{}", ssoTicketUrl);
        
        PostMethod method = new PostMethod(ssoTicketUrl);
        method.addParameter("username", username);
        method.addParameter("password", password);
//        method.addParameter("password", "admin");
        // httpClient.executeMethod(method);
        HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        httpClient.getParams().setBooleanParameter(HttpClientParams.USE_EXPECT_CONTINUE, false);
        // httpClient.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        httpClient.getParams().setParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, true);
        // timeout
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000); // 5秒內無法建立連線, timeout
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(15000); // 15秒內未收到response, timeout
        httpClient.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(50);
        httpClient.getHttpConnectionManager().getParams().setMaxTotalConnections(50);
        
        httpClient.executeMethod(HostConfiguration.ANY_HOST_CONFIGURATION, method, new HttpState());
        ResponseHandler responseHandler = (ResponseHandler) new SSOResponseHandler(method);
        responseHandler.process(httpResponse);
        return responseHandler;
    }

}
