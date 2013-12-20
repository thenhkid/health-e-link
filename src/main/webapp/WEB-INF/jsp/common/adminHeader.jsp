<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<header id="header" class="header" role="banner">
    <!-- Primary Nav -->
    <!--role="navigation" used for accessibility -->
    <nav class="navbar primary-nav" role="navigation">
        <div class="contain">
            <div class="navbar-header">
                <a href="<c:url value='/administrator' />" class="navbar-brand" title="{company name}">
                    <!--
                            <img src="<%=request.getContextPath()%>/dspResources/img/health-e-link/img-health-e-link-logo.png" class="logo" alt="Health-e-Link Logo"/>
                            Required logo specs:
                            logo width: 125px
                            logo height: 30px

                            Plain text can be used without image logo

                            sprite can be used with class="logo":
                    -->
                    <span class="logo" alt="{Company Name Logo}"></span>
                </a>
                 <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
            </div>
            <div class="collapse navbar-collapse navbar-ex1-collapse">
                <ul class="nav navbar-nav" role="menu">
                    <li role="menuitem" class="${param['sect'] == 'lib' ? 'active' : 'none'}"><a href="<c:url value='/administrator/library/list' />" title="Message Type Library Manager">Message Type Library</a><c:if test="${param['sect'] == 'lib'}"><span class="indicator-active arrow-up"></span></c:if></li>
                    <li role="menuitem" class="${param['sect'] == 'org' ? 'active' : 'none'}"><a href="<c:url value='/administrator/organizations/list' />" title="Organization Manager">Organizations</a><c:if test="${param['sect'] == 'org'}"><span class="indicator-active arrow-up"></span></c:if></li>
                    <li role="menuitem" class="${param['sect'] == 'config' ? 'active' : 'none'}"><a href="<c:url value='/administrator/configurations/list' />" title="Configuration Manager">Configurations</a><c:if test="${param['sect'] == 'config'}"><span class="indicator-active arrow-up"></span></c:if></li>
                    <li role="menuitem" class="${param['sect'] == 'connect' ? 'active' : 'none'}"><a href="<c:url value='/administrator/configurations/connections' />" title="Configuration Connection Manager">Connections</a><c:if test="${param['sect'] == 'connect'}"><span class="indicator-active arrow-up"></span></c:if></li>
                    <li role="menuitem" class="${param['sect'] == 'sched' ? 'active' : 'none'}"><a href="" title="Scheduler">Scheduler</a><c:if test="${param['sect'] == 'sched'}"><span class="indicator-active arrow-up"></span></c:if></li>
                    <li role="menuitem" class="${param['sect'] == 'sysadmin' ? 'active' : 'none'}"><a href="<c:url value='/administrator/sysadmin/' />" title="System Administration">System Admin</a><c:if test="${param['sect'] == 'sysadmin'}"><span class="indicator-active arrow-up"></span></c:if></li>
                </ul>
                <ul class="nav navbar-nav navbar-right" id="secondary-nav">
                  <li><a class="logout" href="<c:url value='/logout' />">Log out</a></li>
                </ul>
            </div>
        </div>
    </nav>
    <!-- // End Primary Nav -->
</header>
