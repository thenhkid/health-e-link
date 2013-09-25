<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title><tiles:insertAttribute name="title" /></title>
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

<script src="<%=request.getContextPath()%>/dspResources/js/vendor/jquery-1.10.1.min.js"></script>

<!-- moderizer: for ie8 compatibility -->
<script type="text/javascript" src="<%=request.getContextPath()%>/dspResources/js/vendor/modernizr-2.6.2-respond-1.1.0.min.js"></script>
<script data-main="<%=request.getContextPath()%>/dspResources/js/main" src="<%=request.getContextPath()%>/dspResources/js/vendor/require.js"></script>
</head>
<body>
<!--[if lt IE 7]>
 	<p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
<![endif]-->
<div class="wrap">
<tiles:insertAttribute name="header" />
<tiles:insertAttribute name="actions" />
<div class="container-fluid">
	<div class="row-fluid">
		<tiles:insertAttribute name="menu" />
		<tiles:insertAttribute name="body" />
	</div>
</div>
<tiles:insertAttribute name="footer" />
</div>

</body>
</html>