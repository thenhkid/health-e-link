<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<header id="header" class="header" role="banner">
    <div class="header-inner">
        <nav class="navbar primary-nav" role="navigation">
            <div class="container">
                <div class="navbar-header">
                    <a href="<c:url value='/'/>" class="navbar-brand" title="Data exchange software connecting the healthcare community">
                        <!--
                                <img src="img/health-e-link/img-health-e-link-logo.png" class="logo" alt="Health-e-Link Logo"/>
                                Required logo specs:
                                logo width: 125px
                                logo height: 30px

                                Plain text can be used without image logo

                                sprite can be used with class="logo":
                        -->
                        <span class="identity logo" alt="Health-e-Link Universal Translator" title="Data exchange software connecting the healthcare community"></span>
                    </a>
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                </div>
                <div class="collapse navbar-collapse navbar-ex1-collapse">
                    <ul class="nav navbar-nav navbar-right">
                        <li ${param['page'] == 'about' ? 'class="active"' : ''}>
                            <a href="javascript:void(0);" title="About" data-toggle="dropdown">About <b class="caret"></b></a>
                            <ul class="dropdown-menu" role="menu" aria-labelledby="About dropdown">
                                <li><a href="<c:url value='/about'/>" title="About Health-e-Link">About Health-e-Link</a></li>
                                <li><a href="<c:url value='/about/network-capabilities'/>" title="Network Capabilities">Network Capabilities</a></li>
                            </ul> 
                        </li>
                        <li ${param['page'] == 'productSuite' ? 'class="active"' : ''}>
                            <a href="javascript:void(0);" title="Product Suite" data-toggle="dropdown">Product Suite <b class="caret"></b></a>
                            <ul class="dropdown-menu" role="menu" aria-labelledby="Product Suite dropdown">
                                <%--<li><a href="<c:url value='/product-suite'/>" title="Product Suite Overview">Product Suite Overview</a></li> --%>
                                <li><a href="<c:url value='/#productSuite'/>" title="Product Suite Overview">Product Suite Overview</a></li>
                                <li><a href="<c:url value='/product-suite/careConnector'/>" title="Care Connector">Care Connector</a></li>
                                <li><a href="<c:url value='/product-suite/universal-hie'/>" title="Universal HIE">Universal HIE</a></li>
                                <li><a href="<c:url value='/product-suite/clinical-data-warehouse'/>" title="Clinical Data Warehouse">Clinical Data Warehouse</a></li>
                            </ul> 
                        </li>
                        <li ${param['page'] == 'solutions' ? 'class="active"' : ''}>
                            <a href="<c:url value='/support-services'/>" title="Support Services">Support Services</a>
                            <%--<a href="javascript:void(0);" title="Solutions" data-toggle="dropdown">Consulting Services  <b class="caret"></b></a>
                            <ul class="dropdown-menu" role="menu" aria-labelledby="Solutions dropdown">
                                <li><a href="<c:url value='/solutions'/>" title="Solutions Overview">Solutions Overview</a></li>
                                <li><a href="<c:url value='/solutions/services'/>" title="Services">Services</a></li>
                                <li><a href="<c:url value='/solutions/case-studies'/>" title="Case Studies">Case Studies</a></li>
                            </ul> --%>
                        </li>
                        <%--<li ${param['page'] == 'partners' ? 'class="active"' : ''}><a href="<c:url value='/partners'/>" title="Partners">Partners</a></li>--%>
                        <li ${param['page'] == 'contact' ? 'class="active"' : ''}><a href="<c:url value='/contact'/>" title="Contact Us">Contact Us</a></li>
                        <li ${param['page-id'] == 'profile' ? 'class="active"': ''}>
                            <c:choose>
                                <c:when test="${not empty pageContext.request.userPrincipal.name}">
                                    <a href="javascript:void(0);" title="My Account" data-toggle="dropdown">My Account <b class="caret"></b></a>
                                    <ul class="dropdown-menu" role="menu" aria-labelledby="My account dropdown">
                                        <li><a href="<c:url value='/profile'/>" title="My Account">My Account</a></li>
                                         <li><a href="#settingsModal" id="settings" data-toggle="modal" title="Account Settings" class="settings">Account Settings</a></li>
                                        <c:if test="${not empty userAccess}">
                                            <c:forEach items="${userAccess}" var="sections" varStatus="aStatus">
                                                <li>
                                                    <c:choose>
                                                        <c:when test="${sections.featureId == 3}"><a href="<c:url value='/Health-e-Web/inbox'/>" title="Health-e-Web">ERG</a></c:when>
                                                        <c:when test="${sections.featureId == 4}"><a href="<c:url value='/Health-e-Connect/upload'/>" title="Health-e-Connect">File Exchange</a></c:when>
                                                        <c:when test="${sections.featureId == 5}"><a href="<c:url value='/OrgProfile/editProfile'/>" title="Organization Profile">Organization Profile</a></c:when>
                                                    </c:choose>
                                                </li>
                                            </c:forEach> 
                                        </c:if>
                                        <li><a title="log out" href="<c:url value='/logout' />">Log out</a></li>
                                    </ul> 
                                </c:when>
                                <c:otherwise>
                                    <a href="<c:url value='/login' />" title="Log In">Log In</a>
                                </c:otherwise>
                            </c:choose>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <c:choose>
            <c:when test="${param['page'] == 'home'}">
                <div class="central-graphic container">
                    <h1>Promoting Better Health and Health Care <small>One Community at a Time</small></h1>


                    <div class="central-graphic-image image-a">
                        <img src="<%=request.getContextPath()%>/dspResources/img/front-end/health-e-link/photowithchild.jpg" alt="Process referrals and feedback reports electronically with all partners in your care network." />
                        <div class="caret"></div>
                    </div>

                    <div class="central-graphic-image image-b">
                        <img src="<%=request.getContextPath()%>/dspResources/img/front-end/health-e-link/gymPhoto.jpg" alt="Connect healthcare provider EMR systems with partner systems and exchange patient data seamlessly." />
                        <div class="caret"></div>
                    </div>

                    <div class="central-graphic-image image-c">
                        <img src="<%=request.getContextPath()%>/dspResources/img/front-end/health-e-link/reportPhoto.jpg" alt="Securely and effectively manage patient data across multiple programs of care." />
                        <div class="caret"></div>
                    </div>


                    <div class="central-graphic-content">
                        <h3>Our products and services enable health information exchange between<br/>
                            groups of health care providers, community partners, as well as federal, state and local health agencies.</h3>

                        <a href="<c:url value='/about/network-capabilities'/>" title="Network Capabilities" class="btn btn-primary btn-action btn-primary-action">Network Capabilities</a>
                        <a href="<c:url value='/support-services'/>" title="Support Services" class="btn btn-primary btn-action">Support Services</a>
                    </div>
                </div>
               
            </c:when>
            <c:otherwise>
                <div class="container">
                    <c:choose>
                        <c:when test="${param['page-section'] == 'Health-e-Web'}"><h1 class="page-title"><span class="page-title-icon pull-left"></span>Electronic Referral Gateway</h1></c:when>
                        <c:when test="${param['page-section'] == 'Health-e-Connect'}"><h1 class="page-title"><span class="page-title-icon pull-left"></span>File Exchange</h1></c:when>
                        <c:when test="${param['page-section'] == 'Org'}"><h1 class="page-title"><span class="page-title-icon pull-left"></span>Organization Profile</h1></c:when>
                        <c:otherwise>
                            <h1 class="page-title">${pageTitle}</h1>
                        </c:otherwise>
                    </c:choose>
                </div>   
            </c:otherwise>
        </c:choose>

        <%-- Section Nav --%>
        <c:choose>
            <c:when test="${param['page-section'] == 'Health-e-Web'}">
                <nav class="navbar navbar-default actions-nav" role="navigation">
                    <div class="container">
                        <ul class="nav navbar-nav navbar-actions">
                            <li ${(param['page'] == 'inbox' || fromPage == 'inbox' || pageHeader == 'feedback') && fromPage != 'sent' ? 'class="active"' : ''}><a href="<c:url value='/Health-e-Web/inbox'/>" title="Inbox" class="btn btn-link"><span class="glyphicon glyphicon-inbox"></span>&nbsp; Inbox <c:if test="${inboxTotal > 0}"><span class="badge" style="background-color:transparent; border:1px solid #fff">${inboxTotal}</span></c:if></a><span class="indicator-active arrow-up"></span></li>
                            <li ${(param['page'] == 'sent' || fromPage == 'sent') && fromPage != 'pending' && fromPage != 'inbox' ? 'class="active"' : ''}><a href="<c:url value='/Health-e-Web/sent'/>" title="Sent Items" class="btn btn-link"><span class="glyphicon glyphicon-send"></span>&nbsp; Sent</a><span class="indicator-active arrow-up"></span></li>
                            <li ${param['page'] == 'pending' || fromPage == 'pending' ? 'class="active"' : ''}><a href="<c:url value='/Health-e-Web/pending'/>" title="Pending Items" class="btn btn-link"><span class="glyphicon glyphicon-time"></span>&nbsp; Pending <c:if test="${pendingTotal > 0}"><span class="badge" style="background-color:transparent; border:1px solid #fff">${pendingTotal}</span></c:if></a><span class="indicator-active arrow-up"></span></li>
                            <li ${param['page'] == 'history' ? 'class="active"' : ''}><a href="<c:url value='/Health-e-Web/history'/>" title="History" class="btn btn-link"><span class="glyphicon glyphicon-calendar"></span>&nbsp; History</a><span class="indicator-active arrow-up"></span></li>
                        </ul>
                        <c:if test="${userDetails.createAuthority == true}">
                            <ul class="nav navbar-nav navbar-right navbar-actions">
                                <li ${param['page'] == 'create' && pageHeader != 'feedback' ? 'class="active"' : ''}>
                                    <a href="<c:url value='/Health-e-Web/create'/>" title="Create New Referral" >Create New Referral</a>
                                    <span class="indicator-active arrow-up"></span>
                                </li>
                            </ul>
                        </c:if>
                    </div>
                </nav>
            </c:when>
            <c:when test="${param['page-section'] == 'Health-e-Connect'}">
                <nav class="navbar navbar-default actions-nav" role="navigation">
                    <div class="container">
                        <ul class="nav navbar-nav navbar-actions">
                            <li ${param['page'] == 'upload' ? 'class="active"' : ''}><a href="<c:url value='/Health-e-Connect/upload'/>" title="Upload New File" class="btn btn-link"><span class="glyphicon glyphicon-upload"></span>&nbsp; Upload Files</a><span class="indicator-active arrow-up"></span></li>
                            <li ${param['page'] == 'download' ? 'class="active"' : ''}><a href="<c:url value='/Health-e-Connect/download'/>" title="Download Files" class="btn btn-link"><span class="glyphicon glyphicon-download"></span>&nbsp; Download Files</a><span class="indicator-active arrow-up"></span></li>
                            <li ${param['page'] == 'audit' ? 'class="active"' : ''}><a href="<c:url value='/Health-e-Connect/auditReports'/>" title="View Audit Reports" class="btn btn-link"><span class="glyphicon glyphicon-calendar"></span>&nbsp; Audit Reports</a><span class="indicator-active arrow-up"></span></li>
                        </ul>

                        <c:if test="${userDetails.createAuthority == true && hasConfigurations == true}">
                            <ul class="nav navbar-nav navbar-right navbar-actions">
                                <li>
                                    <a href="#uploadFile" title="Upload File" data-toggle="modal" class="uploadFile">Upload New File</a>
                                    <span class="indicator-active arrow-up"></span>
                                </li>
                            </ul>
                        </c:if>
                    </div>
                </nav>
            </c:when>
            <c:when test="${param['page-section'] == 'Org'}">
                <nav class="navbar navbar-default actions-nav" role="navigation">
                    <div class="container">
                        <ul class="nav navbar-nav navbar-actions">
                            <li ${param['page'] == 'editProfile' ? 'class="active"' : ''}><a href="<c:url value='/OrgProfile/editProfile'/>" title="Edit Organization Profile" class="btn btn-link"><span class="glyphicon glyphicon-edit"></span>&nbsp; Edit Organization Profile</a><span class="indicator-active arrow-up"></span></li>
                            <li ${param['page'] == 'providers' ? 'class="active"' : ''}><a href="<c:url value='/OrgProfile/providers'/>" title="View Organization Providers" class="btn btn-link"><span class="glyphicon glyphicon-user"></span>&nbsp; View Providers</a><span class="indicator-active arrow-up"></span></li>
                            <li ${param['page'] == 'brochures' ? 'class="active"' : ''}><a href="<c:url value='/OrgProfile/brochures'/>" title="View Uploaded Brochures" class="btn btn-link"><span class="glyphicon glyphicon-book"></span>&nbsp; View Brochures</a><span class="indicator-active arrow-up"></span></li>
                            <li ${param['page'] == 'associations' ? 'class="active"' : ''}><a href="<c:url value='/associations/'/>" title="View Associated Organizations" class="btn btn-link"><span class="glyphicon glyphicon-tower"></span>&nbsp; Associated Organizations</a><span class="indicator-active arrow-up"></span></li>
                        </ul>

                        <c:choose>
                            <c:when test="${userDetails.createAuthority == true && param['page'] == 'providers'}">
                                <ul class="nav navbar-nav navbar-right navbar-actions">
                                    <li>
                                        <a href="/OrgProfile/providers/createProvider" title="Create a new provider.">Create New Provider</a>
                                    </li>
                                </ul>
                            </c:when>
                            <c:when test="${userDetails.createAuthority == true && param['page'] == 'brochures'}">
                                <ul class="nav navbar-nav navbar-right navbar-actions">
                                    <li>
                                        <a href="#systemBrochureModal" id="createNewBrochure" data-toggle="modal" title="Create a new brochure">Create New Brochure</a>
                                    </li>
                                </ul>
                            </c:when>
                        </c:choose>
                    </div>
                </nav>
            </c:when>
        </c:choose>
    </div>
</header>
<script type="text/javascript">
//This function will launch the overlay for settings
require(['./main'], function () {
    require(['jquery'], function($) {
  
$(document).on('click', '.settings', function() {
	$.ajax({
        url: '/settings',
        type: "GET",
        success: function(data) {
            $("#settingsModal").html(data);
        }
    });
});
    });
});
</script>