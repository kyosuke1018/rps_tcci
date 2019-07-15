<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%!
    public String getBrowserType(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String userAgentType = "normal";

        if (userAgent != null) {
            userAgent = userAgent.toUpperCase();
            Pattern mobilePattern = Pattern.compile(
                    "(IEMOBILE|WINDOWS CE|NETFRONT|PLAYSTATION|PLAYSTATION|LIKE MAC OS X|MIDP|UP\\.BROWSER|SYMBIAN|NINTENDO|ANDROID)");
            //System.out.println(userAgent);
            Matcher matcher = mobilePattern.matcher(userAgent);
            if (matcher.find()) {
                userAgentType = "mobile";
            }
        }

        return userAgentType;
    }
%>

<%
    request.setAttribute("browserType", getBrowserType(request));
%>

<!DOCTYPE HTML PUBLIC “-//W3C//DTD HTML 4.01 Transitional//EN" “http://www.w3.org/TR/html4/loose.dtd">
<c:if test="${browserType eq 'normal'}" >
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=utf8">
            <title><spring:message code="screen.tcc_sso.title"/></title>
            <link rel="stylesheet" href="<c:url value="/css/eagle.css" />" type="text/css" media="screen" />
            <style>
                #login .contentTitle
                {
                    width: 310px;
                    text-align: center;
                    font-size: <spring:message code="screen.tcc_sso.title.fontsize" />px;
                    font-family: "<spring:message code="screen.tcc_sso.title.font" />" "新細明體";
                    font-weight: bold;
                    color: rgb(200, 0, 0);
                }
                #login .content
                {
                    color: rgb(200, 0, 0);
                }
                #login .left
                {
                    width: 50px;
                    padding-left: 160px;
                    float: left;
                }
                #login .right
                {
                    width: 255px;
                    float: right;
                }
            </style>
            <script type="text/javascript" src="<c:url value="/js/jquery-1.8.0.min.js" />"></script>
            <script type="text/javascript" src="<c:url value="/js/jquery.ba-throttle-debounce.min.js" />"></script>
            <script type="text/javascript" src="<c:url value="/js/eagle.js" />"></script>
            <meta http-equiv="Pragma" content="no-cache">
            <meta http-equiv="Cache-Control" content="no-cache">
            <meta http-equiv="Expires" content="0">
            <meta http-equiv="X-UA-Compatible" content="IE=9" />
        </head>
        <body style="visibility: hidden;">
            <div id="ground"></div>
            <img id="eagle" src="images/eagle/eagle.png"/>
            <img id="step" src="images/eagle/step.png"/>
            <img id="shine" src="images/eagle/shine.png"/>

            <form:form method="post" id="fm1" cssClass="fm-v clearfix" cssStyle="margin: 0px" commandName="${commandName}" htmlEscape="true">
            <div id="login">
                <div style="position:relative">
                    <div class="slogan2"><spring:message code="screen.tcc_sso.slogan2"/></div>
                    <div class="slogan1"><spring:message code="screen.tcc_sso.slogan1"/></div>
                    <div class="clear"></div>
                    <div class="line"></div>
                    <div class="title"><spring:message code="screen.tcc_sso.title"/></div>
                    <div class="clear"></div>
                    <div id="image" class="left">
                        <img src="./images/confirm.gif"/>
                    </div>
                    <div id="message" class="right">
                        <font class="contentTitle" ><spring:message code="screen.success.header" /></font>
                        <br/>
                        <font class="content"><spring:message code="screen.success.success" /></font>
                        <br/>
                        <font class="content"><spring:message code="screen.success.security" /></font>
                    </div>
                </div>
            </div>
            </form:form>
        </body>
    </html>
</c:if>



<c:if test="${browserType eq 'mobile'}" >
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
            <meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;"/>
            <title><spring:message code="screen.tcc_sso.title"/></title>
            <style>
                body
                {	
                    margin: 0;
                    background: url(images/login/back1-1.png) repeat-x;
                }

                #building table
                {
                    width: 100%;
                    margin: 5px auto 0px auto;
                    table-layout: fixed;
                }

                #building .logo
                {
                    margin-top: 15px;
                    width: 280px; 
                    height: 243px;
                }

                #building .logoField
                {	
                    width: 100%;	
                    margin: auto;
                }

                #building .loginField
                {	
                    width: 322px;
                    height: 330px;
                    margin: auto;

                    position: relative;

                    background: url(images/login/input_other.png) no-repeat center center;
                }

                #building .loginField #title
                {
                    width: 265px;
                    height: 18px;

                    position: absolute;
                    top: 25px;
                    left: 30px;
                    font-size: <spring:message code="screen.tcc_sso.title.fontsize" />px;
                    color: #D90000;
                    font-family: "<spring:message code="screen.tcc_sso.title.font" />" "新細明體";
                    font-weight: bold;
                    border-radius: 5px;	
                }

                #building .loginField #image
                {
                    width: 300px;
                    height: 18px;

                    position: absolute;
                    top: 80px;
                    left: 15px;

                    border-radius: 5px;	
                }

                #building .loginField #message
                {
                    width: 230px;
                    height: 18px;

                    position: absolute;
                    top: 65px;
                    left: 75px;

                    border-radius: 5px;	
                }

            </style>
            <script type="text/javascript">
                    <!--
                    function MM_preloadImages() { //v3.0
                        var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
                            var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
                                if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
                        }
                        //-->
            </script>
            <meta http-equiv="Pragma" content="no-cache">
            <meta http-equiv="Cache-Control" content="no-cache">
            <meta http-equiv="Expires" content="0">
        </head>
        <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
            <div id="building">
                <table border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td style="width:52%;background: url(images/login/back1-2_mobile.png) repeat-x bottom;">
                            <div class="logoField">
                                <center><img src="images/login/TCCGroup_item_mobile.png" class="logo"></center>
                            </div>
                        </td>
                    </tr>
                    <tr>                    
                        <td>
                            <div class="loginField">
                                <div id="title">
                                    <spring:message code="screen.tcc_sso.title"/>
                                </div>
                                <div id="image">
                                    <img src="./images/confirm.gif"/>
                                </div>
                                <div id="message">
                                    <h2 style="color: #0066CC;"><spring:message code="screen.success.header" /></h2>
                                    <p style="color: #0066CC;"><spring:message code="screen.success.success" /></p>
                                    <p style="color: #0066CC;"><spring:message code="screen.success.security" /></p>
                                </div>
                            </div>
                        </td>
                    </tr>			
                </table>
            </div>
        </body>
    </html>
</c:if>            