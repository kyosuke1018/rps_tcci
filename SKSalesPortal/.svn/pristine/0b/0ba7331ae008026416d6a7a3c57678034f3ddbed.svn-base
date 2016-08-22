/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.rs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.entity.org.SkSalesChannels;
import com.tcci.sksp.facade.SkSalesChannelsFacade;
import com.tcci.sksp.vo.UserVO;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@Path("user")
@Stateless
public class UserREST {

    Logger logger = LoggerFactory.getLogger(UserREST.class);
    private static final String SALES_MANAGER = "SalesManager";
    @EJB
    TcUserFacade userFacade;
    @EJB
    SkSalesChannelsFacade channelsFacade;

    @GET
    @Path("finduserroles")
    @Produces("text/plain; charset=UTF-8;")
    public String isUserInRoles(@Context HttpServletRequest request) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        UserVO vo = null;
        logger.debug("userFacade={}", userFacade);
        if (request.getUserPrincipal() == null) {
            return "NO_PERMISSION";
        }
        TcUser user = userFacade.findUserByLoginAccount(request.getUserPrincipal().getName());
        logger.debug("user={}", user);
        if (null != user) {
            vo = new UserVO();
            List<String> roles = new ArrayList();
            vo.setTcUser(user);
            logger.debug("request={}", request);
            List<String> allRoles = findRoleByCode(request, null);

            for (String roleName : allRoles) {
                if (request.isUserInRole(roleName) || roleName.equals(SALES_MANAGER)) {
                    roles.add(roleName);
                }
            }
            vo.setRoles(roles);
        }
        return gson.toJson(vo);
    }

    public List<String> findRoleByCode(@Context HttpServletRequest request, String roleName) {
        List<String> roles = new ArrayList<String>();
        InputStream is = request.getServletContext().getResourceAsStream("/WEB-INF/web.xml");
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(is);

            Element webApp = doc.getRootElement();

            // Type safety warning:  dom4j doesn't use generics
            List<Element> roleElements = webApp.elements("security-role");
            for (Element roleEl : roleElements) {
                roles.add(roleEl.element("role-name").getText());
            }
        } catch (DocumentException e) {
            logger.error("e={}", e);
        }
        List<String> result = new ArrayList();
        if (StringUtils.isNotEmpty(roleName)) {
            for (String role : roles) {
                if (role.startsWith(roleName)) {
                    result.add(role);
                }
            }
        } else {
            result.addAll(roles);
        }
        logger.debug("request.getUserPrincipal().getName()={}", request.getUserPrincipal().getName());
        if (isSalesManager(userFacade.findUserByLoginAccount(request.getUserPrincipal().getName()))) {
            result.add(SALES_MANAGER);
        }
        logger.debug("result.size()={}", result.size());
        return result;
    }

    private boolean isSalesManager(TcUser user) {
        boolean isManager = false;
        logger.debug("user={}", user);
        SkSalesChannels channel = channelsFacade.findByManager(user);
        logger.debug("channel={}", channel);
        if (channel != null) {
            isManager = true;
        }
        return isManager;
    }
}
