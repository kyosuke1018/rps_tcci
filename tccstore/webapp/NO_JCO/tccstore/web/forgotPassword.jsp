<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>忘记密码?</title>
        <link rel="stylesheet" href="css/forgotPassword.css">
    </head>
    <body>
        <form action="forgotPassword" class="login" method="post">
            <h1>忘记密码?</h1>
            <%
                final String message = (String) request.getAttribute ("message");
                if (message != null) {
            %>
            <p class="alert"><%= message %></p>
            <br>
            <%
                }
            %>
            <input type="email" name="account" class="login-input" placeholder="name@example.com" autofocus>
            <input type="submit" value="送出" class="login-submit">
            <input type="hidden" name="submit" value="submit">
            <p>填入您注册的电子信箱后按送出，系统将<span class="alert">重设新密码</span>并寄送新密码到您的信箱。</p>
            <a href="faces/index.xhtml">回首页</a>
        </form>
    </body>
</html>
