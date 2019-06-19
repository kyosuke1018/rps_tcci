/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.ec.servlet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author penpl
 */
@WebServlet(name = "WeChatServlet", urlPatterns = {"/WeChatServlet"})
public class WeChatServlet extends HttpServlet {
    public final Logger logger = LoggerFactory.getLogger(WeChatServlet.class);
    private final String APPID = "wx0b90a1c88ac2476e";
    private final String APPSECRET = "538e345dbb40c38e092d711c0cff60cb";
    private final String token = "hello2019";
    
    /**
     *
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //获取参数值
        String signature = request.getParameter("signature");
        String timeStamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        
        logger.info("processRequest signature =  "+signature);
        logger.info("processRequest timeStamp =  "+timeStamp);
        logger.info("processRequest nonce =  "+nonce);
        logger.info("processRequest echostr =  "+echostr);
        /*
        processRequest signature =  2912177be2ba1f355c940d3e35b4d1c715882d24
        processRequest timeStamp =  1546093558
        processRequest nonce =  67530340
        processRequest echostr =  3331417644718866423
        */
        
        if( echostr!=null ){
            PrintWriter out = null;
            try {
                out = response.getWriter();
                //开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
                if (CheckUtil.checkSignature(token, timeStamp, nonce).equals(signature)) {
                    //作出响应，即原样返回随机字符串
                    out.println(echostr);
                }
            } catch (IOException e) {
                logger.error("processRequest IOException :\n", e);
            }finally {
                if( out!=null ){
                    out.close();
                }
            }
            return;
        }
        
        String code = request.getParameter("code");
        logger.info("processRequest code =  "+code);
        if( code!=null ){
            String userinfo = afterGetCode(code);
            
            try (PrintWriter out = response.getWriter()) {
                out.println("<h1>userinfo = " + userinfo + "</h1>");
            }
            return;
        }
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String redirectUrl = "http://tccstore.taiwancement.com/ec-seller-serv/WeChatServlet";
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
            // scope 中参数：
            //  snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），
            //  snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。
            // 并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）跳转页面中的处理代码：
            String scope = "1".equals(request.getParameter("scope"))?"snsapi_base":"snsapi_userinfo";
            String state = Long.toString(System.currentTimeMillis());
            
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                    +APPID+"&redirect_uri="+redirectUrl
                    +"&response_type=code&scope="+scope
                    +"&state="+state+"#wechat_redirect";
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet WeChatServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<a href='"+url+"' >");
            out.println("<h1>Login by WeChat</h1>");
            out.println("</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    public String afterGetCode(String code) throws IOException{
        if (code != null) {
            //获取openid和access_token的连接
            String getOpenIdUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                    + APPID + "&secret=" + APPSECRET+ "&code=CODE&grant_type=authorization_code";
            //获取返回的code
            String requestUrl = getOpenIdUrl.replace("CODE", code);
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(requestUrl);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            //向微信发送请求并获取response
            String response = httpClient.execute(httpGet,responseHandler);
            logger.info("=========================获取token===================");
            logger.info(response);
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(response);
            String access_token = jsonObject.get("access_token").getAsString();
            String openId = jsonObject.get("openid").getAsString();
            logger.info("=======================用户access_token==============");
            logger.info(access_token);
            logger.info(openId);
            //获取用户基本信息的连接
            String getUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
            String userInfoUrl = getUserInfo.replace("ACCESS_TOKEN", access_token).replace("OPENID", openId);
            HttpGet httpGetUserInfo = new HttpGet(userInfoUrl);
            String userInfo = httpClient.execute(httpGetUserInfo,responseHandler);
            //微信那边采用的编码方式为ISO8859-1所以需要转化
            String json = new String(userInfo.getBytes("ISO-8859-1"),"UTF-8");
            logger.info("====================userInfo==============================");
            JsonObject jsonObject1 = (JsonObject) parser.parse(json);
            String nickname = jsonObject1.get("nickname").getAsString();
            String city = jsonObject1.get("city").getAsString();
            String province = jsonObject1.get("province").getAsString();
            String country = jsonObject1.get("country").getAsString();
            String headimgurl = jsonObject1.get("headimgurl").getAsString();
            //性别  1 男  2 女  0 未知
            Integer sex = jsonObject1.get("sex").getAsInt();
            logger.info("昵称"+nickname);
            logger.info("城市"+city);
            logger.info("省"+province);
            logger.info("国家"+country);
            logger.info("头像"+headimgurl);
            logger.info("性别"+sex);
            logger.info(userInfo);
            return userInfo;
        }
        
        return null;
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
/*
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
    </head>
    <body>
        <script type="text/javascript">
            var ua = navigator.userAgent.toLowerCase();
            var isWeixin = ua.indexOf('micromessenger') != -1;
            var isAndroid = ua.indexOf('android') != -1;
            var isIos = (ua.indexOf('iphone') != -1) || (ua.indexOf('ipad') != -1);
            if (!isWeixin) {
                document.head.innerHTML = '<title>抱歉，出错了</title><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0"><link rel="stylesheet" type="text/css" href="https://res.wx.qq.com/open/libs/weui/0.4.1/weui.css">';
                document.body.innerHTML = '<div class="weui_msg"><div class="weui_icon_area"><i class="weui_icon_info weui_icon_msg"></i></div><div class="weui_text_area"><h4 class="weui_msg_title">请在微信客户端打开链接</h4></div></div>';
            }
        </script>
    </body>
</html>
    
在HTTP请求头里面将MicroMessenger修改成 micromessage
*/

}
