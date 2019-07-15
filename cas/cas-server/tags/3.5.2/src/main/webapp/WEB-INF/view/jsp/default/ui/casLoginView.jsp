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
            <link rel="stylesheet" href="<c:url value="/css/2017_recyclable.css" />" type="text/css" media="screen" />
            <link rel="icon" href="<c:url value="/favicon.ico"/>"/>
            <script type="text/javascript" src="<c:url value="/js/jquery-1.8.0.min.js" />"></script>
            <script type="text/javascript" src="<c:url value="/js/jquery.snow.min.1.0.js" />"></script>
            <script type="text/javascript" src="<c:url value="/js/jquery.ba-throttle-debounce.min.js" />"></script>
            <script type="text/javascript" src="<c:url value="/js/2016_warroom2.js" />"></script>
            <script type="text/javascript">
                //<![CDATA[
                setTimeout(function ()
                {
                    $(".tag").click(function () {
                        var index = $(this).index();

                        $(".tag").removeClass("tagFocus");
                        $(".tag:eq(" + index + ")").addClass("tagFocus");
                        $(".tab").css("display", "none");
                        $(".tab:eq(" + index + ")").css("display", "block");
                    })
                    resize();
                    document.getElementById('username').focus();
                }, 200);
                $(window).on('resize', $.debounce(100, resize));
                $(function ()
                {
                    resize();
                });
                //for tms sytem (iframe).
                if (window.top !== window.self) {
                    window.top.location = window.location;
                }
                //]]>
            </script>
            <meta http-equiv="Pragma" content="no-cache">
            <meta http-equiv="Cache-Control" content="no-cache">
            <meta http-equiv="Expires" content="0">
            <meta http-equiv="X-UA-Compatible" content="IE=9" />
        </head>
        <body>
            <div class="background" style="text-align: center;">
                <img class="background" src="<spring:message code='screen.tcc_sso.background_image'/>"/>
                <img class="logo" src="images/2017_recyclable/logo.gif"/>
                <form:form method="post" id="fm1" cssClass="fm-v clearfix" cssStyle="margin: 0px" commandName="${commandName}" htmlEscape="true">
                    <div class="login">
<!--                        <div class="tagBar">
                            <span class="tag tagFocus">TCC</span>
                            <span class="tag">CCIL</span>
                        </div>-->
                        <div class="tcc tab">
                            <table  border="0" cellspacing="0" cellpadding="0" style="white-space: nowrap;border-radius: 5px;padding:3px;">
                                <tr>
                                    <td colspan="2" style="padding: 5px 5px 10px 8px">
                                        <div class="title"><font style="font-size: <spring:message code="screen.tcc_sso.title.fontsize"/>;"><spring:message code="screen.tcc_sso.title"/></font></div>
                                        <div class="clear"></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="left" style="margin-right: 10px; margin-left: 5px;">
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
                                    <td>
                                        <div class="right" style="margin-right:5px;">
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
                        <div class="ccil tab">
                            <div class="title">
                                <a href="${CasWrapperProvider20Url}" style="text-decoration: none;">
                                    CCIL Login
                                    <img class="otherLoginBtn" src="images/otherLogin.png">
                                </a>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </body>
    </html>
</c:if>

<c:if test="${browserType eq 'mobile'}" >
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
            <meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;"/>
            <title><spring:message code="screen.tcc_sso.title"/></title>
            <link rel="stylesheet" type="text/css" href="css/mobile.css" />
            <link rel="icon" href="<c:url value="/favicon.ico"/>"/>
            <script src="js/jquery-1.11.1.min.js"></script>
            <script type="text/javascript">
                                                function lowCaseConverter() {
                                                    var jid = document.getElementById("username");
                                                    var data = jid.value;
                                                    jid.value = data.toLowerCase().trim();
                                                    jid = document.getElementById("password");
                                                    data = jid.value;
                                                    jid.value = data.trim();
                                                    return true;
                                                }
                                                $(function () {
                                                    var documentWidth = document.documentElement.clientWidth;
                                                    var documentHeight = document.documentElement.clientHeight;
                                                    var picArray = new Array("logo_tcc", "logo_csrc", "logo_hpipc", "logo_hppc", "logo_kho", "logo_kric", "logo_molicel", "logo_sking", "logo_sole", "logo_taho",
                                                            "logo_tccc", "logo_tcchk", "logo_tcci", "logo_tcec", "logo_thmc", "logo_ton", "logo_tpcc");
                                                    var cloneArray = picArray.slice();
                                                    var imgLoaded = 0;
                                                    var inputFieldW = parseInt($(".inputField").css("width"));
                                                    var bottomLineH = parseInt($("#container .bottomLine").css("height"));


                                                    $(".bottomLine").css("bottom", "0px");
                                                    $(".inputField").css("top", documentHeight / 3.7 + "px");
                                                    $(".inputField").css("left", documentWidth / 2 - inputFieldW / 2 + "px");
                                                    $(".mainTitle img").attr("src", "images/mobile/mainTitle.png?" + new Date().getTime());
                                                    $(".mainTitle img").load(function () {
                                                        var mainTitleAniTop = 30;
                                                        var mainTitleH = parseInt($(this).css("height"));
                                                        var inputFieldTop = parseInt($(".inputField").css("top"));

                                                        $(this).parent().css("opacity", 1);
                                                        $(this).parent().css("top", documentHeight / 2 - mainTitleH / 2 - bottomLineH - 50 + "px");

                                                        if ((mainTitleAniTop + mainTitleH) > inputFieldTop)
                                                            $(".inputField").css("top", mainTitleAniTop + mainTitleH + 200 + "px");

                                                        setTimeout(function () {
                                                            $(".mainTitle").animate({top: mainTitleAniTop + "px"}, 1000)
                                                        }, 1500);

                                                        setTimeout(function () {
                                                            $(".inputField").animate({opacity: 1}, 1000);
                                                        }, 2000);
                                                        setTimeout(function () {
                                                            $(".inputFieldFooter").animate({opacity: 1}, 1000);
                                                        }, 2000);
                                                    });

                                                    var logoMargin = parseInt($(".logo").css("margin-left")) * 6 + 10;
                                                    $(".logo").css("width", (documentWidth - logoMargin) / 3 + "px");
                                                    $(".logo").each(function () {
                                                        var num = Math.floor(Math.random() * cloneArray.length);
                                                        var name = cloneArray[num];
                                                        cloneArray.splice(num, 1);
                                                        $(this).attr("alt", name);
                                                        $(this).attr("src", "images/mobile/" + name + ".png?" + new Date().getTime());
                                                        imgLoaded++;
                                                    });

                                                    cloneArray = picArray.slice();
                                                    $(".logo").load(function () {
                                                        if (imgLoaded == cloneArray.length) {
                                                            $("#container").css("height", parseInt($("#container").css("height")) + "px");

                                                            $(".logo").each(function (index) {
                                                                var num = Math.floor(Math.random() * cloneArray.length);
                                                                var name = cloneArray[num];
                                                                cloneArray.splice(num, 1);
                                                                setTimeout(function () {
                                                                    $(".logo[alt='" + name + "']").animate({opacity: 0.07}, 1000);
                                                                }, index * 350);
                                                            });
                                                        }
                                                    });
                                                });
            </script>            
        </head>
        <body>
            <form:form method="post" id="mobileForm" commandName="${commandName}" htmlEscape="true">
                <div id="building">	
                    <div id="headerField">
                    </div>	

                    <div id="container">
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo1" class="logo">
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo2" class="logo">
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo3" class="logo">
                        <br>
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo4" class="logo">
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo5" class="logo">
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo6" class="logo">
                        <br>
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo7" class="logo">
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo8" class="logo">
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo9" class="logo">
                        <br>
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo10" class="logo">
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo11" class="logo">
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo12" class="logo">
                        <br>
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo13" class="logo">
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo14" class="logo">
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo15" class="logo">
                        <br>
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo16" class="logo">
                        <img src="images/mobile/transparent_w80.png" alt="canAni" id="logo17" class="logo">

                        <div class="mainTitle">
                            <img src="images/mobile/mainTitle.png">
                        </div>
                        <div class="inputField">
                            <div class="inputFieldHeader"></div>
                            <br>
                            <div class="inputTitle"><spring:message code="screen.welcome.label.netid" /></div>
                            <c:if test="${not empty sessionScope.openIdLocalId}">
                                <strong>${sessionScope.openIdLocalId}</strong>
                                <input type="hidden" id="username" name="username" value="${sessionScope.openIdLocalId}" />
                            </c:if>
                            <c:if test="${empty sessionScope.openIdLocalId}">
                                <spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
                                <form:input cssClass="required" cssErrorClass="error" id="username" size="12" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="off" htmlEscape="true" />
                            </c:if>
                            <br><br>
                            <div class="inputTitle"><spring:message code="screen.welcome.label.password" /></div>
                            <spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
                            <form:password cssClass="required" style="margin-left: 2px;" cssErrorClass="error" id="password" size="12" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
                            <br><br>
                            <form:errors path="*" style="color:red; align: left;"/>
                            <div class="inputFieldFooter">
                                <input type="hidden" name="lt" value="${loginTicket}" />
                                <input type="hidden" name="execution" value="${flowExecutionKey}" />
                                <input type="hidden" name="_eventId" value="submit" />
                                <input style="font-weight: bold;font-size: <spring:message code="screen.tcc_sso.loginbutton.fontsize" />px; font-family:'<spring:message code="screen.tcc_sso.title.font" />';" class="submit" id="submit" name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="3" type="submit" onclick="lowCaseConverter()"/>
                            </div>
                        </div>

                        <div class="bottomLine">
                            <div class="minLine"></div>
                        </div>
                    </div>	

                </div>   
            </form:form>
        </body>
    </html>
</c:if>
