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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<c:if test="${browserType eq 'normal'}" >
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=utf8">
            <title>TCC Group System</title>
            <style type="text/css">
                <!--
                .tcc-login {
                    background-image: url(images/login/MainContainerIndex.jpg);
                    height: 520px;
                    width: 682px;
                    background-repeat: no-repeat;
                    margin-top: 22px;
                    background-position: center;
                }

                .textfield {
                    border: 1px solid #EAEAEA;
                    font-family: "Arial", "Helvetica", "sans-serif";
                    color: #333333;
                    height: 19px;
                    width: 157px;
                    margin-left: 3px;
                }

                .txt {
                    font-family: "Arial", "Helvetica", "sans-serif";
                    font-size: 10px;
                    color: #999999;
                    font-weight: normal;
                    text-align: right;
                    line-height: 15px;
                    padding-top: 0px;
                    margin-top: 0px;
                    margin-bottom: 20px;
                }
                .txt:hover {
                    font-family: "Arial", "Helvetica", "sans-serif";
                    font-size: 10px;
                    color: #99cc00;
                }
                body {
                    background-image: url(images/login/background.png);
                }
                .txtRed {
                    font-family: Helvetica;
                    font-size: 12px;
                    color: #CC0000;
                }
                .info1 {
                    background-color: transparent;
                    font-family: Arial, Helvetica, sans-serif;
                    font-size: 11px;
                    color: #999999;
                    line-height: 13px;
                    margin-left: 464px;
                }
                .infobg {
                    background-image: url(../images/login/info.jpg);
                    background-repeat: repeat-x;
                    background-position: bottom;
                    background-color: #363636;
                }
                .login{
                    font-family: Arial,Helvetica,sans-serif;
                    font-weight: bold;
                }
                -->
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
            <form:form method="post" id="fm1" cssClass="fm-v clearfix" commandName="${commandName}" htmlEscape="true">
			    
                <table width="517" height="486" border="0" align="center" cellpadding="0" cellspacing="0" class="tcc-login">
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td align="center" style="color:#0066CC;font-size:24px;font-weight:bold;"><spring:message code="screen.tcc_sso.title"/></td>
                    </tr>
                    <tr>
                        <td align="right" valign="bottom" style="padding-right:65px;padding-bottom:65px; font-family: Arial, Helvetica, sans-serif;font-size: 12px;">
                            <table border="0" cellspacing="3" cellpadding="0" style="font-size:12px;font-weight:bold;">
                                <tr>
                                    <td align="right">
                                        ID :
                                    </td>
                                    <td>
						<c:if test="${not empty sessionScope.openIdLocalId}">
						<strong>${sessionScope.openIdLocalId}</strong>
						<input type="hidden" id="username" name="username" value="${sessionScope.openIdLocalId}" />
						</c:if>

						<c:if test="${empty sessionScope.openIdLocalId}">
						<spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
						<form:input cssClass="required" cssErrorClass="error" id="username" size="25" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="false" htmlEscape="true" />
						</c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">
                                        <spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
                                        Password :
                                    </td>
                                    <td>
					<form:password cssClass="required" cssErrorClass="error" id="password" size="25" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
					<input type="hidden" name="lt" value="${flowExecutionKey}" />
					<input type="hidden" name="_eventId" value="submit" />
                                    </td>
                                </tr>
                            </table>
                            <span class="txtRed">
                                <form:errors path="*" />
                            </span>

                            <br/>
                            <input class="login" name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="4" type="submit" />
                            <br/>
                        </td>
                    </tr>
                </table>
            </form:form>
        </body>
    </html>
</c:if>
<c:if test="${browserType eq 'mobile'}" >

</c:if>