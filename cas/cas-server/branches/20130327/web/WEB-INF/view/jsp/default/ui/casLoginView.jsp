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
            <link rel="stylesheet" href="<c:url value="/css/eagle.css" />" type="text/css" media="screen" />
            <script type="text/javascript" src="<c:url value="/js/jquery-1.8.0.min.js" />"></script>
            <script type="text/javascript" src="<c:url value="/js/jquery.ba-throttle-debounce.min.js" />"></script>
            <script type="text/javascript" src="<c:url value="/js/eagle.js" />"></script>
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
                        <div class="right">
                            <input type="hidden" name="lt" value="${loginTicket}" />
                            <input type="hidden" name="execution" value="${flowExecutionKey}" />
                            <input type="hidden" name="_eventId" value="submit" />
                            <input style="font-weight: bold;font-size: <spring:message code="screen.tcc_sso.loginbutton.fontsize" />px; font-family:'<spring:message code="screen.tcc_sso.title.font" />';" class="btn-submit" id="submit" name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="3" type="submit" onclick="lowCaseConverter()"/>
                        </div>
                        <div class="left">
                            <div>
                                <div class="id_icon"></div>
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
                                <div class="password_icon"></div>
                                <form:password cssClass="required" cssErrorClass="error" id="password" size="12" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
                            </div>
                            <span class="txtRed">
                                <div id="error">
                                    <form:errors path="*" />
                                </div>
                            </span>
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
                    margin: 0px auto 0px auto;
                    table-layout: fixed;
                }

                #building .logoField
                {	
                    width: 100%;	
                    margin: auto;
                }

                #building .logo
                {
                    margin-top: 15px;
                    width: 280px; 
                    height: 243px;
                }

                #building .loginField
                {	
                    width: 322px;
                    height: 200px;
                    margin: auto;

                    position: relative;

                    background: url(images/login/input2.png) no-repeat center center;
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


                #building .loginField #username_label
                {
                    width: 115px;
                    height: 18px;
                    font-family: "<spring:message code="screen.tcc_sso.title.font" />" "新細明體";

                    position: absolute;
                    top: 75px;
                    left: 30px;

                    border-radius: 5px;	
                }
                #building .loginField #username
                {
                    width: 120px;
                    height: 24px;

                    position: absolute;
                    top: 75px;
                    left: 138px;

                    border-radius: 5px;	
                }

                #building .loginField #password_label
                {
                    width: 115px;
                    height: 18px;
                    font-family: "<spring:message code="screen.tcc_sso.title.font" />" "新細明體";

                    position: absolute;
                    top: 110px;
                    left: 30px;

                    border-radius: 5px; 
                }

                #building .loginField #password
                {
                    width: 120px;
                    height: 24px;

                    position: absolute;
                    top: 109px;
                    left: 138px;

                    border-radius: 5px; 
                }

                #building .loginField #error
                {
                    width: 200px;
                    height: 18px;
                    font-family: "<spring:message code="screen.tcc_sso.title.font" />" "新細明體";
                    color:red;
                    font-size:12px;
                    position: absolute;
                    top: 141px;
                    left: 20px;

                    border-radius: 5px; 
                }


                #building .loginField #login
                {
                    width: 70px;
                    height: 23px;

                    position: absolute;
                    top: 148px;
                    left: 215px;

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
                <div id="building">
                    <table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td style="width:52%;background: url(images/login/back1-2_mobile.png) repeat-x bottom;">
                                <div class="logoField">
                                    <center>
                                        <img src="images/login/TCCGroup_item_mobile.png" class="logo" />
                                    </center>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div class="loginField">
                                    <div id="title">
                                        <spring:message code="screen.tcc_sso.title"/>
                                    </div>
                                    <div id="username_label">
                                        <spring:message code="screen.welcome.label.netid" />
                                    </div>
                                    <c:if test="${not empty sessionScope.openIdLocalId}">
                                        <strong>${sessionScope.openIdLocalId}</strong>
                                        <input type="hidden" id="username" name="username" value="${sessionScope.openIdLocalId}" />
                                    </c:if>

                                    <c:if test="${empty sessionScope.openIdLocalId}">
                                        <spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
                                        <form:input cssClass="required" cssErrorClass="error" id="username" size="12" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="false" htmlEscape="true" />
                                    </c:if>
                                    <spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
                                    <div id="password_label">
                                        <spring:message code="screen.welcome.label.password" />
                                    </div>
                                    <!--form:password cssClass="required" cssErrorClass="error" id="password" style="width:200px" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" /-->
                                    <form:password cssClass="required" cssErrorClass="error" id="password" size="12" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
                                    <span class="txtRed">
                                        <div id="error">
                                            <form:errors path="*" />
                                        </div>
                                    </span>
                                    <br/>
                                    <!--input class="login" name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="3" type="submit" /-->
                                    <input type="hidden" name="lt" value="${loginTicket}" />
                                    <input type="hidden" name="execution" value="${flowExecutionKey}" />
                                    <input type="hidden" name="_eventId" value="submit" />
                                    <div id="login">
                                        <input style="font-weight: bold;font-family:'<spring:message code="screen.tcc_sso.title.font" />';" class="btn-submit" name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="3" type="submit" onclick="lowCaseConverter()"/>                            
                                    </div>
                                    <br/>
                                </div>
                            </td>
                        </tr>			
                    </table>
                </div>
            </form:form>
        </body>
    </html>
</c:if>
