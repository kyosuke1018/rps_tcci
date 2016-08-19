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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
        <title><spring:message code="screen.ecsso.title"/></title>
        <link rel="icon" 	   type="image/x-icon"	href="<c:url value="/themes/theme1/img/browserIcon.png"/>"/>
        <link rel="stylesheet" type="text/css" 		href="<c:url value="/themes/theme1/style.css"/>" />
        <script src="<c:url value="/themes/theme1/jquery-1.11.1.min.js"/>"></script>

        <script type="text/javascript">
            $(function () {
                var documentWidth = document.documentElement.clientWidth;
                var documentHeight = document.documentElement.clientHeight;
                $('#msg').css('left', $('#login').offset().left + 780);
                $('div.loginBtn').click(function () {
                    $('#fm1').submit();
                });
                $('input').keypress(function (e) {
                    if (e.which == 13) {
                        $('#fm1').submit();
                        return false;
                    }
                });
                $('#username').focus();
            });
        </script>
    </head>

    <body>
        <div id="loginBG">
            <form:form method="post" id="fm1" cssClass="fm-v clearfix" commandName="${commandName}" htmlEscape="true">
                <div id="login">
                    <img class="logo" src="<c:url value="/themes/theme1/img/tcc.png"/>"/>
                    <div class="title"><spring:message code="screen.ecsso.title"/></div>
                    <div class="user">
                        <img class="icon" src="<c:url value="/themes/theme1/img/id.png"/>"/>
                        <form:input cssClass="required" cssErrorClass="error" id="username" tabindex="1" path="username" autocomplete="false" htmlEscape="true" size="5" />
                        <img class="icon" src="<c:url value="/themes/theme1/img/password.png"/>"/>
                        <form:password cssClass="required" cssErrorClass="error" id="password" tabindex="2" path="password" htmlEscape="true" autocomplete="off" />
                        <div class="loginBtn" ><spring:message code="screen.welcome.button.login" /></div>
                        <a href="/tccstore/forgotPassword"><spring:message code="screen.ecsso.link.forgotPassword" /></a>
                        <spring:message code="screen.ecsso.otherLogin"/>
                        <a href="${WeiboClientUrl}" title="<spring:message code="screen.ecsso.button.weiboLogin" />"><img src="<c:url value="/themes/theme1/img/weibo.png"/>"/></a>
                        <a href="${QqClientUrl}" title="<spring:message code="screen.ecsso.button.qqLogin" />"><img src="<c:url value="/themes/theme1/img/qq.png"/>"/></a>
                        <!--<a href="${GitHubClientUrl}"><img src="<c:url value="/themes/theme1/img/github.png"/>"/></a>-->
                        <input type="hidden" name="lt" value="${loginTicket}" />
                        <input type="hidden" name="execution" value="${flowExecutionKey}" />
                        <input type="hidden" name="_eventId" value="submit" />
                    </div>

                </div>
                <div id="msgWrapper">
                    <form:errors path="*" id="msg" cssClass="errors" element="div" />

                </div>

            </form:form>
        </div>	

        <div class="clear"></div>

        <div id="background"></div>	
    </body>
</html>