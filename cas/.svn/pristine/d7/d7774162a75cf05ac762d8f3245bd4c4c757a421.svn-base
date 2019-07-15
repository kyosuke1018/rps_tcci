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
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf8">
        <title><spring:message code="screen.tcc_sso.title"/></title>
        <link rel="stylesheet" href="<c:url value="/css/style.css" />" type="text/css" media="screen" />
        <link rel="icon" type="image/x-icon" href="<c:url value="/images/ccil/logo.png"/>"/>
        <style>
            .login {
                left: 692px !important;
            }
            .login .contentTitle
            {
                width: 310px;
                text-align: center;
                font-size: <spring:message code="screen.tcc_sso.title.fontsize" />px;
                font-family: "<spring:message code="screen.tcc_sso.title.font" />" "新細明體";
                font-weight: bold;
                color: #808080;
            }
            .login .content
            {
                font-size: 14px;
                color: #808080;
            }
        </style>
        <script type="text/javascript" src="<c:url value="/js/jquery-1.12.4.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/bg-loaded.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/ccil.js" />"></script>
        <meta http-equiv="Pragma" content="no-cache"/>
        <meta http-equiv="Cache-Control" content="no-cache"/>
        <meta http-equiv="Expires" content="0"/>
        <meta http-equiv="X-UA-Compatible" content="IE=9" />
    </head>
    <body>
        <div class="background">
            <img class="logo" src="images/ccil/logo.png" />
            <div class="login">
                <form:form method="post" id="fm1" cssClass="fm-v clearfix" cssStyle="margin: 0px" commandName="${commandName}" htmlEscape="true">
                    <div style="position:relative">
                        <div class="title"><font style="font-size: <spring:message code="screen.tcc_sso.title.fontsize"/>;"><spring:message code="screen.tcc_sso.title"/></font></div>
                        <div class="clear"></div>
                    </div>
                    <div style="height:112px; width:320px;margin-left: -50px;">
                        <div style="margin-top: 5px;">
                            <font class="contentTitle" ><spring:message code="screen.success.header" /></font>
                            <br/>
                            <font class="content"><spring:message code="screen.success.success" /></font>
                            <br/>
                            <font class="content"><spring:message code="screen.success.security" /></font>
                        </div>
                    </div>
                </form:form>
                <div class="carousels">
                    <div class="cover4s"></div>
                    <div class="cover3s"></div>
                    <div class="cover2s"></div>
                    <div class="cover1s"></div>
                </div>        
            </div>
        </div>
        <div class="carousel">
            <div class="cover4"></div>
            <div class="cover3"></div>
            <div class="cover2"></div>
            <div class="cover1"></div>
        </div>
        <script type="text/javascript">
            //<![CDATA[
            $(".carousel div").bgLoaded({
                afterLoaded: function () {
                    $(".carousel .cover1, .carousels .cover1s").addClass("coverAni1");
                    $(".carousel .cover2, .carousels .cover2s").addClass("coverAni2");
                    $(".carousel .cover3, .carousels .cover3s").addClass("coverAni3");
                    $(".carousel .cover4, .carousels .cover4s").addClass("coverAni4");
                }
            });
            //for tms sytem (iframe).
            if (window.top !== window.self) {
                window.top.location = window.location;
            }
            //]]>
        </script>
    </body>
</html>