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

<!DOCTYPE HTML PUBLIC “-//W3C//DTD HTML 4.01 Transitional//EN" “http://www.w3.org/TR/html4/loose.dtd">
<c:if test="${browserType eq 'normal'}" >
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=utf8">
            <title><spring:message code="screen.tcc_sso.title"/></title>
            <link rel="stylesheet" href="<c:url value="/css/cm.css" />" type="text/css" media="screen" />
            <script type="text/javascript" src="<c:url value="/js/jquery-1.8.0.min.js" />"></script>
            <script type="text/javascript" src="<c:url value="/js/jquery.snow.min.1.0.js" />"></script>
            <script type="text/javascript" src="<c:url value="/js/jquery.ba-throttle-debounce.min.js" />"></script>
            <script type="text/javascript" src="<c:url value="/js/dumplings.js" />"></script>
            <script type="text/javascript">
                //<![CDATA[
                setTimeout(function()
                {
                    resize();
                    document.getElementById('username').focus();
                }, 200);
                //]]>
            </script>
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
                        <table style="border-width: 0px" >
                            <tr>
                                <td style="width:170px;">
                                    <div class="left" style="margin-right: 10px;">
                                        <img src="images/cm/id.png"/>
                                        <c:if test="${not empty sessionScope.openIdLocalId}">
                                            <strong>${sessionScope.openIdLocalId}</strong>
                                            <input type="hidden" id="username" name="username" value="${sessionScope.openIdLocalId}" />
                                        </c:if>
                                        <c:if test="${empty sessionScope.openIdLocalId}">
                                            <spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
                                            <form:input cssClass="required" cssErrorClass="error" id="username" size="12" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="false" htmlEscape="true" />
                                        </c:if>
                                        <div class="space"></div>
                                        <spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
                                        <img src="images/cm/password.png"/>
                                        <form:password cssClass="required" style="margin-left: 2px;" cssErrorClass="error" id="password" size="12" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
                                    </div>                                    
                                </td>
                                <td style="vertical-align: middle; text-align: left;">
                                    <div class="right">
                                        <input type="hidden" name="lt" value="${loginTicket}" />
                                        <input type="hidden" name="execution" value="${flowExecutionKey}" />
                                        <input type="hidden" name="_eventId" value="submit" />
                                        <input style="font-weight: bold;font-size: <spring:message code="screen.tcc_sso.loginbutton.fontsize" />px; font-family:'<spring:message code="screen.tcc_sso.title.font" />';" class="btn-submit" id="submit" name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="3" type="submit" onclick="lowCaseConverter()"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <span class="txtRed">
                                        <div id="error">
                                            <form:errors path="*" />
                                        </div>
                                    </span>
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
            <script type="text/javascript">
                function lowCaseConverter() {
                    var jid = document.getElementById("username");
                    var data = jid.value;
                    jid.value = data.toLowerCase().trim();
                    //alert("username='" + jid.value +"'");
                    jid = document.getElementById("password");
                    data = jid.value;
                    jid.value = data.trim();
                    //alert("password='" + jid.value +"'");
                    return true;
                }
            </script>            
        </head>
        <body>
            <form:form method="post" id="mobileForm" commandName="${commandName}" htmlEscape="true" action="?">
                <div id="login">
                    <table border="0" cellpadding="0" cellspacing="0" style="width:90%;">
                        <tr>
                            <td style="text-align:center;">
                                <div class="slogan2"><spring:message code="screen.tcc_sso.slogan1"/></div>
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
                                <div class="left">
                                    <div>
                                        <img src="images/cm/id.png"/>
                                        <c:if test="${not empty sessionScope.openIdLocalId}">
                                            <strong>${sessionScope.openIdLocalId}</strong>
                                            <input type="hidden" id="username" name="username" value="${sessionScope.openIdLocalId}" />
                                        </c:if>
                                        <c:if test="${empty sessionScope.openIdLocalId}">
                                            <spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
                                            <form:input cssClass="required" cssErrorClass="error" id="username" size="12" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="false" htmlEscape="true" />
                                        </c:if>
                                    </div>
                                    <div class="space"></div>
                                    <div>
                                        <spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
                                        <img src="images/cm/password.png"/>
                                        <form:password cssClass="required" style="margin-left: 2px;" cssErrorClass="error" id="password" size="12" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
                                    </div>
                                    <span class="txtRed">
                                        <div id="error">
                                            <form:errors path="*" />
                                        </div>
                                    </span>
                                </div>
                                <div class="right">
                                    <input type="hidden" name="lt" value="${loginTicket}" />
                                    <input type="hidden" name="execution" value="${flowExecutionKey}" />
                                    <input type="hidden" name="_eventId" value="submit" />
                                    <input style="font-weight: bold;font-size: <spring:message code="screen.tcc_sso.loginbutton.fontsize" />px; font-family:'<spring:message code="screen.tcc_sso.title.font" />';" class="btn-submit" id="submit" name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="3" type="submit" onclick="lowCaseConverter()"/>
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
            </form:form>
        </body>
    </html>
</c:if>
