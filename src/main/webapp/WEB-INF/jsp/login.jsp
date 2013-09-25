<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title></title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<!-- main css compiled from main.less -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/dspResources/css/main.css?v=2">

<!-- default theme -->
<!-- <link rel="stylesheet" href="<%=request.getContextPath()%>/dspResources/css/themes/theme-default.css"> -->

<!-- Health-e-link theme -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/dspResources/css/themes/theme-health-e-link.css">

<!--[if lt IE 9]>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/dspResources/css/ie.css">
<![endif]-->
</head>
<body class="theme-default" id="login" onload='document.f.j_username.focus();' >

	<!--[if lt IE 8]>
	    <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
	<![endif]-->

	<div class="login-container">
		<c:if test="${not empty error}">
			<p class="login-error">
				Your login attempt was not successful, try again.<br /> Caused :
				${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
			</p>
		</c:if>
		<div class="login clearfix">
			<header class="login-header">
				<div class="login-header-content"><span class="logo" alt="{Company Name Logo}"></span></div>
			</header>
			<form role="form" name='f' action="<c:url value='j_spring_security_check' />" method='POST'>
				<div class="form-group">
					<label for="username">Username</label>
					<input id="username" name='j_username' class="form-control" type="text" />
				</div>
				<div class="form-group">
					<label for="passwrod">Password</label>
					<input id="password" name='j_password' class="form-control" type="password" />
				</div>
				<input type="submit" value="Login" class="btn btn-primary pull-right"/>
				<label for="remember-me" class="pull-left"><input type="checkbox" id="remember-me" value="1">&nbsp;Remember Me</label>
			</form>
		</div>
		<p class="login-note"><a href="" title="">Forgot Password</a></p>
	</div>

	<!--
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
	<script>window.jQuery || document.write('<script src="<%=request.getContextPath()%>/dspResources/js/vendor/jquery-1.10.1.min.js"><\/script>')</script>
	-->

<script data-main="<%=request.getContextPath()%>/dspResources/js/main" src="<%=request.getContextPath()%>/dspResources/js/vendor/require.js"></script>
</body>
</html>
