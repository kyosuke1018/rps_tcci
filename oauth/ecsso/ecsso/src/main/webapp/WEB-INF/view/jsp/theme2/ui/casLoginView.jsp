<%--

    Licensed to Apereo under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Apereo licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License.  You may obtain a
    copy of the License at the following location:

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

--%>
<!DOCTYPE html>
<%@ page session="true" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="viewport" content="width=device-width"/>
        <title><spring:message code="screen.ecsso.title"/></title>
        <link rel="icon" 	   type="image/x-icon"	href="<c:url value="/themes/theme2/img/icon_tcc.png"/>"/>
        <link rel="stylesheet" type="text/css" 		href="<c:url value="/themes/theme2/style2.css"/>" />
        <script src="<c:url value="/themes/theme2/jquery-1.11.1.min.js"/>"></script>
        <script type="text/javascript">
            $(function () {
                $('input').keypress(function (e) {
                    if (e.which === 13) {
                        $('fm1').submit();
                    }
                });
                $('#username').focus();
            });
            function setCookie() {
                var expires;
                var rememberPassword;
                if ($('#rememberPassword :checkbox:checked').length === 1) {
                    var expire_days = 365; // 過期日期(天)
                    var d = new Date();
                    d.setTime(d.getTime() + (expire_days * 24 * 60 * 60 * 1000));
                    expires = "expires=" + d.toGMTString();
                    rememberPassword = 'true';
                } else {
                    var d = new Date();
                    d.setTime(d.getTime() -1);
                    expires = "expires=" + d.toGMTString();
                    rememberPassword = 'false';
                }
                document.cookie = "username=" + $('#username').val() + "; " + expires + "; path=/ecsso/;";
                document.cookie = "password=" + $('#password').val() + "; " + expires + "; path=/ecsso/;";
                document.cookie = "rememberPassword=" + rememberPassword + "; " + expires + "; path=/ecsso/;";
            }

            function getCookie(name) {
                var value = "; " + document.cookie;
                var parts = value.split("; " + name + "=");
                if (parts.length === 2)
                    return parts.pop().split(";").shift();
            }

            function restoreUsernamePassword() {
                var username = getCookie('username');
                var password = getCookie('password');
                var rememberPassword = getCookie('rememberPassword');
                if (rememberPassword === 'true') {
                    $('#username').val(username);
                    $('#password').val(password);
                    document.getElementById("checkbox").checked = true;
                }
            }
        </script>
    </head>

    <body onload="restoreUsernamePassword()">
        <div id="header">
            <img class="logo" src="<c:url value="/themes/theme2/img/logo.png"/>"/>
            <span class="title"><spring:message code="screen.ecsso.title"/></span>
        </div>
        <form:form method="post" id="fm1" cssClass="fm-v clearfix" commandName="${commandName}" htmlEscape="true">
            <form:errors path="*" id="msg" cssClass="errors" element="div" />
            <div id="container">
<!--                <ul>
                    <li class="portal active">
                        <img src="<c:url value="/themes/theme2/img/icon_tcc.png"/>"><br />T'CEMENT<br />
                    </li>
                    <li class="portal">
                        <a href="${WeiboClientUrl}&forcelogin=true">
                            <img src="<c:url value="/themes/theme2/img/icon_weibo.png"/>"><br />WEIBO<br />
                        </a>
                    </li>
                    <li class="portal">
                        <a href="${QqClientUrl}">
                            <img src="<c:url value="/themes/theme2/img/icon_qq.png"/>"><br />QQ<br />
                        </a>
                    </li>
                </ul>-->
                <div class="login">			
                    <div class="caption"><spring:message code="screen.welcome.button.login"/></div>
                    <div>
                        <div class="iconCont">
                            <img src="<c:url value="/themes/theme2/img/login_id_gray.png"/>"/>
                        </div>
                        <div class="inputCont">
                            <spring:message code="username.label" var="usernameLabel" />
                            <form:input cssClass="input" cssErrorClass="input_error" id="username" tabindex="1" path="username" autocomplete="false" htmlEscape="true" size="5"
                                        placeholder="${usernameLabel}"/>
                        </div>
                    </div>
                    <div>
                        <div class="iconCont">
                            <img src="<c:url value="/themes/theme2/img/login_pswd_gray.png"/>"/>

                        </div>
                        <div class="inputCont">
                            <spring:message code="password.label" var="passwordLabel" />
                            <form:password cssClass="input" cssErrorClass="input_error" id="password" tabindex="2" path="password" htmlEscape="true" autocomplete="off"
                                           placeholder="${passwordLabel}"/>
                        </div>
                    </div>

                    <div style="margin:10px 0 0 20px !important;">
                        <fieldset id="rememberPassword" style="border: 0px; margin: 0 0 0 0;">
                            <input type="checkbox" id="checkbox" tabindex="3"  value="true"><spring:message code="screen.ecsso.rememberPassword"/>
                        </fieldset>
                    </div>
                    <div>
                        <div class="forgetCont" style="margin: 10px 0 0 0;">
                            <a href="/tccstore/forgotPassword" class="forget"><spring:message code="screen.ecsso.link.forgotPassword" /></a>
                        </div>
                        <div class="btnCont" style="margin: 10px 0 0 0;">
                            <input style="font-weight: bold;" class="loginBtn" id="submit" name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="4" type="submit"
                                   onclick="setCookie();"/>
                        </div>
                    </div>
                </div>
                <input type="hidden" name="lt" value="${loginTicket}" />
                <input type="hidden" name="execution" value="${flowExecutionKey}" />
                <input type="hidden" name="_eventId" value="submit" />
            </div>                            
        </form:form>
    </body>
</html>