package fish.payara.security.oauth2.server;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * doGet => authEndpoint
 * doPost => tokenEndpoint
 */
@WebServlet("/Endpoint")
public class Endpoint extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    /**
     * authEndpoint
     * http://localhost:8080/JavaEESecSSO/Endpoint?redirect_uri=&state=XXXXX&response_type=code&client_id=qwertyuiop
     * 
     * redirect_uri、state、response_type、client_id
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("/Endpoint doGet ... authEndpoint");
        StringBuilder returnURL = new StringBuilder(request.getParameter("redirect_uri"));
        returnURL.append("?state=").append(request.getParameter("state"));
        returnURL.append("&code=plokmijn");

        if (!"code".equals(request.getParameter("response_type"))) {
            response.sendError(401);
        }
        if (!"qwertyuiop".equals(request.getParameter("client_id"))) {
            response.sendError(401);
        }
        LOG.debug("/Endpoint doGet ... authEndpoint redirect to "+returnURL.toString());
        response.sendRedirect(returnURL.toString());
    }

    /**
     * tokenEndpoint
     * http://localhost:8080/JavaEESecSSO/Endpoint
     * 
     * input : 
     *  client_id=qwertyuiop
     *  client_secret=asdfghjklzxcvbnm
     *  grant_type=authorization_code
     *  code=plokmijn
     * 
     * return :
     * {
     *  access_token:"qazwsxedc", 
     *  state:"XXXXX", 
     *  token_type:"bearer"
     * }
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("/Endpoint doPost ... tokenEndpoint");
        boolean grantRight = "authorization_code".equals(request.getParameter("grant_type"));
        boolean codeRight = "plokmijn".equals(request.getParameter("code"));
        boolean clientRight = "qwertyuiop".equals(request.getParameter("client_id"));
        boolean secretRight = "asdfghjklzxcvbnm".equals(request.getParameter("client_secret"));

        JsonObjectBuilder jsonresponse = Json.createObjectBuilder();
        if (grantRight && codeRight && clientRight&& secretRight) {
            jsonresponse.add("access_token", "qazwsxedc");
            jsonresponse.add("state", request.getParameter("state"));
            jsonresponse.add("token_type", "bearer");
            String built = jsonresponse.build().toString();
            response.getWriter().write(built);
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            LOG.debug("/Endpoint doPost ... tokenEndpoint SUCCESS");
        } else {
            jsonresponse.add("error", "somethingwentwrong");
            String errors = Boolean.toString(grantRight) + Boolean.toString(codeRight) + Boolean.toString(clientRight) + Boolean.toString(secretRight);
            jsonresponse.add("error_desc", errors);
            String built = jsonresponse.build().toString();
            response.getWriter().write(built);
            response.sendError(401);
            LOG.debug("/Endpoint doPost ... tokenEndpoint ERROR");
        }
    }

}
