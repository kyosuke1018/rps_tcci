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
$(function(){
	var documentWidth = document.documentElement.clientWidth;
	var documentHeight = document.documentElement.clientHeight;
	$('#msg').css('left', $('#login').offset().left + 780);
});	
</script>
</head>

<body>
	<div id="loginBG">
		<div id="login">
			<img class="logo" src="<c:url value="/themes/theme1/img/tcc.png"/>"/>
			<div class="title"><spring:message code="screen.ecsso.title"/></div>
			<div class="user">
			</div>
		</div>
		<div id="msgWrapper">
			<div id="msg" class="success">
				<h2><spring:message code="screen.success.header" /></h2>
				<p><spring:message code="screen.success.success" /></p>
				<p><spring:message code="screen.success.security" /></p>
			</div>
		</div>
	</div>	
	
	<div class="clear"></div>
	
	<div id="background"></div>	
</body>
</html>