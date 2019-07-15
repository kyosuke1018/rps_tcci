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
        <title>TCC Group Single Sing On System</title>
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
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Cache-Control" content="no-cache">
        <meta http-equiv="Expires" content="0">
    </head>
    <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

        <table width="517" height="486" border="0" align="center" cellpadding="0" cellspacing="0" class="tcc-login">
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td colspan="3" align="center" style="color:#0066CC;font-size:36px;font-weight:bold;"><spring:message code="screen.tcc_sso.title"/></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td width="310px">&nbsp;</td>
                <td width="60px" valign="top" ><p>&nbsp;</p><p>&nbsp;</p><img src="./images/confirm.gif"/></td>
                <td align="right" valign="center" style="padding-right:65px;padding-bottom:65px; font-family: Arial, Helvetica, sans-serif;font-size: 12px;">
                    <table border="0" cellspacing="3" cellpadding="0" style="font-size:12px;font-weight:bold;">
                        <tr>
                            <td align="left">
                                <h2 style="color: #0066CC;"><spring:message code="screen.success.header" /></h2>
                                <p style="color: #0066CC;"><spring:message code="screen.success.success" /></p>
                                <p style="color: #0066CC;"><spring:message code="screen.success.security" /></p>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </body>
</html>
