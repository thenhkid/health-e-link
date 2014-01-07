<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header id="header" class="header" role="banner">
    <div class="header-inner">
        <nav class="navbar primary-nav" role="navigation">
            <div class="container">
                <div class="navbar-header">
                    <a href="<c:url value='/'/>" class="navbar-brand" title="{company name}">
                        <!--
                                <img src="img/health-e-link/img-health-e-link-logo.png" class="logo" alt="Health-e-Link Logo"/>
                                Required logo specs:
                                logo width: 125px
                                logo height: 30px

                                Plain text can be used without image logo

                                sprite can be used with class="logo":
                        -->
                        <span class="identity logo" alt="{Company Name Logo}"></span>
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
                        <li ${param['page'] == 'about' ? 'class="active"' : ''}><a href="" title="About">About</a></li>
                        <li ${param['page'] == 'productSuite' ? 'class="active"' : ''}><a href="<c:url value='product-suite' />" title="Product Suite">Product Suite</a></li>
                        <li ${param['page'] == 'solutions' ? 'class="active"' : ''}><a href="" title="Solutions">Solutions</a></li>
                        <li ${param['page'] == 'contact' ? 'class="active"' : ''}><a href="" title="Contact">Contact</a></li>
                        <li ${param['page-id'] == 'profile' ? 'class="active"': ''}>
                            <c:choose>
                                <c:when test="${not empty pageContext.request.userPrincipal.name}">
                                    <a href="javascript:void(0);" title="My Account" data-toggle="dropdown">My Account <b class="caret"></b></a>
                                    <ul class="dropdown-menu" role="menu" aria-labelledby="My account dropdown">
                                        <li><a href="<c:url value='/profile'/>" title="My Account">My Account</a></li>
                                        <c:if test="${not empty userAccess}">
                                            <c:forEach items="${userAccess}" var="sections" varStatus="aStatus">
                                                <li>
                                                    <c:choose>
                                                        <c:when test="${sections.featureId == 3}"><a href="<c:url value='/Health-e-Web/inbox'/>" title="Health-e-Web">Health-e-Web</a></c:when>
                                                        <c:when test="${sections.featureId == 4}"><a href="<c:url value='/Health-e-Connect'/>" title="Health-e-Connect">Health-e-Connect</a></c:when>
                                                    </c:choose>
                                                </li>
                                            </c:forEach> 
                                        </c:if>
                                        <li><a title="log out" href="<c:url value='/logout' />">Log out</a></li>
                                    </ul> 
                                </c:when>
                                <c:otherwise>
                                    <a href="<c:url value='login' />" title="Log In">Log In</a>
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
                        <img src="<%=request.getContextPath()%>/dspResources/img/front-end/health-e-link/img-example.png" />
                        <div class="caret"></div>
                    </div>

                    <div class="central-graphic-image image-b">
                        <img src="<%=request.getContextPath()%>/dspResources/img/front-end/health-e-link/img-example.png" />
                        <div class="caret"></div>
                    </div>

                    <div class="central-graphic-image image-c">
                        <img src="<%=request.getContextPath()%>/dspResources/img/front-end/health-e-link/img-example.png" />
                        <div class="caret"></div>
                    </div>


                    <div class="central-graphic-content">
                        <h3>Our <a href="" title="">products</a> and <a href="" title="">services</a> enable health information exchange between<br/>
                            groups of health care providers, community partners, and state and local health agencies.</h3>

                        <a href="" title="" class="btn btn-primary btn-action">Our Solutions</a>
                        <a href="" title="" class="btn btn-primary btn-action btn-primary-action">Get Started</a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="container">
                    <c:choose>
                        <c:when test="${param['page-section'] == 'Health-e-Web'}"><h1 class="page-title"><span class="page-title-icon pull-left"></span>Health-e-Web</h1></c:when>
                        <c:otherwise><h1 class="page-title">${pageTitle}</h1></c:otherwise>
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
                            <li ${param['page'] == 'inbox' ? 'class="active"' : ''}><a href="<c:url value='/Health-e-Web/inbox'/>" title="Inbox" class="btn btn-link"><span class="glyphicon glyphicon-inbox"></span>&nbsp; Inbox</a><span class="indicator-active arrow-up"></span></li>
                            <li ${param['page'] == 'sent' ? 'class="active"' : ''}><a href="<c:url value='/Health-e-Web/sent'/>" title="Sent Items" class="btn btn-link"><span class="glyphicon glyphicon-send"></span>&nbsp; Sent</a><span class="indicator-active arrow-up"></span></li>
                            <li ${param['page'] == 'pending' ? 'class="active"' : ''}><a href="<c:url value='/Health-e-Web/pending'/>" title="Pending Items" class="btn btn-link"><span class="glyphicon glyphicon-time"></span>&nbsp; Pending</a><span class="indicator-active arrow-up"></span></li>
                            <li ${param['page'] == 'history' ? 'class="active"' : ''}><a href="<c:url value='/Health-e-Web/history'/>" title="History" class="btn btn-link"><span class="glyphicon glyphicon-calendar"></span>&nbsp; History</a><span class="indicator-active arrow-up"></span></li>
                        </ul>
                        <ul class="nav navbar-nav navbar-right navbar-actions">
                            <li ${param['page'] == 'create' ? 'class="active"' : ''}>
                                <a href="<c:url value='/Health-e-Web/create'/>" title="Create a new message" >Create New Message</a>
                                <span class="indicator-active arrow-up"></span>
                            </li>
                        </ul>
                    </div>
                </nav>
            </c:when>
        </c:choose>
    </div>
</header>