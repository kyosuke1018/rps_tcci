<%--
Author     : Jason Yu
--%>
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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf8"/>
        <title><spring:message code="screen.tcc_sso.title"/></title>
        <link rel="stylesheet" href="<c:url value="/css/style.css" />" type="text/css" media="screen" />
        <link rel="icon" type="image/x-icon" href="<c:url value="/images/ccil/logo.png"/>"/>
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
            <br/>
            <div class="login">
                <form:form method="post" id="fm1" cssClass="fm-v clearfix" cssStyle="margin: 0px" commandName="${commandName}" htmlEscape="true">
                    <div class="title"><spring:message code="screen.tcc_sso.title"/></div>
                    <div class="clear"></div>
                    <div class="user" style="margin-right: 10px; margin-left: 5px;">
                        <img src="images/ccil/id.png"/>
                        <c:if test="${not empty sessionScope.openIdLocalId}">
                            <strong>${sessionScope.openIdLocalId}</strong>
                            <input type="hidden" id="username" name="username" value="${sessionScope.openIdLocalId}" />
                        </c:if>
                        <c:if test="${empty sessionScope.openIdLocalId}">
                            <spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
                            <form:input cssClass="required" style="margin-left: 2px;" cssErrorClass="error" id="username" size="12" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="false" htmlEscape="true" />
                        </c:if>
                        <div class="space"></div>
                        <spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
                        <img src="images/ccil/password.png"/>
                        <form:password cssClass="required" style="margin-left: 2px;" cssErrorClass="error" id="password" size="12" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
                    </div>                                    
                    <div style="text-align: center;">
                        <input type="hidden" name="lt" value="${loginTicket}" />
                        <input type="hidden" name="execution" value="${flowExecutionKey}" />
                        <input type="hidden" name="_eventId" value="submit" />
                        <input id="submit" class="loginBtn" name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="3" type="submit" onclick="lowCaseConverter()"/>
                    </div>
                    <span class="txtRed">
                        <div id="error" style="width: 300px;">
                            <form:errors path="*" />
                        </div>
                    </span>
                </form:form>
            </div>

            <div class="carousels">
                <div class="cover4s"></div>
                <div class="cover3s"></div>
                <div class="cover2s"></div>
                <div class="cover1s"></div>
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
            setTimeout(function ()
            {
                document.getElementById('username').focus();
            }, 200);
            //for tms sytem (iframe).
            if (window.top !== window.self) {
                window.top.location = window.location;
            }
            //]]>
        </script>
    </body>
</html>