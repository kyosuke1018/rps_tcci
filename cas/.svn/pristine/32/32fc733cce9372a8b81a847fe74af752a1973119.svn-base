<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf8">
        <title><spring:message code="screen.tcc_sso.title"/></title>
        <style>
            body
            {	
                margin: 0;
                background: url(images/login/back1-1.png) repeat-x;
            }

            #building
            {
                min-width: 960px;	
                visibility: hidden;
            }

            #building table
            {
                width: 100%;
                min-height: 600px;
                margin: 0px auto 0px auto;

                table-layout: fixed;

                background: url(images/login/back1-2.png) repeat-x bottom;
            }

            #building .logoField
            {	
                width: 100%;	
                margin: auto;
            }

            #building .loginField
            {	
                width: 402px;
                height: 408px;
                margin: auto;

                position: relative;

                background: url(images/login/input_other.png) no-repeat center center;
            }

            #building .loginField #title
            {
                width: 255px;
                height: 18px;

                position: absolute;
                top: 61px;
                left: 90px;
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
                top: 125px;
                left: 55px;

                border-radius: 5px;	
            }

            #building .loginField #message
            {
                width: 230px;
                height: 18px;

                position: absolute;
                top: 127px;
                left: 115px;

                border-radius: 5px;	
            }

            #building .loginField #username
            {
                width: 115px;
                height: 18px;

                position: absolute;
                top: 110px;
                left: 188px;

                border-radius: 5px;	
            }

            #building .loginField #password_label
            {
                width: 115px;
                height: 18px;

                position: absolute;
                top: 141px;
                left: 120px;

                border-radius: 5px; 
            }

            #building .loginField #password
            {
                width: 115px;
                height: 18px;

                position: absolute;
                top: 144px;
                left: 188px;

                border-radius: 5px; 
            }

            #building .loginField #error
            {
                width: 115px;
                height: 18px;
                color:red;
                font-size:12px;
                position: absolute;
                top: 171px;
                left: 60px;

                border-radius: 5px; 
            }


            #building .loginField #login
            {
                width: 58px;
                height: 23px;

                position: absolute;
                top: 178px;
                left: 258px;

            }

            #building .loginField #login:active
            {
                width: 58px;
                height: 23px;

                position: absolute;
                top: 178px;
                left: 248px;
            }

        </style>
        <script type="text/javascript" src="<c:url value="/js/jquery-1.8.0.min.js" />"></script>
        <script type="text/javascript">
            function getViewportHeight() 
            {
                if (document.documentElement.clientHeight)
                {
                    return document.documentElement.clientHeight;
                }
                else if (document.body && document.body.offsetHeight)
                {
                    return document.body.offsetHeight;
                }
                else
                {
                    return 0;
                }
            }
            //維持畫面垂直置中
            $(function()
            {
                var htmlBodyHeight = 0;

                htmlBodyHeight = getViewportHeight();
                setTimeout(function(){
                    $("#building table .logoField img").css("width", htmlBodyHeight*0.7 + "px");
                },100);
                setTimeout(function(){
                    $("#building table").css("margin-top", (htmlBodyHeight - parseInt($("#building table").css("height"), 10) )/2 + "px" );
                    $("#building").css("visibility","visible");
                },100);
            });
        </script>
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Cache-Control" content="no-cache">
        <meta http-equiv="Expires" content="0">
    </head>
    <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <div id="building">

            <table border="0">
                <tr>
                    <td style="width:52%">
                        <div class="logoField">
                            <center><img src="images/login/TCC_item.png"></center>
                        </div>
                    </td>

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
