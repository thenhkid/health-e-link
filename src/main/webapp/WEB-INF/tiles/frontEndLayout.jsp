<%-- 
    Document   : frontEndLayout
    Created on : Nov 27, 2013, 8:51:51 AM
    Author     : chadmccue
--%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<jsp:text><![CDATA[<!--[if lte IE 7]>]]></jsp:text><html class="no-js lt-ie9 lt-ie8 lt-ie7"><jsp:text><![CDATA[<![endif]-->]]></jsp:text>
<jsp:text><![CDATA[<!--[if IE 7]>]]></jsp:text><html class="no-js lt-ie9 lt-ie8"><jsp:text><![CDATA[<![endif]-->]]></jsp:text>
<jsp:text><![CDATA[<!--[if IE 8]>]]></jsp:text><html class="no-js lt-ie9"><jsp:text><![CDATA[<![endif]-->]]></jsp:text>
<jsp:text><![CDATA[<!--[if gt IE 8]>]]></jsp:text><html class="no-js"><jsp:text><![CDATA[<![endif]-->]]></jsp:text>
<head>
    <meta charset="utf-8">	
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title><tiles:insertAttribute name="title" /></title>
    <!%-- main css compiled from main.less --%>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/dspResources/css/main-fe.css?v=2">

    <!%-- Health-e-link theme --%>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/dspResources/css/themes/theme-health-e-link-fe.css">
    <jsp:text><![CDATA[<!--[if lte IE 9]>]]></jsp:text>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/dspResources/css/ie.css">
    <jsp:text><![CDATA[<![endif]-->]]></jsp:text>
    <script src="<%=request.getContextPath()%>/dspResources/js/vendor/jquery-1.10.1.min.js"></script>
    <!%-- moderizer: for ie8 compatibility --%>
    <script type="text/javascript" src="<%=request.getContextPath()%>/dspResources/js/vendor/modernizr-2.6.2-respond-1.1.0.min.js"></script>
    <script data-main="<%=request.getContextPath()%>/dspResources/js/main" src="<%=request.getContextPath()%>/dspResources/js/vendor/require.js"></script>
</head>
<body <c:if test="${param['page']== 'home'}">class="home theme-health-e-link"</c:if>>
    <div class="wrap">
        <tiles:insertAttribute name="header" /> 
        <tiles:insertAttribute name="body" />
        <tiles:insertAttribute name="footer" />
    </div>
</body>
</html>