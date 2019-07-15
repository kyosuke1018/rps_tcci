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
            <link rel="stylesheet" href="<c:url value="/css/cm.css" />" type="text/css" media="screen" />
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
                    padding-left: -30px;
                    float: left;
                }
                #login .right
                {
                    width: 255px;
                    float: right;
                }
            </style>
            <script type="text/javascript" src="<c:url value="/js/jquery-1.8.0.min.js" />"></script>
            <script type="text/javascript" src="<c:url value="/js/jquery.snow.min.1.0.js" />"></script>
            <script type="text/javascript" src="<c:url value="/js/jquery.ba-throttle-debounce.min.js" />"></script>
            <script type="text/javascript" src="<c:url value="/js/dumplings.js" />"></script>
            <meta http-equiv="Pragma" content="no-cache">
            <meta http-equiv="Cache-Control" content="no-cache">
            <meta http-equiv="Expires" content="0">
            <meta http-equiv="X-UA-Compatible" content="IE=9" />
        </head>
        <body>
            <div id="background"><img src="<spring:message code='screen.tcc_sso.background_image'/>"/></div>
                <form:form method="post" id="fm1" cssClass="fm-v clearfix" cssStyle="margin: 0px" commandName="${commandName}" htmlEscape="true">
                <div id="login">
                    <div style="position:relative">
                        <div class="title"><spring:message code="screen.tcc_sso.title"/></div>
                        <div class="clear"></div>
                        <table border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td>
                                    <div id="image" class="left" style="background-color:white;height:50px;width:275px;opacity:0.85;border-top-left-radius: 10%;border-top-right-radius:10%">
                                        <img src="./images/confirm.gif"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div id="message" class="right" style="background-color:white;height:180px; width:275px; opacity:0.85;border-bottom-left-radius:10px; border-bottom-right-radius:10px;">
                                        <div style="margin-left: 10px;">
                                            <font class="contentTitle" ><spring:message code="screen.success.header" /></font>
                                            <br/>
                                            <font class="content"><spring:message code="screen.success.success" /></font>
                                            <br/>
                                            <font class="content"><spring:message code="screen.success.security" /></font>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </table>
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
            <link rel="stylesheet" href="<c:url value="/css/annual_year_mobile.css" />" type="text/css" media="screen" />
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
                #login .message
                {
                    margin: 0px 0px 0px 10px;
                    width: 330px;
                    float: left;
                }
            </style>
            <script type="text/javascript">
                <!--
                function MM_preloadImages() { //v3.0
                    var d = document;
                    if (d.images) {
                        if (!d.MM_p)
                            d.MM_p = new Array();
                        var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
                        for (i = 0; i < a.length; i++)
                            if (a[i].indexOf("#") != 0) {
                                d.MM_p[j] = new Image;
                                d.MM_p[j++].src = a[i];
                            }
                    }
                }
                //-->
            </script>
            <meta http-equiv="Pragma" content="no-cache">
            <meta http-equiv="Cache-Control" content="no-cache">
            <meta http-equiv="Expires" content="0">
        </head>
        <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
            <div id="login">
                <table border="0" cellpadding="0" cellspacing="0" style="width:90%;">
                    <tr>
                        <td style="text-align:center;">
                            <div class="slogan2"><spring:message code="screen.tcc_sso.slogan1"/></div>
                            <div class="clear"></div>
                            <div class="line"></div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="title">
                                <spring:message code="screen.tcc_sso.title"/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div id="message" class="message">
                                <table>
                                    <tr>
                                        <td style="vertical-align: top;">
                                            <img src="./images/confirm.gif"/>

                                        </td>
                                        <td>
                                            <font class="contentTitle"><spring:message code="screen.success.header" /></font>
                                            <br/>
                                            <font class="content"><spring:message code="screen.success.success" /></font>
                                            <br/>
                                            <font class="content"><spring:message code="screen.success.security" /></font>

                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                    </tr>			
                    <tr>
                        <td style="text-align: center;">
                            <img id="eagle" src="images/annual_year/horse.png"
                                 style="margin-bottom:60px;"/>
                        </td>
                    </tr>
                </table>
            </div>
        </body>
    </html>
</c:if>            