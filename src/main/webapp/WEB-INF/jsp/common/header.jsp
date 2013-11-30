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
                        <li>
                            <c:choose>
                                <c:when test="${not empty pageContext.request.userPrincipal.name}">
                                   <a href="#" title="My Account" data-toggle="dropdown">My Account <b class="caret"></b></a>
                                    <ul class="dropdown-menu" role="menu" aria-labelledby="My account dropdown">
                                        <li><a href="" title="">My account nav 1</a></li>
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
                <div class="container"><h1 class="page-title">${pageTitle}</h1></div>   
            </c:otherwise>
        </c:choose>
    </div>
</header>