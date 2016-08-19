<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en"> <!--<![endif]-->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="viewport" content="width=device-width";>
        <title>CRM-台泥电子商务-忘记密码</title>
        <link rel="icon" type="image/x-icon" href="resources/images/icon_tcc.png"/>
        <link rel="stylesheet" type="text/css" href="css/resetPswd.css" />	
    </head>
    <body>
        <form action="forgotPassword" class="login" method="post">
            <div id="header">
                <img class="logo" src="resources/images/logo-logout.png"/>
                <span class="title">CRM-台泥电子商务</span>
            </div>
            <div id="container">
                <div class="resetPswd">
                    <div class="caption">忘记密码</div>			
                    <%
                        final String message = (String) request.getAttribute("message");
                        if (message != null) {
                    %>
                    <br/>
                    <span style="color:red;"><%= message%></span>
                    <%
                        }
                    %>
                    <input type="email" name="account" class="input" placeholder="name@example.com" autofocus>
                    <div class="cont">
                        <a href="faces/index.xhtml">回首页</a>
                        <input type="submit" value="送出" class="forgetBtn">
                        <input type="hidden" name="submit" value="submit">
                    </div>
                    <div class="note">
                        <p>填入您注册的电子信箱后按送出，系统将<b>重设新密码</b>并寄送新密码到您的信箱。</p>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
